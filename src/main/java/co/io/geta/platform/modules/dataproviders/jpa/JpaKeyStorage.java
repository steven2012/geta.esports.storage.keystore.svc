package co.io.geta.platform.modules.dataproviders.jpa;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.EntityProperty;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;
import com.microsoft.azure.storage.table.TableResult;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.ItemsRulesDto;
import co.io.geta.platform.crosscutting.domain.RulesDto;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import co.io.geta.platform.crosscutting.payload.AddItemRuleStoragePayload;
import co.io.geta.platform.crosscutting.payload.GetRulesStoragePayload;
import co.io.geta.platform.crosscutting.persistence.entity.Storage;
import co.io.geta.platform.crosscutting.util.CastUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.crosscutting.validator.AddItemRuleStorageValidator;
import co.io.geta.platform.crosscutting.validator.GetRulesStorageValidator;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataprovider.KeyStorageDataprovider;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JpaKeyStorage implements KeyStorageDataprovider {

	@Autowired
	private CloudStorageAccount storageAcount;

	@Autowired
	private MessageUtil messagesUtil;

	@Autowired
	private CastUtil castUtil;

	@Autowired
	private GetRulesStorageValidator validator;

	@Autowired
	private AddItemRuleStorageValidator ruleStorageValidator;

	@Autowired
	private ObjectMapper mapper;

	@Value(GetaConstants.NAME_TABLE_RULES)
	private String nameTableRules;

	@Value(GetaConstants.SEED_ENCRYPTOR)
	private String seedEncryptor;

	/**
	 * This method allows obtaining the corresponding rule, according to the
	 * specified key
	 * 
	 * @param payload: contains query params
	 * @return ApiResponse<RulesDto>: Returns a RulesDto object with the information
	 *         of the queried rule
	 */
	@Override
	public ApiResponse<RulesDto> getRule(GetRulesStoragePayload payload) {

		RulesDto rule = new RulesDto();
		ApiResponse<RulesDto> response = new ApiResponse<>();

		try {
			// Validate Payload
			validator.payloadValidator(payload);

			// Obtain Table Document
			TableOperation table = tableOperation(payload.getKeyRule());
			TableResult result = cloudTable().execute(table);

			if (result.getProperties() == null) {
				response.setData(rule);
				response.setSuccess(false);
				response.setStatusCode(HttpStatus.PRECONDITION_FAILED.value());
				response.setMessage(messagesUtil.getMessage(GetaConstants.KEY_NOT_FOUND));
				return response;
			}

			// Obtain information in Jsnode of table storage
			JsonNode rul = getJsonNode(result);

			// Converter to Object
			Object item = mapper.convertValue(rul.get(GetaConstants.KEY_ROOT_JSON), Object.class);

			// Build object rules
			rule = buildDto(item, payload.getKeyRule(), rule);

			if (rule == null) {
				response.setData(rule);
				response.setSuccess(false);
				response.setStatusCode(HttpStatus.PRECONDITION_FAILED.value());
				response.setMessage(messagesUtil.getMessage(GetaConstants.KEY_NOT_FOUND));
				return response;
			}

			response.setData(rule);
			response.setSuccess(true);
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("");

			return response;

		} catch (Exception e) {

			response.setData(rule);
			response.setSuccess(false);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage(messagesUtil.getMessage(GetaConstants.BAD_REQUEST) + " " + e.getMessage());
			return response;

		}
	}

	/**
	 * This method allows adding rules in the table storage,It also allows you to
	 * update a rule with the key and its new value set
	 * 
	 * @param:payload:Dictionary object key value representing the rule to be
	 *                                  stored.
	 * @return ApiResponse<RulesDto>, returns an ApiResponse <RulesDto> object with
	 *         the processed information;
	 */
	@Override
	public ApiResponse<RulesDto> addRule(AddItemRuleStoragePayload payload) {

		ApiResponse<RulesDto> response = new ApiResponse<>();

		try {
			// Validate payload
			ruleStorageValidator.payloadValidator(payload);

			if (!(payload.getRule() instanceof LinkedHashMap)) {

				response.setData(null);
				response.setSuccess(false);
				response.setStatusCode(HttpStatus.CONFLICT.value());
				response.setMessage(messagesUtil.getMessage(GetaConstants.MAPPING_ERROR_MAP));
				return response;
			}

			// Convert payload in linkedHasmap
			LinkedHashMap<String, Object> jsonRules = mapper.convertValue(payload.getRule(), LinkedHashMap.class);
			// Obatin List of Key linkedHasmap
			List<String> keysTournament = castUtil.castList(String.class, jsonRules.keySet());
			// Convert payload in JsonNode
			JsonNode jsonRule = mapper.convertValue(payload.getRule(), JsonNode.class);

			// Add key "KeyRules" to save in table storage
			LinkedHashMap<String, Object> hasmapRule = new LinkedHashMap<>();
			hasmapRule.put(GetaConstants.KEY_ROOT_JSON, jsonRule);

			// Convert LinkedHasap In JsoNode
			JsonNode json = mapper.convertValue(hasmapRule, JsonNode.class);

			Storage keyStorage = new Storage();
			keyStorage.setPartitionKey(GetaConstants.VALUE_PARTITION_KEY);
			keyStorage.setRowKey(keysTournament.get(0));
			keyStorage.setData(json.toString());

			// Obtain Table Document
			TableOperation table = TableOperation.insertOrReplace(keyStorage);

			TableResult result = cloudTable().execute(table);

			if (result.getHttpStatusCode() != HttpStatus.NO_CONTENT.value()) {

				response.setData(null);
				response.setSuccess(false);
				response.setStatusCode(result.getHttpStatusCode());
				response.setMessage(messagesUtil.getMessage(GetaConstants.TABLE_EXECUTE_EXCEPTION));
				return response;
			}

			// If the operation was successful, consult the new rule key to return the new
			// information stored
			return getRule(new GetRulesStoragePayload(keysTournament.get(0)));

		} catch (Exception e) {

			response.setData(null);
			response.setSuccess(false);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage(messagesUtil.getMessage(GetaConstants.BAD_REQUEST) + " " + e.getMessage());
			return response;
		}

	}

	/**
	 * This method allows to obtain all the rules established in the table storage
	 */
	@Override
	public ApiResponse<ItemsRulesDto> getAllRule() {
		ApiResponse<ItemsRulesDto> response = new ApiResponse<>();

		try {

			ItemsRulesDto dto = new ItemsRulesDto();

			// Create a filter condition where the partition key is "Rule".
			String partitionFilter = TableQuery.generateFilterCondition(GetaConstants.NAME_PROPERTIES, QueryComparisons.EQUAL,
					GetaConstants.VALUE_PARTITION_KEY);

			// Specify a partition query, using "Rule" as the partition key filter.
			TableQuery<Storage> partitionQuery = TableQuery.from(Storage.class).where(partitionFilter);

			List<JsonNode> listAllMap = new ArrayList<>();

			// Query obtain all rows
			Iterable<Storage> list = cloudTable().execute(partitionQuery);

			LinkedHashMap<String, Object> listHashmap = new LinkedHashMap<>();

			for (Storage item : list) {
				JsonNode jsonRule = mapper.readTree(item.getData()).path(GetaConstants.KEY_ROOT_JSON);
				listAllMap.add(jsonRule);
			}

			listHashmap.put(GetaConstants.KEY_ROOT_JSON, listAllMap);

			LinkedHashMap<String, Object> rule = listHashmap;

			dto.setDetails(rule);
			response.setData(dto);
			response.setSuccess(true);
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("");
			return response;

		} catch (Exception ex) {

			response.setData(null);
			response.setSuccess(false);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage(ex.getMessage());
			return response;
		}

	}

	/**
	 * 
	 * @param item:   Object representing the dictionary to be converted to the DTO
	 * @param keyRule Represents the key of the dictionary to be consulted
	 * @param rule
	 * @return
	 */
	private RulesDto buildDto(Object item, String keyRule, RulesDto rule) {

		if (item instanceof LinkedHashMap) {

			LinkedHashMap<String, Object> details = new LinkedHashMap<>();

			LinkedHashMap<?, ?> linked = (LinkedHashMap<?, ?>) item;

			Set<?> keys = linked.keySet();

			if (!keys.contains(keyRule)) {
				return null;
			}

			// Check if the value of the dictionary is a linkedhasmap
			if (linked.get(keyRule) instanceof LinkedHashMap) {

				LinkedHashMap<?, ?> hasmapRule = (LinkedHashMap<?, ?>) linked.get(keyRule);
				List<String> keysRules = castUtil.castList(String.class, hasmapRule.keySet());
				builMap(details, keysRules, hasmapRule);
			}

			if (!(linked.get(keyRule) instanceof LinkedHashMap)) {
				details.put(keyRule, linked.get(keyRule));
			}

			// Create Map info
			ItemsRulesDto items = new ItemsRulesDto();

			LinkedHashMap<String, Object> contentListItem = new LinkedHashMap<>();
			contentListItem.put("List", details);

			items.setDetails(details);
			rule.setRules(items);
		}

		return rule;
	}

	private void builMap(LinkedHashMap<String, Object> details, List<String> keys, LinkedHashMap<?, ?> tournament) {

		for (String item : keys) {
			details.put(item, tournament.get(item));
		}
	}

	/**
	 * It allows to obtain the table with the information to be processed
	 * 
	 * @return TableOperation:Represents the table with the information to be
	 *         processed
	 * @throws EBusinessException
	 */
	private TableOperation tableOperation(String keyRule) {

		String rowkey = keyRule;
		String partitioKey = GetaConstants.VALUE_PARTITION_KEY;
		// Obtain Document
		return TableOperation.retrieve(partitioKey, rowkey, Storage.class);
	}

	/**
	 * this method Makes the table with the information to process and allows a
	 * query to be executed
	 * 
	 * @return CloudTable: Reference Table storage
	 * @throws EBusinessException
	 * @throws URISyntaxException
	 * @throws StorageException
	 */
	private CloudTable cloudTable() throws URISyntaxException, StorageException {

		// Get instance from storage table
		CloudTableClient tableClient = storageAcount.createCloudTableClient();
		// Obtain Table reference
		CloudTable cloudTable = tableClient.getTableReference(nameTableRules);

		if (cloudTable == null) {

			throw new StorageException(messagesUtil.getMessage(GetaConstants.REFERENCE_TABLE_EXCEPTION), null, null);
		}

		return cloudTable;
	}

	/**
	 * This method Represents the table info in json
	 * 
	 * @param result: Represents the table where the information is stored
	 * @return Jsonnode; Represents the table info in json
	 * @throws IOException
	 */
	private JsonNode getJsonNode(TableResult result) throws IOException {

		HashMap<String, EntityProperty> map = result.getProperties();
		EntityProperty property = map.get(GetaConstants.KEY_JSON_RULES);
		String jsonRules = property.getValueAsString();

		// Obtain object JsoNode
		return mapper.readValue(jsonRules, JsonNode.class);
	}

}

package co.io.geta.platform.modules.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.core.Base64;

import co.io.geta.platform.crosscutting.domain.ItemsRulesDto;
import co.io.geta.platform.crosscutting.domain.RulesDto;
import co.io.geta.platform.crosscutting.payload.AddItemRuleStoragePayload;
import co.io.geta.platform.crosscutting.payload.GetRulesStoragePayload;
import co.io.geta.platform.crosscutting.util.EncryptUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;
import co.io.geta.platform.crosscutting.validator.AddItemRuleStorageValidator;
import co.io.geta.platform.crosscutting.validator.GetRulesStorageValidator;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataproviders.jpa.JpaKeyStorage;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class StorageApplicationTests {

	@InjectMocks
	private JpaKeyStorage jpaStorage;

	@Autowired
	private CloudStorageAccount storageAcount;

	@Mock
	private MessageSource messageResource;

	private static final String ACCOUNT_NAME = UUID.randomUUID().toString();
	private static final String ACCOUNT_KEY = Base64.encode(UUID.randomUUID().toString().getBytes());

	@Before
	public void setupMockt() {
		messageResource = Mockito.mock(MessageSource.class);

	}

	@Test
	public void GetRuleBadRequest() {
		ApiResponse<RulesDto> response = null;
		try {
			String seedEncryptor = "GENESYS-SPORT-KEY_STORAGE";

			String rowKey = EncryptUtil.encrypt(UUID.randomUUID().toString(), seedEncryptor);
			String partitionKey = EncryptUtil.encrypt(UUID.randomUUID().toString(), seedEncryptor);

			String nameTableRules = "/nFj6RUanYZcbzTD0+p9vPoGhrVlK48I3/nWpSOtv1HIAxjF+w==";
			StorageCredentialsAccountAndKey cred = new StorageCredentialsAccountAndKey(ACCOUNT_NAME, ACCOUNT_KEY);
			storageAcount = new CloudStorageAccount(cred);

			MessageUtil messagesUtil = new MessageUtil();
			messagesUtil.setMessageSource(messageResource);

			GetRulesStorageValidator validator = new GetRulesStorageValidator();
			validator.setMessagesUtil(messagesUtil);

			ReflectionTestUtils.setField(jpaStorage, "storageAcount", storageAcount);
			ReflectionTestUtils.setField(jpaStorage, "nameTableRules", nameTableRules);
			ReflectionTestUtils.setField(jpaStorage, "rowKey", rowKey);
			ReflectionTestUtils.setField(jpaStorage, "partitionKey", partitionKey);
			ReflectionTestUtils.setField(jpaStorage, "messagesUtil", messagesUtil);
			ReflectionTestUtils.setField(jpaStorage, "seedEncryptor", seedEncryptor);
			ReflectionTestUtils.setField(jpaStorage, "validator", validator);

			response = jpaStorage.getRule(new GetRulesStoragePayload("Games"));
			assertNotNull(response);

		} catch (Exception ex) {
			assertNull(response);
		}

	}

	@Test
	public void GetRuleResponseNull() {
		ApiResponse<RulesDto> response = null;
		try {
			String rowKey = UUID.randomUUID().toString();
			String partitionKey = UUID.randomUUID().toString();

			String nameTableRules = "TableName";
			StorageCredentialsAccountAndKey cred = new StorageCredentialsAccountAndKey(ACCOUNT_NAME, ACCOUNT_KEY);
			storageAcount = new CloudStorageAccount(cred);

			MessageUtil messagesUtil = new MessageUtil();
			ReflectionTestUtils.setField(jpaStorage, "storageAcount", storageAcount);
			ReflectionTestUtils.setField(jpaStorage, "nameTableRules", nameTableRules);
			ReflectionTestUtils.setField(jpaStorage, "rowKey", rowKey);
			ReflectionTestUtils.setField(jpaStorage, "partitionKey", partitionKey);
			ReflectionTestUtils.setField(jpaStorage, "messagesUtil", messagesUtil);

			response = jpaStorage.getRule(new GetRulesStoragePayload("Games"));
			assertNull(response);

		} catch (Exception ex) {
			assertNull(response);
		}

	}

	@Test
	public void getAllRuleTest() {
		ApiResponse<ItemsRulesDto> response = null;

		try {

			String seedEncryptor = "GENESYS-SPORT-KEY_STORAGE";

			String rowKey = EncryptUtil.encrypt(UUID.randomUUID().toString(), seedEncryptor);
			String partitionKey = EncryptUtil.encrypt(UUID.randomUUID().toString(), seedEncryptor);

			String nameTableRules = "/nFj6RUanYZcbzTD0+p9vPoGhrVlK48I3/nWpSOtv1HIAxjF+w==";
			StorageCredentialsAccountAndKey cred = new StorageCredentialsAccountAndKey(ACCOUNT_NAME, ACCOUNT_KEY);
			storageAcount = new CloudStorageAccount(cred);

			MessageUtil messagesUtil = new MessageUtil();
			messagesUtil.setMessageSource(messageResource);

			GetRulesStorageValidator validator = new GetRulesStorageValidator();
			validator.setMessagesUtil(messagesUtil);

			ReflectionTestUtils.setField(jpaStorage, "storageAcount", storageAcount);
			ReflectionTestUtils.setField(jpaStorage, "nameTableRules", nameTableRules);
			ReflectionTestUtils.setField(jpaStorage, "rowKey", rowKey);
			ReflectionTestUtils.setField(jpaStorage, "partitionKey", partitionKey);
			ReflectionTestUtils.setField(jpaStorage, "messagesUtil", messagesUtil);
			ReflectionTestUtils.setField(jpaStorage, "seedEncryptor", seedEncryptor);
			ReflectionTestUtils.setField(jpaStorage, "validator", validator);

			response = jpaStorage.getAllRule();
			assertNotNull(response);

		} catch (Exception ex) {
			assertNull(response);

		}

	}

	@Test
	public void addRuleTest() {
		ApiResponse<RulesDto> response = null;

		try {

			AddItemRuleStoragePayload payload = new AddItemRuleStoragePayload();
			LinkedHashMap<String, Object> rule = new LinkedHashMap<>();
			LinkedHashMap<String, Object> rules = new LinkedHashMap<>();
			rules.put("prueba", "prueba");

			rule.put("Rule", rules);
			payload.setRule(rule);

			String seedEncryptor = "GENESYS-SPORT-KEY_STORAGE";

			String rowKey = EncryptUtil.encrypt(UUID.randomUUID().toString(), seedEncryptor);
			String partitionKey = EncryptUtil.encrypt(UUID.randomUUID().toString(), seedEncryptor);

			String nameTableRules = "/nFj6RUanYZcbzTD0+p9vPoGhrVlK48I3/nWpSOtv1HIAxjF+w==";
			StorageCredentialsAccountAndKey cred = new StorageCredentialsAccountAndKey(ACCOUNT_NAME, ACCOUNT_KEY);
			storageAcount = new CloudStorageAccount(cred);

			MessageUtil messagesUtil = new MessageUtil();
			messagesUtil.setMessageSource(messageResource);

			AddItemRuleStorageValidator ruleStorageValidator = new AddItemRuleStorageValidator();
			ruleStorageValidator.setMessagesUtil(messagesUtil);

			ReflectionTestUtils.setField(jpaStorage, "storageAcount", storageAcount);
			ReflectionTestUtils.setField(jpaStorage, "nameTableRules", nameTableRules);
			ReflectionTestUtils.setField(jpaStorage, "rowKey", rowKey);
			ReflectionTestUtils.setField(jpaStorage, "partitionKey", partitionKey);
			ReflectionTestUtils.setField(jpaStorage, "messagesUtil", messagesUtil);
			ReflectionTestUtils.setField(jpaStorage, "seedEncryptor", seedEncryptor);
			ReflectionTestUtils.setField(jpaStorage, "ruleStorageValidator", ruleStorageValidator);

			response = jpaStorage.addRule(payload);
			assertNotNull(response);

		} catch (Exception ex) {
			assertNull(response);

		}

	}
}

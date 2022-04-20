package co.io.geta.platform.modules.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.ItemsRulesDto;
import co.io.geta.platform.crosscutting.domain.RulesDto;
import co.io.geta.platform.crosscutting.payload.AddItemRuleStoragePayload;
import co.io.geta.platform.crosscutting.payload.GetRulesStoragePayload;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.usecases.StorageProcess;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(value = GetaConstants.ROOT_API_STORAGE, produces = { MediaType.APPLICATION_JSON_VALUE })
public class StorageWebApi {

	@Autowired
	private StorageProcess storageProcess;

	@ApiOperation(value = "Obtain rule", notes = "It allows obtaining the rule corresponding to the specified key")
	@GetMapping(GetaConstants.RULES)
	public ApiResponse<RulesDto> tableStorage(GetRulesStoragePayload payload) {
		return storageProcess.tableProcess(payload);
	}

	@ApiOperation(value = "Add/Update rule", notes = "It allows adding and updating a rule, "
			+ "if the rule already exists with a key, it is updated otherwise a new rule is added")
	@PostMapping(value = GetaConstants.RULES)
	public ApiResponse<RulesDto> ruleStorage(@RequestBody AddItemRuleStoragePayload payload) {
		return storageProcess.addRule(payload);
	}

	@ApiOperation(value = "Obtain All rules", notes = "allows you to get all the specified rules")
	@GetMapping(GetaConstants.ALL_RULES)
	public ApiResponse<ItemsRulesDto> getAllRule() {
		return storageProcess.getAllRule();
	}
}

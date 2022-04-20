package co.io.geta.platform.modules.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.io.geta.platform.crosscutting.domain.ItemsRulesDto;
import co.io.geta.platform.crosscutting.domain.RulesDto;
import co.io.geta.platform.crosscutting.payload.AddItemRuleStoragePayload;
import co.io.geta.platform.crosscutting.payload.GetRulesStoragePayload;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataprovider.KeyStorageDataprovider;

@Component
public class StorageProcess {

	@Autowired
	private KeyStorageDataprovider keyStorageDataprovider;
	@Autowired
	private ObjectMapper mapper;
	public ApiResponse<RulesDto> tableProcess(GetRulesStoragePayload payload) {

		return keyStorageDataprovider.getRule(payload);
	}

	public ApiResponse<RulesDto> addRule(AddItemRuleStoragePayload payload) {
		return keyStorageDataprovider.addRule(payload);
	}

	public ApiResponse<ItemsRulesDto> getAllRule() {
//		 String str = "{\"nombre\":\"J'ohn\"}";
//		 PruebaDto tes=mapper.convertValue(str, PruebaDto.class);
		return keyStorageDataprovider.getAllRule();
	}

}

package co.io.geta.platform.modules.dataprovider;

import co.io.geta.platform.crosscutting.domain.ItemsRulesDto;
import co.io.geta.platform.crosscutting.domain.RulesDto;
import co.io.geta.platform.crosscutting.payload.AddItemRuleStoragePayload;
import co.io.geta.platform.crosscutting.payload.GetRulesStoragePayload;
import co.io.geta.platform.modules.common.ApiResponse;

public interface KeyStorageDataprovider {

	public ApiResponse<RulesDto> getRule(GetRulesStoragePayload payload);

	public ApiResponse<RulesDto> addRule(AddItemRuleStoragePayload payload);

	public ApiResponse<ItemsRulesDto> getAllRule();

}

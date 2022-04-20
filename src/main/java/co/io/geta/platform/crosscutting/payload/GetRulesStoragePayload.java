package co.io.geta.platform.crosscutting.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Data
public class GetRulesStoragePayload {

	// Key rule Query
	private String keyRule;

}

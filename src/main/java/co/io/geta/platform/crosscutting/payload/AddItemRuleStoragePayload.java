package co.io.geta.platform.crosscutting.payload;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Data
public class AddItemRuleStoragePayload {

	@JsonProperty("Rule")
	private LinkedHashMap<String, Object> rule;

}

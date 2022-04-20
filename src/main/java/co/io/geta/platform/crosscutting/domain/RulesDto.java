package co.io.geta.platform.crosscutting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RulesDto {

	@JsonProperty("KeyRules")
	private ItemsRulesDto rules;

}

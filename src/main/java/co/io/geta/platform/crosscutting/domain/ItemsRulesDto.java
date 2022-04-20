package co.io.geta.platform.crosscutting.domain;

import java.util.LinkedHashMap;

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
public class ItemsRulesDto {

	@JsonProperty("Details")
	private LinkedHashMap<String, Object> details;
}

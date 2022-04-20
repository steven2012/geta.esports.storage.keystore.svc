package co.io.geta.platform.crosscutting.persistence.entity;

import com.microsoft.azure.storage.table.TableServiceEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Storage extends TableServiceEntity {

	private String partition;
	private String row;
	private String data;
}

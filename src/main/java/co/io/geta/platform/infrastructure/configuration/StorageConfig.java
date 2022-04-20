package co.io.geta.platform.infrastructure.configuration;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.azure.storage.CloudStorageAccount;

import co.io.geta.platform.crosscutting.constants.GetaConstants;

@Configuration
public class StorageConfig {

	@Value(GetaConstants.CONECTING_STRING_AZURE_STORAGE)
	private String configAzureStorage;

	public String configConectionStorage() {
		return configAzureStorage;
	}

	@Bean
	public CloudStorageAccount accountStorage() throws InvalidKeyException, URISyntaxException {
		return CloudStorageAccount.parse(configAzureStorage);
	}
}

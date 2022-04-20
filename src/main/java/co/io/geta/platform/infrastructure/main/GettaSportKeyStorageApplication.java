package co.io.geta.platform.infrastructure.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import co.io.geta.platform.crosscutting.util.CastUtil;
import co.io.geta.platform.crosscutting.util.MessageUtil;

@SpringBootApplication(scanBasePackages = { "co.io.geta.platform.infrastructure", "co.io.geta.platform.modules",
		"co.io.geta.platform.crosscutting" })
public class GettaSportKeyStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GettaSportKeyStorageApplication.class, args);
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.addBasenames("properties/manager-messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public MessageUtil messagesUtil() {
		return new MessageUtil();
	}

	@Bean
	public CastUtil castUtil() {
		return new CastUtil();
	}

}

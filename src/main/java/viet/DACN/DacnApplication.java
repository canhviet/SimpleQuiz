package viet.DACN;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration.class
})
public class DacnApplication {

	public static void main(String[] args) {
		SpringApplication.run(DacnApplication.class, args);
	}

}

package viet.DACN;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "viet.DACN.model")
@EnableJpaRepositories(basePackages = "viet.DACN.repo")
public class DacnApplication {

	public static void main(String[] args) {
		SpringApplication.run(DacnApplication.class, args);
	}

}

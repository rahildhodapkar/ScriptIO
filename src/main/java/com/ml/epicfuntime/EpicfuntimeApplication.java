package com.ml.epicfuntime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.ml.epicfuntime.repository")
public class EpicfuntimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpicfuntimeApplication.class, args);
	}

}

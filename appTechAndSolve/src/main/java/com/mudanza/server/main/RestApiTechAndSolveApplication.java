package com.mudanza.server.main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.mudanza.configuration.StorageProperties;
import com.mudanza.service.IStorageService;

@SpringBootApplication(scanBasePackages = {"com.mudanza"})
@EnableConfigurationProperties(StorageProperties.class)
@EnableAutoConfiguration
public class RestApiTechAndSolveApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiTechAndSolveApplication.class, args);
	}
	
	@Bean
    CommandLineRunner init(IStorageService storageService) {
        return args -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
	
}

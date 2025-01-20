package com.example.english_learning_center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.english_learning_center.repositories")
public class EnglishLearningCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishLearningCenterApplication.class, args);
	}

}

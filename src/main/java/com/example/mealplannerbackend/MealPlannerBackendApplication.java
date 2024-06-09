package com.example.mealplannerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MealPlannerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealPlannerBackendApplication.class, args);
	}

}

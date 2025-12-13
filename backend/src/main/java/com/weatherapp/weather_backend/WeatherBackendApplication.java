package com.weatherapp.weather_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WeatherBackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(WeatherBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WeatherBackendApplication.class, args);
		logger.info("Weather Backend Application is running");
	}

}

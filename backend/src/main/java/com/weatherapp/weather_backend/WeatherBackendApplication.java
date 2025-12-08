package com.weatherapp.weather_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class WeatherBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherBackendApplication.class, args);
		System.out.println("Weather Backend Application is running");
	}

}

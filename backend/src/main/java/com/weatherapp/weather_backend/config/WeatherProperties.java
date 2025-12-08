package com.weatherapp.weather_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "weather.api")
@Setter
@Getter
public class WeatherProperties {
    @NotNull
    private String key;
    @NotNull
    private String url;
}

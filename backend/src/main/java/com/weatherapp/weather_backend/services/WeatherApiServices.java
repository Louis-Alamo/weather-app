package com.weatherapp.weather_backend.services;

import com.weatherapp.weather_backend.interfaces.WeatherProvide;

import org.springframework.cache.annotation.Cacheable;

import com.weatherapp.weather_backend.config.WeatherProperties;
import com.weatherapp.weather_backend.dto.WeatherDTO;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
public class WeatherApiServices implements WeatherProvide {

    private final WeatherProperties weatherProperties;
    private final RestTemplate restTemplate;

    public WeatherApiServices(WeatherProperties weatherProperties, RestTemplate restTemplate) {
        this.weatherProperties = weatherProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(value = "weather_cache", key = "#city")
    @SuppressWarnings("null")
    public Optional<WeatherDTO> getWeather(String city) {

        String url = String.format("%s?key=%s&q=%s&days=7",

                Objects.requireNonNull(weatherProperties.getUrl(), "Url is required"),
                Objects.requireNonNull(weatherProperties.getKey(), "Key is required"),
                Objects.requireNonNull(city, "City is required"));

        System.out.println("Se realizo una llamada a la api: " + url);

        return Optional.ofNullable(restTemplate.getForObject(url, WeatherDTO.class));
    }
}

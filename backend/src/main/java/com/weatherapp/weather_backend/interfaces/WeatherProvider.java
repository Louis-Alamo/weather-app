package com.weatherapp.weather_backend.interfaces;

import java.util.Optional;

import com.weatherapp.weather_backend.dto.WeatherResponseDTO;

public interface WeatherProvider {
    Optional<WeatherResponseDTO> getWeather(String city, int day, String lang, String metric);
}

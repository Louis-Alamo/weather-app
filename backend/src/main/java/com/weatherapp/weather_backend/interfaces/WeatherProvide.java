package com.weatherapp.weather_backend.interfaces;

import java.util.Optional;

import com.weatherapp.weather_backend.dto.WeatherResponseDTO;

public interface WeatherProvide {
    public Optional<WeatherResponseDTO> getWeather(String city);
}

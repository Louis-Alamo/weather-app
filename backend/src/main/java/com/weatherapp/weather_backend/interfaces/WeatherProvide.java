package com.weatherapp.weather_backend.interfaces;

import java.util.Optional;

import com.weatherapp.weather_backend.dto.WeatherDTO;

public interface WeatherProvide {
    public Optional<WeatherDTO> getWeather(String city);
}

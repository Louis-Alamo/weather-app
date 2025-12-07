package com.weatherapp.weather_backend.interfaces;

import com.weatherapp.weather_backend.dto.WeatherResponseDTO;

public interface WeatherProvide {
    public WeatherResponseDTO getWeather(String city);
}

package com.weatherapp.weather_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherapp.weather_backend.dto.WeatherDTO;
import com.weatherapp.weather_backend.dto.responsesdto.ApiResponse;
import com.weatherapp.weather_backend.exceptions.CityNotFound;
import com.weatherapp.weather_backend.services.WeatherApiServices;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v1/weather")
@Validated
public class WeatherApiController {

    private final WeatherApiServices weatherApiServices;

    public WeatherApiController(WeatherApiServices weatherApiServices) {
        this.weatherApiServices = weatherApiServices;
    }

    @GetMapping("/{city}")
    public ResponseEntity<ApiResponse<WeatherDTO>> getWeather(
            @PathVariable @NotBlank(message = "City cannot be blank") @NotNull(message = "City cannot be null") String city) {

        return weatherApiServices.getWeather(city)
                .map(weatherDTO -> ResponseEntity.ok(ApiResponse.success(weatherDTO, "Weather data")))
                .orElseThrow(() -> new CityNotFound("City not found"));

    }
}
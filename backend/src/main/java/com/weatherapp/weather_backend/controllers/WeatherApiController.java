package com.weatherapp.weather_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherapp.weather_backend.dto.ApiResponse;
import com.weatherapp.weather_backend.dto.WeatherResponseDTO;
import com.weatherapp.weather_backend.exceptions.CityNotFound;
import com.weatherapp.weather_backend.services.WeatherApiServices;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/v1/weather")
@Validated
public class WeatherApiController {

    private final WeatherApiServices weatherApiServices;

    public WeatherApiController(WeatherApiServices weatherApiServices) {
        this.weatherApiServices = weatherApiServices;
    }

    @GetMapping("/{city}")
    public ResponseEntity<ApiResponse<WeatherResponseDTO>> getWeather(
            @PathVariable @NotBlank(message = "City cannot be blank") @NotNull(message = "City cannot be null") String city,
            @RequestParam(defaultValue = "1") @Min(1) @Max(7) int day,
            @RequestParam(defaultValue = "en") @Pattern(regexp = "^(en|es|fr|de|pt)$", message = "Language must be one of: en, es, fr, de, pt") String lang,
            @RequestParam(defaultValue = "metric") @Pattern(regexp = "^(metric|imperial)$", message = "Metric must be 'metric' or 'imperial'") String metric) {

        return weatherApiServices.getWeather(city, day, lang, metric)
                .map(weatherResponseDTO -> ResponseEntity.ok(ApiResponse.success(weatherResponseDTO, "Weather data")))
                .orElseThrow(() -> new CityNotFound("City not found"));

    }
}
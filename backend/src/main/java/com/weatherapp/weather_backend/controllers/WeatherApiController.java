package com.weatherapp.weather_backend.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherapp.weather_backend.dto.WeatherResponseDTO;
import com.weatherapp.weather_backend.services.WeatherApiServices;

@RestController
@RequestMapping("api/v1/weather")
public class WeatherApiController {

    private final WeatherApiServices weatherApiServices;

    public WeatherApiController(WeatherApiServices weatherApiServices) {
        this.weatherApiServices = weatherApiServices;
    }

    @GetMapping("/{city}")
    public ResponseEntity<WeatherResponseDTO> getWeather(@PathVariable String city) {

        Optional<WeatherResponseDTO> weatherResponseDTO = weatherApiServices.getWeather(city);

        if (weatherResponseDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(weatherResponseDTO.get());
    }

}
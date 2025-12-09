package com.weatherapp.weather_backend.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherDTO(
                Location location,
                Current current) implements Serializable {
}

record Location(
                String name,
                String region,
                String country,
                @JsonProperty("localtime") String localTime) implements Serializable {
}

record Current(
                @JsonProperty("temp_c") double temperature,
                @JsonProperty("feelslike_c") double feelsLike,
                @JsonProperty("humidity") int humidity,
                @JsonProperty("wind_kph") double windKph,
                @JsonProperty("uv") double uvIndex,
                @JsonProperty("vis_km") double visibilityKm,
                @JsonProperty("is_day") int isDay,
                Condition condition) implements Serializable {
}

record Condition(
                String text,
                String icon) implements Serializable {
}
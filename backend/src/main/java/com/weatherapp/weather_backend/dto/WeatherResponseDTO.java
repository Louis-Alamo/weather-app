package com.weatherapp.weather_backend.dto;

import java.io.Serializable;
import java.util.List;

public record WeatherResponseDTO(
        LocationResponse location,
        CurrentResponse current,
        ForecastResponse forecast) implements Serializable {

    public record LocationResponse(
            String name,
            String region,
            String country,
            String localTime) implements Serializable {
    }

    public record CurrentResponse(
            double temperature,
            double feelsLike,
            int humidity,
            double windSpeed,
            double uvIndex,
            double visibility,
            int isDay,
            ConditionResponse condition) implements Serializable {
    }

    public record ConditionResponse(
            String text,
            String icon) implements Serializable {
    }

    public record ForecastResponse(
            List<ForecastDayResponse> forecastday) implements Serializable {
    }

    public record ForecastDayResponse(
            String date,
            DayResponse day) implements Serializable {
    }

    public record DayResponse(
            double maxTemp,
            double minTemp,
            double avgTemp,
            double maxWind,
            double totalPrecipitation,
            double avgVisibility,
            int avgHumidity,
            double uvIndex,
            ConditionResponse condition) implements Serializable {
    }
}
package com.weatherapp.weather_backend.dto;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherDTO(
                Location location,
                Current current,
                Forecast forecast) implements Serializable {

        public record Location(
                        String name,
                        String region,
                        String country,
                        @JsonProperty("localtime") String localTime,
                        double lat,
                        double lon,
                        @JsonProperty("tz_id") String tzId) implements Serializable {
        }

        public record Current(
                        @JsonProperty("temp_c") double tempC,
                        @JsonProperty("temp_f") double tempF,
                        @JsonProperty("feelslike_c") double feelsLikeC,
                        @JsonProperty("feelslike_f") double feelsLikeF,

                        @JsonProperty("wind_kph") double windKph,
                        @JsonProperty("wind_mph") double windMph,
                        @JsonProperty("wind_degree") int windDegree,
                        @JsonProperty("wind_dir") String windDir,
                        @JsonProperty("gust_kph") double gustKph,
                        @JsonProperty("gust_mph") double gustMph,

                        @JsonProperty("precip_mm") double precipMm,
                        @JsonProperty("precip_in") double precipIn,

                        @JsonProperty("pressure_mb") double pressureMb,
                        @JsonProperty("pressure_in") double pressureIn,

                        @JsonProperty("vis_km") double visKm,
                        @JsonProperty("vis_miles") double visMiles,

                        int humidity,
                        int cloud,
                        @JsonProperty("uv") double uvIndex,
                        @JsonProperty("is_day") int isDay,
                        @JsonProperty("last_updated") String lastUpdated,
                        Condition condition) implements Serializable {
        }

        public record Condition(
                        String text,
                        String icon,
                        int code) implements Serializable {
        }

        public record Forecast(
                        List<ForecastDay> forecastday) implements Serializable {
        }

        public record ForecastDay(
                        String date,
                        @JsonProperty("date_epoch") long dateEpoch,
                        Day day) implements Serializable {
        }

        public record Day(

                        @JsonProperty("maxtemp_c") double maxTempC,
                        @JsonProperty("maxtemp_f") double maxTempF,
                        @JsonProperty("mintemp_c") double minTempC,
                        @JsonProperty("mintemp_f") double minTempF,
                        @JsonProperty("avgtemp_c") double avgTempC,
                        @JsonProperty("avgtemp_f") double avgTempF,

                        @JsonProperty("maxwind_kph") double maxWindKph,
                        @JsonProperty("maxwind_mph") double maxWindMph,

                        @JsonProperty("totalprecip_mm") double totalPrecipMm,
                        @JsonProperty("totalprecip_in") double totalPrecipIn,
                        @JsonProperty("totalsnow_cm") double totalSnowCm,

                        @JsonProperty("avgvis_km") double avgVisKm,
                        @JsonProperty("avgvis_miles") double avgVisMiles,

                        @JsonProperty("avghumidity") int avgHumidity,
                        @JsonProperty("daily_will_it_rain") int dailyWillItRain,
                        @JsonProperty("daily_will_it_snow") int dailyWillItSnow,
                        @JsonProperty("daily_chance_of_rain") int dailyChanceOfRain,
                        @JsonProperty("daily_chance_of_snow") int dailyChanceOfSnow,
                        @JsonProperty("uv") double uvIndex,
                        Condition condition) implements Serializable {
        }
}
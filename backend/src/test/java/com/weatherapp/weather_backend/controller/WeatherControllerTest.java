package com.weatherapp.weather_backend.controller;

import com.weatherapp.weather_backend.controllers.WeatherApiController;
import com.weatherapp.weather_backend.dto.WeatherResponseDTO;
import com.weatherapp.weather_backend.exceptions.CityNotFound;
import com.weatherapp.weather_backend.exceptions.WeatherApiException;
import com.weatherapp.weather_backend.exceptions.WeatherApiParseException;
import com.weatherapp.weather_backend.services.WeatherApiServices;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class WeatherControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private WeatherApiServices weatherService;

        // ========================================
        // HAPPY PATH TESTS
        // ========================================

        @Test
        @DisplayName("Should use default parameters (day=1, lang=en, metric=metric)")
        void getWeather_ShouldUseDefaults_WhenNoParamsProvided() throws Exception {
                WeatherResponseDTO dummyData = new WeatherResponseDTO(null, null, null);
                given(weatherService.getWeather(eq("London"), eq(1), eq("en"), eq("metric")))
                                .willReturn(Optional.of(dummyData));

                mockMvc.perform(get("/api/v1/weather/London")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.data").exists())
                                .andExpect(jsonPath("$.message").value("Weather data"))
                                .andExpect(jsonPath("$.timestamp").exists());
        }

        @Test
        @DisplayName("Should pass custom parameters to service")
        void getWeather_ShouldPassParams_WhenProvided() throws Exception {
                WeatherResponseDTO dummyData = new WeatherResponseDTO(null, null, null);
                given(weatherService.getWeather(eq("Madrid"), eq(3), eq("es"), eq("metric")))
                                .willReturn(Optional.of(dummyData));

                mockMvc.perform(get("/api/v1/weather/Madrid")
                                .param("day", "3")
                                .param("lang", "es")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("Should handle imperial metric system")
        void getWeather_ShouldWork_WithImperialMetric() throws Exception {
                WeatherResponseDTO dummyData = new WeatherResponseDTO(null, null, null);
                given(weatherService.getWeather(eq("New York"), eq(7), eq("en"), eq("imperial")))
                                .willReturn(Optional.of(dummyData));

                mockMvc.perform(get("/api/v1/weather/New York")
                                .param("day", "7")
                                .param("metric", "imperial")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("Should handle all valid languages")
        void getWeather_ShouldWork_WithAllValidLanguages() throws Exception {
                String[] validLangs = { "en", "es", "fr", "de", "pt" };

                for (String lang : validLangs) {
                        WeatherResponseDTO dummyData = new WeatherResponseDTO(null, null, null);
                        given(weatherService.getWeather(eq("Paris"), eq(1), eq(lang), eq("metric")))
                                        .willReturn(Optional.of(dummyData));

                        mockMvc.perform(get("/api/v1/weather/Paris")
                                        .param("lang", lang)
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isOk());
                }
        }

        @Test
        @DisplayName("Should handle city with coordinates")
        void getWeather_ShouldWork_WithCoordinates() throws Exception {
                WeatherResponseDTO dummyData = new WeatherResponseDTO(null, null, null);
                given(weatherService.getWeather(eq("40.7128,-74.0060"), eq(1), eq("en"), eq("metric")))
                                .willReturn(Optional.of(dummyData));

                mockMvc.perform(get("/api/v1/weather/40.7128,-74.0060")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        // ========================================
        // VALIDATION TESTS - DAY PARAMETER
        // ========================================

        @Test
        @DisplayName("Should return 400 when day < 1")
        void getWeather_ShouldReturn400_WhenDayIsTooLow() throws Exception {
                mockMvc.perform(get("/api/v1/weather/London")
                                .param("day", "0")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").isNotEmpty())
                                .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("Should return 400 when day > 7")
        void getWeather_ShouldReturn400_WhenDayIsTooHigh() throws Exception {
                mockMvc.perform(get("/api/v1/weather/London")
                                .param("day", "8")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").isNotEmpty());
        }

        @Test
        @DisplayName("Should accept day at boundary values (1 and 7)")
        void getWeather_ShouldWork_AtBoundaryValues() throws Exception {
                WeatherResponseDTO dummyData = new WeatherResponseDTO(null, null, null);

                // Test day=1
                given(weatherService.getWeather(eq("Tokyo"), eq(1), eq("en"), eq("metric")))
                                .willReturn(Optional.of(dummyData));
                mockMvc.perform(get("/api/v1/weather/Tokyo")
                                .param("day", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                // Test day=7
                given(weatherService.getWeather(eq("Tokyo"), eq(7), eq("en"), eq("metric")))
                                .willReturn(Optional.of(dummyData));
                mockMvc.perform(get("/api/v1/weather/Tokyo")
                                .param("day", "7")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        // ========================================
        // VALIDATION TESTS - LANG PARAMETER
        // ========================================

        @Test
        @DisplayName("Should return 400 when lang is invalid")
        void getWeather_ShouldReturn400_WhenLangIsInvalid() throws Exception {
                mockMvc.perform(get("/api/v1/weather/London")
                                .param("lang", "jp")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Language must be one of: en, es, fr, de, pt"));
        }

        @Test
        @DisplayName("Should return 400 when lang has invalid format")
        void getWeather_ShouldReturn400_WhenLangHasInvalidFormat() throws Exception {
                mockMvc.perform(get("/api/v1/weather/London")
                                .param("lang", "english")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        // ========================================
        // VALIDATION TESTS - METRIC PARAMETER
        // ========================================

        @Test
        @DisplayName("Should return 400 when metric is invalid")
        void getWeather_ShouldReturn400_WhenMetricIsInvalid() throws Exception {
                mockMvc.perform(get("/api/v1/weather/London")
                                .param("metric", "kelvin")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Metric must be 'metric' or 'imperial'"));
        }

        // ========================================
        // VALIDATION TESTS - CITY PARAMETER
        // ========================================

        @Test
        @DisplayName("Should return 400 when city is blank")
        void getWeather_ShouldReturn400_WhenCityIsBlank() throws Exception {
                mockMvc.perform(get("/api/v1/weather/   ")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle city with special characters")
        void getWeather_ShouldWork_WithSpecialCharacters() throws Exception {
                WeatherResponseDTO dummyData = new WeatherResponseDTO(null, null, null);
                given(weatherService.getWeather(eq("São Paulo"), eq(1), eq("en"), eq("metric")))
                                .willReturn(Optional.of(dummyData));

                mockMvc.perform(get("/api/v1/weather/São Paulo")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        // ========================================
        // SERVICE EXCEPTION TESTS
        // ========================================

        @Test
        @DisplayName("Should return 404 when city not found")
        void getWeather_ShouldReturn404_WhenCityNotFound() throws Exception {
                given(weatherService.getWeather(eq("Narnia"), eq(1), eq("en"), eq("metric")))
                                .willReturn(Optional.empty());

                mockMvc.perform(get("/api/v1/weather/Narnia")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("City not found"))
                                .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("Should return 404 when service throws CityNotFound exception")
        void getWeather_ShouldReturn404_WhenServiceThrowsCityNotFound() throws Exception {
                given(weatherService.getWeather(eq("InvalidCity"), eq(1), eq("en"), eq("metric")))
                                .willThrow(new CityNotFound(
                                                "Location 'InvalidCity' not found. Please verify the name."));

                mockMvc.perform(get("/api/v1/weather/InvalidCity")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("Should return 502 when WeatherApiException is thrown")
        void getWeather_ShouldReturn502_WhenWeatherApiExceptionThrown() throws Exception {
                given(weatherService.getWeather(eq("London"), eq(1), eq("en"), eq("metric")))
                                .willThrow(new WeatherApiException("Error in weather API request"));

                mockMvc.perform(get("/api/v1/weather/London")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadGateway())
                                .andExpect(jsonPath("$.message").value("Weather API error"));
        }

        @Test
        @DisplayName("Should return 500 when WeatherApiParseException is thrown")
        void getWeather_ShouldReturn500_WhenParseExceptionThrown() throws Exception {
                given(weatherService.getWeather(eq("London"), eq(1), eq("en"), eq("metric")))
                                .willThrow(new WeatherApiParseException("Unknown error while fetching weather data."));

                mockMvc.perform(get("/api/v1/weather/London")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message").value("Data processing error"));
        }

        // ========================================
        // COMBINED SCENARIOS
        // ========================================

        @Test
        @DisplayName("Should handle all custom parameters together")
        void getWeather_ShouldWork_WithAllCustomParams() throws Exception {
                WeatherResponseDTO dummyData = new WeatherResponseDTO(null, null, null);
                given(weatherService.getWeather(eq("Berlin"), eq(7), eq("de"), eq("imperial")))
                                .willReturn(Optional.of(dummyData));

                mockMvc.perform(get("/api/v1/weather/Berlin")
                                .param("day", "7")
                                .param("lang", "de")
                                .param("metric", "imperial")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.data").exists())
                                .andExpect(jsonPath("$.message").exists())
                                .andExpect(jsonPath("$.timestamp").exists());
        }
}
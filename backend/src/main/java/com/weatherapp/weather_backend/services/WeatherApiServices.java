package com.weatherapp.weather_backend.services;

import com.weatherapp.weather_backend.interfaces.WeatherProvider;
import com.weatherapp.weather_backend.config.WeatherProperties;
import com.weatherapp.weather_backend.dto.WeatherApiError;
import com.weatherapp.weather_backend.dto.WeatherDTO;
import com.weatherapp.weather_backend.dto.WeatherResponseDTO;
import com.weatherapp.weather_backend.exceptions.CityNotFound;
import com.weatherapp.weather_backend.exceptions.WeatherApiException;
import com.weatherapp.weather_backend.exceptions.WeatherApiParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherApiServices implements WeatherProvider {

    private static final Logger logger = LoggerFactory.getLogger(WeatherApiServices.class);

    private final WeatherProperties weatherProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherApiServices(WeatherProperties weatherProperties, RestTemplate restTemplate,
            ObjectMapper objectMapper) {
        this.weatherProperties = weatherProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    @Cacheable(value = "weather_cache", key = "#city + '_' + #day + '_' + #lang + '_' + #metric")
    public Optional<WeatherResponseDTO> getWeather(String city, int day, String lang, String metric) {

        String url = UriComponentsBuilder.fromUriString(weatherProperties.getUrl())
                .queryParam("key", weatherProperties.getKey())
                .queryParam("q", city)
                .queryParam("days", day)
                .queryParam("lang", lang)
                .queryParam("aqi", "no")
                .queryParam("alerts", "no")
                .toUriString();

        logger.info("Requesting weather for: {}, system: {}, url: {}", city, metric, sanitizeUrl(url));

        try {
            WeatherDTO weatherDataRaw = restTemplate.getForObject(url, WeatherDTO.class);

            if (weatherDataRaw == null) {
                return Optional.empty();
            }

            return Optional.of(mapToMetric(weatherDataRaw, metric));

        } catch (HttpClientErrorException.BadRequest e) {
            String responseBody = e.getResponseBodyAsString();

            try {
                WeatherApiError apiError = objectMapper.readValue(responseBody, WeatherApiError.class);

                if (apiError.error() != null && apiError.error().code() == 1006) {
                    logger.warn("Location not found: {}", city);
                    throw new CityNotFound("Location '" + city + "' not found. Please verify the name.");
                }

                logger.error("WeatherAPI 400 error (not location): {}", responseBody);
                throw new WeatherApiException("Error in weather API request: " + apiError.error().message());

            } catch (CityNotFound cnf) {
                throw cnf;
            } catch (Exception parseException) {
                logger.error("Error processing API error response", parseException);
                throw new WeatherApiParseException("Unknown error while fetching weather data.", parseException);
            }
        } catch (Exception e) {
            logger.error("General error in weather service", e);
            throw e;
        }
    }

    private String sanitizeUrl(String url) {
        if (url == null)
            return null;
        return url.replaceAll("([?&]key=)[^&]+", "$1***MASKED***");
    }

    private WeatherResponseDTO mapToMetric(WeatherDTO raw, String metric) {

        boolean isMetric = !"imperial".equalsIgnoreCase(metric);

        WeatherResponseDTO.LocationResponse loc = new WeatherResponseDTO.LocationResponse(
                raw.location().name(),
                raw.location().region(),
                raw.location().country(),
                raw.location().localTime());

        WeatherResponseDTO.ConditionResponse currentCond = new WeatherResponseDTO.ConditionResponse(
                raw.current().condition().text(),
                raw.current().condition().icon());

        WeatherResponseDTO.CurrentResponse curr = new WeatherResponseDTO.CurrentResponse(
                isMetric ? raw.current().tempC() : raw.current().tempF(),
                isMetric ? raw.current().feelsLikeC() : raw.current().feelsLikeF(),
                raw.current().humidity(),
                isMetric ? raw.current().windKph() : raw.current().windMph(),
                raw.current().uvIndex(),
                isMetric ? raw.current().visKm() : raw.current().visMiles(),
                raw.current().isDay(),
                currentCond);

        List<WeatherResponseDTO.ForecastDayResponse> forecastList = raw.forecast().forecastday().stream().map(fDay -> {

            WeatherResponseDTO.ConditionResponse dayCond = new WeatherResponseDTO.ConditionResponse(
                    fDay.day().condition().text(),
                    fDay.day().condition().icon());

            WeatherResponseDTO.DayResponse dayResp = new WeatherResponseDTO.DayResponse(
                    isMetric ? fDay.day().maxTempC() : fDay.day().maxTempF(),
                    isMetric ? fDay.day().minTempC() : fDay.day().minTempF(),
                    isMetric ? fDay.day().avgTempC() : fDay.day().avgTempF(),
                    isMetric ? fDay.day().maxWindKph() : fDay.day().maxWindMph(),
                    isMetric ? fDay.day().totalPrecipMm() : fDay.day().totalPrecipIn(),
                    isMetric ? fDay.day().avgVisKm() : fDay.day().avgVisMiles(),
                    fDay.day().avgHumidity(),
                    fDay.day().uvIndex(),
                    dayCond);

            return new WeatherResponseDTO.ForecastDayResponse(fDay.date(), dayResp);

        }).toList();

        return new WeatherResponseDTO(
                loc,
                curr,
                new WeatherResponseDTO.ForecastResponse(forecastList));
    }
}
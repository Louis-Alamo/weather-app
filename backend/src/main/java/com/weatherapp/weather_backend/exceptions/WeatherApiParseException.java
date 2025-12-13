package com.weatherapp.weather_backend.exceptions;

public class WeatherApiParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public WeatherApiParseException(String message) {
        super(message);
    }

    public WeatherApiParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

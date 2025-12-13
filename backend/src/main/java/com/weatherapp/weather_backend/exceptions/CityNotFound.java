package com.weatherapp.weather_backend.exceptions;

public class CityNotFound extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CityNotFound(String message) {
        super(message);
    }
}
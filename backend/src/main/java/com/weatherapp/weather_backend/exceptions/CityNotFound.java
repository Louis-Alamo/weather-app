package com.weatherapp.weather_backend.exceptions;

public class CityNotFound extends RuntimeException {
    public CityNotFound(String message) {
        super(message);
    }
}
package com.meteonow.model;

public class WeatherData {
    private String city;
    private double temperature;
    private String description;
    private String icon;
    private int humidity;
    private double windSpeed;

    public WeatherData(String city, double temperature, String description, String icon, int humidity, double windSpeed) {
        this.city = city;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    
}

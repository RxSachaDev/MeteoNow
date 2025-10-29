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

    public String getCity() {
        return city;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return "City : " + city + " Temperature : " + temperature + " Description : " + description + " Humidity : " + humidity + " Wind Speed : " + windSpeed;
    }
}

package com.meteonow.controller;

import java.net.ProtocolException;

import com.meteonow.model.WeatherData;
import com.meteonow.model.WeatherService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CityController {

    private final WeatherService ws = WeatherService.getInstance();

    @FXML
    private Label cityLabel;

    @FXML
    private Label tempLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private Label windSpeedLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView iconImage;

    public void update(String city) throws ProtocolException{
        WeatherData weatherData = ws.getWeather(city);
        
        if (weatherData != null){
            cityLabel.setText(weatherData.getCity());
            tempLabel.setText(String.valueOf(weatherData.getTemperature()));
            humidityLabel.setText(String.valueOf(weatherData.getHumidity()));
            windSpeedLabel.setText(String.valueOf(weatherData.getWindSpeed()));
            descriptionLabel.setText(weatherData.getDescription());
            iconImage.setImage(new Image(weatherData.getIcon()));
        }
    }
}

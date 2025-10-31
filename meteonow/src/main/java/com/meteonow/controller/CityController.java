package com.meteonow.controller;

import java.net.ProtocolException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.meteonow.model.WeatherData;
import com.meteonow.model.WeatherService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Label errorLabel;

    @FXML
    private ImageView iconImage;

    @FXML
    private TextField findCity;

    @FXML
    public void handleEnterCity(ActionEvent event) throws ProtocolException{
        update(findCity.getText());
    }

    public void update(String city) throws ProtocolException{
        String cityEncoded = URLEncoder.encode(city, StandardCharsets.UTF_8);
        WeatherData weatherData = ws.getWeather(cityEncoded);
        
        if (weatherData != null){
            cityLabel.setText(city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase());
            tempLabel.setText(String.format("%.2f°C", weatherData.getTemperature() - 273.15));
            humidityLabel.setText(String.valueOf(weatherData.getHumidity()));
            windSpeedLabel.setText(String.valueOf(weatherData.getWindSpeed()) + " km/h");
            descriptionLabel.setText(weatherData.getDescription());
            iconImage.setImage(new Image(weatherData.getIcon()));
            errorLabel.setText("");
        } else {
            errorLabel.setText("Cette ville n'est pas reconnu !");
        }
    }
}

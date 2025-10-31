package com.meteonow.controller;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.meteonow.model.WeatherService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MainController {

    private WeatherService weatherService;

    @FXML
    private AnchorPane cityContainer;

    @FXML
    private TextField findCity;

    @FXML
    public void initialize() {
        weatherService = WeatherService.getInstance();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/meteonow/view/city_view.fxml"));
            AnchorPane cityView = loader.load();

            // Récupère le contrôleur associé au city_view.fxml
            CityController cityController = loader.getController();

            // Inscrit CityController comme observateur
            weatherService.addObserver(cityController);

            // Affiche la vue
            cityContainer.getChildren().setAll(cityView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleEnterCity(ActionEvent event) throws ProtocolException{
        
        String cityEncoded = URLEncoder.encode(findCity.getText(), StandardCharsets.UTF_8);
        weatherService.updateWeather(cityEncoded);

    }
}

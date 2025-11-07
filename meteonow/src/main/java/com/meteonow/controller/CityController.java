package com.meteonow.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.meteonow.model.WeatherData;
import com.meteonow.utils.Observer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CityController implements Observer{

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

    
    @Override
    public void update(List<WeatherData> data){
        
        if (data != null){
            String cityDecode = URLDecoder.decode(data.get(0).getCity(), StandardCharsets.UTF_8);
            
            cityLabel.setText(cityDecode.substring(0, 1).toUpperCase() + cityDecode.substring(1).toLowerCase());
            tempLabel.setText(String.format("%.2f°C", data.get(0).getTemperature() - 273.15));
            humidityLabel.setText(String.valueOf(data.get(0).getHumidity()) + "%");
            windSpeedLabel.setText(String.valueOf(data.get(0).getWindSpeed()) + " km/h");
            descriptionLabel.setText(data.get(0).getDescription().substring(0, 1).toUpperCase() + data.get(0).getDescription().substring(1).toLowerCase());
            iconImage.setImage(new Image(data.get(0).getIcon()));
            errorLabel.setText("");
        } else {
            errorLabel.setText("Cette ville n'est pas reconnu !");
        }
    }
}

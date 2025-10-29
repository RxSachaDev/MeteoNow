package com.meteonow.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meteonow.utils.ConfigLoader;

public class WeatherService {
    private static WeatherService instance;
    private final Gson gson = new Gson();

    /**
     * Constructeur privé pour empêcher toute instanciation directe.
     */
    private WeatherService() {

    }

    /**
     * Retourne l’unique instance du service (thread-safe).
     */
    public static WeatherService getInstance() {
        if (instance == null) {
            instance = new WeatherService();
        }
        return instance;
    }

    private URI getWeatherURI(String city) throws IOException {
        ConfigLoader config = ConfigLoader.getInstance();
        String apiKey = config.getProperty("API_KEY");
        return URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);
    }
    
    private JsonObject getWeatherJson(String city) throws ProtocolException{
        try {
            URI uri = getWeatherURI(city);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");

            try (Reader reader = new InputStreamReader(connection.getInputStream())) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                return json; // renvoie tout le JSON si besoin
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public WeatherData getWeather(String city) throws ProtocolException{
        JsonObject json = getWeatherJson(city);

        JsonObject main = json.getAsJsonObject("main");
        JsonObject weather = json.getAsJsonArray("weather").get(0).getAsJsonObject();
        JsonObject wind = json.getAsJsonObject("wind");

        double temperature = main.get("temp").getAsDouble();
        String description = weather.get("description").getAsString();
        int humidity = main.get("humidity").getAsInt();
        double windSpeed = wind.get("speed").getAsDouble();

        return new WeatherData(city, temperature, description, "", humidity, windSpeed);
    }
}
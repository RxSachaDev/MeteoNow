package com.meteonow.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.meteonow.utils.ConfigLoader;
import com.meteonow.utils.Observer;

public class WeatherService {
    private static WeatherService instance;
    private final Gson gson = new Gson();
    private final WeatherCache cache = WeatherCache.getInstance();
    private List<Observer> observers = new ArrayList<>();

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

    public void addObserver(Observer o) { observers.add(o); }
    public void removeObserver(Observer o) { observers.remove(o); }

    public void notifyObservers(List<WeatherData> data) {
        for (Observer o : observers) {
            o.update(data);
        }
    }

    public void updateWeather(String city) throws ProtocolException {
        WeatherData weather = getWeather(city);
        List<WeatherData> listWeather = getWeatherDay(city);
        if (listWeather != null) {
            LinkedList<WeatherData> linkedList = new LinkedList<>(listWeather);
            linkedList.addFirst(weather);
            notifyObservers(linkedList);
        }else {
            notifyObservers(null);
        }
        
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
            return null;
        }
    }

    public WeatherData getWeather(String city) throws ProtocolException{
        if (cache.contains(city)){
            return cache.get(city);
        }
        else{
            JsonObject json = getWeatherJson(city);

            if (json != null){
                JsonObject main = json.getAsJsonObject("main");
                JsonObject weather = json.getAsJsonArray("weather").get(0).getAsJsonObject();
                JsonObject wind = json.getAsJsonObject("wind");

                double temperature = main.get("temp").getAsDouble();
                String description = weather.get("description").getAsString();
                int humidity = main.get("humidity").getAsInt();
                double windSpeed = wind.get("speed").getAsDouble();
                String icon = weather.get("icon").getAsString();

                WeatherData weatherData = new WeatherData(city, temperature, description, "https://openweathermap.org/img/wn/" + icon + "@2x.png", humidity, windSpeed, new Date());
                cache.add(city, weatherData);

                return weatherData;
            } else {
                return null;
            }
        }
    }

    public URI getWeatherDayURI(double lat, double lon) throws IOException{
        ConfigLoader config = ConfigLoader.getInstance();
        String apiKey = config.getProperty("API_KEY");
        return URI.create("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat +"&lon=" + lon + "&appid=" + apiKey);
    }

    private JsonObject getWeatherDayJson(double lat, double lon) throws ProtocolException{
        try {
            URI uri = getWeatherDayURI(lat, lon);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");

            try (Reader reader = new InputStreamReader(connection.getInputStream())) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                return json; // renvoie tout le JSON si besoin
            }

        } catch (Exception e) {
            return null;
        }
    }

    public List<WeatherData> getWeatherDay(String city) throws ProtocolException{
        double[] geoCoding = getGeoCoding(city);
        if (geoCoding == null) return null;
        JsonObject json = getWeatherDayJson(geoCoding[0], geoCoding[1]);
        List<WeatherData> listWeather = new ArrayList<>();
        if (json != null){
                JsonArray list = json.getAsJsonArray("list");
                for (JsonElement elem : list){
                    JsonObject elemObject = elem.getAsJsonObject();
                    JsonObject main = elemObject.getAsJsonObject("main");
                    double temperature = main.get("temp").getAsDouble();
                    int humidity = main.get("humidity").getAsInt();

                    JsonObject weather = elemObject.getAsJsonArray("weather").get(0).getAsJsonObject();
                    String description = weather.get("description").getAsString();
                    String icon = weather.get("icon").getAsString();

                    JsonObject wind = elemObject.getAsJsonObject("wind");
                    double windSpeed = wind.get("speed").getAsDouble();

                    String date = elemObject.get("dt_txt").getAsString();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    LocalDateTime utcTime = LocalDateTime.parse(date, formatter);
                    Date dateConvert = Date.from(utcTime.atZone(ZoneId.of("UTC"))
                                                    .withZoneSameInstant(ZoneId.systemDefault())
                                                    .toInstant());

                    WeatherData weatherData = new WeatherData(city, temperature, description, "https://openweathermap.org/img/wn/" + icon + "@2x.png", humidity, windSpeed, dateConvert);
                    listWeather.add(weatherData);
                }

                return listWeather;
        } else {
            return null;
        }
    }

    public URI getGeoCodingURI(String city) throws IOException{
        ConfigLoader config = ConfigLoader.getInstance();
        String apiKey = config.getProperty("API_KEY");
        return URI.create("https://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + apiKey);
    }

    private JsonArray getGeoCodingJson(String city) throws ProtocolException {
        try {
            URI uri = getGeoCodingURI(city);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");

            try (Reader reader = new InputStreamReader(connection.getInputStream())) {
                JsonArray json = gson.fromJson(reader, JsonArray.class);
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace(); // utile pour voir l’erreur en console
            return null;
        }
    }


    public double[] getGeoCoding(String city) throws ProtocolException{
        JsonArray json = getGeoCodingJson(city);
        if (json != null){
                JsonObject elem = json.get(0).getAsJsonObject();
                double lat = elem.get("lat").getAsDouble();
                double lon = elem.get("lon").getAsDouble();
                double[] result = {lat, lon};
                return result;
        } else {
            return null;
        }
    }
}
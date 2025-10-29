package com.meteonow.model;

public class WeatherService {
    private static WeatherService instance;

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


}
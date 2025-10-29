package com.meteonow.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import com.meteonow.model.WeatherData;

public class WeatherCache {
    private static WeatherCache instance;
    private final Map<String, WeatherData> cache;
    private static final int CACHE_SIZE = 15;

    private WeatherCache() {
        // true = ordre d’accès → permet un comportement LRU (Least Recently Used)
        cache = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, WeatherData> eldest) {
                return size() > CACHE_SIZE;
            }
        };
    }

    public static synchronized WeatherCache getInstance() {
        if (instance == null) {
            instance = new WeatherCache();
        }
        return instance;
    }

    public synchronized void add(String city, WeatherData data) {
        cache.put(city.toLowerCase(), data);
    }

    public synchronized WeatherData get(String city) {
        return cache.get(city.toLowerCase());
    }

    public synchronized boolean contains(String city) {
        return cache.containsKey(city.toLowerCase());
    }

    public synchronized void clear() {
        cache.clear();
    }
}


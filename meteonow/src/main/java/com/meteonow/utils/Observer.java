package com.meteonow.utils;

import com.meteonow.model.WeatherData;

public interface Observer {
    void update(WeatherData data);
}

package com.meteonow.utils;

import java.util.List;

import com.meteonow.model.WeatherData;

public interface Observer {
    void update(List<WeatherData> data);
}

package com.meteonow.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static ConfigLoader instance;
    private final Properties props;

    private ConfigLoader() throws IOException {
        props = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/config.properties")) {
            if (input == null) throw new IOException("config.properties introuvable");
            props.load(input);
        }
    }

    public static ConfigLoader getInstance() throws IOException {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }
}

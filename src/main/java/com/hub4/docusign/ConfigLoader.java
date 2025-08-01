package com.hub4.docusign;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;

    public ConfigLoader(String configPath) throws IOException {
        properties = new Properties();

        try(
                InputStream is = new FileInputStream(configPath)
        ){

            properties.load(is);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}

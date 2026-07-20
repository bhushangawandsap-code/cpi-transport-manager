package com.bhushan.cpitransport.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {

    private final Properties properties;

    public ConfigurationService() {

        properties = new Properties();

        System.out.println("Loading Configurations...");

        loadProperties();
    }

    /**
     * Returns the environment variable if available.
     * Otherwise falls back to the local properties file.
     */
    private String getValue(String envName, String propertyName) {

        String value = System.getenv(envName);

        if (value != null && !value.isBlank()) {
            return value;
        }

        return properties.getProperty(propertyName);
    }

    public String getHost(String environment) {

        return getValue(
                environment.toUpperCase() + "_HOST",
                environment.toLowerCase() + ".host");
    }

    public String getTokenUrl(String environment) {

        return getValue(
                environment.toUpperCase() + "_TOKEN_URL",
                environment.toLowerCase() + ".token.url");
    }

    public String getClientId(String environment) {

        return getValue(
                environment.toUpperCase() + "_CLIENT_ID",
                environment.toLowerCase() + ".client.id");
    }

    public String getClientSecret(String environment) {

        return getValue(
                environment.toUpperCase() + "_CLIENT_SECRET",
                environment.toLowerCase() + ".client.secret");
    }

    public String getApiUrl() {

        return properties.getProperty("integration.api.url");
    }

    private void loadProperties() {

        System.out.println("Inside loadProperties()");

        InputStream inputStream =
                getClass()
                        .getClassLoader()
                        .getResourceAsStream("transport-manager-dev.properties");

        if (inputStream == null) {
            throw new RuntimeException("transport-manager-dev.properties not found");
        }

        try {

            properties.load(inputStream);

        } catch (IOException e) {

            throw new RuntimeException("Failed to load configuration.", e);

        }
    }
}
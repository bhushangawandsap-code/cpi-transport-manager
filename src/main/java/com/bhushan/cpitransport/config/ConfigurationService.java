package com.bhushan.cpitransport.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {


    private final Properties properties;

    private String getValue(String envName, String propertyName) {

        String value = System.getenv(envName);

        if (value != null && !value.isBlank()) {
            return value;
        }

        return properties.getProperty(propertyName);
    }
    public ConfigurationService() {

        properties = new Properties();
        System.out.println("Loading Configurations...");
        loadProperties();


    }
    public String getHost() {

        return getValue("CPI_HOST", "integration.host");

    }
    public String getTokenUrl() {
        return getValue("TOKEN_URL", "integration.token.url");
    }

    public String getClientId() {
        return getValue("CLIENT_ID", "integration.client.id");
    }

    public String getClientSecret() {
        return getValue("CLIENT_SECRET", "integration.client.secret");
    }

    public String getApiUrl() {
        return properties.getProperty("integration.api.url");
    }

    private void loadProperties() {

        System.out.println("Inside loadProperties()");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("transport-manager-dev.properties");
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
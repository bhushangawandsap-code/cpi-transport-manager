package com.bhushan.cpitransport.configuration;

import com.bhushan.cpitransport.configuration.model.Configuration;
import com.bhushan.cpitransport.configuration.model.ConfigurationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurationMergeService {

    private final ObjectMapper objectMapper;

    public ConfigurationMergeService() {

        this.objectMapper = new ObjectMapper();

    }

    public String merge(
            String environment,
            String devConfiguration,
            String targetConfiguration) {

        try {

            ConfigurationResponse devResponse =
                    objectMapper.readValue(
                            devConfiguration,
                            ConfigurationResponse.class);

            ConfigurationResponse targetResponse =
                    objectMapper.readValue(
                            targetConfiguration,
                            ConfigurationResponse.class);

            if (devResponse.getD() == null ||
                    devResponse.getD().getResults() == null) {

                throw new RuntimeException(
                        "DEV configuration is invalid."
                );

            }

            if (targetResponse.getD() == null ||
                    targetResponse.getD().getResults() == null) {

                throw new RuntimeException(
                        "Target configuration is invalid."
                );

            }

            Map<String, Configuration> targetConfigurations =
                    new LinkedHashMap<>();

            for (Configuration configuration :
                    targetResponse.getD().getResults()) {

                targetConfigurations.put(
                        configuration.getParameterKey(),
                        configuration
                );

            }

            int addedParameters = 0;

            for (Configuration configuration :
                    devResponse.getD().getResults()) {

                if (!targetConfigurations.containsKey(
                        configuration.getParameterKey())) {

                    targetConfigurations.put(
                            configuration.getParameterKey(),
                            configuration
                    );

                    addedParameters++;

                    System.out.println(
                            "[" + environment + "] Added Parameter : "
                                    + configuration.getParameterKey()
                    );

                }

            }

            targetResponse.getD().setResults(
                    new ArrayList<>(targetConfigurations.values())
            );

            System.out.println();

            System.out.println(
                    "[" + environment + "] "
                            + addedParameters
                            + " new parameter(s) added."
            );

            return objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(targetResponse);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}
package com.bhushan.cpitransport.file;

import com.bhushan.cpitransport.configuration.model.Configuration;
import com.bhushan.cpitransport.configuration.model.ConfigurationResponse;
import com.bhushan.cpitransport.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileService {

    private final ObjectMapper objectMapper =
            JsonUtil.getObjectMapper();

    public void saveZip(
            byte[] zipData,
            String iflowId,
            String sourceEnvironment) {

        try {

            Path directory = Paths.get(
                    "downloads",
                    sourceEnvironment,
                    "iflows");

            Files.createDirectories(directory);

            Files.write(
                    directory.resolve(iflowId + ".zip"),
                    zipData);

            System.out.println("Download Successful!");
            System.out.println("File saved at : "
                    + directory.resolve(iflowId + ".zip"));

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public void saveConfiguration(
            String configuration,
            String iflowId,
            String sourceEnvironment) {

        try {

            Path directory = Paths.get(
                    "downloads",
                    sourceEnvironment,
                    "configs");

            Files.createDirectories(directory);

            Files.writeString(
                    directory.resolve(iflowId + "-config.json"),
                    JsonUtil.prettyPrint(configuration));

            System.out.println("Configuration Saved Successfully!");

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public void saveConfigurationTemplate(
            String configuration,
            String iflowId,
            String targetEnvironment) {

        try {

            Path directory = Paths.get(
                    "downloads",
                    targetEnvironment.toUpperCase(),
                    "configs");

            Files.createDirectories(directory);

            Files.writeString(
                    directory.resolve(
                            iflowId + "_"
                                    + targetEnvironment.toUpperCase()
                                    + ".json"),
                    JsonUtil.prettyPrint(configuration));

            System.out.println("Configuration Template Saved Successfully!");

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public boolean configurationExists(
            String environment,
            String iflowId) {

        return Files.exists(getConfigurationPath(environment, iflowId));

    }

    public String readConfigurationTemplate(
            String environment,
            String iflowId) {

        try {

            return Files.readString(
                    getConfigurationPath(environment, iflowId));

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public List<Configuration> readConfigurationList(
            String environment,
            String iflowId) {

        try {

            String json =
                    readConfigurationTemplate(
                            environment,
                            iflowId);

            ConfigurationResponse response =
                    objectMapper.readValue(
                            json,
                            ConfigurationResponse.class);

            return response.getD().getResults();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    private Path getConfigurationPath(
            String environment,
            String iflowId) {

        return Paths.get(
                "downloads",
                environment.toUpperCase(),
                "configs",
                iflowId + "_"
                        + environment.toUpperCase()
                        + ".json");

    }

}
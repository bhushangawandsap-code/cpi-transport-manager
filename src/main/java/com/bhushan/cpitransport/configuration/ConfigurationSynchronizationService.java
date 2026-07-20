package com.bhushan.cpitransport.configuration;

import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.file.FileService;
import com.bhushan.cpitransport.http.HttpClientService;

import java.net.http.HttpResponse;

public class ConfigurationSynchronizationService {

    private final ConfigurationMergeService mergeService;
    private final HttpClientService httpClientService;
    private final ConfigurationService configurationService;
    private final FileService fileService;

    public ConfigurationSynchronizationService(
            HttpClientService httpClientService,
            ConfigurationService configurationService,
            FileService fileService,
            ConfigurationMergeService mergeService) {

        this.httpClientService = httpClientService;
        this.configurationService = configurationService;
        this.fileService = fileService;
        this.mergeService = mergeService;
    }

    public void synchronize(
            String accessToken,
            String sourceEnvironment,
            String iflowId) {

        System.out.println("======================================");
        System.out.println("Configuration Synchronization");
        System.out.println("======================================");

        String devConfiguration =
                downloadConfiguration(
                        accessToken,
                        sourceEnvironment,
                        iflowId
                );

        synchronizeEnvironment(
                "DEV",
                devConfiguration,
                iflowId
        );

        synchronizeEnvironment(
                "QA",
                devConfiguration,
                iflowId
        );

        synchronizeEnvironment(
                "PPD",
                devConfiguration,
                iflowId
        );

        synchronizeEnvironment(
                "PROD",
                devConfiguration,
                iflowId
        );

        System.out.println();
        System.out.println("Configuration Synchronization Completed.");

    }

    private String downloadConfiguration(
            String accessToken,
            String sourceEnvironment,
            String iflowId) {

        System.out.println();
        System.out.println("Downloading configuration from "
                + sourceEnvironment.toUpperCase());

        String url =
                configurationService.getHost(sourceEnvironment)
                        + configurationService.getApiUrl()
                        + "/IntegrationDesigntimeArtifacts("
                        + "Id='" + iflowId
                        + "',Version='active'"
                        + ")/Configurations";

        System.out.println(url);

        HttpResponse<String> response =
                httpClientService.get(
                        url,
                        accessToken
                );

        System.out.println("Status Code : "
                + response.statusCode());

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                    "Failed to download configuration."
            );

        }

        return response.body();

    }

    private void synchronizeEnvironment(
            String environment,
            String devConfiguration,
            String iflowId) {

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("Synchronizing " + environment);
        System.out.println("--------------------------------------");

        if (!fileService.configurationExists(
                environment,
                iflowId)) {

            System.out.println("Configuration file not found.");

            fileService.saveConfigurationTemplate(
                    devConfiguration,
                    iflowId,
                    environment
            );

            System.out.println("Configuration created.");

        } else {

            String existingConfiguration =
                    fileService.readConfigurationTemplate(
                            environment,
                            iflowId
                    );

            String mergedConfiguration =
                    mergeService.merge(
                            environment,
                            devConfiguration,
                            existingConfiguration
                    );

            fileService.saveConfigurationTemplate(
                    mergedConfiguration,
                    iflowId,
                    environment
            );

            System.out.println("Configuration merged.");

        }

    }

}
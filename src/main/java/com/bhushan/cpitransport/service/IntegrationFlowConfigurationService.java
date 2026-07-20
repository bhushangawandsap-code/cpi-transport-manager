package com.bhushan.cpitransport.service;

import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.configuration.model.Configuration;
import com.bhushan.cpitransport.configuration.model.ConfigurationUpdateRequest;
import com.bhushan.cpitransport.file.FileService;
import com.bhushan.cpitransport.http.HttpClientService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.List;

public class IntegrationFlowConfigurationService {

    private final HttpClientService httpClientService;
    private final ConfigurationService configurationService;
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    public IntegrationFlowConfigurationService(
            HttpClientService httpClientService,
            ConfigurationService configurationService,
            FileService fileService) {

        this.httpClientService = httpClientService;
        this.configurationService = configurationService;
        this.fileService = fileService;
        this.objectMapper = new ObjectMapper();

    }

    public void updateConfiguration(
            String accessToken,
            String targetEnvironment,
            String iflowId) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("Updating Externalized Parameters...");
        System.out.println("======================================");

        List<Configuration> configurations =
                fileService.readConfigurationList(
                        targetEnvironment,
                        iflowId);

        if (configurations.isEmpty()) {

            System.out.println("No Externalized Parameters Found.");
            return;

        }

        for (Configuration configuration : configurations) {

            updateParameter(
                    accessToken,
                    targetEnvironment,
                    iflowId,
                    configuration);

        }

        System.out.println("======================================");
        System.out.println("All Externalized Parameters Updated.");
        System.out.println("======================================");
        System.out.println();

    }

    private void updateParameter(
            String accessToken,
            String targetEnvironment,
            String iflowId,
            Configuration configuration) {

        try {

            System.out.println("--------------------------------------");
            System.out.println("Parameter : "
                    + configuration.getParameterKey());

            String url =
                    configurationService.getHost(targetEnvironment)
                            + configurationService.getApiUrl()
                            + "/IntegrationDesigntimeArtifacts("
                            + "Id='" + iflowId
                            + "',Version='active'"
                            + ")/$links/Configurations('"
                            + configuration.getParameterKey()
                            + "')";

            ConfigurationUpdateRequest request =
                    new ConfigurationUpdateRequest(
                            configuration.getParameterValue(),
                            configuration.getDataType(),
                            configuration.getDescription()
                    );

            String body =
                    objectMapper.writeValueAsString(request);

            System.out.println("======================================");
            System.out.println("URL:");
            System.out.println(url);

            System.out.println();
            System.out.println("Request Body:");
            System.out.println(body);
            System.out.println("======================================");

            HttpResponse<String> response =
                    httpClientService.put(
                            url,
                            body,
                            accessToken);

            System.out.println("Status Code : "
                    + response.statusCode());

            System.out.println("Response Body:");
            System.out.println(response.body());

            if (response.statusCode() != 200
                    && response.statusCode() != 204  && response.statusCode() != 202) {

                throw new RuntimeException(
                        "Failed to update parameter : "
                                + configuration.getParameterKey());

            }

            System.out.println("Successfully Updated : "
                    + configuration.getParameterKey());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to update parameter : "
                            + configuration.getParameterKey(),
                    e);

        }

    }

}
package com.bhushan.cpitransport.service;

import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.http.HttpClientService;
import com.bhushan.cpitransport.model.IntegrationFlowMetadata;
import com.bhushan.cpitransport.model.IntegrationFlowRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;

public class IntegrationFlowUploadService {

    private final HttpClientService httpClientService;
    private final ConfigurationService configurationService;

    public IntegrationFlowUploadService(
            HttpClientService httpClientService,
            ConfigurationService configurationService) {

        this.httpClientService = httpClientService;
        this.configurationService = configurationService;
    }

    /**
     * Creates a new iFlow (POST)
     */
    public void createIntegrationFlow(
            String accessToken,
            String environment,
            IntegrationFlowMetadata metadata,
            String artifactContent) {

        try {

            System.out.println("======================================");
            System.out.println("Creating Integration Flow...");
            System.out.println("======================================");

            String url =
                    configurationService.getHost(environment)
                            + configurationService.getApiUrl()
                            + "/IntegrationDesigntimeArtifacts";

            System.out.println(url);

            IntegrationFlowRequest request =
                    new IntegrationFlowRequest();

            request.setId(metadata.getId());
            request.setName(metadata.getName());
            request.setPackageId(metadata.getPackageId());
            request.setArtifactContent(artifactContent);

            ObjectMapper mapper = new ObjectMapper();

            String requestBody =
                    mapper.writeValueAsString(request);

            System.out.println(requestBody);

            HttpResponse<String> response =
                    httpClientService.post(
                            url,
                            requestBody,
                            accessToken
                    );

            System.out.println("Status Code : " + response.statusCode());
            System.out.println(response.body());

            if (response.statusCode() != 200 &&
                    response.statusCode() != 201) {

                throw new RuntimeException(
                        "Failed to create iFlow. Status Code : "
                                + response.statusCode()
                );

            }

            System.out.println("iFlow created successfully.");

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    /**
     * Updates an existing iFlow (PUT)
     */
    public void updateIntegrationFlow(
            String accessToken,
            String environment,
            IntegrationFlowMetadata metadata,
            String artifactContent) {

        try {

            System.out.println("======================================");
            System.out.println("Updating Integration Flow...");
            System.out.println("======================================");

            String url =
                    configurationService.getHost(environment)
                            + configurationService.getApiUrl()
                            + "/IntegrationDesigntimeArtifacts("
                            + "Id='" + metadata.getId()
                            + "',Version='active')";

            System.out.println(url);

            IntegrationFlowRequest request =
                    new IntegrationFlowRequest();

            request.setName(metadata.getName());
            request.setArtifactContent(artifactContent);

            ObjectMapper mapper = new ObjectMapper();

            String requestBody =
                    mapper.writeValueAsString(request);

            System.out.println(requestBody);

            HttpResponse<String> response =
                    httpClientService.put(
                            url,
                            requestBody,
                            accessToken
                    );

            System.out.println("Status Code : " + response.statusCode());
            System.out.println(response.body());

            if (response.statusCode() != 200 &&
                    response.statusCode() != 201) {

                throw new RuntimeException(
                        "Failed to update iFlow. Status Code : "
                                + response.statusCode()
                );

            }

            System.out.println("iFlow updated successfully.");

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}
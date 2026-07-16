package com.bhushan.cpitransport.service;

import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.http.HttpClientService;
import com.bhushan.cpitransport.model.IntegrationFlowMetadata;
import com.bhushan.cpitransport.model.IntegrationFlowMetadataResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;

public class IntegrationFlowMetadataService {

    private final HttpClientService httpClientService;
    private final ConfigurationService configurationService;

    public IntegrationFlowMetadataService(
            HttpClientService httpClientService,
            ConfigurationService configurationService) {

        this.httpClientService = httpClientService;
        this.configurationService = configurationService;
    }

    public IntegrationFlowMetadata getMetadata(
            String accessToken, String environment,
            String iflowId) {

        System.out.println("======================================");
        System.out.println("Getting Integration Flow Metadata...");
        System.out.println("======================================");

        String url =
                configurationService.getHost(environment)
                        + configurationService.getApiUrl()
                        + "/IntegrationDesigntimeArtifacts("
                        + "Id='" + iflowId + "',Version='active'"
                        + ")";

        System.out.println(url);
        HttpResponse<String> response =
                httpClientService.get(url, accessToken);

        System.out.println("Status Code : " + response.statusCode());

        System.out.println(response.body());

        if (response.statusCode() == 404) {

            System.out.println("Integration Flow not found.");

            return null;

        }

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                    "Unexpected Status Code : " + response.statusCode()
            );

        }

        try {

            ObjectMapper mapper = new ObjectMapper();

            IntegrationFlowMetadataResponse metadataResponse =
                    mapper.readValue(
                            response.body(),
                            IntegrationFlowMetadataResponse.class
                    );

            return metadataResponse.getMetadata();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

}
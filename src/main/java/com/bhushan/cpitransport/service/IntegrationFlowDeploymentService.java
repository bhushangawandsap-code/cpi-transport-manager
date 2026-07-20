package com.bhushan.cpitransport.service;

import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.http.HttpClientService;

import java.net.http.HttpResponse;

public class IntegrationFlowDeploymentService {

    private final HttpClientService httpClientService;
    private final ConfigurationService configurationService;

    public IntegrationFlowDeploymentService(
            HttpClientService httpClientService,
            ConfigurationService configurationService) {

        this.httpClientService = httpClientService;
        this.configurationService = configurationService;
    }

    public void deploy(
            String accessToken,
            String environment,
            String iflowId) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("Deploying Integration Flow");
        System.out.println("======================================");

        String url =
                configurationService.getHost(environment)
                        + configurationService.getApiUrl()
                        + "/DeployIntegrationDesigntimeArtifact"
                        + "?Id='" + iflowId + "'"
                        + "&Version='active'";

        System.out.println(url);

        HttpResponse<String> response =
                httpClientService.post(
                        url,
                        "",
                        accessToken
                );

        System.out.println("Status Code : " + response.statusCode());
        System.out.println(response.body());

        int status = response.statusCode();

        if (status < 200 || status >= 300) {
            throw new RuntimeException(
                    "Deployment failed for Integration Flow: "
                            + iflowId);
        }

        System.out.println("Deployment request accepted.");
    }
}
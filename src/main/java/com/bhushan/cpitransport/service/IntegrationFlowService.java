package com.bhushan.cpitransport.service;

import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.file.FileService;
import com.bhushan.cpitransport.http.HttpClientService;

import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IntegrationFlowService {

    private final HttpClientService httpClientService;
    private final ConfigurationService configurationService;
    private final FileService fileService;

    public IntegrationFlowService(HttpClientService httpClientService,
                                  ConfigurationService configurationService, FileService fileService) {

        this.httpClientService = httpClientService;
        this.configurationService = configurationService;

        this.fileService = fileService;
    }

    public void listIntegrationFlows( String accessToken,
                                      String environment) {

        System.out.println("======================================");
        System.out.println("Listing Integration Flows...");
        System.out.println("======================================");
        String url =
                configurationService.getHost(environment)
                        + configurationService.getApiUrl()
                        + "/IntegrationDesigntimeArtifacts";
        System.out.println(url);
        HttpResponse<String> response =
                httpClientService.get(url, accessToken);

        System.out.println("Status Code : " + response.statusCode());

        System.out.println("Response Body:");
        System.out.println(response.body());

    }

    public void downloadIntegrationFlow(String accessToken,
                                        String environment,
                                        String iflowId) {

        try {

            System.out.println("======================================");
            System.out.println("Downloading Integration Flow...");
            System.out.println("======================================");

            String url =
                    configurationService.getHost(environment)
                            + configurationService.getApiUrl()
                            + "/IntegrationDesigntimeArtifacts("
                            + "Id='" + iflowId + "',Version='active'"
                            + ")/$value";

            System.out.println(url);

            HttpResponse<byte[]> response =
                    httpClientService.getFile(url, accessToken);

            System.out.println("Status Code : " + response.statusCode());

            if (response.statusCode() == 200) {

                String sourceEnvironment = "DEV";

                fileService.saveZip(
                        response.body(),
                        iflowId,
                        environment.toUpperCase()
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(e);

        }


    }

    public void downloadConfiguration(String accessToken,
                                      String environment,
                                      String iflowId) {

        System.out.println("======================================");
        System.out.println("Downloading Configuration...");
        System.out.println("======================================");

        String url =
                configurationService.getHost(environment)
                        + configurationService.getApiUrl()
                        + "/IntegrationDesigntimeArtifacts("
                        + "Id='" + iflowId + "',Version='active'"
                        + ")/Configurations";

        System.out.println(url);
        HttpResponse<String> response =
                httpClientService.get(url, accessToken);

        System.out.println("Status Code : " + response.statusCode());



        fileService.saveConfiguration(
                response.body(),
                iflowId,
                environment.toUpperCase()
        );

    }
}
package com.bhushan.cpitransport.service;

import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.http.HttpClientService;
import com.bhushan.cpitransport.model.PackageMetadata;
import com.bhushan.cpitransport.model.PackageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;

public class PackageService {

    private final HttpClientService httpClientService;
    private final ConfigurationService configurationService;

    public PackageService(
            HttpClientService httpClientService,
            ConfigurationService configurationService) {

        this.httpClientService = httpClientService;
        this.configurationService = configurationService;
    }

    public boolean packageExists(
            String accessToken,
            String environment,
            String packageId) {

        System.out.println("======================================");
        System.out.println("Checking Package...");
        System.out.println("======================================");

        String url =
                configurationService.getHost(environment)
                        + configurationService.getApiUrl()
                        + "/IntegrationPackages('"
                        + packageId
                        + "')";

        System.out.println(url);

        HttpResponse<String> response =
                httpClientService.get(url, accessToken);

        System.out.println("Status Code : " + response.statusCode());
        System.out.println(response.body());

        if (response.statusCode() == 200) {
            return true;
        }

        if (response.statusCode() == 404) {
            return false;
        }

        throw new RuntimeException(
                "Unexpected Status Code : "
                        + response.statusCode()
        );
    }

    public void createPackage(
            String accessToken,
            String environment,
            PackageMetadata packageMetadata) {

        try {

            System.out.println("======================================");
            System.out.println("Creating Package...");
            System.out.println("======================================");

            String url =
                    configurationService.getHost(environment)
                            + configurationService.getApiUrl()
                            + "/IntegrationPackages";

            PackageRequest request = new PackageRequest();

            request.setId(packageMetadata.getId());
            request.setName(packageMetadata.getName());
            System.out.println("PackageMetaData "+packageMetadata.getShortText());
            request.setShortText(packageMetadata.getShortText());

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
                        "Failed to create package. Status Code : "
                                + response.statusCode()
                );
            }

            System.out.println("Package created successfully.");

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}
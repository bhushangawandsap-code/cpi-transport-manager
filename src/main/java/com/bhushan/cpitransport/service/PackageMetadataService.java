package com.bhushan.cpitransport.service;

import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.http.HttpClientService;
import com.bhushan.cpitransport.model.PackageMetadata;
import com.bhushan.cpitransport.model.PackageMetadataResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;

public class PackageMetadataService {

    private final HttpClientService httpClientService;
    private final ConfigurationService configurationService;

    public PackageMetadataService(
            HttpClientService httpClientService,
            ConfigurationService configurationService) {

        this.httpClientService = httpClientService;
        this.configurationService = configurationService;
    }

    public PackageMetadata getPackageMetadata(
            String accessToken,
            String environment,
            String packageId) {

        System.out.println("======================================");
        System.out.println("Getting Package Metadata...");
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

        if (response.statusCode() == 404) {

            System.out.println("Package not found.");

            return null;

        }

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                    "Failed to retrieve Package Metadata. Status Code : "
                            + response.statusCode()
            );

        }

        try {

            ObjectMapper mapper = new ObjectMapper();

            PackageMetadataResponse packageMetadataResponse =
                    mapper.readValue(
                            response.body(),
                            PackageMetadataResponse.class
                    );

            if (packageMetadataResponse == null) {
                throw new RuntimeException("PackageMetadataResponse is null");
            }

            PackageMetadata metadata =
                    packageMetadataResponse.getMetadata();

            if (metadata == null) {
                throw new RuntimeException("PackageMetadata is null");
            }

            System.out.println("======================================");
            System.out.println("Package Metadata");
            System.out.println("======================================");
            System.out.println("ID         : " + metadata.getId());
            System.out.println("NAME       : " + metadata.getName());
            System.out.println("SHORT TEXT : " + metadata.getShortText());

            return metadata;

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}
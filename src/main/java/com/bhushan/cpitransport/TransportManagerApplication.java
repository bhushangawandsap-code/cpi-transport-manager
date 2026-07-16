package com.bhushan.cpitransport;

import com.bhushan.cpitransport.auth.AuthenticationService;
import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.file.FileService;
import com.bhushan.cpitransport.http.HttpClientService;
import com.bhushan.cpitransport.model.IntegrationFlowMetadata;
import com.bhushan.cpitransport.model.PackageMetadata;
import com.bhushan.cpitransport.service.*;
import com.bhushan.cpitransport.util.Base64Util;

import java.nio.file.Path;

public class TransportManagerApplication {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("      CPI Transport Manager");
        System.out.println("======================================");

        // ----------------------------------------------------
        // Test Data
        // ----------------------------------------------------
        String iflowId = "Test_Iflow";
        String sourceEnvironment = "dev";
        String targetEnvironment = "qa";

        // ----------------------------------------------------
        // Services
        // ----------------------------------------------------
        ConfigurationService configurationService = new ConfigurationService();

        HttpClientService httpClientService = new HttpClientService();

        FileService fileService = new FileService();

        AuthenticationService authenticationService =
                new AuthenticationService(
                        configurationService,
                        httpClientService
                );

        IntegrationFlowService integrationFlowService =
                new IntegrationFlowService(
                        httpClientService,
                        configurationService,
                        fileService
                );

        IntegrationFlowMetadataService metadataService =
                new IntegrationFlowMetadataService(
                        httpClientService,
                        configurationService
                );

        PackageService packageService =
                new PackageService(
                        httpClientService,
                        configurationService
                );

        PackageMetadataService packageMetadataService =
                new PackageMetadataService(
                        httpClientService,
                        configurationService
                );

        IntegrationFlowUploadService uploadService =
                new IntegrationFlowUploadService(
                        httpClientService,
                        configurationService
                );



        Base64Util base64Util = new Base64Util();

        // ----------------------------------------------------
        // Authenticate
        // ----------------------------------------------------
        String sourceAccessToken =
                authenticationService.generateToken(sourceEnvironment);

        String targetAccessToken =
                authenticationService.generateToken(targetEnvironment);


        // ----------------------------------------------------
        // STEP 1
        // Read iFlow Metadata from DEV
        // ----------------------------------------------------
        IntegrationFlowMetadata metadata =
                metadataService.getMetadata(
                        sourceAccessToken,
                        sourceEnvironment,
                        iflowId
                );

        // ----------------------------------------------------
        // STEP 2
        // Download iFlow ZIP
        // ----------------------------------------------------
        integrationFlowService.downloadIntegrationFlow(
                sourceAccessToken,
                sourceEnvironment,
                iflowId
        );

        // ----------------------------------------------------
        // STEP 3
        // Download Configuration
        // ----------------------------------------------------
        integrationFlowService.downloadConfiguration(
                sourceAccessToken,
                sourceEnvironment,
                iflowId
        );

        // ----------------------------------------------------
        // STEP 4
        // Convert ZIP to Base64
        // ----------------------------------------------------
        String base64 =
                base64Util.encodeFile(
                        Path.of(
                                "downloads",
                                "DEV",
                                "iflows",
                                iflowId + ".zip"
                        )
                );

        System.out.println("Base64 Generated Successfully.");

        // ----------------------------------------------------
        // STEP 5
        // Check Package in QA
        // ----------------------------------------------------
        boolean packageExists =
                packageService.packageExists(
                        targetAccessToken,
                        targetEnvironment,
                        metadata.getPackageId()
                );

        if (!packageExists) {

            System.out.println("Package not found in QA.");

            PackageMetadata packageMetadata =
                    packageMetadataService.getPackageMetadata(
                            sourceAccessToken,
                            sourceEnvironment,
                            metadata.getPackageId()
                    );
            if (packageMetadata == null) {
                System.out.println("PackageMetadata is NULL");
            } else {
                System.out.println("PackageMetadata is NOT NULL");
                System.out.println(packageMetadata.getId());
                System.out.println(packageMetadata.getName());
            }

            packageService.createPackage(
                    targetAccessToken,
                    targetEnvironment,
                    packageMetadata
            );

        } else {

            System.out.println("Package already exists in QA.");

        }

        // ----------------------------------------------------
        // STEP 6
        // Check iFlow in QA
        // ----------------------------------------------------
        IntegrationFlowMetadata targetMetadata =
                metadataService.getMetadata(
                        targetAccessToken,
                        targetEnvironment,
                        iflowId
                );

        if (targetMetadata == null) {

            System.out.println("======================================");
            System.out.println("Creating Integration Flow...");
            System.out.println("======================================");

            uploadService.createIntegrationFlow(
                    targetAccessToken,
                    targetEnvironment,
                    metadata,
                    base64
            );

        } else {

            System.out.println("======================================");
            System.out.println("Updating Integration Flow...");
            System.out.println("======================================");

            uploadService.updateIntegrationFlow(
                    targetAccessToken,
                    targetEnvironment,
                    metadata,
                    base64
            );

        }

        // ----------------------------------------------------
        // STEP 7
        // Update Externalized Parameters
        // ----------------------------------------------------
        // TODO

        // ----------------------------------------------------
        // STEP 8
        // Deploy iFlow
        // ----------------------------------------------------
        // TODO

        System.out.println();
        System.out.println("Transport Completed.");

    }
}
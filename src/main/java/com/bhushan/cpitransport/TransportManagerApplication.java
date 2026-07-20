package com.bhushan.cpitransport;

import com.bhushan.cpitransport.auth.AuthenticationService;
import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.configuration.ConfigurationMergeService;
import com.bhushan.cpitransport.configuration.ConfigurationSynchronizationService;
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

// ---------- LOCAL TESTING ----------

// String operation = "transport";          // transport | sync-config
// String iflowId = "Test_Iflow";
// String sourceEnvironment = "dev";
// String targetEnvironment = "qa";
// boolean updateConfiguration = true;
// boolean deployAfterTransport = true;


// ---------- GITHUB ACTIONS ----------

        String operation =
                System.getProperty("operation", "transport");

        String iflowId =
                System.getProperty("iflow", "Test_Iflow");

        String sourceEnvironment =
                System.getProperty("source", "dev");

        String targetEnvironment =
                System.getProperty("target", "qa");

        boolean updateConfiguration =
                Boolean.parseBoolean(
                        System.getProperty(
                                "updateConfiguration",
                                "true"));

        boolean deployAfterTransport =
                Boolean.parseBoolean(
                        System.getProperty(
                                "deploy",
                                "false"));

        System.out.println();
        System.out.println("Operation            : " + operation);
        System.out.println("Integration Flow     : " + iflowId);
        System.out.println("Source Environment   : " + sourceEnvironment);
        System.out.println("Target Environment   : " + targetEnvironment);
        System.out.println("Update Configuration : " + updateConfiguration);
        System.out.println("Deploy               : " + deployAfterTransport);
        System.out.println();
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
 //comment
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

        IntegrationFlowConfigurationService configurationUpdateService =
                new IntegrationFlowConfigurationService(
                        httpClientService,
                        configurationService,
                        fileService
                );



        Base64Util base64Util = new Base64Util();

        // ----------------------------------------------------
        // Authenticate
        // ----------------------------------------------------
        String sourceAccessToken =
                authenticationService.generateToken(sourceEnvironment);

        String targetAccessToken =
                authenticationService.generateToken(targetEnvironment);

        // CONFIGURATION SYNCHRONIZATION
        // ====================================================

        if ("sync-config".equalsIgnoreCase(operation)) {

            ConfigurationMergeService mergeService =
                    new ConfigurationMergeService();

            ConfigurationSynchronizationService
                    synchronizationService =
                    new ConfigurationSynchronizationService(
                            httpClientService,
                            configurationService,
                            fileService,
                            mergeService
                    );

            synchronizationService.synchronize(
                    sourceAccessToken,
                    sourceEnvironment,
                    iflowId
            );

            System.out.println();
            System.out.println("Configuration Synchronization Completed.");

            return;

        }
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

        IntegrationFlowDeploymentService deploymentService =
                new IntegrationFlowDeploymentService(
                        httpClientService,
                        configurationService);

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
        // STEP 7
// Update Externalized Parameters
// ----------------------------------------------------
        if (updateConfiguration) {

            configurationUpdateService.updateConfiguration(
                    targetAccessToken,
                    targetEnvironment,
                    iflowId
            );

        } else {

            System.out.println();
            System.out.println("Externalized parameter update skipped.");

        }

        // ----------------------------------------------------
        // STEP 8
        // Deploy iFlow
        if (deployAfterTransport) {

            deploymentService.deploy(
                    targetAccessToken,
                    targetEnvironment,
                    iflowId
            );

        } else {

            System.out.println();
            System.out.println("Deployment skipped.");

        }
        // ----------------------------------------------------
        // TODO

        System.out.println();
        System.out.println("Transport Completed.");

    }
}
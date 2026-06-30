package com.bhushan.cpitransport;

import com.bhushan.cpitransport.auth.AuthenticationService;
import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.http.HttpClientService;
import com.bhushan.cpitransport.service.IntegrationFlowService;
import com.bhushan.cpitransport.file.FileService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class TransportManagerApplication {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("======================================");
        System.out.println("      CPI Transport Manager");
        System.out.println("======================================");
        System.out.println();
        System.out.println("Application Started...");
        ConfigurationService configurationService = new ConfigurationService();

        System.out.println(configurationService.getHost());

        HttpClientService httpClientService = new HttpClientService();

        AuthenticationService authenticationService = new AuthenticationService(configurationService,httpClientService);

        FileService fileService = new FileService();

        IntegrationFlowService integrationFlowService =
                new IntegrationFlowService(httpClientService, configurationService,fileService);
        String accessToken = authenticationService.generateToken();

        System.out.println();
        System.out.println("Token received successfully!");
        System.out.println(accessToken);

        integrationFlowService.listIntegrationFlows(accessToken);
        integrationFlowService.downloadIntegrationFlow(
                accessToken,
                "Test_iflow"
        );
        integrationFlowService.downloadConfiguration(
                accessToken,
                "Test_iflow"
        );



    }
}
package com.bhushan.cpitransport.auth;

import com.bhushan.cpitransport.config.ConfigurationService;
import com.bhushan.cpitransport.http.HttpClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bhushan.cpitransport.model.OAuthResponse;

import java.net.http.HttpResponse;

public class AuthenticationService {
    private final ConfigurationService configurationService;
    private final HttpClientService httpClientService;
    public AuthenticationService(ConfigurationService configurationService, HttpClientService httpClientService) {
        this.configurationService = configurationService;
        this.httpClientService = httpClientService;
    }

    public String generateToken(String environment) {

        String requestBody =
                "grant_type=client_credentials"
                        + "&client_id=" + configurationService.getClientId(environment)
                        + "&client_secret=" + configurationService.getClientSecret(environment);

        HttpResponse<String> response =
                httpClientService.post(
                        configurationService.getTokenUrl(environment),
                        requestBody
                );

        System.out.println("Status Code : " + response.statusCode());

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            OAuthResponse oauthResponse =
                    objectMapper.readValue(
                            response.body(),
                            OAuthResponse.class
                    );

            return oauthResponse.getAccessToken();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }


    }

}

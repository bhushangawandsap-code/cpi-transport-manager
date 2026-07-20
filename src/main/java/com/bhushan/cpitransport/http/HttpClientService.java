package com.bhushan.cpitransport.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientService {

    private final HttpClient httpClient;

    public HttpClientService() {

        System.out.println("Initializing HTTP Client...");

        this.httpClient = HttpClient.newHttpClient();

    }

    public HttpResponse<String> get(
            String url,
            String accessToken) {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public HttpResponse<byte[]> getFile(
            String url,
            String accessToken) {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray());

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public HttpResponse<String> post(
            String url,
            String body,
            String accessToken) {

        return send(
                "POST",
                url,
                body,
                accessToken
        );

    }

    public HttpResponse<String> put(
            String url,
            String body,
            String accessToken) {

        return send(
                "PUT",
                url,
                body,
                accessToken
        );

    }

    private HttpResponse<String> send(
            String method,
            String url,
            String body,
            String accessToken) {

        try {

            HttpRequest.Builder builder =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Authorization",
                                    "Bearer " + accessToken)
                            .header("Content-Type",
                                    "application/json")
                            .header("Accept",
                                    "application/json");

            if ("POST".equals(method)) {

                builder.POST(
                        HttpRequest.BodyPublishers.ofString(body));

            } else {

                builder.PUT(
                        HttpRequest.BodyPublishers.ofString(body));

            }

            return httpClient.send(
                    builder.build(),
                    HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public HttpResponse<String> post(
            String url,
            String body) {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}
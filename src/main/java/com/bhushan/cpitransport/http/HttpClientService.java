package com.bhushan.cpitransport.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientService {

    private final HttpClient httpClient;

    public HttpClientService() {

        System.out.println("Initializing HTTP Client...");

        httpClient = HttpClient.newHttpClient();

    }
    public void testConnection() {

        System.out.println("Testing HTTP Client...");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.google.com"))
                .GET()
                .build();


        System.out.println(request);
        try {

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response received!");
            System.out.println(response.statusCode());

        } catch (Exception e) {

            throw new RuntimeException(e);

        }



    }
    public HttpResponse<String> post(String url, String body) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response;

        } catch (Exception e) {

            throw new RuntimeException(e);

        }




    }
    public HttpResponse<String> get(String url, String accessToken) {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }
    public HttpResponse<byte[]> getFile(String url, String accessToken) {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }
    public HttpResponse<String> post(
            String url,
            String body,
            String accessToken) {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public HttpResponse<String> put(
            String url,
            String body,
            String accessToken) {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }


}
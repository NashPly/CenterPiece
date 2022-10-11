package com.CenterPiece.APICalls;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TrelloCalls {

    private final String baseUrl = "https://api.trello.com/1/";
    private final HttpClient client;
    private final String urlEndpoint;
    private String parameters;
    private final String key = "90fb4c3f6615067b94535f130c0d7b4f";
    private final String token = "c95f8154db55a4f2297c9ab6d431b1d3d5dfcac19bc3bafb3bce4b35ab9fcf31";

    public TrelloCalls(HttpClient client, String trelloUrlEndPoint, String parameters){
        this.client = client;
        this.urlEndpoint = trelloUrlEndPoint;
        this.parameters = parameters;
    }

    public TrelloCalls(HttpClient client, String trelloUrlEndPoint){
        this.client = client;
        this.urlEndpoint = trelloUrlEndPoint;
    }

    //TODO under construction
    public JSONObject getTrelloAPICall() throws IOException, InterruptedException {

        String uri = String.format("%s%s?%s&key=%s&token=%s", this.baseUrl, this.urlEndpoint, this.parameters, this.key, this.token);

        var request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .GET()
                .build();

        var response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("- GET Call to Trello -");
        System.out.println(response);
        System.out.println(response.body());

        return new JSONObject(response.body());
    }

    public JSONObject postTrelloAPICall() throws IOException, InterruptedException {

        String uri = String.format("%s%s?%s&key=%s&token=%s", this.baseUrl, this.urlEndpoint, this.parameters, this.key, this.token);

        var request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("- POST Call to Trello -");
        System.out.println(response);
        System.out.println(response.body());
        return new JSONObject(response.body());
    }

    public JSONObject putTrelloAPICall(JSONObject innerRequestBody) throws IOException, InterruptedException {

        String uri = String.format("%s%s?%s&key=%s&token=%s",
                this.baseUrl, this.urlEndpoint, this.parameters, this.key, this.token);

        var request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .PUT(buildRequest(innerRequestBody))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("- PUT Call to Trello -");
        System.out.println(response);
        System.out.println(response.body());

        return new JSONObject(response.body());
    }

    public JSONObject deleteTrelloAPICall(String cardId) throws IOException, InterruptedException {

        String uri = String.format("%s%s%s?&key=%s&token=%s",
                this.baseUrl, this.urlEndpoint, cardId, this.key, this.token);

        System.out.println(uri);

        var request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("- Delete Call to Trello -");
        System.out.println(response);
        System.out.println(response.body());

        return new JSONObject(response.body());
    }

    public HttpRequest.BodyPublisher buildRequest(JSONObject innerRequest){
        JSONObject requestBody = new JSONObject();
        requestBody.put("value", innerRequest);
        return HttpRequest.BodyPublishers.ofString(requestBody.toString());
    }
}

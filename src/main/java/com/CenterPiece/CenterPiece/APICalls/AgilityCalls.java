package com.CenterPiece.CenterPiece.APICalls;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AgilityCalls {

    private final HttpClient client;
    private final String contextId;
    private JSONObject requestBody = new JSONObject();
    private String stringRequestBody = "";
    private final String urlEndpoint;
    private String branch;


    public AgilityCalls(HttpClient cl, String cId, String ue, JSONObject bod, String branch){
        client = cl;
        contextId = cId;
        requestBody = bod;
        urlEndpoint = ue;
        this.branch = branch;
    }

    public AgilityCalls(HttpClient cl, String cId, String ue, String bod, String branch){
        client = cl;
        contextId = cId;
        stringRequestBody = bod;
        urlEndpoint = ue;
        this.branch = branch;
    }

    public JSONObject postAgilityAPICall() {

        System.out.println("- POST Call to Agility -");
        String url = "https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/";

        System.out.println("URL -- "+ url + urlEndpoint);
        System.out.println("requestBody -- "+ this.requestBody);

        var request = HttpRequest.newBuilder(
                URI.create(url + urlEndpoint))
                .header("accept", "application/json")
                .header("ContextId", this.contextId)
                .header("Branch", this.branch)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .POST(buildRequest())
                .build();

        APICaller apiCaller = new APICaller(client, request);

        return new JSONObject(apiCaller.makeAPICall().body());
    }



    public HttpRequest.BodyPublisher buildRequest(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("request", this.requestBody);
        System.out.println("\n\n--- HTTP Full Request Body ---");
        System.out.println(requestBody + "\n");
        return HttpRequest.BodyPublishers.ofString(requestBody.toString());
    }

    public JSONObject postAgilityAPICallStringBody() {

        System.out.println("- POST Call to Agility -");
        String url = "https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/";

        System.out.println("URL -- "+ url + urlEndpoint);
        System.out.println("requestBody -- "+ this.requestBody);

        var request = HttpRequest.newBuilder(
                URI.create(url + urlEndpoint))
                .header("accept", "application/json")
                .header("ContextId", this.contextId)
                .header("Branch", this.branch)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .POST(buildRequestStringBody())
                .build();

        APICaller apiCaller = new APICaller(client, request);

        return new JSONObject(apiCaller.makeAPICall().body());
    }

    public HttpRequest.BodyPublisher buildRequestStringBody(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("request", this.stringRequestBody);
        System.out.println("\n\n--- HTTP Full Request Body ---");
        System.out.println(requestBody + "\n");
        return HttpRequest.BodyPublishers.ofString(requestBody.toString());
    }
}

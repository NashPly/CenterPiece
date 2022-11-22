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
    private final JSONObject requestBody;
    private final String urlEndpoint;
    private String branch;


    public AgilityCalls(HttpClient cl, String cId, String ue, JSONObject bod, String branch){
        client = cl;
        contextId = cId;
        requestBody = bod;
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
                .POST(buildRequest())
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(response);
        System.out.println(response.body());

        return new JSONObject(response.body());
    }

    public HttpRequest.BodyPublisher buildRequest(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("request", this.requestBody);
        return HttpRequest.BodyPublishers.ofString(requestBody.toString());
    }
}

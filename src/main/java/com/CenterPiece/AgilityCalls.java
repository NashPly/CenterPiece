package com.CenterPiece;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class AgilityAPICall {

    private String url = "";
    private HttpClient client;
    private String contextId;
    private JSONObject requestBody;
    private String urlEndpoint;


    AgilityAPICall(HttpClient cl, String cId, String ue, JSONObject bod){
        url = "https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/";
        client = cl;
        contextId = cId;
        requestBody = bod;
        urlEndpoint = ue;
    }

    //public static JSONObject postAgilityAPICall(HttpClient client, String contextId, String urlEndpoint, HttpRequest.BodyPublisher requestBody) throws IOException, InterruptedException {

    public JSONObject postAgilityAPICall() throws IOException, InterruptedException {

            var request = HttpRequest.newBuilder(
                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/" + urlEndpoint))
                .header("accept", "application/json")
                .header("ContextId", this.contextId)
                .header("Branch", "CABINETS")
                .POST(buildRequest())
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject responseBody = new JSONObject(response.body());
        return responseBody;
    }

    public HttpRequest.BodyPublisher buildRequest(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("request", this.requestBody);
        return HttpRequest.BodyPublishers.ofString(requestBody.toString());

    }
}

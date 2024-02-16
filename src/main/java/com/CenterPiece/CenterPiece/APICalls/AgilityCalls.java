package com.CenterPiece.CenterPiece.APICalls;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class AgilityCalls {

    private final HttpClient client;
    public AgilityCalls(HttpClient cl){
        this.client = cl;
    }
    public JSONObject postAgilityAPICall(String urlEndpoint, JSONObject innerRequestBody, String contextId, String branch) {
        String url = buildUrl(urlEndpoint);
        System.out.println("URL -- " + url);
        System.out.println("requestBody -- " + innerRequestBody);
        System.out.println("contextId -- " + contextId);

        HttpRequest request = buildRequest(url, innerRequestBody, contextId, branch);
        APICaller apiCaller = new APICaller(this.client, request);
        return new JSONObject(apiCaller.makeAPICall().body());
    }

    private String buildUrl(String urlEndpoint) {
        return "https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/" + urlEndpoint;
    }

    private HttpRequest buildRequest(String url, JSONObject innerRequestBody, String contextId, String branch) {
        return HttpRequest.newBuilder(URI.create(url))
                .header("accept", "*/*")
                .header("Content-Type", "application/json")
                .header("ContextId", contextId)
                .header("Branch", branch)
                .header("Accept", "*/*")
                .POST(buildRequestBody(innerRequestBody))
                .build();
    }
    private static HttpRequest.BodyPublisher buildRequestBody(JSONObject innerRequestBody){
        JSONObject requestBody = new JSONObject();
        requestBody.put("request", innerRequestBody);
        System.out.println("\n\n--- HTTP Full Request Body ---");
        System.out.println(requestBody + "\n");
        return HttpRequest.BodyPublishers.ofString(requestBody.toString());
    }
}

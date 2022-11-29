package com.CenterPiece.CenterPiece.APICalls;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APICaller {

    private HttpClient client;
    private HttpRequest request;

    APICaller(HttpClient client, HttpRequest request){
        this.client = client;
        this.request = request;
    }

    public HttpResponse<String> makeAPICall(){
        HttpResponse<String> response = null;
        try {
            response = this.client.send(this.request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(response);
        System.out.println(response.body());

        if(response.statusCode() != 200){
            System.exit(-1);
        }
        return response;
    }
}

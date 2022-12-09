package com.CenterPiece.CenterPiece.APICalls;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class APICaller {

    private HttpClient client;
    private HttpRequest request;

    APICaller(HttpClient client, HttpRequest request){
        this.client = client;
        this.request = request;
    }

    public HttpResponse<String> makeAPICall(){
        HttpResponse<String> response = null;
        int i = 0;
        while (response == null && i < 10) {

            if(i!=0){

                try {
                    System.out.println("\nCall failed. Trying again in 10 seconds...");
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                response = this.client.send(this.request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("IO Exception created while making this call: \n" + this.request);
                System.out.println(e + "\n");
                System.out.println("Trying again");
                response = null;

            } catch (InterruptedException e) {
                //e.printStackTrace();
                System.out.println("Interrupted Exception created while making this call: \n" + this.request);
                System.out.println(e + "\n");
                System.out.println("Trying again");
                response = null;
            }



            if(response != null){
                System.out.println(response);
                System.out.println(response.body());

                if(response.statusCode() != 200){
                    //System.exit(-1);
                    response = null;

                }
            }

            i++;
        }

        if (response == null){
            System.out.println("CenterPiece tried 10 times to make this call unsuccessfully. Something is wrong");
            System.exit(-1);
        }

        return response;
    }
}

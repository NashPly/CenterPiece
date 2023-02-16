package com.CenterPiece.CenterPiece.APICalls;

import com.CenterPiece.CenterPiece.Objects.ShipToAddress;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class TomTomCalls {

    private ShipToAddress shipToAddress;
    private HttpClient client;
    private String latitude;
    private String longitude;
    private String responseAddress;


    public TomTomCalls(ShipToAddress shipToAddress, HttpClient httpClient){
        this.shipToAddress = shipToAddress;
        this.client = httpClient;

        JSONObject jsonObject = getAddressCall();

        parseTomTomResponse(jsonObject);

    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getResponseAddress() {
        return responseAddress;
    }

    public JSONObject getAddressCall(){

        String url = "https://api.tomtom.com/search/2/structuredGeocode.json?" +
                "countryCode=US&streetNumber="+ this.shipToAddress.getStreetNumber()+
                "&streetName="+this.shipToAddress.getStreetName()+"" +
                "&municipality="+ this.shipToAddress.getCity() +
                "&countrySubdivision="+ this.shipToAddress.getState()+
                "&postalCode="+ this.shipToAddress.getZipCode()+
                "&language=en-US&view=Unified&key=N1F5XkQ57nR0GBKSMryIlJpVIMqzrUo8";

        /*
        String url = "https://api.tomtom.com/search/2/structuredGeocode.json?" +
                "countryCode=US&streetNumber="+urlify(this.shipToAddress.getStreetNumber())+
                "&streetName="+urlify(this.shipToAddress.getStreetName())+"" +
                "&municipality="+ urlify(this.shipToAddress.getCity()) +
                "&countrySubdivision="+ urlify(this.shipToAddress.getState())+
                "&postalCode="+ urlify(this.shipToAddress.getZipCode())+
                "&language=en-US&view=Unified&key=k8rhqHmA2Hj3pF4FDXHLRBGQDbh86egZ";
         */

        var request = HttpRequest.newBuilder(
                URI.create(urlify(url)))
                .header("accept", "*/*")
                .header("accept_encoding", "gzip,deflate,br")
                .GET()
                .build();

        APICaller apiCaller = new APICaller(client, request);



        return new JSONObject(apiCaller.makeAPICall().body());
    }

    private void parseTomTomResponse(JSONObject jsonObject){

        if(jsonObject.has("results")) {
            //var hold = jsonObject.getJSONArray("results").getJSONObject(0);
            this.responseAddress = jsonObject.getJSONArray("results")
                    .getJSONObject(0).getJSONObject("address")
                    .getString("freeformAddress");

            System.out.println();
            this.latitude = jsonObject.getJSONArray("results")
                    .getJSONObject(0).getJSONObject("position")
                    .getNumber("lat").toString();
            this.longitude = jsonObject.getJSONArray("results")
                    .getJSONObject(0).getJSONObject("position")
                    .getNumber("lon").toString();
        }
    }

    //Clean for URL
    private String urlify(String string){

        string = string.replace(" ", "%20");
        string = string.replace(",", "%2C");
        string = string.replace("#", "%23");
        string = string.replace("@", "%40");
        string = string.replace("*", "%2A");
        string = string.replace("'", "%27");
        string = string.replace("\"", "%22");

        return string;
    }

}

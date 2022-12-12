package com.CenterPiece.CenterPiece.Core;

import com.CenterPiece.CenterPiece.APICalls.APICaller;
import com.CenterPiece.CenterPiece.Core.CenterPieceFunctions;
import com.CenterPiece.CenterPiece.ItemCodeHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Calendar;
import java.util.List;

public class CenterPieceSession {

    private final String branch;
    private String contextID;
    private final HttpClient client;

    public CenterPieceSession(String branch, HttpClient client) {
        this.branch = branch;
        this.client = client;
    }

    public void mainProcess(){

        Calendar.getInstance();

        CenterPieceFunctions centerPieceFunctions;

        this.contextID = login();
        System.out.println("\n-- Login - "+ this.branch +" --");
        centerPieceFunctions = new CenterPieceFunctions(this.client, this.contextID, this.branch);

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(client, this.contextID, this.branch);

        JSONArray currentSalesOrders = itemCodeHandler.agilitySalesOrderListLookup();

        List<String> currentSalesOrderNumbers = centerPieceFunctions.salesOrderParser(currentSalesOrders);
        List<Integer> toBeCreatedSOs;

        toBeCreatedSOs = centerPieceFunctions.tallySOsToBeCreated(currentSalesOrderNumbers.size(), currentSalesOrderNumbers);


        //Test using the first to be created
        for (Integer toBeCreatedSO : toBeCreatedSOs) {
            centerPieceFunctions.createTrelloCard(currentSalesOrders.getJSONObject(toBeCreatedSO));
        }

        //Update info

            centerPieceFunctions.updateTrelloCards();

            this.logout();
            System.out.println("\n-- Logout - "+ this.branch+" --");

    }

    public String login(){
        JSONObject innerRequestBody = new JSONObject();
        innerRequestBody.put("LoginID","tbeals");
        innerRequestBody.put("Password","123");

        JSONObject requestBody = new JSONObject();
        requestBody.put("request", innerRequestBody);

        var request = HttpRequest.newBuilder(
                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/Session/Login"))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();


        APICaller apiCaller = new APICaller(client, request);

        //System.out.println(apiCaller.makeAPICall().body());

        JSONObject json = new JSONObject(apiCaller.makeAPICall().body());

        System.out.println(json.getJSONObject("response"));
        
        return json.getJSONObject("response").getString("SessionContextId");

//        HttpResponse<String> response = null;
//        try {
//            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        assert response != null;
//        JSONObject json = new JSONObject(response.body());
//
//        return json.getJSONObject("response").getString("SessionContextId");
    }

    public void logout(){
        JSONObject innerRequestBody = new JSONObject();
        innerRequestBody.put("LoginID","tbeals");
        innerRequestBody.put("Password","123");

        JSONObject requestBody = new JSONObject();
        requestBody.put("request", innerRequestBody);

        var request = HttpRequest.newBuilder(
                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/Session/Logout"))
                .header("accept", "application/json")
                .header("accept", "application/json")
                .header("ContextId", this.contextID)
                .header("Branch", "FABRICATION")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        APICaller apiCaller = new APICaller(client, request);

        apiCaller.makeAPICall();

//        try {
//            client.send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}



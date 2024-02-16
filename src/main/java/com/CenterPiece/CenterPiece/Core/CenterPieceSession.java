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
    private final String environment;
    private String contextID;
    private final HttpClient client;


    public CenterPieceSession(String branch, HttpClient client, String environment) {
        this.branch = branch;
        this.client = client;
        this.environment = environment;
    }

    public void mainProcess(){

        Calendar.getInstance();

        CenterPieceFunctions centerPieceFunctions;

        this.contextID = login(this.client);
        System.out.println("\n-- Login - "+ this.branch +" --");
        centerPieceFunctions = new CenterPieceFunctions(client, this.contextID, this.branch, this.environment);

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(client, this.contextID, this.branch, this.environment);

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
            System.out.println("\n-- Logout - " + this.environment + " " + this.branch+" --");

    }

    public static String login(HttpClient client){
        JSONObject innerRequestBody = new JSONObject();
        innerRequestBody.put("LoginID","tbeals");
        innerRequestBody.put("Password","5668");

        JSONObject requestBody = new JSONObject();
        requestBody.put("request", innerRequestBody);

        var request = HttpRequest.newBuilder(
                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/Session/Login"))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();


        APICaller apiCaller = new APICaller(client, request);

        JSONObject json = new JSONObject(apiCaller.makeAPICall().body());

        System.out.println(json.getJSONObject("response"));
        
        return json.getJSONObject("response").getString("SessionContextId");

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
    }

    public String getBranch() {
        return branch;
    }

    public String getContextID(){
        return contextID;
    }

    public String getEnvironment(){
        return environment;
    }

    public void setContextID(String cID){
        this.contextID = cID;
    }

}



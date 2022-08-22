package com.CenterPiece;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.CenterPiece.APICalls.*;
import org.json.*;


public class CenterPiece {

    public static void main(String[] args) {

        var client = HttpClient.newBuilder().build();
        ScheduledExecutorService session = Executors.newSingleThreadScheduledExecutor();
        mainProcess(client,session);
    }

    private static void mainProcess(HttpClient client, ScheduledExecutorService session){

        Calendar.getInstance();

        session.scheduleAtFixedRate(() -> {

            Calendar.getInstance();

            String contextId = null;
            try {

                contextId = login(client);
                System.out.println("\n-- Login --");

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            JSONArray currentSalesOrders = new JSONArray();
            try {

                ItemCodeHandler itemCodeHandler = new ItemCodeHandler(client, contextId);
                currentSalesOrders = itemCodeHandler.agilitySalesOrderListLookup();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            List<String> currentSalesOrderNumbers = salesOrderParser(currentSalesOrders);
            List<Integer> toBeCreatedSOs = new ArrayList<>();

            try {

                toBeCreatedSOs = tallySOsToBeCreated(client, currentSalesOrderNumbers.size(), currentSalesOrderNumbers);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            //Test using the first to be created
            for (Integer toBeCreatedSO : toBeCreatedSOs) {
                try {
                    createTrelloCard(client, contextId, currentSalesOrders.getJSONObject(toBeCreatedSO));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Update info

            try {
                updateTrelloCards(client,contextId);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            try {
                logout(client, contextId);
                System.out.println("\n-- Logout --");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }, 0, 90, TimeUnit.SECONDS);
    }
    //minutesToNextHour(calendar)

    private static long minutesToNextHour() {
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
        int minutesToNextHour = 60 - minutes;
        System.out.println(minutesToNextHour);
        return minutesToNextHour;
    }

    public static String login(HttpClient client) throws IOException, InterruptedException {
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

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());

        return json.getJSONObject("response").getString("SessionContextId");
    }

    public static void logout(HttpClient client, String contextId) throws IOException, InterruptedException {
        JSONObject innerRequestBody = new JSONObject();
        innerRequestBody.put("LoginID","tbeals");
        innerRequestBody.put("Password","123");

        JSONObject requestBody = new JSONObject();
        requestBody.put("request", innerRequestBody);

        var request = HttpRequest.newBuilder(
                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/Session/Logout"))
                .header("accept", "application/json")
                .header("accept", "application/json")
                .header("ContextId", contextId)
                .header("Branch", "CABINETS")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static List<String> salesOrderParser(JSONArray jsonArray){

        List<String> soList = new ArrayList<>();
        for(int i = 0; i <  jsonArray.length(); i++){
            soList.add(jsonArray.getJSONObject(i).get("OrderID").toString());
        }
        return soList;
    }

    public static JSONObject checkTrelloForSO(HttpClient client, String soNum) throws IOException, InterruptedException {

        String modelTypes = "cards";
        String card_fields = "name,closed,desc,idList";

        TrelloCalls trelloAPICall = new TrelloCalls(client, "search", String.format("query=%s&modelTypes=%s&card_fields=%s",
                soNum, modelTypes, card_fields));

        System.out.println("\n-- Check Trello For SO --");
        var response = trelloAPICall.getTrelloAPICall();
        System.out.println(response);


        JSONArray cards = response.getJSONArray("cards");

        for (int i = 0; i < cards.length(); i++){
            if(!cards.getJSONObject(i).getBoolean("closed")) {
//                System.out.println("\n-- Check Trello For SO --");
//                System.out.println(cards.getJSONObject(i));
                return cards.getJSONObject(i);
            }
        }

        JSONObject json = new JSONObject();

        json.put("id","Empty");
        return json;
    }

    public static List<Integer> tallySOsToBeCreated(HttpClient client, int size, List<String> currentList) throws IOException, InterruptedException {

        List<Integer> tally = new ArrayList<>();
        for (int i = 0; i< size; i++){
            if(checkTrelloForSO(client, currentList.get(i)).has("id")){
                if (checkTrelloForSO(client, currentList.get(i)).getString("id").equals("Empty")) {
                    System.out.println("Tally: " + tally);
                    tally.add(i);
                }
            }
        }
        return tally;
    }

    public static void createTrelloCard(HttpClient client, String contextId, JSONObject jsonSO) throws IOException, InterruptedException {

        System.out.println("\n-- Create Trello Card SO --");
        System.out.println(jsonSO);

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(client, contextId, jsonSO.get("OrderID").toString());
        JSONObject itemInformation = itemCodeHandler.itemParseProcess();
        String parameters = agilityDataForTrelloGather(jsonSO, itemInformation);


        System.out.println("\n-- Created Card --");
        TrelloCalls trelloAPICall = new TrelloCalls(client, "cards", parameters);
        var response = trelloAPICall.postTrelloAPICall();

        checkTrelloCardForEmptyCustomFields(client, response.getString("id"), itemInformation);
    }

    public static void updateTrelloCards(HttpClient client, String contextId) throws IOException, InterruptedException {

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(client, contextId);

        JSONObject fetchedSalesOrderData = itemCodeHandler.agilityChangedSalesOrderListLookup();

        if(fetchedSalesOrderData.has("dtOrderResponse")) {
            JSONArray salesOrderDataArray = fetchedSalesOrderData.getJSONArray("dtOrderResponse");

            for(int i = 0; i < salesOrderDataArray.length(); i++) {

                JSONObject result = checkTrelloForSO(client, String.valueOf(salesOrderDataArray.getJSONObject(i).getNumber("OrderID")));

                ItemCodeHandler salesDataItemHandler = new ItemCodeHandler(client, contextId, salesOrderDataArray.getJSONObject(i));

                if (result.has("id")){
                    if (!result.getString("id").equals("Empty")) {

                        JSONObject itemInformation = salesDataItemHandler.itemParseProcess();

                        itemInformation.remove("idList");
                        itemInformation.put("idList", result.getString("idList"));

                        String parameters = agilityDataForTrelloGather(salesOrderDataArray.getJSONObject(i), itemInformation);

                        System.out.println("\n--- Updated a Trello Card ---");
                        TrelloCalls trelloCalls = new TrelloCalls(client, ("cards/" + result.getString("id")), parameters);
                        var response = trelloCalls.putTrelloAPICall(new JSONObject());

                        checkTrelloCardForEmptyCustomFields(client, response.getString("id"), itemInformation);

                        System.out.println("\nUpdates Applied");
                    }else{
                        System.out.println("\n- Trello Hasn't Updated Yet -");
                    }
                }else{
                    System.out.println("\n- Trello Hasn't Updated Yet -");
                }
            }
        }else{
            System.out.println("\n-- No Updates --");
        }
    }

    public static void checkTrelloCardForEmptyCustomFields(HttpClient client, String cardId, JSONObject itemInformation) throws IOException, InterruptedException {

        if(itemInformation.has("colorCode")){
            if (itemInformation.getString("colorCode") != null)
                updateCustomFieldTrello(client, cardId, itemInformation.getString("colorCustomField"), itemInformation.getString("colorCode"));
        }

        if(itemInformation.has("linkedID") && itemInformation.has("linkedType")){
            if (!(itemInformation.getString("linkedID").equals(null)) && itemInformation.getString("linkedType").equals("RM"))
                updateCustomFieldTrello(client, cardId, itemInformation.getString("rmCustomField"), itemInformation.getString("linkedID"));
        }
        //TODO Look into auto populating PO's
        //Probably applicable with Tru and CNC
//        if(!itemInformation.getString("linkedID").equals("") && itemInformation.getString("linkedType").equals("PO"))
//            customFieldTrello(client,cardId, poFieldId, itemInformation.getString("linkedID"));

    }

    public static void updateCustomFieldTrello(HttpClient client, String cardId, String customFieldID, String value) throws IOException, InterruptedException {

        String urlEndpoint = String.format("cards/%s/customField/%s/item", cardId, customFieldID );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", value);
        System.out.println("\n-- Update Custom Field in Trello --");
        TrelloCalls trelloCalls = new TrelloCalls(client, urlEndpoint, "");
        var response = trelloCalls.putTrelloAPICall(jsonObject);
//        System.out.println(response);

    }

    public static String agilityDataForTrelloGather(JSONObject jsonSO, JSONObject itemInformation){

        String idList = itemInformation.getString("idList");
        String idLabels = itemInformation.getString("idLabel");

        String description = jsonSO.getString("CustomerPO");

        String dueDate = jsonSO.getString("ExpectedDate");

        int dateHold = Integer.parseInt(dueDate.substring(8, 10));

        if(dateHold+1>31) {
            dueDate = dueDate.substring(0, 8) + (Integer.valueOf(dueDate.substring(8, 10)));
        }else{
            dateHold++;
            dueDate = dueDate.substring(0, 8) + dateHold;
        }
        String name = (
                "SO "+
                        jsonSO.getNumber("OrderID") +
                        " - " +
                        jsonSO.getString("ShipToName") +
                        " - " +
                        jsonSO.getString("TransactionJob")
        );

        name = name.replace(" ", "%20");
        name = name.replace("&", "%26");
        name = name.replace(",", "%2C");
        name = name.replace("#", "%23");
        name = name.replace("@", "%40");
        name = name.replace("*", "%2A");

        description = description.replace(" ", "%20");
        description = description.replace("&", "%26");
        description = description.replace(",", "%2C");
        description = description.replace("#", "%23");
        description = description.replace("@", "%40");
        description = description.replace("*", "%2A");

        String parameters = String.format("idList=%s&name=%s&idLabels=%s&due=%s", idList, name, idLabels, dueDate);

        if(!description.isEmpty()){
            parameters += String.format("&desc=%s", description);
        }
        return parameters;
    }
}

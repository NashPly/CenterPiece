package com.CenterPiece;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.CenterPiece.APICalls.*;
import com.fasterxml.jackson.annotation.JsonAlias;
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

        }, 0, 3, TimeUnit.MINUTES);
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
                .header("Branch", "FABRICATION")
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
//        String card_fields = "name,closed,desc,idList,labels";
        String card_fields = "closed,idList,labels";

        TrelloCalls trelloAPICall = new TrelloCalls(client, "search", String.format("query=%s&modelTypes=%s&card_fields=%s&card_attachments=true",
                soNum, modelTypes, card_fields));

        System.out.println("\n-- Check Trello For SO --");
        var response = trelloAPICall.getTrelloAPICall();
        System.out.println(response);

        if(response.has("cards")){
            if(response.getJSONArray("cards").length() > 0){
                JSONArray cards = response.getJSONArray("cards");

                List<JSONObject> openTrelloCards = new ArrayList<>();

                for (int i = 0; i < cards.length(); i++) {
                    if (!cards.getJSONObject(i).getBoolean("closed")) {
        //                System.out.println("\n-- Check Trello For SO --");
        //                System.out.println(cards.getJSONObject(i));
                        //return cards.getJSONObject(i);
                        openTrelloCards.add(cards.getJSONObject(i));
                    }
                }
                if(openTrelloCards.size() > 1){

                    JSONObject desiredCard = new JSONObject();

                    TrelloCalls trelloCalls = new TrelloCalls(client,"cards/");
                    for(int i = 0; i < openTrelloCards.size(); i++){
                        //TODO Handle Multiple cards with attachments
                            if(desiredCard.isEmpty() && openTrelloCards.get(i).getJSONArray("attachments").length() > 0){
                                desiredCard = openTrelloCards.get(i);
                                openTrelloCards.remove(i);
                            }
                    }
                    if(desiredCard.isEmpty()){
                        desiredCard = openTrelloCards.get(0);
                        openTrelloCards.remove(0);
                    }

                    for(JSONObject json: openTrelloCards){
                        System.out.println("\n - Deleted Duplicate TrelloCard -\n" + json + "\n");
                        var deleteResponse = trelloCalls.deleteTrelloAPICall(json.getString("id"));
                    }

                    return desiredCard;
                }else{
                    return openTrelloCards.get(0);
                }
            }
        }

        JSONObject json = new JSONObject();
        json.put("id","Empty");
        return json;
    }

    public static List<Integer> tallySOsToBeCreated(HttpClient client, int size, List<String> currentList) throws IOException, InterruptedException {

        List<Integer> tally = new ArrayList<>();

        for (int i = 0; i< size; i++){
            var checkHold = checkTrelloForSO(client, currentList.get(i));
            if(checkHold.has("id")){
                if (checkHold.getString("id").equals("Empty")) {
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

        List<String> liveTrelloBuckets = new ArrayList<>();
        liveTrelloBuckets.addAll(Arrays.asList("62869b5c1351de037ffd2cbc", "61f2d5c461ac134ef274ae5f",
                "62869b5c1351de037ffd2ccd", "6239c656ab5c356ec1568beb", "62869b5c1351de037ffd2cce",
                "60c26dfb44555566d32ae64d", "62869b5c1351de037ffd2cd0", "61e6d38623686777464221b9",
                "62869b5c1351de037ffd2cd1", "60c26dfb44555566d32ae64e", "62869b5c1351de037ffd2cd4",
                "61b360e35ab37c0d9037c19f"));

        JSONObject fetchedSalesOrderData = itemCodeHandler.agilityChangedSalesOrderListLookup();

        if(fetchedSalesOrderData.has("dtOrderResponse")) {
            JSONArray salesOrderDataArray = fetchedSalesOrderData.getJSONArray("dtOrderResponse");

            for(int i = 0; i < salesOrderDataArray.length(); i++) {

                JSONObject result = checkTrelloForSO(client, String.valueOf(salesOrderDataArray.getJSONObject(i).getNumber("OrderID")));

                ItemCodeHandler salesDataItemHandler = new ItemCodeHandler(client, contextId, salesOrderDataArray.getJSONObject(i));

                if (result.has("id")){
                    if (!result.getString("id").equals("Empty")) {

                        JSONObject itemInformation = salesDataItemHandler.itemParseProcess();

//                        if(result.getString("idList").equals("60c26dfb44555566d32ae651") ||
//                                result.getString("idList").equals("62c4430fcdfa097c5642436b") ||
//                                result.getString("idList").equals("61c1e06cdc22878b2e8c7ae7") ||
//                                result.getString("idList").equals("60c26dfb44555566d32ae64c") ||
//                                result.getString("idList").equals("") ||
//                                result.getString("idList").equals("") ||
//                                result.getString("idList").equals("")) {



                        System.out.println(itemInformation.getString("idList"));
                        if(result.has("idList") &&
                                !(itemInformation.getString("idList").equals("62869b5c1351de037ffd2cd4") ||
                                        itemInformation.getString("idList").equals("61b360e35ab37c0d9037c19f"))){
                            if(!liveTrelloBuckets.contains(result.getString("idList"))) {
                                itemInformation.remove("idList");
                                itemInformation.put("idList", result.getString("idList"));
                            }
                        }

                        ArrayList<String> labelIds = new ArrayList<>();
                        if(result.has("labels")) {
                            for(int x = 0; x < result.getJSONArray("labels").length(); x++){

                                labelIds.add(result.getJSONArray("labels")
                                        .getJSONObject(x).getString("id"));
                            }

                            itemInformation.remove("idLabel");
                            itemInformation.put("idLabel", String.join(",", labelIds));
                        }

                        String parameters = agilityDataForTrelloGather(salesOrderDataArray.getJSONObject(i), itemInformation);

                        System.out.println("\n--- Updated a Trello Card ---");
                        TrelloCalls trelloCalls = new TrelloCalls(client, ("cards/" + result.getString("id")), parameters);
                        var response = trelloCalls.putTrelloAPICall(new JSONObject());

                        checkTrelloCardForEmptyCustomFields(client, response.getString("id"), itemInformation);

                        System.out.println("\nUpdates Applied");
                    }else{
                        System.out.println("\n- Trello Hasn't Updated Yet -");
                        //TODO Work out some way to create a card if there isn't one on Trello yet
                    }
                }else if (result.has("error")) {
                    System.out.println(result);
                } else{
                    System.out.println("\n- Trello Hasn't Updated Yet 2 -");

                    //TODO Work out some way to create a card if there isn't one on Trello yet
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
                        jsonSO.getString("BillToName") +
                        " - " +
                        jsonSO.getString("TransactionJob")
        );



        name = name.replace(" ", "%20");
        name = name.replace("&", "%26");
        name = name.replace(",", "%2C");
        name = name.replace("#", "%23");
        name = name.replace("@", "%40");
        name = name.replace("*", "%2A");
        name = name.replace("'", "%27");

        description = description.replace(" ", "%20");
        description = description.replace("&", "%26");
        description = description.replace(",", "%2C");
        description = description.replace("#", "%23");
        description = description.replace("@", "%40");
        description = description.replace("*", "%2A");
        description = description.replace("'", "%27");

        String parameters = String.format("idList=%s&name=%s&idLabels=%s&due=%s", idList, name, idLabels, dueDate);

        if(!description.isEmpty()){
            parameters += String.format("&desc=%s", description);
        }

        return parameters;
    }
}

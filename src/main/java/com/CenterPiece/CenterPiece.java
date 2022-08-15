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

    public static void main(String[] args) throws IOException, InterruptedException {

        var client = HttpClient.newBuilder().build();
        ScheduledExecutorService sesh = Executors.newSingleThreadScheduledExecutor();
        mainProcess(client,sesh);
//        updateTrelloCard(client,"623b29cbed4f428e2fd2711c", "asdf");
    }

    private static void mainProcess(HttpClient client, ScheduledExecutorService sesh){

        Calendar calendar = Calendar.getInstance();

        sesh.scheduleAtFixedRate(() -> {

            calendar.getInstance();

            String contextId = null;
            try {
                contextId = login(client);
//                System.out.println(contextId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            JSONArray currentSalesOrders = null;
            try {

                ItemCodeHandler itemCodeHandler = new ItemCodeHandler(client, contextId);
                currentSalesOrders = itemCodeHandler.agilitySalesOrderListLookup();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<String> currentSalesOrderNums = salesOrderParser(currentSalesOrders);
            List<Integer> toBeCreatedSOs = null;
            try {
                toBeCreatedSOs = tallySOsToBeCreated(client, currentSalesOrderNums.size(), currentSalesOrderNums);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Test using the first to be created
            for(int x = 0 ; x < toBeCreatedSOs.size(); x++) {
                try {
                    createTrelloCard(client, contextId, currentSalesOrders.getJSONObject(toBeCreatedSOs.get(x)));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                logout(client, contextId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, 0, 60, TimeUnit.SECONDS);
    }
    //minutesToNextHour(calendar)

    private static long minutesToNextHour(Calendar calendar) {
        int minutes = calendar.get(Calendar.MINUTE);
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

//        var objectMapper = new ObjectMapper();
//        String requestBody = objectMapper
//                .writeValueAsString(outerValues);

        var request = HttpRequest.newBuilder(
                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/Session/Login"))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        String contextID = json.getJSONObject("response").getString("SessionContextId");

        return contextID;
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

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //Parses SO Orders to get SO Nums
    public static List<String> salesOrderParser(JSONArray jsonArray){

        List<String> soList = new ArrayList<>();
        for(int i = 0; i <  jsonArray.length(); i++){
            soList.add(jsonArray.getJSONObject(i).get("OrderID").toString());
        }
        return soList;
    }


    public static boolean checkTrelloForSO(HttpClient client, String soNum) throws IOException, InterruptedException {

        System.out.println("soNum");
        System.out.println(soNum);

        String query = soNum;
        String modelTypes = "cards";
        String card_fields = "name,closed";
//        String uri = String.format("https://api.trello.com/1/search?query=%s&modelTypes=%s&card_fields=%s&key=%s&token=%s",
//                query, modelTypes, card_fields);
        TrelloCalls trelloAPICall = new TrelloCalls(client, "search", String.format("query=%s&modelTypes=%s&card_fields=%s",
                query, modelTypes, card_fields));

        var response = trelloAPICall.getTrelloAPICall();

        JSONArray cards = response.getJSONArray("cards");

        Boolean result = false;

        for (int i = 0; i < cards.length(); i++){
            if(cards.getJSONObject(i).getBoolean("closed") == false) {
                result =  true;
            }
        }
        //TODO Logic around multiples where one might be archived and one isnt
//        if(cards.length() > 0){
//
//            if(cards.getJSONObject(0).getBoolean("closed") == false) {
//                return true;
//            }else{
//                return false;
//            }
//        }
//        else
//            return false;
        return result;
    }

    public static List<Integer> tallySOsToBeCreated(HttpClient client, int size, List<String> currentList) throws IOException, InterruptedException {

        List<Integer> tally = new ArrayList<>();
        for (int i = 0; i< size; i++){
            if(!checkTrelloForSO(client, currentList.get(i))){
                tally.add(i);
                System.out.println("Tally: " + tally);
            }
        }
        return tally;
    }

    //TODO retrofit post call
    public static String createTrelloCard(HttpClient client, String contextId, JSONObject jsonSO) throws IOException, InterruptedException {

        System.out.println("JSON jsonSO");
        System.out.println(jsonSO);

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(client, contextId, jsonSO.get("OrderID").toString());

        var itemInformation = itemCodeHandler.itemParseProcess();
        String idList = itemInformation.getString("idList");
        String idLabels = itemInformation.getString("idLabel");

        String description = jsonSO.getString("CustomerPO").replace(" ", "%20");

        String dueDate = jsonSO.getString("ExpectedDate");
        dueDate = dueDate.substring(0,8)+ (Integer.valueOf(dueDate.substring(8,10))+1);

                String name = (
                "SO "+
                jsonSO.get("OrderID") +
                " - " +
                jsonSO.getString("ShipToName") +
                " - " +
                jsonSO.getString("TransactionJob")
        );

        name = name.replace(" ", "%20");
        // = "61f2d5c461ac134ef274ae5f";
        //String idLabels = "60c26dfc44555566d32ae700";
//        String key = "90fb4c3f6615067b94535f130c0d7b4f";
//        String token = "c95f8154db55a4f2297c9ab6d431b1d3d5dfcac19bc3bafb3bce4b35ab9fcf31";
//
//        String uri = String.format("https://api.trello.com/1/cards?idList=%s&name=%s&idLabels=%s&key=%s&token=%s", idList, name, idLabels, key, token);
//
//        var request = HttpRequest.newBuilder(
//                URI.create(uri))
//                .header("accept", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(""))
//                .build();
//
//        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        JSONObject json = new JSONObject(response.body());

        String parameters = String.format("idList=%s&name=%s&idLabels=%s&due=%s", idList, name, idLabels, dueDate);

        if(!description.isEmpty()){
            parameters += String.format("&desc=%s", description);
        }

        TrelloCalls trelloAPICall = new TrelloCalls(client, "cards", parameters);

        var response = trelloAPICall.postTrelloAPICall();

        updateTrelloCard(client, response.getString("id"), itemInformation);

        return response.toString();
    }

    public static void updateTrelloCard(HttpClient client, String cardId, JSONObject itemInformation) throws IOException, InterruptedException {

        JSONObject request = new JSONObject();
        JSONObject innerRequest = new JSONObject();
        String colorCodeFieldId = "6197b500bbb79658801189ce";
        String remanFieldId = "621519b6944e3c4fc091a515";
        String poFieldId = "";

        customFieldTrello(client,cardId, colorCodeFieldId, itemInformation.getString("colorCode"));

        if(!itemInformation.getString("linkedID").equals("") && itemInformation.getString("linkedType").equals("RM"))
        customFieldTrello(client,cardId, remanFieldId, itemInformation.getString("linkedID"));

        if(!itemInformation.getString("linkedID").equals("") && itemInformation.getString("linkedType").equals("PO"))
            customFieldTrello(client,cardId, poFieldId, itemInformation.getString("linkedID"));

    }

    public static void customFieldTrello(HttpClient client, String cardId, String customFieldID, String value) throws IOException, InterruptedException {

        String urlEndpoint = String.format("cards/%s/customField/%s/item", cardId, customFieldID );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", value);
        TrelloCalls trelloCalls = new TrelloCalls(client, urlEndpoint, "");
        var response = trelloCalls.putTrelloAPICall(jsonObject);

        System.out.println("Update Card Response");
        System.out.println(response);

    }

    //TODO retrofit get call
    public static String getTrelloCardById(HttpClient client, String cardId) throws IOException, InterruptedException {

        //String cardId = "62bdc0a16bb6051ddde68d28";
//        String key = "90fb4c3f6615067b94535f130c0d7b4f";
//        String token = "c95f8154db55a4f2297c9ab6d431b1d3d5dfcac19bc3bafb3bce4b35ab9fcf31";

        //String uri = String.format("%s?key=%s&token=%s", cardId, key, token);

        String urlEndpoint = "cards/";
        String parameters = String.format("%s?", cardId);

        TrelloCalls trelloAPICall = new TrelloCalls(client,urlEndpoint, parameters);

        var response = trelloAPICall.getTrelloAPICall();

        System.out.println(response);
        System.out.println(response.get("closed"));

        return "hello";
    }

//    public static JSONObject postAgilityAPICall(HttpClient client, String contextId, String urlEndpoint, HttpRequest.BodyPublisher requestBody) throws IOException, InterruptedException {
//
//        var request = HttpRequest.newBuilder(
//                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/" + urlEndpoint))
//                .header("accept", "application/json")
//                .header("ContextId", contextId)
//                .header("Branch", "CABINETS")
//                .POST(requestBody)
//                .build();
//
//        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        JSONObject responseBody = new JSONObject(response.body());
//        return responseBody;
//    }



}

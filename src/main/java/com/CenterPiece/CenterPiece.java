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
import org.joda.time.DateTime;
import org.json.*;


public class CenterPiece {

    public static void main(String[] args) throws IOException, InterruptedException {

        var client = HttpClient.newBuilder()
                .build();

        itemParseProcess(client);

        ScheduledExecutorService sesh = Executors.newSingleThreadScheduledExecutor();
        mainProcess(client,sesh);

    }

    private static void mainProcess(HttpClient client, ScheduledExecutorService sesh){

        Calendar calendar = Calendar.getInstance();

        sesh.scheduleAtFixedRate(() -> {

            calendar.getInstance();

            String contextId = null;
            try {
                contextId = login(client);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            JSONArray currentSalesOrders = null;
            try {
                System.out.println("made it");
                currentSalesOrders = agilitySalesOrderListLookup(client,contextId);
                System.out.println("currentsalesOrders");
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
                    createTrelloCard(client, currentSalesOrders.getJSONObject(toBeCreatedSOs.get(x)));
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

        }, minutesToNextHour(calendar), 30, TimeUnit.MINUTES);
    }

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

    public static void  logout(HttpClient client, String contextId) throws IOException, InterruptedException {
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

    private static void itemParseProcess(HttpClient client) throws IOException, InterruptedException {

        String contextId = login(client);

        JSONArray salesOrdersArray = agilitySalesOrderListLookup(client, contextId);

        List<String> itemList = itemListParser(client, contextId, salesOrdersArray);

        JSONObject jsonObject = agilityItemSearch(client, contextId, itemList);

        System.out.println("Json Object: " + jsonObject);

        logout(client, contextId);

    }

    //Returns a list of SO Orders
    public static JSONArray agilitySalesOrderListLookup(HttpClient client, String contextId) throws IOException, InterruptedException {

        JSONObject innerRequestBody = new JSONObject();

        DateTime dt = new DateTime();
        String currentHour;
        if(dt.getHourOfDay()<10)
            currentHour = "0" + dt.getHourOfDay();
        else
            currentHour = "" + dt.getHourOfDay();

        String currentDay;
        if(dt.getDayOfMonth()<10)
            currentDay = "0" + dt.getDayOfMonth();
        else
            currentDay = "" + dt.getDayOfMonth();

        String currentMonth;
        if(dt.getMonthOfYear()<10)
            currentMonth = "0" + dt.getMonthOfYear();
        else
            currentMonth = "" + dt.getMonthOfYear();

        innerRequestBody.put("IncludeOpenOrders", true);
        innerRequestBody.put("IncludeInvoicedOrders", false);
        innerRequestBody.put("IncludeCanceledOrders", false);
        innerRequestBody.put("OrderDateRangeStart", "2022-"+currentMonth+"-"+currentDay+"T"+currentHour+":00:00-6:00");
        innerRequestBody.put("OrderDateRangeEnd", "2022-"+currentMonth+"-"+currentDay+"T"+currentHour+":59:59-6:00");
//        innerRequestBody.put("SearchBy", "Order ID");
//        innerRequestBody.put("SearchValue", "204929");

        //requestBody.put("request", innerRequestBody);

//        var request = HttpRequest.newBuilder(
//                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/Orders/SalesOrderList"))
//                .header("accept", "application/json")
//                .header("ContextId", contextId)
//                .header("Branch", "CABINETS")
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
//                .build();
//
//        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        AgilityAPICall agilityAPICall = new AgilityAPICall(client, contextId, "Orders/SalesOrderList", innerRequestBody);

        var response = agilityAPICall.postAgilityAPICall();

        System.out.println(response);
        JSONArray json = response.getJSONObject("response")
                .getJSONObject("OrdersResponse")
                .getJSONObject("dsOrdersResponse")
                .getJSONArray("dtOrderResponse");

        return json;
    }

    private static List<String> itemListParser(HttpClient client, String contextId, JSONArray salesOrdersArray){

        List<String> items = new ArrayList<>();

        for (int x = 0; x < salesOrdersArray.length(); x++){
            items.add(salesOrdersArray.getJSONObject(x).getJSONArray("dtOrderDetailResponse").getJSONObject(0).getString("ItemCode"));
        }
        //var item = salesOrdersArray.getJSONObject(0).getJSONArray("dtOrderDetailResponse").getJSONObject(0).getString("ItemCode");

        //TODO search for this item

        System.out.println("Item: " + items);

        return items;
    }

    //TODO needs to be converted to automatically put item list together
    private static JSONObject agilityItemSearch(HttpClient client, String contextId, List<String> itemList) throws IOException, InterruptedException {
        JSONObject dsItemsListRequest = new JSONObject();
        JSONObject innerDtItemsListRequest = new JSONObject();
        JSONArray dtItemsListRequestArray = new JSONArray();
        JSONObject dtItemsListRequestBody = new JSONObject();

        dtItemsListRequestBody.put("SearchBy", "Item Code");
        dtItemsListRequestBody.put("SearchValue", "NS0000022413");
        dtItemsListRequestBody.put("IncludeNonStock", true);
        dtItemsListRequestBody.put("IncludePriceData", true);
        dtItemsListRequestBody.put("IncludeQuantityData", true);
        dtItemsListRequestBody.put("IncludeNonSaleable", true);

        dtItemsListRequestArray.put(dtItemsListRequestBody);
        innerDtItemsListRequest.put("dtItemsListResponse", dtItemsListRequestArray);
        dsItemsListRequest.put("dtItemsListResponse", innerDtItemsListRequest);

        AgilityAPICall agilityPostCall = new AgilityAPICall(client, contextId, "Inventory/ItemsList", dsItemsListRequest);

        return agilityPostCall.postAgilityAPICall();
        //old
        //return postAgilityAPICall(client, contextId, "Inventory/ItemsList", buildRequest(dsItemsListRequest));
    }


    //Parses SO Orders to get SO Nums
    public static List<String> salesOrderParser(JSONArray jsonArray){

        List<String> soList = new ArrayList<>();
        for(int i = 0; i <  jsonArray.length(); i++){
            soList.add(jsonArray.getJSONObject(i).get("OrderID").toString());
        }
        return soList;
    }

    //TODO retrofit post call
    public static boolean checkTrelloForSO(HttpClient client, String soNum) throws IOException, InterruptedException {

        String query = soNum;
        String modelTypes = "cards";
        String card_fields = "name,closed";
        String key = "90fb4c3f6615067b94535f130c0d7b4f";
        String token = "c95f8154db55a4f2297c9ab6d431b1d3d5dfcac19bc3bafb3bce4b35ab9fcf31";

        String uri = String.format("https://api.trello.com/1/search?query=%s&modelTypes=%s&card_fields=%s&key=%s&token=%s",
                query, modelTypes, card_fields, key, token);

        var request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());

        JSONArray cards = json.getJSONArray("cards");

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
    public static String createTrelloCard(HttpClient client, JSONObject jsonSO) throws IOException, InterruptedException {

        String name = (
                "SO "+
                jsonSO.get("OrderID") +
                " - " +
                jsonSO.getString("ShipToName") +
                ": " +
                jsonSO.getString("TransactionJob")
        );

        name = name.replace(" ", "%20");
        String idList = "61f2d5c461ac134ef274ae5f";
        String idLabels = "60c26dfc44555566d32ae700";
        String key = "90fb4c3f6615067b94535f130c0d7b4f";
        String token = "c95f8154db55a4f2297c9ab6d431b1d3d5dfcac19bc3bafb3bce4b35ab9fcf31";

        String uri = String.format("https://api.trello.com/1/cards?idList=%s&name=%s&idLabels=%s&key=%s&token=%s", idList, name, idLabels, key, token);

//        "https://api.trello.com/1/cards?" +
//                "&name=" + name +
//                "idList=" + idList +
//                "&idLabels="+ idLabels +
//                "&key=" + key +
//                "&token=" + token);

        var request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());

        System.out.println(json);

        return "hello";
    }

    //TODO retrofit get call
    //TODO Under Construction
    public static String getTrelloCardById(HttpClient client, String cardId) throws IOException, InterruptedException {

        //String cardId = "62bdc0a16bb6051ddde68d28";
//        String key = "90fb4c3f6615067b94535f130c0d7b4f";
//        String token = "c95f8154db55a4f2297c9ab6d431b1d3d5dfcac19bc3bafb3bce4b35ab9fcf31";

        //String uri = String.format("%s?key=%s&token=%s", cardId, key, token);

        String urlEndpoint = "cards/";
        String parameters = String.format("%s?", cardId);


        var response = getTrelloAPICall(client,urlEndpoint, parameters);

        System.out.println(response);
        System.out.println(response.get("closed"));

        return "hello";
    }

    public static JSONObject postAgilityAPICall(HttpClient client, String contextId, String urlEndpoint, HttpRequest.BodyPublisher requestBody) throws IOException, InterruptedException {

        var request = HttpRequest.newBuilder(
                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/" + urlEndpoint))
                .header("accept", "application/json")
                .header("ContextId", contextId)
                .header("Branch", "CABINETS")
                .POST(requestBody)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject responseBody = new JSONObject(response.body());
        return responseBody;
    }

    //TODO under construction
    public static JSONObject getTrelloAPICall(HttpClient client, String urlEndpoint, String parameters) throws IOException, InterruptedException {

        String key = "90fb4c3f6615067b94535f130c0d7b4f";
        String token = "c95f8154db55a4f2297c9ab6d431b1d3d5dfcac19bc3bafb3bce4b35ab9fcf31";

        String uri = String.format("https://api.trello.com/1/%s?%s&key=%s&token=%s", urlEndpoint, parameters, key, token);

        var request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject responseBody = new JSONObject(response.body());
        return responseBody;
    }

    //TODO under construction
    public static JSONObject postTrelloAPICall(HttpClient client, String urlEndpoint, String parameters, HttpRequest.BodyPublisher requestBody) throws IOException, InterruptedException {

        String key = "90fb4c3f6615067b94535f130c0d7b4f";
        String token = "c95f8154db55a4f2297c9ab6d431b1d3d5dfcac19bc3bafb3bce4b35ab9fcf31";

        String uri = String.format("https://api.trello.com/1/%s?%s&key=%s&token=%s", urlEndpoint, parameters, key, token);

        var request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .POST(buildRequest())
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject responseBody = new JSONObject(response.body());
        return responseBody;
    }

    public static HttpRequest.BodyPublisher buildRequest(){
        return HttpRequest.BodyPublishers.ofString("");
    }

    public static HttpRequest.BodyPublisher buildRequest(JSONObject innerRequestBody){
        JSONObject requestBody = new JSONObject();
        requestBody.put("request", innerRequestBody);
        return HttpRequest.BodyPublishers.ofString(requestBody.toString());

    }
}

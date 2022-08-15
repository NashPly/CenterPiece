package com.CenterPiece;

import com.CenterPiece.APICalls.AgilityCalls;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.http.HttpClient;

public class ItemCodeHandler {

    private HttpClient client;
    private String contextId;
    private JSONArray salesOrderArray;
    private JSONObject salesOrder;
    private JSONArray salesOrderItemsArray;
    private String salesOrderNumber;
    private String itemCode;
    private String itemGroup = "None";
    private String linkedTranType;
    private String linkedTranID;
    private JSONObject agilityItemSearchResult;

//    private static void itemParseProcess(HttpClient client) throws IOException, InterruptedException {
//
//        String contextId = login(client);
//
//        JSONArray salesOrdersArray = agilitySalesOrderListLookup(client, contextId);
//
//        List<String> itemList = itemListParser(client, contextId, salesOrdersArray);
//
//
//
//        JSONObject jsonObject = agilityItemSearch();
//
//        System.out.println("Json Object: " + jsonObject);
//
//        logout(client, contextId);
//
//    }

    ItemCodeHandler (HttpClient cl, String context, String sO){
        client = cl;
        contextId = context;
        salesOrderNumber = sO;
    }

    ItemCodeHandler (HttpClient cl, String context) throws IOException, InterruptedException {
        client = cl;
        contextId = context;
    }

    //    //Returns a list of SO Orders
//    public static JSONArray agilitySalesOrderListLookup(HttpClient client, String contextId) throws IOException, InterruptedException {
//
//        JSONObject innerRequestBody = new JSONObject();
//
//        DateTime dt = new DateTime();
//        String currentHour;
//        if(dt.getHourOfDay()<10)
//            currentHour = "0" + dt.getHourOfDay();
//        else
//            currentHour = "" + dt.getHourOfDay();
//
//        String currentDay;
//        if(dt.getDayOfMonth()<10)
//            currentDay = "0" + dt.getDayOfMonth();
//        else
//            currentDay = "" + dt.getDayOfMonth();
//
//        String currentMonth;
//        if(dt.getMonthOfYear()<10)
//            currentMonth = "0" + dt.getMonthOfYear();
//        else
//            currentMonth = "" + dt.getMonthOfYear();
//
//        innerRequestBody.put("IncludeOpenOrders", true);
//        innerRequestBody.put("IncludeInvoicedOrders", false);
//        innerRequestBody.put("IncludeCanceledOrders", false);
//        innerRequestBody.put("OrderDateRangeStart", "2022-"+currentMonth+"-"+currentDay+"T"+currentHour+":00:00-6:00");
//        innerRequestBody.put("OrderDateRangeEnd", "2022-"+currentMonth+"-"+currentDay+"T"+currentHour+":59:59-6:00");
////        innerRequestBody.put("SearchBy", "Order ID");
////        innerRequestBody.put("SearchValue", "204929");
//
//        //requestBody.put("request", innerRequestBody);
//
////        var request = HttpRequest.newBuilder(
////                URI.create("https://api-1086-1.dmsi.com/nashvilleplywoodprodAgilityPublic/rest/Orders/SalesOrderList"))
////                .header("accept", "application/json")
////                .header("ContextId", contextId)
////                .header("Branch", "CABINETS")
////                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
////                .build();
////
////        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        AgilityAPICall agilityAPICall = new AgilityAPICall(client, contextId, "Orders/SalesOrderList", innerRequestBody);
//
//        var response = agilityAPICall.postAgilityAPICall();
//
//        System.out.println(response);
//        JSONArray json = response.getJSONObject("response")
//                .getJSONObject("OrdersResponse")
//                .getJSONObject("dsOrdersResponse")
//                .getJSONArray("dtOrderResponse");
//
//        return json;
//    }
//

    public JSONObject itemParseProcess() throws IOException, InterruptedException {

        this.salesOrderArray = agilitySalesOrderListLookup();

        for(int i = 0; i < this.salesOrderArray.length(); i++){
            if(this.salesOrderNumber.equals(this.salesOrderArray.getJSONObject(i).get("OrderID").toString())){
                this.salesOrder = this.salesOrderArray.getJSONObject(i);
            }
        }

        if(this.salesOrder.has("dtOrderDetailResponse")){
            this.salesOrderItemsArray = this.salesOrder
                    .getJSONArray("dtOrderDetailResponse");
            var item = this.salesOrderItemsArray.getJSONObject(0);
            this.itemCode = item.getString("ItemCode");
            this.linkedTranType = item.getString("LinkedTranType");
            this.linkedTranID = String.valueOf(item.getInt("LinkedTranID"));


            //TODO search for this item
            this.agilityItemSearchResult = agilityItemSearch();

            //this.extItemDesc = agilityItemSearchResult.getString("ExtendedDescription").split("\\(\\{0,2}\\)-\\( \\{0,2}\\)\\{0,1}\\|\\( \\{0,2}\\):\\( \\{0,2}\\)\\{0,1}")[1];

            this.itemGroup = this.agilityItemSearchResult.getString("ItemGroupMajor");

        }
        return this.getCardDestinationFromItemCodeResult();
    }

    //Returns a list of SO Orders
    public JSONArray agilitySalesOrderListLookup() throws IOException, InterruptedException {

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
        AgilityCalls agilityAPICall = new AgilityCalls(client, contextId, "Orders/SalesOrderList", innerRequestBody);

        var response = agilityAPICall.postAgilityAPICall();

//        System.out.println("agilitySalesOrderListLookup");
//        System.out.println(response);
        JSONArray json = response.getJSONObject("response")
                .getJSONObject("OrdersResponse")
                .getJSONObject("dsOrdersResponse")
                .getJSONArray("dtOrderResponse");

        return json;
    }
    //    private static List<String> itemListParser(HttpClient client, String contextId, JSONArray salesOrdersArray){
//
//        List<String> items = new ArrayList<>();
//
//        for (int x = 0; x < salesOrdersArray.length(); x++){
//            items.add(salesOrdersArray.getJSONObject(x).getJSONArray("dtOrderDetailResponse").getJSONObject(0).getString("ItemCode"));
//        }
//        //var item = salesOrdersArray.getJSONObject(0).getJSONArray("dtOrderDetailResponse").getJSONObject(0).getString("ItemCode");
//
//        //TODO search for this item
//
//        System.out.println("Item: " + items);
//
//        return items;
//    }
//
//    //TODO needs to be converted to automatically put item list together
//    private static JSONObject agilityItemSearch(HttpClient client, String contextId, List<String> itemList) throws IOException, InterruptedException {
//        JSONObject dsItemsListRequest = new JSONObject();
//        JSONObject innerDtItemsListRequest = new JSONObject();
//        JSONArray dtItemsListRequestArray = new JSONArray();
//        JSONObject dtItemsListRequestBody = new JSONObject();
//
//        dtItemsListRequestBody.put("SearchBy", "Item Code");
//        dtItemsListRequestBody.put("SearchValue", "NS0000022413");
//        dtItemsListRequestBody.put("IncludeNonStock", true);
//        dtItemsListRequestBody.put("IncludePriceData", true);
//        dtItemsListRequestBody.put("IncludeQuantityData", true);
//        dtItemsListRequestBody.put("IncludeNonSaleable", true);
//
//        dtItemsListRequestArray.put(dtItemsListRequestBody);
//        innerDtItemsListRequest.put("dtItemsListResponse", dtItemsListRequestArray);
//        dsItemsListRequest.put("dtItemsListResponse", innerDtItemsListRequest);
//
//        AgilityAPICall agilityPostCall = new AgilityAPICall(client, contextId, "Inventory/ItemsList", dsItemsListRequest);
//
//        return agilityPostCall.postAgilityAPICall();
//        //old
//        //return postAgilityAPICall(client, contextId, "Inventory/ItemsList", buildRequest(dsItemsListRequest));
//    }
    //TODO needs to be converted to automatically put item list together
    public JSONObject agilityItemSearch() throws IOException, InterruptedException {

        JSONObject dsItemsListRequest = new JSONObject();
        JSONObject innerDtItemsListRequest = new JSONObject();
        JSONArray dtItemsListRequestArray = new JSONArray();
        JSONObject dtItemsListRequestBody = new JSONObject();

        dtItemsListRequestBody.put("SearchBy", "Item Code");
        dtItemsListRequestBody.put("SearchValue", this.itemCode);
        dtItemsListRequestBody.put("IncludeNonStock", true);
        dtItemsListRequestBody.put("IncludePriceData", true);
        dtItemsListRequestBody.put("IncludeQuantityData", true);
        dtItemsListRequestBody.put("IncludeNonSaleable", true);

        dtItemsListRequestArray.put(dtItemsListRequestBody);
        innerDtItemsListRequest.put("dtItemsListRequest", dtItemsListRequestArray);
        dsItemsListRequest.put("dsItemsListRequest", innerDtItemsListRequest);

        System.out.println("AgilityItemSearch response");
        AgilityCalls agilityPostCall = new AgilityCalls(client, contextId, "Inventory/ItemsList", dsItemsListRequest);

        JSONObject response =  agilityPostCall.postAgilityAPICall();

        System.out.println("AgilityItemSearch response");
        System.out.println(response);
//        if(response.getString("_errors").length() <0){
//            System.out.println(response);
//            System.out.println("Errors");
//        }

        JSONObject json = response.getJSONObject("response")
                .getJSONObject("ItemsListResponse")
                .getJSONObject("dsItemsListResponse")
                .getJSONArray("dtItemsListResponse")
                .getJSONObject(0);

        return json;

    }

    //TODO gather various ID's
    public JSONObject getCardDestinationFromItemCodeResult(){

        String idList = "";
        String idLabel = "";
        String colorCode = "";
        String linkedType = "";
        String linkedID = "";

        if(this.itemGroup.equals("3300")){

            //kk cabinets
            idList = "62869b5c1351de037ffd2cc4";
            idLabel = "62869b5c1351de037ffd2d26";

        }else if(this.itemGroup.equals("3350")){

            System.out.println("Success");
            //cnc cabinets
            idList = "62869b5c1351de037ffd2cc4";
            idLabel = "62869e47dcae4f52e15c90e1";

        }else if(this.itemGroup.equals("3455")){

            //tru cabinets
            idList = "62869b5c1351de037ffd2cc4";
            idLabel = "62869db3e04b83468347996b";

        }else if(this.itemGroup.equals("3450")){

            //choice cabinets
            idList = "62869b5c1351de037ffd2cc4";
            idLabel = "62869b5c1351de037ffd2d32";

        }else if(this.itemGroup.equals("3500")){

            //counter tops
            idList = "60c26dfb44555566d32ae651";
            idLabel = "60c26dfc44555566d32ae700";
            var extDescRough = this.agilityItemSearchResult.getString("ExtendedDescription").split("-|:");

            //TODO Not a long term parsing solution
            colorCode = extDescRough[1].replaceFirst(" ", "") + "-" + extDescRough[2];

            linkedType = this.linkedTranType;
            linkedID = this.linkedTranID;

        }else if(this.itemGroup.equals("None")){

            idList = "61f2d5c461ac134ef274ae5f";
            idLabel = "62f6a75f8db34f1e9ac4467e";

        }else{

            idList = "61f2d5c461ac134ef274ae5f";
            idLabel = "62f6a75f8db34f1e9ac4467e";

        }

        JSONObject json = new JSONObject();
        json.put("idList", idList);
        json.put("idLabel", idLabel);
        json.put("colorCode", colorCode);
        json.put("linkedType", linkedType);
        json.put("linkedID", linkedID);

        return json;
    }

}

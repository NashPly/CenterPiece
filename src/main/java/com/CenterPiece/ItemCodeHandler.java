package com.CenterPiece;

import com.CenterPiece.APICalls.AgilityCalls;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.http.HttpClient;

public class ItemCodeHandler {

    private final HttpClient client;
    private final String contextId;
    private JSONObject salesOrder;
    private String salesOrderNumber = "0";
    private String itemCode;
    private String itemGroup = "None";
    private String linkedTranType;
    private String linkedTranID;
    private JSONObject agilityItemSearchResult;

    ItemCodeHandler (HttpClient cl, String context) throws IOException, InterruptedException {
        client = cl;
        contextId = context;
    }

    ItemCodeHandler (HttpClient cl, String context, String sO){
        client = cl;
        contextId = context;
        salesOrderNumber = sO;
    }

    ItemCodeHandler (HttpClient cl, String context, JSONObject salesOrder){
        client = cl;
        contextId = context;
        this.salesOrder = salesOrder;
    }

    public JSONObject itemParseProcess() throws IOException, InterruptedException {

        JSONArray salesOrderArray = agilitySalesOrderListLookup();

        for(int i = 0; i < salesOrderArray.length(); i++){
            if(!this.salesOrderNumber.equals("0")){
                if (this.salesOrderNumber.equals(salesOrderArray.getJSONObject(i).getNumber("OrderID").toString())) {
                    this.salesOrder = salesOrderArray.getJSONObject(i);
                }
            }
        }
        System.out.println("");

        if(this.salesOrder.has("dtOrderDetailResponse")){
            JSONArray salesOrderItemsArray = this.salesOrder
                    .getJSONArray("dtOrderDetailResponse");
            var item = salesOrderItemsArray.getJSONObject(0);
            this.itemCode = item.getString("ItemCode");
            this.linkedTranType = item.getString("LinkedTranType");
            this.linkedTranID = String.valueOf(item.getInt("LinkedTranID"));

            this.agilityItemSearchResult = agilityItemSearch();

            this.itemGroup = this.agilityItemSearchResult.getString("ItemGroupMajor");
        }
        return this.getCardDestinationFromItemCodeResult();
    }

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

        System.out.println("\n-- AgilitySalesOrderListLookup --");
        AgilityCalls agilityAPICall = new AgilityCalls(client, contextId, "Orders/SalesOrderList", innerRequestBody);

        var response = agilityAPICall.postAgilityAPICall();


//        System.out.println(response);

        JSONObject json = response.getJSONObject("response")
                .getJSONObject("OrdersResponse")
                .getJSONObject("dsOrdersResponse");

        if(json.has("dtOrderResponse")){
            return json.getJSONArray("dtOrderResponse");
        }

        return new JSONArray();
    }

    public JSONObject agilityChangedSalesOrderListLookup() throws IOException, InterruptedException {

        JSONObject innerRequestBody = new JSONObject();

        DateTime dt = new DateTime();

        int minHold = dt.getMinuteOfHour()-2;
        int hourHold = dt.getHourOfDay();
        int dayHold = dt.getDayOfMonth();
        int monthHold = dt.getMonthOfYear();
        int yearHold = dt.getYear();

        if(minHold<0){
            minHold = dt.getMinuteOfHour()+58;
            hourHold--;
            if(hourHold<0)
                hourHold += 24;
        }

        String searchHour;
        if(hourHold<10)
            searchHour = "0" + hourHold;
        else
            searchHour = "" + hourHold;

        String searchMinute;
        if(minHold<10)
            searchMinute = "0" + minHold;
        else
            searchMinute = "" + minHold;

        String currentDay;

        if(dayHold<10)
            currentDay = "0" + dayHold;
        else
            currentDay = "" + dayHold;

        String currentMonth;

        if(monthHold<10)
            currentMonth = "0" + monthHold;
        else
            currentMonth = "" + monthHold;

        String currentYear = String.valueOf(yearHold);

        innerRequestBody.put("IncludeOpenOrders", true);
        innerRequestBody.put("IncludeInvoicedOrders", true);
        innerRequestBody.put("IncludeCanceledOrders", true);
        innerRequestBody.put("OrderDateRangeStart", "2020-01-01T01:00:00-6:00");
        innerRequestBody.put("OrderDateRangeEnd", currentYear+"-"+currentMonth+"-"+currentDay+"T23:59:59-6:00");
        innerRequestBody.put("FetchOnlyChangedSince", currentYear+"-"+currentMonth+"-"+currentDay+"T"+searchHour+":"+searchMinute+":00-6:00");

        System.out.println("\n-- AgilityChangedSalesOrderListLookup --");
        AgilityCalls agilityAPICall = new AgilityCalls(client, contextId, "Orders/SalesOrderList", innerRequestBody);
        var response = agilityAPICall.postAgilityAPICall();

        return response.getJSONObject("response")
                .getJSONObject("OrdersResponse")
                .getJSONObject("dsOrdersResponse");
    }

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

        System.out.println("- AgilityItemSearch Response -");
        AgilityCalls agilityPostCall = new AgilityCalls(client, contextId, "Inventory/ItemsList", dsItemsListRequest);
        JSONObject response =  agilityPostCall.postAgilityAPICall();


        return response.getJSONObject("response")
                .getJSONObject("ItemsListResponse")
                .getJSONObject("dsItemsListResponse")
                .getJSONArray("dtItemsListResponse")
                .getJSONObject(0);

    }

    public JSONObject getCardDestinationFromItemCodeResult(){

        String idList;
        String idLabel;
        String colorCode = null;
        String linkedType = null;
        String linkedID = null;
        String colorCustomFieldID = null;
        String rmCustomField = null;

        switch (this.itemGroup) {
            case "3300" -> {

                //kk cabinets
                idList = "62869b5c1351de037ffd2cc4";
                idLabel = "62869b5c1351de037ffd2d26";
                colorCustomFieldID = "62869b5c1351de037ffd2da7";
                rmCustomField = "62869b5c1351de037ffd2dab";
                colorCode = this.agilityItemSearchResult.getString("ItemDescription").split(" ")[0];
            }
            case "3350" -> {
                System.out.println("Success");
                //cnc cabinets
                idList = "62869b5c1351de037ffd2cc4";
                idLabel = "62869e47dcae4f52e15c90e1";
                colorCustomFieldID = "62869b5c1351de037ffd2da7";
            }
            case "3455" -> {

                //tru cabinets
                idList = "62869b5c1351de037ffd2cc4";
                idLabel = "62869db3e04b83468347996b";
                colorCustomFieldID = "62869b5c1351de037ffd2da7";
            }
            case "3450" -> {

                //choice cabinets
                idList = "62869b5c1351de037ffd2cc4";
                idLabel = "62869b5c1351de037ffd2d32";
                colorCustomFieldID = "62869b5c1351de037ffd2da7";
                rmCustomField = "62869b5c1351de037ffd2dab";
                colorCode = this.agilityItemSearchResult.getString("ItemDescription").split(" ")[0];
                linkedType = this.linkedTranType;
                linkedID = this.linkedTranID;
            }
            case "3500" -> {

                //counter tops
                idList = "60c26dfb44555566d32ae651";
                idLabel = "60c26dfc44555566d32ae700";
                colorCustomFieldID = "6197b500bbb79658801189ce";
                rmCustomField = "621519b6944e3c4fc091a515";
                if(this.agilityItemSearchResult.getString("ExtendedDescription").contains("FAB")){
                    var extDescRough = this.agilityItemSearchResult.getString("ExtendedDescription").split("-|:");
                    //TODO Not a long term parsing solution
                    colorCode = extDescRough[1].replaceFirst(" ", "") + "-" + extDescRough[2];
                }
                linkedType = this.linkedTranType;
                linkedID = this.linkedTranID;
            }
            default -> {
                idList = "61f2d5c461ac134ef274ae5f";
                idLabel = "62f6a75f8db34f1e9ac4467e";
            }
        }

        JSONObject json = new JSONObject();
        json.put("idList", idList);
        json.put("idLabel", idLabel);
        json.put("colorCustomField", colorCustomFieldID);
        json.put("rmCustomField", rmCustomField);
        json.put("colorCode", colorCode);
        json.put("linkedType", linkedType);
        json.put("linkedID", linkedID);

        return json;
    }
}

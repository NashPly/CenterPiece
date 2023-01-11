package com.CenterPiece.CenterPiece;

import com.CenterPiece.CenterPiece.APICalls.AgilityCalls;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpClient;

public class ItemCodeHandler {

    private final HttpClient client;
    private final String contextId;
    private JSONObject salesOrder = new JSONObject();
    private String salesOrderNumber = "0";
    private String itemCode;
    private String itemGroup = "None";
    private String linkedTranType;
    private String linkedTranID;
    private JSONObject agilityItemSearchResult;
    private final String branch;

    public ItemCodeHandler(HttpClient cl, String context, String branch){
        this.client = cl;
        this.contextId = context;
        this.branch = branch;
    }

    public ItemCodeHandler(HttpClient cl, String context, String salesOrderNum, JSONObject salesOrder, String branch){
        this.client = cl;
        this.contextId = context;
        this.salesOrderNumber = salesOrderNum;
        this.salesOrder = salesOrder;
        this.branch = branch;
    }

    public JSONObject itemParseProcess() {

        JSONArray salesOrderArray = agilitySalesOrderListLookup();

        System.out.println("Returned Sales Orders");

        for(int i = 0; i < salesOrderArray.length(); i++){
            if(!this.salesOrderNumber.equals("0")){
                if (this.salesOrderNumber.equals(salesOrderArray.getJSONObject(i).getNumber("OrderID").toString())) {
                    this.salesOrder = salesOrderArray.getJSONObject(i);
                }
            }
        }

        System.out.println("Populated salesOrder: " + this.salesOrder);

        if (!(this.salesOrder == null) && this.salesOrder.has("dtOrderDetailResponse")) {
            JSONArray salesOrderItemsArray = this.salesOrder
                    .getJSONArray("dtOrderDetailResponse");
            var item = salesOrderItemsArray.getJSONObject(0);
            this.itemCode = item.getString("ItemCode");
            this.linkedTranType = item.getString("LinkedTranType");
            this.linkedTranID = String.valueOf(item.getInt("LinkedTranID"));

            System.out.println("\n-- agilityItemSearchResult --");
            this.agilityItemSearchResult = agilityItemSearch();

            if(this.agilityItemSearchResult != null)
            this.itemGroup = this.agilityItemSearchResult.getString("ItemGroupMajor");
            else if(this.itemCode != null){
                System.out.println("\n--- This Item Search was Null: " + this.itemCode + " ---");
            }
            else if(item != null){
                System.out.println("\n--- There was no item code in here: \n" + item +"---");
            }
            else{
                System.out.println("Something isn'T alright here or there are no line items");
            }

        }
        return this.getCardDestinationFromItemCodeResult();
    }

    public JSONArray agilitySalesOrderListLookup() {

        JSONObject innerRequestBody = new JSONObject();

        TimeHandler timeHandler = new TimeHandler();

        innerRequestBody.put("IncludeOpenOrders", true);
        innerRequestBody.put("IncludeInvoicedOrders", false);
        innerRequestBody.put("IncludeCanceledOrders", false);
        innerRequestBody.put("OrderDateRangeStart", timeHandler.getCurrentYear() + "-" + timeHandler.getCurrentMonth() + "-" +
                timeHandler.getCurrentDayOfMonth() + "T00:00:01-6:00");
                //"05T00:00:01-6:00");
        innerRequestBody.put("OrderDateRangeEnd", timeHandler.getCurrentYear() + "-" + timeHandler.getCurrentMonth() + "-" +
                timeHandler.getCurrentDayOfMonth() + "T"+"23:59:59-6:00");

        System.out.println("\n-- AgilitySalesOrderListLookup --");
        AgilityCalls agilityAPICall = new AgilityCalls(this.client, this.contextId, "Orders/SalesOrderList", innerRequestBody, this.branch);

        var response = agilityAPICall.postAgilityAPICall();

        JSONObject json = response.getJSONObject("response")
                .getJSONObject("OrdersResponse")
                .getJSONObject("dsOrdersResponse");

        if(json.has("dtOrderResponse")){
            return json.getJSONArray("dtOrderResponse");
        }

        return new JSONArray();
    }

    public JSONObject agilityChangedSalesOrderListLookup() {

        JSONObject innerRequestBody = new JSONObject();

//        DateTime dtus = new DateTime();
//        DateTimeZone dtZone = DateTimeZone.forID("America/Chicago");
//        DateTime dt = dtus.withZone(dtZone);
//
//        int minHold = dt.getMinuteOfHour()-2;
//        int hourHold = dt.getHourOfDay();
//        int dayHold = dt.getDayOfMonth();
//        int monthHold = dt.getMonthOfYear();
//        int yearHold = dt.getYear();
//
//        if(minHold<0){
//            minHold = dt.getMinuteOfHour()+56;
//            hourHold--;
//            if(hourHold<0)
//                hourHold += 24;
//        }
//
//        String searchHour;
//        if(hourHold<10)
//            searchHour = "0" + hourHold;
//        else
//            searchHour = "" + hourHold;
//
//        String searchMinute;
//        if(minHold<10)
//            searchMinute = "0" + minHold;
//        else
//            searchMinute = "" + minHold;
//
//        String currentDay;
//
//        if(dayHold<10)
//            currentDay = "0" + dayHold;
//        else
//            currentDay = "" + dayHold;
//
//        String currentMonth;
//
//        if(monthHold<10)
//            currentMonth = "0" + monthHold;
//        else
//            currentMonth = "" + monthHold;
//
//        String currentYear = String.valueOf(yearHold);

        TimeHandler timeHandler = new TimeHandler();

        innerRequestBody.put("IncludeOpenOrders", true);
        innerRequestBody.put("IncludeInvoicedOrders", true);
        innerRequestBody.put("IncludeCanceledOrders", true);
        innerRequestBody.put("OrderDateRangeStart", "2020-01-01T01:00:00-6:00");
        innerRequestBody.put("OrderDateRangeEnd", timeHandler.getCurrentYear() + "-" +
                timeHandler.getCurrentMonth() + "-" + timeHandler.getCurrentDayOfMonth() + "T23:59:59-6:00");
        innerRequestBody.put("FetchOnlyChangedSince", timeHandler.getSearchYear() + "-" +
                timeHandler.getSearchMonth() + "-" + timeHandler.getSearchDayOfMonth() +
                "T" + timeHandler.getSearchHour() + ":" + timeHandler.getSearchMinuteOfHour() + ":00-6:00");

        System.out.println("\n-- AgilityChangedSalesOrderListLookup --");
        AgilityCalls agilityAPICall = new AgilityCalls(client, contextId, "Orders/SalesOrderList", innerRequestBody, branch);
        var response = agilityAPICall.postAgilityAPICall();

        return response.getJSONObject("response")
                .getJSONObject("OrdersResponse")
                .getJSONObject("dsOrdersResponse");
    }

    public JSONObject agilityItemSearch() {

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
        AgilityCalls agilityPostCall = new AgilityCalls(client, contextId, "Inventory/ItemsList", dsItemsListRequest, branch);
        JSONObject response =  agilityPostCall.postAgilityAPICall();

        return response.getJSONObject("response")
                .getJSONObject("ItemsListResponse")
                .getJSONObject("dsItemsListResponse")
                .getJSONArray("dtItemsListResponse")
                .getJSONObject(0);

    }

    public JSONObject getCardDestinationFromItemCodeResult(){

        String boardID;
        String idList = null;
        String idLabel = null;
        String colorCode = null;
        String linkedType = null;
        String linkedID = null;
        String colorCustomFieldID = null;
        String rmCustomField = null;

        //TODO Could to enum for status

        //TODO figure out order status in IF statement
        // Set status variable to go into the various cases


        switch (this.itemGroup) {
            case "1900", "3700" -> {

                boardID = "636bc3a95da9340015e47b84";
                //idList = "636bc3a95da9340015e47b8b";
                idList = orderStatusLogic("Components");
                colorCustomFieldID="636bc3aa5da9340015e47ce4";
                rmCustomField = "636bc3aa5da9340015e47ce8";

            }
            case "3300" -> {
                //kk cabinets

                //idList = "62869b5c1351de037ffd2cc4";
                boardID = "62869b5c1351de037ffd2cbb";
                idList = orderStatusLogic("Cabinets");
                idLabel = "62869b5c1351de037ffd2d26";
                colorCustomFieldID = "62869b5c1351de037ffd2da7";
                rmCustomField = "62869b5c1351de037ffd2dab";
                colorCode = this.agilityItemSearchResult.getString("ItemDescription").split(" ")[0];
            }
            case "3350" -> {
                //cnc cabinets

                //idList = "62869b5c1351de037ffd2cc4";
                boardID = "62869b5c1351de037ffd2cbb";
                idList = orderStatusLogic("Cabinets");
                idLabel = "62869e47dcae4f52e15c90e1";
                colorCustomFieldID = "62869b5c1351de037ffd2da7";

            }
            case "3455" -> {
                //tru cabinets

                //idList = "62869b5c1351de037ffd2cc4";
                boardID = "62869b5c1351de037ffd2cbb";
                idList = orderStatusLogic("Cabinets");
                idLabel = "62869db3e04b83468347996b";
                colorCustomFieldID = "62869b5c1351de037ffd2da7";

            }
            case "3450" -> {
                //choice cabinets

                //idList = "62869b5c1351de037ffd2cc4";
                boardID = "62869b5c1351de037ffd2cbb";
                idList = orderStatusLogic("Cabinets");
                idLabel = "62869b5c1351de037ffd2d32";
                colorCustomFieldID = "62869b5c1351de037ffd2da7";
                rmCustomField = "62869b5c1351de037ffd2dab";
                colorCode = this.agilityItemSearchResult.getString("ItemDescription").split(" ")[0];
                linkedType = this.linkedTranType;
                linkedID = this.linkedTranID;

            }
            case "3500" -> {

                //TODO add in dynamic locations
                // whether it be in side the CASE or determined globally within the function
                // check to see OrderStatus to determine placement
                // pass that in to determine respective location

                //counter tops

                //idList = "60c26dfb44555566d32ae651";
                //System.out.println(orderStatusLogic("Tops"));
                boardID = "60c26dfb44555566d32ae643";
                idList = orderStatusLogic("Tops");
                idLabel = "60c26dfc44555566d32ae700";
                colorCustomFieldID = "6197b500bbb79658801189ce";
                rmCustomField = "621519b6944e3c4fc091a515";

                if(this.agilityItemSearchResult.getString("ExtendedDescription").contains("FAB") || this.agilityItemSearchResult.getString("ExtendedDescription").contains("SLAB")){
                    var extDescRough = this.agilityItemSearchResult.getString("ExtendedDescription").split("-|:");
                    //TODO Not a long term parsing solution
                    colorCode = extDescRough[1].replaceFirst(" ", "") + "-" + extDescRough[2];
                }
                linkedType = this.linkedTranType;
                linkedID = this.linkedTranID;
            }
            default -> {
                boardID = "None Found";
            }
        }

        JSONObject json = new JSONObject();
        json.put("boardID", boardID);
        json.put("idList", idList);
        json.put("idLabel", idLabel);
        json.put("colorCustomField", colorCustomFieldID);
        json.put("rmCustomField", rmCustomField);
        json.put("colorCode", colorCode);
        json.put("linkedType", linkedType);
        json.put("linkedID", linkedID);

        return json;
    }

    public String orderStatusLogic(String board){

        JSONObject itemDetails;

        if(!(this.salesOrder == null) && this.salesOrder.has("dtOrderDetailResponse")) {
             itemDetails = this.salesOrder.getJSONArray("dtOrderDetailResponse").getJSONObject(0);
        } else {
            System.out.println(" - " + board + " Inbox - ");
            return whichBoard( new TrelloListIDs(TrelloLists.INBOX, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.INBOX, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.INBOX, "COMPONENTS").getListID(), board);
        }

        String orderStatus = this.salesOrder.getString("OrderStatus");
        String orderProcessStatus = this.salesOrder.getString("OrderProcessStatus");
        String saleType = this.salesOrder.getString("SaleType"); //WHSE or WILLCALL

        if(orderStatus.equals("Open")){

            switch(orderProcessStatus){
                case "Picked" -> {
                    if(saleType.equals("WHSE")) {

                        System.out.println(" - " + board + " Picked - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "COMPONENTS").getListID(), board);
                    } else if (saleType.equals("WILLCALL")){

                        System.out.println(" - " + board + " Picked - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "COMPONENTS").getListID(), board);
                    }
                }
                case "Staged" -> {
                    if(saleType.equals("WHSE")) {
                        System.out.println(" - " + board + " Staged - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.ON_TRUCK_ON_DELIVERY, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.ON_TRUCK_ON_DELIVERY, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.ON_TRUCK_ON_DELIVERY, "COMPONENTS").getListID(), board);
                    }else if(saleType.equals("WILLCALL")) {
                        System.out.println(" - " + board + " Willcall - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.WILL_CALL, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.WILL_CALL, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.WILL_CALL, "COMPONENTS").getListID(), board);
                    }
                }
                case "" -> {
                    //Fresh order

                    //In Production
                    if(itemDetails.getDouble("TotalBackorderedQuantity") == 1.0 &&
                            itemDetails.getDouble("TotalUnstagedQuantity") == 0.0  &&
                            itemDetails.getDouble("TotalStagedQuantity")== 0.0 &&
                            itemDetails.getDouble("TotalInvoicedQuantity") == 0.0){

                        if (itemDetails.has("LinkedTranType")) {
                            System.out.println(" - " + board + " Processing || Batching - ");
                            return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.BATCHING, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS").getListID(), board);
                        } else {

                            //In Processing
                            System.out.println(" - " + board + " Processing || Batching - ");
                            return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.BATCHING, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS").getListID(), board);

                        }
                    }else if(itemDetails.getDouble("TotalBackorderedQuantity") == 0.0 &&
                            itemDetails.getDouble("TotalUnstagedQuantity") == 1.0  &&
                            itemDetails.getDouble("TotalStagedQuantity")== 0.0 &&
                            itemDetails.getDouble("TotalInvoicedQuantity") == 0.0){

                        //To Be Picked
                        System.out.println(" - " + board + " To Be Picked - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.TO_BE_PICKED, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.TO_BE_PICKED, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.TO_BE_PICKED, "COMPONENTS").getListID(), board);
                    }
                }
            }

        }else if(orderStatus.equals("Invoiced")) {
            System.out.println(" - " + board + " Invoiced - ");
            return whichBoard( new TrelloListIDs(TrelloLists.INVOICED, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.INVOICED, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.INVOICED, "COMPONENTS").getListID(), board);

        }else{
            System.out.println(" - " + board + " Processing || Batching - ");
            return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.BATCHING, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS").getListID(), board);
        }
        System.out.println(" - " + board + " Processing || Batching - ");


        return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS").getListID(), new TrelloListIDs(TrelloLists.BATCHING, "TOPSHOP").getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS").getListID(), board);
    }

    public String whichBoard(String cabList, String topList, String compList, String boardName){
        String destination = "";
        switch(boardName){
            case "Tops" -> destination = topList;
            case "Cabinets" -> destination = cabList;
            case "Components" -> destination = compList;

        }
        return destination;
    }
}

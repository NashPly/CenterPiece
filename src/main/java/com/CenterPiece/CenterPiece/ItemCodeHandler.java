package com.CenterPiece.CenterPiece;

import com.CenterPiece.CenterPiece.APICalls.AgilityCalls;
import com.CenterPiece.CenterPiece.TrelloIDs.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public class ItemCodeHandler {

    private final HttpClient client;
    private final String contextId;
    private final String environment;
    private JSONObject salesOrder = new JSONObject();
    private String salesOrderNumber = "0";
    private String itemCode;
    private String itemGroup = "None";
    private String linkedTranType;
    private String linkedTranPO = "PO";
    private String linkedTranRM = "RM";
    private String linkedTranID;
    private String linkedTranPoID;
    private String linkedTranRmID;
    private Integer countOfBuilds;
    private JSONObject agilityItemSearchResult;
    private final String branch;

    public ItemCodeHandler(HttpClient cl, String context, String branch, String environment){
        this.client = cl;
        this.contextId = context;
        this.branch = branch;
        this.environment = environment;
    }

    public ItemCodeHandler(HttpClient cl, String context, String salesOrderNum, JSONObject salesOrder, String branch, String environment){
        this.client = cl;
        this.contextId = context;
        this.salesOrderNumber = salesOrderNum;
        this.salesOrder = salesOrder;
        this.branch = branch;
        this.environment = environment;
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

        if (!(this.salesOrder == null)) {
            System.out.println("\n -- This sales order isn't null --");
            this.salesOrderNumber = String.valueOf(this.salesOrder.getNumber("OrderID"));
            JSONArray salesOrderItemsArray = null;
            if(this.salesOrder.has("dtOrderDetailResponse")){
                System.out.println("\n -- This item has detail response --");
                salesOrderItemsArray = new JSONArray(this.salesOrder
                    .getJSONArray("dtOrderDetailResponse"));
            }

            JSONObject item = null;

            if(salesOrderItemsArray != null){
                if(!salesOrderItemsArray.isEmpty()){
                    this.countOfBuilds = checkForNumOfCabBuilds(salesOrderItemsArray, salesOrderItemsArray.length());
                    System.out.println("Count of CBUILDS " + countOfBuilds);

                    System.out.println("\n -- This sales order has an item --");

                    List<Integer> linkedPOList = new ArrayList<>();
                    List<Integer> linkedRMList = new ArrayList<>();


                    for(int i = 0; i < salesOrderItemsArray.length(); i++){
                        item = salesOrderItemsArray.getJSONObject(i);

                        switch (item.getString("LinkedTranType")) {
                            case "PO" -> {
                                if(!linkedPOList.contains(item.getInt("LinkedTranID")) && !(item.getInt("LinkedTranID") == 0))
                                    linkedPOList.add(item.getInt("LinkedTranID"));
                            }
                            case "RM" -> {
                                if(!linkedRMList.contains(item.getInt("LinkedTranID")) && !(item.getInt("LinkedTranID") == 0))
                                    linkedRMList.add(item.getInt("LinkedTranID"));
                            }
                        }
                    }

                    this.linkedTranPoID = linkedPOList.toString().replace("[","").replace("]", "");
                    this.linkedTranRmID = linkedRMList.toString().replace("[","").replace("]", "");;

                    this.itemCode = salesOrderItemsArray.getJSONObject(0).getString("ItemCode");
//                this.linkedTranType = item.getString("LinkedTranType");
//                this.linkedTranID = String.valueOf(item.getInt("LinkedTranID"));
                    System.out.println("\n-- agilityItemSearchResult --");
                    this.agilityItemSearchResult = agilityItemSearch();
                }
            }
            System.out.println("\n -- Item defined and searched in Agility --");

            if(this.agilityItemSearchResult != null && !this.agilityItemSearchResult.has("Empty")){
            this.itemGroup = this.agilityItemSearchResult.getString("ItemGroupMajor");
                if(this.itemCode != null){
                    System.out.println("\n--- This Item Search was Null: " + this.itemCode + " ---");
                }
                else if(item != null){
                    System.out.println("\n--- There was no item code in here: \n" + item +"---");
                }
                else{
                    System.out.println("Something isn't alright here or there are no line items");
                }
            }
            System.out.println("\n -- Item checked if valid --");
        }
        return this.getCardDestinationFromItemCodeResult();
    }

    private int checkForNumOfCabBuilds(JSONArray salesOrderItemsArray, int length) {
        JSONObject item = null;
        String itemCode = "";
        for(int i=0; i < length; i++){
            item = salesOrderItemsArray.getJSONObject(i);
            itemCode = item.getString("ItemCode");
            if(itemCode.equals("CBUILD")){
                return item.getInt("TotalOrderedQuantity");
            }
        }
        return 0;
    }

    public JSONArray agilitySalesOrderListLookup() {

        JSONObject innerRequestBody = new JSONObject();

        TimeHandler timeHandler = new TimeHandler();

        innerRequestBody.put("IncludeOpenOrders", true);
        innerRequestBody.put("IncludeInvoicedOrders", true);
        innerRequestBody.put("IncludeCanceledOrders", false);
        innerRequestBody.put("OrderDateRangeStart",
                timeHandler.getYesterdaysYear() + "-" +
                timeHandler.getYesterdaysMonth() + "-" +
                timeHandler.getYesterdaysDayOfMonth() + "T00:00:01-6:00");
//            //Year
//                "2023-"+
//            //Month
//                 "10-" +
//            //Day
//                 "21T00:00:01-6:00");

        innerRequestBody.put("OrderDateRangeEnd",
                timeHandler.getCurrentYear() + "-" +
                timeHandler.getCurrentMonth() + "-" +
                timeHandler.getCurrentDayOfMonth() + "T23:59:59-6:00");

//        //Year
//            "2023-"+
//        //Month
//            "11-" +
//        //Day
//            "01T23:59:59-6:00");

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

        TimeHandler timeHandler = new TimeHandler();

        innerRequestBody.put("IncludeOpenOrders", true);
        innerRequestBody.put("IncludeInvoicedOrders", true);
        innerRequestBody.put("IncludeCanceledOrders", true);
        innerRequestBody.put("OrderDateRangeStart", "2020-01-01T01:00:00-6:00");
        innerRequestBody.put("OrderDateRangeEnd",
                timeHandler.getCurrentYear() + "-" +
                timeHandler.getCurrentMonth() + "-" +
                timeHandler.getCurrentDayOfMonth() + "T23:59:59-6:00");
        //Year
                //"2023-"+
        //Month
                // "10-" +
        //Day
                // "01T23:59:59-6:00");

        innerRequestBody.put("FetchOnlyChangedSince",
                timeHandler.getSearchYear() + "-" +
                timeHandler.getSearchMonth() + "-" +
                timeHandler.getSearchDayOfMonth() +
                //Year
                    //"2023-"+
                //Month
                    // "10-" +
                //Day
                    // "01" +
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

        if(!response.getJSONObject("response")
                .getJSONObject("ItemsListResponse")
                .getJSONObject("dsItemsListResponse").isEmpty()) {
            return response.getJSONObject("response")
                    .getJSONObject("ItemsListResponse")
                    .getJSONObject("dsItemsListResponse")
                    .getJSONArray("dtItemsListResponse")
                    .getJSONObject(0);
        } else {
            return new JSONObject().put("Empty", true);
        }
    }

    public JSONObject getCardDestinationFromItemCodeResult(){

        String boardID = null;
        String idList = null;
        String idLabel = null;
        String colorCode = null;
        String linkedType = null;
        String linkedRmID = null;
        String linkedPoID = null;
        String colorCustomFieldID = null;
        String rmCustomField = null;
        String agilityPoCustomField = null;
        String customerPoCustomField = null;
        String countOfBuildsCustomField = null;

        //TODO Could to enum for status

        //TODO figure out order status in IF statement
        // Set status variable to go into the various cases


        switch (this.itemGroup) {
            case "1000", "1900", "2000", "3600", "3700",  "9800" -> {

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.COMPONENTS, "COMPONENTS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                idList = orderStatusLogic("COMPONENTS", this.salesOrder, this.environment, this.linkedTranPoID);

                TrelloLabelIds trelloLabelIds = new TrelloLabelIds(TrelloLabels.COMPONENTS, "COMPONENTS", this.environment, this.salesOrder.getString("SaleType"));
                idLabel = trelloLabelIds.getAllRelevantLabelIds();

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "COMPONENTS", this.environment);
                TrelloCustomFieldIDs trelloRemanCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.REMAN_NUMBER, "COMPONENTS", this.environment);

                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                rmCustomField = trelloRemanCustomFieldID.getFieldID();

            }
            case "3300" -> {
                //kk cabinets

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, "CABINETS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                idList = orderStatusLogic("CABINETS", this.salesOrder, this.environment, this.linkedTranPoID);

                TrelloLabelIds trelloLabelIds = new TrelloLabelIds(TrelloLabels.KK_CABINET, "CABINETS", this.environment, this.salesOrder.getString("SaleType"));
                idLabel = trelloLabelIds.getAllRelevantLabelIds();

                colorCode = this.agilityItemSearchResult.getString("ItemDescription").split(" ")[0];
                linkedType = this.linkedTranType;
                //
                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloAgilityPoCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.AGILITY_PO_NUMBER, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloCustomerPoFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.CUSTOMER_PO_NUMBER, "CABINETS", this.environment);

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                agilityPoCustomField = trelloAgilityPoCustomFieldID.getFieldID();
                customerPoCustomField = trelloCustomerPoFieldID.getFieldID();

            }
            case "3350" -> {
                //cnc cabinets

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, "CABINETS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                idList = orderStatusLogic("CABINETS", this.salesOrder, this.environment, this.linkedTranPoID);

                TrelloLabelIds trelloLabelIds = new TrelloLabelIds(TrelloLabels.CNC_CABINET, "CABINETS", this.environment, this.salesOrder.getString("SaleType"));
                idLabel = trelloLabelIds.getAllRelevantLabelIds();

                linkedType = this.linkedTranType;
                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloAgilityPoCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.AGILITY_PO_NUMBER, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloCustomerPoFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.CUSTOMER_PO_NUMBER, "CABINETS", this.environment);

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                agilityPoCustomField = trelloAgilityPoCustomFieldID.getFieldID();
                customerPoCustomField = trelloCustomerPoFieldID.getFieldID();

            }
            case "3455" -> {
                //legacy cabinets

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, "CABINETS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                idList = orderStatusLogic("CABINETS", this.salesOrder, this.environment, this.linkedTranPoID);

                TrelloLabelIds trelloLabelIds = new TrelloLabelIds(TrelloLabels.LEGACY_CABINET, "CABINETS", this.environment, this.salesOrder.getString("SaleType"));
                idLabel = trelloLabelIds.getAllRelevantLabelIds();

                linkedType = this.linkedTranType;
                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloAgilityPoCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.AGILITY_PO_NUMBER, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloCustomerPoFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.CUSTOMER_PO_NUMBER, "CABINETS", this.environment);

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                agilityPoCustomField = trelloAgilityPoCustomFieldID.getFieldID();
                customerPoCustomField = trelloCustomerPoFieldID.getFieldID();

            }
            case "3450" -> {
                //choice cabinets

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, "CABINETS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                idList = orderStatusLogic("CABINETS", this.salesOrder, this.environment, this.linkedTranPoID);

                TrelloLabelIds trelloLabelIds = new TrelloLabelIds(TrelloLabels.CHOICE_CABINET, "CABINETS", this.environment, this.salesOrder.getString("SaleType"));
                idLabel = trelloLabelIds.getAllRelevantLabelIds();

                colorCode = this.agilityItemSearchResult.getString("ItemDescription").split(" ")[0];
                linkedType = this.linkedTranType;
                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloRemanCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.REMAN_NUMBER, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloAgilityPoCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.AGILITY_PO_NUMBER, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloCustomerPoFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.CUSTOMER_PO_NUMBER, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloNoOfBuildsFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.NUMBER_OF_BUILDS, "CABINETS", this.environment);

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                rmCustomField = trelloRemanCustomFieldID.getFieldID();
                agilityPoCustomField = trelloAgilityPoCustomFieldID.getFieldID();
                customerPoCustomField = trelloCustomerPoFieldID.getFieldID();
                countOfBuildsCustomField = trelloNoOfBuildsFieldID.getFieldID();

            }
//            case "3100" -> {
//                //choice cabinets
//
//                //idList = "62869b5c1351de037ffd2cc4";
//
//                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, "CABINETS", this.environment);
//                boardID = trelloBoardIDs.getBoardID();
//
////                boardID = "62869b5c1351de037ffd2cbb";
//                idList = orderStatusLogic("CABINETS", this.salesOrder, this.environment, this.linkedTranPoID);
//
//                TrelloLabelIds trelloLabelIds = new TrelloLabelIds(TrelloLabels.CHOICE_CABINET, "CABINETS", this.environment, this.salesOrder.getString("SaleType"));
//                idLabel = trelloLabelIds.getAllRelevantLabelIds();
//
////                if(this.environment.equals("Production")){
////                    idLabel = "62869b5c1351de037ffd2d32";
////                    if(this.salesOrder.getString("SaleType").equals("WHSE")){
////                        idLabel = idLabel + ",65a9526ced9de1398df49ae3";
////                    }else if(this.salesOrder.getString("SaleType").equals("WILLCALL"))
////                        idLabel = idLabel + ",65a952409cdbee377a23b6f7";
////                }
//
//                colorCode = this.agilityItemSearchResult.getString("ItemDescription").split(" ")[0];
//                linkedType = this.linkedTranType;
//                linkedPoID = this.linkedTranPoID;
//                linkedRmID = this.linkedTranRmID;
//
//                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "CABINETS", this.environment);
//                TrelloCustomFieldIDs trelloRemanCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.REMAN_NUMBER, "CABINETS", this.environment);
//                TrelloCustomFieldIDs trelloAgilityPoCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.AGILITY_PO_NUMBER, "CABINETS", this.environment);
//                TrelloCustomFieldIDs trelloCustomerPoFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.CUSTOMER_PO_NUMBER, "CABINETS", this.environment);
//                TrelloCustomFieldIDs trelloNoOfBuildsFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.NUMBER_OF_BUILDS, "CABINETS", this.environment);
//
//                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
//                rmCustomField = trelloRemanCustomFieldID.getFieldID();
//                agilityPoCustomField = trelloAgilityPoCustomFieldID.getFieldID();
//                customerPoCustomField = trelloCustomerPoFieldID.getFieldID();
//                countOfBuildsCustomField = trelloNoOfBuildsFieldID.getFieldID();
//
//            }
            case "3500" -> {

                //counter tops

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.TOP_SHOP, "TOPSHOP", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                idList = orderStatusLogic("TOPSHOP", this.salesOrder, this.environment, this.linkedTranPoID);

                TrelloLabelIds trelloLabelIds = new TrelloLabelIds(TrelloLabels.TOPS, "TOPSHOP", this.environment, this.salesOrder.getString("SaleType"));
                idLabel = trelloLabelIds.getAllRelevantLabelIds();

                linkedType = this.linkedTranType;
                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;


                if(this.agilityItemSearchResult.getString("ExtendedDescription").matches("((F|f)|(S|sL|l))(A|a)(B|b)(S|s)? - [A-z ]{2,40}\\d{3,4}(K|k)?-(\\d{2}|[A-z]{2}) (\\d\"? )?[A-z]{2,20} - .{0,60}")){
                    var extDescRough = this.agilityItemSearchResult.getString("ExtendedDescription").split("-|:");
                    //TODO Not a long term parsing solution
                    colorCode = extDescRough[1].replaceFirst(" ", "") + "-" + extDescRough[2];
                } else {
                    colorCode = "Invalid Color Code Format";
                }

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "TOPSHOP", this.environment);
                TrelloCustomFieldIDs trelloRemanCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.REMAN_NUMBER, "TOPSHOP", this.environment);
                TrelloCustomFieldIDs trelloAgilityPoCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.AGILITY_PO_NUMBER, "TOPSHOP", this.environment);
                TrelloCustomFieldIDs trelloCustomerPoFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.CUSTOMER_PO_NUMBER, "TOPSHOP", this.environment);

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                rmCustomField = trelloRemanCustomFieldID.getFieldID();
                agilityPoCustomField = trelloAgilityPoCustomFieldID.getFieldID();
                customerPoCustomField = trelloCustomerPoFieldID.getFieldID();

            }
            default -> {
                System.out.println("No item shared. Placing it in Top Shop Inbox List");

                TrelloBoardIDs trelloBoardIDs = null;
                TrelloLabelIds trelloLabelIds = null;
                String branch = null;

                switch(this.branch){
                    case "CABINETS" -> {
                        branch = "CABINETS";
                        trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, branch, this.environment);
                        idList = orderStatusLogic("CABINETS", this.salesOrder, this.environment, this.linkedTranPoID);
                    }
                    case "FABRICATION" -> {
                        branch = "TOPSHOP";
                        trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.TOP_SHOP, branch, this.environment);
                        idList = orderStatusLogic("TOPSHOP", this.salesOrder, this.environment, this.linkedTranPoID);
                    }
                    case "COMPONENTS" ->{
                        branch = "COMPONENTS";
                        trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.COMPONENTS, branch, this.environment);
                        idList = orderStatusLogic("COMPONENTS", this.salesOrder, this.environment, this.linkedTranPoID);
                    }

                }

                assert trelloBoardIDs != null;
                boardID = trelloBoardIDs.getBoardID();

                linkedType = this.linkedTranType;
                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, branch, this.environment);
                TrelloCustomFieldIDs trelloRemanCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.REMAN_NUMBER, branch, this.environment);
                TrelloCustomFieldIDs trelloAgilityPoCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.AGILITY_PO_NUMBER, branch, this.environment);
                TrelloCustomFieldIDs trelloCustomerPoFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.CUSTOMER_PO_NUMBER, branch, this.environment);

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                rmCustomField = trelloRemanCustomFieldID.getFieldID();
                agilityPoCustomField = trelloAgilityPoCustomFieldID.getFieldID();
                customerPoCustomField = trelloCustomerPoFieldID.getFieldID();
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
        json.put("linkedPoID", linkedPoID);
        json.put("linkedRmID", linkedRmID);
        json.put("agilityPoCustomField", agilityPoCustomField);
        json.put("customerPoCustomField", customerPoCustomField);
        json.put("countOfBuildsCustomField", countOfBuildsCustomField);
        json.put("countOfBuilds", this.countOfBuilds);

        return json;
    }

    public String orderStatusLogic(String board, JSONObject salesOrder, String environment, String linkedTranPoID){

        JSONObject itemDetails;
        Boolean hasBackOrderedItems = null;
        Boolean hasUnstagedItems = null;
        Boolean hasStagedItems = null;
        Boolean hasInvoicedItems = null;

        if(!(salesOrder == null) && salesOrder.has("dtOrderDetailResponse")) {
            //TODO insert for loop to receive total data

//            if(){
                hasBackOrderedItems = checkIfTrue(salesOrder.getJSONArray("dtOrderDetailResponse"), "TotalBackorderedQuantity");
                hasUnstagedItems = checkIfTrue(salesOrder.getJSONArray("dtOrderDetailResponse"), "TotalUnstagedQuantity");
                hasStagedItems = checkIfTrue(salesOrder.getJSONArray("dtOrderDetailResponse"), "TotalStagedQuantity");
                hasInvoicedItems = checkIfTrue(salesOrder.getJSONArray("dtOrderDetailResponse"), "TotalInvoicedQuantity");
                itemDetails = salesOrder.getJSONArray("dtOrderDetailResponse").getJSONObject(0);
//            }else {
//                System.out.println(" - " + board + " Inbox - ");
//                return whichBoard( new TrelloListIDs(TrelloLists.INBOX, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.INBOX, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.INBOX, "COMPONENTS", environment).getListID(), board);
//            }

        } else {
            System.out.println(" - " + board + " Inbox - ");
            return whichBoard( new TrelloListIDs(TrelloLists.INBOX, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.INBOX, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.INBOX, "COMPONENTS", environment).getListID(), board);
        }

        String orderStatus = salesOrder.getString("OrderStatus");
        String orderProcessStatus = salesOrder.getString("OrderProcessStatus");
        String saleType = salesOrder.getString("SaleType"); //WHSE or WILLCALL

        if(orderStatus.equals("Open")){

            switch(orderProcessStatus){
                case "" -> {
                    //Fresh order

                    //In Production
//                    if(itemDetails.getDouble("TotalBackorderedQuantity") >= 1.0 &&
//                            itemDetails.getDouble("TotalUnstagedQuantity") == 0.0  &&
//                            itemDetails.getDouble("TotalStagedQuantity")== 0.0 &&
//                            itemDetails.getDouble("TotalInvoicedQuantity") == 0.0){
                    if(hasBackOrderedItems){

//                        if (itemDetails.has("LinkedTranType")) {
                        if (!linkedTranPoID.isEmpty()) {
                            System.out.println(" - " + board + " Processing || Batching - ");
                            return whichBoard( new TrelloListIDs(TrelloLists.TO_BE_ORDERED, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.DRAWING, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", environment).getListID(), board);
                        } else {
                            //In Processing
                            System.out.println(" - " + board + " Processing || Batching - ");
                            return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.DRAWING, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", environment).getListID(), board);
                        }
//                    }else if(itemDetails.getDouble("TotalBackorderedQuantity") == 0.0 &&
//                            itemDetails.getDouble("TotalUnstagedQuantity") >= 1.0  &&
//                            itemDetails.getDouble("TotalStagedQuantity")== 0.0 &&
//                            itemDetails.getDouble("TotalInvoicedQuantity") == 0.0){
                    }else if(hasUnstagedItems){

                        //To Be Picked
                        System.out.println(" - " + board + " To Be Picked - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.TO_BE_PICKED, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.TO_BE_PICKED, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.TO_BE_PICKED, "COMPONENTS", environment).getListID(), board);
                    }
                }
                case "Picked" -> {
                    if(hasBackOrderedItems){
                        if (!linkedTranPoID.isEmpty()) {
                            System.out.println(" - " + board + " Processing || Batching - ");
                            return whichBoard( new TrelloListIDs(TrelloLists.TO_BE_ORDERED, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.DRAWING, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", environment).getListID(), board);
                        } else {
                            //In Processing
                            System.out.println(" - " + board + " Processing || Batching - ");
                            return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.DRAWING, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", environment).getListID(), board);

                        }
                    }
                    if(saleType.equals("WHSE")) {

                        System.out.println(" - " + board + " Picked - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.SENT_TO_PICK, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.SENT_TO_PICK, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.SENT_TO_PICK, "COMPONENTS", environment).getListID(), board);
                    } else if (saleType.equals("WILLCALL")){

                        System.out.println(" - " + board + " Picked - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.SENT_TO_PICK, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.SENT_TO_PICK, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.SENT_TO_PICK, "COMPONENTS", environment).getListID(), board);
                    }
                }
                case "Staged" -> {
                    if(saleType.equals("WHSE")) {
                        System.out.println(" - " + board + " Staged - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.ON_TRUCK_ON_DELIVERY, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.ORDER_STAGED, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.ON_TRUCK_ON_DELIVERY, "COMPONENTS", environment).getListID(), board);
                    }else if(saleType.equals("WILLCALL")) {
                        System.out.println(" - " + board + " Willcall - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.WILL_CALL, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.WILL_CALL, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.WILL_CALL, "COMPONENTS", environment).getListID(), board);
                    }
                }
            }

        }else if(orderStatus.equals("Invoiced")) {
            System.out.println(" - " + board + " Invoiced - ");
            return whichBoard( new TrelloListIDs(TrelloLists.INVOICED, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.INVOICED, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.INVOICED, "COMPONENTS", environment).getListID(), board);

        }else if(orderStatus.equals("Canceled")) {
        System.out.println(" - " + board + " Canceled - ");
        return whichBoard( new TrelloListIDs(TrelloLists.CANCELED, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.CANCELED, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.CANCELED, "COMPONENTS", environment).getListID(), board);

    }else{
            System.out.println(" - " + board + " Processing || Batching - ");
            return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.DRAWING, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", environment).getListID(), board);
        }
        System.out.println(" - " + board + " Processing || Batching - ");


        return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS", environment).getListID(), new TrelloListIDs(TrelloLists.DRAWING, "TOPSHOP", environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", environment).getListID(), board);
    }

    public boolean checkIfTrue(JSONArray itemList, String key){

        for (int i = 0; i < itemList.length(); i++){
            if(itemList.getJSONObject(i).getDouble(key) != 0.0) return true;
        }
        return false;
    }

    public String whichBoard(String cabList, String topList, String compList, String boardName){
        String destination = "";
        switch(boardName){
            case "TOPSHOP" -> destination = topList;
            case "CABINETS" -> destination = cabList;
            case "COMPONENTS" -> destination = compList;

        }
        return destination;
    }
}

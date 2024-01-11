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

            if(salesOrderItemsArray != null && !salesOrderItemsArray.isEmpty()){

                this.countOfBuilds = checkForNumOfCabBuilds(salesOrderItemsArray, salesOrderItemsArray.length());
                System.out.println("Count of CBUILDS " + countOfBuilds);

                System.out.println("\n -- This sales order has an item --");

                List<Integer> linkedPOList = new ArrayList<>();
                List<Integer> linkedRMList = new ArrayList<>();


                for(int i = 0; i < salesOrderItemsArray.length(); i++){
                    item = salesOrderItemsArray.getJSONObject(i);

                    switch (item.getString("LinkedTranType")) {
                        case "PO" -> {
                            if(!linkedPOList.contains(item.getInt("LinkedTranID")))
                                linkedPOList.add(item.getInt("LinkedTranID"));
                        }
                        case "RM" -> {
                            if(!linkedRMList.contains(item.getInt("LinkedTranID")))
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
                timeHandler.getCurrentYear() + "-" +
                timeHandler.getCurrentMonth() + "-" +
                timeHandler.getCurrentDayOfMonth() + "T00:00:01-6:00");
//            //Year
//                "2023-"+
//            //Month
//                 "10-" +
//            //Day
//                 "01T00:00:01-6:00");

        innerRequestBody.put("OrderDateRangeEnd",
                timeHandler.getCurrentYear() + "-" +
                timeHandler.getCurrentMonth() + "-" +
                timeHandler.getCurrentDayOfMonth() + "T23:59:59-6:00");

//        //Year
//        "2023-"+
//        //Month
//         "10-" +
//        //Day
//         "10T23:59:59-6:00");

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

        String boardID;
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
            case "1900", "3700", "2000", "1000" -> {

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.COMPONENTS, "COMPONENTS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                //boardID = "636bc3a95da9340015e47b84";
                //idList = "636bc3a95da9340015e47b8b";
                idList = orderStatusLogic("Components");

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "COMPONENTS", this.environment);
                TrelloCustomFieldIDs trelloRemanCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.REMAN_NUMBER, "COMPONENTS", this.environment);

                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                rmCustomField = trelloRemanCustomFieldID.getFieldID();

                //colorCustomFieldID="636bc3aa5da9340015e47ce4";
                //rmCustomField = "636bc3aa5da9340015e47ce8";

            }
            case "3300" -> {
                //kk cabinets

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, "CABINETS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                //idList = "62869b5c1351de037ffd2cc4";
                //boardID = "62869b5c1351de037ffd2cbb";
                idList = orderStatusLogic("Cabinets");
                if(this.environment.equals("Production"))
                    idLabel = "62869b5c1351de037ffd2d26";

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

//                colorCustomFieldID = "62869b5c1351de037ffd2da7";
//                rmCustomField = "62869b5c1351de037ffd2dab";
//                agilityPoCustomField = "62869b5c1351de037ffd2da9";
//                customerPoCustomField = "65944fad870030dd5e8ca1f0";

            }
            case "3350" -> {
                //cnc cabinets

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, "CABINETS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                //idList = "62869b5c1351de037ffd2cc4";
                //boardID = "62869b5c1351de037ffd2cbb";
                idList = orderStatusLogic("Cabinets");
                if(this.environment.equals("Production"))
                    idLabel = "62869e47dcae4f52e15c90e1";

                linkedType = this.linkedTranType;
                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloAgilityPoCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.AGILITY_PO_NUMBER, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloCustomerPoFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.CUSTOMER_PO_NUMBER, "CABINETS", this.environment);

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                agilityPoCustomField = trelloAgilityPoCustomFieldID.getFieldID();
                customerPoCustomField = trelloCustomerPoFieldID.getFieldID();

//                colorCustomFieldID = "62869b5c1351de037ffd2da7";
//                colorCustomFieldID = "62869b5c1351de037ffd2da7";
//                agilityPoCustomField = "62869b5c1351de037ffd2da9";
//                customerPoCustomField = "65944fad870030dd5e8ca1f0";

            }
            case "3455" -> {
                //tru cabinets

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, "CABINETS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                //idList = "62869b5c1351de037ffd2cc4";
                //boardID = "62869b5c1351de037ffd2cbb";
                idList = orderStatusLogic("Cabinets");
                if(this.environment.equals("Production"))
                    idLabel = "62869db3e04b83468347996b";

                linkedType = this.linkedTranType;
                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;

                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.COLOR_CODE, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloAgilityPoCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.AGILITY_PO_NUMBER, "CABINETS", this.environment);
                TrelloCustomFieldIDs trelloCustomerPoFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.CUSTOMER_PO_NUMBER, "CABINETS", this.environment);

                colorCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                agilityPoCustomField = trelloAgilityPoCustomFieldID.getFieldID();
                customerPoCustomField = trelloCustomerPoFieldID.getFieldID();

//                colorCustomFieldID = "62869b5c1351de037ffd2da7";
//                agilityPoCustomField = "62869b5c1351de037ffd2da9";
//                customerPoCustomField = "65944fad870030dd5e8ca1f0";
//                countOfBuildsCustomField = "62f3ac5b4eb96040bdd01827";

            }
            case "3450" -> {
                //choice cabinets

                //idList = "62869b5c1351de037ffd2cc4";

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.CABINETS, "CABINETS", this.environment);
                boardID = trelloBoardIDs.getBoardID();

//                boardID = "62869b5c1351de037ffd2cbb";
                idList = orderStatusLogic("Cabinets");

                if(this.environment.equals("Production"))
                    idLabel = "62869b5c1351de037ffd2d32";

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

//                colorCustomFieldID = "62869b5c1351de037ffd2da7";
//                rmCustomField = "62869b5c1351de037ffd2dab";
//                agilityPoCustomField = "62869b5c1351de037ffd2da9";
//                customerPoCustomField = "65944fad870030dd5e8ca1f0";
//                countOfBuildsCustomField = "62f3ac5b4eb96040bdd01827";

            }
            case "3500" -> {

                //counter tops

                TrelloBoardIDs trelloBoardIDs = new TrelloBoardIDs(TrelloBoards.TOP_SHOP, "TOPSHOP", this.environment);
                boardID = trelloBoardIDs.getBoardID();

                //boardID = "60c26dfb44555566d32ae643";
                idList = orderStatusLogic("Tops");
                if(this.environment.equals("Production"))
                    idLabel = "60c26dfc44555566d32ae700";
                linkedType = this.linkedTranType;
                linkedPoID = this.linkedTranPoID;
                linkedRmID = this.linkedTranRmID;


                if(this.agilityItemSearchResult.getString("ExtendedDescription").matches("((F|f)|(S|sL|l))(A|a)(B|b)(S|s)? - [A-z ]{2,40}\\d{3,4}(K|k)?-(\\d{2}|[A-z]{2}) (\\d\"? )?[A-z]{2,20}")){
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

//                colorCustomFieldID = "6197b500bbb79658801189ce";
//                agilityPoCustomField = "6197b57d371dc08c1f2a469a";
//                rmCustomField = "621519b6944e3c4fc091a515";
//                customerPoCustomField = "65944bbce3ba00017427cb36";

            }
            default -> {
                System.out.println("No item shared. Placing it in Top Shop Inbox List");
                boardID = "60c26dfb44555566d32ae643";
                idList = "61f2d5c461ac134ef274ae5f";
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

    public String orderStatusLogic(String board){

        JSONObject itemDetails;

        if(!(this.salesOrder == null) && this.salesOrder.has("dtOrderDetailResponse")) {
            //TODO insert for loop to receive total data

             itemDetails = this.salesOrder.getJSONArray("dtOrderDetailResponse").getJSONObject(0);
        } else {
            System.out.println(" - " + board + " Inbox - ");
            return whichBoard( new TrelloListIDs(TrelloLists.INBOX, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.INBOX, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.INBOX, "COMPONENTS", this.environment).getListID(), board);
        }

        String orderStatus = this.salesOrder.getString("OrderStatus");
        String orderProcessStatus = this.salesOrder.getString("OrderProcessStatus");
        String saleType = this.salesOrder.getString("SaleType"); //WHSE or WILLCALL

        if(orderStatus.equals("Open")){

            switch(orderProcessStatus){
                case "Picked" -> {
                    if(saleType.equals("WHSE")) {

                        System.out.println(" - " + board + " Picked - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "COMPONENTS", this.environment).getListID(), board);
                    } else if (saleType.equals("WILLCALL")){

                        System.out.println(" - " + board + " Picked - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.PICKED_AND_STAGED, "COMPONENTS", this.environment).getListID(), board);
                    }
                }
                case "Staged" -> {
                    if(saleType.equals("WHSE")) {
                        System.out.println(" - " + board + " Staged - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.ON_TRUCK_ON_DELIVERY, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.ON_TRUCK_ON_DELIVERY, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.ON_TRUCK_ON_DELIVERY, "COMPONENTS", this.environment).getListID(), board);
                    }else if(saleType.equals("WILLCALL")) {
                        System.out.println(" - " + board + " Willcall - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.WILL_CALL, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.WILL_CALL, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.WILL_CALL, "COMPONENTS", this.environment).getListID(), board);
                    }
                }
                case "" -> {
                    //Fresh order

                    //In Production
                    if(itemDetails.getDouble("TotalBackorderedQuantity") >= 1.0 &&
                            itemDetails.getDouble("TotalUnstagedQuantity") == 0.0  &&
                            itemDetails.getDouble("TotalStagedQuantity")== 0.0 &&
                            itemDetails.getDouble("TotalInvoicedQuantity") == 0.0){

                        if (itemDetails.has("LinkedTranType")) {
                            System.out.println(" - " + board + " Processing || Batching - ");
                            return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.BATCHING, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", this.environment).getListID(), board);
                        } else {

                            //In Processing
                            System.out.println(" - " + board + " Processing || Batching - ");
                            return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.BATCHING, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", this.environment).getListID(), board);

                        }
                    }else if(itemDetails.getDouble("TotalBackorderedQuantity") == 0.0 &&
                            itemDetails.getDouble("TotalUnstagedQuantity") >= 1.0  &&
                            itemDetails.getDouble("TotalStagedQuantity")== 0.0 &&
                            itemDetails.getDouble("TotalInvoicedQuantity") == 0.0){

                        //To Be Picked
                        System.out.println(" - " + board + " To Be Picked - ");
                        return whichBoard( new TrelloListIDs(TrelloLists.TO_BE_PICKED, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.TO_BE_PICKED, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.TO_BE_PICKED, "COMPONENTS", this.environment).getListID(), board);
                    }
                }
            }

        }else if(orderStatus.equals("Invoiced")) {
            System.out.println(" - " + board + " Invoiced - ");
            return whichBoard( new TrelloListIDs(TrelloLists.INVOICED, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.INVOICED, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.INVOICED, "COMPONENTS", this.environment).getListID(), board);

        }else{
            System.out.println(" - " + board + " Processing || Batching - ");
            return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.BATCHING, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", this.environment).getListID(), board);
        }
        System.out.println(" - " + board + " Processing || Batching - ");


        return whichBoard( new TrelloListIDs(TrelloLists.PROCESSING, "CABINETS", this.environment).getListID(), new TrelloListIDs(TrelloLists.BATCHING, "TOPSHOP", this.environment).getListID(), new TrelloListIDs(TrelloLists.PROCESSING, "COMPONENTS", this.environment).getListID(), board);
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

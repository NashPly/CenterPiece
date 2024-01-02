package com.CenterPiece.CenterPiece.Core;

import com.CenterPiece.CenterPiece.APICalls.TomTomCalls;
import com.CenterPiece.CenterPiece.APICalls.TrelloCalls;
import com.CenterPiece.CenterPiece.ItemCodeHandler;
import com.CenterPiece.CenterPiece.Objects.SalesOrder;
import com.CenterPiece.CenterPiece.Objects.ShipToAddress;
import com.CenterPiece.CenterPiece.TrelloListIDs;
import com.CenterPiece.CenterPiece.TrelloLists;
import org.json.JSONArray;
import org.json.JSONObject;


import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CenterPieceFunctions {

    private final HttpClient client;
    private final String contextID;
    private List<SalesOrder> salesOrderList;
    private JSONObject itemInformation;
    private final String branch;

    public CenterPieceFunctions(HttpClient client, String contextID, String branch) {
        this.client = client;
        this.contextID = contextID;
        this.branch = branch;
    }

    public List<String> salesOrderParser(JSONArray jsonArray){

        List<String> soList = new ArrayList<>();
        for(int i = 0; i <  jsonArray.length(); i++){
            soList.add(jsonArray.getJSONObject(i).get("OrderID").toString());
        }
        return soList;
    }

    public List<Integer> tallySOsToBeCreated(int size, List<String> currentList) {

        List<Integer> tally = new ArrayList<>();

        for (int i = 0; i< size; i++){
            var checkHold = checkTrelloForSO(currentList.get(i)).getJSONObject(0);
            if(!(checkHold == null) && checkHold.has("id")){
                if (checkHold.getString("id").equals("Empty")) {
                    System.out.println("Tally: " + tally);
                    tally.add(i);
                }
            }
        }
        return tally;
    }

    public  JSONArray checkTrelloForSO(String soNum) {

        String modelTypes = "cards";
        String card_fields = "name,closed,desc,idList,labels";
        //String card_fields = "closed,idList,labels";

        TrelloCalls trelloAPICall = new TrelloCalls(this.client, "search", String.format("query=%s&card_board=true&modelTypes=%s&card_fields=%s&card_attachments=true",
                soNum, modelTypes, card_fields));

        System.out.println("\n-- Check Trello For SO --");
        var response = trelloAPICall.getTrelloAPICallObject();
        System.out.println(response);

        if(!(response == null) && response.has("cards")){
            if(response.getJSONArray("cards").length() > 0){
                JSONArray cards = response.getJSONArray("cards");

                List<JSONObject> openTrelloCards = new ArrayList<>();

                for (int i = 0; i < cards.length(); i++) {
                    if (!cards.getJSONObject(i).getBoolean("closed") && (!cards.getJSONObject(i).getString("name").contains("Cullman PO#"))) {
                        openTrelloCards.add(cards.getJSONObject(i));
                    }
                }
                if(openTrelloCards.size() > 1){

                    JSONObject desiredCard = new JSONObject();
                    JSONArray partialCards = new JSONArray();

                    TrelloCalls trelloCalls = new TrelloCalls(client,"cards/");
                    for(int i = 0; i < openTrelloCards.size(); i++){
                        //TODO Handle Multiple cards with attachments
                        if(openTrelloCards.get(i).getJSONArray("labels").length()>0){

                            for(int q = 0; q < openTrelloCards.get(i).getJSONArray("labels").length(); q++){
                                if(openTrelloCards.size()>0){
                                    if (openTrelloCards.get(i).getJSONArray("labels").getJSONObject(q).get("name").equals("Partial")) {
                                        //Check the partial box

                                        String partialCustomFieldID = "";
                                        switch (openTrelloCards.get(i).getJSONObject("board").getString("id")) {
                                            case "60c26dfb44555566d32ae643" -> partialCustomFieldID = "638e63205f249b03ac9edd81"; //Top Shop
//                                        case "asdf" -> partialCustomFieldID = "asdf"; //Cabinets
//                                        case "asdf" -> partialCustomFieldID = "asdf"; // Components
                                        }

                                        updateCustomFieldTrello(openTrelloCards.get(i).getString("id"), partialCustomFieldID, "true");

                                        partialCards.put(partialCards.length(),openTrelloCards.get(i));
                                        q = openTrelloCards.get(i).getJSONArray("labels").length();
                                        openTrelloCards.remove(i);
                                        i--;
                                        continue;
                                    }
                                }
                            }
                        }
                        if(desiredCard.isEmpty() && openTrelloCards.get(i).getJSONArray("attachments").length() > 0) {
                            desiredCard = openTrelloCards.get(i);
                            openTrelloCards.remove(i);
//                        }else if(partialCard.isEmpty() && openTrelloCards.get(i).getJSONArray("labels").length()>0){
                            //Account for multiple partials
                        }
                    }
                    if(desiredCard.isEmpty()){
                        desiredCard = openTrelloCards.get(openTrelloCards.size()-1);
                        openTrelloCards.remove(openTrelloCards.size()-1);
                    }

                    for(JSONObject json: openTrelloCards){
                        System.out.println("\n - Deleted Duplicate TrelloCard -\n" + json + "\n");
                        var stinker = "hello";
                        trelloCalls.deleteTrelloAPICall(json.getString("id"));
                    }

                    JSONArray jsonArray = new JSONArray().put(0, desiredCard);

                    for(int v = 0; v < partialCards.length(); v++) {
                        jsonArray.put(jsonArray.length(),partialCards.getJSONObject(v));
                    }

                    return jsonArray;
                }else if(openTrelloCards.size() == 1){
                    return new JSONArray().put(0,openTrelloCards.get(0));
                }else{
                    JSONObject json = new JSONObject();
                    json.put("id","Empty");
                    return new JSONArray().put(0,json);
                }
            }
        }

        JSONObject json = new JSONObject();
        json.put("id","Empty");
        return new JSONArray().put(0,json);
    }

    public void createTrelloCard(JSONObject jsonSO) {

        System.out.println("\n-- Create Trello Card SO --");
        System.out.println(jsonSO);

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(this.client, this.contextID, jsonSO.getNumber("OrderID").toString(), jsonSO, this.branch);
        JSONObject itemInformation = itemCodeHandler.itemParseProcess();

        String parameters = agilityDataForTrelloGather(jsonSO, itemInformation);

        System.out.println("\n-- Created Card --");
        TrelloCalls trelloAPICall = new TrelloCalls(client, "cards", parameters);
        var response = trelloAPICall.postTrelloAPICall();

        checkTrelloCardForEmptyCustomFields(response.getString("id"), itemInformation, jsonSO);
    }

    public void updateTrelloCards() {

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(this.client, this.contextID, this.branch);

//        List<String> liveTrelloBuckets = new ArrayList<>(Arrays.asList("62869b5c1351de037ffd2cbc", "61f2d5c461ac134ef274ae5f",
//                "62869b5c1351de037ffd2ccd", "6239c656ab5c356ec1568beb", "62869b5c1351de037ffd2cce",
//                "60c26dfb44555566d32ae64d", "62869b5c1351de037ffd2cd0", "61e6d38623686777464221b9",
//                "62869b5c1351de037ffd2cd1", "60c26dfb44555566d32ae64e", "62869b5c1351de037ffd2cd4",
//                "61b360e35ab37c0d9037c19f","6384cfab789e5f01197094ec", "639871e0cb87d801a97ad7aa"));
//
//        List<TrelloLists> offLimitsTrelloLists = new ArrayList<>(Arrays.asList(TrelloLists.BATCHING,TrelloLists.SO_SID_CHECK,
//                TrelloLists.ON_HOLD, TrelloLists.PROCESSING,TrelloLists.TO_BE_ORDERED ,TrelloLists.ON_ORDER,
//                TrelloLists.RECEIVING, TrelloLists.CREDIT_HOLD, TrelloLists.SCHEDULING_POOL, TrelloLists.PRODUCTION_QUEUE,
//                TrelloLists.SS_And_RS, TrelloLists.IN_PRODUCTION, TrelloLists.TRANSFERRED_TO_NASHVILLE, TrelloLists.ON_TRUCK_ON_DELIVERY));


        JSONObject fetchedSalesOrderData = itemCodeHandler.agilityChangedSalesOrderListLookup();

        //TODO adapt this for Sales orders

        if(!(fetchedSalesOrderData == null) && fetchedSalesOrderData.has("dtOrderResponse")) {

            JSONArray salesOrderDataArray = fetchedSalesOrderData.getJSONArray("dtOrderResponse");

            for(int i = 0; i < salesOrderDataArray.length(); i++) {

                JSONArray resultArray = (checkTrelloForSO(String.valueOf(salesOrderDataArray.getJSONObject(i).getNumber("OrderID"))));

                JSONObject firstResult = resultArray.getJSONObject(0);

                ItemCodeHandler salesDataItemHandler = new ItemCodeHandler(this.client, this.contextID, salesOrderDataArray.getJSONObject(i).getNumber("OrderID").toString(), salesOrderDataArray.getJSONObject(i), this.branch);

                if (!(firstResult == null) && firstResult.has("id")){
                    if (!firstResult.getString("id").equals("Empty")) {

                        JSONObject itemInformation = salesDataItemHandler.itemParseProcess();

                        boolean sameBoard = itemInformation.getString("boardID").equals(firstResult.getJSONObject("board").getString("id"));
                        //boolean foundBoard = !itemInformation.getString("boardID").equals("None Found");

                        if(!itemInformation.getString("boardID").equals("None Found")){

                            System.out.println(itemInformation.getString("idList")+"\n");

                            for(int p = 0; p < resultArray.length(); p++) {

                                ArrayList<String> labelIds = new ArrayList<>();

                                if(resultArray.getJSONObject(p).has("labels") && sameBoard) {
                                    for(int x = 0; x < resultArray.getJSONObject(p).getJSONArray("labels").length(); x++){

                                        labelIds.add(resultArray.getJSONObject(p).getJSONArray("labels")
                                                .getJSONObject(x).getString("id"));
                                    }

                                    itemInformation.remove("idLabel");
                                    itemInformation.put("idLabel", String.join(",", labelIds));
                                }

                                if(resultArray.getJSONObject(p).has("idList")){
                                        if(!(itemInformation.getString("idList").equals("62869b5c1351de037ffd2cd4") ||
                                                itemInformation.getString("idList").equals("61b360e35ab37c0d9037c19f")) &&
                                        sameBoard ){
                                        //TODO above checks if current board is destination board
                                        TrelloListIDs listIDs = new TrelloListIDs(resultArray.getJSONObject(p).getString("idList"));

                                        if(listIDs.offLimits() || labelIds.contains("638e5d85e978f805fbcbf36f")) {
                                            itemInformation.remove("idList");
                                            itemInformation.put("idList", resultArray.getJSONObject(p).getString("idList"));
                                        }
    //                                    if(!liveTrelloBuckets.contains(resultArray.getJSONObject(p).getString("idList")) ||
    //                                            labelIds.contains("638e5d85e978f805fbcbf36f")) {
    //                                        itemInformation.remove("idList");
    //                                        itemInformation.put("idList", resultArray.getJSONObject(p).getString("idList"));
    //                                    }
                                    }
                                }

                                String parameters = agilityDataForTrelloGather(salesOrderDataArray.getJSONObject(i), itemInformation);

                                System.out.println("\n--- Updated a Trello Card ---");
                                TrelloCalls trelloCalls = new TrelloCalls(client, ("cards/" + resultArray.getJSONObject(p).getString("id")), parameters);
                                var response = trelloCalls.putTrelloAPICall(new JSONObject());

                                checkTrelloCardForEmptyCustomFields(response.getString("id"), itemInformation, salesOrderDataArray.getJSONObject(i));

                                System.out.println("\nUpdates Applied");
                            }
                        }else{
                            System.out.println("\n-- No Applicable Boards Found --");
                        }
                    }else{
                        System.out.println("\n- Trello Hasn't Updated Yet -");
                        //TODO Work out some way to create a card if there isn't one on Trello yet
                    }
                }else if (!(firstResult == null) && firstResult.has("error")) {
                    System.out.println(firstResult);
                } else{
                    System.out.println("\n- Trello Hasn't Updated Yet 2 -");

                    //TODO Work out some way to create a card if there isn't one on Trello yet
                }
            }
        }else{
            System.out.println("\n-- No Updates --");
        }
    }

    public void checkTrelloCardForEmptyCustomFields(String cardId, JSONObject itemInformation, JSONObject jsonSO) {

        if(!(itemInformation == null)) {
            //Customer PO in the description

            if (itemInformation.has("linkedID") && itemInformation.has("linkedType")) {
                if (itemInformation.getString("linkedID") != null && itemInformation.getString("linkedType").equals("RM")) {
                    updateCustomFieldTrello(cardId, itemInformation.getString("rmCustomField"), itemInformation.getString("linkedID"));
                } else if (itemInformation.getString("linkedID") != null && itemInformation.getString("linkedType").equals("PO")) {
                    updateCustomFieldTrello(cardId, itemInformation.getString("agilityPoCustomField"), itemInformation.getString("linkedID"));
                }
            }

            if (itemInformation.has("colorCode")) {
                if (itemInformation.getString("colorCode") != null)
                    updateCustomFieldTrello(cardId, itemInformation.getString("colorCustomField"), itemInformation.getString("colorCode"));
            }

            if (!(itemInformation.isNull("countOfBuildsCustomField")))
                updateCustomFieldTrello(cardId, itemInformation.getString("countOfBuildsCustomField"), itemInformation.getInt("countOfBuilds"));


            if (!(itemInformation.isNull("customerPoCustomField")))
                updateCustomFieldTrello(cardId, itemInformation.getString("customerPoCustomField"),
                        jsonSO.getString("CustomerPO"));

        }
    }

    public JSONArray getCardCustomFieldTrello(String cardID){

        TrelloCalls trelloAPICall = new TrelloCalls(this.client, String.format("cards/%s/customFieldItems", cardID));

        System.out.println("\n-- Check Trello Card For CustomField --");
        var response = trelloAPICall.getTrelloAPICallArray();
        System.out.println(response);

        return response;
    }

    public void updateCustomFieldTrello(String cardId, String customFieldID, String value) {
        String urlEndpoint = String.format("cards/%s/customField/%s/item", cardId, customFieldID );
        JSONObject jsonObject = new JSONObject();

        if(value.equals("true") || value.equals("false"))
            jsonObject.put("checked", value);
        else jsonObject.put("text", value);

        System.out.println("\n-- Update Custom Field in Trello --");
        TrelloCalls trelloCalls = new TrelloCalls(client, urlEndpoint, "");
        trelloCalls.putTrelloAPICall(jsonObject);
    }

    public void updateCustomFieldTrello(String cardId, String customFieldID, Integer value) {
        String urlEndpoint = String.format("cards/%s/customField/%s/item", cardId, customFieldID );
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("number", value.toString());

        System.out.println("\n-- Update Custom Field in Trello --");
        TrelloCalls trelloCalls = new TrelloCalls(client, urlEndpoint, "");
        trelloCalls.putTrelloAPICall(jsonObject);
    }

    public String agilityDataForTrelloGather(JSONObject jsonSO, JSONObject itemInformation){

        String boardID = itemInformation.getString("boardID");
        String idList = itemInformation.getString("idList");

        String idLabels = "";
        if(itemInformation.has("idLabel")) idLabels = itemInformation.getString("idLabel");

        String orderDate = jsonSO.getString("OrderDate");
        String dueDate = jsonSO.getString("ExpectedDate");

        orderDate = trelloDateAdjuster(orderDate);

        dueDate = trelloDateAdjuster(dueDate);

        String name = (
                "SO "+
                        jsonSO.getNumber("OrderID") +
                        " - " +
                        jsonSO.getString("BillToName") +
                        " - " +
                        jsonSO.getString("TransactionJob")
        );

        String address = "";
        String city = "";
        String state = "";
        String zip = "";

        name = urlify(name);

        boolean address1ContainsNumbers = checkIfAddressHasStreetNumbers(jsonSO.getString("ShipToAddress1"));

        boolean address2ContainsNumbers = checkIfAddressHasStreetNumbers(jsonSO.getString("ShipToAddress2"));


        if((!(jsonSO.getString("ShipToAddress1").equals("- Verified Address -") || jsonSO.getString("ShipToAddress1").isBlank())) && address1ContainsNumbers == true){
            address = jsonSO.getString("ShipToAddress1");
        }else if(!jsonSO.getString("ShipToAddress2").isBlank() && address2ContainsNumbers == true){
            address = jsonSO.getString("ShipToAddress2");
        }

        boolean addressBlank = false;

        if(address.isBlank() || jsonSO.getString("ShipToCity").isBlank() ||
                jsonSO.getString("ShipToState").isBlank() || jsonSO.getString("ShipToZip").isBlank())
            addressBlank = true;

        String parameters;
        if(!addressBlank) {

            ShipToAddress shipToAddress = new ShipToAddress(
                    address,
                    jsonSO.getString("ShipToCity"),
                    jsonSO.getString("ShipToState"),
                    jsonSO.getString("ShipToZip"));

            TomTomCalls tomTomCalls = new TomTomCalls(shipToAddress, client);

            parameters = String.format(
                    "idBoard=%s&idList=%s&name=%s" +
                            "&idLabels=%s&start=%s&due=%s&coordinates=%s" +
                            "&locationName=%s",
                    boardID, idList, name, idLabels, orderDate,dueDate,
                    urlify(tomTomCalls.getLatitude() + "," + tomTomCalls.getLongitude()),
                    urlify(tomTomCalls.getResponseAddress()));
        }else{
            parameters = String.format(
                    "idBoard=%s&idList=%s&name=%s" +
                            "&idLabels=%s&start=%s&due=%s",
                    boardID, idList, name, idLabels, orderDate, dueDate);
        }

//        if(!description.isEmpty()){
//            parameters += String.format("&desc=%s", description);
//        }

        return parameters;
    }

    //Clean for URL
    private String urlify(String string){

        string = string.replace(" ", "%20");
        string = string.replace("&", "%26");
        string = string.replace(",", "%2C");
        string = string.replace("#", "%23");
        string = string.replace("@", "%40");
        string = string.replace("*", "%2A");
        string = string.replace("'", "%27");
        string = string.replace("\"", "%22");

        return string;
    }


    private boolean checkIfAddressHasStreetNumbers(String address){

        Boolean containsNumbers = false;

        char[] chars2 = address.toCharArray();

        for(int i = 0; i < chars2.length; i++){
            if(Character.isDigit(chars2[i])){
                containsNumbers = true;
            }
        }
        return containsNumbers;
    }

    private String trelloDateAdjuster(String date){
        int dateHold = Integer.parseInt(date.substring(8, 10));

        if(dateHold+1>31) {
            return date.substring(0, 8) + (Integer.valueOf(date.substring(8, 10)));
        }else{
            dateHold++;
            return date.substring(0, 8) + dateHold;
        }
    }
}
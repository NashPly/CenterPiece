package com.CenterPiece.CenterPiece.Core;

import com.CenterPiece.CenterPiece.APICalls.TomTomCalls;
import com.CenterPiece.CenterPiece.APICalls.TrelloCalls;
import com.CenterPiece.CenterPiece.ItemCodeHandler;
import com.CenterPiece.CenterPiece.Objects.SalesOrder;
import com.CenterPiece.CenterPiece.Objects.ShipToAddress;
import com.CenterPiece.CenterPiece.TrelloIDs.TrelloCustomFieldIDs;
import com.CenterPiece.CenterPiece.TrelloIDs.TrelloCustomFields;
import com.CenterPiece.CenterPiece.TrelloIDs.TrelloLabelIds;
import com.CenterPiece.CenterPiece.TrelloIDs.TrelloListIDs;
import org.json.JSONArray;
import org.json.JSONObject;


import java.net.http.HttpClient;
import java.util.*;

public class CenterPieceFunctions {

    private final HttpClient client;
    private final String contextID;
    private final String environment;
    private List<SalesOrder> salesOrderList;
    private JSONObject itemInformation;
    private final String branch;

    public CenterPieceFunctions(HttpClient client, String contextID, String branch, String environment) {
        this.client = client;
        this.contextID = contextID;
        this.branch = branch;
        this.environment = environment;
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
        String boardIds = "";

        if(this.environment.equals("Production"))boardIds = "60c26dfb44555566d32ae643,62869b5c1351de037ffd2cbb,636bc3a95da9340015e47b84,6655edac93277a7afa62cda6";
        else if(this.environment.equals("Test")) boardIds = "6596e9210326360265ae3347,6596e945627ec8be307b1e0f,6596ece3760cfe2637c6f944,6655edac93277a7afa62cda6";
        //String card_fields = "closed,idList,labels";

        TrelloCalls trelloAPICall = new TrelloCalls(this.client, "search", String.format("query=%s&idBoards=%s&card_board=true&modelTypes=%s&card_fields=%s&card_attachments=true",
                soNum, boardIds, modelTypes, card_fields));

        System.out.println("\n-- Check Trello For SO --");
        var response = trelloAPICall.getTrelloAPICallObject();
        System.out.println(response);

        if(!(response == null) && response.has("cards")){
            if(!response.getJSONArray("cards").isEmpty()){
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
                        if(!openTrelloCards.get(i).getJSONArray("labels").isEmpty()){

                            for(int q = 0; q < openTrelloCards.get(i).getJSONArray("labels").length(); q++){
                                if(!openTrelloCards.isEmpty()){
                                    if (openTrelloCards.get(i).getJSONArray("labels").getJSONObject(q).get("name").equals("Partial")) {
                                        //Check the partial box

                                        String partialCustomFieldID = "";

                                        switch (openTrelloCards.get(i).getJSONObject("board").getString("name")) {
                                            case "Top Shop", "Top Shop Test Environment"-> {
                                                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.PARTIAL_CHECKBOX, "TOPSHOP", this.environment);
                                                partialCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                                            }
                                            case "Cabinet Shop", "Cabinet Shop Test Environment" -> {
                                                TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.PARTIAL_CHECKBOX, "CABINETS", this.environment);
                                                partialCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                                            }
                                            case "Component Shop","Component Shop Test Environment" -> {
                                                    TrelloCustomFieldIDs trelloColorCodeCustomFieldID = new TrelloCustomFieldIDs(TrelloCustomFields.PARTIAL_CHECKBOX, "COMPONENTS", this.environment);
                                                    partialCustomFieldID = trelloColorCodeCustomFieldID.getFieldID();
                                                }
                                        }

                                        updateCustomFieldTrello(openTrelloCards.get(i).getString("id"), partialCustomFieldID, "true");

                                        partialCards.put(partialCards.length(),openTrelloCards.get(i));
                                        q = openTrelloCards.get(i).getJSONArray("labels").length();
                                        openTrelloCards.remove(i);
                                        if(i!=0)
                                            i--;
                                        continue;
                                    }
                                }
                            }
                        }
                        if(desiredCard.isEmpty() && !openTrelloCards.get(i).getJSONArray("attachments").isEmpty()) {
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
                        trelloCalls.deleteTrelloCardAPICall(json.getString("id"));
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

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(this.client, this.contextID, jsonSO.getNumber("OrderID").toString(), jsonSO, this.branch, this.environment);
        JSONObject itemInformation = itemCodeHandler.itemParseProcess();

        String parameters = agilityDataForTrelloGather(jsonSO, itemInformation, true);

        System.out.println("\n-- Created Card --");
        TrelloCalls trelloAPICall = new TrelloCalls(client, "cards", parameters);
        var response = trelloAPICall.postTrelloAPICall();

        checkTrelloCardForEmptyCustomFields(response.getString("id"), itemInformation, jsonSO);
    }

    public void updateTrelloCards() {
        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(this.client, this.contextID, this.branch, this.environment);
        JSONObject fetchedSalesOrderData = itemCodeHandler.agilityChangedSalesOrderListLookup();

        if (fetchedSalesOrderData != null && fetchedSalesOrderData.has("dtOrderResponse")) {
            JSONArray salesOrderDataArray = fetchedSalesOrderData.getJSONArray("dtOrderResponse");

            for (int i = 0; i < salesOrderDataArray.length(); i++) {
                updateTrelloCardForSalesOrder(salesOrderDataArray.getJSONObject(i));
            }
        } else {
            System.out.println("\n-- No Updates --");
        }
    }

    private void updateTrelloCardForSalesOrder(JSONObject salesOrderData) {
        JSONArray trelloSearchResultArray = checkTrelloForSO(String.valueOf(salesOrderData.getNumber("OrderID")));

        if (trelloSearchResultArray != null && !trelloSearchResultArray.isEmpty()) {
            JSONObject firstResult = trelloSearchResultArray.getJSONObject(0);
            ItemCodeHandler salesDataItemHandler = new ItemCodeHandler(this.client, this.contextID, salesOrderData.getNumber("OrderID").toString(), salesOrderData, this.branch, this.environment);

            if (firstResult != null && firstResult.has("id") && !firstResult.getString("id").equals("Empty")) {
                updateTrelloCard(firstResult, salesDataItemHandler.itemParseProcess(), trelloSearchResultArray, salesOrderData);
            } else {
                System.out.println("\n- Trello Hasn't Updated Yet -");
                // TODO: Work out some way to create a card if there isn't one on Trello yet
            }
        } else {
            System.out.println("\n-- No Applicable Boards Found --");
        }
    }

    private void updateTrelloCard(JSONObject firstResult, JSONObject agilityItemInformation, JSONArray trelloSearchResultArray, JSONObject salesOrderData) {
        boolean isSameBoard = agilityItemInformation.getString("boardID").equals(trelloSearchResultArray.getJSONObject(0).getJSONObject("board").getString("id"));

        for(Object trelloCard: trelloSearchResultArray) {
            updateCardLabels(agilityItemInformation, (JSONObject) trelloCard, isSameBoard);

            updateCardList(agilityItemInformation, (JSONObject) trelloCard, isSameBoard);
        }
        // Your existing logic for updating Trello card
        System.out.println("\n--- Updated a Trello Card ---");
        TrelloCalls trelloCalls = new TrelloCalls(client, ("cards/" + firstResult.getString("id")),
                agilityDataForTrelloGather(salesOrderData, agilityItemInformation, agilityItemInformation.getString("idList")
                        .equals(firstResult.getString("idList"))));
        var response = trelloCalls.putTrelloAPICall(new JSONObject());

        checkTrelloCardForEmptyCustomFields(response.getString("id"), agilityItemInformation, salesOrderData);

        System.out.println("\nUpdates Applied");
    }

    private void updateCardLabels(JSONObject agilityItemInformation, JSONObject trelloCard, boolean isSameBoard) {
        System.out.println("\n--- Updating Card Labels ---");
        List<String> labelIds = new ArrayList<>();
        List<String> trelloLabelIds = null;

        if(agilityItemInformation.has("idLabel") && agilityItemInformation.get("idLabel") != null)
            trelloLabelIds = new ArrayList<>(List.of(agilityItemInformation.getString("idLabel").split(",")));

        JSONArray labelsArray = trelloCard.getJSONArray("labels");

        if (trelloCard.has("labels") && trelloLabelIds != null && isSameBoard) {
            for (Object labelObject : labelsArray) {
                if (labelObject instanceof JSONObject) {
                    String labelId = ((JSONObject) labelObject).getString("id");
                    labelIds.add(labelId);
                }
            }

            agilityItemInformation.remove("idLabel");
            agilityItemInformation.put("idLabel", String.join(",", compareContrastLabels(labelIds, trelloLabelIds, trelloCard.getString("id"))));
        }

    }

    private void updateCardList(JSONObject agilityItemInformation, JSONObject trelloCard, boolean sameBoard) {
        System.out.println("\n--- Updating Card List ---");
        if(trelloCard.has("idList") &&
                !(agilityItemInformation.getString("idList").equals("62869b5c1351de037ffd2cd4")
                        || agilityItemInformation.getString("idList").equals("61b360e35ab37c0d9037c19f"))
                && sameBoard ){

                TrelloListIDs listIDs = new TrelloListIDs(trelloCard.getString("idList"));

                if(listIDs.offLimits(this.branch) || trelloCard.getJSONArray("labels").toList().contains("638e5d85e978f805fbcbf36f")) {
                    //If either of those two are true, leave it where it is
                    agilityItemInformation.remove("idList");
                    agilityItemInformation.put("idList", listIDs.getListID());
                }
        }
    }

// Other helper functions can be added as needed

    private List<String> compareContrastLabels(List<String> trelloLabels, List<String> centerPieceLabels, String cardId) {

        List<String> resultLabelList = new ArrayList<>();
        TrelloCalls trelloCalls = new TrelloCalls(this.client, "cards/");

        for (int i = 0; i < trelloLabels.size(); i++) {

            if (centerPieceLabels.contains(trelloLabels.get(i))) {
                resultLabelList.add(trelloLabels.get(i));
                centerPieceLabels.remove(trelloLabels.get(i));
            } else if (new TrelloLabelIds(trelloLabels.get(i)).isBrandLabel()) {
                for (String cpLabel : centerPieceLabels) {
                    if (new TrelloLabelIds(cpLabel).isBrandLabel() && !cpLabel.equals(trelloLabels.get(i))) {
                        //Delete Label from card
                        System.out.println("Delete the brand label: " + trelloLabels.get(i) + " from Trello\n");
                        trelloCalls.deleteTrelloCardLabelAPICall(cardId, trelloLabels.get(i));
                        resultLabelList.add(cpLabel);
                        centerPieceLabels.remove(cpLabel);
                    }

                }
            } else if (new TrelloLabelIds(trelloLabels.get(i)).isSaleTypeLabel()) {
                for (String cpLabel : centerPieceLabels) {
                    if (new TrelloLabelIds(cpLabel).isSaleTypeLabel() && !cpLabel.equals(trelloLabels.get(i))) {
                        //Delete Label from card
                        System.out.println("Delete the saletype label: " + trelloLabels.get(i) + " from Trello\n");
                        trelloCalls.deleteTrelloCardLabelAPICall(cardId, trelloLabels.get(i));
                        resultLabelList.add(cpLabel);
                    }
                }
            } else if (new TrelloLabelIds(trelloLabels.get(i)).isPaymentCodeLabel()) {
                for (String cpLabel : centerPieceLabels) {
                    if (new TrelloLabelIds(cpLabel).isPaymentCodeLabel() && !cpLabel.equals(trelloLabels.get(i))) {
                        //Delete Label from card
                        System.out.println("Delete the payment code label: " + trelloLabels.get(i) + " from Trello\n");
                        trelloCalls.deleteTrelloCardLabelAPICall(cardId, trelloLabels.get(i));
                        resultLabelList.add(cpLabel);
                    }
                }
            }else {
                System.out.println("Neither brand nor saletype label. Leave it.\n");
                resultLabelList.add(trelloLabels.get(i));
            }
        }

        //resultLabelList.addAll(centerPieceLabels);
        Set<String> set = new LinkedHashSet<>(resultLabelList);
        resultLabelList.clear();
        resultLabelList.addAll(set);

        return resultLabelList;
    }

    public void checkTrelloCardForEmptyCustomFields(String cardId, JSONObject itemInformation, JSONObject jsonSO) {

        if(!(itemInformation == null)) {
            //Customer PO in the description


            if(itemInformation.has("linkedRmID")){
                if (itemInformation.getString("linkedRmID") != null && itemInformation.has("rmCustomField")) {
                    updateCustomFieldTrello(cardId, itemInformation.getString("rmCustomField"), itemInformation.getString("linkedRmID"));
                }
            }
            if(itemInformation.has("linkedPoID")){
                if (itemInformation.getString("linkedPoID") != null && itemInformation.has("agilityPoCustomField")) {
                    updateCustomFieldTrello(cardId, itemInformation.getString("agilityPoCustomField"), itemInformation.getString("linkedPoID"));
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

    public String agilityDataForTrelloGather(JSONObject jsonSO, JSONObject itemInformation, boolean sameList){

        System.out.println("\n----Formatting Data for Trello URL Parameters ----\n");

        String boardID = itemInformation.getString("boardID");
        String idList = itemInformation.getString("idList");

        String idLabels = "";
        if(itemInformation.has("idLabel")) idLabels = itemInformation.getString("idLabel");

        String orderDate = jsonSO.getString("OrderDate");

        String dueDate = jsonSO.getString("ExpectedDate");

        orderDate = trelloDateAdjuster(orderDate);

        dueDate = trelloDateAdjuster(dueDate);

        String address = "";
        String city = "";
        String state = "";
        String zip = "";

        String name = (
                "SO "+
                        jsonSO.getNumber("OrderID") +
                        " - " +
                        jsonSO.getString("BillToName") +
                        " - " +
                        jsonSO.getString("TransactionJob")
        );

        name = urlify(name);

        boolean address1ContainsNumbers = checkIfAddressHasStreetNumbers(jsonSO.getString("ShipToAddress1"));

        boolean address2ContainsNumbers = checkIfAddressHasStreetNumbers(jsonSO.getString("ShipToAddress2"));

        if((!(jsonSO.getString("ShipToAddress1").equals("- Verified Address -") || jsonSO.getString("ShipToAddress1").isBlank())) && address1ContainsNumbers == true && !(jsonSO.getString("ShipToAddress1").contains("@"))){
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
                            "&idLabels=%s"+ addOrRemoveOrderDate(orderDate) +"&due=%s&coordinates=%s" +
                            "&locationName=%s" + moveToTopIfCabinetsAndMoved(sameList),
                    boardID, idList, name, idLabels,dueDate,
                    urlify(tomTomCalls.getLatitude() + "," + tomTomCalls.getLongitude()),
                    urlify(tomTomCalls.getResponseAddress()));
        }else{
            parameters = String.format(
                    "idBoard=%s&idList=%s&name=%s" +
                            "&idLabels=%s"+ addOrRemoveOrderDate(orderDate) +"&due=%s" + moveToTopIfCabinetsAndMoved(sameList),
                    boardID, idList, name, idLabels, dueDate);
        }

        System.out.println("\n---- Complete - Formatting Data for Trello URL Parameters ----\n");
        return parameters;
    }

    private String addOrRemoveOrderDate(String orderDate){
        if(this.branch.equals("CABINETS")) return "";
        else return "&start=" + orderDate;
    }

    private String moveToTopIfCabinetsAndMoved(boolean sameList){
        if(this.branch.equals("CABINETS")){
            if(sameList) return "";
            else return "&pos=top";
        }else return "";
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
        return date+"T12:00:00.00-06:00";
    }
}
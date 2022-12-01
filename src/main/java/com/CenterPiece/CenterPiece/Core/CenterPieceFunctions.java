package com.CenterPiece.CenterPiece.Core;

import com.CenterPiece.CenterPiece.APICalls.TomTomCalls;
import com.CenterPiece.CenterPiece.APICalls.TrelloCalls;
import com.CenterPiece.CenterPiece.ItemCodeHandler;
import com.CenterPiece.CenterPiece.Objects.SalesOrder;
import com.CenterPiece.CenterPiece.Objects.ShipToAddress;
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
            var checkHold = checkTrelloForSO(currentList.get(i));
            if(!(checkHold == null) && checkHold.has("id")){
                if (checkHold.getString("id").equals("Empty")) {
                    System.out.println("Tally: " + tally);
                    tally.add(i);
                }
            }
        }
        return tally;
    }

    public  JSONObject checkTrelloForSO(String soNum) {

        String modelTypes = "cards";
        String card_fields = "name,closed,desc,idList,labels";
        //String card_fields = "closed,idList,labels";

        TrelloCalls trelloAPICall = new TrelloCalls(this.client, "search", String.format("query=%s&card_board=true&modelTypes=%s&card_fields=%s&card_attachments=true",
                soNum, modelTypes, card_fields));

        System.out.println("\n-- Check Trello For SO --");
        var response = trelloAPICall.getTrelloAPICall();
        System.out.println(response);

        if(!(response == null) && response.has("cards")){
            if(response.getJSONArray("cards").length() > 0){
                JSONArray cards = response.getJSONArray("cards");

                List<JSONObject> openTrelloCards = new ArrayList<>();

                for (int i = 0; i < cards.length(); i++) {
                    if (!cards.getJSONObject(i).getBoolean("closed")) {
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
                        trelloCalls.deleteTrelloAPICall(json.getString("id"));
                    }

                    return desiredCard;
                }else if(openTrelloCards.size() == 1){
                    return openTrelloCards.get(0);
                }else{
                    JSONObject json = new JSONObject();
                    json.put("id","Empty");
                    return json;
                }
            }
        }

        JSONObject json = new JSONObject();
        json.put("id","Empty");
        return json;
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

        checkTrelloCardForEmptyCustomFields(response.getString("id"), itemInformation);
    }

    public void updateTrelloCards() {

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(this.client, this.contextID, this.branch);

        List<String> liveTrelloBuckets = new ArrayList<>(Arrays.asList("62869b5c1351de037ffd2cbc", "61f2d5c461ac134ef274ae5f",
                "62869b5c1351de037ffd2ccd", "6239c656ab5c356ec1568beb", "62869b5c1351de037ffd2cce",
                "60c26dfb44555566d32ae64d", "62869b5c1351de037ffd2cd0", "61e6d38623686777464221b9",
                "62869b5c1351de037ffd2cd1", "60c26dfb44555566d32ae64e", "62869b5c1351de037ffd2cd4",
                "61b360e35ab37c0d9037c19f","6384cfab789e5f01197094ec"));

        JSONObject fetchedSalesOrderData = itemCodeHandler.agilityChangedSalesOrderListLookup();

        //TODO adapt this for Sales orders

        if(!(fetchedSalesOrderData == null) && fetchedSalesOrderData.has("dtOrderResponse")) {

            JSONArray salesOrderDataArray = fetchedSalesOrderData.getJSONArray("dtOrderResponse");

            for(int i = 0; i < salesOrderDataArray.length(); i++) {

                JSONObject result = checkTrelloForSO(String.valueOf(salesOrderDataArray.getJSONObject(i).getNumber("OrderID")));

                ItemCodeHandler salesDataItemHandler = new ItemCodeHandler(this.client, this.contextID, salesOrderDataArray.getJSONObject(i).getNumber("OrderID").toString(), salesOrderDataArray.getJSONObject(i), this.branch);

                if (!(result == null) && result.has("id")){
                    if (!result.getString("id").equals("Empty")) {

                        JSONObject itemInformation = salesDataItemHandler.itemParseProcess();

                        boolean sameBoard = itemInformation.getString("boardID").equals(result.getJSONObject("board").getString("id"));
                        //boolean foundBoard = !itemInformation.getString("boardID").equals("None Found");

                        if(!itemInformation.getString("boardID").equals("None Found")){

                            System.out.println(itemInformation.getString("idList")+"\n");

                            if(result.has("idList") &&
                                    !(itemInformation.getString("idList").equals("62869b5c1351de037ffd2cd4") ||
                                            itemInformation.getString("idList").equals("61b360e35ab37c0d9037c19f")) &&
                            sameBoard){
                                //TODO above checks if current board is destination board
                                if(!liveTrelloBuckets.contains(result.getString("idList"))) {
                                    itemInformation.remove("idList");
                                    itemInformation.put("idList", result.getString("idList"));
                                }
                            }

                            ArrayList<String> labelIds = new ArrayList<>();
                            if(result.has("labels") && sameBoard) {
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

                            checkTrelloCardForEmptyCustomFields(response.getString("id"), itemInformation);

                            System.out.println("\nUpdates Applied");

                        }else{
                           System.out.println("\n-- No Applicable Boards Found --");
                        }
                    }else{
                        System.out.println("\n- Trello Hasn't Updated Yet -");
                        //TODO Work out some way to create a card if there isn't one on Trello yet
                    }
                }else if (!(result == null) && result.has("error")) {
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

    public void checkTrelloCardForEmptyCustomFields(String cardId, JSONObject itemInformation) {

        if(!(itemInformation == null) && itemInformation.has("colorCode")){
            if (itemInformation.getString("colorCode") != null)
                updateCustomFieldTrello(cardId, itemInformation.getString("colorCustomField"), itemInformation.getString("colorCode"));
        }

        if(!(itemInformation == null) && itemInformation.has("linkedID") && itemInformation.has("linkedType")){
            if (itemInformation.getString("linkedID") != null && itemInformation.getString("linkedType").equals("RM"))
                updateCustomFieldTrello(cardId, itemInformation.getString("rmCustomField"), itemInformation.getString("linkedID"));
        }
        //TODO Look into auto populating PO's
        //Probably applicable with Tru and CNC
//        if(!itemInformation.getString("linkedID").equals("") && itemInformation.getString("linkedType").equals("PO"))
//            customFieldTrello(client,cardId, poFieldId, itemInformation.getString("linkedID"));

    }

    public void updateCustomFieldTrello(String cardId, String customFieldID, String value) {
        String urlEndpoint = String.format("cards/%s/customField/%s/item", cardId, customFieldID );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", value);
        System.out.println("\n-- Update Custom Field in Trello --");
        TrelloCalls trelloCalls = new TrelloCalls(client, urlEndpoint, "");
        trelloCalls.putTrelloAPICall(jsonObject);
    }

    public String agilityDataForTrelloGather(JSONObject jsonSO, JSONObject itemInformation){

        String boardID = itemInformation.getString("boardID");
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

        String address = "";
        String city = "";
        String state = "";
        String zip = "";

        name = urlify(name);
        description = urlify(description);

        if(!(jsonSO.getString("ShipToAddress1").equals("- Verified Address -") || jsonSO.getString("ShipToAddress1").isBlank())){
            address = jsonSO.getString("ShipToAddress1");
        }else if(!jsonSO.getString("ShipToAddress2").isBlank()){
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

//        name = name.replace(" ", "%20");
//        name = name.replace("&", "%26");
//        name = name.replace(",", "%2C");
//        name = name.replace("#", "%23");
//        name = name.replace("@", "%40");
//        name = name.replace("*", "%2A");
//        name = name.replace("'", "%27");
//
//        description = description.replace(" ", "%20");
//        description = description.replace("&", "%26");
//        description = description.replace(",", "%2C");
//        description = description.replace("#", "%23");
//        description = description.replace("@", "%40");
//        description = description.replace("*", "%2A");
//        description = description.replace("'", "%27");

            parameters = String.format(
                    "idBoard=%s&idList=%s&name=%s" +
                            "&idLabels=%s&due=%s&coordinates=%s" +
                            "&locationName=%s",
                    boardID, idList, name, idLabels, dueDate,
                    urlify(tomTomCalls.getLatitude() + "," + tomTomCalls.getLongitude()),
                    urlify(tomTomCalls.getResponseAddress()));
        }else{
            parameters = String.format(
                    "idBoard=%s&idList=%s&name=%s" +
                            "&idLabels=%s&due=%s",
                    boardID, idList, name, idLabels, dueDate);
        }

        if(!description.isEmpty()){
            parameters += String.format("&desc=%s", description);
        }

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
}

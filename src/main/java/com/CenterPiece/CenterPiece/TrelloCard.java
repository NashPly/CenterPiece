package com.CenterPiece.CenterPiece;

import org.json.JSONObject;

import java.util.List;

public class TrelloCard {


    private String cardTitle;
    private String description;
    private String cardID;
    private List<JSONObject> idLabels;
    private String idList;

    public TrelloCard(String cardTitle, String description, String cardID, List<JSONObject> idLabels, String idList) {
        this.cardTitle = cardTitle;
        this.description = description;
        this.cardID = cardID;
        this.idLabels = idLabels;
        this.idList = idList;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public List<JSONObject> getIdLabels() {
        return idLabels;
    }

    public void setIdLabels(List<JSONObject> idLabels) {
        this.idLabels = idLabels;
    }

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }
}

package com.CenterPiece.CenterPiece.TrelloIDs;

import java.util.Arrays;

public class TrelloBoardIDs {
    TrelloBoards board;
    String boardId;
    String branch;
    String environment;

    public TrelloBoardIDs(TrelloBoards board, String branch, String environment) {
        this.board = board;
        this.branch = branch;
        this.environment = environment;
    }

    public TrelloBoardIDs(TrelloBoards boards) {this.board = board;}

    public TrelloBoardIDs(String idBoard){this.setBoard(idBoard);}

    public void setBoard(String board) {

        this.boardId = board;

        switch (board) {
            case "60c26dfb44555566d32ae643" -> {
                this.board = TrelloBoards.TOP_SHOP;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }

            case "62869b5c1351de037ffd2cbb" -> {
                this.board = TrelloBoards.CABINETS;
                this.branch = "CABINETS";
                this.environment = "Production";
            }

            case "636bc3a95da9340015e47b84" -> {
                this.board = TrelloBoards.COMPONENTS;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }

            //Test Environment

            case "6596e9210326360265ae3347" -> {
                this.board = TrelloBoards.TOP_SHOP;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }

            case "6596e945627ec8be307b1e0f" -> {
                this.board = TrelloBoards.CABINETS;
                this.branch = "CABINETS";
                this.environment = "Test";
            }

            case "6596ece3760cfe2637c6f944" -> {
                this.board = TrelloBoards.COMPONENTS;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            
            default -> {
                this.board = TrelloBoards.TOP_SHOP;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
        }
    }

    public String getBoardID() {
        if(this.environment.equals("Production")){
            if(this.branch.equals("TOPSHOP")) {
                this.boardId = "60c26dfb44555566d32ae643";
            }else if(this.branch.equals("CABINETS")) {
                this.boardId = "62869b5c1351de037ffd2cbb";
            }else if(this.branch.equals("COMPONENTS")) {
                this.boardId = "636bc3a95da9340015e47b84";
            }else{
                this.boardId = "60c26dfb44555566d32ae643";}
        }else if (this.environment.equals("Test")){
            if(this.branch.equals("TOPSHOP")) {
                this.boardId = "6596e9210326360265ae3347";
            }else if(this.branch.equals("CABINETS")) {
                this.boardId = "6596e945627ec8be307b1e0f";
            }else if(this.branch.equals("COMPONENTS")) {
                this.boardId = "6596ece3760cfe2637c6f944";
            }else{
                this.boardId = "60c26dfb44555566d32ae643";
            }
        }else{
            this.boardId = "60c26dfb44555566d32ae643";
        }
        return this.boardId;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
//TODO create transferred to nashville and COD TO BE PAID
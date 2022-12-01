package com.CenterPiece.CenterPiece;

public class TrelloListIDs {
    TrelloLists list;
    String branch;

    public TrelloListIDs(TrelloLists list, String branch) {
        this.list = list;
        this.branch = branch;
    }

    public TrelloListIDs(TrelloLists lists) {
        this.list = list;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getListID() {

        if(this.branch.equals("TOPSHOP")) {
            switch (list) {
                case QUOTED -> {
                    return "60c26dfb44555566d32ae647";
                }
                case BATCHING -> {
                    return "60c26dfb44555566d32ae651";
                }
                case SO_SID_CHECK -> {
                    return "62c4430fcdfa097c5642436b";
                }
                case ON_HOLD -> {
                    return "61c1e06cdc22878b2e8c7ae7";
                }
                case PROCESSING -> {
                    return "61b35f8a4f5eab8d0b16235e";
                }
                case TO_BE_ORDERED -> {
                    return "6259bb7ee9fc5f8d3659ca5e";
                }
                case ON_ORDER -> {
                    return "60c26dfb44555566d32ae648";
                }
                case RECEIVING -> {
                    return "60c26dfb44555566d32ae649";
                }
                case SCHEDULING_POOL -> {
                    return "60c26dfb44555566d32ae64b";
                }
                case PRODUCTION_QUEUE -> {
                    return "6228d9a38275a92c6f512f69";
                }
                case IN_PRODUCTION -> {
                    return "60c26dfb44555566d32ae64c";
                }
                case TO_BE_PICKED -> {
                    return "6239c656ab5c356ec1568beb";
                }
                case PICKED_AND_STAGED -> {
                    return "60c26dfb44555566d32ae64d";
                }
                case TO_BE_LOADED_ROUTED -> {
                    return "623c7ddd7e9e617c2c26df74";
                }
                case WILL_CALL -> {
                    return "61e6d38623686777464221b9";
                }
                case ON_TRUCK_ON_DELIVERY -> {
                    return "60c26dfb44555566d32ae64e";
                }
                case DELIVERED_PICKED_UP -> {
                    return "60c26dfb44555566d32ae64f";
                }
                case INVOICED -> {
                    return "61b360e35ab37c0d9037c19f";
                }
                case RECEIVED -> {
                    return "6214e94f500c90508ff521ed";
                }
                case BRANCH_ID -> {
                    return "60c26dfb44555566d32ae643";
                }
                case INBOX -> {
                    return "61f2d5c461ac134ef274ae5f";
                }
                default -> {
                    return "61f2d5c461ac134ef274ae5f";
                }
            }
        }

        else if(this.branch.equals("CABINETS")) {
            switch (list) {
                case QUOTED -> {
                    return "62869b5c1351de037ffd2cc2";
                }
                case PROCESSING -> {
                    return "62869b5c1351de037ffd2cc4";
                }
                case TO_BE_ORDERED -> {
                    return "62869b5c1351de037ffd2cc5";
                }
                case ON_ORDER -> {
                    return "62869b5c1351de037ffd2cc6";
                }
                case RECEIVING -> {
                    return "62869b5c1351de037ffd2cc7";
                }
                case ON_HOLD -> {
                    return "62869b5c1351de037ffd2cc8";
                }
                case PRODUCTION_QUEUE -> {
                    return "62869b5c1351de037ffd2cca";
                }
                case IN_PRODUCTION -> {
                    return "62869b5c1351de037ffd2ccb";
                }
                case TO_BE_PICKED -> {
                    return "62869b5c1351de037ffd2ccd";
                }
                case PICKED_AND_STAGED -> {
                    return "62869b5c1351de037ffd2cce";
                }
                case TO_BE_LOADED_ROUTED -> {
                    return "62869b5c1351de037ffd2ccf";
                }
                case WILL_CALL -> {
                    return "62869b5c1351de037ffd2cd0";
                }
                case ON_TRUCK_ON_DELIVERY -> {
                    return "62869b5c1351de037ffd2cd1";
                }
                case DELIVERED_PICKED_UP -> {
                    return "62869b5c1351de037ffd2cd2";
                }
                case INVOICED -> {
                    return "62869b5c1351de037ffd2cd4";
                }
                case RECEIVED -> {
                    return "62869b5c1351de037ffd2cd5";
                }
                case BRANCH_ID -> {
                    return "62869b5c1351de037ffd2cbb";
                }
                case INBOX -> {
                    return "62869b5c1351de037ffd2cbc";
                }
                default -> {
                    return "62869b5c1351de037ffd2cbc";
                }
            }
        }else{
            return "61f2d5c461ac134ef274ae5f";
        }
    }
}

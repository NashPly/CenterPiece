package com.CenterPiece.CenterPiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public TrelloListIDs(String idList){
        this.setList(idList);
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setList(String list) {

        switch (list) {
            case "60c26dfb44555566d32ae647" -> {
                this.list = TrelloLists.QUOTED;
                this.branch = "TOPSHOP";
            }
            case "60c26dfb44555566d32ae651" -> {
                this.list = TrelloLists.BATCHING;
                this.branch = "TOPSHOP";
            }
            case "62c4430fcdfa097c5642436b" -> {
                this.list = TrelloLists.SO_SID_CHECK;
                this.branch = "TOPSHOP";
            }
            case "61c1e06cdc22878b2e8c7ae7" -> {
                this.list = TrelloLists.ON_HOLD;
                this.branch = "TOPSHOP";
            }
            case "61b35f8a4f5eab8d0b16235e" -> {
                this.list = TrelloLists.PROCESSING;
                this.branch = "TOPSHOP";
            }
            case "6259bb7ee9fc5f8d3659ca5e" -> {
                this.list = TrelloLists.TO_BE_ORDERED;
                this.branch = "TOPSHOP";
            }
            case "60c26dfb44555566d32ae648" -> {
                this.list = TrelloLists.ON_ORDER;
                this.branch = "TOPSHOP";
            }
            case "60c26dfb44555566d32ae649" -> {
                this.list = TrelloLists.RECEIVING;
                this.branch = "TOPSHOP";
            }
            case "639871e0cb87d801a97ad7aa" -> {
                this.list = TrelloLists.CREDIT_HOLD;
                this.branch = "TOPSHOP";
            }
            case "60c26dfb44555566d32ae64b" -> {
                this.list = TrelloLists.SCHEDULING_POOL;
                this.branch = "TOPSHOP";
            }
            case "6228d9a38275a92c6f512f69" -> {
                this.list = TrelloLists.PRODUCTION_QUEUE;
                this.branch = "TOPSHOP";
            }
            case "60c26dfb44555566d32ae64c" -> {
                this.list = TrelloLists.IN_PRODUCTION;
                this.branch = "TOPSHOP";
            }
            case "6239c656ab5c356ec1568beb" -> {
                this.list = TrelloLists.TO_BE_PICKED;
                this.branch = "TOPSHOP";
            }
            case "60c26dfb44555566d32ae64d" -> {
                this.list = TrelloLists.PICKED_AND_STAGED;
                this.branch = "TOPSHOP";
            }
            case "623c7ddd7e9e617c2c26df74" -> {
                this.list = TrelloLists.TO_BE_LOADED_ROUTED;
                this.branch = "TOPSHOP";
            }
            case "61e6d38623686777464221b9" -> {
                this.list = TrelloLists.WILL_CALL;
                this.branch = "TOPSHOP";
            }
            case "6384cfab789e5f01197094ec" -> {
                this.list = TrelloLists.TRANSFERRED_TO_NASHVILLE;
                this.branch = "TOPSHOP";
            }
            case "60c26dfb44555566d32ae64e" -> {
                this.list = TrelloLists.ON_TRUCK_ON_DELIVERY;
                this.branch = "TOPSHOP";
            }
            case "61b360d9b996930158a55e11" -> {
                this.list = TrelloLists.COD_TO_BE_PAID;
                this.branch = "TOPSHOP";
            }
            case "60c26dfb44555566d32ae64f" -> {
                this.list = TrelloLists.DELIVERED_PICKED_UP;
                this.branch = "TOPSHOP";
            }
            case "61b360e35ab37c0d9037c19f" -> {
                this.list = TrelloLists.INVOICED;
                this.branch = "TOPSHOP";
            }
            case "6214e94f500c90508ff521ed" -> {
                this.list = TrelloLists.RECEIVED;
                this.branch = "TOPSHOP";
            }
            case "60c26dfb44555566d32ae643" -> {
                this.list = TrelloLists.BRANCH_ID;
                this.branch = "TOPSHOP";
            }
            case "61f2d5c461ac134ef274ae5f" -> {
                this.list = TrelloLists.INBOX;
                this.branch = "TOPSHOP";
            }



            case "62869b5c1351de037ffd2cc2" -> {
                this.list = TrelloLists.QUOTED;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cc4" -> {
                this.list = TrelloLists.PROCESSING;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cc5" -> {
                this.list = TrelloLists.TO_BE_ORDERED;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cc6" -> {
                this.list = TrelloLists.ON_ORDER;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cc7" -> {
                this.list = TrelloLists.RECEIVING;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cca" -> {
                this.list = TrelloLists.PRODUCTION_QUEUE;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2ccb" -> {
                this.list = TrelloLists.IN_PRODUCTION;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2ccd" -> {
                this.list = TrelloLists.TO_BE_PICKED;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cce" -> {
                this.list = TrelloLists.PICKED_AND_STAGED;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2ccf" -> {
                this.list = TrelloLists.TO_BE_LOADED_ROUTED;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cd0" -> {
                this.list = TrelloLists.WILL_CALL;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cd1" -> {
                this.list = TrelloLists.ON_TRUCK_ON_DELIVERY;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cd3" -> {
                this.list = TrelloLists.COD_TO_BE_PAID;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cd2" -> {
                this.list = TrelloLists.DELIVERED_PICKED_UP;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cd4" -> {
                this.list = TrelloLists.INVOICED;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cd5" -> {
                this.list = TrelloLists.RECEIVED;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cbb" -> {
                this.list = TrelloLists.BRANCH_ID;
                this.branch = "CABINETS";
            }
            case "62869b5c1351de037ffd2cbc" -> {
                this.list = TrelloLists.INBOX;
                this.branch = "CABINETS";
            }

            case "636bc3a95da9340015e47b8f" -> {
                this.list = TrelloLists.QUOTED;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b90" -> {
                this.list = TrelloLists.PROCESSING;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b91" -> {
                this.list = TrelloLists.TO_BE_ORDERED;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b92" -> {
                this.list = TrelloLists.ON_ORDER;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b93" -> {
                this.list = TrelloLists.RECEIVING;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b94" -> {
                this.list = TrelloLists.ON_HOLD;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b95" -> {
                this.list = TrelloLists.PRODUCTION_QUEUE;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b96" -> {
                this.list = TrelloLists.IN_PRODUCTION;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b98" -> {
                this.list = TrelloLists.TO_BE_PICKED;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b99" -> {
                this.list = TrelloLists.PICKED_AND_STAGED;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b9a" -> {
                this.list = TrelloLists.TO_BE_LOADED_ROUTED;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b9b" -> {
                this.list = TrelloLists.WILL_CALL;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b9c" -> {
                this.list = TrelloLists.ON_TRUCK_ON_DELIVERY;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b9f" -> {
                this.list = TrelloLists.COD_TO_BE_PAID;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b9d" -> {
                this.list = TrelloLists.DELIVERED_PICKED_UP;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b9e" -> {
                this.list = TrelloLists.INVOICED;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47ba0" -> {
                this.list = TrelloLists.RECEIVED;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b84" -> {
                this.list = TrelloLists.BRANCH_ID;
                this.branch = "COMPONENTS";
            }
            case "636bc3a95da9340015e47b8b" -> {
                this.list = TrelloLists.INBOX;
                this.branch = "COMPONENTS";
            }

            default -> {
                this.list = TrelloLists.INBOX;
                this.branch = "CABINETS";
            }
        }
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
                case CREDIT_HOLD -> {
                    return "639871e0cb87d801a97ad7aa";
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
        }
        else if(this.branch.equals("COMPONENTS")) {
            switch (list) {
                case QUOTED -> {
                    return "636bc3a95da9340015e47b8f";
                }
                case PROCESSING -> {
                    return "636bc3a95da9340015e47b90";
                }
                case TO_BE_ORDERED -> {
                    return "636bc3a95da9340015e47b91";
                }
                case ON_ORDER -> {
                    return "636bc3a95da9340015e47b92";
                }
                case RECEIVING -> {
                    return "636bc3a95da9340015e47b93";
                }
                case ON_HOLD -> {
                    return "636bc3a95da9340015e47b94";
                }
                case PRODUCTION_QUEUE -> {
                    return "636bc3a95da9340015e47b95";
                }
                case IN_PRODUCTION -> {
                    return "636bc3a95da9340015e47b96";
                }
                case TO_BE_PICKED -> {
                    return "636bc3a95da9340015e47b98";
                }
                case PICKED_AND_STAGED -> {
                    return "636bc3a95da9340015e47b99";
                }
                case TO_BE_LOADED_ROUTED -> {
                    return "636bc3a95da9340015e47b9a";
                }
                case WILL_CALL -> {
                    return "636bc3a95da9340015e47b9b";
                }
                case ON_TRUCK_ON_DELIVERY -> {
                    return "636bc3a95da9340015e47b9c";
                }
                case DELIVERED_PICKED_UP -> {
                    return "636bc3a95da9340015e47b9d";
                }
                case INVOICED -> {
                    return "636bc3a95da9340015e47b9e";
                }
                case RECEIVED -> {
                    return "636bc3a95da9340015e47ba0";
                }
                case BRANCH_ID -> {
                    return "636bc3a95da9340015e47b84";
                }
                case INBOX -> {
                    return "636bc3a95da9340015e47b8b";
                }
                default -> {
                    return "636bc3a95da9340015e47b8b";
                }
            }
        }else{
            return "61f2d5c461ac134ef274ae5f";
        }
    }

    public Boolean offLimits(){
//        List<String> liveTrelloBuckets = new ArrayList<>(Arrays.asList("62869b5c1351de037ffd2cbc", "61f2d5c461ac134ef274ae5f",
//                "62869b5c1351de037ffd2ccd", "6239c656ab5c356ec1568beb", "62869b5c1351de037ffd2cce",
//                "60c26dfb44555566d32ae64d", "62869b5c1351de037ffd2cd0", "61e6d38623686777464221b9",
//                "62869b5c1351de037ffd2cd1", "60c26dfb44555566d32ae64e", "62869b5c1351de037ffd2cd4",
//                "61b360e35ab37c0d9037c19f","6384cfab789e5f01197094ec"));

        List<TrelloLists> liveTrelloBuckets = new ArrayList<>(Arrays.asList(TrelloLists.BATCHING, TrelloLists.SO_SID_CHECK,
                TrelloLists.ON_HOLD, TrelloLists.PROCESSING, TrelloLists.TO_BE_ORDERED, TrelloLists.ON_ORDER,
                TrelloLists.RECEIVING, TrelloLists.PRODUCTION_QUEUE, TrelloLists.IN_PRODUCTION, TrelloLists.TO_BE_LOADED_ROUTED,
                TrelloLists.TRANSFERRED_TO_NASHVILLE, TrelloLists.ON_TRUCK_ON_DELIVERY, TrelloLists.DELIVERED_PICKED_UP,
                TrelloLists.COD_TO_BE_PAID));

        //var hold = liveTrelloBuckets.contains(this.list);

        return liveTrelloBuckets.contains(this.list);
    }
}
//TODO create transferred to nashville and COD TO BE PAID
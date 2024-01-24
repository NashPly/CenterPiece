package com.CenterPiece.CenterPiece.TrelloIDs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrelloLabelIds {

    TrelloLabels label;
    String labelId;
    String branch;
    String environment;
    String saleType;

    public TrelloLabelIds(TrelloLabels label, String branch, String environment, String saleType) {
        this.label = label;
        this.branch = branch;
        this.environment = environment;
        this.saleType = saleType;
    }

    public String getAllRelevantLabelIds() {
        if (saleType.equals("WHSE")){
            System.out.println(getlabelID(label) + "," + getlabelID(TrelloLabels.WHSE));
        return (getlabelID(label) + "," + getlabelID(TrelloLabels.WHSE));
    }
        else if (saleType.equals("Willcall"))
            return getlabelID(label) + "," + getlabelID(TrelloLabels.WILLCALL);

        return getlabelID(label);
    }

    public TrelloLabelIds(TrelloLabels label) {
        this.label = label;
    }

    public TrelloLabelIds(String idlabel) {
        this.labelId = idlabel;
        this.setLabel(idlabel);
    }

    private void setLabel(String label) {

        switch (label) {
            case "60c26dfc44555566d32ae700" -> {
                this.label = TrelloLabels.TOPS;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "65b023cfbafe018f9f113c78" -> {
                this.label = TrelloLabels.WHSE;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "6197b500bbb79658801189ce" -> {
                this.label = TrelloLabels.WILLCALL;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "62f6a75f8db34f1e9ac4467e" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "6596e9210326360265ae33d5" -> {
                this.label = TrelloLabels.TOPS;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "65b025c7adcf5813405dd2bd" -> {
                this.label = TrelloLabels.WHSE;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "65b025cd2a22cf0417ecd11b" -> {
                this.label = TrelloLabels.WILLCALL;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "6596e9210326360265ae340e" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }

            case "62869e47dcae4f52e15c90e1" -> {
                this.label = TrelloLabels.CNC_CABINET;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "62869b5c1351de037ffd2d26" -> {
                this.label = TrelloLabels.KK_CABINET;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "62869db3e04b83468347996b" -> {
                this.label = TrelloLabels.LEGACY_CABINET;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "62869b5c1351de037ffd2d32" -> {
                this.label = TrelloLabels.CHOICE_CABINET;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "65a9526ced9de1398df49ae3" -> {
                this.label = TrelloLabels.WHSE;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "65a952409cdbee377a23b6f7" -> {
                this.label = TrelloLabels.WILLCALL;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "633d78eb95e9c201251a9264" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "CABINET";
                this.environment = "Production";
            }

            case "6596e948627ec8be307b2c4b" -> {
                this.label = TrelloLabels.CNC_CABINET;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "6596e948627ec8be307b2c36" -> {
                this.label = TrelloLabels.KK_CABINET;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "6596e948627ec8be307b2c48" -> {
                this.label = TrelloLabels.LEGACY_CABINET;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "6596e948627ec8be307b2c42" -> {
                this.label = TrelloLabels.CHOICE_CABINET;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "65b03b61c814fd09e5c1104f" -> {
                this.label = TrelloLabels.WHSE;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "65b03b67fba3b9aec1b794d6" -> {
                this.label = TrelloLabels.WILLCALL;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "6596e948627ec8be307b2c5a" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "CABINET";
                this.environment = "Test";
            }

            case "646661e617ed02525772a4d8" -> {
                this.label = TrelloLabels.COMPONENTS;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "65b03ba21a41c33bddeb6e0b" -> {
                this.label = TrelloLabels.WHSE;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "65b03ba9cffdcf6ced8482da" -> {
                this.label = TrelloLabels.WILLCALL;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "636bc3a95da9340015e47c2c" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "6596ece3760cfe2637c6f9d5" -> {
                this.label = TrelloLabels.COMPONENTS;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "65b03bc32bcf416e018c5048" -> {
                this.label = TrelloLabels.WHSE;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "65b03bc83da6dd695bc2e220" -> {
                this.label = TrelloLabels.WILLCALL;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6596ece3760cfe2637c6f9d2" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }

            default -> {
                this.label = TrelloLabels.NONE;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
        }
    }

    private String getlabelID(TrelloLabels trelloLabel) {
        //TODO REWORK FOR LABELS

        if (environment.equals("Production")) {
            if (branch.equals("TOPSHOP")) {
                this.labelId = switch (trelloLabel) {
                    case TOPS -> "60c26dfc44555566d32ae700";
                    case WHSE -> "65b023cfbafe018f9f113c78";
                    case WILLCALL -> "6197b500bbb79658801189ce";
                    case NONE -> "62f6a75f8db34f1e9ac4467e";
                    default -> "62f6a75f8db34f1e9ac4467e";
                };
            } else if (branch.equals("CABINETS")) {
                this.labelId = switch (trelloLabel) {
                    case CNC_CABINET -> "62869e47dcae4f52e15c90e1";
                    case KK_CABINET -> "62869b5c1351de037ffd2d26";
                    case LEGACY_CABINET -> "62869db3e04b83468347996b";
                    case CHOICE_CABINET -> "62869b5c1351de037ffd2d32";
                    case WHSE -> "65a9526ced9de1398df49ae3";
                    case WILLCALL -> "65a952409cdbee377a23b6f7";
                    case NONE -> "633d78eb95e9c201251a9264";
                    default -> "633d78eb95e9c201251a9264";
                };
            } else if (branch.equals("COMPONENTS")) {
                this.labelId = switch (trelloLabel) {
                    case COMPONENTS -> "646661e617ed02525772a4d8";
                    case WHSE -> "65b03ba21a41c33bddeb6e0b";
                    case WILLCALL -> "65b03ba9cffdcf6ced8482da";
                    case NONE -> "636bc3a95da9340015e47c2c";
                    default -> "636bc3a95da9340015e47c2c";
                };
            } else {
                // Handle other branches in Production environment
                this.labelId = "";
            }
        } else if (environment.equals("Test")) {
            if (branch.equals("TOPSHOP")) {
                this.labelId = switch (trelloLabel) {
                    case TOPS -> "6596e9210326360265ae33d5";
                    case WHSE -> "65b025c7adcf5813405dd2bd";
                    case WILLCALL -> "65b023e5cf8929b76dd4fa09";
                    case NONE -> "6596e9210326360265ae340e";
                    default -> "6596e9210326360265ae340e";
                };
            } else if (branch.equals("CABINETS")) {
                this.labelId = switch (trelloLabel) {
                    case CNC_CABINET -> "6596e948627ec8be307b2c4b";
                    case KK_CABINET -> "6596e948627ec8be307b2c36";
                    case LEGACY_CABINET -> "6596e948627ec8be307b2c48";
                    case CHOICE_CABINET -> "6596e948627ec8be307b2c42";
                    case WHSE -> "65b03b61c814fd09e5c1104f";
                    case WILLCALL -> "65b03b67fba3b9aec1b794d6";
                    case NONE -> "6596e948627ec8be307b2c5a";
                    default -> "6596e948627ec8be307b2c5a";
                };
            } else if (branch.equals("COMPONENTS")) {
                this.labelId = switch (trelloLabel) {
                    case COMPONENTS -> "6596ece3760cfe2637c6f9d5";
                    case WHSE -> "65b03bc32bcf416e018c5048";
                    case WILLCALL -> "65b03bc83da6dd695bc2e220";
                    case NONE -> "6596ece3760cfe2637c6f9d2";
                    default -> "6596ece3760cfe2637c6f9d2";
                };
            } else {
                // Handle other branches in Test environment
                this.labelId = "";
            }
        } else {
            // Handle other environments
            this.labelId = "";
        }
        return this.labelId;
    }

    public boolean isBrandLabel() {

        List<TrelloLabels> brandLabelList = new ArrayList<>(Arrays.asList(TrelloLabels.CNC_CABINET,
                TrelloLabels.KK_CABINET, TrelloLabels.CHOICE_CABINET, TrelloLabels.LEGACY_CABINET,
                TrelloLabels.TOPS, TrelloLabels.COMPONENTS));

        return brandLabelList.contains(this.label);
    }
    public boolean isSaleTypeLabel() {

        List<TrelloLabels> saleTypeLabelList = new ArrayList<>(Arrays.asList(TrelloLabels.WHSE,TrelloLabels.WILLCALL));

        return saleTypeLabelList.contains(this.label);
    }
}

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
    String paymentTerm;

    public TrelloLabelIds(TrelloLabels label, String branch, String environment, String saleType, String paymentTerm) {
        this.label = label;
        this.branch = branch;
        this.environment = environment;
        this.saleType = saleType;
        this.paymentTerm = paymentTerm;
    }

    public String getAllRelevantLabelIDs(){
        String saleType = this.getSaleTypeLabelIds();
        String payTerm = this.getPaymentTermLabelIds();

        if(payTerm.isEmpty() || payTerm.contains("Net"))
            return saleType;
        else
            return saleType + "," + payTerm;
    }
    public String getSaleTypeLabelIds() {
        if (saleType.equals("WHSE")){
        return (getlabelID(label) + "," + getlabelID(TrelloLabels.WHSE));
    }
        else if (saleType.equals("WILLCALL"))
            return getlabelID(label) + "," + getlabelID(TrelloLabels.WILLCALL);
        return getlabelID(label);
    }
    public String getPaymentTermLabelIds() {
        if (paymentTerm.equals("COD")){
            return getlabelID(TrelloLabels.COD);
        }
        else if (paymentTerm.equals("COD-CC"))
            return getlabelID(TrelloLabels.COD_CC);
        return "";
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
            case "65b123937910864d56086ecc" -> {
                this.label = TrelloLabels.WILLCALL;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "62f6a75f8db34f1e9ac4467e" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "6661be1200d043d6db0d5a54" -> {
                this.label = TrelloLabels.COD;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "6661be18780abc68ed31e372" -> {
                this.label = TrelloLabels.COD_CC;
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
            case "6661c03c1e6ad7f90549afda" -> {
                this.label = TrelloLabels.COD;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "6661c041da82ea3beace3653" -> {
                this.label = TrelloLabels.COD_CC;
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
            case "6661bdd9d973998370c2a23b" -> {
                this.label = TrelloLabels.COD;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "6661bdd1fbd1165d3e318290" -> {
                this.label = TrelloLabels.COD_CC;
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
            case "6661c07fb1c9d6d9ef975eba" -> {
                this.label = TrelloLabels.COD;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "6661c08635d9a6e2ea657b2f" -> {
                this.label = TrelloLabels.COD_CC;
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
            case "6661be2c1f304d20201a37ba" -> {
                this.label = TrelloLabels.COD;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "6661be3307b1d2086978dbbc" -> {
                this.label = TrelloLabels.COD_CC;
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
            case "6661c0656c28cfaa8eb3279c" -> {
                this.label = TrelloLabels.COD;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6661c06a48e4a1b487f59639" -> {
                this.label = TrelloLabels.COD_CC;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }

            case "6655edac93277a7afa62ce24" -> {
                this.label = TrelloLabels.WHSE;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62ce21" -> {
                this.label = TrelloLabels.WILLCALL;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62ce1e" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6661be44503b5f005e1592d4" -> {
                this.label = TrelloLabels.COD;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6661be4a81f1b1e910d8af14" -> {
                this.label = TrelloLabels.COD_CC;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }

// TODO uncomment when test distribution board is created
//            case "6655edac93277a7afa62ce24" -> {
//                this.label = TrelloLabels.WHSE;
//                this.branch = "NASHVILLE";
//                this.environment = "Test";
//            }
//            case "" -> {
//                this.label = TrelloLabels.WILLCALL;
//                this.branch = "NASHVILLE";
//                this.environment = "Test";
//            }
//            case "" -> {
//                this.label = TrelloLabels.NONE;
//                this.branch = "NASHVILLE";
//                this.environment = "Test";
//            }

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
                    case COD -> "6661be1200d043d6db0d5a54";
                    case COD_CC -> "6661be18780abc68ed31e372";
                    case TOPS -> "60c26dfc44555566d32ae700";
                    case WHSE -> "65b023cfbafe018f9f113c78";
                    case WILLCALL -> "65b123937910864d56086ecc";
                    case NONE -> "62f6a75f8db34f1e9ac4467e";
                    default -> "62f6a75f8db34f1e9ac4467e";
                };
            } else if (branch.equals("CABINETS")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661bdd9d973998370c2a23b";
                    case COD_CC -> "6661bdd1fbd1165d3e318290";
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
                    case COD -> "6661be2c1f304d20201a37ba";
                    case COD_CC -> "6661be3307b1d2086978dbbc";
                    case COMPONENTS -> "646661e617ed02525772a4d8";
                    case WHSE -> "65b03ba21a41c33bddeb6e0b";
                    case WILLCALL -> "65b03ba9cffdcf6ced8482da";
                    case NONE -> "636bc3a95da9340015e47c2c";
                    default -> "636bc3a95da9340015e47c2c";
                };
            } else if (branch.equals("NASHVILLE")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661be44503b5f005e1592d4";
                    case COD_CC -> "6661be4a81f1b1e910d8af14";
                    case WHSE -> "6655edac93277a7afa62ce24";
                    case WILLCALL -> "6655edac93277a7afa62ce21";
                    case NONE -> "6655edac93277a7afa62ce1e";
                    default -> "6655edac93277a7afa62ce1e";
                };
            } else {
                // Handle other branches in Production environment
                this.labelId = "";
            }
        } else if (environment.equals("Test")) {
            if (branch.equals("TOPSHOP")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661c03c1e6ad7f90549afda";
                    case COD_CC -> "6661c041da82ea3beace3653";
                    case TOPS -> "6596e9210326360265ae33d5";
                    case WHSE -> "65b025c7adcf5813405dd2bd";
                    case WILLCALL -> "65b023e5cf8929b76dd4fa09";
                    case NONE -> "6596e9210326360265ae340e";
                    default -> "6596e9210326360265ae340e";
                };
            } else if (branch.equals("CABINETS")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661c07fb1c9d6d9ef975eba";
                    case COD_CC -> "6661c08635d9a6e2ea657b2f";
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
                    case COD -> "6661c0656c28cfaa8eb3279c";
                    case COD_CC -> "6661c06a48e4a1b487f59639";
                    case COMPONENTS -> "6596ece3760cfe2637c6f9d5";
                    case WHSE -> "65b03bc32bcf416e018c5048";
                    case WILLCALL -> "65b03bc83da6dd695bc2e220";
                    case NONE -> "6596ece3760cfe2637c6f9d2";
                    default -> "6596ece3760cfe2637c6f9d2";
                };
            } else if (branch.equals("NASHVILLE")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661be44503b5f005e1592d4";
                    case COD_CC -> "6661be4a81f1b1e910d8af14";
                    case WHSE -> "6655edac93277a7afa62ce24";
                    case WILLCALL -> "6655edac93277a7afa62ce21";
                    case NONE -> "6655edac93277a7afa62ce1e";
                    default -> "6655edac93277a7afa62ce1e";
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

    public boolean isPaymentCodeLabel() {

        List<TrelloLabels> paymentCodeLabelList = new ArrayList<>(Arrays.asList(TrelloLabels.COD,TrelloLabels.COD_CC));

        return paymentCodeLabelList.contains(this.label);
    }
}

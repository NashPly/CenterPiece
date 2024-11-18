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

    public TrelloLabelIds(String labelId) {
        this.labelId = labelId;
        setLabel(labelId);
    }

    public TrelloLabelIds(TrelloLabels label) {
        this.label = label;
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

    private void setLabel(String label) {

        switch (label) {
            case "60c26dfc44555566d32ae700" -> {
                this.label = TrelloLabels.TOPS;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "66e455a2714648eae074a6bc" -> {
                this.label = TrelloLabels.CNC_CABINET;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "60c26dfc44555566d32ae6fe" -> {
                this.label = TrelloLabels.KK_CABINET;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "67059a23bbcbdd1e42ba302e" -> {
                this.label = TrelloLabels.LEGACY_CABINET;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "60c26dfc44555566d32ae6f6" -> {
                this.label = TrelloLabels.CHOICE_CABINET;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "67059a23bbcbdd1e42ba2fe8" -> {
                this.label = TrelloLabels.QWIKKIT_DOORS;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "6585a18482dbfe5db419d82c" -> {
                this.label = TrelloLabels.CULLMAN_DOORS;
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
            case "66e2f397f2249d499635a9f6" -> {
                this.label = TrelloLabels.TOP_ORDER;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "66e2f845c0dc46f023f3e9b4" -> {
                this.label = TrelloLabels.CAB_ORDER;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "66e2f3abb42320e73de010e6" -> {
                this.label = TrelloLabels.COMP_ORDER;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "66e2f3b7d48cff86986c50e3" -> {
                this.label = TrelloLabels.MANUAL;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "638e5d85e978f805fbcbf36f" -> {
                this.label = TrelloLabels.PARTIAL;
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
            case "670599c2e24983eb3fe3dc26" -> {
                this.label = TrelloLabels.CNC_CABINET;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "6596e9210326360265ae33d2" -> {
                this.label = TrelloLabels.KK_CABINET;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "670599c2e24983eb3fe3dc1c" -> {
                this.label = TrelloLabels.LEGACY_CABINET;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "6596e9210326360265ae33c6" -> {
                this.label = TrelloLabels.CHOICE_CABINET;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "670599c2e24983eb3fe3dbd7" -> {
                this.label = TrelloLabels.QWIKKIT_DOORS;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "6596e9210326360265ae343b" -> {
                this.label = TrelloLabels.CULLMAN_DOORS;
                this.branch = "CABINET";
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
            case "66e2f4c5e9b6032ee384a8f8" -> {
                this.label = TrelloLabels.TOP_ORDER;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "66e2f4ceb0e35bb7ff8b8601" -> {
                this.label = TrelloLabels.CAB_ORDER;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "66e2f4d912bddb93bafa3b5e" -> {
                this.label = TrelloLabels.COMP_ORDER;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "66e2f5103f9704273bca9fd9" -> {
                this.label = TrelloLabels.MANUAL;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "6596e9210326360265ae341a" -> {
                this.label = TrelloLabels.PARTIAL;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "6596e9210326360265ae340e" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }

            case "62869b5c1351de037ffd2d23" -> {
                this.label = TrelloLabels.TOPS;
                this.branch = "CABINET";
                this.environment = "Production";
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
            case "66c34d929de3e9bb23ad988d" -> {
                this.label = TrelloLabels.QWIKKIT_DOORS;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "66c34d9f7792edea8b47e7c2" -> {
                this.label = TrelloLabels.CULLMAN_DOORS;
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
            case "66e2f3dbf99f209e183b6600" -> {
                this.label = TrelloLabels.TOP_ORDER;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "66e2f3e8c2208cd963f65692" -> {
                this.label = TrelloLabels.CAB_ORDER;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "66e2f3efc791d6f19eb544a0" -> {
                this.label = TrelloLabels.COMP_ORDER;
                this.branch = "CABINET";
                this.environment = "Production";
            }
            case "66e2f3fae3c8c158534f7d58" -> {
                this.label = TrelloLabels.MANUAL;
                this.branch = "CABINET";
                this.environment = "Production";
            }

            case "6705a104529c7558d87fc2de" -> {
                this.label = TrelloLabels.COMPONENTS;
                this.branch = "CABINETS";
                this.environment = "Test";
            }
            case "6596e948627ec8be307b2c33" -> {
                this.label = TrelloLabels.TOPS;
                this.branch = "CABINETS";
                this.environment = "Test";
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
            case "66c34dbc029478341f2d1b16" -> {
                this.label = TrelloLabels.QWIKKIT_DOORS;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "66c34dc35fa012aa63af4e31" -> {
                this.label = TrelloLabels.CULLMAN_DOORS;
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
            case "66e2f4776f326d5edf7a3c20" -> {
                this.label = TrelloLabels.TOP_ORDER;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "66e2f481ae601643a3888183" -> {
                this.label = TrelloLabels.CAB_ORDER;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "66e2f4887688827338c20d60" -> {
                this.label = TrelloLabels.COMP_ORDER;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "66e2f48f63ba5fd2641f44ba" -> {
                this.label = TrelloLabels.MANUAL;
                this.branch = "CABINET";
                this.environment = "Test";
            }
            case "6596e948627ec8be307b2c5d" -> {
                this.label = TrelloLabels.PARTIAL;
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
            case "636bc3a95da9340015e47c05" -> {
                this.label = TrelloLabels.TOPS;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "670599e79102cb5395cb9e6e" -> {
                this.label = TrelloLabels.CNC_CABINET;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "636bc3a95da9340015e47c08" -> {
                this.label = TrelloLabels.KK_CABINET;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "670599e79102cb5395cb9e63" -> {
                this.label = TrelloLabels.LEGACY_CABINET;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "636bc3a95da9340015e47c14" -> {
                this.label = TrelloLabels.CHOICE_CABINET;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "670599e79102cb5395cb9e1d" -> {
                this.label = TrelloLabels.QWIKKIT_DOORS;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "670599e79102cb5395cb9e28" -> {
                this.label = TrelloLabels.CULLMAN_DOORS;
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
            case "66e2f40942a769bad5b22cb8" -> {
                this.label = TrelloLabels.TOP_ORDER;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "66e2f410001492fb2e6d363e" -> {
                this.label = TrelloLabels.CAB_ORDER;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "66e2f41782350588f2552d0a" -> {
                this.label = TrelloLabels.COMP_ORDER;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "66e2f41e4333d0f5f421e40e" -> {
                this.label = TrelloLabels.MANUAL;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "67059af9cf1b3734c1e04462" -> {
                this.label = TrelloLabels.PARTIAL;
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
            case "6596ece3760cfe2637c6f9ab" -> {
                this.label = TrelloLabels.TOPS;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6596ece3760cfe2637c6f9c3" -> {
                this.label = TrelloLabels.CNC_CABINET;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6596ece3760cfe2637c6f9ae" -> {
                this.label = TrelloLabels.KK_CABINET;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "670599f5a6d2049dd1ec1585" -> {
                this.label = TrelloLabels.LEGACY_CABINET;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6596ece3760cfe2637c6f9ba" -> {
                this.label = TrelloLabels.CHOICE_CABINET;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "670599f5a6d2049dd1ec153f" -> {
                this.label = TrelloLabels.QWIKKIT_DOORS;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "670599f5a6d2049dd1ec154a" -> {
                this.label = TrelloLabels.CULLMAN_DOORS;
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
            case "66e2f4ef753a43579d4e1aca" -> {
                this.label = TrelloLabels.TOP_ORDER;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "66e2f4f7524183d8f174020c" -> {
                this.label = TrelloLabels.CAB_ORDER;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "66e2f4fec8feae27e9d80026" -> {
                this.label = TrelloLabels.COMP_ORDER;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "66e2f505287a96b77bad009b" -> {
                this.label = TrelloLabels.MANUAL;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "67059b003342d82819be7be5" -> {
                this.label = TrelloLabels.PARTIAL;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6596ece3760cfe2637c6f9d2" -> {
                this.label = TrelloLabels.NONE;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }


            case "666ca2992a14f33355cf3d96" -> {
                this.label = TrelloLabels.COMPONENTS;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "66e9a03994f57a5af9dedd24" -> {
                this.label = TrelloLabels.TOPS;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "67059a002de9935f902fb502" -> {
                this.label = TrelloLabels.CNC_CABINET;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "67059a002de9935f902fb4d0" -> {
                this.label = TrelloLabels.KK_CABINET;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "66e1c8cafc481f7da01a7c70" -> {
                this.label = TrelloLabels.LEGACY_CABINET;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "67059a002de9935f902fb4c1" -> {
                this.label = TrelloLabels.CHOICE_CABINET;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "67059a002de9935f902fb451" -> {
                this.label = TrelloLabels.QWIKKIT_DOORS;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "66c5f3fcf4d79796859e8a64" -> {
                this.label = TrelloLabels.CULLMAN_DOORS;
                this.branch = "NASHVILLE";
                this.environment = "Production";
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
            case "66e2f43d8a99a3e1b94f3f4b" -> {
                this.label = TrelloLabels.TOP_ORDER;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "66e2f446254a99c50d87905b" -> {
                this.label = TrelloLabels.CAB_ORDER;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "66e2f44e8d549660122a77a7" -> {
                this.label = TrelloLabels.COMP_ORDER;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "66e2f459d6d2ba717a88d6e9" -> {
                this.label = TrelloLabels.MANUAL;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62ce53" -> {
                this.label = TrelloLabels.PARTIAL;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62ce1e" -> {
                this.label = TrelloLabels.NONE;
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
                    case COMPONENTS -> "656e1eaa314713b1c69d47c7";
                    case TOPS -> "60c26dfc44555566d32ae700";
                    case CNC_CABINET -> "66e455a2714648eae074a6bc";
                    case KK_CABINET -> "60c26dfc44555566d32ae6fe";
                    case LEGACY_CABINET -> "67059a23bbcbdd1e42ba302e";
                    case CHOICE_CABINET -> "60c26dfc44555566d32ae6f6";
                    case QWIKKIT_DOORS -> "67059a23bbcbdd1e42ba2fe8";
                    case CULLMAN_DOORS -> "6585a18482dbfe5db419d82c";
                    case WHSE -> "65b023cfbafe018f9f113c78";
                    case WILLCALL -> "65b123937910864d56086ecc";
                    case TOP_ORDER -> "66e2f397f2249d499635a9f6";
                    case CAB_ORDER -> "66e2f845c0dc46f023f3e9b4";
                    case COMP_ORDER -> "66e2f3abb42320e73de010e6";
                    case MANUAL -> "66e2f3b7d48cff86986c50e3";
                    case PARTIAL -> "638e5d85e978f805fbcbf36f";
                    case NONE -> "62f6a75f8db34f1e9ac4467e";
                    default -> "62f6a75f8db34f1e9ac4467e";
                };
            } else if (branch.equals("CABINETS")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661bdd9d973998370c2a23b";
                    case COD_CC -> "6661bdd1fbd1165d3e318290";
                    case COMPONENTS -> "6705a10cb4d2b972748905f7";
                    case TOPS -> "62869b5c1351de037ffd2d23";
                    case CNC_CABINET -> "62869e47dcae4f52e15c90e1";
                    case KK_CABINET -> "62869b5c1351de037ffd2d26";
                    case LEGACY_CABINET -> "62869db3e04b83468347996b";
                    case CHOICE_CABINET -> "62869b5c1351de037ffd2d32";
                    case QWIKKIT_DOORS -> "66c34d929de3e9bb23ad988d";
                    case CULLMAN_DOORS -> "66c34d9f7792edea8b47e7c2";
                    case WHSE -> "65a9526ced9de1398df49ae3";
                    case WILLCALL -> "65a952409cdbee377a23b6f7";
                    case TOP_ORDER -> "66e2f3dbf99f209e183b6600";
                    case CAB_ORDER -> "66e2f3e8c2208cd963f65692";
                    case COMP_ORDER -> "66e2f3efc791d6f19eb544a0";
                    case MANUAL -> "66e2f3fae3c8c158534f7d58";
                    case PARTIAL -> "66295d9a573b78ea4021b7d3";
                    case NONE -> "633d78eb95e9c201251a9264";
                    default -> "633d78eb95e9c201251a9264";
                };
            } else if (branch.equals("COMPONENTS")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661be2c1f304d20201a37ba";
                    case COD_CC -> "6661be3307b1d2086978dbbc";
                    case COMPONENTS -> "646661e617ed02525772a4d8";
                    case TOPS -> "636bc3a95da9340015e47c05";
                    case CNC_CABINET -> "670599e79102cb5395cb9e6e";
                    case KK_CABINET -> "636bc3a95da9340015e47c08";
                    case LEGACY_CABINET -> "670599e79102cb5395cb9e63";
                    case CHOICE_CABINET -> "636bc3a95da9340015e47c14";
                    case QWIKKIT_DOORS -> "670599e79102cb5395cb9e1d";
                    case CULLMAN_DOORS -> "670599e79102cb5395cb9e28";
                    case WHSE -> "65b03ba21a41c33bddeb6e0b";
                    case WILLCALL -> "65b03ba9cffdcf6ced8482da";
                    case TOP_ORDER -> "66e2f40942a769bad5b22cb8";
                    case CAB_ORDER -> "66e2f410001492fb2e6d363e";
                    case COMP_ORDER -> "66e2f41782350588f2552d0a";
                    case MANUAL -> "66e2f41e4333d0f5f421e40e";
                    case PARTIAL -> "67059af9cf1b3734c1e04462";
                    case NONE -> "636bc3a95da9340015e47c2c";
                    default -> "636bc3a95da9340015e47c2c";
                };
            } else if (branch.equals("NASHVILLE")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661be44503b5f005e1592d4";
                    case COD_CC -> "6661be4a81f1b1e910d8af14";
                    case COMPONENTS -> "666ca2992a14f33355cf3d96";
                    case TOPS -> "66e9a03994f57a5af9dedd24";
                    case CNC_CABINET -> "67059a002de9935f902fb502";
                    case KK_CABINET -> "67059a002de9935f902fb4d0";
                    case LEGACY_CABINET -> "66e1c8cafc481f7da01a7c70";
                    case CHOICE_CABINET -> "67059a002de9935f902fb4c1";
                    case QWIKKIT_DOORS -> "67059a002de9935f902fb451";
                    case CULLMAN_DOORS -> "66c5f3fcf4d79796859e8a64";
                    case WHSE -> "6655edac93277a7afa62ce24";
                    case WILLCALL -> "6655edac93277a7afa62ce21";
                    case TOP_ORDER -> "66e2f43d8a99a3e1b94f3f4b";
                    case CAB_ORDER -> "66e2f446254a99c50d87905b";
                    case COMP_ORDER -> "66e2f44e8d549660122a77a7";
                    case MANUAL -> "66e2f459d6d2ba717a88d6e9";
                    case PARTIAL -> "6655edac93277a7afa62ce53";
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
                    case COMPONENTS -> "6596e9210326360265ae3438";
                    case TOPS -> "6596e9210326360265ae33d5";
                    case CNC_CABINET -> "670599c2e24983eb3fe3dc26";
                    case KK_CABINET -> "6596e9210326360265ae33d2";
                    case LEGACY_CABINET -> "670599c2e24983eb3fe3dc1c";
                    case CHOICE_CABINET -> "6596e9210326360265ae33c6";
                    case QWIKKIT_DOORS -> "670599c2e24983eb3fe3dbd7";
                    case CULLMAN_DOORS -> "6596e9210326360265ae343b";
                    case WHSE -> "65b025c7adcf5813405dd2bd";
                    case WILLCALL -> "65b025cd2a22cf0417ecd11b";
                    case TOP_ORDER -> "66e2f4c5e9b6032ee384a8f8";
                    case CAB_ORDER -> "66e2f4ceb0e35bb7ff8b8601";
                    case COMP_ORDER -> "66e2f4d912bddb93bafa3b5e";
                    case MANUAL -> "66e2f5103f9704273bca9fd9";
                    case PARTIAL -> "6596e9210326360265ae341a";
                    case NONE -> "6596e9210326360265ae340e";
                    default -> "6596e9210326360265ae340e";
                };
            } else if (branch.equals("CABINETS")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661c07fb1c9d6d9ef975eba";
                    case COD_CC -> "6661c08635d9a6e2ea657b2f";
                    case COMPONENTS -> "6705a104529c7558d87fc2de";
                    case TOPS -> "6596e948627ec8be307b2c33";
                    case CNC_CABINET -> "6596e948627ec8be307b2c4b";
                    case KK_CABINET -> "6596e948627ec8be307b2c36";
                    case LEGACY_CABINET -> "6596e948627ec8be307b2c48";
                    case CHOICE_CABINET -> "6596e948627ec8be307b2c42";
                    case QWIKKIT_DOORS -> "66c34dbc029478341f2d1b16";
                    case CULLMAN_DOORS -> "66c34dc35fa012aa63af4e31";
                    case WHSE -> "65b03b61c814fd09e5c1104f";
                    case WILLCALL -> "65b03b67fba3b9aec1b794d6";
                    case TOP_ORDER -> "66e2f4776f326d5edf7a3c20";
                    case CAB_ORDER -> "66e2f481ae601643a3888183";
                    case COMP_ORDER -> "66e2f4887688827338c20d60";
                    case MANUAL -> "66e2f48f63ba5fd2641f44ba";
                    case PARTIAL -> "6596e948627ec8be307b2c5d";
                    case NONE -> "6596e948627ec8be307b2c5a";
                    default -> "6596e948627ec8be307b2c5a";
                };
            } else if (branch.equals("COMPONENTS")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661c0656c28cfaa8eb3279c";
                    case COD_CC -> "6661c06a48e4a1b487f59639";
                    case COMPONENTS -> "6596ece3760cfe2637c6f9d5";
                    case TOPS -> "6596ece3760cfe2637c6f9ab";
                    case CNC_CABINET -> "6596ece3760cfe2637c6f9c3";
                    case KK_CABINET -> "6596ece3760cfe2637c6f9ae";
                    case LEGACY_CABINET -> "670599f5a6d2049dd1ec1585";
                    case CHOICE_CABINET -> "6596ece3760cfe2637c6f9ba";
                    case QWIKKIT_DOORS -> "670599f5a6d2049dd1ec153f";
                    case CULLMAN_DOORS -> "670599f5a6d2049dd1ec154a";
                    case WHSE -> "65b03bc32bcf416e018c5048";
                    case WILLCALL -> "65b03bc83da6dd695bc2e220";
                    case TOP_ORDER -> "66e2f4ef753a43579d4e1aca";
                    case CAB_ORDER -> "66e2f4f7524183d8f174020c";
                    case COMP_ORDER -> "66e2f4fec8feae27e9d80026";
                    case MANUAL -> "66e2f505287a96b77bad009b";
                    case PARTIAL -> "67059b003342d82819be7be5";
                    case NONE -> "6596ece3760cfe2637c6f9d2";
                    default -> "6596ece3760cfe2637c6f9d2";
                };
            } else if (branch.equals("NASHVILLE")) {
                this.labelId = switch (trelloLabel) {
                    case COD -> "6661be44503b5f005e1592d4";
                    case COD_CC -> "6661be4a81f1b1e910d8af14";
                    case COMPONENTS -> "666ca2992a14f33355cf3d96";
                    case TOPS -> "66e9a03994f57a5af9dedd24";
                    case CNC_CABINET -> "67059a002de9935f902fb502";
                    case KK_CABINET -> "67059a002de9935f902fb4d0";
                    case LEGACY_CABINET -> "66e1c8cafc481f7da01a7c70";
                    case CHOICE_CABINET -> "67059a002de9935f902fb4c1";
                    case QWIKKIT_DOORS -> "67059a002de9935f902fb451";
                    case CULLMAN_DOORS -> "66c5f3fcf4d79796859e8a64";
                    case WHSE -> "6655edac93277a7afa62ce24";
                    case WILLCALL -> "6655edac93277a7afa62ce21";
                    case TOP_ORDER -> "66e2f43d8a99a3e1b94f3f4b";
                    case CAB_ORDER -> "66e2f446254a99c50d87905b";
                    case COMP_ORDER -> "66e2f44e8d549660122a77a7";
                    case MANUAL -> "66e2f459d6d2ba717a88d6e9";
                    case PARTIAL -> "6655edac93277a7afa62ce53";
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
                TrelloLabels.CULLMAN_DOORS, TrelloLabels.QWIKKIT_DOORS, TrelloLabels.TOPS, TrelloLabels.COMPONENTS));

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

    public boolean isManualOrStuckLabel() {

        List<TrelloLabels> ManualOrCrossoverLabelList = new ArrayList<>(Arrays.asList(TrelloLabels.MANUAL, TrelloLabels.PARTIAL, TrelloLabels.TOP_ORDER, TrelloLabels.CAB_ORDER, TrelloLabels.COMP_ORDER));

        return ManualOrCrossoverLabelList.contains(this.label);
    }
}

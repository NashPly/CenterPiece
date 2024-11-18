package com.CenterPiece.CenterPiece.TrelloIDs;

public class TrelloCustomFieldIDs {

    TrelloCustomFields field;

    String fieldId;
    String branch;
    String environment;

    public TrelloCustomFieldIDs(TrelloCustomFields field, String branch, String environment) {
        this.field = field;
        this.branch = branch;
        this.environment = environment;
    }

    public TrelloCustomFieldIDs(TrelloCustomFields field) {
        this.field = field;
    }

    public TrelloCustomFieldIDs(String idfield){
        this.fieldId = idfield;
        this.setfield(idfield);
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setfield(String field) {

        switch (field) {
            case "6197b500bbb79658801189ce" -> {
                this.field = TrelloCustomFields.COLOR_CODE;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "6197b57d371dc08c1f2a469a" -> {
                this.field = TrelloCustomFields.AGILITY_PO_NUMBER;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "621519b6944e3c4fc091a515" -> {
                this.field = TrelloCustomFields.REMAN_NUMBER;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "659f1842d0b2c5bf8ef3ad08" -> {
                this.field = TrelloCustomFields.NUMBER_OF_BUILDS;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "638e63205f249b03ac9edd81" -> {
                this.field = TrelloCustomFields.PARTIAL_CHECKBOX;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "63b851cf1f3eb900f95a9c39" -> {
                this.field = TrelloCustomFields.NOTES;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }
            case "65944bbce3ba00017427cb36" -> {
                this.field = TrelloCustomFields.CUSTOMER_PO_NUMBER;
                this.branch = "TOPSHOP";
                this.environment = "Production";
            }


            case "659869988ee82979ec5e7479" -> {
                this.field = TrelloCustomFields.COLOR_CODE;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "6598699f39a3eb99b38c954b" -> {
                this.field = TrelloCustomFields.AGILITY_PO_NUMBER;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "659869a44b851e58611d05b9" -> {
                this.field = TrelloCustomFields.REMAN_NUMBER;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "67043a38e82d6167a269ba18" -> {
                this.field = TrelloCustomFields.NUMBER_OF_BUILDS;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "659869a5f1c75347de06e3bd" -> {
                this.field = TrelloCustomFields.PARTIAL_CHECKBOX;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "659869b1cf91817b612b5d67" -> {
                this.field = TrelloCustomFields.NOTES;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }
            case "659869acb46dadfee6454ded" -> {
                this.field = TrelloCustomFields.CUSTOMER_PO_NUMBER;
                this.branch = "TOPSHOP";
                this.environment = "Test";
            }







            case "62869b5c1351de037ffd2da7" -> {
                this.field = TrelloCustomFields.COLOR_CODE;
                this.branch = "CABINETS";
                this.environment = "Production";
            }
            case "62869b5c1351de037ffd2da9" -> {
                this.field = TrelloCustomFields.AGILITY_PO_NUMBER;
                this.branch = "CABINETS";
                this.environment = "Production";
            }
            case "62869b5c1351de037ffd2dab" -> {
                this.field = TrelloCustomFields.REMAN_NUMBER;
                this.branch = "CABINETS";
                this.environment = "Production";
            }
            case "62f3ac5b4eb96040bdd01827" -> {
                this.field = TrelloCustomFields.NUMBER_OF_BUILDS;
                this.branch = "CABINETS";
                this.environment = "Production";
            }
            case "63af037df4d6d906575988e9" -> {
                this.field = TrelloCustomFields.PARTIAL_CHECKBOX;
                this.branch = "CABINETS";
                this.environment = "Production";
            }
            case "641b5c66e8e936598eec927f" -> {
                this.field = TrelloCustomFields.NOTES;
                this.branch = "CABINETS";
                this.environment = "Production";
            }
            case "65944fad870030dd5e8ca1f0" -> {
                this.field = TrelloCustomFields.CUSTOMER_PO_NUMBER;
                this.branch = "CABINETS";
                this.environment = "Production";
            }


            case "6596e961627ec8be307b6f00" -> {
                this.field = TrelloCustomFields.COLOR_CODE;
                this.branch = "CABINETS";
                this.environment = "Test";
            }
            case "6596e961627ec8be307b6f02" -> {
                this.field = TrelloCustomFields.AGILITY_PO_NUMBER;
                this.branch = "CABINETS";
                this.environment = "Test";
            }
            case "6596e961627ec8be307b6f04" -> {
                this.field = TrelloCustomFields.REMAN_NUMBER;
                this.branch = "CABINETS";
                this.environment = "Test";
            }
            case "6596e961627ec8be307b6f06" -> {
                this.field = TrelloCustomFields.NUMBER_OF_BUILDS;
                this.branch = "CABINETS";
                this.environment = "Test";
            }
            case "6596e961627ec8be307b6f08" -> {
                this.field = TrelloCustomFields.PARTIAL_CHECKBOX;
                this.branch = "CABINETS";
                this.environment = "Test";
            }
            case "6596e961627ec8be307b6f0a" -> {
                this.field = TrelloCustomFields.NOTES;
                this.branch = "CABINETS";
                this.environment = "Test";
            }
            case "6596e961627ec8be307b6f0c" -> {
                this.field = TrelloCustomFields.CUSTOMER_PO_NUMBER;
                this.branch = "CABINETS";
                this.environment = "Test";
            }


            case "636bc3aa5da9340015e47ce4" -> {
                this.field = TrelloCustomFields.COLOR_CODE;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "636bc3aa5da9340015e47ce6" -> {
                this.field = TrelloCustomFields.AGILITY_PO_NUMBER;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "636bc3aa5da9340015e47ce8" -> {
                this.field = TrelloCustomFields.REMAN_NUMBER;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "636bc3aa5da9340015e47cea" -> {
                this.field = TrelloCustomFields.NUMBER_OF_BUILDS;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "670586798d5d0267f3394425" -> {
                this.field = TrelloCustomFields.PARTIAL_CHECKBOX;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "670586a5eb19b789fd45d2d2" -> {
                this.field = TrelloCustomFields.NOTES;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }
            case "65dc8d870c637100ad2f3e60" -> {
                this.field = TrelloCustomFields.CUSTOMER_PO_NUMBER;
                this.branch = "COMPONENTS";
                this.environment = "Production";
            }

            case "6596ece3760cfe2637c6fac9" -> {
                this.field = TrelloCustomFields.COLOR_CODE;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "670587259c080ef5b681d46e" -> {
                this.field = TrelloCustomFields.AGILITY_PO_NUMBER;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6596ece3760cfe2637c6facf" -> {
                this.field = TrelloCustomFields.REMAN_NUMBER;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6596ece3760cfe2637c6facb" -> {
                this.field = TrelloCustomFields.NUMBER_OF_BUILDS;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "670587400e25142e5c30b419" -> {
                this.field = TrelloCustomFields.PARTIAL_CHECKBOX;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6705874c4b81613706cb7fc0" -> {
                this.field = TrelloCustomFields.NOTES;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }
            case "6596ece3760cfe2637c6facd" -> {
                this.field = TrelloCustomFields.CUSTOMER_PO_NUMBER;
                this.branch = "COMPONENTS";
                this.environment = "Test";
            }

            case "6655edac93277a7afa62d08f" -> {
                this.field = TrelloCustomFields.COLOR_CODE;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62d091" -> {
                this.field = TrelloCustomFields.AGILITY_PO_NUMBER;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62d093" -> {
                this.field = TrelloCustomFields.REMAN_NUMBER;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62d095" -> {
                this.field = TrelloCustomFields.NUMBER_OF_BUILDS;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62d097" -> {
                this.field = TrelloCustomFields.PARTIAL_CHECKBOX;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62d099" -> {
                this.field = TrelloCustomFields.NOTES;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }
            case "6655edac93277a7afa62d09b" -> {
                this.field = TrelloCustomFields.CUSTOMER_PO_NUMBER;
                this.branch = "NASHVILLE";
                this.environment = "Production";
            }

            default -> {
                this.field = TrelloCustomFields.NOTES;
                this.branch = "TOPSHOP";
                this.environment = "TEST";
            }
        }
    }

    public String getFieldID() {

        if (environment.equals("Production")) {
            if (branch.equals("TOPSHOP")) {
                this.fieldId = switch (field) {
                    case COLOR_CODE -> "6197b500bbb79658801189ce";
                    case AGILITY_PO_NUMBER -> "6197b57d371dc08c1f2a469a";
                    case REMAN_NUMBER -> "621519b6944e3c4fc091a515";
                    case NUMBER_OF_BUILDS -> "659f1842d0b2c5bf8ef3ad08";
                    case PARTIAL_CHECKBOX -> "638e63205f249b03ac9edd81";
                    case NOTES -> "63b851cf1f3eb900f95a9c39";
                    case CUSTOMER_PO_NUMBER -> "65944bbce3ba00017427cb36";
                    default -> "63b851cf1f3eb900f95a9c39";
                };
            } else if (branch.equals("CABINETS")) {
                this.fieldId = switch (field) {
                    case COLOR_CODE -> "62869b5c1351de037ffd2da7";
                    case AGILITY_PO_NUMBER -> "62869b5c1351de037ffd2da9";
                    case REMAN_NUMBER -> "62869b5c1351de037ffd2dab";
                    case NUMBER_OF_BUILDS -> "62f3ac5b4eb96040bdd01827";
                    case PARTIAL_CHECKBOX -> "63af037df4d6d906575988e9";
                    case NOTES -> "641b5c66e8e936598eec927f";
                    case CUSTOMER_PO_NUMBER -> "65944fad870030dd5e8ca1f0";
                    default -> "641b5c66e8e936598eec927f";
                };
            } else if (branch.equals("COMPONENTS")) {
                this.fieldId = switch (field) {
                    case COLOR_CODE -> "636bc3aa5da9340015e47ce4";
                    case AGILITY_PO_NUMBER -> "636bc3aa5da9340015e47ce6";
                    case REMAN_NUMBER -> "636bc3aa5da9340015e47ce8";
                    case NUMBER_OF_BUILDS -> "636bc3aa5da9340015e47cea";
                    case PARTIAL_CHECKBOX -> "670586798d5d0267f3394425";
                    case NOTES -> "670586a5eb19b789fd45d2d2";
                    case CUSTOMER_PO_NUMBER -> "65dc8d870c637100ad2f3e60";
                    default -> "636bc3a95da9340015e47b8b";
                };
            } else if (branch.equals("NASHVILLE")) {
                this.fieldId = switch (field) {
                    case COLOR_CODE -> "6655edac93277a7afa62d08f";
                    case AGILITY_PO_NUMBER -> "6655edac93277a7afa62d091";
                    case REMAN_NUMBER -> "6655edac93277a7afa62d093";
                    case NUMBER_OF_BUILDS -> "6655edac93277a7afa62d095";
                    case PARTIAL_CHECKBOX -> "63af037df4d6d906575988e9";
                    case NOTES -> "6655edac93277a7afa62d099";
                    case CUSTOMER_PO_NUMBER -> "6655edac93277a7afa62d09b";
                    default -> "6655edac93277a7afa62d099";
                };
            } else {
                // Handle other branches in Production environment
                this.fieldId = "61f2d5c461ac134ef274ae5f";
            }
        } else if (environment.equals("Test")) {
            if (branch.equals("TOPSHOP")) {
                this.fieldId = switch (field) {
                    case COLOR_CODE -> "6596e9210326360265ae35b2";
                    case AGILITY_PO_NUMBER -> "6596e9210326360265ae35b4";
                    case REMAN_NUMBER -> "6596e961627ec8be307b6f04";
                    case NUMBER_OF_BUILDS -> "67043a38e82d6167a269ba18";
                    case PARTIAL_CHECKBOX -> "6596e9210326360265ae35b8";
                    case NOTES -> "6596e9210326360265ae35ba";
                    case CUSTOMER_PO_NUMBER -> "6596e9210326360265ae35bc";
                    default -> "6596e9210326360265ae35ba";
                };
            } else if (branch.equals("COMPONENTS")) {
                this.fieldId = switch (field) {
                    case COLOR_CODE -> "6596ece3760cfe2637c6fac9";
                    case AGILITY_PO_NUMBER -> "670587259c080ef5b681d46e";
                    case REMAN_NUMBER -> "6596ece3760cfe2637c6facf";
                    case NUMBER_OF_BUILDS -> "6596ece3760cfe2637c6facb";
                    case PARTIAL_CHECKBOX -> "670587400e25142e5c30b419";
                    case NOTES -> "6705874c4b81613706cb7fc0";
                    case CUSTOMER_PO_NUMBER -> "6596ece3760cfe2637c6facd";
                    default -> "6705874c4b81613706cb7fc0";
                };
            } else if (branch.equals("CABINETS")) {
                this.fieldId = switch (field) {
                    case COLOR_CODE -> "6596e961627ec8be307b6f00";
                    case AGILITY_PO_NUMBER -> "6596e961627ec8be307b6f02";
                    case REMAN_NUMBER -> "6596e961627ec8be307b6f04";
                    case NUMBER_OF_BUILDS -> "6596e961627ec8be307b6f06";
                    case PARTIAL_CHECKBOX -> "6596e961627ec8be307b6f08";
                    case NOTES -> "6596e961627ec8be307b6f0a";
                    case CUSTOMER_PO_NUMBER -> "6596e961627ec8be307b6f0c";
                    default -> "6596e961627ec8be307b6f0a";
                };
                //For when I make a test nashville board
//            } else if (branch.equals("NASHVILLE")) {
//                this.fieldId = switch (field) {
//                    case COLOR_CODE -> "6655edac93277a7afa62d08f";
//                    case AGILITY_PO_NUMBER -> "6655edac93277a7afa62d091";
//                    case REMAN_NUMBER -> "6655edac93277a7afa62d093";
//                    case NUMBER_OF_BUILDS -> "6655edac93277a7afa62d095";
//                    case PARTIAL_CHECKBOX -> "63af037df4d6d906575988e9";
//                    case NOTES -> "6655edac93277a7afa62d099";
//                    case CUSTOMER_PO_NUMBER -> "6655edac93277a7afa62d09b";
//                    default -> "6655edac93277a7afa62d099";
//                };
            } else {
                // Handle other branches in Test environment
                this.fieldId = "61f2d5c461ac134ef274ae5f";
            }
        } else {
            // Handle other environments
            this.fieldId =  "61f2d5c461ac134ef274ae5f";
        }
        return this.fieldId;
    }
}

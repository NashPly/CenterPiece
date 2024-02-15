package com.CenterPiece.CenterPiece;

import com.CenterPiece.CenterPiece.Objects.SalesOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;

class ItemCodeHandlerTest {

    @ParameterizedTest
    @CsvSource({
            "62869b5c1351de037ffd2cc4, 6596e945627ec8be307b1e12, CABINETS, CABINETS, WILLCALL, Open-With-BOs, ''", //B/O's
            "62869b5c1351de037ffd2cc5, 6596e945627ec8be307b1e13, CABINETS, CABINETS, WILLCALL, Open-With-BOs, '12345'", //B/O's
            "62869b5c1351de037ffd2ccd, 6596e945627ec8be307b1e1a, CABINETS, CABINETS, WILLCALL, Open-With-No-BOs, ''", //NO BO's
            "62869b5c1351de037ffd2cce, 6596e945627ec8be307b1e1b, CABINETS, CABINETS, WILLCALL, Picked, ''",
            "62869b5c1351de037ffd2cd0, 6596e945627ec8be307b1e1d, CABINETS, CABINETS, WILLCALL, Staged, ''",
            "62869b5c1351de037ffd2cd4, 6596e945627ec8be307b1e20, CABINETS, CABINETS, WILLCALL, Invoiced, ''",
            "65ca3979ae84629f987e86db, 65ca39e475842efe3de328cb, CABINETS, CABINETS, WILLCALL, Canceled, ''",
            
            "62869b5c1351de037ffd2cc4, 6596e945627ec8be307b1e12, CABINETS, CABINETS, WHSE, Open-With-BOs, ''", //B/O's
            "62869b5c1351de037ffd2cc5, 6596e945627ec8be307b1e13, CABINETS, CABINETS, WHSE, Open-With-BOs, '12345'", //B/O's
            "62869b5c1351de037ffd2ccd, 6596e945627ec8be307b1e1a, CABINETS, CABINETS, WHSE, Open-With-No-BOs, ''", //NO BO's
            "62869b5c1351de037ffd2cce, 6596e945627ec8be307b1e1b, CABINETS, CABINETS, WHSE, Picked, ''",
            "62869b5c1351de037ffd2cd1, 6596e945627ec8be307b1e1e, CABINETS, CABINETS, WHSE, Staged, ''",
            "62869b5c1351de037ffd2cd4, 6596e945627ec8be307b1e20, CABINETS, CABINETS, WHSE, Invoiced, ''",
            "65ca3979ae84629f987e86db, 65ca39e475842efe3de328cb, CABINETS, CABINETS, WILLCALL, Canceled, ''",

            "60c26dfb44555566d32ae651, 6596e9210326360265ae3352, FABRICATION, TOPSHOP, WILLCALL, Open-With-BOs, ''", //B/O's
            "60c26dfb44555566d32ae651, 6596e9210326360265ae3352, FABRICATION, TOPSHOP, WILLCALL, Open-With-BOs, '12345'", //B/O's
            "6239c656ab5c356ec1568beb, 6596e9210326360265ae335e, FABRICATION, TOPSHOP, WILLCALL, Open-With-No-BOs, ''", //NO BO's
            "60c26dfb44555566d32ae64d, 6596e9210326360265ae335f, FABRICATION, TOPSHOP, WILLCALL, Picked, ''",
            "61e6d38623686777464221b9, 6596e9210326360265ae3362, FABRICATION, TOPSHOP, WILLCALL, Staged, ''",
            "61b360e35ab37c0d9037c19f, 6596e9210326360265ae3367, FABRICATION, TOPSHOP, WILLCALL, Invoiced, ''",
            "65ca39a840f98488461880e9, 65ca39d9e3e704fe2bfeb3f4, FABRICATION, TOPSHOP, WILLCALL, Canceled, ''",

            "60c26dfb44555566d32ae651, 6596e9210326360265ae3352, FABRICATION, TOPSHOP, WHSE, Open-With-BOs, ''", //B/O's
            "60c26dfb44555566d32ae651, 6596e9210326360265ae3352, FABRICATION, TOPSHOP, WHSE, Open-With-BOs, '12345'", //B/O's
            "6239c656ab5c356ec1568beb, 6596e9210326360265ae335e, FABRICATION, TOPSHOP, WHSE, Open-With-No-BOs, ''", //NO BO's
            "60c26dfb44555566d32ae64d, 6596e9210326360265ae335f, FABRICATION, TOPSHOP, WHSE, Picked, ''",
            "60c26dfb44555566d32ae64e, 6596e9210326360265ae3363, FABRICATION, TOPSHOP, WHSE, Staged, ''",
            "61b360e35ab37c0d9037c19f, 6596e9210326360265ae3367, FABRICATION, TOPSHOP, WHSE, Invoiced, ''",
            "65ca39a840f98488461880e9, 65ca39d9e3e704fe2bfeb3f4, FABRICATION, TOPSHOP, WHSE, Canceled, ''"

//            ", , FABRICATION, COMPONENTS, WILLCALL, Open-With-BOs, ''", //B/O's
//            ", , FABRICATION, COMPONENTS, WILLCALL, Open-With-BOs, '12345'", //B/O's
//            ", , FABRICATION, COMPONENTS, WILLCALL, Open-With-No-BOs, ''", //NO BO's
//            ", , FABRICATION, COMPONENTS, WILLCALL, Picked, ''",
//            ", , FABRICATION, COMPONENTS, WILLCALL, Staged, ''",
//            ", , FABRICATION, COMPONENTS, WILLCALL, Invoiced, ''",
//
//            ", , FABRICATION, COMPONENTS, WHSE, Open-With-BOs, ''", //B/O's
//            ", , FABRICATION, COMPONENTS, WHSE, Open-With-BOs, '12345'", //B/O's
//            ", , FABRICATION, COMPONENTS, WHSE, Open-With-No-BOs, ''", //NO BO's
//            ", , FABRICATION, COMPONENTS, WHSE, Picked, ''",
//            ", , FABRICATION, COMPONENTS, WHSE, Staged, ''",
//            ", , FABRICATION, COMPONENTS, WHSE, Invoiced, ''"



    })
    void testCorrectBoard(String expectedProductionBoardId, String expectedTestBoardId, String branch, String board, String saleType, String saleStatus, String linkedPoID){

        String filePath = "src/test/java/com/CenterPiece/CenterPiece/APICallResults/OrderLogic-"+ board +"-"+ saleType+"-"+ saleStatus+".json";

        System.out.println("Production - " + filePath);
        assertEquals(getListId(filePath, branch, board,"Production", linkedPoID),expectedProductionBoardId);
        System.out.print("Test       - " + filePath);
        assertEquals(getListId(filePath, branch, board,"Test", linkedPoID),expectedTestBoardId);
        System.out.println(" ");
    }

    public static String getListId(String filePath, String branch, String board, String environment, String linkedPoID){
        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(HttpClient.newBuilder().build(),
                "Fake ContextId", branch, environment);

        JSONConverter jsonConverter = new JSONConverter(filePath);
        return itemCodeHandler.orderStatusLogic(board, jsonConverter.extractSOArrayFromJson(jsonConverter.convertJsonFileToJsonObject()).getJSONObject(0), environment, linkedPoID);
    }

//    public static JSONObject convertJsonFileToJsonObject(String filePath) {
//        // Step 1: Provide the path to the JSON file
//        //String filePath = "src/test/java/com/CenterPiece/CenterPiece/APICallResults/OrderLogic-Cabinets-WHSE-Staged.json";
//
//        // Step 2: Load the JSON file and read its contents
//        JSONObject jsonContent = null;
//        try {
//            jsonContent = new JSONObject(readFile(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return jsonContent;
//    }
//
//    public static JSONArray extractSOArrayFromJson(JSONObject response){
//
//        JSONObject json = response.getJSONObject("response")
//                .getJSONObject("OrdersResponse")
//                .getJSONObject("dsOrdersResponse");
//
//        if(json.has("dtOrderResponse")){
//            return json.getJSONArray("dtOrderResponse");
//        }
//
//        return new JSONArray();
//    }
//
//    // Utility method to read file contents
//    private static String readFile(String filePath) throws IOException {
//        return new String(java.nio.file.Files.readAllBytes(new File(filePath).toPath()));
//    }
}
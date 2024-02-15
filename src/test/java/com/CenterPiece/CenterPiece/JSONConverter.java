package com.CenterPiece.CenterPiece;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class JSONConverter {

    String filePath;

    public JSONConverter(String filePath){
        this.filePath = filePath;
    }

    public JSONObject convertJsonFileToJsonObject() {
        JSONObject jsonContent = null;
        try {
            jsonContent = new JSONObject(readFile());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonContent;
    }

//    public static JSONObject convertJsonFileToJsonObject(String filePath) {
//        JSONObject jsonContent = null;
//        try {
//            jsonContent = new JSONObject(readFile(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return jsonContent;
//    }

    public JSONArray extractSOArrayFromJson(JSONObject response){

        JSONObject json = response.getJSONObject("response")
                .getJSONObject("OrdersResponse")
                .getJSONObject("dsOrdersResponse");

        if(json.has("dtOrderResponse")){
            return json.getJSONArray("dtOrderResponse");
        }

        return new JSONArray();
    }

    // Utility method to read file contents
    private String readFile() throws IOException {
        return new String(java.nio.file.Files.readAllBytes(new File(this.filePath).toPath()));
    }
}

import com.CenterPiece.CenterPiece.Core.CenterPieceSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;

    public class ItemCodeHandlerTest {

        private String salesOrderNumber = "123456";
        private String itemCode = "NS0000012345";
        private String extDesc = "Testing 123";
        private String parseDesc = "FAB - Blackstone 271-46 CONTOUR - 1 ktop;\n\nSales Order:213879";
        private String appendDesc = "FAB - Blackstone 271-46 CONTOUR - 1 ktop;";


        @Test
        void assertAgilityItemUpdateBuildsAPIRequestCorrectly(){
            JSONObject innerRequestBody = new JSONObject();
            JSONObject dsItemUpdate = new JSONObject();
            JSONObject dtItemUpdate = new JSONObject();
            JSONArray dtItemUpdateArray = new JSONArray();
            JSONObject innerDtItemUpdate = new JSONObject();


            innerDtItemUpdate.put("ExtDescription", (extDesc));
            dtItemUpdateArray.put(innerDtItemUpdate);

            dtItemUpdate.put("dtItemUpdate",dtItemUpdateArray);

            dsItemUpdate.put("dsItemUpdate", dtItemUpdate);

            innerRequestBody.put("Item", this.itemCode);
            innerRequestBody.put("ItemUpdateJSON", dsItemUpdate);

            assertEquals(new JSONObject("{\n" +
                    "        \"Item\": \"NS0000012345\",\n" +
                    "        \"ItemUpdateJSON\": {\n" +
                    "            \"dsItemUpdate\": {\n" +
                    "                \"dtItemUpdate\": [\n" +
                    "                    {\"ExtDescription\": \"Testing 123\"}\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }").toString(), innerRequestBody.toString());


        }

        @Test
        void testThatAgilityItemUpdateChangesTheDescriptionCorrectly(){
            assertEquals("213879", this.parseDesc.split("\n\nSales Order:")[1]);
        }
        @Test
        void testThatAgilityItemUpdateAppendsCorrectly(){
            assertEquals(this.parseDesc, this.appendDesc + "\n\nSales Order:213879");
        }
}

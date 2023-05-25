package CoreTest;

import com.CenterPiece.CenterPiece.Core.CenterPieceFunctions;
import com.CenterPiece.CenterPiece.Core.CenterPieceSession;
import com.CenterPiece.CenterPiece.ItemCodeHandler;
import org.json.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CenterPieceFunctionsTest {

    HttpClient client = HttpClient.newBuilder().build();
    CenterPieceSession session;

    String contextID;

    CenterPieceFunctions functions;

    ItemCodeHandler itemCodeHandler;

    public JSONArray currentSalesOrders;

    @BeforeEach
    void setup(){
        this.client = HttpClient.newBuilder().build();

        this.session = new CenterPieceSession("CABINETS", client);

        session.setContextID(session.login());

        this.functions = new CenterPieceFunctions(client, session.getContextID(), session.getBranch());

        this.itemCodeHandler = new ItemCodeHandler(client, session.getContextID(), session.getBranch());

        this.currentSalesOrders = itemCodeHandler.agilitySalesOrderListLookup();
    }

    @AfterEach
    void tearDown(){
        if(this.session.getContextID()!=null)
        this.session.logout();
    }

    @Test
    void testIfJSONSalesOrdersContainOrderID() {
        assertTrue(this.currentSalesOrders.getJSONObject(0).has("OrderID"));
    }

    @Test
    void testIfTallyIsNull() {
        List<String> emptyList = new ArrayList<>();
        assertEquals(emptyList, this.functions.tallySOsToBeCreated(0, null));
    }


    @Test
    void checkTrelloForSO() {
    }

    @Test
    void createTrelloCard() {
    }

    @Test
    void updateTrelloCards() {
    }

    @Test
    void checkTrelloCardForEmptyCustomFields() {
    }

    @Test
    void getCardCustomFieldTrello() {
    }

    @Test
    void updateCustomFieldTrello() {
    }

    @Test
    void agilityDataForTrelloGather() {
    }
}
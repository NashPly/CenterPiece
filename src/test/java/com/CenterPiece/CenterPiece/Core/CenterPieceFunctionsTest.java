package com.CenterPiece.CenterPiece.Core;

import com.CenterPiece.CenterPiece.APICalls.AgilityCalls;
import com.CenterPiece.CenterPiece.Core.CenterPieceFunctions;
import com.CenterPiece.CenterPiece.ItemCodeHandler;
import com.CenterPiece.CenterPiece.JSONConverter;
import com.CenterPiece.CenterPiece.TimeHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CenterPieceFunctionsTest {

//    @Before
//    public void setUp() {
//        JSONConverter jsonConverter = new JSONConverter("src/test/java/com/CenterPiece/CenterPiece/APICallResults/CenterPieceFunctionsJSON/UpdateTrelloCards_WithSalesOrderData.json");
//        fetchedSalesOrderDataMock = new JSONObject(); // Create an empty JSONObject
//        fetchedSalesOrderDataMock.put("dtOrderResponse", jsonConverter.convertJsonFileToJsonObject()); // Add sample data
//
//        when(timeHandlerMock.getCurrentYear()).thenReturn("2023");
//        when(timeHandlerMock.getCurrentMonth()).thenReturn("10");
//        when(timeHandlerMock.getCurrentDayOfMonth()).thenReturn("01");
//        when(timeHandlerMock.getCurrentHour()).thenReturn("12");
//        when(timeHandlerMock.getCurrentMinuteOfHour()).thenReturn("00");
//
//        when(timeHandlerMock.getSearchYear()).thenReturn("2023");
//        when(timeHandlerMock.getSearchMonth()).thenReturn("10");
//        when(timeHandlerMock.getSearchDayOfMonth()).thenReturn("01");
//        when(timeHandlerMock.getSearchHour()).thenReturn("12");
//        when(timeHandlerMock.getSearchMinuteOfHour()).thenReturn("05");
//
//        when(itemCodeHandlerMock.agilityChangedSalesOrderListLookup()).thenReturn(fetchedSalesOrderDataMock);
//    }
    @InjectMocks
    private CenterPieceFunctions centerPieceFunctions;
    @Mock
    private ItemCodeHandler itemCodeHandlerMock;
    @Mock
    private TimeHandler timeHandlerMock;
    @Mock
    private AgilityCalls agilityCallsMock;
    @Mock
    private JSONObject fetchedSalesOrderDataMock;
    @Mock
    private JSONArray salesOrderDataArrayMock;
    @Test
    public void testUpdateTrelloCards_WithSalesOrderData() {
        // Mocking behavior

//        JSONConverter jsonConverter = new JSONConverter("src/test/java/com/CenterPiece/CenterPiece/APICallResults/CenterPieceFunctionsJSON/UpdateTrelloCards_WithSalesOrderData.json");
//        fetchedSalesOrderDataMock = new JSONObject(); // Create an empty JSONObject
//        fetchedSalesOrderDataMock.put("dtOrderResponse",jsonConverter.convertJsonFileToJsonObject().getJSONObject("response")
//                .getJSONObject("OrdersResponse")
//                .getJSONObject("dsOrdersResponse")
//                .getJSONArray("dtOrderResponse")); // Add sample data

        when(timeHandlerMock.getCurrentYear()).thenReturn("2023");
        when(timeHandlerMock.getCurrentMonth()).thenReturn("10");
        when(timeHandlerMock.getCurrentDayOfMonth()).thenReturn("01");
        when(timeHandlerMock.getCurrentHour()).thenReturn("12");
        when(timeHandlerMock.getCurrentMinuteOfHour()).thenReturn("00");

        when(timeHandlerMock.getSearchYear()).thenReturn("2023");
        when(timeHandlerMock.getSearchMonth()).thenReturn("10");
        when(timeHandlerMock.getSearchDayOfMonth()).thenReturn("01");
        when(timeHandlerMock.getSearchHour()).thenReturn("12");
        when(timeHandlerMock.getSearchMinuteOfHour()).thenReturn("05");

        when(itemCodeHandlerMock.agilityChangedSalesOrderListLookup(agilityCallsMock)).thenReturn(fetchedSalesOrderDataMock);

// Mock the behavior of fetchedSalesOrderDataMock
        when(fetchedSalesOrderDataMock.has("dtOrderResponse")).thenReturn(true); // Specific mock
        when(fetchedSalesOrderDataMock.has(anyString())).thenReturn(true); // General mock

// Mock the behavior of fetchedSalesOrderDataMock.getJSONArray and salesOrderDataArrayMock
        when(fetchedSalesOrderDataMock.getJSONArray("dtOrderResponse")).thenReturn(salesOrderDataArrayMock);
        when(salesOrderDataArrayMock.length()).thenReturn(1);


        // Call the function
        centerPieceFunctions.updateTrelloCards();

        // Verify that updateTrelloCardForSalesOrder was called once
        verify(centerPieceFunctions, times(1)).updateTrelloCardForSalesOrder(any(JSONObject.class));
    }


    // Similarly, write tests for other scenarios like when fetchedSalesOrderData is null or does not contain sales order data
}

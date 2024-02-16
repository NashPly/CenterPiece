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

import java.net.http.HttpClient;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CenterPieceFunctionsTest {

    @InjectMocks
    private CenterPieceFunctions centerPieceFunctions;
    @Mock
    private HttpClient clientMock;
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
        // Set up mock behavior for timeHandlerMock
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

        String contextId = "CenterPieceSession.login(clientMock)";
        String branch = "CABINETS";

        // Mock the entire AgilityCalls class
        AgilityCalls mockAgilityCalls = mock(AgilityCalls.class);

        // Mock the behavior of agilityCalls.postAgilityAPICall()
        when(agilityCallsMock.postAgilityAPICall(anyString(), any(JSONObject.class), anyString(), anyString()))
                .thenReturn(createMockResponse());
        // Mock the behavior of agilityCallsMock to return fetchedSalesOrderDataMock
        when(itemCodeHandlerMock.agilityChangedSalesOrderListLookup(agilityCallsMock)).thenReturn(fetchedSalesOrderDataMock);

        // Mock the behavior of fetchedSalesOrderDataMock and its methods
        when(fetchedSalesOrderDataMock.has("dtOrderResponse")).thenReturn(true);
        when(fetchedSalesOrderDataMock.has(anyString())).thenReturn(true);
        when(fetchedSalesOrderDataMock.getJSONArray("dtOrderResponse")).thenReturn(salesOrderDataArrayMock);
        when(salesOrderDataArrayMock.length()).thenReturn(1);

        // Mock the behavior of postAgilityAPICall method of the AgilityCalls class to return a predefined response
        JSONObject mockResponse = new JSONObject();
        mockResponse.put("response", new JSONObject());
        when(mockAgilityCalls.postAgilityAPICall(anyString(), any(JSONObject.class), anyString(), anyString())).thenReturn(mockResponse);

        // Inject the mock AgilityCalls instance into the CenterPieceFunctions object
        centerPieceFunctions.setAgilityCalls(mockAgilityCalls);

        // Set contextId and branch
        centerPieceFunctions.setContextId(contextId);
        centerPieceFunctions.setBranch(branch);

        // Call the method to be tested
        centerPieceFunctions.updateTrelloCards();

        // Verify that updateTrelloCardForSalesOrder method is called with any JSONObject
        verify(centerPieceFunctions, times(1)).updateTrelloCardForSalesOrder(any(JSONObject.class));
    }

    private JSONObject createMockResponse() {
        JSONObject response = new JSONObject();
        JSONObject ordersResponse = new JSONObject();
        JSONObject dsOrdersResponse = new JSONObject();
        JSONArray dtOrderResponse = new JSONArray();
        JSONObject salesOrderObject = getMockSalesOrderObject();
        // Add more fields as needed

        // Add the sales order object to the dtOrderResponse array
        dtOrderResponse.put(salesOrderObject);

        // Construct the dsOrdersResponse object
        dsOrdersResponse.put("dtOrderResponse", dtOrderResponse);

        // Construct the OrdersResponse object
        ordersResponse.put("OrdersResponse", dsOrdersResponse);


        // Add the OrdersResponse object to the response
        response.put("response", ordersResponse);

        return response;
    }
    private static JSONObject getMockSalesOrderObject() {
        JSONObject salesOrderObject = new JSONObject();
        JSONArray dtOrderDetailResponse = new JSONArray();
        JSONObject orderDetailObject = new JSONObject();

        // Construct the sales order detail object
        orderDetailObject.put("BranchID", "FABRICATION");
        orderDetailObject.put("OrderID", 212558);
        // Add more fields as needed

        // Add the sales order detail object to the dtOrderDetailResponse array
        dtOrderDetailResponse.put(orderDetailObject);

        // Construct the sales order object
        salesOrderObject.put("BranchID", "FABRICATION");
        salesOrderObject.put("OrderID", 212558);
        salesOrderObject.put("dtOrderDetailResponse", dtOrderDetailResponse);
        return salesOrderObject;
    }


}

package com.CenterPiece.CenterPiece.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

public class SalesOrder {

    private JSONObject salesOrder;
    private String salesOrderNumber;
    private String branch;
    private String jobName;
    private String customerName;
    private String customerPurchaseOrderNumber;
    private String dueDate;
    private JSONArray lineItems;
    private String purchaseOrderNumber = "0";
    private String remanNumber = "0";

    private String orderStatus;
    private String saleType;
    private String orderProcessStatus;



    public SalesOrder(JSONObject salesOrder) {
        this.salesOrder = salesOrder;
        this.populateSalesOrderFields();
    }

    public SalesOrder(String salesOrderNumber, String branch, String jobName, String customerName, String customerPurchaseOrderNumber, String dueDate, JSONArray lineItems, String purchaseOrderNumber, String remanNumber, String orderStatus, String saleType, String orderProcessStatus) {
        this.salesOrderNumber = salesOrderNumber;
        this.branch = branch;
        this.jobName = jobName;
        this.customerName = customerName;
        this.customerPurchaseOrderNumber = customerPurchaseOrderNumber;
        this.dueDate = dueDate;
        this.lineItems = lineItems;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.remanNumber = remanNumber;
        this.orderStatus = orderStatus;
        this.saleType = saleType;
        this.orderProcessStatus = orderProcessStatus;
    }

    public String getSalesOrderNumber() {
        return salesOrderNumber;
    }

    public String getJobName() {
        return jobName;
    }

    public JSONArray getLineItems() {
        return lineItems;
    }

    public String getBranch() {
        return branch;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public String getRemanNumber() {
        return remanNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPurchaseOrderNumber() {
        return customerPurchaseOrderNumber;
    }

    public String getDueDate() {
        return dueDate;
    }

    public JSONObject getSalesOrder() {
        return salesOrder;
    }

    private void populateSalesOrderFields() {
        this.salesOrderNumber = this.salesOrder.getString("OrderID");
        this.jobName = this.salesOrder.getString("TransactionJob");
        this.customerName = this.salesOrder.getString("BillToName");

        //this.branch = this.salesOrder.getString("asdf");
        this.purchaseOrderNumber = this.salesOrder.getString("asdf");
        this.remanNumber = this.salesOrder.getString("asdf");
        this.customerPurchaseOrderNumber = this.salesOrder.getString("CustomerPO");
        this.dueDate = this.salesOrder.getString("ExpectedDate");
        this.orderStatus = this.salesOrder.getString("OrderStatus");
        this.saleType = this.salesOrder.getString("SaleType");
        this.orderProcessStatus = this.salesOrder.getString("OrderProcessStatus");

        if(this.salesOrder.has("dtOrderDetailResponse")){
            this.lineItems = this.salesOrder.getJSONArray("dtOrderDetailResponse");
            if(this.salesOrder.getString("LinkedTranType") !=null){

                switch(this.salesOrder.getString("LinkedTranType")){
                    case "RM"->{
                        this.remanNumber = this.salesOrder.getString("LinkedTranID");
                    }
                    case "PO"->{
                        this.purchaseOrderNumber = this.salesOrder.getString("LinkedTranID");
                    }
                }
            }
        }
    }
}

public void updateTrelloCards() {

        ItemCodeHandler itemCodeHandler = new ItemCodeHandler(this.client, this.contextID, this.branch, this.environment);

        JSONObject fetchedSalesOrderData = itemCodeHandler.agilityChangedSalesOrderListLookup();

        //TODO adapt this for Sales orders

        if(!(fetchedSalesOrderData == null) && fetchedSalesOrderData.has("dtOrderResponse")) {

            JSONArray salesOrderDataArray = fetchedSalesOrderData.getJSONArray("dtOrderResponse");

            for(int i = 0; i < salesOrderDataArray.length(); i++) {

                JSONArray trelloSearchResultArray = (checkTrelloForSO(String.valueOf(salesOrderDataArray.getJSONObject(i).getNumber("OrderID"))));

                JSONObject firstResult = trelloSearchResultArray.getJSONObject(0);

                ItemCodeHandler salesDataItemHandler = new ItemCodeHandler(this.client, this.contextID, salesOrderDataArray.getJSONObject(i).getNumber("OrderID").toString(), salesOrderDataArray.getJSONObject(i), this.branch, this.environment);

                if (!(firstResult == null) && firstResult.has("id")){
                    if (!firstResult.getString("id").equals("Empty")) {

                        JSONObject agilityItemInformation = salesDataItemHandler.itemParseProcess();

                        boolean sameBoard = agilityItemInformation.getString("boardID").equals(firstResult.getJSONObject("board").getString("id"));

                        //boolean foundBoard = !itemInformation.getString("boardID").equals("None Found");

                        if(!agilityItemInformation.getString("boardID").equals("None Found")){

                            System.out.println(agilityItemInformation.getString("idList")+"\n");

                            for(int p = 0; p < trelloSearchResultArray.length(); p++) {

                                List<String> labelIds = new ArrayList<>();
                                List<String> trelloLabelIds = new ArrayList<>(List.of(agilityItemInformation.getString("idLabel").split(",")));


                                if(trelloSearchResultArray.getJSONObject(p).has("labels") && sameBoard) {
                                    for(int x = 0; x < trelloSearchResultArray.getJSONObject(p).getJSONArray("labels").length(); x++){

                                        labelIds.add(trelloSearchResultArray.getJSONObject(p).getJSONArray("labels")
                                                .getJSONObject(x).getString("id"));
                                    }

                                    agilityItemInformation.remove("idLabel");
                                    agilityItemInformation.put("idLabel", String.join(",", compareContrastLabels(labelIds, trelloLabelIds,
                                            trelloSearchResultArray.getJSONObject(p).getString("id"))));
                                }

                                if(trelloSearchResultArray.getJSONObject(p).has("idList")){
                                        if(!(agilityItemInformation.getString("idList").equals("62869b5c1351de037ffd2cd4") ||
                                                agilityItemInformation.getString("idList").equals("61b360e35ab37c0d9037c19f")) &&
                                        sameBoard ){
                                        //TODO above checks if current board is destination board
                                        TrelloListIDs listIDs = new TrelloListIDs(trelloSearchResultArray.getJSONObject(p).getString("idList"));
                                        //TrelloListIDs listIDs = new TrelloListIDs(itemInformation.getString("idList"));

                                        if(listIDs.offLimits(this.branch) || labelIds.contains("638e5d85e978f805fbcbf36f")) {
                                            //If either of those two are true, leave it where it is
                                            agilityItemInformation.remove("idList");
                                            agilityItemInformation.put("idList", listIDs.getListID());
                                        }
//                                            if(!listIDs.offLimits(this.branch) || !labelIds.contains("638e5d85e978f805fbcbf36f")) {
//                                                itemInformation.remove("idList");
//                                                itemInformation.put("idList", resultArray.getJSONObject(p).getString("idList"));
//                                            }
                                    }
                                }

                                boolean sameList = agilityItemInformation.getString("idList").equals(firstResult.getString("idList"));

                                String parameters = agilityDataForTrelloGather(salesOrderDataArray.getJSONObject(i), agilityItemInformation, sameList);

                                System.out.println("\n--- Updated a Trello Card ---");
                                TrelloCalls trelloCalls = new TrelloCalls(client, ("cards/" + trelloSearchResultArray.getJSONObject(p).getString("id")), parameters);
                                var response = trelloCalls.putTrelloAPICall(new JSONObject());

                                checkTrelloCardForEmptyCustomFields(response.getString("id"), agilityItemInformation, salesOrderDataArray.getJSONObject(i));

                                System.out.println("\nUpdates Applied");
                            }
                        }else{
                            System.out.println("\n-- No Applicable Boards Found --");
                        }
                    }else{
                        System.out.println("\n- Trello Hasn't Updated Yet -");
                        //TODO Work out some way to create a card if there isn't one on Trello yet
                    }
                }else if (!(firstResult == null) && firstResult.has("error")) {
                    System.out.println(firstResult);
                } else{
                    System.out.println("\n- Trello Hasn't Updated Yet 2 -");

                    //TODO Work out some way to create a card if there isn't one on Trello yet
                }
            }
        }else{
            System.out.println("\n-- No Updates --");
        }
    }
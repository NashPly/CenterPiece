package com.CenterPiece.CenterPiece;

public class ShipToAddress {

    private String streetNumber;
    private String streetName;
    private String city;
    private String zipCode;
    private String state;
    private String country = "USA";

    public ShipToAddress(String streetAddress, String city, String state,String zipCode){

        var hold = streetAddress.indexOf(" ");

        this.streetNumber = streetAddress.substring(0,hold);
        this.streetName = streetAddress.substring(hold+1, streetAddress.length());
        this.city = city;
        this.zipCode = zipCode;
        this.state = getFullStateName(state);
    }

    public ShipToAddress(String streetNumber, String streetName, String city, String state, String zipCode) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.zipCode = zipCode;
        this.state = getFullStateName(state);
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    private String getFullStateName(String stateCode){
        if(stateCode.equals("TN")) {
            return "Tennessee";
        }else if(stateCode.equals("KY")){
            return "Kentucky";
        }else if(stateCode.equals("AL")){
            return "Alabama";
        }else{
            return "Invalid State Code";
        }
    }
}

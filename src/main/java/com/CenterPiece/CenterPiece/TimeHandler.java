package com.CenterPiece.CenterPiece;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.YearMonth;

public class TimeHandler {

    private int searchSplit = 2; //Mins

    private DateTime dt;
    private String currentYear;
    private String currentMonth;
    private String currentDayOfMonth;
    private String currentHour;
    private String currentMinuteOfHour;

    private String searchYear;
    private String searchMonth;
    private String searchDayOfMonth;
    private String searchHour;
    private String searchMinuteOfHour;

    public TimeHandler(){

        DateTime dtus = new DateTime();
        DateTimeZone dtZone = DateTimeZone.forID("America/Chicago");
        this.dt = dtus.withZone(dtZone);

        int searchMinHold = dt.getMinuteOfHour()-searchSplit;
        int searchHourHold = dt.getHourOfDay();
        int searchDayHold = dt.getDayOfMonth();
        int searchMonthHold = dt.getMonthOfYear();
        int searchYearHold = dt.getYear();

        if(searchMinHold<0){
            searchMinHold = dt.getMinuteOfHour()+58;
            searchHourHold--;
            if(searchHourHold<0) {
                searchHourHold += 24;
                searchDayHold--;
                if(searchDayHold<0) {
                    //searchDayHold += 31;
                    searchMonthHold--;
                    if(searchMonthHold<0) {
                        searchMonthHold += 12;
                        searchYearHold--;
                    }
                    //Adding the previous year's info
                    YearMonth yearMonthObject = YearMonth.of(searchYearHold, searchMonthHold);
                    searchDayHold += yearMonthObject.lengthOfMonth();
                }
            }
        }

        this.searchYear = addZeroIfLessThanTen(searchYearHold);
        this.searchMonth = addZeroIfLessThanTen(searchMonthHold);
        this.searchDayOfMonth = addZeroIfLessThanTen(searchDayHold);
        this.searchHour = addZeroIfLessThanTen(searchHourHold);
        this.searchMinuteOfHour = addZeroIfLessThanTen(searchMinHold);

        this.currentYear = addZeroIfLessThanTen(dt.getYear());
        this.currentMonth = addZeroIfLessThanTen(dt.getMonthOfYear());
        this.currentDayOfMonth = addZeroIfLessThanTen(dt.getDayOfMonth());
        this.currentHour = addZeroIfLessThanTen(dt.getMinuteOfHour());
        this.currentMinuteOfHour = addZeroIfLessThanTen(dt.getMinuteOfHour());
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public String getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    public String getCurrentHour() {
        return currentHour;
    }

    public String getCurrentMinuteOfHour() {
        return currentMinuteOfHour;
    }

    public String getSearchYear() {
        return searchYear;
    }

    public String getSearchMonth() {
        return searchMonth;
    }

    public String getSearchDayOfMonth() {
        return searchDayOfMonth;
    }

    public String getSearchHour() {
        return searchHour;
    }

    public String getSearchMinuteOfHour() {
        return searchMinuteOfHour;
    }

    private String addZeroIfLessThanTen(int unit){

        if(unit<10)
            return "0" + unit;
        else
            return "" + unit;

    }

}

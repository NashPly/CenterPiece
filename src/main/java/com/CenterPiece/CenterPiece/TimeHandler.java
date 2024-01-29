package com.CenterPiece.CenterPiece;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

public class TimeHandler {

    private int searchSplit = 5; //Mins

    private LocalDateTime localDateTime;
    private DateTime dt;
    private String currentYear;
    private String currentMonth;
    private String currentDayOfMonth;
    private String currentHour;
    private String currentMinuteOfHour;

    private String yesterdaysYear;
    private String yesterdaysMonth;
    private String yesterdaysDayOfMonth;
    private String yesterdaysHour;
    private String yesterdaysMinuteOfHour;
    private String searchYear;
    private String searchMonth;
    private String searchDayOfMonth;
    private String searchHour;
    private String searchMinuteOfHour;

    public TimeHandler(){
        DateTimeZone dtZone = DateTimeZone.forID("America/Chicago");
        this.localDateTime = new LocalDateTime(dtZone);

        this.setToday();
        this.setYesterday();
        this.setSearchBeginning();

    }

    private void setToday(){
        this.currentYear = String.valueOf(this.localDateTime.getYear());
        this.currentMonth = String.valueOf(this.localDateTime.getMonthOfYear());
        this.currentDayOfMonth = String.valueOf(this.localDateTime.getDayOfMonth());
        this.currentHour = String.valueOf(this.localDateTime.getHourOfDay());
        this.currentMinuteOfHour = String.valueOf(this.localDateTime.getMinuteOfHour());
    }

    private void setYesterday(){
        LocalDateTime yesterday = this.localDateTime.minusDays(1);

        this.yesterdaysYear = String.valueOf(yesterday.getYear());
        this.yesterdaysMonth = String.valueOf(yesterday.getMonthOfYear());
        this.yesterdaysDayOfMonth = String.valueOf(yesterday.getDayOfMonth());
        this.yesterdaysHour = String.valueOf(yesterday.getHourOfDay());
        this.yesterdaysMinuteOfHour = String.valueOf(yesterday.getMinuteOfHour());
    }

    private void setSearchBeginning(){

        LocalDateTime searchStart = this.localDateTime.minusMinutes(this.searchSplit);

        this.searchYear = String.valueOf(searchStart.getYear());
        this.searchMonth = String.valueOf(searchStart.getMonthOfYear());
        this.searchDayOfMonth = String.valueOf(searchStart.getDayOfMonth());
        this.searchHour = String.valueOf(searchStart.getHourOfDay());
        this.searchMinuteOfHour = String.valueOf(searchStart.getMinuteOfHour());
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

    public String getYesterdaysYear() {
        return yesterdaysYear;
    }

    public String getYesterdaysMonth() {
        return yesterdaysMonth;
    }

    public String getYesterdaysDayOfMonth() {
        return yesterdaysDayOfMonth;
    }

    public String getYesterdaysHour() {
        return yesterdaysHour;
    }

    public String getYesterdaysMinuteOfHour() {
        return yesterdaysMinuteOfHour;
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
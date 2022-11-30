package com.CenterPiece.CenterPiece;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TimeHandler {

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

        int searchMinHold = dt.getMinuteOfHour()-2;
        int minHold = dt.getMinuteOfHour()-2;
        int hourHold = dt.getHourOfDay();
        int dayHold = dt.getDayOfMonth();
        int monthHold = dt.getMonthOfYear();
        int yearHold = dt.getYear();

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
}

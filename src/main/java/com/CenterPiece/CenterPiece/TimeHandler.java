package com.CenterPiece.CenterPiece;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

public class TimeHandler {

    private static final int SEARCH_SPLIT_MINUTES = 60;
    private String currentYear, currentMonth, currentDayOfMonth, currentHour, currentMinuteOfHour;
    private String yesterdaysYear, yesterdaysMonth, yesterdaysDayOfMonth, yesterdaysHour, yesterdaysMinuteOfHour;
    private String searchYear, searchMonth, searchDayOfMonth, searchHour, searchMinuteOfHour;

    public TimeHandler() {
        DateTimeZone dtZone = DateTimeZone.forID("America/Chicago");
        LocalDateTime localDateTime = new LocalDateTime(dtZone);

        setDateTimeValues(localDateTime, this::setToday);
        setDateTimeValues(localDateTime.minusDays(1), this::setYesterday);
        setDateTimeValues(localDateTime.minusMinutes(SEARCH_SPLIT_MINUTES), this::setSearchBeginning);
    }

    private void setDateTimeValues(LocalDateTime dateTime, DateTimeSetter setter) {
        setter.setValues(dateTime.getYear(),
                dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(),
                dateTime.getHourOfDay(),
                dateTime.getMinuteOfHour());
    }

    private void setToday(Integer year, Integer month, Integer dayOfMonth, Integer hour, Integer minuteOfHour) {
        this.currentYear = addZeroIfLessThanTen(year);
        this.currentMonth = addZeroIfLessThanTen(month);
        this.currentDayOfMonth = addZeroIfLessThanTen(dayOfMonth);
        this.currentHour = addZeroIfLessThanTen(hour);
        this.currentMinuteOfHour = addZeroIfLessThanTen(minuteOfHour);
    }

    private void setYesterday(Integer year, Integer month, Integer dayOfMonth, Integer hour, Integer minuteOfHour) {
        this.yesterdaysYear = addZeroIfLessThanTen(year);
        this.yesterdaysMonth = addZeroIfLessThanTen(month);
        this.yesterdaysDayOfMonth = addZeroIfLessThanTen(dayOfMonth);
        this.yesterdaysHour = addZeroIfLessThanTen(hour);
        this.yesterdaysMinuteOfHour = addZeroIfLessThanTen(minuteOfHour);
    }

    private void setSearchBeginning(Integer year, Integer month, Integer dayOfMonth, Integer hour, Integer minuteOfHour) {
        this.searchYear = addZeroIfLessThanTen(year);
        this.searchMonth = addZeroIfLessThanTen(month);
        this.searchDayOfMonth = addZeroIfLessThanTen(dayOfMonth);
        this.searchHour = addZeroIfLessThanTen(hour);
        this.searchMinuteOfHour = addZeroIfLessThanTen(minuteOfHour);
    }

    // Custom functional interface
    private interface DateTimeSetter {
        void setValues(Integer year, Integer month, Integer dayOfMonth, Integer hour, Integer minuteOfHour);
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

    public Integer getSearchWindow(){ return SEARCH_SPLIT_MINUTES; }
    private String addZeroIfLessThanTen(int unit){

        if(unit<10)
            return "0" + unit;
        else
            return "" + unit;

    }

}
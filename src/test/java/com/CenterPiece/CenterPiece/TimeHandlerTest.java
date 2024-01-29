package com.CenterPiece.CenterPiece;

import org.junit.jupiter.api.Test;

import java.time.Year;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeHandlerTest {

    @Test
    public void testSetYesterday() {
        // Create a TimeHandler instance
        TimeHandler timeHandler = new TimeHandler();

        // Get the current date
        String currentYear = timeHandler.getCurrentYear();
        String currentMonth = timeHandler.getCurrentMonth();
        String currentDayOfMonth = timeHandler.getCurrentDayOfMonth();

        // Set the expected yesterday's date
        String expectedYesterday = calculateExpectedYesterday(currentYear, currentMonth, currentDayOfMonth);

        // Compare the actual result with the expected result
        assertEquals(expectedYesterday, (timeHandler.getYesterdaysYear() +"-"+ timeHandler.getYesterdaysMonth()+"-"+timeHandler.getYesterdaysDayOfMonth()));
    }



    // Helper method to calculate the expected date for yesterday
    private String calculateExpectedYesterday(String currentYear, String currentMonth, String currentDayOfMonth) {
        int year = Integer.parseInt(currentYear);
        int month = Integer.parseInt(currentMonth);
        int dayOfMonth = Integer.parseInt(currentDayOfMonth);

        // Adjust the date for yesterday
        if (dayOfMonth > 1) {
            dayOfMonth--;
        } else {
            if (month > 1) {
                month--;
                dayOfMonth = getDaysInMonth(year, month);
            } else {
                year--;
                month = 12;
                dayOfMonth = getDaysInMonth(year, month);
            }
        }

        return String.format("%04d-%02d-%02d", year, month, dayOfMonth);
    }

    @Test
    public void testSearchWindowBeginning() {
        // Create a TimeHandler instance
        TimeHandler timeHandler = new TimeHandler();

        // Get the current date
        String currentYear = timeHandler.getCurrentYear();
        String currentMonth = timeHandler.getCurrentMonth();
        String currentDayOfMonth = timeHandler.getCurrentDayOfMonth();
        String currentHourOfDay = timeHandler.getCurrentHour();
        String currentMinuteOfHour = timeHandler.getCurrentMinuteOfHour();

        // Set the expected yesterday's date
        String expectedYesterday = calculateExpected2HoursAgo(currentYear, currentMonth,
                currentDayOfMonth, currentHourOfDay, currentMinuteOfHour, timeHandler.getSearchWindow());

        // Compare the actual result with the expected result
        assertEquals(expectedYesterday, (timeHandler.getSearchYear() +"-"+ timeHandler.getSearchMonth()+"-"+
                timeHandler.getSearchDayOfMonth()+"T"+timeHandler.getSearchHour()+":"+
                timeHandler.getSearchMinuteOfHour()));
    }

    // Helper method to calculate the expected date for yesterday
    private String calculateExpected2HoursAgo(String currentYear, String currentMonth, String currentDayOfMonth, String currentHourOfDay, String currentMinuteOfHour, int searchWindow) {
        int year = Integer.parseInt(currentYear);
        int month = Integer.parseInt(currentMonth);
        int dayOfMonth = Integer.parseInt(currentDayOfMonth);
        int hourOfDay = Integer.parseInt(currentHourOfDay);
        int minuteOfHour = Integer.parseInt(currentMinuteOfHour);

        // Adjust the date for yesterday

        if(minuteOfHour > 1 && (searchWindow <= 60)){
            minuteOfHour -= searchWindow;
        }else{
            if(hourOfDay >1 && (searchWindow <= 2400)){
                hourOfDay -= searchWindow/60;
            }else{
                if (dayOfMonth > 1) {
                    dayOfMonth--;
                } else {
                    if (month > 1) {
                        month--;
                        dayOfMonth = getDaysInMonth(year, month);
                    } else {
                        year--;
                        month = 12;
                        dayOfMonth = getDaysInMonth(year, month);
                    }
                }
            }
        }

        return String.format("%04d-%02d-%02dT%02d:%02d", year, month, dayOfMonth, hourOfDay, minuteOfHour);
    }

    // Helper method to get the number of days in a month
    private int getDaysInMonth(int year, int month) {
        return java.time.YearMonth.of(year,month).lengthOfMonth();
    }
}

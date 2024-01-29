package com.CenterPiece.CenterPiece;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TimeHandlerTest {

    @Test
    public void testPreviousDayOfMonth() {
        // Create a TimeHandler instance
        TimeHandler timeHandler = new TimeHandler();

        // Get the expected previous day of the month
        String expectedPreviousDay = getExpectedPreviousDay(timeHandler.getCurrentDayOfMonth());


        // Compare the actual result with the expected result
        assertEquals(expectedPreviousDay, timeHandler.getPreviousDay());
    }

    // Helper method to calculate the expected previous day of the month
    private String getExpectedPreviousDay(String currentDay) {
        int currentDayInt = Integer.parseInt(currentDay);
        int expectedPreviousDayInt;

        if (currentDayInt == 1) {
            // If current day is the first day of the month, set the expected previous day to the last day of the previous month
            expectedPreviousDayInt = 31; // Assuming the maximum day of the month is 31
        } else {
            // Otherwise, subtract 1 from the current day
            expectedPreviousDayInt = currentDayInt - 1;
        }

        // Return the expected previous day as a string
        return addZeroIfLessThanTen(expectedPreviousDayInt);
    }

    // Helper method to add zero if the unit is less than 10
    private String addZeroIfLessThanTen(int unit) {
        return (unit < 10) ? "0" + unit : String.valueOf(unit);
    }

//    @Test
//    public void testPreviousYear() {
//
//        TimeHandler timeHandler = new TimeHandler();
//
//        // Get the expected previous day of the month
//        String expectedPreviousDay = getExpectedPreviousDay(timeHandler.getCurrentYear());
//
//        // Compare the actual result with the expected result
//        assertEquals(expectedPreviousDay, timeHandler.getPreviousDay());
//
//
//
//
//
//
//
//        org.joda.time.LocalDate localDate = new org.joda.time.LocalDate(2024, 1, 1);
//        org.joda.time.LocalDate yesterdayDate = localDate.minusDays(1);
//        org.joda.time.LocalDate expectedYesterdayDate = new org.joda.time.LocalDate(2023, 12, 31);
//
//        assertEquals(expectedYesterdayDate, yesterdayDate);
//    }
//
//    @Test
//    public void testPreviousMonth() {
//
//    }
//
//    @Test
//    public void testPreviousHour(){
//
//    }
}

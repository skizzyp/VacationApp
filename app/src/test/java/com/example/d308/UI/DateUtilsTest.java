package com.example.d308.UI;

import org.junit.Test;
import static org.junit.Assert.*;

public class DateUtilsTest {

    @Test
    public void testIsStartDateBeforeEndDate() {
        String startDate = "12/12/2024";
        String endDate = "12/20/2024";
        assertTrue(com.example.d308.utils.DateUtils.isStartDateBeforeEndDate(startDate, endDate));
    }

    @Test
    public void testIsStartDateBeforeEndDate_Invalid() {
        String startDate = "12/22/2024";
        String endDate = "12/20/2024";
        assertFalse(com.example.d308.utils.DateUtils.isStartDateBeforeEndDate(startDate, endDate));
    }

    @Test
    public void testIsStartDateBeforeEndDate_SameDate() {
        String startDate = "12/20/2024";
        String endDate = "12/20/2024";
        assertFalse(com.example.d308.utils.DateUtils.isStartDateBeforeEndDate(startDate, endDate));
    }
}

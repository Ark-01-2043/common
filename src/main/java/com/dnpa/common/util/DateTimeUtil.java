package com.dnpa.common.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtil {
    public static final String DATE_yyyyMMdd_PATTERN = "yyyy-MM-dd";
    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static boolean isPastDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_yyyyMMdd_PATTERN);
        LocalDate givenDate = LocalDate.parse(dateString, formatter);
        LocalDate today = LocalDate.now();
        return givenDate.isBefore(today);
    }
    public static boolean isFutureDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_yyyyMMdd_PATTERN);
        LocalDate givenDate = LocalDate.parse(dateString, formatter);
        LocalDate today = LocalDate.now();
        return givenDate.isAfter(today);
    }
    public static int compareDateYYYY_MM_DD(String date1String, String date2String){
        return date1String.compareToIgnoreCase(date2String);
    }
    public static String getCurrentTimestampString(){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf2.format(timestamp);
    }
}

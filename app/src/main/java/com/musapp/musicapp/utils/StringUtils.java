package com.musapp.musicapp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class StringUtils {
    private StringUtils(){}

    public static String CalendarToString(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH) + "//" + calendar.get(Calendar.MONTH) + "//" +
        calendar.get(Calendar.YEAR);
    }

    public static String CurrentDateAndTimeToString(){
        DateFormat simple = new SimpleDateFormat("dd MMM HH:mm",Locale.US);
        Date date = new Date(System.currentTimeMillis());
        return simple.format(date);
    }

    public static String millisecondsToDateString(long millis){
        DateFormat simple = new SimpleDateFormat("dd MMM HH:mm", Locale.US);
        Date date = new Date(millis);
        return simple.format(date);
    }

    public static String getSongUri(String fullName){
        return fullName.substring(fullName.lastIndexOf("$")+1);
    }
}

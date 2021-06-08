package com.example.mcs18440032.a1.helpers;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Helper {

    public static long convertDateToLong(String date) throws ParseException {
        if (date.length() == 10) {
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
            df2.setTimeZone(TimeZone.getDefault());
            Date d = df2.parse(date);
            return d.getTime();
        }
        return -1;
    }

    public static long convertTimeToLong(String time) throws ParseException {
        Date t = null;
        SimpleDateFormat sd = new SimpleDateFormat("hh:mm:ss");
        sd.setTimeZone(TimeZone.getDefault());
        if (time.length() == 8) {
            t = sd.parse(time);
        } else {
            t = sd.parse(time + ":00");
        }
        return t.getTime();
    }

    public static String convertDateToString(long date) {
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        df2.setTimeZone(TimeZone.getDefault());
        return df2.format(new Date(date));
    }

    public static String convertTimeToString(long time) {
        Time t = new Time(time);
        return t.toString();
    }

    public static long convertRemainderToLong(String remainder) {
        long longValue = 0;
        switch (remainder) {
            case "15m":
                longValue = 15 * 60000;
                break;
            case "30m":
                longValue = 30 * 60000;
                break;
            case "45m":
                longValue = 45 * 60000;
                break;
            case "1h":
                longValue = 60 * 60000;
                break;
            case "2h":
                longValue = 2 * 60 * 60000;
                break;
            case "4h":
                longValue = 4 * 60 * 60000;
                break;
            case "8h":
                longValue = 6 * 60 * 60000;
                break;
            default:
                break;
        }
        return longValue;
    }

    public static String convertRemainderToString(long remainder) {
        String remainderVal = "";

        switch ((int) remainder) {
            case 15 * 60000:
                remainderVal = "15m";
                break;
            case 30 * 60000:
                remainderVal = "30m";
                break;
            case 45 * 60000:
                remainderVal = "45m";
                break;
            case 60 * 60000:
                remainderVal = "1h";
                break;
            case 2 * 60 * 60000:
                remainderVal = "2h";
                break;
            case 4 * 60 * 60000:
                remainderVal = "4h";
                break;
            case 8 * 60 * 60000:
                remainderVal = "8h";
                break;
            default:
                break;
        }
        return remainderVal;
    }
}

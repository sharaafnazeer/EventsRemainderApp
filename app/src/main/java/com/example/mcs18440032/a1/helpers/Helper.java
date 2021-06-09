package com.example.mcs18440032.a1.helpers;

public class Helper {

    public static String convertRemainderToDbFormat(String remainder) {
        String value = null;
        if (remainder != null) {
            switch (remainder) {
                case "15m":
                    value = "00:15";
                    break;
                case "30m":
                    value = "00:30";
                    break;
                case "45m":
                    value = "00:45";
                    break;
                case "1h":
                    value = "01:00";
                    break;
                case "2h":
                    value = "02:00";
                    break;
                case "4h":
                    value = "04:00";
                    break;
                case "8h":
                    value = "08:00";
                    break;
                default:
                    break;
            }
        }
        return value;
    }

    public static String convertRemainderToUiFormat(String remainder) {
        String remainderVal = "";

        switch (remainder) {
            case "00:15":
                remainderVal = "15m";
                break;
            case "00:30":
                remainderVal = "30m";
                break;
            case "00:45":
                remainderVal = "45m";
                break;
            case "01:00":
                remainderVal = "1h";
                break;
            case "02:00":
                remainderVal = "2h";
                break;
            case "04:00":
                remainderVal = "4h";
                break;
            case "08:00":
                remainderVal = "8h";
                break;
            default:
                break;
        }
        return remainderVal;
    }
}

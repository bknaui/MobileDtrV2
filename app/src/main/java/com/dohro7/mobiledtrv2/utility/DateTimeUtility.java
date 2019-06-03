package com.dohro7.mobiledtrv2.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtility {

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(calendar.getTime());
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        String month = getMonthById(calendar.get(Calendar.MONTH)+1);
        int day = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);

        return month + " " + day + ", " + year;
    }

    public static String getMonthById(int id) {
        switch (id) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "DEFAULT";
        }

    }
}

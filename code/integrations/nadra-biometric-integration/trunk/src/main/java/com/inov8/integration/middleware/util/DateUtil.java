package com.inov8.integration.middleware.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String formatCurrentDate(String format) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        return dateFormatter.format(new Date());
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        return dateFormatter.format(date);
    }
}

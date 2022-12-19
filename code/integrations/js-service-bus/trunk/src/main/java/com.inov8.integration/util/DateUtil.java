package com.inov8.integration.util;


import com.inov8.integration.enums.DateFormatEnum;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class DateUtil {

    private static final AtomicLong LAST_TIME_MS = new AtomicLong();

    public static String formatCurrentDate(String format) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        return dateFormatter.format(new Date());
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormatter.format(date);
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        return dateFormatter.format(date);
    }

    public static String expiryDate(String date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String date2 = null;
        Date date1 = null;
        try {
            date1 = dateFormat.parse(date);
            String year = String.valueOf(date1.getYear() +1900);
            String month = String.valueOf(String.format("%02d", date1.getMonth() +1));
            String day = String.valueOf(String.format("%02d",date1.getDate()));
            date2 = day + month + year;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2;
    }
    public static String cnicDateFormat(String date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String date2 = null;
        Date date1 = null;
        try {
            date1 = dateFormat.parse(date);
            String year = String.valueOf(date1.getYear() +1900);
            String month = String.valueOf(String.format("%02d", date1.getMonth() +1));
            String day = String.valueOf(String.format("%02d",date1.getDate()));
            date2 = day + "-"+month +"-"+ year;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2;
    }

    public static String buildTransactionDate() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String transdatetime = format.format(now);
        return transdatetime;
    }

    public static String buildTransactionTime() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("Hmmss");
        String transdatetime = format.format(now);
        return transdatetime;
    }

    public static String buildRRN(String stan) {
        Date now = new Date(uniqueCurrentTimeMS());
        SimpleDateFormat format = new SimpleDateFormat("ddhhmm");
        String rrn = format.format(now);
        return rrn + stan;
    }

    public static long uniqueCurrentTimeMS() {
        long now = System.currentTimeMillis();
        while (true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now)
                now = lastTime + 1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now))
                return now;
        }
    }

    public static String buildQrRRN(String stan) {
        String dayOfYear;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int lr = year % 10;                                 //last digit of current year
        String last_digit_current_year = String.valueOf(lr);

        Calendar calOne = Calendar.getInstance();
        int dr = calOne.get(Calendar.DAY_OF_YEAR);
        // current day of year
        dayOfYear = String.valueOf(dr);
        dayOfYear = StringUtils.leftPad(dayOfYear, 3, "0");


        DateTime dt = new DateTime();  // current time
        int hour = dt.getHourOfDay();
        String hours = "0";
        if (String.valueOf(hour).length() < 2) {
            hours = hours.concat(String.valueOf(hour));
        } else {
            hours = String.valueOf(hour);
        }
        String julianFormat = String.valueOf(last_digit_current_year + dayOfYear + hours + stan);
        return julianFormat;
    }

    public static String formatCurrentDate(DateFormatEnum dateEnum) {
        return formatCurrentDate(dateEnum.getValue());
    }

    public static String formatDate(Date date, DateFormatEnum dateEnum) {
        return formatDate(date, dateEnum.getValue());
    }

    public static String trimToEmpty(DateTime date) {
        if (date == null)
            return "";
        else
            return date.toString();
    }

    public static void main(String args[]) {
        System.out.println(buildQrRRN("112233"));
    }

}

package com.inov8.integration.middleware.bookme.pdu;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Umar on 10/21/2016.
 */
public class TimesComparator implements Comparator<ShowTimes>{
    private DateFormat primaryFormat = new SimpleDateFormat("h:mm a");
    private DateFormat secondaryFormat = new SimpleDateFormat("H:mm");

    @Override
    public int compare(ShowTimes time1, ShowTimes time2){
        return timeInMillis(time1.getTime()) - timeInMillis(time2.getTime());
    }

    public int timeInMillis(String time){
        return timeInMillis(time, primaryFormat);
    }

    private int timeInMillis(String time, DateFormat format) {
        // you may need more advanced logic here when parsing the time if some times have am/pm and others don't.
       int response = 0;
        try{
            Date date = format.parse(time);
            response=(int)date.getTime();
        }catch(ParseException e){
            if(format != secondaryFormat){
                response =  timeInMillis(time, secondaryFormat);
            }
        }

        return response;
    }

    public static void main(String[] args){
    /*    List<String> times = Arrays.asList(new String[]{"03:00 AM", "12:30 PM", "11:15 AM", "04:00 PM"});
        Collections.sort(times, new TimesComparator());
        System.out.println(times);*/
    }

}

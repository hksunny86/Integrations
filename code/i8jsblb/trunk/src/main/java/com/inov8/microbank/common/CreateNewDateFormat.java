package com.inov8.microbank.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Inov8 on 9/28/2018.
 */
public class CreateNewDateFormat {

    public String createDate(String strDate) throws Exception
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy h:mm:ss");
        String m = strDate.substring(0,2);
        String d = strDate.substring(2,4);
        String h = strDate.substring(4,6);
        String min = strDate.substring(6,8);
        String sec = strDate.substring(8,10);
        String[] dateParts = new Date().toString().split(" ");
        String year = dateParts[dateParts.length-1];
        String val = d + "/" + m + "/" + year + " " + h + ":" + min + ":" + sec;
        Date date = dateFormat.parse(val);
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String finalDate = dateFormat.format(date);
        return finalDate;
    }

    public String getFormattedDate(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
        String dateString = format.format(date);
        return dateString;
    }

    public String formatDate(Date date)
    {
        String nowDate = null;
        String[] dateParts = date.toString().split(" ");
        String day = dateParts[2];
        String monthName = dateParts[1].toUpperCase();
        String year = dateParts[dateParts.length-1];

        nowDate = day + "-" + monthName + "-" + year;
        return nowDate;
    }

    public static void main(String[] args) throws Exception
    {
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        createNewDateFormat.createDate("1006163427");
        createNewDateFormat.getFormattedDate(new Date());
    }
}

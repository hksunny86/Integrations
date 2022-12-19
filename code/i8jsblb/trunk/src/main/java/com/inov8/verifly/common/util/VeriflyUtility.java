package com.inov8.verifly.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.inov8.verifly.common.constants.VeriflyConstants;

public class VeriflyUtility {

    /**
     *  Changed null value into empty String or return original value.
     *
     * @param str  value to validate
     * @return String
     */
    public static String toDefault(String str) {
       // return (str != null && str.trim().length() > 0 ? str : "");
       return str;
    }

    /**
     *  Changed null value into String with value 0 or return original value.
     *  <P>
     *  Required for parsing number values
     *
     * @param str  value to validate
     * @return String
     */
    public static String toDefaultNum(String str) {
//        return (str != null && str.trim().length() > 0 ? str : "0");
        return str;
    }

    /**
     * Changed date from one format to another format.
     *
     * @param date unformatted date
     * @param format format of the date
     * @return String formatted date.
     */
    public static String formatDate(Date date, String format) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        }
        return "";
    }

    /**
     * parse formatted date from String Date
     *
     * @param formattedDate unformatted date
     * @param format format of the date
     * @return  Date
     */
    public static Date parseFormattedDate(String formattedDate, String format) {
        if (formattedDate != null && format != null ) {
            DateFormat formatter = new SimpleDateFormat(format);
            try {
                return formatter.parse(formattedDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * validate that date is in mm/yy format or not
     *
     * @param dateStr date string
     * @return  boolean
     */
    public static boolean isValidDate(String dateStr) {
          if (dateStr == null ) {
              return false;
          }

          String delim = "/";
          int minYear = 01;
          int maxYear = 99;

          try {
              StringTokenizer st = new StringTokenizer(dateStr,delim);

              if(st.countTokens() == 1){
                  return false;
              }
              String month = st.nextToken();
              String year = st.nextToken();
              st = null;

              //verifying length
              if( month.length() <= 0 || month.length() > 2 ||
                     year.length() != 2 ){
                  return false;
              }

              //Date verifying limits
              int monthNum = Integer.parseInt(month);
              int yearNum = Integer.parseInt(year);

              if(monthNum > 12 || monthNum < 1 ||
                     yearNum < minYear || yearNum > maxYear ){
                  return false;
              }

              return true;
          } catch (Exception e) {
              e = null;
              return false;
          }
      }


     /**
      * Check date with current  System date
      * <P>
      * Be sure date must be in correct format before using this method
      *
      * @return  d 	String 		return converted date
      */
     public static boolean isBeforeDateFromCurrentDate(String userDateStr){
         if(userDateStr == null){
             return true;
         }

         Date currDate = new Date();
         Date userDate = parseFormattedDate(userDateStr, VeriflyConstants.DATE_FORMAT_MM_YY);

         String currDateStr = formatDate(currDate, VeriflyConstants.DATE_FORMAT_MM_YY);

         if(currDateStr.equals(userDateStr)){
             return false;
         }else
         return currDate.after(userDate);
     }


    /**
     * This method return current date of system after converting it into the DMY format
     *
     * @return  d 	String 		return converted date
     */
    public static String getCurrentDate() {
        return formatDate(new Date(), VeriflyConstants.DATE_FORMAT_MM_YY);
    }

    public static boolean isNumber(String s) {
            for (int j = 0;j < s.length();j++) {
                    if (!Character.isDigit(s.charAt(j))) {
                            return false;
                    }
            }
            return true;
   }

}

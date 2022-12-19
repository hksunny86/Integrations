/**
 * 
 */
package com.inov8.microbank.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.util.DateUtils;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jan 11, 2007
 * Creation Time: 			11:30:54 AM
 * Description:				
 */
public class PortalDateUtils extends DateUtils
{
	
	
	public static final String LONG_DATE_FORMAT = "EEE, MMM dd, yyyy";
	public static final String SHORT_DATE_FORMAT_NEW = "dd-MON-yy";
	public static final String SHORT_DATE_FORMAT = "dd/MM/yy";
	public static final String SHORT_DATE_FORMAT_2 = "yyyy-MM-dd";
	public static final String FORMAT_DAY_MONTH_YEAR_COMPLETE = "dd/MM/yyyy";
	public static final String SHORT_TIME_FORMAT = "hh:mm a";
														
	public static final String SHORT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm aa";
	public static final String SHORT_DATE_TIME_FORMAT2 = "dd/MM/yyyy HH:mm:ss";
	public static final String SHORT_DATE_TIME_FORMAT3 = "dd/MM/yyyy hh:mm a";
	public static final String FULL_DATE_TIME_FORMAT_JASONSTD = "EEE, dd MMM yyyy HH:mm:ss zzz";
	
	

	public static final String CSV_DATE_FORMAT = "ddMMyy";
	

	public static void resetTime(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
	}

	public static Date formatDateFromStringCSVFormat(String inputDate){
		DateFormat df = new SimpleDateFormat(CSV_DATE_FORMAT);
		if (StringUtil.isNullOrEmpty(inputDate))
			return null;

		Date date = null;
		try {
			date = df.parse(inputDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Returns the current Date in the specified format
	 * @param format
	 * @return - the formatted date
	 */
	public static String currentFormattedDate(String format)
	{
		if(null==format || "".equals(format))
			throw new IllegalArgumentException("format must not be null or empty");
		
		return formatDate(new Date(), format);
	}

	/**
	 * Returns the given Date in the specified format
	 * @param format
	 * @return - the formatted date
	 */
	public static String formatDate(Date date, String format)
	{
		if(null==format || "".equals(format))
			throw new IllegalArgumentException("format must not be null or empty");

		if(null==date)
			throw new IllegalArgumentException("date must not be null");

		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	/**
	 * Converts the given date string to a valid date object
	 * @param dateStr
	 * @param format - The current format of the dateStr
	 * @return Date
	 * @throws ParseException
	 */
	public static Date parseStringAsDate(String dateStr, String format) throws ParseException
	{
		if(null==dateStr || "".equals(dateStr))
			throw new IllegalArgumentException("dateStr must not be null or empty");
		DateFormat df = new SimpleDateFormat(format);
		return df.parse(dateStr);
	}

	/**
	 * Converts the given date string with one format into another date string with the required format 
	 * @param dateStr
	 * @param currentFormat
	 * @param requiredFormat
	 * @return - formatted date
	 * @throws ParseException
	 */
	public static String formatDate(String dateStr, String currentFormat, String requiredFormat)
	throws ParseException
	{
		return formatDate(parseStringAsDate(dateStr, currentFormat), requiredFormat).toLowerCase();
	}

  /**
   * Add a number of years to this day. The actual 
   * number of days added depends on the starting day.
   * @param yearsToAdd  Number of years to add.
   */
	public static Date addYears(Date date, int yearsToAdd)
	{
		Day day = new Day(date);
		day.addYears(yearsToAdd);
		return day.getDate();
	}
	
  /**
   * Subtract a number of years to this day. The actual 
   * number of days subtracted depends on the starting day.
   * @param nYears  Number of years to subtract.
   */
	public static Date subtractYears(Date date, int yearsToAdd)
	{
		return addYears(date, -yearsToAdd);
	}
	
	
	public static Date subtractDays(Date date, int daysToSubtract)
	{
		date.setTime(date.getTime() - (daysToSubtract*24*60*60*1000));
		return date;
	}
	
	/**
	 * Given date is formatted using default pattern {@link #DATE_DEFAULT_FORMAT} as dd/MM/yyyy
	 *
	 * @param date
	 * @return Formated Date as String
	 */
	public static String formatDateDefault(String date) {
		if(date == null)
			return "";

		DateFormat df = new SimpleDateFormat(SHORT_DATE_FORMAT);
		return df.format(date);
	}

	/**
	 * Given date is formatted using default pattern {@link #DATE_DEFAULT_FORMAT} as dd/MM/yyyy
	 *
	 * @param date
	 * @return Formated Date as String
	 */
	public static String formatDateDefault(Date date) {
		if(date == null)
			return "";

		DateFormat df = new SimpleDateFormat(SHORT_DATE_FORMAT);
		return df.format(date);
	}


	/**
	 * Given time is formatted using default pattern {@link #SHORT_TIME_FORMAT} as hh:mm a
	 *
	 * @param date
	 * @return Formated Time as String
	 */
	public static String formatTimeDefault(Date date) {
		if(date == null)
			return "";

		DateFormat df = new SimpleDateFormat(SHORT_TIME_FORMAT);
		return df.format(date);
	}

	public static String formatDateTimeDefault(Date date) {
		if(date == null)
			return "";

		DateFormat df = new SimpleDateFormat(SHORT_DATE_TIME_FORMAT);
		return df.format(date);
	}
	
	public static void main(String []args)
	{
		
		Date nowDate = new Date();
		System.out.println(subtractDays(nowDate, 1));
		System.out.println( currentFormattedDate(PortalDateUtils.SHORT_DATE_FORMAT) );
		
		/*
		System.out.println("AddYears: "+ addYears(new Date(), 1));
		
		
		System.out.println();
		
		System.out.println("FORMATED DATE : "+formatDate(new Date(), "dd-MM-yyyy"));
		*/
		/*
		System.out.println(currentFormattedDate("hh:mm:ss a"));
		System.out.println(currentFormattedDate("yyyy-MM-dd"));
		System.out.println(currentFormattedDate("yyyy-MM-dd HH:ss"));
		System.out.println(currentFormattedDate("EEE, MMM dd, yyyy"));
		try
		{
			System.out.println(formatDate("16:00","hh:mm","hh:mm a"));
		}
		catch(Exception ex)
		{
		}

		*/
		
		//		try
//		{
//			System.out.println(parseStringAsDate("2007-01-11 11:47", "yyyy-MM-dd HH:ss"));
//			System.out.println(formatDate("2007-01-11", "yyyy-MM-dd", "dd/MM/yyyy"));
//			System.out.println(formatDate(null, "yyyy-MM-dd HH:ss", "yyyy-MM-dd"));
//			
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	public static String getServerDate(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		String currentDate=dateFormat.format(date);				
		return currentDate;
	}

	public static Date formateDateFromString(String strDate) {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getBillingMonth(String date) {
		if (StringUtil.isNullOrEmpty(date))
			return "";

		try {
			Date billingMonthDate = new SimpleDateFormat("yyMM").parse(date);
			LocalDate lc = new LocalDate(billingMonthDate);
			DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");

			return formatter.print(lc);
		}

		catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static Date getDateWithoutTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getDateWithoutMiliSecond(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getTomorrowDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	public static Long subtractDates(Date todayDate , Date pastDate){
		long diff = Math.abs(todayDate.getTime() - pastDate.getTime());
		long diffDays = diff / (24 * 60 * 60 * 1000);
		return diffDays;
	}
}

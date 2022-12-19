package com.inov8.microbank.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter
{
	
	public static String formatDouble(Double param){
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		String retVal=null;
		if(param!=null){
			retVal=decimalFormat.format(param);
		}
		return retVal;
	}

	public static String formatOneDouble(Double param){
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		String retVal=null;
		if(param!=null){
			retVal=decimalFormat.format(param);
		}
		return retVal;
	}
	
	public static String formatDoubleByPattern(Double param, String pattern){

		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(param);	
	}	
	
	
	public static String formatNumbers(Double value)
	{
		String formatedNumber = null;
		if(value != null)
		{
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(true);
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			formatedNumber = nf.format(value);
		}
		else
		{
			formatedNumber = "0.00";
		}
		return formatedNumber;
	}
	
	public static String formatDate(Date date)
	{
		String formatedDate = null;
		if(date != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
			formatedDate = sdf.format(date);
			return formatedDate;
		}
		return formatedDate;
	}
	
	public static String formatTime(Date date)
	{
		String formatedTime = null;
		if(date != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
			formatedTime = sdf.format(date);
			formatedTime = formatedTime.toLowerCase();
		}
		return formatedTime;
	}
	
	public static String getCurrentDate()
	{
		Date date = new Date();
		String formatedDate = null;
		if(date != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy");
			formatedDate = sdf.format(date);
			return formatedDate;
		}
		return formatedDate;
	}
	
	public static long convertToSecond(Date date)
	{
		long milliSecond = date.getTime();
		long second = milliSecond/1000;
		return second;
	}
	
	public static String replaceNullWithEmpty(String value)
	{
		String fieldValue;
		if(value != null)
		{
			fieldValue = value;
			fieldValue = fieldValue.trim();
		}
		else
		{
			fieldValue = "";
		}
		return fieldValue;
	}
	

	
		
}

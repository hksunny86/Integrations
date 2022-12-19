package com.inov8.hsm.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="")
public class DateTools {

	// cannot create, the class has static methods only
	private DateTools() {
	}

	
	public static String dateToString(Date date, String dateFormat) {
		if(date == null)
			return null;

		String formatedDate = null;
		try{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			formatedDate = simpleDateFormat.format(date);
		}catch(IllegalArgumentException ex){
			throw new IllegalArgumentException("Date format is not correct: " + formatedDate);
		}
		return formatedDate;
	}
	
	public static Timestamp currentTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static String currentDateToString(String dateFormat) {
		return dateToString(new Date(), dateFormat);
	}
	
	
	public static Date stringToDate(String dateString, String dateFormat) throws ParseException {
		if(StringUtils.isEmpty(dateString))
			return null;

		Date parsedDate = null;
		try{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			parsedDate = simpleDateFormat.parse(dateString);
		}catch(ParseException ex){
			throw new ParseException("Input is not valid date string: " + dateString, 0);
		}catch(IllegalArgumentException ex){
			throw new IllegalArgumentException("Date format is not correct: " + dateFormat);
		}
		return parsedDate;
	}


	public static void main(String[] args) {
		System.out.println(DateTools.dateToString(new Date(), "yyyyMMdd"));
	}
}
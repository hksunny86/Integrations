/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;

public class FieldUtil {
	private static Logger logger = LoggerFactory.getLogger(FieldUtil.class.getSimpleName());
	public static String valueAtIndex(String[] stringArray, int index) {

		if (stringArray != null && stringArray.length > index) {
			return stringArray[index];
		}

		return null;
	}

	public static String buildMessageRecieveTime() {
		Date now = new Date(uniqueCurrentTimeMS());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String transdatetime = format.format(now);
		return transdatetime;
	}
	
	public static String buildRRN(String stan) {
		Date now = new Date(uniqueCurrentTimeMS());
		SimpleDateFormat format = new SimpleDateFormat("ddhhmm");
		String rrn = format.format(now);
		return rrn + stan;
	}
	
	public static String buildPreRRN() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmss");
		String transdatetime = format.format(now);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			logger.error("Exception",e);
		}
		return transdatetime;
	}

	public static String buildRRNTime() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MMddH");
		String formattedString = format.format(now);
		return formattedString;
	}

	public synchronized static String buildAuthId() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("hhmmss");
		String transdatetime = format.format(now);
		return transdatetime;
	}

	public static String buildTransactionDate() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String transdatetime = format.format(now);
		return transdatetime;
	}

	public static String buildTransactionDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String transdatetime = format.format(date);
		return transdatetime;
	}

	public static String getDateYYMMdd() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		String transdatetime = format.format(now);
		return transdatetime;
	}

	public static String buildTransactionTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("Hmmss");
		String transdatetime = format.format(date);
		return transdatetime;
	}

	public static String buildTransactionTime() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("Hmmss");
		String transdatetime = format.format(now);
		return transdatetime;
	}

	public static String getSpecificField(int fromIndex, int toIndex,
			String receivedString) {
		String specificField = "";
		try {
			if (receivedString.length() >= toIndex) {
				specificField = receivedString.substring(fromIndex, toIndex);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return specificField;
	}

	public static String getDateYYYYMMDD() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String transdatetime = format.format(now);
		return transdatetime;
	}

	public static void main(String[] args) throws InterruptedException {
//		System.out.println(buildRRN("012345"));
		getMinitDifference("2017-08-11 20:11:12.715000");
	}

	public static void appendMessageSizeByte(StringBuilder messageBuilder) {
//		Integer messageSize = messageBuilder.length();
//		String paddedMessageSize = StringUtils.leftPad(Integer.toHexString(messageSize)+ "", 4, '0'); 
//		messageBuilder.insert(0, "00D8");
	}

	private static final AtomicLong LAST_TIME_MS = new AtomicLong();

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

	public static int getDateDifference(String prevFingerVarifiedDate){

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();

		Date d1 = null;
		int diffDays =0;
		try {

			d1 = dateFormat.parse(prevFingerVarifiedDate);

			//in milliseconds
			long diff = date.getTime() - d1.getTime();

			diffDays =(int) diff / (24 * 60 * 60 * 1000);

			logger.info( "Difference in days = "+diffDays);

		} catch (Exception e) {
			logger.error("General exception Ocurred in getting difference in Date ",e);
			return -1;
		}
		return diffDays;
	}
	public static long getMinitDifference(String prevFingerVarifiedDate){

		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		Date date = new Date();


		Date d1 = null;
		Date d2= null;
		long diffDays =0;
		try {

			d1 = dateFormat.parse(prevFingerVarifiedDate);
			d2=dateFormat.parse(dateFormat.format(date));

			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			diffDays = diff / (60 * 1000) % 60;

			logger.info( "Difference in days = "+diffDays);

		} catch (Exception e) {
			logger.error("General exception Ocurred in getting difference in Date ",e);
			return -1;
		}
		return diffDays;
	}
	public static String isCardExpired(String cardExpiry){

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();

		Date cardExp_Date = null;
		int diffDays =0;
		try {

			cardExp_Date = dateFormat.parse(cardExpiry);

			if (cardExp_Date.compareTo(currentDate) < 0)
				return "Yes";
			else
				return "No";
		} catch (Exception e) {
			logger.error("General exception Ocurred in comparison of card expiry with current date",e);
			return null;
		}
	}

	private static final Pattern pattern = Pattern.compile("^[0-9a-fA-F]+$");

	/**
	 * Validate hex with regular expression
	 * @param hex hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public static boolean isHex(final String hex){

		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();

	}

	public static String toHexString(byte[] array) {
		return DatatypeConverter.printHexBinary(array);
	}

	public static byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}
}

/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	public static String buildRRN() {
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
		Calendar date = new GregorianCalendar(2014, 5, 4);
		System.out.println(date.getTime());
		System.out.println(buildMessageRecieveTime());
//		StringBuilder sb = new StringBuilder("paksitan paksitan");
//		appendMessageSizeByte(sb);
//		System.out.println(sb.toString()); 
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
}

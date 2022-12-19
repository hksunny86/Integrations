/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldUtil {
	public static String buildTransmissionDateTime() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String transdatetime = format.format(now);
		if (transdatetime.length() == 13)
			transdatetime = transdatetime + "0";
		return transdatetime;
	}

	public static String buildRRN() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HHmmssSSSSSS");
		String transdatetime = format.format(now);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return transdatetime;
	}

	public static String buildRRNTime() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MMddhh");
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

	public static String getSpecificField(int fromIndex, int toIndex, String receivedString) {
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

	public static void main(String[] args) {
		System.out.println(buildRRNTime());
//		System.out.println(buildTransmissionDateTime());
//		System.out.println(buildTransmissionDateTime().length());
	}
}

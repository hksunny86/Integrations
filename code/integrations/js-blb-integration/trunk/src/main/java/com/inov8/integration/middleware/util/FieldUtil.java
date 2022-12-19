/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FieldUtil {
	public static String buildTransmissionDateTime() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHmmss");
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
	
	public static String buildRRN(String stan) {
		Date now = new Date(uniqueCurrentTimeMS());
		SimpleDateFormat format = new SimpleDateFormat("ddhhmm");
		String rrn = format.format(now);
		return rrn + stan;
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
	
	
	public static String chompChars(StringBuilder input, int size){
		String result = input.substring(0, size);
		input = input.replace(0, size, "");
		return result;
	}

	public static void main(String[] args) {
		
//		StringBuilder in = new StringBuilder("00123ABC1999NNN00000000");
//
//		System.out.println(chompChars(in, 2));
//		System.out.println(chompChars(in, 3));
//		System.out.println(chompChars(in, 3));
//		System.out.println(chompChars(in, 4));
//		System.out.println(chompChars(in, 3));
//		System.out.println(chompChars(in, 8));

		Random rand = new Random();

		int  n = rand.nextInt(1000000) + 1;
		System.out.println(n);
	}
}

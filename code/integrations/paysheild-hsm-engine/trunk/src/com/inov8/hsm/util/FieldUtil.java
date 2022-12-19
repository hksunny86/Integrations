/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.hsm.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@VersionInfo(lastModified = "24/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="")
public class FieldUtil {
	private static Logger logger = LoggerFactory.getLogger(FieldUtil.class.getSimpleName());

	private static int HEADER_SIZE = Integer.parseInt(ConfigReader.getInstance().getProperty("header.size", "24", false));
	
	public static String extractString(StringBuilder stringInput, int length) {
		String chunck = "";
		Matcher matcher = Pattern.compile(".{0," + length + "}").matcher(stringInput.toString());
		while (matcher.find()) {
			chunck = stringInput.substring(matcher.start(), matcher.end());
			// Remove from input string
			stringInput.replace(0, chunck.length(), "");
			return chunck;
		}
		return chunck;
	}
	
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

	public static String buildUPID() {
		Date now = new Date(uniqueCurrentTimeMS());
		if(HEADER_SIZE == 16){
			SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmssSSS");
			String transdatetime = format.format(now);
			return StringUtils.leftPad(transdatetime, 16, '0');
		}else{
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS");
			String transdatetime = format.format(now);
			return "HSMFBZA" + StringUtils.leftPad(transdatetime, 16, '0');
		}
	
	}

	public static String buildPreRRN() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmss");
		String transdatetime = format.format(now);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			logger.error("Exception", e);
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

	public static void appendMessageSizeByte(StringBuilder messageBuilder) {
		// Integer messageSize = messageBuilder.length();
		// String paddedMessageSize =
		// StringUtils.leftPad(Integer.toHexString(messageSize)+ "", 4, '0');
		// messageBuilder.insert(0, "00D8");
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

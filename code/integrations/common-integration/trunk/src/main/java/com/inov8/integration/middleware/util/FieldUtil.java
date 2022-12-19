/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.util;

import com.inov8.integration.middleware.util.VersionInfo.Priority;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import static com.inov8.integration.middleware.enums.MessageTypeEnum.MT_0200;
import static com.inov8.integration.middleware.enums.MessageTypeEnum.MT_0210;
import static com.inov8.integration.middleware.enums.TransactionCodeEnum.*;

//@formatter:off
@VersionInfo(
		createdBy = "Zeeshan Ahmad", 
		lastModified = "13-11-2014", 
		priority = Priority.HIGH,
		tags = { "Meezan", "AMBIT", "Field Util Class" }, 
		version = "1.0", 
		releaseVersion = "2.4", 
		patchVersion = "2.4.6", 
		notes = "Field Util Class")
// @formatter: on
public class FieldUtil {
	
	
	private static boolean production = Boolean.valueOf(ConfigReader.getInstance().getProperty("production", "true"));

	public static String valueAtIndex(String[] stringArray, int index) {

		if (stringArray != null && stringArray.length > index) {
			return stringArray[index];
		}

		return null;
	}

	public static String buildTransmissionDateTime() {
		Date now = new Date(uniqueCurrentTimeMS());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String transdatetime = format.format(now);
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
	public static String chompChars(StringBuilder input, int size){
		String result = input.substring(0, size);
		input = input.replace(0, size, "");
		return result;
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

	public static void main(String[] args) throws InterruptedException {

		System.out.println(production);
		
	}

	public static void appendMessageSizeByte(StringBuilder messageBuilder) {
		Integer messageSize = messageBuilder.length();
		// String hexMessageSize = Integer.toHexString(messageSize);
		String paddedMessageSize = StringUtils.leftPad(messageSize.toString(), 4, '0');
		messageBuilder.insert(0, paddedMessageSize);
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
	
	public static String maskMessage(String pdu) {

		if (StringUtils.isNotEmpty(pdu) && production) {
			String messageContents[] = pdu.split("\\|", -1);

			// Mask PAN
			if (messageContents.length > 1 && messageContents[0] != null) {
				if (messageContents[0].endsWith(MT_0200.getValue())) {
					return maskRequestPDUString(pdu);
				} else if (messageContents[0].endsWith(MT_0210.getValue())) {
					return maskResponsePDUString(pdu);
				}
			}
		}

		return pdu;
	}

	private static String maskRequestPDUString(String pdu) {
		if (StringUtils.isNotEmpty(pdu)) {
			String messageContents[] = pdu.split("\\|", -1);

			// Mask PAN
			if (messageContents.length >= 4) {
				messageContents[3] = "****************";
			}
			
			String messageType = valueAtIndex(messageContents, 1);
			
			// Not Password Authentication or Debit Card List
			if (messageType != null && 
					(!messageType.equalsIgnoreCase(AMBIT_USER_PASSWORD_AUTHENTICATION.getValue()) 
							|| !messageType.equalsIgnoreCase(AMBIT_DEBIT_CARD_LIST.getValue()))) {
				// Mask PIN
				if (messageContents.length >= 5) {
					messageContents[4] = "****";
				}
			}
			

			// Not Get Beneficiary List Request
			if (messageType != null && !messageType.equalsIgnoreCase(AMBIT_GET_BENEFICIARY_LIST.getValue())) {
				// Mask Password
				if (messageContents.length >= 7) {
					messageContents[6] = "********";
				}
			}

			return StringUtils.join(messageContents, "|");
		}

		return "";
	}

	private static String maskResponsePDUString(String pdu) {
		if (StringUtils.isNotEmpty(pdu)) {
			String messageContents[] = pdu.split("\\|", -1);

			// Mask PAN
			if (messageContents.length >= 4) {
				messageContents[3] = "****************";
			}

			// User Authentication Response
			if (messageContents.length > 2 && messageContents[1] != null
					&& messageContents[1].equalsIgnoreCase(AMBIT_USER_PASSWORD_AUTHENTICATION.getValue())) {
				// Mask Password
				if (messageContents.length >= 10) {
					messageContents[9] = "****************";
				}
			}

			return StringUtils.join(messageContents, "|");
		}

		return "";
	}
}

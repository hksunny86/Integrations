/**
 * 
 */
package com.inov8.integration.middleware.util;

import org.apache.commons.lang.StringUtils;

public class CommonUtils {

	public static String getFirstLengthHexByte(int length) {
		String firstByte = Integer.toHexString(length / 256);
		if (firstByte.length() == 1) {
			firstByte = "0" + firstByte;
		} else if (firstByte.length() < 1) {
			firstByte = "00";
		}
		return firstByte;
	}

	public static String get2ndLengthHexByte(int length) {
		String secondByte = Integer.toHexString(length % 256);
		if (secondByte.length() == 1) {
			secondByte = "0" + secondByte;
		} else if (secondByte.length() < 1) {
			secondByte = "00";
		}
		return secondByte;
	}

	public static byte[] hexStringToByteArray(String hex) {

		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bts;
	}

	public static String byteArrayToHexString(byte in[]) {
		byte ch = 0x00;
		int i = 0;
		if (in == null || in.length <= 0)
			return null;
		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		StringBuffer out = new StringBuffer(in.length * 2);
		while (i < in.length) {
			ch = (byte) (in[i] & 0xF0); // Strip off high nibble
			ch = (byte) (ch >>> 4); // shift the bits down
			ch = (byte) (ch & 0x0F);// must do this is high order bit is on!
			out.append(pseudo[(int) ch]); // convert thenibble to a String
											// Character
			ch = (byte) (in[i] & 0x0F); // Strip off low nibble
			out.append(pseudo[(int) ch]); // convert the nibble to a String
											// Character
			i++;
		}
		String rslt = new String(out);
		return rslt;
	}
	
	public static String bytesToBinary(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (byte b : bytes) {
			buffer.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
		}
		return buffer.toString();
	}

	public static String addPadding(String padCharacter, boolean leftPad, int requiredLength, String value) {
		String input = StringUtils.defaultString(value).trim();
		if (leftPad)
			return StringUtils.leftPad(input, requiredLength, padCharacter);
		else
			return StringUtils.rightPad(input, requiredLength, padCharacter);
	}

	public static Double decimalRoundTwoDecimals(Double value) {
		Double roundedValue = new Double(0.0);
		if (value != null) {
			roundedValue = Double.valueOf(String.format("%.2f",value));
		}
		return roundedValue;
	}
}
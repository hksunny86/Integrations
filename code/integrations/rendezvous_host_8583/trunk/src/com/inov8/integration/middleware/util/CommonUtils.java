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
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
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

}
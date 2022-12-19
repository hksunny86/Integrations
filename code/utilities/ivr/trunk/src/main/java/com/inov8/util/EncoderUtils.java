package com.inov8.util;

import java.security.MessageDigest;
import org.bouncycastle.util.encoders.Base64;

public class EncoderUtils
{
	public static String encodeBase64(String stringToEncode)
	{
		if ((stringToEncode != null) && (!"".equals(stringToEncode)))
		{
			byte[] data = stringToEncode.getBytes();
			return new String(encodeBase64(data));
		}
		throw new IllegalArgumentException("String must not be null or empty");
	}

	public static String decodeBase64(String stringToDecode)
	{
		if ((stringToDecode != null) && (!"".equals(stringToDecode)))
		{
			byte[] data = stringToDecode.getBytes();
			return new String(decodeBase64(data));
		}
		throw new IllegalArgumentException("String must not be null or empty");
	}

	public static byte[] encodeBase64(byte[] data)
	{
		return Base64.encode(data);
	}

	public static byte[] decodeBase64(byte[] data)
	{
		return Base64.decode(data);
	}

	public static String encodeToSha(String stringToEncode)
	{
		byte[] unencodedData = stringToEncode.getBytes();

		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("SHA");
		} catch (Exception e)
		{
			return stringToEncode;
		}
		md.reset();

		md.update(unencodedData);

		byte[] encodedData = md.digest();

		StringBuffer buf = new StringBuffer();
		for (byte anEncodedPassword : encodedData)
		{
			if ((anEncodedPassword & 0xFF) < 16)
			{
				buf.append("0");
			}
			buf.append(Long.toString(anEncodedPassword & 0xFF, 16));
		}
		return buf.toString();
	}
}

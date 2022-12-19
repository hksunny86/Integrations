/**
 * 
 */
package com.inov8.export.common;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jul 5, 2007
 * Creation Time: 			12:58:34 PM
 * Description:				
 */
public class HexEncodingUtils
{
	/**
	 * Hexadecimal characters corresponding to each half byte value.
	 */
	private static final char[]	HexChars	= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f'						};

	/**
	 * Converts a long integer to an unsigned hexadecimal String. Treats
	 * the integer as an unsigned 64 bit value and left-pads with the
	 * pad character of the caller's choice.
	 *
	 * @param value The long integer to convert to a hexadecimal string.
	 * @param len The total padded length of the string. If the number
	 * is larger than the padded length, then this length
	 * of the string will be the length of the number.
	 * @param pad The character to use for padding.
	 * @return Unsigned hexadecimal numeric string representing
	 * the specified value.
	 */
	public static final String toHex(long value, int len, char pad)
	{
		StringBuffer sb = new StringBuffer(Long.toHexString(value));
		int npad = len - sb.length();
		while (npad-- > 0)
			sb.insert(0, pad);
		return new String(sb);
	}

	/**
	 * Converts an arbitrary array of bytes to ASCII hexadecimal string
	 * form, with two hex characters corresponding to each byte. The
	 * length of the resultant string in characters will be twice the
	 * length of the specified array of bytes.
	 *
	 * @param bytes The array of bytes to convert to ASCII hex form.
	 * @return An ASCII hexadecimal numeric string representing the
	 * specified array of bytes.
	 */
	public static final String toHex(byte[] bytes)
	{
		StringBuffer sb = new StringBuffer();
		int i;
		for (i = 0; i < bytes.length; i++)
		{
			sb.append(HexChars[(bytes[i] >> 4) & 0xf]);
			sb.append(HexChars[bytes[i] & 0xf]);
		}
		return new String(sb);
	}

	public static byte[] fromHex(String string)
	{
		byte[] bytes = new byte[string.length() / 2];
		String buf = string.toLowerCase();

		for (int i = 0; i < buf.length(); i += 2)
		{
			char left = buf.charAt(i);
			char right = buf.charAt(i + 1);
			int index = i / 2;

			if (left < 'a')
			{
				bytes[index] = (byte) ((left - '0') << 4);
			}
			else
			{
				bytes[index] = (byte) ((left - 'a' + 10) << 4);
			}
			if (right < 'a')
			{
				bytes[index] += (byte) (right - '0');
			}
			else
			{
				bytes[index] += (byte) (right - 'a' + 10);
			}
		}

		return bytes;
	}

	public static byte[] fromHex(byte[] array)
	{
		byte[] bytes = new byte[array.length / 2];

		fromHex(array, 0, array.length, bytes, 0);

		return bytes;
	}

	private static int fromHex(byte[] in, int inOff, int length, byte[] out, int outOff)
	{
		int halfLength = length / 2;
		byte left, right;
		for (int i = 0; i < halfLength; i++)
		{
			left = in[inOff + i * 2];
			right = in[inOff + i * 2 + 1];

			if (left < (byte) 'a')
			{
				out[outOff] = (byte) ((left - '0') << 4);
			}
			else
			{
				out[outOff] = (byte) ((left - 'a' + 10) << 4);
			}
			if (right < (byte) 'a')
			{
				out[outOff] += (byte) (right - '0');
			}
			else
			{
				out[outOff] += (byte) (right - 'a' + 10);
			}

			outOff++;
		}

		return halfLength;
	}

}

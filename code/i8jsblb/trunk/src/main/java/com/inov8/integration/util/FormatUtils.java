package com.inov8.integration.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

public class FormatUtils {

	/**
	 * <p>
	 * This method take a phoenix currency value which assumes to have always
	 * two decimal point value with every currency amount. For example, phoenix
	 * 100 means 1.00, 10055 means 100.55 and so on.
	 * </p>
	 * 
	 * @param String
	 *            - incoming value from Phoenix
	 * @return String - A formatted currency value in #0.00 pattern.
	 */
	public static String parsePhoenixAmount(String value) {

		try {
			// The check only for when response from mock.
			if (value != null && value.contains(".")) {
				value = parseMicrobankAmount(value);
			}
			if (value != null && value.length() > 2) {
				int length = value.length();
				int decimalPosition = length - 2;
				StringBuilder builder = new StringBuilder(value);
				builder.insert(decimalPosition, ".").toString();
				Double d = Double.parseDouble(builder.toString());
				// value = d.toString();
				DecimalFormat Currency = new DecimalFormat("#0.00");
				String howmuch = Currency.format(d);
				value = howmuch;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String parsePhoenixBigDecimalAmount(String value) {

		try {
			// The check only for when response from mock.
			if (value != null && value.contains(".")) {
				DecimalFormat Currency = new DecimalFormat("#0.00");
				String howmuch = Currency.format(value);
				value = howmuch;
				return value;
			}
			if (value != null && value.length() > 2) {
				int length = value.length();
				int decimalPosition = length;
				StringBuilder builder = new StringBuilder(value);
				builder.insert(decimalPosition, ".").toString();
				BigDecimal d = new BigDecimal(builder.toString());
				// value = d.toString();
				DecimalFormat Currency = new DecimalFormat("#0.00");
				String howmuch = Currency.format(d);
				value = howmuch;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * <p>
	 * An utility method which translate currency related amount from
	 * i8Microbank to Phoenix supported protocol currency. Phoenix require two
	 * decimal points for every currency amount without decimal point. For
	 * example, 200 would be 20000, 200.08 would be 20008 and so on.
	 * </p>
	 * 
	 * @param String
	 *            - incoming amount value from i8Microbank
	 * @return String - Formatted String amount according to Phoenix protocol
	 *         supported.
	 */
	public static String parseMicrobankAmount(String value) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			if (value != null && value.length() > 1) {
				// CHECK IF AMOUNT HAS ALREADY DECIMAL POINT
				if (value.contains(".")) {
					// SPILIT AMOUNT INTO TWO PARTS, ONE BEFORE DECIMAL & 2ND
					// AFTER DECIMAL
					String args[] = StringUtils.split(value, ".");
					stringBuilder.append(args[0]);

					// CHECK IF ONE DECIMAL PONT VALUE LIKE 200.5
					if (args[1].length() == 1) {
						stringBuilder.append(args[1]);
						stringBuilder.append("0");

						// CHECK IF ALREADY TWO DECIMAL PONT VALUE LIKE 200.50
					} else if (args[1].length() == 2) {
						stringBuilder.append(args[1]);
					}

					// IF THERE IS NO DECIMAL POINT THEN APPEND TWO 00 TO THE
					// VALUE
				} else {
					stringBuilder.append(value);
					stringBuilder.append("00");
				}
			} else {
				throw new RuntimeException(
						"Invalid Transaction Amount Provided. Please verify");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	public static String formatAmount(BigDecimal amount) {
		String howmuch = null;
		amount.setScale(2, RoundingMode.CEILING);
		DecimalFormat Currency = new DecimalFormat("#0.00");
		howmuch = Currency.format(amount);
		return howmuch;
	}

	// TEMP TEST METHOD.
	public static void main(String[] args) {

		// System.out.println(parseMicrobankAmount("567"));
		// System.out.println(parseMicrobankAmount("567.00"));
		// System.out.println(parseMicrobankAmount("200.00"));
		// System.out.println(parseMicrobankAmount("200.6"));
		// System.out.println(parseMicrobankAmount("200.65"));

		System.out.println(formatAmount(new BigDecimal("0")));
	}
}

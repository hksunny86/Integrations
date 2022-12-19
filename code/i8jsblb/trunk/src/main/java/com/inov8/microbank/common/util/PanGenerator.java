package com.inov8.microbank.common.util;

	import java.util.List;
	import java.util.Stack;
	import java.util.Vector;

	public class PanGenerator {

		private static final int PAN_LENGTH = 12;
		public static final String[] CARD_PREFIX_LIST = new String[] {"4"};

		static String strrev(String str) {
			if (str == null)
				return "";
			String revstr = "";
			for (int i = str.length() - 1; i >= 0; i--) {
				revstr += str.charAt(i);
			}

			return revstr;
		}

		/*
		 * 'prefix' is the start of the CC number as a string, any number of digits.
		 * 'length' is the length of the CC number to generate. Typically 13 or 16
		 */
		static String completed_number(String prefix, int length) {

			String ccnumber = prefix;

			// generate digits

			while (ccnumber.length() < (length - 1)) {
				ccnumber += new Double(Math.floor(Math.random() * 10)).intValue();
			}

			// reverse number and convert to int

			String reversedCCnumberString = strrev(ccnumber);

			List<Integer> reversedCCnumberList = new Vector<Integer>();
			for (int i = 0; i < reversedCCnumberString.length(); i++) {
				reversedCCnumberList.add(new Integer(String
						.valueOf(reversedCCnumberString.charAt(i))));
			}

			// calculate sum

			int sum = 0;
			int pos = 0;

			Integer[] reversedCCnumber = reversedCCnumberList
					.toArray(new Integer[reversedCCnumberList.size()]);
			while (pos < length - 1) {

				int odd = reversedCCnumber[pos] * 2;
				if (odd > 9) {
					odd -= 9;
				}

				sum += odd;

				if (pos != (length - 2)) {
					sum += reversedCCnumber[pos + 1];
				}
				pos += 2;
			}

			// calculate check digit

			int checkdigit = new Double(
					((Math.floor(sum / 10) + 1) * 10 - sum) % 10).intValue();
			ccnumber += checkdigit;

			return ccnumber;

		}

		public static String[] credit_card_number(String[] prefixList, int length,
				int howMany) {

			Stack<String> result = new Stack<String>();
			for (int i = 0; i < howMany; i++) {
				int randomArrayIndex = (int) Math.floor(Math.random()
						* prefixList.length);
				String ccnumber = prefixList[randomArrayIndex];
				result.push(completed_number(ccnumber, length));
			}

			return result.toArray(new String[result.size()]);
		}

		public static String[] generatePANs(int howMany) {
			return credit_card_number(CARD_PREFIX_LIST, PAN_LENGTH, howMany);
		}

		public static String generatePAN() {
			return credit_card_number(CARD_PREFIX_LIST, PAN_LENGTH, 1)[0];
		}

		public static boolean isValidCreditCardNumber(String creditCardNumber) {
			boolean isValid = false;

			try {
				String reversedNumber = new StringBuffer(creditCardNumber)
						.reverse().toString();
				int mod10Count = 0;
				for (int i = 0; i < reversedNumber.length(); i++) {
					int augend = Integer.parseInt(String.valueOf(reversedNumber
							.charAt(i)));
					if (((i + 1) % 2) == 0) {
						String productString = String.valueOf(augend * 2);
						augend = 0;
						for (int j = 0; j < productString.length(); j++) {
							augend += Integer.parseInt(String.valueOf(productString
									.charAt(j)));
						}
					}

					mod10Count += augend;
				}

				if ((mod10Count % 10) == 0) {
					isValid = true;
				}
			} catch (NumberFormatException e) {
			}

			return isValid;
		}

		public static void main(String[] args) {
			int howMany = 150;
			String[] creditcardnumbers = generatePANs(howMany);
			for (int i = 0; i < creditcardnumbers.length; i++) {
				System.out.println(creditcardnumbers[i]
						+ ":"
						+ (isValidCreditCardNumber(creditcardnumbers[i]) ? "valid"
								: "invalid"));
			}
		}
	}

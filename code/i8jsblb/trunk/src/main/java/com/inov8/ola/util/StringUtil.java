package com.inov8.ola.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

public class StringUtil
{
	private static String str_msg_command_a = "<msg id=\"";
	private static String str_msg_command_b = "\">";
    public static final String WALK_IN_CUSTOMER_CNIC_SUFFIX = "-W"; 

	/**
	 * @author imran.sarwar
	 * Extracts the command from the given str. e.g given the string <msg id="123".... will return 123
	 * @param str
	 * @return the command id in the message string
	 */
	public static String extractCommand(String str)
	{
		String commandId =StringUtils.substringBetween(str, str_msg_command_a, str_msg_command_b);
		if(commandId != null && !commandId.equals(""))
		{
			commandId = commandId.trim();
		}
		return commandId;
		
		//return StringUtils.substringBetween(str, str_msg_command_a, str_msg_command_b);
	}
	
	/**
	 * @author imran.sarwar
	 * 	Removes control characters (char <= 32) from both ends of this String returning an empty String ("") if the String is empty ("") after the trim or if it is null.
	 *  The String is trimmed using String.trim(). Trim removes start and end characters <= 32. To strip whitespace use stripToEmpty(String).
	 * 	StringUtils.trimToEmpty(null)          = ""
	 * 	StringUtils.trimToEmpty("")            = ""
	 * 	StringUtils.trimToEmpty("     ")       = ""
	 * 	StringUtils.trimToEmpty("abc")         = "abc"
	 * 	StringUtils.trimToEmpty("    abc    ") = "abc"
	 * 
	 * @param str - the String to be trimmed, may be null
	 * @return the trimmed String, or an empty String if null input
	 * 
	*/
	public static String trimToEmpty(String str)
	{
		return StringUtils.trimToEmpty(str);
	}
	
	public static String replaceSpacesWithPlus(String orginalString)
	{
		return orginalString.replaceAll(" ", "+");
	}
	
		
	public static boolean isNumeric(String value) 
	{
		String numerics = "0123456789.";
		return doValidate(numerics, value);
	}

	public static boolean isInteger(String value)
	{
		String numerics = "0123456789";
		return doValidate(numerics, value);
	}

	/**
	 * @author imran.sarwar
	 * Checks if a String is whitespace, empty ("") or null.
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
 	 * StringUtils.isBlank(" ")       = true
 	 * StringUtils.isBlank("bob")     = false
 	 * StringUtils.isBlank("  bob  ") = false
	 * 
	 * @param str - the String to check, may be null
	 * @return true if the String is null, empty or whitespace
	 */
	public static boolean isNullOrEmpty(String str)
	{
		return StringUtils.isBlank(str);
	}
	
	private static boolean doValidate(String numerics, String value) 
	{
		value = value.trim();
		char c;
		for(int i = 0; i < value.length(); i++)
		{
			c = value.charAt(i);
			String.valueOf(c);
					
			if(numerics.indexOf(String.valueOf(c)) == -1)
				return false;
		}
		return true;
	}
	
	private static String replaceNumericWithAsterick(String pin)
	{
		if(pin != null && !pin.equals(""))
		{
			pin = pin.trim();
			for(int i = 0; i < pin.trim().length(); i++)
			{
				pin = pin.replace(pin.charAt(i), '*');
			}
		}
		else
		{
			System.out.println("Pin not found");
		}
		return pin;
	}

	
	public static String replaceString(String pin,int numberOfChar,String replaceChar)
	{
		return StringUtils.leftPad(StringUtils.right(pin, numberOfChar), pin.length(), replaceChar);
	}

	
	public static String leftPadWithChar(String str, String padChar)
	{
		return StringUtils.leftPad(str, str.length()+1, padChar);
	}
	
	
	
	/**
	 * @author imran.sarwar
	 *
	 *  	Removes a substring only if it is at the begining of a source string, otherwise returns the source string.
	 *       A null source string will return null. An empty ("") source string will return the empty string. A null search string will return the source string.
	 *       StringUtils.removeStart(null, *)      = null
	 *       StringUtils.removeStart("", *)        = ""
	 *       StringUtils.removeStart(*, null)      = *
	 *       StringUtils.removeStart("www.domain.com", "www.")   = "domain.com"
	 *       StringUtils.removeStart("domain.com", "www.")       = "domain.com"
	 *       StringUtils.removeStart("www.domain.com", "domain") = "www.domain.com"
	 *       StringUtils.removeStart("abc", "")    = "abc"
	 *  @param   str - the source String to search, may be null
	 *  @param remove - the String to search for and remove, may be null
	 * @return the substring with the string removed if found, null if null String input
	 */
	public static String removeStringFromStart(String source, String strToBeRemoved)
	{
		return StringUtils.removeStart(source, strToBeRemoved);
	}
	
	
	public static String wrapString(String str, int wrapLength, String newLineStr, boolean wrapLongWords)
	{
		return WordUtils.wrap(str, wrapLength, newLineStr, wrapLongWords);
	}
	
	public static String leftPadWithZero(String value, int requiredLength)
	{
		if(value != null && value.length() > 0)
		{
			if(value.length() != requiredLength)
			{
				value = StringUtils.leftPad(value, requiredLength, '0');
			}
		}
		return value;
	}
	

	public static boolean isWalkinCustomerCNIC(String cnic){
		boolean isWalkinCnic = false;
		if(cnic != null && ! cnic.equals("")){
			isWalkinCnic = cnic.indexOf(WALK_IN_CUSTOMER_CNIC_SUFFIX) != -1;
		}

		return isWalkinCnic;
	}

}

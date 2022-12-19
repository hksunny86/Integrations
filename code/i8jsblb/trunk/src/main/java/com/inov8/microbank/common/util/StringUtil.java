package com.inov8.microbank.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

public class StringUtil
{
	private static String str_msg_command_a = "<msg id=\"";
	private static String str_msg_command_b = "\">";
	private static Set<String> accountMasksSet = new HashSet<String>();
    
    static{
    	populateAccountMaskSet();
    }
    
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
	
	public static String replacePinWithAsterics(String xml)
	{
		if(xml != null && !xml.equals(""))
		{
			String pin,newPin,cPin,cvv;
			if(xml.indexOf(CommandFieldConstants.KEY_PIN) != -1)
			{
				pin = StringUtils.substringBetween(xml.substring(xml.indexOf(CommandFieldConstants.KEY_PIN)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN);
				pin = replaceNumericWithAsterick(pin);
				xml = StringUtils.replace(xml, StringUtils.substringBetween(xml.substring(xml.indexOf(CommandFieldConstants.KEY_PIN)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN), pin);
			}
			if(xml.indexOf(CommandFieldConstants.KEY_NEW_PIN) != -1)
			{
				newPin = StringUtils.substringBetween(xml.substring(xml.indexOf(CommandFieldConstants.KEY_NEW_PIN)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN);
				newPin = replaceNumericWithAsterick(newPin);
				xml = StringUtils.replace(xml, StringUtils.substringBetween(xml.substring(xml.indexOf(CommandFieldConstants.KEY_NEW_PIN)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN), newPin);
			}
			if(xml.indexOf(CommandFieldConstants.KEY_CONF_PIN) != -1)
			{
				cPin = StringUtils.substringBetween(xml.substring(xml.indexOf(CommandFieldConstants.KEY_CONF_PIN)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN);
				cPin = replaceNumericWithAsterick(cPin);
				xml = StringUtils.replace(xml, StringUtils.substringBetween(xml.substring(xml.indexOf(CommandFieldConstants.KEY_CONF_PIN)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN), cPin);
			}
			if(xml.indexOf(CommandFieldConstants.KEY_CVV) != -1)
			{
				cvv = StringUtils.substringBetween(xml.substring(xml.indexOf(CommandFieldConstants.KEY_CVV)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN);
				cvv = replaceNumericWithAsterick(cvv);
				xml = StringUtils.replace(xml, StringUtils.substringBetween(xml.substring(xml.indexOf(CommandFieldConstants.KEY_CVV)),XMLConstants.TAG_SYMBOL_CLOSE ,XMLConstants.TAG_SYMBOL_OPEN), cvv);
			}
			
			System.out.println("Replace String : "+xml);
		}
		return xml;
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
	public static String trimNameForNotification(String name,int maxLengthAfterTrim){
		String retVal=null;
		retVal=name;
		if(name!=null && !"".equals(name)){
			
			if(retVal.contains(" ")){
				retVal=retVal.split(" ")[0];
			}
			if(retVal.length() > maxLengthAfterTrim){
				retVal=retVal.substring(0,maxLengthAfterTrim);
			}
		}
		return retVal;
		
		
	} 

	/**
	 * @param fileNameWithExtension The fileNameWithExtension whose name is to be trimmed
	 * @return File name after trimming/excluding the extension
	 * @throws NullPointerException if fileNameWithExtension is null
	 * @throws IllegalArgumentException if fileNameWithExtension does not contain extension
	 */
	public static String trimExtension( String fileNameWithExtension ) throws NullPointerException, IllegalArgumentException
    {
        if( fileNameWithExtension == null || fileNameWithExtension.trim().isEmpty() )
        {
            throw new NullPointerException( );
        }

        int idxOfDot = fileNameWithExtension.indexOf( '.' );
        if( idxOfDot == -1 )
        {
            throw new IllegalArgumentException( "File Name does not contain extension." );
        }
        return fileNameWithExtension.substring( 1, fileNameWithExtension.indexOf( '.' ) );
    }

	public static String getCommaSeparatedStringFromList(List<String> collectionOfStrings){
		StringBuilder result = new StringBuilder();
		
		if(collectionOfStrings == null){
			return result.toString();
		}
		
	    for(String string : collectionOfStrings) {
	    	result.append("'");
	    	result.append(string);
	    	result.append("'");
	    	result.append(",");
	    }
	    return result.length() > 0 ? result.substring(0, result.length() - 1): "";
	}
	
	public static String getCommaSeparatedStringFromLongList(List<Long> collectionOfLongs){
		StringBuilder result = new StringBuilder();
		
		if(collectionOfLongs == null){
			return result.toString();
		}
		
	    for(Long longVal : collectionOfLongs) {
	    	result.append("'");
	    	result.append(longVal);
	    	result.append("'");
	    	result.append(",");
	    }
	    return result.length() > 0 ? result.substring(0, result.length() - 1): "";
	}
	
	public static String buildRRNPrefix() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String transdatetime = format.format(now);
		return transdatetime;
	}
	
	public static boolean isFailureReasonId(String msg){
		boolean result = false;
		if(!StringUtils.isBlank(msg)){
			msg = msg.trim();
			if(StringUtils.isNumeric(msg) && msg.length() > 3 && msg.length() < 11){
				
				result = true;
			}
		}
		return result;
	}

    private static void populateAccountMaskSet(){
    	accountMasksSet.add("-RSO");
    }
    public static String unMaskAccountNo(String accountNo){
        String mask = null;
    	if ((mask = getAccountNoMask(accountNo)) != null) {
			accountNo = accountNo.substring(0,accountNo.indexOf(mask));
		}
    	return accountNo;
    }
    
    private static String getAccountNoMask(String accountNo){
    	String result = null;
    	for(String mask : accountMasksSet){
    		if(accountNo.indexOf(mask) > 1){
    			result = mask;
    		}
    	}
    	return result;
    }
	
    public static String getLastFiveDigitsFromAccountNo(String accountNo){
//    	NVL(SUBSTR(:NEW.BANK_ACCOUNT_NO, LENGTH(:NEW.BANK_ACCOUNT_NO)-4, 5),'')
    	if(accountNo == null){
    		return accountNo;
    	}
    	
    	
    	accountNo = StringUtil.replaceString(accountNo, 5, null); 
    	
    	accountNo = accountNo.trim();
    	return accountNo;
    	
    }
    
    public static String trimToFitInDB(String input, int targetLength){
    	
    	if(StringUtil.isNullOrEmpty(input)){
    		return input;
    	}else if(input.length() <= targetLength){
    		return input;
    	}else{
    		return input.substring(0, targetLength);	
    	}
    }
	
	public static void main(String[] args)
	{
//		double columnCount = 9;
//		String test = "Account Nick";
//		System.out.println(test.length());
//		System.out.println( StringUtil.wrapString(test, (int)(80/columnCount), null, true) );
//		System.out.println("Result of the Method : "+replaceString("12345678912345678912", 5, ""));
	}
}

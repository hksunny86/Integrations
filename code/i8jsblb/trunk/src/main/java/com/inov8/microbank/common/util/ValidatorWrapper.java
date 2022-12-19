package com.inov8.microbank.common.util;

import java.util.ArrayList;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.util.ValidatorUtils;

public class ValidatorWrapper 
{
	
	public static ValidationErrors doRequired(String value,ValidationErrors valErrors,String fieldName) 
	{
		if(GenericValidator.isBlankOrNull(value))
		{
			valErrors = insertSeparator(valErrors);
			valErrors.getStringBuilder().append(fieldName+" is required");
		}
		return valErrors;
	}
	
	public static ValidationErrors doNumeric(String value,ValidationErrors valErrors,String fieldName) 
	{
		String numerics = "0123456789.";
		value = value.trim();
		char c;
		for(int i = 0; i < value.length(); i++)
		{
			c = value.charAt(i);
			String.valueOf(c);
					
			if(numerics.indexOf(String.valueOf(c)) == -1)
			{
				valErrors = insertSeparator(valErrors);
				valErrors.getStringBuilder().append(fieldName+" is not Numeric");
				break;
			}
		}
		return valErrors;
	}
	
	public static ValidationErrors doPassword(String value,ValidationErrors valErrors,String fieldName) 
	{
		String numerics = "0123456789.";
		String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		boolean hasDigit = false;
		boolean hasAlpha = false;
		value = value.trim();
		char c;
		for(int i = 0; i < value.length(); i++)
		{
			c = value.charAt(i);
			String.valueOf(c);
					
			if(numerics.indexOf(String.valueOf(c)) != -1)
			{
//				valErrors = insertSeparator(valErrors);
//				valErrors.getStringBuilder().append(fieldName+" is not Numeric");
				hasDigit = true;
				break;
			}
		}
		for(int i = 0; i < value.length(); i++)
		{
			c = value.charAt(i);
			String.valueOf(c);
					
			if(alpha.indexOf(String.valueOf(c)) != -1)
			{
//				valErrors = insertSeparator(valErrors);
//				valErrors.getStringBuilder().append(fieldName+" is not Numeric");
				hasAlpha = true;
				break;
			}
		}
		if(!(hasAlpha && hasDigit))
		{
			valErrors = insertSeparator(valErrors);
			valErrors.getStringBuilder().append(fieldName+" must be a combination of alphanumeric characters.");
		}
		
		return valErrors;
	}
	
	public static ValidationErrors doInteger(String value, ValidationErrors valErrors, String fieldName)
	{
		String numerics = "0123456789";
		char c;
		value = value.trim();
		
		for(int i = 0; i < value.length(); i++)
		{
			c = value.charAt(i);
			String.valueOf(c);
					
			if(numerics.indexOf(String.valueOf(c)) == -1)
			{
				valErrors = insertSeparator(valErrors);
				valErrors.getStringBuilder().append(fieldName+" is Decimal");
				break;
			}
		}
		return valErrors;
	}
	
	public static ValidationErrors doIntegerWithoutTrim(String value, ValidationErrors valErrors, String fieldName)
	{
		String numerics = "0123456789";
		char c;
		value = value;
		
		for(int i = 0; i < value.length(); i++)
		{
			c = value.charAt(i);
			String.valueOf(c);
					
			if(numerics.indexOf(String.valueOf(c)) == -1)
			{
				valErrors = insertSeparator(valErrors);
				valErrors.getStringBuilder().append(fieldName+" is Decimal");
				break;
			}
		}
		return valErrors;
	}
		
	public static ValidationErrors doCompareStartEnd(String startNumber,String endNumber,ValidationErrors valErrors,String startingName, String endingName) 
	{
		int sNumber = Integer.parseInt(startNumber);
		int eNumber = Integer.parseInt(endNumber);
		if(sNumber > eNumber)
		{
			valErrors.getStringBuilder().append(startingName+" should be less than "+endingName);
		}
		return valErrors;
	}
	
	public static boolean doDate(Object bean, Field field) 
	{
		String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
		String datePattern = field.getVarValue("datePattern");
		return(GenericTypeValidator.formatDate(value,datePattern,false) == null) ? false : true;
	}
	
	public static ValidationErrors doRange(String min,String max, String value, String fieldName,ValidationErrors valErrors) 
	{
		try 
		{
			int valueInt = Integer.parseInt(value);
			int minInt = Integer.parseInt(min);

			int maxInt = Integer.parseInt(max);
			if(!GenericValidator.isInRange(valueInt,minInt,maxInt))
			{
				valErrors = insertSeparator(valErrors);
				valErrors.getStringBuilder().append(fieldName+" Value should be between "+min+" and "+max);
			}
			return valErrors;
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace();
			return valErrors; 
		}
	}
	
	public static ValidationErrors doEqual(String first , String second , ValidationErrors valErrors,String firstName,String secondName)
	{
		if(!first.equals(second))
		{
			valErrors = insertSeparator(valErrors);
			valErrors.getStringBuilder().append(firstName+" is not Equal to "+secondName);
		}
		return valErrors;
	}
	public static ValidationErrors matchPassword(String first , String second , ValidationErrors valErrors,String firstName,String secondName)
	{
		if(!first.equals(second))
		{
			valErrors = insertSeparator(valErrors);
			valErrors.getStringBuilder().append(firstName+" and "+secondName+" do not match.");
		}
		return valErrors;
	}
	

	public static ValidationErrors doCheckList(ArrayList list , ValidationErrors valErrors, String favoriteListName)
	{
		if(list == null || list.size() < 0)
		{
			valErrors = insertSeparator(valErrors);
			valErrors.getStringBuilder().append(favoriteListName+" is Empty");
		}
		return valErrors;
	}
		
	public static ValidationErrors checkLength(String value, int length, ValidationErrors valErrors, String valueName)
	{
		value  = value.trim();
		if(value != null)
		{
			if(value.length() != length)
			{
				valErrors = insertSeparator(valErrors);
				valErrors.getStringBuilder().append(valueName+" Length Must be Equal to "+length);
			}
		}
		return valErrors;
	}
	
	private static ValidationErrors insertSeparator(ValidationErrors valErrors)
	{
		if(valErrors.hasValidationErrors())
		{
			valErrors.getStringBuilder().append("\n");
		}
		return valErrors;
	}
	
	public static ValidationErrors doValidateMobileNo(String mobileNumber,ValidationErrors valErrors, String fieldName)
	{
		String mobileNumberNumeric = "+0123456789";
		char c;
		mobileNumber = mobileNumber.trim();
		
		if(mobileNumber != null && mobileNumber.length() > 0)
		{
			for(int i = 0; i < mobileNumber.length(); i++)
			{
				c = mobileNumber.charAt(i);
				if(mobileNumberNumeric.indexOf(String.valueOf(c)) == -1)
				{
					valErrors = insertSeparator(valErrors);
					valErrors.getStringBuilder().append(" Mobile number you have provided is not correct. Please enter a valid mobile number.");
					break;
				}
			}
		}
		return valErrors;
	}
	
	public static ValidationErrors doValidateCNIC(String cnic,ValidationErrors valErrors, String fieldName) {

		cnic = cnic.trim();
		if(!CommonUtils.isValidCnic(cnic)) {
			valErrors = insertSeparator(valErrors);
			valErrors.getStringBuilder().append(fieldName+" is not valid.Please enter valid CNIC.");
		}

		return valErrors;
	}

	public static ValidationErrors addError(ValidationErrors valErrors, String error) {
		
		if(null == error || "".equals(error) || "".equals(error.trim()))
			return valErrors;
		
		error = error.trim();
		
		valErrors = insertSeparator(valErrors);
		valErrors.getStringBuilder().append(error);
		
		return valErrors;
	}
	
	public static boolean doEmail(String value) 
	{
		return GenericValidator.isEmail(value);
	}
	
	public static boolean doURL(String value) 
	{
		return GenericValidator.isUrl(value);
	}
	
	public static boolean doRegExp(String value,String expression) 
	{
		return GenericValidator.matchRegexp(value, expression);
	}
	
	public static boolean doCreditCard(String value) 
	{
		return GenericValidator.isCreditCard(value);
	}
	
}

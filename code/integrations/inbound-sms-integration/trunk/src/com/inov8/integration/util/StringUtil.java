package com.inov8.integration.util;

public class StringUtil {
	
	public static boolean isEmpty(String string){
		boolean result = false;
		if(string == null || string.equalsIgnoreCase("")  )
			result = true;
		return result;
	}
	
}

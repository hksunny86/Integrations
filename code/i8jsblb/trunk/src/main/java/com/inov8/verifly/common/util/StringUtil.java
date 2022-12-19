package com.inov8.verifly.common.util;

public class StringUtil {
	public static byte[] toBinArray( String hexStr ) {
	    byte bArray[] = new byte[hexStr.length()/2];
	    for(int i=0; i< bArray.length; i++) {
	        bArray[i] = Byte.parseByte(hexStr.substring(2*i,2*i+2),16);
	    }
	    return bArray;
	}

}

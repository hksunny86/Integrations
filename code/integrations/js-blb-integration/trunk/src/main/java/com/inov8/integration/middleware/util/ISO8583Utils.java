package com.inov8.integration.middleware.util;

import org.apache.commons.lang.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ISO8583Utils {

	private static Logger logger = LoggerFactory.getLogger(ISO8583Utils.class.getSimpleName());
	
	private static final int HEADER_LENGTH = 35;
	
	private static GenericPackager GENERIC_PACKAGER = null;
	
	
	public static GenericPackager getISOPackager(){
		try {
			if(GENERIC_PACKAGER == null){
				GENERIC_PACKAGER = new GenericPackager(ISO8583Utils.class.getResourceAsStream("/ISO8583.xml"));
			}
			return GENERIC_PACKAGER;
		} catch (ISOException e) {
			logger.error("Exception",e);
		}
		return null;
	}
	
	public static byte[] getMessageBody(String hexString) {
		String in = new String(hexString);
		return StringUtils.substring(in, HEADER_LENGTH).getBytes();
	}
	
	public static String getMessageHeader(String hexString) {
		String in = new String(hexString);
		return in.substring(0, HEADER_LENGTH + 1);
	}
	
	
}

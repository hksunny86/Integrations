package com.inov8.export.common;

import java.io.InputStream;
import java.util.Properties;

public class MessageSource {
	
	private static Properties p;
	
	public static String getPoperties(String key){
		
		return p.getProperty(key);
	}
	
	public MessageSource() throws Exception{
		try {
			final InputStream stream =this.getClass().getResourceAsStream("/configuration.properties");
			p = new Properties();		
			p.load(stream);
		
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	}

}

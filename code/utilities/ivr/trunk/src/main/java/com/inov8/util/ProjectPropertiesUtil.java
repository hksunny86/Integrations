package com.inov8.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.inov8.Main;

public class ProjectPropertiesUtil {

	private static Properties prop = new Properties();
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void load()
	{
		InputStream is = null;
		try {
			is = new ClassPathResource("project.properties").getInputStream();
			prop.load(is);
		} catch (IOException e) {
			logger.error("", e);
		}
		if(is !=null)
		{
			try {
				is.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}
	
	public static Properties getProperties()
	{
		return prop;
	}
	

	public static String getProperty(String key) 
	{
		return prop.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue)
	{
		return prop.getProperty(key, defaultValue);
	}
}

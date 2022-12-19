package com.inov8.util;

import io.task.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class StartupUtil {

	private static final Logger logger = LoggerFactory.getLogger(StartupUtil.class);
	
	public static void startup()
	{
		ProjectPropertiesUtil.load();
//		WsServer.main(null);
		loadJavaLoggerConfFile();
	}
	
	public static void loadJavaLoggerConfFile()
	{
		Properties prop = ProjectPropertiesUtil.getProperties();
		
		String value = prop.getProperty(IvrConstant.PROP_LOGGING_PROPERTIES);

		if(StringUtil.isNullOrEmpty(value) == false) {
			InputStream is = null;
			try {
				is = new ClassPathResource(value).getInputStream();
				LogManager.getLogManager().readConfiguration(is);
			} catch (Exception e) {
				logger.error("", e);
			}
			if(is!=null)
			{
				try {
					is.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
		else
		{
			logger.info("No logging properties file set");
		}
	}
}

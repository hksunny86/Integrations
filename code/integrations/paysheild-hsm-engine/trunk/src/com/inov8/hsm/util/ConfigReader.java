package com.inov8.hsm.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra", tags = "")
public class ConfigReader {
	private static Properties properties;
	private static ConfigReader instance;
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
	private static Map<String, String> cacheMap = new HashMap<String, String>();

	private ConfigReader() {

	}

	public static ConfigReader getInstance() {
		if (instance == null)
			instance = new ConfigReader();
		return instance;
	}

	public String getProperty(final String key, final String defaultValue, boolean cache) {
		String result = null;
		if (cache) {
			if (cacheMap.containsKey(key)) {
				result = cacheMap.get(key);
			} else {
				result = getProperty(key, defaultValue);
				if (StringUtils.isNotEmpty(result)) {
					cacheMap.put(key, result);
				}
			}
		} else {
			result = getProperty(key, defaultValue);
		}
		return result;
	}

	public String getProperty(final String key, final String defaultValue) {
		InputStream inputStream = null;
		String value = null;
		try {
			// Get the inputStream
			inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("682ede816988e58fb6d057d9d85605e0");
			properties = new EncryptableProperties(encryptor);
			// load the inputStream using the Properties
			properties.load(inputStream);

			value = properties.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error("Exception", e);
			}
		}
		if (value == null) {
			return defaultValue;
		}
		return value.trim();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		System.out.println(ConfigReader.getInstance().getProperty("connection_timeout", "222"));
		long end = System.currentTimeMillis();
		System.out.println("Total Time Taken : " + (end - startTime));

		startTime = System.currentTimeMillis();
		for (int i = 0; i < 65000; i++) {
			ConfigReader.getInstance().getProperty("connection_timeout", "222");
		}
		end = System.currentTimeMillis();
		System.out.println("Total Time Taken : " + (end - startTime));
	}
}

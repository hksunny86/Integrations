package com.inov8.integration.middleware.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	private static Properties properties;
	private static ConfigReader instance;

	private ConfigReader() {

	}

	public static ConfigReader getInstance() {
		if (instance == null)
			instance = new ConfigReader();
		return instance;
	}

	public String getProperty(final String key, final String defaultValue) {
		InputStream inputStream = null;
		String value = null;
		try {
			// Get the inputStream
			inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
			properties = new Properties();
			// load the inputStream using the Properties
			properties.load(inputStream);

			value = properties.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
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

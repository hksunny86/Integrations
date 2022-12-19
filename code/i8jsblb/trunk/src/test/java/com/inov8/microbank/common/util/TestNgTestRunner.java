package com.inov8.microbank.common.util;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

/**
 * @author Shoaib Akhtar This is a utility class for the users of all those IDE
 *         which do not support TestNG.
 */
public class TestNgTestRunner
{
	public static void main(String[] args)
	{
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();

		// testng.setTestClasses(new Class[] { <<Put the name of your test here.
		// Donot Checkin>>.class });
		testng.addListener(tla);
		testng.run();
	}
}

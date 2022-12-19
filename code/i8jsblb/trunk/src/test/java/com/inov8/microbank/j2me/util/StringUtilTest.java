package com.inov8.microbank.j2me.util;

import static org.testng.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;

import com.inov8.microbank.common.util.StringUtil;

@Test(groups = { "unit", "mfsServerUnit" })
public class StringUtilTest
{

	protected final Log log = LogFactory.getLog(getClass());

	/*
	 * Test method for 'com.inov8.microbank.j2me.util.StringUtil.getCommand(String)'
	 */

	public void testExtractCommand()
	{
		String str = "<msg id=\"LOGIN\"><ses id=\"\"/><req id=\"\"/><param name=\"uId\">1235465</param></msg>";
		String res = StringUtil.extractCommand(str);
		log.info(res);
		assertEquals(res, "LOGIN");
	}

}

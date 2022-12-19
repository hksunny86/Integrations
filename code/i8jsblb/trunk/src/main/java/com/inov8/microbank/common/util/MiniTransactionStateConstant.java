package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;


public class MiniTransactionStateConstant
{
	public static final Long PIN_SENT = 1L;
	public static final Long COMPLETED = 2L;
	public static final Long EXPIRED = 3L;
	public static final Long CANCELLED = 4L;
	public static final Long UNSUCCESSFUL = 5L;
	public static final Long PENDING = 6L;
	public static final Long OT_PIN_EXPIRED = 7L;
	
	public static final String PIN_SENT_NAME = "PIN Sent";
	public static final String COMPLETED_NAME = "Completed";
	public static final String EXPIRED_NAME = "Expired";
	public static final String CANCELLED_NAME = "Cancelled";
	public static final String UNSUCCESSFUL_NAME = "UnSuccessful";
	public static final String PENDING_NAME = "Pending";
	public static final String OT_PIN_EXPIRED_NAME = "PIN Expired";
	
	public static final Map<Long, String> miniStateNamesMap = new HashMap<Long, String>();
	
	static{
		miniStateNamesMap.put(PIN_SENT, PIN_SENT_NAME);
		miniStateNamesMap.put(COMPLETED, COMPLETED_NAME);
		miniStateNamesMap.put(EXPIRED, EXPIRED_NAME);
		miniStateNamesMap.put(CANCELLED, CANCELLED_NAME);
		miniStateNamesMap.put(UNSUCCESSFUL, UNSUCCESSFUL_NAME);
		miniStateNamesMap.put(PENDING, PENDING_NAME);
		miniStateNamesMap.put(OT_PIN_EXPIRED, OT_PIN_EXPIRED_NAME);
	}
	
}

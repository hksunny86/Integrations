package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;

public class RegistrationStateConstantsInterface {
	
	public static final Long BULK_RQST_RCVD = 1L;
	public static final Long RQST_RCVD = 2L;
	public static final Long VERIFIED = 3L;
	public static final Long DISCREPANT = 4L;
	public static final Long DECLINE = 5L;
	public static final Long REJECTED = 6L;
	public static final Long BLACKLISTED=7L;
	public static final Long CLOSED = 10l;
	public static final Long DORMANT = 9l;

	
	public static final Map<Long, String> REG_STATE_MAP = new HashMap<Long, String>();
	
	static{
		REG_STATE_MAP.put(BULK_RQST_RCVD, "Bulk Request Received");
		REG_STATE_MAP.put(RQST_RCVD, "Request Received");
		REG_STATE_MAP.put(VERIFIED, "Activate");
		REG_STATE_MAP.put(DISCREPANT, "Discrepant");
		REG_STATE_MAP.put(DECLINE, "Decline");
		REG_STATE_MAP.put(REJECTED, "Rejected");
		REG_STATE_MAP.put(BLACKLISTED, "Blacklisted");
		REG_STATE_MAP.put(DORMANT, "Dormant");
	}
	

}

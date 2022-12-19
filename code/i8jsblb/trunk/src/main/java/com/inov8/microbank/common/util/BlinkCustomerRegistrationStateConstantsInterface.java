package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;

public class BlinkCustomerRegistrationStateConstantsInterface {
	

	public static final Long RQST_RCVD = 2L;
	public static final Long APPROVED = 3L;
	public static final Long DISCREPANT = 4L;
	public static final Long REJECTED = 6L;


	
	public static final Map<Long, String> REG_STATE_MAP = new HashMap<Long, String>();
	
	static{

		REG_STATE_MAP.put(RQST_RCVD, "Request Received");
		REG_STATE_MAP.put(APPROVED, "Activate");
		REG_STATE_MAP.put(DISCREPANT, "Discrepant");
		REG_STATE_MAP.put(REJECTED, "Rejected");

	}
	

}

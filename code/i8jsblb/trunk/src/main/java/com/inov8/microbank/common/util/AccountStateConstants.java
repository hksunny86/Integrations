package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;

public class AccountStateConstants {

	public static final Long ACCOUNT_STATE_HOT = 1l;
	public static final Long ACCOUNT_STATE_WARM = 2l;
	public static final Long ACCOUNT_STATE_COLD = 3l;
	public static final Long ACCOUNT_STATE_DECEASED = 4l;
	public static final Long ACCOUNT_STATE_DORMANT = 5l;
	public static final Long ACCOUNT_STATE_CLOSED = 6l;
	public static final Long CLS_STATE_BLOCKED = 7l;
	public static final Long ACCOUNT_STATE_REJECTED = 8l;

	
	public static final Map<Long, String> ACC_STATE_MAP = new HashMap<Long, String>();
	
	static{
		ACC_STATE_MAP.put(ACCOUNT_STATE_HOT, "Hot");
		ACC_STATE_MAP.put(ACCOUNT_STATE_WARM, "Warm");
		ACC_STATE_MAP.put(ACCOUNT_STATE_COLD, "Cold");
		ACC_STATE_MAP.put(ACCOUNT_STATE_DECEASED, "Deceased");
		ACC_STATE_MAP.put(ACCOUNT_STATE_DORMANT, "Dormant");
		ACC_STATE_MAP.put(ACCOUNT_STATE_CLOSED, "Closed");
		ACC_STATE_MAP.put(CLS_STATE_BLOCKED, "CLS_Blocked");
		ACC_STATE_MAP.put(ACCOUNT_STATE_REJECTED, "Rejected");

	}
}

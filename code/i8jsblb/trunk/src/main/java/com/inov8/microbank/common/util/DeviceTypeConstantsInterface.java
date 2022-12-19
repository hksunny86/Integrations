package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;

public class DeviceTypeConstantsInterface {

	public static final Long MOBILE = 5L;
	public static final Long ATM = 2L;
	public static final Long WEB = 3L;
	public static final Long ALL_PAY = 5L;
	public static final Long MFS_WEB = 6L;
	public static final Long ALLPAY_WEB = 8L;
	public static final Long USSD = 9L;
	public static final Long BULK_DISBURSEMENT = 10L;
	public static final Long SMS_GATEWAY = 12L;
	public static final Long BANKING_MIDDLEWARE = 13L;
	public static final Long WEB_SERVICE = 14L;

	public static final Map<Long, String> DEVICE_TYPES_MAP = new HashMap<Long, String>();

	static{
		DEVICE_TYPES_MAP.put(MOBILE, "Mobile App");
		DEVICE_TYPES_MAP.put(ATM, "ATM");
		DEVICE_TYPES_MAP.put(WEB, "WEB");
		DEVICE_TYPES_MAP.put(ALL_PAY, "Mobile App");
		DEVICE_TYPES_MAP.put(MFS_WEB, "Customer WEB");
		DEVICE_TYPES_MAP.put(ALLPAY_WEB, "Agent Web");
		DEVICE_TYPES_MAP.put(USSD, "USSD");
		DEVICE_TYPES_MAP.put(BULK_DISBURSEMENT, "Backend Services");
		DEVICE_TYPES_MAP.put(BANKING_MIDDLEWARE, "Banking Middleware");
		DEVICE_TYPES_MAP.put(WEB_SERVICE, "Web Service");

	}


}
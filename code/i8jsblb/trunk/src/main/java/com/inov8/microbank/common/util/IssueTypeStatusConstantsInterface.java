package com.inov8.microbank.common.util;

public interface IssueTypeStatusConstantsInterface {
	
	public static final Long CHARGEBACK_NEW = 1L;
	public static final Long CHARGEBACK_INVALID = 2L;
	public static final Long CHARGEBACK_OPEN = 8L;
	public static final Long CHARGEBACK_RIFC = 3L;
	
	public static final Long DISPUTE_NEW = 4L;
	public static final Long DISPUTE_INVALID = 5L;
	public static final Long DISPUTE_OPEN = 6L;
	public static final Long DISPUTE_RIFC = 7L;
	
	public static final Long INOV8_CHARGEBACK_RIFM = 9L;
	public static final Long INOV8_CHARGEBACK_RIFC = 11L;

	public static final Long INOV8_DISPUTE_INVALID = 10L;
	public static final Long INOV8_DISPUTE_RIFC = 12L;

	
	public static final String REQUEST_PARAMETER_NAME = "issueTypeStatusId";
	public static final String MGMT_PAGE_BTN_NAME = "btnName";
	public static final String STATUS = "status";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	
	
	

}

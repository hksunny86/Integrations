package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;

public class SupplierProcessingStatusConstants {
	
	public static final Long COMPLETED = 1l;
	public static final Long FAILED = 2l;
	public static final Long AMBIGUOUS = 3l;
	public static final Long IN_PROGRESS = 4L;
	public static final Long REVERSED = 5L;
	public static final Long UNCLAIMED = 6L;
	public static final Long BANK_SETTLEMENT_PENDING = 7L;
	public static final Long BILL_AUTHORIZATION_SENT = 8L;
	public static final Long IVR_VALIDATION_PENDING   = 9L;
	public static final Long IVR_VALIDATION_ABORTED   = 10L;
	public static final Long PENDING_ACTION_AUTH   = 11L;
	public static final Long REVERSE_COMPLETED = 12L;
	public static final Long PROCESSING = 13L;
	public static final Long REVERSAL_IN_PROCESS = 14L;

	public static final String REVERSAL_IN_PROCESS_NAME    = "Reversal In Process";
	public static final String COMPLETE_NAME    = "Complete";
	public static final String COMPLETED_NAME 	= "Completed";
	public static final String FAILED_NAME 		= "Failed";
	public static final String AMBIGUOUS_NAME 	= "Ambiguous";	
	public static final String IN_PROCESS    	= "In-process";
	public static final String REVERSED_NAME    = "Reversed";	
	public static final String UNCLAIMED_NAME   = "Unclaimed";
	public static final String BANK_SETTLEMENT_PENDING_NAME = "Bank Settlement Pending";
	public static final String IVR_VALIDATION_PENDING_NAME   = "IVR Authorization Pending";
	public static final String IVR_VALIDATION_ABORTED_NAME   = "IVR Authorization Aborted";
	public static final String PENDING_ACTION_AUTH_NAME   = "Action Authorization Pending";
	public static final String REVERSE_COMPLETED_NAME    = "Reverse Completed";	
	public static final String PROCESSING_NAME    = "Transaction Processing";

	public static final Map<Long, String> processingStatusNamesMap = new HashMap<Long, String>();
	
	static{
		processingStatusNamesMap.put(COMPLETED, COMPLETE_NAME);
		processingStatusNamesMap.put(FAILED, FAILED_NAME);
		processingStatusNamesMap.put(AMBIGUOUS, AMBIGUOUS_NAME);
		processingStatusNamesMap.put(IN_PROGRESS, IN_PROCESS);
		processingStatusNamesMap.put(REVERSED, REVERSED_NAME);
		processingStatusNamesMap.put(UNCLAIMED, UNCLAIMED_NAME);
		processingStatusNamesMap.put(BANK_SETTLEMENT_PENDING, BANK_SETTLEMENT_PENDING_NAME);
		processingStatusNamesMap.put(IVR_VALIDATION_PENDING, IVR_VALIDATION_PENDING_NAME);
		processingStatusNamesMap.put(IVR_VALIDATION_ABORTED, IVR_VALIDATION_ABORTED_NAME);
		processingStatusNamesMap.put(REVERSE_COMPLETED, REVERSE_COMPLETED_NAME);
		processingStatusNamesMap.put(PENDING_ACTION_AUTH, PENDING_ACTION_AUTH_NAME);
	}
}

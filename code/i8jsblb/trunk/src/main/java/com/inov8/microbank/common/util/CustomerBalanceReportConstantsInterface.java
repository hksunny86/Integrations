/**
 * 
 */
package com.inov8.microbank.common.util;

/**
 * @author NaseerUl
 *
 */
public interface CustomerBalanceReportConstantsInterface
{
	public static final String KEY_MAX_EXEC_MINS 					= "customerBalanceReportDecryptionScheduler.maxExecutionMins";
	public static final String KEY_CHUNK_SIZE    					= "customerBalanceReportDecryptionScheduler.chunkSize";
	public static final String KEY_IS_END_DAY_BALANCE_NULL  		= "IsEndDayBalanceNull";
	public static final String KEY_SCHEDULER_STATUS 				= "SchedulerStatus";
	public static final String KEY_SCHEDULER_STATUS_UPDATED			= "SchedulerStatusUpdated";
	public static final String KEY_COLLECTION           			= "List";
	public static final String SCHEDULER_STATUS_NEW 				= "New";
	public static final String SCHEDULER_STATUS_INPROCESS 			= "Inprocess";
	public static final String SCHEDULER_STATUS_COMPLETE 			= "Complete";
}

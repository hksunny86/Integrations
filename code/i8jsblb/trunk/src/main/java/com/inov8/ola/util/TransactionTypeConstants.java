package com.inov8.ola.util;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


public interface TransactionTypeConstants
{
	public static final Long DEBIT = 1L;
	public static final Long CREDIT = 2L;
	public static final Long COMMISSION = 3L;
	public static final Long REVERSAL_REASON = 5L;
	
	public static final Long PAYMENT = 4L;
	
	String KEY_RESPONSE_CODE = "RESPONSE_CODE";
	String KEY_BVS_VAL = "BVS_VAL";

}

package com.inov8.microbank.common.util;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 9, 2012 4:56:22 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class TransactionReversalConstants
{
    public static final String COMMAND_NAME        = "transactionReversalVo";

    public static final String IS_REVERSED         = "isReversed";
    public static final String IS_REDEEMED         = "isRedeemed";
    public static final String IS_DUPLICATE_REQUEST= "isDuplicateReq";

    public static final String TRANSACTION_CODE    = "transactionCode";

    public static final String TRANSACTION_CODE_ID = "transactionCodeId";
    
    public static final Integer REDEMPTION_TYPE_FULL = 1;
    public static final Integer REDEMPTION_TYPE_PARTIAL = 2;

}

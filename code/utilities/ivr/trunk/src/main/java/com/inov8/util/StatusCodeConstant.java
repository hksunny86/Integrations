package com.inov8.util;

public interface StatusCodeConstant {
	String CODE_7022 = "7022";// invalid pin
	String CODE_8058 = "8058";// service unavailable
	String CODE_0000 = "0000";//SUCCESS
	String CODE_9999 = "9999";//account blocked
	String CODE_8055 = "8055" ;//INACTIVE ACCOUNT
	String CODE_8056 = "8056" ;//DELETED_ACCOUNT
	String CODE_8059 = "8059" ;//INSUFFICIENT_ACC_BALANCE
	String CODE_8062 = "8062" ;//DAILY_DEBIT_LIMIT_BUSTED
	String CODE_8064 = "8064" ;//MONTHLY_DEBIT_LIMIT_BUSTED
	String CODE_8065 = "8065" ;//YEARLY_DEBIT_LIMIT_BUSTED
	String CODE_8061 = "8061" ;//DAILY_CREDIT_LIMIT_BUSTED
	String CODE_8063 = "8063" ;//MONTHLY_CREDIT_LIMIT_BUSTED
	String CODE_8066 = "8066" ;//YEARLY_CREDIT_LIMIT_BUSTED
	String CODE_8067 = "8067" ;//MAXIMUM_BALANCE_LIMIT_BUSTED
	String CODE_8077 = "8077";//insufficient bal
	String CODE_9001 = "9001";//general error
	String CODE_9019 = "9019";//INVALID customer
	String CODE_9021 = "9021";//daily tx amount limit exceeded
	String CODE_9022 = "9022";//daily tx amount limit will exceed
	String CODE_9010 = "9010";//invalid otp
	String CODE_9015 = "9015";//otp/credentials expired
}

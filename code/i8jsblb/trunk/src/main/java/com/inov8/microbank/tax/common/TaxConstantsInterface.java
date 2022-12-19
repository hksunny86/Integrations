package com.inov8.microbank.tax.common;

public interface TaxConstantsInterface {
	Long WHT_CONFIG_COMMISSION_ID  = 1l;
	Long WHT_CONFIG_WITHDRAWAL_ID  = 2l;
	Long WHT_CONFIG_TRANSFER_ID  = 3l;

	Integer DAILY_WHT_DED_SUCCESS = 1;
	Integer DAILY_WHT_DED_FAIL = 2;
	Integer DAILY_WHT_DED_INITIATED = 0;
}

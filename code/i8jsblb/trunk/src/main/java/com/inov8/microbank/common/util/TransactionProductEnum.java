package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum TransactionProductEnum {

	AGENT_IBFT_PRODUCT(10245158L),
	TRANSACTION_IN_PRODUCT(50020L),
	TRANSACTION_OUT_PRODUCT(50022L),
	CASH_DEPOSIT_PRODUCT(50002L),
	AGENT_CASH_DEPOSIT_PRODUCT(10245142L),
	CASH_WITHDRAWL_PRODUCT(50006L),
	HRA_CASH_WITHDRAWL_PRODUCT(10245106L),
	CASH_WITHDRAWL_CUSTOMER_PRODUCT(50051L),
	ACCOUNT_TO_CASH_PRODUCT(50010L),
	ACCOUNT_TO_CASH_REVERSAL_PRODUCT(-50010L),
	CASH_TO_CASH_PRODUCT(50011L),
	AGENT_TO_AGENT_PRODUCT(50013L),
	RETAIL_PAYMENT_PRODUCT(50054L),
	P2P_TRANSFER_PRODUCT(50000L),
	CNIC_TO_BB_ACCOUNT_PRODUCT(50030L),
	AGENT_RETAIL_PAYMENT_PRODUCT(50031L),
	INITIAL_DEPOSIT_PRODUCT(2510763L),
	IBFT_PRODUCT(2510802L),
	CUSTOM_PRODUCT(0L),
	TELLER_ACCOUNT_HOLDER_CASH_IN_PRODUCT(50033L),
	TELLER_WALK_IN_CASH_IN_PRODUCT(50034L),
	
	// Customer initiated Transactions
	CUST_INIT_ACCOUNT_TO_ACCOUNT_PRODUCT(50050L),
	CUST_INIT_ACCOUNT_TO_CASH_PRODUCT(50052L),
	DEBIT_CARD_CASH_WITHDRAWL_ON_US_PRODUCT(10245111L),
	DEBIT_CARD_CASH_WITHDRAWL_OFF_US_PRODUCT(10245115L),
	POS_DEBIT_CARD_CASH_WITHDRAWL_PRODUCT(10245117L),
	INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWL_OFF_US_PRODUCT(10245315L),
	INTERNATIONAL_POS_DEBIT_CARD_CASH_WITHDRAWL_PRODUCT(10245315L),
	DEBIT_CARD_ISSUANCE_PRODUCT(10245113L),
	CUSTOMER_DEBIT_CARD_ISSUANCE_PRODUCT(10245121L),
	DEBIT_CARD_RE_ISSUANCE_PRODUCT(10245136L),
	DEBIT_CARD_ANNUAL_FEE_PRODUCT(10245137L),
	 ATM_BALANCE_INQUIRY_OFF_US(10245296L),
	 INTERNATIONAL_BALANCE_INQUIRY_OFF_US(10245319L);

	private static final Map<Long, TransactionProductEnum> lookup = new HashMap<Long, TransactionProductEnum>();
	private Long productId;
	
	static {
	
		for(TransactionProductEnum transactionProductEnum : EnumSet.allOf(TransactionProductEnum.class)) {
	        
	        lookup.put(transactionProductEnum.getProductId(), transactionProductEnum);
		}
	}

	private TransactionProductEnum(Long productId) {
		this.productId = productId;
	}

	public long getProductId() {
		return productId.longValue();
	}

	private void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public static TransactionProductEnum lookup(String productId) {
		return lookup.get(Long.parseLong(productId));
	}
	
	
	public static Boolean contains(Long productId){
	
		Boolean retVal=Boolean.FALSE;
		
		for (TransactionProductEnum transactionProductEnum : TransactionProductEnum.values()) {
		
			if(transactionProductEnum.productId.equals(productId)){
				retVal=Boolean.TRUE;
				break;
			}
		}
		
		return retVal;
	}
	
	
	public static TransactionProductEnum getTransactionProductEnum(String productId) {

		TransactionProductEnum transactionProductEnum = lookup("0");
		transactionProductEnum.setProductId(Long.valueOf(productId));
		
		return transactionProductEnum;
	}
	
}

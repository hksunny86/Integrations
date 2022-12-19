package com.inov8.microbank.common.util;

public enum BulkDisbursementType {
	SALARY_DISBURSEMENT(1001), FUEL_DISBURSEMENT(1002), IDP_PAYMENT(1003);

	Integer disbursementType;

	private BulkDisbursementType(Integer disbursementType) {
		this.disbursementType = disbursementType;
	}

	public Integer getValue() {
		return this.disbursementType;
	}
}

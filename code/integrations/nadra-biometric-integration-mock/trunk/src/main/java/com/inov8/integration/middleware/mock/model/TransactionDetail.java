package com.inov8.integration.middleware.mock.model;

import org.apache.commons.lang.StringUtils;

public class TransactionDetail {
	private String id;
	private String amount;
	private String stan;
	private String type;
	private String date;

	@Override
	public String toString() {
		return this.date.substring(0, 10).replace("-", "")+"  114 ATM "+this.stan+StringUtils.leftPad(amount, 15)+"D";
		//return null;
	}
	
	
	//22072013  114 ATM 560556          15.00D22072013  114 CHW 560549       10000.00D22072013  114 DSC 402848          35.00D20072013  114 WHT 922978         187.51D20072013  114 NPA 892505       38094.26D20072013  114 ATM 863373         781.26D20072013  114 CHW 863366       31250.67D20072013  114 ATM 863112         781.26D                                        AVAILABLE BALANCE :   1979487.65
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStan() {
		return stan;
	}
	public void setStan(String stan) {
		this.stan = stan;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}

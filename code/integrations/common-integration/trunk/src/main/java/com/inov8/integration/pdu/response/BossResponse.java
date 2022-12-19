package com.inov8.integration.pdu.response;

import java.util.HashMap;
import java.util.Map;

import com.inov8.integration.pdu.BossPdu;

public class BossResponse extends BossPdu { 
	private static final long serialVersionUID = -2077532714796193184L;

	private String command;
	
	private String bossResponse;
	private String transactionRefNumber;
	private String responseCode;
	private String responseCodeDescription;
	private String keyDN;
	private String keyETOPBALANCE;
	private String keyPHONENUM;
	private String keyPHONEBALANCE;
	private String keyBOSSSONBR;
	private String billType;

	private Map<String, String> extras = new HashMap<String, String>();

	public String getBossResponse() {
		return bossResponse;
	}

	public void setBossResponse(String bossResponse) {
		this.bossResponse = bossResponse;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseCodeDescription() {
		return responseCodeDescription;
	}

	public void setResponseCodeDescription(String responseCodeDescription) {
		this.responseCodeDescription = responseCodeDescription;
	}

	public String getKeyDN() {
		return keyDN;
	}

	public void setKeyDN(String keyDN) {
		this.keyDN = keyDN;
	}

	public String getKeyETOPBALANCE() {
		return keyETOPBALANCE;
	}

	public void setKeyETOPBALANCE(String keyETOPBALANCE) {
		this.keyETOPBALANCE = keyETOPBALANCE;
	}

	public String getKeyPHONENUM() {
		return keyPHONENUM;
	}

	public void setKeyPHONENUM(String keyPHONENUM) {
		this.keyPHONENUM = keyPHONENUM;
	}

	public String getKeyPHONEBALANCE() {
		return keyPHONEBALANCE;
	}

	public void setKeyPHONEBALANCE(String keyPHONEBALANCE) {
		this.keyPHONEBALANCE = keyPHONEBALANCE;
	}

	public String getKeyBOSSSONBR() {
		return keyBOSSSONBR;
	}

	public void setKeyBOSSSONBR(String keyBOSSSONBR) {
		this.keyBOSSSONBR = keyBOSSSONBR;
	}

	public Map<String, String> getExtras() {
		return extras;
	}

	public void setExtras(Map<String, String> extras) {
		this.extras = extras;
	}

	public String getTransactionRefNumber() {
		return transactionRefNumber;
	}

	public void setTransactionRefNumber(String transactionRefNumber) {
		this.transactionRefNumber = transactionRefNumber;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	@Override
	public String toString() {
		return "BossResponse [command=" + command + ", bossResponse=" + bossResponse + ", transactionRefNumber=" + transactionRefNumber + ", responseCode="
				+ responseCode + ", responseCodeDescription=" + responseCodeDescription + ", keyDN=" + keyDN + ", keyETOPBALANCE=" + keyETOPBALANCE
				+ ", keyPHONENUM=" + keyPHONENUM + ", keyPHONEBALANCE=" + keyPHONEBALANCE + ", keyBOSSSONBR=" + keyBOSSSONBR + ", extras=" + extras + ", SUBTYPE=" + billType + "]";
	}
}

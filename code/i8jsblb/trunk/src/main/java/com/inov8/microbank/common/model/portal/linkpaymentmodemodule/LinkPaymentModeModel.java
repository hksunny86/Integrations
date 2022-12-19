package com.inov8.microbank.common.model.portal.linkpaymentmodemodule;

import java.io.Serializable;
import java.util.Date;




public class LinkPaymentModeModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1455263644333809634L;
	/**
	 * 
	 */
	
	public static final String LINK_PAYMENT__MODE_MODEL_KEY = "linkPaymentModeModel";
	private String name;
	private String mfsId;
	private Long paymentMode;
	private Long cardType;
	private String cardNo;	
	private String accountNo;
	private Date expiryDate;
	private String nic;
	private Long appUserId;
	private Long accountType;
	private Long currencyCode;
	
	
	public static String getLINK_PAYMENT__MODE_MODEL_KEY() {
		return LINK_PAYMENT__MODE_MODEL_KEY;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public Long getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(Long paymentMode) {
		this.paymentMode = paymentMode;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getMfsId() {
		return mfsId;
	}
	public void setMfsId(String mfsId) {
		this.mfsId = mfsId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Long getCardType() {
		return cardType;
	}
	public void setCardType(Long cardType) {
		this.cardType = cardType;
	}
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	public Long getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}
	public Long getAccountType() {
		return accountType;
	}
	public void setAccountType(Long accountType) {
		this.accountType = accountType;
	}
	public Long getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(Long currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	
	

}

package com.inov8.microbank.common.model.portal.mfsaccountmodule;

import java.io.Serializable;

public class AdditionalDetailVOModel implements Serializable{

    public String salesTaxRegNo;
    public String membershipNoTradeBody;
    public String incorporationDate;
    public String secpRegNo;
    /*added by atif hussain*/
    public String registrationPlace;
    
	public String getSalesTaxRegNo() {
		return salesTaxRegNo;
	}
	public void setSalesTaxRegNo(String salesTaxRegNo) {
		this.salesTaxRegNo = salesTaxRegNo;
	}
	public String getMembershipNoTradeBody() {
		return membershipNoTradeBody;
	}
	public void setMembershipNoTradeBody(String membershipNoTradeBody) {
		this.membershipNoTradeBody = membershipNoTradeBody;
	}
	public String getIncorporationDate() {
		return incorporationDate;
	}
	public void setIncorporationDate(String incorporationDate) {
		this.incorporationDate = incorporationDate;
	}
	public String getSecpRegNo() {
		return secpRegNo;
	}
	public void setSecpRegNo(String secpRegNo) {
		this.secpRegNo = secpRegNo;
	}
	public String getRegistrationPlace() {
		return registrationPlace;
	}
	public void setRegistrationPlace(String registrationPlace) {
		this.registrationPlace = registrationPlace;
	}
}

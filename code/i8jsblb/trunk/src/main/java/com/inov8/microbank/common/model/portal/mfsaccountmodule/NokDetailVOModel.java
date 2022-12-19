package com.inov8.microbank.common.model.portal.mfsaccountmodule;

import java.io.Serializable;

public class NokDetailVOModel implements Serializable{

    /**
	 * 
	 */
	private static final long	serialVersionUID	= -8577284583171197322L;
	private String nokName;
    private String nokMailingAdd;
    private Long nokCountry;
    private String nokProvince;
    private Long nokCity;
    private Long nokPostalCode;
    private String nokContactNo;
    private String nokMobile;
    private String nokRelationship;
    private Long nokIdType;
    private String nokIdTypeName;
    private String nokIdNumber;
    
    
	public String getNokName() {
		return nokName;
	}
	public void setNokName(String nokName) {
		this.nokName = nokName;
	}
	public String getNokMailingAdd() {
		return nokMailingAdd;
	}
	public void setNokMailingAdd(String nokMailingAdd) {
		this.nokMailingAdd = nokMailingAdd;
	}
	public Long getNokCountry() {
		return nokCountry;
	}
	public void setNokCountry(Long nokCountry) {
		this.nokCountry = nokCountry;
	}
	public String getNokProvince() {
		return nokProvince;
	}
	public void setNokProvince(String nokProvince) {
		this.nokProvince = nokProvince;
	}
	public Long getNokCity() {
		return nokCity;
	}
	public void setNokCity(Long nokCity) {
		this.nokCity = nokCity;
	}
	public Long getNokPostalCode() {
		return nokPostalCode;
	}
	public void setNokPostalCode(Long nokPostalCode) {
		this.nokPostalCode = nokPostalCode;
	}
	public String getNokContactNo() {
		return nokContactNo;
	}
	public void setNokContactNo(String nokContactNo) {
		this.nokContactNo = nokContactNo;
	}
	public String getNokMobile() {
		return nokMobile;
	}
	public void setNokMobile(String nokMobile) {
		this.nokMobile = nokMobile;
	}
	public String getNokRelationship() {
		return nokRelationship;
	}
	public void setNokRelationship(String nokRelationship) {
		this.nokRelationship = nokRelationship;
	}
	public String getNokIdNumber() {
		return nokIdNumber;
	}
	public void setNokIdNumber(String nokIdNumber) {
		this.nokIdNumber = nokIdNumber;
	}
	public Long getNokIdType() {
		return nokIdType;
	}
	public void setNokIdType(Long nokIdType) {
			this.nokIdType = nokIdType;
	}
	public String getNokIdTypeName() {
		return nokIdTypeName;
	}
	public void setNokIdTypeName(String nokIdTypeName) {
			this.nokIdTypeName = nokIdTypeName;
	}
}

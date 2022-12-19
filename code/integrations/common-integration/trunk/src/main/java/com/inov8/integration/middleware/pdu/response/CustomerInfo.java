package com.inov8.integration.middleware.pdu.response;

import java.io.Serializable;

public class CustomerInfo implements Serializable {

	private static final long serialVersionUID = -2699629916982144943L;
	private String name;
	private String dateOfBirth;
	private String gender;
	private String phoneOffice;
	private String phoneMobile;
	private String phoneResidence;
	private String addressOffice;
	private String addressCrrospondence;
	private String addressResidence;
	private String nic;
	private String email;
	private String branchCode;
	private String fax;
	private String priorityFlag;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	public String getPhoneResidence() {
		return phoneResidence;
	}

	public void setPhoneResidence(String phoneResidence) {
		this.phoneResidence = phoneResidence;
	}

	public String getAddressOffice() {
		return addressOffice;
	}

	public void setAddressOffice(String addressOffice) {
		this.addressOffice = addressOffice;
	}

	public String getAddressCrrospondence() {
		return addressCrrospondence;
	}

	public void setAddressCrrospondence(String addressCrrospondence) {
		this.addressCrrospondence = addressCrrospondence;
	}

	public String getAddressResidence() {
		return addressResidence;
	}

	public void setAddressResidence(String addressResidence) {
		this.addressResidence = addressResidence;
	}

	public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPriorityFlag() {
		return priorityFlag;
	}

	public void setPriorityFlag(String priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

}

package com.inov8.integration.middleware.pdu.response;

import java.io.Serializable;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class UBPSCompany implements Serializable{

	private static final long serialVersionUID = 7911023652614138112L;
	private String pattren;
	private String companyName;
	private Integer companyCode;
	private String companyAccNo;
	private String address;
	
	private String phone;
	private String email;
	
	private Boolean allowAfterDueDate;
	private String consumerNoLengthMin;
	private String consumerNoLengthMax ;
	private String mobilePrefix;
	
	
	public String build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(trimToEmpty(pattren)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(companyName)).append(AMBIT_DELIMITER.getValue());
		pduString.append(companyCode).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(companyAccNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(address)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(phone)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(email)).append(AMBIT_DELIMITER.getValue());
		if(allowAfterDueDate != null && allowAfterDueDate == true){
			pduString.append("Y").append(AMBIT_DELIMITER.getValue());
		}else{
			pduString.append("N").append(AMBIT_DELIMITER.getValue());
		}
		pduString.append(trimToEmpty(consumerNoLengthMin)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(consumerNoLengthMax)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(mobilePrefix)).append(AMBIT_DELIMITER.getValue());
		
		return pduString.toString();
	}
	
	public String getPattren() {
		return pattren;
	}

	public void setPattren(String pattren) {
		this.pattren = pattren;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(Integer companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyAccNo() {
		return companyAccNo;
	}

	public void setCompanyAccNo(String companyAccNo) {
		this.companyAccNo = companyAccNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean isAllowAfterDueDate() {
		return allowAfterDueDate;
	}

	public void setAllowAfterDueDate(Boolean allowAfterDueDate) {
		this.allowAfterDueDate = allowAfterDueDate;
	}

	public String getMobilePrefix() {
		return mobilePrefix;
	}

	public void setMobilePrefix(String mobilePrefix) {
		this.mobilePrefix = mobilePrefix;
	}

	public String getConsumerNoLengthMin() {
		return consumerNoLengthMin;
	}

	public void setConsumerNoLengthMin(String consumerNoLengthMin) {
		this.consumerNoLengthMin = consumerNoLengthMin;
	}

	public String getConsumerNoLengthMax() {
		return consumerNoLengthMax;
	}

	public void setConsumerNoLengthMax(String consumerNoLengthMax) {
		this.consumerNoLengthMax = consumerNoLengthMax;
	}
	
}

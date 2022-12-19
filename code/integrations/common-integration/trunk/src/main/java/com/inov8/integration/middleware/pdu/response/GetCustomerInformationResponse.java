package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class GetCustomerInformationResponse extends AMBITPDU {

	private static final long serialVersionUID = 5265651637376598575L;
	private String responseCode;
	private CustomerInfo customerInfo;
	
	public GetCustomerInformationResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}
	
	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());
		
		if(customerInfo != null){
			pduString.append(trimToEmpty(customerInfo.getName())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getDateOfBirth())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getGender())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getPhoneOffice())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getPhoneMobile())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getPhoneResidence())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getAddressOffice())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getAddressCrrospondence())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getAddressResidence())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getNic())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getEmail())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getBranchCode())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getFax())).append(AMBIT_DELIMITER.getValue());
			pduString.append(trimToEmpty(customerInfo.getPriorityFlag())).append(AMBIT_DELIMITER.getValue());
		}
		

		FieldUtil.appendMessageSizeByte(pduString);

		super.setRawPdu(pduString.toString());
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}

}

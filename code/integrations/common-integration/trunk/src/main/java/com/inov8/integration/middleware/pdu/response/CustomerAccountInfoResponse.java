package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class CustomerAccountInfoResponse extends AMBITPDU {

	private static final long serialVersionUID = 1635980526503435972L;

	private String responseCode;

	private List<CustomerAccountInfo> accounts = null;
	
	public CustomerAccountInfoResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}

	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());

		if(CollectionUtils.isNotEmpty(accounts)){
			pduString.append(accounts.size()).append(AMBIT_DELIMITER.getValue());
			for (CustomerAccountInfo accountInfo : accounts) {
				pduString.append(accountInfo.build());
			}
		}else{
			pduString.append(0).append(AMBIT_DELIMITER.getValue());
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

	public List<CustomerAccountInfo> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<CustomerAccountInfo> accounts) {
		this.accounts = accounts;
	}

}
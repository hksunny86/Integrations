package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;
import com.inov8.integration.middleware.util.VersionInfo;
import com.inov8.integration.middleware.util.VersionInfo.Priority;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

//@formatter:off
@VersionInfo(
		createdBy = "Zeeshan Ahmad", 
		lastModified = "18-11-2014", 
		priority = Priority.HIGH,
		tags = { "Meezan","AMBIT", "User Password Auth Response" }, 
		version = "1.0",
		releaseVersion = "2.3",
		patchVersion = "2.3.12",
		notes = "User Password Auth Response for i8 - rdv communication.")
//@formatter: on
public class UserPasswordAuthResponse extends AMBITPDU {
	
	private static final long serialVersionUID = -8376131767087040263L;
	private String responseCode;
	private String newCustomerFlag;
	private String customerName;
	private String changePassFlag;
	private String username;
	private String password;
	private String enableIBFTFlag;

	private List<AccountInfo> accounts;
	
	
	public UserPasswordAuthResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}
	
	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(newCustomerFlag)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(customerName)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(changePassFlag)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(username)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(password)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(enableIBFTFlag)).append(AMBIT_DELIMITER.getValue());

		if(CollectionUtils.isNotEmpty(accounts)){
			pduString.append(accounts.size()).append(AMBIT_DELIMITER.getValue());
			for (AccountInfo acc : accounts) {
				pduString.append(acc.build());
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

	public String getNewCustomerFlag() {
		return newCustomerFlag;
	}

	public void setNewCustomerFlag(String newCustomerFlag) {
		this.newCustomerFlag = newCustomerFlag;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getChangePassFlag() {
		return changePassFlag;
	}

	public void setChangePassFlag(String changePassFlag) {
		this.changePassFlag = changePassFlag;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<AccountInfo> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountInfo> accounts) {
		this.accounts = accounts;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEnableIBFTFlag() {
		return enableIBFTFlag;
	}

	public void setEnableIBFTFlag(String enableIBFTFlag) {
		this.enableIBFTFlag = enableIBFTFlag;
	}

}
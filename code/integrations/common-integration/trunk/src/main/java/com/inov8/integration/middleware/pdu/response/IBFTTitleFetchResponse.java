package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;
import com.inov8.integration.middleware.util.VersionInfo;
import com.inov8.integration.middleware.util.VersionInfo.Priority;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

//@formatter:off
@VersionInfo(
		createdBy = "Zeeshan Ahmad", 
		lastModified = "18-11-2014", 
		priority = Priority.HIGH,
		tags = { "Meezan","AMBIT", "IBFT Title Fetch Response" }, 
		version = "1.0",
		releaseVersion = "2.3",
		patchVersion = "2.3.12",
		notes = "IBFT Title Fetch Response for i8 - rdv communication.")
//@formatter: on
public class IBFTTitleFetchResponse extends AMBITPDU {

	private static final long serialVersionUID = -4370065192801018419L;
	private String responseCode;
	private String fromAccountNo;
	private String toAccountNo;
	private String toBankName;
	private String toBankIMD;
	private String amount;
	private String accountTitle;
	private String branchName;
	
	public IBFTTitleFetchResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}
	
	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(fromAccountNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(toAccountNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(toBankName)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(toBankIMD)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(amount)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(accountTitle)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(branchName)).append(AMBIT_DELIMITER.getValue());

		FieldUtil.appendMessageSizeByte(pduString);

		super.setRawPdu(pduString.toString());
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getFromAccountNo() {
		return fromAccountNo;
	}

	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public String getToBankName() {
		return toBankName;
	}

	public void setToBankName(String toBankName) {
		this.toBankName = toBankName;
	}

	public String getToBankIMD() {
		return toBankIMD;
	}

	public void setToBankIMD(String toBankIMD) {
		this.toBankIMD = toBankIMD;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAccountTitle() {
		return accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	
	
}

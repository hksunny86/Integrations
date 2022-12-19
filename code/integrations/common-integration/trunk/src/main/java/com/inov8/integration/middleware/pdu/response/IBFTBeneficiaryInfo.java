package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.util.VersionInfo;
import com.inov8.integration.middleware.util.VersionInfo.Priority;

import java.io.Serializable;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

//@formatter:off
@VersionInfo(
		createdBy = "Zeeshan Ahmad", 
		lastModified = "18-11-2014", 
		priority = Priority.HIGH,
		tags = { "Meezan","AMBIT", "IBFT Beneficiary Info" }, 
		version = "1.0",
		releaseVersion = "2.3",
		patchVersion = "2.3.12",
		notes = "IBFT Beneficiary Info for i8 - rdv communication.")
//@formatter: on
public class IBFTBeneficiaryInfo implements Serializable{

	private static final long serialVersionUID = 8496140094809084822L;
	
	private String branchName;
	private String accountNo;
	private String title;
	private String alias;
	private String bankName;
	private String bankIMD;
	
	public String build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(trimToEmpty(branchName)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(accountNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(title)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(alias)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(bankName)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(bankIMD)).append(AMBIT_DELIMITER.getValue());

		return pduString.toString();
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankIMD() {
		return bankIMD;
	}

	public void setBankIMD(String bankIMD) {
		this.bankIMD = bankIMD;
	}

	

}

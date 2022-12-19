package com.inov8.integration.middleware.pdu.response;

import java.io.Serializable;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class BeneficiaryInfo implements Serializable{

	private static final long serialVersionUID = -1177089811460059634L;
	private String branchCode;
	private String branchName;
	private String title;
	private String currency;
	private String accountType;
	private String accountNo;
	private String alias;
	private String iban;
	
	public String build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(trimToEmpty(branchCode)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(branchName)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(title)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(currency)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(accountType)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(accountNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(alias)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(iban)).append(AMBIT_DELIMITER.getValue());

		return pduString.toString();
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

}

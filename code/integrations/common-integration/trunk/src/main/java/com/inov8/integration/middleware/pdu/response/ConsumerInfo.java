package com.inov8.integration.middleware.pdu.response;

import java.io.Serializable;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class ConsumerInfo implements Serializable{

	private static final long serialVersionUID = -5768337710814779264L;
	private String billName;
	private String billNo;
	private String company;
	private String companyId;
	private String attachedAccount;
	private String iban;
	
	public String build(){
		StringBuilder pduString = new StringBuilder();

		pduString.append(trimToEmpty(billName)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(billNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(companyId)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(company)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(attachedAccount)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(iban)).append(AMBIT_DELIMITER.getValue());

		return pduString.toString();
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAttachedAccount() {
		return attachedAccount;
	}

	public void setAttachedAccount(String attachedAccount) {
		this.attachedAccount = attachedAccount;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "ConsumerInfo [billName=" + billName + ", billNo=" + billNo + ", company=" + company + ", companyId=" + companyId + ", attachedAccount="
				+ attachedAccount + ", iban=" + iban + "]";
	}

}

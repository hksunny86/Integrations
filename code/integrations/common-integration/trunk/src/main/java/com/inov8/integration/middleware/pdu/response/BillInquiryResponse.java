package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class BillInquiryResponse extends AMBITPDU {

	private static final long serialVersionUID = 5803454056793504018L;
	private String responseCode;
	private String amount;
	private String fromAccountNo;
	private String companyCode;
	private String toAccountNo;
	private String amountAfterDueDate;
	private String isRegistered;
	private String dueDate;
	private String status;
	private String consumerCount;
	private String billName;

	public BillInquiryResponse() {
		if (super.getHeader() == null) {
			super.setHeader(new AMBITHeader());
		}
	}

	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(amount)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(fromAccountNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(companyCode)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(toAccountNo)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(amountAfterDueDate)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(isRegistered)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(dueDate)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(status)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(consumerCount)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(billName)).append(AMBIT_DELIMITER.getValue());

		FieldUtil.appendMessageSizeByte(pduString);

		super.setRawPdu(pduString.toString());
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFromAccountNo() {
		return fromAccountNo;
	}

	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public String getAmountAfterDueDate() {
		return amountAfterDueDate;
	}

	public void setAmountAfterDueDate(String amountAfterDueDate) {
		this.amountAfterDueDate = amountAfterDueDate;
	}

	public String getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(String isRegistered) {
		this.isRegistered = isRegistered;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConsumerCount() {
		return consumerCount;
	}

	public void setConsumerCount(String consumerCount) {
		this.consumerCount = consumerCount;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

}

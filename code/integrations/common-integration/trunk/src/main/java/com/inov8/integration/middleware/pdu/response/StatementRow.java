package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.enums.MiddlewareEnum;
import com.inov8.integration.middleware.util.DateTools;

import java.io.Serializable;
import java.util.Date;

import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class StatementRow implements Serializable {

	private static final long serialVersionUID = -5348907429826890908L;
	private Date date;
	private String stan;
	private String description;
	private String amountDr;
	private String amountCr;
	private String balance;
	
	
	public String build() {

		StringBuilder statementRow = new StringBuilder();

		statementRow.append(trimToEmpty(DateTools.dateToString(date, MiddlewareEnum.FULL_SATAEMENT_RESPONSE_DATE_FORMAT.getValue()))).append("?");
		statementRow.append(trimToEmpty(stan)).append("?");
		statementRow.append(trimToEmpty(description)).append("?");
		statementRow.append(trimToEmpty(amountDr)).append("?");
		statementRow.append(trimToEmpty(amountCr)).append("?");
		statementRow.append(trimToEmpty(balance));
		
		return statementRow.toString();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAmountDr() {
		return amountDr;
	}

	public void setAmountDr(String amountDr) {
		this.amountDr = amountDr;
	}

	public String getAmountCr() {
		return amountCr;
	}

	public void setAmountCr(String amountCr) {
		this.amountCr = amountCr;
	}

}

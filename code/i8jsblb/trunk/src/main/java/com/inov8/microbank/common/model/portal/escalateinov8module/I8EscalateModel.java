package com.inov8.microbank.common.model.portal.escalateinov8module;

import java.io.Serializable;

public class I8EscalateModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2214455368621059532L;
	private Long transactionId;
	private String escalateToPartner;
	private String issueDetail;
	private Long issueId;
	private String issueCode;
	private String transactionCode;
	
	
	
	/**
	 * @return the issueId
	 */
	public Long getIssueId() {
		return issueId;
	}
	/**
	 * @param issueId the issueId to set
	 */
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}
	/**
	 * @return the escalateToPartner
	 */
	public String getEscalateToPartner() {
		return escalateToPartner;
	}
	/**
	 * @param escalateToPartner the escalateToPartner to set
	 */
	public void setEscalateToPartner(String escalateToPartner) {
		this.escalateToPartner = escalateToPartner;
	}
	/**
	 * @return the issueDetail
	 */

	/**
	 * @return the issueDetail
	 */
	public String getIssueDetail() {
		return issueDetail;
	}
	/**
	 * @param issueDetail the issueDetail to set
	 */
	public void setIssueDetail(String issueDetail) {
		this.issueDetail = issueDetail;
	}
	/**
	 * @return the transactionId
	 */
	public Long getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the issueCode
	 */
	public String getIssueCode() {
		return issueCode;
	}
	/**
	 * @param issueCode the issueCode to set
	 */
	public void setIssueCode(String issueCode) {
		this.issueCode = issueCode;
	}
	/**
	 * @return the transactionCode
	 */
	public String getTransactionCode() {
		return transactionCode;
	}
	/**
	 * @param transactionCode the transactionCode to set
	 */
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	
	
	
	
}

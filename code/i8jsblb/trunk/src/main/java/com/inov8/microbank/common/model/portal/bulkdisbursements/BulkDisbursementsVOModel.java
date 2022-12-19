package com.inov8.microbank.common.model.portal.bulkdisbursements;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class BulkDisbursementsVOModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5843757324831538850L;

	private Long productId;
	
	private Integer paymentTypeId;

	private Boolean limitApplicable;

	private Boolean payCashViaCnic;

	private MultipartFile csvFile;
	
	private String sourceACNo;
	
	private String sourceACNick;

    
	public MultipartFile getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(MultipartFile csvFile) {
		this.csvFile = csvFile;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(Integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public Boolean getLimitApplicable() {
		return limitApplicable;
	}

	public void setLimitApplicable(Boolean limitApplicable) {
		this.limitApplicable = limitApplicable;
	}

	public Boolean getPayCashViaCnic() {
		return payCashViaCnic;
	}

	public void setPayCashViaCnic(Boolean payCashViaCnic) {
		this.payCashViaCnic = payCashViaCnic;
	}

	public String getSourceACNo() {
		return sourceACNo;
	}

	public void setSourceACNo(String sourceACNo) {
		this.sourceACNo = sourceACNo;
	}

	public String getSourceACNick() {
		return sourceACNick;
	}

	public void setSourceACNick(String sourceACNick) {
		this.sourceACNick = sourceACNick;
	}
}

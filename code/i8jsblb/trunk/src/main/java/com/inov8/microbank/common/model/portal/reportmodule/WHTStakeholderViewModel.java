package com.inov8.microbank.common.model.portal.reportmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "WITHHOLDING_TAX_REPORT_VIEW")
public class WHTStakeholderViewModel extends BasePersistableModel implements java.io.Serializable {

	private static final long serialVersionUID = -1;

	private Long id;
	private String paymentSection;
	private String taxPayer;
	private Double taxableAmount;
	private Double taxAmount;
//	private Date createdOn;

	private Date startDate;
	private Date endDate;

	@Transient
	@Override
	public Long getPrimaryKey() {
		return getId();
	}

    @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		return "id";
	}

	@Override
	@javax.persistence.Transient
	public void setPrimaryKey( Long primaryKey ) {
		setId( primaryKey );
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "&id=" + getPrimaryKey();
		return parameters;
	}

	// Property accessors
	@Id
	@Column(name = "ID", nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "payment_section")
	public String getPaymentSection() {
		return paymentSection;
	}

	public void setPaymentSection(String paymentSection) {
		this.paymentSection = paymentSection;
	}
	@Column(name = "tax_payer_name")
	public String getTaxPayer() {
		return taxPayer;
	}

	public void setTaxPayer(String taxPayer) {
		this.taxPayer = taxPayer;
	}
	@Column(name = "taxable_amount")
	public Double getTaxableAmount() {
		return taxableAmount;
	}

	public void setTaxableAmount(Double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}
	@Column(name = "tax_amount")
	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
/*	
	@Column(name = "created_on")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
*/
	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}

	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}	

}
package com.inov8.microbank.tax.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

//@Entity
//@org.hibernate.annotations.Entity(dynamicInsert = true)
//@Table(name = "FED_RULE_VIEW")
public class FEDRuleViewModel extends BasePersistableModel {

	private static final long serialVersionUID = 1L;
	private Long pk;
	private Long fedRuleId;
	private Long taxRegimeId;
	private String taxRegimeName;
	private Long serviceId;
	private String serviceName;
	private Long productId;
	private String productName;
	private Double rate;
	private Boolean inclusive;
	private Boolean active;

	private Long createdBy;
	private Long updatedBy;
	private Date updatedOn;
	private Date createdOn;
	private Integer versionNo;
	
//	@Column(name = "PK")
	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

//	@Column(name = "TAX_REGIME_ID")
	public Long getTaxRegimeId() {
		return taxRegimeId;
	}

	public void setTaxRegimeId(Long taxRegimeId) {
		this.taxRegimeId = taxRegimeId;
	}

//	@Column(name = "TAX_REGIME_NAME")
	public String getTaxRegimeName() {
		return taxRegimeName;
	}

	public void setTaxRegimeName(String taxRegimeName) {
		this.taxRegimeName = taxRegimeName;
	}

//	@Column(name = "SERVICE_ID")
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

//	@Column(name = "SERVICE_NAME")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

//	@Column(name = "PRODUCT_ID")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

//	@Column(name = "PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

//	@Column(name = "CREATED_BY")
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdById) {
		this.createdBy = createdById;
	}

//	@Column(name = "UPDATED_BY")
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedById) {
		this.updatedBy = updatedById;
	}

//	@Column(name = "FED_RULE_ID")
	public Long getFedRuleId() {
		return fedRuleId;
	}

	public void setFedRuleId(Long fedRuleId) {
		this.fedRuleId = fedRuleId;
	}


//	@Column(name = "RATE")
	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

//	@Column(name = "INCLUSIVE")
	public Boolean getInclusive() {
		return inclusive;
	}

	public void setInclusive(Boolean inclusive) {
		this.inclusive = inclusive;
	}

//	@Column(name = "IS_ACTIVE")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}


//	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

//	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Version
//	@Column(name = "VERSION_NO")
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@Transient
	@Override
	public Long getPrimaryKey() {
		return getPk();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName() {
		return "pk";
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter() {
		return "&pk=" + getPk();
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		setFedRuleId(arg0);
	}

}

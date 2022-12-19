package com.inov8.microbank.common.model.portal.salesmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;


@Entity
@org.hibernate.annotations.Entity( mutable=false )
@Table(name = "SALES_TEAM_COMM_VIEW")
public class SalesTeamComissionViewModel extends BasePersistableModel implements Serializable
{
    private static final long serialVersionUID = -2994541278816718482L;
 

	private String transactionId;
	private Long bankUserId;
	private String agentId;
	private String username;
	private String supplierName;
	private String productName;
	private Long supplierId;
	private Long productId;
	private Date createdOn;
	private Double totalAmount;
	private Double charges;
	private Double salesTeamCommission;
	private Double bdeCommission;
	private String bdeUsername;
	private String bdeRole;
	private Double tlCommission;
	private String tlRole;
	private String tlUsername;
	private Double inclusiveCharges;
	private Double exclusiveCharges;
	@Transient
	@Override
    public Long getPrimaryKey()
    {
        return getBankUserId();
    }

	@Override
    public void setPrimaryKey( Long primaryKey )
    {
        setBankUserId( primaryKey );
    }

	@Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
		String primaryKeyFieldName = "bankUserId";
	    return primaryKeyFieldName;
    }


	@Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&bankUserId=" + bankUserId;
    }

	
	@Id
	@Column(name = "TRANSACTION_ID")
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	@Column(name="AGENT_ID")
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	@Column(name="SALE_USER_NAME")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="SUPPLIER_NAME")
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	@Column(name="PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Column(name="SUPPLIER_ID" , insertable = false , updatable = false)
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	
	@Column(name="PRODUCT_ID", insertable = false , updatable = false)
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	@Column(name="TRANSACTION_DATE")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@Column(name="AMOUNT")
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Column(name = "CHARGES")
	public Double getCharges() {
		return charges;
	}
	public void setCharges(Double charges) {
		this.charges = charges;
	}
	
	@Column(name="SALE_TEAM_COMMISION")
	public Double getSalesTeamCommission() {
		return salesTeamCommission;
	}
	public void setSalesTeamCommission(Double salesTeamCommission) {
		this.salesTeamCommission = salesTeamCommission;
	}
	
	@Column(name = "BDE_COMMISSION")
	public Double getBdeCommission() {
		return bdeCommission;
	}
	public void setBdeCommission(Double bdeCommission) {
		this.bdeCommission = bdeCommission;
	}
	
	@Column(name="BDE_USER_NAME")
	public String getBdeUsername() {
		return bdeUsername;
	}
	public void setBdeUsername(String bdeUsername) {
		this.bdeUsername = bdeUsername;
	}
	
	@Column(name="BDE_ROLE")
	public String getBdeRole() {
		return bdeRole;
	}
	public void setBdeRole(String bdeRole) {
		this.bdeRole = bdeRole;
	}

	@Column(name = "TL_COMMISSION")
	public Double getTlCommission() {

		return tlCommission;
	}
	public void setTlCommission(Double tlCommission) {
		this.tlCommission = tlCommission;
	}
	
	@Column(name="TL_ROLE")
	public String getTlRole() {
		return tlRole;
	}
	public void setTlRole(String tlRole) {
		this.tlRole = tlRole;
	}
	
	@Column(name="TL_USER_NAME")
	public String getTlUsername() {
		return tlUsername;
	}
	public void setTlUsername(String tlUsername) {
		this.tlUsername = tlUsername;
	}

	@Column(name="CHARGES_INCLUSIVE")
	public Double getInclusiveCharges() {
		return inclusiveCharges;
	}

	public void setInclusiveCharges(Double inclusiveCharges) {
		this.inclusiveCharges = inclusiveCharges;
	}

	@Column(name="CHARGES_EXCLUSIVE")
	public Double getExclusiveCharges() {
		return exclusiveCharges;
	}

	public void setExclusiveCharges(Double exclusiveCharges) {
		this.exclusiveCharges = exclusiveCharges;
	}
	
	@Column(name = "BANK_USER_ID" , nullable = false)
	public Long getBankUserId() {
		return bankUserId;
	}

	public void setBankUserId(Long bankUserId) {
		this.bankUserId = bankUserId;
	}


   

}

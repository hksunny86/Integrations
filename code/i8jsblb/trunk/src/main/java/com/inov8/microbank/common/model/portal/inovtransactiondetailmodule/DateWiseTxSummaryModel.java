package com.inov8.microbank.common.model.portal.inovtransactiondetailmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The DateWiseTxSummaryModel entity bean.
 *
 * @author Rizwan Munir Inov8 Limited
 *
 *
 * @spring.bean name="DateWiseTxSummaryModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DATE_WISE_TX_SUMMARY")
public class DateWiseTxSummaryModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = 9111449919210640606L;
	private Long productId;
	private String productName;
	private String transactionCode;
	private Double transactionAmount;
	private Long supplierId;
	private Double inclusiveCharges;
	private Double exclusiveCharges;
	private Double taxDeducted;
	private Double akblCommission;
	private Double agentCommission;
	private Double agent2Commission;
	private Double franchise1Commission;
	private Double franchise2Commission;
	private Date createdOn;
	private Date updatedOn;
	private Date startDate;
	private Date endDate;
	private Double otherCommission;
	private Double salesTeamCommission;
	
   /**
    * Default constructor.
    */
   public DateWiseTxSummaryModel() {
   }

   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductId();
    }

   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setProductId(primaryKey);
    }


   @Column(name = "PRODUCT_ID"  )
   @Id 
   public Long getProductId() {
	   return productId;
   }

   public void setProductId(Long productId) {
	   this.productId = productId;
   }



    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getProductId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productId=" + getProductId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() { 
		String primaryKeyFieldName = "productId";
		return primaryKeyFieldName;				
    }

    @Column(name = "PRODUCT_NAME"  )
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "TRANSACTION_CODE"  )
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Column(name = "TRANSACTION_AMOUNT"  )
	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	@Column(name = "SERVICE_CHARGES_INCLUSIVE"  )
	public Double getInclusiveCharges() {
		return inclusiveCharges;
	}

	public void setInclusiveCharges(Double inclusiveCharges) {
		this.inclusiveCharges = inclusiveCharges;
	}

	@Column(name = "SERVICE_CHARGES_EXCLUSIVE"  )
	public Double getExclusiveCharges() {
		return exclusiveCharges;
	}

	public void setExclusiveCharges(Double exclusiveCharges) {
		this.exclusiveCharges = exclusiveCharges;
	}

	@Column(name = "TAX_DEDUCTED"  )
	public Double getTaxDeducted() {
		return taxDeducted;
	}

	public void setTaxDeducted(Double taxDeducted) {
		this.taxDeducted = taxDeducted;
	}
	
	@Column(name = "TO_BANK"  )
	public Double getAkblCommission() {
		return akblCommission;
	}

	public void setAkblCommission(Double akblCommission) {
		this.akblCommission = akblCommission;
	}

	@Column(name = "TO_AGENT1"  )
	public Double getAgentCommission() {
		return agentCommission;
	}

	public void setAgentCommission(Double agentCommission) {
		this.agentCommission = agentCommission;
	}

	@Column(name = "TO_AGENT2"  )
	public Double getAgent2Commission() {
		return agent2Commission;
	}

	public void setAgent2Commission(Double agent2Commission) {
		this.agent2Commission = agent2Commission;
	}

	@Column(name = "TO_FRANCHISE1"  )
	public Double getFranchise1Commission() {
		return franchise1Commission;
	}

	public void setFranchise1Commission(Double franchise1Commission) {
		this.franchise1Commission = franchise1Commission;
	}

	@Column(name = "TO_FRANCHISE2"  )
	public Double getFranchise2Commission() {
		return franchise2Commission;
	}

	public void setFranchise2Commission(Double franchise2Commission) {
		this.franchise2Commission = franchise2Commission;
	}
	
	@Column(name = "SUPPLIER_ID"  )
	public Long getSupplierId() {
		return supplierId;
	}
	
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	 @Column(name = "CREATED_ON" )
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "TO_OTHER")
	public Double getOtherCommission() {
		return otherCommission;
	}

	public void setOtherCommission(Double otherCommission) {
		this.otherCommission = otherCommission;
	}

	@Column(name = "TO_SALES_TEAM")
	public Double getSalesTeamCommission() {
		return salesTeamCommission;
	}

	public void setSalesTeamCommission(Double salesTeamCommission) {
		this.salesTeamCommission = salesTeamCommission;
	}
	
	
}

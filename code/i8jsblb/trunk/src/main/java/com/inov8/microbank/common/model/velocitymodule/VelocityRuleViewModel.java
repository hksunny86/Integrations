package com.inov8.microbank.common.model.velocitymodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "VELOCITY_RULE_VIEW")
public class VelocityRuleViewModel extends BasePersistableModel {
   private Long velocityRuleId;
   private Long productId;
   private Long deviceTypeId;
   private Long segmentId;
   private Long distributorId;
   private Long agentType;
   private Long limitTypeId;
   private Long maxNoOfTransaction;
   private Long maxAmountOfTransaction;
   private String errorMessage;
   private Boolean isActive;

   private Date startDate;
   private Date endDate;
   private Long totalNoOfTransaction;
   private Long totalAmountOfTransaction;
   private String limitStatus;
   private Long customerAccountTypeId;
   private Long agentId;
   private Long customerId;
   private String cnic;


	public VelocityRuleViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getVelocityRuleId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setVelocityRuleId(primaryKey);
    }

   @Id 
   @Column(name = "VELOCITY_RULE_ID" , nullable = false )
   public Long getVelocityRuleId() {
      return velocityRuleId;
   }

   public void setVelocityRuleId(Long velocityRuleId) {
      this.velocityRuleId = velocityRuleId;
   }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getVelocityRuleId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&velocityRuleId=" + getVelocityRuleId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "velocityRuleId";
			return primaryKeyFieldName;				
    }


    @Column(name="PRODUCT_ID")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

    @Column(name="DEVICE_TYPE_ID")
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

    @Column(name="SEGMENT_ID")
    public Long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}

    @Column(name="DISTRIBUTOR_ID")
	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

    @Column(name="AGENT_TYPE")
	public Long getAgentType() {
		return agentType;
	}

	public void setAgentType(Long agentType) {
		this.agentType = agentType;
	}

    @Column(name="LIMIT_TYPE_ID")
	public Long getLimitTypeId() {
		return limitTypeId;
	}

	public void setLimitTypeId(Long limitTypeId) {
		this.limitTypeId = limitTypeId;
	}

    @Column(name="MAX_NO_OF_TRX")
	public Long getMaxNoOfTransaction() {
		return maxNoOfTransaction;
	}

	public void setMaxNoOfTransaction(Long maxNoOfTransaction) {
		this.maxNoOfTransaction = maxNoOfTransaction;
	}

    @Column(name="MAX_AMOUNT_OF_TRX")
	public Long getMaxAmountOfTransaction() {
		return maxAmountOfTransaction;
	}

	public void setMaxAmountOfTransaction(Long maxAmountOfTransaction) {
		this.maxAmountOfTransaction = maxAmountOfTransaction;
	}

    @Column(name="START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

    @Column(name="END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

    @Column(name="TOTAL_NO_OF_TRX")
	public Long getTotalNoOfTransaction() {
		return totalNoOfTransaction;
	}

	public void setTotalNoOfTransaction(Long totalNoOfTransaction) {
		this.totalNoOfTransaction = totalNoOfTransaction;
	}

    @Column(name="TOTAL_AMOUNT_OF_TRX")
	public Long getTotalAmountOfTransaction() {
		return totalAmountOfTransaction;
	}

	public void setTotalAmountOfTransaction(Long totalAmountOfTransaction) {
		this.totalAmountOfTransaction = totalAmountOfTransaction;
	}

    @Column(name="LIMIT_STATUS")
	public String getLimitStatus() {
		return limitStatus;
	}

	public void setLimitStatus(String limitStatus) {
		this.limitStatus = limitStatus;
	}

    @Column(name="ERROR_MESSAGE")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

    @Column(name="IS_ACTIVE")
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name="OLA_CUSTOMER_ACCOUNT_TYPE_ID")
	public Long getCustomerAccountTypeId() { return customerAccountTypeId; }

	public void setCustomerAccountTypeId(Long customerAccountTypeId) { this.customerAccountTypeId = customerAccountTypeId; }

	@Column(name="AGENT_ID")
	public Long getAgentId() { return agentId; }

	public void setAgentId(Long agentId) { this.agentId = agentId; }

	@Column(name="CUSTOMER_ID")
	public Long getCustomerId() { return customerId; }

	public void setCustomerId(Long customerId) { this.customerId = customerId; }

	@Column(name="CNIC")
	public String getCnic() { return cnic; }

	public void setCnic(String cnic) { this.cnic = cnic; }

}
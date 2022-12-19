package com.inov8.microbank.common.model.minitransactionmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.DateRangeHolderModel;

/**
 * The TransactionDetailListViewModel entity bean.
 *
 * @author  Muhammad Omar  Inov8 Limited
 *
 *
 */
@XmlRootElement(name="miniTransactionViewModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "MINI_TRANSACTION_VIEW")
public class MiniTransactionViewModel extends BasePersistableModel implements Serializable {
  
	private Long transactionId;
	private Long actionLogId;
	private Long appUserId;
	private Double bamt;
	private Double camt;
	private Long commandId;
	private String commandName;
	private Long miniTransactionId;
	private Long miniTransactionStateId;
	private String miniTransactionState;
	private String mobileNo;
	private String smsText;
	private Double tamt;
	private Date timeDate;
	private Double tpam;
	private Long transactionCodeId;
	private String transactionCode;
	private String agentUserId;
	private String agentMobileNo;
	private String customerMobileNo;
	private String customerCnic;
	private String customerUserId;
	private Long   transactionStatusId;
	private String transactionStatusName;
	private Long customerId;
	private Date createdOn;
	
	private String agentName;
	private String senderMobileNo;
	private String senderCNIC;
	private String recipientMobileNo;
	private String recipientCNIC;
	private String productName;
	
	private DateRangeHolderModel dateRangeHolderModel;

/**
    * Default constructor.
    */
   public MiniTransactionViewModel() {
   }
   public MiniTransactionViewModel(DateRangeHolderModel dateRangeHolderModel) {
	   this.dateRangeHolderModel = dateRangeHolderModel;
   }
   
   /**
    * Return the primary key.
    *
    * @return Long with the primary key.
    */
  @javax.persistence.Transient
  public Long getPrimaryKey() {
       return getMiniTransactionId();
   }

   /**
    * Set the primary key.
    *
    * @param primaryKey the primary key
    */
  @javax.persistence.Transient
  public void setPrimaryKey(Long primaryKey) {
      setMiniTransactionId(primaryKey);
   }

  /**
   * Returns the value of the <code>transactionId</code> property.
   *
   */
     @Column(name = "TRANSACTION_ID")
  public Long getTransactionId() {
     return transactionId;
  }

  /**
   * Sets the value of the <code>transactionId</code> property.
   *
   * @param transactionId the value for the <code>transactionId</code> property
   *    
		    */

  public void setTransactionId(Long transactionId) {
     this.transactionId = transactionId;
  }

  /**
   * Returns the value of the <code>transactionCodeId</code> property.
   *
   */
     @Column(name = "TRANSACTION_CODE_ID" , nullable = false )
  public Long getTransactionCodeId() {
     return transactionCodeId;
  }

  /**
   * Sets the value of the <code>transactionCodeId</code> property.
   *
   * @param transactionCodeId the value for the <code>transactionCodeId</code> property
   *    
   */

  public void setTransactionCodeId(Long transactionCodeId) {
     this.transactionCodeId = transactionCodeId;
  }

  /**
   * Returns the value of the <code>transactionCodeId</code> property.
   *
   */
     @Column(name = "TRANSACTION_CODE" , nullable = false )
  public String getTransactionCode() {
     return transactionCode;
  }

  /**
   * Sets the value of the <code>transactionCode</code> property.
   *
   * @param transactionCode the value for the <code>transactionCode</code> property
   *    
   */
  public void setTransactionCode(String transactionCode) {
     this.transactionCode = transactionCode;
  }

	@Column(name = "ACTION_LOG_ID", nullable = false)
	public Long getActionLogId() {
		return this.actionLogId;
	}

	public void setActionLogId(Long actionLogId) {
		this.actionLogId = actionLogId;
	}

	@Column(name = "APP_USER_ID", nullable = false)
	public Long getAppUserId() {
		return this.appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	@Column(name = "BAMT")
	public Double getBamt() {
		return this.bamt;
	}

	public void setBamt(Double bamt) {
		this.bamt = bamt;
	}

	@Column(name = "CAMT")
	public Double getCamt() {
		return this.camt;
	}

	public void setCamt(Double camt) {
		this.camt = camt;
	}

	@Column(name = "COMMAND_ID", nullable = false)
	public Long getCommandId() {
		return this.commandId;
	}

	public void setCommandId(Long commandId) {
		this.commandId = commandId;
	}

	@Column(name = "COMMAND_NAME")
	public String getCommandName() {
		return this.commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	@Column(name = "MINI_TRANSACTION_ID", nullable = false)
	@Id 
	public Long getMiniTransactionId() {
		return this.miniTransactionId;
	}

	public void setMiniTransactionId(Long miniTransactionId) {
		this.miniTransactionId = miniTransactionId;
	}

	@Column(name = "MINI_TRANSACTION_STATE_ID", nullable = false)
	public Long getMiniTransactionStateId() {
		return this.miniTransactionStateId;
	}

	public void setMiniTransactionStateId(Long miniTransactionStateId) {
		this.miniTransactionStateId = miniTransactionStateId;
	}

	@Column(name = "MINI_TRANSACTION_STATE", nullable = false, length = 50)
	public String getMiniTransactionState() {
		return this.miniTransactionState;
	}

	public void setMiniTransactionState(String miniTransactionState) {
		this.miniTransactionState = miniTransactionState;
	}

	@Column(name = "MOBILE_NO", nullable = false, length = 50)
	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name = "SMS_TEXT", length = 250)
	public String getSmsText() {
		return this.smsText;
	}

	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}

	@Column(name = "TAMT")
	public Double getTamt() {
		return this.tamt;
	}

	public void setTamt(Double tamt) {
		this.tamt = tamt;
	}

	@Column(name = "TIME_DATE")
	public Date getTimeDate() {
		return this.timeDate;
	}

	public void setTimeDate(Date timeDate) {
		this.timeDate = timeDate;
	}

	@Column(name = "TPAM")
	public Double getTpam() {
		return this.tpam;
	}

	public void setTpam(Double tpam) {
		this.tpam = tpam;
	}

	@Column(name = "AGENT_USER_ID", length = 50)
	public String getAgentUserId() {
		return this.agentUserId;
	}

	public void setAgentUserId(String agentUserId) {
		this.agentUserId = agentUserId;
	}

	@Column(name = "AGENT_MOBILE_NO", length = 50)
	public String getAgentMobileNo() {
		return this.agentMobileNo;
	}

	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}

	@Column(name = "CUSTOMER_MOBILE_NO", length = 13)
	public String getCustomerMobileNo() {
		return this.customerMobileNo;
	}

	public void setCustomerMobileNo(String customerMobileNo) {
		this.customerMobileNo = customerMobileNo;
	}

	@Column(name = "CUSTOMER_CNIC", length = 13)
	public String getCustomerCnic() {
		return customerCnic;
	}

	public void setCustomerCnic(String customerCnic) {
		this.customerCnic = customerCnic;
	}

	@Column(name = "CUSTOMER_USER_ID", length = 50)
	public String getCustomerUserId() {
		return this.customerUserId;
	}

	public void setCustomerUserId(String customerUserId) {
		this.customerUserId = customerUserId;
	}
	
	@Column(name = "SUP_PROCESSING_STATUS_ID")
	public Long getTransactionStatusId() {
		return transactionStatusId;
	}
	
	public void setTransactionStatusId(Long transactionStatusId) {
		this.transactionStatusId = transactionStatusId;
	}
	@Column(name = "PROCESSING_STATUS_NAME")
	public String getTransactionStatusName() {
		return transactionStatusName;
	}
	public void setTransactionStatusName(String transactionStatusName) {
		this.transactionStatusName = transactionStatusName;
	}     

	@Column(name = "CUSTOMER_ID")
	public Long getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&miniTransactionId=" + getMiniTransactionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "miniTransactionId";
			return primaryKeyFieldName;				
    }
	/**
	 * @return the dateRangeHolderModel
	 */
    @Transient
    public DateRangeHolderModel getDateRangeHolderModel() {
		return dateRangeHolderModel;
	}
	/**
	 * @param dateRangeHolderModel the dateRangeHolderModel to set
	 */
	public void setDateRangeHolderModel(DateRangeHolderModel dateRangeHolderModel) {
		this.dateRangeHolderModel = dateRangeHolderModel;
	}
	
	@Column(name="AGENT_NAME")
	public String getAgentName() {
		return agentName;
	}
	
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	@Column(name="SENDER_MOBILE_NO")
	public String getSenderMobileNo() {
		return senderMobileNo;
	}
	
	public void setSenderMobileNo(String senderMobileNo) {
		this.senderMobileNo = senderMobileNo;
	}
	
	@Column(name="SENDER_CNIC")
	public String getSenderCNIC() {
		return senderCNIC;
	}
	
	public void setSenderCNIC(String senderCNIC) {
		this.senderCNIC = senderCNIC;
	}
	
	@Column(name="RECIPIENT_MOBILE_NO")
	public String getRecipientMobileNo() {
		return recipientMobileNo;
	}
	
	public void setRecipientMobileNo(String recipientMobileNo) {
		this.recipientMobileNo = recipientMobileNo;
	}
	
	@Column(name="RECIPIENT_CNIC")
	public String getRecipientCNIC() {
		return recipientCNIC;
	}
	
	public void setRecipientCNIC(String recipientCNIC) {
		this.recipientCNIC = recipientCNIC;
	}
	
	@Column(name="PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

}

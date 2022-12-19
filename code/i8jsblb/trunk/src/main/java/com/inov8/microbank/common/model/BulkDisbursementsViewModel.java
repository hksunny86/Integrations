package com.inov8.microbank.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;


/**
 * HolidayModel entity.
 * @author Rizwan Munir
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BULK_DISBURSEMENTS_VIEW")
public class BulkDisbursementsViewModel extends BasePersistableModel {

	private static final long serialVersionUID = -1427106253293008955L;

	private Long bulkDisbursementsId;
	private String employeeNo;
	private String name;
	private Long appUserId;
	private String customerId;
	private Long nic;
	private String mobileNo;
	private Integer type;
	private String paymentType;
	private Long serviceId;
	private String serviceName;
	private Long productId;
	private String productName;
	private String accountCreationStatus;
	private String chequeNo;
	private Double amount;
	private Date paymentDate;
	private String settled;
	private Date settledOn;
	private String posted;
	private Date postedOn;
	private Long batchNumber;
	private Date createdOn;
	private Long createdBy;
	private String createdByName;
	private Date updatedOn;
	private Long updatedBy;
	private String updatedByName;
	private Boolean deleted;
	private String description;
	private Boolean limitApplicable;
	private Boolean payCashViaCnic;
	private String transactionCode;

	private Boolean isValidRecord;
	private Boolean biometricVerification;
	private String reason;
	private Date fileUploadedOn;

    //transient
    private Date paymentFromDate;
    private Date paymentToDate;
    private Date uploadFromDate;
    private Date uploadToDate;
    private Date postedOnToDate;
    private Date postedOnFromDate;
	private Date settledOnToDate;
	private Date settledOnFromDate;

    public BulkDisbursementsViewModel() {
    	
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
    	return getBulkDisbursementsId();
    }
    
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
    	setBulkDisbursementsId(primaryKey);
    }
    
    @Id
    @Column(name = "BULK_DISBURSEMENTS_ID")
    public Long getBulkDisbursementsId() {
    	return bulkDisbursementsId;
    }
    
    public void setBulkDisbursementsId(Long bulkDisbursementsId) {
    	this.bulkDisbursementsId = bulkDisbursementsId;
    }


    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
       String parameters = "";
       parameters += "&bulkDisbursementsId=" + getBulkDisbursementsId();
       return parameters;
    }
    
 	/**
      * Helper method for default Sorting on Primary Keys
      */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName(){ 
    	String primaryKeyFieldName = "bulkDisbursementsId";
    	return primaryKeyFieldName;
    }

    @Column(name = "EMPLOYEE_NO")
	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "APP_USER_ID")
	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	@Column(name = "CUSTOMER_ID")
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Column(name = "CNIC")
	public Long getNic() {
		return nic;
	}

	public void setNic(Long nic) {
		this.nic = nic;
	}

	@Column(name = "MOBILE_NO")
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Transient
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "SERVICE_ID" )
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	@Column(name = "SERVICE_NAME" )
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Column(name = "PRODUCT_ID" )
	   public Long getProductId() {
	      return productId;
	   }

	   public void setProductId(Long productId) {
	      this.productId = productId;
	   }

	   @Column(name = "PRODUCT_NAME" )
	   public String getProductName() {
	      return productName;
	   }

	   public void setProductName(String productName) {
	      this.productName = productName;
	   }
	
	@Column(name="ACCOUNT_CREATION_STATUS")
	public String getAccountCreationStatus() {
		return accountCreationStatus;
	}

	 public void setAccountCreationStatus(String status) {
		this.accountCreationStatus = status;
	}

	@Column(name = "CHEQUE_NO")
	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	@Column(name = "AMOUNT")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name = "PAYMENT_DATE")
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Column(name = "SETTLED")
	public String getSettled() {
		return settled;
	}

	public void setSettled(String settled) {
		this.settled = settled;
	}

	@Column(name = "SETTLED_ON")
	public Date getSettledOn() {
		return settledOn;
	}

	public void setSettledOn(Date settledOn) {
		this.settledOn = settledOn;
	}

	@Column(name = "POSTED")
	public String getPosted() {
		return posted;
	}

	public void setPosted(String posted) {
		this.posted = posted;
	}

	@Column(name = "POSTED_ON")
	public Date getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Column(name = "BATCH_NUMBER")
	public Long getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Long batchNumber) {
		this.batchNumber = batchNumber;
	}

	@Column(name = "CREATION_DATE")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "CREATED_BY")
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CREATED_BY_NAME")
	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "UPDATED_BY")
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "UPDATED_BY_NAME")
	public String getUpdatedByName() {
		return updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	@Column(name = "DELETED")
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "LIMIT_APPLICABLE")
	public Boolean getLimitApplicable() {
		return limitApplicable;
	}

	public void setLimitApplicable(Boolean limitApplicable) {
		this.limitApplicable = limitApplicable;
	}

	@Column(name = "PAY_CASH_VIA_CNIC")
	public Boolean getPayCashViaCnic() {
		return payCashViaCnic;
	}

	public void setPayCashViaCnic(Boolean payCashViaCnic) {
		this.payCashViaCnic = payCashViaCnic;
	}

	@Transient
	public String getPayCashMethod()
	{
		String text = "Transaction ID";
		if(payCashViaCnic != null && payCashViaCnic)
		{
			text = "CNIC";
		}
		return text;
	}

	@Transient
	public Date getPaymentFromDate() {
		return paymentFromDate;
	}

	public void setPaymentFromDate(Date paymentFromDate) {
		this.paymentFromDate = paymentFromDate;
	}

	@Transient
	public Date getPaymentToDate() {
		return paymentToDate;
	}

	public void setPaymentToDate(Date paymentToDate) {
		this.paymentToDate = paymentToDate;
	}

	@Transient
	public Date getUploadFromDate() {
		return uploadFromDate;
	}

	public void setUploadFromDate(Date uploadFromDate) {
		this.uploadFromDate = uploadFromDate;
	}

	@Transient
	public Date getUploadToDate() {
		return uploadToDate;
	}

	public void setUploadToDate(Date uploadToDate) {
		this.uploadToDate = uploadToDate;
	}

	@Transient
	public Date getPostedOnToDate() {
		return postedOnToDate;
	}

	public void setPostedOnToDate(Date postedOnToDate) {
		this.postedOnToDate = postedOnToDate;
	}

	@Transient
	public Date getPostedOnFromDate() {
		return postedOnFromDate;
	}

	public void setPostedOnFromDate(Date postedOnFromDate) {
		this.postedOnFromDate = postedOnFromDate;
	}

	@Transient
	public Date getSettledOnToDate() {
		return settledOnToDate;
	}

	public void setSettledOnToDate(Date settledOnToDate) {
		this.settledOnToDate = settledOnToDate;
	}

	@Transient
	public Date getSettledOnFromDate() {
		return settledOnFromDate;
	}

	public void setSettledOnFromDate(Date settledOnFromDate) {
		this.settledOnFromDate = settledOnFromDate;
	}
	
	@Column(name="TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Column(name="IS_VALID_RECORD")
	public Boolean getIsValidRecord() {
		return isValidRecord;
	}

	public void setIsValidRecord(Boolean isValidRecord) {
		this.isValidRecord = isValidRecord;
	}
	
	@Column(name="FAILURE_REASON")
	public String getReason() {
		return reason;
	}
	
	@Column(name = "IS_BIOMETRIC_REQUIRED" )
	public Boolean getBiometricVerification() {
		return biometricVerification;
	}

	public void setBiometricVerification(Boolean biometricVerification) {
		this.biometricVerification = biometricVerification;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Transient
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	@Column(name = "FILE_UPLOADED_ON" )
	public Date getFileUploadedOn() {
		return fileUploadedOn;
	}

	public void setFileUploadedOn(Date fileUploadedOn) {
		this.fileUploadedOn = fileUploadedOn;
	}
}
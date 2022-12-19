package com.inov8.microbank.disbursement.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BULK_PAYMENTS_VIEW")
public class BulkPaymentViewModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = 846825322815005316L;
	private Long pk;
	private String transactionCode;
    private String batchNumber;
	private String agent2Id;
    private Long serviceId;
    private String serviceName;
	private Long productId;
	private String productName;
	private Long paymentTypeId;
	private String paymentTypeName;
	private Double transactionAmount;
	private Double totalAmount;
	private String processingStatusName;
	private String processingStatusId;
	private String recipientMobileNo;
	private String recipientCnic;
	private Double akblCommission;
	private Double salesTeamCommission;
	private Double othersCommission;
	private Double fed;
	private Double wht;
	private Double inclusiveCharges;
   private Double exclusiveCharges;
	private Date createdOn;
	private Date updatedOn;
	
	private Date disStartDate;
	private Date disEndDate;
	private Date payStartDate;
	private Date payEndDate;
   

   public BulkPaymentViewModel() {
   }

   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPk();
    }

   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setPk(primaryKey);
    }

   @Id
   @Column(name = "PK")
   public Long getPk() {
	   return pk;
   }

   public void setPk(Long pk) {
	   this.pk = pk;
   }

   @Column(name = "RECIPIENT_MOBILE_NO")
   public String getRecipientMobileNo() {
      return recipientMobileNo;
   }

   public void setRecipientMobileNo(String recipientMobileNo) {
      this.recipientMobileNo = recipientMobileNo;
   }
   
   @Column(name = "TRANSACTION_CODE" , nullable = false , length=50 )
   public String getTransactionCode() {
      return transactionCode;
   }

   public void setTransactionCode(String transactionCode) {
      this.transactionCode = transactionCode;
   }

   @Column(name = "CREATED_ON" , nullable = false )
   public Date getCreatedOn() {
      return createdOn;
   }

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   @Column(name = "UPDATED_ON")
   public Date getUpdatedOn()
   {
       return updatedOn;
   }

   public void setUpdatedOn( Date updatedOn )
   {
       this.updatedOn = updatedOn;
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

   @Column(name = "PRODUCT_ID" , nullable = false )
   public Long getProductId() {
      return productId;
   }

   public void setProductId(Long productId) {
      this.productId = productId;
   }

   @Column(name = "PRODUCT_NAME" , nullable = false , length=50 )
   public String getProductName() {
      return productName;
   }

   public void setProductName(String productName) {
      this.productName = productName;
   }

   @Column(name = "PAYMENT_TYPE_ID")
   public Long getPaymentTypeId() {
	   return paymentTypeId;
   }

   public void setPaymentTypeId(Long paymentTypeId) {
	   this.paymentTypeId = paymentTypeId;
   }
		
   @Column(name = "PAYMENT_TYPE_NAME")
   public String getPaymentTypeName() {
	   return paymentTypeName;
   }
		
   public void setPaymentTypeName(String paymentTypeName) {
	   this.paymentTypeName = paymentTypeName;
   }

   @Column(name = "TRANSACTION_AMOUNT" , nullable = false )
   public Double getTransactionAmount() {
      return transactionAmount;
   }

   public void setTransactionAmount(Double transactionAmount) {
      this.transactionAmount = transactionAmount;
   }

   @Column(name = "PROCESSING_STATUS_NAME"  , length=50 )
   public String getProcessingStatusName() {
      return processingStatusName;
   }

   @Column(name = "AGENT2_ID", length = 50)
   public String getAgent2Id() {
	   return this.agent2Id;
   }

   public void setAgent2Id(String agent2Id) {
	   this.agent2Id = agent2Id;
   }


   public void setProcessingStatusName(String processingStatusName) {
      this.processingStatusName = processingStatusName;
   }

   @Column( name="SUP_PROCESSING_STATUS_ID" )
   public String getProcessingStatusId() {
	   return processingStatusId;
   }

   public void setProcessingStatusId(String processingStatusId) {
	   this.processingStatusId = processingStatusId;
   }

   @Column( name="BATCH_NUMBER" )
   public String getBatchNumber() {
      return batchNumber;
   }

   public void setBatchNumber(String batchNumber) {
      this.batchNumber = batchNumber;
   }


   @Column(name = "SERVICE_CHARGES_INCLUSIVE")
   public Double getInclusiveCharges() {
	   return inclusiveCharges;
   }

   public void setInclusiveCharges(Double inclusiveCharges) {
	   this.inclusiveCharges = inclusiveCharges;
   }

   @Column(name = "SERVICE_CHARGES_EXCLUSIVE")
   public Double getExclusiveCharges() {
      return exclusiveCharges;
   }

   public void setExclusiveCharges(Double exclusiveCharges) {
      this.exclusiveCharges = exclusiveCharges;
   }

   @Column(name = "TOTAL_AMOUNT" , nullable = false )
   public Double getTotalAmount() {
      return totalAmount;
   }

   public void setTotalAmount(Double totalAmount) {
      this.totalAmount = totalAmount;
   }

   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&transactionCode=" + getTransactionCode();
      return parameters;
   }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
    	String primaryKeyFieldName = "transactionCode";
		return primaryKeyFieldName;				
    }

    @Column(name = "FED"  )
    public Double getFed() {
		return fed;
	}

    public void setFed(Double fed) {
		this.fed = fed;
	}

    @Column(name = "WHT"  )
	public Double getWht() {
		return wht;
	}

	public void setWht(Double wht) {
		this.wht = wht;
	}
    
	@Column(name = "TO_BANK"  )
	public Double getAkblCommission() {
		return akblCommission;
	}

	public void setAkblCommission(Double akblCommission) {
		this.akblCommission = akblCommission;
	}

    @Column( name = "RECIPIENT_CNIC" )
    public String getRecipientCnic()
    {
        return recipientCnic;
    }

    public void setRecipientCnic( String recipientCnic )
    {
        this.recipientCnic = recipientCnic;
    }

	@javax.persistence.Transient
	public Date getDisStartDate() {
		return disStartDate;
	}

	public void setDisStartDate(Date disStartDate) {
		this.disStartDate = disStartDate;
	}

	@javax.persistence.Transient
	public Date getDisEndDate() {
		return disEndDate;
	}

	public void setDisEndDate(Date disEndDate) {
		this.disEndDate = disEndDate;
	}

	@javax.persistence.Transient
	public Date getPayStartDate() {
		return payStartDate;
	}

	public void setPayStartDate(Date payStartDate) {
		this.payStartDate = payStartDate;
	}

	@javax.persistence.Transient
	public Date getPayEndDate() {
		return payEndDate;
	}

	public void setPayEndDate(Date payEndDate) {
		this.payEndDate = payEndDate;
	}
	 @Column( name = "TO_SALES_TEAM" )
    public Double getSalesTeamCommission() {
		return salesTeamCommission;
	}
 
    public void setSalesTeamCommission(Double salesTeamCommission) {
		this.salesTeamCommission = salesTeamCommission;
	}

    @Column( name = "OTHERS" )
    public Double getOthersCommission() {
		return othersCommission;
	}

	public void setOthersCommission(Double othersCommission) {
		this.othersCommission = othersCommission;
	}

}

package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SETTLEMENT_TRANSACTION_VIEW")
public class SettlementTransactionViewModel extends BasePersistableModel implements Serializable {
	
	private static final long serialVersionUID = -6165744087776103358L;

	private Long pk;
	private Long settlementTransactionId;
	private String transactionId;
	private Long productId;
	private String productName;
	private String accountNo;
	private String accountTitle;	
	private Double debitAmount;
	private Double creditAmount;
	private Long status;
    private Date createdOn;
    private Date startDate;
    private Date endDate;
    private String statusName;
    private Long supplierId;
	
	

	/**
    * Default constructor.
    */
   public SettlementTransactionViewModel() {
   }   

   /**
    * Return the primary key.
    *
    * @return Long with the primary key.
    */
  @javax.persistence.Transient
  public Long getPrimaryKey() {
       return getPk();
   }

   /**
    * Set the primary key.
    *
    * @param primaryKey the primary key
    */
  @javax.persistence.Transient
  public void setPrimaryKey(Long primaryKey) {
      setPk(primaryKey);
   }

  /**
   * Returns the value of the <code>pk</code> property.
   *
   */
     @Column(name = "PK"  )
  @Id 
  public Long getPk() {
     return pk;
  }

  /**
   * Sets the value of the <code>pk</code> property.
   *
   * @param pk the value for the <code>pk</code> property
   *    
		    */

  public void setPk(Long pk) {
     this.pk = pk;
  }
    
    @Column(name = "SETTLEMENT_TRANSACTION_ID")
	public Long getSettlementTransactionId() {
		return settlementTransactionId;
	}

	public void setSettlementTransactionId(Long settlementTransactionId) {
		this.settlementTransactionId = settlementTransactionId;
	}

    @Column(name = "TRANSACTION_ID")
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

    @Column(name = "PRODUCT_ID")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	 @Column(name = "ACCOUNT_NO")
    public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	 @Column(name = "ACCOUNT_TITLE")
	public String getAccountTitle() {
		return accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}
	 @Column(name = "DEBIT_AMOUNT")
	public Double getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}
	 @Column(name = "CREDIT_AMOUNT")
	public Double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	@Column(name = "STATUS")
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

    @Column(name="TRANSACTION_DATE")
    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Transient
	public String getStatusName() {
		if(status == null || status == 0){
			statusName="No";
		}else{
			statusName="Yes";
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Transient
	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	
	@Column(name="PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	 /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPk();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&pk=" + getPk();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "pk";
			return primaryKeyFieldName;				
    }       

}

package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SETTLEMENT_TRANS_DET_VIEW")
public class SettlementTransactionDetailViewModel extends BasePersistableModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4631983681808390680L;
	private Long	settlementTransactionDetailId;
	private Date 	settlementDate;
	private String 	oracleNumber;
	private String	accountTitle;
	private Double 	creditMovemement;
	private Double	debitMovement;
	private String	productName;
	
	private Date startDate;
	private	Date endDate;

	/**
	 * Added by atif hussain
	 */
	private Long accountTypeId;
	private String accountTypeName;
	private Long productId;
	private String prodName;
	
	public SettlementTransactionDetailViewModel(){
		
	}
	
	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSettlementTransactionDetailId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSettlementTransactionDetailId(primaryKey);
    }

   /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getSettlementTransactionDetailId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&settlementTransactionDetailId=" + getSettlementTransactionDetailId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "settlementTransactionDetailId";
			return primaryKeyFieldName;				
    }

    @Id
	@Column(name="SETTLEMENT_TRANSACTION_DET_ID")
	public Long getSettlementTransactionDetailId() {
		return settlementTransactionDetailId;
	}
	public void setSettlementTransactionDetailId(Long settlementTransactionDetailId) {
		this.settlementTransactionDetailId = settlementTransactionDetailId;
	}
	
	@Column(name="SETTLEMENT_DATE")
	public Date getSettlementDate() {
		return settlementDate;
	}
	
	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}
	
	@Column(name="ORACLE_NUMBER")
	public String getOracleNumber() {
		return oracleNumber;
	}
	
	public void setOracleNumber(String oracleNumber) {
		this.oracleNumber = oracleNumber;
	}
	
	@Column(name="ACCOUNT_TITLE")
	public String getAccountTitle() {
		return accountTitle;
	}
	
	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}
	
	@Column(name="CREDIT")
	public Double getCreditMovemement() {
		if(creditMovemement != null){
			DecimalFormat df=new DecimalFormat("0.00");
			String formate = df.format(creditMovemement);
			creditMovemement = Double.parseDouble(formate) ; 
		}
		return creditMovemement;
	}
	
	public void setCreditMovemement(Double creditMovemement) {
		this.creditMovemement = creditMovemement;
	}
	
	@Column(name="DEBIT")
	public Double getDebitMovement() {
		if(debitMovement != null){
			DecimalFormat df=new DecimalFormat("0.00");
			String formate = df.format(debitMovement);
			debitMovement = Double.parseDouble(formate) ; 
		}
		return debitMovement;
	}
	
	public void setDebitMovement(Double debitMovement) {
		this.debitMovement = debitMovement;
	}
	
	@Column(name="PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
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
	


	@Column( name = "CUSTOMER_ACCOUNT_TYPE_ID" )
	public Long getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	@Column( name = "CUSTOMER_ACCOUNT_TYPE_NAME" )
	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	@Column( name = "PRODUCT_ID" )
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	@Column( name = "PROD_NAME" )
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
}

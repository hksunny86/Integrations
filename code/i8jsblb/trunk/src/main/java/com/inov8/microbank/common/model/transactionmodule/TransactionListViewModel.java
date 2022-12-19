package com.inov8.microbank.common.model.transactionmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The TransactionListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="TransactionListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TRANSACTION_LIST_VIEW")
public class TransactionListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -173489726995636761L;
private String tranCode;
   private Long transactionId;
   private Double transactionAmount;
   private String fromDistContactMobNo;
   private String fromRetContactMobNo;
   private String customerMobileNo;
   private String productName;
   private Date tranDate;

   /**
    * Default constructor.
    */
   public TransactionListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setTransactionId(primaryKey);
    }

   /**
    * Returns the value of the <code>tranCode</code> property.
    *
    */
      @Column(name = "TRAN_CODE" , nullable = false , length=30 )
   public String getTranCode() {
      return tranCode;
   }

   /**
    * Sets the value of the <code>tranCode</code> property.
    *
    * @param tranCode the value for the <code>tranCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="30"
    */

   public void setTranCode(String tranCode) {
      this.tranCode = tranCode;
   }

   /**
    * Returns the value of the <code>transactionId</code> property.
    *
    */
      @Column(name = "TRANSACTION_ID" , nullable = false )
   @Id 
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
    * Returns the value of the <code>transactionAmount</code> property.
    *
    */
      @Column(name = "TRANSACTION_AMOUNT" , nullable = false )
   public Double getTransactionAmount() {
      return transactionAmount;
   }

   /**
    * Sets the value of the <code>transactionAmount</code> property.
    *
    * @param transactionAmount the value for the <code>transactionAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTransactionAmount(Double transactionAmount) {
      this.transactionAmount = transactionAmount;
   }

   /**
    * Returns the value of the <code>fromDistContactMobNo</code> property.
    *
    */
      @Column(name = "FROM_DIST_CONTACT_MOB_NO"  , length=13 )
   public String getFromDistContactMobNo() {
      return fromDistContactMobNo;
   }

   /**
    * Sets the value of the <code>fromDistContactMobNo</code> property.
    *
    * @param fromDistContactMobNo the value for the <code>fromDistContactMobNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="13"
    */

   public void setFromDistContactMobNo(String fromDistContactMobNo) {
      this.fromDistContactMobNo = fromDistContactMobNo;
   }

   /**
    * Returns the value of the <code>fromRetContactMobNo</code> property.
    *
    */
      @Column(name = "FROM_RET_CONTACT_MOB_NO"  , length=13 )
   public String getFromRetContactMobNo() {
      return fromRetContactMobNo;
   }

   /**
    * Sets the value of the <code>fromRetContactMobNo</code> property.
    *
    * @param fromRetContactMobNo the value for the <code>fromRetContactMobNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="13"
    */

   public void setFromRetContactMobNo(String fromRetContactMobNo) {
      this.fromRetContactMobNo = fromRetContactMobNo;
   }

   /**
    * Returns the value of the <code>customerMobileNo</code> property.
    *
    */
      @Column(name = "CUSTOMER_MOBILE_NO"  , length=13 )
   public String getCustomerMobileNo() {
      return customerMobileNo;
   }

   /**
    * Sets the value of the <code>customerMobileNo</code> property.
    *
    * @param customerMobileNo the value for the <code>customerMobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="13"
    */

   public void setCustomerMobileNo(String customerMobileNo) {
      this.customerMobileNo = customerMobileNo;
   }

   /**
    * Returns the value of the <code>productName</code> property.
    *
    */
      @Column(name = "PRODUCT_NAME" , nullable = false , length=50 )
   public String getProductName() {
      return productName;
   }

   /**
    * Sets the value of the <code>productName</code> property.
    *
    * @param productName the value for the <code>productName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setProductName(String productName) {
      this.productName = productName;
   }

   /**
    * Returns the value of the <code>tranDate</code> property.
    *
    */
      @Column(name = "TRAN_DATE" , nullable = false )
   public Date getTranDate() {
      return tranDate;
   }

   /**
    * Sets the value of the <code>tranDate</code> property.
    *
    * @param tranDate the value for the <code>tranDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setTranDate(Date tranDate) {
      this.tranDate = tranDate;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getTransactionId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&transactionId=" + getTransactionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "transactionId";
			return primaryKeyFieldName;				
    }       
}

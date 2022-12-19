package com.inov8.microbank.common.model.productmodule.paymentservice;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The LescoLogModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="LescoLogModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="LESCO_LOG_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="LESCO_LOG_seq") } )
//@javax.persistence.SequenceGenerator(name = "LESCO_LOG_seq",sequenceName = "LESCO_LOG_seq")
@Table(name = "LESCO_LOG")
public class LescoLogModel extends BasePersistableModel implements Serializable{
  



   private Long lescoLogId;
   private String companyCode;
   private String companyName;
   private String customerName;
   private String customerAddress;
   private String billingMonth;
   private String mfsId;
   private String consumerNo;
   private String phoneNo;
   private String microbankTxCode;
   private Long paidAmount;
   private Long billAmount;
   private Long lateBillAmount;
   private Date dueDate;
   private Date paidDate;

   /**
    * Default constructor.
    */
   public LescoLogModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getLescoLogId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setLescoLogId(primaryKey);
    }

   /**
    * Returns the value of the <code>lescoLogId</code> property.
    *
    */
      @Column(name = "LESCO_LOG_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LESCO_LOG_seq")
   public Long getLescoLogId() {
      return lescoLogId;
   }

   /**
    * Sets the value of the <code>lescoLogId</code> property.
    *
    * @param lescoLogId the value for the <code>lescoLogId</code> property
    *    
		    */

   public void setLescoLogId(Long lescoLogId) {
      this.lescoLogId = lescoLogId;
   }

   /**
    * Returns the value of the <code>companyCode</code> property.
    *
    */
      @Column(name = "COMPANY_CODE"  , length=50 )
   public String getCompanyCode() {
      return companyCode;
   }

   /**
    * Sets the value of the <code>companyCode</code> property.
    *
    * @param companyCode the value for the <code>companyCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCompanyCode(String companyCode) {
      this.companyCode = companyCode;
   }

   /**
    * Returns the value of the <code>companyName</code> property.
    *
    */
      @Column(name = "COMPANY_NAME"  , length=50 )
   public String getCompanyName() {
      return companyName;
   }

   /**
    * Sets the value of the <code>companyName</code> property.
    *
    * @param companyName the value for the <code>companyName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setCompanyName(String companyName) {
      this.companyName = companyName;
   }

   /**
    * Returns the value of the <code>customerName</code> property.
    *
    */
      @Column(name = "CUSTOMER_NAME"  , length=50 )
   public String getCustomerName() {
      return customerName;
   }

   /**
    * Sets the value of the <code>customerName</code> property.
    *
    * @param customerName the value for the <code>customerName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setCustomerName(String customerName) {
      this.customerName = customerName;
   }

   /**
    * Returns the value of the <code>customerAddress</code> property.
    *
    */
      @Column(name = "CUSTOMER_ADDRESS"  , length=70 )
   public String getCustomerAddress() {
      return customerAddress;
   }

   /**
    * Sets the value of the <code>customerAddress</code> property.
    *
    * @param customerAddress the value for the <code>customerAddress</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="70"
    */

   public void setCustomerAddress(String customerAddress) {
      this.customerAddress = customerAddress;
   }

   /**
    * Returns the value of the <code>billingMonth</code> property.
    *
    */
      @Column(name = "BILLING_MONTH"  , length=20 )
   public String getBillingMonth() {
      return billingMonth;
   }

   /**
    * Sets the value of the <code>billingMonth</code> property.
    *
    * @param billingMonth the value for the <code>billingMonth</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="20"
    */

   public void setBillingMonth(String billingMonth) {
      this.billingMonth = billingMonth;
   }

   /**
    * Returns the value of the <code>mfsId</code> property.
    *
    */
      @Column(name = "MFS_ID"  , length=50 )
   public String getMfsId() {
      return mfsId;
   }

   /**
    * Sets the value of the <code>mfsId</code> property.
    *
    * @param mfsId the value for the <code>mfsId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMfsId(String mfsId) {
      this.mfsId = mfsId;
   }

   /**
    * Returns the value of the <code>consumerNo</code> property.
    *
    */
      @Column(name = "CONSUMER_NO"  , length=50 )
   public String getConsumerNo() {
      return consumerNo;
   }

   /**
    * Sets the value of the <code>consumerNo</code> property.
    *
    * @param consumerNo the value for the <code>consumerNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setConsumerNo(String consumerNo) {
      this.consumerNo = consumerNo;
   }

   /**
    * Returns the value of the <code>phoneNo</code> property.
    *
    */
      @Column(name = "PHONE_NO"  , length=50 )
   public String getPhoneNo() {
      return phoneNo;
   }

   /**
    * Sets the value of the <code>phoneNo</code> property.
    *
    * @param phoneNo the value for the <code>phoneNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPhoneNo(String phoneNo) {
      this.phoneNo = phoneNo;
   }

   /**
    * Returns the value of the <code>microbankTxCode</code> property.
    *
    */
      @Column(name = "MICROBANK_TX_CODE"  , length=50 )
   public String getMicrobankTxCode() {
      return microbankTxCode;
   }

   /**
    * Sets the value of the <code>microbankTxCode</code> property.
    *
    * @param microbankTxCode the value for the <code>microbankTxCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMicrobankTxCode(String microbankTxCode) {
      this.microbankTxCode = microbankTxCode;
   }

   /**
    * Returns the value of the <code>paidAmount</code> property.
    *
    */
      @Column(name = "PAID_AMOUNT"  )
   public Long getPaidAmount() {
      return paidAmount;
   }

   /**
    * Sets the value of the <code>paidAmount</code> property.
    *
    * @param paidAmount the value for the <code>paidAmount</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setPaidAmount(Long paidAmount) {
      this.paidAmount = paidAmount;
   }

   /**
    * Returns the value of the <code>billAmount</code> property.
    *
    */
      @Column(name = "BILL_AMOUNT"  )
   public Long getBillAmount() {
      return billAmount;
   }

   /**
    * Sets the value of the <code>billAmount</code> property.
    *
    * @param billAmount the value for the <code>billAmount</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBillAmount(Long billAmount) {
      this.billAmount = billAmount;
   }

   /**
    * Returns the value of the <code>lateBillAmount</code> property.
    *
    */
      @Column(name = "LATE_BILL_AMOUNT"  )
   public Long getLateBillAmount() {
      return lateBillAmount;
   }

   /**
    * Sets the value of the <code>lateBillAmount</code> property.
    *
    * @param lateBillAmount the value for the <code>lateBillAmount</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setLateBillAmount(Long lateBillAmount) {
      this.lateBillAmount = lateBillAmount;
   }

   /**
    * Returns the value of the <code>dueDate</code> property.
    *
    */
      @Column(name = "DUE_DATE"  )
   public Date getDueDate() {
      return dueDate;
   }

   /**
    * Sets the value of the <code>dueDate</code> property.
    *
    * @param dueDate the value for the <code>dueDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setDueDate(Date dueDate) {
      this.dueDate = dueDate;
   }

   /**
    * Returns the value of the <code>paidDate</code> property.
    *
    */
      @Column(name = "PAID_DATE"  )
   public Date getPaidDate() {
      return paidDate;
   }

   /**
    * Sets the value of the <code>paidDate</code> property.
    *
    * @param paidDate the value for the <code>paidDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setPaidDate(Date paidDate) {
      this.paidDate = paidDate;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getLescoLogId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&lescoLogId=" + getLescoLogId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "lescoLogId";
			return primaryKeyFieldName;				
    }
    
    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	    	
    	return associationModelList;
    }    
          
}

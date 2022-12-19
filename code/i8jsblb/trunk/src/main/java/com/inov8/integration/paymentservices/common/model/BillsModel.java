package com.inov8.integration.paymentservices.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BaseEntity;


/**
 * The BillsModel entity bean.
 *
 * @author    Finalist IT Group
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "KASBUTILS.BILLS")
public class BillsModel implements BaseEntity {
  



   private String utilityCompanyCode;
   private String companyName;
   private String consumerNumber;
   private String billDate;
   private Long billAmount;
   private Long billAmtAftDueDate;
   private String billDueDate;
   private Double billPaid;

   /**
    * Default constructor.
    */
   public BillsModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return String with the primary key.
     */
   @javax.persistence.Transient
   public String getPrimaryKey() {
        return getUtilityCompanyCode();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(String primaryKey) {
       setUtilityCompanyCode(primaryKey);
    }

   /**
    * Returns the value of the <code>utilityCompanyCode</code> property.
    *
    */
      @Column(name = "UTILITY_COMPANY_CODE" , nullable = false , length=50 )
   @Id 
   public String getUtilityCompanyCode() {
      return utilityCompanyCode;
   }

   /**
    * Sets the value of the <code>utilityCompanyCode</code> property.
    *
    * @param utilityCompanyCode the value for the <code>utilityCompanyCode</code> property
    *    
		    */

   public void setUtilityCompanyCode(String utilityCompanyCode) {
      this.utilityCompanyCode = utilityCompanyCode;
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
    * Returns the value of the <code>consumerNumber</code> property.
    *
    */
      @Column(name = "CONSUMER_NUMBER" , nullable = false , length=30 )
   public String getConsumerNumber() {
      return consumerNumber;
   }

   /**
    * Sets the value of the <code>consumerNumber</code> property.
    *
    * @param consumerNumber the value for the <code>consumerNumber</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="30"
    */

   public void setConsumerNumber(String consumerNumber) {
      this.consumerNumber = consumerNumber;
   }

   /**
    * Returns the value of the <code>billDate</code> property.
    *
    */
      @Column(name = "BILL_DATE" , nullable = false , length=20 )
   public String getBillDate() {
      return billDate;
   }

   /**
    * Sets the value of the <code>billDate</code> property.
    *
    * @param billDate the value for the <code>billDate</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="20"
    */

   public void setBillDate(String billDate) {
      this.billDate = billDate;
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
    * Returns the value of the <code>billAmtAftDueDate</code> property.
    *
    */
      @Column(name = "BILL_AMT_AFT_DUE_DATE"  )
   public Long getBillAmtAftDueDate() {
      return billAmtAftDueDate;
   }

   /**
    * Sets the value of the <code>billAmtAftDueDate</code> property.
    *
    * @param billAmtAftDueDate the value for the <code>billAmtAftDueDate</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBillAmtAftDueDate(Long billAmtAftDueDate) {
      this.billAmtAftDueDate = billAmtAftDueDate;
   }

   /**
    * Returns the value of the <code>billDueDate</code> property.
    *
    */
      @Column(name = "BILL_DUE_DATE"  , length=20 )
   public String getBillDueDate() {
      return billDueDate;
   }

   /**
    * Sets the value of the <code>billDueDate</code> property.
    *
    * @param billDueDate the value for the <code>billDueDate</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="20"
    */

   public void setBillDueDate(String billDueDate) {
      this.billDueDate = billDueDate;
   }

   /**
    * Returns the value of the <code>billPaid</code> property.
    *
    */
      @Column(name = "BILL_PAID"  )
   public Double getBillPaid() {
      return billPaid;
   }

   /**
    * Sets the value of the <code>billPaid</code> property.
    *
    * @param billPaid the value for the <code>billPaid</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setBillPaid(Double billPaid) {
      this.billPaid = billPaid;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getUtilityCompanyCode();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&utilityCompanyCode=" + getUtilityCompanyCode();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "utilityCompanyCode";
			return primaryKeyFieldName;				
    }       
}

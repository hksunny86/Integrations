package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CommissionRatesListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommissionRatesListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "COMMISSION_RATES_LIST_VIEW")
public class CommissionRatesListViewModel extends BasePersistableModel implements Serializable {
  



   private Long allpayCommissionRateId;
   private String nationalDistributor;
   private String distributor;
   private String retailer;
   private Date fromDate;
   private Date todate;
   private String reasonName;
   private Double nationalDistributorRate;
   private Double distributorRate;
   private Double retailerRate;
   private String productName;
   private Boolean active;
   /**
    * Default constructor.
    */
   public CommissionRatesListViewModel() {
   }   
   @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAllpayCommissionRateId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAllpayCommissionRateId(primaryKey);
    }

   /**
    * Returns the value of the <code>allpayCommissionRateId</code> property.
    *
    */
      @Column(name = "ALLPAY_COMMISSION_RATE_ID" , nullable = false )
   @Id 
   public Long getAllpayCommissionRateId() {
      return allpayCommissionRateId;
   }

   /**
    * Sets the value of the <code>allpayCommissionRateId</code> property.
    *
    * @param allpayCommissionRateId the value for the <code>allpayCommissionRateId</code> property
    *    
		    */

   public void setAllpayCommissionRateId(Long allpayCommissionRateId) {
      this.allpayCommissionRateId = allpayCommissionRateId;
   }

   /**
    * Returns the value of the <code>nationalDistributor</code> property.
    *
    */
      @Column(name = "NATIONAL_DISTRIBUTOR"  , length=50 )
   public String getNationalDistributor() {
      return nationalDistributor;
   }

   /**
    * Sets the value of the <code>nationalDistributor</code> property.
    *
    * @param nationalDistributor the value for the <code>nationalDistributor</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setNationalDistributor(String nationalDistributor) {
      this.nationalDistributor = nationalDistributor;
   }

   /**
    * Returns the value of the <code>distributor</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR"  , length=50 )
   public String getDistributor() {
      return distributor;
   }

   /**
    * Sets the value of the <code>distributor</code> property.
    *
    * @param distributor the value for the <code>distributor</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setDistributor(String distributor) {
      this.distributor = distributor;
   }

   /**
    * Returns the value of the <code>retailer</code> property.
    *
    */
      @Column(name = "RETAILER"  , length=50 )
   public String getRetailer() {
      return retailer;
   }

   /**
    * Sets the value of the <code>retailer</code> property.
    *
    * @param retailer the value for the <code>retailer</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setRetailer(String retailer) {
      this.retailer = retailer;
   }

   /**
    * Returns the value of the <code>fromDate</code> property.
    *
    */
      @Column(name = "FROM_DATE" , nullable = false )
   public Date getFromDate() {
      return fromDate;
   }

   /**
    * Sets the value of the <code>fromDate</code> property.
    *
    * @param fromDate the value for the <code>fromDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setFromDate(Date fromDate) {
      this.fromDate = fromDate;
   }

   /**
    * Returns the value of the <code>todate</code> property.
    *
    */
      @Column(name = "TODATE"  )
   public Date getTodate() {
      return todate;
   }

   /**
    * Sets the value of the <code>todate</code> property.
    *
    * @param todate the value for the <code>todate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setTodate(Date todate) {
      this.todate = todate;
   }

   /**
    * Returns the value of the <code>reasonName</code> property.
    *
    */
      @Column(name = "REASON_NAME" , nullable = false , length=50 )
   public String getReasonName() {
      return reasonName;
   }

   /**
    * Sets the value of the <code>reasonName</code> property.
    *
    * @param reasonName the value for the <code>reasonName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setReasonName(String reasonName) {
      this.reasonName = reasonName;
   }
   @Column(name = "PRODUCT_NAME" , nullable = false , length=50 )
   public String getProductName() {
      return productName;
   }


   public void setProductName(String productName) {
      this.productName = productName;
   }

   /**
    * Returns the value of the <code>nationalDistributorRate</code> property.
    *
    */
      @Column(name = "NATIONAL_DISTRIBUTOR_RATE" , nullable = false )
   public Double getNationalDistributorRate() {
      return nationalDistributorRate;
   }

   /**
    * Sets the value of the <code>nationalDistributorRate</code> property.
    *
    * @param nationalDistributorRate the value for the <code>nationalDistributorRate</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setNationalDistributorRate(Double nationalDistributorRate) {
      this.nationalDistributorRate = nationalDistributorRate;
   }

   /**
    * Returns the value of the <code>distributorRate</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_RATE" , nullable = false )
   public Double getDistributorRate() {
      return distributorRate;
   }

   /**
    * Sets the value of the <code>distributorRate</code> property.
    *
    * @param distributorRate the value for the <code>distributorRate</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setDistributorRate(Double distributorRate) {
      this.distributorRate = distributorRate;
   }

   /**
    * Returns the value of the <code>retailerRate</code> property.
    *
    */
      @Column(name = "RETAILER_RATE" , nullable = false )
   public Double getRetailerRate() {
      return retailerRate;
   }

   /**
    * Sets the value of the <code>retailerRate</code> property.
    *
    * @param retailerRate the value for the <code>retailerRate</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setRetailerRate(Double retailerRate) {
      this.retailerRate = retailerRate;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAllpayCommissionRateId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&allpayCommissionRateId=" + getAllpayCommissionRateId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "allpayCommissionRateId";
			return primaryKeyFieldName;				
    }       
}

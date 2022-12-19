package com.inov8.microbank.common.model.commissionmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CommissionRateListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommissionRateListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "COMMISSION_RATE_LIST_VIEW")
public class CommissionRateListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 1715412555832334394L;
private Long commissionRateId;
   private Long commissionTypeId;
   private String commissionReasonName;
   private Long commissionStakeholderId;
   //private Long transactionTypeId;
   private Long productId;
   private String commissionTypeName;
   private String productName;
   private String commissionStakeholderName;
   //private String transactionTypeName;
   private Date fromDate;
   private Date toDate;
   private Double rangeStarts;
   private Double rangeEnds;
   private Double rate;
   private Boolean active;
   private Long segmentId;
   private String segmentName;

   /**
    * Default constructor.
    */
   public CommissionRateListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCommissionRateId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCommissionRateId(primaryKey);
    }

   /**
    * Returns the value of the <code>commissionRateId</code> property.
    *
    */
      @Column(name = "COMMISSION_RATE_ID" , nullable = false )
   @Id 
   public Long getCommissionRateId() {
      return commissionRateId;
   }

   /**
    * Sets the value of the <code>commissionRateId</code> property.
    *
    * @param commissionRateId the value for the <code>commissionRateId</code> property
    *    
		    */

   public void setCommissionRateId(Long commissionRateId) {
      this.commissionRateId = commissionRateId;
   }
   /**
    * Returns the value of the <code>commissionReasonName</code> property.
    *
    */
      @Column(name = "COMMISSION_REASON_NAME" , nullable = false , length=50 )
   public String getCommissionReasonName() {
      return commissionReasonName;
   }

   /**
    * Sets the value of the <code>commissionReasonName</code> property.
    *
    * @param commissionReasonName the value for the <code>commissionReasonName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setCommissionReasonName(String commissionReasonName) {
      this.commissionReasonName = commissionReasonName;
   }
   /**
    * Returns the value of the <code>commissionTypeId</code> property.
    *
    */
      @Column(name = "COMMISSION_TYPE_ID" , nullable = false )
   public Long getCommissionTypeId() {
      return commissionTypeId;
   }

   /**
    * Sets the value of the <code>commissionTypeId</code> property.
    *
    * @param commissionTypeId the value for the <code>commissionTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCommissionTypeId(Long commissionTypeId) {
      this.commissionTypeId = commissionTypeId;
   }

   /**
    * Returns the value of the <code>commissionStakeholderId</code> property.
    *
    */
      @Column(name = "COMMISSION_STAKEHOLDER_ID" , nullable = false )
   public Long getCommissionStakeholderId() {
      return commissionStakeholderId;
   }

   /**
    * Sets the value of the <code>commissionStakeholderId</code> property.
    *
    * @param commissionStakeholderId the value for the <code>commissionStakeholderId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCommissionStakeholderId(Long commissionStakeholderId) {
      this.commissionStakeholderId = commissionStakeholderId;
   }

   /**
    * Returns the value of the <code>transactionTypeId</code> property.
    *
    */
     // @Column(name = "TRANSACTION_TYPE_ID" , nullable = false )
//   public Long getTransactionTypeId() {
//      return transactionTypeId;
//   }

   /**
    * Sets the value of the <code>transactionTypeId</code> property.
    *
    * @param transactionTypeId the value for the <code>transactionTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

//   public void setTransactionTypeId(Long transactionTypeId) {
//      this.transactionTypeId = transactionTypeId;
//   }

   /**
    * Returns the value of the <code>productId</code> property.
    *
    */
//      @Column(name = "PRODUCT_ID" , nullable = false )
//   public Long getProductId() {
//      return productId;
//   }

   /**
    * Sets the value of the <code>productId</code> property.
    *
    * @param productId the value for the <code>productId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setProductId(Long productId) {
      this.productId = productId;
   }

   /**
    * Returns the value of the <code>commissionTypeName</code> property.
    *
    */
      @Column(name = "COMMISSION_TYPE_NAME" , nullable = false , length=50 )
   public String getCommissionTypeName() {
      return commissionTypeName;
   }

   /**
    * Sets the value of the <code>commissionTypeName</code> property.
    *
    * @param commissionTypeName the value for the <code>commissionTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setCommissionTypeName(String commissionTypeName) {
      this.commissionTypeName = commissionTypeName;
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
    * Returns the value of the <code>commissionStakeholderName</code> property.
    *
    */
      @Column(name = "COMMISSION_STAKEHOLDER_NAME" , nullable = false , length=50 )
   public String getCommissionStakeholderName() {
      return commissionStakeholderName;
   }

   /**
    * Sets the value of the <code>commissionStakeholderName</code> property.
    *
    * @param commissionStakeholderName the value for the <code>commissionStakeholderName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setCommissionStakeholderName(String commissionStakeholderName) {
      this.commissionStakeholderName = commissionStakeholderName;
   }

   /**
    * Returns the value of the <code>transactionTypeName</code> property.
    *
    */
//      @Column(name = "TRANSACTION_TYPE_NAME" , nullable = false , length=50 )
//   public String getTransactionTypeName() {
//      return transactionTypeName;
//   }

   /**
    * Sets the value of the <code>transactionTypeName</code> property.
    *
    * @param transactionTypeName the value for the <code>transactionTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

//   public void setTransactionTypeName(String transactionTypeName) {
//      this.transactionTypeName = transactionTypeName;
//   }

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
    * Returns the value of the <code>toDate</code> property.
    *
    */
      @Column(name = "TO_DATE"  )
   public Date getToDate() {
      return toDate;
   }

   /**
    * Sets the value of the <code>toDate</code> property.
    *
    * @param toDate the value for the <code>toDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setToDate(Date toDate) {
      this.toDate = toDate;
   }

   /**
    * Returns the value of the <code>rate</code> property.
    *
    */
      @Column(name = "RATE" , nullable = false )
   public Double getRate() {
      return rate;
   }

   /**
    * Sets the value of the <code>rate</code> property.
    *
    * @param rate the value for the <code>rate</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setRate(Double rate) {
      this.rate = rate;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCommissionRateId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&commissionRateId=" + getCommissionRateId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "commissionRateId";
			return primaryKeyFieldName;				
    }

    @Column(name = "RANGE_STARTS" , nullable = false )
	public Double getRangeStarts() {
		return rangeStarts;
	}
    /**
     * Sets the value of the <code>rangeStarts</code> property.
     *
     * @param rate the value for the <code>rangeStarts</code> property
     *    
 		    * @spring.validator type="double"
     * @spring.validator type="doubleRange"		
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="99999999999.9999"
     */
	public void setRangeStarts(Double rangeStarts) {
		this.rangeStarts = rangeStarts;
	}

	@Column(name = "RANGE_ENDS" , nullable = false )
	public Double getRangeEnds() {
		return rangeEnds;
	}
	 /**
	    * Sets the value of the <code>rangeEnds</code> property.
	    *
	    * @param rate the value for the <code>rangeEnds</code> property
	    *    
			    * @spring.validator type="double"
	    * @spring.validator type="doubleRange"		
	    * @spring.validator-args arg1value="${var:min}"
	    * @spring.validator-var name="min" value="0"
	    * @spring.validator-args arg2value="${var:max}"
	    * @spring.validator-var name="max" value="99999999999.9999"
	    */
	public void setRangeEnds(Double rangeEnds) {
		this.rangeEnds = rangeEnds;
	}

	@Column(name = "SEGMENT_ID" , nullable = false )
	public Long getSegmentId() {
		return segmentId;
	}
	/**
	    * Sets the value of the <code>segmentId</code> property.
	    *
	    * @param segmentId the value for the <code>segmentId</code> property
	    *    
			    * @spring.validator type="long"
	    * @spring.validator type="longRange"		
	    * @spring.validator-args arg1value="${var:min}"
	    * @spring.validator-var name="min" value="0"
	    * @spring.validator-args arg2value="${var:max}"
	    * @spring.validator-var name="max" value="9999999999"			
	    */
	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}
	@Column(name = "SEGMENT_NAME" , nullable = false , length=50 )
	public String getSegmentName() {
		return segmentName;
	}
	 /**
	    * Sets the value of the <code>segmentName</code> property.
	    *
	    * @param segmentName the value for the <code>segmentName</code> property
	    *    
			    * @spring.validator type="maxlength"     
	    * @spring.validator-args arg1value="${var:maxlength}"
	    * @spring.validator-var name="maxlength" value="50"
	    * @spring.validator type="mask"
	    * @spring.validator-args arg1value="${mask}"
	    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
	    */
	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}       
}

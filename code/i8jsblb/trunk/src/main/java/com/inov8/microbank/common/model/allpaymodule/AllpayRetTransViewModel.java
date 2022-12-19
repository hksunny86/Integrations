package com.inov8.microbank.common.model.allpaymodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AllpayRetTransViewModel entity bean.
 * 
 * @author Jawwad Farooq Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 * 
 * 
 * @spring.bean name="AllpayRetTransViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "ALLPAY_RET_TRANS_VIEW")
public class AllpayRetTransViewModel extends BasePersistableModel implements Serializable {

	private Long pk;
	private String code;
	private Date txDate;
	private String txTime;
	private Double totalAmount;
	private String customerId;
	private String productId;
	private String retailerLocation;
	private String retailerId;
	private String username;
	private String allpayId;
	private Date startDate;
	private Date endDate;
	private Long retailerContactId;
	private Long retailerHeadId;
    private Long distributorHeadId;


	
	@Column(name = "RETAILER_CONTACT_ID", length = 10)
	public Long getRetailerContactId() {
		return retailerContactId;
	}

	public void setRetailerContactId(Long retailerContactId) {
		this.retailerContactId= retailerContactId;
	}
	
	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}

	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Default constructor.
	 */
	public AllpayRetTransViewModel() {
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
	 * @param primaryKey
	 *            the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setPk(primaryKey);
	}

	/**
	 * Returns the value of the <code>pk</code> property.
	 * 
	 */
	@Column(name = "PK")
	@Id
	public Long getPk() {
		return pk;
	}

	/**
	 * Sets the value of the <code>pk</code> property.
	 * 
	 * @param pk
	 *            the value for the <code>pk</code> property
	 * 
	 */

	public void setPk(Long pk) {
		this.pk = pk;
	}

	/**
	 * Returns the value of the <code>code</code> property.
	 * 
	 */
	@Column(name = "CODE", nullable = false, length = 50)
	public String getCode() {
		return code;
	}

	/**
	 * Sets the value of the <code>code</code> property.
	 * 
	 * @param code
	 *            the value for the <code>code</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="50"
	 */

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Returns the value of the <code>txDate</code> property.
	 * 
	 */
	@Column(name = "TX_DATE", length = 10)
	public Date getTxDate() {
		return txDate;
	}

	/**
	 * Sets the value of the <code>txDate</code> property.
	 * 
	 * @param txDate
	 *            the value for the <code>txDate</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="10"
	 */

	public void setTxDate(Date txDate) {
		this.txDate = txDate;
	}

	/**
	 * Returns the value of the <code>txTime</code> property.
	 * 
	 */
	@Column(name = "TX_TIME", length = 8)
	public String getTxTime() {
		return txTime;
	}

	/**
	 * Sets the value of the <code>txTime</code> property.
	 * 
	 * @param txTime
	 *            the value for the <code>txTime</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="8"
	 */

	public void setTxTime(String txTime) {
		this.txTime = txTime;
	}

	/**
	 * Returns the value of the <code>totalAmount</code> property.
	 * 
	 */
	@Column(name = "TOTAL_AMOUNT", nullable = false)
	public Double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * Sets the value of the <code>totalAmount</code> property.
	 * 
	 * @param totalAmount
	 *            the value for the <code>totalAmount</code> property
	 * 
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * Returns the value of the <code>customerId</code> property.
	 * 
	 */
	@Column(name = "CUSTOMER_ID", length = 50)
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the value of the <code>customerId</code> property.
	 * 
	 * @param customerId
	 *            the value for the <code>customerId</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="50"
	 */

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * Returns the value of the <code>productId</code> property.
	 * 
	 */
	@Column(name = "PRODUCT_ID", nullable = false, length = 50)
	public String getProductId() {
		return productId;
	}

	/**
	 * Sets the value of the <code>productId</code> property.
	 * 
	 * @param productId
	 *            the value for the <code>productId</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="50"
	 */

	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * Returns the value of the <code>retailerLocation</code> property.
	 * 
	 */
	@Column(name = "RETAILER_LOCATION", nullable = false, length = 50)
	public String getRetailerLocation() {
		return retailerLocation;
	}
	
	 /**
	    * Returns the value of the <code>retailerHeadId</code> property.
	    *
	    */
	      @Column(name = "RETAILER_HEAD_ID"  )
	   public Long getRetailerHeadId() {
	      return retailerHeadId;
	   }

	   /**
	    * Sets the value of the <code>retailerHeadId</code> property.
	    *
	    * @param retailerHeadId the value for the <code>retailerHeadId</code> property
	    *    
			    * @spring.validator type="double"
	    * @spring.validator type="doubleRange"		
	    * @spring.validator-args arg1value="${var:min}"
	    * @spring.validator-var name="min" value="0"
	    * @spring.validator-args arg2value="${var:max}"
	    * @spring.validator-var name="max" value="99999999999.9999"
	    */

	   public void setRetailerHeadId(Long retailerHeadId) {
	      this.retailerHeadId = retailerHeadId;
	   }

	   /**
	    * Returns the value of the <code>distributorHeadId</code> property.
	    *
	    */
	      @Column(name = "DISTRIBUTOR_HEAD_ID"  )
	   public Long getDistributorHeadId() {
	      return distributorHeadId;
	   }

	   /**
	    * Sets the value of the <code>distributorHeadId</code> property.
	    *
	    * @param distributorHeadId the value for the <code>distributorHeadId</code> property
	    *    
			    * @spring.validator type="double"
	    * @spring.validator type="doubleRange"		
	    * @spring.validator-args arg1value="${var:min}"
	    * @spring.validator-var name="min" value="0"
	    * @spring.validator-args arg2value="${var:max}"
	    * @spring.validator-var name="max" value="99999999999.9999"
	    */

	   public void setDistributorHeadId(Long distributorHeadId) {
	      this.distributorHeadId = distributorHeadId;
	   }



	/**
	 * Sets the value of the <code>retailerLocation</code> property.
	 * 
	 * @param retailerLocation
	 *            the value for the <code>retailerLocation</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="50"
	 */

	public void setRetailerLocation(String retailerLocation) {
		this.retailerLocation = retailerLocation;
	}

	/**
	 * Returns the value of the <code>retailerId</code> property.
	 * 
	 */
	@Column(name = "RETAILER_ID", length = 101)
	public String getRetailerId() {
		return retailerId;
	}

	/**
	 * Sets the value of the <code>retailerId</code> property.
	 * 
	 * @param retailerId
	 *            the value for the <code>retailerId</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="101"
	 */

	public void setRetailerId(String retailerId) {
		this.retailerId = retailerId;
	}

	/**
	 * Returns the value of the <code>username</code> property.
	 * 
	 */
	@Column(name = "USERNAME", nullable = false, length = 50)
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the value of the <code>username</code> property.
	 * 
	 * @param username
	 *            the value for the <code>username</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="50"
	 * @spring.validator type="mask"
	 * @spring.validator-args arg1value="${mask}"
	 * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
	 */

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the value of the <code>allpayId</code> property.
	 * 
	 */
	@Column(name = "ALLPAY_ID", length = 50)
	public String getAllpayId() {
		return allpayId;
	}

	/**
	 * Sets the value of the <code>allpayId</code> property.
	 * 
	 * @param allpayId
	 *            the value for the <code>allpayId</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="50"
	 */

	public void setAllpayId(String allpayId) {
		this.allpayId = allpayId;
	}

	/**
	 * Used by the display tag library for rendering a checkbox in the list.
	 * 
	 * @return String with a HTML checkbox.
	 */
	@Transient
	public String getCheckbox() {
		String checkBox = "<input type=\"checkbox\" name=\"checkbox";
		checkBox += "_" + getPk();
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
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "pk";
		return primaryKeyFieldName;
	}
}

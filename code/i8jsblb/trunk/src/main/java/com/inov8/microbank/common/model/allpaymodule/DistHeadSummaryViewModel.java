package com.inov8.microbank.common.model.allpaymodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The DistHeadSummaryViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DistHeadSummaryViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DIST_HEAD_SUMMARY_VIEW")
public class DistHeadSummaryViewModel extends BasePersistableModel implements Serializable {
  



   private Long pk;
   private Long distributorContactId;
   private String txDate;
   private String distributorUserId;
   private String accountNick;
   private String accountNo;
   private Double stockReceived;
   private String region;
   private String location;
   private Double headIssuedAmount;

   // #########################################################################################################################
   private String openingBalance;
   private String closingBalance;
   private String startDate;
   private String endDate;
   
	@javax.persistence.Transient
   public String getEndDate()
	{
		return endDate;
	}
	
	@javax.persistence.Transient
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	
	@javax.persistence.Transient
	public String getStartDate()
	{
		return startDate;
	}
	
	@javax.persistence.Transient
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}
   
   
   
	@javax.persistence.Transient
   public String getOpeningBalance()
	{
		return openingBalance;
	}
	
	@javax.persistence.Transient
	public void setOpeningBalance(String openingBalance)
	{
		this.openingBalance = openingBalance;
	}
	
	@javax.persistence.Transient
	public String getClosingBalance()
	{
		return closingBalance;
	}
	
	@javax.persistence.Transient
	public void setClosingBalance(String closingBalance)
	{
		this.closingBalance = closingBalance;
	}
    // #########################################################################################################################
	
	   /**
	    * Returns the value of the <code>txDate</code> property.
	    *
	    */
	      @Column(name = "TX_DATE"  , length=10 )
	   public String getTxDate() {
	      return txDate;
	   }
	   /**
	    * Sets the value of the <code>txDate</code> property.
	    *
	    * @param txDate the value for the <code>txDate</code> property
	    *    
			    * @spring.validator type="maxlength"     
	    * @spring.validator-args arg1value="${var:maxlength}"
	    * @spring.validator-var name="maxlength" value="10"
	    */

	   public void setTxDate(String txDate) {
	      this.txDate = txDate;
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


	
/**
    * Default constructor.
    */
   public DistHeadSummaryViewModel() {
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
    * Returns the value of the <code>distributorContactId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_CONTACT_ID"  )
       
   public Long getDistributorContactId() {
      return distributorContactId;
   }

   /**
    * Sets the value of the <code>distributorContactId</code> property.
    *
    * @param distributorContactId the value for the <code>distributorContactId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDistributorContactId(Long distributorContactId) {
      this.distributorContactId = distributorContactId;
   }

   /**
    * Returns the value of the <code>distributorUserId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_USER_ID"  , length=50 )
   public String getDistributorUserId() {
      return distributorUserId;
   }

   /**
    * Sets the value of the <code>distributorUserId</code> property.
    *
    * @param distributorUserId the value for the <code>distributorUserId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setDistributorUserId(String distributorUserId) {
      this.distributorUserId = distributorUserId;
   }

   /**
    * Returns the value of the <code>accountNick</code> property.
    *
    */
      @Column(name = "ACCOUNT_NICK" , nullable = false , length=50 )
   public String getAccountNick() {
      return accountNick;
   }

   /**
    * Sets the value of the <code>accountNick</code> property.
    *
    * @param accountNick the value for the <code>accountNick</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountNick(String accountNick) {
      this.accountNick = accountNick;
   }

   /**
    * Returns the value of the <code>accountNo</code> property.
    *
    */
      @Column(name = "ACCOUNT_NO"  , length=250 )
   public String getAccountNo() {
      return accountNo;
   }

   /**
    * Sets the value of the <code>accountNo</code> property.
    *
    * @param accountNo the value for the <code>accountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setAccountNo(String accountNo) {
      this.accountNo = accountNo;
   }

   /**
    * Returns the value of the <code>stockReceived</code> property.
    *
    */
      @Column(name = "STOCK_RECEIVED"  )
   public Double getStockReceived() {
      return stockReceived;
   }

   /**
    * Sets the value of the <code>stockReceived</code> property.
    *
    * @param stockReceived the value for the <code>stockReceived</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setStockReceived(Double stockReceived) {
      this.stockReceived = stockReceived;
   }

   /**
    * Returns the value of the <code>region</code> property.
    *
    */
      @Column(name = "REGION"  , length=47 )
   public String getRegion() {
      return region;
   }

   /**
    * Sets the value of the <code>region</code> property.
    *
    * @param region the value for the <code>region</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="47"
    */

   public void setRegion(String region) {
      this.region = region;
   }

   /**
    * Returns the value of the <code>location</code> property.
    *
    */
      @Column(name = "LOCATION"  , length=101 )
   public String getLocation() {
      return location;
   }

   /**
    * Sets the value of the <code>location</code> property.
    *
    * @param location the value for the <code>location</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="101"
    */

   public void setLocation(String location) {
      this.location = location;
   }

   /**
    * Returns the value of the <code>headIssuedAmount</code> property.
    *
    */
      @Column(name = "HEAD_ISSUED_AMOUNT"  )
   public Double getHeadIssuedAmount() {
      return headIssuedAmount;
   }

   /**
    * Sets the value of the <code>headIssuedAmount</code> property.
    *
    * @param headIssuedAmount the value for the <code>headIssuedAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setHeadIssuedAmount(Double headIssuedAmount) {
      this.headIssuedAmount = headIssuedAmount;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getDistributorContactId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&dist=" + getDistributorContactId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "distributorContactId";
			return primaryKeyFieldName;				
    }       
}

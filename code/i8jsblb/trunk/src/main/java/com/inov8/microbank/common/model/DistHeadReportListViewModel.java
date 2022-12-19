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
 * The DistHeadReportListViewtModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DistHeadReportListViewtModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DIST_HEAD_REPORT_LIST_VIEW")
public class DistHeadReportListViewModel extends BasePersistableModel implements Serializable {
  



   private Long pk;
   private Long appUserId;
   private Long distributorContactId;
   private String userId;
   private String distUserId;
   private Double totalAmount;
   private Double transactionAmount;
   private Double totalCommissionAmount;
   private String createdOn;
   private Integer retailerHeadTransactions;
   private String retailerUsername;
   private String distAccountNick;
   private Long customerId;
   private String distAccountNo;
   private String retailerAccountNick;
   private String retailerAccountNo;
   private Double distAmountDisbursed;
   private Double retAmountDisbursed;

   
 //###############################################################################################################################
   
  	private Long balanceDisbursed;
	private Long balanceReceived;
	private String endDayBalance;
	private String startDayBalance;
	private String startDayBalanceRet;
	private Date statsDate;
	private Long dailyAccountStatsId;
	private String balance ;
  
	@javax.persistence.Transient
	public String getBalance()
	{
		return balance;
	}

	@javax.persistence.Transient
	public void setBalance(String balance)
	{
		this.balance = balance;
	}

	@javax.persistence.Transient	
	   public Long getBalanceDisbursed()
		{
			return balanceDisbursed;
		}

	@javax.persistence.Transient
		public void setBalanceDisbursed(Long balanceDisbursed)
		{
			this.balanceDisbursed = balanceDisbursed;
		}

	@javax.persistence.Transient
		public Long getBalanceReceived()
		{
			return balanceReceived;
		}

	@javax.persistence.Transient
		public void setBalanceReceived(Long balanceReceived)
		{
			this.balanceReceived = balanceReceived;
		}

	@javax.persistence.Transient
		public String getEndDayBalance()
		{
			return endDayBalance;
		}

	@javax.persistence.Transient
		public void setEndDayBalance(String endDayBalance)
		{
			this.endDayBalance = endDayBalance;
		}

	@javax.persistence.Transient
		public String getStartDayBalance()
		{
			return startDayBalance;
		}

	@javax.persistence.Transient
		public void setStartDayBalance(String startDayBalance)
		{
			this.startDayBalance = startDayBalance;
		}

	@javax.persistence.Transient
	public String getStartDayBalanceRet()
	{
		return startDayBalanceRet;
	}

	@javax.persistence.Transient
	public void setStartDayBalanceRet(String startDayBalance)
	{
		this.startDayBalanceRet = startDayBalance;
	}

	
	@javax.persistence.Transient
		public Date getStatsDate()
		{
			return statsDate;
		}

	@javax.persistence.Transient
		public void setStatsDate(Date statsDate)
		{
			this.statsDate = statsDate;
		}

	@javax.persistence.Transient
		public Long getDailyAccountStatsId()
		{
			return dailyAccountStatsId;
		}

	@javax.persistence.Transient
		public void setDailyAccountStatsId(Long dailyAccountStatsId)
		{
			this.dailyAccountStatsId = dailyAccountStatsId;
		}

  
  
  // ##################################################################################################################################
  
  
  
   
   
   /**
    * Default constructor.
    */
   public DistHeadReportListViewModel() {
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
      @Column(name = "PK"  , length=90 )
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
    * Returns the value of the <code>appUserId</code> property.
    *
    */
      @Column(name = "APP_USER_ID"  )
   public Long getAppUserId() {
      return appUserId;
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAppUserId(Long appUserId) {
      this.appUserId = appUserId;
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
    * Returns the value of the <code>userId</code> property.
    *
    */
      @Column(name = "USER_ID"  , length=50 )
   public String getUserId() {
      return userId;
   }

   /**
    * Sets the value of the <code>userId</code> property.
    *
    * @param userId the value for the <code>userId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setUserId(String userId) {
      this.userId = userId;
   }

   /**
    * Returns the value of the <code>distUserId</code> property.
    *
    */
      @Column(name = "DIST_USER_ID"  , length=50 )
   public String getDistUserId() {
      return distUserId;
   }

   /**
    * Sets the value of the <code>distUserId</code> property.
    *
    * @param distUserId the value for the <code>distUserId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setDistUserId(String distUserId) {
      this.distUserId = distUserId;
   }

   /**
    * Returns the value of the <code>totalAmount</code> property.
    *
    */
      @Column(name = "TOTAL_AMOUNT"  )
   public Double getTotalAmount() {
      return totalAmount;
   }

   /**
    * Sets the value of the <code>totalAmount</code> property.
    *
    * @param totalAmount the value for the <code>totalAmount</code> property
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
    * Returns the value of the <code>transactionAmount</code> property.
    *
    */
      @Column(name = "TRANSACTION_AMOUNT"  )
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
    * Returns the value of the <code>totalCommissionAmount</code> property.
    *
    */
      @Column(name = "TOTAL_COMMISSION_AMOUNT"  )
   public Double getTotalCommissionAmount() {
      return totalCommissionAmount;
   }

   /**
    * Sets the value of the <code>totalCommissionAmount</code> property.
    *
    * @param totalCommissionAmount the value for the <code>totalCommissionAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTotalCommissionAmount(Double totalCommissionAmount) {
      this.totalCommissionAmount = totalCommissionAmount;
   }

   /**
    * Returns the value of the <code>createdOn</code> property.
    *
    */
      @Column(name = "CREATED_ON"  , length=10 )
   public String getCreatedOn() {
      return createdOn;
   }

   /**
    * Sets the value of the <code>createdOn</code> property.
    *
    * @param createdOn the value for the <code>createdOn</code> property
    *    
		    */

   public void setCreatedOn(String createdOn) {
      this.createdOn = createdOn;
   }

   /**
    * Returns the value of the <code>retailerHeadTransactions</code> property.
    *
    */
      @Column(name = "RETAILER_HEAD_TRANSACTIONS"  )
   public Integer getRetailerHeadTransactions() {
      return retailerHeadTransactions;
   }

   /**
    * Sets the value of the <code>retailerHeadTransactions</code> property.
    *
    * @param retailerHeadTransactions the value for the <code>retailerHeadTransactions</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setRetailerHeadTransactions(Integer retailerHeadTransactions) {
      this.retailerHeadTransactions = retailerHeadTransactions;
   }

   /**
    * Returns the value of the <code>retailerUsername</code> property.
    *
    */
      @Column(name = "RETAILER_USERNAME"  , length=50 )
   public String getRetailerUsername() {
      return retailerUsername;
   }

   /**
    * Sets the value of the <code>retailerUsername</code> property.
    *
    * @param retailerUsername the value for the <code>retailerUsername</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setRetailerUsername(String retailerUsername) {
      this.retailerUsername = retailerUsername;
   }

   /**
    * Returns the value of the <code>distAccountNick</code> property.
    *
    */
      @Column(name = "DIST_ACCOUNT_NICK"  , length=50 )
   public String getDistAccountNick() {
      return distAccountNick;
   }

   /**
    * Sets the value of the <code>distAccountNick</code> property.
    *
    * @param distAccountNick the value for the <code>distAccountNick</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setDistAccountNick(String distAccountNick) {
      this.distAccountNick = distAccountNick;
   }

   /**
    * Returns the value of the <code>customerId</code> property.
    *
    */
      @Column(name = "CUSTOMER_ID"  )
   public Long getCustomerId() {
      return customerId;
   }

   /**
    * Sets the value of the <code>customerId</code> property.
    *
    * @param customerId the value for the <code>customerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomerId(Long customerId) {
      this.customerId = customerId;
   }

   /**
    * Returns the value of the <code>distAccountNo</code> property.
    *
    */
      @Column(name = "DIST_ACCOUNT_NO"  , length=250 )
   public String getDistAccountNo() {
      return distAccountNo;
   }

   /**
    * Sets the value of the <code>distAccountNo</code> property.
    *
    * @param distAccountNo the value for the <code>distAccountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDistAccountNo(String distAccountNo) {
      this.distAccountNo = distAccountNo;
   }

   /**
    * Returns the value of the <code>retailerAccountNick</code> property.
    *
    */
      @Column(name = "RETAILER_ACCOUNT_NICK"  , length=50 )
   public String getRetailerAccountNick() {
      return retailerAccountNick;
   }

   /**
    * Sets the value of the <code>retailerAccountNick</code> property.
    *
    * @param retailerAccountNick the value for the <code>retailerAccountNick</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setRetailerAccountNick(String retailerAccountNick) {
      this.retailerAccountNick = retailerAccountNick;
   }

   /**
    * Returns the value of the <code>retailerAccountNo</code> property.
    *
    */
      @Column(name = "RETAILER_ACCOUNT_NO"  , length=250 )
   public String getRetailerAccountNo() {
      return retailerAccountNo;
   }

   /**
    * Sets the value of the <code>retailerAccountNo</code> property.
    *
    * @param retailerAccountNo the value for the <code>retailerAccountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setRetailerAccountNo(String retailerAccountNo) {
      this.retailerAccountNo = retailerAccountNo;
   }

   /**
    * Returns the value of the <code>distAmountDisbursed</code> property.
    *
    */
      @Column(name = "DIST_AMOUNT_DISBURSED"  )
   public Double getDistAmountDisbursed() {
      return distAmountDisbursed;
   }

   /**
    * Sets the value of the <code>distAmountDisbursed</code> property.
    *
    * @param distAmountDisbursed the value for the <code>distAmountDisbursed</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setDistAmountDisbursed(Double distAmountDisbursed) {
      this.distAmountDisbursed = distAmountDisbursed;
   }

   /**
    * Returns the value of the <code>retAmountDisbursed</code> property.
    *
    */
      @Column(name = "RET_AMOUNT_DISBURSED"  )
   public Double getRetAmountDisbursed() {
      return retAmountDisbursed;
   }

   /**
    * Sets the value of the <code>retAmountDisbursed</code> property.
    *
    * @param retAmountDisbursed the value for the <code>retAmountDisbursed</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setRetAmountDisbursed(Double retAmountDisbursed) {
      this.retAmountDisbursed = retAmountDisbursed;
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

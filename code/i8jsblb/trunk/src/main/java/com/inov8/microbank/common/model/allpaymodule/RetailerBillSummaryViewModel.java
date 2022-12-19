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
 * The RetailerBillSummaryViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="RetailerBillSummaryViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "RETAILER_BILL_SUMMARY_VIEW")
public class RetailerBillSummaryViewModel extends BasePersistableModel implements Serializable {
  



   private Long pk;
   private Long transactionTypeId;
   private Long retailerContactId;
   private Long distributorId;
   private Date txDate;
   private String userId;
   private String name;
   private String retailerContactName;
   private Double txCount;
   private Double amountSubmitted;
   private String username;
   private String retailerSmaAcc;
   private String retAccountNo;
   
   private Long headId;
   private Long distributorContactId;

   //###############################################################################################################################
   
 	private Long balanceDisbursed;
	private Long balanceReceived;
	private String endDayBalance;
	private String startDayBalance;
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
   public RetailerBillSummaryViewModel() {
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
      @Column(name = "PK"  , length=48 )
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
    * Returns the value of the <code>transactionTypeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_TYPE_ID" , nullable = false )
   public Long getTransactionTypeId() {
      return transactionTypeId;
   }

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

      /**
       * Returns the value of the <code>headId</code> property.
       *
       */
         @Column(name = "HEAD_ID" , nullable = false )
      public Long getHeadId() {
         return headId;
      }

      /**
       * Sets the value of the <code>headId</code> property.
       *
       * @param headId the value for the <code>headId</code> property
       *    
   		    * @spring.validator type="long"
       * @spring.validator type="longRange"		
       * @spring.validator-args arg1value="${var:min}"
       * @spring.validator-var name="min" value="0"
       * @spring.validator-args arg2value="${var:max}"
       * @spring.validator-var name="max" value="9999999999"			
       */

      public void setHeadId(Long headId) {
         this.headId = headId;
      }

      /**
       * Returns the value of the <code>distributorContactId</code> property.
       *
       */
         @Column(name = "DISTRIBUTOR_CONTACT_ID" , nullable = false )
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



      
      
   public void setTransactionTypeId(Long transactionTypeId) {
      this.transactionTypeId = transactionTypeId;
   }

   /**
    * Returns the value of the <code>retailerContactId</code> property.
    *
    */
      @Column(name = "RETAILER_CONTACT_ID"  )
   public Long getRetailerContactId() {
      return retailerContactId;
   }

   /**
    * Sets the value of the <code>retailerContactId</code> property.
    *
    * @param retailerContactId the value for the <code>retailerContactId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setRetailerContactId(Long retailerContactId) {
      this.retailerContactId = retailerContactId;
   }

   /**
    * Returns the value of the <code>distributorId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_ID"  )
   public Long getDistributorId() {
      return distributorId;
   }

   /**
    * Sets the value of the <code>distributorId</code> property.
    *
    * @param distributorId the value for the <code>distributorId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDistributorId(Long distributorId) {
      this.distributorId = distributorId;
   }

   /**
    * Returns the value of the <code>txDate</code> property.
    *
    */
      @Column(name = "TX_DATE"  , length=10 )
   public Date getTxDate() {
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

   public void setTxDate(Date txDate) {
      this.txDate = txDate;
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
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=50 )
   public String getName() {
      return name;
   }

   /**
    * Sets the value of the <code>name</code> property.
    *
    * @param name the value for the <code>name</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setName(String name) {
      this.name = name;
   }

   /**
    * Returns the value of the <code>retailerContactName</code> property.
    *
    */
      @Column(name = "RETAILER_CONTACT_NAME"  , length=101 )
   public String getRetailerContactName() {
      return retailerContactName;
   }

   /**
    * Sets the value of the <code>retailerContactName</code> property.
    *
    * @param retailerContactName the value for the <code>retailerContactName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="101"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setRetailerContactName(String retailerContactName) {
      this.retailerContactName = retailerContactName;
   }

   /**
    * Returns the value of the <code>txCount</code> property.
    *
    */
      @Column(name = "TX_COUNT"  )
   public Double getTxCount() {
      return txCount;
   }

   /**
    * Sets the value of the <code>txCount</code> property.
    *
    * @param txCount the value for the <code>txCount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTxCount(Double txCount) {
      this.txCount = txCount;
   }

   /**
    * Returns the value of the <code>amountSubmitted</code> property.
    *
    */
      @Column(name = "AMOUNT_SUBMITTED"  )
   public Double getAmountSubmitted() {
      return amountSubmitted;
   }

   /**
    * Sets the value of the <code>amountSubmitted</code> property.
    *
    * @param amountSubmitted the value for the <code>amountSubmitted</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setAmountSubmitted(Double amountSubmitted) {
      this.amountSubmitted = amountSubmitted;
   }

   /**
    * Returns the value of the <code>username</code> property.
    *
    */
      @Column(name = "USERNAME" , nullable = false , length=50 )
   public String getUsername() {
      return username;
   }

   /**
    * Sets the value of the <code>username</code> property.
    *
    * @param username the value for the <code>username</code> property
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
    * Returns the value of the <code>retailerSmaAcc</code> property.
    *
    */
      @Column(name = "RETAILER_SMA_ACC" , nullable = false , length=50 )
   public String getRetailerSmaAcc() {
      return retailerSmaAcc;
   }

   /**
    * Sets the value of the <code>retailerSmaAcc</code> property.
    *
    * @param retailerSmaAcc the value for the <code>retailerSmaAcc</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setRetailerSmaAcc(String retailerSmaAcc) {
      this.retailerSmaAcc = retailerSmaAcc;
   }

   /**
    * Returns the value of the <code>retAccountNo</code> property.
    *
    */
      @Column(name = "RET_ACCOUNT_NO"  , length=250 )
   public String getRetAccountNo() {
      return retAccountNo;
   }

   /**
    * Sets the value of the <code>retAccountNo</code> property.
    *
    * @param retAccountNo the value for the <code>retAccountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setRetAccountNo(String retAccountNo) {
      this.retAccountNo = retAccountNo;
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

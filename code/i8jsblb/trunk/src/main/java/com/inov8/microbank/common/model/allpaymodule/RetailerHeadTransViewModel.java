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
 * The RetailerHeadTransViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="RetailerHeadTransViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "RETAILER_HEAD_TRANS_VIEW")
public class RetailerHeadTransViewModel extends BasePersistableModel implements Serializable {
  



   private Long pk;
   private Long headAppUserId;
   private Long headContactId;
   private String headUserId;
   private String headAccountNick;
   private String headAccountNo;
   private String retailerUserId;
   private String retAccountNick;
   private String retAccountNo;
   private String username;
   private Date txDate;
   private Double noOfTx;
   private Double txAmount;
   private Double totalAmount;
   private Double amountDisbursed;
   private Double amountCollected;
   private Double totalCommission;
   private Long distributorHeadId;

   
   //###############################################################################################################################
   
	private Long balanceDisbursed;
	private Long balanceReceived;
	private String endDayBalance;
	private String startDayBalance;
	private Date statsDate;
	private Long dailyAccountStatsId;
	private String balance ;
	
	private String retStartDayBalance;
	private String retBalance ;
	private String retEndDayBalance ;
	
	
	@javax.persistence.Transient
	public String getRetEndDayBalance()
	{
		return retEndDayBalance;
	}

	@javax.persistence.Transient
	public void setRetEndDayBalance(String retEndDayBalance)
	{
		this.retEndDayBalance = retEndDayBalance;
	}

	@javax.persistence.Transient
	public String getRetStartDayBalance()
	{
		return retStartDayBalance;
	}

	@javax.persistence.Transient
	public void setRetStartDayBalance(String retStartDayBalance)
	{
		this.retStartDayBalance = retStartDayBalance;
	}

	@javax.persistence.Transient
	public String getRetBalance()
	{
		return retBalance;
	}

	@javax.persistence.Transient
	public void setRetBalance(String retBalance)
	{
		this.retBalance = retBalance;
	}

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
   public RetailerHeadTransViewModel() {
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
    * Returns the value of the <code>headAppUserId</code> property.
    *
    */
      @Column(name = "HEAD_APP_USER_ID" , nullable = false )
   public Long getHeadAppUserId() {
      return headAppUserId;
   }

   /**
    * Sets the value of the <code>headAppUserId</code> property.
    *
    * @param headAppUserId the value for the <code>headAppUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setHeadAppUserId(Long headAppUserId) {
      this.headAppUserId = headAppUserId;
   }

   /**
    * Returns the value of the <code>headContactId</code> property.
    *
    */
      @Column(name = "HEAD_CONTACT_ID"  )
   public Long getHeadContactId() {
      return headContactId;
   }
      
      
      
      public void setUsername(String username) {
          this.username = username;
       }

       /**
        * Returns the value of the <code>headContactId</code> property.
        *
        */
          @Column(name = "USERNAME"  )
       public String getUsername() {
          return username;
       }
          
      

   /**
    * Sets the value of the <code>headContactId</code> property.
    *
    * @param headContactId the value for the <code>headContactId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setHeadContactId(Long headContactId) {
      this.headContactId = headContactId;
   }

   /**
    * Returns the value of the <code>headUserId</code> property.
    *
    */
      @Column(name = "HEAD_USER_ID"  , length=50 )
   public String getHeadUserId() {
      return headUserId;
   }

   /**
    * Sets the value of the <code>headUserId</code> property.
    *
    * @param headUserId the value for the <code>headUserId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setHeadUserId(String headUserId) {
      this.headUserId = headUserId;
   }

   /**
    * Returns the value of the <code>headAccountNick</code> property.
    *
    */
      @Column(name = "HEAD_ACCOUNT_NICK" , nullable = false , length=50 )
   public String getHeadAccountNick() {
      return headAccountNick;
   }

   /**
    * Sets the value of the <code>headAccountNick</code> property.
    *
    * @param headAccountNick the value for the <code>headAccountNick</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setHeadAccountNick(String headAccountNick) {
      this.headAccountNick = headAccountNick;
   }

   /**
    * Returns the value of the <code>headAccountNo</code> property.
    *
    */
      @Column(name = "HEAD_ACCOUNT_NO"  , length=250 )
   public String getHeadAccountNo() {
      return headAccountNo;
   }

   /**
    * Sets the value of the <code>headAccountNo</code> property.
    *
    * @param headAccountNo the value for the <code>headAccountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setHeadAccountNo(String headAccountNo) {
      this.headAccountNo = headAccountNo;
   }

   /**
    * Returns the value of the <code>retailerUserId</code> property.
    *
    */
      @Column(name = "RETAILER_USER_ID"  , length=50 )
   public String getRetailerUserId() {
      return retailerUserId;
   }

   /**
    * Sets the value of the <code>retailerUserId</code> property.
    *
    * @param retailerUserId the value for the <code>retailerUserId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setRetailerUserId(String retailerUserId) {
      this.retailerUserId = retailerUserId;
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
    * Returns the value of the <code>retAccountNick</code> property.
    *
    */
      @Column(name = "RET_ACCOUNT_NICK" , nullable = false , length=50 )
   public String getRetAccountNick() {
      return retAccountNick;
   }

   /**
    * Sets the value of the <code>retAccountNick</code> property.
    *
    * @param retAccountNick the value for the <code>retAccountNick</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setRetAccountNick(String retAccountNick) {
      this.retAccountNick = retAccountNick;
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
    * Returns the value of the <code>noOfTx</code> property.
    *
    */
      @Column(name = "NO_OF_TX"  )
   public Double getNoOfTx() {
      return noOfTx;
   }

   /**
    * Sets the value of the <code>noOfTx</code> property.
    *
    * @param noOfTx the value for the <code>noOfTx</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setNoOfTx(Double noOfTx) {
      this.noOfTx = noOfTx;
   }

   /**
    * Returns the value of the <code>txAmount</code> property.
    *
    */
      @Column(name = "TX_AMOUNT"  )
   public Double getTxAmount() {
      return txAmount;
   }

   /**
    * Sets the value of the <code>txAmount</code> property.
    *
    * @param txAmount the value for the <code>txAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTxAmount(Double txAmount) {
      this.txAmount = txAmount;
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

   
   @Column(name = "AMOUNT_DISBURSED"  )
   public Double getAmountDisbursed() {
      return amountDisbursed ;
   }

   
   public void setAmountDisbursed(Double totalAmount) {
      this.amountDisbursed = totalAmount;
   }
   

   @Column(name = "AMOUNT_COLLECTED"  )
   public Double getAmountCollected() {
      return amountCollected ;
   }

   
   public void setAmountCollected(Double totalAmount) {
      this.amountCollected = totalAmount;
   }

   
   
   /**
    * Returns the value of the <code>totalCommission</code> property.
    *
    */
      @Column(name = "TOTAL_COMMISSION"  )
   public Double getTotalCommission() {
      return totalCommission;
   }

   /**
    * Sets the value of the <code>totalCommission</code> property.
    *
    * @param totalCommission the value for the <code>totalCommission</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTotalCommission(Double totalCommission) {
      this.totalCommission = totalCommission;
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

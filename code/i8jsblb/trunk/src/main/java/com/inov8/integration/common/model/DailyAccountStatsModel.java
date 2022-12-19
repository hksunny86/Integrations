package com.inov8.integration.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The DailyAccountStatsModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DailyAccountStatsModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DAILY_ACCOUNT_STATS_seq",sequenceName = "DAILY_ACCOUNT_STATS_seq")
@Table(name = "DAILY_ACCOUNT_STATS")
public class DailyAccountStatsModel extends BasePersistableModel implements Serializable {
  

   private AccountModel accountIdAccountModel;

   private Long dailyAccountStatsId;
   private String startDayBalance;
   private String endDayBalance;
   private Long balanceReceived;
   private Long balanceDisbursed;
   private Date createdOn;
   private Date updatedOn;
   private Date statsDate;
   private String decryptionSchedulerStatus;

   /**
    * Default constructor.
    */
   public DailyAccountStatsModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDailyAccountStatsId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDailyAccountStatsId(primaryKey);
    }

   /**
    * Returns the value of the <code>dailyAccountStatsId</code> property.
    *
    */
      @Column(name = "DAILY_ACCOUNT_STATS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DAILY_ACCOUNT_STATS_seq")
   public Long getDailyAccountStatsId() {
      return dailyAccountStatsId;
   }

   /**
    * Sets the value of the <code>dailyAccountStatsId</code> property.
    *
    * @param dailyAccountStatsId the value for the <code>dailyAccountStatsId</code> property
    *    
		    */

   public void setDailyAccountStatsId(Long dailyAccountStatsId) {
      this.dailyAccountStatsId = dailyAccountStatsId;
   }

   /**
    * Returns the value of the <code>startDayBalance</code> property.
    *
    */
      @Column(name = "START_DAY_BALANCE"  , length=255 )
   public String getStartDayBalance() {
      return startDayBalance;
   }

   /**
    * Sets the value of the <code>startDayBalance</code> property.
    *
    * @param startDayBalance the value for the <code>startDayBalance</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setStartDayBalance(String startDayBalance) {
      this.startDayBalance = startDayBalance;
   }

   /**
    * Returns the value of the <code>endDayBalance</code> property.
    *
    */
      @Column(name = "END_DAY_BALANCE"  , length=255 )
   public String getEndDayBalance() {
      return endDayBalance;
   }

   /**
    * Sets the value of the <code>endDayBalance</code> property.
    *
    * @param endDayBalance the value for the <code>endDayBalance</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setEndDayBalance(String endDayBalance) {
      this.endDayBalance = endDayBalance;
   }

   /**
    * Returns the value of the <code>balanceReceived</code> property.
    *
    */
      @Column(name = "BALANCE_RECEIVED"  )
   public Long getBalanceReceived() {
      return balanceReceived;
   }

   /**
    * Sets the value of the <code>balanceReceived</code> property.
    *
    * @param balanceReceived the value for the <code>balanceReceived</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBalanceReceived(Long balanceReceived) {
      this.balanceReceived = balanceReceived;
   }

   /**
    * Returns the value of the <code>balanceDisbursed</code> property.
    *
    */
      @Column(name = "BALANCE_DISBURSED"  )
   public Long getBalanceDisbursed() {
      return balanceDisbursed;
   }

   /**
    * Sets the value of the <code>balanceDisbursed</code> property.
    *
    * @param balanceDisbursed the value for the <code>balanceDisbursed</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBalanceDisbursed(Long balanceDisbursed) {
      this.balanceDisbursed = balanceDisbursed;
   }

   /**
    * Returns the value of the <code>createdOn</code> property.
    *
    */
      @Column(name = "CREATED_ON"  )
   public Date getCreatedOn() {
      return createdOn;
   }

   /**
    * Sets the value of the <code>createdOn</code> property.
    *
    * @param createdOn the value for the <code>createdOn</code> property
    *    
		    */

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   /**
    * Returns the value of the <code>updatedOn</code> property.
    *
    */
      @Column(name = "UPDATED_ON"  )
   public Date getUpdatedOn() {
      return updatedOn;
   }

   /**
    * Sets the value of the <code>updatedOn</code> property.
    *
    * @param updatedOn the value for the <code>updatedOn</code> property
    *    
		    */

   public void setUpdatedOn(Date updatedOn) {
      this.updatedOn = updatedOn;
   }

   /**
    * Returns the value of the <code>statsDate</code> property.
    *
    */
      @Column(name = "STATS_DATE"  )
   public Date getStatsDate() {
      return statsDate;
   }

   /**
    * Sets the value of the <code>statsDate</code> property.
    *
    * @param statsDate the value for the <code>statsDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setStatsDate(Date statsDate) {
      this.statsDate = statsDate;
   }

   /**
    * Returns the value of the <code>accountIdAccountModel</code> relation property.
    *
    * @return the value of the <code>accountIdAccountModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACCOUNT_ID")    
   public AccountModel getRelationAccountIdAccountModel(){
      return accountIdAccountModel;
   }
    
   /**
    * Returns the value of the <code>accountIdAccountModel</code> relation property.
    *
    * @return the value of the <code>accountIdAccountModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AccountModel getAccountIdAccountModel(){
      return getRelationAccountIdAccountModel();
   }

   /**
    * Sets the value of the <code>accountIdAccountModel</code> relation property.
    *
    * @param accountModel a value for <code>accountIdAccountModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAccountIdAccountModel(AccountModel accountModel) {
      this.accountIdAccountModel = accountModel;
   }
   
   /**
    * Sets the value of the <code>accountIdAccountModel</code> relation property.
    *
    * @param accountModel a value for <code>accountIdAccountModel</code>.
    */
   @javax.persistence.Transient
   public void setAccountIdAccountModel(AccountModel accountModel) {
      if(null != accountModel)
      {
      	setRelationAccountIdAccountModel((AccountModel)accountModel.clone());
      }      
   }
   



   /**
    * Returns the value of the <code>accountId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAccountId() {
      if (accountIdAccountModel != null) {
         return accountIdAccountModel.getAccountId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>accountId</code> property.
    *
    * @param accountId the value for the <code>accountId</code> property
																					    */
   
   @javax.persistence.Transient
   public void setAccountId(Long accountId) {
      if(accountId == null)
      {      
      	accountIdAccountModel = null;
      }
      else
      {
        accountIdAccountModel = new AccountModel();
      	accountIdAccountModel.setAccountId(accountId);
      }      
   }

   @Column(name="DECRYPTION_SCHEDULER_STATUS")
   public String getDecryptionSchedulerStatus()
   {
	   return decryptionSchedulerStatus;
   }

   public void setDecryptionSchedulerStatus(String decryptionSchedulerStatus)
   {
	   this.decryptionSchedulerStatus = decryptionSchedulerStatus;
   }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getDailyAccountStatsId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&dailyAccountStatsId=" + getDailyAccountStatsId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "dailyAccountStatsId";
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
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AccountModel");
    	associationModel.setPropertyName("relationAccountIdAccountModel");   		
   		associationModel.setValue(getRelationAccountIdAccountModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}

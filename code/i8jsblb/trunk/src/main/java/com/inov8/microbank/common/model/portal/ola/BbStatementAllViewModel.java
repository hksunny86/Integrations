package com.inov8.microbank.common.model.portal.ola;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.integration.common.model.AccountModel;

/**
 * Created By    : Hassan javaid <br>
 * Creation Date : 19 Sep, 2014 6:00:00 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BB_STATEMENT_ALL_VIEW")
public class BbStatementAllViewModel extends BasePersistableModel implements Serializable, Comparable<BbStatementAllViewModel>
{
    private static final long serialVersionUID = 8494313691353800665L;

    private AccountModel accountIdAccountModel;
    private Long pk; 
    private Long ledgerId;
	private Date transactionTime;
	private String transactionCode;
	private Double balanceAfterTransaction;
	private Double debitAmount;
	private Double creditAmount;
	private String transactionSummaryText;
	private String accountTittle;
	private Boolean isProcessedByQueue;
	private String queueStatus;
	private String accountNumber; 
	private Long   category;	

	//Following Transient fields used in Manual Reversal Form
	private Double amount;
	private String amountStr;
	private Long transactionType;
	
	private DateRangeHolderModel dateRangeHolderModel;

	/** default constructor */
	public BbStatementAllViewModel() {
	}

	public BbStatementAllViewModel( DateRangeHolderModel dateRangeHolderModel )
    {
        super();
        this.dateRangeHolderModel = dateRangeHolderModel;
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
	    
    // Property accessors
    @Column( name = "LEDGER_ID" )
    public Long getLedgerId()
    {
        return this.ledgerId;
    }

    public void setLedgerId( Long ledgerId )
    {
        this.ledgerId = ledgerId;
    }


    @Column( name = "TRANSACTION_TIME" )
    public Date getTransactionTime()
    {
        return this.transactionTime;
    }

    public void setTransactionTime( Date transactionTime )
    {
        this.transactionTime = transactionTime;
    }

    @Column( name = "TRANSACTION_CODE" )
    public String getTransactionCode()
    {
        return this.transactionCode;
    }

    public void setTransactionCode( String transactionCode )
    {
        this.transactionCode = transactionCode;
    }

    @Column( name = "BALANCE_AFTER_TRANSACTION" )
    public Double getBalanceAfterTransaction()
    {
        return this.balanceAfterTransaction;
    }

    public void setBalanceAfterTransaction( Double balanceAfterTransaction )
    {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    @Column( name = "DEBIT_AMOUNT" )
    public Double getDebitAmount()
    {
        return this.debitAmount;
    }

    public void setDebitAmount( Double debitAmount )
    {
        this.debitAmount = debitAmount;
    }

    @Column( name = "CREDIT_AMOUNT" )
    public Double getCreditAmount()
    {
        return this.creditAmount;
    }

    public void setCreditAmount( Double creditAmount )
    {
        this.creditAmount = creditAmount;
    }

    @Column( name = "TRANSACTION_SUMMARY_TEXT" )
    public String getTransactionSummaryText()
    {
        return this.transactionSummaryText;
    }

    public void setTransactionSummaryText( String transactionSummaryText )
    {
        this.transactionSummaryText = transactionSummaryText;
    }

    @Transient
    public DateRangeHolderModel getDateRangeHolderModel()
    {
        return dateRangeHolderModel;
    }

    public void setDateRangeHolderModel( DateRangeHolderModel dateRangeHolderModel )
    {
        this.dateRangeHolderModel = dateRangeHolderModel;
    }
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")    
    public AccountModel getRelationAccountIdAccountModel(){
       return this.accountIdAccountModel;
    }
     
    @javax.persistence.Transient
    public AccountModel getAccountIdAccountModel(){
       return getRelationAccountIdAccountModel();
    }

  
    @javax.persistence.Transient
    public void setRelationAccountIdAccountModel(AccountModel accountModel) {
       this.accountIdAccountModel = accountModel;
    }
    
  
    @javax.persistence.Transient
    public void setAccountIdAccountModel(AccountModel accountModel) {
       if(null != accountModel)
       {
    	   setRelationAccountIdAccountModel((AccountModel)accountModel.clone());
       }      
    }
    
	@Transient
	public Long getAccountId() {
		if (accountIdAccountModel != null) {
			return accountIdAccountModel.getAccountId();
		} else {
			return null;
		}
	}

	@Transient
	public void setAccountId(Long accountId) {
		if (accountId == null) {
			this.accountIdAccountModel = null;
		} else {
			accountIdAccountModel = new AccountModel();
			accountIdAccountModel.setAccountId(accountId);
		}
	}
   
	 @Column( name = "ACCOUNT_TITLE" )
	public String getAccountTittle() {
		return accountTittle;
	}

	
	public void setAccountTittle(String accountTittle) {
		this.accountTittle = accountTittle;
	}
	
	 @Column( name = "ACCOUNT_NUMBER" )
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

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
	
	 @Column( name = "CATEGORY" )
	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
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
	    
		@Override
		public int compareTo(BbStatementAllViewModel bbStatementAllViewModel) {

			int category = 0;
			
			if(bbStatementAllViewModel.category != null) {
				
				category = bbStatementAllViewModel.category.intValue();
				
				if(category < 2) {
					category = 1;
				} else {
					category = -1;
				}
			}
	 
			return category;
		}

		@Column(name = "IS_PROCESSED_BY_QUEUE")
		public Boolean getIsProcessedByQueue() {
			return isProcessedByQueue;
		}

		public void setIsProcessedByQueue(Boolean isProcessedByQueue) {
			this.isProcessedByQueue = isProcessedByQueue;
		}

		@Column(name = "QUEUE_STATUS")
		public String getQueueStatus() {
			return queueStatus;
		}

		public void setQueueStatus(String queueStatus) {
			this.queueStatus = queueStatus;
		}

		@Transient
		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		@Transient
		public Long getTransactionType() {
			return transactionType;
		}

		public void setTransactionType(Long transactionType) {
			this.transactionType = transactionType;
		}

		@Transient
		public String getAmountStr() {
			return amountStr;
		}

		public void setAmountStr(String amountStr) {
			this.amountStr = amountStr;
		}
}
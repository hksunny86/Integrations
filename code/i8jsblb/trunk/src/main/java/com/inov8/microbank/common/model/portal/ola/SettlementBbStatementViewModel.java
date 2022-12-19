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
import com.inov8.microbank.common.model.MnoUserModel;
import com.inov8.microbank.common.model.RegistrationStateModel;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 20, 2013 7:22:29 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SETTLEMENT_BB_STATEMENT_VIEW")
public class SettlementBbStatementViewModel extends BasePersistableModel implements Serializable
{
    private static final long serialVersionUID = 8494313691353800665L;

    private AccountModel accountIdAccountModel;
     
    private Long ledgerId;
	private Date transactionTime;
	private String transactionCode;
	private Double balanceAfterTransaction;
	private Double debitAmount;
	private Double creditAmount;
	private String transactionSummaryText;
	private String accountTittle;
	
	private String accountNumber; 

	private DateRangeHolderModel dateRangeHolderModel;

	/** default constructor */
	public SettlementBbStatementViewModel() {
	}

	public SettlementBbStatementViewModel( DateRangeHolderModel dateRangeHolderModel )
    {
        super();
        this.dateRangeHolderModel = dateRangeHolderModel;
    }

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return ledgerId;
    }

	@Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "ledgerId";
    }

	@Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&ledgerId="+ledgerId;
    }

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setLedgerId( primaryKey );
    }

    // Property accessors
    @Id
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
	@javax.persistence.Transient
	public String getAccountNumber() {
		return accountNumber;
	}
	@javax.persistence.Transient
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

}
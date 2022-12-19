package com.inov8.microbank.common.model.portal.ola;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.microbank.common.util.StringUtil;


@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "AGENT_BB_STATEMENT_VIEW")
public class AgentBBStatementViewModel extends BasePersistableModel implements Serializable
{
	private static final long serialVersionUID = 8553820577396513824L;
	private Long ledgerId;
	private Long appUserId;
	private Long accountId;
	private Date transactionTime;
	private String transactionCode;
	private Double balanceAfterTransaction;
	private Double debitAmount;
	private Double creditAmount;
	private String transactionSummaryText;

	private DateRangeHolderModel dateRangeHolderModel;

	/** default constructor */
	public AgentBBStatementViewModel()
	{
	}

	public AgentBBStatementViewModel(DateRangeHolderModel dateRangeHolderModel)
    {
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
        return "&ledgerId=" + ledgerId;
    }

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setLedgerId( primaryKey );
    }

    // Property accessors
	@Id
	@Column(name = "LEDGER_ID")
	public Long getLedgerId()
	{
		return this.ledgerId;
	}

	public void setLedgerId(Long ledgerId)
	{
		this.ledgerId = ledgerId;
	}

	@Column(name = "APP_USER_ID")
	public Long getAppUserId()
	{
		return this.appUserId;
	}

    public void setAppUserId( Long appUserId )
    {
        this.appUserId = appUserId;
    }

    @Column( name = "ACCOUNT_ID" )
    public Long getAccountId()
    {
        return this.accountId;
    }

    public void setAccountId( Long accountId )
    {
        this.accountId = accountId;
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

    @Transient
    public String getTransactionSummaryTextEscape()
    {
        String result = "";
        if(! StringUtil.isNullOrEmpty(transactionSummaryText)){
        	result = transactionSummaryText.replaceAll("<br/>", " ");
        }
    	return result;
    }

}
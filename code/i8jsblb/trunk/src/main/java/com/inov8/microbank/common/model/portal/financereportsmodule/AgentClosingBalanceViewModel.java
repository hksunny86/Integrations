package com.inov8.microbank.common.model.portal.financereportsmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author: Naseer Ullah
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "AGENT_CLOSING_BALANCE_VIEW")
public class AgentClosingBalanceViewModel extends BasePersistableModel implements Serializable, Comparable<AgentClosingBalanceViewModel>
{
    private static final long serialVersionUID = -2243060771072896189L;

 // Fields
 		private Long appUserId;
 		private String userId;
 		private Long accountId;
 		private Double balanceDisbursed;
 		private Double balanceReceived;
 		private Double endDayBalance;
 		
 		private Double startDayBalance;
 		
 		private Date statsDate;
 		private Long dailyAccountStatsId;
 		private transient String sortingBalanceProperty;
 		

	//
	public AgentClosingBalanceViewModel()
	{
	}

	@javax.persistence.Transient
	public Long getPrimaryKey()
	{
	    return getDailyAccountStatsId();
	}

	@javax.persistence.Transient
    public void setPrimaryKey( Long primaryKey )
    {
    	setDailyAccountStatsId( primaryKey );
    }

	@javax.persistence.Transient
    public String getPrimaryKeyParameter()
    {
        return "&dailyAccountStatsId=" + getDailyAccountStatsId();
    }

	@javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        return "dailyAccountStatsId";
    }

    // Property accessors
    @Column( name = "APP_USER_ID", nullable = false )
    public Long getAppUserId()
    {
        return this.appUserId;
    }

    public void setAppUserId( Long appUserId )
    {
        this.appUserId = appUserId;
    }

    @Column( name = "USER_ID", length = 50 )
    public String getUserId()
    {
        return this.userId;
    }

    public void setUserId( String userId )
    {
        this.userId = userId;
    }

    @Column( name = "ACCOUNT_ID", nullable = false )
    public Long getAccountId()
    {
        return this.accountId;
    }

    public void setAccountId( Long accountId )
    {
        this.accountId = accountId;
    }

  
    @Column( name = "BALANCE_DISBURSED" )
    public Double getBalanceDisbursed()
    {
        return this.balanceDisbursed;
    }

    public void setBalanceDisbursed( Double balanceDisbursed )
    {
        this.balanceDisbursed = balanceDisbursed;
    }

    @Column( name = "BALANCE_RECEIVED" )
    public Double getBalanceReceived()
    {
        return this.balanceReceived;
    }

    public void setBalanceReceived( Double balanceReceived )
    {
        this.balanceReceived = balanceReceived;
    }
    
  
    @Column( name = "END_DAY_BALANCE")
    public Double getEndDayBalance()
    {
    	return this.endDayBalance;
    }

    public void setEndDayBalance( Double endDayBalance )
    {
        this.endDayBalance = endDayBalance;
    }

    @Column( name = "START_DAY_BALANCE" )
    public Double getStartDayBalance()
    {
    	return this.startDayBalance;
    }
    public void setStartDayBalance( Double startDayBalance )
    {
        this.startDayBalance = startDayBalance;
    }

	@Column( name = "STATS_DATE", length = 10 )
    public Date getStatsDate()
    {
        return this.statsDate;
    }

    public void setStatsDate( Date statsDate )
    {
        this.statsDate = statsDate;
    }

   
    @Column( name = "DAILY_ACCOUNT_STATS_ID", nullable = false )
    @Id 
    public Long getDailyAccountStatsId()
    {
        return this.dailyAccountStatsId;
    }

    public void setDailyAccountStatsId( Long dailyAccountStatsId )
    {
        this.dailyAccountStatsId = dailyAccountStatsId;
    }
    
    @javax.persistence.Transient
    public String getSortingBalanceProperty() {
    	return sortingBalanceProperty;
    }

    public void setSortingBalanceProperty(String sortingBalanceProperty) {
    	this.sortingBalanceProperty = sortingBalanceProperty;
    }

	@Override
	public int compareTo(AgentClosingBalanceViewModel viewModel) {
		
		if(getSortingBalanceProperty() != null) {

			if(getSortingBalanceProperty().equals("endDayBalance")) {

				Double _balance = viewModel.getEndDayBalance();
				Double balance = this.getEndDayBalance();
				
				return (balance > _balance ? -1 : 1);
				
			} else if(getSortingBalanceProperty().equals("startDayBalance")) {

				Double _balance = viewModel.getStartDayBalance();
				Double balance = this.getStartDayBalance();

				return (balance > _balance ? -1 : 1);
			}			
		}
		
		return 0;
	}
}
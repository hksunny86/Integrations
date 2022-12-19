package com.inov8.microbank.common.model.portal.financereportsmodule;
	
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

	/**
	 * CustomerBalanceReportView entity. @author MyEclipse Persistence Tools
	 */
	
	@Entity 
	@org.hibernate.annotations.Entity(dynamicInsert = true)
	//@javax.persistence.SequenceGenerator(name = "DAILY_ACCOUNT_STATS_SEQ",sequenceName = "DAILY_ACCOUNT_STATS_SEQ",allocationSize=1)
	@Table(name = "CUSTOMER_BALANCE_REPORT")
	public class CustomerBalanceReportSummaryModel extends BasePersistableModel implements Serializable
	{
	    private static final long serialVersionUID = -4784371735445113070L; 
	    
	    // Fields
		private Long appUserId;
		private String userId;
		private Long accountId;
		private String accountNumber;
		private String balance;
		private Long statusId;
		private String firstName;
		private String middleName;
		private String lastName;
		private String address;
		private String cnic;
		private String fatherName;
		private Boolean active;
		private Boolean deleted;
		private String landlineNumber;
		private String mobileNumber;
		private String dob;
		private Double balanceDisbursed;
		private Double balanceReceived;
		private String endDayBalance;
		private String startDayBalance;
		private Date statsDate;
		private Long dailyAccountStatsId;

		//default constructor
		public CustomerBalanceReportSummaryModel() {
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

	    @Column( name = "ACCOUNT_NUMBER", nullable = false )
	    public String getAccountNumber()
	    {
	        return this.accountNumber;
	    }

	    public void setAccountNumber( String accountNumber )
	    {
	        this.accountNumber = accountNumber;
	    }

	    @Column( name = "BALANCE", nullable = false )
	    public String getBalance()
	    {
	        return this.balance;
	    }

	    public void setBalance( String balance )
	    {
	        this.balance = balance;
	    }

	    @Column( name = "STATUS_ID", nullable = false )
	    public Long getStatusId()
	    {
	        return this.statusId;
	    }

	    public void setStatusId( Long statusId )
	    {
	        this.statusId = statusId;
	    }

	    @Column( name = "FIRST_NAME", nullable = false, length = 50 )
	    public String getFirstName()
	    {
	        return this.firstName;
	    }

	    public void setFirstName( String firstName )
	    {
	        this.firstName = firstName;
	    }

	    @Column( name = "MIDDLE_NAME", nullable = false, length = 50 )
	    public String getMiddleName()
	    {
	        return this.middleName;
	    }

	    public void setMiddleName( String middleName )
	    {
	        this.middleName = middleName;
	    }

	    @Column( name = "LAST_NAME", nullable = false, length = 50 )
	    public String getLastName()
	    {
	        return this.lastName;
	    }

	    public void setLastName( String lastName )
	    {
	        this.lastName = lastName;
	    }

	    @Column( name = "ADDRESS", nullable = false, length = 250 )
	    public String getAddress()
	    {
	        return this.address;
	    }

	    public void setAddress( String address )
	    {
	        this.address = address;
	    }

	    @Column( name = "CNIC", nullable = false )
	    public String getCnic()
	    {
	        return this.cnic;
	    }

	    public void setCnic( String cnic )
	    {
	        this.cnic = cnic;
	    }

	    @Column( name = "FATHER_NAME", nullable = false, length = 100 )
	    public String getFatherName()
	    {
	        return this.fatherName;
	    }

	    public void setFatherName( String fatherName )
	    {
	        this.fatherName = fatherName;
	    }

	    @Column( name = "IS_ACTIVE" )
	    public Boolean getActive()
	    {
	        return this.active;
	    }

	    public void setActive( Boolean active )
	    {
	        this.active = active;
	    }

	    @Column( name = "IS_DELETED" )
	    public Boolean getDeleted()
	    {
	        return this.deleted;
	    }

	    public void setDeleted( Boolean deleted )
	    {
	        this.deleted = deleted;
	    }

	    @Column( name = "LANDLINE_NUMBER", nullable = false, length = 50 )
	    public String getLandlineNumber()
	    {
	        return this.landlineNumber;
	    }

	    public void setLandlineNumber( String landlineNumber )
	    {
	        this.landlineNumber = landlineNumber;
	    }

	    @Column( name = "MOBILE_NUMBER", nullable = false, length = 50 )
	    public String getMobileNumber()
	    {
	        return this.mobileNumber;
	    }

	    public void setMobileNumber( String mobileNumber )
	    {
	        this.mobileNumber = mobileNumber;
	    }

	    @Column( name = "DOB", nullable = false )
	    public String getDob()
	    {
	        return this.dob;
	    }

	    public void setDob( String dob )
	    {
	        this.dob = dob;
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

	    @Column( name = "END_DAY_BALANCE" )
	    public String getEndDayBalance()
	    {
	        return this.endDayBalance;
	    }

	    public void setEndDayBalance( String endDayBalance )
	    {
	        this.endDayBalance = endDayBalance;
	    }

	    @Column( name = "START_DAY_BALANCE" )
	    public String getStartDayBalance()
	    {
	        return this.startDayBalance;
	    }

	    public void setStartDayBalance( String startDayBalance )
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
}


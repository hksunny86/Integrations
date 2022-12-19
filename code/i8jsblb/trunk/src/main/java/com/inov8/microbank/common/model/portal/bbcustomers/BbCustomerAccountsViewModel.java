package com.inov8.microbank.common.model.portal.bbcustomers;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Date;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 6:40:52 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@XmlRootElement(name="bbCustomerAccountsViewModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@org.hibernate.annotations.Entity(mutable=false)
@Table(name = "BB_CUSTOMER_ACCOUNTS_VIEW")
public class BbCustomerAccountsViewModel extends BasePersistableModel implements Serializable, Cloneable
{
    private static final long serialVersionUID = -671020775848857623L;

    // Fields
    private Long accountId;
    private Long segmentId;
    private String segment;
	private String accountNumber;
	private String balance;
	private Boolean isActive;
	private Boolean isDeleted;
	private String firstName;
	private String lastName;
	private String cnic;
	private String mobileNumber;
	private String dob;
	private String customerAccountType;

	private String  regionName;
	private String  areaName;
	private String  cityName;
	private Date lastActivityDate;
	private Date accountOpenDate;

	private String fileURL;
	private Long taxRegimeId;
	private String taxRegimeName;

    private Date startDate;
    private Date endDate;


    @Column( name = "SEGMENT")
    public String getSegment() { return segment; }

    public void setSegment(String segment) { this.segment = segment; }

    @Column( name = "SEGMENT_ID")
	public Long getSegmentId() { return segmentId; }

    public void setSegmentId(Long segmentId) { this.segmentId = segmentId; }

	@Column( name = "REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Column( name = "AREA_NAME")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column( name = "BUSINESS_CITY_NAME")
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

    @Column( name = "LAST_TRANSACTION_DATE")
	public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    @Column( name = "ACCOUNT_OPENING_DATE")
    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    /** default constructor */
    public BbCustomerAccountsViewModel()
    {
    }

    @Transient
	@Override
    public Long getPrimaryKey()
    {
        return getAccountId();
    }

	@Override
    public void setPrimaryKey( Long primaryKey )
    {
        setAccountId( primaryKey );
    }

	@Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "accountId";
    }

	@Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&accountId="+accountId;
    }

	@Override
	public BbCustomerAccountsViewModel clone()
	{
	    BbCustomerAccountsViewModel model = new BbCustomerAccountsViewModel();
	    model.setAccountId( accountId );
	    model.setAccountNumber( accountNumber );
	    model.setBalance( balance );
	    model.setCnic( cnic );
	    model.setCustomerAccountType( customerAccountType );
	    model.setDob( dob );
	    model.setFirstName( firstName );
	    model.setIsActive( isActive );
	    model.setIsDeleted( isDeleted );
	    model.setLastName( lastName );
	    model.setMobileNumber( mobileNumber );
	    model.setLastActivityDate( lastActivityDate );
	    return model;
	}

    // Property accessors
	@Id
	@Column(name = "ACCOUNT_ID", nullable = false, precision = 10, scale = 0)
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
    
    @Transient
    public Double getBalanceNumeric()
    {
        try{
        	return Double.parseDouble(this.balance);
        }catch(Exception e){
        	return 0.00D;
        }
    }

    @Column( name = "IS_ACTIVE", precision = 1, scale = 0 )
    public Boolean getIsActive()
    {
        return this.isActive;
    }

    public void setIsActive( Boolean isActive )
    {
        this.isActive = isActive;
    }

    @Column( name = "IS_DELETED", precision = 1, scale = 0 )
    public Boolean getIsDeleted()
    {
        return this.isDeleted;
    }

    public void setIsDeleted( Boolean isDeleted )
    {
        this.isDeleted = isDeleted;
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

    @Column( name = "LAST_NAME", nullable = false, length = 50 )
    public String getLastName()
    {
        return this.lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
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

    @Column( name = "CUSTOMER_ACCOUNT_TYPE", nullable = false, length = 50 )
    public String getCustomerAccountType()
    {
        return this.customerAccountType;
    }

    public void setCustomerAccountType( String customerAccountType )
    {
        this.customerAccountType = customerAccountType;
    }

    @Transient
	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	@Column(name = "TAX_REGIME_ID")
	public Long getTaxRegimeId() {
		return taxRegimeId;
	}

	public void setTaxRegimeId(Long taxRegimeId) {
		this.taxRegimeId = taxRegimeId;
	}

	@Column(name = "TAX_REGIME")
	public String getTaxRegimeName() {
		return taxRegimeName;
	}

	public void setTaxRegimeName(String taxRegimeName) {
		this.taxRegimeName = taxRegimeName;
	}

	@Transient
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
package com.inov8.microbank.common.vo.olacustomeraccounttype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Oct 2, 2013 5:38:56 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class OlaCustomerAccountTypeVo implements Serializable
{
    private static final long serialVersionUID = 4818093638236954879L;

    private Long              customerAccountTypeId;
    private String            name;
    private Boolean           active;

    //accountyType category added by Turab
    private Boolean			  isCategoryCustomerAccountType;
    private String			  customerAccountTypeCategoryName;

    private Long              parentAccountTypeId;

    private List<LimitTypeVo> limitTypeVoList;

    //added by atif hussain
	private String accountNo;
	private String accountNick;
	
    private Boolean 		  dormantMarkingEnabled;
    private Double			  dormantTimePeriod;
	
    public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@SuppressWarnings( "unchecked" )
    public OlaCustomerAccountTypeVo()
    {
        //To initialize limitTypeVoList and add elements in it lazily(on demand). Specially required if this class is used as a Command in Controller 
        limitTypeVoList = LazyList.decorate( new ArrayList<LimitTypeVo>(),
            new Factory()
            {
                @Override
                public LimitTypeVo create()
                {
                    return new LimitTypeVo();
                }
            } );
    }

    public OlaCustomerAccountTypeVo( Long customerAccountTypeId )
    {
        this.customerAccountTypeId = customerAccountTypeId;
    }

    public Long getCustomerAccountTypeId()
    {
        return customerAccountTypeId;
    }

    public void setCustomerAccountTypeId( Long customerAccountTypeId )
    {
        this.customerAccountTypeId = customerAccountTypeId;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive( Boolean active )
    {
        this.active = active;
    }

    public List<LimitTypeVo> getLimitTypeVoList()
    {
        return limitTypeVoList;
    }

    public void setLimitTypeVoList( List<LimitTypeVo> limitTypeVoList )
    {
        this.limitTypeVoList = limitTypeVoList;
    }

	public Boolean getIsCategoryCustomerAccountType() {
		return isCategoryCustomerAccountType;
	}

	public void setIsCategoryCustomerAccountType(
			Boolean isCategoryCustomerAccountType) {
		this.isCategoryCustomerAccountType = isCategoryCustomerAccountType;
	}

	public String getCustomerAccountTypeCategoryName() {
		if ( this.getIsCategoryCustomerAccountType() != null ){
			if ( this.getIsCategoryCustomerAccountType() == false ){
				customerAccountTypeCategoryName="Customer";
			}else{
				customerAccountTypeCategoryName="Agent";
			}
		}/*else{
			customerAccountTypeCategoryName="Undefined";
		}*/
		return customerAccountTypeCategoryName;
	}

	public void setCustomerAccountTypeCategoryName(
			String customerAccountTypeCategoryName) {
		this.customerAccountTypeCategoryName = customerAccountTypeCategoryName;
	}

	public Long getParentAccountTypeId()
	{
		return parentAccountTypeId;
	}

	public void setParentAccountTypeId(Long parentAccountTypeId)
	{
		this.parentAccountTypeId = parentAccountTypeId;
	}

	public String getAccountNick() {
		return accountNick;
	}

	public void setAccountNick(String accountNick) {
		this.accountNick = accountNick;
	}

	public Boolean getDormantMarkingEnabled() {
		return dormantMarkingEnabled;
	}

	public void setDormantMarkingEnabled(Boolean dormantMarkingEnabled) {
		this.dormantMarkingEnabled = dormantMarkingEnabled;
	}

	public Double getDormantTimePeriod() {
		return dormantTimePeriod;
	}

	public void setDormantTimePeriod(Double dormantTimePeriod) {
		this.dormantTimePeriod = dormantTimePeriod;
	}
}

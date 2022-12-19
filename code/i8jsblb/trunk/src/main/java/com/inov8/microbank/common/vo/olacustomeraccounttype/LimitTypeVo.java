package com.inov8.microbank.common.vo.olacustomeraccounttype;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Oct 2, 2013 6:07:52 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class LimitTypeVo
{
    private Long limitTypeId;
    private String name;
    private Boolean applicable;
    private Double debitLimit;
    private Double creditLimit;
    private Long debitLimitId;
    private Long creditLimitId;

    public LimitTypeVo()
    {
        super();
    }

    public LimitTypeVo( Long limitTypeId, String name, Boolean applicable )
    {
        super();
        this.limitTypeId = limitTypeId;
        this.name = name;
        this.applicable = applicable;
    }

    public Long getLimitTypeId()
    {
        return limitTypeId;
    }

    public void setLimitTypeId( Long limitTypeId )
    {
        this.limitTypeId = limitTypeId;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Boolean getApplicable()
    {
        return applicable;
    }

    public void setApplicable( Boolean applicable )
    {
        this.applicable = applicable;
    }

    public Double getDebitLimit()
    {
        return debitLimit;
    }

    public void setDebitLimit( Double debitLimit )
    {
        this.debitLimit = debitLimit;
    }

    public Double getCreditLimit()
    {
        return creditLimit;
    }

    public void setCreditLimit( Double creditLimit )
    {
        this.creditLimit = creditLimit;
    }

	public Long getDebitLimitId() {
		return debitLimitId;
	}

	public void setDebitLimitId(Long debitLimitId) {
		this.debitLimitId = debitLimitId;
	}

	public Long getCreditLimitId() {
		return creditLimitId;
	}

	public void setCreditLimitId(Long creditLimitId) {
		this.creditLimitId = creditLimitId;
	}

}

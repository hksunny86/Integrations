package com.inov8.microbank.common.model.commissionmodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * CommissionShSharesView entity. @author MyEclipse Persistence Tools
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "COMMISSION_SH_SHARES_VIEW")
public class CommissionShSharesViewModel extends BasePersistableModel implements Serializable
{
    private static final long serialVersionUID = 7454876850724023089L;

    // Fields
    private Long supplierId;
	private Long productId;
	private String productName;
	private Double bank;
	private Double fed;
	private Double agent1;
	private Double agent2;
	private Double wht;
	private Double franchise1;
	private Double franchise2;
	private Long mnoId;

	/** default constructor */
	public CommissionShSharesViewModel() {
	}

	@Override
	@Transient
    public Long getPrimaryKey()
    {
        return getProductId();
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName()
    {
        return "productId";
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter()
    {
        return "&productId=" + getPrimaryKey();
    }

    @Override
    @Transient
    public void setPrimaryKey( Long primarykey )
    {
        setProductId( primarykey );
    }

    // Property accessors
    @Column(name = "SUPPLIER_ID", nullable = false, precision = 10, scale = 0)
    public Long getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId( Long supplierId )
    {
        this.supplierId = supplierId;
    }

	@Id
	@Column(name = "PRODUCT_ID", nullable = false, precision = 10, scale = 0)
	public Long getProductId() {
		return this.productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "PRODUCT_NAME", nullable = false, length = 50)
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "BANK", precision = 22, scale = 0)
	public Double getBank() {
		return this.bank;
	}

	public void setBank(Double bank) {
		this.bank = bank;
	}
	
	@Column(name = "FED", precision = 22, scale = 0)
	public Double getFed() {
		return this.fed;
	}

	public void setFed(Double fed) {
		this.fed = fed;
	}

	@Column(name = "AGENT1", precision = 22, scale = 0)
	public Double getAgent1() {
		return this.agent1;
	}

	public void setAgent1(Double agent1) {
		this.agent1 = agent1;
	}

	@Column(name = "AGENT2", precision = 22, scale = 0)
	public Double getAgent2() {
		return this.agent2;
	}

	public void setAgent2(Double agent2) {
		this.agent2 = agent2;
	}

	@Column(name = "WHT", precision = 22, scale = 0)
	public Double getWht() {
		return this.wht;
	}

	public void setWht(Double wht) {
		this.wht = wht;
	}

	@Column(name = "FRANCHISE1", precision = 22, scale = 0)
	public Double getFranchise1() {
		return this.franchise1;
	}

	public void setFranchise1(Double franchise1) {
		this.franchise1 = franchise1;
	}

	@Column(name = "FRANCHISE2", precision = 22, scale = 0)
	public Double getFranchise2() {
		return this.franchise2;
	}

	public void setFranchise2(Double franchise2) {
		this.franchise2 = franchise2;
	}

	@Column(name = "SERVICE_OP_ID")
	public Long getMnoId() {
		return mnoId;
	}

	public void setMnoId(Long mnoId) {
		this.mnoId = mnoId;
	}
}
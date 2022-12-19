package com.inov8.microbank.common.model.stakeholdermodule;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * StakehldAcctMapListViewModel entity. @author MyEclipse Persistence Tools
 */

/**
 * StakehldAcctMapListViewModel entity bean.
 *
 * @author  Hassan javaid  Inov8 Limited
 * @version Date: 2015/01/20
 *
 * @spring.bean name="StakehldAcctMapListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "STAKEHLD_ACCT_MAP_LIST_VIEW")
public class StakehldAcctMapListViewModel extends BasePersistableModel implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5647552193677592024L;
	private Long stakeholderBankInfoId;
	private String accType;
	private String blbAccNo;
	private String blbAccTitle;
	private String ofAccNo;
	private String ofAccTitle;
	private String t24AccNo;
	private String t24AccTitle;

	// Constructors

	/** default constructor */
	public StakehldAcctMapListViewModel() {
	}

	// Property accessors
	
	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getStakeholderBankInfoId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setStakeholderBankInfoId(primaryKey);
    }

	@Column(name = "STAKEHOLDER_BANK_INFO_ID", nullable = false)
	@Id 
	public Long getStakeholderBankInfoId() {
		return this.stakeholderBankInfoId;
	}

	public void setStakeholderBankInfoId(Long stakeholderBankInfoId) {
		this.stakeholderBankInfoId = stakeholderBankInfoId;
	}

	@Column(name = "ACC_TYPE")
	public String getAccType() {
		return this.accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	@Column(name = "BLB_ACC_NO")
	public String getBlbAccNo() {
		return this.blbAccNo;
	}

	public void setBlbAccNo(String blbAccNo) {
		this.blbAccNo = blbAccNo;
	}

	@Column(name = "BLB_ACC_TITLE")
	public String getBlbAccTitle() {
		return this.blbAccTitle;
	}

	public void setBlbAccTitle(String blbAccTitle) {
		this.blbAccTitle = blbAccTitle;
	}

	@Column(name = "OF_ACC_NO")
	public String getOfAccNo() {
		return this.ofAccNo;
	}

	public void setOfAccNo(String ofAccNo) {
		this.ofAccNo = ofAccNo;
	}

	@Column(name = "OF_ACC_TITLE")
	public String getOfAccTitle() {
		return this.ofAccTitle;
	}

	public void setOfAccTitle(String ofAccTitle) {
		this.ofAccTitle = ofAccTitle;
	}

	@Column(name = "T24_ACC_NO")
	public String getT24AccNo() {
		return this.t24AccNo;
	}

	public void setT24AccNo(String t24AccNo) {
		this.t24AccNo = t24AccNo;
	}

	@Column(name = "T24_ACC_TITLE")
	public String getT24AccTitle() {
		return this.t24AccTitle;
	}

	public void setT24AccTitle(String t24AccTitle) {
		this.t24AccTitle = t24AccTitle;
	}

	/**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&stakeholderBankInfoId=" + getStakeholderBankInfoId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "stakeholderBankInfoId";
			return primaryKeyFieldName;				
    }
}
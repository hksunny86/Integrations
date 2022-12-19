package com.inov8.microbank.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author Hassan Javaid
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "DIST_LEVEL_RET_CONT_VIEW")
public class DistributorLvlRetContactViewModel extends BasePersistableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -383084845969983221L;
	private Long	pk;
	private Long	distributorLevelId;
	private Long 	managingLevelId;
	private String 	parentAgentId;
	private String	parentAgentName;
	private Long	parentRetailerContactId;
	private String 	mobileNo;
	private String 	cnic;
	private Long 	retailerId;
	private Long 	ultimateManagingLevelId;
	private String	ultimateAgentName;
	private Long	ultimateRetailerContactId;
	
	
	
	public DistributorLvlRetContactViewModel() {

	}

	@Column(name = "PK", nullable = false)
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
    
    @Column(name="DISTRIBUTOR_LEVEL_ID")
	public Long getDistributorLevelId() {
		return distributorLevelId;
	}

	public void setDistributorLevelId(Long distributorLevelId) {
		this.distributorLevelId = distributorLevelId;
	}

	@Column(name="MANAGING_LEVEL_ID")
	public Long getManagingLevelId() {
		return managingLevelId;
	}

	public void setManagingLevelId(Long managingLevelId) {
		this.managingLevelId = managingLevelId;
	}

	@Column(name="PARENT_AGENT_ID")
	public String getParentAgentId() {
		return parentAgentId;
	}

	public void setParentAgentId(String parentAgentId) {
		this.parentAgentId = parentAgentId;
	}

	@Column(name="AGENT_NAME")
	public String getParentAgentName() {
		return parentAgentName;
	}

	public void setParentAgentName(String parentAgentName) {
		this.parentAgentName = parentAgentName;
	}
	
	@Column(name="PARENT_RETAILER_CONTACT_ID")
	public Long getParentRetailerContactId() {
		return parentRetailerContactId;
	}

	public void setParentRetailerContactId(Long parentRetailerContactId) {
		this.parentRetailerContactId = parentRetailerContactId;
	}

	@Column(name="MOBILE_NO")
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name="CNIC")
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	@Column(name="RETAILER_ID")
	public Long getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Long retailerId) {
		this.retailerId = retailerId;
	}

	@Column(name="ULTIMATE_MANAGING_LEVEL_ID")
	public Long getUltimateManagingLevelId() {
		return ultimateManagingLevelId;
	}

	public void setUltimateManagingLevelId(Long ultimateManagingLevelId) {
		this.ultimateManagingLevelId = ultimateManagingLevelId;
	}

	@Column(name="ULTIMATE_AGENT_NAME")
	public String getUltimateAgentName() {
		return ultimateAgentName;
	}

	public void setUltimateAgentName(String ultimateAgentName) {
		this.ultimateAgentName = ultimateAgentName;
	}

	@Column(name="ULTIMATE_RETAILER_CONTACT_ID")
	public Long getUltimateRetailerContactId() {
		return ultimateRetailerContactId;
	}

	public void setUltimateRetailerContactId(Long ultimateRetailerContactId) {
		this.ultimateRetailerContactId = ultimateRetailerContactId;
	}

}
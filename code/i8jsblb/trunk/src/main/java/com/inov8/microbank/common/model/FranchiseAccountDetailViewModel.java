package com.inov8.microbank.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name="FRANCHISE_ACCOUNT_DETAIL_VIEW")
public class FranchiseAccountDetailViewModel extends BasePersistableModel implements Serializable {
  
   private static final long serialVersionUID = -5840411592659506921L;
   private Long agentAppUserId;
   private Long agentRetailerContactId;
   private Long headRetailerContactId;
   private Long headAppUserId;
   private String frnSmartMoneyAccountId;
   
   /**
    * Default constructor.
    */
   public FranchiseAccountDetailViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAgentAppUserId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setAgentAppUserId(primaryKey);
    }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAgentAppUserId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&agentAppUserId=" + getAgentAppUserId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "agentAppUserId";
			return primaryKeyFieldName;				
    }

    @Column(name = "AGENT_APP_USER_ID")
	@Id
	public Long getAgentAppUserId() {
		return agentAppUserId;
	}

	public void setAgentAppUserId(Long agentAppUserId) {
		this.agentAppUserId = agentAppUserId;
	}

    @Column(name = "AGENT_RETAILER_CONTACT_ID")
	public Long getAgentRetailerContactId() {
		return agentRetailerContactId;
	}

	public void setAgentRetailerContactId(Long agentRetailerContactId) {
		this.agentRetailerContactId = agentRetailerContactId;
	}
    
	@Column(name = "HEAD_RETAILER_CONTACT_ID")
	public Long getHeadRetailerContactId() {
		return headRetailerContactId;
	}

	public void setHeadRetailerContactId(Long headRetailerContactId) {
		this.headRetailerContactId = headRetailerContactId;
	}

	@Column(name = "HEAD_APP_USER_ID")
	public Long getHeadAppUserId() {
		return headAppUserId;
	}

	public void setHeadAppUserId(Long headAppUserId) {
		this.headAppUserId = headAppUserId;
	}

	@Column(name = "FRN_SMART_MONEY_ACCOUNT_ID")
	public String getFrnSmartMoneyAccountId() {
		return frnSmartMoneyAccountId;
	}

	public void setFrnSmartMoneyAccountId(String frnSmartMoneyAccountId) {
		this.frnSmartMoneyAccountId = frnSmartMoneyAccountId;
	}       
}

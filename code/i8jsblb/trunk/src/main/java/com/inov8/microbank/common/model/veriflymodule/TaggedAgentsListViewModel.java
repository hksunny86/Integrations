package com.inov8.microbank.common.model.veriflymodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.DateRangeHolderModel;


@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TAGGED_AGENTS_LIST_VIEW")
public class TaggedAgentsListViewModel extends BasePersistableModel  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6005367025347151908L;
	private Long pk;
	private String mobileNumber;
	private String cnic;
	private String balance;
	private Long totalTransaction;
	private String taggedAgentBusinessName;
	private String parentAgentBusinessName;
	private Long parentId;
	private String parentAgentMobileNumber;
	private String groupTitle;
	private String taggedAgentID;
	 
	@Column(name="TAGGED_AGENT_ID")
	public String getTaggedAgentID() {
		return taggedAgentID;
	}public void setTaggedAgentID(String taggedAgentID) {
		this.taggedAgentID = taggedAgentID;
	}
	@Column(name="TAGGED_AGENT_BUSINESS_NAME")
	public String getTaggedAgentBusinessName() {
		return taggedAgentBusinessName;
	}public void setTaggedAgentBusinessName(String taggedAgentBusinessName) {
		this.taggedAgentBusinessName = taggedAgentBusinessName;
	}
	
	//@Column(name="PARENT_AGENT_BUSINESS_NAME")
	 @javax.persistence.Transient
	public String getParentAgentBusinessName() {
		return parentAgentBusinessName;
	}public void setParentAgentBusinessName(String parentAgentBusinessName) {
		this.parentAgentBusinessName = parentAgentBusinessName;
	}
	
	@Column(name="PARENT_AGENT_TAGGING_ID")
	public Long getParentId() {
		return parentId;
	}public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	//@Column(name="PARENT_AGENT_MOBILENO")
	 @javax.persistence.Transient
	public String getParentAgentMobileNumber() {
		return parentAgentMobileNumber;
	}public void setParentAgentMobileNumber(String parentAgentMobileNumber) {
		this.parentAgentMobileNumber = parentAgentMobileNumber;
	}
	
	//@Column(name="GROUP_TITLE")
	 @javax.persistence.Transient
	public String getGroupTitle() {
		return groupTitle;
	}public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	
	@Id
	@Column(name="PK")
	public Long getPk() {
		return pk;
	}public void setPk(Long pk) {
		this.pk = pk;
	}
	
	@Column(name="TAGGED_AGENT_MOBILENO")
	public String getMobileNumber() {
		return mobileNumber;
	}public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	@Column(name="TAGGED_AGENT_CNIC")
	public String getCnic() {
		return cnic;
	}public void setCnic(String cnic) {
		this.cnic = cnic;
	}
	
	@Column(name="TAGGED_AGENT_BALANCE")
	public String getBalance() {
		return balance;
	}public void setBalance(String balance) {
		this.balance = balance;
	}
	
	@Column(name="TOTAL_TAGGED_AGENT_TX")
	public Long getTotalTransaction() {
		return totalTransaction;
	}public void setTotalTransaction(Long totalTransaction) {
		this.totalTransaction = totalTransaction;
	}
		
	 @javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setPk(primaryKey);
	}
	 
	 @javax.persistence.Transient
	public Long getPrimaryKey() {
		return getPk();
	}

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
	    
	    @Transient
	    public Double getBalanceNumeric()
	    {
	        try{
	        	return Double.parseDouble(this.balance);
	        }catch(Exception e){
	        	if(balance=="")
	        	{
	        		return null;
	        	}
	        	return 0.0D;
	        }
	    }

}

package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;

/**
 * The HandlerModel entity bean.
 *
 * 
 * @version $Revision: 1.20 $, $Date: 2014/10/13 03:40:59 $
 *
 *
 * @spring.bean name="HandlerModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "HANDLER_seq",sequenceName = "HANDLER_seq", allocationSize=1)
@Table(name = "HANDLER")
public class HandlerModel extends BasePersistableModel implements Serializable{

	private static final long serialVersionUID = -5598796192759787154L;
	
	private RetailerContactModel retailerContactIdRetailerContactModel;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private OlaCustomerAccountTypeModel accountTypeIdOlaCustomerAccountTypeModel;
	
	private Long handlerId;
	private Boolean active;
	private String comments;
	private String birthPlace;
	
	private Boolean smsToAgent;
	private Boolean smsToHandler;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;
	private Boolean bvsEnable;
	private Boolean isAgentWebEnabled;
	private Boolean isAgentUssdEnabled;
	private Boolean isDebitCardFeeEnabled;

	/**
	 * Default constructor.
	 */
	public HandlerModel() {
	}  
	public HandlerModel(Long handlerId) {
		this.handlerId = handlerId;
	}   
	
	/**
	 * Return the primary key.
	 *
	 * @return Long with the primary key.
	 */
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getHandlerId();
	}

	/**
	 * Set the primary key.
	 *
	 * @param primaryKey the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setHandlerId(primaryKey);
	}

	/**
	 * Returns the value of the <code>HandlerId</code> property.
	 *
	 */
	@Column(name = "HANDLER_ID" , nullable = false )
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HANDLER_seq")
	public Long getHandlerId() {
		return handlerId;
	}

	/**
	 * Sets the value of the <code>HandlerId</code> property.
	 *
	 * @param HandlerId the value for the <code>HandlerId</code> property
	 *    
	 */

	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}

	/**
	 * Returns the value of the <code>comments</code> property.
	 *
	 */
	@Column(name = "COMMENTS" , nullable = true , length=1000 )
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the value of the <code>comments</code> property.
	 *
	 * @param comments the value for the <code>comments</code> property
	 *    
	 * @spring.validator type="required"
	 * @spring.validator type="maxlength"     
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="1000"
	 */

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Returns the value of the <code>birthPlace</code> property.
	 *
	 */
	@Column(name = "BIRTH_PLACE" , nullable = true , length=50 )
	public String getBirthPlace() {
		return birthPlace;
	}

	/**
	 * Sets the value of the <code>birthPlace</code> property.
	 *
	 * @param birthPlace the value for the <code>birthPlace</code> property
	 *    
	 * @spring.validator type="required"
	 * @spring.validator type="maxlength"     
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="50"
	 */

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	/**
	 * Returns the value of the <code>smsToAgent</code> property.
	 *
	 */
	@Column(name = "SMS_TO_AGENT" , nullable = true)
	public Boolean getSmsToAgent() {
		return smsToAgent;
	}

	/**
	 * Sets the value of the <code>smsToAgent</code> property.
	 *
	 * @param smsToAgent the value for the <code>smsToAgent</code> property
	 *    
	 * @spring.validator type="required"
	 * @spring.validator type="maxlength"     
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="250"
	 */

	public void setSmsToAgent(Boolean smsToAgent) {
		this.smsToAgent = smsToAgent;
	}

	/**
	 * Returns the value of the <code>smsToHandler</code> property.
	 *
	 */
	@Column(name = "SMS_TO_HANDLER" , nullable = true)
	public Boolean getSmsToHandler() {
		return smsToHandler;
	}

	/**
	 * Sets the value of the <code>smsToHandler</code> property.
	 *
	 * @param smsToHandler the value for the <code>smsToHandler</code> property
	 *    
	 * @spring.validator type="required"
	 * @spring.validator type="maxlength"     
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="250"
	 */

	public void setSmsToHandler(Boolean smsToHandler) {
		this.smsToHandler = smsToHandler;
	}

	/**
	 * Returns the value of the <code>active</code> property.
	 *
	 */
	@Column(name = "IS_ACTIVE" , nullable = false )
	public Boolean getActive() {
		return active;
	}

	/**
	 * Sets the value of the <code>active</code> property.
	 *
	 * @param active the value for the <code>active</code> property
	 *    
	 */

	public void setActive(Boolean active) {
		this.active = active;
	}   

	/**
	 * Returns the value of the <code>createdOn</code> property.
	 *
	 */
	@Column(name = "CREATED_ON" , nullable = false )
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * Sets the value of the <code>createdOn</code> property.
	 *
	 * @param createdOn the value for the <code>createdOn</code> property
	 *    
	 */

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Returns the value of the <code>updatedOn</code> property.
	 *
	 */
	@Column(name = "UPDATED_ON" , nullable = false )
	public Date getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * Sets the value of the <code>updatedOn</code> property.
	 *
	 * @param updatedOn the value for the <code>updatedOn</code> property
	 *    
	 */

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * Returns the value of the <code>versionNo</code> property.
	 *
	 */
	@Version 
	@Column(name = "VERSION_NO" , nullable = false )
	public Integer getVersionNo() {
		return versionNo;
	}

	/**
	 * Sets the value of the <code>versionNo</code> property.
	 *
	 * @param versionNo the value for the <code>versionNo</code> property
	 *    
	 */

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	/**
	 * Returns the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
	 *
	 * @return the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
	 *
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRIMARY_RETAILER_CONTACT_ID")    
	public RetailerContactModel getRelationRetailerContactIdRetailerContactModel(){
		return retailerContactIdRetailerContactModel;
	}

	/**
	 * Returns the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
	 *
	 * @return the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
	 *
	 */
	@javax.persistence.Transient
	public RetailerContactModel getRetailerContactIdRetailerContactModel(){
		return getRelationRetailerContactIdRetailerContactModel();
	}

	/**
	 * Sets the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
	 *
	 * @param RetailerContactModel a value for <code>retailerContactIdRetailerContactModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationRetailerContactIdRetailerContactModel(RetailerContactModel RetailerContactModel) {
		this.retailerContactIdRetailerContactModel = RetailerContactModel;
	}

	/**
	 * Sets the value of the <code>retailerContactIdRetailerContactModel</code> relation property.
	 *
	 * @param RetailerContactModel a value for <code>retailerContactIdRetailerContactModel</code>.
	 */
	@javax.persistence.Transient
	public void setRetailerContactIdRetailerContactModel(RetailerContactModel RetailerContactModel) {
		if(null != RetailerContactModel)
		{
			setRelationRetailerContactIdRetailerContactModel((RetailerContactModel)RetailerContactModel.clone());
		}      
	}


	/**
	 * Returns the value of the <code>accountTypeIdRetailerContactModel</code> relation property.
	 *
	 * @return the value of the <code>accountTypeIdRetailerContactModel</code> relation property.
	 *
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_TYPE_ID")    
	public OlaCustomerAccountTypeModel getRelationAccountTypeIdOlaCustomerAccountTypeModel(){
		return accountTypeIdOlaCustomerAccountTypeModel;
	}

	/**
	 * Returns the value of the <code>AccountTypeIdOlaCustomerAccountTypeModel</code> relation property.
	 *
	 * @return the value of the <code>AccountTypeIdOlaCustomerAccountTypeModel</code> relation property.
	 *
	 */
	@javax.persistence.Transient
	public OlaCustomerAccountTypeModel getAccountTypeIdOlaCustomerAccountTypeModel(){
		return getRelationAccountTypeIdOlaCustomerAccountTypeModel();
	}

	/**
	 * Sets the value of the <code>AccountTypeIdOlaCustomerAccountTypeModel</code> relation property.
	 *
	 * @param accountTypeIdOlaCustomerAccountTypeModel a value for <code>AccountTypeIdOlaCustomerAccountTypeModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationAccountTypeIdOlaCustomerAccountTypeModel(OlaCustomerAccountTypeModel olaCustomerAccountTypeModel) {
		this.accountTypeIdOlaCustomerAccountTypeModel = olaCustomerAccountTypeModel;
	}

	/**
	 * Sets the value of the <code>AccountTypeIdOlaCustomerAccountTypeModel</code> relation property.
	 *
	 * @param olaCustomerAccountTypeModel a value for <code>AccountTypeIdOlaCustomerAccountTypeModel</code>.
	 */
	@javax.persistence.Transient
	public void setAccountTypeIdOlaCustomerAccountTypeModel(OlaCustomerAccountTypeModel olaCustomerAccountTypeModel) {
		if(null != olaCustomerAccountTypeModel)
		{
			setRelationAccountTypeIdOlaCustomerAccountTypeModel((OlaCustomerAccountTypeModel)olaCustomerAccountTypeModel.clone());
		}      
	}



	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 * @return the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")    
	public AppUserModel getRelationUpdatedByAppUserModel(){
		return updatedByAppUserModel;
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 * @return the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 */
	@javax.persistence.Transient
	public AppUserModel getUpdatedByAppUserModel(){
		return getRelationUpdatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 * @param appUserModel a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
		this.updatedByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 * @param appUserModel a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
		if(null != appUserModel)
		{
			setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
		}      
	}


	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation property.
	 *
	 * @return the value of the <code>createdByAppUserModel</code> relation property.
	 *
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")    
	public AppUserModel getRelationCreatedByAppUserModel(){
		return createdByAppUserModel;
	}

	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation property.
	 *
	 * @return the value of the <code>createdByAppUserModel</code> relation property.
	 *
	 */
	@javax.persistence.Transient
	public AppUserModel getCreatedByAppUserModel(){
		return getRelationCreatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation property.
	 *
	 * @param appUserModel a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		this.createdByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation property.
	 *
	 * @param appUserModel a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		if(null != appUserModel)
		{
			setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
		}      
	}

	/**
	 * Returns the value of the <code>retailerContactIdRetailerContactModel</code> property.
	 *
	 */
	@javax.persistence.Transient
	public Long getRetailerContactId() {
		if (retailerContactIdRetailerContactModel != null) {
			return retailerContactIdRetailerContactModel.getRetailerContactId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>retailerContactId</code> property.
	 *
	 * @param retailerContactId the value for the <code>retailerContactId</code> property
	 * @spring.validator type="required"
	 */

	@javax.persistence.Transient
	public void setRetailerContactId(Long retailerContactId) {
		if(retailerContactId == null)
		{      
			retailerContactIdRetailerContactModel = null;
		}
		else
		{
			retailerContactIdRetailerContactModel = new RetailerContactModel();
			retailerContactIdRetailerContactModel.setRetailerContactId(retailerContactId);
		}      
	}

	/**
	 * Returns the value of the <code>concernPartnerId</code> property.
	 *
	 */
	@javax.persistence.Transient
	public Long getAccountTypeId() {
		if (accountTypeIdOlaCustomerAccountTypeModel != null) {
			return accountTypeIdOlaCustomerAccountTypeModel.getCustomerAccountTypeId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>concernPartnerId</code> property.
	 *
	 * @param customerAccountTypeId the value for the <code>concernPartnerId</code> property
	 * @spring.validator type="required"
	 */

	@javax.persistence.Transient
	public void setAccountTypeId(Long customerAccountTypeId) {
		if(customerAccountTypeId == null)
		{      
			accountTypeIdOlaCustomerAccountTypeModel = null;
		}
		else
		{
			accountTypeIdOlaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
			accountTypeIdOlaCustomerAccountTypeModel.setCustomerAccountTypeId(customerAccountTypeId);
		}      
	}



	/**
	 * Returns the value of the <code>appUserId</code> property.
	 *
	 */
	@javax.persistence.Transient
	public Long getUpdatedBy() {
		if (updatedByAppUserModel != null) {
			return updatedByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>appUserId</code> property.
	 *
	 * @param appUserId the value for the <code>appUserId</code> property
	 */

	@javax.persistence.Transient
	public void setUpdatedBy(Long appUserId) {
		if(appUserId == null)
		{      
			updatedByAppUserModel = null;
		}
		else
		{
			updatedByAppUserModel = new AppUserModel();
			updatedByAppUserModel.setAppUserId(appUserId);
		}      
	}

	/**
	 * Returns the value of the <code>appUserId</code> property.
	 *
	 */
	@javax.persistence.Transient
	public Long getCreatedBy() {
		if (createdByAppUserModel != null) {
			return createdByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>appUserId</code> property.
	 *
	 * @param appUserId the value for the <code>appUserId</code> property
	 */

	@javax.persistence.Transient
	public void setCreatedBy(Long appUserId) {
		if(appUserId == null)
		{      
			createdByAppUserModel = null;
		}
		else
		{
			createdByAppUserModel = new AppUserModel();
			createdByAppUserModel.setAppUserId(appUserId);
		}      
	}

	@Column(name="BVS_ENABLE")
	public Boolean getBvsEnable() {
		return bvsEnable;
	}
	
	public void setBvsEnable(Boolean bvsEnable) {
		this.bvsEnable = bvsEnable;
	}

	@Column(name="IS_AGENT_WEB_ENABLE")
	public Boolean getIsAgentWebEnabled() {
		return isAgentWebEnabled;
	}

	public void setIsAgentWebEnabled(Boolean isAgentWebEnabled) {
		this.isAgentWebEnabled = isAgentWebEnabled;
	}

// Debit Card FEE Enabled
	@Column(name="IS_DEBIT_CARD_FEE_ENABLE")
	public Boolean getIsDebitCardFeeEnabled() {
		return isDebitCardFeeEnabled;
	}

	public void setIsDebitCardFeeEnabled(Boolean isDebitCardFeeEnabled) {
		this.isDebitCardFeeEnabled = isDebitCardFeeEnabled;
	}



	/**
	 * Used by the display tag library for rendering a checkbox in the list.
	 * @return String with a HTML checkbox.
	 */
	@Transient
	public String getCheckbox() {
		String checkBox = "<input type=\"checkbox\" name=\"checkbox";
		checkBox += "_"+ getHandlerId();
		checkBox += "\"/>";
		return checkBox;
	}

	/**
	 * Helper method for Struts with displaytag
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&handlerId=" + getHandlerId();
		return parameters;
	}
	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName()
	{ 
		String primaryKeyFieldName = "handlerId";
		return primaryKeyFieldName;				
	}
	
	/**
	 * Helper method for Complex Example Queries
	 */
	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList()
	{
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
		AssociationModel associationModel = new AssociationModel();

		associationModel.setClassName("RetailerContactModel");
		associationModel.setPropertyName("relationRetailerContactIdRetailerContactModel");   		
		associationModel.setValue(getRelationRetailerContactIdRetailerContactModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("OlaCustomerAccountTypeModel");
		associationModel.setPropertyName("relationAccountTypeIdOlaCustomerAccountTypeModel");   		
		associationModel.setValue(getRelationAccountTypeIdOlaCustomerAccountTypeModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("ProductCatalogModel");
		associationModel.setPropertyName("productCatalogModel");   		
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
		associationModel.setValue(getRelationUpdatedByAppUserModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");   		
		associationModel.setValue(getRelationCreatedByAppUserModel());

		associationModelList.add(associationModel);

		return associationModelList;
	}

	@Column(name="IS_AGENT_USSD_ENABLE")
	public Boolean getAgentUssdEnabled() {
		return isAgentUssdEnabled;
	}

	public void setAgentUssdEnabled(Boolean agentUssdEnabled) {
		isAgentUssdEnabled = agentUssdEnabled;
	}
}

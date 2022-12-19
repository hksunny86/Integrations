package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "AGENT_TRANSFER_RULE")
@SequenceGenerator(name = "AGENT_TRANSFER_RULE_SEQ",sequenceName = "AGENT_TRANSFER_RULE_SEQ", allocationSize=2)
public class AgentTransferRuleModel extends BasePersistableModel implements Serializable
{
	private static final long serialVersionUID = -1L;

	private Long agentTransferRuleId;
	private DeviceTypeModel deviceTypeModel;
	private Long transferTypeId;
	private Long senderGroupId;
	private Long receiverGroupId;
	private Double rangeStarts;
	private Double rangeEnds;
	private Double exclusiveFixAmount;
	private Double exclusivePercentAmount;
	private Double inclusiveFixAmount;
	private Double inclusivePercentAmount;
	private Boolean thirdPartyCheck;
	
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private Date createdOn;
	private Date updatedOn;
	private Long versionNo;

	public AgentTransferRuleModel()
	{
	}

	@Transient
	@Override
	public Long getPrimaryKey()
	{
		return getAgentTransferRuleId();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName()
	{
		return "agentTransferRuleId";
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter()
	{
		return "&agentTransferRuleId="+getAgentTransferRuleId();
	}

	@Override
	public void setPrimaryKey(Long primaryKey)
	{
		setAgentTransferRuleId(primaryKey);
	}

	@Transient
	@Override
	public List<AssociationModel> getAssociationModelList()
	{
		List<AssociationModel> associationModelList = new ArrayList<>();

		AssociationModel associationModel = new AssociationModel();
		associationModel = new AssociationModel();
		associationModel.setClassName("DeviceTypeModel");
		associationModel.setPropertyName("deviceTypeModel");
		associationModel.setValue(getDeviceTypeModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("createdByAppUserModel");
		associationModel.setValue(getCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("updatedByAppUserModel");
		associationModel.setValue(getUpdatedByAppUserModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AGENT_TRANSFER_RULE_SEQ")
	@Column(name = "AGENT_TRANSFER_RULE_ID")
	public Long getAgentTransferRuleId()
	{
		return this.agentTransferRuleId;
	}

	public void setAgentTransferRuleId(Long agentTransferRuleId)
	{
		this.agentTransferRuleId = agentTransferRuleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICE_TYPE_ID")
	public DeviceTypeModel getDeviceTypeModel()
	{
		return this.deviceTypeModel;
	}

	public void setDeviceTypeModel(DeviceTypeModel deviceTypeModel)
	{
		this.deviceTypeModel = deviceTypeModel;
	}

	@Transient
	public Long getDeviceTypeId()
	{
		return deviceTypeModel == null ? null : deviceTypeModel.getDeviceTypeId();		
	}

	public void setDeviceTypeId(Long deviceTypeId)
	{
		if(deviceTypeId == null)
	    {      
			deviceTypeModel = null;
	    }
	    else
	    {
	    	deviceTypeModel = new DeviceTypeModel(deviceTypeId);
	    }
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getCreatedByAppUserModel()
	{
		return this.createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel)
	{
		this.createdByAppUserModel = createdByAppUserModel;
	}

	@Transient
	public Long getCreatedBy()
	{
		return createdByAppUserModel == null ? null : createdByAppUserModel.getAppUserId();	
	}

	public void setCreatedBy(Long appUserId)
	{
		if(appUserId == null)
	    {      
			createdByAppUserModel = null;
	    }
	    else
	    {
	    	createdByAppUserModel = new AppUserModel(appUserId);
	    }
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getUpdatedByAppUserModel()
	{
		return this.updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel)
	{
		this.updatedByAppUserModel = updatedByAppUserModel;
	}

	@Transient
	public Long getUpdatedBy()
	{
		return updatedByAppUserModel == null ? null : updatedByAppUserModel.getAppUserId();	
	}

	public void setUpdatedBy(Long appUserId)
	{
		if(appUserId == null)
	    {      
			updatedByAppUserModel = null;
	    }
	    else
	    {
	    	updatedByAppUserModel = new AppUserModel(appUserId);
	    }
	}

	@Column(name = "RANGE_STARTS")
	public Double getRangeStarts()
	{
		return this.rangeStarts;
	}

	public void setRangeStarts(Double rangeStarts)
	{
		this.rangeStarts = rangeStarts;
	}

	@Column(name = "RANGE_ENDS")
	public Double getRangeEnds()
	{
		return this.rangeEnds;
	}

	public void setRangeEnds(Double rangeEnds)
	{
		this.rangeEnds = rangeEnds;
	}

	@Column(name = "EXCLUSIVE_FIX_AMOUNT")
	public Double getExclusiveFixAmount()
	{
		return this.exclusiveFixAmount;
	}

	public void setExclusiveFixAmount(Double exclusiveFixAmount)
	{
		this.exclusiveFixAmount = exclusiveFixAmount;
	}

	@Column(name = "EXCLUSIVE_PERCENT_AMOUNT")
	public Double getExclusivePercentAmount()
	{
		return this.exclusivePercentAmount;
	}

	public void setExclusivePercentAmount(Double exclusivePercentAmount)
	{
		this.exclusivePercentAmount = exclusivePercentAmount;
	}

	@Column(name = "INCLUSIVE_FIX_AMOUNT")
	public Double getInclusiveFixAmount()
	{
		return this.inclusiveFixAmount;
	}

	public void setInclusiveFixAmount(Double inclusiveFixAmount)
	{
		this.inclusiveFixAmount = inclusiveFixAmount;
	}

	@Column(name = "INCLUSIVE_PERCENT_AMOUNT")
	public Double getInclusivePercentAmount()
	{
		return this.inclusivePercentAmount;
	}

	public void setInclusivePercentAmount(Double inclusivePercentAmount)
	{
		this.inclusivePercentAmount = inclusivePercentAmount;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn()
	{
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn()
	{
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn)
	{
		this.updatedOn = updatedOn;
	}

	@Version
	@Column(name = "VERSION_NO")
	public Long getVersionNo()
	{
		return this.versionNo;
	}

	public void setVersionNo(Long versionNo)
	{
		this.versionNo = versionNo;
	}
	
	@Column(name="THIRD_PARTY_CHECK")
	public Boolean getThirdPartyCheck() {
		return thirdPartyCheck;
	}

	public void setThirdPartyCheck(Boolean thirdPartyCheck) {
		this.thirdPartyCheck = thirdPartyCheck;
	}

    @Column(name="TRANSFER_TYPE_ID")
	public Long getTransferTypeId() {
		return transferTypeId;
	}

	public void setTransferTypeId(Long transferTypeId) {
		this.transferTypeId = transferTypeId;
	}

    @Column(name="SENDER_AGENT_GROUP_ID")
	public Long getSenderGroupId() {
		return senderGroupId;
	}

	public void setSenderGroupId(Long senderGroupId) {
		this.senderGroupId = senderGroupId;
	}

    @Column(name="RECEIVER_AGENT_GROUP_ID")
	public Long getReceiverGroupId() {
		return receiverGroupId;
	}

	public void setReceiverGroupId(Long receiverGroupId) {
		this.receiverGroupId = receiverGroupId;
	}

}
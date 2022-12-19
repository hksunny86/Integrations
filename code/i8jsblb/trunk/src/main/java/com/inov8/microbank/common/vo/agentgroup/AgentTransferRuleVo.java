package com.inov8.microbank.common.vo.agentgroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import com.inov8.microbank.common.model.AgentTransferRuleModel;
import com.inov8.microbank.common.model.DeviceTypeModel;

public class AgentTransferRuleVo implements Serializable{
	
	private static final long serialVersionUID = -1L;

	private Long agentTransferRuleId;
	private Long deviceTypeId;
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
	
	private List<AgentTransferRuleModel> agentTransferRuleModelList;

	@SuppressWarnings("unchecked")
	public AgentTransferRuleVo(){
		super();
		agentTransferRuleModelList = LazyList.decorate(new ArrayList<AgentTransferRuleModel>(), new Factory() {
			@Override
			public AgentTransferRuleModel create() {
				return new AgentTransferRuleModel();
			}
		});
	}

	public AgentTransferRuleModel getAgentTransferRuleModel(int index){
		return agentTransferRuleModelList.get(index);
	}

	public void addAgentTransferRuleModel(AgentTransferRuleModel agentTransferRuleModel){
		agentTransferRuleModelList.add(agentTransferRuleModel);
	}

	public List<AgentTransferRuleModel> getAgentTransferRuleModelList(){
		return agentTransferRuleModelList;
	}

	public void setAgentTransferRuleModelList(List<AgentTransferRuleModel> agentTransferRuleModelList){
		this.agentTransferRuleModelList = agentTransferRuleModelList;
	}

	public Long getAgentTransferRuleId() {
		return agentTransferRuleId;
	}

	public void setAgentTransferRuleId(Long agentTransferRuleId) {
		this.agentTransferRuleId = agentTransferRuleId;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public Long getTransferTypeId() {
		return transferTypeId;
	}

	public void setTransferTypeId(Long transferTypeId) {
		this.transferTypeId = transferTypeId;
	}

	public Long getSenderGroupId() {
		return senderGroupId;
	}

	public void setSenderGroupId(Long senderGroupId) {
		this.senderGroupId = senderGroupId;
	}

	public Long getReceiverGroupId() {
		return receiverGroupId;
	}

	public void setReceiverGroupId(Long receiverGroupId) {
		this.receiverGroupId = receiverGroupId;
	}

	public Double getRangeStarts() {
		return rangeStarts;
	}

	public void setRangeStarts(Double rangeStarts) {
		this.rangeStarts = rangeStarts;
	}

	public Double getRangeEnds() {
		return rangeEnds;
	}

	public void setRangeEnds(Double rangeEnds) {
		this.rangeEnds = rangeEnds;
	}

	public Double getExclusiveFixAmount() {
		return exclusiveFixAmount;
	}

	public void setExclusiveFixAmount(Double exclusiveFixAmount) {
		this.exclusiveFixAmount = exclusiveFixAmount;
	}

	public Double getExclusivePercentAmount() {
		return exclusivePercentAmount;
	}

	public void setExclusivePercentAmount(Double exclusivePercentAmount) {
		this.exclusivePercentAmount = exclusivePercentAmount;
	}

	public Double getInclusiveFixAmount() {
		return inclusiveFixAmount;
	}

	public void setInclusiveFixAmount(Double inclusiveFixAmount) {
		this.inclusiveFixAmount = inclusiveFixAmount;
	}

	public Double getInclusivePercentAmount() {
		return inclusivePercentAmount;
	}

	public void setInclusivePercentAmount(Double inclusivePercentAmount) {
		this.inclusivePercentAmount = inclusivePercentAmount;
	}

	public Boolean getThirdPartyCheck() {
		return thirdPartyCheck;
	}

	public void setThirdPartyCheck(Boolean thirdPartyCheck) {
		this.thirdPartyCheck = thirdPartyCheck;
	}

}

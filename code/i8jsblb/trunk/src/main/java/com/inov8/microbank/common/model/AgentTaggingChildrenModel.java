package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@javax.persistence.SequenceGenerator(name = "AGENT_GROUP_CHILDREN_SEQ", sequenceName = "AGENT_GROUP_CHILDREN_SEQ", allocationSize=1)
@Table(name = "AGENT_GROUP_CHILDREN")
public class AgentTaggingChildrenModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = -4949320924559852968L;

	private Long agentTaggingChildrenId;
	private Long agentTaggingId;
	private Long appUserModelId;
	private String userId;

	public AgentTaggingChildrenModel() {}

	public AgentTaggingChildrenModel(Long appUserModelId, Long agentTaggingId, String userId) {

		this.appUserModelId = appUserModelId;
		this.agentTaggingId = agentTaggingId;
		this.userId = userId;
	}
	

	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getAgentTaggingChildrenId();
	}

	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setAgentTaggingChildrenId(primaryKey);
	}

	@Id
	@Column(name = "AGENT_GROUP_CHILDREN_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENT_GROUP_CHILDREN_SEQ")
	public Long getAgentTaggingChildrenId() {
		return agentTaggingChildrenId;
	}

	public void setAgentTaggingChildrenId(Long agentTaggingChildrenId) {
		this.agentTaggingChildrenId = agentTaggingChildrenId;
	}

	/**
	 * Helper method for Struts with displaytag
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&agentTaggingChildrenId=" + getAgentTaggingChildrenId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "agentTaggingChildrenId";
		return primaryKeyFieldName;
	}
	

	@Column(name = "AGENT_GROUP_TAGGING_ID")
	public Long getAgentTaggingId() {
		return agentTaggingId;
	}
	public void setAgentTaggingId(Long agentTaggingId) {
		this.agentTaggingId = agentTaggingId;
	}

	@Column(name = "AGENT_APP_USER_ID")
	public Long getAppUserModelId() {
		return appUserModelId;
	}

	public void setAppUserModelId(Long appUserModelId) {
		this.appUserModelId = appUserModelId;
	}
	
	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}

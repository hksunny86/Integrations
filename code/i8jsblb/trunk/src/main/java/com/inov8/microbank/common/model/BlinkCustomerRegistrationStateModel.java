package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * RegistrationState entity. @author MyEclipse Persistence Tools
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "blink_registration_state_seq", sequenceName = "blink_registration_state_seq", allocationSize = 2)
@Table(name = "BLINK_REGISTRATION_STATE")
public class BlinkCustomerRegistrationStateModel extends BasePersistableModel implements Serializable {

	// Fields

	private static final long serialVersionUID = -2694580309541453092L;

	private Long registrationStateId;
	private String name;
	private String description;
	private String comments;
	private Long createdBy;
	private Long updatedBy;
	private Date createdOn;
	private Date updatedOn;
	private Long versionNo;

	// Constructors

	/** default constructor */
	public BlinkCustomerRegistrationStateModel() {
	}
	
	@Override
	@Transient
	public Long getPrimaryKey() {
		return getRegistrationStateId();
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {		
		return "registrationStateId";
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {		
		return "&registrationStateId="+getRegistrationStateId();
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		setRegistrationStateId(primaryKey);		
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="blink_registration_state_seq")
	@Column(name = "BLINK_REGISTRATION_STATE_ID")
	public Long getRegistrationStateId() {
		return this.registrationStateId;
	}

	public void setRegistrationStateId(Long registrationStateId) {
		this.registrationStateId = registrationStateId;
	}

	@Column(name = "NAME")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "COMMENTS")
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "CREATED_BY")
	public Long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_BY")
	public Long getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Version
	@Column(name = "VERSION_NO")
	public Long getVersionNo() {
		return this.versionNo;
	}

	public void setVersionNo(Long versionNo) {
		this.versionNo = versionNo;
	}

}
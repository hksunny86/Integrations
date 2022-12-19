package com.inov8.microbank.common.model.customermodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * PictureType entity. @author MyEclipse Persistence Tools
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PICTURE_TYPE_seq", sequenceName = "PICTURE_TYPE_seq", allocationSize = 2)
@Table(name = "PICTURE_TYPE")
public class PictureTypeModel extends BasePersistableModel implements Serializable {

	// Fields

	private static final long serialVersionUID = -6274544148306561366L;

	private Long pictureTypeId;
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
	public PictureTypeModel() {
	}

	@Override
	@Transient
	public Long getPrimaryKey() {
		return getPictureTypeId();
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		return "pictureTypeId";
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		return "&pictureTypeId="+getPictureTypeId();
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		setPictureTypeId(primaryKey);		
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PICTURE_TYPE_seq")
	@Column(name = "PICTURE_TYPE_ID")
	public Long getPictureTypeId() {
		return this.pictureTypeId;
	}

	public void setPictureTypeId(Long pictureTypeId) {
		this.pictureTypeId = pictureTypeId;
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
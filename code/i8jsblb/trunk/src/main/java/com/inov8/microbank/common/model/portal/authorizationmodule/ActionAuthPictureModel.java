package com.inov8.microbank.common.model.portal.authorizationmodule;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.customermodule.PictureTypeModel;


/**
 * @author Hassan Javaid
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACTION_AUTH_PICTURE_SEQ", sequenceName = "ACTION_AUTH_PICTURE_SEQ", allocationSize = 2)
@Table(name = "ACTION_AUTH_PICTURE")

public class ActionAuthPictureModel  extends BasePersistableModel implements Serializable  {

	 private static final long serialVersionUID = -6260789975171607040L;
	 
	 private Long actionAuthPictureId;
     private Long actionAuthorizationId;
     private PictureTypeModel pictureTypeModel;
     private byte[] picture;
 	 private Long createdBy;
 	 private Long updatedBy;
 	 private Date createdOn;
 	 private Date updatedOn;
 	 private Long versionNo;


    // Constructors

    /** default constructor */
    public ActionAuthPictureModel() {
    }
    
    @Override
	@Transient
	public Long getPrimaryKey() {
		return getActionAuthPictureId();
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		return "actionAuthPictureId";
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		return "&actionAuthPictureId=" + getActionAuthPictureId();
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		setActionAuthPictureId(primaryKey);
	}

	@Override
	@Transient
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
   			associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PictureTypeModel");
    	associationModel.setPropertyName("relationPictureTypeModel");   		
   		associationModel.setValue(getRelationPictureTypeModel());
   		
   		associationModelList.add(associationModel);
   		
		return associationModelList;
	}

    // Property accessors
    @Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTION_AUTH_PICTURE_SEQ")
    @Column(name="ACTION_AUTH_PICTURE_ID")
    public Long getActionAuthPictureId() {
        return this.actionAuthPictureId;
    }
       
    public void setActionAuthPictureId(Long actionAuthPictureId) {
        this.actionAuthPictureId = actionAuthPictureId;
    }
    
    @Column(name="ACTION_ATHORIZATION_ID")
    public Long getActionAuthorizationId() {
		return actionAuthorizationId;
	}

	public void setActionAuthorizationId(Long actionAuthorizationId) {
		this.actionAuthorizationId = actionAuthorizationId;
	}

	// ------------------------------------------------------------------
 	@ManyToOne(fetch = FetchType.LAZY)
 	@JoinColumn(name = "PICTURE_TYPE_ID")
 	public PictureTypeModel getRelationPictureTypeModel() {
 		return pictureTypeModel;
 	}

 	@javax.persistence.Transient
 	public void setRelationPictureTypeModel(PictureTypeModel pictureTypeModel) {
 		this.pictureTypeModel = pictureTypeModel;
 	}

 	@javax.persistence.Transient
 	public PictureTypeModel getPictureTypeModel() {
 		return pictureTypeModel;
 	}

 	@javax.persistence.Transient
 	public void setPictureTypeModel(PictureTypeModel pictureTypeModel) {
 		this.pictureTypeModel = pictureTypeModel;
 	}

 	@javax.persistence.Transient
 	public Long getPictureTypeId() {
 		if (pictureTypeModel != null) {
 			return pictureTypeModel.getPictureTypeId();
 		} else {
 			return null;
 		}
 	}

 	@javax.persistence.Transient
 	public void setPictureTypeId(Long pictureTypeId) {
 		if (pictureTypeId == null) {
 			this.pictureTypeModel = null;
 		} else {
 			pictureTypeModel = new PictureTypeModel();
 			pictureTypeModel.setPictureTypeId(pictureTypeId);
 		}
 	}

 	// ------------------------------------------------------------------
 	@Column(name = "PICTURE")
	public byte[] getPicture() {
		return this.picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
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
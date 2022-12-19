package com.inov8.microbank.common.model.customermodule;

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
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.RetailerContactModel;

/**
 * CustomerPicture entity. @author MyEclipse Persistence Tools
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CUSTOMER_PICTURE_seq", sequenceName = "CUSTOMER_PICTURE_seq", allocationSize = 2)
@Table(name = "CUSTOMER_PICTURE")
public class CustomerPictureModel extends BasePersistableModel implements
		Serializable {

	// Fields

	private static final long serialVersionUID = -6499330678059839685L;

	private Long customerPictureId;
	private Long applicantTypeId;
	private PictureTypeModel pictureTypeModel;
	private CustomerModel customerModel;
	private RetailerContactModel retailerContactModel;
	// private String picture;
	private byte[] picture;
	private Long createdBy;
	private Long updatedBy;
	private Date createdOn;
	private Date updatedOn;
	private Long versionNo;
	private Boolean discrepant;
	
	private Long	applicantDetailId;	//added by Turab

	// Constructors

	/** default constructor */
	public CustomerPictureModel() {
	}

	@Override
	@Transient
	public Long getPrimaryKey() {
		return getCustomerPictureId();
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		return "customerPictureId";
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		return "&customerPictureId=" + getCustomerPictureId();
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		setCustomerPictureId(primaryKey);
	}

	@Override
	@Transient
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CustomerModel");
    	associationModel.setPropertyName("relationCustomerModel");   		
   		associationModel.setValue(getRelationCustomerModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
		associationModel.setClassName("RetailerContactModel");
		associationModel.setPropertyName("retailerContactModel");
		associationModel.setValue(getRetailerContactModel());
		associationModelList.add(associationModel);

   			associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PictureTypeModel");
    	associationModel.setPropertyName("relationPictureTypeModel");   		
   		associationModel.setValue(getRelationPictureTypeModel());
   		
   		associationModelList.add(associationModel);
   		
		return associationModelList;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_PICTURE_seq")
	@Column(name = "CUSTOMER_PICTURE_ID")
	public Long getCustomerPictureId() {
		return this.customerPictureId;
	}

	public void setCustomerPictureId(Long customerPictureId) {
		this.customerPictureId = customerPictureId;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "RETAILER_CONTACT_ID")
	public RetailerContactModel getRetailerContactModel() {
		return retailerContactModel;
	}

	public void setRetailerContactModel(RetailerContactModel retailerContactModel) {
		this.retailerContactModel = retailerContactModel;
	}

	@Transient
	public Long getRetailerContactId(){
		if(retailerContactModel == null)
		{
			return null;
		}
		return retailerContactModel.getRetailerContactId();
	}

	public void setRetailerContactId(Long retailerContactId) {
		if (retailerContactId == null) {
			retailerContactModel = null;
		} else {
			retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerContactId(retailerContactId);
		}
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
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	public CustomerModel getRelationCustomerModel() {
		return customerModel;
	}
	
	@javax.persistence.Transient
	public void setRelationCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	@javax.persistence.Transient
	public CustomerModel getCustomerModel() {
		return customerModel;
	}

	@javax.persistence.Transient
	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	@javax.persistence.Transient
	public Long getCustomerId() {
		if (customerModel != null) {
			return customerModel.getCustomerId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setCustomerId(Long customerId) {
		if (customerId == null) {
			this.customerModel = null;
		} else {
			customerModel = new CustomerModel();
			customerModel.setCustomerId(customerId);
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

	@Column(name = "APPLICANT_TYPE_ID")
	public Long getApplicantTypeId() {
		return applicantTypeId;
	}

	public void setApplicantTypeId(Long applicantTypeId) {
		this.applicantTypeId = applicantTypeId;
	}

	@Column(name = "IS_DISCREPANT")
	public Boolean getDiscrepant() {
		return discrepant;
	}

	public void setDiscrepant(Boolean discrepant) {
		this.discrepant = discrepant;
	}

	@Column(name = "APPLICANT_DETAIL_ID")
	public Long getApplicantDetailId() {
		return applicantDetailId;
	}

	public void setApplicantDetailId(Long applicantDetailId) {
		this.applicantDetailId = applicantDetailId;
	}

}
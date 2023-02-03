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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author Rizwan Munir
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "AC_OWNERSHIP_DETAIL_SEQ", sequenceName = "AC_OWNERSHIP_DETAIL_SEQ", allocationSize = 1)
@Table(name = "AC_OWNERSHIP_DETAIL")
public class ACOwnershipDetailModel extends BasePersistableModel implements Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1322017174471713818L;
	private Long				acOwnershipDetailId;
	private CustomerModel		customerIdCustomerModel;
	private RetailerContactModel retailerContactIdRetailerContactModel;
	private Long				acOwnershipTypeId;
	private String				name;
	private Long				idDocumentType;
	private String				idDocumentNo;
	private Date				dateOfBirth;
	private Boolean				ofac;
	private Boolean				pep;
	private Boolean 			verisysDone;
	private Boolean 			screeningPerformed;
	private Boolean				isDeleted;
	private AppUserModel		createdByAppUserModel;
	private AppUserModel		updatedByAppUserModel;
	private Date				createdOn;
	private Date				updatedOn;
	private Integer				versionNo;

	public ACOwnershipDetailModel() {

	}

	@Column(name = "AC_OWNERSHIP_DETAIL_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AC_OWNERSHIP_DETAIL_SEQ")
	public Long getAcOwnershipDetailId() {
		return this.acOwnershipDetailId;
	}

	public void setAcOwnershipDetailId(Long acOwnershipDetailId) {
		if (acOwnershipDetailId != null) {
			this.acOwnershipDetailId = acOwnershipDetailId;
		}
	}

	/**
	 * Return the primary key.
	 * @return Long with the primary key.
	 */
	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return this.getAcOwnershipDetailId();
	}

	/**
	 * Set the primary key.
	 * @param primaryKey the primary key
	 */
	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		this.setAcOwnershipDetailId(primaryKey);
	}

	@Column(name = "NAME")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AC_OWNERSHIP_TYPE_ID")
	public Long getAcOwnershipTypeId() {
		return this.acOwnershipTypeId;
	}

	public void setAcOwnershipTypeId(Long acOwnershipTypeId) {
		if (acOwnershipTypeId != null) {
			this.acOwnershipTypeId = acOwnershipTypeId;
		}
	}

	@Column(name = "ID_DOCUMENT_TYPE")
	public Long getIdDocumentType() {
		return this.idDocumentType;
	}

	public void setIdDocumentType(Long idDocumentType) {
		if (idDocumentType != null) {
			this.idDocumentType = idDocumentType;
		}
	}

	@Column(name = "ID_DOCUMENT_NO")
	public String getIdDocumentNo() {
		return this.idDocumentNo;
	}

	public void setIdDocumentNo(String idDocumentNo) {
		if (idDocumentNo != null) {
			this.idDocumentNo = idDocumentNo;
		}
	}

	@Column(name = "OFAC")
	public Boolean getOfac() {
		return this.ofac;
	}

	public void setOfac(Boolean ofac) {
		if (ofac != null) {
			this.ofac = ofac;
		}
	}

	@Column(name = "PEP")
	public Boolean getPep() {
		return this.pep;
	}

	@Column(name = "DOB")
	public void setPep(Boolean pep) {
		if (pep != null) {
			this.pep = pep;
		}
	}

	@Column(name = "VERISYS")
	public Boolean getVerisysDone() {
		return verisysDone;
	}

	public void setVerisysDone(Boolean verisysDone) {
		if (verisysDone != null) {
			this.verisysDone = verisysDone;
		}
	}

	@Column(name = "SCREENING")
	public Boolean getScreeningPerformed() {
		return screeningPerformed;
	}

	public void setScreeningPerformed(Boolean screeningPerformed) {
		if (screeningPerformed != null) {
			this.screeningPerformed = screeningPerformed;
		}
	}
	
	@Column(name="DOB")
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		if (dateOfBirth != null) {
			this.dateOfBirth = dateOfBirth;
		}
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * @return the value of the <code>updatedByAppUserModel</code> relation
	 *         property.
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getRelationUpdatedByAppUserModel() {
		return this.updatedByAppUserModel;
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * @return the value of the <code>updatedByAppUserModel</code> relation
	 *         property.
	 */
	@javax.persistence.Transient
	public AppUserModel getUpdatedByAppUserModel() {
		return this.getRelationUpdatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * @param appUserModel a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
		this.updatedByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * @param appUserModel a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			this.setRelationUpdatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * @return the value of the <code>createdByAppUserModel</code> relation
	 *         property.
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getRelationCreatedByAppUserModel() {
		return this.createdByAppUserModel;
	}

	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * @return the value of the <code>createdByAppUserModel</code> relation
	 *         property.
	 */
	@javax.persistence.Transient
	public AppUserModel getCreatedByAppUserModel() {
		return this.getRelationCreatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * @param appUserModel a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		this.createdByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * @param appUserModel a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			this.setRelationCreatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	@javax.persistence.Transient
	public Long getUpdatedBy() {
		if (this.updatedByAppUserModel != null) {
			return this.updatedByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setUpdatedBy(Long appUserId) {
		if (appUserId == null) {
			this.updatedByAppUserModel = null;
		} else {
			this.updatedByAppUserModel = new AppUserModel();
			this.updatedByAppUserModel.setAppUserId(appUserId);
		}
	}

	@javax.persistence.Transient
	public Long getCreatedBy() {
		if (this.createdByAppUserModel != null) {
			return this.createdByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setCreatedBy(Long appUserId) {
		if (appUserId == null) {
			this.createdByAppUserModel = null;
		} else {
			this.createdByAppUserModel = new AppUserModel();
			this.createdByAppUserModel.setAppUserId(appUserId);
		}
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
	public Integer getVersionNo() {
		return this.versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	public CustomerModel getRelationCustomerIdCustomerModel() {
		return this.customerIdCustomerModel;
	}

	/**
	 * Returns the value of the <code>customerIdCustomerModel</code> relation
	 * property.
	 * @return the value of the <code>customerIdCustomerModel</code> relation
	 *         property.
	 */
	@javax.persistence.Transient
	public CustomerModel getCustomerIdCustomerModel() {
		return this.getRelationCustomerIdCustomerModel();
	}

	/**
	 * Sets the value of the <code>customerIdCustomerModel</code> relation
	 * property.
	 * @param customerModel a value for <code>customerIdCustomerModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationCustomerIdCustomerModel(CustomerModel customerModel) {
		this.customerIdCustomerModel = customerModel;
	}

	/**
	 * Sets the value of the <code>customerIdCustomerModel</code> relation
	 * property.
	 * @param customerModel a value for <code>customerIdCustomerModel</code>.
	 */
	@javax.persistence.Transient
	public void setCustomerIdCustomerModel(CustomerModel customerModel) {
		if (null != customerModel) {
			this.setRelationCustomerIdCustomerModel((CustomerModel) customerModel
					.clone());
		}
	}
	
		@javax.persistence.Transient
	public Long getCustomerId() {
		if (this.customerIdCustomerModel != null) {
			return this.customerIdCustomerModel.getCustomerId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setCustomerId(Long customerId) {
		if (customerId == null) {
			this.customerIdCustomerModel = null;
		} else {
			this.customerIdCustomerModel = new CustomerModel();
			this.customerIdCustomerModel.setCustomerId(customerId);
		}
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "RETAILER_CONTACT_ID")
	public RetailerContactModel getRelationRetailerContactIdRetailerContactModel() {
		return this.retailerContactIdRetailerContactModel;
	}

	/**
	 * Returns the value of the <code>retailerContactIdRetailerContactModel</code> relation
	 * property.
	 * @return the value of the <code>retailerContactIdRetailerContactModel</code> relation
	 *         property.
	 */
	@javax.persistence.Transient
	public RetailerContactModel getRetailerContactIdRetailerContactModel() {
		return this.getRelationRetailerContactIdRetailerContactModel();
	}

	/**
	 * Sets the value of the <code>retailerContactIdRetailerContactModel</code> relation
	 * property.
	 * @param retailerContactModel a value for <code>retailerContactIdRetailerContactModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationRetailerContactIdRetailerContactModel(RetailerContactModel retailerContactModel) {
		this.retailerContactIdRetailerContactModel = retailerContactModel;
	}

	/**
	 * Sets the value of the <code>retailerContactIdRetailerContactModel</code> relation
	 * property.
	 * @param retailerContactModel a value for <code>retailerContactIdRetailerContactModel</code>.
	 */
	@javax.persistence.Transient
	public void setRetailerContactIdRetailerContactModel(RetailerContactModel retailerContactModel) {
		if (null != retailerContactModel) {
			this.setRelationRetailerContactIdRetailerContactModel((RetailerContactModel) retailerContactModel
					.clone());
		}
	}
	
	@javax.persistence.Transient
	public Long getRetailerContactId() {
		if (this.retailerContactIdRetailerContactModel != null) {
			return this.retailerContactIdRetailerContactModel.getRetailerContactId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setRetailerContactId(Long retailerContactId) {
		if (retailerContactId == null) {
			this.retailerContactIdRetailerContactModel = null;
		} else {
			this.retailerContactIdRetailerContactModel = new RetailerContactModel();
			this.retailerContactIdRetailerContactModel.setRetailerContactId(retailerContactId);
		}
	}
	
	@Column(name = "IS_DELETED")
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	/**
	 * Helper method for Struts with displaytag
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&acOwnershipDetailId=" + this.getAcOwnershipDetailId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "acOwnershipDetailId";
		return primaryKeyFieldName;
	}

	/**
	 * Helper method for Complex Example Queries
	 */
	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(this.getRelationCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(this.getRelationUpdatedByAppUserModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
		associationModel.setClassName("CustomerModel");
		associationModel.setPropertyName("relationCustomerIdCustomerModel");
		associationModel.setValue(this.getRelationCustomerIdCustomerModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
		associationModel.setClassName("RetailerContactModel");
		associationModel.setPropertyName("relationRetailerContactIdRetailerContactModel");
		associationModel.setValue(this.getRelationRetailerContactIdRetailerContactModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

}
package com.inov8.microbank.common.model;

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
@javax.persistence.SequenceGenerator(name = "APPLICANT_TX_MODE_SEQ", sequenceName = "APPLICANT_TX_MODE_SEQ", allocationSize = 1)
@Table(name = "APPLICANT_TX_MODE")
public class ApplicantTxModeModel extends BasePersistableModel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1645775794230385081L;
	private Long					applicantTxModeId;
	private TransactionModeModel	txModeIdTxModeModel;
	private String					initialAppFormNo;
	private AppUserModel			createdByAppUserModel;
	private AppUserModel			updatedByAppUserModel;
	private Date					createdOn;
	private Date					updatedOn;
	private Integer					versionNo;

	public ApplicantTxModeModel() {

	}

	@Column(name = "APPLICANT_TX_MODE_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPLICANT_TX_MODE_SEQ")
	public Long getApplicantTxModeId() {
		return this.applicantTxModeId;
	}

	public void setApplicantTxModeId(Long applicantTxModeId) {
		if (applicantTxModeId != null) {
			this.applicantTxModeId = applicantTxModeId;
		}
	}

	/**
	 * Return the primary key.
	 * @return Long with the primary key.
	 */
	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return this.getApplicantTxModeId();
	}

	/**
	 * Set the primary key.
	 * @param primaryKey the primary key
	 */
	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		this.setApplicantTxModeId(primaryKey);
	}

	@Column(name = "INITIAL_APP_FORM_NUMBER")
	public String getInitialAppFormNo() {
		return this.initialAppFormNo;
	}

	public void setInitialAppFormNo(String initialAppFormNo) {
		if (initialAppFormNo != null) {
			this.initialAppFormNo = initialAppFormNo;
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANSACTION_MODE_ID")
	public TransactionModeModel getRelationTxModeIdTxModeModel() {
		return this.txModeIdTxModeModel;
	}

	@javax.persistence.Transient
	public TransactionModeModel getTxModeIdTxModeModel() {
		return this.getRelationTxModeIdTxModeModel();
	}

	@javax.persistence.Transient
	public void setRelationTxModeIdTxModeModel(
			TransactionModeModel transactionModeModel) {
		this.txModeIdTxModeModel = transactionModeModel;
	}

	@javax.persistence.Transient
	public void setTxModeIdTxModeModel(TransactionModeModel transactionModeModel) {
		if (null != transactionModeModel) {
			this.setRelationTxModeIdTxModeModel((TransactionModeModel) transactionModeModel
					.clone());
		}
	}

	@javax.persistence.Transient
	public Long getTxModeId() {
		if (this.txModeIdTxModeModel != null) {
			return this.txModeIdTxModeModel.getTransactionModeId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setTxModeId(Long txModeId) {
		if (txModeId == null) {
			this.txModeIdTxModeModel = null;
		} else {
			this.txModeIdTxModeModel = new TransactionModeModel();
			this.txModeIdTxModeModel.setTransactionModeId(txModeId);
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

	/**
	 * Helper method for Struts with displaytag
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&applicantTxModeId=" + this.getApplicantTxModeId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "applicantTxModeId";
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
		associationModel.setClassName("TransactionModeModel");
		associationModel.setPropertyName("relationTxModeIdTxModeModel");
		associationModel.setValue(this.getRelationTxModeIdTxModeModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

}
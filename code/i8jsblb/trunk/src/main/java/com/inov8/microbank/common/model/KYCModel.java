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
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author Rizwan Munir
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "KYC_SEQ", sequenceName = "KYC_SEQ", allocationSize = 1)
@Table(name = "KYC")
public class KYCModel extends BasePersistableModel {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= -7562715135432356870L;
	private Long						kycId;
	private String						initialAppFormNo;
	private FundSourceModel				fundSourceIdFundSourceModel;
	private Long						noOfTxDebit;
	private Long						noOfTxCredit;
	private Long						valOfTxDebit;
	private Long						valOfTxCredit;
	private Long						riskLevel;
	//private CustomerAccountTypeModel	acTypeACTypeModel;
	private Long						acType;
	private String						comments;
	private AppUserModel				createdByAppUserModel;
	private AppUserModel				updatedByAppUserModel;
	private Date						createdOn;
	private Date						updatedOn;
	private Integer						versionNo;
	
	private List<Long> expectedModeOfTransaction;
	public static final String KYC_MODEL_KEY = "KycModelKey";
	//public static final String LEVEL3_KYC_MODEL_KEY = "level3KYCModelKey";
	private Long usecaseId;
    private Long actionId;
    
    private Date 	startDate;
    private Date	endDate;

	public KYCModel() {

	}

	@Column(name = "KYC_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KYC_SEQ")
	public Long getKycId() {
		return this.kycId;
	}

	public void setKycId(Long kycId) {
		if (kycId != null) {
			this.kycId = kycId;
		}
	}

	/**
	 * Return the primary key.
	 * @return Long with the primary key.
	 */
	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return this.getKycId();
	}

	/**
	 * Set the primary key.
	 * @param primaryKey the primary key
	 */
	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		this.setKycId(primaryKey);
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
	@JoinColumn(name = "FUND_SOURCE_ID")
	public FundSourceModel getRelationFundSourceIdFundSourceModel() {
		return this.fundSourceIdFundSourceModel;
	}

	@javax.persistence.Transient
	public FundSourceModel getFundSourceIdFundSourceModel() {
		return this.getRelationFundSourceIdFundSourceModel();
	}

	@javax.persistence.Transient
	public void setRelationFundSourceIdFundSourceModel(
			FundSourceModel fundSourceModel) {
		this.fundSourceIdFundSourceModel = fundSourceModel;
	}

	@javax.persistence.Transient
	public void setFundSourceIdFundSourceModel(FundSourceModel fundSourceModel) {
		if (null != fundSourceModel) {
			this.setRelationFundSourceIdFundSourceModel((FundSourceModel) fundSourceModel
					.clone());
		}
	}

	@javax.persistence.Transient
	public Long getFundSourceId() {
		if (this.fundSourceIdFundSourceModel != null) {
			return this.fundSourceIdFundSourceModel.getFundSourceId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setFundSourceId(Long fundSourceId) {
		if (fundSourceId == null) {
			this.fundSourceIdFundSourceModel = null;
		} else {
			this.fundSourceIdFundSourceModel = new FundSourceModel();
			this.fundSourceIdFundSourceModel.setFundSourceId(fundSourceId);
		}
	}

	@Column(name = "NUMBER_OF_TX_DEBIT")
	public Long getNoOfTxDebit() {
		return this.noOfTxDebit;
	}

	public void setNoOfTxDebit(Long noOfTxDebit) {
		if (noOfTxDebit != null) {
			this.noOfTxDebit = noOfTxDebit;
		}
	}

	@Column(name = "NUMBER_OF_TX_CREDIT")
	public Long getNoOfTxCredit() {
		return this.noOfTxCredit;
	}

	public void setNoOfTxCredit(Long noOfTxCredit) {
		if (noOfTxCredit != null) {
			this.noOfTxCredit = noOfTxCredit;
		}
	}

	@Column(name = "VALUE_OF_TX_DEBIT")
	public Long getValOfTxDebit() {
		return this.valOfTxDebit;
	}

	public void setValOfTxDebit(Long valOfTxDebit) {
		if (valOfTxDebit != null) {
			this.valOfTxDebit = valOfTxDebit;
		}
	}

	@Column(name = "VALUE_OF_TX_CREDIT")
	public Long getValOfTxCredit() {
		return this.valOfTxCredit;
	}

	public void setValOfTxCredit(Long valOfTxCredit) {
		if (valOfTxCredit != null) {
			this.valOfTxCredit = valOfTxCredit;
		}
	}

	@Column(name = "RISK_LEVEL")
	public Long getRiskLevel() {
		return this.riskLevel;
	}

	public void setRiskLevel(Long riskLevel) {
		if (riskLevel != null) {
			this.riskLevel = riskLevel;
		}
	}

	/*@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "AC_TYPE")
	public CustomerAccountTypeModel getRelationAcTypeACTypeModel() {
		return this.acTypeACTypeModel;
	}

	@javax.persistence.Transient
	public CustomerAccountTypeModel getAcTypeACTypeModel() {
		return this.getRelationAcTypeACTypeModel();
	}

	@javax.persistence.Transient
	public void setRelationAcTypeACTypeModel(
			CustomerAccountTypeModel olaCustomerAccountTypeModel) {
		this.acTypeACTypeModel = olaCustomerAccountTypeModel;
	}

	@javax.persistence.Transient
	public void setAcTypeACTypeModel(
			CustomerAccountTypeModel customerAccountTypeModel) {
		if (null != customerAccountTypeModel) {
			this.setRelationAcTypeACTypeModel((CustomerAccountTypeModel) customerAccountTypeModel
					.clone());
		}
	}

	@javax.persistence.Transient
	public Long getAcType() {
		if (this.acTypeACTypeModel != null) {
			return this.acTypeACTypeModel.getCustomerAccountTypeId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setAcType(Long acType) {
		if (acType == null) {
			this.acTypeACTypeModel = null;
		} else {
			this.acTypeACTypeModel = new CustomerAccountTypeModel();
			this.acTypeACTypeModel.setCustomerAccountTypeId(acType);
		}
	}*/

	@Column(name = "COMMENTS")
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		if (comments != null) {
			this.comments = comments;
		}
	}
	
	@Column(name = "AC_TYPE")
	public Long getAcType() {
		return acType;
	}

	public void setAcType(Long acType) {
		this.acType = acType;
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
		parameters += "&kycId=" + this.getKycId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "kycId";
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

		/*associationModel.setClassName("CustomerAccountTypeModel");
		associationModel.setPropertyName("relationAcTypeACTypeModel");
		associationModel.setValue(this.getRelationAcTypeACTypeModel());
		associationModelList.add(associationModel);*/

		associationModel = new AssociationModel();
		associationModel.setClassName("FundSourceModel");
		associationModel.setPropertyName("relationFundSourceIdFundSourceModel");
		associationModel
				.setValue(this.getRelationFundSourceIdFundSourceModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(this.getRelationCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(this.getRelationUpdatedByAppUserModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

	

	@Transient
	public Long getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}
	@Transient
	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	@Transient
	public List<Long> getExpectedModeOfTransaction() {
		return expectedModeOfTransaction;
	}

	public void setExpectedModeOfTransaction(List<Long> expectedModeOfTransaction) {
		this.expectedModeOfTransaction = expectedModeOfTransaction;
	}

	@Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
package com.inov8.microbank.common.model.portal.bulkdisbursements;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BULK_MANUAL_ADJUSTMENT_seq",sequenceName = "BULK_MANUAL_ADJUSTMENT_seq", allocationSize=1)
@Table(name = "BULK_MANUAL_ADJUSTMENT")
@XmlRootElement(name="bulkManualAdjustmentModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class BulkManualAdjustmentModel extends BasePersistableModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7834234666249394596L;

	private Long bulkAdjustmentId;
	private String trxnId;
	private Long adjustmentType;
	private String fromAccount;
	private String fromAccountTitle;
	private String toAccount;
	private String toAccountTitle;
	private Double amount;
	private String description;
	private Boolean isProcessed;
	private String errorDescription;
	private Long batchId;
	private Date createdOn;
	private Date updatedOn;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private Long versionNo;
	private Boolean isApproved;
	private Boolean isSkipped;
	// transient property
	private MultipartFile csvFile;
	private Boolean isValid;
	private String srNo;
	private String balance;
	private String authorizerId;
	private Long actionAuthorizationId;
	private Date startDate;
	private Date endDate;
	private String valildationErrorMessage;
	private Boolean flag;



	@Id
	@Column(name="BULK_ADJUSTMENT_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BULK_MANUAL_ADJUSTMENT_seq")
	public Long getBulkAdjustmentId() {
		return bulkAdjustmentId;
	}

	public void setBulkAdjustmentId(Long bulkAdjustmentId) {
		this.bulkAdjustmentId = bulkAdjustmentId;
	}

	@Column(name="TRXN_CODE")
	public String getTrxnId() {
		return trxnId;
	}

	public void setTrxnId(String trxnId) {
		this.trxnId = trxnId;
	}

	@Column(name="ADJUSTMENT_TYPE")
	public Long getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(Long adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	@Column(name="FROM_ACCOUNT")
	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	@Column(name="TO_ACCOUNT")
	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	@Column(name="AMOUNT")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	@Column(name="ISPROCESSED")
	public Boolean getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	@Column(name="ERRORDESCRIPTION")
	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	@Column(name="BATCH_ID")
	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	@Column(name="CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name="UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getRelationCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	@javax.persistence.Transient
	public AppUserModel getCreatedByAppUserModel() {
		return getRelationCreatedByAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		this.createdByAppUserModel = appUserModel;
	}

	@javax.persistence.Transient
	public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationCreatedByAppUserModel((AppUserModel) appUserModel.clone());
		}
	}

	@javax.persistence.Transient
	public Long getCreatedBy() {
		if (createdByAppUserModel != null) {
			return createdByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setCreatedBy(Long appUserId) {
		if (appUserId == null) {
			createdByAppUserModel = null;
		} else {
			createdByAppUserModel = new AppUserModel();
			createdByAppUserModel.setAppUserId(appUserId);
		}
	}


	//******************************************************
	@javax.persistence.Transient
	public String getProcessingStatus(){
		if(null!=this.isProcessed && this.isProcessed) {
			return "Yes";
		}else {
			return "No";
		}
	}
	@javax.persistence.Transient
	public String getIsSkippedValue(){
		if(null!=this.isSkipped && this.isSkipped) {
			return "Yes";
		}else {
			return "No";
		}
	}
	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


//*****************************************************


	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getRelationUpdatedByAppUserModel(){
		return updatedByAppUserModel;
	}

	@javax.persistence.Transient
	public AppUserModel getUpdatedByAppUserModel(){
		return getRelationUpdatedByAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
		this.updatedByAppUserModel = appUserModel;
	}

	@javax.persistence.Transient
	public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
		if(null != appUserModel)
		{
			setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
		}
	}

	@javax.persistence.Transient
	public Long getUpdatedBy() {
		if (updatedByAppUserModel != null) {
			return updatedByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setUpdatedBy(Long appUserId) {
		if(appUserId == null)
		{
			updatedByAppUserModel = null;
		}
		else
		{
			updatedByAppUserModel = new AppUserModel();
			updatedByAppUserModel.setAppUserId(appUserId);
		}
	}

	@Column(name="VERSION_NO")
	public Long getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Long versionNo) {
		this.versionNo = versionNo;
	}

	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = null;

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(getRelationCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(getRelationUpdatedByAppUserModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}


	@Override
	public void setPrimaryKey(Long primaryKey) {
		setBulkAdjustmentId(primaryKey);
	}

	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getBulkAdjustmentId();
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&bulkAdjustmentId=" + getBulkAdjustmentId();
		return parameters;
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "bulkAdjustmentId";
		return primaryKeyFieldName;
	}
	@javax.persistence.Transient
	public MultipartFile getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(MultipartFile csvFile) {
		this.csvFile = csvFile;
	}

	@Transient
	public String getSrNo() {
		return srNo;
	}

	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}

	@Transient
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Column(name="ISAPPROVED")
	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	@Column(name="AUTHORIZER_ID")
	public String getAuthorizerId() {
		return authorizerId;
	}

	public void setAuthorizerId(String authorizerId) {
		this.authorizerId = authorizerId;
	}

	@Column(name="ACTION_AUTHORIZATION_ID")
	public Long getActionAuthorizationId() {
		return actionAuthorizationId;
	}

	public void setActionAuthorizationId(Long actionAuthorizationId) {
		this.actionAuthorizationId = actionAuthorizationId;
	}


	@Transient
	public String getValildationErrorMessage() {
		return valildationErrorMessage;
	}

	public void setValildationErrorMessage(String valildationErrorMessage) {
		this.valildationErrorMessage = valildationErrorMessage;
	}


	@Column(name="FROM_ACCOUNT_TITLE")
	public String getFromAccountTitle() {
		return fromAccountTitle;
	}

	public void setFromAccountTitle(String fromAccountTitle) {
		this.fromAccountTitle = fromAccountTitle;
	}

	@Column(name="TO_ACCOUNT_TITLE")
	public String getToAccountTitle() {
		return toAccountTitle;
	}

	public void setToAccountTitle(String toAccountTitle) {
		this.toAccountTitle = toAccountTitle;
	}

	@Transient
	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	@Column(name="IS_SKIPPED")
	public Boolean getIsSkipped() {
		return isSkipped;
	}

	public void setIsSkipped(Boolean isSkipped) {
		this.isSkipped = isSkipped;
	}


}

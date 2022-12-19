package com.inov8.microbank.disbursement.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.web.multipart.MultipartFile;

import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.Column;
import javax.persistence.Transient;

public class BulkDisbursementsVOModel extends BasePersistableModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5843757324831538850L;

	private Long productId;
	
	private Long serviceId;

	private Boolean limitApplicable;

	private Boolean payCashViaCnic;

	private Boolean biometricVerification;

	private Boolean fileSourceFTP = Boolean.FALSE;

	private MultipartFile csvFile;

	private Integer accountType = 0;

	private String sourceACNo;

	private String sourceACNick;

	private Date createdOn;
	
	private Date paymentDate;

	private Boolean validRecord;

	private Integer invalidCount = 0;

	private String productName = null;
	
	private String batchNumber = null;

	private String bulkDisbursementsFilePath;

	private Date uploadFromDate;

	private Date uploadToDate;

	public Double getFedPerFile() {
		return fedPerFile;
	}

	public void setFedPerFile(Double fedPerFile) {
		this.fedPerFile = fedPerFile;
	}

	private Double chargesPerFile;

	private Double fedPerFile;
	
	private AppUserModel createdByAppUserModel;

	private CopyOnWriteArrayList<String[]> recordList;

	private Long appUserTypeId;

	private String isApproved;

	public CopyOnWriteArrayList<String[]> getRecordList() {
		return recordList;
	}

	public void setRecordList(CopyOnWriteArrayList<String[]> recordList) {
		this.recordList = recordList;
	}


	public AppUserModel getCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
		this.createdByAppUserModel = createdByAppUserModel;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Boolean getLimitApplicable() {
		return limitApplicable;
	}

	public void setLimitApplicable(Boolean limitApplicable) {
		this.limitApplicable = limitApplicable;
	}

	public Boolean getPayCashViaCnic() {
		return payCashViaCnic;
	}

	public void setPayCashViaCnic(Boolean payCashViaCnic) {
		this.payCashViaCnic = payCashViaCnic;
	}

	public String getSourceACNo() {
		return sourceACNo;
	}

	public void setSourceACNo(String sourceACNo) {
		this.sourceACNo = sourceACNo;
	}

	public String getSourceACNick() {
		return sourceACNick;
	}

	public void setSourceACNick(String sourceACNick) {
		this.sourceACNick = sourceACNick;
	}

	public Boolean getBiometricVerification() {
		return biometricVerification;
	}

	public void setBiometricVerification(Boolean biometricVerification) {
		this.biometricVerification = biometricVerification;
	}

	@Transient
	public Date getUploadFromDate() {
		return uploadFromDate;
	}

	public void setUploadFromDate(Date uploadFromDate) {
		this.uploadFromDate = uploadFromDate;
	}

	@Transient
	public Date getUploadToDate() {
		return uploadToDate;
	}

	public void setUploadToDate(Date uploadToDate) {
		this.uploadToDate = uploadToDate;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	
	public Integer getInvalidCount() {
		return invalidCount;
	}

	public void setInvalidCount(Integer invalidCount) {
		this.invalidCount = invalidCount;
	}

	public Boolean getValidRecord() {
		return validRecord;
	}

	public void setValidRecord(Boolean validRecord) {
		this.validRecord = validRecord;
	}

	public MultipartFile getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(MultipartFile csvFile) {
		this.csvFile = csvFile;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Boolean getFileSourceFTP() {
		return fileSourceFTP;
	}

	public void setFileSourceFTP(Boolean fileSourceFTP) {
		this.fileSourceFTP = fileSourceFTP;
	}

	public String getBulkDisbursementsFilePath()
	{
		return bulkDisbursementsFilePath;
	}

	public void setBulkDisbursementsFilePath(String bulkDisbursementsFilePath)
	{
		this.bulkDisbursementsFilePath = bulkDisbursementsFilePath;
	}

	public Double getChargesPerFile() {
		return chargesPerFile;
	}

	public void setChargesPerFile(Double chargesPerFile) {
		this.chargesPerFile = chargesPerFile;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Long getAppUserTypeId() {
		return appUserTypeId;
	}

	public void setAppUserTypeId(Long appUserTypeId) {
		this.appUserTypeId = appUserTypeId;
	}

	@Override
	public void setPrimaryKey(Long aLong) {

	}

	@Override
	public Long getPrimaryKey() {
		return null;
	}

	@Override
	public String getPrimaryKeyParameter() {
		return null;
	}

	@Override
	public String getPrimaryKeyFieldName() {
		return null;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}
}
package com.inov8.microbank.disbursement.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;

@XmlRootElement(name="bulkDisbursementsFileInfoModel")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@javax.persistence.SequenceGenerator(name = "BULK_DISBFILE_SEQ", sequenceName = "BULK_DISBFILE_SEQ", allocationSize=1)
@Table(name = "BULK_DISBURSEMENTS_FILE_INFO")
public class BulkDisbursementsFileInfoModel extends BasePersistableModel {

	private static final long serialVersionUID = -4949320924559852968L;



	//UPDATE BULK_DISBURSEMENTS_FILE_INFO SET STATUS = 9 WHERE STATUS = 1;
	//UPDATE BULK_DISBURSEMENTS_FILE_INFO SET STATUS = 1 WHERE STATUS = 2;
	//UPDATE BULK_DISBURSEMENTS_FILE_INFO SET STATUS = 2 WHERE STATUS = 4;


	//UPDATE BULK_DISBURSEMENTS_FILE_INFO SET STATUS = 4 WHERE STATUS = 3;
	//UPDATE BULK_DISBURSEMENTS_FILE_INFO SET STATUS = 3 WHERE STATUS = 5;
/*
	public static final Integer STATUS_MARKED = 1;
	public static final Integer STATUS_PARSING = 2;
	public static final Integer STATUS_PARSED = 3;
	public static final Integer STATUS_PARSING_FAILED = 4;
	public static final Integer STATUS_SAVING_FAILED = 5;
*/

	private Long fileInfoId;

	private String batchNumber;
	private String fileName;
	private String filePath;
	private Long totalRecords;
	private Long validRecords;
	private Long inValidRecords;

	private Long createdBy;
	private Long updatedBy;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;

	private Double totalFed;
	private Double totalCharges;
	private Double totalAmount;
	private Double fedPerFile;
	private Double chargesPerFile;
	private Integer status;

	private String sumAccountNumber;
	private Boolean isCoreSumAccountNumber;

	private AppUserTypeModel appUserTypeModel;
	//	private transient AppUserModel appUserModel;
	private ServiceModel serviceModel;
	private ProductModel productModel;
	private transient String parseStatus;
	private String isApproved;
	private Boolean isValid;

	/**
	 * Return the primary key.
	 *
	 * @return Long with the primary key.
	 */
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getFileInfoId();
	}

	/**
	 * Set the primary key.
	 *
	 * @param primaryKey
	 *            the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setFileInfoId(primaryKey);
	}


	@Id
	@Column(name = "BULK_DISB_FILE_INFO_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BULK_DISBFILE_SEQ")
	public Long getFileInfoId() {
		return fileInfoId;
	}

	public void setFileInfoId(Long fileInfoId) {
		this.fileInfoId = fileInfoId;
	}

	@Transient
	public String getPrimaryKeyParameter() {
		return "&fileInfoId=" + getFileInfoId();
	}

	@Transient
	public String getPrimaryKeyFieldName() {
		return "fileInfoId";
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_PATH")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "TOTAL_RECORDS")
	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Column(name = "VALID_RECORDS")
	public Long getValidRecords() {
		return validRecords;
	}

	public void setValidRecords(Long validRecords) {
		this.validRecords = validRecords;
	}

	@Column(name = "INVALID_RECORDS")
	public Long getInValidRecords() {
		return inValidRecords;
	}

	public void setInValidRecords(Long inValidRecords) {
		this.inValidRecords = inValidRecords;
	}

	@Column(name = "BATCH_NUMBER")
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "CREATED_BY", nullable = false)
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false)
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "FILE_TOTAL_CHARGES")
	public Double getTotalCharges() {
		return totalCharges;
	}

	public void setTotalCharges(Double totalCharges) {
		this.totalCharges = totalCharges;
	}

	@Column(name = "FILE_TOTAL_FED")
	public Double getTotalFed() {
		return totalFed;
	}

	public void setTotalFed(Double totalFed) {
		this.totalFed = totalFed;
	}

	@Column(name = "FILE_TOTAL_AMOUNT")
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Version
	@Column(name = "VERSION_NO" , nullable = false )
	public Integer getVersionNo() {
		return versionNo;
	}

	@Column(name = "CHARGES_PER_FILE" )
	public Double getChargesPerFile() {
		return chargesPerFile;
	}

	public void setChargesPerFile(Double chargesPerFile) {
		this.chargesPerFile = chargesPerFile;
	}
	@Column(name = "FED_PER_FILE" )
	public Double getFedPerFile() {
		return fedPerFile;
	}

	@Column(name = "STATUS" )
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setFedPerFile(Double fedPerFile) {
		this.fedPerFile = fedPerFile;
	}

	@Column(name = "IS_CORE_SUM_ACCOUNT_NUMBER" )
	public Boolean getIsCoreSumAccountNumber() {
		return isCoreSumAccountNumber;
	}

	public void setIsCoreSumAccountNumber(Boolean isCoreSumAccountNumber) {
		this.isCoreSumAccountNumber = isCoreSumAccountNumber;
	}

	@Column(name = "SUM_ACCOUNT_NUMBER" )
	public String getSourceAccountNumber() {
		return sumAccountNumber;
	}

	public void setSourceAccountNumber(String sourceAccountNumber) {
		this.sumAccountNumber = sourceAccountNumber;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "APP_USER_TYPE_ID")
	public AppUserTypeModel getAppUserTypeModel() {
		return appUserTypeModel;
	}

	public void setAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
		this.appUserTypeModel = appUserTypeModel;
	}

	@Transient
	public Long getAppUserTypeId() {
		Long appUserTypeId = null;
		if(appUserTypeModel != null) {
			appUserTypeId = appUserTypeModel.getAppUserTypeId();
		}

		return appUserTypeId;
	}

	public void setAppUserTypeId(Long appUserTypeId) {
		if(appUserTypeModel == null)
			appUserTypeModel = new AppUserTypeModel();

		appUserTypeModel.setAppUserTypeId(appUserTypeId);
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_ID")
	public ServiceModel getServiceModel() {
		return serviceModel;
	}

	public void setServiceModel(ServiceModel serviceModel) {
		this.serviceModel = serviceModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public ProductModel getProductModel() {
		return productModel;
	}

	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}

	public void setServiceId(Long serviceId) {
		if(serviceModel == null)
			serviceModel = new ServiceModel();

		serviceModel.setServiceId(serviceId);
	}

	public void setProductId(Long productId) {
		if(productModel == null)
			productModel = new ProductModel();

		productModel.setProductId(productId);
	}

	@Transient
	public String getParseStatus() {
		return parseStatus;
	}

	public void setParseStatus(String parseStatus) {
		this.parseStatus = parseStatus;
	}

	@Column(name="ISAPPROVED")
	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	@Transient
	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
}
package com.inov8.microbank.disbursement.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;

import javax.persistence.*;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DISBURSEMENT_FILE_INFO_VIEW")
public class DisbursementFileInfoViewModel extends BasePersistableModel {

    private static final long serialVersionUID = -1427106253293008955L;

    private Long disbursementFileInfoId;
    private Long serviceId;
    private String serviceName;
    private Long productId;
    private String productName;
    private String filename;
    private String filePath;
    private String batchNumber;
    private String sourceAccountNumber;
    private Double totalAmount;
    private Double totalCharges;
    private Double totalFed;
    private Long totalRecords;
    private Long validRecords;
    private Long invalidRecords;
    private Date createdOn;
    private Long createdBy;
    private String createdByName;
    private Integer status;

    private Long appUserTypeId;
    private String appUserTypeName;

    //transient
    private Date createdOnStart;
    private Date createdOnEnd;
    private String action;

    public DisbursementFileInfoViewModel() {
    }

    @Transient
    public Long getPrimaryKey() {
        return getDisbursementFileInfoId();
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setDisbursementFileInfoId(primaryKey);
    }

    @Id
    @Column(name = "DISBURSEMENT_FILE_INFO_ID")
    public Long getDisbursementFileInfoId() {
        return disbursementFileInfoId;
    }

    public void setDisbursementFileInfoId(Long disbursementFileInfoId) {
        this.disbursementFileInfoId = disbursementFileInfoId;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @Transient
    public String getPrimaryKeyParameter() {
        return "&disbursementFileInfoId=" + getDisbursementFileInfoId();
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @Transient
    public String getPrimaryKeyFieldName() {
        return "disbursementFileInfoId";
    }

    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    @Column(name = "CREATED_BY")
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "CREATED_BY_NAME")
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @Column(name = "SERVICE_ID")
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Column(name = "SERVICE_NAME")
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Column(name = "FILE_NAME")
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Column(name = "FILE_PATH")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(name = "TOTAL_AMOUNT")
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Column(name = "TOTAL_CHARGES")
    public Double getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(Double totalCharges) {
        this.totalCharges = totalCharges;
    }

    @Column(name = "TOTAL_FED")
    public Double getTotalFed() {
        return totalFed;
    }

    public void setTotalFed(Double totalFed) {
        this.totalFed = totalFed;
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
    public Long getInvalidRecords() {
        return invalidRecords;
    }

    public void setInvalidRecords(Long invalidRecords) {
        this.invalidRecords = invalidRecords;
    }

    @Transient
    public Date getCreatedOnStart() {
        return createdOnStart;
    }

    public void setCreatedOnStart(Date createdOnStart) {
        this.createdOnStart = createdOnStart;
    }

    @Transient
    public Date getCreatedOnEnd() {
        return createdOnEnd;
    }

    public void setCreatedOnEnd(Date createdOnEnd) {
        this.createdOnEnd = createdOnEnd;
    }

    @Column(name = "SOURCE_AC_NUMBER")
    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Transient
    public String getStatusStr() {
        return DisbursementStatusConstants.map.get(status);
    }

    @Transient
    public boolean getCancel(){
        return (status == 0 || status == 2 || status == 3 ||status == 4 || status == 6 ||status == 7);
    }

    @Transient
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column(name = "APP_USER_TYPE_ID")
    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId) {
        this.appUserTypeId = appUserTypeId;
    }

    @Column(name = "APP_USER_TYPE_NAME")
    public String getAppUserTypeName() {
        return appUserTypeName;
    }

    public void setAppUserTypeName(String appUserTypeName) {
        this.appUserTypeName = appUserTypeName;
    }

}

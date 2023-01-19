package com.inov8.microbank.debitcard.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.app.model.AppModel;
import com.inov8.microbank.cardconfiguration.model.CardStateModel;
import com.inov8.microbank.cardconfiguration.model.CardStatusModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DEBIT_CARD_SEQ", sequenceName = "DEBIT_CARD_SEQ", allocationSize = 1)
@Table(name = "DEBIT_CARD")
public class DebitCardModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private Long debitCardId;
    private String debitCardEmbosingName;
    private CardStatusModel cardStatusIdCardStatusModel;
    private CardStateModel cardStateIdCardStateModel;
    private AppUserModel appUserIdAppUserModel;
    private String cnic;
    private String mobileNo;
    private String cardNo;
    private Date expiryDate;
    private String expiryDateStr;
    private Date issuanceDate;
    private Date reIssuanceDate;
    private Date activationDate;
    private String cardStatusStr;
    private Integer versionNo;
    private AppModel appIdAppModel;
    private Date importedOn;
    private Date feeDeductionDateAnnual;
    private Date feeDeductionDate;
    private String reissuance;
    private Long reIssuanceStatus;
    private String transactionCode;
    private String fee;
    private String isApprovedDenied;
    private String isReIssuanceApprovedDenied;
    private String isApproved;
    private String isReIssuanceApproved;
    private String checkedByName;
    private Long checkedById;
    private Date reissuanceRequestDate;

    private AppUserModel createdByAppUserModel;
    private Date createdOn;
    private AppUserModel updatedByAppUserModel;
    private Date updatedOn;
    private AppUserModel exportedByAppUserModel;
    private Date exportedOn;
    private SmartMoneyAccountModel smartMoneyAccountModel;
    private DebitCardMailingAddressModel debitCardMailingAddressModel;

    //New Columns For status updated dates

    private Date approvedOn;
    private Date deniedOn;
    private Date inProgressDate;
    private Date activeOn;
    private Date warmOn;
    private Date hotOn;
    private Date coldOn;
    private Date deActiveOn;
    private Date lastInstallmentDateForIssuance;
    private Date newInstallmentDateForIssuance;
    private Date lastInstallmentDateForReIssuance;
    private Date newInstallmentDateForReIssuance;
    private Date lastInstallmentDateForAnnual;
    private Date newInstallmentDateForAnnual;
    private Boolean isInstallments;
    private Long noOfInstallments;
    private Long noOfInstallmentsAnnual;
    private Long remainingNoOfInstallments;
    private Long remainingNoOfInstallmentsAnnual;

    //    private Date issuanceDate;
//    private Date reissuanceDate;
    private Date annualFeeDate;
    private String issuanceByAgent;

// Constructors

    /**
     * default constructorRE_ISSUANCE
     */
    public DebitCardModel() {
    }
    @Column(name = "RE_ISSUANCE")
    public String getReissuance() {
        return reissuance;
    }

    public void setReissuance(String reissuance) {
        this.reissuance = reissuance;
    }

    /**
     * @return Long with the primary key.
     * @Override public BasePersistableModel clone()
     * {
     * return super.clone();
     * }
     * <p/>
     * /**
     * Return the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getDebitCardId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setDebitCardId(primaryKey);
    }


    // Property accessors
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEBIT_CARD_SEQ")
    @Column(name = "DEBIT_CARD_ID", nullable = false)
    public Long getDebitCardId() {
        return this.debitCardId;
    }

    public void setDebitCardId(Long debitCardId) {
        this.debitCardId = debitCardId;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CARD_STATUS_ID")
    public CardStatusModel getRelationCardStatusIdCardStatusModel() {
        return this.cardStatusIdCardStatusModel;
    }

    @javax.persistence.Transient
    public CardStatusModel getCardStatusIdCardStatusModel() {
        return getRelationCardStatusIdCardStatusModel();
    }

    public void setRelationCardStatusIdCardStatusModel(CardStatusModel cardStatusModel) {
        this.cardStatusIdCardStatusModel = cardStatusModel;
    }

    @javax.persistence.Transient
    public void setCardStatusIdCardStatusModel(CardStatusModel cardStatusIdCardStatusModel) {
        if (cardStatusIdCardStatusModel != null) {
            setRelationCardStatusIdCardStatusModel((CardStatusModel) cardStatusIdCardStatusModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getCardStatusId() {
        Long cardStatusId = null;
        if (cardStatusIdCardStatusModel != null) {
            cardStatusId = cardStatusIdCardStatusModel.getCardStatusId();
        }
        return cardStatusId;
    }

    @javax.persistence.Transient
    public void setCardStatusId(Long cardStatusId) {
        if (null != cardStatusId) {
            this.cardStatusIdCardStatusModel = new CardStatusModel();
            cardStatusIdCardStatusModel.setCardStatusId(cardStatusId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CARD_STATE_ID")
    public CardStateModel getRelationCardStateIdCardStateModel() {
        return this.cardStateIdCardStateModel;
    }

    @javax.persistence.Transient
    public CardStateModel getCardStateIdCardStateModel() {
        return getRelationCardStateIdCardStateModel();
    }

    public void setRelationCardStateIdCardStateModel(CardStateModel cardStateModel) {
        this.cardStateIdCardStateModel = cardStateModel;
    }

    @javax.persistence.Transient
    public void setCardStateIdCardStateModel(CardStateModel cardStateIdCardStateModel) {
        if (cardStateIdCardStateModel != null) {
            setRelationCardStateIdCardStateModel((CardStateModel) cardStateIdCardStateModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getCardStateId() {
        Long cardStateId = null;
        if (cardStateIdCardStateModel != null) {
            cardStateId = cardStateIdCardStateModel.getCardStateId();
        }
        return cardStateId;
    }


    @javax.persistence.Transient
    public void setCardStateId(Long cardStateId) {
        if (null != cardStateId) {
            this.cardStateIdCardStateModel = new CardStateModel();
            cardStateIdCardStateModel.setCardStateId(cardStateId);
        }
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_ID")
    public AppUserModel getRelationAppUserIdAppUserModel() {
        return this.appUserIdAppUserModel;
    }

    @javax.persistence.Transient
    public AppUserModel getAppUserIdAppUserModel() {
        return getRelationAppUserIdAppUserModel();
    }

    public void setRelationAppUserIdAppUserModel(AppUserModel appUserIdAppUserModel) {
        this.appUserIdAppUserModel = appUserIdAppUserModel;
    }

    @javax.persistence.Transient
    public void setAppUserIdAppUserModel(AppUserModel appUserIdAppUserModel) {
        if (appUserIdAppUserModel != null) {
            setRelationAppUserIdAppUserModel((AppUserModel) appUserIdAppUserModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getAppUserId() {
        Long appUserId = null;
        if (appUserIdAppUserModel != null) {
            appUserId = appUserIdAppUserModel.getAppUserId();
        }
        return appUserId;
    }

    @javax.persistence.Transient
    public void setAppUserId(Long appUserId) {
        if (null != appUserId) {
            this.appUserIdAppUserModel = new AppUserModel();
            appUserIdAppUserModel.setAppUserId(appUserId);
        }
    }

    @Column(name = "CNIC")
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name = "CARD_NO")
    public String getCardNo() {
        return this.cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Column(name = "EXPIRY_DATE")
    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @javax.persistence.Transient
    public String getExpiryDateStr() {
        return expiryDateStr;
    }

    public void setExpiryDateStr(String expiryDateStr) {
        this.expiryDateStr = expiryDateStr;
    }

    @Column(name = "ISSUANCE_DATE")
    public Date getIssuanceDate() {
        return this.issuanceDate;
    }

    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    @Column(name = "REISSUANCE_DATE ")
    public Date getReIssuanceDate() {
        return reIssuanceDate;
    }

    public void setReIssuanceDate(Date reIssuanceDate) {
        this.reIssuanceDate = reIssuanceDate;
    }

    @Column(name = "ACTIVATION_DATE")
    public Date getActivationDate() {
        return this.activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    @javax.persistence.Transient
    public String getCardStatusStr() {
        return cardStatusStr;
    }

    public void setCardStatusStr(String cardStatusStr) {
        this.cardStatusStr = cardStatusStr;
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

    @Column(name = "EXPORTED_ON")
    public Date getExportedOn() {
        return exportedOn;
    }

    public void setExportedOn(Date exportedOn) {
        this.exportedOn = exportedOn;
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
     * Used by the display tag library for rendering a checkbox in the list.
     *
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + this.getDebitCardId();
        checkBox += "\"/>";
        return checkBox;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&debitCardId=" + getDebitCardId();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "debitCardId";
        return primaryKeyFieldName;
    }

    /////Account CreatedBy///////

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getCreatedByAppUserModel() {
        return createdByAppUserModel;
    }

    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.createdByAppUserModel = (AppUserModel) appUserModel.clone();
        }
    }

    @javax.persistence.Transient
    public Long getCreatedBy() {
        Long createdByAppUserId = null;
        if (createdByAppUserModel != null) {
            createdByAppUserId = createdByAppUserModel.getAppUserId();
        }
        return createdByAppUserId;
    }

    public void setCreatedBy(Long createdBy) {
        if (createdBy == null) {
            createdByAppUserModel = null;
        } else {
            createdByAppUserModel = new AppUserModel(createdBy);
        }
    }
    /////Account UpdatedBy///////


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }

    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.updatedByAppUserModel = (AppUserModel) appUserModel.clone();
        }
    }

    @javax.persistence.Transient
    public Long getUpdatedBy() {
        Long updatedByAppUserId = null;
        if (updatedByAppUserModel != null) {
            updatedByAppUserId = updatedByAppUserModel.getAppUserId();
        }
        return updatedByAppUserId;
    }

    public void setUpdatedBy(Long updatedBy) {
        if (updatedBy == null) {
            updatedByAppUserModel = null;
        } else {
            updatedByAppUserModel = new AppUserModel(updatedBy);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "EXPORTED_BY")
    public AppUserModel getExportedByAppUserModel() {
        return exportedByAppUserModel;
    }

    @javax.persistence.Transient
    public void setExportedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.exportedByAppUserModel = (AppUserModel) appUserModel.clone();
        }
    }

    @javax.persistence.Transient
    public Long getExportedBy() {
        Long exportedByAppUserId = null;
        if (exportedByAppUserModel != null) {
            exportedByAppUserId = exportedByAppUserModel.getAppUserId();
        }
        return exportedByAppUserId;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "SMART_MONEY_ACCOUNT_ID")
    public SmartMoneyAccountModel getSmartMoneyAccountModel() {
        return smartMoneyAccountModel;
    }

//    public void setSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
//        this.smartMoneyAccountModel = smartMoneyAccountModel;
//    }

    @javax.persistence.Transient
    public void setSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
        if (null != smartMoneyAccountModel) {
            this.smartMoneyAccountModel = (SmartMoneyAccountModel) smartMoneyAccountModel.clone();
        }
    }

    @javax.persistence.Transient
    public Long getSmartMoneyAccountId() {
        Long smartMoneyAccountId = null;
        if (smartMoneyAccountModel != null)
            smartMoneyAccountId = smartMoneyAccountModel.getSmartMoneyAccountId();

        return smartMoneyAccountId;
    }

//    public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
//        if (smartMoneyAccountModel == null)
//            smartMoneyAccountModel = new SmartMoneyAccountModel();
//
//        smartMoneyAccountModel.setSmartMoneyAccountId(smartMoneyAccountId);
//    }

    public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
        if (smartMoneyAccountId == null) {
            smartMoneyAccountModel = null;
        } else {
            smartMoneyAccountModel = new SmartMoneyAccountModel(smartMoneyAccountId);
        }
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "MAILING_ADDRESS_ID")
    public DebitCardMailingAddressModel getRelationDebitCardMailingAddressModel() {
        return debitCardMailingAddressModel;
    }

    @javax.persistence.Transient
    public DebitCardMailingAddressModel getDebitCardMailingAddressModel() {
        return getRelationDebitCardMailingAddressModel();
    }

    @javax.persistence.Transient
    public void setRelationDebitCardMailingAddressModel(DebitCardMailingAddressModel debitCardMailingAddressModel) {
        this.debitCardMailingAddressModel = debitCardMailingAddressModel;
    }

    @javax.persistence.Transient
    public void setDebitCardMailingAddressModel(DebitCardMailingAddressModel debitCardMailingAddressModel) {
        if (debitCardMailingAddressModel != null) {
            setRelationDebitCardMailingAddressModel((DebitCardMailingAddressModel) debitCardMailingAddressModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getMailingAddressId() {
        Long mailingAddressId = null;
        if (debitCardMailingAddressModel != null)
            mailingAddressId = debitCardMailingAddressModel.getMailingAddressId();

        return mailingAddressId;
    }

    public void setMailingAddressId(Long mailingAddressId) {
        if (mailingAddressId == null) {
            debitCardMailingAddressModel = null;
        } else {
            debitCardMailingAddressModel = new DebitCardMailingAddressModel(mailingAddressId);
        }
    }

    @Column(name = "EMBOSING_NAME")
    public String getDebitCardEmbosingName() {
        return debitCardEmbosingName;
    }

    public void setDebitCardEmbosingName(String debitCardEmbosingName) {
        this.debitCardEmbosingName = debitCardEmbosingName;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CHANNEL_ID")
    public AppModel getAppIdAppModel() {
        return appIdAppModel;
    }

    public void setAppIdAppModel(AppModel appIdAppModel) {
        this.appIdAppModel = appIdAppModel;
    }

    @javax.persistence.Transient
    public Long getAppId() {
        Long appId = null;
        if (appIdAppModel != null)
            appId = appIdAppModel.getAppId();

        return appId;
    }

    public void setAppId(Long appId) {
        if (appIdAppModel == null)
            appIdAppModel = new AppModel();

        appIdAppModel.setAppId(appId);
    }

    @Column(name = "IMPORTED_ON")
    public Date getImportedOn() {
        return importedOn;
    }

    public void setImportedOn(Date importedOn) {
        this.importedOn = importedOn;
    }

    @Column(name = "RE_ISSUANCE_STATUS")
    public Long getReIssuanceStatus() {
        return reIssuanceStatus;
    }

    public void setReIssuanceStatus(Long reIssuanceStatus) {
        this.reIssuanceStatus = reIssuanceStatus;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DebitCardModel model = new DebitCardModel();
        model.setDebitCardId(resultSet.getLong("DEBIT_CARD_ID"));
        model.setDebitCardEmbosingName(resultSet.getString("EMBOSING_NAME"));
        model.setCardNo(resultSet.getString("CARD_NO"));
        model.setExpiryDate(resultSet.getDate("EXPIRY_DATE"));
        model.setIssuanceDate(resultSet.getDate("ISSUANCE_DATE"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NO"));
        model.setActivationDate(resultSet.getDate("ACTIVATION_DATE"));
        model.setAppUserId(resultSet.getLong("APP_USER_ID"));
        model.setCardStatusId(resultSet.getLong("CARD_STATUS_ID"));
        model.setCardStateId(resultSet.getLong("CARD_STATE_ID"));
        model.setCreatedBy(resultSet.getLong("CREATED_BY"));
        model.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        model.setFeeDeductionDate(resultSet.getDate("FEE_DEDUCTION_DATE"));
        model.setAnnualFeeDate(resultSet.getDate("ANNUAL_FEE_DATE"));
        model.setSmartMoneyAccountId(resultSet.getLong("SMART_MONEY_ACCOUNT_ID"));
        model.setMailingAddressId(resultSet.getLong("MAILING_ADDRESS_ID"));
        model.setAppId(resultSet.getLong("CHANNEL_ID"));
        model.setImportedOn(resultSet.getTimestamp("IMPORTED_ON"));
        model.setReissuance(resultSet.getString("RE_ISSUANCE"));
        model.setReIssuanceStatus(resultSet.getLong("RE_ISSUANCE_STATUS"));
        model.setNewInstallmentDateForReIssuance(resultSet.getDate("NEW_INSTALL_DATE_REISSUANCE"));
        model.setLastInstallmentDateForReIssuance(resultSet.getDate("LAST_INSTALL_DATE_REISSUANCE"));
        model.setLastInstallmentDateForIssuance(resultSet.getDate("LAST_INSTALLMENT_DATE_ISSUANCE"));
        model.setNewInstallmentDateForIssuance(resultSet.getDate("NEW_INSTALLMENT_DATE_ISSUANCE"));
        model.setLastInstallmentDateForAnnual(resultSet.getDate("LAST_INSTALLMENT_DATE_ANNUAL"));
        model.setNewInstallmentDateForAnnual(resultSet.getDate("NEW_INSTALLMENT_DATE_ANNUAL"));
        model.setRemainingNoOfInstallments(resultSet.getLong("REMAINING_NO_OF_INSTALLMENTS"));
        model.setNoOfInstallments(resultSet.getLong("NO_OF_INSTALLMENT"));
        model.setRemainingNoOfInstallmentsAnnual(resultSet.getLong("REMAINING_NO_OF_INSTALL_ANNUAL"));
        return model;
    }

    @Column(name = "FEE_DEDUCTION_DATE")
    public Date getFeeDeductionDate() {
        return feeDeductionDate;
    }

    public void setFeeDeductionDate(Date feeDeductionDate) {
        this.feeDeductionDate = feeDeductionDate;
    }

    @Column(name = "TRANSACTION_CODE")
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Column(name = "IS_APPROVED_DENIED")
    public String getIsApprovedDenied() {
        return isApprovedDenied;
    }

    public void setIsApprovedDenied(String isApprovedDenied) {
        this.isApprovedDenied = isApprovedDenied;
    }

    @Column(name = "IS_REISSAUNCE_APPROVED_DENIED")
    public String getIsReIssuanceApprovedDenied() {
        return isReIssuanceApprovedDenied;
    }

    public void setIsReIssuanceApprovedDenied(String isReIssuanceApprovedDenied) {
        this.isReIssuanceApprovedDenied = isReIssuanceApprovedDenied;
    }

    @Column(name = "IS_APPROVED")
    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    @Column(name = "IS_REISSUANCE_APPROVED")
    public String getIsReIssuanceApproved() {
        return isReIssuanceApproved;
    }

    public void setIsReIssuanceApproved(String isReIssuanceApproved) {
        this.isReIssuanceApproved = isReIssuanceApproved;
    }

    @Column(name = "CHECKED_BY_NAME")
    public String getCheckedByName() {
        return checkedByName;
    }

    public void setCheckedByName(String checkedByName) {
        this.checkedByName = checkedByName;
    }

    @Column(name = "CHECKED_BY")
    public Long getCheckedById() {
        return checkedById;
    }

    public void setCheckedById(Long checkedById) {
        this.checkedById = checkedById;
    }

    @Column(name = "FEE")
    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Column(name = "REISSUANCE_REQUEST_DATE")
    public Date getReissuanceRequestDate() {
        return reissuanceRequestDate;
    }

    public void setReissuanceRequestDate(Date reissuanceRequestDate) {
        this.reissuanceRequestDate = reissuanceRequestDate;
    }

    @Column(name = "APPROVED_ON")
    public Date getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(Date approvedOn) {
        this.approvedOn = approvedOn;
    }
    @Column(name = "DENIED_ON")
    public Date getDeniedOn() {
        return deniedOn;
    }

    public void setDeniedOn(Date deniedOn) {
        this.deniedOn = deniedOn;
    }

    @Column(name = "INPROGRESS_DATE")
    public Date getInProgressDate() {
        return inProgressDate;
    }

    public void setInProgressDate(Date inProgressDate) {
        this.inProgressDate = inProgressDate;
    }

    @Column(name = "ACTIVE_ON")
    public Date getActiveOn() {
        return activeOn;
    }

    public void setActiveOn(Date activeOn) {
        this.activeOn = activeOn;
    }

    @Column(name = "WARM_ON")
    public Date getWarmOn() {
        return warmOn;
    }

    public void setWarmOn(Date warmOn) {
        this.warmOn = warmOn;
    }

    @Column(name = "HOT_ON")
    public Date getHotOn() {
        return hotOn;
    }

    public void setHotOn(Date hotOn) {
        this.hotOn = hotOn;
    }

    @Column(name = "COLD_ON")
    public Date getColdOn() {
        return coldOn;
    }

    public void setColdOn(Date coldOn) {
        this.coldOn = coldOn;
    }

    @Column(name = "DEACTIVE_ON")
    public Date getDeActiveOn() {
        return deActiveOn;
    }

    public void setDeActiveOn(Date deActiveOn) {
        this.deActiveOn = deActiveOn;
    }

    @Column(name = "IS_INSTALLMENT")
    public Boolean getIsInstallments() {
        return isInstallments;
    }

    public void setIsInstallments(Boolean isInstallments) {
        this.isInstallments = isInstallments;
    }

    @Column(name = "NO_OF_INSTALLMENT")
    public Long getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(Long noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }

    @Column(name = "REMAINING_NO_OF_INSTALLMENTS")
    public Long getRemainingNoOfInstallments() {
        return remainingNoOfInstallments;
    }

    public void setRemainingNoOfInstallments(Long remainingNoOfInstallments) {
        this.remainingNoOfInstallments = remainingNoOfInstallments;
    }

    @Column(name = "ANNUAL_FEE_DATE")
    public Date getAnnualFeeDate() {
        return annualFeeDate;
    }

    public void setAnnualFeeDate(Date annualFeeDate) {
        this.annualFeeDate = annualFeeDate;
    }

    @Column(name = "ISSUANCE_BY_AGENT")
    public String getIssuanceByAgent() {
        return issuanceByAgent;
    }

    public void setIssuanceByAgent(String issuanceByAgent) {
        this.issuanceByAgent = issuanceByAgent;
    }

    @Column(name = "LAST_INSTALLMENT_DATE_ISSUANCE")
    public Date getLastInstallmentDateForIssuance() {
        return lastInstallmentDateForIssuance;
    }

    public void setLastInstallmentDateForIssuance(Date lastInstallmentDateForIssuance) {
        this.lastInstallmentDateForIssuance = lastInstallmentDateForIssuance;
    }

    @Column(name = "NEW_INSTALLMENT_DATE_ISSUANCE")
    public Date getNewInstallmentDateForIssuance() {
        return newInstallmentDateForIssuance;
    }

    public void setNewInstallmentDateForIssuance(Date newInstallmentDateForIssuance) {
        this.newInstallmentDateForIssuance = newInstallmentDateForIssuance;
    }

    @Column(name = "LAST_INSTALL_DATE_REISSUANCE")
    public Date getLastInstallmentDateForReIssuance() {
        return lastInstallmentDateForReIssuance;
    }

    public void setLastInstallmentDateForReIssuance(Date lastInstallmentDateForReIssuance) {
        this.lastInstallmentDateForReIssuance = lastInstallmentDateForReIssuance;
    }

    @Column(name = "NEW_INSTALL_DATE_REISSUANCE")
    public Date getNewInstallmentDateForReIssuance() {
        return newInstallmentDateForReIssuance;
    }

    public void setNewInstallmentDateForReIssuance(Date newInstallmentDateForReIssuance) {
        this.newInstallmentDateForReIssuance = newInstallmentDateForReIssuance;
    }

    @Column(name = "LAST_INSTALLMENT_DATE_ANNUAL")
    public Date getLastInstallmentDateForAnnual() {
        return lastInstallmentDateForAnnual;
    }

    public void setLastInstallmentDateForAnnual(Date lastInstallmentDateForAnnual) {
        this.lastInstallmentDateForAnnual = lastInstallmentDateForAnnual;
    }

    @Column(name = "NEW_INSTALLMENT_DATE_ANNUAL")
    public Date getNewInstallmentDateForAnnual() {
        return newInstallmentDateForAnnual;
    }

    public void setNewInstallmentDateForAnnual(Date newInstallmentDateForAnnual) {
        this.newInstallmentDateForAnnual = newInstallmentDateForAnnual;
    }

    @Column(name = "NO_OF_INSTALLMENTS_ANNUAL")
    public Long getNoOfInstallmentsAnnual() {
        return noOfInstallmentsAnnual;
    }

    public void setNoOfInstallmentsAnnual(Long noOfInstallmentsAnnual) {
        this.noOfInstallmentsAnnual = noOfInstallmentsAnnual;
    }

    @Column(name = "REMAINING_NO_OF_INSTALL_ANNUAL")
    public Long getRemainingNoOfInstallmentsAnnual() {
        return remainingNoOfInstallmentsAnnual;
    }

    public void setRemainingNoOfInstallmentsAnnual(Long remainingNoOfInstallmentsAnnual) {
        this.remainingNoOfInstallmentsAnnual = remainingNoOfInstallmentsAnnual;
    }

    @Column(name = "FEE_DEDUCTION_DATE_ANNUAL")
    public Date getFeeDeductionDateAnnual() {
        return feeDeductionDateAnnual;
    }

    public void setFeeDeductionDateAnnual(Date feeDeductionDateAnnual) {
        this.feeDeductionDateAnnual = feeDeductionDateAnnual;
    }
}

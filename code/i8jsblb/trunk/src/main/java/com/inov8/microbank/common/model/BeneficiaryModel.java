package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Muhammad Sajid on 1/24/2017.
 */

@Entity
@javax.persistence.SequenceGenerator(name = "BENEFICIARY_SEQ",sequenceName = "BENEFICIARY_SEQ",allocationSize=1)
@Table(name = "BENEFICIARY")
public class BeneficiaryModel extends BasePersistableModel implements Serializable {


    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private CustomerModel customerIdCustomerModel;

    private Long beneficiaryId;
    private String beneficiaryAccountNick;
    private String beneficiaryAccountNo;
    private String beneficiaryTitle;
    private String beneficiaryBranchName;
    private String beneficiaryBankName;
    private String beneficiaryBankCode;
    private String beneficiaryEmail;
    private String transactionType;

    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private String comments;

    private String hostBeneficiaryId;

    /**
     * Default constructor.
     */
    public BeneficiaryModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getBeneficiaryId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setBeneficiaryId(primaryKey);
    }

    /**
     * Returns the value of the <code>beneficiaryId</code> property.
     */
    @Column(name = "BENEFICIARY_ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BENEFICIARY_SEQ")
    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    /**
     * Sets the value of the <code>beneficiaryId</code> property.
     *
     * @param beneficiaryId the value for the <code>beneficiaryId</code> property
     */

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }


    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&beneficiaryId=" + getBeneficiaryId();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "beneficiaryId";
        return primaryKeyFieldName;
    }


    /**
     * Returns the value of the <code>beneficiaryAccountNo</code> property.
     */
    @Column(name = "BENEFICIARY_ACCOUNT_NO", nullable = false, length = 50)
    public String getBeneficiaryAccountNo() {
        return beneficiaryAccountNo;
    }

    public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
        this.beneficiaryAccountNo = beneficiaryAccountNo;
    }

    /**
     * Returns the value of the <code>beneficiaryTitle</code> property.
     */
    @Column(name = "BENEFICIARY_TITLE", nullable = true, length = 100)
    public String getBeneficiaryTitle() {
        return beneficiaryTitle;
    }

    public void setBeneficiaryTitle(String beneficiaryTitle) {
        this.beneficiaryTitle = beneficiaryTitle;
    }

    /**
     * Returns the value of the <code>beneficiaryBranchName</code> property.
     */
    @Column(name = "BENEFICIARY_BRANCH_NAME", nullable = true, length = 100)
    public String getBeneficiaryBranchName() {
        return beneficiaryBranchName;
    }

    public void setBeneficiaryBranchName(String beneficiaryBranchName) {
        this.beneficiaryBranchName = beneficiaryBranchName;
    }

    /**
     * Returns the value of the <code>beneficiaryBankCode</code> property.
     */
    @Column(name = "BENEFICIARY_BANK_CODE", nullable = true, length = 100)
    public String getBeneficiaryBankCode() {
        return beneficiaryBankCode;
    }

    public void setBeneficiaryBankCode(String beneficiaryBankCode) {
        this.beneficiaryBankCode = beneficiaryBankCode;
    }

    /**
     * Returns the value of the <code>transactionType</code> property.
     */
    @Column(name = "TRANSACTION_TYPE", nullable = true, length = 20)
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }


    /**
     * Returns the value of the <code>hostBeneficiaryId</code> property.
     */
    @Column(name = "HOST_BENEFICIARY_ID", nullable = true, length = 20)
    public String getHostBeneficiaryId() {
        return hostBeneficiaryId;
    }

    public void setHostBeneficiaryId(String hostBeneficiaryId) {
        this.hostBeneficiaryId = hostBeneficiaryId;
    }


    /**
     * Returns the value of the <code>createdOn</code> property.
     */
    @Column(name = "CREATED_ON", nullable = false)
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the value of the <code>createdOn</code> property.
     *
     * @param createdOn the value for the <code>createdOn</code> property
     */

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Returns the value of the <code>updatedOn</code> property.
     */
    @Column(name = "UPDATED_ON", nullable = false)
    public Date getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Sets the value of the <code>updatedOn</code> property.
     *
     * @param updatedOn the value for the <code>updatedOn</code> property
     */

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    /**
     * Returns the value of the <code>versionNo</code> property.
     */
    @Version
    @Column(name = "VERSION_NO", nullable = false)
    public Integer getVersionNo() {
        return versionNo;
    }

    /**
     * Sets the value of the <code>versionNo</code> property.
     *
     * @param versionNo the value for the <code>versionNo</code> property
     */

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }


    /**
     * Returns the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @return the value of the <code>customerIdCustomerModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    public CustomerModel getRelationCustomerIdCustomerModel() {
        return customerIdCustomerModel;
    }

    /**
     * Returns the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @return the value of the <code>customerIdCustomerModel</code> relation property.
     */
    @javax.persistence.Transient
    public CustomerModel getCustomerIdCustomerModel() {
        return getRelationCustomerIdCustomerModel();
    }

    /**
     * Sets the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @param customerModel a value for <code>customerIdCustomerModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCustomerIdCustomerModel(CustomerModel customerModel) {
        this.customerIdCustomerModel = customerModel;
    }

    /**
     * Sets the value of the <code>customerIdCustomerModel</code> relation property.
     *
     * @param customerModel a value for <code>customerIdCustomerModel</code>.
     */
    @javax.persistence.Transient
    public void setCustomerIdCustomerModel(CustomerModel customerModel) {
        if (null != customerModel) {
            setRelationCustomerIdCustomerModel((CustomerModel) customerModel.clone());
        }
    }


    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getRelationCreatedByAppUserModel() {
        return createdByAppUserModel;
    }

    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     */
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel() {
        return getRelationCreatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
        this.createdByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            setRelationCreatedByAppUserModel((AppUserModel) appUserModel.clone());
        }
    }


    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getRelationUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }

    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel() {
        return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
        this.updatedByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            setRelationUpdatedByAppUserModel((AppUserModel) appUserModel.clone());
        }
    }

    /**
     * Returns the value of the <code>appUserId</code> property.
     */
    @javax.persistence.Transient
    public Long getCreatedBy() {
        if (createdByAppUserModel != null) {
            return createdByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
     */

    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
        if (appUserId == null) {
            createdByAppUserModel = null;
        } else {
            createdByAppUserModel = new AppUserModel();
            createdByAppUserModel.setAppUserId(appUserId);
        }
    }

    /**
     * Returns the value of the <code>appUserId</code> property.
     */
    @javax.persistence.Transient
    public Long getUpdatedBy() {
        if (updatedByAppUserModel != null) {
            return updatedByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
     */

    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
        if (appUserId == null) {
            updatedByAppUserModel = null;
        } else {
            updatedByAppUserModel = new AppUserModel();
            updatedByAppUserModel.setAppUserId(appUserId);
        }
    }

    /**
     * Returns the value of the <code>customerId</code> property.
     */
    @javax.persistence.Transient
    public Long getCustomerId() {
        if (customerIdCustomerModel != null) {
            return customerIdCustomerModel.getCustomerId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>customerId</code> property.
     *
     * @param customerId the value for the <code>customerId</code> property
     * @spring.validator type="required"
     */

    @javax.persistence.Transient
    public void setCustomerId(Long customerId) {
        if (customerId == null) {
            customerIdCustomerModel = null;
        } else {
            customerIdCustomerModel = new CustomerModel();
            customerIdCustomerModel.setCustomerId(customerId);
        }
    }

    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;


        associationModel = new AssociationModel();

        associationModel.setClassName("CustomerModel");
        associationModel.setPropertyName("relationCustomerIdCustomerModel");
        associationModel.setValue(getRelationCustomerIdCustomerModel());

        associationModelList.add(associationModel);

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

    @Column(name = "comments")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    @Column(name = "BENEFICIARY_BANK_NAME")
    public String getBeneficiaryBankName() {
        return beneficiaryBankName;
    }

    public void setBeneficiaryBankName(String beneficiaryBankName) {
        this.beneficiaryBankName = beneficiaryBankName;
    }

    @Column(name = "BENEFICIARY_EMAIL")
    public String getBeneficiaryEmail() {
        return beneficiaryEmail;
    }

    public void setBeneficiaryEmail(String beneficiaryEmail) {
        this.beneficiaryEmail = beneficiaryEmail;
    }

    @Column(name = "BENEFICIARY_ACCOUNT_NICK")
    public String getBeneficiaryAccountNick() {
        return beneficiaryAccountNick;
    }

    public void setBeneficiaryAccountNick(String beneficiaryAccountNick) {
        this.beneficiaryAccountNick = beneficiaryAccountNick;
    }
}

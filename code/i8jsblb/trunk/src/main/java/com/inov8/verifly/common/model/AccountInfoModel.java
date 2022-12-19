package com.inov8.verifly.common.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

/**
 * The AccountInfoModel entity bean.
 *
 * @author Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2008/03/10 19:29:08 $
 * @spring.bean name="AccountInfoModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACCOUNT_INFO_seq",
        sequenceName = "ACCOUNT_INFO_seq", allocationSize = 1)
@Table(name = "ACCOUNT_INFO")
public class AccountInfoModel
        extends BasePersistableModel implements RowMapper {

    private VfPaymentModeModel paymentModeIdPaymentModeModel;
    private VfCardTypeModel cardTypeIdCardTypeModel;
    private VfAccountTypeModel accountTypeIdAccountTypeModel;
    private VfCurrencyCodeModel currencyCodeIdCurrencyCodeModel;


    private Collection<LogModel> accountInfoIdLogModelList = new ArrayList<
            LogModel>();

    private Long accountInfoId;
    private String accountNick;
    private String cardNo;
    private String cardExpiryDate;
    private String pin;
    private String oneTimePin;
    private Long customerId;
    private String firstName;
    private String lastName;
    private String customerMobileNo;
    private String comments;
    private String description;
    private Boolean active;
    private Date updatedOn;
    private Date createdOn;
    private Integer versionNo;
    private String accountNo;
    private String newPin;
    private String confirmNewPin;
    private String oldPin;
    private String generatedPin;
    private String newAccountNick;
    private Boolean deleted;
    private String otPin;
    private Date otPinIssuedOn;
    private Long otRetryCount;
    private String pvv;
    private String pan;
    //
    private Long isMigrated;

    /**
     * Default constructor.
     */
    public AccountInfoModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getAccountInfoId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setAccountInfoId(primaryKey);
    }

    /**
     * Returns the value of the <code>accountInfoId</code> property.
     */
    @Column(name = "ACCOUNT_INFO_ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ACCOUNT_INFO_seq")
    public Long getAccountInfoId() {
        return accountInfoId;
    }

    /**
     * Sets the value of the <code>accountInfoId</code> property.
     *
     * @param accountInfoId the value for the <code>accountInfoId</code> property
     */

    public void setAccountInfoId(Long accountInfoId) {
        this.accountInfoId = accountInfoId;
    }

    /**
     * Returns the value of the <code>accountNick</code> property.
     */
    @Column(name = "ACCOUNT_NICK", nullable = false, length = 50)
    public String getAccountNick() {
        return accountNick;
    }

    /**
     * Sets the value of the <code>accountNick</code> property.
     *
     * @param accountNick the value for the <code>accountNick</code> property
     * @spring.validator type="required"
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setAccountNick(String accountNick) {
        this.accountNick = accountNick;
    }

    /**
     * Returns the value of the <code>cardNo</code> property.
     */
    @Column(name = "CARD_NO", nullable = true, length = 50)
    public String getCardNo() {
        return cardNo;
    }

    /**
     * Sets the value of the <code>cardNo</code> property.
     *
     * @param cardNo the value for the <code>cardNo</code> property
     * @spring.validator type="required"
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * Returns the value of the <code>cardExpiryDate</code> property.
     */
    @Column(name = "CARD_EXPIRY_DATE", nullable = true, length = 7)
    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    /**
     * Sets the value of the <code>cardExpiryDate</code> property.
     *
     * @param cardExpiryDate the value for the <code>cardExpiryDate</code> property
     * @spring.validator type="required"
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="7"
     */

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    /**
     * Returns the value of the <code>pin</code> property.
     */
    @Column(name = "PIN", nullable = false, length = 255)
    public String getPin() {
        return pin;
    }

    /**
     * Sets the value of the <code>pin</code> property.
     *
     * @param pin the value for the <code>pin</code> property
     * @spring.validator type="required"
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="255"
     */

    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * Returns the value of the <code>customerId</code> property.
     */
    @Column(name = "CUSTOMER_ID", nullable = false)
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the <code>customerId</code> property.
     *
     * @param customerId the value for the <code>customerId</code> property
     * @spring.validator type="required"
     * @spring.validator type="long"
     * @spring.validator type="intRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns the value of the <code>firstName</code> property.
     */
    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the <code>firstName</code> property.
     *
     * @param firstName the value for the <code>firstName</code> property
     * @spring.validator type="required"
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the value of the <code>lastName</code> property.
     */
    @Column(name = "LAST_NAME", nullable = false, length = 50)
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the <code>lastName</code> property.
     *
     * @param lastName the value for the <code>lastName</code> property
     * @spring.validator type="required"
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the value of the <code>customerMobileNo</code> property.
     */
    @Column(name = "CUSTOMER_MOBILE_NO", nullable = false, length = 20)
    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    /**
     * Sets the value of the <code>customerMobileNo</code> property.
     *
     * @param customerMobileNo the value for the <code>customerMobileNo</code> property
     * @spring.validator type="required"
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="20"
     */

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    /**
     * Returns the value of the <code>comments</code> property.
     */
    @Column(name = "COMMENTS", length = 255)
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the <code>comments</code> property.
     *
     * @param comments the value for the <code>comments</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="255"
     */

    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Returns the value of the <code>description</code> property.
     */
    @Column(name = "DESCRIPTION", length = 255)
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the <code>description</code> property.
     *
     * @param description the value for the <code>description</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="255"
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the value of the <code>active</code> property.
     */
    @Column(name = "IS_ACTIVE", nullable = false)
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the value of the <code>active</code> property.
     *
     * @param active the value for the <code>active</code> property
     */

    public void setActive(Boolean active) {
        this.active = active;
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
     * Returns the value of the <code>accountNo</code> property.
     */
    @Column(name = "ACCOUNT_NO", nullable = true, length = 50)
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * Sets the value of the <code>accountNo</code> property.
     *
     * @param accountNo the value for the <code>accountNo</code> property
     * @spring.validator type="required"
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    /**
     * Returns the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
     *
     * @return the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_TYPE_ID")
    public VfAccountTypeModel getRelationAccountTypeIdAccountTypeModel() {
        return accountTypeIdAccountTypeModel;
    }

    /**
     * Returns the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
     *
     * @return the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
     */
    @javax.persistence.Transient
    public VfAccountTypeModel getAccountTypeIdAccountTypeModel() {
        return getRelationAccountTypeIdAccountTypeModel();
    }

    /**
     * Sets the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
     *
     * @param accountTypeModel a value for <code>accountTypeIdAccountTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationAccountTypeIdAccountTypeModel(VfAccountTypeModel accountTypeModel) {
        this.accountTypeIdAccountTypeModel = accountTypeModel;
    }

    /**
     * Sets the value of the <code>accountTypeIdAccountTypeModel</code> relation property.
     *
     * @param accountTypeModel a value for <code>accountTypeIdAccountTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setAccountTypeIdAccountTypeModel(VfAccountTypeModel accountTypeModel) {
        if (null != accountTypeModel) {
            setRelationAccountTypeIdAccountTypeModel((VfAccountTypeModel) accountTypeModel.clone());
        }
    }

    /**
     * Returns the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
     *
     * @return the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_CODE_ID")
    public VfCurrencyCodeModel getRelationCurrencyCodeIdCurrencyCodeModel() {
        return currencyCodeIdCurrencyCodeModel;
    }

    /**
     * Returns the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
     *
     * @return the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
     */
    @javax.persistence.Transient
    public VfCurrencyCodeModel getCurrencyCodeIdCurrencyCodeModel() {
        return getRelationCurrencyCodeIdCurrencyCodeModel();
    }

    /**
     * Sets the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
     *
     * @param currencyCodeModel a value for <code>currencyCodeIdCurrencyCodeModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCurrencyCodeIdCurrencyCodeModel(VfCurrencyCodeModel currencyCodeModel) {
        this.currencyCodeIdCurrencyCodeModel = currencyCodeModel;
    }

    /**
     * Sets the value of the <code>currencyCodeIdCurrencyCodeModel</code> relation property.
     *
     * @param currencyCodeModel a value for <code>currencyCodeIdCurrencyCodeModel</code>.
     */
    @javax.persistence.Transient
    public void setCurrencyCodeIdCurrencyCodeModel(VfCurrencyCodeModel currencyCodeModel) {
        if (null != currencyCodeModel) {
            setRelationCurrencyCodeIdCurrencyCodeModel((VfCurrencyCodeModel) currencyCodeModel.clone());
        }
    }

    /**
     * Returns the value of the <code>currencyCodeId</code> property.
     */
    @javax.persistence.Transient
    public Long getCurrencyCodeId() {
        if (currencyCodeIdCurrencyCodeModel != null) {
            return currencyCodeIdCurrencyCodeModel.getCurrencyCodeId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>currencyCodeId</code> property.
     *
     * @param currencyCodeId the value for the <code>currencyCodeId</code> property
     */

    @javax.persistence.Transient
    public void setCurrencyCodeId(Long currencyCodeId) {
        if (currencyCodeId == null) {
            currencyCodeIdCurrencyCodeModel = null;
        } else {
            currencyCodeIdCurrencyCodeModel = new VfCurrencyCodeModel();
            currencyCodeIdCurrencyCodeModel.setCurrencyCodeId(currencyCodeId);
        }
    }

    /**
     * Returns the value of the <code>accountTypeId</code> property.
     */
    @javax.persistence.Transient
    public Long getAccountTypeId() {
        if (accountTypeIdAccountTypeModel != null) {
            return accountTypeIdAccountTypeModel.getAccountTypeId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>accountTypeId</code> property.
     *
     * @param accountTypeId the value for the <code>accountTypeId</code> property
     */

    @javax.persistence.Transient
    public void setAccountTypeId(Long accountTypeId) {
        if (accountTypeId == null) {
            accountTypeIdAccountTypeModel = null;
        } else {
            accountTypeIdAccountTypeModel = new VfAccountTypeModel();
            accountTypeIdAccountTypeModel.setAccountTypeId(accountTypeId);
        }
    }


    /**
     * Returns the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
     *
     * @return the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_MODE_ID")
    public VfPaymentModeModel getRelationPaymentModeIdPaymentModeModel() {
        return paymentModeIdPaymentModeModel;
    }

    /**
     * Returns the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
     *
     * @return the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
     */
    @javax.persistence.Transient
    public VfPaymentModeModel getPaymentModeIdPaymentModeModel() {
        return getRelationPaymentModeIdPaymentModeModel();
    }

    /**
     * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
     *
     * @param paymentModeModel a value for <code>paymentModeIdPaymentModeModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationPaymentModeIdPaymentModeModel(VfPaymentModeModel
                                                                 paymentModeModel) {
        this.paymentModeIdPaymentModeModel = paymentModeModel;
    }

    /**
     * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
     *
     * @param paymentModeModel a value for <code>paymentModeIdPaymentModeModel</code>.
     */
    @javax.persistence.Transient
    public void setPaymentModeIdPaymentModeModel(VfPaymentModeModel
                                                         paymentModeModel) {
        if (null != paymentModeModel) {
            setRelationPaymentModeIdPaymentModeModel((VfPaymentModeModel)
                    paymentModeModel.clone());
        }
    }

    /**
     * Returns the value of the <code>cardTypeIdCardTypeModel</code> relation property.
     *
     * @return the value of the <code>cardTypeIdCardTypeModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CARD_TYPE_ID")
    public VfCardTypeModel getRelationCardTypeIdCardTypeModel() {
        return cardTypeIdCardTypeModel;
    }

    /**
     * Returns the value of the <code>cardTypeIdCardTypeModel</code> relation property.
     *
     * @return the value of the <code>cardTypeIdCardTypeModel</code> relation property.
     */
    @javax.persistence.Transient
    public VfCardTypeModel getCardTypeIdCardTypeModel() {
        return getRelationCardTypeIdCardTypeModel();
    }

    /**
     * Sets the value of the <code>cardTypeIdCardTypeModel</code> relation property.
     *
     * @param cardTypeModel a value for <code>cardTypeIdCardTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCardTypeIdCardTypeModel(VfCardTypeModel
                                                           cardTypeModel) {
        this.cardTypeIdCardTypeModel = cardTypeModel;
    }

    /**
     * Sets the value of the <code>cardTypeIdCardTypeModel</code> relation property.
     *
     * @param cardTypeModel a value for <code>cardTypeIdCardTypeModel</code>.
     */
    @javax.persistence.Transient
    public void setCardTypeIdCardTypeModel(VfCardTypeModel cardTypeModel) {
        if (null != cardTypeModel) {
            setRelationCardTypeIdCardTypeModel((VfCardTypeModel) cardTypeModel.clone());
        }
    }

    /**
     * Add the related LogModel to this one-to-many relation.
     *
     * @param logModel object to be added.
     */

    public void addAccountInfoIdLogModel(LogModel logModel) {
        logModel.setRelationAccountInfoIdAccountInfoModel(this);
        accountInfoIdLogModelList.add(logModel);
    }

    /**
     * Remove the related LogModel to this one-to-many relation.
     *
     * @param logModel object to be removed.
     */

    public void removeAccountInfoIdLogModel(LogModel logModel) {
        logModel.setRelationAccountInfoIdAccountInfoModel(null);
        accountInfoIdLogModelList.remove(logModel);
    }

    /**
     * Get a list of related LogModel objects of the AccountInfoModel object.
     * These objects are in a bidirectional one-to-many relation by the AccountInfoId member.
     *
     * @return Collection of LogModel objects.
     */

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY,
            mappedBy = "relationAccountInfoIdAccountInfoModel")
    @JoinColumn(name = "ACCOUNT_INFO_ID")
    public Collection<LogModel> getAccountInfoIdLogModelList() throws Exception {
        return accountInfoIdLogModelList;
    }

    /**
     * Set a list of LogModel related objects to the AccountInfoModel object.
     * These objects are in a bidirectional one-to-many relation by the AccountInfoId member.
     *
     * @param logModelList the list of related objects.
     */
    public void setAccountInfoIdLogModelList(Collection<LogModel> logModelList) throws
            Exception {
        this.accountInfoIdLogModelList = logModelList;
    }

    /**
     * Returns the value of the <code>paymentModeId</code> property.
     */
    @javax.persistence.Transient
    public Long getPaymentModeId() {
        if (paymentModeIdPaymentModeModel != null) {
            return paymentModeIdPaymentModeModel.getPaymentModeId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>paymentModeId</code> property.
     *
     * @param paymentModeId the value for the <code>paymentModeId</code> property
     * @spring.validator type="required"
     */

    @javax.persistence.Transient
    public void setPaymentModeId(Long paymentModeId) {
        if (null != paymentModeId) {
            if (paymentModeIdPaymentModeModel == null) {
                paymentModeIdPaymentModeModel = new VfPaymentModeModel();
            }
            paymentModeIdPaymentModeModel.setPaymentModeId(paymentModeId);
        }
    }

    /**
     * Returns the value of the <code>cardTypeId</code> property.
     */
    @javax.persistence.Transient
    public Long getCardTypeId() {
        if (cardTypeIdCardTypeModel != null) {
            return cardTypeIdCardTypeModel.getCardTypeId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>cardTypeId</code> property.
     *
     * @param cardTypeId the value for the <code>cardTypeId</code> property
     * @spring.validator type="required"
     */

    @javax.persistence.Transient
    public void setCardTypeId(Long cardTypeId) {
        if (null != cardTypeId) {
            if (cardTypeIdCardTypeModel == null) {
                cardTypeIdCardTypeModel = new VfCardTypeModel();
            }
            cardTypeIdCardTypeModel.setCardTypeId(cardTypeId);
        }
    }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     *
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + getAccountInfoId();
        checkBox += "\"/>";
        return checkBox;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&accountInfoId=" + getAccountInfoId();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "accountInfoId";
        return primaryKeyFieldName;
    }


    /**
     * Returns the value of the <code>deleted</code> property.
     */
    @Column(name = "IS_DELETED", nullable = false)
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * Sets the value of the <code>deleted</code> property.
     *
     * @param deleted the value for the <code>deleted</code> property
     */

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel>
                associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();

        associationModel.setClassName("PaymentModeModel");
        associationModel.setPropertyName("relationPaymentModeIdPaymentModeModel");
        associationModel.setValue(getRelationPaymentModeIdPaymentModeModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("CurrencyCodeModel");
        associationModel.setPropertyName("relationCurrencyCodeIdCurrencyCodeModel");
        associationModel.setValue(getRelationCurrencyCodeIdCurrencyCodeModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("AccountTypeModel");
        associationModel.setPropertyName("relationAccountTypeIdAccountTypeModel");
        associationModel.setValue(getRelationAccountTypeIdAccountTypeModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("CardTypeModel");
        associationModel.setPropertyName("relationCardTypeIdCardTypeModel");
        associationModel.setValue(getRelationCardTypeIdCardTypeModel());

        associationModelList.add(associationModel);

        return associationModelList;
    }

    @javax.persistence.Transient
    public String getNewPin() {
        return newPin;
    }

    @javax.persistence.Transient
    public String getOldPin() {
        return oldPin;
    }

    @javax.persistence.Transient
    public String getGeneratedPin() {
        return generatedPin;
    }

    @javax.persistence.Transient
    public String getConfirmNewPin() {
        return confirmNewPin;
    }

    @javax.persistence.Transient
    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    @javax.persistence.Transient
    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

    @javax.persistence.Transient
    public void setGeneratedPin(String generatedPin) {
        this.generatedPin = generatedPin;
    }

    @javax.persistence.Transient
    public void setConfirmNewPin(String confirmNewPin) {
        this.confirmNewPin = confirmNewPin;
    }

    @javax.persistence.Transient
    public String getNewAccountNick() {
        return newAccountNick;
    }

    @javax.persistence.Transient
    public void setNewAccountNick(String newAccountNick) {
        this.newAccountNick = newAccountNick;
    }

    /**
     * Returns the value of the <code>otPin</code> property.
     */
    @Column(name = "OT_PIN", length = 250)
    public String getOtPin() {
        return otPin;
    }

    /**
     * Sets the value of the <code>otPin</code> property.
     *
     * @param otPin the value for the <code>otPin</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setOtPin(String otPin) {
        this.otPin = otPin;
    }

    /**
     * Returns the value of the <code>otPinIssuedOn</code> property.
     */
    @Column(name = "OT_PIN_ISSUED_ON")
    public Date getOtPinIssuedOn() {
        return otPinIssuedOn;
    }

    /**
     * Sets the value of the <code>otPinIssuedOn</code> property.
     *
     * @param otPinIssuedOn the value for the <code>otPinIssuedOn</code> property
     * @spring.validator type="date"
     * @spring.validator-var name="datePattern" value="${date_format}"
     */

    public void setOtPinIssuedOn(Date otPinIssuedOn) {
        this.otPinIssuedOn = otPinIssuedOn;
    }

    /**
     * Returns the value of the <code>otRetryCount</code> property.
     */
    @Column(name = "OT_RETRY_COUNT")
    public Long getOtRetryCount() {
        return otRetryCount;
    }

    /**
     * Sets the value of the <code>otRetryCount</code> property.
     *
     * @param otRetryCount the value for the <code>otRetryCount</code> property
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setOtRetryCount(Long otRetryCount) {
        this.otRetryCount = otRetryCount;
    }

    @javax.persistence.Transient
    public String getOneTimePin() {
        return oneTimePin;
    }

    @javax.persistence.Transient
    public void setOneTimePin(String oneTimePin) {
        this.oneTimePin = oneTimePin;
    }

    public String getPvv() {
        return pvv;
    }

    @Column(name = "PVV", nullable = true, length = 50)
    public void setPvv(String pvv) {
        this.pvv = pvv;
    }

    public String getPan() {
        return pan;
    }

    @Column(name = "PAN", nullable = true, length = 50)
    public void setPan(String pan) {
        this.pan = pan;
    }


    @Column(name = "IS_MIGRATED")
    public Long getIsMigrated() {
        return isMigrated;
    }

    public void setIsMigrated(Long isMigrated) {
        this.isMigrated = isMigrated;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AccountInfoModel vo = new AccountInfoModel();
        vo.setPaymentModeId(resultSet.getLong("PAYMENT_MODE_ID"));
        vo.setCardTypeId(resultSet.getLong("CARD_TYPE_ID"));
        vo.setAccountTypeId(resultSet.getLong("ACCOUNT_TYPE_ID"));
        vo.setCurrencyCodeId(resultSet.getLong("CURRENCY_CODE_ID"));
        vo.setAccountInfoId(resultSet.getLong("ACCOUNT_INFO_ID"));
        vo.setAccountNick(resultSet.getString("ACCOUNT_NICK"));
        vo.setCardNo(resultSet.getString("CARD_NO"));
        vo.setCardExpiryDate(resultSet.getString("CARD_EXPIRY_DATE"));
        vo.setPin(resultSet.getString("PIN"));
        vo.setCustomerId(resultSet.getLong("CUSTOMER_ID"));
        vo.setFirstName(resultSet.getString("FIRST_NAME"));
        vo.setLastName(resultSet.getString("LAST_NAME"));
        vo.setCustomerMobileNo(resultSet.getString("CUSTOMER_MOBILE_NO"));
        vo.setComments(resultSet.getString("COMMENTS"));
        vo.setDescription(resultSet.getString("DESCRIPTION"));
        vo.setActive(resultSet.getBoolean("IS_ACTIVE"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
        vo.setAccountNo(resultSet.getString("ACCOUNT_NO"));
        vo.setVersionNo(resultSet.getInt("VERSION_NO"));
        vo.setDeleted(resultSet.getBoolean("IS_DELETED"));
        vo.setOtPin(resultSet.getString("OT_PIN"));
        vo.setOtPinIssuedOn(resultSet.getDate("OT_PIN_ISSUED_ON"));
        vo.setOtRetryCount(resultSet.getLong("OT_RETRY_COUNT"));
        vo.setPvv(resultSet.getString("PVV"));
        vo.setPan(resultSet.getString("PAN"));
        vo.setIsMigrated(resultSet.getLong("IS_MIGRATED"));
        return vo;
    }
}

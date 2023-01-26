package com.inov8.microbank.cardconfiguration.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.*;
import org.springframework.jdbc.core.RowMapper;
import org.testng.remote.strprotocol.IStringMessage;

/**
 * DebitCard entity. @author Atieq ur Rehman
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CARD_FEE_RULE_SEQ",sequenceName = "CARD_FEE_RULE_SEQ",allocationSize=1)
@Table(name = "CARD_FEE_RULE")
public class CardFeeRuleModel extends BasePersistableModel implements Serializable,RowMapper {

    private static final long serialVersionUID = 1L;
    private Long cardFeeRuleId;
    private AppUserTypeModel appUserTypeModel;
    private SegmentModel segmentModel;
    private DistributorModel distributorModel;
    private CardFeeTypeModel cardFeeTypeModel;
    private OlaCustomerAccountTypeModel accountTypeModel;
    private CardTypeModel cardTypeModel;
    private CardProdCodeModel cardProdCodeModel;
    private MnoModel mnoIdMnoModel;

    private Double amount;
    private Double installmentAmount;
    private Long versionNo;

    private AppUserModel createdByAppUserModel;
    private Date createdOn;
    private AppUserModel updatedByAppUserModel;
    private Date updatedOn;
    private Boolean isDeleted;
    private Boolean isInstallments;
    private Long noOfInstallments;
    private String installmentPlan;


    // Constructors

    /** default constructor */
    public CardFeeRuleModel() {
    }

    /** minimal constructor */
    public CardFeeRuleModel(Long cardFeeRuleId) {
        this.cardFeeRuleId = cardFeeRuleId;
    }

    public CardFeeRuleModel(Long cardProgramId, Long appUserTypeId, Long segmentId, Long distributorId, Long cardFeeTypeId, Long accountTypeId) {
        setCardTypeId(cardProgramId);
        setAppUserTypeId(appUserTypeId);
        setSegmentId(segmentId);
        setDistributorId(distributorId);
        setCardFeeTypeId(cardFeeTypeId);
        setAccountTypeId(accountTypeId);
    }

    public CardFeeRuleModel(Long cardProgramId, Long appUserTypeId, Long segmentId, Long distributorId, Long cardFeeTypeId, Long accountTypeId, Long cardProductCodeId) {
        setCardTypeId(cardProgramId);
        setAppUserTypeId(appUserTypeId);
        setSegmentId(segmentId);
        setDistributorId(distributorId);
        setCardFeeTypeId(cardFeeTypeId);
        setAccountTypeId(accountTypeId);
        setCardProductCodeId(cardProductCodeId);
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getCardFeeRuleId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setCardFeeRuleId(primaryKey);
    }



    // Property accessors
    @Id@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARD_FEE_RULE_SEQ")
    @Column(name = "CARD_FEE_RULE_ID",nullable = false)
    public Long getCardFeeRuleId() {
        return this.cardFeeRuleId;
    }

    public void setCardFeeRuleId(Long cardFeeRuleId) {
        this.cardFeeRuleId = cardFeeRuleId;
    }

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="APP_USER_TYPE_ID")
    public AppUserTypeModel getAppUserTypeModel() {
        return this.appUserTypeModel;
    }

    public void setAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
        this.appUserTypeModel = appUserTypeModel;
    }

    @Transient
    public Long getAppUserTypeId(){
        return appUserTypeModel == null ? null : appUserTypeModel.getAppUserTypeId();
    }

    public void setAppUserTypeId(Long appUserTypeId)
    {
        if(appUserTypeId == null)
        {
            appUserTypeModel = null;
        }
        else
        {
            appUserTypeModel = new AppUserTypeModel();
            appUserTypeModel.setAppUserTypeId(appUserTypeId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="SEGMENT_ID")
    public SegmentModel getSegmentModel() {
        return this.segmentModel;
    }

    public void setSegmentModel(SegmentModel segmentModel) {
        this.segmentModel = segmentModel;
    }

    @Transient
    public Long getSegmentId(){
        return segmentModel == null ? null : segmentModel.getSegmentId();
    }

    public void setSegmentId(Long segmentId)
    {
        if(segmentId == null)
        {
            segmentModel = null;
        }
        else
        {
            segmentModel = new SegmentModel();
            segmentModel.setSegmentId(segmentId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="DISTRIBUTOR_ID")
    public DistributorModel getDistributorModel() {
        return this.distributorModel;
    }

    public void setDistributorModel(DistributorModel distributorModel) {
        this.distributorModel = distributorModel;
    }

    @Transient
    public Long getDistributorId(){
        return distributorModel == null ? null : distributorModel.getDistributorId();
    }

    public void setDistributorId(Long distributorId)
    {
        if(distributorId == null)
        {
            distributorModel = null;
        }
        else
        {
            distributorModel = new DistributorModel();
            distributorModel.setDistributorId(distributorId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="CARD_FEE_TYPE_ID")
    public CardFeeTypeModel getCardFeeTypeModel() {
        return this.cardFeeTypeModel;
    }

    public void setCardFeeTypeModel(CardFeeTypeModel cardFeeTypeModel) {
        this.cardFeeTypeModel = cardFeeTypeModel;
    }

    @Transient
    public Long getCardFeeTypeId(){
        return cardFeeTypeModel == null ? null : cardFeeTypeModel.getCardFeeTypeId();
    }

    public void setCardFeeTypeId(Long cardFeeTypeId)
    {
        if(cardFeeTypeId == null)
        {
            cardFeeTypeModel = null;
        }
        else
        {
            cardFeeTypeModel = new CardFeeTypeModel();
            cardFeeTypeModel.setCardFeeTypeId(cardFeeTypeId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="ACCOUNT_TYPE_ID")
    public OlaCustomerAccountTypeModel getAccountTypeModel() {
        return this.accountTypeModel;
    }

    public void setAccountTypeModel(OlaCustomerAccountTypeModel accountTypeModel) {
        this.accountTypeModel = accountTypeModel;
    }

    @Transient
    public Long getAccountTypeId(){
        return accountTypeModel == null ? null : accountTypeModel.getCustomerAccountTypeId();
    }

    public void setAccountTypeId(Long accountTypeId)
    {
        if(accountTypeId == null)
        {
            accountTypeModel = null;
        }
        else
        {
            accountTypeModel = new OlaCustomerAccountTypeModel();
            accountTypeModel.setCustomerAccountTypeId(accountTypeId);
        }
    }

    @Column(name = "AMOUNT")
    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
    public Long getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getCreatedByAppUserModel(){
        return createdByAppUserModel;
    }

    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        this.createdByAppUserModel = appUserModel;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getUpdatedByAppUserModel(){
        return updatedByAppUserModel;
    }

    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        this.updatedByAppUserModel = appUserModel;
    }


    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="CARD_TYPE_ID")
    public CardTypeModel getCardTypeModel() {
        return this.cardTypeModel;
    }

    public void setCardTypeModel(CardTypeModel cardTypeModel) {
        this.cardTypeModel = cardTypeModel;
    }

    @Transient
    public Long getCardTypeId(){
        return cardTypeModel == null ? null : cardTypeModel.getCardTypeId();
    }

    public void setCardTypeId(Long cardTypeId)
    {
        if(cardTypeId == null)
        {
            cardTypeModel = null;
        }
        else
        {
            cardTypeModel = new CardTypeModel();
            cardTypeModel.setCardTypeId(cardTypeId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="CARD_PRODUCT_TYPE_ID")
    public CardProdCodeModel getCardProdCodeModel() {
        return this.cardProdCodeModel;
    }

    public void setCardProdCodeModel(CardProdCodeModel cardTypeProdIdCardProdCodeModel) {
        this.cardProdCodeModel = cardTypeProdIdCardProdCodeModel;
    }

    @Transient
    public Long getCardProductCodeId(){
        return cardProdCodeModel == null ? null : cardProdCodeModel.getCardProductCodeId();
    }

    public void setCardProductCodeId(Long cardProductCodeId)
    {
        if(cardProductCodeId == null)
        {
            cardProdCodeModel = null;
        }
        else
        {
            cardProdCodeModel = new CardProdCodeModel();
            cardProdCodeModel.setCardProductCodeId(cardProductCodeId);
        }
    }
    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ this.getCardFeeRuleId();
        checkBox += "\"/>";
        return checkBox;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&cardFeeRuleId=" + getCardFeeRuleId();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        return "cardFeeRuleId";
    }

    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();
        associationModel.setClassName("CardTypeModel");
        associationModel.setPropertyName("cardTypeModel");
        associationModel.setValue(getCardTypeModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("CardProdCodeModel");
        associationModel.setPropertyName("cardProdCodeModel");
        associationModel.setValue(getCardProdCodeModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AppUserTypeModel");
        associationModel.setPropertyName("appUserTypeModel");
        associationModel.setValue(getAppUserTypeModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("SegmentModel");
        associationModel.setPropertyName("segmentModel");
        associationModel.setValue(getSegmentModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("DistributorModel");
        associationModel.setPropertyName("distributorModel");
        associationModel.setValue(getDistributorModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("CardFeeTypeModel");
        associationModel.setPropertyName("cardFeeTypeModel");
        associationModel.setValue(getCardFeeTypeModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("OlaCustomerAccountTypeModel");
        associationModel.setPropertyName("accountTypeModel");
        associationModel.setValue(getAccountTypeModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("createdByAppUserModel");
        associationModel.setValue(getCreatedByAppUserModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("updatedByAppUserModel");
        associationModel.setValue(getUpdatedByAppUserModel());
        associationModelList.add(associationModel);

        return associationModelList;
    }

    //--Service Operator Model

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_OP_ID")
    public MnoModel getMnoIdMnoModel(){
        return mnoIdMnoModel;
    }

    @javax.persistence.Transient
    public void setMnoIdMnoModel(MnoModel mnoModel) {
        this.mnoIdMnoModel = mnoModel;
    }

    @Transient
    public Long getMnoId()
    {
        if(mnoIdMnoModel != null)
            return mnoIdMnoModel.getMnoId();
        else
            return null;
    }

    @Transient
    public void setMnoId(Long mnoId)
    {
        if(mnoId == null)
            mnoIdMnoModel = null;
        else
        {
            mnoIdMnoModel = new MnoModel();
            mnoIdMnoModel.setMnoId(mnoId);
        }
    }

    @Column(name = "IS_DELETED")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    @Column(name = "IS_INSTALLMENTS")
    public Boolean getIsInstallments() {
        return isInstallments;
    }

    public void setIsInstallments(Boolean isInstallments) {
        this.isInstallments = isInstallments;
    }

    @Column(name = "NO_OF_INSTALLMENTS")
    public Long getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(Long noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }

    @Column(name = "INSTALLMENT_PLAN")
    public String getInstallmentPlan() {
        return installmentPlan;
    }

    @Column(name = "INSTALLMENT_AMOUNT")
    public Double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public void setInstallmentPlan(String installmentPlan) {
        this.installmentPlan = installmentPlan;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CardFeeRuleModel model = new CardFeeRuleModel();
        model.setCardFeeRuleId(resultSet.getLong("CARD_FEE_RULE_ID"));
        model.setCardTypeId(resultSet.getLong("CARD_TYPE_ID"));
        model.setCardProductCodeId(resultSet.getLong("CARD_PRODUCT_TYPE_ID"));
        model.setAppUserTypeId(resultSet.getLong("APP_USER_TYPE_ID"));
        model.setSegmentId(resultSet.getLong("SEGMENT_ID"));
        model.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
        model.setAccountTypeId(resultSet.getLong("ACCOUNT_TYPE_ID"));
        model.setCardFeeTypeId(resultSet.getLong("CARD_FEE_TYPE_ID"));
        model.setAmount(resultSet.getDouble("AMOUNT"));
        model.setIsDeleted(resultSet.getBoolean("IS_DELETED"));
        model.setIsInstallments(resultSet.getBoolean("IS_INSTALLMENTS"));
        model.setNoOfInstallments(resultSet.getLong("NO_OF_INSTALLMENTS"));
        model.setInstallmentPlan(resultSet.getString("INSTALLMENT_PLAN"));
        model.setInstallmentAmount(resultSet.getDouble("INSTALLMENT_AMOUNT"));
        model.setMnoId(resultSet.getLong("SERVICE_OP_ID"));
        model.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        return model;
    }

}

package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CARD_PRODUCT_TYPE_SEQ",sequenceName = "CARD_PRODUCT_TYPE_SEQ", allocationSize=1)
@Table(name = "CARD_PRODUCT_TYPE")
public class CardProdCodeModel extends BasePersistableModel implements Serializable {


    private Collection<SmartMoneyAccountModel> cardTypeIdSmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();
    private Long cardProductCodeId;
    private String cardProductCode;
    private String cardProductName;
    private String cardTypeCode;



    public void addCardTypeIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
        smartMoneyAccountModel.setRelationCardTypeProdIdCardProdCodeModel(this);
        cardTypeIdSmartMoneyAccountModelList.add(smartMoneyAccountModel);
    }

    /**
     * Remove the related SmartMoneyAccountModel to this one-to-many relation.
     *
     * @param smartMoneyAccountModel object to be removed.
     */

    public void removeCardTypeIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
        smartMoneyAccountModel.setRelationCardTypeIdCardTypeModel(null);
        cardTypeIdSmartMoneyAccountModelList.remove(smartMoneyAccountModel);
    }

    /**
     * Get a list of related SmartMoneyAccountModel objects of the CardTypeModel object.
     * These objects are in a bidirectional one-to-many relation by the CardTypeId member.
     *
     * @return Collection of SmartMoneyAccountModel objects.
     *
     */

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCardTypeProdIdCardProdCodeModel")
    @JoinColumn(name = "CARD_PRODUCT_TYPE_ID")
    public Collection<SmartMoneyAccountModel> getCardTypeIdSmartMoneyAccountModelList() throws Exception {
        return cardTypeIdSmartMoneyAccountModelList;
    }


    /**
     * Set a list of SmartMoneyAccountModel related objects to the CardTypeModel object.
     * These objects are in a bidirectional one-to-many relation by the CardTypeId member.
     *
     * @param smartMoneyAccountModelList the list of related objects.
     */
    public void setCardTypeIdSmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
        this.cardTypeIdSmartMoneyAccountModelList = smartMoneyAccountModelList;
    }

    @Column(name = "CARD_PRODUCT_TYPE_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARD_PRODUCT_TYPE_SEQ")
    public Long getCardProductCodeId() {
        return cardProductCodeId;
    }

    public void setCardProductCodeId(Long cardProductCodeId) {
        this.cardProductCodeId = cardProductCodeId;
    }
    @Column(name = "CARD_PRODUCT_CODE" , nullable = false , length=50 )
    public String getCardProductCode() {
        return cardProductCode;
    }

    public void setCardProductCode(String cardProductCode) {
        this.cardProductCode = cardProductCode;
    }
    @Column(name = "DISCRIPTION" , nullable = false , length=50 )
    public String getCardProductName() {
        return cardProductName;
    }

    public void setCardProductName(String cardProductName) {
        this.cardProductName = cardProductName;
    }

    @Column(name = "CARD_TYPE_CODE" , nullable = false , length=50 )
    public String getCardTypeCode() { return cardTypeCode; }

    public void setCardTypeCode(String cardTypeCode) { this.cardTypeCode = cardTypeCode; }



    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCardProductCodeId();
        checkBox += "\"/>";
        return checkBox;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&cardProductCodeId=" + getCardProductCodeId();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "cardProductCodeId";
        return primaryKeyFieldName;
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getCardProductCodeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setCardProductCodeId(primaryKey);
    }

}

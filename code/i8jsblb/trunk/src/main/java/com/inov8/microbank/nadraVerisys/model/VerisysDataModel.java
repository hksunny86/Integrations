package com.inov8.microbank.nadraVerisys.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Attique on 10/31/2017.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "NADRA_TRANSLATED_DATA_SEQ", sequenceName = "NADRA_TRANSLATED_DATA_SEQ", allocationSize = 1)
@Table(name = "NADRA_TRANSLATED_DATA")

public class VerisysDataModel extends BasePersistableModel implements Serializable {

    private static final long serialVersionUID = 1642133720769133545L;
    private Long verisysDataId;
    private Long appUserId;
    private String cnic;
    private String name;
    private String currentAddress;
    private String permanentAddress;
    private String placeOfBirth;
    private String motherMaidenName;
    private String nameTranslated;
    private String currentAddressTranslated;
    private String permanentAddressTranslated;
    private String placeOfBirthTranslated;
    private String motherMaidenNameTranslated;
    private Date createdOn;
    private Date updatedOn;
    private Boolean translated;
    private Boolean isAccountClosed;

    @Id
    @Column(name = "NADRA_TRANSLATED_DATA_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NADRA_TRANSLATED_DATA_SEQ")
    public Long getVerisysDataId() {
        return verisysDataId;
    }

    public void setVerisysDataId(Long verisysDataId) {
        this.verisysDataId = verisysDataId;
    }

    @Override
    public void setPrimaryKey(Long aLong) {
        setVerisysDataId(aLong);
    }

    @Transient
    @Override
    public Long getPrimaryKey() {
        return getVerisysDataId();
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter() {
        return "&parameter = verisysDataId";
    }


    @Transient
    @Override
    public String getPrimaryKeyFieldName() {
        return "verisysDataId";
    }
    @Column(name = "APP_USER_ID")
    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }
    @Column(name = "CNIC")
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "CURRENT_ADDRESS")
    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }
    @Column(name = "PERMANENT_ADDRESS")
    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }
    @Column(name = "PLACE_OF_BIRTH")
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }
    @Column(name = "MOTHER_MAIDEN_NAME")
    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }
    @Column(name = "NAME_TRANSLATED")
    public String getNameTranslated() {
        return nameTranslated;
    }

    public void setNameTranslated(String nameTranslated) {
        this.nameTranslated = nameTranslated;
    }
    @Column(name = "CURRENT_ADDRESS_TRANSLATED")
    public String getCurrentAddressTranslated() {
        return currentAddressTranslated;
    }

    public void setCurrentAddressTranslated(String currentAddressTranslated) {
        this.currentAddressTranslated = currentAddressTranslated;
    }
    @Column(name = "PERMANENT_ADDRESS_TRANSLATED")
    public String getPermanentAddressTranslated() {
        return permanentAddressTranslated;
    }

    public void setPermanentAddressTranslated(String permanentAddressTranslated) {
        this.permanentAddressTranslated = permanentAddressTranslated;
    }
    @Column(name = "PLACE_OF_BIRTH_TRANSLATED")
    public String getPlaceOfBirthTranslated() {
        return placeOfBirthTranslated;
    }

    public void setPlaceOfBirthTranslated(String placeOfBirthTranslated) {
        this.placeOfBirthTranslated = placeOfBirthTranslated;
    }
    @Column(name = "MOTHER_MAIDEN_NAME_TRANSLATED")
    public String getMotherMaidenNameTranslated() {
        return motherMaidenNameTranslated;
    }

    public void setMotherMaidenNameTranslated(String motherMaidenNameTranslated) {
        this.motherMaidenNameTranslated = motherMaidenNameTranslated;
    }
    @Column(name = "IS_TRANSLATED")
    public Boolean getTranslated() {
        return translated;
    }

    public void setTranslated(Boolean translated) {
        this.translated = translated;
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

    @Column(name = "IS_ACCOUNT_CLOSED")
    public Boolean getAccountClosed() {
        return isAccountClosed;
    }

    public void setAccountClosed(Boolean accountClosed) {
        isAccountClosed = accountClosed;
    }
}

package com.inov8.microbank.fonepay.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Attique on 7/14/2017.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "VIRTUAL_CARD_SEQUENCE",sequenceName = "VIRTUAL_CARD_SEQUENCE", allocationSize=1)
@Table(name = "VIRTUAL_CARD")
public class VirtualCardModel extends BasePersistableModel {
    private AppUserModel updatedByAppUserModel;
    private AppUserModel createdByAppUserModel;
    private Long appUserId;

    private Long virtualCardId;
    private String cnicNo;
    private String mobileNo;
    private String firstName;
    private String lastName;
    private String cardExpiry;
    private Date createdOn;
    private Date updatedOn;
    private String cardNo;
    private String customerId;
    private Boolean isBlocked;
    private Boolean isDeleted;


/*    public VirtualCardModel(VirtualCardEnableDisableVO vo) {

*//*       this.setIsBlocked(vo.getBlocked());
        this.setCustomerId(vo.getCustomerId());
        this.setCardNo(vo.getCardNo());
        this.setCardExpiry(vo.get);
        createdOn = vo.getCreatedOn();
        updatedOn = vo.getCreatedOn();
        this.setCreatedBy(vo.getCreatedBy());
        this.setUpdatedBy(vo.getCreatedBy());
        this.setAppUserId(vo.getAppUserId());*//*
    }*/


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getUpdatedByAppUserModel()
    {
        return updatedByAppUserModel;
    }

    public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel)
    {
        this.updatedByAppUserModel = updatedByAppUserModel;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getCreatedByAppUserModel()
    {
        return createdByAppUserModel;
    }

    public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel)
    {
        this.createdByAppUserModel = createdByAppUserModel;
    }

    @Column(name = "CREATED_ON" , nullable = false )
    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON" , nullable = false )
    public Date getUpdatedOn()
    {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn)
    {
        this.updatedOn = updatedOn;
    }

    @Column(name = "VIRTUAL_CARD_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="VIRTUAL_CARD_SEQUENCE")
    public Long getVirtualCardId()
    {
        return virtualCardId;
    }

    public void setVirtualCardId(Long virtualCardId)
    {
        this.virtualCardId = virtualCardId;
    }

    @Column(name = "CNIC" , nullable = false )
    public String getCnicNo()
    {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo)
    {
        this.cnicNo = cnicNo;
    }

    @Column(name = "MOBILE_NO" , nullable = false )
    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    @Column(name = "FIRST_NAME" , nullable = false )
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME" , nullable = false )
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Column(name = "CARD_NO" , nullable = false )
    public String getCardNo()
    {
        return cardNo;
    }

    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    @Column(name = "CUSTOMER_ID" , nullable = false )
    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    @Column(name = "CARD_EXPIRY" , nullable = false )
    public String getCardExpiry()
    {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry)
    {
        this.cardExpiry = cardExpiry;
    }

    @Column(name = "IS_BLOCKED")
    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }


    @Column(name = "APP_USER_ID")
    public Long getAppUserId(){
        return appUserId;
    }
    public void setAppUserId(Long appUserId){
        this.appUserId=appUserId;
    }


    @Transient
    public Long getCreatedBy() {
        if (createdByAppUserModel != null) {
            return createdByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    @Transient
    public void setCreatedBy(Long appUserId) {
        if(appUserId == null)
        {
            createdByAppUserModel = null;
        }
        else
        {
            createdByAppUserModel = new AppUserModel();
            createdByAppUserModel.setAppUserId(appUserId);
        }
    }

    @Transient
    public Long getUpdatedBy() {
        if (updatedByAppUserModel != null) {
            return updatedByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    @Transient
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

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        this.setVirtualCardId(primaryKey);
    }

    @Transient
    public Long getPrimaryKey() {
        return getVirtualCardId();
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&virtualCarId=" + getVirtualCardId();
        return parameters;
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "virtualCardId";
        return primaryKeyFieldName;
    }
    @Transient
    public List<AssociationModel> getAssociationModelList()
    {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = new AssociationModel();

        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("updatedByAppUserModel");
        associationModel.setValue(getUpdatedByAppUserModel());

        associationModelList.add(associationModel);


        associationModel = new AssociationModel();

        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("createdByAppUserModel");
        associationModel.setValue(getUpdatedByAppUserModel());

        associationModelList.add(associationModel);


        return associationModelList;
    }
    @Column(name = "IS_DELETED"  )
    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}

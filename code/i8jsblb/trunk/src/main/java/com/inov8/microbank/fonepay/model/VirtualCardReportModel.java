package com.inov8.microbank.fonepay.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Attique on 7/14/2017.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "VIRTUAL_CARD_VIEW")
public class VirtualCardReportModel extends BasePersistableModel {


    private Long virtualCardId;
    private String cnicNo;
    private String mobileNo;
    private String name;
    private String cardExpiry;
    private Date createdOn;
    private Date updatedOn;
    private String cardNo;
    private String isBlocked;
    private String customerId;

    private Date startDate;
    private Date endDate;


    @Column(name = "CREATED_ON"  )
    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON"  )
    public Date getUpdatedOn()
    {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn)
    {
        this.updatedOn = updatedOn;
    }

    @Column(name = "VIRTUAL_CARD_ID"  )
    @Id
    public Long getVirtualCardId()
    {
        return virtualCardId;
    }

    public void setVirtualCardId(Long virtualCardId)
    {
        this.virtualCardId = virtualCardId;
    }

    @Column(name = "CNIC"  )
    public String getCnicNo()
    {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo)
    {
        this.cnicNo = cnicNo;
    }

    @Column(name = "MOBILE_NO"  )
    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }


    @Column(name = "NAME"  )
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "CUSTOMER_ID")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Column(name = "CARD_NO"  )
    public String getCardNo()
    {
        return cardNo;
    }

    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    @Column(name = "CARD_EXPIRY" )
    public String getCardExpiry()
    {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry)
    {
        this.cardExpiry = cardExpiry;
    }

    @Column(name = "BLOCKED" )
    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
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
        parameters += "&virtualCardId=" + getVirtualCardId();
        return parameters;
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "virtualCardId";
        return primaryKeyFieldName;
    }


    @Transient
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }



}

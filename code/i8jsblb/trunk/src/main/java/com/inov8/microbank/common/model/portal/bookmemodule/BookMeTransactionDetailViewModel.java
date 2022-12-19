package com.inov8.microbank.common.model.portal.bookmemodule;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BOOKME_TRANSACTION_DETAIL_VIEW")
public class BookMeTransactionDetailViewModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = 7059945306873740626L;

    private Long pk;
    private Date date;
    private String orderRefId;
   private String partnerId;
   private String serviceType;
   private String serviceProviderName;
   private String name;
   private String phone;
   private Double bookingAmount;
   private Double baseFare;
   private Double surcharges;
   private Double taxes;
   private Double fee;
   private Double discount;
    private Date createdOn;
    private Timestamp updatedOn;
    private Date createdOnToDate;

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setPk(primaryKey);
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "PK"  )
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @javax.persistence.Transient
    public Date getCreatedOnToDate() { return createdOnToDate; }

    public void setCreatedOnToDate(Date createdOnToDate) { this.createdOnToDate = createdOnToDate; }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() { return createdOn; }

    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    @Column(name = "UPDATED_ON")
    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "PAID_DATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "ORDER_REF_ID")
    public String getOrderRefId() {
        return orderRefId;
    }

    public void setOrderRefId(String orderRefId) {
        this.orderRefId = orderRefId;
    }
    @Column(name = "PARTNER_ID")
    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }
        @Column(name = "SERVICE_TYPE")
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    @Column(name = "SERVICE_PROVIDER_NAME")
    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }
    @Column(name = "CUSTOMER_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "PHONE_NO")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Column(name = "BILL_AMOUNT")
    public Double getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(Double bookingAmount) {
        this.bookingAmount = bookingAmount;
    }
    @Column(name = "BASE_FARE")
    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }
    @Column(name = "SURCHARGES")
    public Double getSurcharges() {
        return surcharges;
    }

    public void setSurcharges(Double surcharges) {
        this.surcharges = surcharges;
    }
    @Column(name = "TAXES")
    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }
    @Column(name = "FEE")
    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }
    @Column(name = "DISCOUNT")

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}

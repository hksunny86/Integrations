package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BOP_CARD_ISSUANCE_VIEW")
public class BOPCardIssuanceViewModel extends BasePersistableModel implements Serializable {

    private Long pk;
    private String agentId;
    private String agentLocation;
    private String customerMobileNumber;
    private String customerNic;
    private String debitCardNumber;
    private String segment;
    private Date issuanceDate;
    private Date createdOn;
    private Date updatedOn;

    private Date createdOnToDate;

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setPk(primaryKey);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }


    @Id
    @Column(name = "PK"  )  public Long getPk() { return pk; }
    public void setPk(Long pk) { this.pk = pk; }

    @Column(name = "AGENT_ID")
    public String getAgentId() { return agentId; }

    public void setAgentId(String agentId) { this.agentId = agentId; }

    @Column(name = "AGENT_LOCATION")
    public String getAgentLocation() { return agentLocation; }

    public void setAgentLocation(String agentLocation) { this.agentLocation = agentLocation; }

    @Column(name = "CUSTOMER_MOBILE_NUMBER")
    public String getCustomerMobileNumber() { return customerMobileNumber; }

    public void setCustomerMobileNumber(String customerMobileNumber) { this.customerMobileNumber = customerMobileNumber; }

    @Column(name = "CUSTOMER_CNIC")
    public String getCustomerNic() { return customerNic; }

    public void setCustomerNic(String customerNic) { this.customerNic = customerNic; }

    @Column(name = "CARD_NUMBER")
    public String getDebitCardNumber() { return debitCardNumber; }

    public void setDebitCardNumber(String debitCardNumber) { this.debitCardNumber = debitCardNumber; }

    @Column(name = "SEGMENT")
    public String getSegment() { return segment; }

    public void setSegment(String segment) { this.segment = segment; }

    @Column(name = "ISSUANCE_DATE")
    public Date getIssuanceDate() { return issuanceDate; }

    public void setIssuanceDate(Date issuanceDate) { this.issuanceDate = issuanceDate; }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() { return createdOn; }

    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() { return updatedOn; }

    public void setUpdatedOn(Date updatedOn) { this.updatedOn = updatedOn; }

    @javax.persistence.Transient
    public Date getCreatedOnToDate() { return createdOnToDate; }

    public void setCreatedOnToDate(Date createdOnToDate) { this.createdOnToDate = createdOnToDate; }
}

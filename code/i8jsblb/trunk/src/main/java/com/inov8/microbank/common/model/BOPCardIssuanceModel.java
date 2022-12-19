package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BOP_CARD_ISSUANCE_SEQ",sequenceName = "BOP_CARD_ISSUANCE_SEQ", allocationSize=1)
@Table(name = "BOP_CARD_ISSUANCE")
public class BOPCardIssuanceModel extends BasePersistableModel {

    private Long pk;
    private String agentId;
    private String longitude;
    private String latitude;
    private String customerNic;
    private String customerMobileNumber;
    private Date issuanceDate;
    private Date createdOn;
    private Date updatedOn;
    private String segmentId;
    private String segmentName;
    private String debitCardNumber;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOP_CARD_ISSUANCE_SEQ")
    @Column(name = "PK"  ) public Long getPk() { return pk; }

    public void setPk(Long pk) { this.pk = pk; }

    @Column(name = "AGENT_ID")
    public String getAgentId() { return agentId; }

    public void setAgentId(String agentId) { this.agentId = agentId; }

    @Column(name = "LONGITUDE")
    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    @Column(name = "LATITUDE")
    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    @Column(name = "CUST_CNIC")
    public String getCustomerNic() { return customerNic; }

    public void setCustomerNic(String customerNic) { this.customerNic = customerNic; }

    @Column(name = "CUST_MOBILE_NO")
    public String getCustomerMobileNumber() { return customerMobileNumber; }

    public void setCustomerMobileNumber(String customerMobileNumber) { this.customerMobileNumber = customerMobileNumber; }

    @Column(name = "ISSUANCE_DATE")
    public Date getIssuanceDate() { return issuanceDate; }

    public void setIssuanceDate(Date issuanceDate) { this.issuanceDate = issuanceDate; }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() { return createdOn; }

    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() { return updatedOn; }

    public void setUpdatedOn(Date updatedOn) { this.updatedOn = updatedOn; }

    @Column(name = "SEGMENT_ID")
    public String getSegmentId() { return segmentId; }

    public void setSegmentId(String segmentId) { this.segmentId = segmentId; }

    @Column(name = "SEGMENT_NAME")
    public String getSegmentName() { return segmentName; }

    public void setSegmentName(String segmentName) { this.segmentName = segmentName; }

    @Column(name = "CARD_NUMBER")
    public String getDebitCardNumber() { return debitCardNumber; }

    public void setDebitCardNumber(String debitCardNumber) { this.debitCardNumber = debitCardNumber; }
}

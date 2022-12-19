package com.inov8.microbank.common.model;


import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
//@javax.persistence.SequenceGenerator(name = "AGENT_GROUP_CHILDREN_SEQ", sequenceName = "AGENT_GROUP_CHILDREN_SEQ", allocationSize=1)
@Table(name = "AGENT_GROUP_CHILDREN_VIEW")
public class AgentGroupChildrenViewModel extends BasePersistableModel {

    private Long pk;
    private Long agentGroupTaggingId;
    private Long appUserId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String cNic;
    private Long appUserTypeId;
    private Long retailerContactId;
    private String businessName;
    private Long handlerId;

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

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
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "PK")
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Column(name = "AGENT_GROUP_TAGGING_ID")
    public Long getAgentGroupTaggingId() {
        return agentGroupTaggingId;
    }

    public void setAgentGroupTaggingId(Long agentGroupTaggingId) {
        this.agentGroupTaggingId = agentGroupTaggingId;
    }

    @Column(name = "APP_USER_ID")
    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    @Column(name = "USER_ID")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "NIC")
    public String getcNic() {
        return cNic;
    }

    public void setcNic(String cNic) {
        this.cNic = cNic;
    }

    @Column(name = "APP_USER_TYPE_ID")
    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId) {
        this.appUserTypeId = appUserTypeId;
    }

    @Column(name = "RETAILER_CONTACT_ID")
    public Long getRetailerContactId() {
        return retailerContactId;
    }

    public void setRetailerContactId(Long retailerContactId) {
        this.retailerContactId = retailerContactId;
    }

    @Column(name = "BUSINESS_NAME")
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Column(name = "HANDLER_ID")
    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }
}

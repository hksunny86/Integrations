package com.inov8.microbank.common.model.retailermodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The RetailerContactListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="RetailerContactListViewModel"
 */
/**
 * @author AtifHu
 *
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "RETAILER_CONTACT_LIST_VIEW")
public class RetailerContactListViewModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = 9101946211575613867L;

    private Long retailerContactId;
    private Long retailerId;
    private Long distributorId;
    private String distributorName;
    private Long distributorLevelId;
    private String distributorLevelName;
    private Long regionId;
    private String regionName;
    private Long areaId;
    private Boolean active;
    private Integer versionNo;
    private String description;
    private Double balance;
    private String username;
    private String email;
    private Long appUserId;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String retailerName;
    private String areaName;
    private Boolean accountEnabled;
    private Double partnerGroupId;
    private String partnerGroupName;
    private Long deviceTypeId;
    private Boolean head;
    private String allpayId;
    private String nic;
    private Boolean isRso;
    private Date accountOpeningDate;
    private String isAccountLocked;
    private String isCredentialExpired;
    private String isAccountClosed;
    private String accountActive;
    private Long salesHierarchyId;
    private String salesUserName;
    private String areaLevelName;
    private String retailerAddress;
    private String cityName;
    private String agentBusinessName;
    private String headString;
    private Long soId;
    private Long parentRetailerContactId;
    private Long parentAgentId;
    private String accountNo;
    private String coreAccountStatus;


    @Column(name="BUSINESS_NAME")
    public String getAgentBusinessName() {
        return agentBusinessName;
    }

    public void setAgentBusinessName(String agentBusinessName) {
        this.agentBusinessName = agentBusinessName;
    }

    @Column(name="BUSINESS_CITY_NAME")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Column(name="RETAILER_ADDRESS")
    public String getRetailerAddress() {
        return retailerAddress;
    }

    public void setRetailerAddress(String retailerAddress) {
        this.retailerAddress = retailerAddress;
    }

    @Column(name="AREA_LEVEL_NAME")
    public String getAreaLevelName() {
        return areaLevelName;
    }

    public void setAreaLevelName(String areaLevelName) {
        this.areaLevelName = areaLevelName;
    }

    public void setHeadString (String headString){
        this.headString = headString;
    }

    @Column(name = "HEAD_STRING")
    public String getHeadString(){
        return headString;
    }

    @Column(name = "ALLPAY_ID"  , length=50 )
    public String getAllpayId() {
        return allpayId;
    }



    public void setAllpayId(String allpayId) {
        this.allpayId= allpayId;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }

    @Column(name = "IS_HEAD"  )
    public Boolean getHead() {
        return head;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    /**
     * Returns the value of the <code>distributorId</code> property.
     *
     */
    @Column(name = "DEVICE_TYPE_ID"  )
    public Long getDeviceTypeId() {
        return deviceTypeId ;
    }
    public RetailerContactListViewModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getRetailerContactId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setRetailerContactId(primaryKey);
    }

    /**
     * Returns the value of the <code>retailerContactId</code> property.
     *
     */
    @Column(name = "RETAILER_CONTACT_ID" , nullable = false )
    @Id
    public Long getRetailerContactId() {
        return retailerContactId;
    }

    /**
     * Sets the value of the <code>retailerContactId</code> property.
     *
     * @param retailerContactId the value for the <code>retailerContactId</code> property
     *
     */

    public void setRetailerContactId(Long retailerContactId) {
        this.retailerContactId = retailerContactId;
    }

    /**
     * Returns the value of the <code>retailerId</code> property.
     *
     */
    @Column(name = "RETAILER_ID" , nullable = false )
    public Long getRetailerId() {
        return retailerId;
    }

    /**
     * Sets the value of the <code>retailerId</code> property.
     *
     * @param retailerId the value for the <code>retailerId</code> property
     *
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    @Column(name = "DISTRIBUTOR_ID", nullable = false, precision = 10, scale = 0)
    public Long getDistributorId() {
        return this.distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    @Column(name = "DISTRIBUTOR_NAME", nullable = false, length = 50)
    public String getDistributorName() {
        return this.distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    @Column(name = "REGION_ID", precision = 10, scale = 0)
    public Long getRegionId() {
        return this.regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    @Column(name = "DISTRIBUTOR_LEVEL_ID", precision = 10, scale = 0)
    public Long getDistributorLevelId() {
        return this.distributorLevelId;
    }

    public void setDistributorLevelId(Long distributorLevelId) {
        this.distributorLevelId = distributorLevelId;
    }

    @Column(name = "DISTRIBUTOR_LEVEL_NAME", length = 50)
    public String getDistributorLevelName() {
        return this.distributorLevelName;
    }

    public void setDistributorLevelName(String distributorLevelName) {
        this.distributorLevelName = distributorLevelName;
    }

    @Column(name = "REGION_NAME", length = 50)
    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    /**
     * Returns the value of the <code>areaId</code> property.
     *
     */
    @Column(name = "AREA_ID" , nullable = false )
    public Long getAreaId() {
        return areaId;
    }

    /**
     * Sets the value of the <code>areaId</code> property.
     *
     * @param areaId the value for the <code>areaId</code> property
     *
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    /**
     * Returns the value of the <code>active</code> property.
     *
     */
    @Column(name = "IS_ACTIVE" , nullable = false )
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the value of the <code>active</code> property.
     *
     * @param active the value for the <code>active</code> property
     *
     */

    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Returns the value of the <code>versionNo</code> property.
     *
     */
    @Version
    @Column(name = "VERSION_NO" , nullable = false )
    public Integer getVersionNo() {
        return versionNo;
    }

    /**
     * Sets the value of the <code>versionNo</code> property.
     *
     * @param versionNo the value for the <code>versionNo</code> property
     *
     */

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * Returns the value of the <code>description</code> property.
     *
     */
    @Column(name = "DESCRIPTION"  , length=250 )
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the <code>description</code> property.
     *
     * @param description the value for the <code>description</code> property
     *
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the value of the <code>balance</code> property.
     *
     */
    @Column(name = "BALANCE" , nullable = false )
    public Double getBalance() {
        return balance;
    }

    /**
     * Sets the value of the <code>balance</code> property.
     *
     * @param balance the value for the <code>balance</code> property
     *
     * @spring.validator type="double"
     * @spring.validator type="doubleRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="99999999999.9999"
     */

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * Returns the value of the <code>username</code> property.
     *
     */
    @Column(name = "USERNAME" , nullable = false , length=50 )
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the <code>username</code> property.
     *
     * @param username the value for the <code>username</code> property
     *
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the value of the <code>email</code> property.
     *
     */
    @Column(name = "EMAIL"  , length=50 )
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the <code>email</code> property.
     *
     * @param email the value for the <code>email</code> property
     *
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the value of the <code>appUserId</code> property.
     *
     */
    @Column(name = "APP_USER_ID" , nullable = false )
    public Long getAppUserId() {
        return appUserId;
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
     *
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    /**
     * Returns the value of the <code>firstName</code> property.
     *
     */
    @Column(name = "FIRST_NAME" , nullable = false , length=50 )
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the <code>firstName</code> property.
     *
     * @param firstName the value for the <code>firstName</code> property
     *
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
     *
     */
    @Column(name = "LAST_NAME" , nullable = false , length=50 )
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the <code>lastName</code> property.
     *
     * @param lastName the value for the <code>lastName</code> property
     *
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
     * Returns the value of the <code>mobileNo</code> property.
     *
     */
    @Column(name = "MOBILE_NO" , nullable = false , length=50 )
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the value of the <code>mobileNo</code> property.
     *
     * @param mobileNo the value for the <code>mobileNo</code> property
     *
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Returns the value of the <code>retailerName</code> property.
     *
     */
    @Column(name = "RETAILER_NAME" , nullable = false , length=50 )
    public String getRetailerName() {
        return retailerName;
    }

    /**
     * Sets the value of the <code>retailerName</code> property.
     *
     * @param retailerName the value for the <code>retailerName</code> property
     *
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    /**
     * Returns the value of the <code>areaName</code> property.
     *
     */
    @Column(name = "AREA_NAME" , nullable = false , length=50 )
    public String getAreaName() {
        return areaName;
    }

    /**
     * Sets the value of the <code>areaName</code> property.
     *
     * @param areaName the value for the <code>areaName</code> property
     *
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /**
     * Returns the value of the <code>accountEnabled</code> property.
     *
     */
    @Column(name = "IS_ACCOUNT_ENABLED" , nullable = false )
    public Boolean getAccountEnabled() {
        return accountEnabled;
    }

    /**
     * Sets the value of the <code>accountEnabled</code> property.
     *
     * @param accountEnabled the value for the <code>accountEnabled</code> property
     *
     */

    public void setAccountEnabled(Boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }

    /**
     * Returns the value of the <code>partnerGroupId</code> property.
     *
     */
    @Column(name = "PARTNER_GROUP_ID"  )
    public Double getPartnerGroupId() {
        return partnerGroupId;
    }

    /**
     * Sets the value of the <code>partnerGroupId</code> property.
     *
     * @param partnerGroupId the value for the <code>partnerGroupId</code> property
     *
     * @spring.validator type="double"
     * @spring.validator type="doubleRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="99999999999.9999"
     */

    public void setPartnerGroupId(Double partnerGroupId) {
        this.partnerGroupId = partnerGroupId;
    }

    /**
     * Returns the value of the <code>partnerGroupName</code> property.
     *
     */
    @Column(name = "PARTNER_GROUP_NAME"  , length=50 )
    public String getPartnerGroupName() {
        return partnerGroupName;
    }

    /**
     * Sets the value of the <code>partnerGroupName</code> property.
     *
     * @param partnerGroupName the value for the <code>partnerGroupName</code> property
     *
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setPartnerGroupName(String partnerGroupName) {
        this.partnerGroupName = partnerGroupName;
    }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getRetailerContactId();
        checkBox += "\"/>";
        return checkBox;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&retailerContactId=" + getRetailerContactId();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "retailerContactId";
        return primaryKeyFieldName;
    }
    @Column(name = "NIC"  , length=50 )
    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }
    /**
     * Sets the value of the <code>nic</code> property.
     *
     * @param partnerGroupName the value for the <code>nic</code> property
     *
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */
    @Column(name = "IS_RSO")
    public Boolean getIsRso() {
        return isRso;
    }

    public void setIsRso(Boolean isRso) {
        this.isRso = isRso;
    }
    @Column(name = "CREATED_ON", length=50 )
    public Date getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(Date accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }
    @Column(name = "IS_ACCOUNT_ACTIVE" , nullable = false)
    public String getAccountActive() {
        return accountActive;
    }
    public void setAccountActive(String accountActive) {
        this.accountActive = accountActive;
    }
    @Column(name = "IS_ACCOUNT_LOCKED" )
    public String getIsAccountLocked() {
        return this.isAccountLocked;
    }
    public void setIsAccountLocked(String isAccountLocked) {
        this.isAccountLocked = isAccountLocked;
    }
    @Column(name = "IS_CREDENTIALS_EXPIRED"  )
    public String getIsCredentialExpired() {
        return isCredentialExpired;
    }

    public void setIsCredentialExpired(String isCredentialExpired) {
        this.isCredentialExpired = isCredentialExpired;
    }
    @Column(name="IS_ACCOUNT_CLOSED")
    public String getIsAccountClosed() {
        return isAccountClosed;
    }

    public void setIsAccountClosed(String isAccountClosed) {
        this.isAccountClosed = isAccountClosed;
    }
    @Column(name="SALES_HIERARCHY_ID")
    public Long getSalesHierarchyId() {
        return salesHierarchyId;
    }

    public void setSalesHierarchyId(Long salesHierarchyId) {
        this.salesHierarchyId = salesHierarchyId;
    }
    public void setSalesUserName(String salesUserName)
    {
        this.salesUserName = salesUserName;
    }
    @Column(name="SALES_USER_NAME")
    public String getSalesUserName()
    {
        return salesUserName;
    }
    @Column(name="SERVICE_OPERATOR_ID")
    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    @Column(name="PARENT_RETAILER_CONTACT_ID")
    public Long getParentRetailerContactId() {return parentRetailerContactId;}

    public void setParentRetailerContactId(Long parentRetailerContactId) {this.parentRetailerContactId = parentRetailerContactId;}

    @Column(name="CORE_ACCOUNT_NUMBER")
    public String getAccountNo() {return accountNo;}

    public void setAccountNo(String accountNo) {this.accountNo = accountNo;}

    @Column(name="CORE_ACC_STATUS")
    public String getCoreAccountStatus() {return coreAccountStatus;}

    public void setCoreAccountStatus(String coreAccountStatus) {this.coreAccountStatus = coreAccountStatus;}

    @Column(name="PARENT_AGENT_ID")
    public Long getParentAgentId() {
        return parentAgentId;
    }

    public void setParentAgentId(Long parentAgentId) {
        this.parentAgentId = parentAgentId;
    }
}

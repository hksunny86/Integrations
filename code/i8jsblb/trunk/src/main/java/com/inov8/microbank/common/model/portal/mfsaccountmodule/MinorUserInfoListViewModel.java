package com.inov8.microbank.common.model.portal.mfsaccountmodule;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "MINOR_USER_INFO_LIST_VIEW")
public class MinorUserInfoListViewModel extends BasePersistableModel {

    private static final long serialVersionUID = -3733418495452365387L;
    private Long appUserId;
    private String name;
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private Date dob;
    private String nic;
    private Boolean verified;
    private String userId;
    private String mobileNo;
    private String state;
    private String country;
    private String accountEnabled;
    private Long userDeviceAccountsId;
    private String fullAddress;
    private String fullAddressCity;
    private String fullName;
    private String isAccountLocked;
    private Integer customerAccountTypeId;
    private String customerAccountType;
    private String isCredentialExpired;
    private String district;
    private String accountOpenedBy;
    private String accountOpenerName;
    private Long accountOpenerId;
    private Date accountOpeningDate;
    private String segment;
    private String isAccountClosed;
    private String registrationStateID;
    private String registrationState;
    private String accountStateId;
    private String accountState;
    private String initialAppFormNo;
    private String agentBusinessName;
    private Long regionId;
    private String region;
    private Long areaId;
    private String area;
    private Long areaLevelId;
    private String areaLevel;
    private Long businessCityId;
    private String businessCity;
    private Long agentNetworkId;
    private String agentNetworkName;
    private String accountTitle;
    private String accountOpeningMethodId;
    private String accountOpeningMethodName;
    private Date accountUpdatedOn;
    private String accountUpdatedBy;
    private Date startDate;
    private Date endDate;
    private Date createdStartDate;
    private Date createdEndDate;

    private String customerMobileNetwork;
    private Long mobileNetworkId;
    private String iban;
    private String companyName;
    private String stockTrading;
    private String mutualFunds;
    private String clsResponseCode;
    private String fatherCnic;
    private String motherCnic;

    @Column(name = "CLS_RESPONSE_CODE")
    public String getClsResponseCode() {
        return clsResponseCode;
    }

    public void setClsResponseCode(String clsResponseCode) {
        this.clsResponseCode = clsResponseCode;
    }

    @Column(name = "STOCK_TRADING")
    public String getStockTrading() {
        return stockTrading;
    }

    public void setStockTrading(String stockTrading) {
        this.stockTrading = stockTrading;
    }

    @Column(name = "MUTUAL_FUNDS")
    public String getMutualFunds() {
        return mutualFunds;
    }

    public void setMutualFunds(String mutualFunds) {
        this.mutualFunds = mutualFunds;
    }




    @javax.persistence.Transient
    public Date getCreatedStartDate() {
        return createdStartDate;
    }

    public void setCreatedStartDate(Date createdStartDate) {
        this.createdStartDate = createdStartDate;
    }
    @javax.persistence.Transient
    public Date getCreatedEndDate() {
        return createdEndDate;
    }

    public void setCreatedEndDate(Date createdEndDate) {
        this.createdEndDate = createdEndDate;
    }

    @Transient
    public Long getAgentNetworkId() {
        return agentNetworkId;
    }

    public void setAgentNetworkId(Long agentNetworkId) {
        this.agentNetworkId = agentNetworkId;
    }

    @Transient
    public String getAgentNetworkName() {
        return agentNetworkName;
    }

    public void setAgentNetworkName(String agentNetworkName) {
        this.agentNetworkName = agentNetworkName;
    }

    @Column(name = "BUSINESS_CITY_ID")
    public Long getBusinessCityId() {
        return businessCityId;
    }

    public void setBusinessCityId(Long businessCityId) {
        this.businessCityId = businessCityId;
    }

    @Column(name = "BUSINESS_CITY_NAME")
    public String getBusinessCity() {
        return businessCity;
    }

    public void setBusinessCity(String businessCity) {
        this.businessCity = businessCity;
    }

    @Column(name = "REGION_ID")
    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    @Column(name = "AREA_ID")
    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Column(name = "AREA_LEVEL_ID")
    public Long getAreaLevelId() {
        return areaLevelId;
    }

    public void setAreaLevelId(Long areaLevelId) {
        this.areaLevelId = areaLevelId;
    }

    @Column(name = "REGION_NAME")
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Column(name = "AREA_LEVEL_NAME")
    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
    }

    @Column(name = "AREA_NAME")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Column(name = "COMPANY_NAME")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Default constructor.
     */
    public MinorUserInfoListViewModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getAppUserId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setAppUserId(primaryKey);
    }

    /**
     * Returns the value of the <code>appUserId</code> property.
     */
    @Column(name = "APP_USER_ID", nullable = false)
    @Id
    public Long getAppUserId() {
        return appUserId;
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
     */

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    /**
     * Returns the value of the <code>name</code> property.
     */
    @Column(name = "NAME", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the <code>name</code> property.
     *
     * @param name the value for the <code>name</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value of the <code>firstName</code> property.
     */
    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the <code>firstName</code> property.
     *
     * @param firstName the value for the <code>firstName</code> property
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
     */
    @Column(name = "LAST_NAME", nullable = false, length = 50)
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the <code>lastName</code> property.
     *
     * @param lastName the value for the <code>lastName</code> property
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
     * Returns the value of the <code>address1</code> property.
     */
    @Column(name = "ADDRESS1", length = 250)
    public String getAddress1() {
        return address1;
    }

    /**
     * Sets the value of the <code>address1</code> property.
     *
     * @param address1 the value for the <code>address1</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * Returns the value of the <code>address2</code> property.
     */
    @Column(name = "ADDRESS2", length = 250)
    public String getAddress2() {
        return address2;
    }

    /**
     * Sets the value of the <code>address2</code> property.
     *
     * @param address2 the value for the <code>address2</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="250"
     */

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * Returns the value of the <code>city</code> property.
     */
    @Column(name = "CITY", length = 50)
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the <code>city</code> property.
     *
     * @param city the value for the <code>city</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the value of the <code>dob</code> property.
     */
    @Column(name = "DOB")
    public Date getDob() {
        return dob;
    }

    /**
     * Sets the value of the <code>dob</code> property.
     *
     * @param dob the value for the <code>dob</code> property
     * @spring.validator type="date"
     * @spring.validator-var name="datePattern" value="${date_format}"
     */

    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Returns the value of the <code>nic</code> property.
     */
    @Column(name = "NIC", length = 50)
    public String getNic() {
        return nic;
    }

    /**
     * Sets the value of the <code>nic</code> property.
     *
     * @param nic the value for the <code>nic</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setNic(String nic) {
        this.nic = nic;
    }

    /**
     * Returns the value of the <code>verified</code> property.
     */
    @Column(name = "IS_VERIFIED", nullable = false)
    public Boolean getVerified() {
        return verified;
    }

    /**
     * Sets the value of the <code>verified</code> property.
     *
     * @param verified the value for the <code>verified</code> property
     */

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * Returns the value of the <code>userId</code> property.
     */
    @Column(name = "USER_ID", length = 50)
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the <code>userId</code> property.
     *
     * @param userId the value for the <code>userId</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the value of the <code>mobileNo</code> property.
     */
    @Column(name = "MOBILE_NO", nullable = false, length = 50)
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the value of the <code>mobileNo</code> property.
     *
     * @param mobileNo the value for the <code>mobileNo</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "IS_ACCOUNT_LOCKED", nullable = false)
    public String getIsAccountLocked() {
        return this.isAccountLocked;
    }

    public void setIsAccountLocked(String isAccountLocked) {
        this.isAccountLocked = isAccountLocked;
    }


    /**
     * Returns the value of the <code>state</code> property.
     */
    @Column(name = "STATE", length = 50)
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the <code>state</code> property.
     *
     * @param state the value for the <code>state</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns the value of the <code>country</code> property.
     */
    @Column(name = "COUNTRY", length = 50)
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the <code>country</code> property.
     *
     * @param country the value for the <code>country</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the value of the <code>accountEnabled</code> property.
     */
    @Column(name = "IS_ACCOUNT_ENABLED", nullable = false)
    public String getAccountEnabled() {
        return accountEnabled;
    }

    /**
     * Sets the value of the <code>accountEnabled</code> property.
     *
     * @param accountEnabled the value for the <code>accountEnabled</code> property
     */

    public void setAccountEnabled(String accountEnabled) {
        this.accountEnabled = accountEnabled;
    }

    /**
     * Returns the value of the <code>userDeviceAccountsId</code> property.
     */
    @Column(name = "USER_DEVICE_ACCOUNTS_ID", nullable = false)
    public Long getUserDeviceAccountsId() {
        return userDeviceAccountsId;
    }

    /**
     * Sets the value of the <code>userDeviceAccountsId</code> property.
     *
     * @param userDeviceAccountsId the value for the <code>userDeviceAccountsId</code> property
     * @spring.validator type="long"
     * @spring.validator type="longRange"
     * @spring.validator-args arg1value="${var:min}"
     * @spring.validator-var name="min" value="0"
     * @spring.validator-args arg2value="${var:max}"
     * @spring.validator-var name="max" value="9999999999"
     */

    public void setUserDeviceAccountsId(Long userDeviceAccountsId) {
        this.userDeviceAccountsId = userDeviceAccountsId;
    }

    /**
     * Returns the value of the <code>fullAddress</code> property.
     */
    @Column(name = "FULL_ADDRESS", length = 668)
    public String getFullAddress() {
        return fullAddress;
    }

    /**
     * Sets the value of the <code>fullAddress</code> property.
     *
     * @param fullAddress the value for the <code>fullAddress</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="668"
     */

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Column(name = "FULL_ADDRESS_CITY")
    public String getFullAddressCity() {
        return fullAddressCity;
    }

    public void setFullAddressCity(String fullAddressCity) {
        this.fullAddressCity = fullAddressCity;
    }

    /**
     * Returns the value of the <code>fullName</code> property.
     */
    @Column(name = "FULL_NAME", length = 101)
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the value of the <code>fullName</code> property.
     *
     * @param fullName the value for the <code>fullName</code> property
     * @spring.validator type="maxlength"
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="101"
     * @spring.validator type="mask"
     * @spring.validator-args arg1value="${mask}"
     * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
     */

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     *
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + getAppUserId();
        checkBox += "\"/>";
        return checkBox;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&appUserId=" + getAppUserId();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "appUserId";
        return primaryKeyFieldName;
    }

    @Column(name = "CUSTOMER_ACCOUNT_TYPE_ID")
    public Integer getCustomerAccountTypeId() {
        return customerAccountTypeId;
    }

    public void setCustomerAccountTypeId(Integer customerAccountTypeId) {
        this.customerAccountTypeId = customerAccountTypeId;
    }

    @Column(name = "CUSTOMER_ACCOUNT_TYPE", length = 668)
    public String getCustomerAccountType() {
        return customerAccountType;
    }

    public void setCustomerAccountType(String customerAccountType) {
        this.customerAccountType = customerAccountType;
    }

    @Column(name = "IS_CREDENTIALS_EXPIRED", nullable = false)
    public String getIsCredentialExpired() {
        return isCredentialExpired;
    }

    public void setIsCredentialExpired(String isCredentialExpired) {
        this.isCredentialExpired = isCredentialExpired;
    }

    @Column(name = "DISTRICT", nullable = false, length = 50)
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Column(name = "account_opened_by", nullable = false, length = 50)
    public String getAccountOpenedBy() {
        return accountOpenedBy;
    }

    public void setAccountOpenedBy(String accountOpenedBy) {
        this.accountOpenedBy = accountOpenedBy;
    }

    @Column(name = "ACCOUNT_OPENER_NAME", nullable = false, length = 50)
    public String getAccountOpenerName() {
        return accountOpenerName;
    }

    public void setAccountOpenerName(String accountOpenerName) {
        this.accountOpenerName = accountOpenerName;
    }

    @Column(name = "ACCOUNT_OPENER_ID")
    public Long getAccountOpenerId() {
        return accountOpenerId;
    }

    public void setAccountOpenerId(Long accountOpenerId) {
        this.accountOpenerId = accountOpenerId;
    }

    @Column(name = "ACCOUNT_OPENING_DATE", nullable = false, length = 50)
    public Date getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(Date accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    @Column(name = "SEGMENT")
    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    @Column(name = "IS_ACCOUNT_CLOSED")
    public String getIsAccountClosed() {
        return isAccountClosed;
    }

    public void setIsAccountClosed(String isAccountClosed) {
        this.isAccountClosed = isAccountClosed;
    }

    @Column(name = "REGISTRATION_STATE_ID")
    public String getRegistrationStateID() {
        return registrationStateID;
    }

    public void setRegistrationStateID(String registrationStateID) {
        this.registrationStateID = registrationStateID;
    }

    @Column(name = "REGISTRATION_STATE")
    public String getRegistrationState() {
        return registrationState;
    }

    public void setRegistrationState(String registrationState) {
        this.registrationState = registrationState;
    }

    @Column(name = "ACCOUNT_STATE_ID")
    public String getAccountStateId() {
        return accountStateId;
    }

    public void setAccountStateId(String accountStateId) {
        this.accountStateId = accountStateId;
    }

    @Column(name = "ACCOUNT_STATE_NAME")
    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    @Column(name = "INITIAL_APP_FORM_NUMBER")
    public String getInitialAppFormNo() {
        return initialAppFormNo;
    }

    public void setInitialAppFormNo(String initialAppFormNo) {
        this.initialAppFormNo = initialAppFormNo;
    }

    @Column(name = "BUSINESS_NAME")
    public String getAgentBusinessName() {
        return agentBusinessName;
    }

    public void setAgentBusinessName(String agentBusinessName) {
        if (agentBusinessName != null) {
            this.agentBusinessName = agentBusinessName;
        }
    }

    @Column(name = "ACCOUNT_TITLE")
    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    @Column(name = "ACCOUNT_OPENING_METHOD_ID")
    public String getAccountOpeningMethodId() {
        return accountOpeningMethodId;
    }

    public void setAccountOpeningMethodId(String accountOpeningMethodId) {
        this.accountOpeningMethodId = accountOpeningMethodId;
    }

    @Column(name = "ACCOUNT_OPENING_METHOD_NAME")
    public String getAccountOpeningMethodName() {
        return accountOpeningMethodName;
    }

    public void setAccountOpeningMethodName(String accountOpeningMethodName) {
        this.accountOpeningMethodName = accountOpeningMethodName;
    }

    @Column(name = "UPDATED_ON")
    public Date getAccountUpdatedOn() {
        return accountUpdatedOn;
    }

    public void setAccountUpdatedOn(Date accountUpdatedOn) {
        this.accountUpdatedOn = accountUpdatedOn;
    }

    @Column(name = "UPDATED_BY")
    public String getAccountUpdatedBy() {
        return accountUpdatedBy;
    }

    public void setAccountUpdatedBy(String accountUpdatedBy) {
        this.accountUpdatedBy = accountUpdatedBy;
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

    @Column(name = "CUST_MOBILE_NETWORK")
    public String getCustomerMobileNetwork() {
        return customerMobileNetwork;
    }

    public void setCustomerMobileNetwork(String customerMobileNetwork) {
        this.customerMobileNetwork = customerMobileNetwork;
    }

    @Column(name = "MOBILE_NETWORK_ID")
    public Long getMobileNetworkId() {
        return mobileNetworkId;
    }

    public void setMobileNetworkId(Long mobileNetworkId) {
        this.mobileNetworkId = mobileNetworkId;
    }

    @Column(name = "IBAN")
    public String getIban() { return iban; }

    public void setIban(String iban) { this.iban = iban; }

    @Column(name = "FATHER_CNIC")
    public String getFatherCnic() {return fatherCnic;}

    public void setFatherCnic(String fatherCnic) {this.fatherCnic = fatherCnic;}

    @Column(name = "MOTHER_CNIC")
    public String getMotherCnic() {return motherCnic;}

    public void setMotherCnic(String motherCnic) {this.motherCnic = motherCnic;}
}

package com.inov8.microbank.common.model.portal.blbreports;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity( mutable=false )
@Table(name = "HANDLER_ACTIVITY_REPORT_VIEW")
public class HandlerActivityReportModel extends BasePersistableModel implements Serializable {

    private Long pk;
    private String agentId;
    private String jsCashAccountNo;
    private String coreAccountNo;
    private String accountType;
    private Long customerAccountTypeId;
    private String agentBusinessName;
    private String firstName;
    private String lastName;
    private String address;
    private String cnic;
    private String mobileNo;
    private String filerStatus;
    private String agentNetwork;
    private String region;
    private String area;
    private String areaLevelName;
    private String city;
    private String taxRegime;
    private String salesUser;
    private String agentLevel;
    private String isMainAgent;
    private Long handlerAgentId;
    private String designation;
    private String jcashAccountStatus;
    private String coreAccountStatus;
    private String credentialExpired;
    private String accountBalance;
    private String transactionType;
    private String transactionCount;
    private String transactionAmount;
    private String paymentMode;
    private Date accountOpeningDate;
    private Date accountClosingDate;
    private Date transactionSummaryDate;
    private Date startDate;
    private Date endDate;

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return getPk();
    }

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setPk( primaryKey );
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&pk=" + pk;
    }


    @Id
    @Column(name="PK")
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Column(name="MAIN_AGENT_ID")
    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Column(name="JSCASH_ACCOUNT_NO")
    public String getJsCashAccountNo() {
        return jsCashAccountNo;
    }

    public void setJsCashAccountNo(String jsCashAccountNo) {
        this.jsCashAccountNo = jsCashAccountNo;
    }

    @Column(name="CORE_ACCOUNT_NO")
    public String getCoreAccountNo() {
        return coreAccountNo;
    }

    public void setCoreAccountNo(String coreAccountNo) {
        this.coreAccountNo = coreAccountNo;
    }

    @Column(name="ACCOUNT_TYPE")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name="ACCOUNT_TYPE_ID")
    public Long getCustomerAccountTypeId() {return customerAccountTypeId;}

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {this.customerAccountTypeId = customerAccountTypeId;}

    @Column(name="AGENT_BUSINESS_NAME")
    public String getAgentBusinessName() {
        return agentBusinessName;
    }

    public void setAgentBusinessName(String agentBusinessName) {
        this.agentBusinessName = agentBusinessName;
    }

    @Column(name="FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name="LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name="ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name="CNIC")
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name="MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name="FILER_STATUS")
    public String getFilerStatus() {
        return filerStatus;
    }

    public void setFilerStatus(String filerStatus) {
        this.filerStatus = filerStatus;
    }

    @Column(name="AGENT_NETWORK")
    public String getAgentNetwork() {
        return agentNetwork;
    }

    public void setAgentNetwork(String agentNetwork) {
        this.agentNetwork = agentNetwork;
    }

    @Column(name="REIGON")
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Column(name="AREA")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Column(name="AREA_LEVEL_NAME")
    public String getAreaLevelName() {
        return areaLevelName;
    }

    public void setAreaLevelName(String areaLevelName) {
        this.areaLevelName = areaLevelName;
    }

    @Column(name="CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name="TAX_REGIME")
    public String getTaxRegime() {
        return taxRegime;
    }

    public void setTaxRegime(String taxRegime) {
        this.taxRegime = taxRegime;
    }

    @Column(name="SALES_USER_NAME")
    public String getSalesUser() {
        return salesUser;
    }

    public void setSalesUser(String salesUser) {
        this.salesUser = salesUser;
    }

    @Column(name="AGENT_LEVEL")
    public String getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    @Column(name="IS_MAIN_AGENT")
    public String getIsMainAgent() {
        return isMainAgent;
    }

    public void setIsMainAgent(String isMainAgent) {
        this.isMainAgent = isMainAgent;
    }

    @Column(name="HANDLER_AGENT_ID")
    public Long getHandlerAgentId() {
        return handlerAgentId;
    }

    public void setHandlerAgentId(Long handlerAgentId) {
        this.handlerAgentId = handlerAgentId;
    }

    @Column(name="JCASH_ACCOUNT_STATUS")
    public String getJcashAccountStatus() {
        return jcashAccountStatus;
    }

    public void setJcashAccountStatus(String jcashAccountStatus) {
        this.jcashAccountStatus = jcashAccountStatus;
    }

    @Column(name="CORE_ACCOUNT_STATUS")
    public String getCoreAccountStatus() {
        return coreAccountStatus;
    }

    public void setCoreAccountStatus(String coreAccountStatus) {
        this.coreAccountStatus = coreAccountStatus;
    }

    @Column(name="CREDENTIALS_EXPIRED")
    public String getCredentialExpired() {
        return credentialExpired;
    }

    public void setCredentialExpired(String credentialExpired) {
        this.credentialExpired = credentialExpired;
    }

    @Column(name="ACCOUNT_BALANCE")
    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Column(name="TRANSACTION_TYPE")
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Column(name="TRANSACTION_COUNT")
    public String getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(String transactionCount) {
        this.transactionCount = transactionCount;
    }

    @Column(name="PAYMENT_MODE")
    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    @Column(name="ACCOUNT_OPENING_DATE")
    public Date getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(Date accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    @Column(name="TRANSACTION_SUMMARY_DATE")
    public Date getTransactionSummaryDate() {
        return transactionSummaryDate;
    }

    public void setTransactionSummaryDate(Date transactionSummaryDate) {
        this.transactionSummaryDate = transactionSummaryDate;
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

    @Column(name="TRANSACTION_AMOUNT")
    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Column(name="ACCOUNT_CLOSING_DATE")
    public Date getAccountClosingDate() {
        return accountClosingDate;
    }

    public void setAccountClosingDate(Date accountClosingDate) {
        this.accountClosingDate = accountClosingDate;
    }
}

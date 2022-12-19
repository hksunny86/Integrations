package com.inov8.microbank.common.model.portal.blbreports;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity( mutable=false )
@Table(name = "BDE_KPI_REPORT_VIEW")
public class BdeKpiReportViewModel extends BasePersistableModel implements Serializable, RowMapper {

    private Long accountInfoId;
    private Long appUserId;
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
    private Long bdeEmployeeId;
    private String bdeName;
    private String designation;
    private Long itpChallanTransactions;
    private Long ubpTransactions;
    private Long kpkChallanTransactions;
    private String jcashAccountStatus;
    private String coreAccountStatus;
    private String credentialExpired;
    private String accountBalance;
    private String ubpTransactionsAmount;
    private String kpkChallanTransactionsAmount;
    private String itpChallanTransactionsAmount;
    private Date accountOpeningDate;
    private Long walletOpening;
    private Date transactionSummaryDate;
    private Date startDate;
    private Date endDate;

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return getAccountInfoId();
    }

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setAccountInfoId( primaryKey );
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "accountInfoId";
        return primaryKeyFieldName;
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&accountInfoId=" + accountInfoId;
    }

    @Id
    @Column(name="ACCOUNT_INFO_ID")
    public Long getAccountInfoId() {
        return accountInfoId;
    }

    public void setAccountInfoId(Long accountInfoId) {
        this.accountInfoId = accountInfoId;
    }

    @Column(name="APP_USER_ID")
    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    @Column(name="AGENT_ID")
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

    @Column(name="SALE_USER")
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

    @Column(name="BDE_EMPLOYEE_ID")
    public Long getBdeEmployeeId() {
        return bdeEmployeeId;
    }

    public void setBdeEmployeeId(Long bdeEmployeeId) {
        this.bdeEmployeeId = bdeEmployeeId;
    }

    @Column(name="BDE_NAME")
    public String getBdeName() {
        return bdeName;
    }

    public void setBdeName(String bdeName) {
        this.bdeName = bdeName;
    }

    @Column(name="DESIGNATION")
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Column(name="ITP_CHALLAN_TRANSACTIONS")
    public Long getItpChallanTransactions() {
        return itpChallanTransactions;
    }

    public void setItpChallanTransactions(Long itpChallanTransactions) {
        this.itpChallanTransactions = itpChallanTransactions;
    }

    @Column(name="UBP_TRANSACTIONS")
    public Long getUbpTransactions() {
        return ubpTransactions;
    }

    public void setUbpTransactions(Long upbTransactions) {
        this.ubpTransactions = upbTransactions;
    }

    @Column(name="KPK_CHALLAN_TRANSACTIONS")
    public Long getKpkChallanTransactions() {
        return kpkChallanTransactions;
    }

    public void setKpkChallanTransactions(Long kpkChallanTransactions) {
        this.kpkChallanTransactions = kpkChallanTransactions;
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

    @Column(name="ACCOUNT_OPENING_DATE")
    public Date getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(Date accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    @Column(name="WALLET_OPENING")
    public Long getWalletOpening() {
        return walletOpening;
    }

    public void setWalletOpening(Long walletOpening) {
        this.walletOpening = walletOpening;
    }

    @Column(name="AGENT_BUSINESS_NAME")
    public String getAgentBusinessName() {
        return agentBusinessName;
    }

    public void setAgentBusinessName(String agentBusinessName) {
        this.agentBusinessName = agentBusinessName;
    }

    @Column(name="CUSTOMER_ACCOUNT_TYPE_ID")
    public Long getCustomerAccountTypeId() {
        return customerAccountTypeId;
    }

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        this.customerAccountTypeId = customerAccountTypeId;
    }

    @Column(name="TRANSACTION_SUMMARY_DATE")
    public Date getTransactionSummaryDate() {
        return transactionSummaryDate;
    }

    public void setTransactionSummaryDate(Date transactionSummaryDate) {
        this.transactionSummaryDate = transactionSummaryDate;
    }

    @Column(name="UBP_TRANSACTIONS_AMOUNT")
    public String getUbpTransactionsAmount() {
        return ubpTransactionsAmount;
    }

    public void setUbpTransactionsAmount(String ubpTransactionsAmount) {
        this.ubpTransactionsAmount = ubpTransactionsAmount;
    }

    @Column(name="KP_TRANSACTIONS_AMOUNT")
    public String getKpkChallanTransactionsAmount() {
        return kpkChallanTransactionsAmount;
    }

    public void setKpkChallanTransactionsAmount(String kpkChallanTransactionsAmount) {
        this.kpkChallanTransactionsAmount = kpkChallanTransactionsAmount;
    }

    @Column(name="ITP_TRANSACTION_AMOUNT")
    public String getItpChallanTransactionsAmount() {
        return itpChallanTransactionsAmount;
    }

    public void setItpChallanTransactionsAmount(String itpChallanTransactionsAmount) {
        this.itpChallanTransactionsAmount = itpChallanTransactionsAmount;
    }

    @Transient
    @Temporal(TemporalType.DATE)
    public Date getStartDate() {
        return startDate;
    }

    @Temporal(TemporalType.DATE)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Transient
    @Temporal(TemporalType.DATE)
    public Date getEndDate() {
        return endDate;
    }

    @Temporal(TemporalType.DATE)
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        BdeKpiReportViewModel model = new BdeKpiReportViewModel();
        model.setAppUserId(resultSet.getLong("APP_USER_ID"));
        model.setAgentId(resultSet.getString("AGENT_ID"));
        model.setJsCashAccountNo(resultSet.getString("JSCASH_ACCOUNT_NO"));
        model.setCoreAccountNo(resultSet.getString("CORE_ACCOUNT_NO"));
        model.setCustomerAccountTypeId(resultSet.getLong("CUSTOMER_ACCOUNT_TYPE_ID"));
        model.setAccountType(resultSet.getString("ACCOUNT_TYPE"));
        model.setAgentBusinessName(resultSet.getString("AGENT_BUSINESS_NAME"));
        model.setFirstName(resultSet.getString("FIRST_NAME"));
        model.setLastName(resultSet.getString("LAST_NAME"));
        model.setAddress(resultSet.getString("ADDRESS"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NO"));
        model.setFilerStatus(resultSet.getString("FILER_STATUS"));
        model.setAgentNetwork(resultSet.getString("AGENT_NETWORK"));
        model.setRegion(resultSet.getString("REIGON"));
        model.setArea(resultSet.getString("AREA"));
        model.setAreaLevelName(resultSet.getString("AREA_LEVEL_NAME"));
        model.setCity(resultSet.getString("CITY"));
        model.setTaxRegime(resultSet.getString("TAX_REGIME"));
        model.setSalesUser(resultSet.getString("SALE_USER"));
        model.setAgentLevel(resultSet.getString("AGENT_LEVEL"));
        model.setIsMainAgent(resultSet.getString("IS_MAIN_AGENT"));
        model.setBdeEmployeeId(resultSet.getLong("BDE_EMPLOYEE_ID"));
        model.setBdeName(resultSet.getString("BDE_NAME"));
        model.setDesignation(resultSet.getString("DESIGNATION"));
        model.setItpChallanTransactions(resultSet.getLong("ITP_CHALLAN_TRANSACTIONS"));
        model.setItpChallanTransactionsAmount(resultSet.getString("ITP_TRANSACTION_AMOUNT"));
        model.setWalletOpening(resultSet.getLong("WALLET_OPENING"));
        model.setUbpTransactions(resultSet.getLong("UBP_TRANSACTIONS"));
        model.setUbpTransactionsAmount(resultSet.getString("UBP_TRANSACTIONS_AMOUNT"));
        model.setKpkChallanTransactions(resultSet.getLong("KPK_CHALLAN_TRANSACTIONS"));
        model.setKpkChallanTransactionsAmount(resultSet.getString("KP_TRANSACTIONS_AMOUNT"));
        model.setTransactionSummaryDate(resultSet.getDate("TRANSACTION_SUMMARY_DATE"));
        model.setJcashAccountStatus(resultSet.getString("JCASH_ACCOUNT_STATUS"));
        model.setCoreAccountStatus(resultSet.getString("CORE_ACCOUNT_STATUS"));
        model.setCredentialExpired(resultSet.getString("CREDENTIALS_EXPIRED"));
        model.setAccountBalance(resultSet.getString("ACCOUNT_BALANCE"));
        model.setAccountOpeningDate(resultSet.getDate("ACCOUNT_OPENING_DATE"));
        model.setAccountInfoId(resultSet.getLong("ACCOUNT_INFO_ID"));
        return model;
    }
}

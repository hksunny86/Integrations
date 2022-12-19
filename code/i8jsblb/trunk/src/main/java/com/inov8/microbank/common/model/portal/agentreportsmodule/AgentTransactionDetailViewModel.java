package com.inov8.microbank.common.model.portal.agentreportsmodule;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Mar 28, 2013 7:56:45 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@XmlRootElement(name = "agentTransactionDetailViewModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "AGENT_TRANSACTION_DETAIL_VIEW")
public class AgentTransactionDetailViewModel extends BasePersistableModel implements Serializable, RowMapper {
    private static final long serialVersionUID = -2957837944212780742L;

    // Fields
    private String msisdn;
    private String agentId;
    private Long distributorId;
    private String distributorName;
    private Long regionId;
    private String regionName;
    private Long retailerId;
    private String retailerName;
    private Long transactionId;
    private Date createdOn;
    private Date transactionDate;
    private Date transactionDateOnly;
    private String description;
    private Long productId;
    private String productName;
    private Long supplierId;
    private Double bankDebitAmount;
    private Double bankCreditAmount;
    private Double bankBalance;
    private String accountType;
    private String cnicOrAccountid;
    private Double agentCommission;
    private Double franchiseCommission;
    private Date startDate;
    private Date endDate;
    private String agentBusinessName;
    private String areaName;
    private String areaLevel;
    private String cityName;
    private Long soId;

    @Column(name = "BUSINESS_NAME")
    public String getAgentBusinessName() {
        return agentBusinessName;
    }

    public void setAgentBusinessName(String agentBusinessName) {
        this.agentBusinessName = agentBusinessName;
    }

    @Column(name = "AREA_NAME")
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Column(name = "AREA_LEVEL_NAME")
    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
    }

    @Column(name = "BUSINESS_CITY_NAME")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * default constructor
     */
    public AgentTransactionDetailViewModel() {
    }

    @Transient
    @Override
    public Long getPrimaryKey() {
        return getTransactionId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setTransactionId(primaryKey);
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter() {
        return "&transactionId=" + transactionId;
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName() {
        return "transactionId";
    }

    // Property accessors
    @Column(name = "MSISDN", length = 11)
    public String getMsisdn() {
        return this.msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Column(name = "AGENT_ID", length = 50)
    public String getAgentId() {
        return this.agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Column(name = "DISTRIBUTOR_ID", precision = 10, scale = 0)
    public Long getDistributorId() {
        return this.distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    @Column(name = "DISTRIBUTOR_NAME", length = 50)
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

    @Column(name = "REGION_NAME", length = 50)
    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Column(name = "RETAILER_ID", precision = 10, scale = 0)
    public Long getRetailerId() {
        return this.retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    @Column(name = "RETAILER_NAME", length = 50)
    public String getRetailerName() {
        return this.retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    @Id
    @Column(name = "TRANSACTION_ID", precision = 10, scale = 0)
    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Column(name = "CREATED_ON", length = 7)
    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "TRANSACTION_DATE", length = 7)
    public Date getTransactionDate() {
        return this.transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Column(name = "TRANSACTION_DATE_ONLY", length = 7)
    public Date getTransactionDateOnly() {
        return this.transactionDateOnly;
    }

    public void setTransactionDateOnly(Date transactionDateOnly) {
        this.transactionDateOnly = transactionDateOnly;
    }

    @Column(name = "DESCRIPTION", length = 100)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "PRODUCT_ID", precision = 10, scale = 0)
    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "SUPPLIER_ID", precision = 10, scale = 0)
    public Long getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "DEBIT_AMOUNT", precision = 10)
    public Double getBankDebitAmount() {
        return this.bankDebitAmount;
    }

    public void setBankDebitAmount(Double bankDebitAmount) {
        this.bankDebitAmount = bankDebitAmount;
    }

    @Column(name = "CREDIT_AMOUNT", precision = 10)
    public Double getBankCreditAmount() {
        return this.bankCreditAmount;
    }

    public void setBankCreditAmount(Double bankCreditAmount) {
        this.bankCreditAmount = bankCreditAmount;
    }

    @Column(name = "BALANCE", precision = 22, scale = 0)
    public Double getBankBalance() {
        return this.bankBalance;
    }

    public void setBankBalance(Double bankBalance) {
        this.bankBalance = bankBalance;
    }

    @Column(name = "ACCOUNT_TYPE", length = 12)
    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name = "CNIC_OR_ACCOUNTID", length = 250)
    public String getCnicOrAccountid() {
        return this.cnicOrAccountid;
    }

    public void setCnicOrAccountid(String cnicOrAccountid) {
        this.cnicOrAccountid = cnicOrAccountid;
    }

    @Column(name = "AGENT_COMMISSION")
    public Double getAgentCommission() {
        return agentCommission;
    }

    public void setAgentCommission(Double agentCommission) {
        this.agentCommission = agentCommission;
    }

    @Column(name = "FRANCHISE_COMMISSION")
    public Double getFranchiseCommission() {
        return franchiseCommission;
    }

    public void setFranchiseCommission(Double franchiseCommission) {
        this.franchiseCommission = franchiseCommission;
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


    @Column(name = "SERVICE_OPERATOR_ID")
    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AgentTransactionDetailViewModel vo = new AgentTransactionDetailViewModel();
        vo.setMsisdn(resultSet.getString("MSISDN"));
        vo.setAgentId(resultSet.getString("AGENT_ID"));
        vo.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
        vo.setDistributorName(resultSet.getString("DISTRIBUTOR_NAME"));
        vo.setRegionId(resultSet.getLong("REGION_ID"));
        vo.setRegionName(resultSet.getString("REGION_NAME"));
        vo.setRetailerId(resultSet.getLong("RETAILER_ID"));
        vo.setRetailerName(resultSet.getString("RETAILER_NAME"));
        vo.setTransactionId(resultSet.getLong("TRANSACTION_ID"));
        vo.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        vo.setTransactionDate(resultSet.getDate("TRANSACTION_DATE"));
        vo.setTransactionDateOnly(resultSet.getDate("TRANSACTION_DATE_ONLY"));
        vo.setDescription(resultSet.getString("DESCRIPTION"));
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setProductName(resultSet.getString("PRODUCT_NAME"));
        vo.setSupplierId(resultSet.getLong("SUPPLIER_ID"));
        vo.setBankDebitAmount(resultSet.getDouble("DEBIT_AMOUNT"));
        vo.setBankCreditAmount(resultSet.getDouble("CREDIT_AMOUNT"));
        vo.setBankBalance(resultSet.getDouble("BALANCE"));
        vo.setAccountType(resultSet.getString("ACCOUNT_TYPE"));
        vo.setCnicOrAccountid(resultSet.getString("CNIC_OR_ACCOUNTID"));
        vo.setAgentCommission(resultSet.getDouble("AGENT_COMMISSION"));
        vo.setFranchiseCommission(resultSet.getDouble("FRANCHISE_COMMISSION"));
        vo.setAgentBusinessName(resultSet.getString("BUSINESS_NAME"));
        vo.setAreaName(resultSet.getString("AREA_NAME"));
        vo.setAreaLevel(resultSet.getString("AREA_LEVEL_NAME"));
        vo.setCityName(resultSet.getString("BUSINESS_CITY_NAME"));
        vo.setSoId(resultSet.getLong("SERVICE_OPERATOR_ID"));
        return vo;
    }
}
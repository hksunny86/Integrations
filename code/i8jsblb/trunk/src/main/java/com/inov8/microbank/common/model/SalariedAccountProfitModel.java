package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@SequenceGenerator(name = "SALARIED_ACCOUNT_PROFIT_SEQ", sequenceName = "SALARIED_ACCOUNT_PROFIT_SEQ", allocationSize = 1)
@Table(name = "SALARIED_ACCOUNT_PROFIT")
public class SalariedAccountProfitModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private Long id;
    private String mobileNo;
    private String cnic;
    private String profitAmount;
    private Long accountId;
    private Date creditUpdatedOn;
    private Date DebitUpdatedOn;
    private Date LastCreatedOn;
    private Date LastUpdatedOn;



    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALARIED_ACCOUNT_PROFIT_SEQ")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "MOBILE_NO", nullable = false)
    public String getMobileNo() {
        return mobileNo;
    }
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    @Column(name = "CNIC", nullable = false)
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name = "PROFIT_CALCULATED_AMOUNT", nullable = false)
    public String getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(String profitAmount) {
        this.profitAmount = profitAmount;
    }



    @Column(name = "ACCOUNT_ID", nullable = false)
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }


    @Column(name = "CREDIT_UPDATED_ON")
    public Date getCreditUpdatedOn() {
        return creditUpdatedOn;
    }

    public void setCreditUpdatedOn(Date creditUpdatedOn) {
        this.creditUpdatedOn = creditUpdatedOn;
    }
    @Column(name = "DEBIT_UPDATED_ON")
    public Date getDebitUpdatedOn() {
        return DebitUpdatedOn;
    }

    public void setDebitUpdatedOn(Date debitUpdatedOn) {
        DebitUpdatedOn = debitUpdatedOn;
    }


    @Column(name = "LAST_CREATED_ON")
    public Date getLastCreatedOn() {
        return LastCreatedOn;
    }

    public void setLastCreatedOn(Date lastCreatedOn) {
        LastCreatedOn = lastCreatedOn;
    }
    @Column(name = "LAST_UPDATED_ON")
    public Date getLastUpdatedOn() {
        return LastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        LastUpdatedOn = lastUpdatedOn;
    }


    @Transient
    public Long getPrimaryKey() {
        return getId();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "id";
        return primaryKeyFieldName;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&Id=" + getId();
        return parameters;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setId(primaryKey);

    }

    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + this.getId();
        checkBox += "\"/>";
        return checkBox;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        SalariedAccountProfitModel model = new SalariedAccountProfitModel();
        model.setId(resultSet.getLong("ID"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NO"));
        model.setProfitAmount(resultSet.getString("PROFIT_CALCULATED_AMOUNT"));
        model.setAccountId(resultSet.getLong("ACCOUNT_ID"));
        model.setDebitUpdatedOn(resultSet.getTimestamp("DEBIT_UPDATED_ON"));
        model.setCreditUpdatedOn(resultSet.getTimestamp("CREDIT_UPDATED_ON"));
        model.setLastCreatedOn(resultSet.getTimestamp("LAST_CREATED_ON"));
        model.setLastUpdatedOn(resultSet.getTimestamp("LAST_UPDATED_ON"));
        return model;
    }
}

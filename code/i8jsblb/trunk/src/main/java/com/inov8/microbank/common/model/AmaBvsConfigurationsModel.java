package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.springframework.jdbc.core.RowMapper;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "AMA_BVS_CONFIGURATIONS")
public class AmaBvsConfigurationsModel extends BasePersistableModel implements Serializable ,Cloneable, RowMapper {

    private static final long serialVersionUID = -2694580309541453092L;

    private Long amaBvsConfigurationsId;
    private Long noOfDays;
    private Long daysOfIntimation;
    private Double amaDebitBlockAmount;
    private Long createdBy;
    private Long updatedBy;
    private Date createdOn;
    private Date updatedOn;

    public AmaBvsConfigurationsModel() {
    }

    @Override
    @Transient
    public Long getPrimaryKey() {
        return getAmaBvsConfigurationsId();
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName() {
        return "amaBvsConfigurationsId";
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter() {
        return "&amaBvsConfigurationsId="+getAmaBvsConfigurationsId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setAmaBvsConfigurationsId(primaryKey);
    }

    // Property accessors
    @Id
    @Column(name = "AMA_BVS_CONFIGURATIONS_ID")
    public Long getAmaBvsConfigurationsId() {
        return this.amaBvsConfigurationsId;
    }

    public void setAmaBvsConfigurationsId(Long amaBvsConfigurationsId) {this.amaBvsConfigurationsId = amaBvsConfigurationsId;}

    @Column(name = "NO_OF_DAYS")
    public Long getNoOfDays() {return noOfDays;}

    public void setNoOfDays(Long noOfDays) {this.noOfDays = noOfDays;}

    @Column(name = "DAYS_OF_INTIMATION")
    public Long getDaysOfIntimation() {return daysOfIntimation;}

    public void setDaysOfIntimation(Long daysOfIntimation) {this.daysOfIntimation = daysOfIntimation;}

    @Column(name = "CREATED_BY")
    public Long getCreatedBy() {return createdBy;}

    public void setCreatedBy(Long createdBy) {this.createdBy = createdBy;}

    @Column(name = "UPDATED_BY")
    public Long getUpdatedBy() {return updatedBy;}

    public void setUpdatedBy(Long updatedBy) {this.updatedBy = updatedBy;}

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {return createdOn;}

    public void setCreatedOn(Date createdOn) {this.createdOn = createdOn;}

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {return updatedOn;}

    public void setUpdatedOn(Date updatedOn) {this.updatedOn = updatedOn;}

    @Column(name = "AMA_DEBIT_BLOCK_AMOUNT")
    public Double getAmaDebitBlockAmount() {
        return amaDebitBlockAmount;
    }

    public void setAmaDebitBlockAmount(Double amaDebitBlockAmount) {
        this.amaDebitBlockAmount = amaDebitBlockAmount;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AmaBvsConfigurationsModel model = new AmaBvsConfigurationsModel();
        model.setNoOfDays(resultSet.getLong("NO_OF_DAYS"));
        model.setAmaDebitBlockAmount(resultSet.getDouble("AMA_DEBIT_BLOCK_AMOUNT"));
        model.setDaysOfIntimation(resultSet.getLong("DAYS_OF_INTIMATION"));
        return model;
    }
}

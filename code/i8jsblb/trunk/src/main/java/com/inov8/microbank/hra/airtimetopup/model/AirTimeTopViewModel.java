package com.inov8.microbank.hra.airtimetopup.model;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "AIR_TIME_TOP_VIEW")
public class AirTimeTopViewModel extends BasePersistableModel implements Serializable, Cloneable {

    private Long conversionRateId;
    private String rateTypeName;
    private Double rate;
    private Timestamp startDate;
    private Timestamp endDate;
    private String createdBy;



    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) { setConversionRateId(aLong); }

    @javax.persistence.Transient
    public Long getPrimaryKey() { return getConversionRateId(); }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&conversionRateId=" + getConversionRateId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "conversionRateId";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "CONVERSION_RATE_ID")
    public Long getConversionRateId() { return conversionRateId; }

    public void setConversionRateId(Long conversionRateId) { this.conversionRateId = conversionRateId; }

    @Column(name = "NAME")
    public String getRateTypeName() { return rateTypeName; }

    public void setRateTypeName(String rateTypeName) { this.rateTypeName = rateTypeName; }

    @Column(name = "RATE")
    public Double getRate() { return rate; }

    public void setRate(Double rate) { this.rate = rate; }

    @Column(name = "START_DATE")
    public Timestamp getStartDate() { return startDate; }

    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    @Column(name = "END_DATE")
    public Timestamp getEndDate() { return endDate; }

    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }

    @Column(name = "USERNAME")
    public String getCreatedBy() { return createdBy; }

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

}

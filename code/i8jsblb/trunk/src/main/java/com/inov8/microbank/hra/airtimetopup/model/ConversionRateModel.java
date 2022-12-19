package com.inov8.microbank.hra.airtimetopup.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CONVERSION_RATE_SEQ",sequenceName = "CONVERSION_RATE_SEQ", allocationSize=1)
@Table(name = "CONVERSION_RATES")
public class ConversionRateModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private Long conversionRateId;
    private RateTypeModel rateTypeIdRateTypeModel;
    private Timestamp startDate;
    private Timestamp endDate;
    private Double rate;
    private Date createdOn;
    private Date updatedOn;
    private Double version;
    @Transient
    private Double dollarRate;

    private AppUserModel createdByAppUserModel;



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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONVERSION_RATE_SEQ")
    @Column(name = "CONVERSION_RATE_ID")
    public Long getConversionRateId() { return conversionRateId; }

    public void setConversionRateId(Long conversionRateId) { this.conversionRateId = conversionRateId; }


    @Column(name = "START_DATE")
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    @Column(name = "END_DATE")
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Column(name = "RATE")
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    /*@Column(name = "VERSION")
    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }*/

    @Transient
    public Double getDollarRate() {
        return dollarRate;
    }

    public void setDollarRate(Double dollarRate) {
        this.dollarRate = dollarRate;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getRelationCreatedByAppUserModel(){
        return createdByAppUserModel;
    }

    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel(){
        return getRelationCreatedByAppUserModel();
    }

    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
        this.createdByAppUserModel = appUserModel;
    }

    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if(null != appUserModel)
        {
            setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getCreatedBy() {
        if (createdByAppUserModel != null) {
            return createdByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
        if(appUserId == null)
        {
            createdByAppUserModel = null;
        }
        else
        {
            createdByAppUserModel = new AppUserModel();
            createdByAppUserModel.setAppUserId(appUserId);
        }
    }

    /*@javax.persistence.Transient
    public String getCreatedByUsername() {
        if (createdByAppUserModel != null) {
            return createdByAppUserModel.getUsername();
        } else {
            return null;
        }
    }


    @javax.persistence.Transient
    public void setCreatedByUsername(String appUserName) {
        if(appUserName == null)
        {
            createdByAppUserModel = null;
        }
        else
        {
            createdByAppUserModel = new AppUserModel();
            createdByAppUserModel.setUsername(appUserName);
        }
    }*/

    //rateTypeIdRateTypeModel
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "RATE_TYPE_ID")
    public RateTypeModel getRelationRateTypeIdRateTypeModel(){
        return rateTypeIdRateTypeModel ;
    }

    @javax.persistence.Transient
    public RateTypeModel getRateTypeIdRateTypeModel(){
        return getRelationRateTypeIdRateTypeModel();
    }

    @javax.persistence.Transient
    public void setRelationRateTypeIdRateTypeModel(RateTypeModel rateTypeModel) {
        this.rateTypeIdRateTypeModel = rateTypeModel;
    }

    @javax.persistence.Transient
    public void setRateTypeIdRateTypeModel(RateTypeModel rateTypeModel) {
        if(null != rateTypeModel)
        {
            setRelationRateTypeIdRateTypeModel((RateTypeModel)rateTypeModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getRateTypeId() {
        if (rateTypeIdRateTypeModel != null) {
            return rateTypeIdRateTypeModel.getRateTypeId();
        } else {
            return null;
        }
    }


    @javax.persistence.Transient
    public void setRateTypeId(Long rateTypeId) {
        if(rateTypeId == null)
        {
            rateTypeIdRateTypeModel = null;
        }
        else
        {
            rateTypeIdRateTypeModel = new RateTypeModel();
            rateTypeIdRateTypeModel.setRateTypeId(rateTypeId);
        }
    }

    /*@javax.persistence.Transient
    public String getRateName() {
        if (rateTypeIdRateTypeModel != null) {
            return rateTypeIdRateTypeModel.getName();
        } else {
            return null;
        }
    }
    @javax.persistence.Transient
    public void setRateName(String name) {
        if(name == null)
        {
            rateTypeIdRateTypeModel = null;
        }
        else
        {
            rateTypeIdRateTypeModel = new RateTypeModel();
            rateTypeIdRateTypeModel.setName(name);
        }
    }*/



    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();

        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("relationCreatedByAppUserModel");
        associationModel.setValue(getRelationCreatedByAppUserModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("RateTypeModel");
        associationModel.setPropertyName("relationRateTypeIdRateTypeModel");
        associationModel.setValue(getRelationRateTypeIdRateTypeModel());

        associationModelList.add(associationModel);

        return associationModelList;

    }



    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ConversionRateModel model = new ConversionRateModel();
        model.setRateTypeId(resultSet.getLong("RATE_TYPE_ID"));
        model.setConversionRateId(resultSet.getLong("CONVERSION_RATE_ID"));
        model.setRate(resultSet.getDouble("RATE"));
//        model.setTopUpRate(resultSet.getDouble("TOP_UP_RATE"));
        model.setStartDate(resultSet.getTimestamp("START_DATE"));
        model.setEndDate(resultSet.getTimestamp("END_DATE"));
        model.setCreatedOn(resultSet.getDate("CREATED_ON"));
        model.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        //model.setVersion(resultSet.getDouble("VERSION"));

        model.setCreatedBy(resultSet.getLong("CREATED_BY"));
//        model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        return model;
    }

}
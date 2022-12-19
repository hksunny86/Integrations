package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.integration.common.model.LimitTypeModel;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMMISSION_THRESHOLD_RATE_seq", sequenceName = "COMMISSION_THRESHOLD_RATE_seq", allocationSize = 1)
@Table(name = "COMMISSION_THRESHOLD_RATE")
public class CommissionThresholdRateModel extends BasePersistableModel implements
        Serializable, RowMapper {

    private SegmentModel segmentIdSegmentModel;
    private ProductModel productIdProductModel;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private DeviceTypeModel deviceTypeIdDeviceTypeModel;
    private DistributorModel distributorIdDistributorModel;
    private LimitTypeModel limitTypeIdLimitTypeModel;
    private Long commissionThresholdRateId;
    private Double rate;
    private Boolean active;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private Double thresholdAmount = 0D;
    private Double percentageCharges;
    private Double maxThresholdAmount;
    private Boolean isDeleted;


    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return getCommissionThresholdRateId();
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "commissionThresholdRateId";
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&commissionThresholdRateId="+getCommissionThresholdRateId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey)
    {
        setCommissionThresholdRateId(primaryKey);
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "SEGMENT_ID")
    public SegmentModel getRelationSegmentIdSegmentModel() {
        return segmentIdSegmentModel;
    }

    @javax.persistence.Transient
    public SegmentModel getSegmentIdSegmentModel() {
        return getRelationSegmentIdSegmentModel();
    }

    @javax.persistence.Transient
    public void setRelationSegmentIdSegmentModel(SegmentModel segmentModel) {
        this.segmentIdSegmentModel = segmentModel;
    }

    @javax.persistence.Transient
    public void setSegmentIdSegmentModel(SegmentModel segmentModel) {
        if (null != segmentModel) {
            setRelationSegmentIdSegmentModel((SegmentModel) segmentModel
                    .clone());
        }
    }

    @javax.persistence.Transient
    public Long getSegmentId() {
        if (segmentIdSegmentModel != null) {
            return segmentIdSegmentModel.getSegmentId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setSegmentId(Long segmentId) {
        if (segmentId == null) {
            segmentIdSegmentModel = null;
        } else {
            segmentIdSegmentModel = new SegmentModel();
            segmentIdSegmentModel.setSegmentId(segmentId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    public ProductModel getRelationProductIdProductModel() {
        return productIdProductModel;
    }

    @javax.persistence.Transient
    public ProductModel getProductIdProductModel() {
        return getRelationProductIdProductModel();
    }

    @javax.persistence.Transient
    public void setRelationProductIdProductModel(ProductModel productModel) {
        this.productIdProductModel = productModel;
    }

    @javax.persistence.Transient
    public void setProductIdProductModel(ProductModel productModel) {
        if (null != productModel) {
            setRelationProductIdProductModel((ProductModel) productModel
                    .clone());
        }
    }

    @javax.persistence.Transient
    public Long getProductId() {
        if (productIdProductModel != null) {
            return productIdProductModel.getProductId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setProductId(Long productId) {
        if (productId == null) {
            productIdProductModel = null;
        } else {
            productIdProductModel = new ProductModel();
            productIdProductModel.setProductId(productId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getRelationCreatedByAppUserModel() {
        return createdByAppUserModel;
    }

    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel() {
        return getRelationCreatedByAppUserModel();
    }

    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
        this.createdByAppUserModel = appUserModel;
    }

    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            setRelationCreatedByAppUserModel((AppUserModel) appUserModel
                    .clone());
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
        if (appUserId == null) {
            createdByAppUserModel = null;
        } else {
            createdByAppUserModel = new AppUserModel();
            createdByAppUserModel.setAppUserId(appUserId);
        }
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getRelationUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }

    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel() {
        return getRelationUpdatedByAppUserModel();
    }

    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
        this.updatedByAppUserModel = appUserModel;
    }

    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            setRelationUpdatedByAppUserModel((AppUserModel) appUserModel
                    .clone());
        }
    }

    @javax.persistence.Transient
    public Long getUpdatedBy() {
        if (updatedByAppUserModel != null) {
            return updatedByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
        if (appUserId == null) {
            updatedByAppUserModel = null;
        } else {
            updatedByAppUserModel = new AppUserModel();
            updatedByAppUserModel.setAppUserId(appUserId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_TYPE_ID")
    public DeviceTypeModel getRelationDeviceTypeIdDeviceTypeModel() {
        return deviceTypeIdDeviceTypeModel;
    }

    @javax.persistence.Transient
    public DeviceTypeModel getDeviceTypeIdDeviceTypeModel() {
        return getRelationDeviceTypeIdDeviceTypeModel();
    }

    @javax.persistence.Transient
    public void setRelationDeviceTypeIdDeviceTypeModel(
            DeviceTypeModel DeviceTypeModel) {
        this.deviceTypeIdDeviceTypeModel = DeviceTypeModel;
    }

    @javax.persistence.Transient
    public void setDeviceTypeIdDeviceTypeModel(
            DeviceTypeModel DeviceTypeModel) {
        if (null != DeviceTypeModel) {
            setRelationDeviceTypeIdDeviceTypeModel((DeviceTypeModel) DeviceTypeModel
                    .clone());
        }
    }

    @javax.persistence.Transient
    public Long getDeviceTypeId() {
        if (deviceTypeIdDeviceTypeModel != null) {
            return deviceTypeIdDeviceTypeModel.getDeviceTypeId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setDeviceTypeId(Long DeviceTypeId) {
        if (DeviceTypeId == null) {
            deviceTypeIdDeviceTypeModel = null;
        } else {
            deviceTypeIdDeviceTypeModel = new DeviceTypeModel();
            deviceTypeIdDeviceTypeModel.setDeviceTypeId(DeviceTypeId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRIBUTOR_ID")
    public DistributorModel getRelationDistributorIdDistributorModel(){
        return distributorIdDistributorModel;
    }

    @javax.persistence.Transient
    public DistributorModel getDistributorIdDistributorModel(){
        return getRelationDistributorIdDistributorModel();
    }

    @javax.persistence.Transient
    public void setRelationDistributorIdDistributorModel(DistributorModel distributorModel) {
        this.distributorIdDistributorModel = distributorModel;
    }

    @javax.persistence.Transient
    public String getAgentNetworkName()
    {
        if(distributorIdDistributorModel != null)
        {
            return distributorIdDistributorModel.getName();
        }
        else
        {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setDistributorIdDistributorModel(DistributorModel distributorModel) {
        if(null != distributorModel)
        {
            setRelationDistributorIdDistributorModel((DistributorModel)distributorModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getDistributorId() {
        if (distributorIdDistributorModel != null) {
            return distributorIdDistributorModel.getDistributorId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setDistributorId(Long distributorId) {
        if(distributorId == null)
        {
            distributorIdDistributorModel = null;
        }
        else
        {
            distributorIdDistributorModel = new DistributorModel();
            distributorIdDistributorModel.setDistributorId(distributorId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIMIT_TYPE_ID")
    @Fetch(value= FetchMode.JOIN)
    public LimitTypeModel getRelationLimitTypeIdLimitTypeModel(){
        return limitTypeIdLimitTypeModel;
    }

    @Transient
    public LimitTypeModel getLimitTypeIdLimitTypeModel(){
        return getRelationLimitTypeIdLimitTypeModel();
    }

    @Transient
    public void setRelationLimitTypeIdLimitTypeModel(LimitTypeModel limitTypeModel) {
        this.limitTypeIdLimitTypeModel = limitTypeModel;
    }

    @Transient
    public void setLimitTypeIdLimitTypeModel(LimitTypeModel limitTypeModel) {
        if(null != limitTypeModel)
        {
            setRelationLimitTypeIdLimitTypeModel((LimitTypeModel)limitTypeModel.clone());
        }
    }

    @Transient
    public Long getLimitTypeId() {
        if (limitTypeIdLimitTypeModel != null) {
            return limitTypeIdLimitTypeModel.getLimitTypeId();
        } else {
            return null;
        }
    }

    @Transient
    public void setLimitTypeId(Long limitTypeId) {
        if(limitTypeId == null)
        {
            limitTypeIdLimitTypeModel = null;
        }
        else
        {
            limitTypeIdLimitTypeModel = new LimitTypeModel();
            limitTypeIdLimitTypeModel.setLimitTypeId(limitTypeId);
        }
    }


    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMISSION_THRESHOLD_RATE_seq")
    @Column(name = "COMMISSION_THRESHOLD_RATE_ID")
    public Long getCommissionThresholdRateId() {return commissionThresholdRateId;}

    public void setCommissionThresholdRateId(Long commissionThresholdRateId) {this.commissionThresholdRateId = commissionThresholdRateId;}

    @Column(name = "RATE")
    public Double getRate() {return rate;}

    public void setRate(Double rate) {this.rate = rate;}

    @Column(name = "ACTIVE")
    public Boolean getActive() {return active;}

    public void setActive(Boolean active) {this.active = active;}

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {return createdOn;}

    public void setCreatedOn(Date createdOn) {this.createdOn = createdOn;}

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {return updatedOn;}

    public void setUpdatedOn(Date updatedOn) {this.updatedOn = updatedOn;}

    @Version
    @Column(name = "VERSION_NO")
    public Integer getVersionNo() {return versionNo;}

    public void setVersionNo(Integer versionNo) {this.versionNo = versionNo;}

    @Column(name = "THRESHOLD_AMOUNT")
    public Double getThresholdAmount() {return thresholdAmount;}

    public void setThresholdAmount(Double thresholdAmount) {this.thresholdAmount = thresholdAmount;}

    @Column(name = "PERCENTAGE_AMOUNT")
    public Double getPercentageCharges() {return percentageCharges;}

    public void setPercentageCharges(Double percentageCharges) {this.percentageCharges = percentageCharges;}

    @Column(name = "IS_DELETED")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Column(name = "MAX_THRESHOLD_AMOUNT")
    public Double getMaxThresholdAmount() {return maxThresholdAmount;}

    public void setMaxThresholdAmount(Double maxThresholdAmount) {this.maxThresholdAmount = maxThresholdAmount;}

    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();
        associationModel.setClassName("SegmentModel");
        associationModel.setPropertyName("relationSegmentIdSegmentModel");
        associationModel.setValue(getRelationSegmentIdSegmentModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("ProductModel");
        associationModel.setPropertyName("relationProductIdProductModel");
        associationModel.setValue(getRelationProductIdProductModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("DeviceTypeModel");
        associationModel.setPropertyName("relationDeviceTypeIdDeviceTypeModel");
        associationModel.setValue(getRelationDeviceTypeIdDeviceTypeModel());
        associationModelList.add(associationModel);


        associationModel = new AssociationModel();
        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("relationCreatedByAppUserModel");
        associationModel.setValue(getRelationCreatedByAppUserModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("relationUpdatedByAppUserModel");
        associationModel.setValue(getRelationUpdatedByAppUserModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("DistributorModel");
        associationModel.setPropertyName("relationDistributorIdDistributorModel");
        associationModel.setValue(getRelationDistributorIdDistributorModel());
        associationModelList.add(associationModel);
        return associationModelList;
    }
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CommissionThresholdRateModel commissionThresholdRateModel = new CommissionThresholdRateModel();
        commissionThresholdRateModel.setSegmentId(resultSet.getLong("SEGMENT_ID"));
        commissionThresholdRateModel.setProductId(resultSet.getLong("PRODUCT_ID"));
        commissionThresholdRateModel.setLimitTypeId(resultSet.getLong("LIMIT_TYPE_ID"));
        commissionThresholdRateModel.setCreatedBy(resultSet.getLong("CREATED_BY"));
        commissionThresholdRateModel.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        commissionThresholdRateModel.setDeviceTypeId(resultSet.getLong("DEVICE_TYPE_ID"));
        commissionThresholdRateModel.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
        commissionThresholdRateModel.setCommissionThresholdRateId(resultSet.getLong("COMMISSION_THRESHOLD_RATE_ID"));
        commissionThresholdRateModel.setRate(resultSet.getDouble("RATE"));
        commissionThresholdRateModel.setActive(resultSet.getBoolean("IS_ACTIVE"));
        commissionThresholdRateModel.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        commissionThresholdRateModel.setCreatedOn(resultSet.getDate("CREATED_ON"));
        commissionThresholdRateModel.setVersionNo(resultSet.getInt("VERSION_NO"));
        commissionThresholdRateModel.setThresholdAmount(resultSet.getDouble("THRESHOLD_AMOUNT"));
        commissionThresholdRateModel.setPercentageCharges(resultSet.getDouble("PERCENTAGE_CHARGES"));
        commissionThresholdRateModel.setIsDeleted(resultSet.getBoolean("IS_DELETED"));
        commissionThresholdRateModel.setMaxThresholdAmount(resultSet.getDouble("MAX_THRESHOLD_AMOUNT"));
        return commissionThresholdRateModel;
    }
}

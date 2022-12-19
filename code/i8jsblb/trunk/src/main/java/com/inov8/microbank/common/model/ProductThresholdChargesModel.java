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
@Table(name = "PRODUCT_THRESHOLD_CHARGES")
@SequenceGenerator(name = "PRODUCT_THRESHOLD_CHARGES_seq",sequenceName = "PRODUCT_THRESHOLD_CHARGES_seq", allocationSize=2)
public class ProductThresholdChargesModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private static final long serialVersionUID = -3930417941784120449L;

    private Long productThresholdChargesId;
    private ProductModel productModel;
    private DeviceTypeModel deviceTypeModel;
    private SegmentModel segmentModel;
    private DistributorModel distributorModel;
    private LimitTypeModel limitTypeIdLimitTypeModel;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Double thresholdAmount;
    private Double percentageCharges;
    private Double maxThresholdAmount;
    private Date createdOn;
    private Date updatedOn;
    private Long versionNo;
    private Boolean isDeleted;


    public ProductThresholdChargesModel()
    {
    }

    public ProductThresholdChargesModel(Long productId)
    {
        setProductId(productId);
    }

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return getProductThresholdChargesId();
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "productThresholdChargesId";
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&productThresholdChargesId="+getProductThresholdChargesId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey)
    {
        setProductThresholdChargesId(primaryKey);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    public ProductModel getProductModel()
    {
        return this.productModel;
    }

    public void setProductModel(ProductModel productModel)
    {
        this.productModel = productModel;
    }

    @Transient
    public Long getProductId()
    {
        return productModel == null ? null : productModel.getProductId();
    }

    public void setProductId(Long productId)
    {
        if(productId == null)
        {
            productModel = null;
        }
        else
        {
            productModel = new ProductModel(productId);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_TYPE_ID")
    public DeviceTypeModel getDeviceTypeModel()
    {
        return this.deviceTypeModel;
    }

    public void setDeviceTypeModel(DeviceTypeModel deviceTypeModel)
    {
        this.deviceTypeModel = deviceTypeModel;
    }

    @Transient
    public Long getDeviceTypeId()
    {
        return deviceTypeModel == null ? null : deviceTypeModel.getDeviceTypeId();
    }

    public void setDeviceTypeId(Long deviceTypeId)
    {
        if(deviceTypeId == null)
        {
            deviceTypeModel = null;
        }
        else
        {
            deviceTypeModel = new DeviceTypeModel(deviceTypeId);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEGMENT_ID")
    public SegmentModel getSegmentModel()
    {
        return this.segmentModel;
    }

    public void setSegmentModel(SegmentModel segmentModel)
    {
        this.segmentModel = segmentModel;
    }

    @Transient
    public Long getSegmentId()
    {
        return segmentModel == null ? null : segmentModel.getSegmentId();
    }

    public void setSegmentId(Long segmentId)
    {
        if(segmentId == null)
        {
            segmentModel = null;
        }
        else
        {
            segmentModel = new SegmentModel(segmentId);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRIBUTOR_ID")
    public DistributorModel getDistributorModel()
    {
        return this.distributorModel;
    }

    public void setDistributorModel(DistributorModel distributorModel)
    {
        this.distributorModel = distributorModel;
    }

    @Transient
    public Long getDistributorId()
    {
        return distributorModel == null ? null : distributorModel.getDistributorId();
    }

    public void setDistributorId(Long distributorId)
    {
        if(distributorId == null)
        {
            distributorModel = null;
        }
        else
        {
            distributorModel = new DistributorModel(distributorId);
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getCreatedByAppUserModel()
    {
        return this.createdByAppUserModel;
    }

    public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel)
    {
        this.createdByAppUserModel = createdByAppUserModel;
    }

    @Transient
    public Long getCreatedBy()
    {
        return createdByAppUserModel == null ? null : createdByAppUserModel.getAppUserId();
    }

    public void setCreatedBy(Long appUserId)
    {
        if(appUserId == null)
        {
            createdByAppUserModel = null;
        }
        else
        {
            createdByAppUserModel = new AppUserModel(appUserId);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getUpdatedByAppUserModel()
    {
        return this.updatedByAppUserModel;
    }

    public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel)
    {
        this.updatedByAppUserModel = updatedByAppUserModel;
    }

    @Transient
    public Long getUpdatedBy()
    {
        return updatedByAppUserModel == null ? null : updatedByAppUserModel.getAppUserId();
    }

    public void setUpdatedBy(Long appUserId)
    {
        if(appUserId == null)
        {
            updatedByAppUserModel = null;
        }
        else
        {
            updatedByAppUserModel = new AppUserModel(appUserId);
        }
    }

    // Property accessors
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_THRESHOLD_CHARGES_seq")
    @Column(name = "PRODUCT_THRESHOLD_CHARGES_ID")
    public Long getProductThresholdChargesId() {return productThresholdChargesId;}

    public void setProductThresholdChargesId(Long productThresholdChargesId) {this.productThresholdChargesId = productThresholdChargesId;}

    @Column(name = "THRESHOLD_AMOUNT")
    public Double getThresholdAmount() {return thresholdAmount;}

    public void setThresholdAmount(Double thresholdAmount) {this.thresholdAmount = thresholdAmount;}

    @Column(name = "PERCENTAGE_CHARGES")
    public Double getPercentageCharges() {return percentageCharges;}

    public void setPercentageCharges(Double percentageCharges) {this.percentageCharges = percentageCharges;}

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {return createdOn;}

    public void setCreatedOn(Date createdOn) {this.createdOn = createdOn;}

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {return updatedOn;}

    public void setUpdatedOn(Date updatedOn) {this.updatedOn = updatedOn;}

    @Version
    @Column(name = "VERSION_NO")
    public Long getVersionNo() {return versionNo;}

    public void setVersionNo(Long versionNo) {this.versionNo = versionNo;}

    @Column(name = "IS_DELETED")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Column(name = "MAX_THRESHOLD_AMOUNT")
    public Double getMaxThresholdAmount() {
        return maxThresholdAmount;
    }

    public void setMaxThresholdAmount(Double maxThresholdAmount) {
        this.maxThresholdAmount = maxThresholdAmount;
    }

    @Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
        List<AssociationModel> associationModelList = new ArrayList<>();

        AssociationModel associationModel = new AssociationModel();
        associationModel.setClassName("ProductModel");
        associationModel.setPropertyName("productModel");
        associationModel.setValue(getProductModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("DeviceTypeModel");
        associationModel.setPropertyName("deviceTypeModel");
        associationModel.setValue(getDeviceTypeModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("SegmentModel");
        associationModel.setPropertyName("segmentModel");
        associationModel.setValue(getSegmentModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("DistributorModel");
        associationModel.setPropertyName("distributorModel");
        associationModel.setValue(getDistributorModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("LimitTypeModel");
        associationModel.setPropertyName("relationLimitTypeIdLimitTypeModel");
        associationModel.setValue(getRelationLimitTypeIdLimitTypeModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("createdByAppUserModel");
        associationModel.setValue(getCreatedByAppUserModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("updatedByAppUserModel");
        associationModel.setValue(getUpdatedByAppUserModel());
        associationModelList.add(associationModel);

        return associationModelList;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductThresholdChargesModel vo = new ProductThresholdChargesModel();
        vo.setProductThresholdChargesId(resultSet.getLong("PRODUCT_THRESHOLD_CHARGES_ID"));
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setLimitTypeId(resultSet.getLong("LIMIT_TYPE_ID"));
        vo.setSegmentId(resultSet.getLong("SEGMENT_ID"));
        vo.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
        vo.setDeviceTypeId(resultSet.getLong("DEVICE_TYPE_ID"));
        vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCreatedBy(resultSet.getLong("CREATED_BY"));
        vo.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        vo.setVersionNo(resultSet.getLong("VERSION_NO"));
        vo.setThresholdAmount(resultSet.getDouble("THRESHOLD_AMOUNT"));
        vo.setPercentageCharges(resultSet.getDouble("PERCENTAGE_CHARGES"));
        vo.setIsDeleted(resultSet.getBoolean("IS_DELETED"));
        vo.setMaxThresholdAmount(resultSet.getDouble("MAX_THRESHOLD_AMOUNT"));
        return vo;
    }
}

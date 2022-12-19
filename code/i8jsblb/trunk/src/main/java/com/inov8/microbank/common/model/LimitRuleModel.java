package com.inov8.microbank.common.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.swing.tree.RowMapper;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;

/**
 * LimitRuleModel entity.
 *
 * @author Muhammad Omar Butt
 *         Updated by Hassan Javaid on 20-8-2014
 *         Change: Id's replaced by
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "LIMIT_RULE_SEQ", sequenceName = "LIMIT_RULE_SEQ", allocationSize = 1)
@Table(name = "LIMIT_RULE")
public class LimitRuleModel extends BasePersistableModel implements org.springframework.jdbc.core.RowMapper {

    private static final long serialVersionUID = -3304033917286303498L;

    private Long limitRuleId;
    private ProductModel productIdProductModel;
    private SegmentModel segmentIdSegmentModel;
    private OlaCustomerAccountTypeModel olaCustomerAccountTypeModel;
    private Boolean isActive;
    private Date createdOn;
    private Date updatedOn;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Integer versionNo;

    public LimitRuleModel() {
    }

    @Column(name = "LIMIT_RULE_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIMIT_RULE_SEQ")
    public Long getLimitRuleId() {
        return this.limitRuleId;
    }

    public void setLimitRuleId(Long limitRuleId) {
        this.limitRuleId = limitRuleId;
    }

    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getRelationUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }

    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel() {
        return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
        this.updatedByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            setRelationUpdatedByAppUserModel((AppUserModel) appUserModel.clone());
        }
    }


    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getRelationCreatedByAppUserModel() {
        return createdByAppUserModel;
    }

    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     */
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel() {
        return getRelationCreatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
        this.createdByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            setRelationCreatedByAppUserModel((AppUserModel) appUserModel.clone());
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


    @Column(name = "IS_ACTIVE")
    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Column(name = "CREATED_ON", length = 7)
    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return this.updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Version
    @Column(name = "VERSION_NO")
    public Integer getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getLimitRuleId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setLimitRuleId(primaryKey);
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&limitRuleId=" + getLimitRuleId();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "limitRuleId";
        return primaryKeyFieldName;
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    public ProductModel getRelationProductIdProductModel() {
        return productIdProductModel;
    }

    @javax.persistence.Transient
    public void setRelationProductIdProductModel(ProductModel productModel) {
        this.productIdProductModel = productModel;
    }

    @javax.persistence.Transient
    public ProductModel getProductIdProductModel() {
        return getRelationProductIdProductModel();
    }

    @javax.persistence.Transient
    public void setProductIdProductModel(ProductModel productModel) {
        if (null != productModel) {
            setRelationProductIdProductModel((ProductModel) productModel.clone());
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
            ;
        }
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "SEGMENT_ID")
    public SegmentModel getRelationSegmentIdSegmentModel() {
        return segmentIdSegmentModel;
    }

    @javax.persistence.Transient
    public void setRelationSegmentIdSegmentModel(SegmentModel segmentModel) {
        this.segmentIdSegmentModel = segmentModel;
    }

    @javax.persistence.Transient
    public SegmentModel getSegmentIdSegmentModel() {
        return getRelationSegmentIdSegmentModel();
    }

    @javax.persistence.Transient
    public void setSegmentIdSegmentModel(SegmentModel segmentModel) {
        if (null != segmentModel) {
            setRelationSegmentIdSegmentModel((SegmentModel) segmentModel.clone());
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
    @JoinColumn(name = "ACCOUNT_TYPE_ID")
    public OlaCustomerAccountTypeModel getRelationOlaCustomerAccountTypeIdolaCustomerAccountTypeModel() {
        return olaCustomerAccountTypeModel;
    }

    @javax.persistence.Transient
    public void setRelationOlaCustomerAccountTypeIdolaCustomerAccountTypeModel(OlaCustomerAccountTypeModel olaCustomerAccountTypeModel) {
        this.olaCustomerAccountTypeModel = olaCustomerAccountTypeModel;
    }

    @javax.persistence.Transient
    public OlaCustomerAccountTypeModel getOlaCustomerAccountTypeIdolaCustomerAccountTypeModel() {
        return getRelationOlaCustomerAccountTypeIdolaCustomerAccountTypeModel();
    }

    @javax.persistence.Transient
    public void setOlaCustomerAccountTypeIdolaCustomerAccountTypeModel(OlaCustomerAccountTypeModel olaCustomerAccountTypeModel) {
        if (null != olaCustomerAccountTypeModel) {
            setRelationOlaCustomerAccountTypeIdolaCustomerAccountTypeModel((OlaCustomerAccountTypeModel) olaCustomerAccountTypeModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getAccountTypeId() {
        if (olaCustomerAccountTypeModel != null) {
            return olaCustomerAccountTypeModel.getCustomerAccountTypeId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setAccountTypeId(Long olaCustomerAccountTypeId) {
        if (olaCustomerAccountTypeId == null) {
            olaCustomerAccountTypeModel = null;
        } else {
            olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
            olaCustomerAccountTypeModel.setCustomerAccountTypeId(olaCustomerAccountTypeId);
        }
    }

    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = null;

        associationModel = new AssociationModel();

        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("relationUpdatedByAppUserModel");
        associationModel.setValue(getRelationUpdatedByAppUserModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("relationCreatedByAppUserModel");
        associationModel.setValue(getRelationCreatedByAppUserModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("ProductModel");
        associationModel.setPropertyName("relationProductIdProductModel");
        associationModel.setValue(getRelationProductIdProductModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("SegmentModel");
        associationModel.setPropertyName("relationSegmentIdSegmentModel");
        associationModel.setValue(getRelationSegmentIdSegmentModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("OlaCustomerAccountTypeModel");
        associationModel.setPropertyName("relationOlaCustomerAccountTypeIdolaCustomerAccountTypeModel");
        associationModel.setValue(getRelationOlaCustomerAccountTypeIdolaCustomerAccountTypeModel());

        associationModelList.add(associationModel);

        return associationModelList;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        LimitRuleModel vo = new LimitRuleModel();
        vo.setLimitRuleId(resultSet.getLong("LIMIT_RULE_ID"));
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setSegmentId(resultSet.getLong("SEGMENT_ID"));
        vo.setAccountTypeId(resultSet.getLong("ACCOUNT_TYPE_ID"));
        vo.setIsActive(resultSet.getBoolean("IS_ACTIVE"));
        vo.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCreatedBy(resultSet.getLong("CREATED_BY"));
        vo.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        vo.setVersionNo(resultSet.getInt("VERSION_NO"));
        return vo;
    }
}
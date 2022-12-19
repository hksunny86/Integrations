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
@javax.persistence.SequenceGenerator(name = "OFFLINE_BILLERS_CONFIG_seq", sequenceName = "OFFLINE_BILLERS_CONFIG_seq", allocationSize = 1)
@Table(name = "OFFLINE_BILLERS_CONFIG")
public class OfflineBillersConfigModel extends BasePersistableModel implements
        Serializable, RowMapper {

    private Long offlineBillersConfigId;
    private String productId;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date updatedOn;
    private Date createdOn;
    private Integer versionNo;

    @Transient
    @Override
    public Long getPrimaryKey() {
        return getOfflineBillersConfigId();
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName() {
        return "offlineBillersConfigId";
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter() {
        return "&offlineBillersConfigId=" + getPrimaryKey();
    }

    @Override
    public void setPrimaryKey(Long arg0) {
        setOfflineBillersConfigId(arg0);
    }

    @Id
    @Column(name = "OFFLINE_BILLERS_CONFIG_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OFFLINE_BILLERS_CONFIG_seq")
    public Long getOfflineBillersConfigId() {
        return offlineBillersConfigId;
    }

    public void setOfflineBillersConfigId(Long offlineBillersConfigId) {
        this.offlineBillersConfigId = offlineBillersConfigId;
    }

    @Column(name = "PRODUCT_ID", nullable = false)
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getCreatedByAppUserModel() {
        return createdByAppUserModel;
    }

    public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
        this.createdByAppUserModel = createdByAppUserModel;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }

    public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel) {
        this.updatedByAppUserModel = updatedByAppUserModel;
    }

    @Column(name = "UPDATED_ON", nullable = false)
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "CREATED_ON", nullable = false)
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "VERSION_NO", nullable = false)
    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        OfflineBillersConfigModel model = new OfflineBillersConfigModel();
        model.setOfflineBillersConfigId(resultSet.getLong("OFFLINE_BILLERS_CONFIG_ID"));
        model.setProductId(resultSet.getString("PRODUCT_ID"));
        model.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        model.setCreatedOn(resultSet.getDate("CREATED_ON"));
        model.setVersionNo(resultSet.getInt("VERSION_NO"));

        return model;
    }
}

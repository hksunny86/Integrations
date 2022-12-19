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
@javax.persistence.SequenceGenerator(name = "FILER_RATE_CONFIG_seq", sequenceName = "FILER_RATE_CONFIG_seq", allocationSize = 1)
@Table(name = "FILER_RATE_CONFIG")
public class FilerRateConfigModel extends BasePersistableModel implements
        Serializable, RowMapper {

    private static final long serialVersionUID = 1L;

    private Long filerRateConfigId;
    private String title;
    private Boolean isFiler;
    private Double rate;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date updatedOn;
    private Date createdOn;
    private Integer versionNo;

    @Id
    @Column(name = "FILER_RATE_CONFIG_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILER_RATE_CONFIG_seq")
    public Long getFilerRateConfigId() {
        return filerRateConfigId;
    }

    public void setFilerRateConfigId(Long filerRateConfigId) {
        this.filerRateConfigId = filerRateConfigId;
    }

    @Column(name = "TITLE", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "IS_FILER", nullable = false)
    public Boolean getIsFiler() {
        return isFiler;
    }

    public void setIsFiler(Boolean isFiler) {
        isFiler = isFiler;
    }

    @Column(name = "RATE", nullable = false)
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
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

    @Transient
    @Override
    public Long getPrimaryKey() {
        return getFilerRateConfigId();
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName() {
        return "filerRateConfigId";
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter() {
        return "&filerRateConfigId=" + getPrimaryKey();
    }

    @Override
    public void setPrimaryKey(Long arg0) {
        setFilerRateConfigId(arg0);
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        FilerRateConfigModel model = new FilerRateConfigModel();
        model.setFilerRateConfigId(resultSet.getLong("FILER_RATE_CONFIG_ID"));
        model.setRate(resultSet.getDouble("RATE"));
        model.setIsFiler(resultSet.getBoolean("IS_FILER"));
        model.setTitle(resultSet.getString("TITLE"));
        model.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        model.setCreatedOn(resultSet.getDate("CREATED_ON"));
        model.setVersionNo(resultSet.getInt("VERSION_NO"));

        return model;
    }
}

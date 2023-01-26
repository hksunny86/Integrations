package com.inov8.microbank.common.model.agentsegmentrestrictionmodule;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name = "DEVICE_VERIFICATION_SEQ", strategy = "sequence", parameters = {@org.hibernate.annotations.Parameter(name = "sequence", value = "DEVICE_VERIFICATION_SEQ")})
@SequenceGenerator(name = "DEVICE_VERIFICATION_SEQ", sequenceName = "DEVICE_VERIFICATION_SEQ")
@Table(name = "DEVICE_VERIFICATION")
public class CustomerUDIDDeviceVerification extends BasePersistableModel implements Serializable, RowMapper {
    private Long deviceId;
    private String mobileNo;
    private String unquiIdentifier;
    private String deviceName;
    private String createdBy;
    private String updatedBy;
    private String remarks;
    private Date createdOn;
    private Date updatedOn;

    @Column(name = "DEVICE_VERIFICATION_ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEVICE_VERIFICATION_SEQ")
    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "MOBILE_NUMBER")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "DEVICE_UDID")
    public String getUnquiIdentifier() {
        return unquiIdentifier;
    }

    public void setUnquiIdentifier(String unquiIdentifier) {
        this.unquiIdentifier = unquiIdentifier;
    }

    @Column(name = "DEVICE_NAME")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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


    @Column(name = "COMMENTS")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED_BY")
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Transient
    public void setPrimaryKey(Long aLong) {

    }

    @Transient
    public Long getPrimaryKey() {
        return null;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&deviceId=" + getDeviceId();
        return parameters;
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "deviceId";
        return primaryKeyFieldName;
    }



    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CustomerUDIDDeviceVerification model = new CustomerUDIDDeviceVerification();
        model.setDeviceId(resultSet.getLong("DEVICE_VERIFICATION_ID"));
        model.setDeviceName(resultSet.getString("DEVICE_NAME"));
        model.setRemarks(resultSet.getString("COMMENTS"));
        model.setMobileNo(resultSet.getString("MOBILE_NUMBER"));
        model.setUnquiIdentifier(resultSet.getString("DEVICE_UDID"));
        model.setCreatedOn(resultSet.getDate("CREATED_ON"));
        model.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        model.setCreatedBy(resultSet.getString("CREATED_BY"));
        model.setUpdatedBy(resultSet.getString("UPDATED_BY"));
        return model;
    }
}

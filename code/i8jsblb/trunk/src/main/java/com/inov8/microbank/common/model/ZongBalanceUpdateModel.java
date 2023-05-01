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
@SequenceGenerator(name = "ZONG_BALANCE_SEQ", sequenceName = "ZONG_BALANCE_SEQ", allocationSize = 1)
@Table(name = "ZONG_BALANCE")
public class ZongBalanceUpdateModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {
    private Long zongBalanceId;
    private String mobileNo;
    private Double balance;
    private Date createdOn;
    private Date updatedOn;

    @Id
    @Column(name = "ZONG_BALANCE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZONG_BALANCE_SEQ")
    public Long getZongBalanceId() {
        return zongBalanceId;
    }

    public void setZongBalanceId(Long zongBalanceId) {
        this.zongBalanceId = zongBalanceId;
    }




    @Column(name = "MOBILE_NUMBER")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "BALANCE")
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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


    @Transient
    public Long getPrimaryKey() {
        return getZongBalanceId();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "zongBalanceId";
        return primaryKeyFieldName;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&zongBalanceId=" + getZongBalanceId();
        return parameters;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setZongBalanceId(primaryKey);

    }

    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + this.getZongBalanceId();
        checkBox += "\"/>";
        return checkBox;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ZongBalanceUpdateModel model = new ZongBalanceUpdateModel();
        model.setZongBalanceId(resultSet.getLong("ZONG_BALANCE_ID"));
        model.setBalance(resultSet.getDouble("BALANCE"));
        model.setMobileNo(resultSet.getString("MOBILE_NUMBER"));
        model.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        return model;
    }
}

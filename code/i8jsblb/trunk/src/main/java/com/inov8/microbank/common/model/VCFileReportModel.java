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
@SequenceGenerator(name = "VIRTUAL_CARD_SEQUENCE", sequenceName = "VIRTUAL_CARD_SEQUENCE", allocationSize = 2)
@Table(name = "VC_FILE_REPORT_VIEW")
public class VCFileReportModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {


    private String deviceNumber;
    private String walletNumber;
    private Long load;
    private Date transactionDate;
    private Double runBal;
    private String mobileNo;
    private String purchases;
    private Date createdOnEndDate;
    private Date createdOnStartDate;
    private Date start;
    private Date end;

    public VCFileReportModel() {
    }

    @Transient
    public Long getPrimaryKey() {
        return getLoad();
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setLoad(primaryKey);

    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "load";
        return primaryKeyFieldName;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&load=" + getLoad();
        return parameters;
    }

    @Transient
    public Date getCreatedOnEndDate() {
        return createdOnEndDate;
    }

    public void setCreatedOnEndDate(Date createdOnEndDate) {
        this.createdOnEndDate = createdOnEndDate;
    }

    @Transient
    public Date getCreatedOnStartDate() {
        return createdOnStartDate;
    }

    public void setCreatedOnStartDate(Date createdOnStartDate) {
        this.createdOnStartDate = createdOnStartDate;
    }

    @Transient
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Transient
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }


    @Column(name = "MOBILE_NUMBER")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "DEVICE_NUMBER")
    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @Column(name = "TDATE")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;

    }

    @Id
    @Column(name = "LOAD")
    public Long getLoad() {
        return load;
    }

    public void setLoad(Long load) {
        this.load = load;
    }


    @Column(name = "PURCHASES")
    public String getPurchases() {
        return purchases;
    }

    public void setPurchases(String purchases) {
        this.purchases = purchases;
    }

    @Column(name = "WALLET_NUMBER")
    public String getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(String walletNumber) {
        this.walletNumber = walletNumber;
    }

    @Column(name = "RBAL")
    public Double getRunBal() {
        return runBal;
    }

    public void setRunBal(Double runBal) {
        this.runBal = runBal;
    }


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        VCFileReportModel model = new VCFileReportModel();
        model.setDeviceNumber(resultSet.getString("DEVICE_NUMBER"));
        model.setTransactionDate(resultSet.getDate("TDATE"));
        model.setWalletNumber(resultSet.getString("WALLET_NUMBER"));
        model.setMobileNo(resultSet.getString("MOBILE_NUMBER"));
        model.setRunBal(resultSet.getDouble("RBAL"));
        model.setLoad(resultSet.getLong("LOAD"));
        model.setPurchases(resultSet.getString("PURCHASES"));
        return model;
    }
}

package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@XmlRootElement(name="actionAuthorizationModel")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "TASDEEQ_DATA_seq",sequenceName = "TASDEEQ_DATA_seq", allocationSize=1)
@Table(name = "TASDEEQ_DATA")
public class TasdeeqDataModel extends BasePersistableModel implements
        Serializable {

    private Long tasdeeqDataId;
    private String reportDate;
    private String reportTime;
    private String name;
    private String mobileNo;
    private String cnic;
    private String city;
    private String noOfActiveAccounts;
    private Double totalOutstandingBalance;
    private String dob;
    private String plus3024M;
    private String plus6024M;
    private String plus9024M;
    private String plus12024M;
    private String plus1504M;
    private String plus18024M;
    private String writeOff;
    private String validStatus;
    private Date createdOn;
    private Date updatedOn;
    private String createdBy;
    private String updatedBy;

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getTasdeeqDataId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setTasdeeqDataId(primaryKey);
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&tasdeeqDataId=" + getTasdeeqDataId();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "getTasdeeqDataId";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "TASDEEQ_DATA_ID"  )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASDEEQ_DATA_seq")
    public Long getTasdeeqDataId() {
        return tasdeeqDataId;
    }

    public void setTasdeeqDataId(Long tasdeeqDataId) {
        this.tasdeeqDataId = tasdeeqDataId;
    }

    @Column(name = "REPORT_DATE"  )
    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    @Column(name = "REPORT_TIME"  )
    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    @Column(name = "NAME"  )
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CNIC"  )
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name = "CITY"  )
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "NO_OF_ACTIVE_ACCOUNTS"  )
    public String getNoOfActiveAccounts() {
        return noOfActiveAccounts;
    }

    public void setNoOfActiveAccounts(String noOfActiveAccounts) {
        this.noOfActiveAccounts = noOfActiveAccounts;
    }

    @Column(name = "TOTAL_OUTSTANDING_BALANCE"  )
    public Double getTotalOutstandingBalance() {
        return totalOutstandingBalance;
    }

    public void setTotalOutstandingBalance(Double totalOutstandingBalance) {
        this.totalOutstandingBalance = totalOutstandingBalance;
    }

    @Column(name = "DOB"  )
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Column(name = "PLUS_30_24M"  )
    public String getPlus3024M() {
        return plus3024M;
    }

    public void setPlus3024M(String plus3024M) {
        this.plus3024M = plus3024M;
    }

    @Column(name = "PLUS_60_24M"  )
    public String getPlus6024M() {
        return plus6024M;
    }

    public void setPlus6024M(String plus6024M) {
        this.plus6024M = plus6024M;
    }

    @Column(name = "PLUS_90_24M"  )
    public String getPlus9024M() {
        return plus9024M;
    }

    public void setPlus9024M(String plus9024M) {
        this.plus9024M = plus9024M;
    }

    @Column(name = "PLUS_120_24M"  )
    public String getPlus12024M() {
        return plus12024M;
    }

    public void setPlus12024M(String plus12024M) {
        this.plus12024M = plus12024M;
    }

    @Column(name = "PLUS_150_24M"  )
    public String getPlus1504M() {
        return plus1504M;
    }

    public void setPlus1504M(String plus1504M) {
        this.plus1504M = plus1504M;
    }

    @Column(name = "PLUS_180_24M"  )
    public String getPlus18024M() {
        return plus18024M;
    }

    public void setPlus18024M(String plus18024M) {
        this.plus18024M = plus18024M;
    }

    @Column(name = "WRITE_OFF"  )
    public String getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(String wrtieOff) {
        this.writeOff = writeOff;
    }

    @Column(name = "MOBILE_NO"  )
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "VALID_STATUS"  )
    public String getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(String validStatus) {
        this.validStatus = validStatus;
    }

    @Column(name = "CREATED_ON"  )
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON"  )
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "CREATED_BY"  )
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED_BY"  )
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}

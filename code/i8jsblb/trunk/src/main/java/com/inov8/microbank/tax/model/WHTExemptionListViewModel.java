package com.inov8.microbank.tax.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Zeeshan on 7/1/2016.
 */

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "WHT_EXEMPTION_VIEW")
public class WHTExemptionListViewModel extends BasePersistableModel implements java.io.Serializable{

    private static final long serialVersionUID = -3733408495452365387L;
    private Long whtExemptionId;
    private String userId;
    private String mobile;
    private String cnic;
    private String name;
    private Date exemptionStartDate;
    private Date exemptionEndDate;

    private Date exemptionFromStartDate;
    private Date exemptionToStartDate;
    private Date exemptionFromEndDate;
    private Date exemptionToEndDate;


    @Column(name="EXEMPTION_END_DATE")
    public Date getExemptionEndDate() {return exemptionEndDate;}

    public void setExemptionEndDate(Date exemptionEndDate) {this.exemptionEndDate = exemptionEndDate;}

    @Column(name="EXEMPTION_START_DATE")
    public Date getExemptionStartDate() {return exemptionStartDate;}

    public void setExemptionStartDate(Date exemptionStartDate) {this.exemptionStartDate = exemptionStartDate;}

    @Column(name="AGENT_NAME")
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Column(name = "AGENT_CNIC")
    public String getCnic() {return cnic;}

    public void setCnic(String cnic) {this.cnic = cnic;}

    @Column(name="AGENT_MOBILE_NO")
    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}

    @Column(name="AGENT_ID")
    public String getUserId() {return userId;}

    public void setUserId(String userId) {this.userId = userId;}

    @Column(name ="WHT_EXEMPTION_ID")
    @Id
    public Long getWhtExemptionId() {return whtExemptionId;}

    public void setWhtExemptionId(Long whtExemptionId) {this.whtExemptionId = whtExemptionId;}

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {setWhtExemptionId(primaryKey);}

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getWhtExemptionId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {

        String parameters = "";
        parameters += "&whtExemptionId=" + getWhtExemptionId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {

        String primaryKeyFieldName = "whtExemptionId";
        return primaryKeyFieldName;
    }

    @javax.persistence.Transient
    public Date getExemptionToEndDate() {return exemptionToEndDate;}

    public void setExemptionToEndDate(Date exemptionToEndDate) {this.exemptionToEndDate = exemptionToEndDate;}

    @javax.persistence.Transient
    public Date getExemptionFromEndDate() {return exemptionFromEndDate;}

    public void setExemptionFromEndDate(Date exemptionFromEndDate) {this.exemptionFromEndDate = exemptionFromEndDate;}

    @javax.persistence.Transient
    public Date getExemptionToStartDate() {return exemptionToStartDate;}

    public void setExemptionToStartDate(Date exemptionToStartDate) {this.exemptionToStartDate = exemptionToStartDate;}

    @javax.persistence.Transient
    public Date getExemptionFromStartDate() {return exemptionFromStartDate;}

    public void setExemptionFromStartDate(Date exemptionFromStartDate) {this.exemptionFromStartDate = exemptionFromStartDate;}
}

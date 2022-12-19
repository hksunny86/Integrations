package com.inov8.microbank.account.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "WALKIN_BLACKLISTED_NIC_VIEW")
public class WalkInBlackListedNicViewModel extends BasePersistableModel implements Serializable{

    private Long pk;
    private String cNic;
    private String action;
    private Date actionDate;
    private Date actionEndDate;
    private String actionPerformedBy;
    private String comments;

    @Id
    @Column(name = "PK")
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        this.setPk(primaryKey);
    }

    @Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Column(name = "CNIC_NO")
    public String getcNic() {
        return cNic;
    }

    public void setcNic(String cNic) {
        this.cNic = cNic;
    }

    @Column(name = "ACTION")
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column(name = "ACTION_DATE")
    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    @Column(name = "ACTION_PERFORMED_BY")
    public String getActionPerformedBy() {
        return actionPerformedBy;
    }

    public void setActionPerformedBy(String actionPerformedBy) {
        this.actionPerformedBy = actionPerformedBy;
    }

    @Column(name = "COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Transient
    public Date getActionEndDate() {
        return actionEndDate;
    }

    public void setActionEndDate(Date actionEndDate) {
        this.actionEndDate = actionEndDate;
    }
}

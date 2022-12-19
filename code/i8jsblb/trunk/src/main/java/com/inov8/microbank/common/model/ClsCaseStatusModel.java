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
//@javax.persistence.SequenceGenerator(name = "REGISTRATION_STATE_seq", sequenceName = "REGISTRATION_STATE_seq", allocationSize = 2)
@Table(name = "CLS_CASE_STATUS")
public class ClsCaseStatusModel extends BasePersistableModel implements Serializable {

    private static final long serialVersionUID = -2694580309541453092L;

    private Long clsCaseStatusId;
    private String name;
    private String description;
    private String comments;
    private Long createdBy;
    private Long updatedBy;
    private Date createdOn;
    private Date updatedOn;
    private Long versionNo;

    @Override
    @Transient
    public Long getPrimaryKey() {
        return getClsCaseStatusId();
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName() {
        return "clsCaseStatusId";
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter() {
        return "&clsCaseStatusId="+getClsCaseStatusId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setClsCaseStatusId(primaryKey);
    }

    // Property accessors
    @Id
//    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REGISTRATION_STATE_seq")
    @Column(name = "CLS_CASE_STATUS_ID")
    public Long getClsCaseStatusId() {return clsCaseStatusId;}

    public void setClsCaseStatusId(Long clsCaseStatusId) {this.clsCaseStatusId = clsCaseStatusId;}

    @Column(name = "NAME")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "COMMENTS")
    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name = "CREATED_BY")
    public Long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED_BY")
    public Long getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "CREATED_ON")
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
    public Long getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }
}

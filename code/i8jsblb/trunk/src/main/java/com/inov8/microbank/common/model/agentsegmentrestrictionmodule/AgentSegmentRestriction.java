package com.inov8.microbank.common.model.agentsegmentrestrictionmodule;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.portal.blbnewreports.DigiDebitCardAnnualReportViewModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import javax.swing.tree.TreePath;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name = "AGENT_SEGMENT_EXCEPTIONS_SEQ", strategy = "sequence", parameters = {@org.hibernate.annotations.Parameter(name = "sequence", value = "AGENT_SEGMENT_EXCEPTIONS_SEQ")})
@javax.persistence.SequenceGenerator(name = "AGENT_SEGMENT_EXCEPTIONS_SEQ", sequenceName = "AGENT_SEGMENT_EXCEPTIONS_SEQ")
@Table(name = "AGENT_SEGMENT_EXCEPTIONS")
public class AgentSegmentRestriction extends BasePersistableModel implements Serializable, RowMapper {
    private Long agentSegmentExceptionId;
    private Long segmentId;
    private Long productId;
    private Long retailerId;
    private Long retailerContactId;
    private Boolean IsActive;
    private Date createdOn;
    private Date updatedOn;
    private String agentID;
    private String name;
    private String productName;
    private String createdBy;
    private String updatedBy;

    @Column(name = "SEGMENT_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "MFS_ID")
    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    @Column(name = "AGENT_SEGMENT_EXCEPTIONS_ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENT_SEGMENT_EXCEPTIONS_SEQ")
    public Long getAgentSegmentExceptionId() {
        return agentSegmentExceptionId;
    }

    public void setAgentSegmentExceptionId(Long agentSegmentExceptionId) {
        this.agentSegmentExceptionId = agentSegmentExceptionId;
    }

    @Column(name = "SEGMENT_ID")

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    @Column(name = "PRODUCT_ID")

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "RETAILER_ID")

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    @Column(name = "RETAILER_CONTACT_ID")

    public Long getRetailerContactId() {
        return retailerContactId;
    }

    public void setRetailerContactId(Long retailerContactId) {
        this.retailerContactId = retailerContactId;
    }

    @Column(name = "IS_ACTIVE")

    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean isActive) {
        IsActive = isActive;
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

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {

    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return null;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&agentSegmentExceptionId=" + getAgentSegmentExceptionId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "agentSegmentExceptionId";
        return primaryKeyFieldName;
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

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AgentSegmentRestriction model = new AgentSegmentRestriction();
        model.setAgentID(resultSet.getString("MFS_ID"));
        model.setAgentSegmentExceptionId(resultSet.getLong("AGENT_SEGMENT_EXCEPTIONS_ID"));
        model.setProductId(resultSet.getLong("PRODUCT_ID"));
        model.setSegmentId(resultSet.getLong("SEGMENT_ID"));
        model.setProductName(resultSet.getString("PRODUCT_NAME"));
        model.setName(resultSet.getString("SEGMENT_NAME"));
        model.setRetailerId(resultSet.getLong("RETAILER_ID"));
        model.setRetailerContactId(resultSet.getLong("RETAILER_CONTACT_ID"));
        model.setCreatedOn(resultSet.getDate("CREATED_ON"));
        model.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        model.setIsActive(resultSet.getBoolean("IS_ACTIVE"));
        model.setCreatedBy(resultSet.getString("CREATED_BY"));
        model.setUpdatedBy(resultSet.getString("UPDATED_BY"));
        return model;
    }
}

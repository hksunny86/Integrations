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
@SequenceGenerator(name = "AGENT_BVS_STAT_SEQ", sequenceName = "AGENT_BVS_STAT_SEQ", allocationSize = 1)
@Table(name = "AGENT_BVS_STAT")
public class AgentBvsStatModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {
    private Long statId;
    private Long agentId;
    private Long bvsFail;
    private Long alertRequired;
    private Date lastUpdate;


    @Id
    @Column(name = "STAT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENT_BVS_STAT_SEQ")
    public Long getStatId() {
        return statId;
    }

    public void setStatId(Long statId) {
        this.statId = statId;
    }

    @Column(name = "AGENT_ID")
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    @Column(name = "BVS_FAILURE_COUNT")
    public Long getBvsFail() {
        return bvsFail;
    }

    public void setBvsFail(Long bvsFail) {
        this.bvsFail = bvsFail;
    }

    @Column(name = "ALERT_REQUIRED")
    public Long getAlertRequired() {
        return alertRequired;
    }

    public void setAlertRequired(Long alertRequired) {
        this.alertRequired = alertRequired;
    }

    @Column(name = "LAST_UPDATED_ON")
    public Date getLastUpdate() { return lastUpdate; }

    public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }


    @Transient
    public Long getPrimaryKey() {
        return getStatId();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "statId";
        return primaryKeyFieldName;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&statId=" + getStatId();
        return parameters;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setStatId(primaryKey);

    }

    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + this.getStatId();
        checkBox += "\"/>";
        return checkBox;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AgentBvsStatModel model = new AgentBvsStatModel();
        model.setStatId(resultSet.getLong("STAT_ID"));
        model.setAgentId(resultSet.getLong("AGENT_ID"));
        model.setBvsFail(resultSet.getLong("BVS_FAILURE_COUNT"));
        model.setAlertRequired(resultSet.getLong("ALERT_REQUIRED"));
        model.setLastUpdate(resultSet.getTimestamp("LAST_UPDATED_ON"));
        return model;
    }
}

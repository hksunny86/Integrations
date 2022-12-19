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
@SequenceGenerator(name = "AGENT_LOCATION_STAT_SEQ", sequenceName = "AGENT_LOCATION_STAT_SEQ", allocationSize = 1)
@Table(name = "AGENT_LOCATION_STAT")
public class AgentLocationStatModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {
    private Long statId;
    private Long agentId;
    private Long isValidLocation;
    private Date lastUpdate;

    @Transient
    public void setPrimaryKey(Long aLong) {
        setStatId(aLong);
    }

    @Transient
    public Long getPrimaryKey() {
        return getStatId();
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&statId=" + getStatId();
        return parameters;
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "statId";
        return primaryKeyFieldName;
    }
    @Id
    @Column(name = "STAT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENT_LOCATION_STAT_SEQ")
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

    @Column(name = "IS_VALID_LOCATION")
    public Long getIsValidLocation() {
        return isValidLocation;
    }

    public void setIsValidLocation(Long isValidLocation) {
        this.isValidLocation = isValidLocation;
    }

    @Column(name = "LAST_UPDATED_ON")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AgentLocationStatModel model = new AgentLocationStatModel();
        model.setStatId(resultSet.getLong("STAT_ID"));
        model.setAgentId(resultSet.getLong("AGENT_ID"));
        model.setLastUpdate(resultSet.getTimestamp("LAST_UPDATED_ON"));
        model.setIsValidLocation(resultSet.getLong("IS_VALID_LOCATION"));
        return model;
    }
}

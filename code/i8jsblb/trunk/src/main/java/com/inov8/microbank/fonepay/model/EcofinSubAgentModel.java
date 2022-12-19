package com.inov8.microbank.fonepay.model;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


/**
 * The persistent class for the ECOFIN_SUB_AGENT database table.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@SequenceGenerator(name = "ECOFIN_SUB_AGENT_SEQ", sequenceName = "ECOFIN_SUB_AGENT_SEQ", allocationSize = 1)
@Table(name = "ECOFIN_SUB_AGENT")
public class EcofinSubAgentModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private static final long serialVersionUID = 1L;


    private long ecofinSubAgentId;
    private String agentCnic;
    private String agentName;
    private Date birthDate;
    private String city;
    private Date cnicIssDate;
    private BigDecimal isActive;
    private String pos;

    public EcofinSubAgentModel() {
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setEcofinSubAgentId(primaryKey);
    }

    @Transient
    public Long getPrimaryKey() {
        return getEcofinSubAgentId();
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&ecofinSubAgentId=" + getEcofinSubAgentId();
        return parameters;
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "ecofinSubAgentId";
        return primaryKeyFieldName;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ECOFIN_SUB_AGENT_SEQ")
    @Column(name = "ECOFIN_SUB_AGENT_ID")
    public long getEcofinSubAgentId() {
        return this.ecofinSubAgentId;
    }

    public void setEcofinSubAgentId(long ecofinSubAgentId) {
        this.ecofinSubAgentId = ecofinSubAgentId;
    }

    @Column(name = "AGENT_CNIC")
    public String getAgentCnic() {
        return this.agentCnic;
    }

    public void setAgentCnic(String agentCnic) {
        this.agentCnic = agentCnic;
    }

    @Column(name = "AGENT_NAME")
    public String getAgentName() {
        return this.agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH_DATE")
    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Column(name = "CITY")
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CNIC_ISS_DATE")
    public Date getCnicIssDate() {
        return this.cnicIssDate;
    }

    public void setCnicIssDate(Date cnicIssDate) {
        this.cnicIssDate = cnicIssDate;
    }

    @Column(name = "IS_ACTIVE")
    public BigDecimal getIsActive() {
        return this.isActive;
    }

    public void setIsActive(BigDecimal isActive) {
        this.isActive = isActive;
    }

    @Column(name = "POS")
    public String getPos() {
        return this.pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        EcofinSubAgentModel model = new EcofinSubAgentModel();
        model.setEcofinSubAgentId(resultSet.getLong("ECOFIN_SUB_AGENT_ID"));
        model.setAgentCnic(resultSet.getString("AGENT_CNIC"));
        model.setAgentName(resultSet.getString("AGENT_NAME"));
        model.setBirthDate(resultSet.getDate("BIRTH_DATE"));
        model.setCity(resultSet.getString("CITY"));
        model.setCnicIssDate(resultSet.getDate("CNIC_ISS_DATE"));
        model.setIsActive(resultSet.getBigDecimal("IS_ACTIVE"));
        model.setPos(resultSet.getString("POS"));
        return model;
    }
}
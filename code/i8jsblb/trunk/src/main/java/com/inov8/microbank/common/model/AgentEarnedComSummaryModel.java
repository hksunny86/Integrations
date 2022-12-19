package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * AgentEarnedComSummary entity. @author MyEclipse Persistence Tools
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "AGENT_EARNED_COM_SEQ",sequenceName = "AGENT_EARNED_COM_SEQ", allocationSize=2)
@Table(name = "AGENT_EARNED_COM_SUMMARY")
public class AgentEarnedComSummaryModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = 8090317795855891860L;

	// Fields
    private Long agentEarnedComId;
	private String agentId;
	private Long appUserId;
	private Double comToday;
	private Date dateToday;
	private Double comYesterday;
	private Date dateYesterday;
	private Double comThisWeek;
	private Date dateStartOfWeek;
	private Date dateEndOfWeek;
	private Double comLastWeek;
	private Date dateStartOfLastweek;
	private Date dateEndOfLastweek;
	private Double comThisMonth;
	private Double comLastMonth;
	private Date lastUpdatedOn;
	private String lastUpdatedBy;
	private Date dateStartOfMonth;
	private Date dateEndOfMonth;
	private Date dateStartOfLastmonth;
	private Date dateEndOfLastmonth;

	// Constructors

	/** default constructor */
	public AgentEarnedComSummaryModel() {
	}

	@Transient
    @Override
    public String getPrimaryKeyFieldName() {
        return "agentEarnedComId";
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter() {
        return "&agentEarnedComId="+agentEarnedComId;
    }

    @Transient
    @Override
    public Long getPrimaryKey() {       
        return getAgentEarnedComId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setAgentEarnedComId( primaryKey );
    }

	// Property accessors
	@Id
	@Column(name = "AGENT_EARNED_COM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAgentEarnedComId() {
		return this.agentEarnedComId;
	}

	public void setAgentEarnedComId(Long agentEarnedComId) {
		this.agentEarnedComId = agentEarnedComId;
	}

	@Column(name = "AGENT_ID", length = 20)
	public String getAgentId() {
		return this.agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	@Column(name = "APP_USER_ID", precision = 10, scale = 0)
	public Long getAppUserId() {
		return this.appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	@Column(name = "COM_TODAY", precision = 10)
	public Double getComToday() {
		return this.comToday;
	}

	public void setComToday(Double comToday) {
		this.comToday = comToday;
	}

	
	@Column(name = "DATE_TODAY", length = 7)
	public Date getDateToday() {
		return this.dateToday;
	}

	public void setDateToday(Date dateToday) {
		this.dateToday = dateToday;
	}

	@Column(name = "COM_YESTERDAY", precision = 10)
	public Double getComYesterday() {
		return this.comYesterday;
	}

	public void setComYesterday(Double comYesterday) {
		this.comYesterday = comYesterday;
	}

	
	@Column(name = "DATE_YESTERDAY", length = 7)
	public Date getDateYesterday() {
		return this.dateYesterday;
	}

	public void setDateYesterday(Date dateYesterday) {
		this.dateYesterday = dateYesterday;
	}

	@Column(name = "COM_THIS_WEEK", precision = 10)
	public Double getComThisWeek() {
		return this.comThisWeek;
	}

	public void setComThisWeek(Double comThisWeek) {
		this.comThisWeek = comThisWeek;
	}

	
	@Column(name = "DATE_START_OF_WEEK", length = 7)
	public Date getDateStartOfWeek() {
		return this.dateStartOfWeek;
	}

	public void setDateStartOfWeek(Date dateStartOfWeek) {
		this.dateStartOfWeek = dateStartOfWeek;
	}

	
	@Column(name = "DATE_END_OF_WEEK", length = 7)
	public Date getDateEndOfWeek() {
		return this.dateEndOfWeek;
	}

	public void setDateEndOfWeek(Date dateEndOfWeek) {
		this.dateEndOfWeek = dateEndOfWeek;
	}

	@Column(name = "COM_LAST_WEEK", precision = 10)
	public Double getComLastWeek() {
		return this.comLastWeek;
	}

	public void setComLastWeek(Double comLastWeek) {
		this.comLastWeek = comLastWeek;
	}

	
	@Column(name = "DATE_START_OF_LASTWEEK", length = 7)
	public Date getDateStartOfLastweek() {
		return this.dateStartOfLastweek;
	}

	public void setDateStartOfLastweek(Date dateStartOfLastweek) {
		this.dateStartOfLastweek = dateStartOfLastweek;
	}

	
	@Column(name = "DATE_END_OF_LASTWEEK", length = 7)
	public Date getDateEndOfLastweek() {
		return this.dateEndOfLastweek;
	}

	public void setDateEndOfLastweek(Date dateEndOfLastweek) {
		this.dateEndOfLastweek = dateEndOfLastweek;
	}

	@Column(name = "COM_THIS_MONTH", precision = 10)
	public Double getComThisMonth() {
		return this.comThisMonth;
	}

	public void setComThisMonth(Double comThisMonth) {
		this.comThisMonth = comThisMonth;
	}

	@Column(name = "COM_LAST_MONTH", precision = 10)
	public Double getComLastMonth() {
		return this.comLastMonth;
	}

	public void setComLastMonth(Double comLastMonth) {
		this.comLastMonth = comLastMonth;
	}

	
	@Column(name = "LAST_UPDATED_ON", length = 7)
	public Date getLastUpdatedOn() {
		return this.lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	@Column(name = "LAST_UPDATED_BY", length = 20)
	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	
	@Column(name = "DATE_START_OF_MONTH", length = 7)
	public Date getDateStartOfMonth() {
		return this.dateStartOfMonth;
	}

	public void setDateStartOfMonth(Date dateStartOfMonth) {
		this.dateStartOfMonth = dateStartOfMonth;
	}

	
	@Column(name = "DATE_END_OF_MONTH", length = 7)
	public Date getDateEndOfMonth() {
		return this.dateEndOfMonth;
	}

	public void setDateEndOfMonth(Date dateEndOfMonth) {
		this.dateEndOfMonth = dateEndOfMonth;
	}

	
	@Column(name = "DATE_START_OF_LASTMONTH", length = 7)
	public Date getDateStartOfLastmonth() {
		return this.dateStartOfLastmonth;
	}

	public void setDateStartOfLastmonth(Date dateStartOfLastmonth) {
		this.dateStartOfLastmonth = dateStartOfLastmonth;
	}

	
	@Column(name = "DATE_END_OF_LASTMONTH", length = 7)
	public Date getDateEndOfLastmonth() {
		return this.dateEndOfLastmonth;
	}

	public void setDateEndOfLastmonth(Date dateEndOfLastmonth) {
		this.dateEndOfLastmonth = dateEndOfLastmonth;
	}

}
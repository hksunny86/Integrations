package com.inov8.microbank.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author NaseerUl
 */
@Entity
@org.hibernate.annotations.Entity(mutable=false)
@Table(name = "APP_USER_HISTORY_VIEW")
public class AppUserHistoryViewModel extends BasePersistableModel
{
	private static final long serialVersionUID = 8287941443123534187L;

	private Long appUserHistoryId;
	private Long appUserId;
	private String nic;
	private String closedByUsername;
	private Date closedOn;
	private String closingComments;
	private String settledByUsername;
	private Date settledOn;
	private String settlementComments;
	private Date createdOn;

	public AppUserHistoryViewModel()
	{
	}

	@Transient
	@Override
	public Long getPrimaryKey()
	{
		return getAppUserHistoryId();
	}

	@Override
	public void setPrimaryKey(Long primaryKey)
	{
		setAppUserHistoryId(primaryKey);
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter()
	{
		return "&appUserHistoryId="+getAppUserHistoryId();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName()
	{
		return "appUserHistoryId";
	}

	@Id
	@Column(name="APP_USER_HISTORY_ID")
	public Long getAppUserHistoryId()
	{
		return appUserHistoryId;
	}

	public void setAppUserHistoryId(Long appUserHistoryId)
	{
		this.appUserHistoryId = appUserHistoryId;
	}

	@Column(name="NIC")
	public String getNic()
	{
		return nic;
	}

	public void setNic(String nic)
	{
		this.nic = nic;
	}

	@Column(name="ACCOUNT_CLOSED_BY_USERNAME")
	public String getClosedByUsername()
	{
		return closedByUsername;
	}

	public void setClosedByUsername(String closedByUsername)
	{
		this.closedByUsername = closedByUsername;
	}

	@Column(name = "ACCOUNT_CLOSED_ON" )
	public Date getClosedOn()
	{
		return closedOn;
	}

	public void setClosedOn(Date closedOn)
	{
		this.closedOn = closedOn;
	}

	@Column(name = "CLOSING_COMMENTS" )
	public String getClosingComments()
	{
		return closingComments;
	}

	public void setClosingComments(String closingComments)
	{
		this.closingComments = closingComments;
	}

	@Column(name="ACCOUNT_SETTLED_BY_USERNAME")
	public String getSettledByUsername()
	{
		return settledByUsername;
	}

	public void setSettledByUsername(String settledByUsername)
	{
		this.settledByUsername = settledByUsername;
	}

	@Column(name = "ACCOUNT_SETTLED_ON")
	public Date getSettledOn()
	{
		return settledOn;
	}

	public void setSettledOn(Date settledOn)
	{
		this.settledOn = settledOn;
	}

	@Column(name = "SETTLEMENT_COMMENTS")
	public String getSettlementComments()
	{
		return settlementComments;
	}

	public void setSettlementComments(String settlementComments)
	{
		this.settlementComments = settlementComments;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn()
	{
		return createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	@Column(name = "APP_USER_ID")
	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}
}

package com.inov8.microbank.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author NaseerUl
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APP_USER_HISTORY_seq",sequenceName = "APP_USER_HISTORY_seq", allocationSize=2)
@Table(name = "APP_USER_HISTORY")
public class AppUserHistoryModel extends BasePersistableModel
{
	private static final long serialVersionUID = 8287941443123534187L;

	private Long appUserHistoryId;
	private AppUserModel appUserModel;
	private String nic;
	private AppUserModel closedByAppUserModel;
	private Date closedOn;
	private String closingComments;
	private AppUserModel settledByAppUserModel;
	private Date settledOn;
	private String settlementComments;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;

	public AppUserHistoryModel()
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

	@Transient
	@Override
	public List<AssociationModel> getAssociationModelList()
	{
		List<AssociationModel> associationModelList = new ArrayList<>();

		AssociationModel associationModel = new AssociationModel();
		associationModel.setPropertyName("appUserModel");
		associationModel.setClassName("AppUserModel");
		associationModel.setValue(getAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setPropertyName("closedByAppUserModel");
		associationModel.setClassName("AppUserModel");
		associationModel.setValue(getClosedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setPropertyName("settledByAppUserModel");
		associationModel.setClassName("AppUserModel");
		associationModel.setValue(getSettledByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setPropertyName("createdByAppUserModel");
		associationModel.setClassName("AppUserModel");
		associationModel.setValue(getCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setPropertyName("updatedByAppUserModel");
		associationModel.setClassName("AppUserModel");
		associationModel.setValue(getUpdatedByAppUserModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_USER_HISTORY_seq")
	@Column(name="APP_USER_HISTORY_ID")
	public Long getAppUserHistoryId()
	{
		return appUserHistoryId;
	}

	public void setAppUserHistoryId(Long appUserHistoryId)
	{
		this.appUserHistoryId = appUserHistoryId;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "APP_USER_ID")
	public AppUserModel getAppUserModel()
	{
		return appUserModel;
	}

	public void setAppUserModel(AppUserModel appUserModel)
	{
		this.appUserModel = appUserModel;
	}

	@Transient
	public Long getAppUserId()
	{
		return appUserModel == null? null: appUserModel.getAppUserId();
	}

	public void setAppUserId(Long appUserId)
	{
		if(appUserId == null)
		{
			appUserModel = null;
		}
		else
		{
			appUserModel = new AppUserModel(appUserId);
		}
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

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_CLOSED_BY")
	public AppUserModel getClosedByAppUserModel()
	{
		return closedByAppUserModel;
	}

	public void setClosedByAppUserModel(AppUserModel closedByAppUserModel)
	{
		this.closedByAppUserModel = closedByAppUserModel;
	}

	@Transient
	public Long getClosedByAppUserId()
	{
		return closedByAppUserModel == null? null: closedByAppUserModel.getAppUserId();
	}

	public void setClosedByAppUserId(Long appUserId)
	{
		if(appUserId == null)
		{
			closedByAppUserModel = null;
		}
		else
		{
			closedByAppUserModel = new AppUserModel(appUserId);
		}
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

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_SETTLED_BY")
	public AppUserModel getSettledByAppUserModel()
	{
		return settledByAppUserModel;
	}

	public void setSettledByAppUserModel(AppUserModel settledByAppUserModel)
	{
		this.settledByAppUserModel = settledByAppUserModel;
	}

	@Transient
	public Long getSettledByAppUserId()
	{
		return settledByAppUserModel == null? null: settledByAppUserModel.getAppUserId();
	}

	public void setSettledByAppUserId(Long appUserId)
	{
		if(appUserId == null)
		{
			settledByAppUserModel = null;
		}
		else
		{
			settledByAppUserModel = new AppUserModel(appUserId);
		}
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

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getCreatedByAppUserModel()
	{
		return createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel)
	{
		this.createdByAppUserModel = createdByAppUserModel;
	}

	@Transient
	public Long getCreatedByAppUserId()
	{
		return createdByAppUserModel == null? null: createdByAppUserModel.getAppUserId();
	}

	public void setCreatedByAppUserId(Long appUserId)
	{
		if(appUserId == null)
		{
			createdByAppUserModel = null;
		}
		else
		{
			createdByAppUserModel = new AppUserModel(appUserId);
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getUpdatedByAppUserModel()
	{
		return updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel)
	{
		this.updatedByAppUserModel = updatedByAppUserModel;
	}

	@Transient
	public Long getUpdatedByAppUserId()
	{
		return updatedByAppUserModel == null? null: updatedByAppUserModel.getAppUserId();
	}

	public void setUpdatedByAppUserId(Long appUserId)
	{
		if(appUserId == null)
		{
			updatedByAppUserModel = null;
		}
		else
		{
			updatedByAppUserModel = new AppUserModel(appUserId);
		}
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

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn()
	{
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn)
	{
		this.updatedOn = updatedOn;
	}

	@Version
    @Column(name = "VERSION_NO")
	public Integer getVersionNo()
	{
		return versionNo;
	}

	public void setVersionNo(Integer versionNo)
	{
		this.versionNo = versionNo;
	}

}

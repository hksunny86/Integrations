package com.inov8.microbank.account.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Malik on 10/9/2016.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BLACKLISTED_CNICS_seq",sequenceName = "BLACKLISTED_CNICS_seq")
@Table(name = "BLACKLISTED_CNICS")
public class BlacklistedCnicsModel extends BasePersistableModel
{

    private AppUserModel updatedByAppUserModel;
    private AppUserModel createdByAppUserModel;

    private Long blacklistedCnicsId;
    private String cnicNo;
    private Boolean isBlacklisted;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private String comments;


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

    @Column(name = "BLACKLISTED_CNICS_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="BLACKLISTED_CNICS_seq")
    public Long getBlacklistedCnicsId()
    {
        return blacklistedCnicsId;
    }

    public void setBlacklistedCnicsId(Long blacklistedCnicsId)
    {
        this.blacklistedCnicsId = blacklistedCnicsId;
    }

    @Column(name = "CNIC_NO" , nullable = false )
    public String getCnicNo()
    {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo)
    {
        this.cnicNo = cnicNo;
    }

    @Column(name = "IS_BLACKLISTED" )
    public Boolean getBlacklisted()
    {
        return isBlacklisted;
    }

    public void setBlacklisted(Boolean blacklisted)
    {
        isBlacklisted = blacklisted;
    }

    @Column(name = "CREATED_ON" , nullable = false )
    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON" , nullable = false )
    public Date getUpdatedOn()
    {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn)
    {
        this.updatedOn = updatedOn;
    }

    @Column(name = "VERSION_NO" , nullable = false )
    public Integer getVersionNo()
    {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo)
    {
        this.versionNo = versionNo;
    }

    @Column(name="COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Transient
    public Long getCreatedBy() {
        if (createdByAppUserModel != null) {
            return createdByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
     */

    @Transient
    public void setCreatedBy(Long appUserId) {
        if(appUserId == null)
        {
            createdByAppUserModel = null;
        }
        else
        {
            createdByAppUserModel = new AppUserModel();
            createdByAppUserModel.setAppUserId(appUserId);
        }
    }

    @Transient
    public Long getUpdatedBy() {
        if (updatedByAppUserModel != null) {
            return updatedByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
     */

    @Transient
    public void setUpdatedBy(Long appUserId) {
        if(appUserId == null)
        {
            updatedByAppUserModel = null;
        }
        else
        {
            updatedByAppUserModel = new AppUserModel();
            updatedByAppUserModel.setAppUserId(appUserId);
        }
    }


    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey)
    {
        this.setBlacklistedCnicsId(primaryKey);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey()
    {
    	
       return getBlacklistedCnicsId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter()
    {
        String parameters = "";
        parameters += "&blacklistedCnicsId=" + getBlacklistedCnicsId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "blacklistedCnicsId";
        return primaryKeyFieldName;
    }
    
  

	@Transient
    public List<AssociationModel> getAssociationModelList()
    {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = new AssociationModel();

        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("updatedByAppUserModel");
        associationModel.setValue(getUpdatedByAppUserModel());

        associationModelList.add(associationModel);

        associationModel = new AssociationModel();

        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("createdByAppUserModel");
        associationModel.setValue(getUpdatedByAppUserModel());

        associationModelList.add(associationModel);

        return associationModelList;
    }
}

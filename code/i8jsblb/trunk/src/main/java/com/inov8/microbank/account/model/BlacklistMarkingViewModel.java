package com.inov8.microbank.account.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.account.vo.BlacklistMarkingVo;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Malik on 8/11/2016.
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BLACKLIST_MARKING_VIEW")
public class BlacklistMarkingViewModel extends BasePersistableModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long appUserId;
    private String cnic;
    private Long appUserTypeId;
    private String appUserType;
    private String userId;
    private Long regStateId;
    private String regState;
    private Long prevRegStateId;
    private String prevRegState;
    private Long blacklisted;
    private String mobileNo;
    private String changedCnicList;
    
    

    private List<BlacklistMarkingVo> blacklistMarkingVoList;

    public BlacklistMarkingViewModel()
    {
        super();
        blacklistMarkingVoList = LazyList.decorate(new ArrayList<BlacklistMarkingVo>(), FactoryUtils.instantiateFactory(BlacklistMarkingVo.class));
    }

    @Column(name="APP_USER_ID")
    @Id
    public Long getAppUserId()
    {
        return appUserId;
    }

    public void setAppUserId(Long appUserId)
    {
        this.appUserId = appUserId;
    }

    @Column(name = "NIC")
    public String getCnic()
    {
        return cnic;
    }

    public void setCnic(String cnic)
    {
        this.cnic = cnic;
    }

    @Column(name = "APP_USER_TYPE_ID")
    public Long getAppUserTypeId()
    {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId)
    {
        this.appUserTypeId = appUserTypeId;
    }

    @Column(name = "APP_USER_TYPE_NAME")
    public String getAppUserType()
    {
        return appUserType;
    }

    public void setAppUserType(String appUserType)
    {
        this.appUserType = appUserType;
    }

    @Column(name = "USER_ID")
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    @Column(name = "REGISTRATION_STATE_ID")
    public Long getRegStateId()
    {
        return regStateId;
    }

    public void setRegStateId(Long regStateId)
    {
        this.regStateId = regStateId;
    }

    @Column(name = "REGISTRATION_STATE_NAME")
    public String getRegState()
    {
        return regState;
    }

    public void setRegState(String regState)
    {
        this.regState = regState;
    }

    @Column(name = "PREV_REGISTRATION_STATE_ID")
    public Long getPrevRegStateId()
    {
        return prevRegStateId;
    }

    public void setPrevRegStateId(Long prevRegStateId)
    {
        this.prevRegStateId = prevRegStateId;
    }

    @Column(name = "PREV_REGISTRATION_STATE_NAME")
    public String getPrevRegState()
    {
        return prevRegState;
    }

    public void setPrevRegState(String prevRegState)
    {
        this.prevRegState = prevRegState;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }
    @Transient
    public List<BlacklistMarkingVo> getBlacklistMarkingVoList()
    {
        return blacklistMarkingVoList;
    }

    public void setBlacklistMarkingVoList(List<BlacklistMarkingVo> blacklistMarkingVoList)
    {
        this.blacklistMarkingVoList = blacklistMarkingVoList;
    }

    @Column(name = "IS_BLACKLISTED")
    public Long getBlacklisted()
    {
        return blacklisted;
    }

    public void setBlacklisted(Long blacklisted)
    {
        this.blacklisted = blacklisted;
    }

    
    @javax.persistence.Transient
    public String getChangedCnicList() {
		return changedCnicList;
	}

	public void setChangedCnicList(String changedCnicList) {
		this.changedCnicList = changedCnicList;
	}
    

	@Override
    @Transient
    public void setPrimaryKey(Long appUserId)
    {
        this.appUserId=appUserId;
    }

    @Override
    @Transient
    public Long getPrimaryKey()
    {
        return getAppUserId();
    }

    @Override
    @Transient
    public String getPrimaryKeyParameter()
    {
        String parameters = "";
        parameters += "&appUserId=" + getAppUserId();
        return parameters;
    }

    @Override
    @Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "appUserId";
        return primaryKeyFieldName;
    }
}

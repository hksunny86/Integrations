package com.inov8.microbank.tax.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inov8.framework.common.model.BasePersistableModel;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Zeeshan on 6/29/2016.
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WHTExemptionVO extends BasePersistableModel implements Serializable {
    private Long whtExemptionId;
    private String userId;
    private String agentName;
    private String mobile;
    private String agentCnic;
    private Boolean active;
    private Boolean isEditAllowed;

    private Date startDate;
    private Date endDate;
    
    private String startDateStr;
    private String endDateStr;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentCnic() {
        return agentCnic;
    }

    public void setAgentCnic(String agentCnic) {
        this.agentCnic = agentCnic;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getWhtExemptionId() {
        return whtExemptionId;
    }

    public void setWhtExemptionId(Long whtExemptionId) {
        this.whtExemptionId = whtExemptionId;
    }

    public Boolean getEditAllowed() {
        return isEditAllowed;
    }

    public void setEditAllowed(Boolean editAllowed) {
        isEditAllowed = editAllowed;
    }

    @Override
    public void setPrimaryKey(Long aLong) {

    }

    @Override
    public Long getPrimaryKey() {
        return null;
    }

    @Override
    public String getPrimaryKeyParameter() {
        return null;
    }

    @Override
    public String getPrimaryKeyFieldName() {
        return null;
    }
}

package com.inov8.microbank.tax.vo;

import com.inov8.microbank.tax.model.WHTConfigModel;

import java.util.Date;

/**
 * Created by Malik on 7/1/2016.
 */
public class WHTConfigVo
{
    private static final long serialVersionUID = 1L;
    private Long whtConfigId;
    private String title;
    private Double filerRate;
    private Double nonFilerRate;
    private Double thresholdLimit;

    public Long getWhtConfigId()
    {
        return whtConfigId;
    }

    public void setWhtConfigId(Long whtConfigId)
    {
        this.whtConfigId = whtConfigId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Double getFilerRate()
    {
        return filerRate;
    }

    public void setFilerRate(Double filerRate)
    {
        this.filerRate = filerRate;
    }

    public Double getNonFilerRate()
    {
        return nonFilerRate;
    }

    public void setNonFilerRate(Double nonFilerRate)
    {
        this.nonFilerRate = nonFilerRate;
    }

    public Double getThresholdLimit()
    {
        return thresholdLimit;
    }

    public void setThresholdLimit(Double thresholdLimit)
    {
        this.thresholdLimit = thresholdLimit;
    }
}

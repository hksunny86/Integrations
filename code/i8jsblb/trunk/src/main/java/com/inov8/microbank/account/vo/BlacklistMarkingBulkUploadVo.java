package com.inov8.microbank.account.vo;

import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Malik on 8/23/2016.
 */
@XmlRootElement(name="bulkCustomerAccountVo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class BlacklistMarkingBulkUploadVo implements Serializable
{
    @XmlTransient
    private MultipartFile csvFile;
    private String cnic;
    private String status;
    private Long appUserId;
    @XmlTransient
    private Boolean isBlacklisted;
    @XmlTransient
    private Boolean validRecord;
    @XmlTransient
    private int invalidRecordsCount;
    @XmlTransient
    private Boolean accountClosedUnsettled;
    @XmlTransient
    private Boolean accountClosedSettled;
    
    @XmlTransient
    private Boolean isDuplicate;

    @XmlTransient

    private Long registrationStateId;
    @XmlTransient
    private Long prevRegistrationStateId;

    public MultipartFile getCsvFile()
    {
        return csvFile;
    }

    public void setCsvFile(MultipartFile csvFile)
    {
        this.csvFile = csvFile;
    }

    public Boolean getIsBlacklisted()
    {
        return isBlacklisted;
    }

    public void setIsBlacklisted(Boolean isBlacklisted)
    {
        this.isBlacklisted = isBlacklisted;
    }

    public Boolean getValidRecord()
    {
        return validRecord;
    }

    public void setValidRecord(Boolean validRecord)
    {
        this.validRecord = validRecord;
    }

    public int getInvalidRecordsCount()
    {
        return invalidRecordsCount;
    }

    public void setInvalidRecordsCount(int invalidRecordsCount)
    {
        this.invalidRecordsCount = invalidRecordsCount;
    }

    public String getCnic()
    {
        return cnic;
    }

    public void setCnic(String cnic)
    {
        this.cnic = cnic;
    }


    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Boolean getAccountClosedUnsettled()
    {
        return accountClosedUnsettled;
    }

    public void setAccountClosedUnsettled(Boolean accountClosedUnsettled)
    {
        this.accountClosedUnsettled = accountClosedUnsettled;
    }

    public Boolean getAccountClosedSettled()
    {
        return accountClosedSettled;
    }

    public void setAccountClosedSettled(Boolean accountClosedSettled)
    {
        this.accountClosedSettled = accountClosedSettled;
    }

    public Long getRegistrationStateId()
    {
        return registrationStateId;
    }

    public void setRegistrationStateId(Long registrationStateId)
    {
        this.registrationStateId = registrationStateId;
    }

    public Long getPrevRegistrationStateId()
    {
        return prevRegistrationStateId;
    }

    public void setPrevRegistrationStateId(Long prevRegistrationStateId)
    {
        this.prevRegistrationStateId = prevRegistrationStateId;
    }


    public Long getAppUserId()
    {
        return appUserId;
    }

    public void setAppUserId(Long appUserId)
    {
        this.appUserId = appUserId;
    }

	public Boolean getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(Boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}



}

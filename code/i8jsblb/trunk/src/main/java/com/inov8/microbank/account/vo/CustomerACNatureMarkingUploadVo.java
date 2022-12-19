package com.inov8.microbank.account.vo;

import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name="bulkCustomerMarkingVo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class CustomerACNatureMarkingUploadVo implements Serializable {
    @XmlTransient
    private MultipartFile csvFile;
    private String cnic;
    private String status;
    private Long appUserId;
    @XmlTransient
    private Long customerACNature;
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

    public void setAccountClosedUnsettled(Boolean accountClosedUnsettled) { this.accountClosedUnsettled = accountClosedUnsettled;}

    public Boolean getAccountClosedSettled()
    {
        return accountClosedSettled;
    }

    public void setAccountClosedSettled(Boolean accountClosedSettled) {this.accountClosedSettled = accountClosedSettled;}

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

    public void setPrevRegistrationStateId(Long prevRegistrationStateId) { this.prevRegistrationStateId = prevRegistrationStateId;}

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

    public Long getCustomerACNature() {return customerACNature;}

    public void setCustomerACNature(Long customerACNature) {this.customerACNature = customerACNature;}
}

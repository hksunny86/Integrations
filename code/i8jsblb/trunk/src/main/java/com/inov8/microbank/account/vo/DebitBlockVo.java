package com.inov8.microbank.account.vo;

import com.inov8.framework.common.model.BasePersistableModel;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DebitBlockVo extends BasePersistableModel implements Serializable {

    private Long appUserId;
    private String mfsId;
    private String encryptedAppUserId;
    private Boolean isAgent;
    private Long usecaseId;
    private Double amount;
    private Boolean isDebitBlocked;
    private String mobileNo;

    private Long accountHolderId;
    private Long noOfMonths;

    public DebitBlockVo() {

    }

    public DebitBlockVo(Long appUserId, Double amount, Boolean isDebitBlocked) {
        super();
        this.appUserId = appUserId;
        this.amount = amount;
        this.isDebitBlocked = isDebitBlocked;
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

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getMfsId() {
        return mfsId;
    }

    public void setMfsId(String mfsId) {
        this.mfsId = mfsId;
    }

    public String getEncryptedAppUserId() {
        return encryptedAppUserId;
    }

    public void setEncryptedAppUserId(String encryptedAppUserId) {
        this.encryptedAppUserId = encryptedAppUserId;
    }

    public Boolean getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(Boolean agent) {
        isAgent = agent;
    }

    public Long getUsecaseId() {
        return usecaseId;
    }

    public void setUsecaseId(Long usecaseId) {
        this.usecaseId = usecaseId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getIsDebitBlocked() {
        return isDebitBlocked;
    }

    public void setIsDebitBlocked(Boolean debitBlocked) {
        isDebitBlocked = debitBlocked;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Long getAccountHolderId() {
        return accountHolderId;
    }

    public void setAccountHolderId(Long accountHolderId) {
        this.accountHolderId = accountHolderId;
    }

    public Long getNoOfMonths() {
        return noOfMonths;
    }

    public void setNoOfMonths(Long noOfMonths) {
        this.noOfMonths = noOfMonths;
    }
}

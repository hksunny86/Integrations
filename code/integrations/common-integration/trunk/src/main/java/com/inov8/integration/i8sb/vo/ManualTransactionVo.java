package com.inov8.integration.i8sb.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Administrator on 11/8/2019.
 */
public class ManualTransactionVo implements Serializable {
    private String channelId;
    private String accountId;
    private String status;
    private String agentID;
    private String amount;
    private String  frequency;
    private String remainingFrequency;
    private String startDate;
    private String  endDate;
    private String remainingTime;
    private String  remainingAmount;
    private String manualTransWindowId;
    private String transCodeDesc;
    private String transactionCode;
    private String startTime;
    private String endTime;
    private String description;


    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRemainingFrequency() {
        return remainingFrequency;
    }

    public void setRemainingFrequency(String remainingFrequency) {
        this.remainingFrequency = remainingFrequency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getManualTransWindowId() {
        return manualTransWindowId;
    }

    public void setManualTransWindowId(String manualTransWindowId) {
        this.manualTransWindowId = manualTransWindowId;
    }

    public String getTransCodeDesc() {
        return transCodeDesc;
    }

    public void setTransCodeDesc(String transCodeDesc) {
        this.transCodeDesc = transCodeDesc;
    }

}

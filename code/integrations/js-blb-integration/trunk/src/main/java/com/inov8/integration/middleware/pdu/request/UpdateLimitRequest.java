package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UpdateCustomerLimitRequest")
public class UpdateLimitRequest implements Serializable {

    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "Accountlevel")
    private String accountLevel;
    @XmlElement(name = "DailySendingLimit")
    private String dailySendingLimit;
    @XmlElement(name = "DailyReceivingLimit")
    private String dailyReceivingLimit;
    @XmlElement(name = "MonthlySendingLimit")
    private String monthlySendingLimit;
    @XmlElement(name = "MonthlyReceivingLimit")
    private String monthlyReceivingLimit;
    @XmlElement(name = "YearlySendingLimit")
    private String yearlySendingLimit;
    @XmlElement(name = "YearlyReceivinglimit")
    private String yearlyReceivingLimit;
    @XmlElement(name = "MobileNumber")
    private String mobileNumber;
    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "ChannelId")
    private String channelId;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "HashData")
    private String hashData;

    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getDailySendingLimit() {
        return dailySendingLimit;
    }

    public void setDailySendingLimit(String dailySendingLimit) {
        this.dailySendingLimit = dailySendingLimit;
    }

    public String getDailyReceivingLimit() {
        return dailyReceivingLimit;
    }

    public void setDailyReceivingLimit(String dailyReceivingLimit) {
        this.dailyReceivingLimit = dailyReceivingLimit;
    }

    public String getMonthlyReceivingLimit() {
        return monthlyReceivingLimit;
    }

    public void setMonthlyReceivingLimit(String monthlyReceivingLimit) {
        this.monthlyReceivingLimit = monthlyReceivingLimit;
    }

    public String getYearlySendingLimit() {
        return yearlySendingLimit;
    }

    public void setYearlySendingLimit(String yearlySendingLimit) {
        this.yearlySendingLimit = yearlySendingLimit;
    }

    public String getYearlyReceivingLimit() {
        return yearlyReceivingLimit;
    }

    public void setYearlyReceivingLimit(String yearlyReceivingLimit) {
        this.yearlyReceivingLimit = yearlyReceivingLimit;
    }

    public String getMonthlySendingLimit() {
        return monthlySendingLimit;
    }

    public void setMonthlySendingLimit(String monthlySendingLimit) {
        this.monthlySendingLimit = monthlySendingLimit;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}

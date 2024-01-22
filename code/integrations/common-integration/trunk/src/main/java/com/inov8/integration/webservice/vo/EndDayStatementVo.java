package com.inov8.integration.webservice.vo;


import java.io.Serializable;

public class EndDayStatementVo implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String mobileNumber;
    private String dateTime;
    private String dayEndBalance;

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

    public String getDayEndBalance() {
        return dayEndBalance;
    }

    public void setDayEndBalance(String dayEndBalance) {
        this.dayEndBalance = dayEndBalance;
    }
}

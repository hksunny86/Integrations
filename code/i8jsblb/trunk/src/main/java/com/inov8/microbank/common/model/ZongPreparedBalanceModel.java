package com.inov8.microbank.common.model;

import java.io.Serializable;

// @Created On 4/11/2023 : Tuesday
// @Created By muhammad.aqeel
public class ZongPreparedBalanceModel implements Serializable  {
    private Double balance;
    private Long mobileNo;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
    }
}

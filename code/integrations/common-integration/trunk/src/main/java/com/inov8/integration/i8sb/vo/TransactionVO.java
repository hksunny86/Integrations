package com.inov8.integration.i8sb.vo;

import java.io.Serializable;

public class TransactionVO  implements Serializable {

    private String dateTime;
    private String debitAmount;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }



}

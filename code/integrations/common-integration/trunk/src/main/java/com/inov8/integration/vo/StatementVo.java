package com.inov8.integration.vo;

import java.io.Serializable;

/**
 * Created by Inov8 on 1/2/2020.
 */
public class StatementVo implements Serializable{
    private String transactionamount;
    private String transactiontype;
    private String dateTime;


    public String getTransactionamount() {
        return transactionamount;
    }

    public void setTransactionamount(String transactionamount) {
        this.transactionamount = transactionamount;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

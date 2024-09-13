package com.example.dbscheduler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "Cooling_Task")
@Data

public class CoolingTask implements Serializable {

    private static final long serialversionUID =
            129348938L;
    @Id
    @Column(name = "COOLING_TASK_ID")
    private Long id;

    @Column(name = "COMMAND")
    private String command;
    @Column(name = "ACCOUNT_NO")
    private String accountNo;
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;
    @Column(name = "TRANSACTION_AMOUNT")
    private Long transactionAmount;

    public CoolingTask(String command, String accountNo, Long transactionId, Long transactionAmount) {
        this.command = command;
        this.accountNo = accountNo;
        this.transactionId = transactionId;
        this.transactionAmount = transactionAmount;
    }

    public CoolingTask() {

    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Long transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}

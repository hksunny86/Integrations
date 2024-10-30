package com.warmbytes.expensededuction.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "EXPENSES_TRANSACTION")
@Entity
@Data
@NoArgsConstructor
public class ExpensesTransaction {

    @Id
    @Column(name = "EXPENSES_TRANSACTION_ID")
    private Long expensesTransactionId;
    @Column(name = "AMOUNT")
    private String amount;
    @Column(name = "NIC")
    private String nic;
    @Column(name = "MOBILE_NO")
    private String mobileNo;
    @Column(name = "BATCH_NO")
    private String batchNo;
    @Column(name = "TRANSACTION_ID")
    private String transactionId;
    @Column(name = "IS_PROCESS")
    private Boolean isProcess;
    @Column(name = "CREATED_ON")
    private Date createdOn;
    @Column(name = "UPDATED_ON")
    private Date updatedOn;
    @Column(name = "UPDATED_BY")
    private Long updatedBy;
    @Column(name = "CREATED_BY")
    private Long createdBy;
    @Column(name = "TRANSACTION_STATUS")
    private String transactionStatus;
    @Column(name = "FAILURE_REASON")
    private String failureReason;
}

package com.example.dbscheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "BLINK_CUSTOMER_LIMIT")
@Data
public class BlinkCustomerLimit implements Serializable {
    private static final long serialVersionUID = 11L;

    @Column(name = "LIMIT_ID" , nullable = false )
    @Id
    private Long limitId;

    @Column(name = "CUSTOMER_ID" )
    private Long customerId;

    @Column(name = "TRANSACTION_TYPE" )
    private Long transactionType;

    @Column(name = "LIMIT_TYPE" )
    private Long limitType;

    @Column(name = "ACCOUNT_ID" )
    private Long accountId;

    @Column(name = "MAXIMUM" )
    private Long maximum;

}

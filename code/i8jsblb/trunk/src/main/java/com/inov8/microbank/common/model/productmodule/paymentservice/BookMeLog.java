package com.inov8.microbank.common.model.productmodule.paymentservice;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name = "BOOKME_LOG_SEQ", strategy = "sequence", parameters = {@org.hibernate.annotations.Parameter(name = "sequence", value = "BOOKME_LOG_SEQ")})
@javax.persistence.SequenceGenerator(name = "BOOKME_LOG_SEQ", sequenceName = "BOOKME_LOG_SEQ")
@Table(name = "BOOKME_LOG")
public class BookMeLog extends BasePersistableModel implements Serializable {

    private Long bookMeLogId;
    private String customerName;
    private String phoneNo;
    private String microbankTxCode;
    private Long paidAmount;
    private Long TransactionAmount;
    private Date paidDate;
    private String orderRefId;
    private String serviceType;
    private String serviceProviderName;
    private Double transactionProcessingAmount;
    private Double totalAmount;
    private Double billAmount;
    private Double baseFare;
    private Double discount;
    private Double fee;
    private Double tax;
    private String bookMeStatus;
    private String bookMeCustomerName;
    private String bookMeCustomerEmail;
    private String bookMeCustomerMobileNo;
    private String bookMeCustomerCnic;

    @Column(name = "BOOKME_CUSTOMER_NAME")
    public String getBookMeCustomerName() {
        return bookMeCustomerName;
    }

    public void setBookMeCustomerName(String bookMeCustomerName) {
        this.bookMeCustomerName = bookMeCustomerName;
    }
    @Column(name = "BOOKME_CUSTOMER_EMAIL")
    public String getBookMeCustomerEmail() {
        return bookMeCustomerEmail;
    }

    public void setBookMeCustomerEmail(String bookMeCustomerEmail) {
        this.bookMeCustomerEmail = bookMeCustomerEmail;
    }
    @Column(name = "BOOKME_CUSTOMER_MOBILENO")
    public String getBookMeCustomerMobileNo() {
        return bookMeCustomerMobileNo;
    }

    public void setBookMeCustomerMobileNo(String bookMeCustomerMobileNo) {
        this.bookMeCustomerMobileNo = bookMeCustomerMobileNo;
    }
    @Column(name = "BOOKME_CUSTOMER_CNIC")
    public String getBookMeCustomerCnic() {
        return bookMeCustomerCnic;
    }

    public void setBookMeCustomerCnic(String bookMeCustomerCnic) {
        this.bookMeCustomerCnic = bookMeCustomerCnic;
    }

    @Column(name = "BOOKME_STATUS")
    public String getBookMeStatus() {
        return bookMeStatus;
    }

    public void setBookMeStatus(String bookMeStatus) {
        this.bookMeStatus = bookMeStatus;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Column(name = "FEE")
    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    @Column(name = "TAXES")
    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    @Column(name = "BASE_FARE")
    public double getBaseFare() {
        return baseFare;
    }

    @Column(name = "DISCOUNT")
    public double getDiscount() {
        return discount;
    }


    @Column(name = "BILL_AMOUNT")
    public double getBillAmount() {
        return billAmount;
    }

    @Column(name = "TRANSACTION_PROCESSING_AMOUNT")
    public Double getTransactionProcessingAmount() {
        return transactionProcessingAmount;
    }

    public void setTransactionProcessingAmount(Double transactionProcessingAmount) {
        this.transactionProcessingAmount = transactionProcessingAmount;
    }

    @Column(name = "TOTAL_AMOUNT")
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookMeLog() {
    }

    @Column(name = "BOOKME_LOG_ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOKME_LOG_SEQ")
    public Long getBookMeLogId() {
        return bookMeLogId;
    }

    public void setBookMeLogId(Long bookMeLogId) {
        this.bookMeLogId = bookMeLogId;
    }

    @Column(name = "CUSTOMER_NAME", length = 50)
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "ORDER_REF_ID", length = 50)
    public String getOrderRefId() {
        return orderRefId;
    }

    public void setOrderRefId(String orderRefId) {
        this.orderRefId = orderRefId;
    }

    @Column(name = "SERVICE_TYPE", length = 50)
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Column(name = "SERVICE_PROVIDER_NAME", length = 50)
    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    @Column(name = "PHONE_NO")
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Column(name = "MICROBANK_TX_CODE", length = 50)
    public String getMicrobankTxCode() {
        return microbankTxCode;
    }

    public void setMicrobankTxCode(String microbankTxCode) {
        this.microbankTxCode = microbankTxCode;
    }

    @Column(name = "PAID_AMOUNT")
    public Long getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Long paidAmount) {
        this.paidAmount = paidAmount;
    }

    @Column(name = "TRANSACTION_AMOUNT")
    public Long getTransactionAmount() {
        return TransactionAmount;
    }

    public void setTransactionAmount(Long transactionAmount) {
        TransactionAmount = transactionAmount;
    }

    @Column(name = "PAID_DATE")
    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {

    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return null;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&bookMeLogId=" + getBookMeLogId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "bookMeLogId";
        return primaryKeyFieldName;
    }
}

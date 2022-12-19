package com.inov8.microbank.server.service.integration.vo;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;

import java.util.Date;

public class BookMeTransactionVO implements ProductVO {
    private String orderRefId;
    private String ServiceType;
    private String serviceProviderName;
    private Double transactionAmount;
    private String customerCnic;
    private String customerName;
    private String customerMobileNo;
    private Double transactionProcessingAmount;
    private Double totalAmount;
    private Date paidDate;
    private Double baseFare;
    private Double discount;
    private Double fee;
    private Double tax;
    private String bookMeStatus;
    private String bookMeCustomerName;
    private String bookMeCustomerEmail;
    private String bookMeCustomerMobileNo;
    private String bookMeCustomerCnic;


    public String getBookMeCustomerName() {
        return bookMeCustomerName;
    }

    public void setBookMeCustomerName(String bookMeCustomerName) {
        this.bookMeCustomerName = bookMeCustomerName;
    }

    public String getBookMeCustomerEmail() {
        return bookMeCustomerEmail;
    }

    public void setBookMeCustomerEmail(String bookMeCustomerEmail) {
        this.bookMeCustomerEmail = bookMeCustomerEmail;
    }

    public String getBookMeCustomerMobileNo() {
        return bookMeCustomerMobileNo;
    }

    public void setBookMeCustomerMobileNo(String bookMeCustomerMobileNo) {
        this.bookMeCustomerMobileNo = bookMeCustomerMobileNo;
    }

    public String getBookMeCustomerCnic() {
        return bookMeCustomerCnic;
    }

    public void setBookMeCustomerCnic(String bookMeCustomerCnic) {
        this.bookMeCustomerCnic = bookMeCustomerCnic;
    }

    public String getBookMeStatus() {
        return bookMeStatus;
    }

    public void setBookMeStatus(String bookMeStatus) {
        this.bookMeStatus = bookMeStatus;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderRefId() {
        return orderRefId;
    }

    public void setOrderRefId(String orderRefId) {
        this.orderRefId = orderRefId;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getCustomerCnic() {
        return customerCnic;
    }

    public void setCustomerCnic(String customerCnic) {
        this.customerCnic = customerCnic;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    public Double getTransactionProcessingAmount() {
        return transactionProcessingAmount;
    }

    public void setTransactionProcessingAmount(Double transactionProcessingAmount) {
        this.transactionProcessingAmount = transactionProcessingAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public void setResponseCode(String responseCode) {

    }

    @Override
    public String getResponseCode() {
        return null;
    }

    @Override
    public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper) {
        BookMeTransactionVO bookMeTransactionVO = (BookMeTransactionVO) productVO;


        if (baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null) {
            String value = baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString().trim();
            bookMeTransactionVO.setCustomerMobileNo(value);
        }


        if (baseWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT) != null) {
            String value = baseWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT).toString().trim();
            try {
                bookMeTransactionVO.setTransactionAmount(Double.parseDouble(value));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (baseWrapper.getObject(CommandFieldConstants.KEY_ORDER_ID) != null) {
            String value = baseWrapper.getObject(CommandFieldConstants.KEY_ORDER_ID).toString().trim();
            try {
                bookMeTransactionVO.setOrderRefId(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (baseWrapper.getObject(CommandFieldConstants.KEY_SERVICE_PROVIDER_NAME) != null) {
            String value = baseWrapper.getObject(CommandFieldConstants.KEY_SERVICE_PROVIDER_NAME).toString().trim();
            try {
                bookMeTransactionVO.setServiceProviderName(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (baseWrapper.getObject(CommandFieldConstants.KEY_SERVICE_TYPE) != null) {
            String value = baseWrapper.getObject(CommandFieldConstants.KEY_SERVICE_TYPE).toString().trim();
            try {
                bookMeTransactionVO.setServiceType(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return bookMeTransactionVO;
    }

    @Override
    public void validateVO(ProductVO productVO) throws FrameworkCheckedException {

    }
}

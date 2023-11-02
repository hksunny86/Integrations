package com.inov8.integration.webservice.vo;

import java.io.Serializable;

public class CustomerValidationVO implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String MessageType;
    private String RequestCode;
    private String TransmissionDateTime;
    private String STAN;
    private String ValueField;
    private String CustomerIdentification;
    private String ResponseCode;
    private String FullName;
    private String DateOfBirth;
    private String MotherName;
    private String FirstTimeCaller;

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getRequestCode() {
        return RequestCode;
    }

    public void setRequestCode(String requestCode) {
        RequestCode = requestCode;
    }

    public String getTransmissionDateTime() {
        return TransmissionDateTime;
    }

    public void setTransmissionDateTime(String transmissionDateTime) {
        TransmissionDateTime = transmissionDateTime;
    }

    public String getSTAN() {
        return STAN;
    }

    public void setSTAN(String STAN) {
        this.STAN = STAN;
    }

    public String getValueField() {
        return ValueField;
    }

    public void setValueField(String valueField) {
        ValueField = valueField;
    }

    public String getCustomerIdentification() {
        return CustomerIdentification;
    }

    public void setCustomerIdentification(String customerIdentification) {
        CustomerIdentification = customerIdentification;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String motherName) {
        MotherName = motherName;
    }

    public String getFirstTimeCaller() {
        return FirstTimeCaller;
    }

    public void setFirstTimeCaller(String firstTimeCaller) {
        FirstTimeCaller = firstTimeCaller;
    }
}

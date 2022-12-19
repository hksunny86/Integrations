package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("Instrument")
public class Instrument implements Serializable {

    private final static long serialVersionUID = 1L;

    private String instrumentNumber;
    private String instrumentStatus;
    private String instrumentAmount;
    private String instrumentClearingDate;

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    public void setInstrumentNumber(String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    public String getInstrumentStatus() {
        return instrumentStatus;
    }

    public void setInstrumentStatus(String instrumentStatus) {
        this.instrumentStatus = instrumentStatus;
    }

    public String getInstrumentAmount() {
        return instrumentAmount;
    }

    public void setInstrumentAmount(String instrumentAmount) {
        this.instrumentAmount = instrumentAmount;
    }

    public String getInstrumentClearingDate() {
        return instrumentClearingDate;
    }

    public void setInstrumentClearingDate(String instrumentClearingDate) {
        this.instrumentClearingDate = instrumentClearingDate;
    }
}

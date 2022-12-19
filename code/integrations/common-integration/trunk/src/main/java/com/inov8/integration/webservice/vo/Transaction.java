package com.inov8.integration.webservice.vo;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Transaction")
public class Transaction implements Serializable {


    private static final long serialVersionUID = -4994580002925333773L;

    @XmlElement(name = "Description")
    private String description;
    @XmlElement(name = "Date")
    private String date;
    @XmlElement(name = "Amount")
    private String amount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
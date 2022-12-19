package com.inov8.integration.middleware.pdu.response;


import com.inov8.integration.webservice.vo.Transaction;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MiniStatementResponse")
public class MiniStatementResponse implements Serializable {

    private static final long serialVersionUID = -1276171995664728552L;

    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "HashData")
    private String hashData;
    @XmlElement(name = "Transaction")
    @XmlElementWrapper(name = "Transactions")
    private List<Transaction> transactions;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

}

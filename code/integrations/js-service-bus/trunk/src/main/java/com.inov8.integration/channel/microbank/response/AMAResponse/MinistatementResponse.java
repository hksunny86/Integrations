package com.inov8.integration.channel.microbank.response.AMAResponse;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


/**
 * Created by Inov8 on 1/2/2020.
 */
public class MinistatementResponse extends Response {




    @XmlElement(name = "responseCode")
    private String responseCode;
    @XmlElement(name = "reserved1")
    private String reserved1;
    @XmlElement(name = "reserved2")
    private String reserved2;
    @XmlElement(name = "reserved3")
    private String reserved3;
    @XmlElement(name = "reserved4")
    private String reserved4;
    @XmlElement(name = "data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }


    @XmlRootElement(name = "data")
    public static class Data {
        @XmlElement(name = "currentBalance")
        private String currentBalance;
        @XmlElement(name = "availableBalance")
        private String availableBalance;
        @XmlElement(name = "currency")
        private String currency;
        @XmlElement(name = "status")
        private String status;
        @XmlElement(name = "statementEntries")
        private List<StatementEntries> statementEntries;


        public String getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(String currentBalance) {
            this.currentBalance = currentBalance;
        }

        public String getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(String availableBalance) {
            this.availableBalance = availableBalance;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<StatementEntries> getStatementEntries() {
            return statementEntries;
        }

        public void setStatementEntries(List<StatementEntries> statementEntries) {
            this.statementEntries = statementEntries;
        }
    }

    @XmlRootElement(name = "statementEntries")
    public static class StatementEntries {
        @XmlElement(name = "txnAmount")
        private String transactionAmount;
        @XmlElement(name = "txnType")
        private String transactionType;
        @XmlElement(name = "dateTime")
        private String dateTime;

        public String getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(String transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
    }
    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        return null;
    }
}

package com.inov8.integration.channel.microbank.response.bopBlbResponse;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.*;

/**
 * Created by Administrator on 2/4/2020.
 */
import javax.xml.bind.annotation.*;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="GetBalanceInquiryServiceResponse")
public class BOPBalanceInquiryResponse extends Response {

        @XmlElement(name = "responseCode")
        private String responseCode;
        @XmlElement(name = "responseDescription")
        private String responseDescription;
        @XmlElement(name = "availableBalance")
        private String availableBalance;
        @XmlElement(name = "currentBalance")
        private String currentBalance;
        @XmlElement(name = "currency")
        private String currency;
        @XmlElement(name = "rrn")
        private String rrn;
        @XmlElement(name = "accountStatus")
        private String accountStatus;
        @XmlElement(name = "reserved1")
        private String reserved1;
        @XmlElement(name = "reserved2")
        private String reserved2;
        @XmlElement(name = "reserved3")
        private String reserved3;
        @XmlElement(name = "reserved4")
        private String reserved4;


        public String getResponseDescription() {
            return responseDescription;
        }

        public void setResponseDescription(String responseDescription) {
            this.responseDescription = responseDescription;
        }

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

        public String getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(String availableBalance) {
            this.availableBalance = availableBalance;
        }

        public String getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(String currentBalance) {
            this.currentBalance = currentBalance;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
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

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        return null;
    }
}

package com.inov8.integration.channel.APIGEE.request.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CashWithdrawlRequest extends Request implements Serializable {
    private static final long serialVersionUID = 2134956891427267824L;

    public BispWithdrawal getBispWithdrawal() {
        return bispWithdrawal;
    }

    public void setBispWithdrawal(BispWithdrawal bispWithdrawal) {
        this.bispWithdrawal = bispWithdrawal;
    }

    @JsonProperty("BISPWithdrawal")
    private BispWithdrawal bispWithdrawal = new BispWithdrawal();
    @JsonProperty("accessToken")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }





    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.getBispWithdrawal().setCustomerAccountNumber(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.getBispWithdrawal().setTransactionAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.getBispWithdrawal().setTransactionId("0"+i8SBSwitchControllerRequestVO.getTransactionId());
        this.getBispWithdrawal().setAgentAccountNumber(i8SBSwitchControllerRequestVO.getSenderMobile());
        this.getBispWithdrawal().setSellerCode(i8SBSwitchControllerRequestVO.getAgentId());
        this.getBispWithdrawal().setSessionId(i8SBSwitchControllerRequestVO.getSessionId());
        this.getBispWithdrawal().setSessionIdNadra(i8SBSwitchControllerRequestVO.getSessionIdNadra());
//        this.getBispWithdrawal().setTransmissionDateTime(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        this.getBispWithdrawal().setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.getBispWithdrawal().setProjectCode(i8SBSwitchControllerRequestVO.getProductCode());
        this.getBispWithdrawal().setTerminalId(i8SBSwitchControllerRequestVO.getAgentId());
        if (i8SBSwitchControllerRequestVO.getMobilePhone() != null && i8SBSwitchControllerRequestVO.getMobilePhone().equalsIgnoreCase("NULL")) {
            this.getBispWithdrawal().setMobileNumber("");
        } else {
            this.getBispWithdrawal().setMobileNumber(i8SBSwitchControllerRequestVO.getMobilePhone());
        }
        this.getBispWithdrawal().setAreaName(i8SBSwitchControllerRequestVO.getAreaName());
        this.getBispWithdrawal().setFingerTempelete(i8SBSwitchControllerRequestVO.getFingerTemplete());
        this.getBispWithdrawal().setFingerIndex(i8SBSwitchControllerRequestVO.getFingerIndex());
        this.getBispWithdrawal().setTempeleteType(i8SBSwitchControllerRequestVO.getTempeleteType());
        this.getBispWithdrawal().setNfiq(i8SBSwitchControllerRequestVO.getNfiq());
        this.getBispWithdrawal().setMinutiaeCount(i8SBSwitchControllerRequestVO.getMinutiaeCount());
        this.getBispWithdrawal().setReserved1(i8SBSwitchControllerRequestVO.getReserved1());
        this.getBispWithdrawal().setWalletAccountID(i8SBSwitchControllerRequestVO.getWalletAccountId());
        this.getBispWithdrawal().setTranType(i8SBSwitchControllerRequestVO.getTransactionType());
        this.getBispWithdrawal().setGpsLatitude("31.5250023");
        this.getBispWithdrawal().setGpsLongitude("74.3475984");
        this.getBispWithdrawal().setMacAddress(i8SBSwitchControllerRequestVO.getMacAddress());
        this.getBispWithdrawal().setImeiNumber(i8SBSwitchControllerRequestVO.getImeiNumber());
        this.getBispWithdrawal().setInternetIP(i8SBSwitchControllerRequestVO.getIpAddress());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getBispWithdrawal().getCustomerAccountNumber())) {
            throw new I8SBValidationException("[Failed] AccountNumber: " + this.getBispWithdrawal().getCustomerAccountNumber());
        }
        if (StringUtils.isEmpty(this.getBispWithdrawal().getTransactionAmount())) {
            throw new I8SBValidationException("[Failed] TransactionAmount: " + this.getBispWithdrawal().getTransactionAmount());
        }
        if (StringUtils.isEmpty(this.getBispWithdrawal().getTransactionId())) {
            throw new I8SBValidationException("[Failed] TransactionId: " + this.getBispWithdrawal().getTransactionId());
        }

        if (StringUtils.isEmpty(this.getBispWithdrawal().getAgentAccountNumber())) {
            throw new I8SBValidationException("[Failed] AgentAccountNumber: " + this.getBispWithdrawal().getAgentAccountNumber());
        }
        if (StringUtils.isEmpty(this.getBispWithdrawal().getSellerCode())) {
            throw new I8SBValidationException("[Failed] SellerCode: " + this.getBispWithdrawal().getSellerCode());
        }
        if (StringUtils.isEmpty(this.getBispWithdrawal().getSessionId())) {
            throw new I8SBValidationException("[Failed] SessionId: " + this.getBispWithdrawal().getSessionId());
        }

        if (StringUtils.isEmpty(this.getBispWithdrawal().getCnic())) {
            throw new I8SBValidationException("[Failed] Cnic: " + this.getBispWithdrawal().getCnic());
        }
        if (StringUtils.isEmpty(this.getBispWithdrawal().getProjectCode())) {
            throw new I8SBValidationException("[Failed] ProjectCode: " + this.getBispWithdrawal().getProjectCode());
        }

        if (StringUtils.isEmpty(this.getBispWithdrawal().getAreaName())) {
            throw new I8SBValidationException("[Failed] AreaName: " + this.getBispWithdrawal().getAreaName());
        }
        if(StringUtils.isEmpty(this.getBispWithdrawal().getGpsLatitude())){
            throw new I8SBValidationException("[Failed] GpsLatitude: "+this.getBispWithdrawal().getGpsLatitude());
        }
        if (StringUtils.isEmpty(this.getBispWithdrawal().getGpsLongitude())){
            throw new I8SBValidationException("[Failed] GpsLongitude: "+this.getBispWithdrawal().getGpsLongitude());
        }
        if(StringUtils.isEmpty(this.getBispWithdrawal().getMacAddress())){
            throw new I8SBValidationException("[Failed] MacAddress: "+this.getBispWithdrawal().getMacAddress());
        }
        if(StringUtils.isEmpty(this.getBispWithdrawal().getImeiNumber())){
            throw new I8SBValidationException("[Failed] ImeiNumber: "+this.getBispWithdrawal().getMacAddress());
        }
        return true;
    }

    public static class BispWithdrawal {
        @JsonProperty("CustomerAccNum")
        private String customerAccountNumber;
        @JsonProperty("TransactionAmount")
        private String transactionAmount;
        @JsonProperty("TransactionID")
        private String transactionId;
        @JsonProperty("AgentAccNum")
        private String agentAccountNumber;
        @JsonProperty("SellerCode")
        private String sellerCode;
        @JsonProperty("SessionIDBAFL")
        private String sessionId;
        @JsonProperty("SessionIDNADRA")
        private String sessionIdNadra;
        @JsonProperty("CNIC")
        private String cnic;
        @JsonProperty("ProjectCode")
        private String projectCode;
        @JsonProperty("TerminalID")
        private String terminalId;
        @JsonProperty("MobileNumber")
        private String mobileNumber;
        @JsonProperty("FingerTemplate")
        private String fingerTempelete;
        @JsonProperty("FingerIndex")
        private String fingerIndex;
        @JsonProperty("TemplateType")
        private String tempeleteType;
        @JsonProperty("AreaName")
        private String areaName;
        @JsonProperty("NFIQ")
        private String nfiq;
        @JsonProperty("MinutiaeCount")
        private String minutiaeCount;
        @JsonProperty("Reserved")
        private String reserved1;
        @JsonProperty("Reserved2")
        private String reserved2;
        @JsonProperty("Reserved3")
        private String reserved3;
        @JsonProperty("Reserved4")
        private String reserved4;
        @JsonProperty("Reserved5")
        private String reserved5;
        @JsonProperty("WalletAccountId")
        private String  walletAccountID;
        @JsonProperty("TranType")
        private String tranType;
        @JsonProperty("GPSLatitude")
        private String gpsLatitude;
        @JsonProperty("GPSLongitude")
        private String gpsLongitude;
        @JsonProperty("IMEINumber")
        private String imeiNumber;
        @JsonProperty("InternetIP")
        private String internetIP;
        @JsonProperty("MACAddress")
        private String macAddress;

        public String getGpsLatitude() {
            return gpsLatitude;
        }

        public void setGpsLatitude(String gpsLatitude) {
            this.gpsLatitude = gpsLatitude;
        }

        public String getGpsLongitude() {
            return gpsLongitude;
        }

        public void setGpsLongitude(String gpsLongitude) {
            this.gpsLongitude = gpsLongitude;
        }

        public String getImeiNumber() {
            return imeiNumber;
        }

        public void setImeiNumber(String imeiNumber) {
            this.imeiNumber = imeiNumber;
        }

        public String getInternetIP() {
            return internetIP;
        }

        public void setInternetIP(String internetIP) {
            this.internetIP = internetIP;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public String getWalletAccountID() {
            return walletAccountID;
        }

        public void setWalletAccountID(String walletAccountID) {
            this.walletAccountID = walletAccountID;
        }

        public String getTranType() {
            return tranType;
        }

        public void setTranType(String tranType) {
            this.tranType = tranType;
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

        public String getReserved5() {
            return reserved5;
        }

        public void setReserved5(String reserved5) {
            this.reserved5 = reserved5;
        }

        public String getCustomerAccountNumber() {
            return customerAccountNumber;
        }

        public void setCustomerAccountNumber(String customerAccountNumber) {
            this.customerAccountNumber = customerAccountNumber;
        }

        public String getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(String transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }



        public String getAgentAccountNumber() {
            return agentAccountNumber;
        }

        public void setAgentAccountNumber(String agentAccountNumber) {
            this.agentAccountNumber = agentAccountNumber;
        }

        public String getSellerCode() {
            return sellerCode;
        }

        public void setSellerCode(String sellerCode) {
            this.sellerCode = sellerCode;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getSessionIdNadra() {
            return sessionIdNadra;
        }

        public void setSessionIdNadra(String sessionIdNadra) {
            this.sessionIdNadra = sessionIdNadra;
        }

        public String getCnic() {
            return cnic;
        }

        public void setCnic(String cnic) {
            this.cnic = cnic;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getFingerTempelete() {
            return fingerTempelete;
        }

        public void setFingerTempelete(String fingerTempelete) {
            this.fingerTempelete = fingerTempelete;
        }

        public String getFingerIndex() {
            return fingerIndex;
        }

        public void setFingerIndex(String fingerIndex) {
            this.fingerIndex = fingerIndex;
        }

        public String getTempeleteType() {
            return tempeleteType;
        }

        public void setTempeleteType(String tempeleteType) {
            this.tempeleteType = tempeleteType;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getReserved1() {
            return reserved1;
        }

        public void setReserved1(String reserved1) {
            this.reserved1 = reserved1;
        }


        public String getNfiq() {
            return nfiq;
        }

        public void setNfiq(String nfiq) {
            this.nfiq = nfiq;
        }

        public String getMinutiaeCount() {
            return minutiaeCount;
        }

        public void setMinutiaeCount(String minutiaeCount) {
            this.minutiaeCount = minutiaeCount;
        }
    }
}

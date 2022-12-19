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

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BalanceInquiryRequest extends Request implements Serializable {
    private static final long serialVersionUID = 2134956891427267824L;
    @JsonProperty("BISPBVSBalanceInquiry")
    private BISPBVSBalanceInquiry bispbvsBalanceInquiry = new BISPBVSBalanceInquiry();

    public BISPBVSBalanceInquiry getBispbvsBalanceInquiry() {
        return bispbvsBalanceInquiry;
    }

    public void setBispbvsBalanceInquiry(BISPBVSBalanceInquiry bispbvsBalanceInquiry) {
        this.bispbvsBalanceInquiry = bispbvsBalanceInquiry;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("accessToken")
    private String accessToken;

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.getBispbvsBalanceInquiry().setTransactionId("000" + i8SBSwitchControllerRequestVO.getTransactionId());
        this.getBispbvsBalanceInquiry().setCnic(i8SBSwitchControllerRequestVO.getCNIC());
//        this.getBispbvsBalanceInquiry().setTransmissionDateTime(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        this.getBispbvsBalanceInquiry().setProjectCode(i8SBSwitchControllerRequestVO.getProductCode());
        this.getBispbvsBalanceInquiry().setAgentAccountNumber(i8SBSwitchControllerRequestVO.getSenderMobile());
        this.getBispbvsBalanceInquiry().setSellerCode(i8SBSwitchControllerRequestVO.getAgentId());
        this.getBispbvsBalanceInquiry().setTerminalId(i8SBSwitchControllerRequestVO.getAgentId());
        if (i8SBSwitchControllerRequestVO.getMobilePhone() != null && i8SBSwitchControllerRequestVO.getMobilePhone().equalsIgnoreCase("NULL")) {
            this.getBispbvsBalanceInquiry().setMobileNumber("");
        } else {
            this.getBispbvsBalanceInquiry().setMobileNumber(i8SBSwitchControllerRequestVO.getConsumerNumber());
        }
//        this.setAccessToken(i8SBSwitchControllerRequestVO.getAccessToken());
        this.getBispbvsBalanceInquiry().setWalletRequired(i8SBSwitchControllerRequestVO.getWalletRequired());
        this.getBispbvsBalanceInquiry().setReserved1(i8SBSwitchControllerRequestVO.getReserved1());
        this.getBispbvsBalanceInquiry().setGpsLatitude("31.5250023");
        this.getBispbvsBalanceInquiry().setGpsLongitude("74.3475984");
        this.getBispbvsBalanceInquiry().setMacAddress(i8SBSwitchControllerRequestVO.getMacAddress());
        this.getBispbvsBalanceInquiry().setImeiNumber(i8SBSwitchControllerRequestVO.getImeiNumber());
        this.getBispbvsBalanceInquiry().setInternetIP(i8SBSwitchControllerRequestVO.getIpAddress());
    }

    @Override

    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getTransactionId())) {
            throw new I8SBValidationException("[Failed] TransactionId: " + this.getBispbvsBalanceInquiry().getTransactionId());
        }

        if (StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getCnic())) {
            throw new I8SBValidationException("[Failed] Cnic: " + this.getBispbvsBalanceInquiry().getCnic());
        }
        if (StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getProjectCode())) {
            throw new I8SBValidationException("[Failed] ProjectCode: " + this.getBispbvsBalanceInquiry().getProjectCode());

        }
        if (StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getAgentAccountNumber())) {

            throw new I8SBValidationException("[Failed] AgentAccountNumber: " + this.getBispbvsBalanceInquiry().getAgentAccountNumber());
        }
        if (StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getSellerCode())) {
            throw new I8SBValidationException("[Failed] AgentId: " + this.getBispbvsBalanceInquiry().getSellerCode());
        }
        if (StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getTerminalId())) {
            throw new I8SBValidationException("[Failed] TerminalId: " + this.getBispbvsBalanceInquiry().getTerminalId());
        }
        if(StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getGpsLatitude())){
            throw new I8SBValidationException("[Failed] GpsLatitude: "+this.getBispbvsBalanceInquiry().getGpsLatitude());
        }
        if (StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getGpsLongitude())){
            throw new I8SBValidationException("[Failed] GpsLongitude: "+this.getBispbvsBalanceInquiry().getGpsLongitude());
        }
        if(StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getMacAddress())){
            throw new I8SBValidationException("[Failed] MacAddress: "+this.getBispbvsBalanceInquiry().getMacAddress());
        }
        if(StringUtils.isEmpty(this.getBispbvsBalanceInquiry().getImeiNumber())){
            throw new I8SBValidationException("[Failed] ImeiNumber: "+this.getBispbvsBalanceInquiry().getMacAddress());
        }
        return true;
    }

    public static class BISPBVSBalanceInquiry {
        @JsonProperty("TransactionID")
        private String transactionId;
        @JsonProperty("CNIC")
        private String cnic;
        @JsonProperty("TransmissionDateAndTime")
        private String transmissionDateTime;
        @JsonProperty("ProjectCode")
        private String projectCode;
        @JsonProperty("AgentAccNum")
        private String agentAccountNumber;
        @JsonProperty("SellerCode")
        private String sellerCode;
        @JsonProperty("TerminalID")
        private String terminalId;
        @JsonProperty("MobileNumber")
        private String mobileNumber;
        @JsonProperty("WalletIdRequired")
        private String walletRequired;
        @JsonProperty("Reserved")
        private String reserved1;
        @JsonProperty("reserved2")
        private String reserved2;
        @JsonProperty("reserved3")
        private String reserved3;
        @JsonProperty("reserved4")
        private String reserved4;
        @JsonProperty("reserved5")
        private String reserved5;
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

        public String getWalletRequired() {
            return walletRequired;
        }

        public void setWalletRequired(String walletRequired) {
            this.walletRequired = walletRequired;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
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

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getReserved1() {
            return reserved1;
        }

        public void setReserved1(String reserved1) {
            this.reserved1 = reserved1;
        }

        public String getTransmissionDateTime() {
            return transmissionDateTime;
        }

        public void setTransmissionDateTime(String transmissionDateTime) {
            this.transmissionDateTime = transmissionDateTime;
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

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }
    }
}

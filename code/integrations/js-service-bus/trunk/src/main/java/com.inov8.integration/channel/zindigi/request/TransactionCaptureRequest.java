package com.inov8.integration.channel.zindigi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "password",
        "mobileNo",
        "date",
        "rrn",
        "transactionType",
        "amount",
        "segment",
        "hashData"
}
)
public class TransactionCaptureRequest extends Request implements Serializable {

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("mobileNo")
    private String mobileNo;
    @JsonProperty("date")
    private String date;
    @JsonProperty("rrn")
    private String rrn;
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("segment")
    private String segment;
    @JsonProperty("hashData")
    private String hashData;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setUsername(PropertyReader.getProperty("zindigi.username"));
        this.setPassword(PropertyReader.getProperty("zindigi.password"));
        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setTransactionType(i8SBSwitchControllerRequestVO.getTransactionType());
        this.setAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.setSegment(i8SBSwitchControllerRequestVO.getSegmentCode());
        String hashData = org.apache.commons.codec.digest.DigestUtils.sha256Hex(this.getUsername() + this.getPassword() + this.getMobileNo() + this.getRrn() + this.getTransactionType() + this.getAmount() + this.getSegment());
        this.setHashData(hashData);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getMobileNo())) {
            throw new I8SBValidationException("[Failed] Mobile Number :" + this.getMobileNo());
        }
        if (StringUtils.isEmpty(this.getAmount())) {
            throw new I8SBValidationException("[Failed] Amount :" + this.getAmount());
        }
        return true;
    }
}
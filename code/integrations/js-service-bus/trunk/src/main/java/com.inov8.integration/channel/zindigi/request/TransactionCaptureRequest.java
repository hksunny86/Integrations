package com.inov8.integration.channel.zindigi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.inov8.integration.enums.DateFormatEnum.MERCHANT_CAMPING_DATE_FORMAT;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rnr",
        "mobileNo",
        "transactionType",
        "amount",
        "date",
        "segment",
        "username",
        "password",
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @JsonProperty("rnr")
    private String rrn;
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("amount")
    private Double amount;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
        Double am= Double.valueOf(i8SBSwitchControllerRequestVO.getAmount());
        String amo= ((Long)am.longValue()).toString();
        this.setAmount(Double.valueOf(i8SBSwitchControllerRequestVO.getAmount()));
        this.setSegment(i8SBSwitchControllerRequestVO.getSegmentId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String newDate=null;
        String finalDate=null;
            newDate = sdf.format(new Date());
            this.setDate(new Date());

        String hashData = org.apache.commons.codec.digest.DigestUtils.sha256Hex(this.getRrn() + this.getMobileNo() + this.getTransactionType() + amo +newDate+ this.getSegment() + this.getUsername() + this.getPassword());
        this.setHashData(hashData);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getMobileNo())) {
            throw new I8SBValidationException("[Failed] Mobile Number :" + this.getMobileNo());
        }

        return true;
    }
}
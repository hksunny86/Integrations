package com.inov8.integration.channel.AppInSnap.request.CustomerDataSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.inov8.integration.channel.AppInSnap.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CustomerTransactionDataSetRequest extends Request implements Serializable {
    private static final long serialVersionUID = 2134956891427267824L;

    private String TransactionId;
    private String SenderCnic;
    private String SenderMobile;
    private String SenderCity;
    private String TransactionAmount;
    private String TransactionDate;
    private String ReceiverCnic;
    private String ReceiverMobile;
    private String ReceiverCity;
    private String OptionalField1;
    private String OptionalField2;
    private String OptionalField3;

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getSenderCnic() {
        return SenderCnic;
    }

    public void setSenderCnic(String senderCnic) {
        SenderCnic = senderCnic;
    }

    public String getSenderMobile() {
        return SenderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        SenderMobile = senderMobile;
    }

    public String getSenderCity() {
        return SenderCity;
    }

    public void setSenderCity(String senderCity) {
        SenderCity = senderCity;
    }

    public String getTransactionAmount() {
        return TransactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        TransactionAmount = transactionAmount;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    public String getReceiverCnic() {
        return ReceiverCnic;
    }

    public void setReceiverCnic(String receiverCnic) {
        ReceiverCnic = receiverCnic;
    }

    public String getReceiverMobile() {
        return ReceiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        ReceiverMobile = receiverMobile;
    }

    public String getReceiverCity() {
        return ReceiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        ReceiverCity = receiverCity;
    }

    public String getOptionalField1() {
        return OptionalField1;
    }

    public void setOptionalField1(String optionalField1) {
        OptionalField1 = optionalField1;
    }

    public String getOptionalField2() {
        return OptionalField2;
    }

    public void setOptionalField2(String optionalField2) {
        OptionalField2 = optionalField2;
    }

    public String getOptionalField3() {
        return OptionalField3;
    }

    public void setOptionalField3(String optionalField3) {
        OptionalField3 = optionalField3;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setTransactionId(i8SBSwitchControllerRequestVO.getTransactionId());
        this.setTransactionAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setTransactionDate(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        this.setSenderCity(i8SBSwitchControllerRequestVO.getSenderCity());
        this.setSenderCnic(i8SBSwitchControllerRequestVO.getSenderCnic());
        this.setSenderMobile(i8SBSwitchControllerRequestVO.getSenderMobile());
        this.setReceiverCity(i8SBSwitchControllerRequestVO.getRecieverCity());
        this.setReceiverMobile(i8SBSwitchControllerRequestVO.getRecieverMobileNo());
        this.setReceiverCnic(i8SBSwitchControllerRequestVO.getRecieverCnic());
        this.setOptionalField1(i8SBSwitchControllerRequestVO.getReserved1());
        this.setOptionalField2(i8SBSwitchControllerRequestVO.getReserved2());
        this.setOptionalField3(i8SBSwitchControllerRequestVO.getReserved3());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getTransactionId())) {
            throw new I8SBValidationException("[Failed] TransactionId:" + this.getTransactionId());
        }
        if (StringUtils.isEmpty(this.getTransactionDate())) {
            throw new I8SBValidationException("[Failed] TransactionDate:" + this.getTransactionDate());
        }
        if (StringUtils.isEmpty(this.getTransactionAmount())) {
            throw new I8SBValidationException("[Failed] TransactionAmount:" + this.getTransactionAmount());
        }
        if (StringUtils.isEmpty(this.getReceiverMobile())) {
            throw new I8SBValidationException("[Failed] RecieverMobile:" + this.getReceiverMobile());
        }
        if (StringUtils.isEmpty(this.getReceiverCnic())) {
            throw new I8SBValidationException("[Failed] RecieverCnic:" + this.getReceiverCnic());
        }
        if (StringUtils.isEmpty((this.getReceiverCity()))) {
            throw new I8SBValidationException("[Failed] RecieverCity:" + this.getReceiverCity());
        }
        if (StringUtils.isEmpty(this.getSenderMobile())) {
            throw new I8SBValidationException("[Failed] RecieverMobile:" + this.getSenderMobile());
        }
        if (StringUtils.isEmpty(this.getSenderCnic())) {
            throw new I8SBValidationException("[Failed] RecieverCnic:" + this.getSenderCnic());
        }
        if (StringUtils.isEmpty((this.getSenderCity()))) {

            throw new I8SBValidationException("[Failed] RecieverCity:" + this.getSenderCity());
        }
        return true;
    }
}

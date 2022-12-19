package com.inov8.integration.channel.vrg.echallan.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.CommonUtils;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;

/**
 * <p>Java class for BillInquiryResponse complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="BillInquiryResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BillInquiryResponse")
public class BillInquiryResponse extends Response {

    @XmlElement(name = "Response_Code")
    private String responseCode;
    @XmlElement(name = "Consumer_Detail")
    private String consumerDetail;
    @XmlElement(name = "Bill_Status")
    private String billStatus;
    @XmlElement(name = "Due_Date")
    private String dueDate;
    @XmlElement(name = "Amount_Within_Due_Date")
    private String amountWithinDueDate;
    @XmlElement(name = "Amount_After_Due_Date")
    private String amountAfterDueDate;
    @XmlElement(name = "Billing_Month")
    private String billingMonth;
    @XmlElement(name = "Date_Paid")
    private String datePaid;
    @XmlElement(name = "Amount_Paid")
    private String amountPaid;
    @XmlElement(name = "Tran_Auth_Id")
    private String tranAuthID;
    @XmlElement(name = "Reserved")
    private String reserved;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getConsumerDetail() {
        return consumerDetail;
    }

    public void setConsumerDetail(String consumerDetail) {
        this.consumerDetail = consumerDetail;
    }

    public String getAmountWithinDueDate() {
        return amountWithinDueDate;
    }

    public void setAmountWithinDueDate(String amountWithinDueDate) {
        this.amountWithinDueDate = amountWithinDueDate;
    }

    public String getAmountAfterDueDate() {
        return amountAfterDueDate;
    }

    public void setAmountAfterDueDate(String amountAfterDueDate) {
        this.amountAfterDueDate = amountAfterDueDate;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(String datePaid) {
        this.datePaid = datePaid;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getTranAuthID() {
        return tranAuthID;
    }

    public void setTranAuthID(String tranAuthID) {
        this.tranAuthID = tranAuthID;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.responseCode);
//        i8SBSwitchControllerResponseVO.setConsumerTitle(this.consumerDetail);
//        i8SBSwitchControllerResponseVO.setBillStatus(this.billStatus);
//        i8SBSwitchControllerResponseVO.setDueDate(this.dueDate);
//        i8SBSwitchControllerResponseVO.setBillAmount(amountWithinDueDate);
//        i8SBSwitchControllerResponseVO.setBillAmountAfterDueDate(amountAfterDueDate);
//        i8SBSwitchControllerResponseVO.setBillingMonth(this.billingMonth);
//        i8SBSwitchControllerResponseVO.setDatePaid(this.datePaid);
//        i8SBSwitchControllerResponseVO.setAmount(this.amountPaid);
//        i8SBSwitchControllerResponseVO.setTransactionId(this.tranAuthID);

        i8SBSwitchControllerResponseVO.setConsumerTitle("asif");
        i8SBSwitchControllerResponseVO.setBillStatus("u");
        i8SBSwitchControllerResponseVO.setDueDate("20180111");
        i8SBSwitchControllerResponseVO.setBillAmount("300.211");
        i8SBSwitchControllerResponseVO.setBillAmountAfterDueDate("300.41");
        i8SBSwitchControllerResponseVO.setBillingMonth("01");
        i8SBSwitchControllerResponseVO.setDatePaid("20210111");
        i8SBSwitchControllerResponseVO.setAmount("3517.211");
        i8SBSwitchControllerResponseVO.setTransactionId("000000001000025");
        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public void parseResponse(I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {

        List<String> matchedString = CommonUtils.eChallanBillInquiryResponseParser(getReturnedString());

        if (matchedString != null && matchedString.size() == 11) {

            if (!StringUtils.isWhitespace(matchedString.get(0))) this.setResponseCode(matchedString.get(0));
            if (!StringUtils.isWhitespace(matchedString.get(1))) this.setConsumerDetail(matchedString.get(1).trim());
            if (!StringUtils.isWhitespace(matchedString.get(2))) this.setBillStatus(matchedString.get(2));
            if (!StringUtils.isWhitespace(matchedString.get(3))) this.setDueDate(matchedString.get(3));
            if (!StringUtils.isWhitespace(matchedString.get(4)))
                this.setAmountWithinDueDate(new BigDecimal(new BigInteger(matchedString.get(4)), 2).toString());
            if (!StringUtils.isWhitespace(matchedString.get(5)))
                this.setAmountAfterDueDate(new BigDecimal(new BigInteger(matchedString.get(5)), 2).toString());
            if (!StringUtils.isWhitespace(matchedString.get(6))) this.setBillingMonth(matchedString.get(6));
            if (!StringUtils.isWhitespace(matchedString.get(7))) this.setDatePaid(matchedString.get(7));
            if (!StringUtils.isWhitespace(matchedString.get(8)))
                this.setAmountPaid(new Long(matchedString.get(8).substring(0, 10)).toString());
            if (!StringUtils.isWhitespace(matchedString.get(9))) this.setTranAuthID(matchedString.get(9));
            if (!StringUtils.isWhitespace(matchedString.get(10))) this.setReserved(matchedString.get(10).trim());

        } else if (getReturnedString().length() >= 2) {
            this.setResponseCode(getReturnedString().substring(0, 2));
        }
    }

    public static void main(String args[]) {
        //String str = String.format("%.2f", 12.3456);
        System.out.println("Formatted number = " + new BigDecimal(new BigInteger("12345678"), 2));
    }

}



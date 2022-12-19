package com.inov8.integration.channel.vrg.echallan.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Formatter;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BillPayment")
public class BillPaymentRequest extends Request implements Serializable {

    private static final long serialVersionUID = 252818645430121545L;
    @XmlElement(name = "tranDate")
    private String date;
    @XmlElement(name = "tranTime")
    private String time;
    @XmlElement(name = "authId")
    private String tranAuthId;
    @XmlElement(name = "amount")
    private String transactionAmount;

    public BillPaymentRequest() {
        super();
    }

    public BillPaymentRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        super(i8SBSwitchControllerRequestVO);

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return transactionAmount;
    }

    public void setAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTranAuthId() {
        return tranAuthId;
    }

    public void setTranAuthId(String tranAuthId) {
        this.tranAuthId = tranAuthId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        String formattedamount = null;
        if (i8SBSwitchControllerRequestVO.getTransactionAmount() != null) {
            formattedamount = formatDoubleByPattern(Double.valueOf(i8SBSwitchControllerRequestVO.getTransactionAmount()), "####.00");
        }
        if (NumberUtils.isNumber(formattedamount)) this.setAmount(StringUtils.leftPad(formattedamount.replace(".", ""), 13, '0'));
        this.setTranAuthId(i8SBSwitchControllerRequestVO.getRRN());
        this.setDate(DateUtil.buildTransactionDate());
        this.setTime(DateUtil.buildTransactionTime());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        validateCommonRequestAttributes();

        if (StringUtils.isEmpty(this.getAmount())) {
            throw new I8SBValidationException("[FAILED] Validation Failed Transaction Amount: " +
                    this.getAmount());
        }

        if (StringUtils.isEmpty(this.getTranAuthId())) {
            throw new I8SBValidationException("[FAILED] Validation Failed Transaction Auth ID: " +
                    this.getTranAuthId());
        }

        return true;
    }

    public static String formatDoubleByPattern(Double param, String pattern) {

        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(param);
    }

    @Override
    public String toString() {
        return null;
    }
}

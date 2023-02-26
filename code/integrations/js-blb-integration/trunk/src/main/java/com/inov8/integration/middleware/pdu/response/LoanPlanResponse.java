package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.webservice.optasiaVO.*;
import com.inov8.integration.webservice.vo.WebServiceVO;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "LoanAmount",
        "DueDatePlan",
        "HashData",
})
public class LoanPlanResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("LoanAmount")
    private List<LoanAmount> loanAmountList;
    @JsonProperty("DueDatePlan")
    private List<DueDatePlan> dueDatePlans;
    @JsonProperty("HashData")
    private String hashData;

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

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public List<LoanAmount> getLoanAmountList() {
        return loanAmountList;
    }

    public void setLoanAmountList(List<LoanAmount> loanAmountList) {
        this.loanAmountList = loanAmountList;
    }

    public List<DueDatePlan> getDueDatePlans() {
        return dueDatePlans;
    }

    public void setDueDatePlans(List<DueDatePlan> dueDatePlans) {
        this.dueDatePlans = dueDatePlans;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public WebServiceVO repaymentPlan(WebServiceVO webServiceVO) throws Exception {

        double amount = Double.parseDouble(webServiceVO.getAmount());

//        LoanAmount week1 = new LoanAmount();
//        LoanAmount week2 = new LoanAmount();
//        LoanAmount week3 = new LoanAmount();
//        LoanAmount week4 = new LoanAmount();
//        DueDatePlan week5 = new DueDatePlan();
//        DueDatePlan week6 = new DueDatePlan();
//        DueDatePlan week7 = new DueDatePlan();
//        DueDatePlan week8 = new DueDatePlan();
//        List<LoanAmount> amountList = new ArrayList<>();
//        List<DueDatePlan> dueDatePlanList = new ArrayList<>();
//        DecimalFormat df = new DecimalFormat("####0.00");
//
//        week1.setTitle("Week1");
//        week1.setWeek(String.valueOf(1));
//        week1.setFee(String.valueOf(df.format(1 * 0.045 * amount)));
//        week1.setAmount(String.valueOf(df.format(amount + Double.parseDouble(week1.getFee()))));
//        amountList.add(week1);
//
//        week2.setTitle("Week2");
//        week2.setWeek(String.valueOf(2));
//        week2.setFee(String.valueOf(df.format(2 * 0.045 * amount)));
//        week2.setAmount(String.valueOf(df.format(amount + Double.parseDouble(week2.getFee()))));
//        amountList.add(week2);
//
//        week3.setTitle("Week3");
//        week3.setWeek(String.valueOf(3));
//        week3.setFee(String.valueOf(df.format(3 * 0.045 * amount)));
//        week3.setAmount(String.valueOf(df.format(amount + Double.parseDouble(week3.getFee()))));
//        amountList.add(week3);
//
//        week4.setTitle("Week4");
//        week4.setWeek(String.valueOf(4));
//        week4.setFee(String.valueOf(df.format(4 * 0.045 * amount)));
//        week4.setAmount(String.valueOf(df.format(amount + Double.parseDouble(week4.getFee()))));
//        amountList.add(week4);
//
//        week5.setTitle("Week5");
//        week5.setWeek(String.valueOf(5));
//        week5.setFee(String.valueOf( df.format(0.04 * amount + (Double.parseDouble(week4.getFee())))));
//        week5.setAmount(String.valueOf(df.format(amount + Double.parseDouble(week5.getFee()))));
//        dueDatePlanList.add(week5);
//
//        week6.setTitle("Week6");
//        week6.setWeek(String.valueOf(6));
//        week6.setFee(String.valueOf(df.format(0.04 * (Double.parseDouble(week5.getAmount())) + (Double.parseDouble(week5.getFee())))));
//        week6.setAmount(String.valueOf(df.format(amount + Double.parseDouble(week6.getFee()))));
//        dueDatePlanList.add(week6);
//
//        week7.setTitle("Week7");
//        week7.setWeek(String.valueOf(7));
//        week7.setFee(String.valueOf(df.format(0.04 * (Double.parseDouble(week6.getAmount())) + (Double.parseDouble(week6.getFee())))));
//        week7.setAmount(String.valueOf(df.format(amount + Double.parseDouble(week7.getFee()))));
//        dueDatePlanList.add(week7);
//
//        week8.setTitle("Week8");
//        week8.setWeek(String.valueOf(8));
//        week8.setFee(String.valueOf(df.format(0.04 * (Double.parseDouble(week7.getAmount())) + (Double.parseDouble(week7.getFee())))));
//        week8.setAmount(String.valueOf(df.format(amount + Double.parseDouble(week8.getFee()))));
//        dueDatePlanList.add(week8);

        List<LoanAmount> amountList = new ArrayList<>();
        List<DueDatePlan> dueDatePlanList = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("####0.00");

        for (int i = 1; i <= 8; i++) {
            if (i <= 4) {
                LoanAmount loanAmountWeek1 = new LoanAmount();
                double fee = 4.5 * i;
                double totalAmount = amount + fee;
                loanAmountWeek1.setFee(df.format(String.valueOf(fee)));
                loanAmountWeek1.setAmount(df.format(String.valueOf(totalAmount)));
                loanAmountWeek1.setWeek(String.valueOf(i));
                loanAmountWeek1.setTitle("Week " + i);
                amountList.add(loanAmountWeek1);
                this.setLoanAmountList(amountList);
            } else {
                DueDatePlan dueDatePlanObj = new DueDatePlan();
                double fee = (4.0 * i) + 2;
                double totalAmount = amount + fee;
                dueDatePlanObj.setFee(df.format(String.valueOf(fee)));
                dueDatePlanObj.setAmount(df.format(String.valueOf(totalAmount)));
                dueDatePlanObj.setWeek(String.valueOf(i));
                dueDatePlanObj.setTitle("Week " + i);
                dueDatePlanList.add(dueDatePlanObj);
                this.setDueDatePlans(dueDatePlanList);
            }
        }


//            this.setLoanAmountList(amountList);
//            this.setDueDatePlans(dueDatePlanList);
        this.setResponseDescription("Success");
        webServiceVO.setLoanAmountList(amountList);
        webServiceVO.setDueDatePlanList(dueDatePlanList);
        webServiceVO.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
        webServiceVO.setResponseCodeDescription("Success");

        return webServiceVO;
    }

}

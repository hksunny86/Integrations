package com.inov8.integration.channel.tasdeeq.mock;

import com.inov8.integration.channel.tasdeeq.request.CustomAnalyticsRequest;
import com.inov8.integration.channel.tasdeeq.response.CustomAnalyticsResponse;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

public class TasdeeqMock {


    public CustomAnalyticsResponse customAnalyticsMockResponse(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        CustomAnalyticsResponse customAnalyticsResponse = new CustomAnalyticsResponse();

        if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("3520228234023")) {
            customAnalyticsResponse.setMessageCode("10922000");
            customAnalyticsResponse.setMessage("Please enter valid CNIC e.g. 12345-1234567-1/1234512345671");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("3430128365371")) {
            customAnalyticsResponse.setMessageCode("10922003");
            customAnalyticsResponse.setMessage("Please enter valid Date of Birth e.g. 01-Jan-2001");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("3660238069587")) {
            customAnalyticsResponse.setMessageCode("10922007");
            customAnalyticsResponse.setMessage("Please enter valid Loan Amount");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("4230123992120")) {
            customAnalyticsResponse.setMessageCode("10922011");
            customAnalyticsResponse.setMessage("Invalid CNIC: Last digit of CNIC must be odd for Male");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("4210121775769")) {
            customAnalyticsResponse.setMessageCode("10922012");
            customAnalyticsResponse.setMessage("Invalid CNIC: Last digit of CNIC must be even for Female");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("4310571652525")) {
            customAnalyticsResponse.setMessageCode("10922016");
            customAnalyticsResponse.setMessage("Please enter CNIC to Generate Report");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("3740572923269")) {
            customAnalyticsResponse.setMessageCode("10000000");
            customAnalyticsResponse.setMessage("Something went wrong. Please try again!");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("1520136041607")) {
            customAnalyticsResponse.setMessageCode("10922008");
            customAnalyticsResponse.setMessage("Loan Amount must be greater than zero");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("4220144169943")) {
            customAnalyticsResponse.setMessageCode("10921001");
            customAnalyticsResponse.setMessage("Please enter valid Full Name");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("3460244971127")) {
            customAnalyticsResponse.setMessageCode("10921002");
            customAnalyticsResponse.setMessage("Please enter valid gender e.g 'M', 'm', 'F' or 'f'");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("3660385025071")) {
            customAnalyticsResponse.setMessageCode("10921003");
            customAnalyticsResponse.setMessage("Please enter valid City");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("4440134117857")) {
            customAnalyticsResponse.setMessageCode("10921006");
            customAnalyticsResponse.setMessage("Please enter valid Father/Husband Name");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("4250199062493")) {
            customAnalyticsResponse.setMessageCode("10921018");
            customAnalyticsResponse.setMessage("CNIC Field Length Exceeded");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("4530435389339")) {
            customAnalyticsResponse.setMessageCode("10921019");
            customAnalyticsResponse.setMessage("Full Name Field Length Exceeded");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("1520250562209")) {
            customAnalyticsResponse.setMessageCode("10921020");
            customAnalyticsResponse.setMessage("Date of Birth Field Length Exceeded");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("3610499761927")) {
            customAnalyticsResponse.setMessageCode("10921021");
            customAnalyticsResponse.setMessage("Gender Field Length Exceeded");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("1730114050979")) {
            customAnalyticsResponse.setMessageCode("10921022");
            customAnalyticsResponse.setMessage("Current Address Field Length Exceeded");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("3503736712674")) {
            customAnalyticsResponse.setMessageCode("10921023");
            customAnalyticsResponse.setMessage("City Field Length Exceeded");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("1620240221275")) {
            customAnalyticsResponse.setMessageCode("10921025");
            customAnalyticsResponse.setMessage("Loan Amount Field Length Exceeded");
        } else if (i8SBSwitchControllerRequestVO.getCNIC().equalsIgnoreCase("3520251717474")) {
            customAnalyticsResponse.setMessageCode("10921029");
            customAnalyticsResponse.setMessage("Father/Husband Name Field Length Exceeded");
        }


        return customAnalyticsResponse;
    }

    public String customAnalytics() {

        String customAnalytics = "{\n" +
                "    \"statusCode\": \"111\",\n" +
                "    \"messageCode\": \"00922001\",\n" +
                "    \"message\": \"Report generated successfully\",\n" +
                "    \"data\": {\n" +
                "        \"reportDate\": \"13 Mar 2023\",\n" +
                "        \"reportTime\": \"13:55:45\",\n" +
                "        \"refNo\": \"202303-39138371\",\n" +
                "        \"NAME\": \"AIMAN ZAFFAR\",\n" +
                "        \"CNIC\": \"35202-5171747-4\",\n" +
                "        \"DOB\": \"31/12/1992\",\n" +
                "        \"CITY\": \"*\",\n" +
                "        \"noOfActiveAccounts\": 0,\n" +
                "        \"totalOutstandingBalance\": \"A\",\n" +
                "        \"PLUS_30_24M\": \"A\",\n" +
                "        \"PLUS_60_24M\": \"B\",\n" +
                "        \"PLUS_90_24M\": \"C\",\n" +
                "        \"PLUS_120_24M\": \"*\",\n" +
                "        \"PLUS_150_24M\": \"*\",\n" +
                "        \"PLUS_180_24M\": \"*\",\n" +
                "        \"WRITE_OFF\": \"*\",\n" +
                "        \"disclaimerText\": \"The information contained in this report has been compiled from data provided by financial institutions and does not represent the opinion of Aequitas Information Services Limited with regards to the credit worthiness of the subject. As such, Aequitas Information Services Limited will not be liable for any loss or damage arising from the information contained herein and does not warrant the completeness, timeliness or accuracy of any data. The information contained in this report is supplied on a confidential basis to you and shall not be disclosed to any other person.\",\n" +
                "        \"remarks\": \"\"\n" +
                "    }\n" +
                "}";
        return customAnalytics;
    }

    public String authenticatedUpdated() {

        String authenticatedUpdated = "{\"messageCode\": \"00170017\", \"message\": \"Login Successful\", \"data\": {\"auth_token\": \"O7dvrfGaUOunKqxi5gtRgSbFtwyNwD\"}, \"statusCode\": \"111\"}";

        return authenticatedUpdated;
    }
}

package com.inov8.integration.channel.tasdeeq.mock;

public class TasdeeqMock {

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
                "        \"totalOutstandingBalance\": \"*\",\n" +
                "        \"PLUS_30_24M\": \"*\",\n" +
                "        \"PLUS_60_24M\": \"*\",\n" +
                "        \"PLUS_90_24M\": \"*\",\n" +
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

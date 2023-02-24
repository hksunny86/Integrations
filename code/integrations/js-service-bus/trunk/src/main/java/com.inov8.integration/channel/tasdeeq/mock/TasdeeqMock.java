package com.inov8.integration.channel.tasdeeq.mock;

public class TasdeeqMock {

    public String customAnalytics() {

        String customAnalytics = "{ \"statusCode\": \"111\", \n" +
                " \"messageCode\": \"00922001\",\n" +
                " \"message\": \"Report generated successfully\", \n" +
                " \"data\": { \n" +
                "   \"reportDate\": \"13 Aug 2020\", \n" +
                "   \"reportTime\": \"15:26:38\",\n" +
                "   \"NAME\": \"\", \n" +
                "   \"CNIC\": \"xxxxx-xxxxxxx-x\",\n" +
                "   \"CITY\": \"\", \n" +
                "   \"noOfActiveAccounts\": \"\", \n" +
                "   \"TotalOutstandingBalance\":\"\",\n" +
                "   \"DOB\": \"\",\n" +
                "   \"PLUS_30_24M\": \"*\",\n" +
                "   \"PLUS_60_24M\": \"*\",\n" +
                "   \"PLUS_90_24M\": \"*\",\n" +
                "   \"PLUS_120_24M\": \"*\",\n" +
                "   \"PLUS_150_24M\": \"*\", \n" +
                "   \"PLUS_180_24M\": \"*\", \n" +
                "   \"WRITE_OFF\": \"*\", \n" +
                "   \"disclaimerText\": \"The information contained in this report has been compiled from data provided by financial institutions and does not represent the opinion of Aequitas Information Services Limited with regards to the credit worthiness of the subject. As such, Aequitas Information Services Limited will not be liable for any loss or damage arising from the information contained herein and does not warrant the completeness, timeliness or accuracy of any data. The information contained in this report is supplied on a confidential basis to you and shall not be disclosed to any other person.\",\n" +
                "   \"remarks\": \"\"\n" +
                " }\n" +
                "}";
        return customAnalytics;
    }

    public String authenticatedUpdated() {

        String authenticatedUpdated = "{\n" +
                "\"statusCode\": \"111\",\n" +
                "\"messageCode\": \"00170017\",\n" +
                "\"message\": \"Login successful\",\n" +
                "\"data\": {\n" +
                "\"auth_token\": \"<auth_token>\"\n" +
                "}\n" +
                "}";

        return authenticatedUpdated;
    }
}

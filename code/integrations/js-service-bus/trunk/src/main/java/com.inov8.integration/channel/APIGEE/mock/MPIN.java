package com.inov8.integration.channel.APIGEE.mock;

public class MPIN {
    public String ATMPinGeneration(){
        String str = "{\n" +
                "   \"message\":\"success\",\n" +
                "   \"apiResponse\":{\n" +
                "      \"MessageType\":\"DC\",\n" +
                "      \"RequestCode\":\"00\",\n" +
                "      \"TransmissionDateTime\":\"01022021\",\n" +
                "      \"STAN\":\"012356789\",\n" +
                "      \"ResponseCode\":\"00\"\n" +
                "   }\n" +
                "}\n";

        return str;
    }

    public String CardDetails(){
        String str = "{\n" +
                "   \"MTI\": \"0210\",\n" +
                "   \"ProcessingCode\": \"734000\",\n" +
                "   \"TransmissionDatetime\": \"0101120302\",\n" +
                "   \"SystemsTraceAuditNumber\": \"060000\",\n" +
                "   \"TimeLocalTransaction\": \"120302\",\n" +
                "   \"DateLocalTransaction\": \"0101\",\n" +
                "   \"MerchantType\": \"0069\",\n" +
                "   \"ResponseCode\": \"00\",\n" +
                "   \"CIF\": \"182424\",\n" +
                "   \"DebitCardNumber\": \"6037331771019912015\",\n" +
                "   \"MaskedCardNumber\": \"6037331771XXXXXXXXX\",\n" +
                "   \"CardStatus\": \"HOT\",\n" +
                "   \"CardStatusCode\": \"003\",\n" +
                "   \"AssociatedAccounts\": [{\"AccountNumber\": \"0000115943\"}]\n" +
                "}\n";
        return str;
    }
}

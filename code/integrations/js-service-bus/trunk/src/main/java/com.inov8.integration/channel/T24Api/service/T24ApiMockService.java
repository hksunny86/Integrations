package com.inov8.integration.channel.T24Api.service;

public class T24ApiMockService {

    public String ibftTitleFetch() {
        String cardReIssuance =

//                "{\"ISOMessage\": {\n" +
//                "\"MTI\": \"0210\",\n" +
//                "\"ProcessingCode_003\": \"400000\",\n" +
//                "\"AmountTransaction_004\": \"000000000100\",\n" +
//                "\"TransmissionDatetime_007\": \"0224213142\",\n" +
//                "\"SystemsTraceAuditNumber_011\": \"851916\",\n" +
//                "\"TimeLocalTransaction_012\": \"213142\",\n" +
//                "\"DateLocalTransaction_013\": \"0224\",\n" +
//                "\"MerchantType_018\": \"0099\",\n" +
//                "\"ResponseCode_039\": \"00\",\n" +
//                "\"AdditionalResponseData_044\": \"2000000000000000063784617\",\n" +
//                "\"CurrencyCodeTransaction_049\": \"586\",\n" +
//                "\"CurrencyCodeSettlement_050\": \"586\",\n" +
//                "\"AccountIdentification1_102\": \"0000102485\",\n" +
//                "\"AccountIdentification2_103\": \"0000102420\"\n" +
//                "}}";


             "{\n" +
                     "  \"ISOMessage\": {\n" +
                     "    \"MTI\": \"0210\",\n" +
                     "    \"PrimaryAccountNumber_002\": \"6110120070561625\",\n" +
                     "    \"ProcessingCode_003\": \"620000\",\n" +
                     "    \"TransmissionDatetime_007\": \"1103213142\",\n" +
                     "    \"SystemsTraceAuditNumber_011\": \"195559\",\n" +
                     "    \"TimeLocalTransaction_012\": \"003114\",\n" +
                     "    \"DateLocalTransaction_013\": \"1103\",\n" +
                     "    \"MerchantType_018\": \"6041\",\n" +
                     "    \"AcquiringInstitutionIdentificationCode_032\": \"008\",\n" +
                     "    \"UserId\": \"nasirmalik\",\n" +
                     "    \"CNIC\": \"6110120070561\",\n" +
                     "    \"ResponseCode_039\": \"000\",\n" +
                     "    \"AccountIdentification1_102\": \"24817000141103\",\n" +
                     "    \"ToAccountTitle\": \"TAIMOOR ALI 1-LINK\",\n" +
                     "    \"BranchName\": \"\"\n" +
                     "  }\n" +
                     "}";




            return cardReIssuance;
    }


    public String ibft() {
        String cardReIssuance =

                "{\"ISOMessage\": {\n" +
                "\"MTI\": \"0210\",\n" +
                "\"ProcessingCode_003\": \"400000\",\n" +
                "\"AmountTransaction_004\": \"000000000100\",\n" +
                "\"TransmissionDatetime_007\": \"0224213142\",\n" +
                "\"SystemsTraceAuditNumber_011\": \"851916\",\n" +
                "\"TimeLocalTransaction_012\": \"213142\",\n" +
                "\"DateLocalTransaction_013\": \"0224\",\n" +
                "\"MerchantType_018\": \"0099\",\n" +
                "\"ResponseCode_039\": \"00\",\n" +
                "\"AdditionalResponseData_044\": \"2000000000000000063784617\",\n" +
                "\"CurrencyCodeTransaction_049\": \"586\",\n" +
                "\"CurrencyCodeSettlement_050\": \"586\",\n" +
                "\"AccountIdentification1_102\": \"120211005\",\n" +
                "\"AccountIdentification2_103\": \"12312321321\"\n" +
                "}}";



        return cardReIssuance;
    }

    public String transactionValidation() {
        String cardReIssuance = "{\n" +
                "    \"ResponseCode\": \"404\",\n" +
                "    \"ResponseDescription\": \"Not Found\",\n" +
                "    \"PushNotification\": null\n" +
                "}\n";



        return cardReIssuance;
    }
}

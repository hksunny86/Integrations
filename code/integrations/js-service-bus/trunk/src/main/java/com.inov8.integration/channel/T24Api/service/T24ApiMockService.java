package com.inov8.integration.channel.T24Api.service;

public class T24ApiMockService {

    public String ibftTitleFetch() {
        String cardReIssuance ="{\n" +
                "    \"ISOMessage\": {\n" +
                "        \"MTI\": \"0210\",\n" +
                "        \"PrimaryAccountNumber_002\": \"6110120070561625\",\n" +
                "        \"ProcessingCode_003\": \"620000\",\n" +
                "        \"TransmissionDatetime_007\": \"1103213142\",\n" +
                "        \"SystemsTraceAuditNumber_011\": \"195559\",\n" +
                "        \"TimeLocalTransaction_012\": \"003114\",\n" +
                "        \"DateLocalTransaction_013\": \"1103\",\n" +
                "        \"MerchantType_018\": \"6041\",\n" +
                "        \"AcquiringInstitutionIdentificationCode_032\": \"008\",\n" +
                "        \"UserId\": \"nasirmalik\",\n" +
                "        \"CNIC\": \"6110120070561\",\n" +
                "        \"ResponseCode_039\": \"000\",\n" +
                "        \"AccountIdentification1_102\": \"24817000141103\",\n" +
                "        \"ToAccountTitle\": \"TAIMOOR ALI 1-LINK\",\n" +
                "        \"BranchName\": \"\",\n" +
                "        \"ReservedPrivate_120\": \"\"\n" +
                "    }\n" +
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
                "    \"ResponseCode\": \"00\",\n" +
                "    \"ResponseDescription\": \"Success\",\n" +
                "    \"PushNotification\": true\n" +
                "}\n";



        return cardReIssuance;
    }

    public String customerDeviceDetails(){
        return "{\n" +
                "    \"customers\": [\n" +
                "        {\n" +
                "            \"ID\": \"1\",\n" +
                "            \"MobileNo\": \"03324779796\",\n" +
                "            \"UniqueIdentifier\": \"123\",\n" +
                "            \"DeviceName\": \"Mobile\",\n" +
                "            \"RequestType\": \"Device Update\",\n" +
                "            \"ApprovalStatus\": \"ACTIVE\",\n" +
                "            \"Remarks\": \"A\",\n" +
                "            \"RequestedDate\": \"20230414181818\",\n" +
                "            \"RequestedTime\": \"20230414181818\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"ID\": \"2\",\n" +
                "            \"MobileNo\": \"03057683344\",\n" +
                "            \"UniqueIdentifier\": \"321\",\n" +
                "            \"DeviceName\": \"Web\",\n" +
                "            \"RequestType\": \"Unblock\",\n" +
                "            \"ApprovalStatus\": \"INACTIVE\",\n" +
                "            \"Remarks\": \"P\",\n" +
                "            \"RequestedDate\": \"20230414181818\",\n" +
                "            \"RequestedTime\": \"20230414181818\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"ResponseCode\": \"00\",\n" +
                "    \"ResponseDescription\": \"Success\"\n" +
                "}";
    }

    public String creditPayment(){
        return "{\n" +
                "   \"ISOMessage\":{\n" +
                "      \"MTI\":\"0210\",\n" +
                "      \"PrimaryAccountNumber_002\":\"115530091291213200\",\n" +
                "      \"ProcessingCode_003\":\"450000\",\n" +
                "      \"AmountTransaction_004\":\"000000100000\",\n" +
                "      \"TransmissionDatetime_007\":\"0912132115\",\n" +
                "      \"SystemsTraceAuditNumber_011\":\"530880\",\n" +
                "      \"TimeLocalTransaction_012\":\"132115\",\n" +
                "      \"DateLocalTransaction_013\":\"0912\",\n" +
                "      \"MerchantType_018\":\"0098\",\n" +
                "      \"ResponseCode_039\":\"00\",\n" +
                "      \"AdditionalResponseData_044\":\"0200000000000000001000000\",\n" +
                "      \"Track1Data_045\":\"000000000\",\n" +
                "      \"PersonalIdentificationNumberData_052\":\"586586\",\n" +
                "      \"OriginalDataElements_090\":\"         020011553009129121320000000000000\",\n" +
                "      \"ReplacementAmounts_095\":\"000000000\",\n" +
                "      \"AccountIdentification1_102\":\"         PKR145247020\",\n" +
                "      \"AccountIdentification2_103\":\"         PKR120211002\",\n" +
                "      \"TransactionDescription_104\":\"         RASTBLB-C.20240912132\",\n" +
                "      \"ReservedPrivate_120\":\"115530880SADAPKKA^Stacje^PK67SADA0000003912212226|\"\n" +
                "   }\n" +
                "}";
    }
}

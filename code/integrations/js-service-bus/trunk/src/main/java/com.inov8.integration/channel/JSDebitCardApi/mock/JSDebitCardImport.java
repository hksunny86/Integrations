package com.inov8.integration.channel.JSDebitCardApi.mock;

public class JSDebitCardImport {

    public String cardReIssuance() {
        String cardReIssuance = "{\n" +
                "   \"processingCode\": \"CardReIssuance\",\n" +
                "   \"merchantType\": \"0088\",\n" +
                "   \"traceNo\": \"785888\",\n" +
                "   \"dateTime\": \"20210105201527\",\n" +
                "   \"responseCode\": \"00\",\n" +
                "   \"data\": {\"CCIMessageResponse\":    {\n" +
                "      \"Header\":       {\n" +
                "         \"Message_Type\": \"0210\",\n" +
                "         \"Transaction_Code\": \"421\",\n" +
                "         \"Transmission_Date_Time\": \"20170427155000\",\n" +
                "         \"STAN\": \"785888\",\n" +
                "         \"UserId\": \"user\",\n" +
                "         \"Password\": \"password\"\n" +
                "      },\n" +
                "      \"Body\": {\"Response\": {\"ResponseCode\": \"02|Already Set for Reissuance\"}}\n" +
                "   }}\n" +
                "}\n";

        return cardReIssuance;
    }

    public String updateCardStatus() {
        String updateCardStatus = "{\n" +
                "    \"processingCode\": \"UpdateCardStatus\",\n" +
                "    \"merchantType\": \"0088\",\n" +
                "    \"traceNo\": \"500116\",\n" +
                "    \"dateTime\": \"20210105201527\",\n" +
                "    \"responseCode\": \"00|Success\",\n" +
                "    \"data\": {\n" +
                "        \"CCIMessageResponse\": {\n" +
                "            \"Header\": {\n" +
                "                \"Message_Type\": \"0210\",\n" +
                "                \"Transaction_Code\": \"407\",\n" +
                "                \"Transmission_Date_Time\": \"20170427155000\",\n" +
                "                \"STAN\": \"5313403\",\n" +
                "                \"UserId\": \"user1\",\n" +
                "                \"Password\": \"nmsXTYas9/AUsLtR5e1q1w==\"\n" +
                "            },\n" +
                "            \"Body\": {\n" +
                "                \"Response\": {\n" +
                "                    \"ResponseCode\": \"00|Success\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n";


        return updateCardStatus;
    }

    public String importCustomerResponse (){
        String importCustomer = "{\n" +
                "    \"processingCode\": \"ImportCustomer\",\n" +
                "    \"merchantType\": \"0088\",\n" +
                "    \"traceNo\": \"500116\",\n" +
                "    \"dateTime\": \"20210105201527\",\n" +
                "    \"responseCode\": \"00|Success\",\n" +
                "    \"data\": {\n" +
                "        \"CCIMessageResponse\": {\n" +
                "            \"Header\": {\n" +
                "                \"Message_Type\": \"0210\",\n" +
                "                \"Transaction_Code\": \"401\",\n" +
                "                \"Transmission_Date_Time\": \"20210325155022\",\n" +
                "                \"STAN\": \"531336\",\n" +
                "                \"UserId\": \"user\",\n" +
                "                \"Password\": \"password\"\n" +
                "            },\n" +
                "            \"Body\": {\n" +
                "                \"Response\": {\n" +
                "                    \"ResponseCode\": \"00|Success\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n";

        return importCustomer;
    }

    public String importAccountResponse(){
        String importAccount = "{\n" +
                "    \"processingCode\": \"ImportAccount\",\n" +
                "    \"merchantType\": \"0088\",\n" +
                "    \"traceNo\": \"500116\",\n" +
                "    \"dateTime\": \"20210105201527\",\n" +
                "    \"responseCode\": \"00|Success\",\n" +
                "    \"data\": {\n" +
                "        \"CCIMessageResponse\": {\n" +
                "            \"Header\": {\n" +
                "                \"Message_Type\": \"0210\",\n" +
                "                \"Transaction_Code\": \"402\",\n" +
                "                \"Transmission_Date_Time\": \"20210325155022\",\n" +
                "                \"STAN\": \"531353\",\n" +
                "                \"UserId\": \"user\",\n" +
                "                \"Password\": \"password\"\n" +
                "            },\n" +
                "            \"Body\": {\n" +
                "                \"Response\": {\n" +
                "                    \"ResponseCode\": \"00|Success\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n";

        return importAccount;
    }
    public String importCardResponse(){
        String importCard = "{\n" +
                "   \"processingCode\": \"ImportCard\",\n" +
                "   \"merchantType\": \"0088\",\n" +
                "   \"traceNo\": \"021499\",\n" +
                "   \"dateTime\": \"20210105201527\",\n" +
                "   \"responseCode\": \"00|Success\",\n" +
                "   \"data\": {\"CCIMessageResponse\":    {\n" +
                "      \"Header\":       {\n" +
                "         \"Message_Type\": \"0210\",\n" +
                "         \"Transaction_Code\": \"403\",\n" +
                "         \"Transmission_Date_Time\": \"20210325155022\",\n" +
                "         \"STAN\": \"021499\",\n" +
                "         \"UserId\": \"user\",\n" +
                "         \"Password\": \"password\"\n" +
                "      },\n" +
                "      \"Body\": {\"Response\": {\"ResponseCode\": \"00|Success\"}}\n" +
                "   }}\n" +
                "}\n";

        return importCard;
    }


    public String getCVV(){
        String importCard = "{\n" +
                "  \"processingCode\": \"GetCVV\",\n" +
                "  \"merchantType\": \"0088\",\n" +
                "  \"traceNo\": \"519667\",\n" +
                "  \"dateTime\": \"20220622113711\",\n" +
                "  \"responseCode\": \"00\",\n" +
                "  \"data\": {\n" +
                "    \"CCIMessageResponse\": {\n" +
                "      \"Header\": {\n" +
                "        \"Message_Type\": \"0210\",\n" +
                "        \"Transaction_Code\": \"406\",\n" +
                "        \"Transmission_Date_Time\": \"20220622113711\",\n" +
                "        \"STAN\": \"519667\",\n" +
                "        \"UserId\": \"user\",\n" +
                "        \"Password\": \"password\"\n" +
                "      },\n" +
                "      \"Body\": {\n" +
                "        \"Response\": {\n" +
                "          \"CardNumber\": \"5172960000323705\",\n" +
                "          \"CardTitle\": \"DANIYAL KAREEM\",\n" +
                "          \"Response_Code\": \"00|Success\",\n" +
                "          \"Expiry\": \"12/31/2025 2:02:58 PM\",\n" +
                "          \"CVV2\": \"095\",\n" +
                "          \"Issue_Date\": \"12/22/2020 2:02:58 PM\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        return importCard;
    }
}

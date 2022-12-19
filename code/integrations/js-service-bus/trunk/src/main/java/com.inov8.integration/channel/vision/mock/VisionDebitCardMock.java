package com.inov8.integration.channel.vision.mock;

public class VisionDebitCardMock {
    public String VisionMock(){
        String visionDebitCard = "{\n" +
                "    \"MTI\": \"0210\",\n" +
                "    \"ProcessingCode\": \"768000\",\n" +
                "    \"TransmissionDatetime\": \"0101120302\",\n" +
                "    \"SystemsTraceAuditNumber\": \"060100\",\n" +
                "    \"TimeLocalTransaction\": \"120302\",\n" +
                "    \"DateLocalTransaction\": \"0101\",\n" +
                "    \"MerchantType\": \"0069\",\n" +
                "    \"CNIC\": \"3520243953539\",\n" +
                "    \"CardDetails\": [\n" +
                "        {\n" +
                "            \"CardType\": \"BLB Wallet VISA Platinum\",\n" +
                "            \"CardNo\": \"4272970000435501\",\n" +
                "            \"CardTitle\": \"MUHAMMAD IBRAHIM\",\n" +
                "            \"AccountNumber\": \"03343118436\",\n" +
                "            \"CIF\": \"3530347668491\",\n" +
                "            \"CardStatus\": \"003\",\n" +
                "            \"CreationDate\": \"2019-08-02 12:58:51\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"CardType\": \"BLB PayPak Card\",\n" +
                "            \"CardNo\": \"2205770000034923\",\n" +
                "            \"CardTitle\": \"MUHAMMAD IBRAHIM\",\n" +
                "            \"AccountNumber\": \"03343118436\",\n" +
                "            \"CIF\": \"3530347668491\",\n" +
                "            \"CardStatus\": \"002\",\n" +
                "            \"CreationDate\": \"2020-01-18 01:42:02\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"ResponseCode\": \"00\",\n" +
                "    \"ResponseDescription\": \"SUCCESS\"\n" +
                "}";

        return  visionDebitCard;
    }
}

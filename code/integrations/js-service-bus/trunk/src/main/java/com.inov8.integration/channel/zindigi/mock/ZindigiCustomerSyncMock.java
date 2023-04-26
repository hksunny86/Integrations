package com.inov8.integration.channel.zindigi.mock;

public class ZindigiCustomerSyncMock {

    public String ZindigiCustomerSyncMock() {

        String zindigiCustomerSync = "{\n" +
                "  \"ResponseCode\":\"00\",\n" +
                "  \"Description\": \"Completed\"\n" +
                "}";

        return zindigiCustomerSync;

    }

    public String L2AccountUpgradeValidation() {
        String l2AccountUpgradeValidation = "{\n" +
                "    \"ResponseCode\": \"200\",\n" +
                "    \"ResponseDescription\": \"Objection\",\n" +
                "    \"PushNotification\": \"true\"\n" +
                "}\n";

        return l2AccountUpgradeValidation;
    }

    public String p2mStatusUpdate(){

     String p2mStatusUpdate = "{\n" +
            "  \"ResponseCode\":\"00\",\n" +
            "  \"ResponseDescription\": \"Completed\"\n" +
            "}";
        return p2mStatusUpdate;
}
}

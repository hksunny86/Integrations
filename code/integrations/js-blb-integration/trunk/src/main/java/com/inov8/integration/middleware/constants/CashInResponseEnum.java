package com.inov8.integration.middleware.constants;

public enum CashInResponseEnum implements EnumHelper {

    TRXID("transactionId", "//trans/trn/@TRXID"),
    CMOB("customerMobile", "//trans/trn/@CMOB"),
    CNIC("cnic", "//trans/trn/@CNIC"),
    DATE("transactionDateTime", "//trans/trn/@DATE"),
    TPAMF("transactionProcessingCharges", "//trans/trn/@TPAMF"),
    CAMTF("commissionAmount", "//trans/trn/@CAMTF"),
    TAMTF("totalAmount", "//trans/trn/@TAMTF"),
    BALF("remainingBalance", "//trans/trn/@BALF"),
    TXAMF("transactionAmount", "//trans/trn/@TXAMF");


    CashInResponseEnum(String name, String path) {
        this.name = name;
        this.path = path;
    }

    private String name;
    private String path;


    @Override
    public EnumHelper[] result() {
        return values();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public Class getListEnum() {
        return null;
    }

    @Override
    public Class getListClass() {
        return null;
    }
}


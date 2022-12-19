package com.inov8.integration.middleware.constants;

public enum BillPaymentResponseEnum implements EnumHelper {
    //id("id", "ADDRESS"),
    TXAM("transactionAmount", "//trans/trn/@TXAM"),
    TXAMF("transactionAmountFormatted", "//trans/trn/@TXAMF"),
    CAMT("commissionAmount", "//trans/trn/@CAMT"),
    CAMTF("commissionAmountFormatted", "//trans/trn/@CAMTF"),
    DAMTF("discountAmountFormatted", "//trans/trn/@DAMTF"),
    TPAM("transactionProcessingAmount", "//trans/trn/@TPAM"),
    TPAMF("transactionProcessingAmountFormatted", "//trans/trn/@TPAMF"),
    TAMT("totalAmount", "//trans/trn/@TAMT"),
    TAMTF("totalAmountFormatted", "//trans/trn/@TAMTF"),
    FACCNO("fromAccountNumber", "//trans/trn/@FACCNO"),
    FACCT("fromAccountTitle", "//trans/trn/@FACCT"),
    PROD("product", "//trans/trn/@PROD"),
    TRXID("transactionID", "//trans/trn/@TRXID"),
    RRN("retreivalReferenceNumer", "//trans/trn/@RRN"),
    DATEF("dateFormatted", "//trans/trn/@DATEF"),
    BALF("balaneFormatted", "//trans/trn/@BALF"),
    CONSUMER("consumer", "//trans/trn/@CONSUMER"),
    CONSUMERLABEL("consumerLabel", "//trans/trn/@CONSUMERLABEL");


    BillPaymentResponseEnum(String name, String path) {
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


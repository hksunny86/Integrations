package com.inov8.integration.enums;


public enum TransactionType {

    GET_SECRET_IDENTITY("GET_SECRET_IDENTITY"),
    VERIFICATION("VERIFICATION"),
    OTC_VERIFICATION("OTC_VERIFICATION"),
    LAST_VERIFICATION("LAST_VERIFICATION"),
    OTC_VERIFY_FINGER_PRINT("OTCVerifyFingerPrint"),
    SUBMIT_BANK_ACCOUNT_DETAIL("SubmitBankAccountDetail"),
    LAST_VERIFICATION_RESULT("LastVerificationResult"),
    SECRET_IDENTITY_DEMOGRAPHICS("SecretIdentityDemographics"),
    FINGER_PRINT_VERIFICATION("FingerPrintVerification"),
    OTC_IDENTITY_DEMOGRAPHICS_DATA("OTCIdentityDemographicsData"),
    OTC_SUBMIT_MANUAL_VERIFICATION("OTCSubmitManualVerification"),
    OTC_LAST_VERIFICATION_RESULT("OTCLastVerificationResult"),
    SUBMIT_MANUAL_VERIFICATION("SubmitManualVerification");

    private String value;

    TransactionType(String value){this.value=value;}

    public String getValue(){return this.value;}


}

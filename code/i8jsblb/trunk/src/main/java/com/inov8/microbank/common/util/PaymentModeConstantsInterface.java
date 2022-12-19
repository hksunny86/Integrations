package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;

public class PaymentModeConstantsInterface {
    public static final Long CREDIT_CARD = 1L;
    public static final Long DEBIT_CARD = 2L;
    public static final Long BRANCHLESS_BANKING_ACCOUNT = 3L;
    public static final Long HOME_REMMITTANCE_ACCOUNT = 7L;
    public static final Long CREDIT_REGISTER = 4L;
    public static final Long CASH = 5L;
    public static final Long CORE_BANKING_ACCOUNT = 6L;
    public static final Long HRA_BANKING_ACCOUNT = 7L;
    public static final Long INTERNET_BANKING_ACCOUNT = 8L;

    public static final String CREDIT_CARD_NAME = "Credit Card";
    public static final String DEBIT_CARD_NAME = "Debit Card";
    public static final String BRANCHLESS_BANKING_ACCOUNT_NAME = "BB Account";
    public static final String CREDIT_REGISTER_NAME = "Credit Register";
    public static final String CASH_NAME = "Cash";
    public static final String CORE_BANKING_ACCOUNT_NAME = "Core Banking Account";
    public static final String HRA_BANKING_ACCOUNT_NAME = "HRA Account";

    public static final Map<Long, String> paymentModeConstantsMap = new HashMap<Long, String>();

    static {
        paymentModeConstantsMap.put(BRANCHLESS_BANKING_ACCOUNT, BRANCHLESS_BANKING_ACCOUNT_NAME);
        paymentModeConstantsMap.put(CREDIT_CARD, CREDIT_CARD_NAME);
        paymentModeConstantsMap.put(DEBIT_CARD, DEBIT_CARD_NAME);
        paymentModeConstantsMap.put(BRANCHLESS_BANKING_ACCOUNT, BRANCHLESS_BANKING_ACCOUNT_NAME);
        paymentModeConstantsMap.put(CREDIT_REGISTER, CREDIT_REGISTER_NAME);
        paymentModeConstantsMap.put(CASH, CASH_NAME);
        paymentModeConstantsMap.put(CORE_BANKING_ACCOUNT, CORE_BANKING_ACCOUNT_NAME);
        paymentModeConstantsMap.put(HRA_BANKING_ACCOUNT, HRA_BANKING_ACCOUNT_NAME);

    }

}

package com.inov8.microbank.common.util;

public interface PoolAccountConstantsInterface {


    public static final Long ASKARI_INCOME_ACCOUNT_ID = 1L;
    public static final Long ZONG_INCOME_ACCOUNT_ID = 2L;
    public static final Long INOV8_INCOME_ACCOUNT_ID = 3L;
    //	public static final Long WHT_INCOME_ACCOUNT_ID = 4L;
//	public static final Long FED_INCOME_ACCOUNT_ID = 5L;
    public static final Long AGENT1_INCOME_ACCOUNT_ID = 6L;
    public static final Long AGENT2_INCOME_ACCOUNT_ID = 6L;
    public static final Long CREDIT_TRANSFER_SUNDRY_ACCOUNT_ID = 9L;
    public static final Long UNCLAIMED_C2C_SUNDARY_ACCOUNT = 10L;
    public static final Long RETAIL_PAYMENT_SUNDRY_ACCOUNT_ID = 15L;
    public static final Long TOPUP_POOL_MIRROR_ACCOUNT_ID = 16L;
    public static final Long CREDIT_CARD_POOL_ACCOUNT_ID = 17L;
    //	public static final Long INCL_CHARGES_SUNDRY_ACCOUNT_ID = 18L;
    public static final Long FED_OLA_ACCOUNT_ID = 19L;
    public static final Long WHT_OLA_ACCOUNT_ID = 20L;

//    Long BULK_DISBURSEMENT_SUNDRY_BLB = 169L;
    Long PARENT_GL_BLB_DEPOSITS = 101L;
    Long PARENT_GL_PAYABLE = 103L;
    Long PARENT_GL_EXPENSE = 105L;
    Long PARENT_GL_INCOME = 107L;
    Long PARENT_GL_CONTROL_AC = 109L;
    Long PARENT_GL_SUNDRY = 111L;
    Long PARENT_GL_SETTLEMENT = 113L;
    Long PARENT_GL_LIABILITY = 115L;
    Long PARENT_GL_RECEIVABLES = 117L;
    Long PARENT_GL_WHT_PAYABLES = 119L;


    public static final Long BANK_OLA_ACCOUNT_ID = 21L;
    public static final Long TELECOM_OLA_ACCOUNT_ID = 22L;
    public static final Long INOV8_OLA_ACCOUNT_ID = 23L;
    public static final Long CASH_WITHDRAWAL_SUNDRY_ACCOUNT_ID = 24L; // not used
    public static final Long BULK_DISBURSEMENT_POOL_ACCOUNT_OLA = 25L;
    public static final Long BULK_DISBURSEMENT_POOL_ACCOUNT_CORE = 26L;
    public static final Long SALES_TEAM_OLA_ACCOUNT_ID = 28L;
    public static final Long SALES_TEAM_CORE_ACCOUNT_ID = 29L;

    public static final Long UTILITY_BILL_POOL_T24_ACCOUNT_ID = 40L;
    public static final Long UTILITY_BILL_POOL_ACCOUNT_ID = 42L;
    public static final Long CUSTOMER_POOL_ACCOUNT_ID = 2500055L;
    public static final Long COMMISSION_RECON_ACCOUNT_ID = 2500261L;

    public static final Long ARMY_BULK_SALARY_DISBURSEMENT_ACCOUNT = 2505101L;
    public static final Long UTILITY_BILL_POOL_ACCOUNT_RECEIVEABLE_ID = 2505103L;
    public static final Long COMMISSION_RECON_MIRROR_ACCOUNT_ID = 2505104L; // not used
    public static final Long TOPUP_POOL_ID = 2505106L;
    public static final Long FRANCHISE1_INCOME_ACCOUNT_ID = 2505107L;
    public static final Long FRANCHISE2_INCOME_ACCOUNT_ID = 2505108L;
    public static final Long UTILITY_BILL_NADRA_POOL_ACCOUNT_ID = 2505110L;

    public static final Long DONATION_COLLECTION_SUNDRY_ACCOUNT_ID = 2505112L;
    public static final Long ACC_CLOSURE_SUNDRY_ACCOUNT_ID = 2505114L;
    public static final Long BULK_PAYMENT_IDP_POOL_ACCOUNT_ID = 2505118L;

    public static final Long TRANSFER_IN_POOL_ACCOUNT_ID = 2505122L;
    public static final Long TRANSFER_OUT_POOL_ACCOUNT_ID = 2505124L;
    public static final Long HIERARCHY_INCOME_ACCOUNT_ID = 2505126L;
    public static final Long AGENT_POOL_ACCOUNT_ID = 2505128L;
    public static final Long T24_SETTLEMENT_ACCOUNT_ID = 2505132L;


    public static final Long IBFT_POOL_OLA_ACCOUNT_ID = 57L;
    public static final Long IBFT_POOL_OF_ACCOUNT_ID = 58L;


    public static final Long DONATION_POOL_OLA_ACCOUNT_ID = 7000031165L;
    public static final Long DONATION_POOL_OF_ACCOUNT_ID = 7000031163L;
    public static final Long DONATION_POOL_T24_ACCOUNT_ID =  7000031167L;

    public static final Long CARD_PAYMENT_OLA_ACCOUNT_ID = 2507350L;
    public static final Long CARD_PAYMENT_OF_ACCOUNT_ID = 2507348L;


    // Used for ledger entries of walk-in customer in A2C and C2C transaction (Account.account_id = 10)
    public static final Long WALK_IN_SUNDRY_ACCOUNT_ID = 10L;

    // Used for to balance ledger entries in case of multiple credits (Account.account_id = 25)
    public static final Long LEDGER_SETTLEMENT_ACCOUNT_ID = 25L;
    public static final Long FUEL_DISBURSEMENT_ACCOUNT = 0000L;

    /********************************************************************************/

    public static final Long JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID = 36L;
    public static final Long JSBL_OF_FED_POOL_ACCOUNT_ID = 32L;
    public static final Long JSBL_OF_WHT_POOL_ACCOUNT_ID = 34L;

    public static final Long INWARD_FUND_TRANSFER_ACCOUNT_ID = 44L;
    public static final Long INWARD_FUND_TRANSFER_OLA_POOL_ACCOUNT_ID = 58L;

    /********************************************************************************/
//  Following accounts are no longer used- 45 & 46 are used instead 
//	public static final Long ACCOUNT_TO_CASH_SUNDRY_ACCOUNT_ID = 8L;
//	public static final Long CASH_TO_CASH_SUNDRY_ACCOUNT_ID = 11L; 
//	public static final Long CASH_TO_CASH_PHOENIX_SUNDRY_ACCOUNT_ID = 13L;
//	public static final Long BULK_PAYMENT_SUNDRY_OLA_ACCOUNT_ID = 14L;
//	public static final Long ACCOUNT_TO_CASH_CORE_SUNDRY_ACCOUNT_ID = 30L;
//	public static final Long BULK_PAYMENT_SUNDRY_PHOENIX_ID = 2505120L;

    // 45 and 46 are used in BLB to CNIC, CNIC to CNIC and Bulk Payment to CNIC
    public static final Long FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID = 45L;
    public static final Long FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID = 46L;
    public static final Long BANK_INCOME_CORE_ACCOUNT_ID = 47L;
    public static final Long BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA = 48L;
    public static final Long BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_CORE = 49L;
    public static final Long UNCLAIMED_C2C_SUNDARY_OLA_ACCOUNT = 50L;

    public static final Long AGENT_DEFAULT_COMMISSION_ACCOUNT_OLA = 51L;
    public static final Long AGENT_DEFAULT_COMMISSION_ACCOUNT_CORE = 52L;

    public static final Long OF_SETTLEMENT_UBP_POOL_ACCOUNT = 54L;
    public static final Long OF_SETTLEMENT_IFT_POOL_ACCOUNT = 55L;
    public static final Long OF_SETTLEMENT_BULK_DISBURSEMENT_POOL_ACCOUNT = 56L;

    public static final Long REDEMPTION_GL_ACCOUNT_CORE = 60L;
    public static final Long REDEMPTION_GL_ACCOUNT_OLA = 61L;

    public static final Long WHT_231A_GL_ACCOUNT_CORE = 62L;
    public static final Long WHT_231A_GL_ACCOUNT_OLA = 63L;

    public static final Long WHT_236P_GL_ACCOUNT_CORE = 64L;
    public static final Long WHT_236P_GL_ACCOUNT_OLA = 65L;

    public static final Long FONEPAY_SETTLEMENT_ACCOUNT_CORE = 66L;
    public static final Long FONEPAY_SETTLEMENT_ACCOUNT_OLA = 67L;
    public static final Long APIGEE_CASHIN_SETTLEMENT_ACCOUNT_CORE = 68L;
    public static final Long APIGEE_CASHIN_SETTLEMENT_ACCOUNT_OLA = 69L;
    public static final Long APIGEE_PAYMENT_SETTLEMENT_ACCOUNT_CORE = 7000031092L;
    public static final Long APIGEE_PAYMENT_SETTLEMENT_ACCOUNT_OLA = 7000031091L;
    //	public static final Long APIGEE_PAYMENT_SETTLEMENT_ACCOUNT_CORE = 72L;
//	public static final Long APIGEE_PAYMENT_SETTLEMENT_ACCOUNT_OLA = 73L;
    public static final Long COLLECTION_POOL_ACCOUNT_CORE = 70L;
    public static final Long COLLECTION_POOL_ACCOUNT_OLA = 71L;
    public static final Long THIRDPARTY_CASH_OUT_SETTLEMENT_ACCOUNT_CORE = 80L;
    public static final Long THIRDPARTY_CASH_OUT_ACCOUNT_OLA = 81L;

    public static final Long EOBI_CASH_OUT_SETTLEMENT_ACCOUNT_CORE = 82L;
    public static final Long EOBI_CASH_OUT_ACCOUNT_OLA = 83L;

    Long WHT_PAYABLE_SETT_AC = 34L;

    public static final Long DEBIT_CARD_CW_ON_US_POOL_ACCOUNT_GL = 93L;
    public static final Long DEBIT_CARD_CW_ON_US_POOL_ACCOUNT = 94L;
    public static final Long DEBIT_CARD_CW_OFF_US_POOL_ACCOUNT_GL = 84L;
    public static final Long DEBIT_CARD_CW_OFF_US_POOL_ACCOUNT = 86L;
    public static final Long POS_DEBIT_CARD_CW_POOL_ACCOUNT = 92L;
    public static final Long POS_DEBIT_CARD_CW_POOL_ACCOUNT_GL = 90L;

    public static final Long DEBIT_CARD_POS_REFUND_GL = 95L;
    public static final Long DEBIT_CARD_POS_REFUND_POOL_ACCOUNT = 96L;

    //Challan Collection Pool Accounts
    public static final Long CUSTOMER_ET_COLLECTION_ACCOUNT_GL = 107L;
    public static final Long CUSTOMER_ET_COLLECTION_POOL_ACCOUNT = 108L;

    public static final Long CUSTOMER_KP_COLLECTION_ACCOUNT_GL = 109L;
    public static final Long CUSTOMER_KP_COLLECTION_POOL_ACCOUNT = 110L;

    public static final Long LICENSE_FEE_COLLECTION_ACCOUNT_GL = 111L;
    public static final Long LICENSE_FEE_COLLECTION_POOL_ACCOUNT = 112L;

    public static final Long SARHAD_UNIVERSITY_COLLECTION_ACCOUNT_GL = 2507454L;
    public static final Long SARHAD_UNIVERSITY_COLLECTION_POOL_ACCOUNT = 2507456L;

    public static final Long AGENT_ET_COLLECTION_ACCOUNT_GL = 118L;
    public static final Long AGENT_ET_COLLECTION_POOL_ACCOUNT = 119L;

    public static final Long AGENT_KP_COLLECTION_ACCOUNT_GL = 120L;
    public static final Long AGENT_KP_COLLECTION_POOL_ACCOUNT = 121L;

    public static final Long AGENT_LICENSE_FEE_COLLECTION_ACCOUNT_GL = 122L;
    public static final Long AGENT_LICENSE_FEE_COLLECTION_POOL_ACCOUNT = 123L;

    public static final Long AGENT_BALCUHSITAN_ET_COLLECTION_ACCOUNT_GL = 2507277L;
    public static final Long AGENT_BALUCHISTAN_ET_COLLECTION_POOL_ACCOUNT = 2507278L;

    public static final Long AGENT_E_LEARNING_MANAGEMENT_SYSTEM_ACCOUNT_GL = 7000031065L;
    public static final Long AGENT_E_LEARNING_MANAGEMENT_SYSTEM_POOL_ACCOUNT = 7000031064L;

    public static final Long AGENT_CASH_DEPOSIT_ACCOUNT_GL = 130L;
    public static final Long AGENT_CASH_DEPOSIT_POOL_ACCOUNT = 131L;

    public static final Long DEBIT_CARD_ISSUANCE_FEE_GL = 97L;
    public static final Long DEBIT_CARD_ISSUANCE_FEE_POOL_ACCOUNT = 98L;

    public static final Long DEBIT_CARD_RE_ISSUANCE_FEE_GL = 99L;
    public static final Long DEBIT_CARD_RE_ISSUANCE_FEE_POOL_ACCOUNT = 102L;

    public static final Long DEBIT_CARD_ANNUAL_FEE_GL = 103L;
    public static final Long DEBIT_CARD_ANNUAL_FEE_POOL_ACCOUNT = 104L;

    public static final Long AGENT_EXCISE_AND_TAXATION_PAYMENT_ACCOUNT_GL = 127L;
    public static final Long AGENT_EXCISE_AND_TAXATION_PAYMENT_POOL_ACCOUNT = 128L;

    public static final Long AIR_TIME_TOP_UP_ACCOUNT_GL = 135L;
    public static final Long AIR_TIME_TOP_UP_POOL_ACCOUNT = 136L;

    Long BOP_CASH_OUT_GL = 137L;
    Long BOP_CASH_OUT_POOL_ACCOUNT = 138L;

    Long THIRD_PARTY_ACC_OPENING_GL = 139L;
    Long THIRD_PARTY_ACC_OPENING_BLB = 140L;

    Long BOP_CASH_OUT_COVID_19_GL = 141L;
    Long BOP_CASH_OUT_COVID_19_POOL_ACCOUNT = 142L;

    Long AGENT_IBFT_GL = 143L;
    Long AGENT_IBFT_POOL_ACCOUNT = 144L;

    Long ONE_LINK_CHARGES_GL = 145L;
    Long ONE_LINK_CHARGES_POOL_ACCOUNT = 146L;

    Long UTILITY_ONE_BILL_POOL_T24_ACCOUNT_ID = 148L;
    Long UTILITY_ONE_BILL_POOL_ACCOUNT_ID = 150L;
    Long UTILITY_ONE_BILL_BLB_POOL_ACCOUNT_ID = 149L;


    Long OFFLINE_BILLER_POOL_ACCOUNT_ID = 7000034437L;
    Long OFFLINE_BILLER_BLB_POOL_ACCOUNT_ID = 7000034438L;

    Long OFFLINE_BILLER_ASKARI_POOL_ACCOUNT_ID = 7000031212L;
    Long OFFLINE_BILLER_ASKARI_BLB_POOL_ACCOUNT_ID = 7000031213L;

    Long ADVANCE_LOAN_SALARY_GL = 154L;
    Long ADVANCE_LOAN_SALARY_BLB = 155L;

    Long BOOK_ME_GL = 160L;
    Long BOOK_ME_BLB = 159L;

    Long INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US_GL = 170L;
    Long INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US_BLB = 172L;

    Long International_POS_DEBIT_CARD_CASH_WITHDRAWAL_GL = 174L;
    Long International_POS_DEBIT_CARD_CASH_WITHDRAWAL_BLB = 176L;

    Long ADVANCE_SALARY_LOAN_GL = 2507445L;
    Long ADVANCE_SALARY_LOAN_BLB = 2507446L;

//    Long VIRTUAL_PREPAID_CARD_INTERNAL_BLB = 2505756L;
//    Long VIRTUAL_PREPAID_CARD_INTERNAL_GL = 2505754L;
//      Long VIRTUAL_PREPAID_CARD_INTERNAL_T24 = 2505758L;
//      Long VIRTUAL_PREPAID_CARD_SETTLEMENT = 2505760L;


//    on UAT
    Long VIRTUAL_PREPAID_CARD_INTERNAL_BLB = 2507530L;
    Long VIRTUAL_PREPAID_CARD_INTERNAL_GL = 2507528L;

    Long VIRTUAL_PREPAID_CARD_INTERNAL_T24 = 2507602L;
    Long VIRTUAL_PREPAID_CARD_SETTLEMENT = 2507604L;

    Long VIRTUAL_PREPAID_CARD_COLLECTION_BLB = 2507600L;
    Long VIRTUAL_PREPAID_CARD_COLLECTION_GL = 2507598L;

    Long BULK_DISBURSEMENT_SUNDRY_BLB = 2507302L;
    Long BULK_DISBURSEMENT_SUNDRY_OF = 2507009L;

//    end UAT

//    Long VIRTUAL_PREPAID_CARD_COLLECTION_BLB = 2505752L;
//    Long VIRTUAL_PREPAID_CARD_COLLECTION_GL = 2505750L;


//    for production
//    Long BULK_DISBURSEMENT_SUNDRY_BLB = 2507302L;


    Long ADVANCE_TAX_FILER_BLB = 2508002L;
    Long ADVANCE_TAX_FILER_OF = 2508000L;

    Long ADVANCE_TAX_NON_FILER_BLB = 2508006L;
    Long ADVANCE_TAX_NON_FILER_OF = 2508004L;

    Long ADVANCE_TAX_FILER_FROM_T24 = 2508008L;
    Long ADVANCE_TAX_FILER_TO_T24 = 2508010L;
    Long ADVANCE_TAX_NON_FILER_FROM_T24 = 2508012L;
    Long ADVANCE_TAX_NON_FILER_TO_T24 = 2508014L;

}

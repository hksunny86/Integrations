package com.inov8.microbank.common.util;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 12, 2013 2:33:58 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : Remember constants declared in interface are implicitly public static final. No need to mention.<br>
 */
public interface ProductConstantsInterface {
    Long AGENT_TO_AGENT_TRANSFER = 50013L;
    Long RSO_TO_AGENT_TRANSFER = 50014L;
    Long CASH_WITHDRAWAL = 50006L;
    Long AGENT_CASH_DEPOSIT = 10245142L;
    Long HRA_CASH_WITHDRAWAL = 10245106L;
    Long CASH_DEPOSIT = 50002L;
    Long ACT_TO_ACT = 50000L;
    Long BULK_DISBURSEMENT = 2510733L;
    Long ACCOUNT_TO_CASH = 50010L;
    Long CASH_TRANSFER = 50011L;
    //Long CUSTOMER_RETAIL_PAYMENT        = 50018L;
    Long CUSTOMER_CREDIT_CARD = 50024L;
    Long AGENT_CREDIT_CARD = 50025L;
    Long ZONG_TOPUP = 2510727L;
    Long APOTHECARE_PAYMENT = 2510793L;
    Long APOTHECARE = 2510791L;
    Long DAWAT_E_ISLAMI_ZAKAT_PAYMENT = 2510794L;
    Long DAWAT_E_ISLAMI_ZAKAT = 2510795L;
    Long DAWAT_E_ISLAMI_SADQA_PAYMENT = 2510796L;
    Long DAWAT_E_ISLAMI_SADQA = 2510797L;
    Long ML_TRANSFER_TO_RETAILER = 2510798L;
    Long ML_TRANSFER_TO_CUSTOMER = 2510800L;
    Long BULK_PAYMENT = 2510801L; //    Long BULK_PAYMENT = 2510816L;
    Long BB_TO_CORE_ACCOUNT = 50026L;
    Long CNIC_TO_CORE_ACCOUNT = 50028L;
    Long CNIC_TO_BB_ACCOUNT = 50030L;
    Long RETAIL_PAYMENT = 50031L;
    Long TRANSFER_IN = 50020L;
    Long TRANSFER_OUT = 50022L;
    Long TELLER_ACCOUNT_HOLDER_CASH_IN = 50033L;
    Long TELLER_WALK_IN_CASH_IN = 50034L;
    Long TELLER_CASH_OUT = 50035L;
    Long MANUAL_ADJUSTMENT = 50037L;
    Long RECEVIE_CASH = 50039L;
    Long WEB_SERVICE_PAYMENT = 50040L;
    Long VIRTUAL_CARD_PAYMENT = 50041L;
    Long FONEPAY_AGENT_PAYMENT = 50042L;
    Long APIGEE_PAYMENT = 50043L;
    Long WEB_SERVICE_CASH_IN = 50044L;
    Long ACT_TO_ACT_CI = 50050L;
    Long ACT_TO_CASH_CI = 50052L;
    Long VRG_CUSTOMER_CHALLAN_PAYMENT = 50055L;
    Long VRG_CHALLAN_PAYMENT = 50056L;
    Long EOBI_CASH_OUT = 2510986L;
    Long MOBILINK_PREPAID = 2510719L;
    Long MOBILINK_POSTPAID = 2510742L;
    Long ZONG_PREPAID = 2510744L;
    Long ZONG_POSTPAID = 2510745L;
    Long WARID_PREPAID = 2510715L;
    Long WARID_POSTPAID = 2510753L;
    Long TELENOR_PREPAID = 2510738L;
    Long TELENOR_POSTPAID = 2510749L;
    Long UFONE_PREPAID = 2510765L;
    Long UFONE_POSTPAID = 2510743L;
    Long PTCL_VFONE = 2510783L;
    Long PTCL_LANDLINE = 2510708L;
    Long PTCL_EVO_PREPAID = 2510747L;
    Long PTCL_EVO_POSTPAID = 2510748L;
    Long PTCL_DEFAULTER = 2510751L;
    Long QUEBEE_CONSUMER = 2510789L;
    Long QUEBEE_DISTRIBUTOR = 2510767L;
    Long WATEEN = 2510740L;
    Long WITRIBE = 2510741L;

    //Mobile Networks Products
    Long MOBILINK_PREPAID_AIRTIME = 10245146L;
    Long WARID_PREPAID_AIRTIME = 10245148L;
    Long UFONE_PREPAID_AIRTIME = 10245150L;
    Long TELENOR_PREPAID_AIRTIME = 10245152L;
    Long ZONG_PREPAID_AIRTIME = 10245154L;


    //Customer Initiated Products
    Long CUSTOMER_CASH_WITHDRAWAL = 50051L;
    Long CUSTOMER_BB_TO_CORE_ACCOUNT = 50053L;
    Long CUSTOMER_RETAIL_PAYMENT = 50054L;

    Long ACCOUNT_OPENING = 2510763L;
    Long IBFT = 2510802L;

    Long DEBIT_CARD_CASH_WITHDRAWAL_ON_US = 10245111L;
    Long DEBIT_CARD_CASH_WITHDRAWAL_OFF_US = 10245115L;
    Long INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US = 10245315L;
    Long POS_DEBIT_CARD_CASH_WITHDRAWAL = 10245117L;
    Long International_POS_DEBIT_CARD_CASH_WITHDRAWAL = 10245317L;
    Long POS_DEBIT_CARD_REFUND = 10245123L;
    Long HRA_TO_WALLET_TRANSACTION = 10245125L;
    //
    Long CUSTOMER_ET_COLLECTION = 10245130L;
    Long CUSTOMER_KP_CHALLAN_COLLECTION = 10245127L;
    Long CUSTOMER_BALUCHISTAN_ET_COLLECTION = 10245183L;
    Long CUSTOMER_VALLENCIA_COLLECTION=10245359L;
    Long AGENT_VALLENCIA_COLLECTION=10245361L;
    Long CUSTOMER_E_LEARNING_MANAGEMENT_SYSTEM = 10245214L;
    Long CUST_IHL_ISLAMABAD_CAMP = 10245229L;
    Long CUST_IHL_Gujranawala_CAMP = 10245231L;
    Long CUST_SARHAD_UNIVERSITY_COLLECTION = 10245263L;
    Long LICENSE_FEE_COLLECTION = 10245129L;

    Long AGENT_ET_COLLECTION = 10245133L;
    Long AGENT_KP_CHALLAN_COLLECTION = 10245131L;
    Long AGENT_KP_CHALLAN_COLLECTION_BY_ACCOUNT = 10245309L;
    Long AGENT_LICENSE_FEE_COLLECTION = 10245132L;
    Long AGENT_BALUCHISTAN_ET_COLLECTION = 10245171L;
    Long AGENT_E_LEARNING_MANAGEMENT_SYSTEM = 10245213L;
    Long IHL_ISLAMABAD_CAMP = 10245233L;
    Long IHL_Gujranawala_CAMP = 10245235L;
    Long SARHAD_UNIVERSITY_COLLECTION = 10245265L;
    Long DEBIT_CARD_ISSUANCE = 10245113L;
    Long CUSTOMER_DEBIT_CARD_ISSUANCE = 10245121L;
    Long DEBIT_CARD_RE_ISSUANCE = 10245136L;
    Long DEBIT_CARD_ANNUAL_FEE = 10245137L;
    Long AGENT_EXCISE_AND_TAXATION = 10245140L;

    Long BISP_CASH_OUT = 2510812L;
    Long BISP_CASH_OUT_WALLET = 10245144L;

    Long BOP_CASH_OUT = 10245156L;

    Long THIRD_PARTY_ACCOUNT_OPENING = 10245157L;

    Long BOP_CASH_OUT_COVID_19 = 10245159L;

    Long AGENT_BB_TO_IBFT = 10245158L;
    Long CUSTOMER_BB_TO_IBFT = 10245160L;
    Long BOP_CARD_ISSUANCE_REISSUANCE = 10245166L;
    Long ADVANCE_SALARY_LOAN = 10245168L;
    Long ADVANCE_SALARY_LOAN_ID = 10245258L;
    Long PROOF_OF_LIFE = 10245170L;

    String CASH_WITHDRAWAL_NAME = "Cash Withdrawal";
    String CASH_DEPOSIT_NAME = "Cash Deposit";
    String ACT_TO_ACT_NAME = "Account to Account";
    String ACCOUNT_TO_CASH_NAME = "Account to Cash";
    String CASH_TRANSFER_NAME = "Cash Transfer";
    String BULK_PAYMENT_NAME = "IDP Payment";
    String BB_TO_CORE_ACCOUNT_NAME = "BLB to Core Account";
    String CNIC_TO_CORE_ACCOUNT_NAME = "CNIC to Core Account";
    String MANUAL_ADJUSTMENT_NAME = "Manual Adjustment";
    String EOBI_CASH_OUT_ID = "2510986";

    //BookME Products

    Long EVENT = 10245181L;
    Long HOTEL = 10245179L;
    Long CINEMA = 10245177L;
    Long AIR = 10245175L;
    Long BUS_TICKETING = 10245173L;
    Long DEBIT_CARD_ACTIVATION = 10245185L;
    Long DEBIT_CARD_PIN_CHANGE = 10245187L;
    Long ATM_BALANCE_INQUIRY_OFF_US = 10245296L;
        Long INTERNATIONAL_BALANCE_INQUIRY_OFF_US=10245319L;
    Long CORE_TO_WALLET = 10245255L;
    Long CORE_TO_WALLET_MB = 10245256L;
    Long CASH_BACK = 10245305L;

    String ACCOUNT_OPENING_NAME = "Account Opening";
    Long CUST_ACCOUNT_OPENING = 111111L;
    Long PORTAL_ACCOUNT_OPENING = 222222L;
    Long STOCK_PURCHASED = 10245266L;
    Long STOCK_WITHDRAWAL = 10245266L;

    Long VC_TRANSFER_PRODUCT = 10245343L;
    Long RELIEF_FUND_PRODUCT = 10245355L;
    Long LOAN_XTRA_CASH = 10245400L;

}

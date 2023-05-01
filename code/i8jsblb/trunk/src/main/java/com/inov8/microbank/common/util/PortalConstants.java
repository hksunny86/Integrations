/**
 * 
 */
package com.inov8.microbank.common.util;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.model.ExampleConfigHolderModel;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Dec 28, 2006
 * Creation Time: 			3:39:37 PM
 * Description:				
 */
public class PortalConstants
{
	public static final String KEY_LOGO = "logo";
	public static final String BULK_DISBURSEMENTS_CREATE = "BULK_DISBURSEMENTS_C";
	public static final String KEY_ACTION_ID = "actionId";
	public static final String KEY_USECASE_ID = "usecaseId";
	public static final String KEY_TRANS_ID = "tId";
	public static final String KEY_TRANS_CODE = "tcode";
	public static final String KEY_ISSUE_COMMENTS = "comment";
	public static final String ISSUE_MFS_ID = "issueMfsId";
	public static final String KEY_MFS_ACCOUNT_ID = "mfsAccountId";	
	public static final String KEY_KYC_IAFN = "mfsKycInitialApplicationFormNo";
	public static final String KEY_APP_USER_ID = "appUserId";
	public static final String KEY_ACTION_AUTH_ID = "actionAuthorizationId";
	public static final String KEY_MFS_ID = "mfsId";
	public static final String KEY_PARTNER_ID = "mfsId"; //-- added by Haroon. 080114
	public static final String KEY_NOTIFY_BY_SMS = "notifyBySMS";
	public static final String KEY_CUSTOM_FIELD_1 = "customField1";
	public static final String KEY_CREATED_ON = "createdOn";
	public static final String KEY_APPUSER_USERNAME = "username";
	public static final String KEY_UPDATION_TIME = "updationTime";
	
	public static final String IBFT_STATUS_IN_PROGRESS = "Pushed to SAF";
	public static final String IBFT_STATUS_CLEAR_SAF = "DA-ISSUED";

	public static final String IBFT_STATUS_SUCCESS = "Successful";
	public static final String DEBIT_PAYMENT_STATUS_SUCCESS = "Successful";

	public static final String IBFT_STATUS_FAILED = "Failed";
	public static final String IBFT_ADVICE_TYPE = "IBFT";
	public static final String CORE_TO_WALLET_ADVICE_TYPE = "coreToWallet";
	public static final String CREDIT_PAYMENT_ADVICE_TYPE = "creditPayment";
	public static final String DEBIT_PAYMENT_ADVICE_TYPE = "debitPayment";


	public static final String SAF_STATUS_SUCCESSFUL = "Successful";
	public static final String SAF_STATUS_FAILED = "Failed";
	public static final String SAF_STATUS_INITIATED = "Initiated";
	
	// Black Listing Constants
	//************************************************************************
	public static final String KEY_REG_STATE_ID = "regStateId";
	public static final String KEY_CNIC_NO = "cnic";
	public static final String KEY_MOBILE_NO = "mobileNo";
	public static final String KEY_APP_USER_TYPE_ID = "appUserTypeId";
	public static final String KEY_USER_ID = "userId";
	
	//Blacklist Marking
	public static final String MNG_BLKLST_MRKNG_READ = "MNG_BLKLST_MRKNG_R";
	public static final String MNG_BLKLST_MRKNG_UPDATE = "MNG_BLKLST_MRKNG_U";

	//AccountAsSaving Marking
	public static final String MNG_ACC_SAVING_MRKNG_UPDATE = "MNG_ACC_SVNG_U";

	
	//************************************************************************
	
	public static final String SMS_URL = "smsURL";
	
	public static final Long WEB_TYPE_DEVICE = new Long(3);

	//
	public static final Long CREATE_AD_USECASE_ID = 1498L;
	//

	public static final Long ACTION_STATUS_START = new Long(1);
	public static final Long ACTION_STATUS_END = new Long(2);
	public static final Long ACTION_STATUS_APPROVED = new Long(3);
	public static final Long ACTION_STATUS_PENDING_APPROVAL = new Long(4);
	public static final Long ACTION_STATUS_APPROVAL_DENIED = new Long(5);
	public static final Long ACTION_STATUS_APPROVED_INTIMATED = new Long(6);
	public static final Long ACTION_STATUS_RESOLVED = new Long(7);

	
	public static final Long ACTION_CREATE = new Long(1);
	public static final Long ACTION_RETRIEVE = new Long(2);
	public static final Long ACTION_UPDATE = new Long(3);
	public static final Long ACTION_DELETE = new Long(4);
	public static final Long ACTION_DEFAULT = new Long(5);
	public static final Long ACTION_EXPORT_LOGS = new Long(6);
	public static final Long ACTION_UPDATE_ADMIN = new Long(7);
	
	public static final String NEW_MFS_ACCOUNT_MESSAGE = "Dear Customer! Your New Branchless Banking Account is created";
	
	public static final long LINK_PAYMENT_MODE_USECASE_ID = 1001;
	public static final long MFS_ACCOUNT_CREATE_USECASE_ID = 1002;
	public static final long PG_FORGOT_PIN_USECASE_ID = 1003;
	public static final long PG_FORGOT_PASSWORD_USECASE_ID = 1034;
	public static final long MNO_FORGOT_PIN_USECASE_ID = 1011;
	public static final long MNO_FORGOT_PASSWORD_USECASE_ID = 1033;
	public static final long MFS_ACCOUNT_UPDATE_USECASE_ID = 1004;
	public static final long DEACTIVATE_CUSTOMER_USECASE_ID = 1005;
	public static final long REACTIVATE_CUSTOMER_USECASE_ID = 1106;
	public static final long DEACTIVATE_AGENT_USECASE_ID = 1107;
	public static final long REACTIVATE_AGENT_USECASE_ID = 1108;
	public static final long FORGOT_VERIFLY_PIN_USECASE_ID = 1006;
	public static final long DELINK_PAYMENT_MODE_USECASE_ID = 1007;
	public static final long RELINK_PAYMENT_MODE_USECASE_ID = 1112;
	public static final long DELETE_PAYMENT_MODE_USECASE_ID = 1079;
	public static final long MNO_CHANGE_MODILE_NO_USECASE_ID = 1008;
	public static final long I8_USER_MANAGEMENT_CREATE_USECASE_ID = 1012;
	public static final long I8_USER_MANAGEMENT_UPDATE_USECASE_ID = 1013;
	public static final long I8_ESCALATE_FORM_USECASE_ID = 1014;
	public static final long MNO_VIEW_SALES_USECASE_ID = 1015;
	public static final long MFS_DEVICE_ACCOUNT_USECASE_ID = 1019;
	public static final long PG_ADMINISTRATION_USECASE_ID = 1020;
	public static final long CHANGE_ACCOUNT_NICK_USECASE_ID = 1021;
	public static final long PG_ADMIN_RESET_PASSWORD_USECASE_ID = 1022;
	public static final long ONE_TIME_PIN_REGENERATE_USECASE_ID = 1035;
	public static final long BLOCK_CUSTOMER_USECASE_ID = 1036;
	public static final long UNBLOCK_CUSTOMER_USECASE_ID = 1110;
	public static final long BLOCK_AGENT_USECASE_ID = 1111;
	public static final long UNBLOCK_AGENT_USECASE_ID = 1109;
	public static final long RESET_AGENT_PASSWORD_PORTAL_USECASE_ID = 1037;
	public static final long RESET_USER_PASSWORD_PORTAL_USECASE_ID = 1113;
	public static final long RESET_PASSWORD_WEB_USECASE_ID = 1038;
	public static final long USER_GROUP_CREATE_USECASE_ID = 1039;
	public static final long USER_GROUP_UPDATE_USECASE_ID = 1114;
	public static final long USER_GROUP_ACT_DEACT_USECASE_ID = 1040;
	public static final long RETAILER_FORM_USECASE_ID = 1041;
	public static final long RETAILER_FORM_UPDATE_USECASE_ID = 1105;
	public static final long RETAILER_ACT_DEACT_USECASE_ID = 1042;
	public static final long CHARGE_BACK_USECASE_ID = 1043;
	public static final long RESEND_SMS_USECASE_ID = 1044;
	public static final long OLA_SEARCH_ACCOUNT = 1030;
	public static final long OLA_CREATE_ACCOUNT = 1031;
	public static final long OLA_UPDATE_ACCOUNT = 1032;
	public static final long AREA_USECASE_ID = 1045;
	public static final long PRINT_FORM_USECASE_ID = 1046;
	public static final long REVERSE_FT_USECASE_ID = 1047;
	public static final long NO_RECTIFICATION_USECASE_ID = 1048;
	public static final long EXTERNALLY_RECTIFIED_USECASE_ID = 1049;
	//**********************************************************
	public static final long PRODUCT_UPDATE_USECASE_ID = 1065;
	public static final long PRODUCT_CREATE_USECASE_ID = 1229;
	public static final long MARK_BLACKLISTED_USECASE_ID =1256;
	public static final long UNMARK_BLACKLISTED_USECASE_ID=1258;
	public static final long DEACTIVATE_WEB_SERVICE_USECASE_ID=1264;
	public static final long ACTIVATE_WEB_SERVICE_USECASE_ID=1265;
	public static final long RESTORE_FROM_DORMANCY_USECASE_ID=1267;
	public static final long MARK_BLACKLISTED_BULK_USECASE_ID =1260;
	public static final long UNMARK_BLACKLISTED_BULK_USECASE_ID=1262;
	public static final long MARK_SAVING_AC_NATURE_BULK_USECASE_ID =1518;
	public static final long MARK_CURRENT_AC_NATURE_BULK_USECASE_ID=1520;
	public static final long MARK_SAVING_AC_NATURE_INDIVIDUAL_USECASE_ID =1522;
	public static final long MARK_CURRENT_AC_NATURE_INDIVIDUAL_USECASE_ID=1524;
	//**********************************************************
	
	public static final long CUSTOMER_SEGMENT_USECASE_ID = 1066;
	public static final long TX_CHARGES_USECASE_ID = 1067;
	public static final long LEVEL2_KYC_USECASE_ID = 1031; //TODO create usecase id for this
	public static final long LEVEL3_KYC_USECASE_ID = 1001; //TODO create usecase id for this
	public static final long LEVEL3_AGENT_MERCHANT_USECASE_ID = 1001; //TODO create usecase id for this
	//Agent Hierarchy
	public static final long AREALEVEL_USECASE_ID = 1062;
	public static final long AGENTLEVEL_USECASE_ID = 1061;
	public static final long REGION_USECASE_ID = 1060;
	public static final long AGENT_NETWORK_USECASE_ID = 1063;
	public static final long REGIONAL_HIERARCHY_USECASE_ID = 1100;
	public static final long CUST_ACT_TYPE_CREATE_UPDATE_USECASE_ID = 1064;
	public static final long COMM_SH_SHARES_CREATE_UPDATE_USECASE_ID = 1068;
	public static final long COMM_SH_SHARES_CREATE_DELETE_USECASE_ID = 1069;
	public static final long CUSTOMER_ACCOUNT_CLOSURE = 1070;
	public static final long CUSTOMER_ACCOUNT_SETTLEMENT = 1071;
	public static final long AGENT_ACCOUNT_CLOSURE = 1072;
	public static final long AGENT_ACCOUNT_SETTLEMENT = 1073;
	public static final long HANDLER_ACCOUNT_CLOSURE = 1116;
	public static final long HANDLER_ACCOUNT_SETTLEMENT = 1117;
	public static final long CREATE_COMPLAINT_NATURE_USECASE_ID = 1084;
	public static final long UPDATE_COMPLAINT_NATURE_USECASE_ID = 1085;
	public static final long CREATE_ALERT_RECIPIENT_USECASE_ID = 1086;
	public static final long UPDATE_ALERT_RECIPIENT_USECASE_ID = 1087;
	public static final long CREATE_ALERT_CONFIG_USECASE_ID = 1088;
	public static final long UPDATE_ALERT_CONFIG_USECASE_ID = 1089;
	public static final long UPDATE_CUSTOMER_MOBILE_USECASE_ID = 1091;
	public static final long UPDATE_AGENT_MOBILE_USECASE_ID = 1092;
	public static final long UPDATE_HANDLER_MOBILE_USECASE_ID = 1115;
	public static final long ONE_TIME_PIN_RESET_USECASE_ID=1095;
	public static final long UPDATE_USECASE=1099;
	public static final long UPDATE_BULK_DISBURSEMENT_USECASE_ID = 1093;
	public static final long DELETE_BULK_DISBURSEMENT_USECASE_ID = 1094;
	public static final long MANUAL_ADJUSTMENT_USECASE_ID = 1098;
	public static final long CREATE_VELOCITY_RULE_USECASE_ID = 1101;
	public static final long UPDATE_VELOCITY_RULE_USECASE_ID = 1102;
	public static final long CREATE_EXCLUDE_LIMIT_USECASE_ID = 1103;
	public static final long UPDATE_EXCLUDE_LIMIT_USECASE_ID = 1104;
	public static final long CREATE_AGENT_USECASE_ID = 1118;	
	public static final long UPDATE_AGENT_USECASE_ID = 1119;
	public static final long DEACTIVATE_HANDLER_USECASE_ID = 1200;
	public static final long REACTIVATE_HANDLER_USECASE_ID = 1201;
	public static final long BLOCK_HANDLER_USECASE_ID = 1202;
	public static final long UNBLOCK_HANDLER_USECASE_ID = 1203;
	public static final long CREATE_L2_USECASE_ID = 1206;
	public static final long UPDATE_L2_USECASE_ID = 1207;
	public static final long CREATE_L3_USECASE_ID = 1208;
	public static final long UPDATE_L3_USECASE_ID = 1209;
	public static final long UPDATE_P2P_DETAILS_USECASE_ID = 1210;
	
	public static final long UPDATE_CUSTOMER_ID_DOC_NO_USECASE_ID = 1211;
	public static final long UPDATE_AGENT_ID_DOC_NO_USECASE_ID = 1212;
	public static final long UPDATE_HANDLER_ID_DOC_NO_USECASE_ID = 1213;
	public static final long UPDATE_AGENT_TRANSFER_RULE_USECASE_ID = 1215;
	public static final long UPDATE_AGENT_GUOUP_TAGGING_USECASE_ID = 1217;
	public static final long CREATE_AGENT_GUOUP_TAGGING_USECASE_ID = 1219;
	public static final long TRANS_REDEMPTION_USECASE_ID = 1221;
	public static final long TRANSACTION_REVERSAL_USECASE_ID = 1227;

	public static final long BULK_MANUAL_ADJUSTMENT_USECASE_ID = 1228;
	public static final long BULK_AUTO_REVERSAL_USECASE_ID =1532;
	public static final long KEY_CREATE_UPDATE_TAX_REGIME_USECASE_ID=1252;
	public static final long KEY_CREATE_UPDATE_FED_RULE_MGMT_USECASE_ID=1253;
	public static final long KEY_UPDATE_CUST_LOGIN_PIN_USECASE_ID=1266;

	//Added by Sheheryaar for Action Log C.R
	public static final Long KEY_SEARCH_AGENT_USECASE_ID = 1501L;
	public static final Long KEY_SEARCH_P2P_TRX_USECASE_ID = 1506L;
	public static final Long KEY_SENDER_REDEEM_TRX_USECASE_ID = 1503L;
	public static final Long KEY_SENDER_REDEEM_REVERSAL_USECASE_ID = 1507L;
	public static final Long KEY_SEARCH_TRX_REDEEM_USECASE_ID = 1504L;
	public static final Long KEY_MY_AUTH_RQST_USECASE_ID = 1502L;
	public static final Long KEY_LIST_AUTH_RQST_USECASE_ID = 1505L;
	public static final Long KEY_SEARCH_REGENERATE_TRX_CODE_USECASE_ID = 1508L;
	public static final Long KEY_REGENERATE_TRX_CODE_USECASE_ID = 1509L;
	public static final Long KEY_AGENT_DETAIL_TRX_REPORT_USECASE_ID = 1510L;
	public static final Long KEY_BLB_ACCOUNTS_REPORT_USECASE_ID = 1511L;
	public static final Long KEY_DIGI_DORMANCY_ACCOUNTS_REPORT_USECASE_ID = 1517L;
	//
	public static final Long KEY_DEBIT_CARD_ISSUENCE_USECASE_ID = 1512L;
	public static final Long KEY_DEBIT_CARD_REISSUENCE_USECASE_ID = 1514L;

	//
	public static final Long KEY_BISP_REVERSAL_USECASE_ID = 1516L;

	//Scheduler App user ID
	public static final long SCHEDULER_APP_USER_ID = 3L;
	public static final long WEB_SERVICE_APP_USER_ID = 4L;

	public static final String REF_DATA_REQUEST_PARAM = "rType";

	public static final Long REF_DATA_SUPPLIER = 1L;
	public static final Long REF_DATA_SERVICE = 2L;
	public static final Long REF_DATA_OPERATOR = 1L; // like i8
	public static final Long REF_DATA_BANK = 6L;
	public static final Long REF_DATA_MNO = 7L;
	
	public static final String REF_DATA_REQUEST_PARAM_APP_USER_ROLE_ID = "appUserRoleId";
	
	// domain defination Use Case
	public static final long CUSTOMER_FORM_USECASE_ID = 1016;
	public static final long RETAILER_CONTACT_FORM_USECASE_ID = 1017;
	public static final long RETAILER_CONTACT_FORM_UPDATE_USECASE_ID = 1100;
	public static final long DISTRIBUTOR_CONTACT_FORM_USECASE_ID = 1018;
	
	public static final long CUTOMER_BALANCE_DECRYPTION_USECASE_ID=1083;

	//Bulk Disbursements
	public static final long MANAGE_BATCH_JOB_USECASE_ID=1234;
	public static final long MANAGE_DISBURSEMENT_BATCH_USECASE_ID = 1250;
	public static final long CANCEL_DISBURSEMENT_BATCH_USECASE_ID = 1251;
	
	public static final long WHT_EXEMPTION_USECASE_ID = 1254;
	public static final long WHT_CONFIG_USECASE_ID = 1263;

	public static final long RETAG_WALKIN_MOBILE_CNIC_USECASE_ID = 1240L;
	public static final long BLOCK_UNBLOCK_VIRTUAL_CARD_USECASE_ID = 1241L;
	public static final long UPDATE_ACCOUNT_TO_BLINK_USECASE_ID=1526L;

	public static final long UPDATE_CUSTOMER_NAME_USECASE_ID=1530L;

	public static final long CUSTOMER_DEBIT_BLOCK_USECASE_ID=1238;
	public static final long CUSTOMER_DEBIT_UNBLOCK_USECASE_ID=1237;

	public static final Long KEY_MFS_DEBIT_CARD_UPDATE_USECASE_ID = 1528L;

	public static final long MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID = 1534L;
	public static final long MFS_MERCHANT_ACCOUNT_UPDATE_USECASE_ID = 1536L;


	public static final String CONSTRAINT_VIOLATION_EXCEPTION = "ConstraintViolationException";
	
	//payment gateway 
	public static final String MNG_GEN_ACC_READ ="MNG_GEN_ACC_R";
	public static final String MNG_GEN_ACC_UPDATE ="MNG_GEN_ACC_U";
	public static final String MNG_GEN_ACC_DELETE ="MNG_GEN_ACC_D";
	public static final String MNG_GEN_ACC_CREATE ="MNG_GEN_ACC_C";
	
	public static final String LNK_PAY_MOD_READ ="LNK_PAY_MOD_R";
	public static final String LNK_PAY_MOD_UPDATE ="LNK_PAY_MOD_U";
	public static final String LNK_PAY_MOD_DELETE ="LNK_PAY_MOD_D";
	public static final String LNK_PAY_MOD_CREATE ="LNK_PAY_MOD_C";
	
	public static final String FRGT_GEN_PIN_READ ="FRGT_GEN_PIN_R";
	public static final String FRGT_GEN_PIN_UPDATE ="FRGT_GEN_PIN_U";
	
	public static final String FRGT_BNK_PIN_READ ="FRGT_BNK_PIN_R";
	public static final String FRGT_BNK_PIN_UPDATE ="FRGT_BNK_PIN_U";
	public static final String CHNG_BNK_PIN_U = "CHNG_BNK_PIN_U";
	
	public static final String MNG_ACC_NICK_UPDATE ="MNG_ACC_NICK_U";
	
	public static final String TX_RPT_READ ="TX_RPT_R";
	
	public static final String MNG_CHARGBACK_READ ="MNG_CHARGBACK_R";
	public static final String MNG_CHARGBACK_UPDATE ="MNG_CHARGBACK_U";
	
	public static final String ESC_TO_I8_RPT_READ ="ESC_TO_I8_RPT_R";
	public static final String ESC_TO_I8_RPT_UPDATE ="ESC_TO_I8_RPT_U";
	
	public static final String MNG_MY_CNCRN_READ ="MNG_MY_CNCRN_R";
	public static final String MNG_MY_CNCRN_UPDATE ="MNG_MY_CNCRN_U";
	public static final String MNG_MY_CNCRN_CREATE ="MNG_MY_CNCRN_C";

	public static final String ADM_USR_MGMT_READ ="ADM_USR_MGMT_R";
	public static final String ADM_USR_MGMT_UPDATE ="ADM_USR_MGMT_U";
	public static final String ADM_USR_MGMT_CREATE ="ADM_USR_MGMT_C";
	public static final String ADM_USR_MGMT_DELETE ="ADM_USR_MGMT_D";
	
	public static final String MNG_PRT_CNCRN_READ ="MNG_PRT_CNCRN_R";
	public static final String MNG_PRT_CNCRN_UPDATE ="MNG_PRT_CNCRN_U";	
	
	public static final String RSND_SMS_AG_TX_UPDATE ="RSND_SMS_AG_TX_U";
	
	public static final String EXP_RPT_PDF_XLS_READ ="EXP_RPT_PDF_XLS_R";
	public static final String RST_USR_PWD_UPDATE ="RST_USR_PWD_U";
	
	public static final String MNG_PRDCT_LMTS_READ ="MNG_PRDCT_LMTS_R";
	public static final String MNG_PRDCT_LMTS_UPDATE="MNG_PRDCT_LMTS_U";
	
	//Admin Operator
	public final static String ADMIN_GP_READ = "ADM_GP_R";
	public final static String ADMIN_GP_UPDATE = "ADM_GP_U";
	public final static String ADMIN_GP_DELETE = "ADM_GP_D";
	public final static String ADMIN_GP_CREATE = "ADM_GP_C";

	//CSR Operator
	public final static String CSR_GP_READ = "CSR_GP_R";
	public final static String CSR_GP_UPDATE = "CSR_GP_U";
	public final static String CSR_GP_DELETE = "CSR_GP_D";
	public final static String CSR_GP_CREATE = "CSR_GP_C";	
	//MNO
	public static final String MNO_GP_READ ="MNO_GP_R";
	public static final String MNO_GP_UPDATE ="MNO_GP_U";
	public static final String MNO_GP_DELETE ="MNO_GP_D";
	public static final String MNO_GP_CREATE ="MNO_GP_C";
	//Retailer
	public static final String RET_GP_READ ="RET_GP_R";
	public static final String RET_GP_UPDATE ="RET_GP_U";
	public static final String RET_GP_DELETE ="RET_GP_D";
	public static final String RET_GP_CREATE ="RET_GP_C";
	//Payment Services
	public static final String PAS_GP_READ ="PAS_GP_R";
	public static final String PAS_GP_UPDATE ="PAS_GP_U";
	public static final String PAS_GP_DELETE ="PAS_GP_D";
	public static final String PAS_GP_CREATE ="PAS_GP_C";
	//Product Supplier
	public static final String PRS_GP_READ ="PRS_GP_R";
	public static final String PRS_GP_UPDATE ="PRS_GP_U";
	public static final String PRS_GP_DELETE ="PRS_GP_D";
	public static final String PRS_GP_CREATE ="PRS_GP_C";
	
	//Product Supplier
	public static final String PG_GP_READ ="PG_GP_R";
	public static final String PG_GP_UPDATE ="PG_GP_U";
	public static final String PG_GP_DELETE ="PG_GP_D";
	public static final String PG_GP_CREATE ="PG_GP_C";

	public static final String ALLPAY_MNG_ACC_CREATE ="ALLPAY_MNG_ACC_C";
	public static final String ALLPAY_MNG_ACC_UPDATE ="ALLPAY_MNG_ACC_U";
	public static final String ALLPAY_MNG_ACC_DELETE ="ALLPAY_MNG_ACC_D";
	public static final String ALLPAY_MNG_ACC_READ ="ALLPAY_MNG_ACC_R";
	
	//Home Page Section
	public static final String HOME_PAGE_QUICK_SEARCH_CUST_READ ="QUICK_SEARCH_CUST_R";
	public static final String HOME_PAGE_QUICK_SEARCH_AGNT_READ ="QUICK_SEARCH_AGNT_R";
	public static final String HOME_PAGE_QUICK_SEARCH_HANDLER_READ ="QUICK_SEARCH_HANDLER_R";
	public static final String HOME_PAGE_QUICK_SEARCH_TX_READ ="QUICK_SEARCH_TX_R";
	public static final String HOME_PAGE_QUICK_SEARCH_CASH_PAYMENT_READ ="QUICK_SEARCH_CP_R";
	public static final String HOME_PAGE_QUICK_SEARCH_COMP_READ ="QUICK_SEARCH_COMP_R";
	public static final String HOME_PAGE_QUICK_SEARCH_COLL_P_READ ="QUICK_SEARCH_COLL_P_R";
	public static final String HOME_PAGE_QUICK_MY_CNCRN_READ ="QUICK_MY_CNCRN_R";
	public static final String HOME_PAGE_QUICK_AGNT_TX_HIST_READ ="QUICK_AGNT_TX_HIST_R";
	public static final String HOME_PAGE_VIEW_AGNT_WEB_APP_READ ="VIEW_AGNT_WEB_APP_R";
	public static final String HOME_PAGE_QUICK_BB_CUST_CREATE ="QUICK_BB_CUST_C";

	//Reports -> Transaction Reports
	public static final String RPT_TX_DETAIL_READ = "RPT_TX_DETAIL_R";
	public static final String RPT_VIEW_TX_READ = "RPT_VIEW_TX_R";
	public static final String RPT_CTR_READ = "RPT_CTR_R";
	public static final String RPT_CTR_REVERSE_READ = "RPT_CTR_REVERSE_R";
	public static final String RPT_CTR_REVERSE_UPDATE = "RPT_CTR_REVERSE_U";
	public static final String RPT_POSTED_TX_SEND_REV_UPDATE = "RPT_POSTED_TX_SEND_REV_U";
	public static final String RPT_POSTED_TX_NO_RTF_UPDATE = "RPT_POSTED_TX_NO_RTF_U";
	public static final String RPT_POSTED_TX_EXT_RES_UPDATE = "RPT_POSTED_TX_EXT_RES_U";
	public static final String RPT_SETTLEMENT_BB_STMT_READ = "RPT_SETTLEMENT_BB_STMT_R";
	
	//added by atif hussain
	public static final String RPT_RETRY_ADVICE_READ = "RPT_RETRY_ADVICE_R";
	public static final String RPT_RETRY_ADVICE_UPDATE = "RPT_RETRY_ADVICE_U";

	public static final String IBFT_RETRY_ADVICE_READ = "IBFT_RETRY_ADVICE_R";
	public static final String IBFT_RETRY_ADVICE_UPDATE = "IBFT_RETRY_ADVICE_U";

	//Reports -> Misc. Reports
	public static final String RPT_SRCH_WALK_IN_TX_HIST_READ = "RPT_SRCH_WALK_IN_TX_HIST_R";
	public static final String RPT_SRCH_WALK_IN_CP_READ = "RPT_SRCH_WALK_IN_CP_R";
	public static final String RPT_TX_CODE_HISTORY_READ="TX_CODE_HISTORY_R";

	//Administration Menu
	public static final String MNG_USR_GRPS_CREATE = "MNG_USR_GRPS_C";
	public static final String MNG_USR_GRPS_UPDATE  = "MNG_USR_GRPS_U";
	public static final String MNG_USRS_CREATE  = "MNG_USRS_C";
	public static final String MNG_USRS_UPDATE  = "MNG_USRS_U";
	public static final String MNG_ALERT_RECIPIENT_CREATE  = "MNG_ALERT_RECIPIENT_C";
	public static final String MNG_ALERT_RECIPIENT_UPDATE  = "MNG_ALERT_RECIPIENT_U";
	public static final String MNG_ALERT_CONFIG_CREATE  = "MNG_ALERT_CONFIG_C";
	public static final String MNG_ALERT_CONFIG_UPDATE  = "MNG_ALERT_CONFIG_U";

	//Configuration
	public static final String MNG_COMM_SH_ACC_CREATE = "MNG_COMM_SH_ACC_C";
	public static final String MNG_COMM_SH_ACC_UPDATE = "MNG_COMM_SH_ACC_U";
	
	public static final String MNG_SH_ACC_READ = "MNG_SH_ACC_R";


	//Update Account To Blink
	public static final String UPD_ACC_BLINK_CREATE = "Upd_Acc_To_Blink_C";
	public static final String UPD_ACC_BLINK_READ = "Upd_Acc_To_Blink_R";
	public static final String UPD_ACC_BLINK_UPDATE = "Upd_Acc_To_Blink_U";


	//Manage BB Customers
	public static final String MNG_BB_CUST_CREATE = "MNG_BB_CUST_C";
	public static final String MNG_BB_CUST_READ = "MNG_BB_CUST_R";
	public static final String MNG_BB_CUST_UPDATE = "MNG_BB_CUST_U";
	public static final String MNG_L2_CUST_CREATE = "MNG_L2_CUST_C";
	public static final String MNG_L2_CUST_READ = "MNG_L2_CUST_R";
	public static final String MNG_L2_CUST_UPDATE = "MNG_L2_CUST_U";
	public static final String BB_CUST_PRINT_FORM_READ = "BB_CUST_PRINT_FORM_R";
	public static final String BB_CUST_REPRINT_FORM_READ = "BB_CUST_REPRINT_FORM_R";
	public static final String BLOCK_BB_CUST_UPDATE = "BLOCK_BB_CUST_U";
	public static final String UNBLOCK_BB_CUST_UPDATE = "UNBLOCK_BB_CUST_U";
	public static final String DEACTIVATE_BB_CUST_UPDATE = "DEACTIVATE_BB_CUST_U";
	public static final String DEACTIVATE_WEB_SERVICE_BB_CUST_UPDATE = "DEACTIVATE_WEB_SERVICE_BB_CUST_U";
	public static final String ACTIVATE_WEB_SERVICE_BB_CUST_UPDATE = "ACTIVATE_WEB_SERVICE_BB_CUST_U";
	public static final String REACTIVATE_BB_CUST_UPDATE = "REACTIVATE_BB_CUST_U";
	public static final String REGENERATE_BB_CUST_PIN_UPDATE = "REGENERATE_BB_CUST_PIN_U";
	public static final String UPDATE_CUSTOMER_MOBILE = "UPDATE_CUSTOMER_MOBILE_U";
	public static final String UPDATE_CUSTOMER_CNIC = "UPDATE_CUSTOMER_CNIC_U";
	public static final String CUST_BB_STMT_READ = "CUST_BB_STMT_R";
	public static final String CUST_TX_HIST_READ = "CUST_TX_HIST_R";
	public static final String CUST_CP_REQ_READ = "CUST_CP_REQ_R";
	public static final String MNG_CUST_AC_TP_LMT_CREATE = "MNG_CUST_AC_TP_LMT_C";
	public static final String MNG_CUST_AC_TP_LMT_READ = "MNG_CUST_AC_TP_LMT_R";
	public static final String MNG_CUST_AC_TP_LMT_UPDATE = "MNG_CUST_AC_TP_LMT_U";
	public static final String SEND_BB_TX_REVERSAL = "SEND_BB_TX_REVERSAL_U";
	//Manage Commission Stakeholder Shares permissions
	public static final String MNG_COMM_SH_SHARE_CREATE = "MNG_COMM_SH_SHARE_C";
	public static final String MNG_COMM_SH_SHARE_READ = "MNG_COMM_SH_SHARE_R";
	public static final String MNG_COMM_SH_SHARE_UPDATE = "MNG_COMM_SH_SHARE_U";
	//Manage Ads
	public static final String MNG_AD_CREATE = "MNG_BB_CUST_C";
	public static final String MNG_AD_READ = "MNG_BB_CUST_C";
	public static final String MNG_AD_UPDATE = "MNG_BB_CUST_C";
	
	//Manage BB Agents
	public static final String MNG_AGNT_NET_CREATE = "MNG_AGNT_NET_C";
	public static final String MNG_AGNT_NET_UPDATE = "MNG_AGNT_NET_U";

	public static final String MNG_REG_HIER_CREATE = "MNG_REG_HIER_C";
	public static final String MNG_REG_HIER_UPDATE = "MNG_REG_HIER_U";
	
	public static final String MNG_SALES_HIER_CREATE = "MNG_SALES_HIER_C";
	public static final String MNG_SALES_HIER_UPDATE = "MNG_SALES_HIER_U";
	public static final String MNG_SALES_HIER_READ = "MNG_SALES_HIER_R";
	
	public static final String MNG_FRANCH_CREATE   = "MNG_FRANCH_C";
	public static final String MNG_FRANCH_UPDATE   = "MNG_FRANCH_U";

	public static final String MNG_L3_ACT_CREATE   = "MNG_L3_ACT_C";
	public static final String MNG_L3_ACT_UPDATE   = "MNG_L3_ACT_U";
	public static final String MNG_L3_ACT_READ   	= "MNG_L3_ACT_R";
	
	public static final String MNG_L3_AMDF_UPDATE   = "MNG_L3_AMDF_U";
	public static final String MNG_L3_AMDF_CREATE   = "MNG_L3_AMDF_C";
	public static final String MNG_L3_AMDF_READ   	= "MNG_L3_AMDF_R";
	
	public static final String MNG_L3_KYC_CREATE   = "MNG_L3_KYC_C";
	public static final String MNG_L3_KYC_UPDATE   = "MNG_L3_KYC_U";
	public static final String MNG_L3_KYC_READ   = "MNG_L3_KYC_R";
	

	public static final String MNG_AGNTS_CREATE = "MNG_AGNTS_C";
	public static final String MNG_AGNTS_READ = "MNG_AGNTS_R";
	public static final String MNG_AGNTS_UPDATE  = "MNG_AGNTS_U";

	public static final String AGNT_PRINT_FORM_READ = "AGNT_PRINT_FORM_R";

	public static final String BLOCK_AGNT_UPDATE    = "BLOCK_AGNT_U";
	public static final String UNBLOCK_AGNT_UPDATE  = "UNBLOCK_AGNT_U";
	public static final String DEACTIVATE_AGNT_UPDATE   = "DEACTIVATE_AGNT_U";
	public static final String REACTIVATE_AGNT_UPDATE   = "REACTIVATE_AGNT_U";
	public static final String REGENERATE_AGNT_PIN_UPDATE   = "REGENERATE_AGNT_PIN_U";
	public static final String REGENERATE_AGNT_LOGIN_PIN_UPDATE   = "REGENERATE_AGNT_LOGIN_PIN_U";
	public static final String REGENERATE_CUST_LOGIN_PIN   = "REGEN_CUST_PIN_U";
	public static final String UPDATE_AGENT_MOBILE   = "UPDATE_AGENT_MOBILE_U";
	public static final String UPDATE_AGENT_CNIC   = "UPDATE_AGENT_CNIC_U";
	public static final String RESET_AGNT_PASS_UPDATE  = "RESET_AGNT_PASS_U";
	public static final String AGNT_BB_STMT_READ    = "AGNT_BB_STMT_R";
	public static final String AGNT_TX_HIST_READ    = "AGNT_TX_HIST_R";
	
	public static final String MNG_IS_AGENT_WEB_ENABLE_UPDATE  = "MNG_IS_AGENT_WEB_ENABLE_U";
	// Agent's Handler
	public static final String MNG_HNDLR_CREATE = "MNG_HNDLR_C";
	public static final String MNG_HNDLR_READ = "MNG_HNDLR_R";
	public static final String MNG_HNDLR_UPDATE  = "MNG_HNDLR_U";
	
	
	public static final String HNDLR_PRINT_FORM_READ = "HNDLR_PRINT_FORM_R";
	public static final String BLOCK_HNDLR_UPDATE    = "BLOCK_HNDLR_U";
	public static final String UNBLOCK_HNDLR_UPDATE  = "UNBLOCK_HNDLR_U";
	public static final String DEACTIVATE_HNDLR_UPDATE   = "DEACTIVATE_HNDLR_U";
	public static final String REACTIVATE_HNDLR_UPDATE   = "REACTIVATE_HNDLR_U";
	public static final String REGENERATE_HNDLR_PIN_UPDATE   = "REGENERATE_HNDLR_PIN_U";
	public static final String REGENERATE_HNDLR_LOGIN_PIN_UPDATE   = "REGENERATE_HNDLR_LOGIN_PIN_U";
	public static final String UPDATE_HNDLR_MOBILE   = "UPDATE_HNDLR_MOBILE_U";
	public static final String UPDATE_HNDLR_CNIC   = "UPDATE_HNDLR_CNIC_U";
	public static final String RESET_HNDLR_PASS_UPDATE  = "RESET_HNDLR_PASS_U";
	public static final String HNDLR_BB_STMT_READ    = "HNDLR_BB_STMT_R";
	public static final String HNDLR_TX_HIST_READ    = "HNDLR_TX_HIST_R";
	public static final String MNG_HNDLR_ACC_CLOSURE_UPDATE = "MNG_HNDLR_ACC_CLOSURE_U";

	// Product Catalog
	public static final String MNG_PRDCT_CAT_CREATE = "MNG_PRDCT_CAT_C";
	public static final String MNG_PRDCT_CAT_READ = "MNG_PRDCT_CAT_R";
	public static final String MNG_PRDCT_CAT_UPDATE = "MNG_PRDCT_CAT_U";
	
	public static final String LINK_PAY_MOD_CREATE = "LINK_PAY_MOD_C";
	public static final String DE_LINK_PAY_MOD_UPDATE = "DE_LINK_PAY_MOD_U";
	public static final String RE_LINK_PAY_MOD_UPDATE = "RE_LINK_PAY_MOD_U";
	public static final String DELETE_PAY_MOD_DELETE = "DELETE_PAY_MOD_D";

	//Complaints
	public static final String UPDATE_COMPLAINT_UPDATE = "UPDATE_COMPLAINT_U";
	public static final String ADD_COMP_CREATE ="ADD_COMPLAINT_C";
	
	//Manage Complaint Nature
	public static final String UPDATE_COMPLAINT_NATURE = "MNG_COMPLAINT_NATURE_U";
	public static final String ADD_COMPLAINT_NATURE ="MNG_COMPLAINT_NATURE_C";

	//Manage Concerns
	public static final String CONCERNS_LIST_READ = "CONCERNS_LIST_R";
	public static final String CONCERNS_LIST_UPDATE = "CONCERNS_LIST_U";
	public static final String CONCERN_RULES_UPDATE = "CONCERN_RULES_U";
	public static final String CONCERN_CAT_DEF_CREATE = "CONCERN_CAT_DEF_C";
	public static final String CONCERN_CAT_DEF_UPDATE = "CONCERN_CAT_DEF_U";
	public static final String CONCERN_CAT_DEF_DELETE  = "CONCERN_CAT_DEF_D";

	//Manage Chargebacks
	public static final String VIEW_CHARGEBACKS_RIFC_UPDATE = "VIEW_CHARGEBACKS_RIFC_U";
	public static final String VIEW_CHARGEBACKS_RIFA_UPDATE = "VIEW_CHARGEBACKS_RIFA_U";

	//Misc
	public static final String EXPORT_XLS_READ = "EXPORT_XLS_R";
	public static final String EXPORT_XLSX_READ = "EXPORT_XLSX_R";
	public static final String EXPORT_PDF_READ = "EXPORT_PDF_R";
	public static final String EXPORT_CSV_READ = "EXPORT_CSV_R";
	public static final String REQ_CHARGEBACK_READ = "REQ_CHARGEBACK_R";
	public static final String REQ_CHARGEBACK_UPDATE = "REQ_CHARGEBACK_U";
	public static final String RESEND_SMS_UPDATE = "RESEND_SMS_U";
    public static final String REGENERATE_TX_CODE_UPDATE = "REGENERATE_TX_CODE_U";
    public static final String REGENERATE_TX_CODE_READ = "REGENERATE_TX_CODE_R";
    public static final String RESET_TX_CODE_UPDATE = "RESET_TX_CODE_U";


	//CRUD Operation Constants
	public static final String CRUD_ACTION_NAME ="crudActionName";
	public static final String CRUD_ACTION_CREATE ="C";
	public static final String CRUD_ACTION_READ ="R";
	public static final String CRUD_ACTION_UPDATE ="U";
	public static final String CRUD_ACTION_DELETE ="D";

	//ProductIntgModuleInfo
	public static final Long PRODUCT_INTG_MODULE_INFO_ID_NADRA = 33L;

	public static final Double MIN_DEBIT_CREDIT_LIMIT = 1.0;

	//Manage Customer Segments
	public static final String MNG_CUST_SEGMENT_READ = "MNG_CUST_SGMNT_R";
	public static final String MNG_CUST_SEGMENT_CREATE = "MNG_CUST_SGMNT_C";
	public static final String MNG_CUST_SEGMENT_UPDATE = "MNG_CUST_SGMNT_U";
	
	//Manage Transaction Charges
	public static final String MNG_TX_CHARGES_READ = "MNG_TX_CHARGES_R";
	public static final String MNG_TX_CHARGES_CREATE = "MNG_TX_CHARGES_C";
	public static final String MNG_TX_CHARGES_UPDATE = "MNG_TX_CHARGES_U";
	
	//Agent/Customer Account Closure
	
	public static final String MNG_CUST_ACC_CLOSURE_UPDATE = "MNG_CUST_ACC_CLOSURE_U";
	public static final String MNG_AGENT_ACC_CLOSURE_UPDATE = "MNG_AGENT_ACC_CLOSURE_U";
	// Usecase Management
	
	public static final String MNG_USECASE_READ= "MNG_USECASE_R";
	public static final String MNG_USECASE_UPDATE= "MNG_USECASE_U";
	// Authorization
	public static final String ACTION_AUTHORIZATION_READ= "ACTION_AUTH_R";
	public static final String MY_AUTHORIZATION_REQUESTS_READ= "MY_ACTION_AUTH_R";
	public static final String ACTION_AUTHORIZATION_UPDATE= "ACTION_AUTH_U";
		
	public static final String UPDATE_BULK_DISBURSEMENTS = "MNG_BULK_DISBURSEMENTS_U";
	public static final String DELETE_BULK_DISBURSEMENTS = "MNG_BULK_DISBURSEMENTS_D";
	
	// Manage Velocity Rules
	public static final String MNG_VELOCITY_RULES_CREATE= "MNG_VELOCITY_RULES_C";
	public static final String MNG_VELOCITY_RULES_UPDATE= "MNG_VELOCITY_RULES_U";
	public static final String MNG_VELOCITY_RULES_READ= "MNG_VELOCITY_RULES_R";


	///Manage Operating Hours Rules
	public static final String MNG_OPERATING_HOURS_RULES_CREATE= "MNG_OPERATING_HOURS_RULES_C";
	public static final String MNG_OPERATING_HOURS_RULES_UPDATE= "MNG_OPERATING_HOURS_RULES_U";
	public static final String MNG_OPERATING_HOURS_RULES_READ= "MNG_OPERATING_HOURS_RULES_R";

	// Manage Exclude Limits Rules
	public static final String MNG_EXCLUDE_LIMIT_RULES_CREATE= "MNG_EXCLD_LIMIT_RULES_C";
	public static final String MNG_EXCLUDE_LIMIT_RULES_UPDATE= "MNG_EXCLD_LIMIT_RULES_U";
	public static final String MNG_EXCLUDE_LIMIT_RULES_READ= "MNG_EXCLD_LIMIT_RULES_R";
	
	//manage TAX Regime
	public static final String MNG_TAX_REGIME_R= "MNG_TAX_REGIME_R";
	public static final String MNG_TAX_REGIME_C= "MNG_TAX_REGIME_C";
	public static final String MNG_TAX_REGIME_U= "MNG_TAX_REGIME_U";
	
	// Manage KYC
	public static final String MNG_L2_KYC_R= "MNG_L2_KYC_R";
	public static final String MNG_L2_KYC_C= "MNG_L2_KYC_C";
	public static final String MNG_L2_KYC_U= "MNG_L2_KYC_U";
	
	public static final String UPDATE_P2P_DETAILS=  "UPDATE_P2P_DETAILS_U";
	public static final String SENDER_REDEEM_U=  "SENDER_REDEEM_U";


	public static final String PREFIX_SETTLED_ACCOUNT = "_";

	public static final String ACC_OPENING_COMM_TRANSACTION_ID = "AccOpeningCommTransactionId";

	public static final ExampleConfigHolderModel EXACT_CONFIG_HOLDER_MODEL = new ExampleConfigHolderModel(false,false,false,MatchMode.EXACT);
	public static final String PERMS_EXPORT_XLS_READ;
	public static final String PERMS_EXPORT_XLSX_READ;
	public static final String PERMS_EXPORT_PDF_READ;
	public static final String PERMS_REQ_CHARGEBACK_READ;
	public static final String PERMS_EXPORT_CSV_READ; 
	public static final String PERMS_TRX_DETAIL_POPUP = ADMIN_GP_READ + ",TRX_DETAIL_POPUP_R";
	public static final String AGENT_GRP_U = "AGENT_GRP_U";
	public static final String AGENT_GRP_CREATE = "AGENT_GRP_C";
	public static final String TRANSACTION_REDEEM_UPDATE = "TRANSACTION_REDEEM_U";
	
	//public static final String BULK_DISBURSEMENTS_CREATE = "BULK_DISBURSEMENTS_C";
	
	
	
	//public static final String MNG_PRODUCT_CREATE = "MNG_PRODUCT_C";
	//public static final String MNG_PRODUCT_UPDATE = "MNG_PRODUCT_U";
	public static final String MNG_PRODUCT_CREATE = "MNG_PRDCT_LMTS_C";
	public static final String MNG_PRODUCT_UPDATE = "MNG_PRDCT_LMTS_U";
	
	//manage FED RULE MANAGEMENT
		public static final String MNG_FED_RULE_R= "MNG_FED_RULE_R";
		public static final String MNG_FED_RULE_C= "MNG_FED_RULE_C";
		public static final String MNG_FED_RULE_U= "MNG_FED_RULE_U";
		
		//WH Tax Config
		public static final String WHT_CNFG_UPDATE = "WHT_CNFG_U";
		public static final String WHT_CNFG_READ = "WHT_CNFG_R";
		public static final String SERCH_WHT_TRX_READ = "SERCH_WHT_TRX_R";
		public static final String SERCH_WHT_EXEMP_READ = "SERCH_WHT_EXEMP_R";
		
		//WHT Exemption
		public static final String MNG_WHT_CREATE = "MNG_WHT_EXEM_C";
		public static final String MNG_WHT_UPDATE = "MNG_WHT_EXEM_U";
		public static final String MNG_WHT_READ = "MNG_WHT_EXEM_R";

		//Manage Debit Block customer
		public static final String MNG_DEBIT_BLK_CUST_U = "MNG_DEBIT_BLK_CUST_U";

		//Bulk Disbursement
		public static final String BULK_SUM_VIEW_POPUP_READ= "BULK_SUM_VIEW_POPUP_R";
		public static final String BULK_SUM_VIEW_POPUP_CREATE= "BULK_SUM_VIEW_POPUP_C";
		public static final String BULK_SUM_VIEW_POPUP_UPDATE= "BULK_SUM_VIEW_POPUP_U";
		
		//Dormant state restore
		public static final String MNG_CUST_DORMANT_UPDATE = "MNG_CUST_DORMANT_U";
		public static final String MNG_RET_DORMANT_UPDATE = "MNG_RET_DORMANT_U";
		public static final String MNG_HDLR_DORMANT_UPDATE = "MNG_HDLR_DORMANT_U";

		public static final String RPT_PAY_MTNC_UPDATE = "RPT_PMTNC_REQ_U";

	public static final String MNG_PRDCT_LIMIT = "MNG_PRDCT_LMT";
	public static final String MNG_PRDCT_LMT_UPDATE = "MNG_PRDCT_LMT_U";
	public static final String MNG_PRDCT_LMT_DELETE = "MNG_PRDCT_LMT_D";
	public static final String MNG_PRDCT_LMT_READ = "MNG_PRDCT_LMT_R";

		public static final String MNG_PRDCT_CHARGES_READ = "MNG_PRDCT_CHRGS_R";
	    public static final String MNG_PRDCT_CHARGES_UPDATE = "MNG_PRDCT_CHRGS_U";
	    public static final String MNG_PRDCT_CHARGES_DELETE = "MNG_PRDCT_CHRGS_D";

		public static final String MNG_PRDCT_SHARES_DELETE = "MNG_PRDCT_SHRS_D";
		public static final String MNG_PRDCT_SHARES_UPDATE = "MNG_PRDCT_SHRS_U";
		public static final String MNG_PRDCT_SHARES_READ = "MNG_PRDCT_SHRS_R";

	//PG_GP_R,MNG_CARDS_FEE_R,MNG_CARDS_FEE_C,MNG_CARDS_FEE_U,MNG_CARDS_FEE_D

	public static final String MNG_CARD_FEE_READ = "MNG_CARDS_FEE_R";
	public static final String MNG_CARD_FEE_UPDATE = "MNG_CARDS_FEE_U";
	public static final String MNG_CARD_FEE_CREATE = "MNG_CARDS_FEE_C";
	public static final String MNG_CARD_FEE_DELETE = "MNG_CARDS_FEE_D";
	public static final String BISP_REVERSAL_RETRY_UPDATE = "BISP_RVRSL_RETRY_U";
		
		public static final Long CUSTOMER_DEFAULT_CATALOG = 5002L;
		public static final Long HRA_CUSTOMER_CATALOG = 5003L;
	
	public static final String IS_BVS_ENABLE = "BVSE";

	static
    {
        StringBuilder reqChargebackReadPermsBuilder = new StringBuilder( 100 );
        reqChargebackReadPermsBuilder.append( ADMIN_GP_READ ).append(',').append( PG_GP_READ )
        .append(',').append( MNO_GP_READ ).append(',').append( RET_GP_READ )
        .append(',').append( CSR_GP_READ ).append(',').append( REQ_CHARGEBACK_READ );
        PERMS_REQ_CHARGEBACK_READ = reqChargebackReadPermsBuilder.toString();

        StringBuilder globalPermsBuilder = new StringBuilder( 100 );
        globalPermsBuilder.append( ADMIN_GP_READ ).append(',').append( PG_GP_READ )
        .append(',').append( MNO_GP_READ ).append(',').append( RET_GP_READ )
        .append(',').append( CSR_GP_READ ).append(',').append( PRS_GP_READ );

        PERMS_EXPORT_XLS_READ = globalPermsBuilder.toString() + "," + EXPORT_XLS_READ;
        PERMS_EXPORT_XLSX_READ = globalPermsBuilder.toString() + "," + EXPORT_XLSX_READ;
        PERMS_EXPORT_PDF_READ = globalPermsBuilder.toString() + "," + EXPORT_PDF_READ;
        PERMS_EXPORT_CSV_READ = globalPermsBuilder.toString() + "," + EXPORT_CSV_READ;
    }
}

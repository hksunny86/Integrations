package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;

import com.inov8.microbank.webapp.action.portal.transactiondetaili8module.ReportTypeEnum;

public enum AuthoirzationDetailEnum
{
	LINK_PAYMENT_MODE_USECASE(PortalConstants.LINK_PAYMENT_MODE_USECASE_ID,"p-linkpaymentmodeauthorizationdetail"),
	BLOCK_CUSTOMER_USECASE(PortalConstants.BLOCK_CUSTOMER_USECASE_ID,"p-lockunlockaccountauthorizationdetail"),
	UNBLOCK_CUSTOMER_USECASE(PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID,"p-lockunlockaccountauthorizationdetail"),
	DEACTIVATE_CUSTOMER_USECASE(PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID,"p-lockunlockaccountauthorizationdetail"),
	REACTIVATE_CUSTOMER_USECASE(PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID,"p-lockunlockaccountauthorizationdetail"),
	REACTIVATE_AGENT_USECASE(PortalConstants.REACTIVATE_AGENT_USECASE_ID,"p-lockunlockaccountauthorizationdetail"),
	DEACTIVATE_AGENT_USECASE(PortalConstants.DEACTIVATE_AGENT_USECASE_ID,"p-lockunlockaccountauthorizationdetail"),
	BLOCK_AGENT_USECASE(PortalConstants.BLOCK_AGENT_USECASE_ID,"p-lockunlockaccountauthorizationdetail"),
	UNBLOCK_AGENT_USECASE(PortalConstants.UNBLOCK_AGENT_USECASE_ID,"p-lockunlockaccountauthorizationdetail"),
	RESET_AGENT_PASSWORD_PORTAL_USECASE(PortalConstants.RESET_AGENT_PASSWORD_PORTAL_USECASE_ID,"p-resetportalpasswordauthorizationdetail"),
	RESET_USER_PASSWORD_PORTAL_USECASE(PortalConstants.RESET_USER_PASSWORD_PORTAL_USECASE_ID,"p-resetuserportalpasswordauthorizationdetail"),
	RESEND_SMS_USECASE_ID(PortalConstants.RESEND_SMS_USECASE_ID,"p-resendsmsauthorizationdetail"),
	ONE_TIME_PIN_RESET_USECASE(PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID,"p_resettransactioncodeauthorizationDetail"),
	DELETE_PAYMENT_MODE_USECASE(PortalConstants.DELETE_PAYMENT_MODE_USECASE_ID,"p-allpaypaymentmodeauthorizationdetail"),
	DELINK_RELINK_PAYMENT_MODE_USECASE(PortalConstants.DELINK_PAYMENT_MODE_USECASE_ID,"p-allpaypaymentmodeauthorizationdetail"),
	RELINK_PAYMENT_MODE_USECASE(PortalConstants.RELINK_PAYMENT_MODE_USECASE_ID,"p-allpaypaymentmodeauthorizationdetail"),
	I8_USER_MANAGEMENT_CREATE_USECASE(PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID,"p-createnewuserauthorizationdetailfrom"),
	I8_USER_MANAGEMENT_UPDATE_USECASE(PortalConstants.I8_USER_MANAGEMENT_UPDATE_USECASE_ID,"p-updateuserauthorizationdetailform"),
	RETAILER_CONTACT_FORM_USECASE(PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID,"p-createagentauthorizationdetailform"),
	RETAILER_FORM_USECASE(PortalConstants.RETAILER_FORM_USECASE_ID,"p-createfranchiseauthorizationdetailform"),
	RETAILER_FORM_UPDATE_USECASE(PortalConstants.RETAILER_FORM_UPDATE_USECASE_ID,"p-updatefranchiseauthorizationdetailform"),
	MFS_ACCOUNT_CREATE_USECASE(PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID,"p-createcustomerauthorizationdetail"),
	MFS_ACCOUNT_UPDATE_USECASE(PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID,"p-updatecustomerauthorizationdetail"),
	KEY_MFS_DEBIT_CARD_UPDATE_USECASE_ID(PortalConstants.KEY_MFS_DEBIT_CARD_UPDATE_USECASE_ID,"p-updatedebitcardrequestauthorizationdetail"),
	USER_GROUP_CREATE_USECASE(PortalConstants.USER_GROUP_CREATE_USECASE_ID,"p-usergroupauthorizationdetail"),
	USER_GROUP_UPDATE_USECASE(PortalConstants.USER_GROUP_UPDATE_USECASE_ID,"p-usergroupauthorizationdetail"),
	MANUAL_ADJUSTMENT_USECASE(PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID,"p-manualadjustmentauthorizationdetail"),
	L2_CREATE_USECASE(PortalConstants.CREATE_L2_USECASE_ID,"p-createl2accountauthorizationdetail"),
	L2_UPDATE_USECASE(PortalConstants.UPDATE_L2_USECASE_ID,"p-updatel2accountauthorizationdetail"),
	L3_CREATE_USECASE(PortalConstants.CREATE_L3_USECASE_ID,"p-createl3accountauthorizationdetail"),
	L3_UPDATE_USECASE(PortalConstants.UPDATE_L3_USECASE_ID,"p-updatel3accountauthorizationdetail"),
	USECASE_MANAGEMENT_USECASE(PortalConstants.UPDATE_USECASE,"p-updateusecaseauthorizationdetail"),
	P2P_DETAILS_UPDATE_USECASE(PortalConstants.UPDATE_P2P_DETAILS_USECASE_ID,"p-updatep2ptxauthorizationdetail"),
	UPDATE_CUSTOMER_MOBILE_USECASE(PortalConstants.UPDATE_CUSTOMER_MOBILE_USECASE_ID, "p-updatemobileauthorizationdetail"),
	UPDATE_AGENT_MOBILE_USECASE(PortalConstants.UPDATE_AGENT_MOBILE_USECASE_ID, "p-updatemobileauthorizationdetail"),
	UPDATE_HANDLER_MOBILE_USECASE(PortalConstants.UPDATE_HANDLER_MOBILE_USECASE_ID, "p-updatemobileauthorizationdetail"),
	UPDATE_CUSTOMER_CNIC_USECASE(PortalConstants.UPDATE_CUSTOMER_ID_DOC_NO_USECASE_ID, "p-updatecnicauthorizationdetail.html"),
	UPDATE_AGENT_CNIC_USECASE(PortalConstants.UPDATE_AGENT_ID_DOC_NO_USECASE_ID, "p-updatecnicauthorizationdetail.html"),
	UPDATE_HANDLER_CNIC_USECASE(PortalConstants.UPDATE_HANDLER_ID_DOC_NO_USECASE_ID, "p-updatecnicauthorizationdetail.html"),
	AGENT_GROUP_TAGGING_UPDATE_USECASE(PortalConstants.UPDATE_AGENT_GUOUP_TAGGING_USECASE_ID, "p-agenttaggingauthorizationdetail.html"),
	AGENT_GROUP_TAGGING_CREATE_USECASE(PortalConstants.CREATE_AGENT_GUOUP_TAGGING_USECASE_ID, "p-agenttaggingauthorizationdetail.html");
	
	
    AuthoirzationDetailEnum(Long usecaseId, String url)
    {
        this.usecaseId = usecaseId;
        this.url = url;
    }

    private static final Map<Long,String> usecaseIdUrlMap;
    
	private Long usecaseId;

    private String url;

    public static String getUrlByUsecaseId(Long usecaseId)
    {
		return usecaseIdUrlMap.get(usecaseId);
	}

	public Long getUsecaseId() {
		return usecaseId;
	}
	public String getUrl() {
		return url;
	}

	static
	{
		AuthoirzationDetailEnum[] authoirzationDetailEnums = values();
		usecaseIdUrlMap = new HashMap<>(authoirzationDetailEnums.length);
		for (AuthoirzationDetailEnum authoirzationDetailEnum : authoirzationDetailEnums)
		{
			usecaseIdUrlMap.put(authoirzationDetailEnum.getUsecaseId(), authoirzationDetailEnum.getUrl()); 
		}
	}
}

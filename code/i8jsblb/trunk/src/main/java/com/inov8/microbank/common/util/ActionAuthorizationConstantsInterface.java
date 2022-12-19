package com.inov8.microbank.common.util;

public interface ActionAuthorizationConstantsInterface {

	public static final Long ACTION_STATUS_APPROVED = new Long(3);
	public static final Long ACTION_STATUS_PENDING_APPROVAL = new Long(4);
	public static final Long ACTION_STATUS_APPROVAL_DENIED = new Long(5);
	public static final Long ACTION_STATUS_APPROVED_INTIMATED = new Long(6);
	public static final Long ACTION_STATUS_RESOLVED = new Long(7);
	public static final Long ACTION_STATUS_CANCELLED = new Long(8);
	public static final Long ACTION_STATUS_ASIGNED_BACK = new Long(9);
	public static final Long ACTION_STATUS_RE_SUBMIT = new Long(10);
	
	// Authorization
	public static final String ACTION_AUTHORIZATION_READ = "ACTION_AUTH_R";
	public static final String ACTION_AUTHORIZATION_UPDATE = "ACTION_AUTH_U";
	public static final String KEY_ACTION_AUTH_ID = "actionAuthorizationId";
	public static final String KEY_NEW_AUTH_MODEL = "newAuthorizationModel";	
	public static final String KEY_AUTHORIZATION_MSG = "authorizationMsg";
	public static final String KEY_OLD_MODEL_STRING = "oldDataModelStr";
	public static final String KEY_INITIATOR_COMMENTS = "initiatorComments";
	public static final String KEY_METHODE_NAME = "methodeName";
	public static final String KEY_MODEL_CLASS = "modelClassName";
	public static final String KEY_MODEL_CLASS_QUALIFIED_NAME = "modelClassCompleteName";
	public static final String KEY_MANAGER_BEAN_NAME = "managerName";
	public static final String KEY_FORM_NAME = "formName";
	public static final String KEY_MODEL_STRING = "dataModelStr";
	public static final String KEY_REQ_REF_ID = "reqReferenceId";
	public static final String REF_DATA_EXCEPTION_MSG = "actionAuthorization.parseException";
	
}
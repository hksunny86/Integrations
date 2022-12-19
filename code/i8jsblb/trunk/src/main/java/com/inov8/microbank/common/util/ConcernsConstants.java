package com.inov8.microbank.common.util;

public interface ConcernsConstants {
	String ACTION_RAISE_CONCERN 		= "Raise Concern";
	String ACTION_RAISE_AGAIN		 	= "Raise Again";
	String ACTION_RESOLVE		 		= "Resolve";
	String ACTION_REPLY					= "Reply";
	String ACTION_SEND_TO_CREATOR		= "Send to Creator";
	String ACTION_SEND_TO_RESOLVER		= "Send to Resolving Partner";	
	String ACTION_CHOOSE_ACTION			= "Choose Action";

	String PAGE_MY_CONCERN				= "myConcern";
	String PAGE_LIST_CONCERN			= "listConcern";
	
	Long STATUS_OPEN = 1L;
	Long STATUS_INPROCESS = 2L;
	Long STATUS_REPLIED = 3L;
	Long STATUS_CLOSED = 4L;
	Long STATUS_VOID = 5L;
	
	String KEY_CONCERN_CODE = "concernCode";
	String KEY_PARTNER_ID = "partnerId";
	String KEY_RAISEDAGAIN_ACTION_REQUIRED = "raisedAgainActionRequired";
	String KEY_IS_CREATOR = "isCreator";
	String KEY_CONCERN_PAGE = "concernPage";
	
	String CLOSED_COMMENTS = "Thank you for your cooperation. The issue is resolved.";
	
	
}

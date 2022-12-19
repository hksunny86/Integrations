package com.inov8.microbank.server.service.portal.allpaymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface AllpayDelinkRelinkPaymentModeManager
{

	public static final String	KEY_IS_RELINK			= "isRelink";

	public static final String	KEY_APP_USER_ID			= "appUserId";

	public static final String	KEY_SMART_MONEY_ACC_ID	= "smartMoneyAccountId";

	public SearchBaseWrapper searchDelinkRelinkPaymentMode(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	public BaseWrapper updateDelinkRelinkVeriflyPin(BaseWrapper baseWrapper) throws FrameworkCheckedException, Exception;
	public BaseWrapper deleteAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException, Exception;
}

/**
 * 
 */
package com.inov8.microbank.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalBillInfo;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;

/**
 * @author imran.sarwar
 * Creation Time: Oct 5, 2006 9:02:18 AM
 */
public class MfsSupportFilter 
	implements Filter
{

	/** Logger for this class*/
	protected final Log logger = LogFactory.getLog(getClass());

	public void destroy()
	{
	}

	public void doFilter(ServletRequest sReq, ServletResponse res,
			FilterChain fChain) throws IOException, ServletException
	{
		try {
			HttpServletRequest req = (HttpServletRequest) sReq;

			AppUserModel appUserModel = (AppUserModel) req.getSession(true)
					.getAttribute(CommandFieldConstants.KEY_APP_USER);
			UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) req.getSession(true)
					.getAttribute(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNTS_MODEL);
			UtilityBillVO billInfo = (UtilityBillVO) req.getSession(true)
					.getAttribute(CommandFieldConstants.KEY_BILL_INFO);

			if (appUserModel != null) {
				ThreadLocalAppUser.setAppUserModel(appUserModel);
				if (userDeviceAccountsModel != null) {
					ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(userDeviceAccountsModel);
				}
				if (billInfo != null) {
					ThreadLocalBillInfo.setBillInfo(billInfo);
				}
			}
			// pass the request/response on
			fChain.doFilter(sReq, res);
		}
		catch (Exception e){
			logger.error("", e);
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void init(FilterConfig arg0) throws ServletException
	{
	}

}

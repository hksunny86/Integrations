package com.inov8.microbank.webapp.action.mfs.jme;

import com.inov8.framework.common.util.XMLUtil;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfs.MfsRequestHandler;
import com.inov8.microbank.mfs.jme.model.Message;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.inov8.microbank.common.util.CommandFieldConstants.REQ_PARAM_MSG;

public class J2meController
		implements Controller
{
	/** Logger for this class */
	protected final Log			logger			= LogFactory.getLog(getClass());

	private MfsRequestHandler	requestHandler	= null;

	private List ipList;

	public ModelAndView handleRequest(HttpServletRequest request,
									  HttpServletResponse response) throws Exception
	{
		Long sTime = DateTimeUtils.currentTimeMillis();
		MfsRequestWrapper sessionWrapper = (MfsRequestWrapper) request.getSession(false).getAttribute(MfsRequestWrapper.KEY_MFS_REQUEST_MAP);
		if(null == sessionWrapper)
		{
			if(logger.isDebugEnabled())
				logger.debug(">>>>>>>>> SessionWrapper is null, creating and storing in HttpSession");
			sessionWrapper = new MfsRequestWrapper();
			request.getSession(false).setAttribute(MfsRequestWrapper.KEY_MFS_REQUEST_MAP, sessionWrapper);
		}

/*		if(!this.isAccessAllowed(request))
		{
			// Here write an error message with a special code
			writeResponse(response, requestHandler.prepareErrorMessage(new Message(ErrorCodes.INVALID_MSG, ErrorLevel.HIGH, "Access to requested resource is not granted.")));
			return null;
		}
*/
		String xml = request.getParameter(REQ_PARAM_MSG);
		if(xml == null || "".equals(xml)){

			xml = request.getParameter(REQ_PARAM_MSG);
		}
		// if xml is null or empty then fall back to get the data from servlet strem directly
		if(xml == null || "".equals(xml))
		{
			logger.info("Request Parameter not found. Trying to read data from the servlet stream");
			int len = (int) request.getContentLength();
			ServletInputStream in = request.getInputStream();
			byte[] data;
			if (len != -1)
			{
				int total = 0;
				data = new byte[len];
				while (total < len)
				{
					total += in.read(data, total, len - total);
				}
			}
			else
			{
				ByteArrayOutputStream tmp = new ByteArrayOutputStream();
				int ch;
				while ((ch = in.read()) != -1)
				{
					tmp.write(ch);
				}
				data = tmp.toByteArray();
			}
			if(data.length==0)
			{
				// Here write an error message with a special code
				writeResponse(response, requestHandler.prepareErrorMessage(new Message(ErrorCodes.INVALID_MSG, ErrorLevel.HIGH, "")));
				return null;
			}
			else
			{
				//String tempXml = new String(data);
				String tempXml = new String(data, "UTF-8");
				xml = StringUtil.removeStringFromStart(tempXml, REQ_PARAM_MSG+"=");
			}
		}


		//Turab:Security:encrypt/decrypt request and response xml
		// Request Decryption
		logger.info("Encrypted XML Received From Mobile ::: "+ xml);
		xml = EncryptionUtil.decryptWithAES(XMLConstants.AES_HANDSHAKE_KEY,xml);
		logger.info("Plain XML Received After Decryption ::: "+ xml);
		if(logger.isDebugEnabled())
			logger.debug("Request: XML" + XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));

		try
		{
            if(request.getSession(true) != null && request.getSession(true).getAttribute(CommandFieldConstants.KEY_UDID) != null){
                sessionWrapper.getRequestInformation("DEMO_MAP").setUserDeviceId(
                        (String) request.getSession(true).getAttribute(CommandFieldConstants.KEY_UDID));
            }
			xml = requestHandler.handleRequest(xml, sessionWrapper);

			if(logger.isDebugEnabled())
				logger.debug("Response: XML" + XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogOutputXMLLocationSteps));


			// For login only when this call returns AppUserModel should be in
			// the ThreadLocal just put it on session
			UtilityBillVO billInfo = ThreadLocalBillInfo.getBillInfo();
			AppUserModel userModel = ThreadLocalAppUser.getAppUserModel();
			UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
			if (userModel != null)
			{
				HttpSession session = request.getSession(true);
				session.setAttribute(CommandFieldConstants.KEY_APP_USER, userModel);
				if(userDeviceAccountsModel != null)
				{
					session.setAttribute(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNTS_MODEL, userDeviceAccountsModel);
				}
				if(session.getAttribute(CommandFieldConstants.KEY_UDID) == null && sessionWrapper.getRequestInformation("DEMO_MAP") != null){
                    session.setAttribute(CommandFieldConstants.KEY_UDID,sessionWrapper.getRequestInformation("DEMO_MAP").getUserDeviceId());
				}
			}
			else
			{
				HttpSession session = request.getSession(false);
				if(session != null)
				{
					session.setAttribute(CommandFieldConstants.KEY_APP_USER, null);
					if(userDeviceAccountsModel == null)
					{
						session.setAttribute(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNTS_MODEL, null);
					}
					session.removeAttribute(CommandFieldConstants.KEY_UDID);
					session.invalidate();
				}

			}
			if(billInfo != null){
				HttpSession session = request.getSession(true);
				session.setAttribute(CommandFieldConstants.KEY_BILL_INFO, billInfo);
			}else{
				HttpSession session = request.getSession(false);
				if(session != null){
					session.setAttribute(CommandFieldConstants.KEY_BILL_INFO, null);
				}
			}

			//Turab:Security:encrypt/decrypt request and response xml
			//Response Encyption
			logger.info("Plain XML Going to encrypt to send Mobile ::: "+ xml);
			xml = EncryptionUtil.encryptWithAES(XMLConstants.AES_HANDSHAKE_KEY, xml);
			logger.info("Encrypted XML Sending To Mobile ::: "+ xml);

			writeResponse(response, xml);
		}
		finally
		{
			// we are done now remove the appUserModel from ThreadLocal
			ThreadLocalEncryptionType.remove();
			ThreadLocalAppUser.remove();
			ThreadLocalActionLog.remove();
			ThreadLocalUserDeviceAccounts.remove();
			ThreadLocalBillInfo.remove();
		}
		logger.info("Total Time taken by J2meController.handleRequest() :: " + String.valueOf(DateTimeUtils.currentTimeMillis() - sTime));
		return null;

		// Maqsood Shahzad - returning ModelandView which was null before as the response was being written on the stream
//		Map<String,String> resp = new HashMap<String,String>();
//		resp.put("response", xml);
//		return new ModelAndView("",resp);
	}

	protected boolean isAccessAllowed(HttpServletRequest request)
	{
		String ipAddress = request.getRemoteAddr();
		if(null != ipList)
		{
			if(ipList.contains(ipAddress))
			{
				return true;
			}

		}
		else
		{
			return true;
		}
		return false;
	}

	private void writeResponse(HttpServletResponse response,
							   String responseToWrite) throws IOException
	{
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseToWrite);
	}

	public MfsRequestHandler getRequestHandler()
	{
		return requestHandler;
	}

	public void setRequestHandler(MfsRequestHandler requestHandler)
	{
		this.requestHandler = requestHandler;
	}
	public void setIpList(List ipList)
	{
		this.ipList = ipList;
	}

}
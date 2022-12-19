/*
 * Usage rights pending...
 * 
 * 
 * 
 * 
 * 
 * 
 * ****************************************************************************
 */

package com.inov8.microbank.ws.server;

import io.task.context.Context;
import io.task.util.StringUtil;
import io.task.util.TaskConstant;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.util.IvrConstant;
import com.inov8.util.WsUtil;

/**<pre>
 * Created By : Ahmed Mobasher Khan
 * Creation Date : Aug 13, 2014
 * 
 * Purpose : 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments : 
 * </pre>
 */
@WebService(name="ivrwebservice",serviceName="ivrwebservice")
public class IvrWebServiceImpl implements IvrWebService
{
	protected static Logger logger = LoggerFactory.getLogger(IvrWebServiceImpl.class );

	private Context context;
	
	@WebMethod(exclude=true)
	public void setContext(Context context)
	{
		this.context = context;
	}
	
	@WebMethod(exclude=true)
	public Context getContext()
	{
		return context;
	}
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.IvrWebService#handleRequest(com.inov8.microbank.server.IVRRequestDTO)
	 */
	@Override
	@WebMethod(operationName="handleRequest")
	public String initPinRequest(@WebParam(name="ivrRequestDTO")IVRRequestDTO ivrRequestDTO)
	{
		logger.info("Recevied request: {}", ivrRequestDTO.toString());
		Map<String, Object> dataMap = new HashMap<String, Object>();

		Map<String,String> wsParamMap = new HashMap<String, String>();
		
		wsParamMap.put(IvrConstant.AGENT_ID, StringUtil.setNullToEmpty(ivrRequestDTO.getAgentId()));
		wsParamMap.put(IvrConstant.AGENT_MOBILE, StringUtil.setNullToEmpty(ivrRequestDTO.getAgentMobileNo()));
		wsParamMap.put(IvrConstant.HANDLER_MOBILE, StringUtil.setNullToEmpty(ivrRequestDTO.getHandlerMobileNo()));
		wsParamMap.put(IvrConstant.CUSTOMER_MOBILE, StringUtil.setNullToEmpty(ivrRequestDTO.getCustomerMobileNo()));
		wsParamMap.put(IvrConstant.PIN, StringUtil.setNullToEmpty(ivrRequestDTO.getPin()));
		wsParamMap.put(IvrConstant.TX_ID, StringUtil.setNullToEmpty(ivrRequestDTO.getTransactionId()));
		wsParamMap.put(IvrConstant.TX_AMOUNT, StringUtil.getNullOrString(ivrRequestDTO.getAmount()));
		wsParamMap.put(IvrConstant.TX_CHARGES, StringUtil.getNullOrString(ivrRequestDTO.getCharges()));
		wsParamMap.put(IvrConstant.ARE_CREDENTIALS_EXPIRED, StringUtil.getNullOrString(ivrRequestDTO.getIsCredentialsExpired()));
		wsParamMap.put(IvrConstant.IS_PIN_AUTHENTICATED, StringUtil.getNullOrString(ivrRequestDTO.getIsPinAuthenticated()));
		wsParamMap.put(IvrConstant.PRODUCT_ID, StringUtil.getNullOrString(ivrRequestDTO.getProductId()));
//		wsParamMap.put(IvrConstant.RETRY_COUNT, StringUtil.getNullOrString(ivrRequestDTO.getRetryCount()));
		wsParamMap.put(IvrConstant.RECIPIENT_CNIC, StringUtil.setNullToEmpty(ivrRequestDTO.getRecipientCnic()));
		wsParamMap.put(IvrConstant.RECIPIENT_MOBILE, StringUtil.setNullToEmpty(ivrRequestDTO.getRecipientMobile()));
		wsParamMap.put(IvrConstant.BANK_ACC_NUM, StringUtil.setNullToEmpty(ivrRequestDTO.getCoreAccountNo()));
		wsParamMap.put(IvrConstant.CONSUMER_NUMBER, StringUtil.setNullToEmpty(ivrRequestDTO.getConsumerNo()));
		wsParamMap.put(IvrConstant.NFC_TAG_NO, StringUtil.setNullToEmpty(ivrRequestDTO.getNfcTagNo()));
		wsParamMap.put(IvrConstant.NFC_TAG_IDENTIFIER, StringUtil.setNullToEmpty(ivrRequestDTO.getNfcTagIdentifier()));
		wsParamMap.put(IvrConstant.IVR_ID, StringUtil.setNullToEmpty(ivrRequestDTO.getIvrId()));
		
		dataMap.put(IvrConstant.WS_PARAMS, wsParamMap);
		dataMap.putAll(wsParamMap);
//			dataMap.put(IvrConstant.LANG_ID, "ur");

		if(WsUtil.validate(dataMap, getContext()))
		{
			String ivrId = ivrRequestDTO.getIvrId();
			if(StringUtil.isNullOrEmpty(ivrId)) {
				dataMap.put(TaskConstant.NEXT_TASK_ID, wsParamMap.get(IvrConstant.PRODUCT_ID));
			} else {
				dataMap.put(TaskConstant.NEXT_TASK_ID, ivrId.toLowerCase() + wsParamMap.get(IvrConstant.PRODUCT_ID));
			}
			getContext().startFlow(dataMap);
		}

		logger.info("Response for request: {} is: {}",ivrRequestDTO,(String) dataMap.get(TaskConstant.TASK_STATUS));

		return (String) dataMap.get(TaskConstant.TASK_STATUS);
	}

}

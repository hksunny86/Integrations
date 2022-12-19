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

package com.inov8.microbank.ws.service;

import io.task.util.StringUtil;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.util.EncryptionUtil;
import com.inov8.util.IvrConstant;
import com.inov8.util.StatusCodeConstant;

/**<pre>
 * Created By : Ahmed Mobasher Khan
 * Creation Date : Aug 14, 2014
 * 
 * Purpose : 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments : 
 * </pre>
 */
public class WsClient
{
	private static Logger logger = LoggerFactory.getLogger(WsClient.class);
	private IVRRequestHandlerDelegate ivrRequestHandlerDelegate;
	
	public void setIvrRequestHandlerDelegate(IVRRequestHandlerDelegate iVRRequestHandlerDelegate)
	{
		this.ivrRequestHandlerDelegate = iVRRequestHandlerDelegate;
	}
	
	public void verfiyPin(Map<String, Object> dataMap)
	{
		IvrResponseDTO resp = new IvrResponseDTO();
		resp.setAgentId((String) dataMap.get(IvrConstant.AGENT_ID));
		resp.setAgentMobileNo((String) dataMap.get(IvrConstant.AGENT_MOBILE));
		resp.setHandlerMobileNo((String) dataMap.get(IvrConstant.HANDLER_MOBILE));
		resp.setAmount(dataMap.get(IvrConstant.TX_AMOUNT) == null ? null : Double.parseDouble((String) dataMap.get(IvrConstant.TX_AMOUNT)));
		resp.setCharges(dataMap.get(IvrConstant.TX_CHARGES) == null ? null : Double.parseDouble((String) dataMap.get(IvrConstant.TX_CHARGES)));
		resp.setCustomerMobileNo((String) dataMap.get(IvrConstant.CUSTOMER_MOBILE));
		resp.setIsCredentialsExpired(dataMap.get(IvrConstant.ARE_CREDENTIALS_EXPIRED) == null ? null : Boolean.parseBoolean((String) dataMap.get(IvrConstant.ARE_CREDENTIALS_EXPIRED)));
		resp.setIsPinAuthenticated(dataMap.get(IvrConstant.IS_PIN_AUTHENTICATED) == null ? null : Boolean.parseBoolean((String) dataMap.get(IvrConstant.IS_PIN_AUTHENTICATED)));
		resp.setPin((String) dataMap.get(IvrConstant.PIN));
		resp.setProductId(dataMap.get(IvrConstant.PRODUCT_ID) == null ? null : Long.parseLong((String) dataMap.get(IvrConstant.PRODUCT_ID)));
		resp.setRetryCount(dataMap.get(IvrConstant.RETRY_COUNT) == null ? 0 : Integer.parseInt((String) dataMap.get(IvrConstant.RETRY_COUNT)));
		resp.setNewPin((String) dataMap.get(IvrConstant.NEW_PIN));
		resp.setTransactionId((String) dataMap.get(IvrConstant.TX_ID));
		resp.setResponseCode((String) dataMap.get(IvrConstant.RESPONSE_CODE));
		resp.setNfcTagNo((String) dataMap.get(IvrConstant.NFC_TAG_NO));
		resp.setNfcTagIdentifier((String) dataMap.get(IvrConstant.NFC_TAG_IDENTIFIER));
	
		if(logger.isInfoEnabled())
			logger.info("Sending request for Pin verification: {}",resp.toString());
		
//		IvrRequestDTO ivrRequestDTO = new IVRRequestHandlerService().getIVRRequestHandlerPort().handleIVRRequest(resp);
		IvrRequestDTO ivrRequestDTO = ivrRequestHandlerDelegate.handleIVRRequest(resp);
		
		if(logger.isInfoEnabled())
			logger.info("Received response: {}",ivrRequestDTO.toString());
		
//		dataMap.put(IvrConstant.AGENT_ID, StringUtil.setNullToEmpty(ivrRequestDTO.getAgentId()));
//		dataMap.put(IvrConstant.AGENT_MOBILE, StringUtil.setNullToEmpty(ivrRequestDTO.getAgentMobileNo()));
//		dataMap.put(IvrConstant.CUSTOMER_MOBILE, StringUtil.setNullToEmpty(ivrRequestDTO.getCustomerMobileNo()));
//		dataMap.put(IvrConstant.PIN, StringUtil.setNullToEmpty(ivrRequestDTO.getPin()));
//		dataMap.put(IvrConstant.TX_ID, StringUtil.setNullToEmpty(ivrRequestDTO.getTransactionId()));
//		dataMap.put(IvrConstant.TX_AMOUNT, StringUtil.getNullOrString(ivrRequestDTO.getAmount()));
//		dataMap.put(IvrConstant.TX_CHARGES, StringUtil.getNullOrString(ivrRequestDTO.getCharges()));
		dataMap.put(IvrConstant.ARE_CREDENTIALS_EXPIRED, StringUtil.getNullOrString(ivrRequestDTO.isIsCredentialsExpired()));
		dataMap.put(IvrConstant.IS_PIN_AUTHENTICATED, StringUtil.getNullOrString(ivrRequestDTO.isIsPinAuthenticated()));
//		dataMap.put(IvrConstant.PRODUCT_ID, StringUtil.getNullOrString(ivrRequestDTO.getProductId()));
		dataMap.put(IvrConstant.RETRY_COUNT, StringUtil.getNullOrString(ivrRequestDTO.getRetryCount()));
		dataMap.put(IvrConstant.TX_RESPONSE, StringUtil.setNullToEmpty(ivrRequestDTO.getTransactionResponse()));

	}
	
	public void processKeyInOtp(Map<String, Object> dataMap)
	{
		Integer retry = (Integer) dataMap.get(IvrConstant.RETRY_COUNT);
		if(retry == null) {
			retry = 0;
		}
		dataMap.put(IvrConstant.RETRY_COUNT, retry);

		IvrKeyInRequest ivrKeyInRequest = new IvrKeyInRequest();

		ivrKeyInRequest.setCommandId(IvrConstant.COMMAND_ID_144);
		ivrKeyInRequest.setCallId((String) dataMap.get(IvrConstant.UNIQUE_ID));
		ivrKeyInRequest.setCustomerData(EncryptionUtil.encryptWithAES(IvrConstant.AES_ENCRYPTION_KEY, (String) dataMap.get(IvrConstant.CUSTOMER_DATA)));
		ivrKeyInRequest.setDeviceTypeId(IvrConstant.DEVICE_TYPE_ID);
		ivrKeyInRequest.setEncryptionType("1");
		ivrKeyInRequest.setTerminalMobileNo((String) dataMap.get(IvrConstant.CALLER));
		ivrKeyInRequest.setTxAmount(Integer.parseInt( (String) dataMap.get(IvrConstant.TX_AMOUNT)));
		
		logger.info("Sending request: {}",ivrKeyInRequest);
		IvrKeyInResponse ivrKeyInResponse = ivrRequestHandlerDelegate.processKeyIn(ivrKeyInRequest);
		logger.info("Received response: {}",ivrKeyInResponse);
		
		dataMap.put(IvrConstant.CUSTOMER_DATA, ivrKeyInResponse.getCardNo());
		dataMap.put(IvrConstant.CUSTOMER_MOBILE, ivrKeyInResponse.getCustomerMobileNo());
		dataMap.put(IvrConstant.TX_RESPONSE, ivrKeyInResponse.getErrorCode());
		dataMap.put(IvrConstant.TX_ID, ivrKeyInResponse.getTxCode());
		dataMap.put(IvrConstant.TX_CODE, ivrKeyInResponse.getTxCodeId());
		
		if(ivrKeyInResponse.getErrorCode().equals(StatusCodeConstant.CODE_0000) == false) {
			retry = retry + 1;
			if(retry == 2)
				dataMap.put(IvrConstant.TX_RESPONSE, StatusCodeConstant.CODE_9001);
		}
	}	

	public void processKeyIn(Map<String, Object> dataMap)
	{
		IvrKeyInRequest ivrKeyInRequest = new IvrKeyInRequest();
		Integer retry = (Integer) dataMap.get(IvrConstant.RETRY_COUNT);
		if(retry == null) {
			retry = 0;
		}

		dataMap.put(IvrConstant.RETRY_COUNT, retry);
		
		ivrKeyInRequest.setCommandId(IvrConstant.COMMAND_ID_145);
		ivrKeyInRequest.setCallId((String) dataMap.get(IvrConstant.UNIQUE_ID));
		ivrKeyInRequest.setCustomerData((String) dataMap.get(IvrConstant.CUSTOMER_DATA));
		ivrKeyInRequest.setCustomerMobileNo((String) dataMap.get(IvrConstant.CUSTOMER_MOBILE));
		ivrKeyInRequest.setDeviceTypeId(IvrConstant.DEVICE_TYPE_ID);
		ivrKeyInRequest.setEncryptionType("1");
		ivrKeyInRequest.setOtpPin(EncryptionUtil.encryptWithAES(IvrConstant.AES_ENCRYPTION_KEY, (String) dataMap.get(IvrConstant.PIN)));
		ivrKeyInRequest.setTerminalMobileNo((String) dataMap.get(IvrConstant.CALLER));
		ivrKeyInRequest.setTxAmount(Integer.parseInt( (String) dataMap.get(IvrConstant.TX_AMOUNT)));
		ivrKeyInRequest.setTxId((String) dataMap.get(IvrConstant.TX_ID));
		ivrKeyInRequest.setTxCode((Long) dataMap.get(IvrConstant.TX_CODE));
		
		logger.info("Sending request: {}",ivrKeyInRequest);
		IvrKeyInResponse ivrKeyInResponse = ivrRequestHandlerDelegate.processKeyIn(ivrKeyInRequest);
		logger.info("Received response: {}",ivrKeyInResponse);

		dataMap.put(IvrConstant.APPR_CODE, ivrKeyInResponse.getApprCode());
		dataMap.put(IvrConstant.BATCH_NO, ivrKeyInResponse.getBatchNo());
		dataMap.put(IvrConstant.TX_RESPONSE, ivrKeyInResponse.getErrorCode());

		String errCode = ivrKeyInResponse.getErrorCode();
		if(errCode.equals(StatusCodeConstant.CODE_0000) == false && errCode.equals(StatusCodeConstant.CODE_9015) == false) {
			retry = retry + 1;
			if(retry == 2)
				dataMap.put(IvrConstant.TX_RESPONSE, StatusCodeConstant.CODE_9001);
		}
	}	
}

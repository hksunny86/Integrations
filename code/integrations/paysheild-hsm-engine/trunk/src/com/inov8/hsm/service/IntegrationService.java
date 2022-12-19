package com.inov8.hsm.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inov8.hsm.client.HSMClient;
import com.inov8.hsm.controller.NetworkInfoBean;
import com.inov8.hsm.dto.PayShieldDTO;
import com.inov8.hsm.pdu.BasePDU;
import com.inov8.hsm.pdu.request.DecryptEncryptedPINRequest;
import com.inov8.hsm.pdu.request.EncryptClearPINRequest;
import com.inov8.hsm.pdu.request.GeneratePINBlockRequest;
import com.inov8.hsm.pdu.request.GenerateRandomPINRequest;
import com.inov8.hsm.pdu.request.GenerateVisaPVVRequest;
import com.inov8.hsm.pdu.request.ValidatePINBlockWithPVVRequest;
import com.inov8.hsm.pdu.request.VerifyAndGeneratePVVRequest;
import com.inov8.hsm.persistance.TransactionDAO;
import com.inov8.hsm.persistance.model.TransactionLogModel;
import com.inov8.hsm.queue.HSMResponsePoolHandler;
import com.inov8.hsm.util.ConfigReader;
import com.inov8.hsm.util.DateTools;
import com.inov8.hsm.util.FieldUtil;
import com.inov8.hsm.util.VersionInfo;

@Service("IntegrationService")
@SuppressWarnings("all")
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="HSM Controller")
public class IntegrationService {

	@Autowired
	private TransactionDAO transactionDAO;
	@Autowired
	private HSMResponsePoolHandler poolHandler;
	@Autowired
	private HSMClient middlewareClient;
	@Autowired
	private NetworkInfoBean networkInfoBean;

	private static Logger logger = LoggerFactory.getLogger(IntegrationService.class.getSimpleName());
	
	private String ZPK = ConfigReader.getInstance().getProperty("hsm.zpk", "");
	private String PVK = ConfigReader.getInstance().getProperty("hsm.pvk", "");

	/*
	 * SAVES TRANSACTION REQUEST PDU INTO THE DATABASE
	 */
	
	private void saveTransaction(BasePDU pdu, PayShieldDTO payShieldDTO, boolean isConnected) {

		TransactionLogModel transaction = new TransactionLogModel();

		transaction.setUniquePacketIdentifier(pdu.getHeader().getUPID());
		transaction.setCommand(pdu.getHeader().getCommand());
		transaction.setRequestPacket(new String(pdu.getRawPdu()));
		transaction.setRequestTime(DateTools.currentTimestamp());
		transaction.setMicrobankTrxId(payShieldDTO.getMicrobankTransactionCode());
		
		try {
//			this.transactionDAO.save(transaction);
			logger.debug("Transaction Saved UPID : " + transaction.getUniquePacketIdentifier());
			logger.debug("Microbank Transaction Code: " + payShieldDTO.getMicrobankTransactionCode());
		} catch (Exception e) {
			logger.error("Exception", e);
		}

	}

	public PayShieldDTO generateSystemPIN(PayShieldDTO payShieldDTO) {
		
		String UPID = FieldUtil.buildUPID();
		GenerateRandomPINRequest request = new GenerateRandomPINRequest(UPID);
		
		request.setPAN(payShieldDTO.getPAN());
		request.setPINLength("04");
		payShieldDTO.setUPID(UPID);
		
		
		request.build();
		if (networkInfoBean.isConnected()) {
			saveTransaction(request, payShieldDTO, true);
			middlewareClient.sendMessage(request);
			poolHandler.checkResponsePool(payShieldDTO);
			
			if(payShieldDTO.getResponseCode().equals("00")){
				// Generate Visa PVV
				generatePVV(payShieldDTO);
				if(payShieldDTO.getResponseCode().equals("00")){
					// Decrypt PIN to plain
					decryptPIN(payShieldDTO);
				}
			}

		} else {
			saveTransaction(request, payShieldDTO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			payShieldDTO.setResponseCode("911");
		}
		
		return payShieldDTO;
	}
	
	public PayShieldDTO generateUserPIN(PayShieldDTO payShieldDTO) {
		
		payShieldDTO = encryptPIN(payShieldDTO);
		
		if(payShieldDTO.getResponseCode().equals("00")){
			// Generate Visa PVV
			payShieldDTO = generatePVV(payShieldDTO);
		}
		
		return payShieldDTO;
	}

	public PayShieldDTO changeSystemPIN(PayShieldDTO payShieldDTO) {
		
		// Generate New System PIN
		generateSystemPIN(payShieldDTO);
		
		// Generate OLD and New PINs PIN Blocks
		this.generateNewOldPINBlock(payShieldDTO);
		if(!payShieldDTO.getResponseCode().equals("00")){
			return payShieldDTO;
		}
		
		String UPID = FieldUtil.buildUPID();
		VerifyAndGeneratePVVRequest request = new VerifyAndGeneratePVVRequest(UPID);
		
		request.setKeyType("001");
		request.setZPK(ZPK);
		request.setPVK(PVK);
		request.setCurrentPinBlock(payShieldDTO.getOldPinBlock());
		request.setCurrentFormatCode("01");
		request.setPAN(payShieldDTO.getPAN());
		request.setPVKI("0");
		request.setCurrentPVV(payShieldDTO.getOldPVV());
		request.setNewPinBlock(payShieldDTO.getPinBlock());
		payShieldDTO.setUPID(UPID);
		
		request.build();
		if (networkInfoBean.isConnected()) {
			saveTransaction(request, payShieldDTO, true);
			middlewareClient.sendMessage(request);
			poolHandler.checkResponsePool(payShieldDTO);
			
			if(StringUtils.isNotEmpty(payShieldDTO.getOldPIN())){
				payShieldDTO.setOldPIN(payShieldDTO.getOldPIN().replace("F", ""));
			}
			if(StringUtils.isNotEmpty(payShieldDTO.getPIN())){
				payShieldDTO.setPIN(payShieldDTO.getPIN().replace("F", ""));
			}
			
		} else {
			saveTransaction(request, payShieldDTO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			payShieldDTO.setResponseCode("911");
		}
		
		return payShieldDTO;
	}

	public PayShieldDTO changeUserPIN(PayShieldDTO payShieldDTO) {
		// Generate OLD and New PINs PIN Blocks
		this.generateNewOldPINBlock(payShieldDTO);
		if(!payShieldDTO.getResponseCode().equals("00")){
			return payShieldDTO;
		}
		
		String UPID = FieldUtil.buildUPID();
		VerifyAndGeneratePVVRequest request = new VerifyAndGeneratePVVRequest(UPID);
		
		request.setKeyType("001");
		request.setZPK(ZPK);
		request.setPVK(PVK);
		request.setCurrentPinBlock(payShieldDTO.getOldPinBlock());
		request.setCurrentFormatCode("01");
		request.setPAN(payShieldDTO.getPAN());
		request.setPVKI("0");
		request.setCurrentPVV(payShieldDTO.getOldPVV());
		request.setNewPinBlock(payShieldDTO.getPinBlock());
		payShieldDTO.setUPID(UPID);
		
		request.build();
		if (networkInfoBean.isConnected()) {
			saveTransaction(request, payShieldDTO, true);
			middlewareClient.sendMessage(request);
			poolHandler.checkResponsePool(payShieldDTO);
			if(StringUtils.isNotEmpty(payShieldDTO.getOldPIN())){
				payShieldDTO.setOldPIN(payShieldDTO.getOldPIN().replace("F", ""));
			}
			if(StringUtils.isNotEmpty(payShieldDTO.getPIN())){
				payShieldDTO.setPIN(payShieldDTO.getPIN().replace("F", ""));
			}
			
		} else {
			saveTransaction(request, payShieldDTO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			payShieldDTO.setResponseCode("911");
		}
		
		return payShieldDTO;
	}

	public PayShieldDTO verifyPIN(PayShieldDTO payShieldDTO) {
		
		// Encrypt PIN
		payShieldDTO = encryptPIN(payShieldDTO);
		if(!payShieldDTO.getResponseCode().equals("00")){
			return payShieldDTO;
		}
		
		// Generate PIN Block
		payShieldDTO = generatePINBlock(payShieldDTO);
		if(!payShieldDTO.getResponseCode().equals("00")){
			return payShieldDTO;
		}
		
		String UPID = FieldUtil.buildUPID();
		ValidatePINBlockWithPVVRequest request = new ValidatePINBlockWithPVVRequest(UPID);
		
		request.setZPK(ZPK);
		request.setPVK(PVK);
		request.setPinBlock(payShieldDTO.getPinBlock());
		request.setFormatCode("01");
		request.setPAN(payShieldDTO.getPAN());
		request.setPVKI("0");
		request.setPVV(payShieldDTO.getPVV());
		payShieldDTO.setUPID(UPID);
		
		request.build();
		if (networkInfoBean.isConnected()) {
			saveTransaction(request, payShieldDTO, true);
			middlewareClient.sendMessage(request);
			poolHandler.checkResponsePool(payShieldDTO);
			
		} else {
			saveTransaction(request, payShieldDTO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			payShieldDTO.setResponseCode("911");
		}
		
		return payShieldDTO;
	}
	
	///// Private Methods //
	
	private PayShieldDTO decryptPIN(PayShieldDTO payShieldDTO) {
		String UPID = FieldUtil.buildUPID();
		DecryptEncryptedPINRequest request = new DecryptEncryptedPINRequest(UPID);
		request.setPAN(payShieldDTO.getPAN());
		request.setPIN(payShieldDTO.getEncryptedPIN());
		payShieldDTO.setUPID(UPID);
		
		request.build();
		if (networkInfoBean.isConnected()) {
			saveTransaction(request, payShieldDTO, true);
			middlewareClient.sendMessage(request);
			poolHandler.checkResponsePool(payShieldDTO);
		}else{
			saveTransaction(request, payShieldDTO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			payShieldDTO.setResponseCode("911");
		}
		return payShieldDTO;
	}
	
	private PayShieldDTO generatePVV(PayShieldDTO payShieldDTO) {
		String UPID = FieldUtil.buildUPID();
		GenerateVisaPVVRequest request = new GenerateVisaPVVRequest(UPID);
		request.setPVK(PVK);
		request.setPIN(payShieldDTO.getEncryptedPIN());
		request.setPAN(payShieldDTO.getPAN());
		request.setPVKI("0");
		payShieldDTO.setUPID(UPID);
		
		request.build();
		if (networkInfoBean.isConnected()) {
			saveTransaction(request, payShieldDTO, true);
			middlewareClient.sendMessage(request);
			poolHandler.checkResponsePool(payShieldDTO);
		}else{
			saveTransaction(request, payShieldDTO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			payShieldDTO.setResponseCode("911");
		}
		return payShieldDTO;
	}
	
	private PayShieldDTO encryptPIN(PayShieldDTO payShieldDTO) {
		String UPID = FieldUtil.buildUPID();
		EncryptClearPINRequest request = new EncryptClearPINRequest(UPID);
		
		// Append 'F' to fill length of 5 of PIN as per Thales
		payShieldDTO.setPIN(StringUtils.rightPad(payShieldDTO.getPIN(), 5, 'F'));
		payShieldDTO.setOldPIN(StringUtils.rightPad(payShieldDTO.getOldPIN(), 5, 'F'));

		if(payShieldDTO.isOldPINOperation()){
			request.setPIN(payShieldDTO.getOldPIN());
		}else{
			request.setPIN(payShieldDTO.getPIN());
		}
		request.setPAN(payShieldDTO.getPAN());
		payShieldDTO.setUPID(UPID);
		
		request.build();
		if (networkInfoBean.isConnected()) {
			saveTransaction(request, payShieldDTO, true);
			middlewareClient.sendMessage(request);
			poolHandler.checkResponsePool(payShieldDTO);
			
			if(payShieldDTO.isOldPINOperation()){
				// Remove 'F' from PIN
				payShieldDTO.setOldPIN(payShieldDTO.getOldPIN().replace("F", ""));
			}else{
				// Remove 'F' from PIN
				payShieldDTO.setPIN(payShieldDTO.getPIN().replace("F", ""));
			}
						
		}else{
			saveTransaction(request, payShieldDTO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			payShieldDTO.setResponseCode("911");
		}
		return payShieldDTO;
	}
	
	private PayShieldDTO generatePINBlock(PayShieldDTO payShieldDTO) {
		
		String UPID = FieldUtil.buildUPID();
		GeneratePINBlockRequest request = new GeneratePINBlockRequest(UPID);
		
		request.setZPK(ZPK);
		request.setFormatCode("01");
		request.setPAN(payShieldDTO.getPAN());
		
		if(payShieldDTO.isOldPINOperation()){
			request.setPIN(payShieldDTO.getEncryptedOldPIN());
		}else{
			request.setPIN(payShieldDTO.getEncryptedPIN());
		}
		
		payShieldDTO.setUPID(UPID);
		
		request.build();
		if (networkInfoBean.isConnected()) {
			saveTransaction(request, payShieldDTO, true);
			middlewareClient.sendMessage(request);
			poolHandler.checkResponsePool(payShieldDTO);
		}else{
			saveTransaction(request, payShieldDTO, false);
			logger.debug("No connection found with Middleware Server => Sending 911 response Code");
			payShieldDTO.setResponseCode("911");
		}
		return payShieldDTO;
	}
	
	
	private PayShieldDTO generateNewOldPINBlock(PayShieldDTO payShieldDTO) {
		// New PIN Block Generation
		// Encrypt PIN
		payShieldDTO = encryptPIN(payShieldDTO);
		if(!payShieldDTO.getResponseCode().equals("00")){
			return payShieldDTO;
		}
		
		// Generate PIN Block
		payShieldDTO = generatePINBlock(payShieldDTO);
		if(!payShieldDTO.getResponseCode().equals("00")){
			return payShieldDTO;
		}
		
		// OLD PIN Block Generation
		payShieldDTO.setOldPINOperation(true);
		// Encrypt PIN
		payShieldDTO = encryptPIN(payShieldDTO);
		if(!payShieldDTO.getResponseCode().equals("00")){
			return payShieldDTO;
		}
		
		// Generate PIN Block
		payShieldDTO = generatePINBlock(payShieldDTO);
		if(!payShieldDTO.getResponseCode().equals("00")){
			return payShieldDTO;
		}
		
		return payShieldDTO;
	}
	
	
}

package com.inov8.hsm.queue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inov8.hsm.dto.PayShieldDTO;
import com.inov8.hsm.enums.TransactionStatus;
import com.inov8.hsm.pdu.BasePDU;
import com.inov8.hsm.pdu.response.DecryptEncryptedPINResponse;
import com.inov8.hsm.pdu.response.EncryptClearPINResponse;
import com.inov8.hsm.pdu.response.GeneratePINBlockResponse;
import com.inov8.hsm.pdu.response.GenerateRandomPINResponse;
import com.inov8.hsm.pdu.response.GenerateVisaPVVResponse;
import com.inov8.hsm.pdu.response.ValidatePINBlockWithPVVResponse;
import com.inov8.hsm.pdu.response.VerifyAndGeneratePVVResponse;
import com.inov8.hsm.persistance.TransactionDAO;
import com.inov8.hsm.persistance.model.TransactionLogModel;
import com.inov8.hsm.util.ConfigReader;
import com.inov8.hsm.util.DateTools;
import com.inov8.hsm.util.VersionInfo;

@Component("TransactionResponsePoolHandler")
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra", tags = "HSM Controller")
public class HSMResponsePoolHandler {
	private static Logger logger = LoggerFactory.getLogger(HSMResponsePoolHandler.class);

	@Autowired
	private HSMResponsePool transactionResponsePool;

	@Autowired
	private TransactionDAO transactionDAO;

	String txTimeoutString;
	String sleepTimeString;
	String debug;

	Integer txTimeout;
	Integer sleepTime;

	public HSMResponsePoolHandler() {
	}

	private void loadApplicationConfig() {
		txTimeoutString = ConfigReader.getInstance().getProperty("txtimeout", "70000");
		sleepTimeString = ConfigReader.getInstance().getProperty("sleeptime", "1000", true);
		debug = ConfigReader.getInstance().getProperty("debug", "false");
		txTimeout = Integer.parseInt(txTimeoutString);
		sleepTime = Integer.parseInt(sleepTimeString);
	}

	public PayShieldDTO checkResponsePool(PayShieldDTO payShieldDTO) {
		loadApplicationConfig();
		String UPID = payShieldDTO.getUPID();
		Long startTime = System.currentTimeMillis();

		while (true) {
			long timeElapsed = System.currentTimeMillis() - startTime.longValue();

			if (timeElapsed >= txTimeout) {
				logger.debug("***** TRANSACTION REQUEST TIMED OUT  *****");
				
				logger.debug("Microbank Transaction Code => " + payShieldDTO.getMicrobankTransactionCode());
//				this.transactionDAO.updateTransactionStatus(UPID, TransactionStatus.TIMEOUT.getValue().intValue());
				payShieldDTO.setMicrobankTransactionCode(payShieldDTO.getMicrobankTransactionCode());
				
				logger.debug("UPID => " + UPID);
				logger.debug("Elapsed Time => " + timeElapsed + "  :: Timeout Configured => " + txTimeout);
				logger.debug("***** TRANSACTION REQUEST TIMED OUT  *****");
				
				payShieldDTO.setUPID(UPID);
				payShieldDTO.setResponseCode("404");
				logger.debug("Response Code 404 for UPID: " + UPID);
				return payShieldDTO;
			}
			// Locate the response from shared resources
			BasePDU basePDU = transactionResponsePool.get(UPID);

			if (basePDU != null) {
				if(basePDU.getHeader().getCommand().equalsIgnoreCase("JB")){
					GenerateRandomPINResponse response = (GenerateRandomPINResponse) basePDU;
					// Got Encrypted PIN
					payShieldDTO.setEncryptedPIN(response.getPIN());
					payShieldDTO.setResponseCode(response.getResponseCode());
				}
				
				else if(basePDU.getHeader().getCommand().equalsIgnoreCase("NH")){
					DecryptEncryptedPINResponse response = (DecryptEncryptedPINResponse) basePDU;
					// Got Plain PIN
					if(StringUtils.isNotEmpty(response.getPIN())){
						// Remove 'F' from Plain PIN
						payShieldDTO.setPIN(response.getPIN().replace("F", ""));
					}
					payShieldDTO.setResponseCode(response.getResponseCode());
				}
				
				else if(basePDU.getHeader().getCommand().equalsIgnoreCase("DH")){
					GenerateVisaPVVResponse response = (GenerateVisaPVVResponse) basePDU;
					payShieldDTO.setPVV(response.getPVV());
					payShieldDTO.setResponseCode(response.getResponseCode());
				}
				
				else if(basePDU.getHeader().getCommand().equalsIgnoreCase("BB")){
					EncryptClearPINResponse response = (EncryptClearPINResponse) basePDU;
					if(payShieldDTO.isOldPINOperation()){
						payShieldDTO.setEncryptedOldPIN(response.getPIN());
					}else{
						payShieldDTO.setEncryptedPIN(response.getPIN());
					}
					payShieldDTO.setResponseCode(response.getResponseCode());
				}
				
				else if(basePDU.getHeader().getCommand().equalsIgnoreCase("JH")){
					GeneratePINBlockResponse response = (GeneratePINBlockResponse) basePDU;
					
					if(payShieldDTO.isOldPINOperation()){
						payShieldDTO.setOldPinBlock(response.getPinBlock());
					}else{
						payShieldDTO.setPinBlock(response.getPinBlock());
					}
					
					payShieldDTO.setResponseCode(response.getResponseCode());
				}
				
				else if(basePDU.getHeader().getCommand().equalsIgnoreCase("ED")){
					ValidatePINBlockWithPVVResponse response = (ValidatePINBlockWithPVVResponse) basePDU;
					
					payShieldDTO.setResponseCode(response.getResponseCode());
				}
				
				else if(basePDU.getHeader().getCommand().equalsIgnoreCase("CV")){
					VerifyAndGeneratePVVResponse response = (VerifyAndGeneratePVVResponse) basePDU;
					
					payShieldDTO.setPVV(response.getPVV());
					payShieldDTO.setResponseCode(response.getResponseCode());
				}
				
				this.transactionResponsePool.remove(UPID);

//				TransactionLogModel trx = this.transactionDAO.select(UPID);
//				if(trx != null){
//					trx.setResponseTime(DateTools.currentTimestamp());
//					trx.setResponseCode(payShieldDTO.getResponseCode());
//					trx.setResponsePacket(new String(basePDU.getRawPdu()));
//	
////					this.transactionDAO.update(trx);
//				}else{
//					logger.debug("UPID not found in database : " + UPID);
//				}
				logger.debug("UPID being removed from  Transaction Pool: " + UPID);
				logger.debug("UPID status updated in database as COMPLETED: " + UPID);
				payShieldDTO.setUPID(UPID);
				return payShieldDTO;
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.error("Exception", e);
				logger.error(e.getMessage());
			}
		}
	}

}
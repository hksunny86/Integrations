package com.inov8.microbank.otp.service;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.operatormodule.OperatorDAO;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by Atieq Rehman on 8/27/2016.
 */
public class MiniTransactionManagerImpl implements MiniTransactionManager {
	final Log logger = LogFactory.getLog(getClass());
	private MiniTransactionDAO miniTransactionDAO;
	private OperatorDAO operatorDAO;
	private SmsSender smsSender;

	@Override
	public MiniTransactionModel generateOTP(MiniTransactionModel miniTransactionModel, String messageKey, String operation) throws FrameworkCheckedException {

		// mark all previous records having same app user id, mobile and command id in state PIN_SENT
		miniTransactionDAO.updateMiniTransactionModels(miniTransactionModel, MiniTransactionStateConstant.EXPIRED);

		String actualPin = RandomUtils.generateRandom(4, false, true);
		String encryptedPin = EncryptionUtil.encryptWithAES(operatorDAO.findByPrimaryKey(1l).getAESKey(), actualPin);
		logger.info("Dear User,Your One Time Pin: " + actualPin);
		miniTransactionModel.setOneTimePin(encryptedPin);
		miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
		miniTransactionModel.setOtRetryCount(0L);

		Date currentDate = new Date();
		miniTransactionModel.setCreatedOn(currentDate);
		miniTransactionModel.setUpdatedOn(currentDate);
		miniTransactionModel.setCreatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
		miniTransactionModel.setUpdatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
		miniTransactionModel.setTimeDate(currentDate);
		if(ThreadLocalActionLog.getActionLogId() != null)
			miniTransactionModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

		miniTransactionDAO.saveOrUpdate(miniTransactionModel);

		miniTransactionModel.setPlainOTP(actualPin);

		smsSender.send(new SmsMessage(miniTransactionModel.getMobileNo(),
				MessageUtil.getMessage(messageKey, new Object[] { operation, miniTransactionModel.getPlainOTP()})));

		return miniTransactionModel;
	}

	@Override
    public Long verifyOTP(MiniTransactionModel miniTransactionModel, String pin) throws FrameworkCheckedException {
		miniTransactionModel = miniTransactionDAO.loadMiniTransactionModel(miniTransactionModel);

        Long errorCode = -1l;
		if(miniTransactionModel == null) {
			logger.warn("MiniTransactionManagerImpl.verifyOTP() : no Mini Transaction Model found with given parameters.");

			return ErrorCodes.OTP_INVALID;
		}

		long transValidityInMilliSecs = MessageUtil.getOTPValidityInMin() * 60 * 1000;
		long timeDiff = System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime();

		//check PIN expiry
		if (timeDiff > transValidityInMilliSecs) {
			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.EXPIRED);

			logger.warn("MiniTransactionManagerImpl.verifyOTP() : given OTP is expired. (based on time configured in min : " + MessageUtil.getOTPValidityInMin());
            errorCode = ErrorCodes.OTP_EXPIRED;
		}

		else {
			if(miniTransactionModel.getOtRetryCount()==null || "".equals(miniTransactionModel.getOtRetryCount()))
				miniTransactionModel.setOtRetryCount(0L);
			Long retryCount = miniTransactionModel.getOtRetryCount() + 1L;
			miniTransactionModel.setOtRetryCount(retryCount);

			//check PIN validity
			if (pin.equals(miniTransactionModel.getOneTimePin())) {
				miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.COMPLETED);
			}

			else {
				int retryLimit = MessageUtil.getOTPRetryLimit();
				if (retryCount == retryLimit) {
					miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.EXPIRED);

					logger.warn("MiniTransactionManagerImpl.verifyOTP() : given OTP is expired. (based on retry limit configured : " + MessageUtil.getOTPRetryLimit());
					errorCode = ErrorCodes.OTP_EXPIRED;
				}

				else {
					logger.warn("MiniTransactionManagerImpl.verifyOTP() : given OTP is invalid.");
					errorCode = ErrorCodes.OTP_INVALID;
				}
			}
		}

		if(miniTransactionModel != null) {
			miniTransactionModel.setUpdatedOn(new Date());
			miniTransactionDAO.saveOrUpdate(miniTransactionModel);
		}

        return errorCode;
	}

	@Override
	public Long resendOTP(MiniTransactionModel miniTransactionModel, String messageKey, String operation) throws FrameworkCheckedException {
		miniTransactionModel = miniTransactionDAO.loadMiniTransactionModel(miniTransactionModel);

		Long errorCode = -1l;
		if(miniTransactionModel == null) {
			logger.warn("MiniTransactionManagerImpl.resendOTP() : no Mini Transaction Model found with given parameters.");

			return ErrorCodes.OTP_EXPIRED;
		}

		long transValidityInMilliSecs = MessageUtil.getOTPValidityInMin() * 60 * 1000;
		long timeDiff = System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime();

		//check PIN expiry
		if (timeDiff > transValidityInMilliSecs) {
			logger.warn("MiniTransactionManagerImpl.verifyOTP() : given OTP is expired. (based on retry limit configured : " + MessageUtil.getOTPRetryLimit());

			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.EXPIRED);
			errorCode = ErrorCodes.OTP_EXPIRED;
			miniTransactionModel.setUpdatedOn(new Date());
			miniTransactionDAO.saveOrUpdate(miniTransactionModel);
		}

		else {
			String pin = miniTransactionModel.getOneTimePin();

			String plainPin = EncryptionUtil.decryptWithAES(operatorDAO.findByPrimaryKey(1l).getAESKey(), pin);
			miniTransactionModel.setPlainOTP(plainPin);
		}

		if(errorCode == -1) {
			smsSender.send(new SmsMessage(miniTransactionModel.getMobileNo(),
					MessageUtil.getMessage(messageKey, new Object[]{operation, miniTransactionModel.getPlainOTP()})));
		}

		return errorCode;
	}

	@Override
	public MiniTransactionModel getModelWithDefaults(String encKey, Long commandId, String mobileNo, Long appUserId) {
		MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
		miniTransactionModel.setCommandId(commandId);
		miniTransactionModel.setAppUserId(appUserId);
		miniTransactionModel.setMobileNo(mobileNo);

		return populateModelWithDefaults(encKey, 4, miniTransactionModel);
	}
	
	@Override
	public MiniTransactionModel populateModelWithDefaults(String encKey, int keyLength, MiniTransactionModel miniTransactionModel) {

		String actualPin = RandomUtils.generateRandom(keyLength, false, true);
		String encPin = EncryptionUtil.encryptWithAES(encKey, actualPin);

		if (StringUtil.isNullOrEmpty(encPin)) {
			throw new RuntimeException("Encryption failed");
		}

		Date currentDate = new Date();
		miniTransactionModel.setOneTimePin(encPin);
//		miniTransactionModel.setPlainPin(actualPin);
		miniTransactionModel.setOtRetryCount(0L);
		miniTransactionModel.setCreatedOn(currentDate);
		miniTransactionModel.setUpdatedOn(currentDate);
		miniTransactionModel.setCreatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
		miniTransactionModel.setUpdatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
		miniTransactionModel.setTimeDate(currentDate);
		miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
		miniTransactionModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

		return miniTransactionModel;
	}

	@Override
	public boolean updateIfExpired(MiniTransactionModel miniTransactionModel) {
		boolean isExpired = false;
		Long expiryTimeLimit = MessageUtil.getOTPValidityInMin() * 60 /*Seconds*/ * 1000 /*Milli Seconds*/;
		if(miniTransactionModel.getOtRetryCount() == null) {
			miniTransactionModel.setOtRetryCount(0L);
		}
		if(miniTransactionModel.getOtRetryCount() > 2
				|| (System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime() >= expiryTimeLimit)
				|| miniTransactionModel.getMiniTransactionStateId() == MiniTransactionStateConstant.OT_PIN_EXPIRED.longValue()) {
			isExpired = true;
			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.OT_PIN_EXPIRED.longValue());
			miniTransactionDAO.saveOrUpdate(miniTransactionModel);
		}
		return isExpired;
	}

	@Override
	public SearchBaseWrapper getMiniTransactionModelByTransactionCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		MiniTransactionModel miniTransactionModel = (MiniTransactionModel) searchBaseWrapper.getBasePersistableModel();

		List<MiniTransactionModel> list = (List<MiniTransactionModel>) this.miniTransactionDAO.findByExample(miniTransactionModel).getResultsetList();
		if (list != null && list.size() > 0) {
			miniTransactionModel = list.get(0);
		} else {
			miniTransactionModel = null;
		}
		searchBaseWrapper.setBasePersistableModel(miniTransactionModel);
		return searchBaseWrapper;
	}

	@Override
	public boolean updateIfExpired(MiniTransactionModel miniTransactionModel, Long productId) {
		boolean isExpired = false;
		Long expiryLimitInMin = MessageUtil.getOTPValidityInMin();
//		CashPaymentValidityModel cashPaymentValidityModel;

		if(productId.longValue() == ProductConstantsInterface.CASH_WITHDRAWAL){
			expiryLimitInMin = MessageUtil.getCWOTPValidityInMin()* 24 /*hours*/ ;
		}
		/*else if(productId.longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH){
			cashPaymentValidityModel = cashPaymentValidityDAO.loadCashPaymentValidityModel(ProductConstantsInterface.ACCOUNT_TO_CASH);
			expiryLimitInMin = cashPaymentValidityModel.getValidityDays()* 24 *//*hours*//* * 60 *//*min*//*;;
		}
		else if(productId.longValue() == ProductConstantsInterface.CASH_TRANSFER){
			cashPaymentValidityModel = cashPaymentValidityDAO.loadCashPaymentValidityModel(ProductConstantsInterface.CASH_TRANSFER);
			expiryLimitInMin = cashPaymentValidityModel.getValidityDays()* 24 *//*hours*//* * 60 *//*min*//*;;
		}*/

		Long expiryTimeLimit = expiryLimitInMin * 60 /*Seconds*/ * 1000 /*Milli Seconds*/;

		if(miniTransactionModel.getOtRetryCount() == null) {
			miniTransactionModel.setOtRetryCount(0L);
		}
		if(miniTransactionModel.getOtRetryCount() > 2
				|| (System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime() >= expiryTimeLimit)
				|| miniTransactionModel.getMiniTransactionStateId() == MiniTransactionStateConstant.OT_PIN_EXPIRED.longValue()) {
			isExpired = true;
			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.OT_PIN_EXPIRED.longValue());
			miniTransactionDAO.saveOrUpdate(miniTransactionModel);
		}
		return isExpired;
	}

	@Override
	public void validateOneTimePin(MiniTransactionModel miniTransactionModel, String oneTimePin, String encType) throws CommandException, FrameworkCheckedException {
		
		Long transactionStateId = miniTransactionModel.getMiniTransactionStateId();
		long errorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
		
		if(transactionStateId != MiniTransactionStateConstant.PIN_SENT.longValue()) {
			String errorMessage = MessageUtil.getMessage("MINI.TransactionClosed");
			 
			if(MiniTransactionStateConstant.OT_PIN_EXPIRED == transactionStateId) {
				errorMessage = MessageUtil.getMessage(ErrorCodes.OTP_EXPIRED);
				errorCode = ErrorCodes.OTP_EXPIRED;
			}
			 
		    throw new CommandException(errorMessage, errorCode, ErrorLevel.MEDIUM,new Throwable());
		}
		 
		boolean isTxCodeInvalid = false;	
		String errorMessage = "";
		oneTimePin = StringUtil.replaceSpacesWithPlus(oneTimePin);
		String encryptedOTPin = EncryptionUtil.encryptWithAES(operatorDAO.findByPrimaryKey(Long.parseLong(encType)).getAESKey(),oneTimePin);
		Long retryCount = miniTransactionModel.getOtRetryCount();
		long otPinRetryCount = retryCount != null ? retryCount.longValue() : 0;
		
		if (!miniTransactionModel.getOneTimePin().equals(encryptedOTPin)) { 
			isTxCodeInvalid = true;
			
			if(otPinRetryCount + 1 == CommandFieldConstants.KEY_OT_PIN_RETRY_LIMIT.longValue()) {
				miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.OT_PIN_EXPIRED);
				errorMessage = MessageUtil.getMessage(ErrorCodes.OTP_EXPIRED);
				errorCode = ErrorCodes.OTP_EXPIRED;
			}	
			else {
//				errorMessage = MessageUtil.getMessage(ErrorCodes.OTP_INVALID);
//				errorCode = ErrorCodes.OTP_INVALID;
			}

			miniTransactionModel.setOtRetryCount(++otPinRetryCount);
		}
	
		if(isTxCodeInvalid) {
			if( ThreadLocalAppUser.getAppUserModel() != null && ThreadLocalAppUser.getAppUserModel().getAppUserId() != null )
				miniTransactionModel.setUpdatedBy( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
			miniTransactionModel.setUpdatedOn(new Date()) ;
			
			miniTransactionDAO.saveOrUpdate(miniTransactionModel);
			
			throw new CommandException(errorMessage, errorCode, ErrorLevel.MEDIUM);
		}
	}

	@Override
	public MiniTransactionModel getMiniTransactionModelWithDefaults(Long operatorId, Long commandId, String mobileNo, Long appUserId, Long actionLogId)
			throws FrameworkCheckedException {
		return this.getModelWithDefaults(operatorDAO.findByPrimaryKey(operatorId).getAESKey(), commandId, mobileNo, appUserId);
	}

	@Override
	public MiniTransactionModel saveOrUpdate(MiniTransactionModel miniTransactionModel) {
		return miniTransactionDAO.saveOrUpdate(miniTransactionModel);
	}

	@Override
	public MiniTransactionModel findBYPrimaryKey(Long miniTransactionModelId) {
		return miniTransactionDAO.findByPrimaryKey(miniTransactionModelId);
	}

	public MiniTransactionDAO getMiniTransactionDAO() {
		return miniTransactionDAO;
	}

	public void setMiniTransactionDAO(MiniTransactionDAO miniTransactionDAO) {
		this.miniTransactionDAO = miniTransactionDAO;
	}


	public void setOperatorDAO(OperatorDAO operatorDAO) {
		this.operatorDAO = operatorDAO;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}
}

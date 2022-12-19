/**
 * 
 */
package com.inov8.microbank.server.service.smssendermodule;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.support.ApplicationObjectSupport;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.FailedSmsModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.minitransactionmodule.MiniTransactionViewModel;
import com.inov8.microbank.common.model.minitransactionmodule.TransactionCodeHistoryViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RealTimeSmsSender;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.messagemodule.FailedSmsDAO;
import com.inov8.microbank.server.dao.minitransactionmodule.MiniTransactionViewDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionCodeHistoryDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDAO;
import com.inov8.microbank.server.dao.userdeviceaccount.UserDeviceAccountListViewDAO;
import com.inov8.microbank.server.messaging.listener.DlqMessageListener;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.dailyjob.FailedSmsSender;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jan 4, 2007
 * Creation Time: 			5:00:40 PM
 * Description:				
 */
public class SmsSenderServiceImpl extends ApplicationObjectSupport implements SmsSenderService
{
	protected static Log logger	= LogFactory.getLog(SmsSenderServiceImpl.class);

	private TransactionDAO transactionDAO;
	private MiniTransactionViewDAO miniTransactionViewDAO;
	private SmsSender smsSender;
	private FinancialIntegrationManager financialIntegrationManager;
	private SmartMoneyAccountDAO	smartMoneyAccountDAO;
	private ActionLogManager		actionLogManager;
	private MiniTransactionDAO miniTransactionDAO;
	private TransactionCodeHistoryDAO transactionCodeHistoryDAO;
	private TransactionDetailI8Manager transactionDetailI8Manager;
	private UserDeviceAccountListViewDAO userDeviceAccountListViewDAO;
	private FailedSmsDAO failedSmsDAO;
    private RealTimeSmsSender realTimeSmsSender;

	public SmsSenderServiceImpl()
	{
		super();
	}
	
	public void sendSms(String mobileNumber, String textToSms) throws FrameworkCheckedException
	{
		smsSender.send(new SmsMessage(mobileNumber, textToSms));
	}

	public void resendSms(Long transactionId) throws FrameworkCheckedException
	{
		TransactionModel model = loadTransactionModel(transactionId);
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RESEND_SMS_USECASE_ID);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
    	ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		sendSms(model.getNotificationMobileNo(), model.getConfirmationMessage());
		
		actionLogModel.setCustomField1(""+model.getPrimaryKey());
		actionLogModel.setCustomField11(model.getTransactionCodeId().toString());
		this.actionLogManager.completeActionLog(actionLogModel);
	}

	@Override
	public void resendSmsUsingStrategy( Long transactionId, String resendSmsStrategy ) throws FrameworkCheckedException
	{
	    TransactionModel model = transactionDAO.loadTxAndTxDetailModel( transactionId );
	   
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RESEND_SMS_USECASE_ID);
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        ResendSmsStrategyEnum.valueOf( resendSmsStrategy ).resendSms( model, smsSender,transactionDetailI8Manager,userDeviceAccountListViewDAO);

        actionLogModel.setCustomField1(""+model.getPrimaryKey());
        actionLogModel.setCustomField11(model.getTransactionCodeId().toString());
        this.actionLogManager.completeActionLog(actionLogModel);
	}
	
	@Override
	public void resendSmsUsingStrategy( BaseWrapper baseWrapper ) throws FrameworkCheckedException
	{
	    TransactionModel model = transactionDAO.loadTxAndTxDetailModel((Long) baseWrapper.getObject("transactionId"));
	    String resendSmsStrategy = (String) baseWrapper.getObject("resendSmsStrategy");

       
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        ResendSmsStrategyEnum.valueOf( resendSmsStrategy ).resendSms( model, smsSender,transactionDetailI8Manager,userDeviceAccountListViewDAO);

        actionLogModel.setCustomField1(""+model.getPrimaryKey());
        actionLogModel.setCustomField11(model.getNotificationMobileNo());
        this.actionLogManager.completeActionLog(actionLogModel);
	}

	public void resendCashWithdrawalSms(Long transactionId) throws FrameworkCheckedException
	{
		String generatedPIN = "";
		MiniTransactionViewModel txModel = this.miniTransactionViewDAO.findByPrimaryKey(transactionId);
		DateTime expiryDate = new DateTime(txModel.getTimeDate()).plusDays(1);
		
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		smartMoneyAccountModel.setCustomerId(txModel.getCustomerId());
		smartMoneyAccountModel.setDefAccount(true);

		try{		
			CustomList<SmartMoneyAccountModel> customList = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
	
			if(customList != null && customList.getResultsetList() != null && !customList.getResultsetList().isEmpty()) {
				SmartMoneyAccountModel SMAmodel = customList.getResultsetList().get(0);
	
				BaseWrapper baseWrapperSmartMoney = new BaseWrapperImpl();
				baseWrapperSmartMoney.setBasePersistableModel(SMAmodel);
				AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperSmartMoney);
	    		
				ActionLogModel actionLogModel = new ActionLogModel();
				actionLogModel.setActionId(PortalConstants.ACTION_UPDATE);
				actionLogModel.setUsecaseId(PortalConstants.ONE_TIME_PIN_REGENERATE_USECASE_ID);
				actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
				actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
				actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
				actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
				actionLogModel.setCustomField1(txModel.getTransactionCode().toString());
				actionLogModel.setCustomField11(txModel.getTransactionCode().toString());
				actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
				actionLogModel = logAction(actionLogModel, true);

				//setting actionLogId into thread local
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
				
				
				
				LogModel logModel = new LogModel();
				logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
				logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
				
				AccountInfoModel accountInfoModel = new AccountInfoModel();
				accountInfoModel.setCustomerId(SMAmodel.getCustomerId());
				accountInfoModel.setAccountNick(SMAmodel.getName());

				veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
				veriflyBaseWrapper.setBasePersistableModel(SMAmodel);
				veriflyBaseWrapper.setLogModel(logModel);
            	
				veriflyBaseWrapper = abstractFinancialInstitution.generateOneTimePin(veriflyBaseWrapper);
				if (veriflyBaseWrapper.isErrorStatus()){
					generatedPIN = veriflyBaseWrapper.getAccountInfoModel().getOtPin();
				}
				
				// RESET Transaction TimeDate
				  MiniTransactionModel miniTransactionModel = this.miniTransactionDAO.findByPrimaryKey(txModel.getMiniTransactionId());
				  miniTransactionModel.setUpdatedOn(new Date());
				  miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				  miniTransactionModel.setTimeDate(new Date());
				  this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);
				  
				actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
				actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
				actionLogModel.setCustomField1(String.valueOf(accountInfoModel.getCustomerId()));
				actionLogModel = logAction(actionLogModel,false);
				  
			}
		} catch (Exception e){
			throw new FrameworkCheckedException(e.getMessage(), e);
		}
		
		String brandName = MessageUtil.getMessage("jsbl.brandName");
		DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
		
		String smsText = getMessageSourceAccessor().getMessage(
							"smsCommand.cashwithdrawal.resend",
							new Object[] { 
									brandName,
									txModel.getTransactionCode().toString(),
									generatedPIN,
									dtf.print(new DateTime()),
									tf.print(new LocalTime()),});
		
		sendSms(txModel.getCustomerMobileNo(), smsText);
	}

	private ActionLogModel logAction(ActionLogModel actionLogModel, boolean isNewTrans)
		throws FrameworkCheckedException{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		if (isNewTrans)
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		else
			baseWrapper = this.actionLogManager.createOrUpdateActionLog(baseWrapper);
		return (ActionLogModel) baseWrapper.getBasePersistableModel();
	}

	public void resendCashPaymentSms(Long transactionId,Boolean isCash2Cash)
			throws FrameworkCheckedException {

		String originalPin = "";
		String encryptedPin = "";
		MiniTransactionViewModel txModel = this.miniTransactionViewDAO.findByPrimaryKey(transactionId);
		MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
		TransactionCodeHistoryViewModel transactionCodeHtrModel= new TransactionCodeHistoryViewModel();
		try{		
	    		
			ActionLogModel actionLogModel = new ActionLogModel();
			actionLogModel.setActionId(PortalConstants.ACTION_UPDATE);
			actionLogModel.setUsecaseId(PortalConstants.KEY_REGENERATE_TRX_CODE_USECASE_ID);
			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
			actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
			actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
			actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
			actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
			actionLogModel = logAction(actionLogModel, true);

			//setting actionLogId into thread local
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			
			//mark existing mini_transacitons to Expired
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			MiniTransactionModel miniTransaction = new MiniTransactionModel();
			// Mark all previous transactions as expired........
			//miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT);
			miniTransaction.setMiniTransactionId(transactionId);
			miniTransaction = this.miniTransactionDAO.findByPrimaryKey(transactionId);
		  
			baseWrapper.setBasePersistableModel(miniTransaction);
			this.modifyPINSentMiniTransToExpired(baseWrapper);
			//end of expire mark
			
			/*Generate Pin and store in MiniTransaction*/
			originalPin = CommonUtils.generateOneTimePin(5);
			encryptedPin = EncoderUtils.encodeToSha(originalPin);

			miniTransactionModel = this.miniTransactionDAO.findByPrimaryKey(transactionId);
			miniTransactionModel.setMiniTransactionId(null);
			miniTransactionModel.setUpdatedOn(new Date());
			miniTransactionModel.setCreatedOn(new Date());
			miniTransactionModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			miniTransactionModel.setTimeDate(new Date());
			miniTransactionModel.setOneTimePin(encryptedPin);
			miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT);
			this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);
			
			transactionCodeHtrModel.setCode(miniTransactionModel.getRelationTransactionCodeIdTransactionCodeModel().getCode());
			//transactionCodeHtrModel.setComments(miniTransactionModel.getComments());
			transactionCodeHtrModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			transactionCodeHtrModel.setCreatedOn(new Date());
			transactionCodeHtrModel.setUsecaseId(PortalConstants.ONE_TIME_PIN_REGENERATE_USECASE_ID);
			transactionCodeHistoryDAO.saveOrUpdate(transactionCodeHtrModel);
		  
			actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
			actionLogModel.setCustomField1(String.valueOf(miniTransactionModel.getMiniTransactionId()));
			actionLogModel.setCustomField11(transactionCodeHtrModel.getCode());
			actionLogModel.setTrxData(transactionCodeHtrModel.getCode() + "|||||||||||||");
			actionLogModel = logAction(actionLogModel,false);
			  
		} catch (Exception e){
			throw new FrameworkCheckedException(e.getMessage(), e);
		}
		
		String brandName = MessageUtil.getMessage("jsbl.brandName");
		DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
		
		String msgKey = (isCash2Cash==null || isCash2Cash)?"smsCommand.cash2cash.resend":"smsCommand.acc2cash.resend";
		
		String smsText = getMessageSourceAccessor().getMessage(
							msgKey,
							new Object[] {
									brandName,
									txModel.getTransactionCode().toString(),
									originalPin,
									dtf.print(new DateTime()),
									tf.print(new LocalTime()),});
		
		sendSms(isCash2Cash==null ? txModel.getRecipientMobileNo() : txModel.getSenderMobileNo(), smsText);
	}
	
	public BaseWrapper modifyPINSentMiniTransToExpired(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		MiniTransactionModel miniTransactionModel = (MiniTransactionModel)baseWrapper.getBasePersistableModel();
		List<MiniTransactionModel> miniTransactionModelList = this.miniTransactionDAO.LoadMiniTransactionModelByPK(miniTransactionModel.getTransactionCodeId());
		Iterator<MiniTransactionModel> ite = miniTransactionModelList.iterator() ;
		while( ite.hasNext() ){
			miniTransactionModel = ite.next() ;
			
			
			//Check the 24 hours validity
			/*long transValidityInMilliSecs = 24 * 60  * 60 * 1000;
			long timeDiff = System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime();*/

			//if (timeDiff > transValidityInMilliSecs){
				miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.EXPIRED ) ;
				miniTransactionModel.setUpdatedOn(new Date());
				miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);
			//}
		}		
		return baseWrapper;
	}
	
	public void resendSms(Long transactionId, String mobileNumber) throws FrameworkCheckedException
    {
        TransactionModel model = loadTransactionModel(transactionId);
        sendSms(mobileNumber, model.getConfirmationMessage());
    }
	
	public void saveFailedSms(SmsMessage smsMessage) throws FrameworkCheckedException{
		FailedSmsModel failedSmsModel = new FailedSmsModel();
		failedSmsModel.setCreatedOn(new Date());
		failedSmsModel.setMobileNumber(smsMessage.getMobileNo());
		failedSmsModel.setSmsText(smsMessage.getMessageText());
		
		failedSmsDAO.saveFailedSms(failedSmsModel);
	}
	
	public void makeFailedSmsRetry(FailedSmsModel failedSmsModel) throws FrameworkCheckedException{
		try{
			
			SmsMessage smsMessage = new SmsMessage(failedSmsModel.getMobileNumber(), failedSmsModel.getSmsText());
			logger.info("Step1: Deleting from Failed_SMS: failed_sms_id:"+failedSmsModel.getFailedSmsId()+ " , mob:"+failedSmsModel.getMobileNumber()+ ", created_on:"+failedSmsModel.getCreatedOn());
			failedSmsDAO.deleteByPrimaryKey(failedSmsModel.getPrimaryKey());
			logger.info("Step2: Sending Failed SMS: failed_sms_id:"+failedSmsModel.getFailedSmsId()+ " , mob:"+failedSmsModel.getMobileNumber()+ ", created_on:"+failedSmsModel.getCreatedOn());
			this.realTimeSmsSender.send(smsMessage);
			logger.info("Step3: Completed sending of Failed SMS: failed_sms_id:"+failedSmsModel.getFailedSmsId()+ " , mob:"+failedSmsModel.getMobileNumber()+ ", created_on:"+failedSmsModel.getCreatedOn());
		
		}catch(Exception e){
			logger.error("Exception in makeFailedSmsRetry...",e);
			throw new FrameworkCheckedException(e.getMessage());
		}
	}
	
	@Override
	public void sendSmsList(ArrayList<SmsMessage> smsMessageList) throws FrameworkCheckedException
	{
		smsSender.send(smsMessageList);
	}
    
    private TransactionModel loadTransactionModel(Long transactionId) throws FrameworkCheckedException
    {
        return transactionDAO.findByPrimaryKey(transactionId);
    }

    public void setSmsSender(SmsSender smsSender)
    {
        this.smsSender = smsSender;
    }

    public void setTransactionDAO(TransactionDAO transactionDAO)
    {
        this.transactionDAO = transactionDAO;
    }

    public void setMiniTransactionViewDAO(MiniTransactionViewDAO miniTransactionViewDAO)
    {
        this.miniTransactionViewDAO = miniTransactionViewDAO;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
    {
        this.financialIntegrationManager = financialIntegrationManager;
    }
    
    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO)
    {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }

    public void setActionLogManager(ActionLogManager actionLogManager)
    {
        this.actionLogManager = actionLogManager;
    }

    public void setMiniTransactionDAO( MiniTransactionDAO miniTransactionDAO )
    {
        this.miniTransactionDAO = miniTransactionDAO;
    }

	/**
	 * @return the transactionCodeHtrDAO
	 */
	public TransactionCodeHistoryDAO getTransactionCodeHistoryDAO() {
		return transactionCodeHistoryDAO;
	}

	/**
	 * @param transactionCodeHtrDAO the transactionCodeHtrDAO to set
	 */
	public void setTransactionCodeHistoryDAO(TransactionCodeHistoryDAO transactionCodeHistoryDAO) {
		this.transactionCodeHistoryDAO = transactionCodeHistoryDAO;
	}


	public UserDeviceAccountListViewDAO getUserDeviceAccountListViewDAO() {
		return userDeviceAccountListViewDAO;
	}

	public void setUserDeviceAccountListViewDAO(
			UserDeviceAccountListViewDAO userDeviceAccountListViewDAO) {
		this.userDeviceAccountListViewDAO = userDeviceAccountListViewDAO;
	}

	public TransactionDetailI8Manager getTransactionDetailI8Manager() {
		return transactionDetailI8Manager;
	}

	public void setTransactionDetailI8Manager(
			TransactionDetailI8Manager transactionDetailI8Manager) {
		this.transactionDetailI8Manager = transactionDetailI8Manager;
	}

	public void setFailedSmsDAO(FailedSmsDAO failedSmsDAO) {
		this.failedSmsDAO = failedSmsDAO;
	}
	
	public void setRealTimeSmsSender(RealTimeSmsSender realTimeSmsSender){
		this.realTimeSmsSender = realTimeSmsSender;
	}
	

}

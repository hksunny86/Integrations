package com.inov8.microbank.server.service.financialintegrationmodule;



import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.verifly.common.constants.VeriflyConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.util.VeriflyKeyConstantInterface;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import org.joda.time.DateTimeUtils;


public class VeriflyFinancialInstitutionImpl extends AbstractFinancialInstitution
{
	protected final Log logger = LogFactory.getLog(VeriflyFinancialInstitutionImpl.class);

	public VeriflyFinancialInstitutionImpl()
	{
		super();
	}


	public VeriflyFinancialInstitutionImpl(SwitchController switchController, GenericDao genericDao)
	{
		super(switchController, genericDao);
	}


	VeriflyManagerService veriflyManagerService;

	public VeriflyBaseWrapper activatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.activatePIN(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper changeAccountNick(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.changeAccountNick(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper changePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper.putObject(SwitchConstants.KEY_SWITCHWRAPPER,null);
		veriflyBaseWrapper = veriflyManager.changePIN(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper deactivatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.deactivatePIN(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper deleteAccount(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.deleteAccount(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper generateOneTimePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.generateOneTimePin(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper generatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception,ImplementationNotSupportedException
	{
		if( veriflyBaseWrapper.getObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID ) != null )
		{
			Long deviceTypeId = (Long)veriflyBaseWrapper.getObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID ) ;

			if( deviceTypeId.longValue() == DeviceTypeConstantsInterface.MOBILE.longValue() ||
					deviceTypeId.longValue() == DeviceTypeConstantsInterface.MFS_WEB.longValue() )
			{
				super.generatePin(veriflyBaseWrapper);
			}

			else
			{
				VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
				SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
				veriflyBaseWrapper.setBasePersistableModel(null);
				veriflyBaseWrapper = veriflyManager.generatePIN(veriflyBaseWrapper);
				veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			}
		}

		return veriflyBaseWrapper;
	}

	public boolean isVeriflyRequired() throws FrameworkCheckedException
	{
		return true;
	}

	public VeriflyBaseWrapper markAsDeleted(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.markAsDeleted(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper modifyAccountInfo(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.modifyAccountInfo(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper modifyAccountInfoForBBAgents(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.modifyAccountInfoForBBAgents(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper resetPin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.resetPIN(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper verifyOneTimePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception,ImplementationNotSupportedException
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.verifyOneTimePin(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper verifyPin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);

		logger.info("[VeriflyFinancialInstitution.verifyPin] LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
				". SmartMoneyAccountID:" + smartMoneyAccountModel.getSmartMoneyAccountId() +
				" Account Nick:" + smartMoneyAccountModel.getName() +
				" Verifly AccountInfo.getCustomerID:" + veriflyBaseWrapper.getAccountInfoModel().getCustomerId());

		veriflyBaseWrapper = veriflyManager.verifyPIN(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
	}

	public VeriflyBaseWrapper verifyCredentials(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception
	{
		Long sTime = DateTimeUtils.currentTimeMillis();
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);

		logger.info("[VeriflyFinancialInstitution.verifyCredentials] LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
				". SmartMoneyAccountID:" + smartMoneyAccountModel.getSmartMoneyAccountId() +
				" Account Nick:" + smartMoneyAccountModel.getName() +
				" Verifly AccountInfo.getCustomerID:" + veriflyBaseWrapper.getAccountInfoModel().getCustomerId());

		veriflyBaseWrapper = veriflyManager.verifyCredentials(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		logger.info("Total Time taken by VerifyCredentials() :: " + String.valueOf(DateTimeUtils.currentTimeMillis() - sTime));
		return veriflyBaseWrapper;
	}

	public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception
	{
			
	/*	if(switchWrapper.getWorkFlowWrapper() != null)
		{
			if(switchWrapper.getWorkFlowWrapper().getMPin() != null) 
			{
//				System.out.println("TPin  : "+switchWrapper.getWorkFlowWrapper().getMPin());
//				System.out.println("VeriflyFinancialInstitutionImpl.checkBalance() TPin : "+switchWrapper.getWorkFlowWrapper().getMPin());
			}
			
		}
		*/


		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)switchWrapper.getBasePersistableModel();

		String pin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN);
//		String mPin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_MPIN);		

		AccountInfoModel threadLocalAccountInfoModel = getThreadLocalAccountInfoModel(switchWrapper);
		AccountInfoModel accountInfoModel = new AccountInfoModel() ;
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		boolean errorMessagesFlag = false;
		boolean isThreadLocalAccountInfoLoaded = false;

		if (threadLocalAccountInfoModel != null && threadLocalAccountInfoModel.getAccountNo() != null) {
			accountInfoModel = threadLocalAccountInfoModel;
			errorMessagesFlag = true;
			isThreadLocalAccountInfoLoaded = true;

		}else{

			if( appUserModel.getCustomerId() != null )
				accountInfoModel.setCustomerId(appUserModel.getCustomerId());
			else
				accountInfoModel.setCustomerId(appUserModel.getAppUserId());


			accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
			accountInfoModel.setOldPin(pin);

			LogModel logModel = new LogModel();
			logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
			logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

			if( switchWrapper.getWorkFlowWrapper().getTransactionModel() != null )
				logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());
			//		else



			veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

			veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
			veriflyBaseWrapper.setLogModel(logModel);

			veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

			Boolean skipCheckPin = (Boolean) switchWrapper.getObject(VeriflyKeyConstantInterface.skipCheckPin);
			veriflyBaseWrapper.setSkipPanCheck(skipCheckPin);

			veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
			errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();
		}

		if(errorMessagesFlag)
		{
			if( false == isThreadLocalAccountInfoLoaded){
				accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
				setThreadLocalAccountInfoModel(switchWrapper, accountInfoModel);
			}

			switchWrapper.setAccountInfoModel(accountInfoModel);

			switchWrapper.setBankId( smartMoneyAccountModel.getBankId() );
			switchWrapper.setPaymentModeId( smartMoneyAccountModel.getPaymentModeId() );
			//WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			if(switchWrapper.getWorkFlowWrapper().getTransactionModel() == null)
			{
				switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
			}

			logger.info("[VeriflyFinancialInstitution.checkBalance] Going to check balance for AppUserID:" + appUserModel.getAppUserId());

			switchWrapper = this.switchController.checkBalance(switchWrapper);
		}
		else
		{
			String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
			logger.error("[VeriflyFinancialInstitution.checkBalance] Error in validating Verifly pin for AppUserID:" + appUserModel.getAppUserId() + ". Error Message:" + veriflyErrorMessage);
			throw new CommandException(veriflyErrorMessage,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		return switchWrapper;
	}



	public SwitchWrapper rollback(SwitchWrapper switchWrapper) throws FrameworkCheckedException
	{
		return null;
	}

	public SwitchWrapper transaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception
	{
		WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

		AccountInfoModel accountInfoModel = workFlowWrapper.getAccountInfoModel();
		LogModel logModel = new LogModel();
		logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
		logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();


		veriflyBaseWrapper.setBasePersistableModel(workFlowWrapper.getSmartMoneyAccountModel());

		if( ThreadLocalAppUser.getAppUserModel().getCustomerId() != null )
			accountInfoModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
		else
			accountInfoModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getAppUserId());

		accountInfoModel.setAccountNick(workFlowWrapper.getSmartMoneyAccountModel().getName());
		logModel.setTransactionCodeId(workFlowWrapper.getTransactionModel().getTransactionCodeId());

		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
		veriflyBaseWrapper.setLogModel(logModel);
		veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);

		boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

		if(errorMessagesFlag)
		{
			switchWrapper.setAccountInfoModel(veriflyBaseWrapper.getAccountInfoModel());
			switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
			switchWrapper.setBankId(workFlowWrapper.getSmartMoneyAccountModel().getBankId());

			if( workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId().equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) )
				switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(veriflyBaseWrapper.getAccountInfoModel().getAccountNo(), 5, "*"));
			else if( workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId().equals(PaymentModeConstantsInterface.CREDIT_CARD) )
				switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(veriflyBaseWrapper.getAccountInfoModel().getCardNo(), 5, "*"));

			switchWrapper.setPaymentModeId(workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
			switchWrapper.setVeriflyBaseWrapper(veriflyBaseWrapper);
//			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		}
		else
		{
			throw new FrameworkCheckedException(veriflyBaseWrapper.getErrorMessage());

		}

		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();

		switchWrapper = switchController.transaction(switchWrapper, commissionWrapper);
		return switchWrapper;
	}

	private AccountInfoModel getThreadLocalAccountInfoModel(SwitchWrapper switchWrapper){
		AccountInfoModel accountInfoModel = null;
		
		/*if(switchWrapper.getWorkFlowWrapper().isCheckBalance()){
			
			accountInfoModel = ThreadLocalAccountInfo.getLoggedInCustomerAccountInfo();
			
		}*/

		return accountInfoModel;
	}

	private void setThreadLocalAccountInfoModel(SwitchWrapper switchWrapper, AccountInfoModel accountInfoModel){
		
		/*if(switchWrapper.getWorkFlowWrapper().isCheckBalance()){
			
			ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);
			
		}*/
	}

	public Map<Long, String> getStatusCodes() throws Exception
	{
		return null;
	}


	public SwitchWrapper customerAccountRelationshipInquiry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception
	{
		return null;
	}


	private VeriflyManager loadVeriflyManager(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException
	{
		VeriflyManager veriflyManager = null;
		if(veriflyBaseWrapper.getBasePersistableModel() instanceof BankModel)
		{
			veriflyManager = this.veriflyManagerService.getVeriflyMgrByBankId((BankModel)veriflyBaseWrapper.getBasePersistableModel());
		}
		else if(veriflyBaseWrapper.getBasePersistableModel() instanceof SmartMoneyAccountModel)
		{
			veriflyManager = this.veriflyManagerService.getVeriflyMgrByAccountId((SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel());
		}
		else
		{
			throw new FrameworkCheckedException("Invalid Identifier...");
		}
		return veriflyManager;
	}


	public void setVeriflyManagerService(VeriflyManagerService veriflyManagerService)
	{
		this.veriflyManagerService = veriflyManagerService;
	}

}

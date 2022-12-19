package com.inov8.microbank.server.service.integration.dispenser;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.vo.AgentToAgentTransferVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.exceptionMappingType;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class AgentToAgentTransferDispenser extends BillPaymentProductDispenser
{

	private ProductIntgModuleInfoManager productIntgModuleInfoManager;
	private CommissionManager commissionManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private CustTransManager customerManager;
	private SettlementManager settlementManager;
	private ProductManager productManager;
	private GenericDao genericDAO;
	private ProductUnitManager productUnitManager;
	private FailureLogManager failureLogManager;
	private AppUserManager appUserManager;
	private ShipmentManager shipmentManager;
	private AppUserDAO appUserDao;
	private ApplicationContext ctx;
	private AbstractFinancialInstitution phoenixFinancialInst ;
	private SmartMoneyAccountDAO smartMoneyAccountDAO;
	private VeriflyManagerService veriflyManagerService;
	private RetailerContactManager retailerContactManager;

	private final Log logger = LogFactory.getLog(this.getClass());

	public AgentToAgentTransferDispenser(CommissionManager commissionManager,
			SmartMoneyAccountManager smartMoneyAccountManager, SettlementManager settlementManager,
			ProductManager productManager, AppUserManager appUserManager,
			ProductUnitManager productUnitManager, ShipmentManager shipmentManager, GenericDao genericDAO,ApplicationContext ctx)
	{
		this.commissionManager = commissionManager;
		this.smartMoneyAccountManager = smartMoneyAccountManager;
		this.settlementManager = settlementManager;
		this.productManager = productManager;
		this.appUserManager = appUserManager;
		this.productUnitManager = productUnitManager;
		this.shipmentManager = shipmentManager;
		this.genericDAO = genericDAO;
		this.appUserDao = (AppUserDAO) ctx.getBean("appUserDAO");
		this.ctx = ctx ;

		this.smartMoneyAccountDAO = (SmartMoneyAccountDAO) ctx.getBean("smartMoneyAccountDAO") ;
		this.phoenixFinancialInst = (PhoenixFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl") ;
		this.veriflyManagerService = (VeriflyManagerService) ctx.getBean("veriflyController");
		super.setAuditLogModule((FailureLogManager) ctx.getBean("failureLogManager"));
		this.retailerContactManager = (RetailerContactManager) ctx.getBean("retailerContactManager");
	}

	/**
	 * doSale
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		
			logger.info("Inside doSale of AgentToAgentTransferDispenser");
			
		
		try
		{
			AgentToAgentTransferVO agentToAgentTransferVO = (AgentToAgentTransferVO) workFlowWrapper.getProductVO();
						
			SmartMoneyAccountModel sma = new SmartMoneyAccountModel() ;
//			sma.setRetailerContactId(agentToAgentTransferVO.getAgentId()) ;
			sma.setRetailerContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId()) ;
			sma.setDefAccount(true);
			sma.setDeleted(false);
			ExampleConfigHolderModel example = new ExampleConfigHolderModel();
			example.setEnableLike(Boolean.FALSE);
			CustomList<SmartMoneyAccountModel> smaList = this.smartMoneyAccountDAO.findByExample(sma, null, null, example);
//			CustomList<SmartMoneyAccountModel> smaList = this.smartMoneyAccountDAO.loadCustomerSmartMoneyAccountByHQL(sma);
			

			if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) 
			{
				sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
				
				TransactionModel transactionModelTemp = workFlowWrapper.getTransactionModel() ;
				//Checking balance before hitting for FT
				SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
				switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
//				switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, workFlowWrapper.getAccountInfoModel().getOldPin());
				switchWrapperTemp.setWorkFlowWrapper(workFlowWrapper);
				switchWrapperTemp.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
				switchWrapperTemp.setBasePersistableModel( sma ) ;
				
//				workFlowWrapper.getTransactionModel().setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());
				workFlowWrapper.getTransactionModel().setCustomerMobileNo(null);

				logger.info("[AgentToAgentTransferDispenser.doSale] checking Balance for SmartMoneyAcctID:" + sma.getSmartMoneyAccountId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
				switchWrapperTemp = phoenixFinancialInst.checkBalanceWithoutPin(switchWrapperTemp); //TODO this is modification
				
				workFlowWrapper.setTransactionModel(transactionModelTemp);
				//End checking balance before hitting for FT
				
				if(switchWrapperTemp != null && (switchWrapperTemp.getBalance() - workFlowWrapper.getTransactionModel().getTotalAmount() < 0)){
					throw new CommandException("Insufficient Account Balance.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
				
				
				SwitchWrapper switchWrapper = new SwitchWrapperImpl();
				switchWrapper.setBasePersistableModel(sma) ;
//				workFlowWrapper.setOlaSmartMoneyAccountModel(sma) ;
				switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
				
				switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
				switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
				switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
				switchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
				switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
				switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
				switchWrapper.setBasePersistableModel(sma);
				switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
				transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
				
				logger.info("[AgentToAgentTransferDispenser.doSale] Going to DebitCredit Phoenix Accounts. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
				
				phoenixFinancialInst.debitCreditAccount(switchWrapper);
								
				
				switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
				agentToAgentTransferVO.setBalance(switchWrapperTemp.getBalance()-workFlowWrapper.getTransactionModel().getTotalAmount()/* + agentToAgentTransferVO.getAmount()*/);
				switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode("00");
				switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
				switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());
				
			}else{
				throw new CommandException("Your Account is inactive.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}		
		catch (Exception e)
		{
			if(e instanceof CommandException)
			{
				
				throw new WorkFlowException(e.getMessage());
			}
			e.printStackTrace();
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_DOWN);
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doSale of cashinproductdispenser");
		}

		return workFlowWrapper;



	}


	/**
	 * getBillInfo
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		
		// check if funds transfer between these two agents is possible
		RetailerContactModel fromRetailerContactModel = new RetailerContactModel();
		RetailerContactModel toRetailerContactModel = new RetailerContactModel();
		toRetailerContactModel.setRetailerContactId(workFlowWrapper.getToRetailerContactAppUserModel().getRetailerContactId());
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(toRetailerContactModel);
		baseWrapper = retailerContactManager.loadRetailerContact(baseWrapper);
		if(null != baseWrapper.getBasePersistableModel())
		{
			toRetailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
			fromRetailerContactModel.setRetailerContactId(UserUtils.getCurrentUser().getRetailerContactId());
			baseWrapper.setBasePersistableModel(fromRetailerContactModel);
			baseWrapper = retailerContactManager.loadRetailerContact(baseWrapper);
			if(null != baseWrapper.getBasePersistableModel())
			{
				fromRetailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
				
				/* Commenting out below conditions to Open Agent Fund Transfer horizontally and vertically within the same and across multiple Agent Networks
				 
				if((fromRetailerContactModel.getRetailerIdRetailerModel().getDistributorId() != toRetailerContactModel.getRetailerIdRetailerModel().getDistributorId())
						|| (fromRetailerContactModel.getRetailerId() != toRetailerContactModel.getRetailerId()))
				{
					logger.error("[AgentToAgentTransferDispenser.getBillInfo] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Msg: Funds transfer to this agent is not possible");
					throw new WorkFlowException("Funds transfer to this agent is not possible\n");
				}*/
				
				/*Validation Checks for RSO Agents*/
				if(fromRetailerContactModel.getRso()){
					
				    SmartMoneyAccountModel recipientSmaModel = workFlowWrapper.getSmartMoneyAccountModel();
				   	
					//Recipient Agent App User Validations...
					if(false == isValidAppUserModel(workFlowWrapper.getToRetailerContactAppUserModel())){
						logger.error("[AgentToAgentTransferDispenser.getBillInfo] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Msg: Either Franchisee account is not linked or it is delinked/deleted.");
						throw new WorkFlowException("Retailer account is either not linked or it is delinked/deleted/closed.");
					}
					
					//Recipient Agent Smart Money Account Validations...
					if (false == isValidSmartMoneyAccount(recipientSmaModel)) {
						
						logger.error("[AgentToAgentTransferDispenser.getBillInfo] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Msg: Either Franchisee account is not linked or it is delinked/deleted.");
						throw new WorkFlowException("Retailer account is either not linked or it is delinked/deleted.");
					}
					
				}
				
				/*else{
					
					if(!fromRetailerContactModel.getHead() && !toRetailerContactModel.getHead() && !toRetailerContactModel.getRso())
					{
						logger.error("[AgentToAgentTransferDispenser.getBillInfo] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Msg: Funds Transfer between direct agents is not possible");
						throw new WorkFlowException("Funds Transfer between direct agents is not possible");
					}
				}*/
			}
		}
		// End check if funds transfer between these two agents is possible
		
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		SmartMoneyAccountModel smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
		
//		String pin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN);
//		String mPin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_MPIN);		
		
		AccountInfoModel accountInfoModel = new AccountInfoModel();
		accountInfoModel.setCustomerId(workFlowWrapper.getToRetailerContactAppUserModel().getAppUserId());
		
		accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
//		accountInfoModel.setOldPin(pin);

		LogModel logModel = new LogModel();
		logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
		logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
		
		if( null != switchWrapper.getWorkFlowWrapper() && switchWrapper.getWorkFlowWrapper().getTransactionModel() != null )
			logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());
//		else
			
		

		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		
		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
		veriflyBaseWrapper.setLogModel(logModel);
		
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		try
		{
			logger.info("[AgentToAgentTransferDispenser.getBillInfo] Going to verify Credentials for SmartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);
		}
		catch(Exception fex)
		{
			logger.error(fex.getStackTrace());
			throw new WorkFlowException("Recipient agent cannot be verified.\n");
		}
		boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

		if(!errorMessagesFlag){
			logger.error(veriflyBaseWrapper.getErrorMessage());
			throw new WorkFlowException("Recipient agent cannot be verified.");
		}
		
		return workFlowWrapper;
	}

	/**
	 * rollback
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		return null;
	}

	/**
	 * verify
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper verify(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		return workFlowWrapper;
	}

	public VeriflyBaseWrapper verifyCredentials(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception
	{
		VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)veriflyBaseWrapper.getBasePersistableModel();
		veriflyBaseWrapper.setBasePersistableModel(null);
		veriflyBaseWrapper = veriflyManager.verifyCredentials(veriflyBaseWrapper);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		return veriflyBaseWrapper;
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
	
	private boolean isValidSmartMoneyAccount(SmartMoneyAccountModel smartMoneyAccountModel)
	 {
	     boolean validated = false;
	     if( smartMoneyAccountModel != null && smartMoneyAccountModel.getActive() && !smartMoneyAccountModel.getDeleted() )
	     {
	         validated = true;
	     }
	     else
	     {
	         validated = false;
	         
	     }

	     return validated;
	 }

	private boolean isValidAppUserModel(AppUserModel headRetailerAppUserModel){
		
		boolean validated = false;
		if( headRetailerAppUserModel != null && false == headRetailerAppUserModel.getAccountClosedSettled() && false == headRetailerAppUserModel.getAccountClosedUnsettled()
					&& true == headRetailerAppUserModel.getAccountEnabled() &&  false == headRetailerAppUserModel.getAccountExpired() && false == headRetailerAppUserModel.getAccountLocked())
		{
			validated = true;
		}
		else
		{
			validated = false;

		}
		
		 return validated;
	}
	
	
	private boolean isValidAgentUserDeviceAccount(UserDeviceAccountsModel franchiseUserDeviceModel) throws FrameworkCheckedException
	 {
	     boolean validated = true;
	     try
	     {
	    		 if( franchiseUserDeviceModel == null || !franchiseUserDeviceModel.getAccountEnabled()
	    				 || franchiseUserDeviceModel.getAccountLocked()  || franchiseUserDeviceModel.getAccountExpired())
	    		 {
	    			 validated = false;
	    			 throw new WorkFlowException("Franchisee account[ID = " + franchiseUserDeviceModel.getUserId() + "] is deactivated/blocked.");
	    		 }
	     }
	     catch( Exception e )
	     {
	    	 validated = false;
	    	 throw new WorkFlowException("Franchisee account is deactivated/blocked.");
	     }
	         
	     return validated;
	 }

}

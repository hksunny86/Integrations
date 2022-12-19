package com.inov8.microbank.server.service.integration.dispenser;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.exception.WorkFlowExceptionTranslator;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
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
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.CashToCashVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.TransactionTypeConstants;

/**
 * @author kashif
 */
public class CashToCashDispenser extends BillPaymentProductDispenser {

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
	private ApplicationContext applicationContext;
	private AbstractFinancialInstitution olaFinancialInst ;
	private AbstractFinancialInstitution phoenixFinancialInst ;
	private SmartMoneyAccountDAO smartMoneyAccountDAO;
	private WorkFlowExceptionTranslator workflowExceptionTranslator;

	private final Log logger = LogFactory.getLog(this.getClass());

	public CashToCashDispenser(CommissionManager commissionManager,
			SmartMoneyAccountManager smartMoneyAccountManager, SettlementManager settlementManager, 
			ProductManager productManager, AppUserManager appUserManager, ProductUnitManager productUnitManager, 
									ShipmentManager shipmentManager, GenericDao genericDAO, ApplicationContext applicationContext) {
		
		this.commissionManager = commissionManager;
		this.smartMoneyAccountManager = smartMoneyAccountManager;
		this.settlementManager = settlementManager;
		this.productManager = productManager;
		this.appUserManager = appUserManager;
		this.productUnitManager = productUnitManager;
		this.shipmentManager = shipmentManager;
		this.genericDAO = genericDAO;
		this.appUserDao = (AppUserDAO) applicationContext.getBean("appUserDAO");
		this.applicationContext = applicationContext ;

		this.smartMoneyAccountDAO = (SmartMoneyAccountDAO) applicationContext.getBean("smartMoneyAccountDAO") ;
		this.olaFinancialInst = (OLAVeriflyFinancialInstitutionImpl) applicationContext.getBean("com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl") ;
		this.phoenixFinancialInst = (PhoenixFinancialInstitutionImpl) applicationContext.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl") ;
		super.setAuditLogModule((FailureLogManager) applicationContext.getBean("failureLogManager"));
		this.workflowExceptionTranslator = (WorkFlowExceptionTranslator) applicationContext.getBean("workflowExceptionTranslator") ;
	}
	
	
	public WorkFlowWrapper verify(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

		return workFlowWrapper;
	}

	
	public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws Exception {

		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doSale of cashinproductdispenser");
		}
		
		AppUserModel appUser = ThreadLocalAppUser.getAppUserModel();
		logger.info("[CashToCashDispenser.doSale] Preparing VO. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		
		try
		{
			CashToCashVO acctToCashVO = (CashToCashVO) workFlowWrapper.getProductVO();
						
			SmartMoneyAccountModel smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();

			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
				
			/*switchWrapper.setBasePersistableModel(smartMoneyAccountModel) ;
			workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel) ;
				
			switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
			switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1(""+smartMoneyAccountModel.getSmartMoneyAccountId()); //set customer's smart money account ID
			TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
				
			switchWrapper.setOlavo(new OLAVO());
			switchWrapper.getOlavo().setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);

			logger.info("[CashToCashDispenser.doSale] Going to Credit C2C Sundry A/C. Sender SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
			
			//credit Cash To Cash sundry account in OLA
			switchWrapper.setIsCashToCashLeg2(false);
			switchWrapper.getWorkFlowWrapper().setCashToCashLeg1(true);
			switchWrapper = olaFinancialInst.cashToCashTransaction(switchWrapper);
			workFlowWrapper.setOlaSwitchWrapper_2(switchWrapper);
				
			switchWrapper = new SwitchWrapperImpl();*/
			
			logger.info("[CashToCashDispenser.doSale] Preparing for Check balance. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			
			switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
			TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
			switchWrapper.setBasePersistableModel(smartMoneyAccountModel) ;
			switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
			switchWrapper.setTransactionAmount(workFlowWrapper.getTransactionAmount());
			
			
			/***************************************************************************************/
			/*************************Reverse Check Balance Order with FT***************************/
			/***************************************************************************************/
			switchWrapper.getWorkFlowWrapper().setCashToCashLeg1(true);
			switchWrapper.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
			
			logger.info("[CashToCashDispenser.doSale] Checking Agent Balance. Agent SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
			
			SwitchWrapper newSwitchWrapper = phoenixFinancialInst.checkBalance(switchWrapper);

			Double balanceBeforeFT = newSwitchWrapper.getBalance();
			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
			
			/***************************************************************************************/
			/*********************END Reverse Check Balance Order with FT***************************/
			/***************************************************************************************/
			
			
			
			switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setBasePersistableModel(smartMoneyAccountModel) ;
			switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
			switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
			switchWrapper.setTransactionAmount(workFlowWrapper.getTransactionAmount());
			switchWrapper.getWorkFlowWrapper().setCashToCashLeg1(true);
			
			logger.info("[CashToCashDispenser.doSale] Going to Debit/Credit Phoenix A/C. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Sender SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
			
			switchWrapper = phoenixFinancialInst.debitCreditAccount(switchWrapper) ;
			
			switchWrapper.getWorkFlowWrapper().getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
		    
			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;

			workFlowWrapper.setSwitchWrapper(switchWrapper);
			
			/* Now calculate balance by subtracting FT amount form Previous balance and set in VO for Sending agent SMS*/
			((CashToCashVO) (workFlowWrapper.getProductVO())).setBalance(balanceBeforeFT - switchWrapper.getTransactionAmount());
			
			/********************************************** sundry account debit moved after Phoenix calls ****************************/
			switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setBasePersistableModel(smartMoneyAccountModel) ;
			workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel) ;
				
			switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
			switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1(""+smartMoneyAccountModel.getSmartMoneyAccountId()); //set customer's smart money account ID
				
			switchWrapper.setOlavo(new OLAVO());
			switchWrapper.getOlavo().setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);

			logger.info("[CashToCashDispenser.doSale] Going to Credit C2C Sundry A/C. Sender SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
			
			//credit Cash To Cash sundry account in OLA
			switchWrapper.setIsCashToCashLeg2(false);
			switchWrapper.getWorkFlowWrapper().setCashToCashLeg1(true);
			switchWrapper = olaFinancialInst.cashToCashTransaction(switchWrapper);
			workFlowWrapper.setOlaSwitchWrapper_2(switchWrapper);
				
			/**************************************************************************************************************************/
			
			/*switchWrapper.getWorkFlowWrapper().setCashToCashLeg1(true);
			switchWrapper.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
			
			logger.info("[CashToCashDispenser.doSale] Checking Agent Balance. Agent SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
			
			SwitchWrapper newSwitchWrapper = phoenixFinancialInst.checkBalance(switchWrapper);

			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
			((CashToCashVO) (workFlowWrapper.getProductVO())).setBalance(newSwitchWrapper.getBalance());
			*/
		} catch(WorkFlowException e) {
			logger.error("[CashToCashDispenser.doSale] Exception occured for Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId() + " Exception Message:" + e.getMessage());
			throw new WorkFlowException(e.getMessage());			
		} catch(FrameworkCheckedException e) {			
			logger.error("[CashToCashDispenser.doSale] Exception occured for Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId() + " Exception Message:" + e.getMessage());
			throw new FrameworkCheckedException(e.getMessage());
		} catch (Exception e) {			
			logger.error("[CashToCashDispenser.doSale] Exception occured for Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId() + " Exception Message:" + e.getMessage());
			throw new WorkFlowException(e.getMessage());
		}

		return workFlowWrapper;
	}

	
	public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		
		return null;
	}

	
	@Override
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper)	throws FrameworkCheckedException {

		if(logger.isDebugEnabled()) {
			logger.debug("Inside getBillInfo of cashinproductdispenser");
		}
		
		CashToCashVO cashToCashVO = (CashToCashVO) workFlowWrapper.getProductVO();
		
		try{
			//verify throughput limits for walkin customer
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			switchWrapper.setBankId(workFlowWrapper.getSmartMoneyAccountModel().getBankId());
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
	
			OLAVO olavo = new OLAVO();
			olavo.setCustomerAccountTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);//Walk-in
			olavo.setBalance(workFlowWrapper.getTransactionAmount());
			olavo.setTransactionDateTime(new Date());
			switchWrapper.setOlavo(olavo);

			// Check Throughput Limit of Sender
			switchWrapper = verifyWalkinCustomerThroughputLimits(switchWrapper, workFlowWrapper.getSenderWalkinCustomerModel().getCnic(), TransactionTypeConstantsInterface.OLA_DEBIT);
			boolean isSenderBvsRequired = (Boolean)switchWrapper.getOlavo().getResponseCodeMap().get(TransactionTypeConstants.KEY_BVS_VAL);
			if (isSenderBvsRequired && workFlowWrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.USSD){
				throw new CommandException("This transaction can only be performed by agent having Biometric Device", ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.HIGH, new Throwable());
			}

			workFlowWrapper.putObject(CommandFieldConstants.IS_SENDER_BVS_REQUIRED, isSenderBvsRequired ? "1" : "0");

			// Check Throughput Limit of Receiver
			olavo.setCustomerAccountTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);//Walk-in
			olavo.setBalance(workFlowWrapper.getTransactionAmount());
			olavo.setTransactionDateTime(new Date());
			switchWrapper.setOlavo(olavo);
			switchWrapper = verifyWalkinCustomerThroughputLimits(switchWrapper, workFlowWrapper.getRecipientWalkinCustomerModel().getCnic(), TransactionTypeConstantsInterface.OLA_CREDIT);
			workFlowWrapper.putObject(CommandFieldConstants.IS_RECEIVER_BVS_REQUIRED, (Boolean)switchWrapper.getOlavo().getResponseCodeMap().get(TransactionTypeConstants.KEY_BVS_VAL) ? "1" : "0");
			
		} catch (Exception e) {
			throw translateException(e);
		}

		return workFlowWrapper;
	}
	
	
	private SwitchWrapper verifyWalkinCustomerThroughputLimits(SwitchWrapper switchWrapper, String walkInCNIC, String transactionTypeId) {

		logger.info("verifyWalkinCustomerThroughputLimits(...) "+walkInCNIC);
		
		switchWrapper.getOlavo().setTransactionTypeId(transactionTypeId);
		switchWrapper.getOlavo().setCnic(CommonUtils.maskWalkinCustomerCNIC(walkInCNIC));
		
		try {
			
			switchWrapper = olaFinancialInst.verifyWalkinCustomerThroughputLimits(switchWrapper);
			
		} catch (WorkFlowException e) {
			throw new WorkFlowException(e.getMessage());
		} catch (FrameworkCheckedException e) {
			throw new WorkFlowException(e.getMessage());
		} catch (Exception e) {
			throw new WorkFlowException(e.getMessage());
		}		
		return switchWrapper;
	}
	
	private WorkFlowException translateException(Exception ex){
		try{
			if(!StringUtil.isNullOrEmpty(ex.getMessage()) && StringUtil.isFailureReasonId(ex.getMessage())){
				Long failureReasonId = Long.parseLong(ex.getMessage());
				return this.workflowExceptionTranslator.translateWorkFlowException(new WorkFlowException(ex.getMessage()),FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
			}else{
				logger.error("Unexpected Error Occurred..." + ex.getMessage() , ex);
			}
		}catch(Exception e){
			logger.error("Exception while translating exception...", e);
		}
		throw new WorkFlowException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR));
	}
}
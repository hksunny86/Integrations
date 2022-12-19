package com.inov8.microbank.server.service.integration.dispenser;

/**
 * Project Name: 			iServ	
 * @author 					Jawwad Farooq
 * Creation Date: 			February 2008  			
 * Description:				
 */

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.IntegrationModuleConstants;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacadeImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.CreditCardPaymentVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

public class CreditCardPaymentDispenser extends BillPaymentProductDispenser
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
	private FailureLogManager auditLogModule;
	private ApplicationContext ctx;

	private AbstractFinancialInstitution phoenixFinancialInst ;

		
	private final Log logger = LogFactory.getLog(this.getClass());

	public CreditCardPaymentDispenser(CommissionManager commissionManager,
			SmartMoneyAccountManager smartMoneyAccountManager, SettlementManager settlementManager,
			ProductManager productManager, AppUserManager appUserManager,
			ProductUnitManager productUnitManager, ShipmentManager shipmentManager, GenericDao genericDAO,
			ApplicationContext ctx)
	{
		this.commissionManager = commissionManager;
		this.smartMoneyAccountManager = smartMoneyAccountManager;
		this.settlementManager = settlementManager;
		this.productManager = productManager;
		this.appUserManager = appUserManager;
		this.productUnitManager = productUnitManager;
		this.shipmentManager = shipmentManager;
		this.genericDAO = genericDAO ;
		this.ctx = ctx ;
		try
		{
			this.auditLogModule = (FailureLogManager)ctx.getBean("auditLogModuleFacade");
			super.setPostedTransactionReportFacade((PostedTransactionReportFacadeImpl)ctx.getBean("postedTransactionReportFacade"));
			this.phoenixFinancialInst = (PhoenixFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl") ;
			super.setStakeholderBankInfoManager((StakeholderBankInfoManager)ctx.getBean("stakeholderBankInfoManager"));
		}
		catch (BeansException e)
		{			
			e.printStackTrace();
		}
	}

	/**
	 * doSale for Customer & Agent Credit Card Bill Payment
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{		

		AppUserModel appUser = ThreadLocalAppUser.getAppUserModel();
		
		logger.info("[CreditCardPaymentDispenser.doSale] Preparing VO. Logged in AppUserID: " + appUser.getAppUserId());
		
		try
		{
			SmartMoneyAccountModel smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();

			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setBasePersistableModel(smartMoneyAccountModel) ;
			switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;

			StakeholderBankInfoModel poolBankInfoModel = new StakeholderBankInfoModel();
			SmartMoneyAccountModel sma = (SmartMoneyAccountModel)switchWrapper.getBasePersistableModel();
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

			if(sma.getCustomerId() != null && sma.getCustomerId() > 0){
				
				poolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
				searchBaseWrapper.setBasePersistableModel(poolBankInfoModel);
				
				poolBankInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
				switchWrapper.setFromAccountNo(poolBankInfoModel.getAccountNo());
			
			}else if(sma.getRetailerContactId() != null && sma.getRetailerContactId() > 0){
				
				StakeholderBankInfoModel stakeholderBankInfoModel = this.stakeholderBankInfoManager.loadDistributorStakeholderBankInfoModel(appUser);
				switchWrapper.setFromAccountNo(stakeholderBankInfoModel.getAccountNo());			
			
			}

			switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
			
			switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
			switchWrapper.setTransactionAmount(workFlowWrapper.getTransactionModel().getTransactionAmount());
			
			logger.info("[CreditCardPaymentDispenser.doSale] Going to Pay Credit Card Bill. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Sender SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
			
			switchWrapper = phoenixFinancialInst.creditCardBillPayment(switchWrapper) ;
			
			switchWrapper.getWorkFlowWrapper().getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
		    
			workFlowWrapper.setSwitchWrapper(switchWrapper);
			
		} catch(WorkFlowException e) {
			logger.error("[CreditCardPaymentDispenser.doSale] Exception occured for Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId() + " Exception Message:" + e.getMessage());
			throw new WorkFlowException(e.getMessage());			
		} catch(FrameworkCheckedException e) {			
			logger.error("[CreditCardPaymentDispenser.doSale] Exception occured for Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId() + " Exception Message:" + e.getMessage());
			throw new FrameworkCheckedException(e.getMessage());
		} catch (Exception e) {			
			logger.error("[CreditCardPaymentDispenser.doSale] Exception occured for Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " SmartMoneyAccountId: " + workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId() + " Exception Message:" + e.getMessage());
			throw new WorkFlowException(e.getMessage());
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
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of getBillInfo of CreditCardPaymentDispenser");
			
		}
		CreditCardPaymentVO creditCardVO = (CreditCardPaymentVO) workFlowWrapper.getProductVO();
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setConsumerNumber(creditCardVO.getConsumerNo());
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.CREDIT_CARD_BILL_INQUIRY_PHOENIX);
		
		if( workFlowWrapper.getTransactionCodeModel() != null ){
			creditCardVO.setTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());	
		}
		
		creditCardVO.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId()) ;
		
		SwitchController paymentServicesIntgModule = getRemoteServiceManager( workFlowWrapper.getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl() );
		
		if( paymentServicesIntgModule == null )
	        throw new WorkFlowException( WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH ) ;
		
		
		switchWrapper.setConsumerNumber(creditCardVO.getConsumerNo());
		
		PhoenixIntegrationMessageVO integrationMessageVO = new PhoenixIntegrationMessageVO();
		integrationMessageVO.setCardNumber(creditCardVO.getCardNumber());
		integrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
		
		try
		{
			if(workFlowWrapper.getTransactionCodeModel() != null && workFlowWrapper.getTransactionCodeModel().getCode() != null){
				integrationMessageVO.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
			}
			
			switchWrapper.setIntegrationMessageVO(integrationMessageVO);
			integrationMessageVO = (PhoenixIntegrationMessageVO) paymentServicesIntgModule.checkBalanceForCreditCard(integrationMessageVO);
			switchWrapper.setIntegrationMessageVO(integrationMessageVO);
			
			if (integrationMessageVO.getResponseCode() == null) {
				logger.error("[CreditCardPaymentDispenser.getBillInfo] PHOENIX is down.Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
						" Credit Card No: **********" + creditCardVO.getCardNumber().substring(10) + 
						"Exception --> " + ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);

			}else {
								
				if (integrationMessageVO.getResponseCode().equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
					
					creditCardVO.setDueAmount(Double.parseDouble(Formatter.formatDouble(Double.parseDouble(integrationMessageVO.getPreviousBalance()))));
					creditCardVO.setMinimumDueAmount(Double.parseDouble(Formatter.formatDouble(Double.parseDouble(integrationMessageVO.getMinPaymentDue()))));
					creditCardVO.setOutstandingDueAmount(Double.parseDouble(Formatter.formatDouble(Double.parseDouble(integrationMessageVO.getOutstandingBalance()))));
					creditCardVO.setDueDate(sdf.parse(integrationMessageVO.getDueDate()));
				
				} else {
					
					String msg = parseResponseCode(integrationMessageVO.getResponseCode(), Boolean.TRUE);
					logger.error("[CreditCardPaymentDispenser.getBillInfo] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
							" Credit Card No: **********" + creditCardVO.getCardNumber().substring(10) + 
							" Phoenix Response Code:" + integrationMessageVO.getResponseCode() +
							" Message --> " + msg);
					throw new WorkFlowException(msg);
				}			
			}
		}
		catch (RemoteAccessException e)
		{
			logger.error("[CreditCardPaymentDispenser.getBillInfo] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" Credit Card No: **********" + creditCardVO.getCardNumber().substring(10) + 
					" Exception --> " + e.getMessage());
			throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.SERVICE_DOWN_MSG ) ;
		}
		catch (WorkFlowException e)
		{
			logger.error("[CreditCardPaymentDispenser.getBillInfo] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" Credit Card No: **********" + creditCardVO.getCardNumber().substring(10) + 
					" Exception --> " + e.getMessage());

			throw new WorkFlowException( e.getLocalizedMessage() ) ;			
		}
		catch (Exception e)
		{
			logger.error("[CreditCardPaymentDispenser.getBillInfo] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" Credit Card No: **********" + creditCardVO.getCardNumber().substring(10) + 
					" Exception --> " + e.getMessage());

			throw new WorkFlowException( WorkFlowErrorCodeConstants.INTEGRATION_ERROR ) ;			
		}
		finally{
			if(baseWrapper != null)
			{
				createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
			}
		}
					
		if( integrationMessageVO == null ){
			throw new WorkFlowException( WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID ) ;
		}
		
		workFlowWrapper.setProductVO((ProductVO) creditCardVO);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of getBillInfo of CreditCardPaymentDispenser");
			
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
		return workFlowWrapper;
	}
	
	private SwitchController getRemoteServiceManager(String url)
	{
		return HttpInvokerUtil.getHttpInvokerFactoryBean(SwitchController.class, url);
	}
	
	protected String getPhoenixAmount(String strAmount)
	{
		String sign = StringUtils.left(strAmount, 1);
		strAmount = StringUtils.removeStart(strAmount, sign);
		double amount = Double.parseDouble(strAmount);
		amount = amount / 100;
		strAmount = sign + String.valueOf(amount);
		return strAmount;
	}
	
	public AuditLogModel auditLogBeforeCall(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO, String inputParam)
			throws WorkFlowException
	{
		AuditLogModel auditLogModel = new AuditLogModel();
		auditLogModel.setTransactionStartTime(new Timestamp(System.currentTimeMillis()));
		auditLogModel.setIntegrationModuleId(IntegrationModuleConstants.SWITCH_MODULE);

		//auditLogModel.setTransactionCodeId(phoenixIntegrationMessageVO.getTransactionCode());

		auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(inputParam,
				XPathConstants.PhoenixAuditLogInputParamLocationSteps));
		auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(auditLogModel);
		try
		{
			auditLogModel = (AuditLogModel) this.auditLogModule.auditLogRequiresNewTransaction(baseWrapper)
					.getBasePersistableModel();
		}
		catch (FrameworkCheckedException ex)
		{
			throw new WorkFlowException(ex.getMessage(), ex);
		}

		return auditLogModel;
	}
	
	public void auditLogAfterCall(AuditLogModel auditLogModel, String outputParam) throws WorkFlowException
	{
		auditLogModel.setTransactionEndTime(new Timestamp(System.currentTimeMillis()));
		BaseWrapper baseWrapper = new BaseWrapperImpl();

		auditLogModel.setOutputParam(outputParam);
		baseWrapper.setBasePersistableModel(auditLogModel);

		try
		{
			this.auditLogModule.auditLogRequiresNewTransaction(baseWrapper);
		}
		catch (FrameworkCheckedException ex)
		{
			throw new WorkFlowException(ex.getMessage(), ex);
		}
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
	
	private String parseResponseCode(String responseCode, Boolean isTransaction) {

		logger.info("========> Response code from PHOENIX : " + responseCode);
		
		String message = "";
		
		if (responseCode == null) {
					
			message = WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN;
			
		} else if(!responseCode.equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {

			if (isTransaction) {
				
				message = "phoenix.trans." + responseCode;
				
			} else {
				
				message = "phoenix.req." + responseCode;
			}
			
			try {
				
				message = MessageUtil.getMessage(message);
				
			} catch (NoSuchMessageException e) {
				
				message = WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG;
			}
		}
		
		return message;
	}	
}

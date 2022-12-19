package com.inov8.microbank.server.service.integration.dispenser;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.remoting.RemoteAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.productmodule.paymentservice.LescoLogModel;
import com.inov8.microbank.common.util.Day;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.IntegrationModuleConstants;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.TransactionConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacadeImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class NADRABillPaymentDispenser extends BillPaymentProductDispenser {
	
	private final Log logger = LogFactory.getLog(this.getClass());

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
	private ApplicationContext ctx;
	private StakeholderBankInfoManager stakeholderBankInfoManager;



	public NADRABillPaymentDispenser(CommissionManager commissionManager, SmartMoneyAccountManager smartMoneyAccountManager, SettlementManager settlementManager,
			ProductManager productManager, AppUserManager appUserManager, ProductUnitManager productUnitManager, ShipmentManager shipmentManager,
									 GenericDao genericDAO,ApplicationContext ctx) {
		
		this.commissionManager = commissionManager;
		this.smartMoneyAccountManager = smartMoneyAccountManager;
		this.settlementManager = settlementManager;
		this.productManager = productManager;
		this.appUserManager = appUserManager;
		this.productUnitManager = productUnitManager;
		this.shipmentManager = shipmentManager;
		this.genericDAO = genericDAO ;
		this.ctx = ctx;

		super.financialIntegrationManager = (FinancialIntegrationManager) ctx.getBean("financialIntegrationManager");
		this.setFailureLogManager((FailureLogManager) ctx.getBean("failureLogManager"));
		super.setAuditLogModule((FailureLogManager) ctx.getBean("failureLogManager"));
		this.setStakeholderBankInfoManager((StakeholderBankInfoManager)ctx.getBean("stakeholderBankInfoManager"));
		super.setStakeholderBankInfoManager(stakeholderBankInfoManager);
		super.setPhoenixFinancialInstitution((PhoenixFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl")) ;
		super.setPostedTransactionReportFacade((PostedTransactionReportFacadeImpl)ctx.getBean("postedTransactionReportFacade"));
	}
	
	public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		
		if(logger.isDebugEnabled()) {
			
			logger.debug("Start of doSale of NADRABillPaymentDispenser");
		}
		
		UtilityBillVO utilityBillVO = (UtilityBillVO) workFlowWrapper.getProductVO();
		
		if(utilityBillVO.isBillPaid()) {
			
			throw new WorkFlowException( WorkFlowErrorCodeConstants.BILL_ALREADY_PAID_MSG ) ;
		}

		LescoLogModel logModel = new LescoLogModel();
		logModel.setBillAmount(utilityBillVO.getBillAmount().longValue());
		logModel.setBillingMonth(utilityBillVO.getBillingMonth().toString());
		logModel.setCompanyCode(utilityBillVO.getCompanyCode());
		logModel.setCompanyName(utilityBillVO.getCompanyName());
		logModel.setConsumerNo(utilityBillVO.getConsumerNo());
		logModel.setCustomerAddress(utilityBillVO.getCustomerAddress());
		logModel.setCustomerName(utilityBillVO.getCustomerName());
		logModel.setDueDate(utilityBillVO.getDueDate());
		logModel.setMicrobankTxCode( workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() ) ;
		logModel.setMfsId(workFlowWrapper.getTransactionModel().getMfsId() ) ;
		logModel.setLateBillAmount(utilityBillVO.getLateBillAmount().longValue() ) ;
		logModel.setPaidAmount(workFlowWrapper.getTransactionModel().getTransactionAmount().longValue() ) ;
		logModel.setPaidDate(new Date()) ;
		
		this.genericDAO.createEntity(logModel);
	
		return doBillPaymentViaNADRA(workFlowWrapper);
	}	
	
	
	
	
	private WorkFlowWrapper doBillPaymentViaNADRA(WorkFlowWrapper workFlowWrapper) {
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		switchWrapper.setBasePersistableModel(workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		
		TransactionModel transactionModel = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
		
		try {
			
			workFlowWrapper = doBillPaymentWithoutQueue(workFlowWrapper);
		
		} catch(Exception ex) {

			logger.error(ex.getLocalizedMessage());
			
			if(ex instanceof CommandException) 
				throw new WorkFlowException(ex.getLocalizedMessage());
			else if (ex instanceof WorkFlowException)
				throw (WorkFlowException) ex;
			else if (ex instanceof IOException)
				throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			else if (ex instanceof RemoteAccessException)
				throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			else
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
		}
		
		switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModel ) ;
		
		return workFlowWrapper;
	}
	


	/**
	 * getBillInfo
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of getBillInfo method of NADRABillPaymentDispenser");
		}
		
		UtilityBillVO utilityBillVO = (UtilityBillVO) workFlowWrapper.getProductVO();
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setConsumerNumber(utilityBillVO.getConsumerNo());
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.BILL_INQUIRY_NADRA);
		
		utilityBillVO.setCompanyCode(utilityBillVO.getCompanyCode());
		utilityBillVO.setCompanyName(utilityBillVO.getCompanyName());
		
		if( workFlowWrapper.getTransactionCodeModel() != null ) {
			
			utilityBillVO.setTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());	
			utilityBillVO.setMfsId( ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId() ) ;
		}
		
		SwitchController paymentServicesIntgModule = getRemoteServiceManager( workFlowWrapper.getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl() );
		
		if(paymentServicesIntgModule == null) {
	        throw new WorkFlowException( WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH ) ;
		}
		
		prepairSwitchWrapper(workFlowWrapper.getProductModel().getProductId(), switchWrapper);
		
		PhoenixIntegrationMessageVO integrationMessageVO = new PhoenixIntegrationMessageVO();
		integrationMessageVO.setCompanyName(switchWrapper.getUtilityCompanyId());
		integrationMessageVO.setConsumerNumber(utilityBillVO.getConsumerNo());
		integrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
		
		try {
			
			if(workFlowWrapper.getTransactionCodeModel() != null && workFlowWrapper.getTransactionCodeModel().getCode() != null){
				integrationMessageVO.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
			}
			
			logger.info("[NADRABillPaymentDispenser.getBillInfo] Checking if bill is alredy paid. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ 
						" consumer No:" + utilityBillVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId());
			
			integrationMessageVO = (PhoenixIntegrationMessageVO) paymentServicesIntgModule.viewBill(integrationMessageVO) ;
			integrationMessageVO.setResponseCode(integrationMessageVO.getStatus());
			
			switchWrapper.setIntegrationMessageVO(integrationMessageVO);
			
			if(integrationMessageVO.getStatus() != null && (integrationMessageVO.getStatus().equalsIgnoreCase("Invalid Consumer Number"))) {
				
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID);
			
			} else if(integrationMessageVO.getBillStatus() != null && (integrationMessageVO.getBillStatus().equalsIgnoreCase("b"))) {
				
				throw new WorkFlowException(WorkFlowErrorCodeConstants.REFERENCE_NUMBER_BLOCKED);
			
			} else if (null != integrationMessageVO.getResponseCode() && "92".equals(integrationMessageVO.getResponseCode())) {
			
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID);
			
			} else if(integrationMessageVO.getBillStatus() != null && !(integrationMessageVO.getBillStatus().equalsIgnoreCase("NO SUCH BILL"))) {
				
				LescoLogModel lescoLogModel = new LescoLogModel();
				lescoLogModel.setConsumerNo( utilityBillVO.getConsumerNo());
				lescoLogModel.setBillingMonth( utilityBillVO.getBillingMonth());
				
				List<LescoLogModel> list = this.genericDAO.findEntityByExample(lescoLogModel, null);
				
				if(list != null && !list.isEmpty()) {
					
					utilityBillVO.setBillPaid(Boolean.TRUE);
				}
			}
			
			if (integrationMessageVO.getStatus() == null) {
				
				logger.error("[NADRABillPaymentDispenser.getBillInfo] PHOENIX is down.Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
						" consumer No:" + utilityBillVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() +
						"Exception --> " + ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
				
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
			}
			
			else {
				
				switchWrapper.setAmountPaid(integrationMessageVO.getDueDatePayableAmount());
				switchWrapper.setIntegrationMessageVO(integrationMessageVO);
				
				if (integrationMessageVO.getStatus().equalsIgnoreCase("ok")) {

					utilityBillVO.setCustomerName(integrationMessageVO.getConsumerName());

					String billingMonth = integrationMessageVO.getBillMonth();
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
					Date billingMonthDate = df.parse(billingMonth);
					LocalDate lc = new LocalDate(billingMonthDate);
					DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");
					String month = formatter.print(lc);
					
					utilityBillVO.setBillingMonth(month);
					
					utilityBillVO.setBillAmount(Double.parseDouble(Formatter.formatDouble(Double.parseDouble(integrationMessageVO.getBillAmount()))));
					Date paymentDate = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					paymentDate = sdf.parse(integrationMessageVO.getDueDate());
					utilityBillVO.setDueDate(paymentDate);
					utilityBillVO.setLateBillAmount(Double.parseDouble(Formatter.formatDouble(Double.parseDouble(integrationMessageVO.getLateAmount()))));
					utilityBillVO.setBillPaid(integrationMessageVO.getStatus().equalsIgnoreCase("Bill Does Not Exist")?true:false);
					DateTime dueDate = new DateTime(paymentDate).withTime(0, 0, 0, 0);
					
					DateTime nowDate = new DateTime().withTime(0, 0, 0, 0);
					
					if(nowDate.isAfter(dueDate.getMillis())) {
						
						utilityBillVO.setBillAmount(utilityBillVO.getLateBillAmount());
						switchWrapper.setAmountPaid(integrationMessageVO.getPaymentAfterDueDate());
					}
					
					
					if(workFlowWrapper.getTransactionDetailMasterModel() != null){
						workFlowWrapper.getTransactionDetailMasterModel().setBillDueDate(utilityBillVO.getDueDate());
					}


				} else {
					
					String msg = parseResponseCode(integrationMessageVO.getStatus());
					logger.error("[NADRABillPaymentDispenser.getBillInfo] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
							" consumer No:" + utilityBillVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() +
							" Phoenix Response Code:" + integrationMessageVO.getStatus() +
							"Message --> " + msg);
					logger.error(integrationMessageVO.getResponseCode()+" : "+msg);
					throw new WorkFlowException(msg);
				}			
			}
		
		} catch (RemoteAccessException e) {
			
			logger.error("[NADRABillPaymentDispenser.getBillInfo] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" consumer No:" + utilityBillVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() + 
					" Exception --> " + e.getMessage());
			
			throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.SERVICE_DOWN_MSG ) ;
		
		} catch (WorkFlowException e) {
			
			logger.error("[NADRABillPaymentDispenser.getBillInfo] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" consumer No:" + utilityBillVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() +" Exception --> " + e.getMessage());

			throw new WorkFlowException( e.getLocalizedMessage() ) ;			
		
		} catch (Exception e) {
			
			logger.error("[NADRABillPaymentDispenser.getBillInfo] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" consumer No:" + utilityBillVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() + " Exception --> " + e.getMessage());

			throw new WorkFlowException( WorkFlowErrorCodeConstants.INTEGRATION_ERROR ) ;			
		}
		
		finally {
			
			if(baseWrapper != null) {
//				createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper); kashefbasher
			}
		}
					
		if(integrationMessageVO == null) {
			
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID ) ;
		}
		
		workFlowWrapper.setProductVO((ProductVO) utilityBillVO);
		
		if(logger.isDebugEnabled()) {
			
			logger.debug("End of getBillInfo of NADRABillPaymentDispenser");
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
	public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		return null;
	}

	/**
	 * verify
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper verify(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		return workFlowWrapper;
	}

	
	private SwitchController getRemoteServiceManager(String url)
	{
		return HttpInvokerUtil.getHttpInvokerFactoryBean(SwitchController.class, url);
	}
	
	
	
	public String getMessageFromResource(String responseCode) {
		String message = null;

		try {
			if (responseCode != null && responseCode.length() > 0) {
				message = MessageUtil.getMessage(responseCode);
				if (message != null && message.length() > 0) {
					return message;
				} else {
					message = MessageUtil.getMessage(TransactionConstantsInterface.GENERIC_ERROR_MESSAGE);
				}
			} else {
				message = MessageUtil.getMessage(TransactionConstantsInterface.REQUEST_PROCESSING_FAILED);
			}
		} catch (Exception ex) {
			message = MessageUtil.getMessage(TransactionConstantsInterface.GENERIC_ERROR_MESSAGE);
		}

		return message;
	}
	
	
	
	public AuditLogModel auditLogBeforeCall(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO, String inputParam) throws WorkFlowException {
		
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
			auditLogModel = (AuditLogModel) this.failureLogManager.auditLogRequiresNewTransaction(baseWrapper)
					.getBasePersistableModel();
		}
		catch (FrameworkCheckedException ex)
		{
			throw new WorkFlowException(ex.getMessage(), ex);
		}
		
		return auditLogModel;
	}

	/**
	 * @param responseCode
	 * @param isTransaction
	 */
	private String parseResponseCode(String responseCode) {

		logger.info("========> Response code from NADRA : " + responseCode);
		
		String message = "";
		
		if (responseCode == null) {
					
			message = WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN;
			
		} else if(!responseCode.equalsIgnoreCase("ok")) {
			
			try {
				
				message = "Your request cannot be processed at the moment. Please try later.";
				
			} catch (NoSuchMessageException e) {
				
				message = WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG;
			}
		}
		
		return message;
	}	
	
	public static void main(String[] args) {
		LocalDate date = new LocalDate(YearMonth.YEAR,4,Day.today().getDayOfMonth());
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
		String month = formatter.print(date);
		System.out.println(month);
	}

	public void setFailureLogManager(FailureLogManager failureLogManager) {
		this.failureLogManager = failureLogManager;
	}

	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}

	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}

}

package com.inov8.microbank.server.service.integration.dispenser;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.productmodule.paymentservice.LescoLogModel;
import com.inov8.microbank.common.util.Day;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.IntegrationModuleConstants;
import com.inov8.microbank.common.util.InternetCompanyEnum;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentServicesCodeConstants;
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
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class InternetBillPaymentDispenser extends BillPaymentProductDispenser
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
	private ApplicationContext ctx;
	private StakeholderBankInfoManager stakeholderBankInfoManager;


	private final Log logger = LogFactory.getLog(this.getClass());

	public InternetBillPaymentDispenser(CommissionManager commissionManager,
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
		this.genericDAO = genericDAO ;

		this.ctx=ctx;
		
		this.setFailureLogManager((FailureLogManager) ctx.getBean("failureLogManager"));
		super.setAuditLogModule((FailureLogManager) ctx.getBean("failureLogManager"));
		this.setStakeholderBankInfoManager((StakeholderBankInfoManager)ctx.getBean("stakeholderBankInfoManager"));
		super.setStakeholderBankInfoManager(stakeholderBankInfoManager);
		super.setPhoenixFinancialInstitution((PhoenixFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl")) ;
		super.setPostedTransactionReportFacade((PostedTransactionReportFacadeImpl)ctx.getBean("postedTransactionReportFacade"));

		super.financialIntegrationManager = (FinancialIntegrationManager) ctx.getBean("financialIntegrationManager");
	}
	
	public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of InternetBillPaymentDispenser.doSale");
		}
		
		UtilityBillVO internetVO = (UtilityBillVO) workFlowWrapper.getProductVO();
		
		if(internetVO.isBillPaid()) {
			throw new WorkFlowException( WorkFlowErrorCodeConstants.BILL_ALREADY_PAID_MSG ) ;
		}
		
		workFlowWrapper = doBillPaymentWithoutQueue(workFlowWrapper);

		return workFlowWrapper;
	}	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * doSale
	 *
	 * @param workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws FrameworkCheckedException
	 */
	
	
	
	/*public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of doSale of InternetBillPaymentDispenser");
			
		}
		LescoLogModel lescoLogModel = new LescoLogModel();
		UtilityBillVO lescoVO = (UtilityBillVO) workFlowWrapper.getProductVO();
		SimpleDateFormat simpleDateFmt = new SimpleDateFormat("dd/MM/yy");
		
		if( lescoVO.isBillPaid() )
		{
			throw new WorkFlowException( WorkFlowErrorCodeConstants.BILL_ALREADY_PAID_MSG ) ;
		}
		
		lescoLogModel.setBillAmount( lescoVO.getBillAmount().longValue() ) ;
		lescoLogModel.setBillingMonth( lescoVO.getBillingMonth().toString() ) ;
		lescoLogModel.setCompanyCode( PaymentServicesCodeConstants.LESCO_CODE ) ;
		lescoLogModel.setCompanyName( PaymentServicesCodeConstants.LESCO_NAME ) ;
		lescoLogModel.setConsumerNo( lescoVO.getConsumerNo() ) ;
		lescoLogModel.setCustomerAddress( lescoVO.getCustomerAddress() ) ;
		lescoLogModel.setCustomerName( lescoVO.getCustomerName() ) ;
		
		//Change by Rizwan Because DueDate and Paid Date is converted in DATE From String datatype...
		
//		lescoLogModel.setDueDate( simpleDateFmt.format(lescoVO.getDueDate()) ) ;
		lescoLogModel.setDueDate( lescoVO.getDueDate());
		lescoLogModel.setMicrobankTxCode( workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() ) ;
		lescoLogModel.setMfsId( workFlowWrapper.getTransactionModel().getMfsId() ) ;
		lescoLogModel.setLateBillAmount( lescoVO.getLateBillAmount().longValue() ) ;
		lescoLogModel.setPaidAmount( workFlowWrapper.getTransactionModel().getTransactionAmount().longValue() ) ;
		lescoLogModel.setPaidDate( new Date() ) ;
		
		this.genericDAO.createEntity(lescoLogModel);		
//		workFlowWrapper.getFinancialTransactionsMileStones().setBillPaid(true);
		
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		if( ThreadLocalAppUser.getAppUserModel().getRetailerContactId() != null || 
				ThreadLocalAppUser.getAppUserModel().getDistributorContactId() != null )
		{
			workFlowWrapper = doBillPaymentWithoutQueue(workFlowWrapper);
		}
		else
		{
		
			JmsProducer jmsProducer = (JmsProducer)ctx.getBean("jmsProducer") ;
			
			KasbBillPaymentMessage kasbBillPaymentMessage = new KasbBillPaymentMessage();
			kasbBillPaymentMessage.setMicrobankTransactionCode( workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() ) ;
			kasbBillPaymentMessage.setMicrobankTransactionCodeId( workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getPrimaryKey() ) ;
			kasbBillPaymentMessage.setMerchantAccountNo( workFlowWrapper.getOperatorPayingBankInfoModel().getPayingAccountNo() ) ;
			kasbBillPaymentMessage.setUtilityCompanyCode( lescoLogModel.getCompanyCode() ) ;
			kasbBillPaymentMessage.setConsumerNo( lescoLogModel.getConsumerNo() ) ;
			kasbBillPaymentMessage.setTransactionAmount( workFlowWrapper.getTransactionModel().getTransactionAmount() ) ;
			kasbBillPaymentMessage.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
			kasbBillPaymentMessage.setServiceURL( workFlowWrapper.getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl() ) ;
			kasbBillPaymentMessage.setMfsId( ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId()) ;
			kasbBillPaymentMessage.setTransactionCode( workFlowWrapper.getTransactionCodeModel().getCode() ) ;
			
			if(logger.isDebugEnabled())
			{
				logger.debug("Publishing the message on "+DestinationConstants.KASB_BILL_PAYMENT_DESTINATION);
			}
			jmsProducer.produce(kasbBillPaymentMessage, DestinationConstants.KASB_BILL_PAYMENT_DESTINATION);
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		if(logger.isDebugEnabled())
		{
			logger.debug("End of doSale of InternetBillPaymentDispenser");
			
		}
		return workFlowWrapper;
	}
	
	
	*/

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
			logger.debug("Start of getBillInfo of InternetBillPaymentDispenser");
			
		}
		UtilityBillVO lescoVO = (UtilityBillVO) workFlowWrapper.getProductVO();
		
		
		
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setConsumerNumber(lescoVO.getConsumerNo());
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.BILL_INQUIRY_PHOENIX);
		
		
		
		lescoVO.setCompanyCode(lescoVO.getCompanyCode());
		lescoVO.setCompanyName(lescoVO.getCompanyName());
		if( workFlowWrapper.getTransactionCodeModel() != null )
			lescoVO.setTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());	
		lescoVO.setMfsId( ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId() ) ;
		
		SwitchController paymentServicesIntgModule = getRemoteServiceManager( workFlowWrapper.getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl() );
		//PaymentServicesIntgModule paymentServicesIntgModule = getRemoteServiceManager( workFlowWrapper.getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl() );
		
		if( paymentServicesIntgModule == null )
	        throw new WorkFlowException( WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH ) ;
		
		//SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		
//		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.LESCO.getValue()) 
//				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.LESCO_BILL.getValue()))
//		{
//			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.LESCO_CODE);
//		}
//		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.GEPCO.getValue())
//				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.GEPCO_BILL.getValue()))
//		{
//			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.GEPCO_CODE);
//		}
//		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.IESCO.getValue())
//				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.IESCO_BILL.getValue()))
//		{
//			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.IESCO_CODE);
//		}
//		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.HESCO.getValue())
//				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.HESCO_BILL.getValue()))
//		{
//			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.HESCO_CODE);
//		}
//		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.KESC.getValue())
//				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.KESC_BILL.getValue()))
//		{
//			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.KESC_CODE);
//		}
//		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.SNGPL.getValue())
//				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.SNGPL_BILL.getValue()))
//		{
//			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.SNGPL_CODE);
//		}
//		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.SSGC.getValue())
//				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.SSGC_BILL.getValue()))
//		{
//			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.SSGC_CODE);
//		}
//		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.PTCL.getValue())
//				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(UtilityCompanyEnum.PTCL_BILL.getValue()))
//		{
//			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.PTCL_CODE);
//		}
		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.WATEEN.getValue())
				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.WATEEN_BILL.getValue()))
		{
			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.WATEEN_CODE);
		}
		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.WITRIBE.getValue())
				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.WITRIBE_BILL.getValue()))
		{
			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.WITRIBE_CODE);
		}
		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.VFONE.getValue())
				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.VFONE_BILL.getValue()))
		{
			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.VFONE_PREPAID_CODE);
		}
		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.EVO_PREPAID.getValue())
				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.EVO_PREPAID_BILL.getValue()))
		{
			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.EVO_PREPAID_CODE);
		}
		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.EVO_POSTPAID.getValue())
				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.EVO_POSTPAID_BILL.getValue()))
		{
			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.EVO_POSTPAID_CODE);
		}
		
		if(workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.DEFAULTER.getValue())
				|| workFlowWrapper.getProductModel().getProductId().longValue() == Long.parseLong(InternetCompanyEnum.DEFAULTER_BILL.getValue()))
		{
			switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.PTCL_DEFAULTER_CODE);
		}
		
//		switchWrapper.setUtilityCompanyId(PaymentServicesCodeConstants.LESCO_CODE);
		switchWrapper.setConsumerNumber(lescoVO.getConsumerNo());
		PhoenixIntegrationMessageVO integrationMessageVO = new PhoenixIntegrationMessageVO();
		integrationMessageVO.setUtilityCompanyId(switchWrapper.getUtilityCompanyId());
		integrationMessageVO.setConsumerNumber(lescoVO.getConsumerNo());
		integrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
		
		try
		{
			/*Added by Mudassir againg bug 804*/
			if(workFlowWrapper.getTransactionCodeModel() != null && workFlowWrapper.getTransactionCodeModel().getCode() != null){
				integrationMessageVO.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
			}
			
			logger.info("[InternetBillPaymentDispenser.getBillInfo] Checking if bill is alredy paid. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ 
						" consumer No:" + lescoVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId());
			integrationMessageVO = (PhoenixIntegrationMessageVO) paymentServicesIntgModule.billInquiry(integrationMessageVO) ;
			switchWrapper.setIntegrationMessageVO(integrationMessageVO);
			
			if(integrationMessageVO.getBillStatus() != null && (integrationMessageVO.getBillStatus().equalsIgnoreCase("p")))
			{
				logger.error("[InternetBillPaymentDispenser.getBillInfo] Bill is alredy paid. Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
						" consumer No:" + lescoVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId());
				throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_ALREADY_PAID_MSG);
			}
			else if(integrationMessageVO.getBillStatus() != null && (integrationMessageVO.getBillStatus().equalsIgnoreCase("b")))
			{
				logger.error("[InternetBillPaymentDispenser.getBillInfo] Reference Number Blocked. Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
						" consumer No:" + lescoVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId());
			throw new WorkFlowException(WorkFlowErrorCodeConstants.REFERENCE_NUMBER_BLOCKED);
			}
			else if (null != integrationMessageVO.getResponseCode() && "92".equals(integrationMessageVO.getResponseCode()))
			{
				logger.error("[InternetBillPaymentDispenser.getBillInfo] Invalid Consumer Number. Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
						" consumer No:" + lescoVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId());
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID);
			}
			else if(integrationMessageVO.getBillStatus() != null && !(integrationMessageVO.getBillStatus().equalsIgnoreCase("p")))
			{
//				LescoLogModel lescoLogModel = new LescoLogModel();
//				lescoLogModel.setConsumerNo( lescoVO.getConsumerNo());
//				lescoLogModel.setBillingMonth( lescoVO.getBillingMonth());
//				List<LescoLogModel> list = this.genericDAO.findEntityByExample(lescoLogModel, null);
//				if(list != null && list.size() > 0)
//				{
//					lescoVO.setBillPaid(Boolean.TRUE);
//				}
				
			}
			
			if (integrationMessageVO.getResponseCode() == null) {
				logger.error("[InternetBillPaymentDispenser.getBillInfo] PHOENIX is down.Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
						" consumer No:" + lescoVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() +
						"Exception --> " + ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
			}
			
			else {
				
				switchWrapper.setAmountPaid(integrationMessageVO.getPaymentAfterDueDate());
				switchWrapper.setIntegrationMessageVO(integrationMessageVO);
			
				
				
				if (integrationMessageVO.getResponseCode().equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
					lescoVO.setCustomerName(integrationMessageVO.getSubscriberName());
					
//					LocalDate date = new LocalDate(YearMonth.YEAR,Integer.parseInt(integrationMessageVO.getBillingMonth()),Day.today().getDayOfMonth());
//					DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
//					String month = formatter.print(date);
					
					String billingMonth = integrationMessageVO.getBillingMonth();
					
					// In case of EVO_POSTPAID we are getting no date, in case of WiTribe, we are getting date in 0000 format
					// so now we are putting 'N/A' in billingMonth
					
					logger.info("billingMonth : -"+billingMonth+"-");
					
					billingMonth = (billingMonth != null && !billingMonth.equals("") && !billingMonth.equals("0000") 
							&& !billingMonth.equals("000") && !billingMonth.equals("00") && !billingMonth.equals("0")) ? billingMonth : null;
					
					if(billingMonth != null) {
					
						SimpleDateFormat df = null;
						
						if(PaymentServicesCodeConstants.EVO_POSTPAID_CODE.equalsIgnoreCase(switchWrapper.getUtilityCompanyId())) {
							
							
							df = new SimpleDateFormat("MMyy");
							
						} else {
							
							df = new SimpleDateFormat("yyMM");
						}
						
						Date billingMonthDate = df.parse(billingMonth);
						LocalDate lc = new LocalDate(billingMonthDate);
						DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");
						String month = formatter.print(lc);
						
						lescoVO.setBillingMonth(month);
						
					} else {
						
						lescoVO.setBillingMonth("N/A");
					}	
					
					
//					SimpleDateFormat df = new SimpleDateFormat("yyMM");
//					Date billingMonthDate = df.parse(billingMonth);
//					LocalDate lc = new LocalDate(billingMonthDate);
//					DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");
//					String month = formatter.print(lc);
//					
//					lescoVO.setBillingMonth(month);
					lescoVO.setBillAmount(Double.parseDouble(Formatter.formatDouble(Double.parseDouble(integrationMessageVO.getDueDatePayableAmount()))));
					Date paymentDate = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
					paymentDate = sdf.parse(integrationMessageVO.getPaymentDueDate());
					lescoVO.setDueDate(paymentDate);
					lescoVO.setLateBillAmount(Double.parseDouble(Formatter.formatDouble(Double.parseDouble(integrationMessageVO.getPaymentAfterDueDate()))));
					lescoVO.setBillPaid(integrationMessageVO.getBillStatus().equalsIgnoreCase("p")?true:false);
					DateTime dueDate = new DateTime(paymentDate).withTime(0, 0, 0, 0);
					
					DateTime nowDate = new DateTime().withTime(0, 0, 0, 0);
					
					if(nowDate.isAfter(dueDate.getMillis()))
					{
						lescoVO.setBillAmount(lescoVO.getLateBillAmount());
					}
					
					
//					switchWrapper.setPaymentAuthResponseId(integrationMessageVO.getPaymentAuthResponseId());
//					switchWrapper.setNetCED(integrationMessageVO.getNetCED());
//					switchWrapper.setNetWithholdingTAX(integrationMessageVO.getNetWithholdingTAX());
				} else {
					
					String msg = parseResponseCode(integrationMessageVO.getResponseCode(), Boolean.TRUE);
					logger.error("[InternetBillPaymentDispenser.getBillInfo] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
							" consumer No:" + lescoVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() +
							" Phoenix Response Code:" + integrationMessageVO.getResponseCode() +
							"Message --> " + msg);
					logger.error(integrationMessageVO.getResponseCode()+" : "+msg);
					throw new WorkFlowException(msg);
				}			
			}
		}
		catch (RemoteAccessException e)
		{
			logger.error("[InternetBillPaymentDispenser.getBillInfo] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" consumer No:" + lescoVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() + 
					" Exception --> " + e.getMessage());
			throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.SERVICE_DOWN_MSG ) ;
		}
		catch (WorkFlowException e)
		{
			logger.error("[InternetBillPaymentDispenser.getBillInfo] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" consumer No:" + lescoVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() +
					" Exception --> " + e.getMessage());

			throw new WorkFlowException( e.getLocalizedMessage() ) ;			
		}
		catch (Exception e)
		{
			logger.error("[InternetBillPaymentDispenser.getBillInfo] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" consumer No:" + lescoVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() +
					" Exception --> " + e.getMessage());

			throw new WorkFlowException( WorkFlowErrorCodeConstants.INTEGRATION_ERROR ) ;			
		}
		finally{
			if(baseWrapper != null)
			{
				createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
			}
		}
					
		if( integrationMessageVO == null )
			throw new WorkFlowException( WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID ) ;
		//if( lescoVO.getDueDate().before( DateUtils.getDayStartDate( new Date())) && !lescoVO.isBillPaid())
		//	throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_PAYMENT_AFT_DUE_DATE);
		
		
		
		workFlowWrapper.setProductVO((ProductVO) lescoVO);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of getBillInfo of InternetBillPaymentDispenser");
			
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

	/*private PaymentServicesIntgModule getRemoteServiceManager(String url)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of getRemoteServiceManager of InternetBillPaymentDispenser");
			
		}
		try
		{
			factory.setServiceUrl(url);
			factory.setServiceInterface(PaymentServicesIntgModule.class);
			factory.afterPropertiesSet();
			if(logger.isDebugEnabled())
			{
				logger.debug("End of getRemoteServiceManager of InternetBillPaymentDispenser");
				
			}
			return (PaymentServicesIntgModule) factory.getObject();
		}
		catch (Exception ex)
		{
			
			return null;
		}
	}
	*/
	
	private SwitchController getRemoteServiceManager(String url)
	{
		return HttpInvokerUtil.getHttpInvokerFactoryBean(SwitchController.class, url);
	}
	
	
	

	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
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
	
	public static void main(String[] args)
	{
		LocalDate date = new LocalDate(YearMonth.YEAR,4,Day.today().getDayOfMonth());
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
		String month = formatter.print(date);
		System.out.println(month);
	}

	public void setFailureLogManager(FailureLogManager failureLogManager) {
		this.failureLogManager = failureLogManager;
	}

	public void setStakeholderBankInfoManager(
			StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}


}

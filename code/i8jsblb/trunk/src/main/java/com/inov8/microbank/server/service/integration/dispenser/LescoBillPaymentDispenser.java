package com.inov8.microbank.server.service.integration.dispenser;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.inov8.framework.common.util.CustomList;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BillPaymentsDueDateModel;
import com.inov8.microbank.common.model.SafRepoCoreModel;
import com.inov8.microbank.common.util.*;
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
import com.inov8.integration.middleware.controller.MiddlewareSwitchController;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.productmodule.paymentservice.LescoLogModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacadeImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.middleware.MiddlewareSwitchImpl;
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
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

import java.net.ConnectException;
import java.util.List;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class LescoBillPaymentDispenser extends BillPaymentProductDispenser
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

	public LescoBillPaymentDispenser(CommissionManager commissionManager,
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
//		this.billpaymentsDueDateDAO = billpaymentsDueDateDAO;
		
		this.ctx=ctx;

		super.financialIntegrationManager = (FinancialIntegrationManager) ctx.getBean("financialIntegrationManager");
		
		this.setFailureLogManager((FailureLogManager) ctx.getBean("failureLogManager"));
		super.setAuditLogModule((FailureLogManager) ctx.getBean("failureLogManager"));
		this.setStakeholderBankInfoManager((StakeholderBankInfoManager)ctx.getBean("stakeholderBankInfoManager"));
		super.setSupplierBankInfoManager((SupplierBankInfoManager)ctx.getBean("supplierBankInfoManager"));
		
		super.setStakeholderBankInfoManager(stakeholderBankInfoManager);
		super.setPhoenixFinancialInstitution((PhoenixFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl")) ;
		super.setPostedTransactionReportFacade((PostedTransactionReportFacadeImpl)ctx.getBean("postedTransactionReportFacade"));
	}
	
	public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException	{
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of doSale of LescoBillPaymentDispenser");			
		}
		
		UtilityBillVO lescoVO = (UtilityBillVO) workFlowWrapper.getProductVO();
		
		if( lescoVO.isBillPaid()) {
			throw new WorkFlowException( WorkFlowErrorCodeConstants.BILL_ALREADY_PAID_MSG ) ;
		}

//		DateTime dueDate = new DateTime(lescoVO.getDueDate()).withTime(0, 0, 0, 0);

//		DateTime nowDate = new DateTime().withTime(0, 0, 0, 0);
//		if(nowDate.isAfter(dueDate.getMillis())) {
//
//			logger.info("\nBill Due Date : "+dueDate +"\nCurrent Date : "+nowDate);
//
//			BillPaymentsDueDateModel billPaymentsDueDateModel = new BillPaymentsDueDateModel();
//			billPaymentsDueDateModel.setProductId(workFlowWrapper.getProductModel().getProductId());
//			CustomList<BillPaymentsDueDateModel> customList = billpaymentsDueDateDAO.findByExample
//					(billPaymentsDueDateModel,null,null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
//			List<BillPaymentsDueDateModel> billPaymentsDueDateModelList= customList.getResultsetList();
//
//			if (billPaymentsDueDateModelList != null && billPaymentsDueDateModelList.size() > 0) {
//				throw new WorkFlowException(MessageUtil.getMessage("exceeded.due.date"));
//			}
//		}

		LescoLogModel lescoLogModel = new LescoLogModel();		
		lescoLogModel.setBillAmount( lescoVO.getBillAmount().longValue() ) ;
		lescoLogModel.setBillingMonth( lescoVO.getBillingMonth()) ;		
		lescoLogModel.setCompanyCode( lescoVO.getCompanyCode() ) ;
		lescoLogModel.setCompanyName( lescoVO.getCompanyName() ) ;
		lescoLogModel.setConsumerNo( lescoVO.getConsumerNo() ) ;
		lescoLogModel.setCustomerAddress( lescoVO.getCustomerAddress() ) ;
		lescoLogModel.setCustomerName( lescoVO.getCustomerName() ) ;		
		lescoLogModel.setDueDate( lescoVO.getDueDate());
		lescoLogModel.setMicrobankTxCode( workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() ) ;
		lescoLogModel.setMfsId( workFlowWrapper.getTransactionModel().getMfsId() ) ;
		lescoLogModel.setLateBillAmount( lescoVO.getLateBillAmount().longValue() ) ;
		lescoLogModel.setPaidAmount( workFlowWrapper.getTransactionModel().getTransactionAmount().longValue() ) ;
		lescoLogModel.setPaidDate( new Date() ) ;
		
		this.genericDAO.createEntity(lescoLogModel);		

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
			logger.debug("Start of doSale of LescoBillPaymentDispenser");
			
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
			logger.debug("End of doSale of LescoBillPaymentDispenser");
			
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
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of getBillInfo of LescoBillPaymentDispenser");	
		}
		
		UtilityBillVO utilityBillVO = (UtilityBillVO) workFlowWrapper.getProductVO();
		
		SwitchWrapper switchWrapper = workFlowWrapper.getSwitchWrapper();
		
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		
		Long productId = workFlowWrapper.getProductModel().getProductId();
		
		super.prepairSwitchWrapper(workFlowWrapper.getProductModel().getProductId(), switchWrapper);
		
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.BILL_INQUIRY_PHOENIX);
		
		utilityBillVO.setCompanyCode(utilityBillVO.getCompanyCode());
		utilityBillVO.setCompanyName(utilityBillVO.getCompanyName());
		
		if( workFlowWrapper.getTransactionCodeModel() != null )
			utilityBillVO.setTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());	
		
//		workFlowWrapper.getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().setUrl("http://10.0.1.40:9090/middleware-integration/ws/middlewareswitch");
		
		MiddlewareSwitchController middlewareSwitchController = (MiddlewareSwitchController) getRemoteServiceManager( workFlowWrapper.getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl() );
		
		if( middlewareSwitchController == null )
	        throw new WorkFlowException( WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH ) ;
		
		MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
		middlewareMessageVO.setAccountNo1(switchWrapper.getFromAccountNo());
		middlewareMessageVO.setAccountNo2(switchWrapper.getToAccountNo());
		middlewareMessageVO.setCnicNo(switchWrapper.getSenderCNIC());
		middlewareMessageVO.setConsumerNo(switchWrapper.getConsumerNumber());
		middlewareMessageVO.setCompnayCode(switchWrapper.getUtilityCompanyId());
		middlewareMessageVO.setBillCategoryId(switchWrapper.getUtilityCompanyCategoryId());
		middlewareMessageVO.setBillAggregator("01");

		logger.info("\nSending Bill Info Request"+"\n"+
		"A/C #   "+middlewareMessageVO.getAccountNo1()+"\n"+
		"CNIC #  "+middlewareMessageVO.getCnicNo()+"\n"+
		"Bill #  "+middlewareMessageVO.getConsumerNo()+"\n"+
		"Co Code "+middlewareMessageVO.getCompnayCode()+"\n"+
		"Ct Code "+middlewareMessageVO.getBillCategoryId()+"\n"+
		"MW URL  "+workFlowWrapper.getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl());
		
		try
		{
			if(workFlowWrapper.getTransactionCodeModel() != null && workFlowWrapper.getTransactionCodeModel().getCode() != null){
				middlewareMessageVO.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
			}
			
			switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

			middlewareMessageVO = (MiddlewareMessageVO) middlewareSwitchController.billInquiry(middlewareMessageVO) ;

//			middlewareMessageVO.setResponseCode("00");
//			middlewareMessageVO.setBillStatus("U");
//			middlewareMessageVO.setTransactionAmount("500");
//			middlewareMessageVO.setBillDueDate(new Date());
//			middlewareMessageVO.setAmountDueDate("500");
//			middlewareMessageVO.setAmountAfterDueDate("500");

			switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
			
			logger.info("Middleware Response code = '"+middlewareMessageVO.getResponseCode()+"' Bill Status" +middlewareMessageVO.getBillStatus());

			if( middlewareMessageVO == null ) {
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID, "135");
			}
			else if (null != middlewareMessageVO.getResponseCode() && "82".equals(middlewareMessageVO.getResponseCode())) {
				logger.error("Invalid Consumer Number No:" + utilityBillVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId());
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID,"135");
			}

			if (middlewareMessageVO.getResponseCode() == null) {
				
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
			}
			
			else {
				
				switchWrapper.setAmountPaid(middlewareMessageVO.getTransactionAmount());
				switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
							
				if (middlewareMessageVO.getResponseCode().equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {

					SimpleDateFormat df = new SimpleDateFormat("yyMM");
					Date billingMonthDate = middlewareMessageVO.getBillDueDate();

					utilityBillVO.setBillingMonth(CommonUtils.getBillingMonthFromDueDate(billingMonthDate));

					if(!StringUtil.isNullOrEmpty(middlewareMessageVO.getAmountDueDate())) {

						utilityBillVO.setBillAmount(Double.parseDouble(Formatter.formatDouble(Double.parseDouble(middlewareMessageVO.getAmountDueDate()))));
					}

					if(!StringUtil.isNullOrEmpty(middlewareMessageVO.getAmountAfterDueDate())) {

						utilityBillVO.setLateBillAmount(Double.parseDouble(Formatter.formatDouble(Double.parseDouble(middlewareMessageVO.getAmountAfterDueDate()))));
					}

					String billStatus = middlewareMessageVO.getBillStatus();

					logger.info("Got Bill Status = " +billStatus);

					if(!StringUtil.isNullOrEmpty(billStatus) &&
							(billStatus.trim().equalsIgnoreCase("U") ||
									billStatus.trim().equalsIgnoreCase("P") ||
										billStatus.trim().equalsIgnoreCase("B") ||
											billStatus.trim().equalsIgnoreCase("T")) ||
											(isValidMobileBillStatus(String.valueOf(productId), middlewareMessageVO))) {

						billStatus = middlewareMessageVO.getBillStatus();

						Boolean isBillPaid = (billStatus.equalsIgnoreCase("U") || billStatus.equalsIgnoreCase("T")) ? Boolean.FALSE : Boolean.TRUE;

						utilityBillVO.setBillPaid(isBillPaid);

						if(billStatus.trim().equalsIgnoreCase("P")) {

							throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_ALREADY_PAID_MSG,"131");

						} else if(billStatus.trim().equalsIgnoreCase("B")) {

							throw new WorkFlowException(WorkFlowErrorCodeConstants.REFERENCE_NUMBER_BLOCKED,"132");
						}

					} else {
						logger.error("Invalid Bill Status Found "+billStatus);
						throw new WorkFlowException("Invalid Bill Status Found '"+billStatus+"'");
					}

					Date billDueDate = middlewareMessageVO.getBillDueDate();
					utilityBillVO.setDueDate(billDueDate);

					logger.info("\nBill Due Date @i8 : "+billDueDate +"\nBill Due Date @RDV: "+middlewareMessageVO.getBillDueDate()+"\n"+middlewareMessageVO.toString());

					DateTime dueDate = new DateTime(billDueDate).withTime(0, 0, 0, 0);

					DateTime nowDate = new DateTime().withTime(0, 0, 0, 0);

//					if(nowDate.isAfter(dueDate.getMillis())) {
//
//						logger.info("\nBill Due Date : "+dueDate +"\nCurrent Date : "+nowDate);
//
//						BillPaymentsDueDateModel billPaymentsDueDateModel = new BillPaymentsDueDateModel();
//						billPaymentsDueDateModel.setProductId(productId);
//						CustomList<BillPaymentsDueDateModel> customList = billpaymentsDueDateDAO.findByExample
//								(billPaymentsDueDateModel,null,null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
//						List<BillPaymentsDueDateModel> billPaymentsDueDateModelList= customList.getResultsetList();
//
//						if (billPaymentsDueDateModelList != null && billPaymentsDueDateModelList.size() > 0) {
//							throw new WorkFlowException(MessageUtil.getMessage("exceeded.due.date"));
//						}
////						if(!isValidMobileBillStatus(String.valueOf(productId), middlewareMessageVO)) {
////
////							throw new WorkFlowException(MessageUtil.getMessage("exceeded.due.date"));
////						}
//					}

				} else {

					String msg = new MiddlewareSwitchImpl().getResponseCodeDetail(middlewareMessageVO.getResponseCode());

					logger.error(msg);
					throw new WorkFlowException(msg);
				}
			}
		}
		catch (RemoteAccessException e)
		{
			logger.error(e);
			throw new WorkFlowException(new MiddlewareSwitchImpl().getResponseCodeDetail("404"),"134");
		}
		catch (WorkFlowException e)
		{
			e.printStackTrace();
			logger.error("[LescoBillPaymentDispenser.getBillInfo] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" consumer No:" + utilityBillVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() +
					" Exception --> " + e.getMessage());

			throw new WorkFlowException( e.getLocalizedMessage() ) ;			
		}
		catch (Exception e)
		{
			logger.error(e);
			logger.error("[LescoBillPaymentDispenser.getBillInfo] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+
					" consumer No:" + utilityBillVO.getConsumerNo() + " Utility Company ID:" + switchWrapper.getUtilityCompanyId() +
					" Exception --> " + e.getMessage());

			if(e instanceof RemoteAccessException || e instanceof ConnectException) {
				throw new WorkFlowException(new MiddlewareSwitchImpl().getResponseCodeDetail("404"));	
			} else {
				throw new WorkFlowException( WorkFlowErrorCodeConstants.INTEGRATION_ERROR ) ;	
			}
		}
		finally
		{
			if(baseWrapper != null)
			{
				createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
			}
		}
					
		if( middlewareMessageVO == null )
			throw new WorkFlowException( WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID ) ;
		//if( lescoVO.getDueDate().before( DateUtils.getDayStartDate( new Date())) && !lescoVO.isBillPaid())
		//	throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_PAYMENT_AFT_DUE_DATE);
		
		
		
		workFlowWrapper.setProductVO((ProductVO) utilityBillVO);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of getBillInfo of LescoBillPaymentDispenser");
			
		}

		return workFlowWrapper;
	}

	
	private Boolean isValidMobileBillStatus(String productId, MiddlewareMessageVO middlewareMessageVO) {
		
		String billStatus = "";
		
		if(!StringUtil.isNullOrEmpty(middlewareMessageVO.getBillStatus())) {
			
			billStatus = middlewareMessageVO.getBillStatus().trim();
		}
		
		logger.info("\nFound Bill Status "+billStatus);
		
		Boolean isValidBillStatus = Boolean.FALSE;
		
		InternetCompanyEnum internetCompanyEnum = InternetCompanyEnum.lookup(productId);
		
		if(internetCompanyEnum != null && internetCompanyEnum.name().contains("POSTPAID") && ("T".equalsIgnoreCase(billStatus))) {
			
			isValidBillStatus = Boolean.TRUE;
			middlewareMessageVO.setBillStatus("T");
		}
		
		if(internetCompanyEnum != null && internetCompanyEnum.name().contains("PREPAID") && "T".equalsIgnoreCase(billStatus)) {
			
			isValidBillStatus = Boolean.TRUE;
			middlewareMessageVO.setBillStatus("T");
		}


		if(internetCompanyEnum != null && internetCompanyEnum.name().contains("U") && "U".equalsIgnoreCase(billStatus)) {

			isValidBillStatus = Boolean.TRUE;
			middlewareMessageVO.setBillStatus("T");
		}
		
		return isValidBillStatus;
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
			logger.debug("Start of getRemoteServiceManager of LescoBillPaymentDispenser");
			
		}
		try
		{
			factory.setServiceUrl(url);
			factory.setServiceInterface(PaymentServicesIntgModule.class);
			factory.afterPropertiesSet();
			if(logger.isDebugEnabled())
			{
				logger.debug("End of getRemoteServiceManager of LescoBillPaymentDispenser");
				
			}
			return (PaymentServicesIntgModule) factory.getObject();
		}
		catch (Exception ex)
		{
			
			return null;
		}
	}
	*/
	
	private Object getRemoteServiceManager(String url)
	{
		return HttpInvokerUtil.getHttpInvokerFactoryBean(MiddlewareSwitchController.class, url);
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

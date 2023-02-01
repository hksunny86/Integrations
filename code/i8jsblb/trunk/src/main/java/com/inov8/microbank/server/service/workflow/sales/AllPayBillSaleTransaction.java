package com.inov8.microbank.server.service.workflow.sales;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.OperatorBankInfoModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.UtilityCompanyEnum;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.P2PVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

public class AllPayBillSaleTransaction extends SalesTransaction {
	
	protected final Log log = LogFactory.getLog(getClass());
	private MessageSource messageSource;
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");


	SupplierManager supplierManager;

	CommissionManager commissionManager;

	SettlementManager settlementManager;

	private ProductDispenseController productDispenseController;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private CustTransManager customerManager;
	private SmsSender smsSender;
	private ProductManager productManager;
	private AppUserManager appUserManager;
	private VeriflyManagerService veriflyController;
	private SwitchController switchController;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private NotificationMessageManager notificationMessageManager;
	private SupplierBankInfoManager supplierBankInfoManager;
	private OperatorManager operatorManager;
	private RetailerContactManager retailerContactManager;	
	private FinancialIntegrationManager financialIntegrationManager;
	private GenericDao genericDAO;
	private AbstractFinancialInstitution phoenixFinancialInstitution;
	private String rrnPrefix;
	

	public void setNotificationMessageManager(
			NotificationMessageManager notificationMessageManager) {
		this.notificationMessageManager = notificationMessageManager;
	}

	public void setUserDeviceAccountsManager(
			UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public AllPayBillSaleTransaction() {
	}

	/**
	 * Pulls the bill information from the supplier system
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside getBillInfo(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction...");
			logger.debug("Loading ProductDispenser...");
		}
		BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
		logger.debug("Fetching Bill Info through Product Dispenser...");
		wrapper = billSaleProductDispenser.getBillInfo(wrapper);
		

		return wrapper;
	}

	/**
	 * Validate input from the user against the information pulled from the
	 * supplier
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper)
			throws FrameworkCheckedException {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside validateBillInfo(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction...");
		}
		BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
		wrapper =  billSaleProductDispenser.verify(wrapper);
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending validateBillInfo(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction...");
		}
			
		return wrapper;
		
	}

	/**
	 * This method calls the commission module to calculate the commission on
	 * this product and transaction.The wrapper should have product,payment mode
	 * and principal amount that is passed onto the commission module
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception
	{
		// ------ Calculate the commission
		// ------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction...");
		}
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue())
		{
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.setBasePersistableModel(custModel);
			bWrapper = customerManager.loadCustomer(bWrapper);
			if(null != bWrapper.getBasePersistableModel())
			{
				custModel = (CustomerModel) bWrapper.getBasePersistableModel();
				wrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
			}
		}
		else
		{
			SegmentModel segmentModel = new SegmentModel();
			segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
			wrapper.setSegmentModel(segmentModel);
		}
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		if(null != wrapper.getCustomerModel()){
			SegmentModel segmentModel=new SegmentModel();
			segmentModel=wrapper.getCustomerModel().getSegmentIdSegmentModel();
			wrapper.setSegmentModel(segmentModel);
		}
		
		RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
	    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction...");
		}
		return commissionWrapper;
	}

	/**
	 * 
	 * @param commissionHolder
	 *            CommissionAmountsHolder
	 * @param calculatedCommissionHolder
	 *            CommissionAmountsHolder
	 * @throws FrameworkCheckedException
	 */
	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateCommission of AllPayBillSaleTransaction...");
		}
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		BillPaymentVO productVO = (BillPaymentVO) workFlowWrapper.getProductVO();

		if (productVO.getCurrentBillAmount().doubleValue() != workFlowWrapper.getBillAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
		}
		if (commissionHolder.getTotalCommissionAmount().doubleValue() != workFlowWrapper.getTotalCommissionAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (commissionHolder.getTotalAmount().doubleValue() != workFlowWrapper.getTotalAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
		}
		if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
		{
			if (commissionHolder.getTotalAmount().doubleValue() < workFlowWrapper.getDiscountAmount())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
			}
		}

		if (this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue() != workFlowWrapper.getTxProcessingAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}
		if (logger.isDebugEnabled())
		{

			logger.debug("Ending validateCommission of AllPayBillSaleTransaction...");
		}

	}

	/**
	 * This method calls the settlement module to settle the payment amounts
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper) {
		return wrapper;
	}

	/**
	 * This method is responsible for inserting the data into the transaction
	 * tables
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) {
		return wrapper;
	}

	/**
	 * This method is responsible for updating the supplier with the information
	 * for example updating LESCO system with all the bill collections
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper updateSupplier(WorkFlowWrapper wrapper) {
		return wrapper;
	}

	/**
	 * Validates the user input
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws Exception
	 */

	public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

		// ------------------------Validates the Customer's requirements
		// -----------------------------------
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction");
		}
		
//		 --------------------- Validates the Retailer's requirements -----------------------------
	    if (wrapper.getFromRetailerContactModel() != null)
	    {
	      if (!wrapper.getFromRetailerContactModel().getActive())
	      {
	        throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NOT_ACTIVE);
	      }
//              if( wrapper.getFromRetailerContactModel().getBalance().doubleValue() < wrapper.getTransactionAmount().doubleValue() )
//              {
//                throw new WorkFlowException(WorkFlowErrorCodeConstants.INSUFFICIENT_RETAILER_BALANCE);
//              }
	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NULL);
	    }

		if (wrapper.getAppUserModel() != null)
		{
			if ("".equals(wrapper.getAppUserModel().getMobileNo()))
			{
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
			}
		}
		else
		{
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);
		}
		if (wrapper.getUserDeviceAccountModel() == null)
		{
			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
		}

		if (wrapper.getOlaSmartMoneyAccountModel() != null) 
		{
			if (!wrapper.getOlaSmartMoneyAccountModel().getActive()) 
			{
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);
			}			
		} 
		else
		{
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
		}

		// -------------------------------- Validates the Product's requirements
		// ---------------------------------
		if (wrapper.getProductModel() != null) {
			if (!wrapper.getProductModel().getActive()) {
		
				throw new WorkFlowException(
						WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
			}
		} else {
		
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
		}
		
		// -------------------------------- Validates the Product's requirements ---------------------------------------------------		
		if (wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT.longValue()) {
			if (!wrapper.getProductModel().getActive()) 
			{
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_SERVICE_TYPE);
			}
		}

		// ----------------------- Validates the Supplier's requirements
		// ---------------------------------------------
		if (wrapper.getProductModel().getSupplierIdSupplierModel() != null) {
			if (!wrapper.getProductModel().getSupplierIdSupplierModel()
					.getActive()) {
		
				throw new WorkFlowException(
						WorkFlowErrorCodeConstants.SUPPLIER_NOT_ACTIVE);
			}
		} else {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.SUPPLIER_NULL);
		}

		// ----------------------- Validates the Supplier Bank Info requirements
		// -----------------
		if (wrapper.getSupplierBankInfoModel() == null
				|| wrapper.getSupplierBankInfoModel().getAccountNo() == null
				|| "".equals(wrapper.getSupplierBankInfoModel().getAccountNo())) {
		
			logger.error("Either SupplierBankInfo or account No is null");
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.SUPPLIER_MERCH_NO_NULL);
		}
		// ---------------------------------------------------------------------------------------

		// ----------------------- Validates the operator Bank Info requirements
		// -----------------
		if (wrapper.getOperatorBankInfoModel() == null) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
		}
		if (wrapper.getOperatorPayingBankInfoModel() == null) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
		}
		// ---------------------------------------------------------------------------------------

		// ------------------------- Validates the iNPUT's requirements
		// -----------------------------------
		if (wrapper.getBillAmount() < 0) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
		}
		if (wrapper.getTotalAmount() < 0) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
		}
		if (wrapper.getTotalCommissionAmount() < 0) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
		}
		if (wrapper.getTxProcessingAmount() < 0) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
		}

		// ----------------------- Validates the PaymentMode's requirements
		// --------------------------------------
		if (wrapper.getPaymentModeModel() != null) {
			if (wrapper.getPaymentModeModel().getPaymentModeId() <= 0) {
		
				throw new WorkFlowException("PaymentModeID is not supplied.");
			}
		}

		// ----------------------- Validates the Service's requirements
		// ---------------------------
		if (wrapper.getProductModel().getServiceIdServiceModel() != null) {
			if (!wrapper.getProductModel().getServiceIdServiceModel()
					.getActive()) {
		
				throw new WorkFlowException(
						WorkFlowErrorCodeConstants.SERVICE_INACTIVE);
			}
		} else {
		
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
		}
		
		if(logger.isDebugEnabled())
		{
			
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction");
		}
		return wrapper;
	}

	/**
	 * Method respponsible for processing ther Bill Sale transaction
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

	public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception
	{
 		TransactionDetailModel txDetailModel = new TransactionDetailModel();
		String txAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTransactionAmount());
		String seviceChargesAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTransactionAmount());

		/**
		 * pull the bill information from the supplier, calculate commissions on
		 * the bill and return in to iPos for the customer to accept or reject
		 */
	/*	System.out.println();
		long start = System.currentTimeMillis();
		for (int i = 0; i<30;i++)
		{
			smsSender.send(new SmsMessage("03214447088", "test"));
		}
		long difference = System.currentTimeMillis()- start;
		System.out.println(difference);
		System.out.println();
		*/
		
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Inside doSale(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction..");
			}
			
			if (logger.isDebugEnabled())
			{
				logger.debug("Saving Transaction in DB....");
			}
			
			wrapper.getTransactionModel().setConfirmationMessage(" _ ");
			wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());

			
			wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.FAILED );
			
			// Set Handler Detail in Transaction and Transaction Detail Master
			if(wrapper.getHandlerModel() != null){
					wrapper.getTransactionModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
					wrapper.getTransactionModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
					wrapper.getTransactionDetailMasterModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
					wrapper.getTransactionDetailMasterModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
			}
			txManager.transactionRequiresNewTransaction(wrapper);
			
			//for walkin customer cash deposit
			if(null != wrapper.getWalkInCustomerMob() && !"".equals(wrapper.getWalkInCustomerMob()))
			{
				txDetailModel.setCustomField6(wrapper.getWalkInCustomerMob());
				
			}
			if(null != wrapper.getWalkInCustomerCNIC() && !"".equals(wrapper.getWalkInCustomerCNIC()))
			{
				txDetailModel.setCustomField7(wrapper.getWalkInCustomerCNIC());
			}
			
			
			wrapper = this.getBillInfo(wrapper);
			// this.validateBillInfo(wrapper); //validate bill info against the
			// one coming from iPos to see if they match

			CommissionWrapper commissionWrapper = null;
			CommissionAmountsHolder commissionAmounts = null;
			
			if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() == 50002L)
			{
				if(null != wrapper.getWalkInCustomerCNIC() && !"".equals(wrapper.getWalkInCustomerCNIC())){
					
					if (wrapper.getCustomerAppUserModel().getNic().equals(wrapper.getWalkInCustomerCNIC())) {
						
						commissionAmounts = new CommissionAmountsHolder(true);//initialize with default zero values.
						commissionAmounts.setTransactionAmount(wrapper.getTransactionModel().getTransactionAmount());
						commissionAmounts.setTotalAmount(wrapper.getTransactionModel().getTransactionAmount());
						commissionAmounts.setBillingOrganizationAmount(wrapper.getTransactionModel().getTransactionAmount());
						
					}else{//commissions applied only if a depositor is NOT a BB Customer.(Umer Saleem: 27-Jan-14) 
						commissionWrapper = this.calculateCommission(wrapper);
						commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
							CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
					}
				}else{
					commissionAmounts = new CommissionAmountsHolder(true);//initialize with default zero values.
					commissionAmounts.setTransactionAmount(wrapper.getTransactionModel().getTransactionAmount());
					commissionAmounts.setTotalAmount(wrapper.getTransactionModel().getTransactionAmount());
					commissionAmounts.setBillingOrganizationAmount(wrapper.getTransactionModel().getTransactionAmount());
					
				}
				
			}else {
				
				commissionWrapper = this.calculateCommission(wrapper);
				commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
						CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			}
			
			//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
			if(null != wrapper.getFromRetailerContactModel() && wrapper.getFromRetailerContactModel().getHead()){
				commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
				commissionAmounts.setFranchise1CommissionAmount(0.0d);
			}
			
			ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
			productDeviceFlowModel.setProductId(wrapper.getProductModel().getPrimaryKey());
			productDeviceFlowModel.setDeviceTypeId( DeviceTypeConstantsInterface.MOBILE );
			
			List<ProductDeviceFlowListViewModel> list = this.genericDAO.findEntityByExample(productDeviceFlowModel, null);

			if( list != null && list.size() > 0 )
			{
				productDeviceFlowModel = list.get(0);	
				wrapper.setProductDeviceFlowModel(productDeviceFlowModel);
			}
			
			if ( productDeviceFlowModel != null && productDeviceFlowModel.getDeviceFlowId() != null && 
					productDeviceFlowModel.getDeviceFlowId().longValue() != 17 )
			{
				this.validateCommission(commissionWrapper, wrapper); // validate
			}

			wrapper.setCommissionAmountsHolder(commissionAmounts);

			wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount()+commissionAmounts.getFedCommissionAmount());
			wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());

			txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
			txDetailModel.setProductIdProductModel(wrapper.getProductModel());
			txDetailModel.setConsumerNo(((BillPaymentVO) wrapper.getProductVO()).getConsumerNo());
			wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
//			if (wrapper.getUserDeviceAccountModel().getCommissioned())
//			{
//				ProductModel productModel = wrapper.getProductModel();
//				wrapper.setDiscountAmount(productModel.getFixedDiscount() + (productModel.getPercentDiscount() / 100) * commissionAmounts.getTotalAmount());
//				wrapper.getTransactionModel().setDiscountAmount(wrapper.getDiscountAmount());
//			}
//			if (null != wrapper.getDiscountAmount() && wrapper.getDiscountAmount() > wrapper.getTransactionModel().getTotalAmount())
//			{
//				throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
//			}

			wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(wrapper.getCustomerAccount().getNumber(), 5, "*"));

			
			
			txAmount = Formatter.formatDouble(wrapper.getTransactionModel().getTransactionAmount());
			seviceChargesAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTransactionAmount());

			txDetailModel.setSettled(false);
			wrapper.setTransactionDetailModel(txDetailModel);
			wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
			
			

			if (!wrapper.getTransactionDetailModel().getConsumerNo().equalsIgnoreCase(""))
				wrapper.getTransactionModel().setConfirmationMessage(
						SMSUtil.buildBillSaleSMS(wrapper.getInstruction().getSmsMessageText(), wrapper.getProductModel().getName(), txAmount, seviceChargesAmount,
								wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), wrapper.getTransactionDetailModel().getConsumerNo()));

			else
				wrapper.getTransactionModel().setConfirmationMessage(
						SMSUtil.buildVariableProductSMS(wrapper.getInstruction().getSmsMessageText(), txAmount, wrapper.getTransactionModel()
								.getTransactionCodeIdTransactionCodeModel().getCode(), wrapper.getProductModel().getHelpLineNotificationMessageModel()
								.getSmsMessageText()));

			

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			//Added  by Maqsood for Phoenix
			SmartMoneyAccountModel sma = wrapper.getOlaSmartMoneyAccountModel();
			SmartMoneyAccountModel agentSmartMoneyAccountModel = sma;
			//-----------
			
			baseWrapper.setBasePersistableModel(sma);
			wrapper.setSmartMoneyAccountModel(sma);
			
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setWorkFlowWrapper(wrapper);
			switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());

			/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Moved dosale here.....afraid 12/05/12*/
			
			if (logger.isDebugEnabled())
			{
				logger.debug("Executing Bill Sale on BillSaleProductDispenser....");
			}
			TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
			// Calls the integration party
			
//			if(wrapper.getProductModel().getProductId() == 50002L)
//			{
//				wrapper = billSaleProductDispenser.doSale(wrapper);
////			}
//			/*************************************************************/
//			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
			
			
			double balance = 0 ;
			transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
			
			//Moving check balance before FT
			
			if ( (productDeviceFlowModel != null && productDeviceFlowModel.getDeviceFlowId() != null && 
					productDeviceFlowModel.getDeviceFlowId().longValue() == 17) || UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())))
			{
				SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
				
				switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
				switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
				switchWrapperTemp.setWorkFlowWrapper(wrapper);
				switchWrapperTemp.setTransactionTransactionModel(wrapper.getTransactionModel());
				switchWrapperTemp.setBasePersistableModel( agentSmartMoneyAccountModel ) ;
				if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() == 50002L)
				{
					wrapper.getTransactionModel().setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());
					wrapper.getTransactionModel().setCustomerMobileNo(null);
//					switchWrapperTemp = abstractFinancialInstitution.checkBalanceWithoutPin(switchWrapperTemp); TODO this is original - Commented by mudassir as it seems wrong logic. 

					switchWrapperTemp = phoenixFinancialInstitution.checkBalanceWithoutPin(switchWrapperTemp); //TODO this is modification
				}
				else if(UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())))
				{
					
					wrapper.getTransactionModel().setCustomerMobileNo(null);
//					abstractFinancialInstitution = phoenixFinancialInstitution;
					logger.info("[AllPayBillSaleTransaction.dosale]Checking Agent Balance. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
					switchWrapperTemp = phoenixFinancialInstitution.checkBalance(switchWrapperTemp);
					transactionModelTemp.setProcessingSwitchIdSwitchModel(switchWrapperTemp.getSwitchSwitchModel());
					if(switchWrapperTemp.getBalance() < transactionModelTemp.getTotalAmount())
					{
						String response = this.getMessageSource().getMessage("MINI.InsufficientBalance", null, null) ; 
						logger.error("[AllPayBillSaleTransaction.doSale] Exception thrown for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Response:" + response);
						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);		 
					}
					
				}
				else
				{
					switchWrapperTemp = abstractFinancialInstitution.checkBalance(switchWrapperTemp);
				}
				balance = switchWrapperTemp.getBalance() ;
				
				switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
				wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(switchWrapperTemp.getAccountInfoModel().getAccountNo(), 5, "*"));
//				switchWrapper.setAgentBalance(balance);
			
			}
			
			
			transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
			// Calls the integration party
			
//			if(wrapper.getProductModel().getProductId() == 50002L)
//			{
			
			//Moving SMS before Bill Payment to avoid any JMS related issue 22nd May 2013
			
			
			if(UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())))
			{
				
				if(wrapper.getHandlerModel() == null  ||
						(wrapper.getHandlerModel() != null && wrapper.getHandlerModel().getSmsToAgent())){

					String agentSMS=this.getMessageSource().getMessage(
							"USSD.AgentBillPaymentSMS",
							new Object[] {
									Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
									wrapper.getProductModel().getName(),
									wrapper.getTransactionDetailModel().getConsumerNo(),
									dtf.print(new DateTime()),
									tf.print(new LocalTime()),
									wrapper.getTransactionCodeModel().getCode()
									},
									null);
					wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
					this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS));
					SmsMessage message=new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS);
					message.setMobileNo(wrapper.getAppUserModel().getMobileNo());
					message.setMessageType("ZINDIGI");
					message.setTitle("Agent Bill Payment");
					this.smsSender.pushNotification(message);
				}

				if(wrapper.getHandlerModel() != null && wrapper.getHandlerModel().getSmsToHandler()){

					String handlerSMS=this.getMessageSource().getMessage(
							"USSD.AgentBillPaymentSMS",
							new Object[] {
									Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
									wrapper.getProductModel().getName(),
									wrapper.getTransactionDetailModel().getConsumerNo(),
									dtf.print(new DateTime()),
									tf.print(new LocalTime()),
									wrapper.getTransactionCodeModel().getCode()
									},
									null);
					this.smsSender.send(new SmsMessage(wrapper.getHandlerAppUserModel().getMobileNo(), handlerSMS));
				}

				String customerSMS=this.getMessageSource().getMessage(
						"USSD.CustomerBillPaymentNotificationforAgent",
						new Object[] {
								Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
								wrapper.getProductModel().getName(),
								wrapper.getTransactionDetailModel().getConsumerNo(),
								dtf.print(new DateTime()),
								tf.print(new LocalTime()),
								wrapper.getTransactionCodeModel().getCode()
								//,wrapper.getAppUserModel().getMobileNo()
								},
								null);
				wrapper.getTransactionDetailModel().setCustomField8(customerSMS);
				wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getWalkInCustomerMob());
				wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
				this.smsSender.send(new SmsMessage(wrapper.getWalkInCustomerMob(), customerSMS));
				SmsMessage message = new SmsMessage(wrapper.getWalkInCustomerMob(), customerSMS);
				message.setMessageText(customerSMS);
				message.setMobileNo(wrapper.getWalkInCustomerMob());
				message.setMessageType("ZINDIGI");
				message.setTitle("Customer Bill Payment");
				this.smsSender.pushNotification(message);
			}
				
				//End moving SMS before Bill Payment 22nd May 2013
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
			//Get RRN Prefix befor going for FT to use in rollback in case transactino fails.
			rrnPrefix = buildRRNPrefix();
			
			wrapper = billSaleProductDispenser.doSale(wrapper);
//			}
			/*************************************************************/
			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
			
			//The following code is for Phoenix implementation
			switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
			switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
			switchWrapper.setBasePersistableModel(sma);
			switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()-switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
			//-----------------------------------------------------
			if(null != sma.getPaymentModeId() && sma.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue())
			{
				if(wrapper.getProductModel().getProductId() == 50002L)
				{
					sendCashDepositSMS(wrapper);
					logger.info("[AllPayBillSaleTransaction.dosale] Debit Agent Account and Credit Customer Pool Account. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
//					abstractFinancialInstitution = phoenixFinancialInstitution;
					switchWrapper = phoenixFinancialInstitution.debitCreditAccount(switchWrapper);
					switchWrapper.setAgentBalance(balance-switchWrapper.getTransactionAmount());
//					wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(switchWrapper.getAccountInfoModel().getAccountNo(), 5, "*"));
					
				}
				else
				{
					CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
							CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
					switchWrapper.setAgentBalance((balance-switchWrapper.getTransactionAmount())-commissionHolder.getAgent1CommissionAmount());
				}
			}
			else
			{
				switchWrapper = abstractFinancialInstitution.transaction(switchWrapper) ;
			}
			
			
			
			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
//			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
//			if ( (productDeviceFlowModel != null && productDeviceFlowModel.getDeviceFlowId() != null && 
//					productDeviceFlowModel.getDeviceFlowId().longValue() == 17) || UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())))
//			{
//				System.out.println();
//				SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
//				
//				switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
//				switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
//				switchWrapperTemp.setWorkFlowWrapper(wrapper);
//				switchWrapperTemp.setTransactionTransactionModel(wrapper.getTransactionModel());
//				switchWrapperTemp.setBasePersistableModel( agentSmartMoneyAccountModel ) ;
//				if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() == 50002L)
//				{
//					wrapper.getTransactionModel().setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());
//					wrapper.getTransactionModel().setCustomerMobileNo(null);
////					switchWrapperTemp = abstractFinancialInstitution.checkBalanceWithoutPin(switchWrapperTemp); TODO this is original - Commented by mudassir as it seems wrong logic. 
//
//					switchWrapperTemp = phoenixFinancialInstitution.checkBalanceWithoutPin(switchWrapperTemp); //TODO this is modification
//				}
//				else if(UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())))
//				{
//					
//					wrapper.getTransactionModel().setCustomerMobileNo(null);
////					abstractFinancialInstitution = phoenixFinancialInstitution;
//					switchWrapperTemp = phoenixFinancialInstitution.checkBalance(switchWrapperTemp);
//					transactionModelTemp.setProcessingSwitchIdSwitchModel(switchWrapperTemp.getSwitchSwitchModel());
//					
//				}
//				else
//				{
//					switchWrapperTemp = abstractFinancialInstitution.checkBalance(switchWrapperTemp);
//				}
//				balance = switchWrapperTemp.getBalance() ;
//				
//				switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
//				wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(switchWrapperTemp.getAccountInfoModel().getAccountNo(), 5, "*"));
//				switchWrapper.setAgentBalance(balance);
//			
//			}
				
			wrapper.setSwitchWrapper(switchWrapper);
			if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() != 50002L) //setting account info model in case of bill payment
			{
				wrapper.setAccountInfoModel(switchWrapper.getAccountInfoModel());
			}
			// this.settlementManager.settleBankPayment(commissionWrapper,
			// wrapper);

			txDetailModel.setSettled(true);
			
			if(wrapper.getFirstFTIntegrationVO() != null) {
				
				String bankResponseCode = wrapper.getFirstFTIntegrationVO().getResponseCode();
				
				if(bankResponseCode != null) {
					wrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
				}
			}			
			
			txManager.saveTransaction(wrapper);
			
			if (logger.isDebugEnabled())
			{
				logger.debug("Going to settle commissions using SettlementManager....");
			}

			if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() == 50002L
				&& (null != wrapper.getWalkInCustomerCNIC() && ! "".equals(wrapper.getWalkInCustomerCNIC()))
					&& false == wrapper.getWalkInCustomerCNIC().equals(wrapper.getCustomerAppUserModel().getNic())){
				
				this.settlementManager.settleCommission(commissionWrapper, wrapper);
				
			}else if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() != 50002L){
				
				this.settlementManager.settleCommission(commissionWrapper, wrapper);
				
			}
			
			if (logger.isDebugEnabled())
			{
				logger.debug("Going to settle Bank Payment using SettlementManager....");
			}

			
			this.settleAmount(wrapper); // settle all amounts to the respective
			// stakeholders
			if(wrapper.getProductModel().getProductId().longValue() == 50002L && (null != sma.getPaymentModeId() && sma.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue()) && wrapper.getTotalCommissionAmount() > 0)
			{
				logger.info("[AllPayBillSaleTransaction.doSale] Going to Settle Cash Deposit Commisions in OLA. Trx ID:" + wrapper.getTransactionCodeModel().getCode() + " AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
				abstractFinancialInstitution.settleCashDepositCommission(switchWrapper);
			}

			
//			if(wrapper.getProductModel().getProductId() != 50002L)
//			{
//				wrapper = billSaleProductDispenser.doSale(wrapper);
//			}
			
			
			
			if ( productDeviceFlowModel != null && productDeviceFlowModel.getDeviceFlowId() != null && 
					productDeviceFlowModel.getDeviceFlowId().longValue() != 17 )
			{
//			if (logger.isDebugEnabled())
//			{
//				logger.debug("Sending SMS to subscriber....");
//			}
//			if (!(wrapper.getCustomerAppUserModel().getMobileNo().trim().equals(wrapper.getAppUserModel().getMobileNo())))
//			{
//				smsSender.send(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage()));
//
//			}
//			else
//			{
//				smsSender.sendDelayed(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage()));
//
//			}
			// -----------------------------------------------------------------------
		}
		/*else if(wrapper.getProductModel().getProductId() == 50002)
		{
			
			 
//			 You have successfully deposited <amount> in branchless banking account <a/c number> and  mobile number <mob. number>.
//			Your new balance is <new balance>
//			Your new balance is <new balance>.

//			String sms = "You have successfully deposited " + 
//			Formatter.formatDouble((wrapper.getTransactionModel().getTotalAmount())) + " in MWallet account " + ((P2PVO)wrapper.getProductVO()).getMfsId() 
//			+ " and  mobile number " + ((P2PVO)wrapper.getProductVO()).getMobileNo() + ". Your new balance is " + Formatter.formatDouble(balance)
//				+ ". For more information please call helpline XXX-XXX-XXX" ;
//			this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), sms));
//			
			
			String agentSMS=this.getMessageSource().getMessage(
					"USSD.AgentCashDepositSMS",
					new Object[] {
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
							((P2PVO)wrapper.getProductVO()).getCustomerName(),
							((P2PVO)wrapper.getProductVO()).getMobileNo(),
							dtf.print(new DateTime()),
							tf.print(new LocalTime()),
							wrapper.getTransactionCodeModel().getCode(),
							wrapper.getAppUserModel().getMobileNo()
							},
							null);
			String sms = "You have successfully deposited " + 
			Formatter.formatDouble((wrapper.getTransactionModel().getTotalAmount())) + " in MWallet account " + ((P2PVO)wrapper.getProductVO()).getMobileNo() 
			+ " and  mobile number " + ((P2PVO)wrapper.getProductVO()).getMobileNo() + ". Your new balance is " + Formatter.formatDouble(balance)
				+ ". For more information please call helpline XXX-XXX-XXX" ;
			wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
			this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS));
			
			
//			Dear Customer, 
//			<amount> has been deposited in your branchless banking  account <a/c number>. from retailer (retailer name, a/c number and mob number> .
//			Your new balance is <new balance>
			Double charges=wrapper.getCommissionAmountsHolder().getTotalCommissionAmount()+
					wrapper.getCommissionAmountsHolder().getFedCommissionAmount();
			String customerSMS= this.getMessageSource().getMessage(
					"USSD.CustomerCashDepositSMS",
					new Object[] {
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
							ThreadLocalAppUser.getAppUserModel().getMobileNo(),
							dtf.print(new DateTime()),
							tf.print(new LocalTime()),
							Formatter.formatDouble(charges),
							Formatter.formatDouble(((P2PVO)wrapper.getProductVO()).getBalance()),
							wrapper.getTransactionCodeModel().getCode()
							},
							null);
			sms = "Dear Customer, " + 
			Formatter.formatDouble((wrapper.getTransactionModel().getTotalAmount())) + " has been deposited in your MWallet account through Agent( " + 
			wrapper.getAppUserModel().getFirstName() + " " +wrapper.getAppUserModel().getLastName() + ", " + ThreadLocalAppUser.getAppUserModel().getMobileNo() + "/ a.c. " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId() + "). "
				+ "Your new balance is " + Formatter.formatDouble(((P2PVO)wrapper.getProductVO()).getBalance()) + ". For more information please call helpline XXX-XXX-XXX" ;
			this.smsSender.send(new SmsMessage(((P2PVO)wrapper.getProductVO()).getMobileNo(), customerSMS));
			
			if(null != wrapper.getWalkInCustomerMob() && !"".equals(wrapper.getWalkInCustomerMob()))
			{
				String walkinCustomerSMS= this.getMessageSource().getMessage(
						"USSD.DepositorCashDepositSMS",
						new Object[] { 
								Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
								//wrapper.getAccountInfoModel().getAccountNo(),
								((P2PVO)wrapper.getProductVO()).getMobileNo(),
								((P2PVO)wrapper.getProductVO()).getCustomerName(),								
								ThreadLocalAppUser.getAppUserModel().getMobileNo(),
								Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
								dtf.print(new DateTime()),
								tf.print(new LocalTime()),								
								wrapper.getTransactionCodeModel().getCode()
								
								},
								null);	
				
				sms = "Dear Customer, "
							+ Formatter.formatDouble((wrapper
									.getTransactionModel().getTotalAmount()))
							+ " has been deposited against MWallet account "
							+ ((P2PVO) wrapper
							.getProductVO()).getMobileNo()
							+ " from Agent( "
							+ wrapper.getAppUserModel().getFirstName()
							+ " "
							+ wrapper.getAppUserModel().getLastName()
							+ ", "
							+ ThreadLocalAppUser.getAppUserModel()
									.getMobileNo()
							+ " ). For more information please call helpline XXX-XXX-XXX";
				wrapper.getTransactionDetailModel().setCustomField8(walkinCustomerSMS);
					this.smsSender.send(new SmsMessage(wrapper.getWalkInCustomerMob(), walkinCustomerSMS));
			}
			
//			wrapper.getTransactionModel().setConfirmationMessage(sms);
			
			wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
		}*/
		
//		else if(UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())))
//		{
//			String agentSMS=this.getMessageSource().getMessage(
//					"USSD.AgentBillPaymentSMS",
//					new Object[] {
//							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
//							wrapper.getProductModel().getName(),
//							wrapper.getTransactionDetailModel().getConsumerNo(),
//							dtf.print(new DateTime()),
//							tf.print(new LocalTime()),
//							wrapper.getTransactionCodeModel().getCode()
//							},
//							null);
//			wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
//			this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS));
//			
//			String customerSMS=this.getMessageSource().getMessage(
//					"USSD.CustomerBillPaymentNotificationforAgent",
//					new Object[] {
//							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
//							wrapper.getProductModel().getName(),
//							wrapper.getTransactionDetailModel().getConsumerNo(),
//							dtf.print(new DateTime()),
//							tf.print(new LocalTime()),
//							wrapper.getTransactionCodeModel().getCode()
//							//,wrapper.getAppUserModel().getMobileNo()
//							},
//							null);
//			wrapper.getTransactionDetailModel().setCustomField8(customerSMS);
//			wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getWalkInCustomerMob());
//			wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
//			this.smsSender.send(new SmsMessage(wrapper.getWalkInCustomerMob(), customerSMS));
			
		
//		}
			
			

			
			
			
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
			
			if(wrapper.getFirstFTIntegrationVO() != null) {
				
				String bankResponseCode = wrapper.getFirstFTIntegrationVO().getResponseCode();
				
				if(bankResponseCode != null) {
					wrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
				}
			}			
			
			txManager.saveTransaction(wrapper); // save the transaction
			
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doSale of AllPayBillSaleTransaction.");
		}

		return wrapper;
	}

	private void sendCashDepositSMS(WorkFlowWrapper wrapper) throws Exception{

//		 You have successfully deposited <amount> in branchless banking account <a/c number> and  mobile number <mob. number>.
//		Your new balance is <new balance>
//		Your new balance is <new balance>.

//		String sms = "You have successfully deposited " + 
//		Formatter.formatDouble((wrapper.getTransactionModel().getTotalAmount())) + " in MWallet account " + ((P2PVO)wrapper.getProductVO()).getMfsId() 
//		+ " and  mobile number " + ((P2PVO)wrapper.getProductVO()).getMobileNo() + ". Your new balance is " + Formatter.formatDouble(balance)
//			+ ". For more information please call helpline XXX-XXX-XXX" ;
//		this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), sms));
//		
		
		String agentSMS=this.getMessageSource().getMessage(
				"USSD.AgentCashDepositSMS",
				new Object[] {
						Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
						((P2PVO)wrapper.getProductVO()).getCustomerName(),
						((P2PVO)wrapper.getProductVO()).getMobileNo(),
						dtf.print(new DateTime()),
						tf.print(new LocalTime()),
						wrapper.getTransactionCodeModel().getCode(),
						wrapper.getAppUserModel().getMobileNo()
						},
						null);
		/*String sms = "You have successfully deposited " + 
		Formatter.formatDouble((wrapper.getTransactionModel().getTotalAmount())) + " in MWallet account " + ((P2PVO)wrapper.getProductVO()).getMobileNo() 
		+ " and  mobile number " + ((P2PVO)wrapper.getProductVO()).getMobileNo() + ". Your new balance is " + Formatter.formatDouble(balance)
			+ ". For more information please call helpline XXX-XXX-XXX" ;*/
		wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
		this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS));
		SmsMessage message = new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS);
		message.setMessageText(agentSMS);
		message.setMobileNo(wrapper.getAppUserModel().getMobileNo());
		message.setMessageType("ZINDIGI");
		message.setTitle("Agent Cash Deposit");
		this.smsSender.pushNotification(message);
		
		
//		Dear Customer, 
//		<amount> has been deposited in your branchless banking  account <a/c number>. from retailer (retailer name, a/c number and mob number> .
//		Your new balance is <new balance>
		Double charges = 0d;
		if(wrapper.getCommissionAmountsHolder().getIsInclusiveCharges() == false){
			charges=wrapper.getCommissionAmountsHolder().getTotalCommissionAmount()+
					wrapper.getCommissionAmountsHolder().getFedCommissionAmount();
		}
		
		String customerSMS= this.getMessageSource().getMessage(
				"USSD.CustomerCashDepositSMS",
				new Object[] {
						Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
						ThreadLocalAppUser.getAppUserModel().getMobileNo(),
						dtf.print(new DateTime()),
						tf.print(new LocalTime()),
						Formatter.formatDouble(charges),
						Formatter.formatDouble(((P2PVO)wrapper.getProductVO()).getBalance()),
						wrapper.getTransactionCodeModel().getCode()
						},
						null);
		/*sms = "Dear Customer, " + 
		Formatter.formatDouble((wrapper.getTransactionModel().getTotalAmount())) + " has been deposited in your MWallet account through Agent( " + 
		wrapper.getAppUserModel().getFirstName() + " " +wrapper.getAppUserModel().getLastName() + ", " + ThreadLocalAppUser.getAppUserModel().getMobileNo() + "/ a.c. " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId() + "). "
			+ "Your new balance is " + Formatter.formatDouble(((P2PVO)wrapper.getProductVO()).getBalance()) + ". For more information please call helpline XXX-XXX-XXX" ;*/
		this.smsSender.send(new SmsMessage(((P2PVO)wrapper.getProductVO()).getMobileNo(), customerSMS));
		
		if(null != wrapper.getWalkInCustomerMob() && !"".equals(wrapper.getWalkInCustomerMob()) 
				&& false == wrapper.getWalkInCustomerMob().equals(wrapper.getCustomerAppUserModel().getMobileNo()))
		{
			String walkinCustomerSMS= this.getMessageSource().getMessage(
					"USSD.DepositorCashDepositSMS",
					new Object[] { 
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
							//wrapper.getAccountInfoModel().getAccountNo(),
							((P2PVO)wrapper.getProductVO()).getMobileNo(),
							((P2PVO)wrapper.getProductVO()).getCustomerName(),								
							ThreadLocalAppUser.getAppUserModel().getMobileNo(),
//							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
							dtf.print(new DateTime()),
							tf.print(new LocalTime()),								
							wrapper.getTransactionCodeModel().getCode()
							
							},
							null);	
			
			/*sms = "Dear Customer, "
						+ Formatter.formatDouble((wrapper
								.getTransactionModel().getTotalAmount()))
						+ " has been deposited against MWallet account "
						+ ((P2PVO) wrapper
						.getProductVO()).getMobileNo()
						+ " from Agent( "
						+ wrapper.getAppUserModel().getFirstName()
						+ " "
						+ wrapper.getAppUserModel().getLastName()
						+ ", "
						+ ThreadLocalAppUser.getAppUserModel()
								.getMobileNo()
						+ " ). For more information please call helpline XXX-XXX-XXX";*/
//			wrapper.getTransactionDetailModel().setCustomField8(walkinCustomerSMS);
				this.smsSender.send(new SmsMessage(wrapper.getWalkInCustomerMob(), walkinCustomerSMS));
		}
		
		
		wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
	
	}
	
	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)
			throws Exception {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction....");
		}
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		wrapper = super.doPreStart(wrapper);

		// Populate the Product model from DB
		baseWrapper.setBasePersistableModel(wrapper.getProductModel());
		baseWrapper = productManager.loadProduct(baseWrapper);
		wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());
		
		// Populate Retailer Contact model from DB
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    RetailerContactModel retailerContact = new RetailerContactModel();
	    retailerContact.setRetailerContactId( wrapper.getFromRetailerContactAppUserModel().getRetailerContactId() );
	    searchBaseWrapper.setBasePersistableModel( retailerContact );
	    searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
	    wrapper.setFromRetailerContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());


//		wrapper.setCustomerModel(new CustomerModel());
//		wrapper.getCustomerModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());

		// --Setting instruction and success Message
		NotificationMessageModel notificationMessage = new NotificationMessageModel();
		notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
		baseWrapper.setBasePersistableModel(notificationMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		// wrapper.getProductModel().setInstructionIdNotificationMessageModel( (
		// NotificationMessageModel) baseWrapper.getBasePersistableModel());
		NotificationMessageModel successMessage = new NotificationMessageModel();
		successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
		baseWrapper.setBasePersistableModel(successMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		
		// Populate the OLA Smart Money Account from DB
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
		sma.setRetailerContactId( ThreadLocalAppUser.getAppUserModel().getRetailerContactId() ) ;
		baseWrapper.setBasePersistableModel(sma);
		baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
		wrapper.setOlaSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

		wrapper.setPaymentModeModel(new PaymentModeModel());
		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

		// Populate Supplier Bank Info Model
		SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
		supplierBankInfoModel.setSupplierId(wrapper.getProductModel().getSupplierId());
		supplierBankInfoModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		baseWrapper.setBasePersistableModel(supplierBankInfoModel);
		supplierBankInfoModel = (SupplierBankInfoModel) this.supplierBankInfoManager.loadSupplierBankInfo(baseWrapper).getBasePersistableModel();
		wrapper.setSupplierBankInfoModel(supplierBankInfoModel);

		OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();

		if (supplierBankInfoModel != null)
		{
			// Populate Operator's Paying Bank Info Model
			operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
			operatorBankInfoModel.setPaymentModeId(supplierBankInfoModel.getPaymentModeId());
			operatorBankInfoModel.setBankId(supplierBankInfoModel.getBankId());
			baseWrapper.setBasePersistableModel(operatorBankInfoModel);
			wrapper.setOperatorPayingBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
		}

		// Populate Operator Bank Info Model
		// OperatorBankInfoModel operatorBankInfoModel = new
		// OperatorBankInfoModel();
		operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
		operatorBankInfoModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		operatorBankInfoModel.setBankId(wrapper.getSmartMoneyAccountModel().getBankId());
		baseWrapper.setBasePersistableModel(operatorBankInfoModel);
		wrapper.setOperatorBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
		
		
		
		SearchBaseWrapper sWrapper = new SearchBaseWrapperImpl();
		
		if(!UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())))
		{
			AppUserModel appUserModel =new AppUserModel();
			appUserModel.setMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
//			sWrapper.setBasePersistableModel(appUserModel);
//			BaseWrapper bWrapper = new BaseWrapperImpl();
//			appUserModel = appUserManager.loadAppUserByQuery(wrapper.getCustomerAppUserModel().getMobileNo(), UserTypeConstantsInterface.CUSTOMER.longValue());
			appUserModel = appUserManager.getAppUserModel(appUserModel);
			if(null != appUserModel)
			{
				wrapper.setCustomerModel(appUserModel.getCustomerIdCustomerModel());
				wrapper.setCustomerAppUserModel(appUserModel);
			}
		}
		
		// Set Handler User Device Account Model
		if(wrapper.getHandlerAppUserModel() != null && wrapper.getHandlerAppUserModel().getAppUserId() != null){			
			UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
			handlerUserDeviceAccountsModel.setAppUserId(wrapper.getHandlerAppUserModel().getAppUserId());
			handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
			baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			wrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		}
		
		
		logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction....");
		
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)
			throws Exception {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction....");
		}
		wrapper = super.doPreProcess(wrapper);

		TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
		txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		txModel.setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() ) ;
		txModel.setSmartMoneyAccountId(wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
		txModel.setRetailerId(wrapper.getFromRetailerContactModel().getRetailerId());
		txModel.setDistributorId(wrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		txModel.setProcessingBankId(wrapper.getOlaSmartMoneyAccountModel().getBankId());
		txModel.setCustomerMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		
		
		txModel.setTransactionAmount(wrapper.getBillAmount());
		txModel.setTotalAmount(wrapper.getBillAmount());
		txModel.setTotalCommissionAmount(0d);
		txModel.setDiscountAmount(0d);

		// Transaction Type model of transaction is populated
		txModel.setTransactionTypeIdTransactionTypeModel(wrapper
				.getTransactionTypeModel());

		// Sets the Device type
		txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel()
				.getPaymentModeId());

		// Customer model of transaction is populated
//		txModel.setCustomerIdCustomerModel(wrapper.getCustomerModel());

		// Customer mobile No
		txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel()
				.getMobileNo());
		txModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel()
				.getMobileNo());
		txModel.setMfsId(/*wrapper.getUserDeviceAccountModel().getUserId()*/ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());

		wrapper.setTransactionModel(txModel);
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of AllPayBillSaleTransaction....");
		}

		return wrapper;
	}


	public void setSupplierManager(SupplierManager supplierManager) {
		this.supplierManager = supplierManager;
	}

	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}

	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}

	public void setCustomerManager(CustTransManager customerManager) {
		this.customerManager = customerManager;
	}

	public void setProductDispenseController(
			ProductDispenseController productDispenseController) {
		this.productDispenseController = productDispenseController;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}

	public void setVeriflyController(VeriflyManagerService veriflyController) {
		this.veriflyController = veriflyController;
	}

	public void setSupplierBankInfoManager(
			SupplierBankInfoManager supplierBankInfoManager) {
		this.supplierBankInfoManager = supplierBankInfoManager;
	}

	public Double getTransactionProcessingCharges(
			CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside getTransactionProcessingCharges of AllPayBillSaleTransaction....");
		}
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = (List) commissionWrapper
				.getCommissionWrapperHashMap().get(
						CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList) 
		{
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.ALLPAY_SERVICE_CHARGE.longValue() ) 
			{
				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue())
					transProcessingAmount += commissionRateModel.getRate();
				else
					transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;
				
				//transProcessingAmount += commissionRateModel.getRate();
				//System.out.println( "  !!!!!!!!!!!!!!!!!!1 " + transProcessingAmount );
			}
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending getTransactionProcessingCharges of AllPayBillSaleTransaction....");
		}
		return transProcessingAmount;
	}

	public void setOperatorManager(OperatorManager operatorManager) {
		this.operatorManager = operatorManager;
	}

	@Override
	protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper)
			throws Exception {
		// TODO Auto-generated method stub
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper)
			throws Exception {
		// TODO Auto-generated method stub
		return wrapper;
	}

	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
	{
		String code = wrapper.getTransactionCodeModel().getCode();
		logger.info("[AllPayBillSaleTransaction.rollback] Rolling back transaction with ID : "+code);
		boolean isBillAdviceSent = wrapper.getBillPaymentIntegrationVO() != null;
		
		if(null != wrapper.getFirstFTIntegrationVO() && ! isBillAdviceSent)
		{
			String rrn = wrapper.getFirstFTIntegrationVO().getRetrievalReferenceNumber();
			rrn = rrnPrefix + rrn;
			rrnPrefix = null;
			logger.info("[AllPayBillSaleTransaction.rollback] FT was done so trying to roll back with RRN  : "+rrn+" and trx code : "+code);
			PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO();
			phoenixIntegrationMessageVO.setRetrievalReferenceNumber( rrn );
			phoenixIntegrationMessageVO.setChannelId("MR0001");
			
			SwitchWrapper switchWrapper = wrapper.getFirstFTSwitchWrapper();
			switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
			String fromAccount = new String(switchWrapper.getFromAccountNo());
			String toAccount = new String(switchWrapper.getToAccountNo());
			//reverse accounts for posted transaction report...
			switchWrapper.setFromAccountNo(toAccount);
			switchWrapper.setToAccountNo(fromAccount);
			
			logger.info("[AllPayBillSaleTransaction.rollback] Hitting Phoenix to rollback with RRN : "+rrn+" and trx code : "+code);
			switchWrapper = phoenixFinancialInstitution.reverseFundTransfer(switchWrapper);
			
		}
		
		
		return wrapper;
	}
	
	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public void setRetailerContactManager(RetailerContactManager retailerContactManager)
	{
		this.retailerContactManager = retailerContactManager;
	}

	public void setGenericDAO(GenericDao genericDAO)
	{
		this.genericDAO = genericDAO;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setPhoenixFinancialInstitution(
			AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}

}

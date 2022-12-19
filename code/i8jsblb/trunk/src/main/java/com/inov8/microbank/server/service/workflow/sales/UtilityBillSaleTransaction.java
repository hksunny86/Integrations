package com.inov8.microbank.server.service.workflow.sales;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.util.*;
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
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.OperatorBankInfoModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;

public class UtilityBillSaleTransaction extends SalesTransaction {
	
	protected final Log log = LogFactory.getLog(getClass());
	private MessageSource messageSource;
	private DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
	
	private String OLA_FROM_ACCOUNT = "OLA_FROM_ACCOUNT";
	private String OLA_TO_ACCOUNT = "OLA_TO_ACCOUNT";

	private SupplierManager supplierManager;
	private CommissionManager commissionManager;
	private ProductDispenseController productDispenseController;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private CustTransManager customerManager;
	private ProductManager productManager;
//	private AppUserManager appUserManager;
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
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private ActionLogManager actionLogManager = null;


	/**
	 * Pulls the bill information from the supplier system
	 * 
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Inside getBillInfo(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction...");
			logger.debug("Loading ProductDispenser...");
		}
		
		BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
		logger.debug("Fetching Bill Info through Product Dispenser...");
		return billSaleProductDispenser.getBillInfo(wrapper);
	}

	/**
	 * Validate input from the user against the information pulled from the
	 * supplier
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper) throws FrameworkCheckedException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Inside validateBillInfo(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction...");
		}
		
		BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
		wrapper = billSaleProductDispenser.verify(wrapper);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Ending validateBillInfo(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction...");
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
	public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
		// ------ Calculate the commission
		// ------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction...");
		}
		/*
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
		*/
		
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());

	    wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction...");
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
			logger.debug("Inside validateCommission of UtilityBillSaleTransaction...");
		}
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		BillPaymentVO productVO = (BillPaymentVO) workFlowWrapper.getProductVO();

//		if (productVO.getCurrentBillAmount().doubleValue() != workFlowWrapper.getBillAmount().doubleValue())
//		{
//
//			throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
//		}
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

			logger.debug("Ending validateCommission of UtilityBillSaleTransaction...");
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
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction");
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

		if (wrapper.getOperatorPayingBankInfoModel() == null) {
		
//			throw new WorkFlowException(WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
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
			logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction");
		}
		return wrapper;
	}

	/**
	 * Method responsible for processing the Bill Sale transaction
	 * 
	 * @param _workFlowWrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

	public WorkFlowWrapper doSale(WorkFlowWrapper _workFlowWrapper) throws Exception {
		
 		TransactionDetailModel txDetailModel = new TransactionDetailModel();
		String notificationMobileNo = _workFlowWrapper.getCustomerAppUserModel().getMobileNo();
		Integer paymentType = (Integer) _workFlowWrapper.getCustomField();
		boolean isCustomerTransaction = paymentType.intValue() == 0 ? Boolean.TRUE : Boolean.FALSE;
		
		String consumerCnic = (isCustomerTransaction) ? _workFlowWrapper.getCustomerAppUserModel().getNic() : _workFlowWrapper.getAppUserModel().getNic(); 
		
 		CommissionWrapper commissionWrapper = this.calculateCommission(_workFlowWrapper);
		CommissionAmountsHolder commissionAmounts = 
				(CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
				
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());
		
		_workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);
		_workFlowWrapper.setCommissionWrapper(commissionWrapper);		
		_workFlowWrapper.putObject("misc", paymentType.intValue() == 0 ? Boolean.TRUE : Boolean.FALSE);
		
		OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) financialIntegrationManager.loadFinancialInstitution(baseWrapper);	

		olaVeriflyFinancialInstitution.checkDebitCreditLimitOLAVO(_workFlowWrapper);
		
		if(isCustomerTransaction) {
			
			AppUserModel appUserModel = _workFlowWrapper.getAppUserModel();
			_workFlowWrapper.setAppUserModel(_workFlowWrapper.getCustomerAppUserModel());
			
			olaVeriflyFinancialInstitution.checkDebitCreditLimitOLAVO(_workFlowWrapper);
			_workFlowWrapper.setAppUserModel(appUserModel);
		}
		
//		this.validateCommission(commissionWrapper, _workFlowWrapper);
		
		_workFlowWrapper.setCommissionWrapper(commissionWrapper);
		_workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);
		
 		if (isCustomerTransaction && _workFlowWrapper.getIsIvrResponse() == Boolean.FALSE) {
			
			_workFlowWrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
			_workFlowWrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
			_workFlowWrapper.getTransactionModel().setCreatedOn(new Date());
			_workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING);
			txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
			txDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
			txDetailModel.setConsumerNo(((UtilityBillVO) _workFlowWrapper.getProductVO()).getConsumerNo());
			_workFlowWrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
			_workFlowWrapper.getTransactionModel().setFromRetContactId(_workFlowWrapper.getRetailerContactModel().getRetailerContactId());
			
		    _workFlowWrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);
		    
			txDetailModel.setCustomField9(_workFlowWrapper.getObject(CommandFieldConstants.KEY_PAYMENT_TYPE).toString());
			
			txDetailModel.setSettled(Boolean.FALSE);
			_workFlowWrapper.setTransactionDetailModel(txDetailModel);
			_workFlowWrapper.getTransactionModel().setConfirmationMessage(" _ ");
			
			// Set Handler Detail in Transaction and Transaction Detail Master
			if(_workFlowWrapper.getHandlerModel() != null){
				    _workFlowWrapper.getTransactionModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
				    _workFlowWrapper.getTransactionModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
				    _workFlowWrapper.getTransactionDetailMasterModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
				    _workFlowWrapper.getTransactionDetailMasterModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
			}
			
			_workFlowWrapper.getTransactionDetailMasterModel().setBillDueDate(((UtilityBillVO) _workFlowWrapper.getProductVO()).getDueDate());
			_workFlowWrapper.getTransactionDetailMasterModel().setBillAmount(((UtilityBillVO) _workFlowWrapper.getProductVO()).getBillAmount());
			_workFlowWrapper.getTransactionDetailMasterModel().setLateBillAmount(((UtilityBillVO) _workFlowWrapper.getProductVO()).getLateBillAmount());
			
			return super.makeIvrRequest(_workFlowWrapper);
			
		} else {
			
//			txDetailModel = _workFlowWrapper.getTransactionDetailModel();
			_workFlowWrapper.getTransactionDetailMasterModel().setSendingRegion(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionId());
			_workFlowWrapper.getTransactionDetailMasterModel().setSendingRegionName(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionName());

			_workFlowWrapper.getTransactionDetailMasterModel().setSenderAreaId(_workFlowWrapper.getRetailerContactModel().getAreaId());
			_workFlowWrapper.getTransactionDetailMasterModel().setSenderAreaName(_workFlowWrapper.getRetailerContactModel().getAreaName());

			_workFlowWrapper.getTransactionDetailMasterModel().setSenderDistributorId(_workFlowWrapper.getDistributorModel().getDistributorId());
			_workFlowWrapper.getTransactionDetailMasterModel().setSenderDistributorName(_workFlowWrapper.getDistributorModel().getName());
			if (null != _workFlowWrapper.getDistributorModel().getMnoId()) {
				_workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPId(_workFlowWrapper.getDistributorModel().getMnoId());
				_workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPName(_workFlowWrapper.getDistributorModel().getMnoModel().getName());
//				_workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPName(_workFlowWrapper.getDistributorModel().getMnoModel().getName());
			}
			_workFlowWrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
			_workFlowWrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount()+commissionAmounts.getExclusiveFixAmount()+commissionAmounts.getExclusivePercentAmount());
			_workFlowWrapper.getTransactionModel().setCreatedOn(new Date());
			_workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.IN_PROGRESS);
			txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
			txDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
			txDetailModel.setConsumerNo(((UtilityBillVO) _workFlowWrapper.getProductVO()).getConsumerNo());
			_workFlowWrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
			_workFlowWrapper.getTransactionModel().setFromRetContactId(_workFlowWrapper.getRetailerContactModel().getRetailerContactId());
			_workFlowWrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);
			
			txDetailModel.setSettled(Boolean.FALSE);
			_workFlowWrapper.setTransactionDetailModel(txDetailModel);
			_workFlowWrapper.getTransactionModel().setConfirmationMessage(" _ ");
			
			  txManager.saveTransaction(_workFlowWrapper);			
		}

		String txAmount = Formatter.formatNumbers(_workFlowWrapper.getTransactionModel().getTransactionAmount());
		String seviceChargesAmount = Formatter.formatNumbers(_workFlowWrapper.getTransactionModel().getTotalAmount() - _workFlowWrapper.getTransactionModel().getTransactionAmount());
		
		/**
		 * pull the bill information from the supplier, calculate commissions on bill and return in to iPos for the customer to accept or reject
		 */

		if (logger.isDebugEnabled()) {
			logger.debug("Inside doSale(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction..");
		}
			
		if (logger.isDebugEnabled()) {
			logger.debug("Saving Transaction in DB....");
		}

		//Added  by Maqsood for Phoenix
		SmartMoneyAccountModel agentSmartMoneyAccountModel = _workFlowWrapper.getOlaSmartMoneyAccountModel();
		
		SwitchWrapper switchWrapper = _workFlowWrapper.getSwitchWrapper();
		switchWrapper.setSenderCNIC(consumerCnic);
		
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		
		// Disabled fetch bill info on JSBL request [22-Sep-2016]
		//_workFlowWrapper = this.getBillInfo(_workFlowWrapper);
		
/***********/// need to prepare productVO from infoCommand
		
		
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
				
		_workFlowWrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.FAILED );

		txManager.saveTransaction(_workFlowWrapper);
			
			//for walkin customer cash deposit
		if(null != _workFlowWrapper.getWalkInCustomerMob() && !"".equals(_workFlowWrapper.getWalkInCustomerMob())) {
			txDetailModel.setCustomField6(_workFlowWrapper.getWalkInCustomerMob());
		}
			
		if(null != _workFlowWrapper.getWalkInCustomerCNIC() && !"".equals(_workFlowWrapper.getWalkInCustomerCNIC())) {
			txDetailModel.setCustomField7(_workFlowWrapper.getWalkInCustomerCNIC());
		}

		//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
		
		if(_workFlowWrapper.getFromRetailerContactModel() != null && _workFlowWrapper.getFromRetailerContactModel().getHead()) {
			
			commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
			commissionAmounts.setFranchise1CommissionAmount(0.0d);
		}
			

		txAmount = Formatter.formatDouble(_workFlowWrapper.getTransactionModel().getTransactionAmount());
		seviceChargesAmount = Formatter.formatNumbers(_workFlowWrapper.getTransactionModel().getTotalAmount() - _workFlowWrapper.getTransactionModel().getTransactionAmount());

		txDetailModel.setSettled(Boolean.FALSE);
		_workFlowWrapper.setTransactionDetailModel(txDetailModel);
			
		if (!_workFlowWrapper.getTransactionDetailModel().getConsumerNo().equalsIgnoreCase("")) {

			_workFlowWrapper.getTransactionModel().setConfirmationMessage(
					SMSUtil.buildBillSaleSMS(_workFlowWrapper.getInstruction().getSmsMessageText(), _workFlowWrapper.getProductModel().getName(), txAmount, seviceChargesAmount,
							_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), _workFlowWrapper.getTransactionDetailModel().getConsumerNo()));
		} else {
				_workFlowWrapper.getTransactionModel().setConfirmationMessage(
						SMSUtil.buildVariableProductSMS(_workFlowWrapper.getInstruction().getSmsMessageText(), txAmount, _workFlowWrapper.getTransactionModel()
								.getTransactionCodeIdTransactionCodeModel().getCode(), _workFlowWrapper.getProductModel().getHelpLineNotificationMessageModel()
								.getSmsMessageText()));
		}

		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setTransactionTransactionModel(_workFlowWrapper.getTransactionModel());
		switchWrapper.setAccountInfoModel(_workFlowWrapper.getAccountInfoModel());
		
		if (logger.isDebugEnabled()) {
			logger.debug("Executing Bill Sale on BillSaleProductDispenser....");
		}
		
		BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);
		
		
		AppUserModel appUserModel = ((isCustomerTransaction) ? _workFlowWrapper.getCustomerAppUserModel() : _workFlowWrapper.getAppUserModel()); 
	    _workFlowWrapper.getTransactionModel().setSaleMobileNo(appUserModel.getMobileNo());

		TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;		

		ThreadLocalActionLog.setActionLogId(logActionLogModel());

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)_workFlowWrapper.getOlaSmartMoneyAccountModel();
        
        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        switchWrapper.setBankId( smartMoneyAccountModel.getBankId() );
        switchWrapper.setPaymentModeId( smartMoneyAccountModel.getPaymentModeId() );
       	switchWrapper = olaVeriflyFinancialInstitution.verifyCredentialsWithoutPin(switchWrapper);		
			
		//Moving check balance before FT
					
		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapper.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getAccountInfoModel().getOldPin());
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setTransactionTransactionModel(_workFlowWrapper.getTransactionModel());
		switchWrapper.setBasePersistableModel( agentSmartMoneyAccountModel ) ;
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);			
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
		_workFlowWrapper.getTransactionModel().setCustomerMobileNo(null);
		_workFlowWrapper.setAccountInfoModel(switchWrapper.getAccountInfoModel());

		logger.info("[UtilityBillSaleTransaction.dosale]Checking Agent Balance. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
						
		transactionModelTemp.setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());
			
		switchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

		switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;

		_workFlowWrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
				
		transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;	
			
		rrnPrefix = buildRRNPrefix();		
			
			
		SmartMoneyAccountModel headRetailerSmartMoneyAccountModel = _workFlowWrapper.getHeadRetailerSmaModel();
			
		if(headRetailerSmartMoneyAccountModel != null) {
			
			AccountInfoModel headRetailerAccountInfoModel = olaVeriflyFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(headRetailerSmartMoneyAccountModel, _workFlowWrapper.getHeadRetailerAppuserModel().getAppUserId(), _workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getTransactionCodeId());
			_workFlowWrapper.getDataMap().put("headRetailerAccountInfoModel", headRetailerAccountInfoModel);
		}
			
		SmartMoneyAccountModel customerSmartMoneyAccountModel = (SmartMoneyAccountModel) _workFlowWrapper.getObject("CUSTOMER_SmartMoneyAccountModel");
		AccountInfoModel customerAccountInfoModel = null;
		
		if(customerSmartMoneyAccountModel != null) {
			
			customerAccountInfoModel = olaVeriflyFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(customerSmartMoneyAccountModel, _workFlowWrapper.getCustomerAppUserModel().getCustomerId(), _workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getTransactionCodeId());
			_workFlowWrapper.getDataMap().put("CUSTOMER_RetailerAccountInfoModel", customerAccountInfoModel);
		}
			
//		switchWrapper.setAgentBalance(balance);
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
			
		/*
		 *** 	SENDING BILL PAYMENT TO OLA BANKING.
		 */				
		logger.info("OLA > SENDING BILL PAYMENT TO OLA BANKING.");
			
		ProductModel productModel = _workFlowWrapper.getProductModel();
						
		_workFlowWrapper = billSaleProductDispenser.doSale(_workFlowWrapper);
			
		_workFlowWrapper.getDataMap().put(OLA_FROM_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getFromAccountNo());
		_workFlowWrapper.getDataMap().put(OLA_TO_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getToAccountNo());
			
		switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
			
		switchWrapper.getWorkFlowWrapper().setProductModel(productModel);
			
		//The following code is for Phoenix implementation
			
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());

		billSaleProductDispenser.prepairSwitchWrapper(_workFlowWrapper.getProductModel().getProductId(), switchWrapper);
			
		if(null != _workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId() && 
				_workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId().longValue() == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue()) {
	
				CommissionAmountsHolder commissionHolder = 
					(CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
					
//				switchWrapper.setAgentBalance((balance-switchWrapper.getTransactionAmount())-commissionHolder.getAgent1CommissionAmount());
		}
			
		switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
		_workFlowWrapper.setOLASwitchWrapper(_workFlowWrapper.getSwitchWrapper());
		
		txDetailModel.setSettled(Boolean.TRUE);
			
		if(_workFlowWrapper.getFirstFTIntegrationVO() != null) {
				
			String bankResponseCode = _workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();
				
			if(bankResponseCode != null) {
				_workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
			}
		}				
		
			
		SAVE_TRANSACTION : {

			TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();

			transactionModel.addTransactionIdTransactionDetailModel(txDetailModel);
			transactionModel.setSupProcessingStatusId( SupplierProcessingStatusConstants.BILL_AUTHORIZATION_SENT );
			transactionModel.setNotificationMobileNo(notificationMobileNo);
				
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setTransactionModel(transactionModel);

			txManager.saveTransaction(workFlowWrapper);
				
			transactionModel = workFlowWrapper.getTransactionModel();
				
			_workFlowWrapper.setTransactionModel(transactionModel);
			_workFlowWrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
		}
		
		this.sendSms(_workFlowWrapper, isCustomerTransaction);
			
		
		/* * * * 	SENDING BILL PAYMENT TO CORE BANKING. * * * */	

		_workFlowWrapper.setAppUserModel(appUserModel);
		_workFlowWrapper.putObject("ACTION_LOG_ID", ThreadLocalActionLog.getActionLogId());
		
		Boolean isInclusiveChargesIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
		isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : Boolean.TRUE;
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_BILL_POOL_T24_ACCOUNT_ID;
		if(OneBillProductEnum.contains(_workFlowWrapper.getProductModel().getProductId().toString()))
			utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_POOL_T24_ACCOUNT_ID;

		searchBaseWrapper.setBasePersistableModel(new StakeholderBankInfoModel(utilityBillPoolAccountId));
		
		searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
		
		StakeholderBankInfoModel utilityBillPoolT24Account = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();

		switchWrapper.setFromAccountNo(utilityBillPoolT24Account.getAccountNo());	
		switchWrapper.setSenderCNIC(consumerCnic);			
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setPaymentModeId(SwitchConstants.CORE_BANKING_SWITCH);	
		
		Double amount = _workFlowWrapper.getTransactionModel().getTransactionAmount();

		if(!isInclusiveChargesIncluded) {
			
			amount -= (_workFlowWrapper.getCommissionAmountsHolder().getInclusiveFixAmount() + _workFlowWrapper.getCommissionAmountsHolder().getInclusivePercentAmount());
		}
		
        switchWrapper.setAmountPaid(Formatter.formatDouble(amount));
		
		TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();
			
		logger.info("Middleware > SENDING BILL PAYMENT TO Core Integration for Authorization of Bill By Queue. \n From : "+switchWrapper.getFromAccountNo()+"\n To :"+switchWrapper.getToAccountNo());
			
		switchWrapper = phoenixFinancialInstitution.pushBillPayment(switchWrapper);
		_workFlowWrapper.setMiddlewareSwitchWrapper(switchWrapper); // for day end O.F. settlement of Core FT
		_workFlowWrapper.setTransactionModel(transactionModel);			
			
		SETTLE_COMMISSION_BLOCK : {
	
			if (logger.isDebugEnabled()) {				
				logger.debug("Going to settle commissions using SettlementManager....");
			}
				
			settlementManager.settleCommission(commissionWrapper, _workFlowWrapper);			
		}
			
			
		SETTLE_COMMISSION_BLOCK : {
			
			if (logger.isDebugEnabled()) {
					
				logger.debug("Going to settle Bank Payment using SettlementManager....");
			}
				
			settleAmount(_workFlowWrapper); // settle all amounts to the respective
		}
						
			
		if(_workFlowWrapper.getFirstFTIntegrationVO() != null) {
				
			String bankResponseCode = _workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();
				
			if(bankResponseCode != null) {
				_workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
			}
		}

		transactionModel.setCustomerMobileNo(notificationMobileNo);
		transactionModel.setNotificationMobileNo(notificationMobileNo);
		transactionModel.setSupProcessingStatusId( SupplierProcessingStatusConstants.COMPLETED);
		_workFlowWrapper.setTransactionModel(transactionModel);
			
		txManager.saveTransaction(_workFlowWrapper); // save the transaction
			
		
		if (logger.isDebugEnabled()) {
			
			logger.debug("Ending doSale of UtilityBillSaleTransaction.");
		}

		return _workFlowWrapper;
	}

	
	/**
	 * @param _workFlowWrapper
	 * @throws FrameworkCheckedException 
	 */
	private void sendSms(WorkFlowWrapper _workFlowWrapper, Boolean isPayByAccount) throws FrameworkCheckedException {
		
		String agentMsgString = null;
		String customerMsgString = null;
		Object[] agentSMSParam = null;
		Object[] customerSMSParam = null;

		String brandName = null;
		if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L)) {
			brandName = MessageUtil.getMessage("sco.brandName");
		} else {
			brandName = MessageUtil.getMessage("jsbl.brandName");
		}
		String trxCode = _workFlowWrapper.getTransactionCodeModel().getCode();
		String totalAmount = Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTotalAmount());
		String agentBalance = Formatter.formatDouble(_workFlowWrapper.getSwitchWrapper().getOlavo().getAgentBalanceAfterTransaction());
		String customerBalance = Formatter.formatDouble(_workFlowWrapper.getSwitchWrapper().getOlavo().getFromBalanceAfterTransaction());
		String productName = _workFlowWrapper.getProductModel().getName();
		String consumer = _workFlowWrapper.getTransactionDetailModel().getConsumerNo();
		String date = dtf.print(new DateTime());
		String time = tf.print(new LocalTime());		
		
		Long categoryId = 0L;
		Long POST_PAID = 5L;
		Long PRE_PAID = 6L;
		
		if(_workFlowWrapper.getProductModel().getCategoryId() != null) {
			categoryId = _workFlowWrapper.getProductModel().getCategoryId();
		}
				
		if(categoryId != POST_PAID.longValue() && categoryId != PRE_PAID.longValue()) {
			
			if(isPayByAccount) {
				agentMsgString = "ubp.paybyaccount.agent";
				customerMsgString = "ubp.paybyaccount.customer";					
				agentSMSParam = new Object[] {brandName,trxCode,productName,consumer,totalAmount,brandName,time,date};		
				customerSMSParam = new Object[] {brandName,trxCode,productName,consumer,totalAmount,time,date,customerBalance};
			} else {
				agentMsgString = "ubp.paybycash.agent";
				customerMsgString = "ubp.paybycash.customer";
				agentSMSParam = new Object[] {brandName,trxCode,productName,consumer,totalAmount,time,date,agentBalance};
				customerSMSParam = new Object[] {brandName,trxCode,productName,consumer,totalAmount,brandName,time,date};	
			}	
			
		} else if(categoryId == POST_PAID.longValue()) {
			
			if(isPayByAccount) {
				agentMsgString = "ubp.paybyaccount.postpaid.agent";//{0}\nTrx ID: {1}\nYou have successfully paid bill for {2} {3}\nRs.{4}\nat {5}\non {6}.\nAvl Bal: Rs.{7}
				customerMsgString = "ubp.paybyaccount.postpaid.customer";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
				agentSMSParam = new Object[] {brandName,trxCode,productName,consumer,totalAmount,time,date,agentBalance};		
				customerSMSParam = new Object[] {brandName,trxCode,productName,consumer,totalAmount,brandName,time,date,customerBalance};
			} else {
				agentMsgString = "ubp.paybycash.postpaid.agent";
				customerMsgString = "ubp.paybycash.postpaid.customer";
				agentSMSParam = new Object[] {brandName,trxCode,productName,consumer,totalAmount,time,date,agentBalance};
				customerSMSParam = new Object[] {brandName,trxCode,productName,consumer,totalAmount,brandName,time,date,agentBalance};	
			}			
		
		} else if(categoryId == PRE_PAID.longValue()) {
			
			if(isPayByAccount) {
				agentMsgString = "ubp.paybyaccount.prepaid.agent";
				customerMsgString = "ubp.paybyaccount.prepaid.customer";
				agentSMSParam = new Object[] {brandName,trxCode,totalAmount,consumer,brandName,time,date,agentBalance};
				customerSMSParam = new Object[] {brandName,trxCode,totalAmount,consumer,time,date,customerBalance};
			} else {
				agentMsgString = "ubp.paybycash.prepaid.agent";
				customerMsgString = "ubp.paybycash.prepaid.customer";
				agentSMSParam = new Object[] {brandName,trxCode,totalAmount,consumer,time,date,agentBalance};
				customerSMSParam = new Object[] {brandName,trxCode,totalAmount,consumer,brandName,time,date};	
			}			
			
			_workFlowWrapper.getSwitchWrapper().getOlavo().setAgentBalanceAfterTransaction(Double.valueOf(agentBalance));
		}		
		
		String agentSMS = this.getMessageSource().getMessage(agentMsgString, agentSMSParam, null);			
		String customerSMS = this.getMessageSource().getMessage(customerMsgString, customerSMSParam, null);
		
		SmsMessage agentSMSMessage = new SmsMessage(_workFlowWrapper.getAppUserModel().getMobileNo(), agentSMS);
		SmsMessage customerSMSMessage = new SmsMessage(_workFlowWrapper.getCustomerAppUserModel().getMobileNo(), customerSMS);
			
		_workFlowWrapper.getTransactionDetailModel().setCustomField4(agentSMS);			
		_workFlowWrapper.getTransactionDetailModel().setCustomField8(customerSMS);
		_workFlowWrapper.getTransactionModel().setNotificationMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());//todo
		_workFlowWrapper.getTransactionModel().setConfirmationMessage(customerSMS);
			
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
		messageList.add(agentSMSMessage);
		messageList.add(customerSMSMessage);
		
		if(_workFlowWrapper.getHandlerModel() != null && _workFlowWrapper.getHandlerModel().getSmsToHandler()) {
			messageList.add(new SmsMessage(_workFlowWrapper.getHandlerAppUserModel().getMobileNo(), agentSMS));
		}
		
		_workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
	}

	
	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper _workFlowWrapper) throws Exception {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction....");
		}
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		if (_workFlowWrapper.getIsIvrResponse() == false) {
			//generate new trasnaction code and initialize Transaction/TransctionDetailMaster objects.
			_workFlowWrapper = super.doPreStart(_workFlowWrapper);
		}else{
			//load existing transaction objects
			TransactionCodeModel txCodeModel = _workFlowWrapper.getTransactionCodeModel();
			baseWrapper.setBasePersistableModel(txCodeModel);
			baseWrapper = txManager.loadTransactionCodeByCode(baseWrapper);
			_workFlowWrapper.setTransactionCodeModel((TransactionCodeModel)baseWrapper.getBasePersistableModel());
			txManager.generateTransactionObject(_workFlowWrapper);
			searchBaseWrapper.setBasePersistableModel(_workFlowWrapper.getTransactionCodeModel());
			txManager.loadTransactionByTransactionCode(searchBaseWrapper);
			
			_workFlowWrapper.setTransactionModel((TransactionModel)searchBaseWrapper.getBasePersistableModel());
			_workFlowWrapper.setTotalAmount(_workFlowWrapper.getTransactionModel().getTotalAmount());
			_workFlowWrapper.setTotalCommissionAmount(_workFlowWrapper.getTransactionModel().getTotalCommissionAmount());
			_workFlowWrapper.setBillAmount(_workFlowWrapper.getTransactionModel().getTransactionAmount());
			_workFlowWrapper.setTransactionAmount(_workFlowWrapper.getTransactionModel().getTransactionAmount());
			_workFlowWrapper.setTxProcessingAmount(_workFlowWrapper.getTransactionModel().getTotalCommissionAmount());
			
			List<TransactionDetailModel> transactionDetailModelList = new ArrayList<TransactionDetailModel>(_workFlowWrapper.getTransactionModel().getTransactionIdTransactionDetailModelList());
			if(transactionDetailModelList != null && transactionDetailModelList.size() > 0) {
				_workFlowWrapper.setTransactionDetailModel(transactionDetailModelList.get(0));
			}
		}
		// Populate the Product model from DB
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getProductModel());
		baseWrapper = productManager.loadProduct(baseWrapper);
		_workFlowWrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());
		
		// Populate Retailer Contact model from DB
		searchBaseWrapper = new SearchBaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
	    retailerContactModel.setRetailerContactId( _workFlowWrapper.getFromRetailerContactAppUserModel().getRetailerContactId() );
	    searchBaseWrapper.setBasePersistableModel(retailerContactModel);
	    searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
	    retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();
	    _workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
	    
	    if(!retailerContactModel.getHead()) {
		    
		    baseWrapper = retailerContactManager.loadHeadRetailerContactAppUser(retailerContactModel.getRetailerId());
		    RetailerContactModel _retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
		    
		    if(_retailerContactModel != null) {
			
		    	SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
				smartMoneyAccountModel.setRetailerContactId(_retailerContactModel.getRetailerContactId());
				smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				smartMoneyAccountModel.setActive(Boolean.TRUE);
				smartMoneyAccountModel.setDeleted(Boolean.FALSE);			
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = this.smartMoneyAccountManager.loadSmartMoneyAccount(searchBaseWrapper);
				smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);		    
			    
				AppUserModel appUserModel = new AppUserModel();
				appUserModel.setRetailerContactId(_retailerContactModel.getRetailerContactId());			
				baseWrapper.setBasePersistableModel(appUserModel);			
				baseWrapper = this.appUserManager.loadAppUser(baseWrapper);
				
				appUserModel = (AppUserModel)baseWrapper.getBasePersistableModel();
			    _workFlowWrapper.setHeadRetailerContactModel(_retailerContactModel);
			    _workFlowWrapper.setHeadRetailerSmaModel(smartMoneyAccountModel);
			    _workFlowWrapper.setHeadRetailerAppuserModel(appUserModel);
		    }
	    }
	    
	    
	    if(_workFlowWrapper.getCustomerAppUserModel() != null && ((Integer)_workFlowWrapper.getCustomField()).intValue() == 0) {

			AppUserModel appUserModel = _workFlowWrapper.getCustomerAppUserModel();
			
			BaseWrapper _baseWrapper = new BaseWrapperImpl();
			
	    	SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
			smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
			smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			smartMoneyAccountModel.setActive(Boolean.TRUE);
			smartMoneyAccountModel.setDeleted(Boolean.FALSE);			
			_baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			
			_baseWrapper = this.smartMoneyAccountManager.searchSmartMoneyAccount(_baseWrapper);
			smartMoneyAccountModel = (SmartMoneyAccountModel)_baseWrapper.getBasePersistableModel();		    
		    
			_workFlowWrapper.putObject("CUSTOMER_SmartMoneyAccountModel", smartMoneyAccountModel);


			// For Agent Retention Commission calculation - reloading customerAppUserModel (to avoid the owning Session was closed issue)
			AppUserModel customerAppUserModel = new AppUserModel();
			customerAppUserModel.setMobileNo(_workFlowWrapper.getCustomerAppUserModel().getMobileNo());
			customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);
			
			if(null != customerAppUserModel){
				_workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
				_workFlowWrapper.setCustomerModel(customerAppUserModel.getCustomerIdCustomerModel());
			}
	    }	    
	    
	    DistributorModel distributorModel = new DistributorModel();
	    distributorModel.setDistributorId(retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
	    _workFlowWrapper.setDistributorModel(distributorModel);
	    
		NotificationMessageModel notificationMessage = new NotificationMessageModel();
		notificationMessage.setNotificationMessageId(_workFlowWrapper.getProductModel().getInstructionId());
		baseWrapper.setBasePersistableModel(notificationMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		_workFlowWrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		NotificationMessageModel successMessage = new NotificationMessageModel();
		successMessage.setNotificationMessageId(_workFlowWrapper.getProductModel().getSuccessMessageId());
		baseWrapper.setBasePersistableModel(successMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		_workFlowWrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(_workFlowWrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		
		_workFlowWrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		_workFlowWrapper.setPaymentModeModel(new PaymentModeModel());
		_workFlowWrapper.getPaymentModeModel().setPaymentModeId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

		// Populate Supplier Bank Info Model
		
		SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
		supplierBankInfoModel.setSupplierId(_workFlowWrapper.getProductModel().getSupplierId());

		supplierBankInfoModel = this.supplierBankInfoManager.getSupplierBankInfoModel(supplierBankInfoModel);

		_workFlowWrapper.setSupplierBankInfoModel(supplierBankInfoModel);

		OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();

		if (supplierBankInfoModel != null) {
			// Populate Operator's Paying Bank Info Model
			operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
			operatorBankInfoModel.setPaymentModeId(supplierBankInfoModel.getPaymentModeId());
			operatorBankInfoModel.setBankId(supplierBankInfoModel.getBankId());
			baseWrapper.setBasePersistableModel(operatorBankInfoModel);
			_workFlowWrapper.setOperatorPayingBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
		}

		// Populate Operator Bank Info Model

		operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
		operatorBankInfoModel.setPaymentModeId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
		operatorBankInfoModel.setBankId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getBankId());
		baseWrapper.setBasePersistableModel(operatorBankInfoModel);
		_workFlowWrapper.setOperatorBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
		Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_BILL_POOL_ACCOUNT_ID;
		if(OneBillProductEnum.contains(_workFlowWrapper.getProductModel().getProductId().toString()))
			utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_BLB_POOL_ACCOUNT_ID;
		
		StakeholderBankInfoModel ubpPoolStakeholderBankInfoModel= new StakeholderBankInfoModel(utilityBillPoolAccountId);
		
		SearchBaseWrapper ubpSearchBaseWrapper = new SearchBaseWrapperImpl();	
		ubpSearchBaseWrapper.setBasePersistableModel(ubpPoolStakeholderBankInfoModel);
		ubpSearchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(ubpSearchBaseWrapper);	
		
		ubpPoolStakeholderBankInfoModel = (StakeholderBankInfoModel) ubpSearchBaseWrapper.getBasePersistableModel();
		_workFlowWrapper.getDataMap().put(utilityBillPoolAccountId.toString(), ubpPoolStakeholderBankInfoModel);
		
		// Populate Handler's Retailer Contact model from DB
		if(_workFlowWrapper.getHandlerModel() != null){
			searchBaseWrapper = new SearchBaseWrapperImpl();
			RetailerContactModel retailerContact = new RetailerContactModel();
			retailerContact.setRetailerContactId( _workFlowWrapper.getHandlerModel().getRetailerContactId() );
			searchBaseWrapper.setBasePersistableModel( retailerContact );
			searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
			_workFlowWrapper.setHandlerRetContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());

			// Populate the Handler OLA Smart Money Account from DB
			SmartMoneyAccountModel sma = smartMoneyAccountManager.getSMAccountByHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
			_workFlowWrapper.setHandlerSMAModel(sma);

			// Set Handler User Device Account Model
			UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
			handlerUserDeviceAccountsModel.setAppUserId(_workFlowWrapper.getHandlerAppUserModel().getAppUserId());
			handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
			baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			_workFlowWrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		}
		
		
		logger.debug("Ending doPreStart(WorkFlowWrapper _workFlowWrapper) of UtilityBillSaleTransaction....");
		
		return _workFlowWrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper workFlowWrapper) throws Exception {
	
		if(logger.isDebugEnabled()) {
			logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction....");
		}
		workFlowWrapper = super.doPreProcess(workFlowWrapper);
		
		if (workFlowWrapper.getIsIvrResponse()) {
			return workFlowWrapper;
		}

		TransactionModel transactionModel = workFlowWrapper.getTransactionModel();

		transactionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		transactionModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
		transactionModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		transactionModel.setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() ) ;
		transactionModel.setRetailerId(workFlowWrapper.getFromRetailerContactModel().getRetailerId());
		transactionModel.setDistributorId(workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		transactionModel.setProcessingBankId(workFlowWrapper.getOlaSmartMoneyAccountModel().getBankId());
		transactionModel.setTransactionAmount(workFlowWrapper.getBillAmount());
		transactionModel.setTotalAmount(workFlowWrapper.getBillAmount());
		transactionModel.setTotalCommissionAmount(0d);
		transactionModel.setDiscountAmount(0d);

		transactionModel.setTransactionTypeIdTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());

		transactionModel.setDeviceTypeId(workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());

		transactionModel.setPaymentModeId(workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

		transactionModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		
		Integer paymentType = (Integer) workFlowWrapper.getCustomField();
		boolean isCustomerTransaction = paymentType.intValue() == 0 ? Boolean.TRUE : Boolean.FALSE;
		
		Long smartMoneyAccountId = null;
		
		if(!isCustomerTransaction) {
			smartMoneyAccountId = workFlowWrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId();
		} else {
			SmartMoneyAccountModel customerSmartMoneyAccountModel = (SmartMoneyAccountModel) workFlowWrapper.getObject("CUSTOMER_SmartMoneyAccountModel");			
			smartMoneyAccountId = customerSmartMoneyAccountModel.getSmartMoneyAccountId();
		}
		
		transactionModel.setSmartMoneyAccountId(smartMoneyAccountId);
		String mfsId = null;
		
		if(isCustomerTransaction) {
			
			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setAppUserId(workFlowWrapper.getCustomerAppUserModel().getAppUserId());
			userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();			
			baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);			
			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
			mfsId = userDeviceAccountsModel.getUserId();
			
		} else {

			mfsId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
		}
		
		transactionModel.setMfsId(mfsId);

		workFlowWrapper.setTransactionModel(transactionModel);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of UtilityBillSaleTransaction....");
		}

		return workFlowWrapper;
	}


	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		
		if(logger.isDebugEnabled())	{
			logger.debug("Inside getTransactionProcessingCharges of UtilityBillSaleTransaction....");
		}
		
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList) {
			
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.ALLPAY_SERVICE_CHARGE.longValue()) {

				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue()) {
				
					transProcessingAmount += commissionRateModel.getRate();
				} else {
					transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;
				}
			}
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Ending getTransactionProcessingCharges of UtilityBillSaleTransaction....");
		}
		
		return transProcessingAmount;
	}

	public void setOperatorManager(OperatorManager operatorManager) {
		this.operatorManager = operatorManager;
	}

	@Override
	protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper) throws Exception {
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper) throws Exception {
		return wrapper;
	}

	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws Exception {
		
		MARK_TRANSACTION_FAILED : {
		
			workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
			workFlowWrapper.getTransactionModel().setTransactionId(null);
//			workFlowWrapper.getTransactionDetailModel().setTransactionDetailId(null);
			workFlowWrapper.getTransactionModel().setConfirmationMessage(" ");
//			txManager.saveTransaction(workFlowWrapper);		
		}
		
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(workFlowWrapper.getOlaSmartMoneyAccountModel());
		
		OLAVeriflyFinancialInstitutionImpl financialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		String olaFromAccount = (String)workFlowWrapper.getDataMap().get(OLA_FROM_ACCOUNT);
		String olaToAccount = (String)workFlowWrapper.getDataMap().get(OLA_TO_ACCOUNT);

		if(!StringUtil.isNullOrEmpty(olaFromAccount) && !StringUtil.isNullOrEmpty(olaToAccount)) {
			
			OLAVO olaVO = workFlowWrapper.getSwitchWrapper().getOlavo();
			olaVO.setPayingAccNo(olaToAccount);
			olaVO.setReceivingAccNo(olaFromAccount);
			
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();	
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
			switchWrapper.setBasePersistableModel(workFlowWrapper.getOlaSmartMoneyAccountModel());
			switchWrapper.setBankId(workFlowWrapper.getOlaSmartMoneyAccountModel().getBankId());	
			switchWrapper.setTransactionAmount(workFlowWrapper.getBillAmount());		
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setFromAccountNo(olaToAccount);
			switchWrapper.setToAccountNo(olaFromAccount);		
			switchWrapper.setOlavo(olaVO);
			
			financialInstitution.rollback(switchWrapper);
		}	
		
		return workFlowWrapper;
	}
	
	
	protected Long logActionLogModel() {
		
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setAppUserIdAppUserModel(ThreadLocalAppUser.getAppUserModel());
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
	
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();

		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		
		try {
		
			baseWrapperActionLog = actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
			
		} catch (FrameworkCheckedException e) {
			logger.error(e.getLocalizedMessage());
		}
		
		if(baseWrapperActionLog != null && baseWrapperActionLog.getBasePersistableModel() != null) {
			
			actionLogModel = (ActionLogModel) baseWrapperActionLog.getBasePersistableModel();			
		}
		
		return actionLogModel.getActionLogId();
	}	
	
	
//	DEPENDANCY INJECTION (IOC)
	
	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)	{
		this.financialIntegrationManager = financialIntegrationManager;
	}
	public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}
	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}
	public MessageSource getMessageSource() {
		return messageSource;
	}
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}
	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
	public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
		this.notificationMessageManager = notificationMessageManager;
	}
	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}
	public void setSupplierManager(SupplierManager supplierManager) {
		this.supplierManager = supplierManager;
	}
	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}
	public void setCustomerManager(CustTransManager customerManager) {
		this.customerManager = customerManager;
	}
	public void setProductDispenseController(ProductDispenseController productDispenseController) {
		this.productDispenseController = productDispenseController;
	}
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}
	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}
	public void setVeriflyController(VeriflyManagerService veriflyController) {
		this.veriflyController = veriflyController;
	}
	public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager) {
		this.supplierBankInfoManager = supplierBankInfoManager;
	}
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
}

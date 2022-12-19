package com.inov8.microbank.server.service.workflow.sales;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.BulkBillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;


public class BulkBillPaymentTransaction extends SalesTransaction {
	
	protected final Log log = LogFactory.getLog(getClass());
	private MessageSource messageSource;
	private DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
	
	private String OLA_FROM_ACCOUNT = "OLA_FROM_ACCOUNT";
	private String OLA_TO_ACCOUNT = "OLA_TO_ACCOUNT";

	private CustTransManager customerManager;
	private CommissionManager commissionManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private ProductDispenseController productDispenseController;
	private GenericDao genericDAO;
	private RetailerContactManager retailerContactManager;	
	private FinancialIntegrationManager financialIntegrationManager;
	private AbstractFinancialInstitution phoenixFinancialInstitution;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private SupplierBankInfoManager supplierBankInfoManager;
	private String rrnPrefix;



	public WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Inside getBillInfo(WorkFlowWrapper wrapper) of BulkBillPaymentTransaction...");
			logger.debug("Loading ProductDispenser...");
		}
		
		BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
		logger.debug("Fetching Bill Info through Product Dispenser...");
		return billSaleProductDispenser.getBillInfo(wrapper);
	}


	
	public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper) throws FrameworkCheckedException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Inside validateBillInfo(WorkFlowWrapper wrapper) of BulkBillPaymentTransaction...");
		}
		
		BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
		wrapper = billSaleProductDispenser.verify(wrapper);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Ending validateBillInfo(WorkFlowWrapper wrapper) of BulkBillPaymentTransaction...");
		}
			
		return wrapper;
	}


	public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {

		if (logger.isDebugEnabled())
		{
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of BulkBillPaymentTransaction...");
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

		return commissionWrapper;
	}


	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateCommission of BulkBillPaymentTransaction...");
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

			logger.debug("Ending validateCommission of BulkBillPaymentTransaction...");
		}

	}


	public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper) {
		return wrapper;
	}


	public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) {
		return wrapper;
	}


	public WorkFlowWrapper updateSupplier(WorkFlowWrapper wrapper) {
		return wrapper;
	}

	
	public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {


		if(logger.isDebugEnabled()) {
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of BulkBillPaymentTransaction");
		}
		
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
		}		return wrapper;
	}



	public WorkFlowWrapper doSale(WorkFlowWrapper _workFlowWrapper) throws Exception {
		
		CopyOnWriteArrayList<BulkBillPaymentVO> bulkBillPaymentVOList = (CopyOnWriteArrayList<BulkBillPaymentVO>) _workFlowWrapper.getObject(BulkBillPaymentVO.BulkBillPaymentVO);

		StakeholderBankInfoModel stakeholderBankInfoModel = this.stakeholderBankInfoManager.loadDistributorStakeholderBankInfoModel(ThreadLocalAppUser.getAppUserModel());
				
		for (BulkBillPaymentVO billPaymentVO : bulkBillPaymentVOList) {
			
			WorkFlowWrapper workFlowWrapper = _workFlowWrapper;
			workFlowWrapper.setTransactionModel(null);
			workFlowWrapper.setTransactionCodeModel(null);
			workFlowWrapper.setTransactionDetailModel(null);

			 executeBulkBillPayment(workFlowWrapper, billPaymentVO, stakeholderBankInfoModel);
		}
		
		return _workFlowWrapper;
	}
	
	
	public WorkFlowWrapper executeBulkBillPayment(WorkFlowWrapper _workFlowWrapper, BulkBillPaymentVO billPaymentVO, StakeholderBankInfoModel stakeholderBankInfoModel) throws Exception {

		TransactionCodeModel transactionCodeModel = this.txManager.generateTransactionCodeRequiresNewTransaction(_workFlowWrapper).getTransactionCodeModel();
		
		TransactionModel transactionModel = new TransactionModel();
		
		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
		transactionDetailModel.setCustomField6(billPaymentVO.getMobileNumber());
		transactionDetailModel.setCustomField7(_workFlowWrapper.getWalkInCustomerCNIC());
		transactionDetailModel.setConsumerNo(billPaymentVO.getConsumerNo());
		transactionDetailModel.setSettled(Boolean.FALSE);
		transactionDetailModel.setProductId(billPaymentVO.getProductId());	
		
		transactionModel.setTransactionCodeIdTransactionCodeModel(transactionCodeModel);
		transactionModel.setTransactionAmount(billPaymentVO.getBillAmount());
		transactionModel.setConfirmationMessage(" _ ");
		transactionModel.setNotificationMobileNo(billPaymentVO.getMobileNumber());
		transactionModel.setBankAccountNo(StringUtil.replaceString(_workFlowWrapper.getAccountInfoModel().getAccountNo(), 5, "*"));
		transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
		transactionModel.setTotalAmount(billPaymentVO.getBillAmount());	
		transactionModel.setCreatedByAppUserModel(ThreadLocalAppUser.getAppUserModel());
		transactionModel.setUpdatedByAppUserModel(ThreadLocalAppUser.getAppUserModel());
		transactionModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
		transactionModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		transactionModel.setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() ) ;
		transactionModel.setSmartMoneyAccountId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
		transactionModel.setRetailerId(_workFlowWrapper.getFromRetailerContactModel().getRetailerId());
		transactionModel.setDistributorId(_workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		transactionModel.setProcessingBankId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getBankId());
		transactionModel.setTotalCommissionAmount(0d);
		transactionModel.setDiscountAmount(0d);
		transactionModel.setTransactionTypeIdTransactionTypeModel(_workFlowWrapper.getTransactionTypeModel());
		transactionModel.setDeviceTypeId(_workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
		transactionModel.setPaymentModeId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
		transactionModel.setCustomerMobileNo(billPaymentVO.getMobileNumber());
		transactionModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		transactionModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());		
		
		transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);	
		
		_workFlowWrapper.setTransactionModel(transactionModel);
		
		txManager.transactionRequiresNewTransaction(_workFlowWrapper);

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, billPaymentVO.getProductId());
		baseWrapper.putObject(CommandFieldConstants.KEY_CUST_CODE, billPaymentVO.getConsumerNo());
		
		UtilityBillVO utilityBillVO = (UtilityBillVO) this.productDispenseController.loadProductVO(baseWrapper);
		
		utilityBillVO.setBillAmount(billPaymentVO.getBillAmount());
		utilityBillVO.setLateBillAmount(billPaymentVO.getBillAmount());
		utilityBillVO.setConsumerNo(billPaymentVO.getConsumerNo());
		utilityBillVO.setBillingMonth("");
		utilityBillVO.setCompanyCode("");
		utilityBillVO.setCompanyName("");
		utilityBillVO.setCustomerAddress("");
		utilityBillVO.setCustomerName("");
		utilityBillVO.setDueDate(null);
		
		_workFlowWrapper.setProductVO(utilityBillVO);
		
		ProductModel productModel = (ProductModel)baseWrapper.getBasePersistableModel();
		_workFlowWrapper.setProductModel(productModel);

		
		ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
		productDeviceFlowModel.setProductId(billPaymentVO.getProductId());
		productDeviceFlowModel.setDeviceTypeId( DeviceTypeConstantsInterface.ALLPAY_WEB);
			
		List<ProductDeviceFlowListViewModel> list = this.genericDAO.findEntityByExample(productDeviceFlowModel, null);

		if(list != null && list.size() > 0) {
			productDeviceFlowModel = list.get(0);	
			_workFlowWrapper.setProductDeviceFlowModel(productDeviceFlowModel);
		}

		transactionDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
		_workFlowWrapper.setTransactionDetailModel(transactionDetailModel);			

		SmartMoneyAccountModel smartMoneyAccountModel = _workFlowWrapper.getOlaSmartMoneyAccountModel();

		SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
		supplierBankInfoModel.setSupplierId(productModel.getSupplierId());
		supplierBankInfoModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
		baseWrapper.setBasePersistableModel(supplierBankInfoModel);
		supplierBankInfoModel = (SupplierBankInfoModel) this.supplierBankInfoManager.loadSupplierBankInfo(baseWrapper).getBasePersistableModel();
		_workFlowWrapper.setSupplierBankInfoModel(supplierBankInfoModel);		
					
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setTransactionTransactionModel(transactionModel);
		switchWrapper.setAccountInfoModel(_workFlowWrapper.getAccountInfoModel());
		
		if (logger.isDebugEnabled()) {
			logger.debug("Executing Bill Sale on BillSaleProductDispenser....");
		}
			
		Double balance = 0.0D ;
		
		BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);

		rrnPrefix = buildRRNPrefix();
			
		SwitchWrapper switchWrapperImpl = new SwitchWrapperImpl();
		switchWrapperImpl.setAgentBalance(balance);
		switchWrapperImpl.setSkipAccountInfoLoading(Boolean.TRUE);
		_workFlowWrapper.setSwitchWrapper(switchWrapperImpl);
			
		/*
		 *** 	SENDING BILL PAYMENT TO OLA BANKING.
		 */				
		
		logger.info("OLA > SENDING BILL PAYMENT TO OLA BANKING.");
												
		_workFlowWrapper = billSaleProductDispenser.doSale(_workFlowWrapper);
			
		_workFlowWrapper.getDataMap().put(OLA_FROM_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getFromAccountNo());
		_workFlowWrapper.getDataMap().put(OLA_TO_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getToAccountNo());
			
		switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel) ;
			
		switchWrapper.getWorkFlowWrapper().setProductModel(productModel);			
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapper.setBasePersistableModel(smartMoneyAccountModel);

		billSaleProductDispenser.prepairSwitchWrapper(_workFlowWrapper.getProductModel().getProductId(), switchWrapper);
			
		switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel) ;
		_workFlowWrapper.setSwitchWrapper(switchWrapper);

		if(_workFlowWrapper.getFirstFTIntegrationVO() != null) {
				
			String bankResponseCode = _workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();
				
			if(bankResponseCode != null) {
				_workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
			}
		}						
			
		SAVE_TRANSACTION : {
				
			transactionModel.setSupProcessingStatusId( SupplierProcessingStatusConstants.BILL_AUTHORIZATION_SENT );
				
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setTransactionModel(transactionModel);

			txManager.transactionRequiresNewTransaction(workFlowWrapper);
				
			transactionModel = workFlowWrapper.getTransactionModel();
				
			_workFlowWrapper.setTransactionModel(transactionModel);
			_workFlowWrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
				
		}
			
		switchWrapper.setFromAccountNo(stakeholderBankInfoModel.getAccountNo());			
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);	
		switchWrapper.setSkipAccountInfoLoading(Boolean.TRUE);
			

		logger.info("Phoenix/NADRA > SENDING BILL PAYMENT TO Phoenix/NADRA Integration for Authorization of Bill.");
			
		switchWrapper = phoenixFinancialInstitution.billPayment(switchWrapper);
			
		_workFlowWrapper.setTransactionModel(transactionModel);	
		
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
			
		CREATE_SEND_SMS : {			
					
			Object[] agentSMSParam = new Object[] {Formatter.formatDouble(transactionModel.getTotalAmount()),
									productModel.getName(), transactionDetailModel.getConsumerNo(),
									dtf.print(new DateTime()), tf.print(new LocalTime()), transactionCodeModel.getCode()};
					
			Object[] customerSMSParam = new Object[] {Formatter.formatDouble(transactionModel.getTotalAmount()),
									productModel.getName(), transactionDetailModel.getConsumerNo(),
									dtf.print(new DateTime()), tf.print(new LocalTime()), transactionCodeModel.getCode()};
					
			String agentSMS = this.getMessageSource().getMessage("USSD.AgentBillPaymentSMS", agentSMSParam, null);
					
			String customerSMS = this.getMessageSource().getMessage("USSD.CustomerBillPaymentNotificationforAgent", customerSMSParam, null);
					
			transactionDetailModel.setCustomField4(agentSMS);
			transactionDetailModel.setCustomField8(customerSMS);
			transactionDetailModel.setSettled(Boolean.TRUE);

			transactionModel.setConfirmationMessage(customerSMS);
			transactionModel.setNotificationMobileNo(billPaymentVO.getMobileNumber());

			messageList.add(new SmsMessage(_workFlowWrapper.getAppUserModel().getMobileNo(), agentSMS));
			messageList.add(new SmsMessage(billPaymentVO.getMobileNumber(), customerSMS));
		}
						
			
		if(_workFlowWrapper.getFirstFTIntegrationVO() != null) {
				
			String bankResponseCode = _workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();
				
			if(bankResponseCode != null) {
				_workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
			}
		}			
			
		_workFlowWrapper.setTransactionModel(transactionModel);
			
		txManager.saveTransaction(_workFlowWrapper);

		_workFlowWrapper.setTransactionModel(transactionModel);
		_workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
		_workFlowWrapper.setTransactionDetailModel(transactionDetailModel);

		_workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
		
		MAKE_RESPONSE : {
			
			long supProcessingStatusId = transactionModel.getSupProcessingStatusId().longValue();
			
			if(supProcessingStatusId == 1L) {
				billPaymentVO.setSupProcessingStatus("Complete");
			}
			if(supProcessingStatusId == 2L) {
				billPaymentVO.setSupProcessingStatus("Failed");
			}
			if(supProcessingStatusId == 3L) {
				billPaymentVO.setSupProcessingStatus("Ambiguous");
			}
			if(supProcessingStatusId == 4L) {
				billPaymentVO.setSupProcessingStatus("In-process");
			}
			if(supProcessingStatusId == 5L) {
				billPaymentVO.setSupProcessingStatus("Reversed");
			}
			if(supProcessingStatusId == 6L) {
				billPaymentVO.setSupProcessingStatus("Unclaimed");
			}
			if(supProcessingStatusId == 7L) {
				billPaymentVO.setSupProcessingStatus("Bank Settlement Pending");
			}
			if(supProcessingStatusId == 8L) {
				billPaymentVO.setSupProcessingStatus("Bill Authorization Status Sent");
			}
			
			billPaymentVO.setProductName(productModel.getName());
			billPaymentVO.setTransactionCode(transactionCodeModel.getCode());
		}
		
		if (logger.isDebugEnabled()) {
			
			logger.debug("Ending doSale of BulkBillPaymentTransaction.");
		}

		return _workFlowWrapper;
	}


	
	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper _workFlowWrapper) throws Exception {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of BulkBillPaymentTransaction....");
		}
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		_workFlowWrapper = super.doPreStart(_workFlowWrapper);
		
		// Populate Retailer Contact model from DB
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
	    retailerContactModel.setRetailerContactId( _workFlowWrapper.getFromRetailerContactAppUserModel().getRetailerContactId() );
	    searchBaseWrapper.setBasePersistableModel(retailerContactModel);
	    searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
	    retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();
	    _workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
	    
	    DistributorModel distributorModel = new DistributorModel();
	    distributorModel.setDistributorId(retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
	    _workFlowWrapper.setDistributorModel(distributorModel);

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(_workFlowWrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALLPAY_WEB);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		
		_workFlowWrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		_workFlowWrapper.setPaymentModeModel(new PaymentModeModel());
		_workFlowWrapper.getPaymentModeModel().setPaymentModeId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

		logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of BulkBillPaymentTransaction....");
		
		return _workFlowWrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper workFlowWrapper) throws Exception {
	
		if(logger.isDebugEnabled()) {
			logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of BulkBillPaymentTransaction....");
		}
		
		workFlowWrapper = super.doPreProcess(workFlowWrapper);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of BulkBillPaymentTransaction....");
		}

		return workFlowWrapper;
	}


	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		
		if(logger.isDebugEnabled())	{
			logger.debug("Inside getTransactionProcessingCharges of BulkBillPaymentTransaction....");
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
			logger.debug("Ending getTransactionProcessingCharges of BulkBillPaymentTransaction....");
		}
		
		return transProcessingAmount;
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
		/*
		MARK_TRANSACTION_FAILED : {
		
			workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
			workFlowWrapper.getTransactionModel().setTransactionId(null);
			workFlowWrapper.getTransactionDetailModel().setTransactionDetailId(null);
			
			txManager.saveTransaction(workFlowWrapper);		
		}
		
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(workFlowWrapper.getSmartMoneyAccountModel());
		
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
		*/
		return workFlowWrapper;
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
	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}
	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
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
	public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager) {
		this.supplierBankInfoManager = supplierBankInfoManager;
	}
}

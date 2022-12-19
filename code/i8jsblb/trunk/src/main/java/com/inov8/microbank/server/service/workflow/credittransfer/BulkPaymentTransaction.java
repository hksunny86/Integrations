package com.inov8.microbank.server.service.workflow.credittransfer;


import java.util.Date;
import java.util.List;

import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.disbursement.service.BulkDisbursementsManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.BulkPaymentVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.ola.integration.vo.OLAVO;

public class BulkPaymentTransaction extends CreditTransferTransaction{

  private FinancialIntegrationManager financialIntegrationManager;
  private NotificationMessageManager notificationMessageManager;
  private ProductManager productManager;
  private CommissionManager commissionManager;
  private MfsAccountManager mfsAccountManager;
  private WalkinCustomerModel recepientWalkinCustomerModel;
  private SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel;
  private MessageSource messageSource;
  private BulkDisbursementsManager bulkDisbursementsManager;
  private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
  
  /**
   * Credit transfer transaction takes place over here
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   */
  public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws Exception{

	  if (wrapper.isWalkinLimitApplicable()) {
		  wrapper = this.verifyThroughputLimits(wrapper);
	  } 
		
	    TransactionDetailModel txDetailModel = new TransactionDetailModel();

	  
//	  	CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
//		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
//		commission is calculated in transaction... so skipping...
//		this.validateCommission(commissionWrapper, wrapper);
//		wrapper.setCommissionAmountsHolder(commissionAmounts);
	    
	    CommissionAmountsHolder commissionAmounts = wrapper.getCommissionAmountsHolder();
	    
		wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
//		wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());

		txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
		txDetailModel.setCustomField5(wrapper.getWalkInCustomerMob());
		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
		
		
	    BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		
		String txAmount = Formatter.formatNumbers(wrapper.getTransactionAmount());
	    wrapper.getTransactionModel().setConfirmationMessage(" _ ");
//	    wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
	    wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
	    wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getWalkInCustomerMob());
//	    wrapper.getTransactionModel().setSaleMobileNo(wrapper.getRecipientWalkinCustomerModel().getMobileNumber());
//	    wrapper.getTransactionModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());
	    txDetailModel.setSettled(false);
	    
	    wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstantsInterface.IN_PROGRESS);
		
//		set recipient CNIC
		txDetailModel.setCustomField9(wrapper.getRecipientWalkinCustomerModel().getCnic());
		
		wrapper.setTransactionDetailModel(txDetailModel);
		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);

		txManager.saveTransaction(wrapper); //save the transaction
		wrapper.setCommissionSettledOnLeg2(true);

		this.settlementManager.settleCommission(wrapper.getCommissionWrapper(), wrapper);

		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel()) ;
		
		switchWrapper.setOlavo(new OLAVO());
		switchWrapper.getOlavo().setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);

		logger.info("[BulkPaymentTransaction.doCreditTransfer] Going to settle commissions at OLA. Sender SmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		
		switchWrapper = ((OLAVeriflyFinancialInstitutionImpl)olaVeriflyFinancialInstitution).bulkPaymentTransaction(switchWrapper);
		wrapper.setOLASwitchWrapper(switchWrapper);
		
		// this amount is to be disbursed to customer at the time of account opening
		txDetailModel.setActualBillableAmount(commissionAmounts.getTransactionAmount() - switchWrapper.getInclusiveChargesApplied());
		
		if(switchWrapper.getSwitchSwitchModel() != null){
			wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
		}
		wrapper.getTransactionDetailModel().setCustomField11(switchWrapper.getOlavo().getAuthCode());
//		((BulkPaymentVO)(wrapper.getProductVO())).setBalance(switchWrapper.getBalance());
		
		logger.info("[BulkPaymentTransaction.doCreditTransfer()] Going to Settle Commissions. Transaction ID: " + wrapper.getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		
//		switchWrapper = abstractFinancialInstitution.settleBulkPaymentCommission(switchWrapper);
//		wrapper.setOlaSwitchWrapper_2(switchWrapper);
//
		switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
//		
		switchWrapper = abstractFinancialInstitution.saveBulkPaymentWalkInLedgerEntry(switchWrapper);
//		wrapper.setOlaSwitchWrapper_3(switchWrapper);

		switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField12(switchWrapper.getOlavo().getAuthCode());

	    BulkDisbursementsModel bdModel = new BulkDisbursementsModel();

	    logger.info("[BulkPaymentTransaction.doCreditTransfer()] Going to mark settled=1 for BulkDisbursmentsId=" + wrapper.getBulkDisbursmentsId()+" , transactionID:"+wrapper.getTransactionCodeModel().getCode());
		SearchBaseWrapper sWrapper = new SearchBaseWrapperImpl();
		bdModel.setBulkDisbursementsId(wrapper.getBulkDisbursmentsId());
		sWrapper.setBasePersistableModel(bdModel);
		sWrapper = this.bulkDisbursementsManager.loadBulkDisbursement(sWrapper);
		bdModel = (BulkDisbursementsModel) sWrapper.getBasePersistableModel();
	    
		if(null != bdModel){
			bdModel.setTransactionCode(wrapper.getTransactionCodeModel().getCode());
		    bdModel.setSettled(Boolean.TRUE);
		    bdModel.setSettledOn(new Date());
		    bdModel.setUpdatedOn(new Date());
		    bdModel = this.bulkDisbursementsManager.saveOrUpdateBulkDisbursement(bdModel);
		    txDetailModel.setCustomField14(bdModel.getServiceId().toString());
		    txDetailModel.setCustomField15(bdModel.getPayCashViaCnic().toString());
	    }else{
	    	logger.error("[BulkPaymentTransaction.doCreditTransfer()] Unable to load BulkDisbursementModel against BulkDisbursmentsId:"+wrapper.getBulkDisbursmentsId());
	    	throw new WorkFlowException("Unable to load BulkDisbursementModel");
	    }
	    
		txManager.saveTransaction(wrapper);
//	    rrnPrefix = buildRRNPrefix();
		
		//credit Acccount To Cash sundry account in OLA
//		wrapper = accountToCashDispenser.doSale(wrapper);

	  String brandName = null;
	  if(UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(Long.valueOf(50028L))) {
		  brandName = MessageUtil.getMessage("sco.brandName");
	  } else {
		  brandName = MessageUtil.getBrandName();
	  }


        String[] smsParams = new String[] {
                brandName,
                wrapper.getTransactionCodeModel().getCode(),
                Formatter.formatDouble(wrapper.getTransactionModel().getTransactionAmount() - wrapper.getOLASwitchWrapper().getInclusiveChargesApplied()),
                wrapper.getProductModel().getName(),
                PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
                PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                brandName};

        String recipientSmsText = MessageUtil.getMessage("bulkPayment.recipient.Leg1SMS", smsParams);

        txDetailModel.setCustomField8(recipientSmsText);

        wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(wrapper.getWalkInCustomerMob() , recipientSmsText));

        txManager.saveTransaction(wrapper);

        return wrapper;
    
  }

	/**
	 * verifies throughput limits of WalkIn customer
	 * 
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws Exception
	 */
	private WorkFlowWrapper verifyThroughputLimits(WorkFlowWrapper wrapper) throws Exception{
		Double transactionAmount = wrapper.getCommissionAmountsHolder().getTransactionAmount();
		String walkinCnic = wrapper.getWalkInCustomerCNIC();
		try{
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
			switchWrapper.setWorkFlowWrapper(wrapper);
	
			OLAVO olavo = new OLAVO();
			olavo.setBalance(transactionAmount);
			olavo.setCnic(CommonUtils.maskWalkinCustomerCNIC(walkinCnic));
			olavo.setCustomerAccountTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
			olavo.setTransactionDateTime(new Date());
			olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
			
			switchWrapper.setOlavo(olavo);
			logger.info("[BulkPaymentTransaction.verifyThroughputLimits()] Going to verify Walkin Customer Throughput Limits for CNIC:" + walkinCnic);
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			
			switchWrapper = abstractFinancialInstitution.verifyWalkinCustomerThroughputLimits(switchWrapper);
		
		}catch (Exception e){
			logger.info("[BulkPaymentTransaction.verifyThroughputLimits()] Exception in verifying Walkin Customer Throughput Limits for CNIC:" + walkinCnic + " Message: " + e.getMessage());
			throw new WorkFlowException(e.getMessage());
		}
		return wrapper;
	}
	

  /**
   * doValidate
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws Exception
   */
protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

    if ( wrapper.getTransactionAmount() <= 0 ) {
       throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
    }
    
    if ( null == wrapper.getBulkDisbursmentsId() || wrapper.getBulkDisbursmentsId() <= 0) {
        throw new WorkFlowException("BulkDisbursementID missing");
     }

    return wrapper;
  }

  public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
    txManager.saveTransaction(wrapper);
    return wrapper;
  }

  protected WorkFlowWrapper doCreditTransferStart(WorkFlowWrapper wrapper) {
    return wrapper;
  }

  protected WorkFlowWrapper doCreditTransferEnd(WorkFlowWrapper wrapper) {
    return wrapper;
  }

  /**
   * Populates the transaction object with all the necessary data to save it in the db.
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   */
  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)throws Exception
  {
	  BaseWrapper baseWrapper = new BaseWrapperImpl();

		wrapper = super.doPreStart(wrapper);

		// Populate the Product model from DB
		baseWrapper.setBasePersistableModel(wrapper.getProductModel());
		baseWrapper = productManager.loadProduct(baseWrapper);
		wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

		// --Setting instruction and success Message
		/*NotificationMessageModel notificationMessage = new NotificationMessageModel();
		notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
		baseWrapper.setBasePersistableModel(notificationMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		NotificationMessageModel successMessage = new NotificationMessageModel();
		successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
		baseWrapper.setBasePersistableModel(successMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());
*/
		// Populate the Customer model from DB
//		baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
//		baseWrapper = customerManager.loadCustomer(baseWrapper);
//		wrapper.setCustomerModel((CustomerModel) baseWrapper.getBasePersistableModel());

		// Populate the Smart Money Account from DB
//		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
//		baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
//		wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());
//		
		wrapper.setOlaSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());
//
//		wrapper.setPaymentModeModel(new PaymentModeModel());
//		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		
		// Added by mudassir - Populate the walk in customer Smart Money Account from DB
		WalkinCustomerModel walkinCustomerModel = mfsAccountManager.getWalkinCustomerModel(wrapper.getRecipientWalkinCustomerModel());
		wrapper.setRecipientWalkinCustomerModel(walkinCustomerModel);

		SmartMoneyAccountModel smartMoneyAccountModel = wrapper.getRecipientWalkinSmartMoneyAccountModel();
		smartMoneyAccountModel.setWalkinCustomerModel(walkinCustomerModel);
		SmartMoneyAccountModel walkinRecipeientSMA = mfsAccountManager.getSmartMoneyAccount(smartMoneyAccountModel);
		
		wrapper.setRecipientWalkinSmartMoneyAccountModel(walkinRecipeientSMA);
		wrapper.setSmartMoneyAccountModel(walkinRecipeientSMA);
		wrapper.setOlaSmartMoneyAccountModel(walkinRecipeientSMA);

		
    return wrapper;
  }
  

  @Override
  protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
  {
	  wrapper = super.doPreProcess(wrapper);

		TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		txModel.setTransactionAmount(wrapper.getTransactionAmount());
		txModel.setTotalAmount(wrapper.getTransactionAmount());
		txModel.setTotalCommissionAmount(0d);
		txModel.setDiscountAmount(0d);

		// Transaction Type model of transaction is populated
		txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

		// Sets the Device type
		txModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
		txModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

		// Smart Money Account Id is populated
	  	if(wrapper.getSmartMoneyAccountModel() != null) {
			txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

			// Payment mode model of transaction is populated
			txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

			// Populate processing Bank Id
			txModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
		}
		wrapper.setTransactionModel(txModel);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of BulkPaymentTransaction....");
		}

    /*wrapper = super.doPreProcess(wrapper);
    TransactionModel transactionModel = wrapper.getTransactionModel();

    transactionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
    transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
    transactionModel.setTransactionAmount(wrapper.getTransactionAmount());
    transactionModel.setTotalAmount(wrapper.getTransactionAmount());
    transactionModel.setTotalCommissionAmount(0d);
    transactionModel.setDiscountAmount(0d);
    transactionModel.setFromDistContactId(wrapper.getFromDistributorContactModel().getDistributorContactId());
    transactionModel.setToDistContactId(wrapper.getToDistributorContactModel().getDistributorContactId());
//    transactionModel.setDistributorIdDistributorModel(wrapper.getFromDistributorContactModel().getDistributorIdDistributorModel());
    transactionModel.setDistributorNmContactId(wrapper.getDistributorNmContactModel().getDistributorContactId());

    transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
    transactionModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
    transactionModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

    transactionModel.setFromDistContactMobNo(wrapper.getFromDistributorContactAppUserModel().getMobileNo());
    transactionModel.setSaleMobileNo(wrapper.getFromDistributorContactAppUserModel().getMobileNo());
    transactionModel.setToDistContactMobNo(wrapper.getToDistributorContactAppUserModel().getMobileNo());
    transactionModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getPrimaryKey());
    transactionModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
    transactionModel.setFromDistContactName(wrapper.getFromDistributorContactAppUserModel().getFirstName()+ " "+ wrapper.getFromDistributorContactAppUserModel().getLastName());

    if( wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList() != null && ((List)wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList()).get(0) != null )
    {
      AppUserModel toDistAppUser = (AppUserModel)((List)wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList()).get(0) ;
      wrapper.getTransactionModel().setToDistContactName(toDistAppUser.getFirstName() + " " + toDistAppUser.getLastName()) ;
    }

    transactionModel.setDistributorId( wrapper.getFromDistributorContactModel().getDistributorId() );
*/
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
		if (logger.isDebugEnabled()) {
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of BillSaleTransaction...");
		}
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
		RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
	    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of BillSaleTransaction...");
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
		 java.text.DecimalFormat fourDForm = new java.text.DecimalFormat("#.####");
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateCommission of BillSaleTransaction...");
		}
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		BulkPaymentVO productVO = (BulkPaymentVO) workFlowWrapper.getProductVO();

		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalCommissionAmount().doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalCommissionAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalAmount().doubleValue())).equals(  Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalAmount().doubleValue()))))
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

		if (!Double.valueOf(Formatter.formatDouble(this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTxProcessingAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}
		if (logger.isDebugEnabled())
		{

			logger.debug("Ending validateCommission of BillSaleTransaction...");
		}

	}

	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside getTransactionProcessingCharges of BillSaleTransaction....");
		}
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList)
		{
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.EXCLUSIVE_CHARGES.longValue())
			{
				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue())
					transProcessingAmount += commissionRateModel.getRate();
				else
					transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;

				//transProcessingAmount += commissionRateModel.getRate();
				//System.out.println( "  !!!!!!!!!!!!!!!!!!1 " + transProcessingAmount );
			}
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending getTransactionProcessingCharges of BillSaleTransaction....");
		}
		return transProcessingAmount;
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
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception{

		String code = wrapper.getTransactionCodeModel().getCode();
		logger.info("[BulkPaymentTransaction.rollback] Rolling back transaction with ID: " + code); 
		
		AbstractFinancialInstitution olaFinancialInstitution;
		
		if(null != wrapper.getOLASwitchWrapper() || null != wrapper.getOlaSwitchWrapper_2()){
			olaFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(OLAVeriflyFinancialInstitutionImpl.class.getName());
		
			if(null != wrapper.getOLASwitchWrapper()){
				logger.error("[CashToCashTransaction.rollback] trying to rollback Bulk Payment Sundry Account. Transaction ID:" + code + ". OLA Auth Code:" + wrapper.getOLASwitchWrapper().getOlavo().getAuthCode());
				olaFinancialInstitution.rollback(wrapper.getOLASwitchWrapper());
			}
			
			if(null != wrapper.getOlaSwitchWrapper_2()){
				logger.error("[CashToCashTransaction.rollback] trying to rollback OLA Commission Recon Mirror Account. Transaction ID:" + code + ". OLA Auth Code:" + wrapper.getOlaSwitchWrapper_2().getOlavo().getAuthCode());
				olaFinancialInstitution.rollback(wrapper.getOlaSwitchWrapper_2());
			}
		}

		return wrapper;
	}
	

  public void setSmsSender(SmsSender smsSender)
  {
    this.smsSender = smsSender;
  }

public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager)
{
	this.notificationMessageManager = notificationMessageManager;
}

public void setProductManager(ProductManager productManager)
{
	this.productManager = productManager;
}

public void setCommissionManager(CommissionManager commissionManager)
{
	this.commissionManager = commissionManager;
}

public WalkinCustomerModel getRecepientWalkinCustomerModel() {
	return recepientWalkinCustomerModel;
}

public void setRecepientWalkinCustomerModel(WalkinCustomerModel recepientWalkinCustomerModel) {
	this.recepientWalkinCustomerModel = recepientWalkinCustomerModel;
}

public SmartMoneyAccountModel getRecepientWalkinSmartMoneyAccountModel() {
	return recepientWalkinSmartMoneyAccountModel;
}

public void setRecepientWalkinSmartMoneyAccountModel(SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel) {
	this.recepientWalkinSmartMoneyAccountModel = recepientWalkinSmartMoneyAccountModel;
}

public void setMfsAccountManager(MfsAccountManager mfsAccountManager)
{
	this.mfsAccountManager = mfsAccountManager;
}

public MessageSource getMessageSource() {
	return messageSource;
}

public void setMessageSource(MessageSource messageSource) {
	this.messageSource = messageSource;
}

public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
{
	this.financialIntegrationManager = financialIntegrationManager;
}

public void setBulkDisbursementsManager(BulkDisbursementsManager bulkDisbursementsManager)
{
	this.bulkDisbursementsManager = bulkDisbursementsManager;
}
public void setOlaVeriflyFinancialInstitution(AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
	this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
}

}

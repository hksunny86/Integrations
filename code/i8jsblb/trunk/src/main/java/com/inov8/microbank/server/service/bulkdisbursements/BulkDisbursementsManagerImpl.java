package com.inov8.microbank.server.service.bulkdisbursements;

import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_TX_AMOUNT;
import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.oxm.XmlMappingException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.BulkDisbursementsViewModel;
import com.inov8.microbank.disbursement.model.BulkPaymentViewModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SettlementTransactionModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsXmlVo;
import com.inov8.microbank.server.dao.disbursementmodule.BulkDisbursementDAO;
import com.inov8.microbank.server.dao.disbursementmodule.BulkDisbursementViewDAO;
import com.inov8.microbank.server.dao.disbursementmodule.BulkPaymentViewDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.server.service.xml.XmlMarshaller;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.thoughtworks.xstream.XStream;

public class BulkDisbursementsManagerImpl implements BulkDisbursementsManager{
	private static Logger logger = Logger.getLogger(BulkDisbursementsManagerImpl.class);

	private BulkDisbursementDAO bulkDisbursementHibernateDAO;
	private BulkDisbursementViewDAO bulkDisbursementViewHibernateDAO;
	private BulkPaymentViewDAO bulkPaymentViewHibernateDAO;
	private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
	private AppUserManager appUserManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private TransactionModuleManager transactionManager;
	private ActionLogManager actionLogManager;
	private SwitchController switchController;
	private TransactionDetailMasterManager transactionDetailMasterManager;
	private ProductManager productManager;
	private JmsProducer jmsProducer;
	private XmlMarshaller<BulkDisbursementsXmlVo> xmlMarshaller;
	protected SettlementManager settlementManager;

	@Override
	public BulkDisbursementsModel saveOrUpdateBulkDisbursement(BulkDisbursementsModel bulkDisbursementsModel) throws FrameworkCheckedException
	{
		return bulkDisbursementHibernateDAO.saveOrUpdate(bulkDisbursementsModel);
	}

	public void createOrUpdateBulkDisbursements(List<BulkDisbursementsModel> dis) throws FrameworkCheckedException {
		bulkDisbursementHibernateDAO.saveOrUpdateCollection(dis);
	}
	
	@Override
	public void createOrUpdateBulkPayments(List<BulkDisbursementsModel> bulkPaymentList) throws FrameworkCheckedException
	{
		bulkDisbursementHibernateDAO.saveOrUpdateCollection(bulkPaymentList);
		for (BulkDisbursementsModel bulkPayment : bulkPaymentList)
		{
			String xml = null;
	        try
	        {
				xml = xmlMarshaller.marshal(bulkPayment.toBulkDisbursementsXmlVo());
				jmsProducer.produce(xml, DestinationConstants.BULK_WALK_IN_ACCOUNTS_DESTINATION);
			}
	        catch (XmlMappingException | IOException e)
	        {
				logger.error(e.getMessage(),e);
			}
		}
	}

	@Override
	public void update(BulkDisbursementsModel model) throws FrameworkCheckedException
	{
		bulkDisbursementHibernateDAO.update(model);
	}

	/**
	 * Fund Transfer on Phoenix from sourceAccount to customerPoolAccount)
	 * @param sourceAccount The Source Account
	 * @param targetAccount The Target Account
	 * @param totalDisbursableAmount The Amount to be disbursed
	 * @param targetAccounts The list of Bulk
	 * @throws FrameworkCheckedException if any exception occurs at any step to trigger rollback
	 */
 	public void makeDebitCreditAccount(String sourceAccount, String targetAccount, Double totalDisbursableAmount, List<BulkDisbursementsModel> bulkDisbursementsModels, WorkFlowWrapper wrapper) throws FrameworkCheckedException {

 		logger.info("OLA FT -> Bulk Dirsbursement Pool to Bulk Disbursement Settlement A/C with Total Amount: " + totalDisbursableAmount);
		
 		wrapper = creditOLABulkDisbursementSundryAccount(totalDisbursableAmount,wrapper);
		
 		bulkDisbursementHibernateDAO.saveOrUpdateCollection(bulkDisbursementsModels);
		
 		logger.info("@@@@@@@@ saving settlement transaction entry for T24 FT: with Total Amount: " + totalDisbursableAmount);
 		saveMiddlewareFTSettlementEntry(totalDisbursableAmount, ProductConstantsInterface.BULK_DISBURSEMENT);
 		
 		logger.info("@@@@@@@@ Going to hit T24 for FT: Source A/C ("+sourceAccount+")  to Bulk Dirsbursement Pool A/C ("+targetAccount+") - with Total Amount: " + totalDisbursableAmount);
 		creditCoreBulkDisbursementSundryAccount(sourceAccount,targetAccount,totalDisbursableAmount,wrapper);

	}
 	
 	
 	
 	/**
 	 * @param fromAccountNumber
 	 * @param toAccountNumber
 	 * @param amount
 	 * @param productId
 	 * @throws FrameworkCheckedException
 	 */
 	public String postCoreFundTransfer(String fromAccountNumber, String toAccountNumber, Double amount, Long productId, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

 		logger.info("\nFrom Account Number "+fromAccountNumber+"\nTo Account Number "+toAccountNumber+"\nAmount "+amount+"\nProductId "+productId);
 		
		ActionLogModel actionLogModel = new ActionLogModel();
		XStream xstream = new XStream();
		String xml = xstream.toXML("");
		actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
		actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogBeforeStart(actionLogModel); 		
 		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
		middlewareMessageVO.setTransactionAmount(String.valueOf(amount));
		middlewareMessageVO.setAccountNo1(fromAccountNumber);
		middlewareMessageVO.setAccountNo2(toAccountNumber);
		
		switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

		switchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setFromAccountNo(fromAccountNumber);

		switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountNo(toAccountNumber);

		switchWrapper.setTransactionAmount(amount);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		
		try {
			
			workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());		
			workFlowWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);		
			workFlowWrapper.setTransactionModel(new TransactionModel());		
			
			ProductModel productModel = new ProductModel();
			productModel.setProductId(productId);
			workFlowWrapper.setProductModel(productModel);
			
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
			ThreadLocalAppUser.setAppUserModel(appUserModel);
			switchWrapper.setBankId(50110L);
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

			switchWrapper = switchController.debitAccount(switchWrapper);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			throw new FrameworkCheckedException("Fund transfer at phoenix failed, unable to transfer payments.");
		}

		String responseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();

		if (StringUtils.isNotEmpty(responseCode) && responseCode.equals("00")) {
	 		logger.info("@@@@@@@@ Bulk Payment Schedular - saving settlement transaction entry for T24 FT: with Total Amount: " + amount);
	 		saveMiddlewareFTSettlementEntry(amount, ProductConstantsInterface.BULK_PAYMENT);

			logger.info("FT at Phoenix was successful.");		
		} else {
		
			logger.error("*** FUND TRANSFER AT RDV FAILED, CAN'T TRANSFER SALARIES ***");
			throw new FrameworkCheckedException("Fund transfer at RDV failed, unable to transfer.");
		}
		
		return responseCode;
 	} 	
 	
 	/**
 	 * @param bulkDisbursementsModels
 	 * @throws FrameworkCheckedException
 	 */
 	public void saveOrUpdateCollection(List<BulkDisbursementsModel> bulkDisbursementsModels) throws FrameworkCheckedException {

		try {
			
			bulkDisbursementHibernateDAO.saveOrUpdateCollection(bulkDisbursementsModels);
			
		} catch (DataAccessException e) {

			throw new FrameworkCheckedException(e.getLocalizedMessage());
		}
 	}

 	/**
	 * Transfer Salary of an individual account at OLA
	 * @param sourceAccount The StakeholderBankInfoModel
 	 * @param disbursement The BulkDisbursementsModel
	 */
	public void makeTransferFund(StakeholderBankInfoModel sourceAccount, BulkDisbursementsModel disbursement, String notificationMessage, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		try {
			ActionLogModel actionLogModel = new ActionLogModel();
			XStream xstream = new XStream();

			Long appUserId = disbursement.getCreatedByAppUserModel().getAppUserId();
			//disbursement.setRelationCreatedByAppUserModel(null);
			disbursement.setCreatedBy(appUserId);
			String xml = xstream.toXML("");
			actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml,
					XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogBeforeStart(actionLogModel);

			boolean status = this.transferFundOla(disbursement, sourceAccount, notificationMessage, workFlowWrapper);
			
			actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xml,
					XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogAfterEnd(actionLogModel);

			if (status) {
				disbursement.setSettled(Boolean.TRUE);
				disbursement.setSettledOn(new Date());
				bulkDisbursementHibernateDAO.saveOrUpdate(disbursement);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FrameworkCheckedException(e.getMessage(),e);
		}
	}

	private boolean transferFundOla(BulkDisbursementsModel model, StakeholderBankInfoModel stakeholderBankInfoModel, String notificationMessage, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		boolean transferred = false;
		
		TransactionDetailMasterModel txDetailMasterModel = new TransactionDetailMasterModel(true);
		txDetailMasterModel.setBankAccountNo(stakeholderBankInfoModel.getAccountNo());//set the source account number
		txDetailMasterModel.setBankAccountNoLastFive(StringUtil.getLastFiveDigitsFromAccountNo(stakeholderBankInfoModel.getAccountNo()));
		
		Double amount = model.getAmount();
		String MSISDN = model.getMobileNo();
		AppUserModel appUserModel = new AppUserModel();
		AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
		appUserTypeModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		appUserModel.setAppUserTypeIdAppUserTypeModel(appUserTypeModel);
		appUserModel.setMobileNo(MSISDN);
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		sBaseWrapper.setBasePersistableModel(appUserModel);
		sBaseWrapper = appUserManager.searchAppUserByMobile(sBaseWrapper);
		if (null != sBaseWrapper.getCustomList() && null != sBaseWrapper.getCustomList().getResultsetList()
				&& sBaseWrapper.getCustomList().getResultsetList().size() > 0) {
			appUserModel = (AppUserModel) sBaseWrapper.getCustomList().getResultsetList().get(0);
		}
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		baseWrapper = smartMoneyAccountManager.searchSmartMoneyAccount(baseWrapper);
		if (null != baseWrapper.getBasePersistableModel()) {
			smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
		}
		//workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
		workFlowWrapper.setAccountInfoModel(new AccountInfoModel());
		workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
		workFlowWrapper.setCustomerModel(smartMoneyAccountModel.getCustomerIdCustomerModel());
		workFlowWrapper.setTransactionDetailMasterModel(txDetailMasterModel);
		workFlowWrapper.setAppUserModel(appUserModel);
		// appUserModel.getUsername()// mfs id

		AppUserModel schedulerAppUserModel = new AppUserModel();
		schedulerAppUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
		ThreadLocalAppUser.setAppUserModel(schedulerAppUserModel);
		workFlowWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);

		ThreadLocalAppUser.setAppUserModel(appUserModel);
		
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTotalAmount(amount);
		transactionModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
		transactionModel.setTransactionCodeId(workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
		transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		transactionModel.setCustomerId(null);
		// transactionModel.setMfsId(appUserModel.getUsername());
		transactionModel.setTransactionTypeId(TransactionTypeConstantsInterface.BULK_DISBURSEMENT);
		// transactionModel.setCustomerMobileNo(MSISDN);
//		transactionModel.setBankResponseCode(switchWrapper.getResponseCode());
		transactionModel.setTotalAmount(amount);
		transactionModel.setTotalCommissionAmount(0.0);
		transactionModel.setTransactionAmount(amount);
		transactionModel.setNotificationMobileNo(MSISDN);
		transactionModel.setSaleMobileNo(null);

		transactionModel.setConfirmationMessage(notificationMessage);

		transactionModel.setIssue(false);
		transactionModel.setSupProcessingStatusId(1L);
		transactionModel.setDiscountAmount(0.0);

		transactionModel.setCreatedOn(new Date());
		transactionModel.setUpdatedOn(new Date());

		transactionModel.setCreatedBy(model.getCreatedByAppUserModel().getAppUserId());
		transactionModel.setUpdatedBy(model.getCreatedByAppUserModel().getAppUserId());
		transactionModel.setBankAccountNo(stakeholderBankInfoModel.getAccountNo());
		transactionModel.setProcessingSwitchId(6L);
		transactionModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);

		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
		transactionDetailModel.setTransactionId(transactionModel.getTransactionId());
		transactionDetailModel.setConsumerNo(MSISDN);
		transactionDetailModel.setActualBillableAmount(amount);
		transactionDetailModel.setSettled(true);
		transactionDetailModel.setProductId(ProductConstantsInterface.BULK_DISBURSEMENT);
		txDetailMasterModel.setProductId(ProductConstantsInterface.BULK_DISBURSEMENT);
		transactionDetailModel.setConsumerNo(MSISDN);
		transactionDetailModel.setActualBillableAmount(amount);
		transactionDetailModel.setSettled(true);
		transactionDetailModel.setCustomField1(smartMoneyAccountModel.getSmartMoneyAccountId().toString());
		transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);

		workFlowWrapper.setTransactionModel(transactionModel);
		workFlowWrapper.setTransactionDetailModel(transactionDetailModel);
		
		
//		OLAVO olavo = new OLAVO();
//		olavo.setIsBillPayment(true);
//		olavo.setReasonId(com.inov8.microbank.common.util.ReasonConstants.SALARY_DISBURSEMENT);
//		switchWrapper.setOlavo(olavo);
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		switchWrapper.setBasePersistableModel(smartMoneyAccountModel);
		switchWrapper.putObject(KEY_TX_AMOUNT, amount);		
		switchWrapper.setBankId(50050L);

		try
		{
			switchWrapper = olaVeriflyFinancialInstitution.makeBulkDisbursmentToCustomer(switchWrapper);
		}
		catch (Exception e)
		{
			throw new FrameworkCheckedException(e.getMessage(),e);
		}

		
		transactionDetailModel.setCustomField2(switchWrapper.getAccountInfoModel().getAccountNo());
		transactionDetailModel.setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId().toString());

		String responseCode = switchWrapper.getOlavo().getResponseCode();

		if (responseCode != null && responseCode.equals("00"))
		{
			transactionManager.saveTransaction(workFlowWrapper);
			
			saveTransactionDetailMaster(workFlowWrapper);
			
			saveFTSettlementEntry(workFlowWrapper);
			
			transferred = true;
		}
		else
		{
			transferred = false;
		}

		return transferred;
	}
	private void saveTransactionDetailMaster(WorkFlowWrapper workflowWrapper){
		TransactionDetailMasterModel txDetailMasterModel = workflowWrapper.getTransactionDetailMasterModel();
		TransactionModel trxModel = workflowWrapper.getTransactionModel();
		TransactionCodeModel txCodeModel = workflowWrapper.getTransactionCodeModel();
		TransactionDetailModel txDetailModel = workflowWrapper.getTransactionDetailModel();
		AppUserModel appUserModel = workflowWrapper.getAppUserModel();
		try {
			txDetailMasterModel.setRecipientMfsId(transactionDetailMasterManager.loadUserIdByMobileNo(appUserModel.getMobileNo()));
			txDetailMasterModel.setRecipientMobileNo(appUserModel.getMobileNo());
			txDetailMasterModel.setRecipientCnic(appUserModel.getNic());
			txDetailMasterModel.setConsumerNo(appUserModel.getMobileNo());
		} catch (Exception e) {
			logger.error("Exception occured whild loading UserID. Exception Message:" + e.getMessage());
			e.printStackTrace();
		}
		
		ProductModel productModel = new ProductModel();
		productModel.setPrimaryKey(ProductConstantsInterface.BULK_DISBURSEMENT);
		SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
		sbWrapper.setBasePersistableModel(productModel);
		try {
			sbWrapper = productManager.loadProduct(sbWrapper);
			productModel = (ProductModel)sbWrapper.getBasePersistableModel();
			txDetailMasterModel.setProductId(productModel.getProductId());
			txDetailMasterModel.setProductName(productModel.getName());
			txDetailMasterModel.setSupplierName(productModel.getSupplierIdSupplierModel().getName());
			txDetailMasterModel.setSupplierId(productModel.getSupplierId());
			
		} catch (FrameworkCheckedException fce) {
			fce.printStackTrace();
		}
		
		txDetailMasterModel.setTransactionId(trxModel.getTransactionId());
		txDetailMasterModel.setDeviceType(DeviceTypeConstantsInterface.DEVICE_TYPES_MAP.get(DeviceTypeConstantsInterface.BULK_DISBURSEMENT));
		txDetailMasterModel.setTransactionCodeId(txCodeModel.getTransactionCodeId());
		txDetailMasterModel.setTransactionCode(txCodeModel.getCode());
		txDetailMasterModel.setCreatedOn(txCodeModel.getCreatedOn());
		txDetailMasterModel.setUpdatedOn(txCodeModel.getUpdatedOn());
		txDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(workflowWrapper.getTransactionModel().getSupProcessingStatusId()));
		txDetailMasterModel.setPaymentModeId(trxModel.getPaymentModeId());
		txDetailMasterModel.setPaymentMode(PaymentModeConstantsInterface.paymentModeConstantsMap.get(trxModel.getPaymentModeId()));
		txDetailMasterModel.setTransactionAmount(trxModel.getTransactionAmount());
		txDetailMasterModel.setTotalAmount(trxModel.getTotalAmount());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(txDetailMasterModel);
		try{
			logger.info("Going to Save TransactionDetailMaster for salary disbursement of Mobile No: " + appUserModel.getMobileNo());
			transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(baseWrapper);
		}catch (Exception e) {
			logger.error("Exception occured while saving TransactionDetailMaster. Exception Message:" + e.getMessage());
			e.printStackTrace();
		}
	}
	private void actionLogBeforeStart(ActionLogModel actionLogModel) {

		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
		if (actionLogModel.getActionLogId() != null) {
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}

	private void actionLogAfterEnd(ActionLogModel actionLogModel) {
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
		actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
		insertActionLogRequiresNewTransaction(actionLogModel);
	}
	
	@Override
	public SearchBaseWrapper searchBulkDisbursements(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		BulkDisbursementsViewModel model = (BulkDisbursementsViewModel)wrapper.getBasePersistableModel();
	    CustomList<BulkDisbursementsViewModel> customList = null;
	    customList = this.bulkDisbursementViewHibernateDAO.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModelList() );
	    wrapper.setCustomList( customList );
		return wrapper;
	}
	
	@Override
	public SearchBaseWrapper searchBulkPayments(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		BulkPaymentViewModel model = (BulkPaymentViewModel) wrapper.getBasePersistableModel();
	    CustomList<BulkPaymentViewModel> customList = null;
	    customList = this.bulkPaymentViewHibernateDAO.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModelList() );
	    wrapper.setCustomList( customList );
		return wrapper;
	}

	@Override
	public SearchBaseWrapper loadBulkDisbursement(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		BulkDisbursementsModel model = (BulkDisbursementsModel)wrapper.getBasePersistableModel();
	    model = this.bulkDisbursementHibernateDAO.findByPrimaryKey(model.getPrimaryKey());
	    wrapper.setBasePersistableModel(model);
		return wrapper;
	}
	
	@Override
	public BaseWrapper updateBulkDisbursement(BaseWrapper wrapper) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(wrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		BulkDisbursementsModel model = (BulkDisbursementsModel)wrapper.getBasePersistableModel();
	    model = this.bulkDisbursementHibernateDAO.saveOrUpdate(model);
	    wrapper.setBasePersistableModel(model);
	    actionLogModel.setCustomField11(model.getCnic());
	    this.actionLogManager.completeActionLog(actionLogModel);
		return wrapper;
	}
	
	@Override
	public List<LabelValueBean> loadBankUsersList() throws FrameworkCheckedException {
		return bulkDisbursementHibernateDAO.loadBankUsersList();
	}
	
	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try {
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		} catch (Exception ex) {
			logger.error("Exception occurred while processing", ex);

		}
		return actionLogModel;
	}

	private WorkFlowWrapper creditOLABulkDisbursementSundryAccount(Double totalAmount, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException{
		try{
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
			ThreadLocalAppUser.setAppUserModel(appUserModel);
			
			ActionLogModel actionLogModel = new ActionLogModel();
			XStream xstream = new XStream();
			String xml = xstream.toXML("");
			actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogBeforeStart(actionLogModel);

			
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.putObject(KEY_TX_AMOUNT, totalAmount);		
			switchWrapper.setBankId(50050L);
			
			workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());		
			workFlowWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);
			
			ProductModel productModel = new ProductModel();
			productModel.setProductId(ProductConstantsInterface.BULK_DISBURSEMENT);
			workFlowWrapper.setProductModel(productModel);
			
			TransactionModel transactionModel = new TransactionModel();
			transactionModel.setTransactionAmount(totalAmount);
			
			workFlowWrapper.setTransactionModel(transactionModel);		
			
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			
			switchWrapper = olaVeriflyFinancialInstitution.creditBulkDisbursmentOLA(switchWrapper);
			
			actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogAfterEnd(actionLogModel);
			
			//Save Settlement Transaction Entry
			SettlementTransactionModel settlementModel = new SettlementTransactionModel();
//			settlementModel.setTransactionID(-1L); No transaction record saved for scheduler FT for Salary Disbursement
			settlementModel.setProductID(workFlowWrapper.getProductModel().getProductId());
			settlementModel.setCreatedBy(appUserModel.getAppUserId());
			settlementModel.setUpdatedBy(appUserModel.getAppUserId());
			settlementModel.setCreatedOn(new Date());
			settlementModel.setUpdatedOn(new Date());
			settlementModel.setStatus(0L);
			settlementModel.setFromBankInfoID(PoolAccountConstantsInterface.OF_SETTLEMENT_BULK_DISBURSEMENT_POOL_ACCOUNT);
			settlementModel.setToBankInfoID(PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_CORE);
			settlementModel.setAmount(totalAmount);
	
			this.settlementManager.saveSettlementTransactionModel(settlementModel);

			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FrameworkCheckedException(e.getMessage(),e);
		}
		
		return workFlowWrapper;
	}

	private void creditCoreBulkDisbursementSundryAccount(String sourceAccount, String recipientAccount, Double totalDisbursableAmount, WorkFlowWrapper wrapper) throws FrameworkCheckedException{
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		switchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setFromAccountNo(sourceAccount);

		switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountNo(recipientAccount);

		switchWrapper.setTransactionAmount(totalDisbursableAmount);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		try {
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
			ThreadLocalAppUser.setAppUserModel(appUserModel);
			
			ActionLogModel actionLogModel = new ActionLogModel();
			XStream xstream = new XStream();
			String xml = xstream.toXML("");
			actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogBeforeStart(actionLogModel);

//			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
//			ProductModel productModel = new ProductModel();
//			productModel.setProductId(productId);
//			workFlowWrapper.setProductModel(productModel);
//			
			switchWrapper.setWorkFlowWrapper(wrapper);
			switchWrapper.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
//			AppUserModel appUserModel = new AppUserModel();
//			appUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
//			ThreadLocalAppUser.setAppUserModel(appUserModel);
			switchWrapper.setBankId(50110L);
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
			
            MiddlewareMessageVO integrationMessageVO = new MiddlewareMessageVO();
            integrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            integrationMessageVO.setAccountNo1(switchWrapper.getFromAccountNo());
            integrationMessageVO.setAccountNo2(switchWrapper.getToAccountNo());
            integrationMessageVO.setTransactionAmount(String.valueOf(switchWrapper.getTransactionAmount()));
            switchWrapper.setMiddlewareIntegrationMessageVO(integrationMessageVO);

			switchWrapper = switchController.debitAccount(switchWrapper);
			
			actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogAfterEnd(actionLogModel);

		} catch (Exception e) {
				logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				logger.error("!!!!!  ERROR: Exception at middleware while FT for bulk disbursment.  !!!!!");
				logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new FrameworkCheckedException("Fund transfer at phoenix failed, unable to transfer salaries.");
		}
		
		String responseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();

		if (StringUtils.isNotEmpty(responseCode) && responseCode.equals("00")) {
			logger.info("FT at Phoenix was successful.");
		} else {
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("!!!!!  ERROR: FT at middleware Failed. Response Code:" + responseCode + "  !!!!!");
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
			logger.error("*** FUND TRANSFER AT PHOENIX FAILED, CAN'T TRANSFER SALARIES ***");
			throw new FrameworkCheckedException("Fund transfer at phoenix failed, unable to transfer salaries.");
		}

	}
	
	private void saveFTSettlementEntry(WorkFlowWrapper wrapper) throws FrameworkCheckedException{
		
		try{
		
			// For A/C type based Customer Pool Account
			Long poolAccountBankInfoId = settlementManager.getStakeholderBankInfoId(wrapper.getAppUserModel().getCustomerIdCustomerModel().getCustomerAccountTypeId());
		
			SettlementTransactionModel settlementModel = new SettlementTransactionModel();
			settlementModel.setTransactionID(wrapper.getTransactionModel().getTransactionId());
			settlementModel.setProductID(wrapper.getTransactionDetailModel().getProductId());
			settlementModel.setCreatedBy(wrapper.getTransactionModel().getCreatedBy());
			settlementModel.setUpdatedBy(wrapper.getTransactionModel().getCreatedBy());
			settlementModel.setCreatedOn(new Date());
			settlementModel.setUpdatedOn(new Date());
			settlementModel.setStatus(0L);
			settlementModel.setFromBankInfoID(PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_CORE);
			settlementModel.setToBankInfoID(poolAccountBankInfoId);
			settlementModel.setAmount(wrapper.getTransactionModel().getTransactionAmount());
	
			this.settlementManager.saveSettlementTransactionModel(settlementModel);
			
			
			settlementModel = new SettlementTransactionModel();
			settlementModel.setTransactionID(wrapper.getTransactionModel().getTransactionId());
			settlementModel.setProductID(wrapper.getTransactionDetailModel().getProductId());
			settlementModel.setCreatedBy(wrapper.getTransactionModel().getCreatedBy());
			settlementModel.setUpdatedBy(wrapper.getTransactionModel().getCreatedBy());
			settlementModel.setCreatedOn(new Date());
			settlementModel.setUpdatedOn(new Date());
			settlementModel.setStatus(0L);
			settlementModel.setFromBankInfoID(PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID);
			settlementModel.setToBankInfoID(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
			settlementModel.setAmount(wrapper.getTransactionModel().getTransactionAmount());
			
			this.settlementManager.saveSettlementTransactionModel(settlementModel);
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new FrameworkCheckedException(e.getMessage());
		}		
	}

	private void saveMiddlewareFTSettlementEntry(Double totalAmount, Long productId) throws FrameworkCheckedException{
		
		try{
			SettlementTransactionModel settlementModel = new SettlementTransactionModel();
			settlementModel.setProductID(productId);
			settlementModel.setCreatedBy(SCHEDULER_APP_USER_ID);
			settlementModel.setUpdatedBy(SCHEDULER_APP_USER_ID);
			settlementModel.setCreatedOn(new Date());
			settlementModel.setUpdatedOn(new Date());
			settlementModel.setStatus(0L);
			settlementModel.setFromBankInfoID(PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID);
			settlementModel.setToBankInfoID(PoolAccountConstantsInterface.OF_SETTLEMENT_BULK_DISBURSEMENT_POOL_ACCOUNT);
			settlementModel.setAmount(totalAmount);
	
			this.settlementManager.saveSettlementTransactionModel(settlementModel);
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new FrameworkCheckedException(e.getMessage());
		}		
	}

	public static void setLogger(Logger logger) {
		BulkDisbursementsManagerImpl.logger = logger;
	}

	public void setBulkDisbursementHibernateDAO(
			BulkDisbursementDAO bulkDisbursementHibernateDAO) {
		this.bulkDisbursementHibernateDAO = bulkDisbursementHibernateDAO;
	}

	public void setBulkDisbursementViewHibernateDAO(BulkDisbursementViewDAO bulkDisbursementViewHibernateDAO) {
		this.bulkDisbursementViewHibernateDAO = bulkDisbursementViewHibernateDAO;
	}

	public void setBulkPaymentViewHibernateDAO(BulkPaymentViewDAO bulkPaymentViewHibernateDAO) {
		this.bulkPaymentViewHibernateDAO = bulkPaymentViewHibernateDAO;
	}

	public void setOlaVeriflyFinancialInstitution(
			AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setTransactionManager(TransactionModuleManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}
	public TransactionDetailMasterManager getTransactionDetailMasterManager() {
		return transactionDetailMasterManager;
	}

	public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}

	public void setXmlMarshaller(XmlMarshaller<BulkDisbursementsXmlVo> xmlMarshaller) {
		this.xmlMarshaller = xmlMarshaller;
	}
	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}
}

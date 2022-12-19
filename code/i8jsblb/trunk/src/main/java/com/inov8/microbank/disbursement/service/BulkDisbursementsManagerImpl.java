package com.inov8.microbank.disbursement.service;

import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_TX_AMOUNT;
import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.jms.DestinationConstants;
//import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkDisbursementFileInfoRefDataModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.mfs.jme.model.Message;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.enums.ResponseCodeEnum;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.BulkDisbursementsViewModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.BankIMDEnum;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierConstants;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.util.ThreadGenerator;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.disbursement.model.BulkPaymentViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.disbursement.dao.*;
import com.inov8.microbank.disbursement.model.*;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsVOModel;
import com.inov8.microbank.disbursement.vo.DisbursementVO;
import com.inov8.microbank.disbursement.vo.DisbursementWrapper;
import com.inov8.microbank.server.dao.portal.bbaccountsview.BBAccountsViewDao;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.server.service.workflow.controller.WorkFlowController;
import com.inov8.microbank.tax.service.TaxManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;
import com.thoughtworks.xstream.XStream;
import org.springframework.web.context.ContextLoader;

public class BulkDisbursementsManagerImpl implements BulkDisbursementsManager, ApplicationContextAware {

    private static Logger logger = Logger.getLogger(BulkDisbursementsManagerImpl.class);

    private DisbursementFileInfoViewDAO disbursementFileInfoViewDAO;
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
    protected SettlementManager settlementManager;
    protected AbstractFinancialInstitution phoenixFinancialInstitution;
    private BulkDisbursementsFileInfoDAO bulkDisbursementsFileInfoDAO;
    private StakeholderBankInfoManager stakeholderBankInfoManager;
    private WorkFlowController workFlowController;
    private CommissionManager commissionManager;
    private static ApplicationContext applicationContext = null;
    private TaxManager taxManager;
    protected EncryptionHandler encryptionHandler;
    private AccountInfoDAO accountInfoDao;
    private Map<String, Integer> bulkPaymentThreadConfigMap;
    private Map<String, Integer> bulkDisbursementThreadConfigMap;
//    private DayendManager dayendManager;
    private BBAccountsViewDao bBAccountsViewDao;
    private AdvanceSalaryLoanDAO advanceSalaryLoanDAO;

    @Override
    public BulkDisbursementsModel saveOrUpdateBulkDisbursement(BulkDisbursementsModel bulkDisbursementsModel) throws FrameworkCheckedException {
        return bulkDisbursementHibernateDAO.saveOrUpdate(bulkDisbursementsModel);
    }

    /**
     * @param //disbursementType
     * @param //end
     * @throws FrameworkCheckedException
     */
    @Override
    public SwitchWrapper performBLBSumFT(AccountInfoModel sourceAccountInfoModel, StakeholderBankInfoModel productBLBAccount,
                                         DisbursementWrapper disbursementWrapper, WorkFlowWrapper workFlowWrapper) throws Exception {
    	
    	ProductModel productModel = workFlowWrapper.getProductModel();

    	ThreadLocalAppUser.setAppUserModel(new AppUserModel(SCHEDULER_APP_USER_ID));

        if (null != sourceAccountInfoModel.getCustomerMobileNo()) {
            workFlowWrapper = validateSourceAccount(sourceAccountInfoModel);
        }

        Double transactionAmount = CommonUtils.formatAmountTwoDecimal(disbursementWrapper.getAmount());
        Double charges = CommonUtils.formatAmountTwoDecimal(disbursementWrapper.getCharges());

        // source a/c validation
        CommissionAmountsHolder commissionAmountsHolder = new CommissionAmountsHolder();
        commissionAmountsHolder.setTransactionAmount(transactionAmount);
        commissionAmountsHolder.setTransactionProcessingAmount(charges);

//        workFlowWrapper.setBusinessDate(dayendManager.getBusinessDate());
        workFlowWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);
        workFlowWrapper.setTransactionModel(new TransactionModel());
        workFlowWrapper.setProductModel(productModel);
        workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
        workFlowWrapper.putObject("OLA_SUM_FT", Boolean.TRUE);
        workFlowWrapper.setAccountInfoModel(sourceAccountInfoModel);

        workFlowWrapper.setToSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.putObject(KEY_TX_AMOUNT, transactionAmount);
        switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
        switchWrapper.setFromAccountNo(sourceAccountInfoModel.getAccountNo());
        switchWrapper.setToAccountNo(productBLBAccount.getAccountNo());
        switchWrapper.setAccountInfoModel(sourceAccountInfoModel);
        switchWrapper.setWorkFlowWrapper(workFlowWrapper);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        switchWrapper.setStakeholderBankInfoModel(productBLBAccount);

        switchWrapper = olaVeriflyFinancialInstitution.bulkDisbursementFromL2SumFT(switchWrapper);
		
        switchWrapper.setResponseCode(switchWrapper.getOlavo().getResponseCode());
        if (switchWrapper.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            switchWrapper.setTransactionAmount(transactionAmount + charges);
            saveTransactionData(switchWrapper);

            settlementManager.prepareDataForDayEndSettlement(workFlowWrapper);
//            performDayEndSettlement(switchWrapper);
        }

        return switchWrapper;
    }

    public AccountInfoModel loadAccountInfoModel(AccountInfoModel accountInfoModel) throws Exception {

        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setEnableLike(Boolean.FALSE);
        exampleHolder.setMatchMode(MatchMode.EXACT);

        CustomList<AccountInfoModel> customList = this.accountInfoDao.findByExample(accountInfoModel, null, null, exampleHolder);

        if (customList != null && customList.getResultsetList().isEmpty()) {
            throw new FrameworkCheckedException("BLB Account Not found.");
        }

        accountInfoModel = customList.getResultsetList().get(0);

        AccountInfoModel _accountInfoModel = (AccountInfoModel) accountInfoModel.clone();

        String decryptAccountNumber = this.encryptionHandler.decrypt(_accountInfoModel.getAccountNo());
        _accountInfoModel.setAccountNo(decryptAccountNumber);

        return _accountInfoModel;
    }

    private WorkFlowWrapper validateSourceAccount(AccountInfoModel accountInfoModel) throws Exception {
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setMobileNo(accountInfoModel.getCustomerMobileNo());
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        appUserModel = smartMoneyAccountManager.getAppUserModel(appUserModel);

        if (appUserModel == null) {
            throw new FrameworkCheckedException("No profile is available of source a/c.");
        }

        if (appUserModel.getAccountClosedSettled() || appUserModel.getAccountClosedUnsettled()) {
            throw new FrameworkCheckedException("Source a/c is closed");
        }

//        if (RegistrationStateConstants.BLACK_LISTED == appUserModel.getRegistrationStateId()) {
//            throw new FrameworkCheckedException("Source a/c is marked black listed");
//        }

        CustomerModel customerModel = appUserManager.getCustomerModelByPK(appUserModel.getCustomerId());
        if (customerModel != null) {
            if (!customerModel.getRegister()) {
                throw new FrameworkCheckedException("Source a/c profile is in-active.");
            }

            Long accountTypeId = customerModel.getCustomerAccountTypeId();
            if (CustomerAccountTypeConstants.LEVEL_2_CORPORATE != accountTypeId) {
                throw new FrameworkCheckedException("Source a/c type is changed. Only Level 2 A/c is allowed.");
            }
        }

        workFlowWrapper.setCustomerModel(customerModel);
        workFlowWrapper.setFromSegmentId(customerModel.getSegmentId());
        workFlowWrapper.putObject("SENDER_CNIC", appUserModel.getNic());

        return workFlowWrapper;
    }

    /**
     * @param switchWrapper
     * @param //bulkDisbursementsModelList
     */
    @Override
    public void saveTransactionData(SwitchWrapper switchWrapper) {

        ProductModel productModel = switchWrapper.getWorkFlowWrapper().getProductModel();

        try {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(productModel);
            this.productManager.loadProduct(baseWrapper);
            productModel = (ProductModel) baseWrapper.getBasePersistableModel();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return;
        }

        TransactionModel transactionModel = switchWrapper.getWorkFlowWrapper().getTransactionModel();

        transactionModel.setTransactionCodeIdTransactionCodeModel(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel());
        transactionModel.setConfirmationMessage(" _ ");
        transactionModel.setTransactionAmount(switchWrapper.getTransactionAmount());
        transactionModel.setTotalAmount(switchWrapper.getTransactionAmount());
        transactionModel.setTotalCommissionAmount(0d);
        transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
        transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        transactionModel.setTransactionTypeId(TransactionTypeConstantsInterface.BULK_DISBURSEMENT);
        transactionModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
        transactionModel.setProcessingBankId(switchWrapper.getBankId());
        transactionModel.setDiscountAmount(0d);
        transactionModel.setNotificationMobileNo("N/A");
        transactionModel.setCreatedBy(2L);
        transactionModel.setUpdatedBy(2L);
        transactionModel.setCreatedOn(new Date());
        transactionModel.setUpdatedOn(new Date());
//        transactionModel.setBusinessDate(switchWrapper.getWorkFlowWrapper().getBusinessDate());
        TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
        transactionDetailModel.setSettled(true);
        transactionDetailModel.setProductId(productModel.getProductId());
        transactionDetailModel.setTransactionIdTransactionModel(transactionModel);
        transactionDetailModel.setActualBillableAmount(transactionModel.getTransactionAmount());

        transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);

        TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel(true);
        transactionDetailMasterModel.setTransactionCode(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
        transactionDetailMasterModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
        transactionDetailMasterModel.setProductId(productModel.getProductId());
        if(switchWrapper.getWorkFlowWrapper().getSwitchWrapper()!=null) {
            transactionDetailMasterModel.setStan(switchWrapper.getWorkFlowWrapper().getSwitchWrapper().getMiddlewareIntegrationMessageVO().getStan());
        }
        transactionDetailMasterModel.setProductName(productModel.getName());
        transactionDetailMasterModel.setSupplierId(productModel.getSupplierId());
        transactionDetailMasterModel.setProductName(productModel.getName());
        transactionDetailMasterModel.setProductCode(productModel.getProductCode());
        transactionDetailMasterModel.setBillType(productModel.getBillType());
        transactionDetailMasterModel.setSupplierId(productModel.getSupplierId());
        transactionDetailMasterModel.setSupplierName(productModel.getSupplierIdSupplierModel().getName());
        transactionDetailMasterModel.setDeviceType(DeviceTypeConstantsInterface.DEVICE_TYPES_MAP.get(DeviceTypeConstantsInterface.BULK_DISBURSEMENT));
        transactionDetailMasterModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        transactionDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(transactionModel.getSupProcessingStatusId()));
        transactionDetailMasterModel.setTransactionAmount(transactionModel.getTransactionAmount());
        transactionDetailMasterModel.setTotalAmount(transactionModel.getTotalAmount());
        transactionDetailMasterModel.setCreatedOn(new Date());
        transactionDetailMasterModel.setUpdatedOn(new Date());
//        transactionDetailMasterModel.setBusinessDate(switchWrapper.getWorkFlowWrapper().getBusinessDate());

        String senderCNIC = (String) switchWrapper.getWorkFlowWrapper().getObject("SENDER_CNIC");
        if (!StringUtil.isNullOrEmpty(senderCNIC))
            transactionDetailMasterModel.setSenderCnic(senderCNIC);

        try {

            BaseWrapper wrapper = new BaseWrapperImpl();
            wrapper.setBasePersistableModel(transactionModel);

            this.transactionManager.saveTransactionModel(wrapper);

            transactionModel = (TransactionModel) wrapper.getBasePersistableModel();
            switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);

            transactionDetailMasterModel.setTransactionId(transactionModel.getTransactionId());
            transactionDetailMasterModel.setTransactionDetailId(transactionDetailModel.getTransactionDetailId());

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(transactionDetailMasterModel);

            transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(baseWrapper);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    private SwitchWrapper postSumFTAtMiddleware(String sourceAccountNumber,
    											String bulkSundryCoreAccount,
    											Double amount, Double charges,
    											WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

        logger.info("### POST CORE FUND TRANSFER, PRODUCT : " + workFlowWrapper.getProductModel().getName() +
                " SOURCE CORE A/C # " + sourceAccountNumber + " DESTINATION CORE A/C # " + bulkSundryCoreAccount +
                " AMOUNT " + amount + " CHARGES " + charges);
        
        Double totalAmount = CommonUtils.formatAmountTwoDecimal(amount + charges);
        
        SwitchWrapper switchWrapper = performCoreFT(sourceAccountNumber, bulkSundryCoreAccount, totalAmount, workFlowWrapper);
        
        return switchWrapper;
    }

    private SwitchWrapper rollbackSumFTAtMiddleware(String sourceAccountNumber, String targetAccountNumber, Double amount,
                                                   Double charges, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

        logger.info("### CORE FUND TRANSFER REVERSAL, PRODUCT : " + workFlowWrapper.getProductModel().getName() +
                " SOURCE CORE A/C # " + sourceAccountNumber + " DESTINATION CORE A/C # " + targetAccountNumber +
                " AMOUNT " + amount + " CHARGES " + charges);

        Double totalAmount = CommonUtils.formatAmountTwoDecimal(amount + charges);
        
        SwitchWrapper switchWrapper = performCoreFT(sourceAccountNumber, targetAccountNumber, totalAmount, workFlowWrapper);
        
        return switchWrapper;
    }

    protected SwitchWrapper performCoreFT(String sourceAccountNumber, String targetAccountNumber, Double amount, Double charges, WorkFlowWrapper workFlowWrapper) {
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();

        try {
            ActionLogModel actionLogModel = new ActionLogModel();

            XStream xstream = new XStream();
            String xml = xstream.toXML("");
            actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
            actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
            this.actionLogBeforeStart(actionLogModel);

            MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
            middlewareMessageVO.setTransactionAmount(CommonUtils.formatAmountTwoDecimal(amount + charges).toString());
            middlewareMessageVO.setAccountNo1(sourceAccountNumber);
            middlewareMessageVO.setAccountNo2(targetAccountNumber);
            middlewareMessageVO.setBankIMD(BankIMDEnum.JS_BANK.getId().toString());
//            middlewareMessageVO.setFromAccountIMD(BankIMDEnum.JS_BANK.getId().toString());
//            middlewareMessageVO.setToAccountIMD(BankIMDEnum.JS_BANK.getId().toString());

            switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

            switchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
            switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
            switchWrapper.setFromAccountNo(sourceAccountNumber);

            switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
            switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
            switchWrapper.setToAccountNo(targetAccountNumber);

            switchWrapper.setTransactionAmount(amount + charges);
            switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

            ThreadLocalAppUser.setAppUserModel(new AppUserModel(SCHEDULER_APP_USER_ID));

            transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);
            workFlowWrapper.setTransactionModel(new TransactionModel());

            switchWrapper.setWorkFlowWrapper(workFlowWrapper);
            switchWrapper.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
            switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

            switchWrapper = phoenixFinancialInstitution.debitCreditAccount(switchWrapper);
        } catch (Exception e) {
            logger.error("performCoreFT failed" + e.getMessage());

            throw (WorkFlowException) e;
        }

        String responseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();
        switchWrapper.setResponseCode(responseCode);

        if (StringUtils.isNotEmpty(responseCode) && responseCode.equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
            logger.info("### performCoreFT with Total Amount + Charges : " + (amount + charges));

        } else {
            throw new WorkFlowException("Fund transfer at MIDDLEWARE failed");
        }
        return switchWrapper;
    }
*/
    public void makeAcHolderTransferFund(DisbursementVO disbursementVO, WorkFlowWrapper workFlowWrapper) throws Exception {
        try {

            ActionLogModel actionLogModel = new ActionLogModel();
            XStream xstream = new XStream();

            String xml = xstream.toXML("");
            actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
            actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
            this.actionLogBeforeStart(actionLogModel);

//                AdvanceSalaryLoanModel advanceSalaryLoanModel = new AdvanceSalaryLoanModel();
//                advanceSalaryLoanModel.setMobileNo(disbursementVO.getMobileNo());
//                advanceSalaryLoanModel.setIsCompleted(false);
//                CustomList<AdvanceSalaryLoanModel> list = getCommonCommandManager().getAdvanceSalaryLoanDAO().findByExample(advanceSalaryLoanModel);
//
//                if(list != null && list.getResultsetList().size() > 0){
//                    Double loanFreeAmount = disbursementVO.getAmount() - list.getResultsetList().get(0).getInstallmentAmount();
//    //                disbursementVO.setAmount(loanFreeAmount);
//                    workFlowWrapper.setLoanAmount(Double.valueOf(list.getResultsetList().get(0).getInstallmentAmount()));
//                }

            boolean status = this.acHolderBLBTransfer(disbursementVO, workFlowWrapper);

            actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));
            this.actionLogAfterEnd(actionLogModel);

            if (status) {
//                if(list != null && list.getResultsetList().size() > 0) {
//                    if (workFlowWrapper.getLoanAmount() != null) {
//                        if (workFlowWrapper.getLoanAmount() > 0) {
//                            if (list.getResultsetList().get(0).getNoOfInstallment().equals(list.getResultsetList().get(0).getNoOfInstallmentPaid())) {
//                                advanceSalaryLoanModel.setIsCompleted(true);
//                            } else {
//                                advanceSalaryLoanModel.setIsCompleted(false);
//                                advanceSalaryLoanModel.setNoOfInstallmentPaid(list.getResultsetList().get(0).getNoOfInstallmentPaid() + 1);
//                            }
//                        }
//                    }
//                    if (advanceSalaryLoanModel.getNoOfInstallmentPaid().equals(list.getResultsetList().get(0).getNoOfInstallmentPaid() + 1)) {
//                        advanceSalaryLoanModel.setIsCompleted(true);
//                    }
//
//                    advanceSalaryLoanModel.setLastPaymentDate(new Date());
//                    advanceSalaryLoanModel.setUpdatedOn(new Date());
//                    advanceSalaryLoanDAO.update(advanceSalaryLoanModel.getNoOfInstallmentPaid(),
//                            advanceSalaryLoanModel.getIsCompleted(), list.getResultsetList().get(0).getAdvaceSalaryLoanId());
//                }
                bulkDisbursementHibernateDAO.update(disbursementVO.getDisbursementId(), workFlowWrapper.getTransactionCodeModel().getCode());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());

            throw e;
        }
    }

    private boolean acHolderBLBTransfer(DisbursementVO disbursementVO, WorkFlowWrapper workFlowWrapper) throws Exception {

        Boolean transferred = false;
        Date now = new Date();
        Double amount = disbursementVO.getAmount();
        String MSISDN = disbursementVO.getMobileNo();
        Long appUserTypeId = disbursementVO.getAppUserTypeId();
        AppUserModel appUserModel = appUserManager.loadAppUserByQuery(MSISDN, appUserTypeId);
        if (appUserModel == null || appUserModel.getAccountClosedUnsettled() || appUserModel.getAccountClosedSettled()) {
            throw new FrameworkCheckedException("Account is closed. Mobile : " + MSISDN);
        }

        SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel);

        logger.info("PaymentModeId is:" +smartMoneyAccountModel.getPaymentModeId());
        CustomerModel customerModel = null;
        RetailerContactModel retailerContactModel = null;
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setPaymentModeId(com.inov8.verifly.common.constants.PaymentModeConstants.BANK_ACCOUNT);

        if (UserTypeConstantsInterface.CUSTOMER.longValue() == appUserTypeId) {
            customerModel = smartMoneyAccountModel.getCustomerIdCustomerModel();
            accountInfoModel.setCustomerId(customerModel.getCustomerId());
        } else if (UserTypeConstantsInterface.RETAILER.longValue() == appUserTypeId) {
            retailerContactModel = (RetailerContactModel) smartMoneyAccountModel.getRetailerContactIdRetailerContactModel().clone();
            accountInfoModel.setCustomerId(appUserModel.getAppUserId());
        }

        accountInfoModel = loadAccountInfoModel(accountInfoModel);

        workFlowWrapper.setAccountInfoModel(accountInfoModel);
        workFlowWrapper.setCustomerModel(customerModel);
        workFlowWrapper.setRetailerContactModel(retailerContactModel);
        workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
        workFlowWrapper.setAppUserModel(appUserModel);
//        workFlowWrapper.setCheckIfLimitApplicable(disbursementVO.getLimitApplicable());

        ThreadLocalAppUser.setAppUserModel(new AppUserModel(SCHEDULER_APP_USER_ID));

        workFlowWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);

        TransactionModel transactionModel = new TransactionModel();
//        transactionModel.setBusinessDate(workFlowWrapper.getBusinessDate());
        transactionModel.setTotalAmount(amount);
        transactionModel.setFromRetContactId(appUserModel.getRetailerContactId());
        transactionModel.setFromRetContactMobNo(appUserModel.getMobileNo());
        transactionModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
        transactionModel.setTransactionTypeId(TransactionTypeConstantsInterface.BULK_DISBURSEMENT);
        transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        transactionModel.setTransactionCodeId(workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
        transactionModel.setCustomerId(appUserModel.getCustomerId());
        transactionModel.setConfirmationMessage("");
        transactionModel.setTotalAmount(amount);
        transactionModel.setTotalCommissionAmount(0.0);
        transactionModel.setTransactionAmount(amount);
        transactionModel.setNotificationMobileNo(MSISDN);
        transactionModel.setSaleMobileNo(null);
//        transactionModel.setMfsId(appUserModel.getMfsId());
        transactionModel.setIssue(false);
        transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
        transactionModel.setDiscountAmount(0.0);
        transactionModel.setCreatedOn(now);
        transactionModel.setUpdatedOn(now);

        Long userId = workFlowWrapper.getAppUserModel().getAppUserId();

        StakeholderBankInfoModel productStakeholderBankInfoModel = (StakeholderBankInfoModel) workFlowWrapper.getObject(StakeholderBankInfoModel.ACCOUNT_TYPE_BLB);
        transactionModel.setCreatedBy(SCHEDULER_APP_USER_ID);
        transactionModel.setUpdatedBy(SCHEDULER_APP_USER_ID);
        transactionModel.setBankAccountNo(productStakeholderBankInfoModel.getAccountNo());
        transactionModel.setProcessingSwitchId(SwitchConstants.OLA_SWITCH);
        transactionModel.setProcessingBankId(BankConstantsInterface.OLA_BANK_ID);

        if (customerModel != null) {
            transactionModel.setCustomerId(customerModel.getCustomerId());
        } else if (retailerContactModel != null) {
            transactionModel.setToRetContactIdRetailerContactModel(retailerContactModel);
            transactionModel.setToRetailerId(retailerContactModel.getRetailerId());
        }

        TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
        transactionDetailModel.setTransactionId(transactionModel.getTransactionId());
        transactionDetailModel.setConsumerNo(MSISDN);
        transactionDetailModel.setActualBillableAmount(amount);
        transactionDetailModel.setSettled(true);
        transactionDetailModel.setProductId(disbursementVO.getProductId());

        transactionDetailModel.setConsumerNo(MSISDN);
        transactionDetailModel.setActualBillableAmount(amount);
        transactionDetailModel.setSettled(true);
        transactionDetailModel.setCustomField1(smartMoneyAccountModel.getSmartMoneyAccountId().toString());
        transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);

        TransactionDetailMasterModel txDetailMasterModel = new TransactionDetailMasterModel(true);
        long productId = Long.parseLong(MessageUtil.getMessage("SalaryDisbursementProductId"));

        if (workFlowWrapper.getProductModel().getProductId().equals(productId)){
            StakeholderBankInfoModel bankInfoModel = new StakeholderBankInfoModel();
            bankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA);
            SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(bankInfoModel);
            bankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

            txDetailMasterModel.setBankAccountNo(bankInfoModel.getAccountNo());//set the source account number

        }else{
            txDetailMasterModel.setBankAccountNo(productStakeholderBankInfoModel.getAccountNo());//set the source account number

        }
        txDetailMasterModel.setBankAccountNoLastFive(StringUtil.getLastFiveDigitsFromAccountNo(productStakeholderBankInfoModel.getAccountNo()));
        txDetailMasterModel.setProductId(disbursementVO.getProductId());

        ProductModel productModel = workFlowWrapper.getProductModel();
        txDetailMasterModel.setProductName(productModel.getName());
        txDetailMasterModel.setServiceId(productModel.getServiceId());
        txDetailMasterModel.setServiceName(productModel.getServiceIdServiceModel().getName());
        txDetailMasterModel.setSupplierName(SupplierConstants.BRANCHLESS_BANKING_SUPPLIER_NAME);
        txDetailMasterModel.setSupplierId(SupplierConstants.BRANCHLESS_BANKING_SUPPLIER);

        txDetailMasterModel.setRecipientAccountNo(accountInfoModel.getAccountNo());
        txDetailMasterModel.setRecipientAccountNick(accountInfoModel.getAccountNick());
        txDetailMasterModel.setBankId(BankConstantsInterface.OLA_BANK_ID);
        txDetailMasterModel.setBankName("JS Bank Limited");
        txDetailMasterModel.setSenderDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
//        txDetailMasterModel.setBusinessDate(workFlowWrapper.getBusinessDate());
//        TaxRegimeModel taxRegimeModel = workFlowWrapper.getTaxRegimeModel();
//        if (taxRegimeModel == null) {
//            taxRegimeModel = new TaxRegimeModel();
//            taxRegimeModel.setTaxRegimeId(TaxRegimeConstants.FEDERAL);
//            taxRegimeModel.setName("Faderal");
//        }
//
//        txDetailMasterModel.setTaxRegime1Id(workFlowWrapper.getTaxRegimeModel() != null ? workFlowWrapper.getTaxRegimeModel().getTaxRegimeId() : null);

        String description = disbursementVO.getDescription();
        if (StringUtil.isNullOrEmpty(description))
            description = "";
        else
            description = "by " + description;

        String notification = MessageUtil.getMessage("DISBURSEMENT.NOTIFICATION",
                new Object[]{disbursementVO.getMobileNo(), Formatter.formatDouble(disbursementVO.getAmount()), description});

        workFlowWrapper.setTransactionDetailMasterModel(txDetailMasterModel);

        transactionModel.setConfirmationMessage(notification);
        
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(transactionModel);
        this.transactionManager.saveTransactionModel(baseWrapper);

        workFlowWrapper.setTransactionModel(transactionModel);
        workFlowWrapper.setTransactionDetailModel(transactionDetailModel);

        StakeholderBankInfoModel blbSundryStakeholderBankInfoModel = (StakeholderBankInfoModel) workFlowWrapper.getObject("SUNDRY_ACCOUNT");

        workFlowWrapper.putObject("SUNDRY_ACCOUNT", blbSundryStakeholderBankInfoModel);
        workFlowWrapper.putObject("PRODUCT_ACCOUNT", productStakeholderBankInfoModel);

        txDetailMasterModel.setTransactionId(transactionModel.getTransactionId());
        txDetailMasterModel.setTransactionDetailId(transactionDetailModel.getTransactionDetailId());

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(workFlowWrapper);
        switchWrapper.setBasePersistableModel(smartMoneyAccountModel);
        switchWrapper.putObject(KEY_TX_AMOUNT, amount);
        switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);

        try {
//            Long taxRegimeId = workFlowWrapper.getTaxRegimeModel().getTaxRegimeId();
            CommissionWrapper commissionWrapper = this.calculateCommission(workFlowWrapper.getProductModel(), amount, null, Boolean.TRUE, null, Boolean.TRUE,3l);
            CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
            commissionAmountsHolder.setTransactionAmount(disbursementVO.getAmount());

            workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);

//            String userDeviceId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
//            UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
//            uda.setAppUserId(appUserModel.getAppUserId());
//            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(String.valueOf(appUserModel.getUsername()));
//            String userDeviceId = uda.getUserId();
//            // Velocity validation - start
//            BaseWrapper bWrapper = new BaseWrapperImpl();
//            bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, -1L);
//            bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
//            bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, -1L);
//            bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, -1L);
//            bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, workFlowWrapper.getTransactionModel().getTransactionAmount());
//            bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
//            bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, customerModel.getCustomerAccountTypeId());
//            bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userDeviceId);
////					bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
//            this.getCommonCommandManager().checkVelocityCondition(bWrapper);

            switchWrapper = olaVeriflyFinancialInstitution.makeBulkDisbursmentToCustomer(switchWrapper);
            
            this.settlementManager.settleCommission(commissionWrapper, workFlowWrapper);

            settlementManager.prepareDataForDayEndSettlement(switchWrapper.getWorkFlowWrapper());

            transactionDetailModel.setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId().toString());
            txDetailMasterModel.setRecipientAccountNo(switchWrapper.getToAccountNo());
            workFlowWrapper.setTransactionDetailMasterModel(txDetailMasterModel);
            workFlowWrapper.getTransactionModel().setTotalAmount(commissionAmountsHolder.getTotalAmount());
            workFlowWrapper.getTransactionDetailMasterModel().setTotalAmount(commissionAmountsHolder.getTotalAmount());
//            transactionDetailMasterManager.saveTransactionDetailMaster(txDetailMasterModel);

            String responseCode = switchWrapper.getOlavo().getResponseCode();
            if (responseCode != null && responseCode.equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {
                transactionManager.saveTransaction(workFlowWrapper);
                saveTransactionDetailMaster(workFlowWrapper);
                transferred = true;
            }
        } catch (Exception e) {
            logger.error("exception Occurred : " + e.getMessage());

            throw e;
        }

        return transferred;
    }

    private void saveTransactionDetailMaster(WorkFlowWrapper workflowWrapper) {

        TransactionDetailMasterModel txDetailMasterModel = workflowWrapper.getTransactionDetailMasterModel();
        TransactionModel trxModel = workflowWrapper.getTransactionModel();
        TransactionCodeModel txCodeModel = workflowWrapper.getTransactionCodeModel();
        TransactionDetailModel txDetailModel = workflowWrapper.getTransactionDetailModel();
        AppUserModel appUserModel = workflowWrapper.getAppUserModel();

        try {
//            txDetailMasterModel.setRecipientMfsId(appUserModel.getMfsId());
            txDetailMasterModel.setRecipientMobileNo(appUserModel.getMobileNo());
            txDetailMasterModel.setRecipientCnic(appUserModel.getNic());
            txDetailMasterModel.setConsumerNo(appUserModel.getMobileNo());
            txDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
            txDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.COMPLETE_NAME);

            txDetailMasterModel.setRecipientAccountNo(workflowWrapper.getTransactionDetailMasterModel().getRecipientAccountNo());
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
            txDetailMasterModel.setSegmentId(workflowWrapper.getCustomerModel().getSegmentId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(txDetailMasterModel);
            transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(baseWrapper);
        } catch (Exception e) {
            logger.error("Exception occurred while saving TransactionDetailMaster. Exception Message:" + e.getMessage());
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
//1
    @Override
    public SearchBaseWrapper searchBulkDisbursements(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        BulkDisbursementsViewModel model = (BulkDisbursementsViewModel) wrapper.getBasePersistableModel();
        CustomList<BulkDisbursementsViewModel> customList = null;
        customList = this.bulkDisbursementViewHibernateDAO.findByExample(model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModelList());
        wrapper.setCustomList(customList);
        return wrapper;
    }

    @Override
    public List<BulkDisbursementsModel> searchBulkDisbursementsModelList(SearchBaseWrapper wrapper) throws FrameworkCheckedException {

        BulkDisbursementsModel bulkDisbursementsModel = (BulkDisbursementsModel) wrapper.getBasePersistableModel();

        CustomList<BulkDisbursementsModel> customList = bulkDisbursementHibernateDAO.findByExample(bulkDisbursementsModel, wrapper.getPagingHelperModel(),
                wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel(), PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        List<BulkDisbursementsModel> list = new ArrayList<BulkDisbursementsModel>(customList.getResultsetList());

        return list;
    }

    @Override
    public SearchBaseWrapper searchBulkPayments(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        BulkPaymentViewModel model = (BulkPaymentViewModel) wrapper.getBasePersistableModel();
        CustomList<BulkPaymentViewModel> customList = null;
        customList = this.bulkPaymentViewHibernateDAO.findByExample(model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModelList());
        wrapper.setCustomList(customList);
        return wrapper;
    }

    @Override
    public SearchBaseWrapper loadBulkDisbursement(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        BulkDisbursementsModel model = (BulkDisbursementsModel) wrapper.getBasePersistableModel();
        model = this.bulkDisbursementHibernateDAO.findByPrimaryKey(model.getPrimaryKey());
        wrapper.setBasePersistableModel(model);
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

    public BulkDisbursementsFileInfoModel saveUpdateBulkDisbursementsFileInfoModel(BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel) {

        bulkDisbursementsFileInfoModel.setUpdatedOn(new Date(System.currentTimeMillis()));
        this.bulkDisbursementsFileInfoDAO.saveOrUpdate(bulkDisbursementsFileInfoModel);

        return bulkDisbursementsFileInfoModel;
    }

    /**
     * @param //bulkDisbursementsModelList
     * @throws Exception
     */
    public void saveBulkDisbursementsModelList(BulkDisbursementsVOModel bulkDisbursementsVOModel, BulkDisbursementsFileInfoModel fileInfoModel) throws Exception {

        try {
            CopyOnWriteArrayList<String[]> recordList = bulkDisbursementsVOModel.getRecordList();
            for (String[] bulkDisbursementsModel : recordList) {
                bulkDisbursementsModel[23] = String.valueOf(fileInfoModel.getFileInfoId());
            }

            bulkDisbursementHibernateDAO.createOrUpdateBulkDisbursements(recordList);

            fileInfoModel.setStatus(DisbursementStatusConstants.STATUS_PARSED);
            fileInfoModel.setUpdatedOn(new Date(System.currentTimeMillis()));

            this.bulkDisbursementsFileInfoDAO.saveOrUpdate(fileInfoModel);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(e.getLocalizedMessage());
        }
    }

    public void setProductAccounts(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

        ProductModel productModel = workFlowWrapper.getProductModel();
        if (productModel != null && productModel.getCommissionStakeHolderId() == null) {
            productModel = this.productManager.loadProductByProductId(productModel.getProductId());
            workFlowWrapper.setProductModel((ProductModel) productModel.clone());
        }

        logger.info("Loading Stakeholder for Product : " + productModel.getName());

        List<StakeholderBankInfoModel> stakeholderBankInfoModelList = stakeholderBankInfoManager.getStakeholderBankInfoByCommShId(productModel.getCommissionStakeHolderId());

        for (StakeholderBankInfoModel stakeholderBankInfoModel : stakeholderBankInfoModelList) {
            if (StakeholderBankInfoModel.ACCOUNT_TYPE_BLB.equals(stakeholderBankInfoModel.getAccountType())) {
                workFlowWrapper.putObject(StakeholderBankInfoModel.ACCOUNT_TYPE_BLB, stakeholderBankInfoModel);
                logger.info("ACCOUNT_TYPE_BLB " + stakeholderBankInfoModel.getAccountNo());
            } else if (StakeholderBankInfoModel.ACCOUNT_TYPE_OF_SET.equals(stakeholderBankInfoModel.getAccountType())) {
                workFlowWrapper.putObject(StakeholderBankInfoModel.ACCOUNT_TYPE_OF_SET, stakeholderBankInfoModel);
                logger.info("ACCOUNT_TYPE_OF_SET " + stakeholderBankInfoModel.getAccountNo());
            } else if (StakeholderBankInfoModel.ACCOUNT_TYPE_CORE.equals(stakeholderBankInfoModel.getAccountType())) {
                workFlowWrapper.putObject(StakeholderBankInfoModel.ACCOUNT_TYPE_CORE, stakeholderBankInfoModel);
                logger.info("ACCOUNT_TYPE_CORE " + stakeholderBankInfoModel.getAccountNo());
            }
        }
    }

    private List<CommissionWrapper> commissionWrappers = new ArrayList<>(100);

    private CommissionWrapper getCommissionWrapper(WorkFlowWrapper wrapper) throws Exception {
        CommissionWrapper commissionWrapper = null;

        for (CommissionWrapper cw : commissionWrappers) {
            CommissionAmountsHolder cah = (CommissionAmountsHolder) cw.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

            long productId = wrapper.getProductModel().getProductId();
            Double txAmount = wrapper.getTransactionAmount();

            if (cah.getProductId() == productId && cah.getTransactionAmount().doubleValue() == txAmount) {
                commissionWrapper = cw;

                break;
            }
        }

        if (commissionWrapper == null) {
            commissionWrapper = commissionManager.calculateCommission(wrapper);
            commissionWrappers.add(commissionWrapper);
        }

        return commissionWrapper;
    }

    private void populateCommissionWrapper(DisbursementVO disbursementVO, WorkFlowWrapper workFlowWrapper) throws Exception {
        workFlowWrapper.setTransactionAmount(disbursementVO.getAmount());
        workFlowWrapper.setTransactionModel(new TransactionModel(disbursementVO.getAmount()));

        long start = System.currentTimeMillis();
        CommissionWrapper commissionWrapper = getCommissionWrapper(workFlowWrapper);

        CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        workFlowWrapper.setCommissionWrapper(commissionWrapper);
        workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
    }

    public WorkFlowWrapper nonAcHolderFundTransfer(DisbursementVO disbursementVO, WorkFlowWrapper workFlowWrapper) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("nonAcHolderFundTransfer()");
        }

        ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());

        try {
            populateCommissionWrapper(disbursementVO, workFlowWrapper);

            StakeholderBankInfoModel blbSundryStakeholderBankInfoModel = (StakeholderBankInfoModel) workFlowWrapper.getObject("SUNDRY_ACCOUNT");
            StakeholderBankInfoModel productStakeholderBankInfoModel = (StakeholderBankInfoModel) workFlowWrapper.getObject(StakeholderBankInfoModel.ACCOUNT_TYPE_BLB);

            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setFromAccountNo(productStakeholderBankInfoModel.getAccountNo());
            switchWrapper.setToAccountBB(blbSundryStakeholderBankInfoModel.getAccountNo());
            switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

            workFlowWrapper.setSwitchWrapper(switchWrapper);
            workFlowWrapper.setWalkInCustomerCNIC(disbursementVO.getCnic());
            workFlowWrapper.setWalkInCustomerMob(disbursementVO.getMobileNo());
            workFlowWrapper.setWalkinLimitApplicable(disbursementVO.getLimitApplicable());
//            workFlowWrapper.setCheckIfLimitApplicable(disbursementVO.getLimitApplicable());
//            workFlowWrapper.setCoreSumAccountNumber(disbursementVO.isCoreSumAccountNumber());
            workFlowWrapper.setDiscountAmount(0D);
            workFlowWrapper.setRecipientWalkinCustomerModel(new WalkinCustomerModel(workFlowWrapper.getWalkInCustomerCNIC()));
            workFlowWrapper.setRecipientWalkinSmartMoneyAccountModel(new SmartMoneyAccountModel());
            workFlowWrapper.setBulkDisbursmentsId(disbursementVO.getDisbursementId());
            workFlowWrapper.putObject("PRODUCT_ACCOUNT", productStakeholderBankInfoModel);

            if (this.workFlowController == null) {
                workFlowController = (WorkFlowController) applicationContext.getBean("workFlowController");
            }

            String pin = CommonUtils.generateOneTimePin(5);
            String encryptedPin = EncoderUtils.encodeToSha(pin);
            workFlowWrapper.setOneTimePin(pin);

            workFlowWrapper = workFlowController.workflowProcess(workFlowWrapper);

            TransactionModel transactionModel = workFlowWrapper.getTransactionModel();

            LogModel logModel = new LogModel();
            logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
            logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

            MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
            miniTransactionModel.setTransactionCodeIdTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
            miniTransactionModel.setTransactionCodeId(transactionModel.getTransactionCodeIdTransactionCodeModel().getTransactionCodeId());
            miniTransactionModel.setCommandId(Long.valueOf(CommandFieldConstants.CMD_BULK_PAYMENT));
            miniTransactionModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            miniTransactionModel.setTimeDate(new Date());
            miniTransactionModel.setMobileNo(workFlowWrapper.getWalkInCustomerMob());
            miniTransactionModel.setSmsText(workFlowWrapper.getWalkInCustomerMob() + " " + transactionModel.getTotalAmount());
            miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
            miniTransactionModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
            miniTransactionModel.setOneTimePin(encryptedPin);
            miniTransactionModel.setCAMT(transactionModel.getTotalCommissionAmount());
            miniTransactionModel.setBAMT(transactionModel.getTransactionAmount());
            miniTransactionModel.setTAMT(transactionModel.getTotalAmount());
            miniTransactionModel.setCreatedOn(new Date());
            miniTransactionModel.setCreatedByAppUserModel(ThreadLocalAppUser.getAppUserModel());

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(miniTransactionModel);
            this.transactionManager.saveMiniTransaction(baseWrapper);
        } catch (Exception e) {
            logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
            e.printStackTrace();

            throw e;
        }

        return workFlowWrapper;
    }

    public CommissionWrapper calculateCommission(ProductModel productModel, Double amount, Long segmentId, Boolean isCustomerProduct,Long parm
                                                 , Boolean calculateShares,Long taxRegimeModelId) throws FrameworkCheckedException {

       TaxRegimeModel taxRegimeModel = taxManager.searchTaxRegimeById(taxRegimeModelId);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionAmount(amount);

        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setTransactionModel(transactionModel);
        workFlowWrapper.setProductModel(productModel);

        if (segmentId == null)
            segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;

        workFlowWrapper.setSegmentModel(new SegmentModel(segmentId));
 //       workFlowWrapper.setIsCustomerProduct(isCustomerProduct);
       workFlowWrapper.setTaxRegimeModel(taxRegimeModel);
        workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel(DeviceTypeConstantsInterface.BULK_DISBURSEMENT));
//        if (calculateShares)
//            workFlowWrapper.getDataMap().put("SHARES_CAL_REQUIRED", "Y");
//        else
//            workFlowWrapper.getDataMap().put("SHARES_CAL_REQUIRED", "N");

        return commissionManager.calculateCommission(workFlowWrapper);

        // return new CommissionWrapperImpl();
    }

    private void makeSumBLBFundTransfer(SwitchWrapper switchWrapper) throws Exception {

        CustomerModel customerModel = null;
        AccountInfoModel accountInfoModel = null;

        CommissionAmountsHolder commissionAmountsHolder = new CommissionAmountsHolder();
        commissionAmountsHolder.setTransactionAmount(switchWrapper.getTransactionAmount());
        commissionAmountsHolder.setTransactionProcessingAmount(0.0D);

        if (switchWrapper.getInclusiveChargesApplied() != null && switchWrapper.getInclusiveChargesApplied() > 0D) {
            commissionAmountsHolder.setTransactionProcessingAmount(switchWrapper.getInclusiveChargesApplied());
        }

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();
        workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
        workFlowWrapper.setFromSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
        workFlowWrapper.setToSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
        workFlowWrapper.setCustomerModel(customerModel);
        workFlowWrapper.setAccountInfoModel(accountInfoModel);

        switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

//        StakeholderBankInfoModel sourceSBIInfoModel = stakeholderBankInfoManager.loadStakeholderBankInfoModel(PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA);
        StakeholderBankInfoModel targetSBIInfoModel = (StakeholderBankInfoModel) workFlowWrapper.getObject(StakeholderBankInfoModel.ACCOUNT_TYPE_BLB);

//        switchWrapper.putObject("SOURCE_SBI", sourceSBIInfoModel);
        switchWrapper.putObject("TARGET_SBI", targetSBIInfoModel);

//        switchWrapper.setFromAccountNo(sourceSBIInfoModel.getAccountNo());
        switchWrapper.setToAccountNo(targetSBIInfoModel.getAccountNo());

        switchWrapper.setWorkFlowWrapper(workFlowWrapper);

        switchWrapper = olaVeriflyFinancialInstitution.bulkDisbursementSumFT(switchWrapper);

        switchWrapper.setResponseCode(switchWrapper.getOlavo().getResponseCode());

        if (switchWrapper.getResponseCode().equals(ResponseCodeEnum.PROCESSED_OK.getValue())) {

			/*logger.info("\n"+stakeholderBankInfoModel.getAccountNo() +"("+stakeholderBankInfoModel.getStakeholderBankInfoId()+") >> "+stakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModel().getAccountNo()+"("+stakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModel().getStakeholderBankInfoId()+") >> "+
					stakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModel().getOfSettlementStakeholderBankInfoModel().getAccountNo() +"("+stakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModel().getOfSettlementStakeholderBankInfoModel().getStakeholderBankInfoId()
					+"\n"+_stakeholderBankInfoModel.getAccountNo() +"("+_stakeholderBankInfoModel.getStakeholderBankInfoId()+") >> "+_stakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModel().getAccountNo()+"("+_stakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModel().getStakeholderBankInfoId()+") >> "+
					_stakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModel().getOfSettlementStakeholderBankInfoModel().getAccountNo() +"("+_stakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModel().getOfSettlementStakeholderBankInfoModel().getStakeholderBankInfoId()
			);*/
        	ThreadLocalAppUser.setAppUserModel(new AppUserModel(SCHEDULER_APP_USER_ID));

            settlementManager.prepareDataForDayEndSettlement(workFlowWrapper);
            //performDayEndSettlement(switchWrapper);
        }
    }

    @Override
    public void performDayEndSettlement(SwitchWrapper switchWrapper) throws Exception {
/*
        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        String fromAccount = switchWrapper.getFromAccountNo();
        String toAccount = switchWrapper.getToAccountNo();
        Double transactionAmount = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionAmount();
        Double transactionCharges = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount();

        Long accountType = CustomerAccountTypeConstants.SETTLEMENT;

        if (workFlowWrapper.getObject("OLA_SUM_FT") != null && workFlowWrapper.getCustomerModel() != null) {

            Boolean isOlaSumFT = (Boolean) workFlowWrapper.getObject("OLA_SUM_FT");

            if (isOlaSumFT) {
                accountType = workFlowWrapper.getCustomerModel().getCustomerAccountTypeId();
            }
        }

        SettlementVO fromAccountSettlementVO = new SettlementVO(workFlowWrapper.getTransactionModel().getTransactionId(),
                workFlowWrapper.getProductModel().getProductId(), workFlowWrapper.getBusinessDate(), Boolean.TRUE,
                transactionAmount, fromAccount, accountType.longValue(), null);

        accountType = CustomerAccountTypeConstants.SETTLEMENT;

        if (workFlowWrapper.getObject("OLA_SUM_FT") != null && workFlowWrapper.getCustomerModel() != null) {

            Boolean isOlaSumFT = (Boolean) workFlowWrapper.getObject("OLA_SUM_FT");

            if (isOlaSumFT) {
                accountType = workFlowWrapper.getCustomerModel().getCustomerAccountTypeId();
            }
        }

        SettlementVO fromAccountChargesSettlementVO = new SettlementVO(workFlowWrapper.getTransactionModel().getTransactionId(),
                workFlowWrapper.getProductModel().getProductId(), workFlowWrapper.getBusinessDate(), Boolean.TRUE,
                transactionCharges, fromAccount, accountType.longValue(), null);

        accountType = CustomerAccountTypeConstants.SETTLEMENT;

        if (switchWrapper.getWorkFlowWrapper().getIsCustomerProduct() && switchWrapper.getWorkFlowWrapper().getCustomerModel() != null) {

            accountType = switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId();
        }

        SettlementVO toAccountSettlementVO = new SettlementVO(workFlowWrapper.getTransactionModel().getTransactionId(),
                workFlowWrapper.getProductModel().getProductId(), workFlowWrapper.getBusinessDate(), Boolean.FALSE,
                transactionAmount, toAccount, accountType, null);

        SettlementVO toAccountChargesSettlementVO = new SettlementVO(workFlowWrapper.getTransactionModel().getTransactionId(),
                workFlowWrapper.getProductModel().getProductId(), workFlowWrapper.getBusinessDate(), Boolean.FALSE,
                transactionCharges, toAccount, accountType, null);

        settlementManager.performDayEndSettlement(fromAccountSettlementVO);
        settlementManager.performDayEndSettlement(fromAccountChargesSettlementVO);
        settlementManager.performDayEndSettlement(toAccountSettlementVO);
        settlementManager.performDayEndSettlement(toAccountChargesSettlementVO);
*/        
    }

    @Override
    public void performDisbursementLeg1(DisbursementWrapper disbursementWrapper, WorkFlowWrapper workFlowWrapper, Date currentDateTime, Boolean isCoreSourceAccountNo) throws Exception {

        Long serviceId = disbursementWrapper.getServiceId();
        Map<String, Integer> threadConfigMap = null;
        int threadType = -1;
        if (serviceId.longValue() == ServiceConstantsInterface.BULK_DISB_ACC_HOLDER.longValue()) {
            threadConfigMap = this.bulkDisbursementThreadConfigMap;
            threadType = ThreadGenerator.THREAD_AC_DISBURSEMENT;
        }

        else if (serviceId.longValue() == ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue()) {
            threadConfigMap = this.bulkPaymentThreadConfigMap;
            threadType = ThreadGenerator.THREAD_NON_AC_DISBURSEMENT;
        }

//        workFlowWrapper.setBusinessDate(dayendManager.getBusinessDate());
//        TaxRegimeModel taxRegimeModel = taxManager.searchTaxRegimeById(TaxRegimeConstants.FEDERAL);
//        workFlowWrapper.setTaxRegimeModel(taxRegimeModel);
        workFlowWrapper.setSegmentModel(new SegmentModel(CommissionConstantsInterface.DEFAULT_SEGMENT_ID));
        workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel(TransactionTypeConstantsInterface.BULK_PAYMENT_TX));
        workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel(DeviceTypeConstantsInterface.BULK_DISBURSEMENT));
        workFlowWrapper.setAppUserModel(UserUtils.getCurrentUser());

        ThreadGenerator threadGenerator = new ThreadGenerator(applicationContext, threadType, workFlowWrapper, disbursementWrapper.getDisbursementVOList(), threadConfigMap);
        threadGenerator.execute();
    }

    @Override
    public void updatePostedRecords(String batchNumber) {
        this.bulkDisbursementHibernateDAO.updatePostedRecords(batchNumber);
    }

    @Override
    public void updatePostedRecordsForT24(String batchNumber) {
        this.bulkDisbursementHibernateDAO.updatePostedRecordsForT24(batchNumber);
    }

    @Override
    public BBAccountsViewModel getBBAccountsViewModel(BBAccountsViewModel model) throws FrameworkCheckedException {
        CustomList<BBAccountsViewModel> modelList = new CustomList<BBAccountsViewModel>();
        modelList = this.bBAccountsViewDao.findByExample(model, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
        if (modelList.getResultsetList().size() > 0) {
            model = modelList.getResultsetList().get(0);
        }
        return model;
    }

    @Override
    public List<DisbursementFileInfoViewModel> findDFIVModelByExample(SearchBaseWrapper searchBaseWrapper) throws Exception {

        CustomList<DisbursementFileInfoViewModel> list = disbursementFileInfoViewDAO.findByExample(
                (DisbursementFileInfoViewModel) searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModel());


        return list != null ? list.getResultsetList() : null;
    }

    public List<DisbursementWrapper> findDueDisbursement(Long serviceId, Date currentDateTime, Boolean isCoreSourceAccountNo, Boolean posted, Boolean settled) throws Exception {
        List<DisbursementVO> disbursementVOList = bulkDisbursementHibernateDAO.findDueDisbursement(serviceId, currentDateTime, isCoreSourceAccountNo, posted, settled);
        if (CollectionUtils.isEmpty(disbursementVOList)) {
            logger.info("### NO RECORD FOUND TO PROCESS CORE SUM FT for any batch Service Id : " + serviceId +
                    " From " + (isCoreSourceAccountNo ? " Core " : " BLB ") + "Source A/c " +
                    " Posted : " + (posted ? " Yes " : " No ") + " Settled: " + (settled ? " Yes " : " No "));

            return null;
        }

        // group all found records by batch number
        List<DisbursementWrapper> wrappers = new ArrayList<>(5);
        for (DisbursementVO vo : disbursementVOList) {
            groupByBatchNumber(wrappers, vo);
        }

        return wrappers;
    }

    @Override
    public List<DisbursementWrapper> findDueDisbursementForT24(Long serviceId, Date currentDateTime, Boolean isCoreSourceAccountNo, Boolean posted, Boolean settled) throws Exception {
        List<DisbursementVO> disbursementVOList = bulkDisbursementHibernateDAO.findDueDisbursementForT24(serviceId, currentDateTime, isCoreSourceAccountNo, posted, settled);
        if (CollectionUtils.isEmpty(disbursementVOList)) {
            logger.info("### NO RECORD FOUND TO PROCESS CORE SUM FT for any batch Service Id : " + serviceId +
                    " From " + (isCoreSourceAccountNo ? " Core " : " BLB ") + "Source A/c " +
                    " Posted : " + (posted ? " Yes " : " No ") + " Settled: " + (settled ? " Yes " : " No "));

            return null;
        }

        // group all found records by batch number
        List<DisbursementWrapper> wrappers = new ArrayList<>(5);
        for (DisbursementVO vo : disbursementVOList) {
            groupByBatchNumber(wrappers, vo);
        }

        return wrappers;
    }


    private void groupByBatchNumber(List<DisbursementWrapper> list, DisbursementVO vo) {
        String batchNumber = vo.getBatchNumber();
        DisbursementWrapper target = null;
        for (DisbursementWrapper wrapper : list) {
            if (batchNumber.equalsIgnoreCase(wrapper.getBatchNumber())) {
                target = wrapper;
                break;
            }
        }

        if (target == null) {
            list.add(new DisbursementWrapper(vo));
        } else {
            target.add(vo);
        }
    }

    @Override
    public Object getDisbursementFileSettlementStatus(String batchNumber) {
        return bulkDisbursementsFileInfoDAO.getDisbursementFileSettlementStatus(batchNumber);
    }

    @Override
    public int updateDisbursementFileStatus(Long fileInfoId, Integer status) {
        return bulkDisbursementsFileInfoDAO.updateDisbursementFileStatus(fileInfoId, status);
    }

    public void setBulkDisbursementHibernateDAO(BulkDisbursementDAO bulkDisbursementHibernateDAO) {
        this.bulkDisbursementHibernateDAO = bulkDisbursementHibernateDAO;
    }

    public void setBulkDisbursementViewHibernateDAO(BulkDisbursementViewDAO bulkDisbursementViewHibernateDAO) {
        this.bulkDisbursementViewHibernateDAO = bulkDisbursementViewHibernateDAO;
    }

    public void setBulkPaymentViewHibernateDAO(BulkPaymentViewDAO bulkPaymentViewHibernateDAO) {
        this.bulkPaymentViewHibernateDAO = bulkPaymentViewHibernateDAO;
    }

    public void setOlaVeriflyFinancialInstitution(AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
        this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
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

    public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
        this.phoenixFinancialInstitution = phoenixFinancialInstitution;
    }

    public void setBulkDisbursementsFileInfoDAO(BulkDisbursementsFileInfoDAO bulkDisbursementsFileInfoDAO) {
        this.bulkDisbursementsFileInfoDAO = bulkDisbursementsFileInfoDAO;
    }

    public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

//    public void setTaxManager(TaxManager taxManager) {
//        this.taxManager = taxManager;
//    }

    public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
        this.encryptionHandler = encryptionHandler;
    }

    public void setAccountInfoDao(AccountInfoDAO accountInfoDao) {
        this.accountInfoDao = accountInfoDao;
    }

    public void setBulkPaymentThreadConfigMap(Map<String, Integer> bulkPaymentThreadConfigMap) {
        this.bulkPaymentThreadConfigMap = bulkPaymentThreadConfigMap;
    }

    public void setBulkDisbursementThreadConfigMap(Map<String, Integer> bulkDisbursementThreadConfigMap) {
        this.bulkDisbursementThreadConfigMap = bulkDisbursementThreadConfigMap;
    }

//    public void setDayendManager(DayendManager dayendManager) {
//        this.dayendManager = dayendManager;
//    }

    public void setbBAccountsViewDao(BBAccountsViewDao bBAccountsViewDao) {
        this.bBAccountsViewDao = bBAccountsViewDao;
    }

    public void setDisbursementFileInfoViewDAO(DisbursementFileInfoViewDAO disbursementFileInfoViewDAO) {
        this.disbursementFileInfoViewDAO = disbursementFileInfoViewDAO;
    }

    @Override
    public void makeCoreFundTransfer(Long serviceId, Date currentDateTime) throws Exception {
    }

    @Override
    public void makeBLBFundTransfer(Long serviceId, Date currentDateTime) throws Exception {
    }
    
	private SwitchWrapper performCoreFT(String sourceAccount, String recipientAccount, Double totalDisbursableAmount, WorkFlowWrapper wrapper) throws FrameworkCheckedException{
		
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

//            transactionManager.generateTransactionCodeRequiresNewTransaction(wrapper);
//            wrapper.setTransactionModel(new TransactionModel());

			switchWrapper.setWorkFlowWrapper(wrapper);
			switchWrapper.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
			switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
			
            MiddlewareMessageVO integrationMessageVO = new MiddlewareMessageVO();
            integrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            integrationMessageVO.setAccountNo1(switchWrapper.getFromAccountNo());
            integrationMessageVO.setAccountNo2(switchWrapper.getToAccountNo());
            integrationMessageVO.setTransactionAmount(String.valueOf(switchWrapper.getTransactionAmount()));
            switchWrapper.setMiddlewareIntegrationMessageVO(integrationMessageVO);

			switchWrapper = switchController.debitAccount(switchWrapper);

            wrapper.setSwitchWrapper(switchWrapper);
			actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogAfterEnd(actionLogModel);

		} catch (Exception e) {
				logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				logger.error("!!!!!  ERROR: Exception at middleware while FT for bulk disbursment.  !!!!!");
				logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
			logger.error(e.getMessage(), e);
			throw new FrameworkCheckedException("Fund transfer at middleware failed, unable to transfer funds.");
		}
		
		String responseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();

		if (StringUtils.isNotEmpty(responseCode) && (responseCode.equals("00") || responseCode.equals("I8SB-200"))) {
			logger.info("FT at middleware was successful.");
		} else {
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("!!!!!  ERROR: FT at middleware Failed. Response Code:" + responseCode + "  !!!!!");
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
			logger.error("*** FUND TRANSFER AT MIDDLEEARE FAILED, CAN'T TRANSFER FUNDS ***");
			throw new FrameworkCheckedException("Fund transfer at middleware failed, unable to transfer funds.");
		}
		
		return switchWrapper;
		
	}

    public List<BulkDisbursementsModel> makeCoreSumFT(DisbursementWrapper disbursementWrapper, StakeholderBankInfoModel stakeholderBankInfoModel, WorkFlowWrapper workFlowWrapper) throws Exception {
        CopyOnWriteArrayList<BulkDisbursementsModel> successList = new CopyOnWriteArrayList<>();

        String sourceAccountNumber = disbursementWrapper.getSourceAccount();
        String bulkSundryCoreAccountNumber = stakeholderBankInfoModel.getAccountNo();
        Double amount = CommonUtils.formatAmountTwoDecimal(disbursementWrapper.getAmount());
        Double charges = CommonUtils.formatAmountTwoDecimal(disbursementWrapper.getCharges());

        try {
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setTransactionAmount(disbursementWrapper.getAmount());
            switchWrapper.setInclusiveChargesApplied(disbursementWrapper.getCharges());
            switchWrapper.setWorkFlowWrapper(workFlowWrapper);
            
			workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());		
			workFlowWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);
			
	        Double totalAmount = CommonUtils.formatAmountTwoDecimal(amount + charges);

			TransactionModel transactionModel = new TransactionModel();
			transactionModel.setTransactionAmount(totalAmount);
			
			workFlowWrapper.setTransactionModel(transactionModel);		
            
			saveTransactionData(switchWrapper);
            
            logger.info("SUM FT Tx Code :" + workFlowWrapper.getTransactionCodeModel().getCode() + " Source A/C " + sourceAccountNumber);
            
            // perform SUM FT at OLA
            this.makeSumBLBFundTransfer(switchWrapper);
            
            logger.info("### POST CORE FUND TRANSFER, PRODUCT : " + workFlowWrapper.getProductModel().getName() +
                    " SOURCE CORE A/C # " + sourceAccountNumber + " DESTINATION CORE A/C # " + bulkSundryCoreAccountNumber +
                    " AMOUNT " + amount + " CHARGES " + charges);
            
            this.performCoreFT(sourceAccountNumber, bulkSundryCoreAccountNumber, totalAmount, workFlowWrapper);

            TransactionDetailMasterModel tdm = transactionDetailMasterManager.loadTransactionDetailMasterModel(workFlowWrapper.getTransactionCodeModel().getCode());
            tdm.setStan(workFlowWrapper.getSwitchWrapper().getMiddlewareIntegrationMessageVO().getStan());
            transactionDetailMasterManager.saveTransactionDetailMaster(tdm);
//            saveTransactionData(switchWrapper);

        } catch (Exception e) {
            logger.error("makeCoreSumFT() failed, Exception : " + e.getMessage(), e);
            throw e;
        }

        return successList;
    }

    @Override
    public void saveBulkDisbursementWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        List<BulkDisbursementsFileInfoModel> bulkDisbursementsFileInfoModelList = new ArrayList<>();
        BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = (BulkDisbursementsFileInfoModel) baseWrapper.getBasePersistableModel();

        bulkDisbursementsFileInfoModelList.add(bulkDisbursementsFileInfoModel);
        bulkDisbursementsFileInfoDAO.saveOrUpdateCollection(bulkDisbursementsFileInfoModelList);
    }

    @Override
    public List<BulkDisbursementsModel> loadBulkDisbursementModelList(SearchBaseWrapper searchBaseWrapper) throws Exception {
        BulkDisbursementsModel bulkDisbursementsModel = (BulkDisbursementsModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<BulkDisbursementsModel> customList = bulkDisbursementHibernateDAO.findByExample
                (bulkDisbursementsModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
        List<BulkDisbursementsModel> resultList = new ArrayList<>();
        if (customList != null && CollectionUtils.isNotEmpty(customList.getResultsetList())) {
            resultList = customList.getResultsetList();
        }

        return resultList;
    }

    @Override
    public void updateIsApprovedForBatch(String batchNumber) throws Exception {
        BulkDisbursementsModel model = new BulkDisbursementsModel();
        List<BulkDisbursementsModel> updatedBulkDisbursementsList = new ArrayList<>();
        model.setBatchNumber(batchNumber);
        CustomList<BulkDisbursementsModel> bulkDisbursementModelList = (CustomList<BulkDisbursementsModel>)
                bulkDisbursementHibernateDAO.findByExample(model, null);

//        if(bulkDisbursementsId != null && bulkDisbursementsId.length > 0){
//            HashSet<String> hs = new HashSet<String>(Arrays.asList(bulkDisbursementsId));
            for(BulkDisbursementsModel bulkDisbursementsModel : bulkDisbursementModelList.getResultsetList()){
                bulkDisbursementsModel.setIsApproved("1");
                bulkDisbursementsModel.setUpdatedOn(new Date());
                bulkDisbursementsModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

                updatedBulkDisbursementsList.add(bulkDisbursementsModel);

            }
            bulkDisbursementHibernateDAO.saveOrUpdateCollection(updatedBulkDisbursementsList);
//        }
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

	public void setTaxManager(TaxManager taxManager) {
		this.taxManager = taxManager;
	}


    public void setAdvanceSalaryLoanDAO(AdvanceSalaryLoanDAO advanceSalaryLoanDAO) {
        this.advanceSalaryLoanDAO = advanceSalaryLoanDAO;
    }
}
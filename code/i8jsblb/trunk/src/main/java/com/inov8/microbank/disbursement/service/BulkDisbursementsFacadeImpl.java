package com.inov8.microbank.disbursement.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.enums.ResponseCodeEnum;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.disbursement.dao.BulkDisbursementsFileInfoDAO;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsVOModel;
import com.inov8.microbank.disbursement.vo.DisbursementVO;
import com.inov8.microbank.disbursement.vo.DisbursementWrapper;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.verifly.common.model.AccountInfoModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BulkDisbursementsFacadeImpl implements BulkDisbursementsFacade {

    private static Logger logger = Logger.getLogger(BulkDisbursementsFacadeImpl.class);

    private BulkDisbursementsManager bulkDisbursementsManager;
    private FrameworkExceptionTranslator frameworkExceptionTranslator;
    private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
    private StakeholderBankInfoManager stakeholderBankInfoManager;
    private BulkDisbursementsFileInfoDAO bulkDisbursementsFileInfoDAO;

    @Override
    public BulkDisbursementsModel saveOrUpdateBulkDisbursement(BulkDisbursementsModel bulkDisbursementsModel) throws FrameworkCheckedException {
        try {
            return bulkDisbursementsManager.saveOrUpdateBulkDisbursement(bulkDisbursementsModel);
        } catch (Exception e) {
            throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
    }

    @Override
    public WorkFlowWrapper nonAcHolderFundTransfer(DisbursementVO disbursementVO, WorkFlowWrapper workFlowWrapper) throws Exception {
        return bulkDisbursementsManager.nonAcHolderFundTransfer(disbursementVO, workFlowWrapper);
    }

    @Override
    public void makeAcHolderTransferFund(DisbursementVO disbursementVO, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        try {
            bulkDisbursementsManager.makeAcHolderTransferFund(disbursementVO, workFlowWrapper);
            creditAccountQueingPreProcessor.startProcessing(workFlowWrapper);
        } catch (Exception e) {
            throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.INSERT_ACTION);
        }
    }

    @Override
    public SearchBaseWrapper searchBulkDisbursements(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        try {
            wrapper = bulkDisbursementsManager.searchBulkDisbursements(wrapper);
        } catch (Exception e) {
            throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
        }
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchBulkPayments(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        try {
            wrapper = bulkDisbursementsManager.searchBulkPayments(wrapper);
        } catch (Exception e) {
            throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
        }
        return wrapper;
    }

    @Override
    public SearchBaseWrapper loadBulkDisbursement(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        try {
            wrapper = bulkDisbursementsManager.loadBulkDisbursement(wrapper);
        } catch (Exception e) {
            throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
        }
        return wrapper;
    }


    @Override
    public List<LabelValueBean> loadBankUsersList() throws FrameworkCheckedException {
        try {
            return bulkDisbursementsManager.loadBankUsersList();
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<BulkDisbursementsModel> searchBulkDisbursementsModelList(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        return this.bulkDisbursementsManager.searchBulkDisbursementsModelList(wrapper);
    }

    @Override
    public BulkDisbursementsFileInfoModel saveUpdateBulkDisbursementsFileInfoModel(BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel) {
        return bulkDisbursementsManager.saveUpdateBulkDisbursementsFileInfoModel(bulkDisbursementsFileInfoModel);
    }

    public List<DisbursementFileInfoViewModel> findDFIVModelByExample(SearchBaseWrapper searchBaseWrapper) throws Exception {
        return bulkDisbursementsManager.findDFIVModelByExample(searchBaseWrapper);
    }

    public List<DisbursementWrapper> findDueDisbursement(Long serviceId, Date currentDateTime, Boolean isCoreSourceAccountNo, Boolean posted, Boolean settled) throws Exception {
        return bulkDisbursementsManager.findDueDisbursement(serviceId, currentDateTime, isCoreSourceAccountNo, posted, settled);
    }

    @Override
    public List<DisbursementWrapper> findDueDisbursementForT24(Long serviceId, Date currentDateTime, Boolean isCoreSourceAccountNo, Boolean posted, Boolean settled) throws Exception {
        return bulkDisbursementsManager.findDueDisbursementForT24(serviceId, currentDateTime, isCoreSourceAccountNo, posted, settled);
    }

    @Override
    public void saveBulkDisbursementsModelList(BulkDisbursementsVOModel bulkDisbursementsVOModel, BulkDisbursementsFileInfoModel fileInfoModel) throws Exception {
        bulkDisbursementsManager.saveBulkDisbursementsModelList(bulkDisbursementsVOModel, fileInfoModel);
    }

    @Override
    public CommissionWrapper calculateCommission(ProductModel productModel, Double amount, Long segmentId, Boolean isCustomerProduct, Long param, Boolean calculateShares, Long taxRegimeId) throws FrameworkCheckedException {
        return this.bulkDisbursementsManager.calculateCommission(productModel, amount, segmentId, isCustomerProduct, param, calculateShares, taxRegimeId);
    }

    @Override
    public void setProductAccounts(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        this.bulkDisbursementsManager.setProductAccounts(workFlowWrapper);
    }

    @Override
    public BBAccountsViewModel getBBAccountsViewModel(BBAccountsViewModel model) throws FrameworkCheckedException {
        return bulkDisbursementsManager.getBBAccountsViewModel(model);
    }

    @Override
    public void makeCoreFundTransfer(Long serviceId, Date currentDateTime) throws Exception {

        StakeholderBankInfoModel stakeholderBankInfoModel = stakeholderBankInfoManager.loadStakeholderBankInfoModel(PoolAccountConstantsInterface.BULK_DISBURSEMENT_POOL_ACCOUNT_CORE);
        String bulkSundryCoreAccountNumber = stakeholderBankInfoModel.getAccountNo();
        if (StringUtil.isNullOrEmpty(bulkSundryCoreAccountNumber)) {
            logger.error("Unable to load Bulk Disbursement Sundry Core A/C : " + PoolAccountConstantsInterface.BULK_DISBURSEMENT_POOL_ACCOUNT_CORE);
            return;
        }

        // group all found records by batch number
        List<DisbursementWrapper> wrappers = findDueDisbursementForT24(serviceId, currentDateTime, true, false, false);
        if (CollectionUtils.isNotEmpty(wrappers)) {

            WorkFlowWrapper workFlowWrapper = null;
            for (DisbursementWrapper wrapper : wrappers) {
                try {
                    logger.info("Checking if any batch is already in process.");
                    BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = bulkDisbursementsFileInfoDAO.getBulkDisbursementsDataByStatus("In-Process");
                    if(bulkDisbursementsFileInfoModel != null){
                        logger.error("Some Batch is already in process. " + bulkDisbursementsFileInfoModel.getBatchNumber());
                        throw new Exception("Some Batch is already in process " + bulkDisbursementsFileInfoModel.getBatchNumber());
                    }

                    workFlowWrapper = new WorkFlowWrapperImpl();
                    workFlowWrapper.setProductModel(wrapper.getProductModel());
                    this.setProductAccounts(workFlowWrapper);

                    if (StringUtil.isNullOrEmpty(wrapper.getSourceAccount())) {
                        logger.info("Source a/c is null/empty ");
                        continue;
                    }

                    Double amount = CommonUtils.formatAmountTwoDecimal(wrapper.getAmount());
                    if (amount < 1) {
                        logger.info("Amount is less than 1");
                        continue;
                    }

                    // perform Core FT for Sum of amounts & charges
                    bulkDisbursementsManager.makeCoreSumFT(wrapper, stakeholderBankInfoModel, workFlowWrapper);

                    updatePostedRecordsForT24(wrapper.getBatchNumber());

                    creditAccountQueingPreProcessor.startProcessing(workFlowWrapper);
                } catch (Exception e) {
                    logger.error("Exception occurred on posting for Batch Number : " + wrapper.getBatchNumber() + " Product " + wrapper.getProductName(), e);
                    throw new Exception("Exception occurred on posting for Batch Number",e);
                }
            }
        }

        performDisbursementLeg1(serviceId, currentDateTime, true, true, false);
    }

/*    public List<BulkDisbursementsModel> postCoreTransactions(DisbursementWrapper disbursementWrapper, StakeholderBankInfoModel stakeholderBankInfoModel, WorkFlowWrapper workFlowWrapper) throws Exception {
        CopyOnWriteArrayList<BulkDisbursementsModel> successList = new CopyOnWriteArrayList<>();

        String sourceAccountNumber = disbursementWrapper.getSourceAccount();
        String bulkSundryCoreAccountNumber = stakeholderBankInfoModel.getAccountNo();
        Double amount = CommonUtils.formatAmountTwoDecimal(disbursementWrapper.getAmount());
        Double charges = CommonUtils.formatAmountTwoDecimal(disbursementWrapper.getCharges());

        int middlewareFTSuccessful = 0;
        try {
            SwitchWrapper switchWrapper = postSumFTAtMiddleware(sourceAccountNumber, bulkSundryCoreAccountNumber, amount, charges, workFlowWrapper);

    		String responseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();

            if (ResponseCodeEnum.PROCESSED_OK.getValue().equals(responseCode)) {
                middlewareFTSuccessful = 1;

                saveTransactionData(switchWrapper);

                logger.info("SUM FT Tx Code :" + workFlowWrapper.getTransactionCodeModel().getCode() + " Source A/C " + sourceAccountNumber);

                switchWrapper.setTransactionAmount(disbursementWrapper.getAmount());
                switchWrapper.setInclusiveChargesApplied(disbursementWrapper.getCharges());

                // perform SUM FT at OLA
                this.makeSumBLBFundTransfer(switchWrapper);

                //			successList.addAll(_bulkDisbursementsModelList);
            }
        } catch (Exception e) {
            logger.error("postCoreTransactions() failed, Exception : " + e.getMessage());
            e.printStackTrace();

            //reversal FT in case if core FT is successful but OLA is failed
            if (middlewareFTSuccessful == 1) {
                rollbackSumFTAtMiddleware(bulkSundryCoreAccountNumber, sourceAccountNumber, amount, charges, workFlowWrapper);
            }

            throw e;
        }

        return successList;
    }
*/

    @Override
    public void makeBLBFundTransfer(Long serviceId, Date currentDateTime) throws Exception {

        // group all found records by batch number
        List<DisbursementWrapper> wrappers = findDueDisbursement(serviceId, currentDateTime, false, false, false);
        if (CollectionUtils.isNotEmpty(wrappers)) {

            WorkFlowWrapper workFlowWrapper = null;
            for (DisbursementWrapper wrapper : wrappers) {
                try {
                    logger.info("Checking if any batch is already in process.");
                    BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = bulkDisbursementsFileInfoDAO.getBulkDisbursementsDataByStatus("In-Process");
                    if(bulkDisbursementsFileInfoModel != null){
                        logger.error("Some Batch is already in process.");
                        throw new Exception("Some Batch is already in process");
                    }

                    workFlowWrapper = new WorkFlowWrapperImpl();
                    workFlowWrapper.setProductModel(wrapper.getProductModel());

                    this.setProductAccounts(workFlowWrapper);

                    if (StringUtil.isNullOrEmpty(wrapper.getSourceAccount())) {
                        logger.info("Source a/c is null/empty ");
                        continue;
                    }

                    Double amount = CommonUtils.formatAmountTwoDecimal(wrapper.getAmount());
                    if (amount < 1) {
                        logger.info("Amount is less than 1");
                        continue;
                    }

                    String encryptAccountNumber = com.inov8.ola.util.EncryptionUtil.encryptAccountNo(wrapper.getSourceAccount());

                    BBAccountsViewModel bbACModel = new BBAccountsViewModel();
                    bbACModel.setAccountNumber(encryptAccountNumber);
                    bbACModel = getBBAccountsViewModel(bbACModel);

                    StakeholderBankInfoModel productBLBStakeholder = (StakeholderBankInfoModel) workFlowWrapper.getObject(StakeholderBankInfoModel.ACCOUNT_TYPE_BLB);
                    ProductModel productModel = workFlowWrapper.getProductModel();

                    if (productBLBStakeholder != null) {
                        AccountInfoModel accountInfoModel = populateAccountInfo(bbACModel);
                        //sum FT
                        SwitchWrapper switchWrapper = this.performBLBSumFT(accountInfoModel, productBLBStakeholder, wrapper, workFlowWrapper);

                        updatePostedRecords(wrapper.getBatchNumber());

                        creditAccountQueingPreProcessor.startProcessing(switchWrapper.getWorkFlowWrapper());
                    } else {
                        logger.error("BLB A/c is not available. Invalid Stakeholder is linked with Product Id : " + productModel.getProductId() + " Name : " + productModel.getName() +
                                " Stakeholder Bank Info Id : " + productModel.getCommissionStakeHolderId());
                    }
                } catch (Exception e) {
                    logger.error("Exception occurred on posting for Batch Number : " + wrapper.getBatchNumber() + " Product " + wrapper.getProductName(), e);
                    throw new Exception("Exception occurred on posting for Batch Number",e);
                }
            }
        }

        performDisbursementLeg1(serviceId, currentDateTime, false, true, false);
    }

    private AccountInfoModel populateAccountInfo(BBAccountsViewModel bbAccountsViewModel) {
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setAccountTypeId(bbAccountsViewModel.getAccountTypeId());
        try {
            accountInfoModel.setAccountNo(com.inov8.ola.util.EncryptionUtil.decryptAccountNo(bbAccountsViewModel.getAccountNumber()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != bbAccountsViewModel.getMobileNo()) {
            accountInfoModel.setCustomerMobileNo(bbAccountsViewModel.getMobileNo());
        }
        return accountInfoModel;
    }

    private void performDisbursementLeg1(Long serviceId, Date currentDateTime, Boolean isCoreSumAccount, Boolean posted, Boolean settled) throws Exception {
        List<DisbursementWrapper> wrappers = findDueDisbursement(serviceId, currentDateTime, isCoreSumAccount, posted, settled);

        if (CollectionUtils.isEmpty(wrappers)) {
            logger.info("No un settled transaction(s) are available for service id " + serviceId);
            return;
        }

        StakeholderBankInfoModel blbSundryStakeholderBankInfoModel = null;
        if (ServiceConstantsInterface.BULK_DISB_ACC_HOLDER.longValue() == serviceId) {
            blbSundryStakeholderBankInfoModel = stakeholderBankInfoManager.loadStakeholderBankInfoModel(PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA);
        } else {
            blbSundryStakeholderBankInfoModel = stakeholderBankInfoManager.loadStakeholderBankInfoModel(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
        }

        WorkFlowWrapper workFlowWrapper;
        for (DisbursementWrapper wrapper : wrappers) {
            bulkDisbursementsManager.updateDisbursementFileProcessingStatus(wrapper.getDisbursementFileInfoId(), "In-Process");
            try {
                long start = System.currentTimeMillis();

                List<DisbursementVO> disbursementVOList = wrapper.getDisbursementVOList();
                if (CollectionUtils.isEmpty(disbursementVOList)) {
                    logger.info("No unsettled disbursement jobs found.");

                    return;
                }

                logger.info(disbursementVOList.size() + " unsettled disbursement job(s) found.");

                workFlowWrapper = new WorkFlowWrapperImpl();
                workFlowWrapper.setProductModel(wrapper.getProductModel());
                workFlowWrapper.putObject("SUNDRY_ACCOUNT", blbSundryStakeholderBankInfoModel);
                workFlowWrapper.setAppUserModel(UserUtils.getCurrentUser());
                this.setProductAccounts(workFlowWrapper);

                bulkDisbursementsManager.performDisbursementLeg1(wrapper, workFlowWrapper, currentDateTime, true);

                Object obj = bulkDisbursementsManager.getDisbursementFileSettlementStatus(wrapper.getBatchNumber());
                if (obj != null) {
                    Object[] results = (Object[]) obj;

                    BigDecimal totalSettled = (BigDecimal) results[2];
                    BigDecimal validRecords = (BigDecimal) results[4];
                    if (validRecords.longValue() == totalSettled.longValue()) {
                        bulkDisbursementsManager.updateDisbursementFileStatus(wrapper.getDisbursementFileInfoId(), DisbursementStatusConstants.STATUS_DISBURSED);
                        bulkDisbursementsManager.updateDisbursementFileProcessingStatus(wrapper.getDisbursementFileInfoId(), "Completed");

                        logger.info("Batch : " + wrapper.getBatchNumber() + " Size " + wrapper.getDisbursementVOList().size() +
                                " Completed in " + (System.currentTimeMillis() - start) / 1000.d + " seconds.");
                    }
                }

//                logger.info("Batch : " + wrapper.getBatchNumber() + " Size " + wrapper.getDisbursementVOList().size() +
//                        " Completed in " + (System.currentTimeMillis() - start)/1000.d + " seconds.");
            } catch (Exception e) {
                logger.error("Exception occurred on Transaction " + e.getMessage());
            }
        }
    }

    @Override
    public void performDisbursementLeg1(DisbursementWrapper wrapper, WorkFlowWrapper workFlowWrapper, Date currentDateTime, Boolean isCoreSourceAccountNo) throws Exception {
        bulkDisbursementsManager.performDisbursementLeg1(wrapper, workFlowWrapper, currentDateTime, isCoreSourceAccountNo);
    }

    public static void setLogger(Logger logger) {
        BulkDisbursementsFacadeImpl.logger = logger;
    }

    @Override
    public void updatePostedRecords(String batchNumber) {
        bulkDisbursementsManager.updatePostedRecords(batchNumber);
    }

    @Override
    public void updatePostedRecordsForT24(String batchNumber) {
        bulkDisbursementsManager.updatePostedRecordsForT24(batchNumber);
    }

    /*  @Override
        public SwitchWrapper rollbackSumFTAtMiddleware(String sourceAccountNumber,
                                                       String targetAccountNumber, Double amount, Double charges,
                                                       WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

            return bulkDisbursementsManager.rollbackSumFTAtMiddleware(sourceAccountNumber, targetAccountNumber, amount, charges, workFlowWrapper);
        }

        @Override
        public void makeSumBLBFundTransfer(SwitchWrapper switchWrapper) throws Exception {
            bulkDisbursementsManager.makeSumBLBFundTransfer(switchWrapper);
        }

        @Override
        public SwitchWrapper postSumFTAtMiddleware(String sourceAccountNumber, String bulkSundryCoreAccount, Double amount, Double charges,
                                                   WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
            return bulkDisbursementsManager.postSumFTAtMiddleware(sourceAccountNumber, bulkSundryCoreAccount, amount, charges, workFlowWrapper);
        }
    */
    @Override
    public void saveTransactionData(SwitchWrapper switchWrapper) {
        bulkDisbursementsManager.saveTransactionData(switchWrapper);
    }

    @Override
    public SwitchWrapper performBLBSumFT(AccountInfoModel sourceAccountInfoModel, StakeholderBankInfoModel productBLBAccount,
                                         DisbursementWrapper disbursementWrapper, WorkFlowWrapper workFlowWrapper) throws Exception {
        return bulkDisbursementsManager.performBLBSumFT(sourceAccountInfoModel, productBLBAccount, disbursementWrapper, workFlowWrapper);
    }

    @Override
    public void performDayEndSettlement(SwitchWrapper switchWrapper) throws Exception {
        bulkDisbursementsManager.performDayEndSettlement(switchWrapper);
    }

    public Object getDisbursementFileSettlementStatus(String batchNumber) {
        return bulkDisbursementsManager.getDisbursementFileSettlementStatus(batchNumber);
    }

    public int updateDisbursementFileStatus(Long fileInfoId, Integer status) {
        return bulkDisbursementsManager.updateDisbursementFileStatus(fileInfoId, status);
    }

    public void setBulkDisbursementsManager(BulkDisbursementsManager bulkDisbursementsManager) {
        this.bulkDisbursementsManager = bulkDisbursementsManager;
    }

    public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator) {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

    public void setCreditAccountQueingPreProcessor(CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
        this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
    }

    public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }
    public void setBulkDisbursementsFileInfoDAO(BulkDisbursementsFileInfoDAO bulkDisbursementsFileInfoDAO) {
        this.bulkDisbursementsFileInfoDAO = bulkDisbursementsFileInfoDAO;
    }

    @Override
    public List<BulkDisbursementsModel> makeCoreSumFT(
            DisbursementWrapper disbursementWrapper,
            StakeholderBankInfoModel stakeholderBankInfoModel,
            WorkFlowWrapper workFlowWrapper) throws Exception {

        return bulkDisbursementsManager.makeCoreSumFT(disbursementWrapper, stakeholderBankInfoModel, workFlowWrapper);

    }

    @Override
    public void saveBulkDisbursementWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            bulkDisbursementsManager.saveBulkDisbursementWithAuthorization(baseWrapper);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<BulkDisbursementsModel> loadBulkDisbursementModelList(SearchBaseWrapper searchBaseWrapper) throws Exception {
        try {
            return bulkDisbursementsManager.loadBulkDisbursementModelList(searchBaseWrapper);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public void updateIsApprovedForBatch(String batchNumber) throws Exception {
        try {
            bulkDisbursementsManager.updateIsApprovedForBatch(batchNumber);
        }
   		 catch(Exception ex){
        }
    }

    @Override
    public int updateDisbursementFileProcessingStatus(Long fileInfoId, String processingStatus) {
        return bulkDisbursementsManager.updateDisbursementFileProcessingStatus(fileInfoId, processingStatus);
    }
}
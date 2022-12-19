package com.inov8.microbank.schedulers;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.LedgerModel;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.commissionmodule.CommissionTransactionViewModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.transactionreversal.ManualReversalVO;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.safrepo.WalletSafRepoDAO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.facade.portal.commissionmodule.CommissionTransactionViewFacade;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.AccountToAccountVO;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualReversalManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.ola.server.service.ledger.LedgerManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class WalletToWalletTransactionScheduler {
    private static final Logger LOGGER = Logger.getLogger(WalletToWalletTransactionScheduler.class);

    private WalletSafRepoDAO walletSafRepoDAO;
    private CommonCommandManager commonCommandManager;
    private CommandManager commandManager;
    private TransactionCodeModel transactionCodeModel = null;
    private TransactionModel transactionModel;
    private TransactionDetailMasterModel transactionDetailMasterModel = null;
    private SmartMoneyAccountModel olaSmartMoneyAccountModel;
    CommissionWrapper commissionWrapper;
    private ManualReversalManager manualReversalFacade;
    protected SettlementManager settlementManager;
    private CommissionManager commissionManager;
    private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
    private TransactionDetailMasterManager transactionDetailMasterManager;
    private CommissionTransactionViewFacade commissionTransactionViewFacade;
    private PortalOlaFacade portalOlaFacade;
    private ManualAdjustmentManager manualAdjustmentManager;

    private MessageSource messageSource;
    protected TransactionModuleManager txManager;

    private FinancialIntegrationManager financialIntegrationManager;


    public void init() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("WalletToWalletTransactionScheduler init");

        LOGGER.info("*********** Executing WalletToWalletTransactionScheduler ***********");
        List<WalletSafRepoModel> toBeProcessedList = null;

        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
            Date currentDate = new Date();

            toBeProcessedList = walletSafRepoDAO.loadAllToBeProcessedRecords();
            LOGGER.info("Total records to be processed " + toBeProcessedList.size());

            if (toBeProcessedList.size() != 0) {

                for (WalletSafRepoModel model : toBeProcessedList) {

                    LOGGER.info("Scheduler for reciever mobile number: " + model.getReceiverMobileNumber() + " running");

//                    LOGGER.info("Checking If any Reversal Exists");
//                    boolean valid = validateTransactionByTrxId(toBeProcessedList.get(0).getTransactionCode());

//                    if(valid){
//                        model.setIsComplete(1L);
//                        model.setTransactionStatus("Reverse Completed");
//                        walletSafRepoDAO.updateWalletSafRepo(model);
//                        throw new Exception("Transaction is already processed:"+toBeProcessedList.get(0).getTransactionCode());
//                    }

                    int daysToBePassed = Days.daysBetween(new DateTime(model.getCreatedOn().getTime()), new DateTime(currentDate.getTime())).getDays();

                    AppUserModel senderAppUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(model.getSenderMobileNumber());

                    LOGGER.info("Checking sender mobile number");
                    if (!checkActiveUser(senderAppUserModel)) {
                        throw new Exception("Sender Account is in invalid State");
                    }
                    LOGGER.info("End checking sender mobile number");

                    ThreadLocalAppUser.setAppUserModel(senderAppUserModel);
                    olaSmartMoneyAccountModel = getSmartMoneyAccountModel(senderAppUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                    LOGGER.info("Checking receiver mobile number");
                    AppUserModel appUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(model.getReceiverMobileNumber());
                    if (!checkActiveUser(appUserModel)) {
                        throw new Exception("Receiver Account is in invalid State");
                    }
                    LOGGER.info("End Checking receiver mobile number");

                    if (appUserModel != null) {

                        UserDeviceAccountsModel userDeviceAccountsModel = this.getCommonCommandManager().loadUserDeviceAccountByUserId(appUserModel.getUsername());

                        SmartMoneyAccountModel receiverSMA = getSmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                        BaseWrapper bWrapper = new BaseWrapperImpl();

                        bWrapper.setBasePersistableModel(new TransactionCodeModel(model.getTransactionCode()));
                        bWrapper = commonCommandManager.loadTransactionCodeByCode(bWrapper);
                        transactionCodeModel = ((TransactionCodeModel) (bWrapper.getBasePersistableModel()));

                        transactionModel = new TransactionModel();

                        transactionDetailMasterModel = new TransactionDetailMasterModel();
                        transactionDetailMasterModel.setTransactionCode(model.getTransactionCode());

                        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                        searchBaseWrapper.setBasePersistableModel(transactionDetailMasterModel);

                        commonCommandManager.loadTransactionDetailMaster(searchBaseWrapper);

                        transactionDetailMasterModel = (TransactionDetailMasterModel) searchBaseWrapper.getBasePersistableModel();

                        CustomerModel customerModel = new CustomerModel();
                        if(appUserModel.getCustomerId() != null) {
                            customerModel = commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());

                            ProductModel productModel = new ProductModel();
                            Long productId = ProductConstantsInterface.ACT_TO_ACT_CI;
                            if (productModel == null || (productModel != null && productModel.getProductId() == null)) {
                                BaseWrapper baseWrapper1 = new BaseWrapperImpl();
                                productModel = new ProductModel();
                                productModel.setProductId(productId);
                                baseWrapper1.setBasePersistableModel(productModel);
                                baseWrapper1 = commandManager.getCommonCommandManager().loadProduct(baseWrapper1);
                                productModel = (ProductModel) baseWrapper1.getBasePersistableModel();
                            }

                            WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

                            workFlowWrapper.setProductModel(productModel);
                            transactionModel.setTransactionAmount(model.getTransactionAmount());
                            transactionModel.setTransactionId(transactionDetailMasterModel.getTransactionId());
                            workFlowWrapper.setTransactionModel(transactionModel);

                            SegmentModel segmentModel = new SegmentModel();
                            segmentModel.setSegmentId(customerModel.getSegmentId());
                            workFlowWrapper.setSegmentModel(segmentModel);

                            workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);

//                        CommissionTransactionViewModel commissionTransactionViewModel = new CommissionTransactionViewModel();
//                        commissionTransactionViewModel.setTransactionId(model.getTransactionCode());
//                        List<CommissionTransactionViewModel> list = this.commissionTransactionViewFacade.searchCommissionTransactionView(commissionTransactionViewModel);
//                        workFlowWrapper.setCommissionAmountsHolder(list);

                            CommissionAmountsHolder commissionAmountsHolder = commissionManager.loadCommissionDetailsUnsettled(workFlowWrapper.getTransactionDetailMasterModel().getTransactionDetailId());
                            workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);

                            this.commissionManager.makeAgent2CommissionSettlement(workFlowWrapper);

                            workFlowWrapper.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
                            workFlowWrapper.setReceivingSmartMoneyAccountModel(receiverSMA);
                            workFlowWrapper.setTransactionCodeModel(this.transactionCodeModel);
                            workFlowWrapper.setTransactionModel(transactionModel);
                            workFlowWrapper.setCustomerModel(customerModel);
                            workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
                            workFlowWrapper.setProductModel(productModel);
//                    workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);

                            workFlowWrapper.getTransactionModel().setTotalAmount(model.getTotalAmount());

                            BaseWrapper baseWrapper = new BaseWrapperImpl();
                            baseWrapper.setBasePersistableModel(workFlowWrapper.getSmartMoneyAccountModel());


                            OLAVeriflyFinancialInstitutionImpl olaFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

                            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                            switchWrapper.setWorkFlowWrapper(workFlowWrapper);
                            switchWrapper.setBasePersistableModel(workFlowWrapper.getSmartMoneyAccountModel());

                            switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
                            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                            switchWrapper = olaFinancialInstitution.walletToWalletTransactionTransferFunds(switchWrapper);

                            loadAndForwardAccountToQueue(workFlowWrapper);

                            workFlowWrapper.setOLASwitchWrapper(switchWrapper);

                            settlementManager.prepareDataForDayEndSettlement(workFlowWrapper);

                            model.setIsComplete(1L);
                            model.setTransactionStatus("Complete");
                            walletSafRepoDAO.updateWalletSafRepo(model);
//
//                        settlementManager.updateCommissionTransactionSettled(workFlowWrapper.getTransactionDetailMasterModel().getTransactionDetailId());

                            transactionDetailMasterModel.setRecipientMfsId(userDeviceAccountsModel.getUserId());
                            transactionDetailMasterModel.setRecipientCnic(appUserModel.getNic());
                            transactionDetailMasterModel.setProcessingStatusName("Completed");
                            this.getCommonCommandManager().saveTransactionDetailMasterModel(transactionDetailMasterModel);
                        }
                        else {
                            LOGGER.info("Mobile Number already exist as Retailer/Walk-in: " + appUserModel.getMobileNo());
                            LOGGER.info("Since Mobile Number is retailer, initiating reversal");
                            //new impl start
                            Integer adjustmentType = 1;
                            String trxCode = model.getTransactionCode();

                            ManualReversalVO manualReversalVO = new ManualReversalVO();
                            manualReversalVO.setTransactionCode(trxCode);
                            manualReversalVO.setAdjustmentType(adjustmentType);
                            boolean isReversal = (adjustmentType != null && adjustmentType == 1) ? true : false;

                            if (!StringUtil.isNullOrEmpty(trxCode)) {
                                LOGGER.info("Going to load details against transactionCode:" + trxCode);

                                TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
                                transactionDetailMasterModel.setTransactionCode(trxCode);
                                BaseWrapper baseWrapper = new BaseWrapperImpl();
                                baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
                                this.transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
                                transactionDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
                                if (transactionDetailMasterModel == null || transactionDetailMasterModel.getTransactionId() == null) {
                                    throw new Exception("Invalid transaction ID");
                                }
                                prepareVOFromMasterModel(manualReversalVO, transactionDetailMasterModel);

                                CommissionTransactionViewModel commissionTransactionViewModel = new CommissionTransactionViewModel();
                                commissionTransactionViewModel.setTransactionId(trxCode);
                                List<CommissionTransactionViewModel> list = this.commissionTransactionViewFacade.searchCommissionTransactionView(commissionTransactionViewModel);
                                manualReversalVO.setCommissionTransactionList(list);

                                BbStatementAllViewModel bbStatementAllViewModel = new BbStatementAllViewModel();
                                bbStatementAllViewModel.setTransactionCode(trxCode);
                                LinkedHashMap<String, SortingOrder> sortingMap = new LinkedHashMap<String, SortingOrder>();
                                sortingMap.put("category", SortingOrder.ASC);
                                sortingMap.put("ledgerId", SortingOrder.ASC);
                                SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
                                sbWrapper.setSortingOrderMap(sortingMap);
                                sbWrapper.setBasePersistableModel(bbStatementAllViewModel);
                                List<BbStatementAllViewModel> settlementBBStatementList = portalOlaFacade.searchBbStatementAllView(sbWrapper).getResultsetList();
                                if (null != settlementBBStatementList && settlementBBStatementList.size() > 0) {
                                    for (BbStatementAllViewModel modelInList : settlementBBStatementList) {
                                        modelInList.setAccountNumber(com.inov8.ola.util.EncryptionUtil.decryptAccountNo(modelInList.getAccountNumber()));
                                        boolean isDebit = (modelInList.getDebitAmount() != null && modelInList.getDebitAmount() > 0) ? true : false;
                                        if (isDebit) {
                                            modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getDebitAmount()) : "");
                                            modelInList.setCreditAmount(null);
                                            modelInList.setTransactionType(isReversal ? TransactionTypeConstants.CREDIT : null); // reverse the action for reversal only
                                        } else {
                                            modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getCreditAmount()) : "");
                                            modelInList.setDebitAmount(null);
                                            modelInList.setTransactionType(isReversal ? TransactionTypeConstants.DEBIT : null); // reverse the action for reversal only
                                        }
                                    }
                                }
                                manualReversalVO.setFundTransferEntryList(settlementBBStatementList);
                            }

                            boolean validReversal = this.validateManualReversalForm(manualReversalVO);

                            if (validReversal) {
                                if (manualReversalVO.getIsReversed() == null) {
                                    this.prepareInitiatorDetails(manualReversalVO);
                                    manualReversalFacade.makeReversal(manualReversalVO);
                                    //new impl end

                                    model.setIsComplete(1L);
                                    model.setTransactionStatus("Reverse Completed");
                                    walletSafRepoDAO.updateWalletSafRepo(model);
                                } else if (manualReversalVO.getIsReversed().equals("0")) {
                                    this.prepareInitiatorDetails(manualReversalVO);
                                    manualReversalFacade.makeReversal(manualReversalVO);
                                    //new impl end

                                    model.setIsComplete(1L);
                                    model.setTransactionStatus("Reverse Completed");
                                    walletSafRepoDAO.updateWalletSafRepo(model);
                                } else {
                                    LOGGER.info("Already Processed Trx ID, hence marking reversed in WalletSafRepo :" + trxCode);
                                    model.setIsComplete(1L);
                                    model.setTransactionStatus("Reverse Completed");
                                    walletSafRepoDAO.updateWalletSafRepo(model);
                                }
                            }
                        }

                    } else if (appUserModel == null && daysToBePassed <= 3) {
                        LOGGER.info("*********** Days Passed *********** " + daysToBePassed + " Day/Days");

                    } else {
                        LOGGER.info("*********** Executing Reversal WalletToWalletTransactionScheduler Scheduler ***********");
                        //new impl start
                        Integer adjustmentType = 1;
                        String trxCode = model.getTransactionCode();

                        ManualReversalVO manualReversalVO = new ManualReversalVO();
                        manualReversalVO.setTransactionCode(trxCode);
                        manualReversalVO.setAdjustmentType(adjustmentType);
                        boolean isReversal = (adjustmentType != null && adjustmentType == 1) ? true : false;

                        if (!StringUtil.isNullOrEmpty(trxCode)) {
                            LOGGER.info("Going to load details against transactionCode:" + trxCode);

                            TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
                            transactionDetailMasterModel.setTransactionCode(trxCode);
                            BaseWrapper baseWrapper = new BaseWrapperImpl();
                            baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
                            this.transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
                            transactionDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
                            if (transactionDetailMasterModel == null || transactionDetailMasterModel.getTransactionId() == null) {
                                throw new Exception("Invalid transaction ID");
                            }
                            prepareVOFromMasterModel(manualReversalVO, transactionDetailMasterModel);

                            CommissionTransactionViewModel commissionTransactionViewModel = new CommissionTransactionViewModel();
                            commissionTransactionViewModel.setTransactionId(trxCode);
                            List<CommissionTransactionViewModel> list = this.commissionTransactionViewFacade.searchCommissionTransactionView(commissionTransactionViewModel);
                            manualReversalVO.setCommissionTransactionList(list);

                            BbStatementAllViewModel bbStatementAllViewModel = new BbStatementAllViewModel();
                            bbStatementAllViewModel.setTransactionCode(trxCode);
                            LinkedHashMap<String, SortingOrder> sortingMap = new LinkedHashMap<String, SortingOrder>();
                            sortingMap.put("category", SortingOrder.ASC);
                            sortingMap.put("ledgerId", SortingOrder.ASC);
                            SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
                            sbWrapper.setSortingOrderMap(sortingMap);
                            sbWrapper.setBasePersistableModel(bbStatementAllViewModel);
                            List<BbStatementAllViewModel> settlementBBStatementList = portalOlaFacade.searchBbStatementAllView(sbWrapper).getResultsetList();
                            if (null != settlementBBStatementList && settlementBBStatementList.size() > 0) {
                                for (BbStatementAllViewModel modelInList : settlementBBStatementList) {
                                    modelInList.setAccountNumber(com.inov8.ola.util.EncryptionUtil.decryptAccountNo(modelInList.getAccountNumber()));
                                    boolean isDebit = (modelInList.getDebitAmount() != null && modelInList.getDebitAmount() > 0) ? true : false;
                                    if (isDebit) {
                                        modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getDebitAmount()) : "");
                                        modelInList.setCreditAmount(null);
                                        modelInList.setTransactionType(isReversal ? TransactionTypeConstants.CREDIT : null); // reverse the action for reversal only
                                    } else {
                                        modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getCreditAmount()) : "");
                                        modelInList.setDebitAmount(null);
                                        modelInList.setTransactionType(isReversal ? TransactionTypeConstants.DEBIT : null); // reverse the action for reversal only
                                    }
                                }
                            }
                            manualReversalVO.setFundTransferEntryList(settlementBBStatementList);
                        }

                        boolean validReversal = this.validateManualReversalForm(manualReversalVO);

                        if (validReversal) {
                            if (manualReversalVO.getIsReversed() == null) {
                                this.prepareInitiatorDetails(manualReversalVO);
                                manualReversalFacade.makeReversal(manualReversalVO);
                                //new impl end

                                model.setIsComplete(1L);
                                model.setTransactionStatus("Reverse Completed");
                                walletSafRepoDAO.updateWalletSafRepo(model);
                            } else if (manualReversalVO.getIsReversed().equals("0")) {
                                this.prepareInitiatorDetails(manualReversalVO);
                                manualReversalFacade.makeReversal(manualReversalVO);
                                //new impl end
                                model.setIsComplete(1L);
                                model.setTransactionStatus("Reverse Completed");
                                walletSafRepoDAO.updateWalletSafRepo(model);
                            } else {
                                LOGGER.info("Already Processed Trx ID, hence marking reversed in WalletSafRepo :" + trxCode);
                                model.setIsComplete(1L);
                                model.setTransactionStatus("Reverse Completed");
                                walletSafRepoDAO.updateWalletSafRepo(model);
                            }
                        }
                    }
                }
            }
            else{
                LOGGER.info("*********** toBeProcessedList is Empty ***********");
            }
        }
        catch (Exception ex){
            LOGGER.error("Error while executing WalletToWalletTransactionScheduler.init() :: " + ex.getMessage(), ex);
        }
        LOGGER.info("*********** Finished Executing WalletToWalletTransactionScheduler Scheduler ***********");
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());

    }

    private SmartMoneyAccountModel getSmartMoneyAccountModel(AppUserModel appUserModel , Long paymentModeId) throws CommandException {

        SmartMoneyAccountModel smartMoneyAccountModel = null;

        SmartMoneyAccountModel _smartMoneyAccountModel = new SmartMoneyAccountModel();

        if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

            _smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());

        } else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

            _smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
        }

        _smartMoneyAccountModel.setDeleted(Boolean.FALSE);
        _smartMoneyAccountModel.setActive(Boolean.TRUE);
        _smartMoneyAccountModel.setPaymentModeId(paymentModeId);

        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        wrapper.setBasePersistableModel(_smartMoneyAccountModel);

        try {

            SearchBaseWrapper searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(wrapper);

            if(searchBaseWrapper != null) {

                List<SmartMoneyAccountModel> resultsetList = searchBaseWrapper.getCustomList().getResultsetList();

                if(resultsetList != null && !resultsetList.isEmpty()) {

                    smartMoneyAccountModel = resultsetList.get(0);
                }
            }

        } catch (FrameworkCheckedException e) {

            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }

        LOGGER.debug("Found Smart Money Account "+ smartMoneyAccountModel.getName());

        return smartMoneyAccountModel;
    }

    private void prepareVOFromMasterModel(ManualReversalVO manualReversalVO, TransactionDetailMasterModel transactionDetailMasterModel){

        manualReversalVO.setExclusiveCharges(transactionDetailMasterModel.getExclusiveCharges());
        manualReversalVO.setInclusiveCharges(transactionDetailMasterModel.getInclusiveCharges());
        manualReversalVO.setProductId(transactionDetailMasterModel.getProductId());
        manualReversalVO.setProductName(transactionDetailMasterModel.getProductName());
        manualReversalVO.setSupProcessingStatusId(transactionDetailMasterModel.getSupProcessingStatusId());
        manualReversalVO.setSupProcessingStatusName(transactionDetailMasterModel.getProcessingStatusName());
        manualReversalVO.setTransactionAmount(transactionDetailMasterModel.getTransactionAmount());
        manualReversalVO.setTransactionDate(transactionDetailMasterModel.getCreatedOn());

    }

    private void prepareInitiatorDetails(ManualReversalVO manualReversalVO){
        String firstName = UserUtils.getCurrentUser().getFirstName();
        String lastName = UserUtils.getCurrentUser().getLastName();
        lastName = ((StringUtil.isNullOrEmpty(lastName)) ? "" : " " + lastName);
        Long appUserId = UserUtils.getCurrentUser().getAppUserId();

        manualReversalVO.setInitiatorAppUserId(appUserId);
        manualReversalVO.setInitiatorName(firstName + lastName);
    }

    private boolean validateManualReversalForm(ManualReversalVO manualReversalVO){

        Integer adjustmentType = manualReversalVO.getAdjustmentType();
        if(adjustmentType == null || adjustmentType.intValue() == 0){
            LOGGER.error("Adjustment Type empty");
            return false;
        }

        if(!StringUtil.isNullOrEmpty(manualReversalVO.getTransactionCode())){
            TransactionDetailMasterModel tdmModel = null;
            try {
                tdmModel = this.manualReversalFacade.getTransactionDetailMasterModel(manualReversalVO.getTransactionCode());
            } catch (FrameworkCheckedException e) {
                LOGGER.error("Exception while loading TransactionDetailMasterModel for trx ID:"+manualReversalVO.getTransactionCode());
                return false;
            }
            if(tdmModel == null){
                LOGGER.error("Invalid Trx ID provided:"+manualReversalVO.getTransactionCode());
                return false;
            }

            if(SupplierProcessingStatusConstants.REVERSE_COMPLETED.longValue() == tdmModel.getSupProcessingStatusId() && adjustmentType.intValue() == 1){
                LOGGER.error("Transaction status is already Reverse Completed for trx ID:"+manualReversalVO.getTransactionCode());
                manualReversalVO.setIsReversed("1");
                return false;
            }

            // 08-MAR-2017 check commented on request of JSBL
			/*if(adjustmentType.intValue() == 1 &&
					(tdmModel.getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH ||
						tdmModel.getProductId().longValue() == ProductConstantsInterface.CASH_TRANSFER ||
						tdmModel.getProductId().longValue() == ProductConstantsInterface.BULK_PAYMENT
							) && (tdmModel.getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.COMPLETED ||
							tdmModel.getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.REVERSE_COMPLETED)){

				logger.error("Transaction reversal is not allowed for " + tdmModel.getProductName() +  "  with status completed / reversed-completed");
				super.saveMessage(req,"Transaction reversal is not allowed for " + tdmModel.getProductName() + " with status completed / reversed-completed ");
				return false;
			}*/

        }


        List<BbStatementAllViewModel> entryList = manualReversalVO.getFundTransferEntryList();
        if(entryList==null || entryList.size() == 0){
            LOGGER.error("Entry list is empty");
            return false;
        }

        double totalDebit = 0;
        double totalCredit = 0;
        double fmtAmount = 0;

        for(BbStatementAllViewModel entry: entryList){
            BBAccountsViewModel model = new BBAccountsViewModel();
            if(StringUtil.isNullOrEmpty(entry.getAccountNumber())){
                LOGGER.error("Account Number is empty");
                return false;
            }else{
                try {
                    model.setAccountNumber(com.inov8.ola.util.EncryptionUtil.encryptWithDES(entry.getAccountNumber()));
                    model = manualAdjustmentManager.getBBAccountsViewModel(model);
                    if(model !=null){
                        if(model.getTitle() == null || model.getTitle().equals("null") || model.getTitle().trim().equals("")){
                            LOGGER.error("Account does not exist against the  account number : " + entry.getAccountNumber());
                            return false;
                        }else if(model.getAccountTypeId() != null && model.getAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT){
                            LOGGER.info("Settlement Acc Type loaded against accNumber:"+entry.getAccountNumber()+" ... so SKIPPING account status/active check");
                        }else{
                            if( model.getIsActive()== null || !model.getIsActive() ){
                                LOGGER.error("Account is not active against the account number : " + entry.getAccountNumber());
                                return false;
                            }
                            if(model.getStatusId() == null || model.getStatusId().longValue() != 1){
                                LOGGER.error("Account Status is not active against the account number : " + entry.getAccountNumber() );
                                return false;
                            }
                            if (model.getAcState() != null && model.getAcState().equalsIgnoreCase("CLOSED")){
                                LOGGER.error("Account is CLOSED against the account number : " + entry.getAccountNumber());
                                return false;
                            }
                        }

                    }

                    else{
                        LOGGER.error(entry.getAccountNumber() + "Account Number does not exist");
                        return false;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(StringUtil.isNullOrEmpty(entry.getAmountStr())){
                LOGGER.error("Amount is empty");
                return false;
            }else{
                try{
                    entry.setAmount(parseAmount(entry.getAmountStr()));
                }catch(NumberFormatException e){
                    LOGGER.error("Manual Reversal: Invalid Amount:"+entry.getAmountStr(),e);
                    return false;
                }
                fmtAmount = formatAmount(entry.getAmount());
                entry.setAmount(fmtAmount);
                if(fmtAmount < 0.01D){
                    LOGGER.error("Manual Reversal: Amount cannot be less than 0.01");
                    return false;
                }
                if(fmtAmount > 999999999999.99D){
                    LOGGER.error("Manual Reversal: Amount cannot be greater than 999999999999.99");
                    return false;
                }
            }
            if(entry.getTransactionType() == null){
                LOGGER.error("Transaction type is empty");
                return false;
            }

            if(entry.getTransactionType().longValue() == TransactionTypeConstants.DEBIT){
                totalDebit += fmtAmount;
            }else{
                totalCredit += fmtAmount;
            }
        }

        if(totalDebit == 0){
            LOGGER.error("Total debit amount is 0");
            return false;
        }else if(totalCredit == 0){
            LOGGER.error("Total credit amount is 0");
            return false;
        }else if(formatAmount(totalDebit).longValue() != formatAmount(totalCredit).longValue()){
            LOGGER.error("Total debit and credit amount is not equal... Total Debit:"+totalDebit+" Total Credit:"+totalCredit);
            return false;
        }

        manualReversalVO.setTotalAmount(totalDebit);

        return true;
    }

//    private boolean validateTransactionByTrxId(String transactionId) {
//        TransactionDetailMasterModel tdmModel = null;
//        try {
//            tdmModel = this.manualReversalFacade.getTransactionDetailMasterModelByTrxIdAndStatusId(transactionId,
//                    SupplierProcessingStatusConstants.REVERSE_COMPLETED);
//            if(tdmModel != null)
//            {
//                return true;
//            }
//        } catch (FrameworkCheckedException e) {
//            LOGGER.error("Exception while loading TransactionDetailMasterModel for trx ID:"+transactionId);
//        }
//        return false;
//    }

        private Double parseAmount(String amountStr){
        if(StringUtil.isNullOrEmpty(amountStr)){
            return 0.0D;
        }else{
            return Double.parseDouble(amountStr);
        }
    }

    private Double formatAmount(Double amount){
        if(amount == null){
            return 0.0D;
        }else{
            String amt = Formatter.formatDouble(amount);
            return Double.parseDouble(amt);
        }
    }

    private Boolean checkActiveUser(AppUserModel appUserModel) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        if (appUserModel != null) {
            LOGGER.info("[walletToWallet.checkActiveAppUser] checking activation status for AppUserID:" + appUserModel.getAppUserId());
            if (appUserModel.getCustomerId() != null) {
                if (appUserModel.getRegistrationStateId() == null
                        || (appUserModel.getRegistrationStateId() != null && (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REJECTED)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DECLINE)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT)))) {
                    LOGGER.info("[walletToWallet.checkActiveAppUser] customer is in Registration state :" + appUserModel.getRegistrationStateId());
                    return true;
                }
            }
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                BaseWrapper bWrapper = new BaseWrapperImpl();
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = this.getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                UserDeviceAccountsModel uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();

                if (uda.getAccountLocked()) {
                    return true;
                }

                if (!uda.getAccountEnabled()) {
                    return true;
                }
        }
        else {
            return true;
        }

        return true;
    }

        public void setWalletSafRepoDAO(WalletSafRepoDAO walletSafRepoDAO) {
        this.walletSafRepoDAO = walletSafRepoDAO;
    }

    public CommonCommandManager getCommonCommandManager() {
        return commonCommandManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }
    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager){
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setManualReversalFacade(ManualReversalManager manualReversalFacade) {
        this.manualReversalFacade = manualReversalFacade;
    }
    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

    private void loadAndForwardAccountToQueue(final WorkFlowWrapper workFlowWrapper) throws InterruptedException {
        creditAccountQueingPreProcessor.startProcessing(workFlowWrapper);
    }

    public void setCreditAccountQueingPreProcessor(CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
        this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
    }

    public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

    public void setCommissionTransactionViewFacade(CommissionTransactionViewFacade commissionTransactionViewFacade) {
        this.commissionTransactionViewFacade = commissionTransactionViewFacade;
    }

    public void setPortalOlaFacade(PortalOlaFacade portalOlaFacade) {
        this.portalOlaFacade = portalOlaFacade;
    }

    public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
        this.manualAdjustmentManager = manualAdjustmentManager;
    }
}

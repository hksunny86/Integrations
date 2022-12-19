package com.inov8.microbank.tax.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.TaxRegimeModelVO;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.portal.taxregimemodule.TaxRegimeDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.customermodule.CustomerManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.tax.common.TaxConstantsInterface;
import com.inov8.microbank.tax.dao.*;
import com.inov8.microbank.tax.model.*;
import com.inov8.microbank.tax.vo.FedRuleManagementVO;
import com.inov8.microbank.tax.vo.WHTConfigVo;
import com.inov8.microbank.tax.vo.WHTConfigWrapper;
import com.inov8.microbank.tax.vo.WHTExemptionVO;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

public class TaxManagerImpl implements TaxManager {
    private FEDRuleDAO fedRuleDAO;
    //private FEDRuleViewDAO fedRuleViewDAO;
    private WHTConfigDAO whtConfigDAO;
    private WHTExemptionDAO whtExemptionDAO;
    private FilerRateConfigDAO filerRateConfigDAO;
    private TaxRegimeDAO taxRegimeDAO;
    private WHTExemptionListViewDAO whtExemptionListViewDAO;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private AppUserManager appUserManager;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private TransactionModuleManager transactionManager;
    private ActionLogManager actionLogManager;
    private TransactionDetailMasterManager transactionDetailMasterManager;
    //private ProductManager productManager;
    private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
    protected SettlementManager settlementManager;
    private CustomerManager customerManager;
    private DailyWhtDeductionManager dailyWhtDeductionManager;
    private CustomerDAO customerDAO;
    private RetailerContactDAO retailerContactDAO;
    private FinancialIntegrationManager financialIntegrationManager;

    private WHTDeductionSchedularStatusDAO whtDeductionSchedularStatusDAO;
    private DailyWhtDeductionDAO dailyWhtDeductionDAO;

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public FEDRuleViewModel loadFEDRuleViewModel(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        ProductModel productModel = workFlowWrapper.getProductModel();
        FEDRuleModel fedRuleModel = new FEDRuleModel();
        fedRuleModel.setServiceId(productModel.getServiceId());
        fedRuleModel.setActive(true);
        FEDRuleModel applicableFedRuleModel = null;
        TaxRegimeModel senderTaxRegimeModel = workFlowWrapper.getTaxRegimeModel();
        //load all active rules based on service id
        List<FEDRuleModel> fedRuleModelList = fedRuleDAO.loadAllActiveRulesByServiceId(fedRuleModel);
        if (!fedRuleModelList.isEmpty()) {
            // service=Selected , Product:Selected, Tax_regime:Selected
            for (FEDRuleModel ruleModel : fedRuleModelList) {
                if (null != ruleModel && null != ruleModel.getTaxRegimeId() && 0L != ruleModel.getTaxRegimeId()) {
                    if (ruleModel.getProductId() != null && ruleModel.getProductId().longValue() == productModel.getProductId().longValue() && ruleModel.getTaxRegimeId().longValue() == senderTaxRegimeModel.getTaxRegimeId().longValue()) {
                        applicableFedRuleModel = ruleModel;
                        break;
                    }
                }
            }
            if (applicableFedRuleModel == null) { // service=Selected , Product:Null, Tax_regime:Selected
                for (FEDRuleModel ruleModel : fedRuleModelList) {
                    if (null != ruleModel && null != ruleModel.getTaxRegimeId() && 0L != ruleModel.getTaxRegimeId()) {
                        if (ruleModel.getServiceId().longValue() == productModel.getServiceId().longValue()
                                && ruleModel.getProductId() == 0L && ruleModel.getTaxRegimeId().longValue() == senderTaxRegimeModel.getTaxRegimeId().longValue()) {
                            applicableFedRuleModel = ruleModel;
                            break;
                        }
                    }
                }
            }
            if (applicableFedRuleModel == null) { // service=Selected , Product:Selected, Tax_regime:Null
                for (FEDRuleModel ruleModel : fedRuleModelList) {
                    if (null != ruleModel && null != ruleModel.getServiceId() && ruleModel.getServiceId() != 0L && ruleModel.getServiceId().longValue() == productModel.getServiceId().longValue()) {
                        if (ruleModel.getProductId() != 0L && ruleModel.getProductId().longValue() == productModel.getProductId()) {
                            applicableFedRuleModel = ruleModel;
                            break;
                        }
                    }

                }
            }
            if (applicableFedRuleModel == null) { // service=Selected , Product:Null, Tax_regime:Null
                for (FEDRuleModel ruleModel : fedRuleModelList) {
                    if (null != ruleModel && 0L != ruleModel.getServiceId() && ruleModel.getServiceId().longValue() == productModel.getServiceId().longValue()) {
                        if (ruleModel.getProductId() == 0L && ruleModel.getTaxRegimeId() == 0L) {
                            applicableFedRuleModel = ruleModel;
                            break;
                        }
                    }

                }
            }
        }
        FEDRuleViewModel fedRuleViewModel = new FEDRuleViewModel();
        fedRuleViewModel.setServiceId(productModel.getServiceId());
        fedRuleViewModel.setProductId(productModel.getProductId());
        fedRuleViewModel.setInclusive(Boolean.TRUE); //Bug ID #11209. Make Inclusive FED as Default
        //TaxRegimeModel senderTaxRegimeModel = workFlowWrapper.getTaxRegimeModel();
        if (applicableFedRuleModel != null) {
            fedRuleViewModel.setActive(applicableFedRuleModel.getActive());
            fedRuleViewModel.setInclusive(applicableFedRuleModel.getInclusive());
            //customized tax regime
            TaxRegimeModel customizedTaxRegimeModel = applicableFedRuleModel.getTaxRegimeModel();
            // if no customized tax regime is available, then user sender tax regime
            if (customizedTaxRegimeModel == null || (customizedTaxRegimeModel != null && customizedTaxRegimeModel.getTaxRegimeId().longValue() == 0L)) {
                fedRuleViewModel.setRate(applicableFedRuleModel.getRate());
                //				if(senderTaxRegimeModel != null) {
                //					fedRuleViewModel.setRate(senderTaxRegimeModel.getFed());
                //					fedRuleViewModel.setTaxRegimeId(senderTaxRegimeModel.getTaxRegimeId());
                //				}
            } else {
                if (senderTaxRegimeModel != null && customizedTaxRegimeModel.getTaxRegimeId().longValue() == senderTaxRegimeModel.getTaxRegimeId()) {
                    // check if FED rate is given along with tax regime
                    fedRuleViewModel.setTaxRegimeId(customizedTaxRegimeModel.getTaxRegimeId());
                    Double fed = CommonUtils.getDoubleOrDefaultValue(applicableFedRuleModel.getRate());
                    /*if( fed > 0.0) {*/
                    fedRuleViewModel.setRate(fed);
                    //}

                    //else {
                    //use given tax regime to get default FED rate
                    //	fedRuleViewModel.setRate(senderTaxRegimeModel.getFed());
                    //}
                } else if (senderTaxRegimeModel != null) {
                    fedRuleViewModel.setTaxRegimeId(senderTaxRegimeModel.getTaxRegimeId());
                    fedRuleViewModel.setRate(senderTaxRegimeModel.getFed());
                }
            }
        } else {
            if (senderTaxRegimeModel != null) {
                fedRuleViewModel.setRate(senderTaxRegimeModel.getFed());
                fedRuleViewModel.setTaxRegimeId(senderTaxRegimeModel.getTaxRegimeId());
            }
        }
        return fedRuleViewModel;
    }

    @Override
    public WHTConfigModel loadWHTConfigModel(Long whtConfigModelId) {
        return whtConfigDAO.findByPrimaryKey(whtConfigModelId);
    }

    @Override
    public List<WHTExemptionModel> loadWHTExemptionByAppUserId(Long appUserId) {
        return whtExemptionDAO.loadWHTExemptionByAppUserId(appUserId);
    }

    @Override
    public void saveOrUpdateWhtExemption(WHTExemptionModel whtExemptionModel) {
        Date now = new Date();
        whtExemptionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        whtExemptionModel.setUpdatedOn(now);
        whtExemptionModel.setVersionNo(1);
        whtExemptionDAO.saveOrUpdate(whtExemptionModel);
    }

    @Override
    public BaseWrapper saveUpdateWhtExemptionModelsWithAuthorization(BaseWrapper wrapper) throws Exception {

        wrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.WHT_CONFIG_USECASE_ID);
        wrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, UserUtils.getCurrentUser().getUsername());
        wrapper.putObject(PortalConstants.KEY_CREATED_ON, new Date());
        wrapper.putObject(PortalConstants.KEY_APP_USER_ID, UserUtils.getCurrentUser().getAppUserId());

        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(wrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        WHTExemptionVO whtExemptionVO = (WHTExemptionVO) wrapper.getBasePersistableModel();
        WHTExemptionModel whtExemptionModel = new WHTExemptionModel();
        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_TIME_FORMAT);
        UserDeviceAccountsModel udaModel = userDeviceAccountsManager.loadUserDeviceAccountByUserId(whtExemptionVO.getUserId().toString());

        if (whtExemptionVO.getWhtExemptionId() != null) {        //case of update
            whtExemptionModel = this.loadWhtExemptionModelByExemptionId(whtExemptionVO.getWhtExemptionId());
            whtExemptionModel.setStartDate(whtExemptionVO.getStartDate());
            whtExemptionModel.setEndDate(whtExemptionVO.getEndDate());
            whtExemptionModel.setUpdatedOn(new Date());
            whtExemptionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        } else {            //case of create
            whtExemptionModel.setStartDate(whtExemptionVO.getStartDate());
            whtExemptionModel.setEndDate(whtExemptionVO.getEndDate());
            whtExemptionModel.setAppUserModel(udaModel.getAppUserIdAppUserModel());
            whtExemptionModel.setActive(Boolean.TRUE);
            whtExemptionModel.setCreatedOn(new Date());
            whtExemptionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());

        }

        this.saveOrUpdateWhtExemption(whtExemptionModel);
        return wrapper;
    }

    @Override
    public WHTExemptionModel loadWhtExemptionModelByExemptionId(Long exemptionId) {
        return whtExemptionDAO.findByPrimaryKey(exemptionId);
    }

    @Override
    public WHTConfigModel loadCommissionWHTConfigModel(Long appUserId) {
        WHTExemptionModel whtExemptionModel = null;
        boolean noExemptionAvailable = true;

        if (appUserId != null) {
            whtExemptionModel = whtExemptionDAO.loadValidWHTExemption(appUserId);
            if (whtExemptionModel != null)
                noExemptionAvailable = false;
        }

        if (noExemptionAvailable) {
            return loadWHTConfigModel(TaxConstantsInterface.WHT_CONFIG_COMMISSION_ID);
        }

        return null;
    }

    @Override
    public SearchBaseWrapper searchWhtExemptionModel(SearchBaseWrapper searchBaseWrapper) {

        CustomList<WHTExemptionListViewModel>
                list = this.whtExemptionListViewDAO.findByExample((WHTExemptionListViewModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModelList());

        //CustomList<WHTExemptionListViewModel> list = whtExemptionListViewDAO.findByExample(searchBaseWrapper.getBasePersistableModel(),searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper);

        if (list != null) {
            searchBaseWrapper.setCustomList(list);
        }
        return searchBaseWrapper;

    }

    @Override
    public WHTExemptionModel loadValidWHTExemption(Long appUserId) {
        WHTExemptionModel whtExemptionModel = whtExemptionDAO.loadValidWHTExemptionForScheduler(appUserId);
        return whtExemptionModel;
    }

    @Override
    public SearchBaseWrapper searchTaxRegimeDefaultFEDRates(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<TaxRegimeModel> customList = taxRegimeDAO.findAll(searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    /*@Override
    public CustomList<TaxRegimeModel> searchActiveTaxRegimeDefaultFEDRates() throws FrameworkCheckedException {
        TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
        taxRegimeModel.setIsActive(true);
        return taxRegimeDAO.findByExample(taxRegimeModel);
    }
*/
    @Override
    public TaxRegimeModel searchTaxRegimeById(Long taxRegimeId) throws FrameworkCheckedException {
        return taxRegimeDAO.findByPrimaryKey(taxRegimeId);
    }

    @Override
    public BaseWrapper createTaxRegimeFEDRatesWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        TaxRegimeModelVO taxRegimeModelVO = (TaxRegimeModelVO) baseWrapper.getBasePersistableModel();
        taxRegimeDAO.saveOrUpdate(getTaxRegimeModel(taxRegimeModelVO));

        actionLogModel.setCustomField11(taxRegimeModelVO.getName());
        this.actionLogManager.completeActionLog(actionLogModel);

        return baseWrapper;
    }

    @Override
    public int getAssociatedUsersWithTaxRegime(Long taxRegimeId) throws FrameworkCheckedException {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setTaxRegimeId(taxRegimeId);
        ExampleConfigHolderModel exampleConfigHolder = new ExampleConfigHolderModel();
        exampleConfigHolder.setEnableLike(false);
        exampleConfigHolder.setMatchMode(MatchMode.EXACT);
        int customerCount = customerDAO.countByExample(customerModel, exampleConfigHolder);

        RetailerContactModel retailerContactModel = new RetailerContactModel();
        retailerContactModel.setTaxRegimeId(taxRegimeId);
        int retailerCount = retailerContactDAO.countByExample(retailerContactModel, exampleConfigHolder);

        return customerCount + retailerCount;
    }

    @Override
    public List<FEDRuleModel> loadFEDRuleModelList() throws FrameworkCheckedException {
        return fedRuleDAO.findAll().getResultsetList();
    }

    public List<WorkFlowWrapper> prepareDailyWHTEntries(List<DateWiseUserWHTAmountViewModel> modelList, Map<Long, WHTConfigModel> configModelMap) throws Exception {

        logger.info("Start of TaxManagerImpl.prepareDailyWHTEntries()");

        if (CollectionUtils.isEmpty(modelList)) {
            whtDeductionSchedularStatusDAO.updateWhtSchedulerStatus();
            return null;
        } else {
            List<WorkFlowWrapper> saveDailyWHTEntriesList = new ArrayList(modelList.size());
            WorkFlowWrapper wrapper = null;

            for (DateWiseUserWHTAmountViewModel model : modelList) {
                logger.info(model.getAppUserId() + " : " + model.getTransactionAmount() + " : " + model.getMobileNo() + " : " + model.getWhtConfigId());
                wrapper = new WorkFlowWrapperImpl();

                try {
                    WHTConfigModel whtConfigModel = new WHTConfigModel();
                    whtConfigModel = configModelMap.get(model.getWhtConfigId());
                    AppUserModel appUserModel = new AppUserModel();
                    appUserModel.setAppUserId(model.getAppUserId());
                    //AppUserModel appUserModel = appUserManager.loadAppUser(model.getAppUserId());

                    Boolean filer = appUserManager.isAppUserFiler(model.getAppUserId());
                    Double whtRate = filer ? whtConfigModel.getFilerRate() : whtConfigModel.getNonFilerRate();
                    Double applicableAmount = model.getTransactionAmount();
                    Double whtAmount = calculateWhtAmount(whtRate, applicableAmount);

                    wrapper.setIsFiler(filer);
                    if (filer) {
                        wrapper.setFilerRate(whtRate);
                    } else {
                        wrapper.setNonFilerRate(whtRate);
                    }

                    wrapper.setSumAmount(model.getTransactionAmount());
                    wrapper.setAppUserModel(appUserModel);
                    wrapper.setTransactionAmount(whtAmount);
                    wrapper.setTotalAmount(whtAmount);
                    wrapper.putObject("WHT_CONFIG_ID", model.getWhtConfigId());
                    wrapper.setTransactionCodeModel(new TransactionCodeModel());
                    wrapper.setWHTDedTransactionDate(model.getTransactionDate());

                    saveDailyWHTEntriesList.add(wrapper);


                } catch (Exception e) {
                    logger.error("TaxManagerImpl.prepareDailyWHTEntries() :Unable to process WHT Entry for App User Id : "
                            + model.getAppUserId() + " Exception Message : " + e.getMessage());
                    throw e;
                }
            }
            //saveDailyWHTDeductionModels(saveDailyWHTEntriesList);
            return saveDailyWHTEntriesList;
        }

    }

    private double calculateWhtAmount(double commissionPercentage, double transactionAmount) {
        return CommonUtils.roundTwoDecimals((transactionAmount * commissionPercentage) / 100);
    }

    public List<DailyWhtDeductionModel> loadUnsettledWithholdingDeductionList(Date toDate) throws Exception {
        return dailyWhtDeductionManager.loadUnsettledWhtDeductionList(toDate);
    }

    public WorkFlowWrapper makeDebitForWHT(DailyWhtDeductionModel dailyWhtDeductionModel) throws Exception {

        logger.info("Start of TaxManagerImpl.makeDebitForWHT()");

        Long whtAccountIdOla = null;
        Long whtAccountIdCore = null;
        WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();

        WorkFlowWrapper codeWrapper = new WorkFlowWrapperImpl();
        codeWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(codeWrapper);
        dailyWhtDeductionModel.setUpdatedOn(new Date());
        dailyWhtDeductionModel.setTransactionCodeId(codeWrapper.getTransactionCodeModel().getTransactionCodeId());

        dailyWhtDeductionDAO.saveOrUpdate(dailyWhtDeductionModel);


        AppUserModel appUserModel = appUserManager.loadAppUser(dailyWhtDeductionModel.getAppUserId());
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        wrapper.setAppUserModel(appUserModel);
        wrapper.setTransactionAmount(dailyWhtDeductionModel.getAmount());
        wrapper.setTotalAmount(dailyWhtDeductionModel.getAmount());
        wrapper.putObject("WHT_CONFIG_ID", dailyWhtDeductionModel.getWhtConfigId());
        if (dailyWhtDeductionModel.getWhtConfigId().longValue() == TaxConstantsInterface.WHT_CONFIG_WITHDRAWAL_ID) {
            wrapper.putObject("REASON_ID", ReasonConstants.WHT_DEDUCTION_ON_WITHDRAWAL);
            whtAccountIdOla = PoolAccountConstantsInterface.WHT_231A_GL_ACCOUNT_OLA;
            whtAccountIdCore = PoolAccountConstantsInterface.WHT_231A_GL_ACCOUNT_CORE;
        } else {
            wrapper.putObject("REASON_ID", ReasonConstants.WHT_DEDUCTION_ON_TRANSFER);
            whtAccountIdOla = PoolAccountConstantsInterface.WHT_236P_GL_ACCOUNT_OLA;
            whtAccountIdCore = PoolAccountConstantsInterface.WHT_236P_GL_ACCOUNT_CORE;
        }

        ActionLogModel actionLogModel = this.actionLogBeforeStart();

        SmartMoneyAccountModel sMAModel = smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel);
        long appUserTypeId = appUserModel.getAppUserTypeId();

        CustomerModel customerModel = null;
        RetailerContactModel retailerContactModel = null;

        if (UserTypeConstantsInterface.CUSTOMER == appUserTypeId) {
            customerModel = customerManager.findByPrimaryKey(appUserModel.getCustomerId());
            wrapper.setCustomerModel((CustomerModel) customerModel.clone());
        } else {
            retailerContactModel = retailerContactDAO.findByPrimaryKey(appUserModel.getRetailerContactId());
            wrapper.setRetailerContactModel((RetailerContactModel) retailerContactModel.clone());
        }

        wrapper.setAppUserModel(appUserModel);
        wrapper.setSmartMoneyAccountModel(sMAModel);
        wrapper.setOlaSmartMoneyAccountModel(sMAModel);
        wrapper.setProductModel(new ProductModel(2510734L));

        TransactionCodeModel txCodeModel = new TransactionCodeModel();
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        Date fromTimestamp = new Date();
        Date fromDate = PortalDateUtils.getDateWithoutTime(PortalDateUtils.subtractDays(fromTimestamp, 1));

        if (dailyWhtDeductionModel.getStatus() == 0 && dailyWhtDeductionModel.getCreatedOn().before(fromDate)) {
            BaseWrapper bWrapper = new BaseWrapperImpl();
            wrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(wrapper);
            dailyWhtDeductionModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getTransactionCodeId());
            dailyWhtDeductionModel.setUpdatedOn(new Date());
            bWrapper.setBasePersistableModel(dailyWhtDeductionModel);
            bWrapper = dailyWhtDeductionManager.saveDailyWhtDeduction(bWrapper);
            dailyWhtDeductionModel = (DailyWhtDeductionModel) bWrapper.getBasePersistableModel();
            txCodeModel.setTransactionCodeId(dailyWhtDeductionModel.getTransactionCodeId());
            baseWrapper.setBasePersistableModel(txCodeModel);
            baseWrapper = transactionManager.loadTransactionCode(baseWrapper);
            wrapper.setTransactionCodeModel((TransactionCodeModel) baseWrapper.getBasePersistableModel().clone());

        } else {
            txCodeModel.setTransactionCodeId(dailyWhtDeductionModel.getTransactionCodeId());
            baseWrapper.setBasePersistableModel(txCodeModel);
            baseWrapper = transactionManager.loadTransactionCode(baseWrapper);
            wrapper.setTransactionCodeModel((TransactionCodeModel) baseWrapper.getBasePersistableModel().clone());
        }


        populateTransactionModel(wrapper);

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        switchWrapper.setBasePersistableModel(sMAModel);
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.putObject("REASON_ID", wrapper.getObject("REASON_ID"));

        BaseWrapper bWrapper2 = new BaseWrapperImpl();
        bWrapper2.setBasePersistableModel(new BankModel(BankConstantsInterface.OLA_BANK_ID));
        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(bWrapper2);

        abstractFinancialInstitution.customerWHTDeduction(switchWrapper, whtAccountIdOla, whtAccountIdCore);

        settlementManager.prepareDataForDayEndSettlement(wrapper);

        dailyWhtDeductionModel.setResponseCode("00");
        dailyWhtDeductionModel.setStatus(TaxConstantsInterface.DAILY_WHT_DED_SUCCESS);
        dailyWhtDeductionModel.setUpdatedOn(new Date());
        baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(dailyWhtDeductionModel);
        dailyWhtDeductionManager.saveDailyWhtDeduction(baseWrapper);

        actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(actionLogModel.getInputXml(), XPathConstants.actionLogInputXMLLocationSteps));
        this.actionLogAfterEnd(actionLogModel);
        logger.info("End of TaxManagerImpl.makeDebitForWHT()");
        return wrapper;
    }

    public void saveDailyWHTDeductionModels(List<WorkFlowWrapper> saveDailyWHTEntriesList) throws Exception {

        List<DailyWhtDeductionModel> dailyWhtDeductionModelList = new ArrayList<>(saveDailyWHTEntriesList.size());

        for (WorkFlowWrapper dailWHTEntries : saveDailyWHTEntriesList) {
            DailyWhtDeductionModel model = new DailyWhtDeductionModel();
            model.setWhtConfigId((Long) dailWHTEntries.getObject("WHT_CONFIG_ID"));
            model.setAmount(dailWHTEntries.getTotalAmount());
            model.setAppUserId(dailWHTEntries.getAppUserModel().getAppUserId());
            model.setStatus(TaxConstantsInterface.DAILY_WHT_DED_SUCCESS);
            model.setResponseCode("00");
            model.setCreatedByAppUserModel(new AppUserModel(SCHEDULER_APP_USER_ID));
            model.setUpdatedByAppUserModel(new AppUserModel(SCHEDULER_APP_USER_ID));
            model.setCreatedOn(new Date());
            model.setUpdatedOn(new Date());
            model.setWHTDedTransactionDate(dailWHTEntries.getWHTDedTransactionDate());
            model.setIsFiler(dailWHTEntries.getIsFiler());
            model.setFilerRate(dailWHTEntries.getFilerRate());
            model.setNonFilerRate(dailWHTEntries.getNonFilerRate());
            model.setSumAmount(dailWHTEntries.getSumAmount());
            dailyWhtDeductionModelList.add(model);
        }

        dailyWhtDeductionManager.saveDailyWhtDeduction(dailyWhtDeductionModelList);

    }

    @Override
    public FilerRateConfigModel loadFilerRateConfigModelByFiler(Long filer) {
        return filerRateConfigDAO.loadFilerRateConfigModelByFiler(filer);
    }

    public BaseWrapper loadAndValidateWhtExemption(BaseWrapper baseWrapper) throws Exception {
        WHTExemptionVO whtExemptionVO = (WHTExemptionVO) baseWrapper.getBasePersistableModel();
        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_TIME_FORMAT);
        UserDeviceAccountsModel udaModel = userDeviceAccountsManager.loadUserDeviceAccountByUserId(whtExemptionVO.getUserId().toString());
        if (null == udaModel) {
            throw new Exception("Data is not saved because " + whtExemptionVO.getUserId() + " Agent Id is not Valid!");
        }
        List<WHTExemptionModel> whtExemptionModelList = this.loadWHTExemptionByAppUserId(udaModel.getAppUserId());

        if (!CollectionUtils.isEmpty(whtExemptionModelList)) {
            WHTExemptionModel validWHTExemption = whtExemptionModelList.get(0);

            if (null != whtExemptionVO.getWhtExemptionId() && (validWHTExemption.getStartDate().before(new Date()))) {
                throw new Exception("Record could not be updated.You can only update Exemption start  which is in future");
            } else if (null != whtExemptionVO.getWhtExemptionId() && validWHTExemption.getStartDate().after(new Date())) {
                return baseWrapper;
            } else if (validWHTExemption != null && validWHTExemption.getEndDate().compareTo(whtExemptionVO.getStartDate()) > 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(validWHTExemption.getEndDate());
                c.add(Calendar.DATE, 1);
                c.getTime();
                throw new Exception("Record could not be saved . Exemption Start Date should be from " + c.getTime());
            }

        }
        return baseWrapper;
    }

    private void populateTransactionModel(WorkFlowWrapper wrapper) throws Exception {

        Date now = new Date();
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setBusinessDate(wrapper.getBusinessDate());
        transactionModel.setTotalAmount(wrapper.getTotalAmount());
        transactionModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
        transactionModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getTransactionCodeId());
        transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

        if (wrapper.getCustomerModel() != null) {
            transactionModel.setCustomerId(wrapper.getCustomerModel().getCustomerId());
        } else if (wrapper.getRetailerContactModel() != null) {
            transactionModel.setFromRetContactIdRetailerContactModel(wrapper.getRetailerContactModel());
        }

        BaseWrapper bWrapper = new BaseWrapperImpl();

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
        //userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        bWrapper.setBasePersistableModel(userDeviceAccountsModel);
        bWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(bWrapper);
        UserDeviceAccountsModel uDAModel = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
        if (uDAModel == null || uDAModel.getUserDeviceAccountsId() == null) {
            throw new FrameworkCheckedException("Unable to load User Device Account Info against given App User ID: " + wrapper.getAppUserModel().getAppUserId());
        }

        transactionModel.setMfsId(uDAModel.getUserId());
        transactionModel.setTransactionTypeId(TransactionTypeConstantsInterface.MANUAL_ADJUSTMENT_TX);
        transactionModel.setTotalCommissionAmount(0.0);
        transactionModel.setTransactionAmount(wrapper.getTransactionAmount());
        transactionModel.setNotificationMobileNo(wrapper.getAppUserModel().getMobileNo());
        transactionModel.setSaleMobileNo(null);

        transactionModel.setConfirmationMessage(" ");

        transactionModel.setIssue(false);
        transactionModel.setSupProcessingStatusId(1L);
        transactionModel.setDiscountAmount(0.0);

        transactionModel.setCreatedOn(now);
        transactionModel.setUpdatedOn(now);
        transactionModel.setCreatedBy(SCHEDULER_APP_USER_ID);
        transactionModel.setUpdatedBy(SCHEDULER_APP_USER_ID);
        transactionModel.setProcessingBankId(BankConstantsInterface.OLA_BANK_ID);

        TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
        transactionDetailModel.setTransactionId(transactionModel.getTransactionId());
        transactionDetailModel.setConsumerNo(wrapper.getAppUserModel().getMobileNo());
        transactionDetailModel.setActualBillableAmount(wrapper.getTransactionAmount());
        transactionDetailModel.setSettled(true);
        transactionDetailModel.setProductId(wrapper.getProductModel().getProductId());
        transactionDetailModel.setCustomField1(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId().toString());
        transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);

        TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel(true);
        transactionDetailMasterModel.setBusinessDate(wrapper.getBusinessDate());
        wrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
        wrapper.setTransactionModel(transactionModel);
        wrapper.setTransactionDetailModel(transactionDetailModel);

        transactionDetailMasterModel.setTransactionCode(wrapper.getTransactionCodeModel().getCode());
        transactionDetailMasterModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getTransactionCodeId());
        transactionDetailMasterModel.setProductId(wrapper.getProductModel().getProductId());
        transactionDetailMasterModel.setProductName("Settlement");
        transactionDetailMasterModel.setSupplierId(SupplierConstants.BRANCHLESS_BANKING_SUPPLIER);
        transactionDetailMasterModel.setSupplierName(SupplierConstants.BRANCHLESS_BANKING_SUPPLIER_NAME);

        transactionDetailMasterModel.setDeviceType(DeviceTypeConstantsInterface.DEVICE_TYPES_MAP.get(DeviceTypeConstantsInterface.BULK_DISBURSEMENT));
        transactionDetailMasterModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        transactionDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(transactionModel.getSupProcessingStatusId()));
        transactionDetailMasterModel.setSupProcessingStatusId(transactionModel.getSupProcessingStatusId());
        transactionDetailMasterModel.setTransactionAmount(transactionModel.getTransactionAmount());
        transactionDetailMasterModel.setTotalAmount(transactionModel.getTotalAmount());
        transactionDetailMasterModel.setMfsId(uDAModel.getUserId());
        transactionDetailMasterModel.setSenderCnic(wrapper.getAppUserModel().getNic());
        transactionDetailMasterModel.setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());
        transactionDetailMasterModel.setCreatedOn(now);
        transactionDetailMasterModel.setUpdatedOn(now);

        wrapper.setBasePersistableModel(transactionModel);
        this.transactionManager.saveTransactionModel(wrapper);

        transactionDetailMasterModel.setTransactionId(transactionModel.getTransactionId());
        transactionDetailMasterModel.setTransactionDetailId(transactionDetailModel.getTransactionDetailId());

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(transactionDetailMasterModel);

        transactionDetailMasterManager.saveTransactionDetailMaster(baseWrapper);
    }

    private ActionLogModel actionLogBeforeStart() {
        ActionLogModel actionLogModel = new ActionLogModel();
        XStream xstream = new XStream();
        String xml = xstream.toXML("");
        actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
        actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml,
                XPathConstants.actionLogInputXMLLocationSteps));
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
        actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
        actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
        if (actionLogModel.getActionLogId() != null) {
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }

        return actionLogModel;
    }

    private void actionLogAfterEnd(ActionLogModel actionLogModel) {
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
        actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
        insertActionLogRequiresNewTransaction(actionLogModel);
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

    @Override
    public WHTConfigWrapper loadAllActiveWHTConfigVo() {
        WHTConfigWrapper whtConfigWrapper = new WHTConfigWrapper();
        WHTConfigVo whtConfigVo;
        List<WHTConfigModel> whtConfigModelList = whtConfigDAO.loadAllActiveWHTConfigModels();
        for (WHTConfigModel model : whtConfigModelList) {
            whtConfigVo = new WHTConfigVo();
            whtConfigVo.setWhtConfigId(model.getWhtConfigId());
            whtConfigVo.setFilerRate(model.getFilerRate());
            whtConfigVo.setNonFilerRate(model.getNonFilerRate());
            whtConfigVo.setThresholdLimit(model.getThresholdLimit());
            whtConfigWrapper.addWHTConfigVo(whtConfigVo);
        }
        return whtConfigWrapper;
    }

    @Override
    public BaseWrapper updateWHTConfigModelWithAuthorization(BaseWrapper wrapper) throws FrameworkCheckedException {
        WHTConfigWrapper whtConfigWrapper = (WHTConfigWrapper) wrapper.getBasePersistableModel();
        List<WHTConfigModel> whtConfigModelList = whtConfigDAO.loadAllActiveWHTConfigModels();
        List<WHTConfigModel> updatedWhtConfigModelList = new ArrayList<>();

        for (WHTConfigModel savedModel : whtConfigModelList) {

            for (WHTConfigVo updatedVo : whtConfigWrapper.getWhtConfigVoList()) {
                if (savedModel.getWhtConfigId().longValue() == updatedVo.getWhtConfigId().longValue()) {
                    savedModel.setFilerRate(updatedVo.getFilerRate());
                    savedModel.setNonFilerRate(updatedVo.getNonFilerRate());
                    if (updatedVo.getThresholdLimit() != null) {
                        savedModel.setThresholdLimit(updatedVo.getThresholdLimit());
                    }
                    savedModel.setUpdatedOn(new Date());
                    savedModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    updatedWhtConfigModelList.add(savedModel);
                }

            }
        }
        whtConfigDAO.saveOrUpdateCollection(updatedWhtConfigModelList);
        return wrapper;
    }

	/*public BaseWrapper saveDailyWhtDeduction(BaseWrapper baseWrapper) throws Exception{
		return dailyWhtDeductionManager.saveDailyWhtDeductionRequiresNewTransaction(baseWrapper);
	}*/

    public void setFedRuleDAO(FEDRuleDAO fedRuleDAO) {
        this.fedRuleDAO = fedRuleDAO;
    }

    public void setWhtConfigDAO(WHTConfigDAO whtConfigDAO) {
        this.whtConfigDAO = whtConfigDAO;
    }

	/*public void setFedRuleViewDAO(FEDRuleViewDAO fedRuleViewDAO) {
		this.fedRuleViewDAO = fedRuleViewDAO;
	}*/

    public void setWhtExemptionDAO(WHTExemptionDAO whtExemptionDAO) {
        this.whtExemptionDAO = whtExemptionDAO;
    }

    public void setTaxRegimeDAO(TaxRegimeDAO taxRegimeDAO) {
        this.taxRegimeDAO = taxRegimeDAO;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    @Override
    public BaseWrapper saveUpdateFedRules(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        FedRuleManagementVO vo = (FedRuleManagementVO) baseWrapper.getBasePersistableModel();
        List<FEDRuleModel> fedRulesList = vo.getFedRuleModelList();
        fedRuleDAO.deleteFedRules();
        fedRuleDAO.saveOrUpdateCollection(fedRulesList);

        this.actionLogManager.completeActionLog(actionLogModel);

        return baseWrapper;
    }

    @Override
    public Boolean isTaxRegimeNameUnique(String taxRegimeName, Long taxRegimeId) throws FrameworkCheckedException {

        TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
        Boolean retVal = false;
        taxRegimeModel.setName(taxRegimeName);
        CustomList list = taxRegimeDAO.findByExample(taxRegimeModel, null, null, new ExampleConfigHolderModel(false, true, false, MatchMode.EXACT));
        if (CollectionUtils.isEmpty(list.getResultsetList())) // no record with same name
        {
            retVal = true;
        } else {
            if (list.getResultsetList().size() == 1) //only one record found now check if id is same then it is unique
            {
                taxRegimeModel = (TaxRegimeModel) list.getResultsetList().get(0);
                if (taxRegimeModel.getTaxRegimeId().equals(taxRegimeId)) {
                    retVal = true;
                } else {
                    retVal = false;
                }
            } else    //multiple record found with same name; it is not unique
            {
                retVal = false;
            }
        }

        return retVal;
    }

    private TaxRegimeModel getTaxRegimeModel(TaxRegimeModelVO taxRegimeModelVO) {
        TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
        taxRegimeModel.setTaxRegimeId(taxRegimeModelVO.getTaxRegimeId());
        taxRegimeModel.setName(taxRegimeModelVO.getName());
        taxRegimeModel.setDescription(taxRegimeModelVO.getDescription());
        taxRegimeModel.setFed(taxRegimeModelVO.getFed());
        taxRegimeModel.setCreatedBy(taxRegimeModelVO.getCreatedByAppUserModelId());
        taxRegimeModel.setCreatedOn(taxRegimeModelVO.getCreatedOn());
        taxRegimeModel.setUpdatedBy(taxRegimeModelVO.getUpdatedByAppUserModelId());
        taxRegimeModel.setUpdatedOn(taxRegimeModelVO.getUpdatedOn());
        taxRegimeModel.setVersionNo(taxRegimeModelVO.getVersionNo());
        taxRegimeModel.setIsActive(taxRegimeModelVO.getIsActive());

        return taxRegimeModel;
    }

    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
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

    public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

	/*public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}*/

    public void setOlaVeriflyFinancialInstitution(AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
        this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    public void setWhtExemptionListViewDAO(WHTExemptionListViewDAO whtExemptionListViewDAO) {
        this.whtExemptionListViewDAO = whtExemptionListViewDAO;
    }

    public void setDailyWhtDeductionManager(DailyWhtDeductionManager dailyWhtDeductionManager) {
        this.dailyWhtDeductionManager = dailyWhtDeductionManager;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO) {
        this.retailerContactDAO = retailerContactDAO;
    }

    public void setFinancialIntegrationManager(
            FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    @Override
    public void saveOrUpdateWHTDeductionSchedularStatus(
            WHTDeductionSchedularStatusModel wHTDeductionSchedularStatusModel)
            throws Exception {
        logger.info("Start of TaxManagerImpl.saveOrUpdateWHTDeductionSchedularStatus()");
        whtDeductionSchedularStatusDAO.saveOrUpdate(wHTDeductionSchedularStatusModel);
        logger.info("End of TaxManagerImpl.saveOrUpdateWHTDeductionSchedularStatus()");

    }

    public void setWhtDeductionSchedularStatusDAO(
            WHTDeductionSchedularStatusDAO whtDeductionSchedularStatusDAO) {
        this.whtDeductionSchedularStatusDAO = whtDeductionSchedularStatusDAO;
    }

    @Override
    public List<WHTDeductionSchedularStatusModel> findWHTDeductionMissedEntries(WHTDeductionSchedularStatusModel wHTDeductionSchedularStatusModel) throws Exception {
        // TODO Auto-generated method stub

        CustomList<WHTDeductionSchedularStatusModel> whtDeductionSchedularStatusModelsList = whtDeductionSchedularStatusDAO.findByExample(wHTDeductionSchedularStatusModel, null);

        if (null != whtDeductionSchedularStatusModelsList && whtDeductionSchedularStatusModelsList.getResultsetList().size() > 0)
            return whtDeductionSchedularStatusModelsList.getResultsetList();
        else

            return null;
    }

    @Override
    public List<WHTDeductionSchedularStatusModel> findWHTDeductionSchedularStatusEntries()
            throws Exception {
        // TODO Auto-generated method stub
        List<WHTDeductionSchedularStatusModel> whtDeductionSchedularStatusModelsList = whtDeductionSchedularStatusDAO.findWHTDeductionMissedEntries();
        return whtDeductionSchedularStatusModelsList;
    }

    public void setDailyWhtDeductionDAO(DailyWhtDeductionDAO dailyWhtDeductionDAO) {
        this.dailyWhtDeductionDAO = dailyWhtDeductionDAO;
    }

    @Override
    public void updateWhtDeductionSchedulerStatus() throws Exception {
        // TODO Auto-generated method stub


    }


    public void setFilerRateConfigDAO(FilerRateConfigDAO filerRateConfigDAO) {
        this.filerRateConfigDAO = filerRateConfigDAO;
    }
}

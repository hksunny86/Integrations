package com.inov8.microbank.server.service.portal.mfsaccountmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.debitcard.dao.DebitCardModelDAO;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.nadraVerisys.service.VerisysDataManager;
import com.inov8.microbank.server.dao.appuserhistorymodule.AppUserHistoryDao;
import com.inov8.microbank.server.dao.customermodule.CustomerRemitterDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.ola.server.dao.account.AccountDAO;
import com.inov8.ola.server.service.accountholder.AccountHolderManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MfsAccountClosureManagerImpl implements MfsAccountClosureManager {

    private final static Log logger = LogFactory.getLog(MfsAccountClosureManagerImpl.class);

    private FinancialIntegrationManager financialIntegrationManager;
    private MfsAccountManager mfsAccountManager;
    private StakeholderBankInfoManager stakeholderBankInfoManager;
    private ActionLogManager actionLogManager;
    private SwitchController switchController;
    private AppUserManager appUserManager;
    private AppUserHistoryDao appUserHistoryDao;
    private AccountHolderManager accountHolderManager;
    private VeriflyManagerService veriflyController;
    private SmartMoneyAccountDAO smartMoneyAccountDAO;
    private HandlerManager handlerManager;
    private VerisysDataManager verisysDataManager;
    private AccountInfoDAO accountInfoDAO;
    private CustomerRemitterDAO customerRemitterDAO;
    private CustTransManager custTransManager;
    private AccountDAO accountDAO;
    private DebitCardModelDAO debitCardModelDAO;
    private ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO;
    private ESBAdapter esbAdapter;


    public BaseWrapper makeCustomerAccountClosed(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        Long paymentModeId = (Long) baseWrapper.getObject("paymentModeId");
        AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        String cNic = appUserModel.getNic();
        String mobileNo = appUserModel.getMobileNo();
        Long appUserId = appUserModel.getAppUserId();
        AppUserModel tempAppUserModel;
        AppUserHistoryModel appUserHistoryModel = null;
        tempAppUserModel = this.mfsAccountManager.getAppUserModelByPrimaryKey(appUserModel.getAppUserId());
        double accountBalance = 0;
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getObject("smartMoneyAccountModel");

        if (smartMoneyAccountModel.getActive() == true) {
            try {
                accountBalance = getCustomerBalance(tempAppUserModel.getMobileNo(), paymentModeId);
            } catch (Exception e) {
                e.printStackTrace();
                throw new FrameworkCheckedException(e.getMessage());
            }
            if (accountBalance > 0) {
                throw new FrameworkCheckedException("Balance not Zero", null, 151, null);
            }
        }

        Boolean isClosedSetteled = (Boolean) baseWrapper.getObject("isClosedSettled");

        smartMoneyAccountModel.setActive(Boolean.FALSE);
        smartMoneyAccountDAO.updateSmartMoneyAccountModelToCloseAccount(smartMoneyAccountModel, isClosedSetteled);

        AccountInfoModel accountInfoModel = (AccountInfoModel) baseWrapper.getObject("accountInfoModel");
        if (accountInfoModel != null)
            this.accountInfoDAO.updateAccountInfoModelToCloseAccount(accountInfoModel.getAccountInfoId());

        int result = 0;
        AccountHolderModel accountHolderModel = (AccountHolderModel) baseWrapper.getObject("accountHolderModel");
        if (accountHolderModel != null) {
            try {
                result = accountHolderManager.updateAccountHolderModelToCloseAccount(accountHolderModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AccountModel accountModel = (AccountModel) baseWrapper.getObject("accountModel");
        if (accountModel != null)
            result = accountDAO.updateAccountModelToCloseAccount(accountModel);

        if (smartMoneyAccountModel.getPaymentModeId().equals(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT)) {
            List<CustomerRemitterModel> customerRemitterModelList = this.customerRemitterDAO.getActiveCustomerRemitterModelByCustomerId(smartMoneyAccountModel.getCustomerId());
            List<CustomerRemitterModel> newRemitterList = new ArrayList<>();
            CustomerRemitterModel customerRemitterModel = null;
            if (customerRemitterModelList != null && !CollectionUtils.isEmpty(customerRemitterModelList)) {
                for (int i = 0; i < customerRemitterModelList.size(); i++) {
                    customerRemitterModel = customerRemitterModelList.get(i);
                    customerRemitterModel.setIsActive(0L);
                    newRemitterList.add(customerRemitterModel);
                }
            }
            if (null != newRemitterList && newRemitterList.size() > 0) {
                this.custTransManager.saveOrUpdateCustomerRemitter(newRemitterList);
            }
        }

        BankModel bankModel = new BankModel();
        bankModel.setBankId(CommissionConstantsInterface.BANK_ID);

        tempAppUserModel.setUpdatedOn(new Date());
        tempAppUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

        if (!tempAppUserModel.getAccountClosedUnsettled() && paymentModeId == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) {
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.CUSTOMER_ACCOUNT_CLOSURE));
            ThreadLocalAppUser.setAppUserModel(tempAppUserModel);
            tempAppUserModel.setAccountClosedUnsettled(true);
            tempAppUserModel.setClosedByAppUserModel(UserUtils.getCurrentUser());
            tempAppUserModel.setClosedOn(new Date());
            tempAppUserModel.setClosingComments(appUserModel.getClosingComments());
            tempAppUserModel.setAccountEnabled(false);
            appUserHistoryModel = tempAppUserModel.toAppUserHistoryModel();
        } else if (paymentModeId == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) {
            String newMobileNo = tempAppUserModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + System.currentTimeMillis();
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.CUSTOMER_ACCOUNT_SETTLEMENT));
            tempAppUserModel.setMobileNo(newMobileNo);
            String oldCnic = tempAppUserModel.getNic();
            String newCnic = oldCnic + PortalConstants.PREFIX_SETTLED_ACCOUNT + System.currentTimeMillis();
            tempAppUserModel.setNic(newCnic);
            tempAppUserModel.setAccountClosedSettled(true);
            tempAppUserModel.setRegistrationStateId(RegistrationStateConstantsInterface.CLOSED);
            tempAppUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED);

            tempAppUserModel.setSettledByAppUserModel(UserUtils.getCurrentUser());
            tempAppUserModel.setSettledOn(new Date());
            tempAppUserModel.setSettlementComments(appUserModel.getSettlementComments());

            appUserHistoryModel = searchAppUserHistoryModel(tempAppUserModel.getAppUserId());

            appUserHistoryModel.setSettledByAppUserModel(UserUtils.getCurrentUser());
            appUserHistoryModel.setSettledOn(new Date());
            appUserHistoryModel.setSettlementComments(appUserModel.getSettlementComments());

//		    oldCnic = EncryptionUtil.encryptPin(oldCnic);
//		    newCnic = EncryptionUtil.encryptPin(newCnic);
            accountHolderManager.updateCnicAndMobileNo(oldCnic, newCnic, tempAppUserModel.getMobileNo());

            //Turab March 30, 2018 removed this code as this was corrupting urdu data incase of closed settling account of customer
		    /*VerisysDataModel vModel=new VerisysDataModel();
			vModel=verisysDataManager.loadVerisysDataModel(tempAppUserModel.getAppUserId());
			vModel.setAccountClosed(true);
			verisysDataManager.saveOrUpdate(vModel);*/

            verisysDataManager.markClosedByAppUserId(tempAppUserModel.getAppUserId());

            DebitCardModel debitCardModel = debitCardModelDAO.getDebitCardModelByCustomerAppUserId(appUserId);
            if (debitCardModel != null) {
                Long curretnTime = System.currentTimeMillis();

                debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_HOT);
                debitCardModel.setCardStateId(CardConstantsInterface.CARD_STATE_HOT);
                debitCardModel.setUpdatedOn(new Date());
                debitCardModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForUpdateCardStatus(I8SBConstants.RequestType_JSDEBITCARD_UpdateCardStatus);
                requestVO.setCNIC(debitCardModel.getCnic());
                requestVO.setCardNumber(debitCardModel.getCardNo());
//                requestVO.setStatus(debitCardModel.getCardStatusStr());
                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();

                debitCardModel.setCnic(debitCardModel.getCnic() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                debitCardModel.setMobileNo(debitCardModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                debitCardModel.setCardNo(debitCardModel.getCardNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);

                responseVO = requestVO.getI8SBSwitchControllerResponseVO();

                debitCardModelDAO.saveOrUpdate(debitCardModel);
            }

            Long curretnTime = System.currentTimeMillis();
            ClsPendingAccountOpeningModel clsPendingAccountOpeningModel = new ClsPendingAccountOpeningModel();

            clsPendingAccountOpeningModel = clsPendingAccountOpeningDAO.loadClsPendingAccountOpeningByMobileByQuery(tempAppUserModel.getMobileNo());

            if (clsPendingAccountOpeningModel!=null) {
                clsPendingAccountOpeningModel.setMobileNo(tempAppUserModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                clsPendingAccountOpeningModel.setCnic(tempAppUserModel.getNic() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                clsPendingAccountOpeningModel.setUpdatedOn(new Date());
                clsPendingAccountOpeningModel.setUpdatedBy(UserUtils.getCurrentUser().getUpdatedBy());
                clsPendingAccountOpeningModel.setRegistrationStateId(RegistrationStateConstantsInterface.CLOSED);
                clsPendingAccountOpeningModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED);
                clsPendingAccountOpeningModel.setIsSmsRequired(false);

                clsPendingAccountOpeningDAO.saveOrUpdate(clsPendingAccountOpeningModel);
            }
        }

        if (paymentModeId == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) {
            baseWrapper.setBasePersistableModel(tempAppUserModel);
            baseWrapper = this.mfsAccountManager.updateAppUserModel(baseWrapper);
            if (appUserHistoryModel != null) {
                appUserHistoryModel.setAppUserId(tempAppUserModel.getAppUserId());
                appUserHistoryDao.saveOrUpdate(appUserHistoryModel);
            }
            DebitCardModel debitCardModel = debitCardModelDAO.getDebitCardModelByCustomerAppUserId(appUserId);
            if (debitCardModel != null) {
                Long curretnTime = System.currentTimeMillis();

                debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_HOT);
                debitCardModel.setCardStateId(CardConstantsInterface.CARD_STATE_HOT);
                debitCardModel.setUpdatedOn(new Date());
                debitCardModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForUpdateCardStatus(I8SBConstants.RequestType_JSDEBITCARD_UpdateCardStatus);
                requestVO.setCNIC(debitCardModel.getCnic());
                requestVO.setCardNumber(debitCardModel.getCardNo());
//                requestVO.setStatus(debitCardModel.getCardStatusStr());
                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();

                debitCardModel.setCnic(debitCardModel.getCnic() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                debitCardModel.setMobileNo(debitCardModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                debitCardModel.setCardNo(debitCardModel.getCardNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);

                responseVO = requestVO.getI8SBSwitchControllerResponseVO();
//                if (responseVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {

                debitCardModelDAO.saveOrUpdate(debitCardModel);
//                }
            }

            Long curretnTime = System.currentTimeMillis();
            ClsPendingAccountOpeningModel clsPendingAccountOpeningModel = new ClsPendingAccountOpeningModel();

            clsPendingAccountOpeningModel = clsPendingAccountOpeningDAO.loadClsPendingAccountOpeningByMobileByQuery(tempAppUserModel.getMobileNo());

            if (clsPendingAccountOpeningModel!=null) {
                clsPendingAccountOpeningModel.setMobileNo(tempAppUserModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                clsPendingAccountOpeningModel.setCnic(tempAppUserModel.getNic() + PortalConstants.PREFIX_SETTLED_ACCOUNT + curretnTime);
                clsPendingAccountOpeningModel.setUpdatedOn(new Date());
                clsPendingAccountOpeningModel.setUpdatedBy(UserUtils.getCurrentUser().getUpdatedBy());
                clsPendingAccountOpeningModel.setRegistrationStateId(RegistrationStateConstantsInterface.CLOSED);
                clsPendingAccountOpeningModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED);
                clsPendingAccountOpeningModel.setIsSmsRequired(false);
                clsPendingAccountOpeningDAO.saveOrUpdate(clsPendingAccountOpeningModel);
            }

            actionLogModel.setCustomField1(tempAppUserModel.getCustomerId().toString());
            actionLogModel.setCustomField11(baseWrapper.getObject("mfsId").toString());
            actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
            this.actionLogManager.completeActionLog(actionLogModel);
        }

        return baseWrapper;
    }

    public BaseWrapper makeAgentAccountClosed(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        Long appUserId = appUserModel.getAppUserId();
        AppUserModel tempAppUserModel;
        AppUserHistoryModel appUserHistoryModel = null;
        tempAppUserModel = this.mfsAccountManager.getAppUserModelByPrimaryKey(appUserModel.getAppUserId());
        double accountBalance = 0;

        int result = 0;
        AccountHolderModel accountHolderModel = (AccountHolderModel) baseWrapper.getObject("accountHolderModel");
        if (accountHolderModel != null) {
            try {
                result = accountHolderManager.updateAccountHolderModelToCloseAccount(accountHolderModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AccountModel accountModel = (AccountModel) baseWrapper.getObject("accountModel");
        if (accountModel != null)
            result = accountDAO.updateAccountModelToCloseAccount(accountModel);

//      	tempAppUserModel = this.appUserManager.getUser((String.valueOf(appUserId)));

        ////////////////////////////////Start Close handlers Accounts/////////////
        HandlerSearchViewModel handlerSearchViewModel = new HandlerSearchViewModel();
        List<HandlerSearchViewModel> handlerSearchViewModelList = new ArrayList<>();

        handlerSearchViewModel.setRetailerContactId(tempAppUserModel.getRetailerContactId());
        if (!tempAppUserModel.getAccountClosedUnsettled())
            handlerSearchViewModel.setAccountClosed("OPEN");
        else
            handlerSearchViewModel.setAccountClosed("CLOSED SETTLED");

        BaseWrapper baseWrapper2 = new BaseWrapperImpl();
        baseWrapper2.setBasePersistableModel(handlerSearchViewModel);

        handlerSearchViewModelList = handlerManager.findHandlerViews(baseWrapper2);

        if (null != handlerSearchViewModelList && handlerSearchViewModelList.size() > 0) {
            appUserManager.checkHandlerPendingTransactions(handlerSearchViewModelList);

            for (HandlerSearchViewModel handlerSearchViewModel2 : handlerSearchViewModelList) {
                BaseWrapper tempBaseWrapper = new BaseWrapperImpl();
                AppUserModel appUserModel2 = new AppUserModel();
                appUserModel2.setAppUserId(handlerSearchViewModel2.getAppUserId());
                tempBaseWrapper.setBasePersistableModel(appUserModel2);
                this.makeHandlerAccountClosed(tempBaseWrapper);
            }
        }
        ////////////////////// End Close handlers Accounts///

        BankModel bankModel = new BankModel();
        bankModel.setBankId(CommissionConstantsInterface.BANK_ID);

        tempAppUserModel = this.appUserManager.getUser((String.valueOf(appUserId)));
        tempAppUserModel.setUpdatedOn(new Date());
        tempAppUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        if (!tempAppUserModel.getAccountClosedUnsettled()) {

            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.AGENT_ACCOUNT_CLOSURE));
            tempAppUserModel.setAccountClosedUnsettled(true);
            tempAppUserModel.setClosedByAppUserModel(UserUtils.getCurrentUser());
            tempAppUserModel.setClosedOn(new Date());
            tempAppUserModel.setClosingComments(appUserModel.getClosingComments());
            tempAppUserModel.setAccountEnabled(false);
            appUserHistoryModel = tempAppUserModel.toAppUserHistoryModel();
        } else {
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.AGENT_ACCOUNT_SETTLEMENT));
            tempAppUserModel.setMobileNo(tempAppUserModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + System.currentTimeMillis());
            String oldCnic = tempAppUserModel.getNic();
            String newCnic = oldCnic + PortalConstants.PREFIX_SETTLED_ACCOUNT + System.currentTimeMillis();
            tempAppUserModel.setNic(newCnic);
            tempAppUserModel.setAccountClosedSettled(true);
            tempAppUserModel.setRegistrationStateId(RegistrationStateConstantsInterface.CLOSED);
            tempAppUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED);

            tempAppUserModel.setSettledByAppUserModel(UserUtils.getCurrentUser());
            tempAppUserModel.setSettledOn(new Date());
            tempAppUserModel.setSettlementComments(appUserModel.getSettlementComments());

            appUserHistoryModel = searchAppUserHistoryModel(tempAppUserModel.getAppUserId());

            appUserHistoryModel.setSettledByAppUserModel(UserUtils.getCurrentUser());
            appUserHistoryModel.setSettledOn(new Date());
            appUserHistoryModel.setSettlementComments(appUserModel.getSettlementComments());

//		    oldCnic = EncryptionUtil.encryptPin(oldCnic);
//		    newCnic = EncryptionUtil.encryptPin(newCnic);
            accountHolderManager.updateCnicAndMobileNo(oldCnic, newCnic, tempAppUserModel.getMobileNo());
        }

        baseWrapper.setBasePersistableModel(tempAppUserModel);
        baseWrapper = this.appUserManager.closeAgentAccount(baseWrapper); //resolve pending transactions first

        try {
            accountBalance = checkAgentBalance(tempAppUserModel); //check for zero balance
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(e.getMessage());
        }
        if (accountBalance > 0) {
            throw new FrameworkCheckedException("Balance not Zero");
        }

        appUserHistoryModel.setAppUserId(tempAppUserModel.getAppUserId());
        appUserHistoryDao.saveOrUpdate(appUserHistoryModel);

        actionLogModel.setCustomField1(tempAppUserModel.getRetailerContactId().toString());
        actionLogModel.setCustomField11(baseWrapper.getObject("retailerId").toString());
        actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
        this.actionLogManager.completeActionLog(actionLogModel);
        return baseWrapper;
    }

    private AppUserHistoryModel searchAppUserHistoryModel(Long appUserId) {
        AppUserHistoryModel appUserHistoryModel = new AppUserHistoryModel();
        appUserHistoryModel.setAppUserId(appUserId);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(appUserHistoryModel);
        CustomList<AppUserHistoryModel> customList = appUserHistoryDao.findByExample(appUserHistoryModel);
        if (customList != null) {
            List<AppUserHistoryModel> appUserHistoryModelList = customList.getResultsetList();
            if (CollectionUtils.isNotEmpty(appUserHistoryModelList)) {
                appUserHistoryModel = appUserHistoryModelList.get(0);
            }
        }
        return appUserHistoryModel;
    }

    public BaseWrapper makeHandlerAccountClosed(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        Long appUserId = appUserModel.getAppUserId();
        AppUserModel tempAppUserModel = null;
        AppUserHistoryModel appUserHistoryModel = null;
        BankModel bankModel = new BankModel();
        bankModel.setBankId(CommissionConstantsInterface.BANK_ID);

        tempAppUserModel = this.appUserManager.getUser((String.valueOf(appUserId)));
        tempAppUserModel.setUpdatedOn(new Date());
        tempAppUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        if (!tempAppUserModel.getAccountClosedUnsettled()) {

            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.HANDLER_ACCOUNT_CLOSURE));
            tempAppUserModel.setAccountClosedUnsettled(true);
            tempAppUserModel.setClosedByAppUserModel(UserUtils.getCurrentUser());
            tempAppUserModel.setClosedOn(new Date());
            tempAppUserModel.setClosingComments(appUserModel.getClosingComments());
            tempAppUserModel.setAccountEnabled(false);
            tempAppUserModel.setMobileNo(tempAppUserModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + System.currentTimeMillis());
            String oldCnic = tempAppUserModel.getNic();
            String newCnic = oldCnic + PortalConstants.PREFIX_SETTLED_ACCOUNT + System.currentTimeMillis();
            tempAppUserModel.setNic(newCnic);

            tempAppUserModel.setAccountClosedSettled(true);
            tempAppUserModel.setRegistrationStateId(RegistrationStateConstantsInterface.CLOSED);
            tempAppUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED);
            tempAppUserModel.setSettledByAppUserModel(UserUtils.getCurrentUser());
            tempAppUserModel.setSettledOn(new Date());
            tempAppUserModel.setSettlementComments(appUserModel.getSettlementComments());
            appUserHistoryModel = tempAppUserModel.toAppUserHistoryModel();
            //appUserHistoryModel = searchAppUserHistoryModel(tempAppUserModel.getAppUserId());

            appUserHistoryModel.setSettledByAppUserModel(UserUtils.getCurrentUser());
            appUserHistoryModel.setSettledOn(new Date());
            appUserHistoryModel.setCreatedOn(new Date());
            appUserHistoryModel.setSettlementComments(appUserModel.getSettlementComments());

//		    oldCnic = EncryptionUtil.encryptPin(oldCnic);
//		    newCnic = EncryptionUtil.encryptPin(newCnic);
            accountHolderManager.updateCnicAndMobileNo(oldCnic, newCnic, tempAppUserModel.getMobileNo());

        } else {
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.HANDLER_ACCOUNT_SETTLEMENT));
            tempAppUserModel.setMobileNo(tempAppUserModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + System.currentTimeMillis());
            String oldCnic = tempAppUserModel.getNic();
            String newCnic = oldCnic + PortalConstants.PREFIX_SETTLED_ACCOUNT + System.currentTimeMillis();
            tempAppUserModel.setNic(newCnic);

            tempAppUserModel.setAccountClosedSettled(true);
            tempAppUserModel.setRegistrationStateId(RegistrationStateConstantsInterface.CLOSED);
            tempAppUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED);
            tempAppUserModel.setSettledByAppUserModel(UserUtils.getCurrentUser());
            tempAppUserModel.setSettledOn(new Date());
            tempAppUserModel.setSettlementComments(appUserModel.getSettlementComments());

            appUserHistoryModel = searchAppUserHistoryModel(tempAppUserModel.getAppUserId());

            appUserHistoryModel.setSettledByAppUserModel(UserUtils.getCurrentUser());
            appUserHistoryModel.setSettledOn(new Date());
            appUserHistoryModel.setSettlementComments(appUserModel.getSettlementComments());

//		    oldCnic = EncryptionUtil.encryptPin(oldCnic);
//		    newCnic = EncryptionUtil.encryptPin(newCnic);
            accountHolderManager.updateCnicAndMobileNo(oldCnic, newCnic, tempAppUserModel.getMobileNo());
        }
        baseWrapper.setBasePersistableModel(tempAppUserModel);
        baseWrapper = this.mfsAccountManager.closeHandlerAccount(baseWrapper);

        appUserHistoryModel.setAppUserId(tempAppUserModel.getAppUserId());
        appUserHistoryDao.saveOrUpdate(appUserHistoryModel);

        return baseWrapper;
    }

    private void settleAccountBalances(AppUserModel tempAppUserModel) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        SmartMoneyAccountModel smartMoneyAccountModel = mfsAccountManager.getSmartMoneyAccountByCustomerId(tempAppUserModel.getCustomerId());
        baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setBasePersistableModel(smartMoneyAccountModel);
        switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());
        this.recordActionLogBeforeStart();
        switchWrapper = abstractFinancialInstitution.checkBalanceWithoutPin(switchWrapper);

        Double balance = switchWrapper.getBalance();
        logger.info("[MfsAccountClosureManagerImpl.settleAccountBalances] AppUserID: " + tempAppUserModel.getAppUserId() + " OLA Balance: " + balance);
        if (balance != null && balance > 0) {
            //debit customer account
            switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setBasePersistableModel(smartMoneyAccountModel);
            switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());
            switchWrapper.setBalance(balance);
            switchWrapper.getWorkFlowWrapper().setSmartMoneyAccountModel(smartMoneyAccountModel);
            switchWrapper.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());

            logger.info("[MfsAccountClosureManagerImpl.settleAccountBalances] AppUserID: " + tempAppUserModel.getAppUserId() + " Going to Debit customer with amount: " + balance);
            switchWrapper = abstractFinancialInstitution.closeOLAAccount(switchWrapper);

            // Now Make a phoenix call for FT from Customer Pool Account to Closure Sundry Account.
            StakeholderBankInfoModel customerPoolBankInfo = getAccount(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
            StakeholderBankInfoModel closureSundryBankInfo = getAccount(PoolAccountConstantsInterface.ACC_CLOSURE_SUNDRY_ACCOUNT_ID);

            debitCreditPhoenixAccounts(customerPoolBankInfo.getAccountNo(), closureSundryBankInfo.getAccountNo(), balance);
        }
    }

    private void recordActionLogBeforeStart() throws Exception {
        ActionLogModel actionLogModel = new ActionLogModel();
        XStream xstream = new XStream();
        try {
            xstream.toXML("");
            actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALLPAY_WEB);
            actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(""),
                    XPathConstants.actionLogInputXMLLocationSteps));

            actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
            actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
            actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
            if (actionLogModel.getActionLogId() != null) {
                ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
            }
        } finally {
        }
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

    private boolean debitCreditPhoenixAccounts(String fromAccountNo, String toAccountNo, Double transactionAmount) throws FrameworkCheckedException {
        boolean success = true;

        SwitchWrapper switchWrapperMain = new SwitchWrapperImpl();
        IntegrationMessageVO integrationMessageVOMain = new PhoenixIntegrationMessageVO();
        switchWrapperMain.setIntegrationMessageVO(integrationMessageVOMain);

        switchWrapperMain.setFromAccountNo(fromAccountNo);
        switchWrapperMain.setFromAccountType("20");
        switchWrapperMain.setFromCurrencyCode("586");

        switchWrapperMain.setToAccountNo(toAccountNo);
        switchWrapperMain.setToAccountType("20");
        switchWrapperMain.setToCurrencyCode("586");

        switchWrapperMain.setTransactionAmount(transactionAmount);
        switchWrapperMain.setCurrencyCode("586");

        switchWrapperMain.setWorkFlowWrapper(new WorkFlowWrapperImpl());
        switchWrapperMain.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
        AppUserModel appUserModelMain = new AppUserModel();
        appUserModelMain.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        ThreadLocalAppUser.setAppUserModel(appUserModelMain);
        switchWrapperMain.setBankId(50110L);
        switchWrapperMain.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
        logger.info("[MfsAccountClosureManagerImpl.debitCreditPhoenixAccounts] Going to make debitCredit tx from account " + fromAccountNo + " to Account: " + toAccountNo);
        switchWrapperMain = switchController.debitCreditAccount(switchWrapperMain);

        //if FT is unsuccessful, update success flag.
        if (switchWrapperMain.getIntegrationMessageVO().getResponseCode() == null || !switchWrapperMain.getIntegrationMessageVO().getResponseCode().equals("00")) {
            logger.info("[MfsAccountClosureManagerImpl.debitCreditPhoenixAccounts] Failed to make FT from account " + fromAccountNo + " to Account: " + toAccountNo);
            success = false;
        }

        return success;
    }

    public StakeholderBankInfoModel getAccount(Long key) {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
        stakeholderBankInfoModel.setPrimaryKey(key);
        searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
        try {
            stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper)
                    .getBasePersistableModel();
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        return stakeholderBankInfoModel;
    }

    public Double getCustomerBalance(String customerMobileNumber, Long paymentModeId) throws WorkFlowException, FrameworkCheckedException, Exception {

        AppUserModel customerAppUserModel = new AppUserModel();
        customerAppUserModel.setMobileNo(customerMobileNumber);
        customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);

        if (customerAppUserModel == null) {
            logger.error("[CommonCommandManager.checkCustomerBalance] Invalid customer mobile number. customerMobileNumber:" + customerMobileNumber);
            throw new CommandException("Unable to check customer balance. Invalid customer mobile number.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        }

        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        sma.setCustomerId(customerAppUserModel.getCustomerId());
        sma.setPaymentModeId(paymentModeId);
        sma.setDefAccount(true);
        sma.setDeleted(false);
        sma.setActive(Boolean.TRUE);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(sma);
        searchBaseWrapper = this.loadSmartMoneyAccount(searchBaseWrapper);
        if (null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
            sma = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
        } else {
            logger.error("[CommonCommandManager.checkCustomerBalance] Unable to load smart money account. customerAppUserId:" + customerAppUserModel.getAppUserId());
            throw new CommandException("Unable to check customer balance. Unable to load smart money account.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        }

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setCustomerId(customerAppUserModel.getCustomerId());
        accountInfoModel.setAccountNick(sma.getName());

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(sma);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.setBasePersistableModel(sma);
        veriflyBaseWrapper.setBasePersistableModel(null);
        VeriflyManager veriflyManager = this.loadVeriflyManagerByAccountId(bWrapper);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        try {
            veriflyBaseWrapper = veriflyManager.verifyCredentials(veriflyBaseWrapper);
            boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

            if (errorMessagesFlag) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

                switchWrapper.setAccountInfoModel(accountInfoModel);
                switchWrapper.setWorkFlowWrapper(workFlowWrapper);

                switchWrapper.setBankId(sma.getBankId());
                switchWrapper.setPaymentModeId(sma.getPaymentModeId());
                switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

                switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, customerAppUserModel.getNic());

                if (switchWrapper.getWorkFlowWrapper() != null) {
                    switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                }

                logger.info("[CommonCommandManager.checkCustomerBalance] Going to Check Customer Balance for AppUserID:" + customerAppUserModel.getAppUserId());

                switchWrapper = this.switchController.checkBalance(switchWrapper);

                double balance = switchWrapper.getBalance();

                return balance;

            } else {

            }
        } catch (CommandException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CommandException("Your account cannot be contacted. Please try again later.\n", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }


        return null;
    }

    public Double checkAgentBalance(AppUserModel appUserModel) throws WorkFlowException, FrameworkCheckedException, Exception {

        boolean isFetched = false;

        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        //AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        sma.setRetailerContactId(appUserModel.getRetailerContactId());
        sma.setDeleted(false);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(sma);
        searchBaseWrapper = this.loadSmartMoneyAccount(searchBaseWrapper);
        if (null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
            sma = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
        }
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setCustomerId(appUserModel.getAppUserId());
        accountInfoModel.setAccountNick(sma.getName());
//		accountInfoModel.setOldPin(pin);

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(sma);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.setBasePersistableModel(sma);
        veriflyBaseWrapper.setBasePersistableModel(null);
        VeriflyManager veriflyManager = this.loadVeriflyManagerByAccountId(bWrapper);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        try {
            veriflyBaseWrapper = veriflyManager.verifyCredentials(veriflyBaseWrapper);
            boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

            if (errorMessagesFlag) {

                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

                switchWrapper.setAccountInfoModel(accountInfoModel);
                switchWrapper.setWorkFlowWrapper(workFlowWrapper);

                switchWrapper.setBankId(sma.getBankId());
                switchWrapper.setPaymentModeId(sma.getPaymentModeId());
                switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

                switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());

                if (switchWrapper.getWorkFlowWrapper() != null) {
                    switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                }

                logger.info("[CommonCommandManager.checkAgentBalance] Going to Check Balance for AppUserID:" + appUserModel.getAppUserId());

                switchWrapper = this.switchController.checkBalance(switchWrapper);

                double balance = switchWrapper.getBalance();

                return balance;

            }

        } catch (Exception e) {
            throw new CommandException("Your account cannot be contacted. Please try again later.\n", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }

        logger.info("checkAgentBalance() Ended");

        return null;

    }

    public SearchBaseWrapper loadSmartMoneyAccount(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadSmartMoneyAccount()");
        }
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<SmartMoneyAccountModel> customList = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadSmartMoneyAccount()");
        }
        return searchBaseWrapper;
    }

    public VeriflyManager loadVeriflyManagerByAccountId(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadVeriflyManagerByAccountId()");
        }
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
        VeriflyManager veriflyManager = this.veriflyController.getVeriflyMgrByAccountId(smartMoneyAccountModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadVeriflyManagerByAccountId()");
        }
        return veriflyManager;
    }


    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setSwitchController(SwitchController switchController) {
        this.switchController = switchController;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setStakeholderBankInfoManager(
            StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setAppUserHistoryDao(AppUserHistoryDao appUserHistoryDao) {
        this.appUserHistoryDao = appUserHistoryDao;
    }

    public void setAccountHolderManager(AccountHolderManager accountHolderManager) {
        this.accountHolderManager = accountHolderManager;
    }

    public void setVeriflyController(VeriflyManagerService veriflyController) {
        this.veriflyController = veriflyController;
    }

    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }

    public void setHandlerManager(HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
    }

    public void setVerisysDataManager(VerisysDataManager verisysDataManager) {
        this.verisysDataManager = verisysDataManager;
    }

    public void setAccountInfoDAO(AccountInfoDAO accountInfoDAO) {
        this.accountInfoDAO = accountInfoDAO;
    }

    public void setCustomerRemitterDAO(CustomerRemitterDAO customerRemitterDAO) {
        this.customerRemitterDAO = customerRemitterDAO;
    }

    public void setCustTransManager(CustTransManager custTransManager) {
        this.custTransManager = custTransManager;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setDebitCardModelDAO(DebitCardModelDAO debitCardModelDAO) {
        this.debitCardModelDAO = debitCardModelDAO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setClsPendingAccountOpeningDAO(ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO) {
        this.clsPendingAccountOpeningDAO = clsPendingAccountOpeningDAO;
    }
}

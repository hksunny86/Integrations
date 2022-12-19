package com.inov8.microbank.debitcard.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.cardconfiguration.model.CardStateModel;
import com.inov8.microbank.cardconfiguration.model.CardStatusModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsDebitCardModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import com.inov8.microbank.server.dao.fetchcardtype.FetchCardTypeDAO;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.microbank.webapp.action.portal.mfsaccountmodule.MfsAccountController;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DebitCardChangeInfoController extends AdvanceAuthorizationFormController {

    private static final Logger LOGGER = Logger.getLogger(DebitCardChangeInfoController.class);

    private ReferenceDataManager referenceDataManager;
    private MfsAccountManager mfsAccountManager;
    private DebitCardManager debitCardManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private CommonCommandManager commonCommandManager;
    @Autowired
    private FetchCardTypeDAO fetchCardTypeDAO;

    public DebitCardChangeInfoController() {
        setCommandName("mfsDebitCardModel");
        setCommandClass(MfsDebitCardModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest request) throws Exception {
        Map referenceDataMap = new HashMap();
        List<CardStateModel> cardStateList = debitCardManager.getAllCardStates();
        List<CardStatusModel> cardStatusList = debitCardManager.getAllCardSatus();
        List<CardProdCodeModel> cardProductTypeList = debitCardManager.getAllCardProductTypes();

        referenceDataMap.put("cardStateList", cardStateList);
        referenceDataMap.put("cardStatusList", cardStatusList);
        referenceDataMap.put("cardProductTypeList", cardProductTypeList);

        return referenceDataMap;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
        String debitCardId = ServletRequestUtils.getStringParameter(request, "debitCardId");
        boolean isReSubmit = ServletRequestUtils.getBooleanParameter(request, "isReSubmit", false);

        DebitCardModel debitCardModel = getCommonCommandManager().getDebitCardModelDao().getDebitCardModelByDebitCardId(Long.valueOf(debitCardId));

        BaseWrapper baseWrapperBank = new BaseWrapperImpl();
        BankModel bankModel = new BankModel();
        bankModel.setBankId(CommissionConstantsInterface.BANK_ID);
        baseWrapperBank.setBasePersistableModel(bankModel);
        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
        boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
        request.setAttribute("veriflyRequired", veriflyRequired);

        /// Added for Resubmit Authorization Request
        if (isReSubmit) {
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "authId");
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

            if (actionAuthorizationModel.getCreatedById().longValue() != UserUtils.getCurrentUser().getAppUserId().longValue()) {
                throw new FrameworkCheckedException("illegal operation performed");
            }

            XStream xstream = new XStream();
            MfsDebitCardModel mfsDebitCardModel = (MfsDebitCardModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

            mfsDebitCardModel.setUsecaseId(actionAuthorizationModel.getUsecaseId());

            request.setAttribute("appUserId", mfsDebitCardModel.getAppUserId());

            request.setAttribute("pageMfsId", mfsDebitCardModel.getMfsId());
            return mfsDebitCardModel;
        }

        Long id = null;
        if (null != debitCardModel.getAppUserId() && debitCardModel.getAppUserId().toString().trim().length() > 0) {
            id = new Long(debitCardModel.getAppUserId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            AppUserModel appUserModel = new AppUserModel();
            appUserModel.setAppUserId(id);
            baseWrapper.setBasePersistableModel(appUserModel);
            baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);

            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();

            UserDeviceAccountsModel userDeviceModel = mfsAccountManager.getDeviceAccountByAppUserId(appUserModel.getAppUserId(), DeviceTypeConstantsInterface.ALL_PAY);
            Long catalogId = null;
            if (userDeviceModel != null) {
                catalogId = userDeviceModel.getProdCatalogId();
            }

            DebitCardRequestsViewModel debitCardRequestsViewModel = new DebitCardRequestsViewModel();
            debitCardRequestsViewModel = getCommonCommandManager().getDebitCardRequestsViewModelByDebitCardId(Long.valueOf(debitCardId));

            MfsDebitCardModel mfsDebitCardModel = new MfsDebitCardModel();
            mfsDebitCardModel.setEmbossingName(debitCardRequestsViewModel.getEmbossingName());
            mfsDebitCardModel.setNadraName(debitCardRequestsViewModel.getNadraName());
            mfsDebitCardModel.setCnic(debitCardRequestsViewModel.getCnic());
            mfsDebitCardModel.setMobileNo(debitCardRequestsViewModel.getMobileNo());
            mfsDebitCardModel.setMailingAddress(debitCardRequestsViewModel.getMailingAddress());
            mfsDebitCardModel.setCardNumber(debitCardRequestsViewModel.getCardNumber());
            mfsDebitCardModel.setCardStatusId(debitCardRequestsViewModel.getCardStatusId());
            mfsDebitCardModel.setCardStatus(debitCardRequestsViewModel.getCardStatus());
            mfsDebitCardModel.setCardProductCodeId(debitCardRequestsViewModel.getCardProductCodeId());
            mfsDebitCardModel.setCardProductType(debitCardRequestsViewModel.getCardProductType());
            mfsDebitCardModel.setCardStateId(debitCardRequestsViewModel.getCardStateId());
            mfsDebitCardModel.setCardState(debitCardRequestsViewModel.getCardState());
            mfsDebitCardModel.setSegmentName(debitCardRequestsViewModel.getSegmentName());
            mfsDebitCardModel.setSegmentId(debitCardRequestsViewModel.getSegmentId());
            mfsDebitCardModel.setChannelName(debitCardRequestsViewModel.getChannelName());
            mfsDebitCardModel.setCardProductType(debitCardRequestsViewModel.getCardProductType());
            mfsDebitCardModel.setCardTypeCode(debitCardRequestsViewModel.getCardTypeCode());
            mfsDebitCardModel.setMailingAddressId(debitCardRequestsViewModel.getMailingAddressId());
            mfsDebitCardModel.setAppUserId(debitCardRequestsViewModel.getAppUserId());

            request.setAttribute("pageMfsId", (String) baseWrapper.getObject("userId"));
            mfsDebitCardModel.setMfsId((String) baseWrapper.getObject("userId"));
            request.setAttribute("mailingAddressId", debitCardRequestsViewModel.getMailingAddressId());
            // for the logging process
            mfsDebitCardModel.setActionId(PortalConstants.ACTION_UPDATE);
            mfsDebitCardModel.setUsecaseId(new Long(PortalConstants.KEY_MFS_DEBIT_CARD_UPDATE_USECASE_ID));

            return mfsDebitCardModel;
        } else {
            MfsDebitCardModel mfsDebitCardModel = new MfsDebitCardModel();
            // for the logging process
            mfsDebitCardModel.setActionId(PortalConstants.ACTION_CREATE);
            mfsDebitCardModel.setUsecaseId(new Long(PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID));

//            Long appUserTypeId = this.appUserManager.getAppUserTypeId(UserUtils.getCurrentUser().getAppUserId());
//            mfsAccountModel.setAppUserTypeId(appUserTypeId);

            return mfsDebitCardModel;
        }
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AppUserModel appUserModel = null;
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo");
            appUserModel = new AppUserModel();
            appUserModel.setMobileNo(mobileNo);
//            baseWrapper.setBasePersistableModel(appUserModel);
            appUserModel = this.mfsAccountManager.getAppUserModelByMobileNumber(mobileNo);
//            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
            if (appUserModel.getNic() == null) {
                request.setAttribute("nicNullInDB", "true");
            }


//            SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
//            sma.setCustomerId(appUserModel.getCustomerId());
//            sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
//
//            SmartMoneyAccountModel smartMoneyAccountModel = this.mfsAccountManager.getSmartMoneyAccountByExample(sma);
//            if (smartMoneyAccountModel != null)
//                baseWrapper.putObject("smartMoneyAccountModel", smartMoneyAccountModel);

            MfsDebitCardModel mfsDebitCardModel = (MfsDebitCardModel) command;

            if (!validate(request, mfsDebitCardModel)) {
                return super.showForm(request, response, errors);
            }

            DebitCardRequestsViewModel debitCardRequestsViewModel = new DebitCardRequestsViewModel();
            debitCardRequestsViewModel = getCommonCommandManager().getDebitCardRequestsViewModelByAppUserId(appUserModel.getAppUserId(), mobileNo);

            mfsDebitCardModel.setMailingAddressId(debitCardRequestsViewModel.getMailingAddressId());

            mfsDebitCardModel.setAppUserId(appUserModel.getAppUserId());
            mfsDebitCardModel.setWithAuthFlag("1");

            baseWrapper.putObject(MfsDebitCardModel.MFS_DEBIT_CARD_MODEL_KEY, mfsDebitCardModel);
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsDebitCardModel.getActionId());
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsDebitCardModel.getUsecaseId());
            baseWrapper = this.mfsAccountManager.updateMfsDebitCard(baseWrapper);
        } catch (FrameworkCheckedException ex) {
            request.setAttribute("exceptionOccured", "true");
            if (appUserModel.getUsername() != null)
                request.setAttribute("pageMfsId", appUserModel.getUsername());

            MfsDebitCardModel mfsDebitCardModel = new MfsDebitCardModel();
            String msg = ex.getMessage();
            String[] args = {(String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ID)};

            if ("MobileNumUniqueException".equals(msg)) {
                this.saveMessage(request, super.getText("newMfsAccount.mobileNumNotUnique2", args, request.getLocale()));
            } else if ("NICUniqueException".equals(msg)) {
                this.saveMessage(request, super.getText("newMfsAccount.nicNotUnique2", args, request.getLocale()));
            } else {
                this.saveMessage(request, super.getText("newMfsAccount.unknown", request.getLocale()));
            }
            return super.showForm(request, response, errors);
        } catch (Exception exception) {
            request.setAttribute("exceptionOccured", "true");
            if (appUserModel.getUsername() != null)
                request.setAttribute("pageMfsId", appUserModel.getUsername());

//            MfsAccountModel mfsAccountModel = new MfsAccountModel();
//            Long appUserTypeId = this.appUserManager.getAppUserTypeId(UserUtils.getCurrentUser().getAppUserId());
//            mfsAccountModel.setAppUserTypeId(appUserTypeId);

            this.saveMessage(request, super.getText("newMfsAccount.unknown", request.getLocale()));
            return super.showForm(request, response, errors);
        }
        this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale()));
        ModelAndView modelAndView = new ModelAndView(new RedirectView("debitcardmanagement.html?actionId=3"));
        return modelAndView;
    }

    @Override
    protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        MfsDebitCardModel mfsDebitCardModel = (MfsDebitCardModel) command;

        if (!validate(request, mfsDebitCardModel)) {
            return super.showForm(request, response, errors);
        }

        ModelAndView modelAndView = null;
        AppUserModel appUserModel = null;
        Long appUserId = null;
        String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo");

        boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(request, "resubmitRequest", false);
        Long actionAuthorizationId = null;
        if (resubmitRequest)
            actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");

//        if (null != appUserIdStr && appUserIdStr.trim().length() > 0) {
//            if (!resubmitRequest)
//                appUserId = new Long(ServletRequestUtils.getStringParameter(request, "appUserId"));
//            else
//                appUserId = Long.parseLong(appUserIdStr);
//        }

        appUserModel = new AppUserModel();
        appUserModel.setMobileNo(mobileNo);
//        baseWrapper.setBasePersistableModel(appUserModel);
        appUserModel = this.mfsAccountManager.getAppUserModelByMobileNumber(mobileNo);
//        appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        if (appUserModel.getNic() == null) {
            request.setAttribute("nicNullInDB", "true");
        }

        DebitCardRequestsViewModel debitCardRequestsViewModel = new DebitCardRequestsViewModel();
        debitCardRequestsViewModel = getCommonCommandManager().getDebitCardRequestsViewModelByAppUserId(appUserModel.getAppUserId(), mobileNo);

        mfsDebitCardModel.setMailingAddressId(debitCardRequestsViewModel.getMailingAddressId());
        CardProdCodeModel cardProdCodeModel = fetchCardTypeDAO.findByPrimaryKey(mfsDebitCardModel.getCardProductCodeId());


        mfsDebitCardModel.setCardProductType(cardProdCodeModel.getCardProductName());
        mfsDebitCardModel.setAppUserId(appUserModel.getAppUserId());

        try {
            XStream xstream = new XStream();

            MfsDebitCardModel mfsDebitCardModelAuth = (MfsDebitCardModel) mfsDebitCardModel.clone();

            String refDataModelString = xstream.toXML(mfsDebitCardModelAuth);

            UsecaseModel usecaseModel = usecaseFacade.loadUsecase(mfsDebitCardModel.getUsecaseId());
            Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(mfsDebitCardModel.getUsecaseId(), new Long(0));

            if (appUserModel.getAppUserId() == null || appUserModel.getAppUserId() < 1) {
                AppUserModel _appUserModel = new AppUserModel();
                _appUserModel.setMobileNo(mfsDebitCardModel.getMobileNo());
                _appUserModel.setNic(mfsDebitCardModel.getCnic());

                mfsAccountManager.isUniqueCNICMobile(_appUserModel, baseWrapper);
            }

            if (nextAuthorizationLevel.intValue() < 1) {
                baseWrapper.putObject(MfsDebitCardModel.MFS_DEBIT_CARD_MODEL_KEY, mfsDebitCardModel);
                baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsDebitCardModel.getActionId());
                baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsDebitCardModel.getUsecaseId());

                baseWrapper = this.mfsAccountManager.updateMfsDebitCard(baseWrapper);

                actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel, "", refDataModelString, null, usecaseModel, actionAuthorizationId, request);

                if (mfsDebitCardModel.getUsecaseId().longValue() == PortalConstants.KEY_MFS_DEBIT_CARD_UPDATE_USECASE_ID) {

                    this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale())
                            + "Action is authorized successfully. Changes are saved against refernce Action ID : " + actionAuthorizationId);
                    modelAndView = new ModelAndView(new RedirectView("debitcardmanagement.html?actionId=3"));
                } else {
                    this.saveMessage(request, super.getText("newMfsAccount.recordSaveSuccessful", request.getLocale())
                            + "Action is authorized successfully. Changes are saved against refernce Action ID : " + actionAuthorizationId);
                    String eappUserId = appUserId.toString();
                    modelAndView = new ModelAndView(this.getSuccessView() + "&" + PortalConstants.KEY_APP_USER_ID + "=" + eappUserId);
                }
            } else {
                AppUserModel aum = new AppUserModel();

                actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel, "", refDataModelString, null, usecaseModel.getUsecaseId(), mfsDebitCardModel.getMobileNo(), resubmitRequest, actionAuthorizationId, request);
                this.saveMessage(request, "Action is pending for approval against reference Action ID : " + actionAuthorizationId);
                modelAndView = new ModelAndView(new RedirectView("debitcardmanagement.html?actionId=3"));
            }
        } catch (FrameworkCheckedException ex) {
            String msg = ex.getMessage();
            String[] args = {(String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ID)};
            if ("MobileNumUniqueException".equals(msg)) {
                this.saveMessage(request, super.getText("newMfsAccount.mobileNumNotUnique2", args, request.getLocale()));
            } else if ("NICUniqueException".equals(msg)) {
                this.saveMessage(request, super.getText("newMfsAccount.nicNotUnique2", args, request.getLocale()));
            } else if (msg.contains("Action authorization request already exist")) {
                this.saveMessage(request, msg);
            } else {
                this.saveMessage(request, super.getText("newMfsAccount.unknown", request.getLocale()));
            }
            return super.showForm(request, response, errors);
        } catch (Exception exception) {
            this.saveMessage(request, MessageUtil.getMessage("6075"));
            return super.showForm(request, response, errors);
        }
        return modelAndView;
    }

    private boolean validate(HttpServletRequest req, MfsDebitCardModel mfsDebitCardModel) {
        boolean flag = true;
        if (mfsDebitCardModel.getMobileNo() == null) {
            this.saveMessage(req, "Mobile No: is required.");
            flag = false;
        }
        if (mfsDebitCardModel.getNadraName() == null) {
            this.saveMessage(req, "Account Title: is required.");
            flag = false;
        }
        if (mfsDebitCardModel.getCnic() == null) {
            this.saveMessage(req, "CNIC #: is required.");
            flag = false;
        }
        if (mfsDebitCardModel.getEmbossingName() == null) {
            this.saveMessage(req, "Embosing Name: is required.");
            flag = false;
        }

        if (mfsDebitCardModel.getCardNumber() == null) {
            this.saveMessage(req, "Card Number: is required.");
            flag = false;
        }

        if (mfsDebitCardModel.getMailingAddress() == null) {
            this.saveMessage(req, "Mailing Address: is required.");
            flag = false;
        }
        if (this.getCommonCommandManager().isCnicBlacklisted(mfsDebitCardModel.getCnic())) {
            this.saveMessage(req, MessageUtil.getMessage("walkinAccountBlacklisted"));
            flag = false;
        }

//        if (mfsDebitCardModel.getCardState() == null) {
//            this.saveMessage(req, "Card State: is required.");
//            flag = false;
//        }
//        if (mfsDebitCardModel.getCardStatus() == null) {
//            this.saveMessage(req, "Card Status: is required.");
//            flag = false;
//        }
        return flag;
    }


    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    @Override
    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setDebitCardManager(DebitCardManager debitCardManager) {
        this.debitCardManager = debitCardManager;
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setFetchCardTypeDAO(FetchCardTypeDAO fetchCardTypeDAO) {
        this.fetchCardTypeDAO = fetchCardTypeDAO;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
}

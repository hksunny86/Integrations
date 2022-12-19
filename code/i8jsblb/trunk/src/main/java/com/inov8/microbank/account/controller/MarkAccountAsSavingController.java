package com.inov8.microbank.account.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.account.vo.AccountSavingMarkVo;
import com.inov8.microbank.account.vo.BlacklistMarkingVo;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountClosureFacade;
import com.inov8.microbank.server.service.bulkdisbursements.CustomerPendingTrxManager;
import com.inov8.microbank.server.service.customermodule.CustomerManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManagerImpl;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.accountholder.AccountHolderManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Malik on 8/22/2016.
 */
public class MarkAccountAsSavingController extends AdvanceFormController {

    private CustomerDAO customerDAO;
    private CommonCommandManager commonCommandManager;
    private MfsAccountManager mfsAccountManager;
    private AppUserManager appUserManager;
    private SmsSender smsSender;
    private AccountHolderManager accountHolderManager;
    private AccountManager accountManager;
    private String appUserId;
    private ReferenceDataManager referenceDataManager;

    public MarkAccountAsSavingController() {
        setCommandName("appUserModel");
        setCommandClass(AppUserModel.class);
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
        appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
        String customerId = ServletRequestUtils.getStringParameter(req, "customerId");
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AppUserModel appUserModel = new AppUserModel();

        if (null != appUserId) {
            appUserModel.setAppUserId(Long.valueOf(EncryptionUtil.decryptForAppUserId(appUserId)));
            baseWrapper.setBasePersistableModel(appUserModel);
            baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        }

        if (null != customerId) {
            appUserModel.setCustomerId(Long.valueOf(customerId));
        }

        return appUserModel;

    }

    @Override
    protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
//        ArrayList<String> list = new ArrayList();

        AppUserModel appUserModel1 = appUserManager.loadAppUser(Long.parseLong(appUserId));

        CustomerModel customerModel = this.customerDAO.findByPrimaryKey(appUserModel1.getCustomerId());
        List<String> customerAccountTypeList = new ArrayList<>();
        if(customerModel.getAcNature().equals(1L)){
            customerAccountTypeList.add("Saving");
        }
        if(customerModel.getAcNature().equals(2L)){
            customerAccountTypeList.add("Current");
        }

        Map referenceDataMap = new HashMap();

        referenceDataMap.put("customeraccountTypeList", customerAccountTypeList);

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
        ModelAndView modelAndView = null;
        CustomerModel customerModel = new CustomerModel();
        AppUserModel appUserModel = (AppUserModel) obj;
        try {
            AppUserModel appUserModel1 = appUserManager.loadAppUser(appUserModel.getAppUserId());
            customerModel = this.customerDAO.findByPrimaryKey(appUserModel1.getCustomerId());
            String acType = ServletRequestUtils.getStringParameter(req, "acType");
            if (acType.equals("[Current]")) {
                customerModel.setAcNature(1L);
            }
            if (acType.equals("[Saving]")) {
                customerModel.setAcNature(2L);
            }

            customerModel.setUpdatedOn(new Date());
            customerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            String mfsId = ServletRequestUtils.getStringParameter(req, "customerId");
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
            baseWrapper.putObject("mfsId", mfsId);

            AccountHolderModel accountHolderModel = null;
            AccountModel accountModel = this.accountManager.getAccountModelByCNIC(appUserModel1.getNic());
            if (accountModel != null) {
                if (accountModel.getAccountHolderId() != null) {
                    BaseWrapper accountHolderWrapper = new BaseWrapperImpl();
                    AccountHolderModel acHolderModel = new AccountHolderModel();
                    acHolderModel.setAccountHolderId(accountModel.getAccountHolderId());
                    accountHolderWrapper.setBasePersistableModel(acHolderModel);
                    accountHolderWrapper = this.accountHolderManager.loadAccountHolder(accountHolderWrapper);

                    accountHolderModel = (AccountHolderModel) accountHolderWrapper.getBasePersistableModel();
                    accountHolderModel.setActive(Boolean.FALSE);
                }
                accountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE);
            }

            baseWrapper.putObject("accountHolderModel", accountHolderModel);
            baseWrapper.putObject("accountModel", accountModel);
            baseWrapper.putObject("customerModel", customerModel);
            baseWrapper.setBasePersistableModel(appUserModel1);

            if (customerModel.getAcNature().equals(1L) || customerModel.getAcNature().equals(2L))
                appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
            else
                appUserModel = appUserModel1;
            if (appUserModel != null && customerModel.getAcNature().equals(2L)) {
                logger.info("[MarkAccountAsSavingController.onUpdate] Sending SMS to customer after marking the acount as saving successfully. MobileNo:" + appUserModel.getMobileNo());
                String messageString = "Dear Customer, your account has been marked to Saving.";//MessageUtil.getMessage("smsCommand.close_customer");
                SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString);

                try {

                    smsSender.send(smsMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("[MarkAccountAsSavingController.onUpdate] Exception while sending SMS to Mobile No:" + appUserModel.getMobileNo());
                }
            }
            if (appUserModel != null && customerModel.getAcNature().equals(1L)) {
                logger.info("[MarkAccountAsSavingController.onUpdate] Sending SMS to customer after marking the acount as Current successfully. MobileNo:" + appUserModel.getMobileNo());
                String messageString = "Dear Customer, your account has been marked to Current.";//MessageUtil.getMessage("smsCommand.close_customer");
//                String messageString = "Dear Customer, your " + acType + " account has been marked as Current successfully.";//MessageUtil.getMessage("smsCommand.close_customer");
                SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString);

                try {

                    smsSender.send(smsMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("[MarkAccountAsSavingController.onUpdate] Exception while sending SMS to Mobile No:" + appUserModel.getMobileNo());
                }
            }
            Map<String, String> map = new HashMap<String, String>();
            if (acType.equals("[Saving]") || acType.equals("[Current]") ) {
                map.put("status", "success");
                modelAndView = new ModelAndView(this.getSuccessView() + "?status=success&appUserId=" + appUserModel.getAppUserId(), map);
                Long id = appUserModel.getAppUserId();
                appUserModel = this.mfsAccountManager.getAppUserModelByPrimaryKey(id);
                baseWrapper.setBasePersistableModel(appUserModel);
                this.appUserManager.saveOrUpdateAppUser(baseWrapper);
                this.commonCommandManager.saveOrUpdateCustomerModel(customerModel);
            } else {
                map.put("status", "success");
                modelAndView = new ModelAndView(this.getSuccessView() + "?status=success&appUserId=" + appUserModel.getAppUserId(), map);
            }
        } catch (Exception ex) {
            req.setAttribute("message", "Account Cannot be Mark As Saving or Current, Kindly consult administrator for details.");
            req.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(req, res, errors);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalActionLog.remove();
        }
         return modelAndView;
    }


    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public AppUserManager getAppUserManager() {
        return appUserManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setAccountHolderManager(AccountHolderManager accountHolderManager) {
        this.accountHolderManager = accountHolderManager;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }
}

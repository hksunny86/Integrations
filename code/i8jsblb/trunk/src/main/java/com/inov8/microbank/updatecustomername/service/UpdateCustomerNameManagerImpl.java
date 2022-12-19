package com.inov8.microbank.updatecustomername.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.digidormancyaccountmodule.DigiDormancyAccountViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.updatecustomername.dao.UpdateCustomerNameDAO;
import com.inov8.microbank.updatecustomername.facade.UpdateCustomerNameFacade;
import com.inov8.microbank.updatecustomername.model.UpdateCustomerNameModel;
import com.inov8.microbank.updatecustomername.vo.UpdateCustomerNameVo;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.context.ContextLoader;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UpdateCustomerNameManagerImpl implements UpdateCustomerNameManager {

    UpdateCustomerNameDAO updateCustomerNameDAO;
    ActionLogManager actionLogManager;
    UpdateCustomerNameFacade updateCustomerNameFacade;
    private CustTransManager custTransManager;
    private MessageSource messageSource;

    @Override
    public SearchBaseWrapper searchUpdateCustomerNames(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        UpdateCustomerNameModel model = (UpdateCustomerNameModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<UpdateCustomerNameModel> customList = updateCustomerNameDAO.findByExample(model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(), new ExampleConfigHolderModel(false, true, false, MatchMode.EXACT));
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }


    @Override
    public UpdateCustomerNameModel getUpdateCustomer(String cnic) {
        return updateCustomerNameDAO.getCustomerNameAndNameUpdate(cnic, false);
    }

    @Override
    public BaseWrapper saveOrUpdateCustomerName(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        UpdateCustomerNameModel updateCustomerNameModel = (UpdateCustomerNameModel) baseWrapper.getBasePersistableModel();
        try {
            updateCustomerNameDAO.saveOrUpdate(updateCustomerNameModel);
        } catch (DataIntegrityViolationException e) {
            throw new FrameworkCheckedException("Exception occured while creating/updating the UpdateCustomerName", e);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        return baseWrapper;
    }

    @Override
    public BaseWrapper updateCustomer(BaseWrapper baseWrapper, UpdateCustomerNameVo updateCustomerNameVo) throws FrameworkCheckedException {
        String actionLogHandler = (String) baseWrapper.getObject(CommandFieldConstants.KEY_ACTION_LOG_HANDLER);

        ActionLogModel actionLogModel = new ActionLogModel();
        if (actionLogHandler == null) {
            actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
            actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
            actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);  // the process is starting
            actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
            actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
            actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
            actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
            actionLogModel = logAction(actionLogModel);
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }

        AppUserModel appUserModel = getCommonCommandManager().loadAppUserByCnicAndType(updateCustomerNameVo.getCnic());
        if (appUserModel != null) {
            CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());

            baseWrapper = new BaseWrapperImpl();
            appUserModel.setFirstName(updateCustomerNameVo.getFirstName());
            appUserModel.setLastName(updateCustomerNameVo.getLastName());
            appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            appUserModel.setUpdatedOn(new Date());
            baseWrapper.setBasePersistableModel(appUserModel);
            getCommonCommandManager().getAppUserManager().saveOrUpdateAppUser(baseWrapper);

            baseWrapper = new BaseWrapperImpl();
            customerModel.setName(updateCustomerNameVo.getFirstName() + " " + updateCustomerNameVo.getLastName());
            customerModel.setUpdatedOn(new Date());
            customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            baseWrapper.setBasePersistableModel(customerModel);
            baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
            baseWrapper = new BaseWrapperImpl();
            UpdateCustomerNameModel updateCustomerNameModel = new UpdateCustomerNameModel();
            updateCustomerNameModel = updateCustomerNameFacade.getUpdateCustomer(updateCustomerNameVo.getCnic());

            updateCustomerNameModel.setUpdated(true);
            updateCustomerNameModel.setUpdateCustomerNameId(updateCustomerNameVo.getUpdateCustomerNameId());
            updateCustomerNameModel.setUpdatedOn(new Date());
            updateCustomerNameModel.setNadraName(updateCustomerNameVo.getFirstName() + " " + updateCustomerNameVo.getLastName());
            updateCustomerNameModel.setActionStatusId(updateCustomerNameVo.getActionStatus());

            baseWrapper.setBasePersistableModel(updateCustomerNameModel);

            updateCustomerNameFacade.saveOrUpdateCustomerName(baseWrapper);
            String customerSMS = this.getMessageSource().getMessage("update.customer.name.request.Update", null, null);
            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(appUserModel.getMobileNo(), customerSMS));
            getCommonCommandManager().sendSMSToUser(baseWrapper);
        }

        return baseWrapper;
    }

    private ActionLogModel logAction(ActionLogModel actionLogModel)
            throws FrameworkCheckedException {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(actionLogModel);
        if (null == actionLogModel.getActionLogId()) {
            baseWrapper =
                    this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
        } else {
            baseWrapper =
                    this.actionLogManager.createOrUpdateActionLog(baseWrapper);
        }
        return (ActionLogModel) baseWrapper.getBasePersistableModel();
    }

    public void setUpdateCustomerNameDAO(UpdateCustomerNameDAO updateCustomerNameDAO) {
        this.updateCustomerNameDAO = updateCustomerNameDAO;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setUpdateCustomerNameFacade(UpdateCustomerNameFacade updateCustomerNameFacade) {
        this.updateCustomerNameFacade = updateCustomerNameFacade;
    }

    public void setCustTransManager(CustTransManager custTransManager) {
        this.custTransManager = custTransManager;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
}

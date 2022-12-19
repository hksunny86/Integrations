package com.inov8.microbank.webapp.action.portal.agentsegmentrestrictionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionDAO;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.webapp.action.portal.bigreports.ReportCriteriaSessionObject;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class AgentSegmentDeActiveController extends BaseFormSearchController {
    private ReferenceDataManager referenceDataManager;
    private DeviceTypeManager deviceTypeManager;
    private CommonCommandManager commonCommandManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private AppUserModel appUserModel;
    private RetailerContactModel retailerContactModel;
    private AgentSegmentRestrictionDAO agentSegmentRestrictionDAO;

    private AgentSegmentRestrictionManager agentSegmentRestrictionManager;
    private ProductManager productManager;
    private RetailerContactManager retailerContactManager;

    public AgentSegmentDeActiveController() {
        setCommandClass(AgentSegmentRestriction.class);
        setCommandName("AgentSegmentDeActiveController");

    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {


        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        pagingHelperModel.setTotalRecordsCount(0);

        AgentSegmentRestriction agentSegmentRestriction = (AgentSegmentRestriction) o;
//
//
//        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
//        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
//        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
//        exampleConfigHolderModel.setIgnoreCase(Boolean.FALSE);
//        exampleConfigHolderModel.setExcludeZeroes(Boolean.TRUE);
//        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
//        CustomList<MerchantDiscountCardModel> list =
////                vcFileDAO.findAll(searchBaseWrapper.getPagingHelperModel());
//                this.age.findByExample(vcFileModel,
//                        searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(),
//                        exampleConfigHolderModel);

        agentSegmentRestriction.setIsActive(true);
        agentSegmentRestriction.setAgentID(agentSegmentRestriction.getAgentID());
        BaseWrapperImpl baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(agentSegmentRestriction);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        CustomList<AgentSegmentRestriction> list =
//                vcFileDAO.findAll(searchBaseWrapper.getPagingHelperModel());
                this.agentSegmentRestrictionDAO.findByExample(agentSegmentRestriction,
                        searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(),
                        exampleConfigHolderModel);
        return new ModelAndView(getSuccessView(), "reqList", list.getResultsetList());

    }

//    @Override
//    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
//        return new AgentSegmentRestriction();
//    }
//
//    @Override
//    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
//
//        RetailerContactModel retContactModel = new RetailerContactModel();
//
//        BaseWrapper baseWrapper = new BaseWrapperImpl();
//        String initialApplicationFormNumber;
//        Boolean securityCheck = Boolean.FALSE;
//        AgentSegmentRestriction agentSegmentRestriction = (AgentSegmentRestriction) o;
//        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
//        RetailerContactModel retailerModel = null;
//
//        SegmentModel segmentModel = null;
//
//        segmentModel = getCommonCommandManager().getSegmentDao().findByPrimaryKey(agentSegmentRestriction.getSegmentId());
//
//        userDeviceAccountsModel = getCommonCommandManager().loadUserDeviceAccountByUserId(agentSegmentRestriction.getAgentID());
//        if (userDeviceAccountsModel == null) {
//            this.saveMessage(httpServletRequest, "Agent Not Exist");
//            return super.showForm(httpServletRequest, httpServletResponse, e);
//        } else {
//            appUserModel = getRetailerAppUserModel(userDeviceAccountsModel.getAppUserId());
//            if (appUserModel==null){
//                appUserModel = getRetailerAppUserModel1(userDeviceAccountsModel.getAppUserId());
//                HandlerModel handlerModel = new HandlerModel();
//                handlerModel.setHandlerId(appUserModel.getHandlerId());
//                baseWrapper.setBasePersistableModel(handlerModel);
//                this.getCommonCommandManager().loadHandler(baseWrapper);
//                handlerModel = (HandlerModel)baseWrapper.getBasePersistableModel();
//                this.appUserModel = this.getCommonCommandManager().loadAppUserByRetailerContractId(handlerModel.getRetailerContactId());
//                retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
//                baseWrapper.setBasePersistableModel(retContactModel);
//                baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
//                retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
//            }else {
//                appUserModel.getRetailerContactId();
//                retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
//                baseWrapper.setBasePersistableModel(retContactModel);
//                baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
//                retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
//            }
//
//        }
//        if (appUserModel == null) {
//            this.saveMessage(httpServletRequest, "Agent Not Exist In App User");
//            return super.showForm(httpServletRequest, httpServletResponse, e);
//        } else if (appUserModel.getRegistrationStateId() == null
//                || (appUserModel.getRegistrationStateId() != null && (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT)
//                || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REJECTED)
//                || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DECLINE)
//                || appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT)
//                || appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.CLOSED)))) {
//            this.saveMessage(httpServletRequest, "ACCOUNT INVALID STATE");
//            return super.showForm(httpServletRequest, httpServletResponse, e);
//        } else if (!retailerContactModel.getActive()) {
//            this.saveMessage(httpServletRequest, "ACCOUNT INVALID STATE");
//            return super.showForm(httpServletRequest, httpServletResponse, e);
//        } else if (userDeviceAccountsModel.getAccountLocked()) {
//            this.saveMessage(httpServletRequest, "Account Blocked");
//            return super.showForm(httpServletRequest, httpServletResponse, e);
//        } else if (!userDeviceAccountsModel.getAccountEnabled()) {
//            this.saveMessage(httpServletRequest, "Account Deactivated");
//            return super.showForm(httpServletRequest, httpServletResponse, e);
//        } else if (this.getCommonCommandManager().isCnicBlacklisted(appUserModel.getNic())) {
//            this.saveMessage(httpServletRequest, "Cnic BlackListed");
//            return super.showForm(httpServletRequest, httpServletResponse, e);
//        } else {
//
//            agentSegmentRestriction.setRetailerContactId(retailerContactModel.getRetailerContactId());
//            agentSegmentRestriction.setRetailerId(retailerContactModel.getRetailerId());
//            agentSegmentRestriction.setCreatedOn(new Date());
//            agentSegmentRestriction.setUpdatedOn(new Date());
//            agentSegmentRestriction.setIsActive(true);
//            agentSegmentRestriction.setName(segmentModel.getName());
//            baseWrapper.setBasePersistableModel(agentSegmentRestriction);
//            Boolean result = this.agentSegmentRestrictionManager.checkAgentSegmentRestriction(baseWrapper);
//            if (result.equals(true)) {
//                this.saveMessage(httpServletRequest, "AgentSegment with the same Data already exists.");
//                return super.showForm(httpServletRequest, httpServletResponse, e);
//            } else {
//                baseWrapper = this.agentSegmentRestrictionManager.createAgentSegmentRestriction(baseWrapper);
//                if (null != baseWrapper.getBasePersistableModel()) {
//                    String msg = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
//                    if (null == msg) {
//                        msg = "Record saved successfully";
//                    }
//                    this.saveMessage(httpServletRequest, msg);
//                    Long productId = null;
//                    Object basePersistableModel = baseWrapper.getBasePersistableModel();
//                    if (basePersistableModel instanceof AgentSegmentRestriction) {
//                        AgentSegmentRestriction agentSegmentRestriction1 = (AgentSegmentRestriction) basePersistableModel;
//                        productId = agentSegmentRestriction1.getAgentSegmentExceptionId();
//
//                        ModelAndView modelAndView = new ModelAndView("redirect:p_agentsegmentrestriction.html?productId=" + productId);
//                        return modelAndView;
//                    } else {
//                        ModelAndView modelAndView = new ModelAndView(getSuccessView());
//                        return modelAndView;
//                    }
//
//                } else {
//                    this.saveMessage(httpServletRequest, "Data Not.");
//                    return super.showForm(httpServletRequest, httpServletResponse, e);
//                }
//            }
//
//
//        }
//    }
//
//    @Override
//    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
//        return null;
//    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public DeviceTypeManager getDeviceTypeManager() {
        return deviceTypeManager;
    }

    public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
        this.deviceTypeManager = deviceTypeManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

//    public void setAgentSegmentRestrictionManager(AgentSegmentRestrictionManager agentSegmentRestrictionManager) {
//        this.agentSegmentRestrictionManager = agentSegmentRestrictionManager;
//    }

    private AppUserModel getRetailerAppUserModel(Long appUserId) {

        AppUserModel _appUserModel = new AppUserModel();
        try {

            _appUserModel = getCommonCommandManager().loadRetailerAppUserModelByAppUserId(appUserId);

        } catch (FrameworkCheckedException e) {

            e.printStackTrace();
        }
        return _appUserModel;
    }

    private AppUserModel getRetailerAppUserModel1(Long appUserId) {

        AppUserModel _appUserModel = new AppUserModel();
        try {

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            _appUserModel.setAppUserId(appUserId);
            baseWrapper.setBasePersistableModel(_appUserModel);
            baseWrapper = getCommonCommandManager().loadAppUser(baseWrapper);
            _appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();

        } catch (FrameworkCheckedException e) {

            e.printStackTrace();
        }
        return _appUserModel;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }


    private CommonCommandManager getCommonCommandManager() {
        ApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        CommonCommandManager commonCommandManager = (CommonCommandManager) webApplicationContext.getBean("commonCommandManager");
        return commonCommandManager;
    }


    public void setAgentSegmentRestrictionDAO(AgentSegmentRestrictionDAO agentSegmentRestrictionDAO) {
        this.agentSegmentRestrictionDAO = agentSegmentRestrictionDAO;
    }

    public void setRetailerContactManager(
            RetailerContactManager retailerContactManager) {
        this.retailerContactManager = retailerContactManager;
    }
}

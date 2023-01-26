package com.inov8.microbank.webapp.action.portal.transactiondetail;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.CustomerDeviceVerification;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerDeviceVerificationModel;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.CustomerUDIDDeviceVerification;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.CustomerDeviceVerificationDAO;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomerDeviceVerificationUpdateController extends AdvanceFormController {

    private ReferenceDataManager referenceDataManager;
    private ESBAdapter esbAdapter;
    private CustomerDeviceVerificationDAO customerDeviceVerificationDAO;
    private AgentSegmentRestrictionManager agentSegmentRestrictionManager;




    public CustomerDeviceVerificationUpdateController() {
        setCommandClass(CustomerDeviceVerificationModel.class);
        setCommandName("CustomerDeviceVerificationUpdateController");
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map referenceDataMap = new HashMap();

        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
        String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo");
        String deviceId = ServletRequestUtils.getStringParameter(request, "deviceID");
        String deviceName = ServletRequestUtils.getStringParameter(request, "deviceName");

        CustomerDeviceVerificationModel customerDeviceVerificationModel = new CustomerDeviceVerificationModel();

        customerDeviceVerificationModel.setMobileNo(mobileNo);
        customerDeviceVerificationModel.setId(deviceId);
        customerDeviceVerificationModel.setDeviceName(deviceName);

        return customerDeviceVerificationModel;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CustomerDeviceVerificationModel refferalCustomerModel = (CustomerDeviceVerificationModel) command;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel();
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        AppUserModel appUserModel = new AppUserModel();

        Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER, UserTypeConstantsInterface.HANDLER};
        AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(refferalCustomerModel.getMobileNo(), appUserTypeIds);

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        esbAdapter = new ESBAdapter();
        String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
        String stan = String.valueOf((new Random().nextInt(90000000)));
        requestVO = esbAdapter.prepareCustomerDeviceVerificationRequest(I8SBConstants.RequestType_UPDATE_CUSTOMER_DEVICE_DETAIL);
        requestVO.setMobileNumber(refferalCustomerModel.getMobileNo());
        requestVO.setIdCode(refferalCustomerModel.getId());
        if (refferalCustomerModel.getApprovalStatus().equals("ACTIVE")) {
            requestVO.setStatus("A");
        } else {
            requestVO.setStatus("P");
        }
        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
        if (!responseVO.getResponseCode().equals("I8SB-200")) {
            this.saveMessage(request, super.getText("nnewMfsAccount.recordSaveUnSuccessful", request.getLocale()));
        }
        CustomerUDIDDeviceVerification customerDeviceVerification = null;
        customerDeviceVerification = new CustomerUDIDDeviceVerification();

        customerDeviceVerification.setMobileNo(refferalCustomerModel.getMobileNo());
        customerDeviceVerification.setDeviceName(refferalCustomerModel.getDeviceName());
        customerDeviceVerification.setUnquiIdentifier(refferalCustomerModel.getId());
        customerDeviceVerification.setRemarks(refferalCustomerModel.getRemarks());
        customerDeviceVerification.setCreatedOn(new Date());
        customerDeviceVerification.setUpdatedOn(new Date());
        customerDeviceVerification.setCreatedBy(UserUtils.getCurrentUser().getAppUserId().toString());
        customerDeviceVerification.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId().toString());
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(customerDeviceVerification);
        customerDeviceVerificationDAO.saveOrUpdate(customerDeviceVerification);
        this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale()));
        ModelAndView modelAndView = new ModelAndView(new RedirectView("p_deviceverification.html?actionId=3"));

        return modelAndView;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
//        CustomerDeviceVerificationModel refferalCustomerModel = (CustomerDeviceVerificationModel) command;
//        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel();
//        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
//        dateRangeHolderModels.add(dateRangeHolderModel);
//        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
//        AppUserModel appUserModel = new AppUserModel();
//
//        Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER, UserTypeConstantsInterface.HANDLER};
//        AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(refferalCustomerModel.getMobileNo(), appUserTypeIds);
//
//        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
//        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
//        esbAdapter = new ESBAdapter();
//        String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
//        String stan = String.valueOf((new Random().nextInt(90000000)));
//        requestVO = esbAdapter.prepareCustomerDeviceVerificationRequest(I8SBConstants.RequestType_UPDATE_CUSTOMER_DEVICE_DETAIL);
//        requestVO.setMobileNumber(refferalCustomerModel.getMobileNo());
//        requestVO.setIdCode(refferalCustomerModel.getId());
//        if (refferalCustomerModel.getApprovalStatus().equals("ACTIVE")) {
//            requestVO.setStatus("A");
//        } else {
//            requestVO.setStatus("P");
//        }
//        SwitchWrapper sWrapper = new SwitchWrapperImpl();
//        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
//        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//        if (!responseVO.getResponseCode().equals("I8SB-200")) {
//            this.saveMessage(request, super.getText("nnewMfsAccount.recordSaveUnSuccessful", request.getLocale()));
//        }
//
//        this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale()));
//        ModelAndView modelAndView = new ModelAndView(new RedirectView("p_deviceverification.html?actionId=3"));
//
//        return modelAndView;
        return null;
    }



    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }


    private CommonCommandManager getCommonCommandManager() {
        ApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        CommonCommandManager commonCommandManager = (CommonCommandManager) webApplicationContext.getBean("commonCommandManager");
        return commonCommandManager;
    }

    public void setCustomerDeviceVerificationDAO(CustomerDeviceVerificationDAO customerDeviceVerificationDAO) {
        this.customerDeviceVerificationDAO = customerDeviceVerificationDAO;
    }

    public void setAgentSegmentRestrictionManager(AgentSegmentRestrictionManager agentSegmentRestrictionManager) {
        this.agentSegmentRestrictionManager = agentSegmentRestrictionManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}

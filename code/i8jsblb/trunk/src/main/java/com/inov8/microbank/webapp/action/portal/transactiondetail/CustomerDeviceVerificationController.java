package com.inov8.microbank.webapp.action.portal.transactiondetail;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.CustomerDeviceVerification;
import com.inov8.integration.i8sb.vo.Data;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerDeviceVerificationModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.RefferalCustomerModel;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.CustomerUDIDDeviceVerification;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.CustomerDeviceVerificationDAO;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomerDeviceVerificationController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private ESBAdapter esbAdapter;


    public CustomerDeviceVerificationController() {
        setCommandClass(CustomerDeviceVerificationModel.class);
        setCommandName("CustomerDeviceVerificationController");
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                    PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {

        CustomerDeviceVerificationModel refferalCustomerModel = (CustomerDeviceVerificationModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel();
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER, UserTypeConstantsInterface.HANDLER};
        AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(refferalCustomerModel.getMobileNo(), appUserTypeIds);
        CustomerModel customerModel = new CustomerModel();
        if (customerAppUserModel != null) {

            customerModel = getCommonCommandManager().getCustomerModelById(customerAppUserModel.getCustomerId());
        }

        if (linkedHashMap.isEmpty()) {
            linkedHashMap.put("sender_mobile", SortingOrder.DESC);

        }

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        esbAdapter = new ESBAdapter();
        String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
        String stan = String.valueOf((new Random().nextInt(90000000)));
        requestVO = esbAdapter.prepareCustomerDeviceVerificationRequest(I8SBConstants.RequestType_GET_CUSTOMER_DEVICE_DETAIL);
        requestVO.setMobileNumber(refferalCustomerModel.getMobileNo());
        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
        if (!responseVO.getResponseCode().equals("I8SB-200")) {
            throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }

        ArrayList<?> data = new ArrayList<>();
        CustomList<CustomerDeviceVerificationModel> list = new CustomList<>();
        ArrayList<CustomerDeviceVerificationModel> refferalCustomerModelsLi = new ArrayList<>();
        data = (ArrayList<?>) responseVO.getCollectionOfList().get("asa");
        CustomerUDIDDeviceVerification customerDeviceVerification = null;
        if (data != null) {
            List<?> associatedAccountList = data;
            for (int i = 0; i < associatedAccountList.size(); i++) {
                refferalCustomerModel = new CustomerDeviceVerificationModel();
                customerDeviceVerification = new CustomerUDIDDeviceVerification();

                refferalCustomerModel.setMobileNo(((CustomerDeviceVerification) associatedAccountList.get(i)).getMobileNo());
                refferalCustomerModel.setId(((CustomerDeviceVerification) associatedAccountList.get(i)).getId());
                refferalCustomerModel.setUnquieIdentifier(((CustomerDeviceVerification) associatedAccountList.get(i)).getUniqueIdentifier());
                refferalCustomerModel.setDeviceName(((CustomerDeviceVerification) associatedAccountList.get(i)).getDeviceName());
                refferalCustomerModel.setRequestType(((CustomerDeviceVerification) associatedAccountList.get(i)).getRequestType());
                refferalCustomerModel.setApprovalStatus(((CustomerDeviceVerification) associatedAccountList.get(i)).getApprovalStatus());
                refferalCustomerModel.setRemarks(((CustomerDeviceVerification) associatedAccountList.get(i)).getRemarks());
                refferalCustomerModel.setRequestedDate(((CustomerDeviceVerification) associatedAccountList.get(i)).getRequestedDate());
                refferalCustomerModel.setRequestedTime(((CustomerDeviceVerification) associatedAccountList.get(i)).getRequestedTime());
                refferalCustomerModel.setFatherHusbandName(customerAppUserModel.getMotherMaidenName());
                refferalCustomerModel.setFullName(customerAppUserModel.getFullName());
                refferalCustomerModel.setCnic(customerAppUserModel.getNic());
                refferalCustomerModel.setDob(customerAppUserModel.getDob());
                refferalCustomerModel.setCnicIssuanceDate(customerAppUserModel.getCnicIssuanceDate());
                refferalCustomerModel.setCnicExpiryDate(customerAppUserModel.getNicExpiryDate());
                refferalCustomerModel.setAppUserId(customerAppUserModel.getAppUserId());
                refferalCustomerModel.setAddress(customerModel.getBirthPlace());
                refferalCustomerModelsLi.add(refferalCustomerModel);
            }
            list.setResultsetList(refferalCustomerModelsLi);
        }
        pagingHelperModel.setTotalRecordsCount(refferalCustomerModelsLi.size());

        return new ModelAndView(getSuccessView(), "reqList", list.getResultsetList());

    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    private CommonCommandManager getCommonCommandManager() {
        ApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        CommonCommandManager commonCommandManager = (CommonCommandManager) webApplicationContext.getBean("commonCommandManager");
        return commonCommandManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}

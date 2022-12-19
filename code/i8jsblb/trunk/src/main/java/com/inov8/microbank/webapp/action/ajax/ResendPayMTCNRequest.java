package com.inov8.microbank.webapp.action.ajax;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerAddressesModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.util.AddressTypeConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.hra.paymtnc.model.PayMtncRequestModel;
import com.inov8.microbank.hra.service.HRAManager;
import com.inov8.microbank.server.dao.addressmodule.AddressDAO;
import com.inov8.microbank.server.dao.addressmodule.CustomerAddressesDAO;
import com.inov8.microbank.server.dao.portal.citymodule.CityDAO;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.batik.css.engine.value.StringValue;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class ResendPayMTCNRequest extends AjaxController {

    private HRAManager hraManager;
    private ESBAdapter esbAdapter;
    private AppUserManager appUserManager;
    private CustomerAddressesDAO customerAddressesDAO;
    private CityDAO cityDAO;
    @Autowired
    private TransactionReversalFacade transactionReversalFacade;
    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String payMtncReqId = (String) request.getParameter("paymtcnId");
        PayMtncRequestModel model = hraManager.findPayMtncModelByPrimaryKay(Long.parseLong(payMtncReqId));
        if(model != null) {
            if (model.getIsAccountOpening() != null && model.getIsAccountOpening().equals(1L)) {
                AppUserModel appModel = new AppUserModel();
                appModel.setNic(model.getCnic());
                appModel.setMobileNo(model.getMobileNo());
                AppUserModel appUserModel = appUserManager.getAppUserModel(appModel);

                CustomerModel customerModel = appUserManager.getCustomerModelByPK(appUserModel.getCustomerId());

                java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("E MMMMM dd HH:mm:ss z yyyy");

                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareZindigiCustomerSyncRequest(appUserModel.getFullName(), appUserModel.getMobileNo(), appUserModel.getNic(),
                        simpleDateFormat.format(appUserModel.getCnicIssuanceDate()), simpleDateFormat.format(appUserModel.getNicExpiryDate()),
                        customerModel.getGender(), simpleDateFormat.format(appUserModel.getDob()), appUserModel.getAddress1(), appUserModel.getEmail(),
                        appUserModel.getLastName(), appUserModel.getMotherMaidenName(), appUserModel.getCustomerMobileNetwork());

                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                i8sbSwitchWrapper.putObject("payMtncRequestModel", model);
                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);

                I8SBSwitchControllerResponseVO responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();

                ESBAdapter.processI8sbResponseCode(responseVO, false);

                if(i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO().getResponseCode().equals("I8SB-200")){
                    model.setIsValid(0L);
                    model.setThirdPartyResponseCode(responseVO.getResponseCode());
                    model.setUpdatedOn(new Date());
                    this.esbAdapter.getPayMtncRequestDAO().saveOrUpdate(model);
                }
//                BaseWrapper baseWrapper=new BaseWrapperImpl();
//                baseWrapper.setBasePersistableModel(model);
//
//                transactionReversalFacade.makeAccountOpeningRetryAdvice(baseWrapper);

            } else {
                String customerCity = null;
                AppUserModel example = new AppUserModel();
                example.setNic(model.getCnic());
                example.setMobileNo(model.getMobileNo());
                AppUserModel appUserModel = appUserManager.getAppUserModel(example);
                CustomerAddressesModel exampleAddress = new CustomerAddressesModel();
                exampleAddress.setCustomerId(appUserModel.getCustomerId());
                List<CustomerAddressesModel> addressList = this.customerAddressesDAO.findByExample(exampleAddress).getResultsetList();
                if (!addressList.isEmpty()) {
                    for (CustomerAddressesModel a : addressList) {
                        if (a.getAddressIdAddressModel().getCityId() != null) {
                            customerCity = cityDAO.findByPrimaryKey(a.getAddressIdAddressModel().getCityId()).getName();
                            break;
                        }
                    }
                }
                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.preparePayMTNCRequest(model.getCnic(), model.getMobileNo(), null,
                        appUserModel.getFirstName(), appUserModel.getLastName(), customerCity);
                //
                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                i8sbSwitchWrapper.putObject("payMtncRequestModel", model);
                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
            }
        }
        return "Request submitted successfully.";
    }

    public void setHraManager(HRAManager hraManager) {
        this.hraManager = hraManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setCustomerAddressesDAO(CustomerAddressesDAO customerAddressesDAO) {
        this.customerAddressesDAO = customerAddressesDAO;
    }

    public void setCityDAO(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }

    public void setTransactionReversalFacade(
            TransactionReversalFacade transactionReversalFacade) {
        this.transactionReversalFacade = transactionReversalFacade;
    }
}

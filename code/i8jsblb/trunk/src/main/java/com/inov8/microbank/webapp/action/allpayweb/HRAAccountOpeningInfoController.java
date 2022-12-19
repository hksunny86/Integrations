package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

public class HRAAccountOpeningInfoController extends AdvanceFormController {

    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;

    public HRAAccountOpeningInfoController()
    {
        setCommandName("object");
        setCommandClass(MfsAccountModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        if(!isTokenValid(httpServletRequest)) {
            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }
        String isLinked = requestWrapper.getParameter("IS_LINKED_REQ");
        String cwAmount = requestWrapper.getParameter("CW_AMOUNT");
        String mobileNumber = requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE).trim();
        String cNic= requestWrapper.getParameter(CommandFieldConstants.KEY_CNIC).trim();
        logger.info("[HRAAccountOpeningInfoController.onCreate] Validating Customer. Mobile No:" + mobileNumber + " and NIC:" + cNic);
        requestWrapper.addParameter(CommandFieldConstants.KEY_APP_ID,"1");
        requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB.toString());
        String responseXML = mfsWebController.handleRequest(requestWrapper,CommandFieldConstants.CMD_OPEN_HRA_ACCOUNT_INQUIRY);
        Boolean isErrorXML = MfsWebUtil.isErrorXML(responseXML);
        if(isErrorXML) {
            requestWrapper.setAttribute("IS_LINKED_REQ",isLinked);
            return new ModelAndView(getFormView());
        }
        this.mfsWebResponseDataPopulator.populateProductPurchase(requestWrapper, responseXML);
        requestWrapper.setAttribute("isValid","0");
        requestWrapper.setAttribute("mpinValid","0");
        requestWrapper.setAttribute("IS_LINKED_REQ",isLinked);
        requestWrapper.addParameter("CW_AMOUNT",cwAmount);
        return new ModelAndView(getSuccessView());
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        return null;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }

    public void setMfsWebController(MfsWebManager mfsWebController) {
        this.mfsWebController = mfsWebController;
    }

    public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
        this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
    }
}

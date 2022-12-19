package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MinistatementController extends AdvanceFormController {

    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

    public MinistatementController() {
        setCommandClass(Object.class);
        setCommandName("object");
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        requestWrapper.setAttribute("pinRequired", String.valueOf(true));
        requestWrapper.setAttribute(CommandFieldConstants.KEY_PIN_RETRY_COUNT, String.valueOf(0));

        return new Object();
    }

    /*
     insert into device_type_command values (222, 8, 30, 1,1,1,1,SYSDATE,SYSDATE);
     */
    @Override
    protected Map loadReferenceData(HttpServletRequest request) throws Exception {

//		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
//		AllPayWebResponseDataPopulator.setMainMenuData(requestWrapper);
        return null;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse arg1, Object arg2, BindException arg3) throws Exception {

        AllPayRequestWrapper requestWrapper = initializeRequest(request);


        AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID));
        ThreadLocalAppUser.setAppUserModel(appUserModel);

        requestWrapper.addParameter("PIN_RETRY_COUNT", "0");
		requestWrapper.addParameter("ENCT", "1");

        String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_VERIFY_PIN);

        if (MfsWebUtil.isErrorXML(responseXml)) {
            requestWrapper.setAttribute("pinRequired", String.valueOf(true));
            mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);

        } else if (responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {
            ThreadLocalAppUser.setAppUserModel(appUserModel);
            String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_MINISTATEMENT_AGENT);

            if (MfsWebUtil.isErrorXML(responseXML)) {
                mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
            } else {
                mfsWebResponseDataPopulator.populateMiniStatementAgentData(requestWrapper, responseXML);
            }
            requestWrapper.setAttribute("pinRequired", String.valueOf(false));
        }
        return new ModelAndView(super.getFormView());
    }

    private AllPayRequestWrapper initializeRequest(HttpServletRequest request) {

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, String.valueOf(DeviceTypeConstantsInterface.ALLPAY_WEB));
        return requestWrapper;
    }


    @Override
    protected ModelAndView onUpdate(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, BindException arg3) throws Exception {
        return onCreate(arg0, arg1, arg2, arg3);
    }

    public void setMfsWebController(MfsWebManager mfsWebController) {
        this.mfsWebController = mfsWebController;
    }

    public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
        this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }
}

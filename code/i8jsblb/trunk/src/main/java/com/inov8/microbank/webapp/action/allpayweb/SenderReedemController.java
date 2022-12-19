package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;


public class SenderReedemController extends AdvanceFormController
{

    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator = null;

    private final static Log logger = LogFactory.getLog(SenderReedemController.class);


    public SenderReedemController()
    {
        setCommandName("object");
        setCommandClass(Object.class);
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception
    {

        return new Object();
    }


    @Override
    protected Map loadReferenceData(HttpServletRequest request) throws Exception
    {

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception
    {

        return onCreate(request, response, model, exception);
    }


    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException exception) throws Exception
    {

        if(!isTokenValid(request)) {
            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }


        AllPayRequestWrapper requestWrapper = initializeRequest(request);
        String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_SENDER_REEDEM_INFO);
        String nextView = null;
        if(MfsWebUtil.isErrorXML(responseXml)) {

            mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);
            nextView = super.getFormView();

        } else if(responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {

            nextView = getSuccessView();

        }

        return new ModelAndView(nextView);


    }

    private AllPayRequestWrapper initializeRequest(HttpServletRequest request)
    {

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, String.valueOf(DeviceTypeConstantsInterface.ALLPAY_WEB));
        requestWrapper.addParameter(CommandFieldConstants.KEY_AGENT_MOB_NO, UserUtils.getCurrentUser().getMobileNo());

        return requestWrapper;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator)
    {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }

    public void setMfsWebController(MfsWebManager mfsWebController)
    {
        this.mfsWebController = mfsWebController;
    }

    public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator)
    {
        this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
    }


}

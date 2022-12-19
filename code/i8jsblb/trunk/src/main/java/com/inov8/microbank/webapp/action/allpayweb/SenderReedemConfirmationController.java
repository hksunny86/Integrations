package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

/**
 * Created by Yasir Shabbir on 11/28/2016.
 */
public class SenderReedemConfirmationController extends AdvanceFormController
{

    private final static Log logger = LogFactory.getLog(PayCashWithDrawalController.class);

    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;


    public SenderReedemConfirmationController()
    {
        setCommandName("object");
        setCommandClass(Object.class);
    }


    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception
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

        logger.info("onUpdate(...)");

        return onCreate(request, response, model, exception);
    }


    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException exception) throws Exception
    {


        if(!isTokenValid(request)) {
            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);



        String plainPin = request.getParameter(CommandFieldConstants.KEY_PIN);
        if(!StringUtil.isNullOrEmpty(plainPin)) {
            Integer pinTryCount = 0;

            if(request.getSession().getAttribute("INVALID_BANK_PIN_COUNT") != null) {
                pinTryCount = (Integer) request.getSession().getAttribute("INVALID_BANK_PIN_COUNT");
            }


            requestWrapper.addParameter("PIN_RETRY_COUNT", String.valueOf(pinTryCount));
            requestWrapper.addParameter(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");

            String responseXML  = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_VERIFY_PIN);

            Boolean hasError = MfsWebUtil.isErrorXML(responseXML) || request.getAttribute("errors") != null;

            populatePreviousScreen(requestWrapper);
            if(hasError) {
                mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
                return new ModelAndView(getFormView());
            }
        }


        return new ModelAndView(getSuccessView());
    }


    protected void populatePreviousScreen(AllPayRequestWrapper httpServletRequest)
    {
        final Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        for(String key : parameterMap.keySet()) {
            httpServletRequest.setAttribute(key, httpServletRequest.getParameter(key));
        }
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
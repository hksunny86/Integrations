package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 */
public class WelcomeScreenController extends AdvanceFormController
{

    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator = null;
    private AgentWebManager agentWebManager;


    public void setMfsWebController(MfsWebManager mfsWebController) {
        this.mfsWebController = mfsWebController;
    }

    public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
        this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }

    public void setAgentWebManager(AgentWebManager agentWebManager) {
        this.agentWebManager = agentWebManager;
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        return new Object();
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        return modelAndView;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        return onCreate(httpServletRequest, httpServletResponse, o, e);

    }
}

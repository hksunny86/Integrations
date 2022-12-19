package com.inov8.microbank.webapp.action.portal.mnologsmodule;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.mnologsmodule.MnologsListViewModel;
import com.inov8.microbank.server.service.portal.mnologsmodule.MnoLogsManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Inov8 on 11/29/2018.
 */
public class DisplayActionLogXMLController extends BaseFormSearchController {

    private MnoLogsManager mnoLogsManager;

    public DisplayActionLogXMLController()
    {
        setCommandName("displayActionLogXMLController");
        setCommandClass(MnologsListViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
        String actionId = httpServletRequest.getParameter("actionLogId");
        SearchBaseWrapper baseWrapper = new SearchBaseWrapperImpl();
        baseWrapper.putObject("actionLogId",actionId);
        baseWrapper = mnoLogsManager.loadMnoLogs(baseWrapper);
        MnologsListViewModel mnologsListViewModel = (MnologsListViewModel) baseWrapper.getBasePersistableModel();
        if(mnologsListViewModel != null)
        {
            if(mnologsListViewModel.getInputXML() != null)
                httpServletRequest.setAttribute("inputXML",mnologsListViewModel.getInputXML());
            else
                httpServletRequest.setAttribute("inputXML","");
            if(mnologsListViewModel.getOutputXML() != null)
                httpServletRequest.setAttribute("outputXML",mnologsListViewModel.getOutputXML());
            else
                httpServletRequest.setAttribute("outputXML","");
        }
        return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        return null;
    }

    public void setMnoLogsManager(MnoLogsManager mnoLogsManager) {
        this.mnoLogsManager = mnoLogsManager;
    }
}

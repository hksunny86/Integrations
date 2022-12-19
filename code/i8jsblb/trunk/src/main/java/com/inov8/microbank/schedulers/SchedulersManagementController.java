package com.inov8.microbank.schedulers;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.debitcard.model.SchedulersManagementModel;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class SchedulersManagementController extends BaseSearchController {

    private ReferenceDataManager referenceDataManager;

    public ReferenceDataManager getReferenceDataManager() {
        return referenceDataManager;
    }

    public SchedulersManagementController() {
        super.setFilterSearchCommandClass(SchedulersManagementModel.class);
    }

    @Override
    protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object o, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        SchedulersManagementModel model = new SchedulersManagementModel();
        SchedulersManagementModel model2 = new SchedulersManagementModel();
        SchedulersManagementModel model3 = new SchedulersManagementModel();
        model.setJobName("Debit Card Import Export Scheduler");
        model.setJobType("Cron");
        model.setCronExpression("0 0 0/3 1/1 * ? *");
        model.setAction("Run Now");
        model2.setJobName("Debit Card Annual Fee Scheduler");
        model2.setJobType("Cron");
        model2.setCronExpression("0 0 0/3 1/1 * ? *");
        model2.setAction("Run Now");
        model3.setJobName("Ac Holder Disbursement Scheduler");
        model3.setJobType("Cron");
        model3.setCronExpression("0 0 0/3 1/1 * ? *");
        model3.setAction("Run Now");
        List<SchedulersManagementModel> list = new ArrayList<>();
        list.add(model);
        list.add(model2);
        list.add(model3);
        Integer totalRecordsCount = list.size();
        if (linkedHashMap.isEmpty()) {
            linkedHashMap.put("jobName", SortingOrder.ASC);
        }
        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        pagingHelperModel.setTotalRecordsCount(totalRecordsCount);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        searchBaseWrapper.setBasePersistableModel(model);
        httpServletRequest.setAttribute("totalRecordsCount", totalRecordsCount);
        if (httpServletRequest.getParameter("isManual") != null && httpServletRequest.getParameter("isManual").equals("true")) {
            httpServletRequest.removeAttribute("isManual");
            Map<String, String> map = new HashMap<String, String>();
            map.put("_status", IssueTypeStatusConstantsInterface.SUCCESS);
            map.put("message", "Scheduler Executed Successfully.");
            httpServletRequest.setAttribute("jobs", list);
            /*httpServletRequest.setAttribute("status", IssueTypeStatusConstantsInterface.SUCCESS);
            httpServletRequest.setAttribute("message", "Data for Debit Card Issuance has been Imported & Exported Successfully.");*/
            return new ModelAndView(getSearchView(), map);
        }
        return new ModelAndView(getSearchView(), "jobs", list);
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }
}

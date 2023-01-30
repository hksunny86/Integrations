package com.inov8.microbank.ivr.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.IVRMpinVerificationViewModel;
import com.inov8.microbank.ivr.manager.IVRMpinVerificationManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// @Created On 1/27/2023 : Friday
// @Created By muhammad.aqeel
public class IVRMpinVerificationReportController extends BaseFormSearchController {
    private IVRMpinVerificationManager ivrMpinVerificationManager;

    public IVRMpinVerificationReportController() {
        this.setCommandClass(IVRMpinVerificationViewModel.class);
        this.setCommandName("iVRMpinVerificationViewModel");
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return new HashMap();
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        IVRMpinVerificationViewModel model = (IVRMpinVerificationViewModel)o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(model);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("logDateAndTime", model.getLogStartDate(), model.getLogEndDate());
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        searchBaseWrapper = this.ivrMpinVerificationManager.loadIVRMpinVerificationData(searchBaseWrapper);
        List<IVRMpinVerificationViewModel> list = null;
        if (searchBaseWrapper.getCustomList() != null && !searchBaseWrapper.getCustomList().getResultsetList().isEmpty()) {
            list = searchBaseWrapper.getCustomList().getResultsetList();
//            searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(list.size());
        } else {
            searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
        }
        for(IVRMpinVerificationViewModel ivrMpinVerificationViewModel: list){
            String date = null;
            String time = null;
            if (ivrMpinVerificationViewModel.getLogDateAndTime() != null) {
                SimpleDateFormat dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dateFormatTime = new SimpleDateFormat("hh:mm a");
                date = dateFormatDate.format(ivrMpinVerificationViewModel.getLogDateAndTime());
                time = dateFormatTime.format(ivrMpinVerificationViewModel.getLogDateAndTime());
                ivrMpinVerificationViewModel.setVerificationDate(date);
                ivrMpinVerificationViewModel.setVerificationTime(time);
            }
        }
        return new ModelAndView("p_ivrMpinVerificationReport", "ivrMpinVerificationList", list);
    }

    public void setIvrMpinVerificationManager(IVRMpinVerificationManager ivrMpinVerificationManager) {
        this.ivrMpinVerificationManager = ivrMpinVerificationManager;
    }
}

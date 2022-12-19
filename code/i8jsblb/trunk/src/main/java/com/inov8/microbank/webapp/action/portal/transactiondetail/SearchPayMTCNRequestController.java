package com.inov8.microbank.webapp.action.portal.transactiondetail;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.hra.paymtnc.model.PayMtncRequestModel;
import com.inov8.microbank.hra.service.HRAManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchPayMTCNRequestController extends BaseFormSearchController {

    private HRAManager hraManager;

    public SearchPayMTCNRequestController()
    {
        setCommandClass(PayMtncRequestModel.class);
        setCommandName("SearchPayMTCNRequestController");
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                    PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        PayMtncRequestModel payMtncRequestModel = (PayMtncRequestModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(payMtncRequestModel);
        searchBaseWrapper.setSortingOrderMap( linkedHashMap );
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", payMtncRequestModel.getCreatedOnStartDate(),
                payMtncRequestModel.getCreatedOnEndDate());
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
        if(linkedHashMap.isEmpty())
        {
            linkedHashMap.put("updatedOn", SortingOrder.DESC);
        }
        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        searchBaseWrapper = hraManager.searchPayMtncRequests(searchBaseWrapper);
        List<PayMtncRequestModel> list = searchBaseWrapper.getCustomList().getResultsetList();
        return new ModelAndView( getSuccessView(), "reqList",list);
    }

    public void setHraManager(HRAManager hraManager) {
        this.hraManager = hraManager;
    }
}

package com.inov8.microbank.tax.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.taxregimemodule.TaxRegimeFacade;
import com.inov8.microbank.tax.model.WHTExemptionListViewModel;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Zeeshan on 7/1/2016.
 */
public class SearchWhtExemptionController extends BaseFormSearchController{

    private TaxRegimeFacade taxRegimeFacade;

    public SearchWhtExemptionController(){
        setCommandName("wHTExemptionView");
        setCommandClass(WHTExemptionListViewModel.class);
    }

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}
	
    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        WHTExemptionListViewModel whtExemptionListViewModel = (WHTExemptionListViewModel) o;
        

        searchBaseWrapper.setBasePersistableModel(whtExemptionListViewModel);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("exemptionStartDate",whtExemptionListViewModel.getExemptionFromStartDate(),whtExemptionListViewModel.getExemptionToStartDate());
        DateRangeHolderModel dateRangeHolderModelEndDate = new DateRangeHolderModel("exemptionEndDate", whtExemptionListViewModel.getExemptionFromEndDate(),whtExemptionListViewModel.getExemptionToEndDate());
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        dateRangeHolderModels.add(dateRangeHolderModelEndDate);
        searchBaseWrapper.setDateRangeHolderModelList(dateRangeHolderModels);

        //sorting order
        if(sortingOrderMap.isEmpty())
        {
            sortingOrderMap.put("exemptionStartDate", SortingOrder.DESC);
        }
        searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

        searchBaseWrapper = taxRegimeFacade.searchWhtExemptionModel(searchBaseWrapper);
        List<WHTExemptionListViewModel> list = null;
        if(null!=searchBaseWrapper.getCustomList())
        {
            list = searchBaseWrapper.getCustomList().getResultsetList();
        }

        String successView = StringUtil.trimExtension( httpServletRequest.getServletPath() );
        return  new ModelAndView(successView,"whtExemptionModelList",list);

    }

    public void setTaxRegimeFacade(TaxRegimeFacade taxRegimeFacade) {
        this.taxRegimeFacade = taxRegimeFacade;
    }
}

package com.inov8.microbank.fonepay.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.tax.model.WHTExemptionListViewModel;

public class SearchIntegratedSystemLogController extends BaseFormSearchController {

	private FonePayManager fonePayManager;
	
    public SearchIntegratedSystemLogController(){
        setCommandName("fonePayLogModel");
        setCommandClass(FonePayListViewModel.class);
    }
	
	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
	        List<LabelValueBean> requestTypeList = new ArrayList<LabelValueBean>();
	        LabelValueBean requestType = new LabelValueBean(FonePayConstants.REQ_VERIFY_NEW_ACCOUNT, FonePayConstants.REQ_VERIFY_NEW_ACCOUNT);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_VERIFY_EXISTING_ACCOUNT, FonePayConstants.REQ_VERIFY_EXISTING_ACCOUNT);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_ACCOUNT_OPENING, FonePayConstants.REQ_ACCOUNT_OPENING);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_ACCOUNT_OPENING_CONVENTIONAL, FonePayConstants.REQ_ACCOUNT_OPENING_CONVENTIONAL);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_ACCOUNT_LINK, FonePayConstants.REQ_ACCOUNT_LINK);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_ACCOUNT_DELINK, FonePayConstants.REQ_ACCOUNT_DELINK);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_REVERSAL, FonePayConstants.REQ_REVERSAL);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_VERIFY_OTP, FonePayConstants.REQ_VERIFY_OTP);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_CARD_TAGGING, FonePayConstants.REQ_CARD_TAGGING);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_PAYMENT_FONEPAY, FonePayConstants.REQ_PAYMENT_FONEPAY);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_PAYMENT_CARD, FonePayConstants.REQ_PAYMENT_CARD);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_CARD_ACTIVATE, FonePayConstants.REQ_CARD_ACTIVATE);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_CARD_DEACTIVATE, FonePayConstants.REQ_CARD_DEACTIVATE);
	        requestTypeList.add(requestType);
	        requestType = new LabelValueBean(FonePayConstants.REQ_CARD_DELETE, FonePayConstants.REQ_CARD_DELETE);
	        requestTypeList.add(requestType);
	        referenceDataMap.put("requestTypeList", requestTypeList);
	        return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, PagingHelperModel arg3,
			LinkedHashMap<String, SortingOrder> arg4) throws Exception {

		FonePayListViewModel fonePayListViewModel = new FonePayListViewModel();
		FonePayLogModel fonePayLogModel = new FonePayLogModel();
		fonePayListViewModel = (FonePayListViewModel) arg2;
		fonePayLogModel = (FonePayLogModel)fonePayListViewModel;
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("created_on", fonePayListViewModel.getFromDate(), fonePayListViewModel.getToDate());
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(arg3);
		if(arg4.isEmpty())
        {
			arg4.put("created_on", SortingOrder.DESC);
        }
		searchBaseWrapper.setSortingOrderMap(arg4);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		searchBaseWrapper.setBasePersistableModel(fonePayLogModel);
		
		searchBaseWrapper = fonePayManager.searchFonePayLogModels(searchBaseWrapper);
		List<FonePayLogModel> fonePayLogModelList = null;
		
		if(null!=searchBaseWrapper.getCustomList().getResultsetList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 ){
			fonePayLogModelList = searchBaseWrapper.getCustomList().getResultsetList();
		}
		
		return new ModelAndView(getSuccessView(),"fonePayLogModelList",fonePayLogModelList);
	}

	public void setFonePayManager(FonePayManager fonePayManager) {
		this.fonePayManager = fonePayManager;
	}
	
}

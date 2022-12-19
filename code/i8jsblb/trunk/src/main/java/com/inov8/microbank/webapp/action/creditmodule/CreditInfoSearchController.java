package com.inov8.microbank.webapp.action.creditmodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.creditmodule.CreditInfoListViewModel;
import com.inov8.microbank.common.model.creditmodule.ExtendedCreditInfoListViewModel;
import com.inov8.microbank.common.util.CreditInfoConstants;
import com.inov8.microbank.server.service.creditmodule.CreditInfoManager;


public class CreditInfoSearchController extends BaseFormSearchController
{
	
	private ReferenceDataManager referenceDataManager;
	private CreditInfoManager creditInfoManager;
	
	public CreditInfoManager getCreditInfoManager()
	{
		return creditInfoManager;
	}
	public void setCreditInfoManager(CreditInfoManager creditInfoManager)
	{
		this.creditInfoManager = creditInfoManager;
	}
	public ReferenceDataManager getReferenceDataManager()
	{
		return referenceDataManager;
	}
	public CreditInfoSearchController() {
		setCommandName("extendedCreditInfoListViewModel");
		setCommandClass(ExtendedCreditInfoListViewModel.class);
	}
	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		DistributorModel distributorModel = new DistributorModel();
	    //distributorModel.setActive(true);
	    ReferenceDataWrapper  referenceDataWrapper = new ReferenceDataWrapperImpl(
	        distributorModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(distributorModel);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<SupplierModel> distributorModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	      distributorModelList = referenceDataWrapper.
	          getReferenceDataList();
	    }
	    Map referenceDataMap = new HashMap();
	    referenceDataMap.put("distributorModelList",
	                         distributorModelList);
	    
	    RetailerModel retailerModel = new RetailerModel();
	    distributorModel.setActive(true);
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	        retailerModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(retailerModel);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<SupplierModel> retailerModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	      retailerModelList = referenceDataWrapper.
	          getReferenceDataList();
	    }
	    
	    referenceDataMap.put("retailerModelList",
	                         retailerModelList);
	    
	    
	    
	    return referenceDataMap;
	    
	}
	
	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		// TODO Auto-generated method stub
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		//TransactionDetInfoListViewModel transactionDetInfoListViewModel = (TransactionDetInfoListViewModel) object;
		ExtendedCreditInfoListViewModel creditInfoModel = (ExtendedCreditInfoListViewModel)object;
		if(ServletRequestUtils.getLongParameter(request, "distributorId",-1L) != -1L)
		{
			creditInfoModel.setType(CreditInfoConstants.DISTRIBUTOR);
			creditInfoModel.setOrganization(ServletRequestUtils.getLongParameter(request, "distributorId"));
		}
		else if(ServletRequestUtils.getLongParameter(request, "retailerId" , -1L)!= -1)
		{
			creditInfoModel.setType(CreditInfoConstants.RETAILER);
			creditInfoModel.setOrganization(ServletRequestUtils.getLongParameter(request, "retailerId"));
		} 
		
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("name", SortingOrder.ASC);
		}

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setBasePersistableModel((CreditInfoListViewModel)creditInfoModel);
		searchBaseWrapper = this.creditInfoManager.searchDistributorOrRetailer(searchBaseWrapper);
		
		return new ModelAndView(super.getSuccessView(), "extendedCreditInfoListViewModelList", searchBaseWrapper.getCustomList()
				.getResultsetList());



	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}


}

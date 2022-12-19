package com.inov8.microbank.webapp.action.portal.reports.finance;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.UnsettledAgentCommModel;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;

public class UnsettledCommissionSearchController extends BaseFormSearchController{
    
	private CommissionManager commissionManager;
    private ReferenceDataManager referenceDataManager;
    
	public UnsettledCommissionSearchController() {
		setCommandName("unsettledAgentCommModel");
		setCommandClass(UnsettledAgentCommModel.class);
	}

	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
		String strSupplierId = req.getParameter("supplierId");
		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(supplierModel, "name", SortingOrder.ASC);
		
		try{
			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<SupplierModel> supplierModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null){
				supplierModelList = referenceDataWrapper.getReferenceDataList();
			}
			ProductModel productModel = null;
			List<ProductModel> productModelList = null;
			if(!GenericValidator.isBlankOrNull(strSupplierId)){
				Long supplierId = Long.parseLong(strSupplierId);
				productModel = new ProductModel();
				productModel.setSupplierId(supplierId);
				productModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if (referenceDataWrapper.getReferenceDataList() != null){
					productModelList = referenceDataWrapper.getReferenceDataList();
				}
			}
			referenceDataMap.put("supplierModelList", supplierModelList);
			referenceDataMap.put("productModelList", productModelList);
		}catch(Exception ex){
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}

	    return referenceDataMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		UnsettledAgentCommModel unsettledAgentCommModel = (UnsettledAgentCommModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("transactionDate", 
								unsettledAgentCommModel.getStartDate(),
								unsettledAgentCommModel.getEndDate());

		searchBaseWrapper.setBasePersistableModel((UnsettledAgentCommModel) unsettledAgentCommModel);
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

        if(sortingOrderMap.isEmpty()){
			sortingOrderMap.put( "transactionDate", SortingOrder.DESC);
		}

        searchBaseWrapper.setSortingOrderMap( sortingOrderMap ); 
        searchBaseWrapper = this.commissionManager.searchUnsettledAgentCommission( searchBaseWrapper );
		List<UnsettledAgentCommModel> list = searchBaseWrapper.getCustomList().getResultsetList();
		
        return new ModelAndView(getSuccessView(),"unsettledAgentCommModelList",list);
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}
}
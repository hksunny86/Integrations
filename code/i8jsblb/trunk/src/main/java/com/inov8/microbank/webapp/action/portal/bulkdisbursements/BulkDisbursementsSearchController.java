package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.BulkDisbursementsViewModel;
import com.inov8.microbank.common.model.PaymentTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.AccountCreationStatusEnum;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.disbursement.service.BulkDisbursementsFacade;
import com.inov8.microbank.server.facade.ProductFacade;

public class BulkDisbursementsSearchController extends BaseFormSearchController{
    
	private BulkDisbursementsFacade bulkDisbursementsFacade;
    private ProductFacade productFacade;
    private ReferenceDataManager referenceDataManager;
    
	public BulkDisbursementsSearchController() {
		super.setCommandName("bulkDisbursementsViewModel");
		super.setCommandClass(BulkDisbursementsViewModel.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
	    Map<String, Object> referenceDataMap = new HashMap<String, Object>(3);
	    referenceDataMap.put("bankUsersList", this.bulkDisbursementsFacade.loadBankUsersList());
	    List<ProductModel> productModelList = productFacade.loadProductsByIds("name", SortingOrder.ASC, ProductConstantsInterface.BULK_DISBURSEMENT, ProductConstantsInterface.BULK_PAYMENT);
		referenceDataMap.put("productModelList", productModelList);
		List<LabelValueBean> accountCreationStatusList = new ArrayList<>(3);
		for (AccountCreationStatusEnum accountCreationStatusEnum : AccountCreationStatusEnum.values()) {
			accountCreationStatusList.add(new LabelValueBean(accountCreationStatusEnum.toString(),accountCreationStatusEnum.toString()));
		}
		referenceDataMap.put("accountCreationStatusList", accountCreationStatusList);
		
		List paymentTypeModelList = null;
		ReferenceDataWrapper referenceDataWrapper = null;
	    PaymentTypeModel model = new PaymentTypeModel();
	    model.setProductId(ProductConstantsInterface.BULK_PAYMENT);
		referenceDataWrapper = new ReferenceDataWrapperImpl(model, "name", SortingOrder.ASC);
	    this.referenceDataManager.getReferenceData(referenceDataWrapper);
	    paymentTypeModelList = referenceDataWrapper.getReferenceDataList();
	    
	    referenceDataMap.put("paymentTypeModelList", paymentTypeModelList);
	    
	    return referenceDataMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		BulkDisbursementsViewModel bulkDisbursementsModel = (BulkDisbursementsViewModel) model;
		bulkDisbursementsModel.setDeleted(Boolean.FALSE);
		searchBaseWrapper.setBasePersistableModel(bulkDisbursementsModel);
		ArrayList<DateRangeHolderModel> dateRangeHolderModelList = new ArrayList<DateRangeHolderModel>();
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "paymentDate", bulkDisbursementsModel.getPaymentFromDate(),
				bulkDisbursementsModel.getPaymentToDate());
		dateRangeHolderModelList.add(dateRangeHolderModel);
		dateRangeHolderModel = new DateRangeHolderModel( "createdOn", bulkDisbursementsModel.getUploadFromDate(),
				bulkDisbursementsModel.getUploadToDate());
		dateRangeHolderModelList.add(dateRangeHolderModel);
		dateRangeHolderModel = new DateRangeHolderModel( "postedOn", bulkDisbursementsModel.getPostedOnFromDate(),
				bulkDisbursementsModel.getPostedOnToDate());
		dateRangeHolderModelList.add(dateRangeHolderModel);
		dateRangeHolderModel = new DateRangeHolderModel( "settledOn", bulkDisbursementsModel.getSettledOnFromDate(),
				bulkDisbursementsModel.getSettledOnToDate());
		dateRangeHolderModelList.add(dateRangeHolderModel);
		searchBaseWrapper.setDateRangeHolderModelList( dateRangeHolderModelList );

		if(sortingOrderMap.isEmpty())
	    {
	       sortingOrderMap.put("createdOn", SortingOrder.DESC);
	    }
		else if( sortingOrderMap.containsKey( "payCashMethod" ) )
		{
		    SortingOrder sortingOrder = sortingOrderMap.remove( "payCashMethod" );
		    sortingOrder = (sortingOrder == SortingOrder.ASC ? SortingOrder.DESC : SortingOrder.ASC); 
		    sortingOrderMap.put( "payCashViaCnic", sortingOrder );
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.bulkDisbursementsFacade.searchBulkDisbursements(searchBaseWrapper);
		CustomList<BulkDisbursementsViewModel> list = searchBaseWrapper.getCustomList();
		
		return new ModelAndView(super.getSuccessView(), "bulkDisbursementsViewModelList", list.getResultsetList());

	}
	
	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	public void setBulkDisbursementsFacade(BulkDisbursementsFacade bulkDisbursementsFacade) {
		this.bulkDisbursementsFacade = bulkDisbursementsFacade;
	}

	public void setProductFacade(ProductFacade productFacade) {
		this.productFacade = productFacade;
	}
	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
	    this.referenceDataManager = referenceDataManager;
	  }
	
}
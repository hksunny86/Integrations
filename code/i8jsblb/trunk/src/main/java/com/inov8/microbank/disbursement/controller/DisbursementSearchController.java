package com.inov8.microbank.disbursement.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.BulkDisbursementsViewModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.disbursement.service.BulkDisbursementsFacade;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.servicemodule.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class DisbursementSearchController extends BaseFormSearchController{

	@Autowired
	private BulkDisbursementsFacade bulkDisbursementsFacade;

	private ReferenceDataManager referenceDataManager;
	@Autowired
	private ServiceManager serviceManager;

	@Autowired
	private ProductManager productManager;

	public DisbursementSearchController() {
		super.setCommandName("bulkDisbursementsViewModel");
		super.setCommandClass(BulkDisbursementsViewModel.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
	    Map<String, Object> referenceDataMap = new HashMap<String, Object>(3);
		referenceDataMap.put("bankUsersList", this.bulkDisbursementsFacade.loadBankUsersList());
		referenceDataMap.put("serviceList", serviceManager.getServiceLabels(ServiceConstantsInterface.BULK_DISB_ACC_HOLDER,
				ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER));

		String serviceId = req.getParameter("serviceId");
		if(!StringUtil.isNullOrEmpty(serviceId)) {
			referenceDataMap.put("productList", productManager.getProductLabelsByReferencedClass(ServiceModel.class, Long.parseLong(serviceId)));
		}

		List<LabelValueBean> accountCreationStatusList = new ArrayList<>(3);
		for (AccountCreationStatusEnum accountCreationStatusEnum : AccountCreationStatusEnum.values()) {
			accountCreationStatusList.add(new LabelValueBean(accountCreationStatusEnum.toString(),accountCreationStatusEnum.toString()));
		}

		referenceDataMap.put("accountCreationStatusList", accountCreationStatusList);

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
		dateRangeHolderModel = new DateRangeHolderModel( "fileUploadedOn", bulkDisbursementsModel.getUploadFromDate(),
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
	       sortingOrderMap.put("bulkDisbursementsId", SortingOrder.DESC);
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
				
		req.getSession().removeAttribute("messages");
		
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

	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}
}
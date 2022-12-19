package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

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
import com.inov8.microbank.common.model.PaymentTypeModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.disbursement.model.BulkPaymentViewModel;

import com.inov8.microbank.disbursement.service.BulkDisbursementsFacade;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.servicemodule.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class BulkPaymentViewController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	private BulkDisbursementsFacade bulkDisbursementsFacade;
	@Autowired
	private ServiceManager serviceManager;

	@Autowired
	private ProductManager productManager;

	public BulkPaymentViewController()
	{
		 super.setCommandName("bulkPaymentViewModel");
		super.setCommandClass(BulkPaymentViewModel.class);
	}

	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>(3);

		referenceDataMap.put("serviceList", serviceManager.getServiceLabels(ServiceConstantsInterface.BULK_DISB_ACC_HOLDER,
				ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER));

		String serviceId = httpServletRequest.getParameter("serviceId");
		if(!StringUtil.isNullOrEmpty(serviceId)) {
			referenceDataMap.put("productList", productManager.getProductLabelsByReferencedClass(ServiceModel.class, Long.parseLong(serviceId)));
		}

	    return referenceDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		BulkPaymentViewModel bulkPaymentViewModel=(BulkPaymentViewModel) model;
		ArrayList<DateRangeHolderModel> dateRangeHolderModelList = new ArrayList<DateRangeHolderModel>();
		DateRangeHolderModel disDateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", bulkPaymentViewModel.getDisStartDate(),
				bulkPaymentViewModel.getDisEndDate());
		dateRangeHolderModelList.add(disDateRangeHolderModel);
		DateRangeHolderModel payDateRangeHolderModel = new DateRangeHolderModel(
				"updatedOn", bulkPaymentViewModel.getPayStartDate(),
				bulkPaymentViewModel.getPayEndDate());
		dateRangeHolderModelList.add(payDateRangeHolderModel);
		searchBaseWrapper.setBasePersistableModel(bulkPaymentViewModel);
		searchBaseWrapper.setDateRangeHolderModelList(dateRangeHolderModelList);
		//sorting order
		if(sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		searchBaseWrapper = this.bulkDisbursementsFacade.searchBulkPayments(searchBaseWrapper);
		CustomList<BulkPaymentViewModel> list = searchBaseWrapper.getCustomList();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(list.getResultsetList().size());
		return new ModelAndView(super.getSuccessView(), "bulkPaymentsViewModelList", list.getResultsetList());
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setBulkDisbursementsFacade(BulkDisbursementsFacade bulkDisbursementsFacade) {
		this.bulkDisbursementsFacade = bulkDisbursementsFacade;
	}

}

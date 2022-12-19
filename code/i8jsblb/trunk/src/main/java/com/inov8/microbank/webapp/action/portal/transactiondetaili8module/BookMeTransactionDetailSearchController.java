package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.bookmemodule.BookMeTransactionDetaili8ViewModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.portal.bookmemodule.BookMeTransactionDetaili8DAO;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.mnomodule.MnoManager;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.webapp.action.allpayweb.formbean.Product;
import com.inov8.microbank.webapp.action.portal.bigreports.ReportCriteriaSessionObject;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class BookMeTransactionDetailSearchController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	private ProductManager productManager;
	private DeviceTypeManager deviceTypeManager;
	private BookMeTransactionDetaili8DAO bookMeTransactionDetaili8DAO;

	public BookMeTransactionDetailSearchController()
	{
		 super.setCommandName("bookMeTransactionDetaili8ViewModel");
		 super.setCommandClass(BookMeTransactionDetaili8ViewModel.class);
	}

	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		
		Integer totalRecordsCount = (Integer) httpServletRequest.getAttribute("totalRecordsCount");
		
		if(totalRecordsCount == null || totalRecordsCount == 0) {

			httpServletRequest.getSession().removeAttribute(ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR);
		}
		
		httpServletRequest.setAttribute(ReportConstants.REPORT_ID, ReportConstants.REPORT_TRANSACTION_DETAIL_REPORT);

		Map<String,Object> referenceDataMap = new HashMap<String,Object>();

	    if (log.isDebugEnabled())
	    {
	      log.debug("Inside reference data");
	    }

		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl();
		try
		{
			ProductModel productModel = null;
			List<ProductModel> productModelList = productManager.loadProductsByIds("productId", SortingOrder.ASC, ProductConstantsInterface.AIR,
					ProductConstantsInterface.EVENT, ProductConstantsInterface.BUS_TICKETING, ProductConstantsInterface.CINEMA, ProductConstantsInterface.HOTEL);
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						productModel, "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					productModelList = referenceDataWrapper.getReferenceDataList();
				}

		referenceDataMap.put("productModelList", productModelList);
			List<DeviceTypeModel> deviceTypeModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY);
			referenceDataMap.put("deviceTypeModelList", deviceTypeModelList);

		}
		catch (Exception ex)
		{
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}

	    return referenceDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object o,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> linkedHashMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		BookMeTransactionDetaili8ViewModel bookMeTransactionDetaili8ViewModel = (BookMeTransactionDetaili8ViewModel) o;
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", bookMeTransactionDetaili8ViewModel.getCreatedOn(),
				bookMeTransactionDetaili8ViewModel.getCreatedOnToDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		searchBaseWrapper.setBasePersistableModel(bookMeTransactionDetaili8ViewModel);

		if(linkedHashMap.isEmpty())
		{
			linkedHashMap.put("createdOn", SortingOrder.DESC);
		}

		searchBaseWrapper.setSortingOrderMap(linkedHashMap);
		ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
		exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
		exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
		CustomList<BookMeTransactionDetaili8ViewModel> list = this.bookMeTransactionDetaili8DAO.findByExample(bookMeTransactionDetaili8ViewModel,
				searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel(),
				exampleConfigHolderModel);

//		List<TransactionDetailPortalListModel> list = null;
//		if(searchBaseWrapper.getCustomList() != null)
//		{
//			list = searchBaseWrapper.getCustomList().getResultsetList();
//		}
//
//		httpServletRequest.getSession().setAttribute(ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR, new ReportCriteriaSessionObject(searchBaseWrapper));
//
//		Integer totalRecordsCount = 0;
//
//		if(pagingHelperModel != null) {
//
//			totalRecordsCount = pagingHelperModel.getTotalRecordsCount();
//		}
//
//		httpServletRequest.setAttribute("totalRecordsCount", totalRecordsCount);

		return new ModelAndView( getSuccessView(), "bookMei8List",list.getResultsetList());
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{ this.referenceDataManager = referenceDataManager; }

	public DeviceTypeManager getDeviceTypeManager() { return deviceTypeManager; }

	public void setDeviceTypeManager( DeviceTypeManager deviceTypeManager )
    {
        this.deviceTypeManager = deviceTypeManager;
    }

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setBookMeTransactionDetaili8DAO(BookMeTransactionDetaili8DAO bookMeTransactionDetaili8DAO) {
		this.bookMeTransactionDetaili8DAO = bookMeTransactionDetaili8DAO;
	}
}

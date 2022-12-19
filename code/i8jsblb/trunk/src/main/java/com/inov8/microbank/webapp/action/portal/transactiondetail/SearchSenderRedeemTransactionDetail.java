package com.inov8.microbank.webapp.action.portal.transactiondetail;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.web.bind.ServletRequestUtils;
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
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ReportConstants;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;
import com.inov8.microbank.webapp.action.portal.bigreports.ReportCriteriaSessionObject;
import com.inov8.microbank.webapp.action.portal.transactiondetaili8module.ReportTypeEnum;

public class SearchSenderRedeemTransactionDetail extends BaseFormSearchController{

	private ReferenceDataManager referenceDataManager;
	private TransactionDetailI8Manager transactionDetailI8Manager;
	private DeviceTypeManager deviceTypeManager;
	
	public SearchSenderRedeemTransactionDetail()
	{
		 super.setCommandName("extendedTransactionDetailPortalListModel");
		 super.setCommandClass(ExtendedTransactionDetailPortalListModel.class);
	}
	
	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
         Integer totalRecordsCount = (Integer) request.getAttribute("totalRecordsCount");
		
		if(totalRecordsCount == null || totalRecordsCount == 0) {

			request.getSession().removeAttribute(ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR);
		}
		
		request.setAttribute(ReportConstants.REPORT_ID, ReportConstants.REPORT_TRANSACTION_DETAIL_REPORT);

		Map<String,Object> referenceDataMap = new HashMap<String,Object>();

	    if (log.isDebugEnabled())
	    {
	      log.debug("Inside reference data");
	    }

		/**
		 * code fragment to load reference data for SupplierModel
		 */
		String strSupplierId = request.getParameter("supplierId");

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				supplierModel, "name", SortingOrder.ASC);
		try
		{
		
		referenceDataManager.getReferenceData(referenceDataWrapper);

		List<SupplierModel> supplierModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			supplierModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("supplierModelList", supplierModelList);
		}
		catch (Exception ex)
		{
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}

	    return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,
			HttpServletResponse response, Object command,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedTransactionDetailPortalListModel extendedTransactionDetailPortalListModel = (ExtendedTransactionDetailPortalListModel) command;
		String reportType = ServletRequestUtils.getStringParameter( request, "reportType" );
		if( reportType != null )
		{
		    Long productId = ReportTypeEnum.valueOf( reportType.toUpperCase() ).getProductId();
		    if( productId != null )
		    {
		        extendedTransactionDetailPortalListModel.setProductId( productId );
		    }
		}
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedTransactionDetailPortalListModel.getStartDate(),
				extendedTransactionDetailPortalListModel.getEndDate());
		searchBaseWrapper.setBasePersistableModel((TransactionDetailPortalListModel)extendedTransactionDetailPortalListModel);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		//sorting order 
		if(sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		else
		{
		    SortingOrder sortingOrder = sortingOrderMap.remove( "isManualOTPinString" );
		    if( sortingOrder != null )
		    {
		        sortingOrderMap.put("isManualOTPin", sortingOrder);
		    }
		}
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		if("P_TO_P".equalsIgnoreCase(reportType)){
			String dateRange = "createdOn";
			if("1".equals(extendedTransactionDetailPortalListModel.getProcessingStatusId())){
				dateRange = "updatedOn";
			}
			dateRangeHolderModel = new DateRangeHolderModel(
					dateRange, extendedTransactionDetailPortalListModel.getStartDate(),
					extendedTransactionDetailPortalListModel.getEndDate());
			searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
			searchBaseWrapper = this.transactionDetailI8Manager.searchSenderRedeemTransactionReportDetail(searchBaseWrapper);
		}else{
			searchBaseWrapper = this.transactionDetailI8Manager.searchSenderRedeemTransactionReportDetail(searchBaseWrapper);
		}
		
		List<TransactionDetailPortalListModel> list = null;
		if(searchBaseWrapper.getCustomList() != null)
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		
		request.getSession().setAttribute(ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR, new ReportCriteriaSessionObject(searchBaseWrapper));
		
		Integer totalRecordsCount = 0;
		
		if(pagingHelperModel != null) {
			
			totalRecordsCount = pagingHelperModel.getTotalRecordsCount();
		}		
		
		request.setAttribute("totalRecordsCount", totalRecordsCount);
		
		return new ModelAndView( buildView( reportType ), "transactionDetailPortalList",list);
	}
	
	private String buildView( final String reportType )
	{
	    String view = null;
        if( reportType == null )
        {
            view = super.getSuccessView();
        }
        else
        {
            view = ReportTypeEnum.valueOf( reportType.toUpperCase() ).getView();
        }

        return view;
	}
	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetailI8Manager(
			TransactionDetailI8Manager transactionDetailI8Manager) {
		this.transactionDetailI8Manager = transactionDetailI8Manager;
	}

	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}


}

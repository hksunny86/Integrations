package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ReportConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.mnomodule.MnoManager;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;
import com.inov8.microbank.webapp.action.portal.bigreports.ReportCriteriaSessionObject;
import org.apache.commons.validator.GenericValidator;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class TransactionDetailI8SearchController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	private TransactionDetailI8Manager transactionDetailI8Manager;
	private DeviceTypeManager deviceTypeManager;
	private MnoManager mnoManager;
	private DistributorManager distributorManager;
	public TransactionDetailI8SearchController()
	{
		 super.setCommandName("extendedTransactionDetailPortalListModel");
		 super.setCommandClass(ExtendedTransactionDetailPortalListModel.class);
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

		/**
		 * code fragment to load reference data for SupplierModel
		 */
		String strSupplierId = httpServletRequest.getParameter("supplierId");

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				supplierModel, "name", SortingOrder.ASC);
		try {

			AppUserModel loggedInUser = UserUtils.getCurrentUser();
			MnoModel mnoModel = new MnoModel();

			List<DistributorModel> distributorModelList = new ArrayList<>();
			if (loggedInUser.getAppUserTypeId().equals(UserTypeConstantsInterface.MNO)) {
				MnoUserModel mnoUserModel = loggedInUser.getMnoUserIdMnoUserModel();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper.setBasePersistableModel(mnoUserModel);
				searchBaseWrapper = mnoManager.searchMnoByMnoUser(searchBaseWrapper);
				mnoModel = (MnoModel) searchBaseWrapper.getBasePersistableModel();
				DistributorModel distributorModel = new DistributorModel();
				distributorModel.setMnoId(mnoModel.getMnoId());
				searchBaseWrapper.setBasePersistableModel(distributorModel);
				distributorModelList = distributorManager.searchDistributorsByExample(searchBaseWrapper);
			}

			referenceDataManager.getReferenceData(referenceDataWrapper);

			List<SupplierModel> supplierModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				supplierModelList = referenceDataWrapper.getReferenceDataList();
			}

//Segment
			SegmentModel segmentModel = new SegmentModel();
			List<SegmentModel> segmentModelList = null;
			segmentModel.setIsActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					segmentModel, "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			if (referenceDataWrapper.getReferenceDataList() != null) {
				segmentModelList = referenceDataWrapper.getReferenceDataList();
			}
//
			ProductModel productModel = null;
			List<ProductModel> productModelList = null;
			if (!GenericValidator.isBlankOrNull(strSupplierId)) {
				Long supplierId = Long.parseLong(strSupplierId);
				productModel = new ProductModel();
				productModel.setSupplierId(supplierId);
				productModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						productModel, "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if (referenceDataWrapper.getReferenceDataList() != null) {
					productModelList = referenceDataWrapper.getReferenceDataList();
				}
			}

			referenceDataMap.put("supplierModelList", supplierModelList);
			referenceDataMap.put("productModelList", productModelList);
			referenceDataMap.put("distributorModelList", distributorModelList);
			referenceDataMap.put("segmentModelList", segmentModelList);


			//Load Device Type Reference data
			List<DeviceTypeModel> deviceTypeModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY, DeviceTypeConstantsInterface.ALLPAY_WEB,
					DeviceTypeConstantsInterface.BANKING_MIDDLEWARE, DeviceTypeConstantsInterface.USSD);
			referenceDataMap.put("deviceTypeModelList", deviceTypeModelList);

			//Load Reference Data For Bank....

			BankModel bankModel = new BankModel();
			bankModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					bankModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(bankModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);

			List<BankModel> bankModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				bankModelList = referenceDataWrapper.
						getReferenceDataList();
			}
			referenceDataMap.put("bankModelList", bankModelList);

			//Load Reference Data for Payment Mode

			PaymentModeModel paymentModeModel = new PaymentModeModel();
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					paymentModeModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(paymentModeModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<PaymentModeModel> paymentModeList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				paymentModeList = referenceDataWrapper.getReferenceDataList();
			}
			referenceDataMap.put("paymentModeList", paymentModeList);

			SupplierProcessingStatusModel supplierProcessingStatusModel = new SupplierProcessingStatusModel();
			List<SupplierProcessingStatusModel> supplierProcessingStatusModelList = null;
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					supplierProcessingStatusModel, "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			if (referenceDataWrapper.getReferenceDataList() != null) {
				supplierProcessingStatusModelList = referenceDataWrapper.getReferenceDataList();
			}
			referenceDataMap.put("supplierProcessingStatusModelList", supplierProcessingStatusModelList);

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
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedTransactionDetailPortalListModel extendedTransactionDetailPortalListModel = (ExtendedTransactionDetailPortalListModel) model;
		String reportType = ServletRequestUtils.getStringParameter( httpServletRequest, "reportType" );
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
		DateRangeHolderModel dateRangeHolderModelOnUpdateDate = new DateRangeHolderModel("updatedOn", extendedTransactionDetailPortalListModel.getUpdatedOnStartDate(),extendedTransactionDetailPortalListModel.getUpdatedOnEndDate());
		AppUserModel loggedInUser= UserUtils.getCurrentUser();
		MnoModel mnoModel=new MnoModel();
		if(loggedInUser.getAppUserTypeId().equals(UserTypeConstantsInterface.MNO)){
			MnoUserModel mnoUserModel=loggedInUser.getMnoUserIdMnoUserModel();
			SearchBaseWrapper sWrapper=new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(mnoUserModel);
			searchBaseWrapper=mnoManager.searchMnoByMnoUser(searchBaseWrapper);
			mnoModel=(MnoModel) searchBaseWrapper.getBasePersistableModel();
			extendedTransactionDetailPortalListModel.setSenderServiceOPId(mnoModel.getMnoId());
		}


		searchBaseWrapper.setBasePersistableModel((TransactionDetailPortalListModel)extendedTransactionDetailPortalListModel);
		
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        dateRangeHolderModels.add(dateRangeHolderModelOnUpdateDate);
        searchBaseWrapper.setDateRangeHolderModelList(dateRangeHolderModels);
		
		//searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		//searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModelOnUpdateDate);
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
			searchBaseWrapper = this.transactionDetailI8Manager.searchP2PTransactionDetail(searchBaseWrapper);
		}else{
			searchBaseWrapper = this.transactionDetailI8Manager.searchTransactionDetailForI8(searchBaseWrapper);
		}
		
		List<TransactionDetailPortalListModel> list = null;
		if(searchBaseWrapper.getCustomList() != null)
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		
		httpServletRequest.getSession().setAttribute(ReportCriteriaSessionObject.REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR, new ReportCriteriaSessionObject(searchBaseWrapper));
		
		Integer totalRecordsCount = 0;
		
		if(pagingHelperModel != null) {
			
			totalRecordsCount = pagingHelperModel.getTotalRecordsCount();
		}		
		
		httpServletRequest.setAttribute("totalRecordsCount", totalRecordsCount);
		
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

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetailI8Manager(TransactionDetailI8Manager transactionDetailI8Manager)
	{
		this.transactionDetailI8Manager = transactionDetailI8Manager;
	}

	public DeviceTypeManager getDeviceTypeManager()
    {
        return deviceTypeManager;
    }

	public void setDeviceTypeManager( DeviceTypeManager deviceTypeManager )
    {
        this.deviceTypeManager = deviceTypeManager;
    }

	public void setMnoManager(MnoManager mnoManager) {
		this.mnoManager = mnoManager;
	}

	public void setDistributorManager(DistributorManager distributorManager) {
		this.distributorManager = distributorManager;
	}
}

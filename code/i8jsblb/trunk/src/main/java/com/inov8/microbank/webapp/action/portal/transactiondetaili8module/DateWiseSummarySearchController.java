package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

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
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;

public class DateWiseSummarySearchController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	private TransactionDetailI8Manager transactionDetailI8Manager;

	public DateWiseSummarySearchController()
	{
        super.setCommandName( "extendedTransactionDetailPortalListModel" );
        super.setCommandClass( ExtendedTransactionDetailPortalListModel.class );
	}

	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>();
		List<SupplierModel> supplierModelList = null;
		List<ProductModel> productModelList = null;

	    if( log.isDebugEnabled() )
	    {
	      log.debug("Inside reference data");
	    }

		/**
		 * code fragment to load reference data for SupplierModel
		 */
		String strSupplierId = httpServletRequest.getParameter("supplierId");

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( supplierModel, "name", SortingOrder.ASC );

		try
		{
            referenceDataManager.getReferenceData( referenceDataWrapper );

    		supplierModelList = referenceDataWrapper.getReferenceDataList();

    		ProductModel productModel = null;
    		

    		if( !GenericValidator.isBlankOrNull( strSupplierId ) )
    		{
                Long supplierId = Long.parseLong( strSupplierId );
    			productModel = new ProductModel();
    			productModel.setSupplierId(supplierId);
    			productModel.setActive(true);
    			referenceDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
    			referenceDataManager.getReferenceData(referenceDataWrapper);
    			productModelList = referenceDataWrapper.getReferenceDataList();
    		}
    		
		}
        catch( Exception ex )
        {
            log.error("Error getting product data in loadReferenceData", ex);
            ex.printStackTrace();
        }

        referenceDataMap.put("supplierModelList", supplierModelList);
        referenceDataMap.put("productModelList", productModelList);
	    return referenceDataMap;
	}

	protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap ) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		ExtendedTransactionDetailPortalListModel extendedTransactionDetailPortalListModel = (ExtendedTransactionDetailPortalListModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedTransactionDetailPortalListModel.getStartDate(), extendedTransactionDetailPortalListModel.getEndDate());

		searchBaseWrapper.setBasePersistableModel( (TransactionDetailPortalListModel) extendedTransactionDetailPortalListModel );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		//sorting order 
        if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put( "supplierId", SortingOrder.ASC );
			sortingOrderMap.put( "productId", SortingOrder.ASC );
		}

        searchBaseWrapper.setSortingOrderMap( sortingOrderMap ); 
		List<TransactionDetailPortalListModel> list = this.transactionDetailI8Manager.fetchDateWiseSummary( searchBaseWrapper );
		if( list != null && !list.isEmpty() )
		{
		    pagingHelperModel.setTotalRecordsCount( list.size() );
		}
		else
		{
		    pagingHelperModel.setTotalRecordsCount( 0 );
		}

		String successView = StringUtil.trimExtension( request.getServletPath() );
        return new ModelAndView( successView, "transactionDetailPortalList", list );
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetailI8Manager(TransactionDetailI8Manager transactionDetailI8Manager)
	{
		this.transactionDetailI8Manager = transactionDetailI8Manager;
	}	
}

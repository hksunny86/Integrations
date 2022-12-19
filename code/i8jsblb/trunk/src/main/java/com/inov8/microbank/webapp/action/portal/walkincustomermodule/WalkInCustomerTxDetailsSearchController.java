package com.inov8.microbank.webapp.action.portal.walkincustomermodule;

import java.util.Collections;
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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.ExtendedTransactionAllViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;
import com.inov8.microbank.server.facade.portal.transactiondetail.TransactionDetailFacade;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 2, 2012 4:30:40 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class WalkInCustomerTxDetailsSearchController extends BaseFormSearchController
{
	private TransactionDetailFacade transactionDetailFacade;
	private ReferenceDataManager referenceDataManager;

	public WalkInCustomerTxDetailsSearchController()
	{
		super.setCommandName("extendedTransactionAllViewModel");
		super.setCommandClass(ExtendedTransactionAllViewModel.class);	
	}
	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		Map referenceDataMap = new HashMap();
	try{	
		SupplierProcessingStatusModel supplierProcessingStatusModel = new SupplierProcessingStatusModel();
		ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( supplierProcessingStatusModel, "name", SortingOrder.ASC );
		referenceDataManager.getReferenceData( refDataWrapper );
		referenceDataMap.put( "supplierProcessingStatusList", refDataWrapper.getReferenceDataList() );
		}
		catch (Exception ex)
		{
			log.error("Error getting supplierProcessingStatusList data in loadReferenceData", ex);
			ex.printStackTrace();
		}
	return referenceDataMap; 
	}
	@Override
	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception{
	

		ModelAndView modelAndView = new ModelAndView(getSuccessView());
		List<?> resultList = Collections.EMPTY_LIST;

		String cnic = httpServletRequest.getParameter( "cnic" );
		ExtendedTransactionAllViewModel extendedTransactionAllViewModel=(ExtendedTransactionAllViewModel) model;

		if( cnic != null && !cnic.isEmpty() )
		{

			if( sortingOrderMap.isEmpty() )
	        {
	        	sortingOrderMap.put( "createdOn", SortingOrder.DESC );
	        }
			DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedTransactionAllViewModel.getStartDate(),
					extendedTransactionAllViewModel.getEndDate());
			if(sortingOrderMap.isEmpty()){
	        	sortingOrderMap.put("createdOn", SortingOrder.DESC );
	        }
			TransactionAllViewModel transactionAllViewModel = (TransactionAllViewModel)extendedTransactionAllViewModel;
			try
            {
                resultList = transactionDetailFacade.findWalkInCustomerTransactions( cnic,transactionAllViewModel,pagingHelperModel, sortingOrderMap,dateRangeHolderModel );
            }
            catch( Exception e )
            {
                logger.error( e.getMessage(), e );
            }

		}
		else
		{
			pagingHelperModel.setTotalRecordsCount( 0 );
		}

		modelAndView.addObject("transactionAllViewModelList", resultList);		
		return modelAndView;
	}

	public void setTransactionDetailFacade( TransactionDetailFacade transactionDetailFacade )
    {
        this.transactionDetailFacade = transactionDetailFacade;
    }	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
}

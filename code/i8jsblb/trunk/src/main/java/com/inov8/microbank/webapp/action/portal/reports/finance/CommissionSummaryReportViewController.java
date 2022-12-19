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
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CommissionSummaryReportViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.ExtendedCommissionSummaryReportViewModel;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.facade.portal.financereportsmodule.FinanceReportsFacacde;

public class CommissionSummaryReportViewController extends BaseFormSearchController
{
    private CommonFacade commonFacade;
    //Autowired
    private FinanceReportsFacacde financeReportsFacacde;

	public CommissionSummaryReportViewController()
	{
		 super.setCommandName("extendedCommissionSummaryReportViewModel");
		 super.setCommandClass( ExtendedCommissionSummaryReportViewModel.class );
	}

	@Override
	protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
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
        String strSupplierId = request.getParameter("supplierId");

        SupplierModel supplierModel = new SupplierModel();
        supplierModel.setActive(true);
        ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( supplierModel, "name", SortingOrder.ASC );

        try
        {
            commonFacade.getReferenceData( referenceDataWrapper );

            supplierModelList = referenceDataWrapper.getReferenceDataList();

            ProductModel productModel = null;
            

            if( !GenericValidator.isBlankOrNull( strSupplierId ) )
            {
                Long supplierId = Long.parseLong( strSupplierId );
                productModel = new ProductModel();
                productModel.setSupplierId(supplierId);
                productModel.setActive(true);
                referenceDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
                commonFacade.getReferenceData(referenceDataWrapper);
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

	@Override
	protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap ) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

        ExtendedCommissionSummaryReportViewModel extendedCommissionSummaryReportViewModel = (ExtendedCommissionSummaryReportViewModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "transactionDate",
		    extendedCommissionSummaryReportViewModel.getStartDate(), extendedCommissionSummaryReportViewModel.getEndDate() );

		searchBaseWrapper.setBasePersistableModel( (CommissionSummaryReportViewModel) extendedCommissionSummaryReportViewModel );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        //sorting order 
		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put( "transactionDate", SortingOrder.DESC );
		}
        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		searchBaseWrapper = financeReportsFacacde.searchCommissionSummaryReportView( searchBaseWrapper );
		List<CommissionSummaryReportViewModel> list = null;
		if( searchBaseWrapper.getCustomList() != null )
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView( getSuccessView(), "commissionSummaryReportViewModelList", list );
	}

	public void setCommonFacade( CommonFacade commonFacade )
    {
        this.commonFacade = commonFacade;
    }

	public void setFinanceReportsFacacde( FinanceReportsFacacde financeReportsFacacde )
    {
        this.financeReportsFacacde = financeReportsFacacde;
    }

}

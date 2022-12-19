package com.inov8.microbank.webapp.action.portal.reports.finance;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.SegmentModel;
import org.apache.commons.validator.GenericValidator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportSummaryModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerClosingBalanceViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.ExtendedCustomerBalanceReportSummaryModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.ExtendedCustomerClosingBalanceViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.server.facade.portal.financereportsmodule.FinanceReportsFacacde;
import com.inov8.ola.util.EncryptionUtil;

public class CustomerBalanceReportViewController extends BaseFormSearchController
{
    //Autowired
    private FinanceReportsFacacde financeReportsFacacde;
	private ReferenceDataManager referenceDataManager;
	public CustomerBalanceReportViewController()
	{
		 super.setCommandName("extendedCustomerBalanceReportSummaryModel");
		 super.setCommandClass( ExtendedCustomerClosingBalanceViewModel.class );
	}

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}
	@Override
	protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>( );

		try{
			SegmentModel segmentModel = new SegmentModel();
			List<SegmentModel> segmentModelList = null;
			segmentModel.setIsActive(true);
			ReferenceDataWrapperImpl referenceDataWrapper = new ReferenceDataWrapperImpl(
					segmentModel, "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			if(referenceDataWrapper.getReferenceDataList() != null)
			{
				segmentModelList = referenceDataWrapper.getReferenceDataList();
			}
			referenceDataMap.put("segmentModelList", segmentModelList);
		}


		catch (Exception ex)
		{
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}
		return referenceDataMap;
	}



	@Override
	protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap ) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

        ExtendedCustomerClosingBalanceViewModel extendedCustomerClosingBalanceViewModel = (ExtendedCustomerClosingBalanceViewModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "statsDate",
				extendedCustomerClosingBalanceViewModel.getStartDate(), extendedCustomerClosingBalanceViewModel.getEndDate() );

		searchBaseWrapper.setBasePersistableModel( (CustomerClosingBalanceViewModel) extendedCustomerClosingBalanceViewModel );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        //sorting order 
		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put( "statsDate", SortingOrder.DESC );
		}
        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		searchBaseWrapper = financeReportsFacacde.searchCustomerClosingBalanceView(searchBaseWrapper);
		List<CustomerBalanceReportSummaryModel> list = null;
		if( searchBaseWrapper.getCustomList() != null )
		{
			list =  (List<CustomerBalanceReportSummaryModel>) searchBaseWrapper.getCustomList().getResultsetList();
		}
		
		//bad way to make formatted string
		/*for(CustomerBalanceReportSummaryModel smodel : list) {
			try{
				String str = smodel.getEndDayBalance();
				if(!GenericValidator.isBlankOrNull(str))
					smodel.setEndDayBalance(Formatter.formatDoubleByPattern(Double.parseDouble(str),"#,###.00"));
				
				str = smodel.getStartDayBalance();
				if(!GenericValidator.isBlankOrNull(str))
					smodel.setStartDayBalance(Formatter.formatDoubleByPattern(Double.parseDouble(str),"#,###.00"));
				
			}catch(Exception e){
				e.printStackTrace();
			}
			//smodel.setEndDayBalance(Formatter.formatDouble(Double.parseDouble(smodel.getEndDayBalance())));
			
		}*/
		return new ModelAndView( getSuccessView(), "customerBalanceReportViewModelList", list );
	}

/*	private List<CustomerBalanceReportViewModel> decryptProps( List<CustomerBalanceReportViewModel> list )
    {
	    try
        {
            for( CustomerBalanceReportViewModel customerBalanceReportViewModel : list )
            {
                customerBalanceReportViewModel.setStartDayBalance( EncryptionUtil.decryptPin( customerBalanceReportViewModel.getStartDayBalance() ) );
                customerBalanceReportViewModel.setEndDayBalance( EncryptionUtil.decryptPin( customerBalanceReportViewModel.getEndDayBalance() ) );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        return list;
    }
*/
	public void setFinanceReportsFacacde( FinanceReportsFacacde financeReportsFacacde )
    {
        this.financeReportsFacacde = financeReportsFacacde;
    }

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
}

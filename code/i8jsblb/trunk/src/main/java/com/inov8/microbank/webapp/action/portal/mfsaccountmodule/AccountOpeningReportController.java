package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ExtendedUserInfoListViewModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AccountOpeningReportController extends BaseFormSearchController
{
    private static final Log     LOG = LogFactory.getLog( AccountOpeningReportController.class );

    private ReferenceDataManager referenceDataManager;
	private AgentHierarchyManager agentHierarchyManager;
    private MfsAccountManager  mfsAccountManager;

	public AccountOpeningReportController()
	{
        setCommandName( "extendedUserInfoListViewModel" );
        setCommandClass( ExtendedUserInfoListViewModel.class );
	}

    @SuppressWarnings( "unchecked" )
    @Override
	protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
	{
        Map<String,Object> referenceDataMap = new HashMap<String,Object>();

        OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
        customerAccountTypeModel.setIsCustomerAccountType(Boolean.TRUE);
        customerAccountTypeModel.setParentAccountTypeId(null);
        ReferenceDataWrapper customerAccountTypeDataWrapper = new ReferenceDataWrapperImpl( customerAccountTypeModel, "name", SortingOrder.ASC );

        try
        {
          referenceDataManager.getReferenceDataByExcluding(customerAccountTypeDataWrapper,3L,9L);
        }
        catch( Exception e )
        {
            LOG.error( e.getMessage(), e );
        }

        List<OlaCustomerAccountTypeModel> customerAccountTypeList = customerAccountTypeDataWrapper.getReferenceDataList();
        referenceDataMap.put( "customerAccountTypeList", customerAccountTypeList );

        RetailerModel retailerModel = new RetailerModel();
        ReferenceDataWrapper retailerRefDataWrapper = new ReferenceDataWrapperImpl( retailerModel, "name", SortingOrder.ASC );

        try
        {
          referenceDataManager.getReferenceData( retailerRefDataWrapper );
        }
        catch( Exception e )
        {
            LOG.error( e.getMessage(), e );
        }

        List<RetailerModel> retailerList = retailerRefDataWrapper.getReferenceDataList();
        referenceDataMap.put( "retailerList", retailerList );

		retailerRefDataWrapper = new ReferenceDataWrapperImpl(new DistributorModel(), "distributorId", SortingOrder.ASC);
		referenceDataManager.getReferenceData(retailerRefDataWrapper);
		List<DistributorModel> distributorModelList = retailerRefDataWrapper.getReferenceDataList();
		referenceDataMap.put("distributorModelList", distributorModelList);

		CityModel cityModel = new CityModel();
		retailerRefDataWrapper = new ReferenceDataWrapperImpl( cityModel, "name", SortingOrder.ASC );

		try
		{
			referenceDataManager.getReferenceData( retailerRefDataWrapper );
		}
		catch( Exception e )
		{
			LOG.error( e.getMessage(), e );
		}
		List<CityModel> cityModelList = retailerRefDataWrapper.getReferenceDataList();
		referenceDataMap.put( "cityModelList", cityModelList );
        return referenceDataMap;
	}

    @SuppressWarnings( "unchecked" )
    @Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel,
			                         LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception 
    {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		UserInfoListViewModel userInfoListViewModel = (UserInfoListViewModel) model;

		userInfoListViewModel.setAccountEnabled(null);

        searchBaseWrapper.setBasePersistableModel( userInfoListViewModel );

		ExtendedUserInfoListViewModel extendedUserInfoListViewModel = (ExtendedUserInfoListViewModel) model;
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "accountOpeningDate",
		    extendedUserInfoListViewModel.getStartDate(), extendedUserInfoListViewModel.getEndDate() );
		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put( "accountOpeningDate", SortingOrder.DESC );
        }

        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );
        searchBaseWrapper = this.mfsAccountManager.searchUserInfo( searchBaseWrapper );

		List<UserInfoListViewModel> resultList = searchBaseWrapper.getCustomList().getResultsetList();

		return new ModelAndView( getSuccessView(), "userInfoListViewModelList", resultList );
	}

	public void setReferenceDataManager( ReferenceDataManager referenceDataManager )
    {
        this.referenceDataManager = referenceDataManager;
    }

	public void setMfsAccountManager( MfsAccountManager mfsAccountManager )
	{
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}
}

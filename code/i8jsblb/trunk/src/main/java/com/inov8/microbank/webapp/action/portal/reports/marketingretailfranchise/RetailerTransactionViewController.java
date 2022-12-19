package com.inov8.microbank.webapp.action.portal.reports.marketingretailfranchise;

import java.util.ArrayList;
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
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionalHierarchyModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.common.model.portal.retailertransactionmodule.ExtendedRetailerTransactionViewModel;
import com.inov8.microbank.common.model.portal.retailertransactionmodule.RetailerTransactionViewModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.RetailerFacade;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;

public class RetailerTransactionViewController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	//Autowired
	private RetailerFacade retailerFacade;
	private AgentHierarchyManager agentHierarchyManager;
	private AllpayRetailerAccountManager allpayRetailerAccountManager;

	public RetailerTransactionViewController()
	{
		 super.setCommandName("extendedRetailerTransactionViewModel");
		 super.setCommandClass(ExtendedRetailerTransactionViewModel.class);
	}
	@SuppressWarnings( "unchecked" )
    protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>();

		/**
		 * code fragment to load reference data for SupplierModel
		 */
		String strSupplierId = request.getParameter("supplierId");

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( supplierModel, "name", SortingOrder.ASC );

        try
        {
            referenceDataManager.getReferenceData( referenceDataWrapper );

            List<SupplierModel> supplierModelList = null;
            if( referenceDataWrapper.getReferenceDataList() != null )
            {
                supplierModelList = referenceDataWrapper.getReferenceDataList();
            }

            ProductModel productModel = null;
            List<ProductModel> productModelList = null;

            if( !GenericValidator.isBlankOrNull( strSupplierId ) )
            {
                Long supplierId = Long.parseLong( strSupplierId );
                productModel = new ProductModel();
                productModel.setSupplierId( supplierId );
                productModel.setActive( true );
                referenceDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
                referenceDataManager.getReferenceData( referenceDataWrapper );
                if ( referenceDataWrapper.getReferenceDataList() != null )
                {
                    productModelList = referenceDataWrapper.getReferenceDataList();
                }
            }            
            referenceDataMap.put( "supplierModelList", supplierModelList );
            referenceDataMap.put( "productModelList", productModelList );

            SupplierProcessingStatusModel supplierProcessingStatusModel = new SupplierProcessingStatusModel();
            ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( supplierProcessingStatusModel, "name", SortingOrder.ASC );
            referenceDataManager.getReferenceData( refDataWrapper );
            referenceDataMap.put( "supplierProcessingStatusList", refDataWrapper.getReferenceDataList() );
            
            List<DistributorModel> distributorModelList = new ArrayList<DistributorModel>();
            
            SalesHierarchyModel salesHierarchyModel = new SalesHierarchyModel();  
            SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findSaleUserByBankUserId(UserUtils.getCurrentUser().getAppUserId());
            if(null!= searchBaseWrapper.getBasePersistableModel()){
            	salesHierarchyModel =(SalesHierarchyModel) searchBaseWrapper.getBasePersistableModel();
            	RetailerContactListViewModel retailerContactModel = new RetailerContactListViewModel();
                retailerContactModel.setSalesHierarchyId(salesHierarchyModel.getSalesHierarchyId());
      		  	searchBaseWrapper.setBasePersistableModel(retailerContactModel);  
      		    searchBaseWrapper = this.allpayRetailerAccountManager.searchAccount(searchBaseWrapper);
      		    List<RetailerContactListViewModel> retailerContactModelList= searchBaseWrapper.getCustomList().getResultsetList();
      		    
      		    if(null!=retailerContactModelList){
      		    	for (RetailerContactListViewModel retailerContactListViewModel : retailerContactModelList) {	
      		    		searchBaseWrapper = agentHierarchyManager.findDistributorsById(retailerContactListViewModel.getDistributorId());
      		    		DistributorModel distributorModel = (DistributorModel) searchBaseWrapper.getBasePersistableModel();
      		    		if(null!=distributorModel){
      		    			
      		    			boolean alreadyExists=false;
      		    			for (DistributorModel distributorModel2 : distributorModelList) {
      		    				if(distributorModelList.size()>0 && (distributorModel.getDistributorId().longValue()==distributorModel2.getDistributorId().longValue())){
      		    					alreadyExists=true;
      		    					break;
      		    				}
							}
      		    			
      		    			if (alreadyExists==false)
      		    				distributorModelList.add(distributorModel);
      		    		}
      		    			
    				}
      		    }
            }   
      		referenceDataMap.put( "distributorModelList",distributorModelList);
      		
      		 DistributorModel distributorModel = new DistributorModel();
             refDataWrapper = new ReferenceDataWrapperImpl( distributorModel, "name", SortingOrder.ASC );
             referenceDataManager.getReferenceData( refDataWrapper );
             referenceDataMap.put( "distributorModelList2", refDataWrapper.getReferenceDataList() );
         
             
             RegionalHierarchyModel hierarchyModel= new RegionalHierarchyModel();
             refDataWrapper = new ReferenceDataWrapperImpl(hierarchyModel, "regionalHierarchyName", SortingOrder.ASC );
             referenceDataManager.getReferenceData(refDataWrapper);
             referenceDataMap.put("receiverRegHierModelList", refDataWrapper.getReferenceDataList() );
             referenceDataMap.put("senderRegHierModelList", refDataWrapper.getReferenceDataList() );
        }
        catch( Exception ex )
        {
            log.error( ex.getMessage(), ex );
        }
	    return referenceDataMap;
	}

    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
	{
        ExtendedRetailerTransactionViewModel extendedRetailerTransactionViewModel = (ExtendedRetailerTransactionViewModel) model;
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<RetailerTransactionViewModel> list = new ArrayList<RetailerTransactionViewModel>();
		
		
		SalesHierarchyModel salesHierarchyModel = new SalesHierarchyModel();  
        SearchBaseWrapper searchBaseWrapper2 = this.agentHierarchyManager.findSaleUserByBankUserId(UserUtils.getCurrentUser().getAppUserId());
        if(null!= searchBaseWrapper2.getBasePersistableModel())
        	salesHierarchyModel =(SalesHierarchyModel) searchBaseWrapper2.getBasePersistableModel();
		
        extendedRetailerTransactionViewModel.setSenderSalesHierarchyId(salesHierarchyModel.getSalesHierarchyId());
        //extendedRetailerTransactionViewModel.setRecieverSalesHierarchyId(salesHierarchyModel.getSalesHierarchyId());
		
        extendedRetailerTransactionViewModel.setSenderRegHierId(null);
        extendedRetailerTransactionViewModel.setReceiverRegHierId(null);
        
        
        searchBaseWrapper.setBasePersistableModel( (RetailerTransactionViewModel)extendedRetailerTransactionViewModel );
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn",
		    extendedRetailerTransactionViewModel.getStartDate(), extendedRetailerTransactionViewModel.getEndDate() );
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		//sorting order 
		if(sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		searchBaseWrapper = retailerFacade.searchRetailerTransactionView( searchBaseWrapper );

		if(searchBaseWrapper.getCustomList() != null && null!=salesHierarchyModel.getSalesHierarchyId())
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		else
			searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
		
		String successView = StringUtil.trimExtension( request.getServletPath() );
		return new ModelAndView( successView, "retailerTransactionViewModelList", list );
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setRetailerFacade( RetailerFacade retailerFacade )
    {
        this.retailerFacade = retailerFacade;
    }
	
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}
	
	public void setAllpayRetailerAccountManager(
			AllpayRetailerAccountManager allpayRetailerAccountManager) {
		this.allpayRetailerAccountManager = allpayRetailerAccountManager;
	}
	
}

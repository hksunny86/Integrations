package com.inov8.microbank.webapp.action.portal.ola;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.LimitTypeModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.common.model.OlaTransactionTypeModel;
import com.inov8.microbank.common.model.portal.ola.OlaCustomerTxLimitViewModel;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.ola.server.facade.AccountFacade;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 7:29:27 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class OlaCustomerTxLimitViewController extends BaseFormSearchController
{
    //Autowired
    private PortalOlaFacade portalOlaFacade;
    //Autowired
    private CommonFacade commonFacade;
    
    @Autowired
    private AccountFacade accountFacade;

    public OlaCustomerTxLimitViewController()
    {
        setCommandClass( OlaCustomerTxLimitViewModel.class );
        setCommandName( "olaCustomerTxLimitViewModel" );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
    	List<OlaCustomerAccountTypeModel>	customerAccountTypeModellist=new ArrayList<OlaCustomerAccountTypeModel>();
    	Map<String, Object> referenceDataMap = new HashMap<String, Object>( 3 );
        List<OlaTransactionTypeModel> olaTransactionTypeModelList = new ArrayList<OlaTransactionTypeModel>();
        List<LimitTypeModel> limitTypeModelList = null;

        try
        {
            
        	OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
        	olaCustomerAccountTypeModel.setActive(Boolean.TRUE);
        	
        	ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( olaCustomerAccountTypeModel, "name", SortingOrder.ASC );
            commonFacade.getReferenceData( referenceDataWrapper );
            List<OlaCustomerAccountTypeModel> accountTypes = referenceDataWrapper.getReferenceDataList();
            
            if(accountTypes != null && !accountTypes.isEmpty())
            {
            	for(OlaCustomerAccountTypeModel model : accountTypes)
            	{
            		long typeId = model.getCustomerAccountTypeId().longValue();
            		
            		if(CustomerAccountTypeConstants.SETTLEMENT != typeId && CustomerAccountTypeConstants.WALK_IN_CUSTOMER != typeId)
            		{
            			customerAccountTypeModellist.add(model);
            		}
            	}
            }
				
			customerAccountTypeModellist	=	sortAccountTypes(customerAccountTypeModellist);
            
            OlaTransactionTypeModel olaTransactionTypeModel = new OlaTransactionTypeModel();
            referenceDataWrapper = new ReferenceDataWrapperImpl( olaTransactionTypeModel, "name", SortingOrder.ASC );
            commonFacade.getReferenceData( referenceDataWrapper );
            List<OlaTransactionTypeModel> transactionTypes = referenceDataWrapper.getReferenceDataList();
            if(transactionTypes != null && !transactionTypes.isEmpty()){
            	for(OlaTransactionTypeModel model : transactionTypes){
            		long typeId = model.getTransactionTypeId().longValue();
            		
            		if(TransactionTypeConstants.DEBIT == typeId || TransactionTypeConstants.CREDIT == typeId)
            			olaTransactionTypeModelList.add(model);
            	}
            }
          
            LimitTypeModel limitTypeModel = new LimitTypeModel();
            referenceDataWrapper = new ReferenceDataWrapperImpl( limitTypeModel, "name", SortingOrder.ASC );
            commonFacade.getReferenceData( referenceDataWrapper );
            limitTypeModelList = referenceDataWrapper.getReferenceDataList();
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
        }
        
        referenceDataMap.put( "olaCustomerAccountTypeModelList", customerAccountTypeModellist );
        referenceDataMap.put( "olaTransactionTypeModelList", olaTransactionTypeModelList );
        referenceDataMap.put( "limitTypeModelList", limitTypeModelList );
        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        List<OlaCustomerTxLimitViewModel> olaCustomerTxLimitViewModelList = null;

        try
        {
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            OlaCustomerTxLimitViewModel olaCustomerTxLimitViewModel = (OlaCustomerTxLimitViewModel) model;
            searchBaseWrapper.setBasePersistableModel( olaCustomerTxLimitViewModel );
            searchBaseWrapper.setPagingHelperModel( pagingHelperModel );
            searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

            olaCustomerTxLimitViewModelList = portalOlaFacade.searchOlaCustomerTxLimitView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
        }

        return new ModelAndView( getFormView(), "olaCustomerTxLimitViewModelList", olaCustomerTxLimitViewModelList );
    }

    private List<OlaCustomerAccountTypeModel> sortAccountTypes(List<OlaCustomerAccountTypeModel>  list){
		
		List<OlaCustomerAccountTypeModel> parentList	=	new ArrayList<>();
		List<OlaCustomerAccountTypeModel> childList	=	new ArrayList<>();
		
		for (OlaCustomerAccountTypeModel model : list) {
			if(model.getParentAccountTypeId()==null){
				parentList.add(model);
			}
			else{
				childList.add(model);
			}
		}
		
		list=new ArrayList<OlaCustomerAccountTypeModel>();
		
		for (OlaCustomerAccountTypeModel parent : parentList) {
			
			list.add(parent);
			
			for (OlaCustomerAccountTypeModel child : childList) {
				
				if(child.getParentAccountTypeId()!=null && child.getParentAccountTypeId().longValue()==parent.getCustomerAccountTypeId().longValue()){
					child.setName("--- "+child.getName());
					list.add(child);
				}
			}
		}
		
		return list;
	}
    public void setPortalOlaFacade( PortalOlaFacade portalOlaFacade )
    {
        this.portalOlaFacade = portalOlaFacade;
    }

    public void setCommonFacade( CommonFacade commonFacade )
    {
        this.commonFacade = commonFacade;
    }

	public void setAccountFacade(AccountFacade accountFacade) {
		this.accountFacade = accountFacade;
	}
}

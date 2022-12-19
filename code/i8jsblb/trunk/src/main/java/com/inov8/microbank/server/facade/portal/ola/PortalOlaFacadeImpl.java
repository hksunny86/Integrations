package com.inov8.microbank.server.facade.portal.ola;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.model.portal.ola.CustomerBbStatementViewModel;
import com.inov8.microbank.common.model.portal.ola.OlaCustomerTxLimitViewModel;
import com.inov8.microbank.common.model.portal.ola.SettlementBbStatementViewModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentTransactionsVOModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentsListViewModel;
import com.inov8.microbank.server.service.portal.ola.PortalOlaManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : May 17, 2013 3:31:04 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class PortalOlaFacadeImpl implements PortalOlaFacade
{
    //Autowired
    private PortalOlaManager portalOlaManager;

    //Autowired
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    public PortalOlaFacadeImpl()
    {
    }

    @Override
    public List<OlaCustomerTxLimitViewModel> searchOlaCustomerTxLimitView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return portalOlaManager.searchOlaCustomerTxLimitView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    @Override
	public SearchBaseWrapper searchTaggingParentDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
    	try
    	{
		return portalOlaManager.searchTaggingParentDetail(searchBaseWrapper);
    	}
		
		 catch( Exception e )
	        {
	            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
	        }
	}
    

    @Override
   	public TaggedAgentsListViewModel searchTaggedAgentDetail(
   			String taggedAgentId)
   			throws FrameworkCheckedException {
       	try
       	{
   		return portalOlaManager.searchTaggedAgentDetail(taggedAgentId);
       	}
   		
   		 catch( Exception e )
   	        {
   	            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
   	        }
   	}
    
    @Override
    public SearchBaseWrapper searchCustomerBbStatementView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return portalOlaManager.searchCustomerBbStatementView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    @Override
    public SearchBaseWrapper searchSettlementBbStatementView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return portalOlaManager.searchSettlementBbStatementView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    @Override
    public SearchBaseWrapper searchAgentBBStatementView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        try
        {
            return portalOlaManager.searchAgentBBStatementView( wrapper );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }
    @Override
	public CustomList<SettlementBbStatementViewModel> searchSettlementBbStatementView(
			SettlementBbStatementViewModel settlementBbStatementViewModel)
			throws FrameworkCheckedException {	
		try
        {
			return portalOlaManager.searchSettlementBbStatementView(settlementBbStatementViewModel);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public CustomList<BbStatementAllViewModel> searchBbStatementAllView( SearchBaseWrapper searchBaseWrapper )
			throws FrameworkCheckedException {	
		try
        {
			return portalOlaManager.searchBbStatementAllView(searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	
	@Override
	public SearchBaseWrapper searchTaggedAgents( SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {	
		try
        {
			return portalOlaManager.searchTaggedAgents(searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
    
    public void setPortalOlaManager( PortalOlaManager portalOlaManager )
    {
        this.portalOlaManager = portalOlaManager;
    }

    public void setFrameworkExceptionTranslator( FrameworkExceptionTranslator frameworkExceptionTranslator )
    {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

	@Override
	public Double getBalanceByDate(Date calendar, Long accountId) throws Exception {
		// TODO Auto-generated method stub
		return portalOlaManager.getBalanceByDate(calendar, accountId);
	}

	@Override
	public SearchBaseWrapper searchTaggedAgentTransactionDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try
       	{
   		return portalOlaManager.searchTaggedAgentTransactionDetail(searchBaseWrapper);
       	}
   		
   		 catch( Exception e )
   	        {
   	            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
   	        }
	}

    @Override
    public List<CustomerBbStatementViewModel> searchBBStatementViewByPaymentModeId(CustomerBbStatementViewModel customerBbStatementViewModel,
                                                                                   SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
    {
        return portalOlaManager.searchBBStatementViewByPaymentModeId(customerBbStatementViewModel,searchBaseWrapper);
    }

}

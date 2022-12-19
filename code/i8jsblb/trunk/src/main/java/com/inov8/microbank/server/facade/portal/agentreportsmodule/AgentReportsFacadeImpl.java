package com.inov8.microbank.server.facade.portal.agentreportsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.agentreportsmodule.AgentReportsManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Mar 29, 2013 6:33:09 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class AgentReportsFacadeImpl implements AgentReportsFacade
{
    //Autowired
    private AgentReportsManager agentReportsManager;
    //Autowired
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    @Override
    public SearchBaseWrapper searchAgentTransactionDetailView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        try
        {
            return agentReportsManager.searchAgentTransactionDetailView( wrapper );
        }
        catch( FrameworkCheckedException e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    @Override
    public SearchBaseWrapper searchBbAccountsByAgentsView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        try
        {
            return agentReportsManager.searchBbAccountsByAgentsView( wrapper );
        }
        catch( FrameworkCheckedException e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }
    
    @Override
    public SearchBaseWrapper searchAgentTransactionSummaryView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
    	try
    	{
    		return agentReportsManager.searchAgentTransactionSummaryView( searchBaseWrapper );
    	}
    	catch( FrameworkCheckedException e )
    	{
    		throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
    	}
    }
    
	@Override
	public SearchBaseWrapper searchHandlerTransactionDetailView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
    	try
    	{
    		return agentReportsManager.searchHandlerTransactionDetailView( wrapper );
    	}
    	catch( FrameworkCheckedException e )
    	{
    		throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
    	}
	}

    public void setAgentReportsManager( AgentReportsManager agentReportsManager )
    {
        this.agentReportsManager = agentReportsManager;
    }


}

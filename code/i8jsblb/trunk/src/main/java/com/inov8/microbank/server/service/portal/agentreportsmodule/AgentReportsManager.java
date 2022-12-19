package com.inov8.microbank.server.service.portal.agentreportsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Mar 29, 2013 4:47:12 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface AgentReportsManager
{
    SearchBaseWrapper searchAgentTransactionDetailView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
    
    SearchBaseWrapper searchAgentTransactionSummaryView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

    SearchBaseWrapper searchBbAccountsByAgentsView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

	SearchBaseWrapper searchHandlerTransactionDetailView(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
}

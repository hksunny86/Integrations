package com.inov8.microbank.server.service.portal.ola;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.model.portal.ola.CustomerBbStatementViewModel;
import com.inov8.microbank.common.model.portal.ola.OlaCustomerTxLimitViewModel;
import com.inov8.microbank.common.model.portal.ola.SettlementBbStatementViewModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentTransactionsVOModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentsListViewModel;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : May 17, 2013 3:25:43 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface PortalOlaManager
{
    List<OlaCustomerTxLimitViewModel> searchOlaCustomerTxLimitView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    SearchBaseWrapper searchCustomerBbStatementView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    SearchBaseWrapper searchSettlementBbStatementView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    SearchBaseWrapper searchAgentBBStatementView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	CustomList<SettlementBbStatementViewModel> searchSettlementBbStatementView(SettlementBbStatementViewModel settlementBbStatementViewModel)
																				throws FrameworkCheckedException;
	CustomList<BbStatementAllViewModel> searchBbStatementAllView(SearchBaseWrapper searchBaseWrapper)
																				throws FrameworkCheckedException;
    public Double getBalanceByDate(Date calendar, Long accountId) throws Exception;
    
	
	
	
	SearchBaseWrapper searchTaggedAgents(SearchBaseWrapper baseWrapper)
			throws FrameworkCheckedException;
	SearchBaseWrapper searchTaggingParentDetail(
			SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	
    TaggedAgentsListViewModel searchTaggedAgentDetail(String taggedAgentId)
		throws FrameworkCheckedException;

	SearchBaseWrapper searchTaggedAgentTransactionDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;


	public List<CustomerBbStatementViewModel> searchBBStatementViewByPaymentModeId(CustomerBbStatementViewModel customerBbStatementViewModel,
																				   SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}

package com.inov8.microbank.server.service.portal.agentreportsmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.portal.agentreportsmodule.AgentTransactionDetailViewModel;
import com.inov8.microbank.common.model.portal.agentreportsmodule.AgentTransactionSummaryViewModel;
import com.inov8.microbank.common.model.portal.agentreportsmodule.BbAccountsByAgentsViewModel;
import com.inov8.microbank.common.model.portal.agentreportsmodule.HandlerTransactionDetailViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.portal.reports.agent.AgentTransactionDetailViewDao;
import com.inov8.microbank.server.dao.portal.reports.agent.AgentTransactionSummaryViewDao;
import com.inov8.microbank.server.dao.portal.reports.agent.BbAccountsByAgentsViewDao;
import com.inov8.microbank.server.dao.portal.reports.agent.HandlerTransactionDetailViewDao;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Mar 29, 2013 4:48:19 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class AgentReportsManagerImpl implements AgentReportsManager
{
    //Autowired
    private AgentTransactionDetailViewDao agentTransactionDetailViewDao;
    
    //Autowired
    private HandlerTransactionDetailViewDao handlerTransactionDetailViewDao;
    
    //Autowired
    private AgentTransactionSummaryViewDao agentTransactionSummaryViewDao;
    //Autowired
    private BbAccountsByAgentsViewDao bbAccountsByAgentsViewDao;
    //
    private ActionLogManager actionLogManager;

    public AgentReportsManagerImpl()
    {        
    }

    
//*********************************************************************************************************************************************    
    @Override
    public SearchBaseWrapper searchAgentTransactionDetailView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setUsecaseId(PortalConstants.KEY_AGENT_DETAIL_TRX_REPORT_USECASE_ID);
        actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,actionLogModel);
        AgentTransactionDetailViewModel model = (AgentTransactionDetailViewModel) wrapper.getBasePersistableModel();
        //CustomList<AgentTransactionDetailViewModel> customList = agentTransactionDetailViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        List<AgentTransactionDetailViewModel> list = agentTransactionDetailViewDao.loadAgentTransactionDetail( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        
        CustomList<AgentTransactionDetailViewModel> customList = new CustomList<AgentTransactionDetailViewModel>();
        customList.setResultsetList(list);
        wrapper.setCustomList( customList );

        actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,actionLogModel);
        return wrapper;
    }
//*********************************************************************************************************************************************    
    @Override
    public SearchBaseWrapper searchAgentTransactionSummaryView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        AgentTransactionSummaryViewModel model = (AgentTransactionSummaryViewModel) wrapper.getBasePersistableModel();
        List<AgentTransactionSummaryViewModel> list = agentTransactionSummaryViewDao.loadAgentTransactionSummary( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        CustomList<AgentTransactionSummaryViewModel> customList = new CustomList<AgentTransactionSummaryViewModel>();
        customList.setResultsetList(list);
        wrapper.setCustomList( customList );
        return wrapper;
    }
    
    @Override
    public SearchBaseWrapper searchHandlerTransactionDetailView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
    	HandlerTransactionDetailViewModel model = (HandlerTransactionDetailViewModel) wrapper.getBasePersistableModel();
        CustomList<HandlerTransactionDetailViewModel> list = handlerTransactionDetailViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( list );
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchBbAccountsByAgentsView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        BbAccountsByAgentsViewModel model = (BbAccountsByAgentsViewModel) wrapper.getBasePersistableModel();
        CustomList<BbAccountsByAgentsViewModel> customList = bbAccountsByAgentsViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( customList );
        return wrapper;
    }

    public void setAgentTransactionDetailViewDao( AgentTransactionDetailViewDao agentTransactionDetailViewDao )
    {
        this.agentTransactionDetailViewDao = agentTransactionDetailViewDao;
    }

    public void setBbAccountsByAgentsViewDao( BbAccountsByAgentsViewDao bbAccountsByAgentsViewDao )
    {
        this.bbAccountsByAgentsViewDao = bbAccountsByAgentsViewDao;
    }

	public void setAgentTransactionSummaryViewDao(AgentTransactionSummaryViewDao agentTransactionSummaryViewDao) {
		this.agentTransactionSummaryViewDao = agentTransactionSummaryViewDao;
	}

	public void setHandlerTransactionDetailViewDao(HandlerTransactionDetailViewDao handlerTransactionDetailViewDao) {
		this.handlerTransactionDetailViewDao = handlerTransactionDetailViewDao;
	}

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }
}

package com.inov8.microbank.server.service.portal.agentcashmovementmodule;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AgentOpeningBalModel;
import com.inov8.microbank.common.model.CashBankMappingModel;
import com.inov8.microbank.common.model.portal.agentcashmodule.AgentCashViewModel;
import com.inov8.microbank.server.dao.portal.agentcashmovementmodule.AgentCashViewDAO;
import com.inov8.microbank.server.dao.tillbalancemodule.AgentOpeningBalanceDAO;
import com.inov8.microbank.server.dao.tillbalancemodule.CashBankMappingDAO;

public class AgentCashMovementManagerImpl implements AgentCashMovementManager {
	  
	private AgentCashViewDAO agentCashViewDAO;
	private AgentOpeningBalanceDAO agentOpeningBalanceDAO;
	private CashBankMappingDAO cashBankMappingDAO;

	public SearchBaseWrapper searchAgentCashMovement(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		ExampleConfigHolderModel configHolderModel = new ExampleConfigHolderModel(false, false, false, MatchMode.EXACT);
        CustomList<AgentCashViewModel> list = this.agentCashViewDAO.findByExample( 
        		(AgentCashViewModel) searchBaseWrapper.getBasePersistableModel(),
        		searchBaseWrapper.getPagingHelperModel(),
        		searchBaseWrapper.getSortingOrderMap(),
        		searchBaseWrapper.getDateRangeHolderModel(),configHolderModel);
        
        searchBaseWrapper.setCustomList(list);
        return searchBaseWrapper;
	}

	public boolean createOrUpdateAgentCashRequiresNewTransaction(AgentOpeningBalModel openingBalModel,CashBankMappingModel cashBankMappingModel) throws FrameworkCheckedException {
		this.agentOpeningBalanceDAO.saveOrUpdate(openingBalModel);
		this.cashBankMappingDAO.saveOrUpdate(cashBankMappingModel);
		return true;
	}

	
    public void setAgentCashViewDAO( AgentCashViewDAO agentCashViewDAO ){
        this.agentCashViewDAO = agentCashViewDAO;
    }

	public void setAgentOpeningBalanceDAO(AgentOpeningBalanceDAO agentOpeningBalanceDAO) {
		this.agentOpeningBalanceDAO = agentOpeningBalanceDAO;
	}

	public void setCashBankMappingDAO(CashBankMappingDAO cashBankMappingDAO) {
		this.cashBankMappingDAO = cashBankMappingDAO;
	}
	
}

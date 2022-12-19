package com.inov8.microbank.server.facade.portal.agentcashmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AgentOpeningBalModel;
import com.inov8.microbank.common.model.CashBankMappingModel;
import com.inov8.microbank.server.service.portal.agentcashmovementmodule.AgentCashMovementManager;

public class AgentCashFacadeImpl implements AgentCashFacade {
	
	private AgentCashMovementManager agentCashMovementManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchAgentCashMovement(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return agentCashMovementManager.searchAgentCashMovement(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setAgentCashMovementManager(AgentCashMovementManager agentCashMovementManager) {
		this.agentCashMovementManager = agentCashMovementManager;
	}

	@Override
	public boolean createOrUpdateAgentCashRequiresNewTransaction(AgentOpeningBalModel openingBalModel,
				CashBankMappingModel cashBankMappingModel) throws FrameworkCheckedException {

		try {
			return agentCashMovementManager.createOrUpdateAgentCashRequiresNewTransaction(openingBalModel, cashBankMappingModel);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}
}

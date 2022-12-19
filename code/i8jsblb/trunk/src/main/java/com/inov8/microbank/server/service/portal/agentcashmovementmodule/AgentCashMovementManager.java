package com.inov8.microbank.server.service.portal.agentcashmovementmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AgentOpeningBalModel;
import com.inov8.microbank.common.model.CashBankMappingModel;

public interface AgentCashMovementManager{
   
	SearchBaseWrapper searchAgentCashMovement(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	boolean createOrUpdateAgentCashRequiresNewTransaction(AgentOpeningBalModel openingBalModel,CashBankMappingModel cashBankMappingModel) throws FrameworkCheckedException;

}

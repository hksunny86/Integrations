package com.inov8.microbank.server.service.tillbalancemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface TillBalanceManager {
	
	  SearchBaseWrapper loadAgentOpeningBalance(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;
	  
	  BaseWrapper loadAgentOpeningBalance(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
	  
	  SearchBaseWrapper loadCashBankMapping(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;
	  
	  BaseWrapper loadCashBankMapping(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
	  
	  BaseWrapper updateAgentOpeningBalance(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

	  BaseWrapper createAgentOpeningBalance(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
	  
	  BaseWrapper agentOpeningBalanceRequiresNewTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
	  public SearchBaseWrapper searchAgentOpeningBalanceByExample(SearchBaseWrapper searchBaseWrapper)throws
	  FrameworkCheckedException;
	  
	  BaseWrapper updateCashBankMapping(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

	  BaseWrapper createCashBankMapping(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
	  public SearchBaseWrapper searchCashBankMappingByExample(SearchBaseWrapper searchBaseWrapper)throws
	  FrameworkCheckedException;

	  

}

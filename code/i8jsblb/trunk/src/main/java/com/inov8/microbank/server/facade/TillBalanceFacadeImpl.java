package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.tillbalancemodule.TillBalanceManager;

public class TillBalanceFacadeImpl implements TillBalanceFacade {
	
	private TillBalanceManager tillBalanceManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	@Override
	public SearchBaseWrapper loadAgentOpeningBalance(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try
	    {
	      this.tillBalanceManager.loadAgentOpeningBalance(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          FIND_ACTION);
	    }
	    return searchBaseWrapper;
	}

	@Override
	public BaseWrapper loadAgentOpeningBalance(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try
	    {
	      this.tillBalanceManager.loadAgentOpeningBalance(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          FIND_BY_PRIMARY_KEY_ACTION);
	    }
	    return baseWrapper;
	}

	@Override
	public SearchBaseWrapper loadCashBankMapping(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		
		try
	    {
	      this.tillBalanceManager.loadCashBankMapping(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          FIND_ACTION);
	    }
	    return searchBaseWrapper;
	}

	@Override
	public BaseWrapper loadCashBankMapping(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try
	    {
	      this.tillBalanceManager.loadCashBankMapping(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          FIND_BY_PRIMARY_KEY_ACTION);
	    }
	    return baseWrapper;
	}

	@Override
	public BaseWrapper updateAgentOpeningBalance(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try
	    {
	      this.tillBalanceManager.updateAgentOpeningBalance(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.UPDATE_ACTION);
	    }
	    return baseWrapper;
	}

	@Override
	public BaseWrapper createAgentOpeningBalance(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		try
	    {
	      this.tillBalanceManager.createAgentOpeningBalance(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.INSERT_ACTION);
	    }
	    return baseWrapper;
	}
	
	@Override
	public BaseWrapper agentOpeningBalanceRequiresNewTransaction(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		try
	    {
	      this.tillBalanceManager.agentOpeningBalanceRequiresNewTransaction(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.INSERT_ACTION);
	    }
	    return baseWrapper;
	}


	@Override
	public SearchBaseWrapper searchAgentOpeningBalanceByExample(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		
		try
	    {
	      this.tillBalanceManager.searchAgentOpeningBalanceByExample(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          FIND_BY_PRIMARY_KEY_ACTION);
	    }
	    return searchBaseWrapper;
	}

	@Override
	public BaseWrapper updateCashBankMapping(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try
	    {
	      this.tillBalanceManager.updateCashBankMapping(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          UPDATE_ACTION);
	    }
	    return baseWrapper;
	}

	@Override
	public BaseWrapper createCashBankMapping(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		try
	    {
	      this.tillBalanceManager.createCashBankMapping(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          INSERT_ACTION);
	    }
	    return baseWrapper;
	}

	@Override
	public SearchBaseWrapper searchCashBankMappingByExample(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try
	    {
	      this.tillBalanceManager.searchCashBankMappingByExample(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          FIND_ACTION);
	    }
	    return searchBaseWrapper;
	}

	public void setTillBalanceManager(TillBalanceManager tillBalanceManager) {
		this.tillBalanceManager = tillBalanceManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}

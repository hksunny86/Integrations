package com.inov8.microbank.server.facade.portal.chargebackmodule;

import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.chargebackmodule.ChargebackListViewModel;
import com.inov8.microbank.server.service.portal.chargebackmodule.ChargebackManager;

public class ChargebackFacadeImpl implements ChargebackFacade {
	
	private ChargebackManager chargebackManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchChargebackTransaction(SearchBaseWrapper searchBaseWrapper) throws Exception {
		// TODO Auto-generated method stub
		try
	    {
			return this.chargebackManager.searchChargebackTransaction(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.FIND_ACTION);
	    }
		
	}

	public void setChargebackManager(ChargebackManager chargebackManager) {
		this.chargebackManager = chargebackManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public CustomList<ChargebackListViewModel> getChargebackTransactions(SearchBaseWrapper wrapper) throws Exception {
		try{
			return this.chargebackManager.getChargebackTransactions(wrapper);
	    }catch (Exception ex){
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.FIND_ACTION);
	    }
	}

}

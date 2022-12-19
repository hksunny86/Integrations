package com.inov8.microbank.server.service.currencycodemodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CurrencyCodeModel;
import com.inov8.microbank.server.dao.currencycodemodule.CurrencyCodeDAO;

public class CurrencyCodeManagerImpl implements CurrencyCodeManager {

	private CurrencyCodeDAO currencyCodeDAO; 
	public SearchBaseWrapper loadCurrencyCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
	 
		CustomList list = this.currencyCodeDAO.findByExample((CurrencyCodeModel)searchBaseWrapper.getBasePersistableModel());
		
		List<CurrencyCodeModel> currencyCodeModelList = list.getResultsetList() ;
		
		if( currencyCodeModelList != null && currencyCodeModelList.size() > 0 )
			searchBaseWrapper.setBasePersistableModel( currencyCodeModelList.get(0) ) ;
		
	return searchBaseWrapper;	
	}
	public void setCurrencyCodeDAO(CurrencyCodeDAO currencyCodeDAO) {
		this.currencyCodeDAO = currencyCodeDAO;
	}
	
	

}

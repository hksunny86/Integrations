package com.inov8.microbank.server.service.portal.chargebackmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.chargebackmodule.ChargebackListViewModel;

public interface ChargebackManager {
	
	
	public SearchBaseWrapper searchChargebackTransaction(SearchBaseWrapper searchBaseWrapper)throws Exception;
	public CustomList<ChargebackListViewModel> getChargebackTransactions(SearchBaseWrapper wrapper) throws Exception;

}

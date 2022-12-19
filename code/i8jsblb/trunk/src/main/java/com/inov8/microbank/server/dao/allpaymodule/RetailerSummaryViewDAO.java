package com.inov8.microbank.server.dao.allpaymodule;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.allpaymodule.RetailerSummaryViewModel;

public interface RetailerSummaryViewDAO 
extends BaseDAO<RetailerSummaryViewModel, Long>
{
	public SearchBaseWrapper getRetailerSummary( SearchBaseWrapper searchBaseWrapper );
	
}

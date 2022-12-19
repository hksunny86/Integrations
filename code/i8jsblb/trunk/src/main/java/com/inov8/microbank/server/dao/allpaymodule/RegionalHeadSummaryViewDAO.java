package com.inov8.microbank.server.dao.allpaymodule;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.allpaymodule.RegionalHeadSummaryViewModel;

public interface RegionalHeadSummaryViewDAO 
	extends BaseDAO<RegionalHeadSummaryViewModel, Long> 
{
	
	public SearchBaseWrapper getRegionalHeadSummary( SearchBaseWrapper searchBaseWrapper );
	
	
}

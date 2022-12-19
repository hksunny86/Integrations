package com.inov8.microbank.server.dao.allpaymodule;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.allpaymodule.DistHeadSummaryViewModel;

public interface DistHeadSummaryViewDAO extends BaseDAO<DistHeadSummaryViewModel, Long>
{
	SearchBaseWrapper getDistHeadSummary( SearchBaseWrapper searchBaseWrapper );
}

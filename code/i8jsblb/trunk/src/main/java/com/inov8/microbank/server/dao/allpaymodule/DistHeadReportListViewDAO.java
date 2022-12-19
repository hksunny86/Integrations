package com.inov8.microbank.server.dao.allpaymodule;

import java.util.List;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.DistHeadReportListViewModel;

public interface DistHeadReportListViewDAO 
extends BaseDAO<DistHeadReportListViewModel, Long>
{
	public List<DistHeadReportListViewModel> getReport( SearchBaseWrapper searchBaseWrapper );
}

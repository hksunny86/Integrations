package com.inov8.microbank.server.dao.complaintsmodule;

import java.util.List;
import java.util.Map;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ComplaintModel;
import com.inov8.microbank.common.model.ComplaintReportModel;

public interface ComplaintDAO extends BaseDAO<ComplaintModel, Long> {

	public void updateComplaintStatus(Map<Long, String> updateStatusMap, Map<Long, String> updateEscStatusMap, List<ComplaintReportModel> complaintReportModelList);
}
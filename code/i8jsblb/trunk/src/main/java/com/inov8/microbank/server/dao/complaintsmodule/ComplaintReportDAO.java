package com.inov8.microbank.server.dao.complaintsmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ComplaintReportModel;

public interface ComplaintReportDAO extends BaseDAO<ComplaintReportModel, Long> {
	public List<ComplaintReportModel> searchComplaintByConsumerNo(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	public List<ComplaintReportModel> searchUserComplaintHistory(Long appUserId) throws FrameworkCheckedException;
}
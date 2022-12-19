package com.inov8.microbank.server.dao.complaintsmodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ComplaintHistoryModel;
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryVO;

public interface ComplaintHistoryDAO extends BaseDAO<ComplaintHistoryModel, Long> {
	
	public List<ComplaintHistoryModel> getComplaintHistoryModelList(ComplaintHistoryModel complaintHistoryModel);	
	public List<ComplaintHistoryVO> getComplaintHistoryVOList(Long complaintId);
	public void updateComplaintHistoryStatus(Long complaintId,String complaintStatus, String remarks);
}
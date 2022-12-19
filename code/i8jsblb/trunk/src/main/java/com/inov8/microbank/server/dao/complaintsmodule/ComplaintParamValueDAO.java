package com.inov8.microbank.server.dao.complaintsmodule;

import java.util.Map;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ComplaintParamValueModel;

public interface ComplaintParamValueDAO extends BaseDAO<ComplaintParamValueModel, Long> {

	public Map<String, String> getComplaintParamValueMap(Long complaintId);
}
package com.inov8.microbank.server.dao.messagemodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.FailedSmsModel;

public interface FailedSmsDAO extends BaseDAO<FailedSmsModel, Long>{
	public List<FailedSmsModel> getFailedSmsList(Long chunkSize);
	public void saveFailedSms(FailedSmsModel failedSmsModel);
	
}

package com.inov8.microbank.server.dao.messagingmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.MiddlewareRetryAdviceModel;

public interface MiddlewareRetryAdviceDAO extends BaseDAO<MiddlewareRetryAdviceModel, Long>
{

	public void updateRetryAdviceReportStatus(Long trxCodeId) throws FrameworkCheckedException;

}

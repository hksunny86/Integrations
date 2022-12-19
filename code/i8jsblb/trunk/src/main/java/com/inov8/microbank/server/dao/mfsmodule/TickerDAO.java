package com.inov8.microbank.server.dao.mfsmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.TickerModel;

public interface TickerDAO extends BaseDAO <TickerModel, Long>
{
	public BaseWrapper loadDefaultTicker();
}

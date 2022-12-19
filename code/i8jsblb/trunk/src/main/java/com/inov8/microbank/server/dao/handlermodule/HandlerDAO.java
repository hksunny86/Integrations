package com.inov8.microbank.server.dao.handlermodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.HandlerModel;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public interface HandlerDAO extends BaseDAO<HandlerModel, Long> {
	
	public List<Long> getRetalerDataMap(Long retailerContactId);
}

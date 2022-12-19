package com.inov8.microbank.server.dao.commissionmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CommissionRateDefaultModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface CommissionRateDefaultDAO
    extends BaseDAO<CommissionRateDefaultModel, Long>
{
	List<CommissionRateDefaultModel> loadDefaultCommissionRateList(CommissionRateDefaultModel defaultRateModel);
}

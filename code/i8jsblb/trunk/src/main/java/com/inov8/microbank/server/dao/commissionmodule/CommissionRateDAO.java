package com.inov8.microbank.server.dao.commissionmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.vo.product.CommissionRateVO;

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
public interface CommissionRateDAO
    extends BaseDAO<CommissionRateModel, Long>
{
	public boolean getDuplicateCommissionRateRecords(CommissionRateModel commissionRateModel);
	public boolean isCommissionRangeValid(CommissionRateModel commissionRateModel) throws FrameworkCheckedException;
	List<CommissionRateModel> loadCommissionRateList(CommissionRateModel vo);
}

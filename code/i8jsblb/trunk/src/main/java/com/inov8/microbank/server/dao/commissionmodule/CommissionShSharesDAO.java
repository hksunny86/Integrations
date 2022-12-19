package com.inov8.microbank.server.dao.commissionmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.vo.product.CommissionShSharesVO;

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
public interface CommissionShSharesDAO
    extends BaseDAO<CommissionShSharesModel, Long>
{
	boolean removeCommissionShSharesByStakeholderIds(List<Long> removeSharesList) throws FrameworkCheckedException; 
	boolean removeCommissionShSharesByShShareIds(List<Long> removeSharesList) throws FrameworkCheckedException;
	List<CommissionShSharesModel>  loadCommissionShSharesList(CommissionShSharesModel vo) throws FrameworkCheckedException;
}

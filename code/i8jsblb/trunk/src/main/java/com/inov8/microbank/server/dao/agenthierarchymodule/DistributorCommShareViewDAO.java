package com.inov8.microbank.server.dao.agenthierarchymodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommShareViewModel;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Rashid Mahmood
 * @version 1.0
 */

public interface DistributorCommShareViewDAO
    extends BaseDAO<DistributorCommShareViewModel, Long>
{
	List<DistributorCommShareViewModel> loadDistributorCommissionSharesList(DistributorCommShareViewModel commShareViewModel);
}

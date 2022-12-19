package com.inov8.microbank.server.dao.commissionmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CommissionShSharesDefaultModel;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.vo.product.CommissionShSharesDefaultVO;

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
public interface CommissionShSharesDefaultDAO
    extends BaseDAO<CommissionShSharesDefaultModel, Long>
{
//	List<CommissionShSharesDefaultModel>  loadDefaultCommissionShSharesList(CommissionShSharesDefaultModel defaultSharesModel) throws FrameworkCheckedException;

    List<CommissionShSharesDefaultModel> commissionShSharesDefaultLoad(Long productId);

}

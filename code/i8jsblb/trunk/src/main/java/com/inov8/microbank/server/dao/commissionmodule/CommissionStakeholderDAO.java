package com.inov8.microbank.server.dao.commissionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.vo.product.CommissionStakeholderVO;

import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface CommissionStakeholderDAO
    extends BaseDAO<CommissionStakeholderModel, Long>
{

    List<CommissionStakeholderModel> findAllStakeHolders() throws FrameworkCheckedException;

    CommissionStakeholderModel findStakeHolderById(Long stakeHolderId) throws FrameworkCheckedException;
}

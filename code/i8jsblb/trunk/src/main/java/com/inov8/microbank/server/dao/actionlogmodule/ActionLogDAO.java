package com.inov8.microbank.server.dao.actionlogmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ActionLogModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public interface ActionLogDAO extends BaseDAO<ActionLogModel, Long>
{
    ActionLogModel getActionLogModelByActionAuthId(Long actionAuthId) throws FrameworkCheckedException;

    ActionLogModel findByPrimaryKey(ActionLogModel actionLogModel);
}

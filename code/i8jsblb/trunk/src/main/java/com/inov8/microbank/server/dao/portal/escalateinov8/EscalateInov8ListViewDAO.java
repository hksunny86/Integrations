package com.inov8.microbank.server.dao.portal.escalateinov8;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.escalateinov8module.EscalateInov8ListViewModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public interface EscalateInov8ListViewDAO
                extends BaseDAO<EscalateInov8ListViewModel, Long>
{
	public Long findAppUserByBankId(BaseWrapper wrapper);
	public Long findAppUserBySupplierId(BaseWrapper wrapper);
}


package com.inov8.microbank.server.dao.distributormodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.DistributorContactModel;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public interface DistributorContactDAO
    extends BaseDAO<DistributorContactModel, Long>
{
  public boolean isManagingContact( Long fromDistributor, Long toDistributor ) ;
}

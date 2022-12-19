package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;

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

public interface DistributorFacade
    extends
    DistributorManager, DistributorLevelManager, DistributorContactManager

{

}

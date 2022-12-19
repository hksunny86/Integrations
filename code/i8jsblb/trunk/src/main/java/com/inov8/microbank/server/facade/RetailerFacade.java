package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.retailermodule.RetailerCommissionManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.retailermodule.RetailerManager;
import com.inov8.microbank.server.service.retailermodule.RetailerTransactionManager;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Asad Hayat
 * @version 1.0
 *
 */

public interface RetailerFacade
    extends RetailerTransactionManager
    , RetailerCommissionManager, RetailerManager, RetailerContactManager
{
}

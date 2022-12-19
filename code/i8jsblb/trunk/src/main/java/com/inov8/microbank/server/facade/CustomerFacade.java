package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.customermodule.CustomerManager;

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
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public interface CustomerFacade
    extends CustTransManager,CustomerManager
{

}

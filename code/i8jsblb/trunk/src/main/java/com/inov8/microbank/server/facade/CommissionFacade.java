package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionRateManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionReasonManager;

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
public interface CommissionFacade
    extends CommissionManager, CommissionRateManager, CommissionReasonManager
{
}

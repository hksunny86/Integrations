package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.creditmodule.CreditInfoManager;
import com.inov8.microbank.server.service.creditmodule.DistDistCreditManager;
import com.inov8.microbank.server.service.creditmodule.DistRetCreditManager;
import com.inov8.microbank.server.service.creditmodule.Inov8CustomerCreditManager;
import com.inov8.microbank.server.service.creditmodule.Inov8DistributorCreditManager;
import com.inov8.microbank.server.service.creditmodule.RetCustomerCreditManager;
import com.inov8.microbank.server.service.creditmodule.RetHeadRetCreditManager;
import com.inov8.microbank.server.service.creditmodule.RetRetCreditManager;

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

public interface CreditFacade
    extends RetHeadRetCreditManager, RetRetCreditManager,
    Inov8DistributorCreditManager, DistDistCreditManager, DistRetCreditManager,
    RetCustomerCreditManager, Inov8CustomerCreditManager,CreditInfoManager

{
}

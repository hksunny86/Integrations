package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.operatormodule.OperatorBankInfoManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.operatormodule.OperatorUserManager;

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
public interface OperatorFacade
    extends OperatorManager,OperatorUserManager, OperatorBankInfoManager
{

}

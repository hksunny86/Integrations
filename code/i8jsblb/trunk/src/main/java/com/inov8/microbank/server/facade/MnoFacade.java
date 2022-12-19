package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.mnomodule.MnoDialingCodeManager;
import com.inov8.microbank.server.service.mnomodule.MnoManager;
import com.inov8.microbank.server.service.mnomodule.MnoUserManager;

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

public interface MnoFacade
    extends MnoUserManager,MnoManager,MnoDialingCodeManager
{

}

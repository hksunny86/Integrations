package com.inov8.microbank.server.facade;

import com.inov8.framework.server.service.common.PopupManager;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.server.service.common.AreaManager;

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
public interface CommonFacade
    extends ReferenceDataManager, PopupManager,AreaManager
{
}

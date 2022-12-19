package com.inov8.microbank.server.service.integrationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.IntegrationModuleModel;
import com.inov8.microbank.server.dao.integrationmodule.IntegrationModuleDAO;

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
public class IntegrationModuleManagerImpl
    implements IntegrationModuleManager
{
  private IntegrationModuleDAO integrationModuleDAO;

  public IntegrationModuleManagerImpl()
  {}

  public SearchBaseWrapper searchIntegrationModule(SearchBaseWrapper
      searchBaseWrapper) throws
      FrameworkCheckedException
  {
    CustomList<IntegrationModuleModel>
        list = this.integrationModuleDAO.findByExample( (IntegrationModuleModel)
        searchBaseWrapper.getBasePersistableModel());

    searchBaseWrapper.setCustomList(list);

    return searchBaseWrapper;
  }

  public void setIntegrationModuleDAO(IntegrationModuleDAO integrationModuleDAO)
  {
    this.integrationModuleDAO = integrationModuleDAO;
  }
}

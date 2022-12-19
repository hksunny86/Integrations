package com.inov8.microbank.server.service.distributormodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorLvlRetContactViewModel;

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

public interface DistributorLevelManager
{
  public SearchBaseWrapper loadDistributorLevel(SearchBaseWrapper
                                                searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadDistributorLevel(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchDistributorLevel(SearchBaseWrapper
                                                  searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createOrUpdateDistributorLevel(BaseWrapper
      baseWrapper) throws FrameworkCheckedException;
  
  public BaseWrapper updateDistributorLevel(BaseWrapper
	      baseWrapper) throws FrameworkCheckedException;

  public boolean findDistributorContactByDistributorLevelId(BaseWrapper
      baseWrapper) throws FrameworkCheckedException;
  
  public List<DistributorLevelModel> searchDistributorLevelModels(Long distributorId,String distributorLevelHQL) throws
  FrameworkCheckedException;

  public SearchBaseWrapper getParentAgentsBydistributorLevelId(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

  List<DistributorLvlRetContactViewModel> getParentAgentsByDistributorLevelId(Long retailerId, Long distributorLevelId, Long distributorId) throws FrameworkCheckedException;

}

package com.inov8.microbank.server.service.distributormodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DistributorModel;

import java.util.List;

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

public interface DistributorManager
{

    public List<DistributorModel> findAllDistributor();

  public SearchBaseWrapper loadDistributor(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchDistributorsByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

  public List<DistributorModel> searchDistributorsByExample(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
  
  public BaseWrapper loadDistributor(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchDistributor(SearchBaseWrapper
                                             searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createOrUpdateDistributor(BaseWrapper
                                               baseWrapper) throws
      FrameworkCheckedException;

  public boolean findDistributorLevelByDistributorId(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
  public BaseWrapper saveOrUpdateDistributor(BaseWrapper baseWrapper) throws FrameworkCheckedException;

  public List<DistributorModel> findActiveDistributor();
}

package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorLvlRetContactViewModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;

import java.util.ArrayList;
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

public class DistributorFacadeImpl
    implements DistributorFacade
{
  private FrameworkExceptionTranslator frameworkExceptionTranslator;

  private DistributorManager distributorManager;
  private DistributorLevelManager distributorLevelManager;
  private DistributorContactManager distributorContactManager;

  public SearchBaseWrapper searchDistributorsByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
  {
	  try
	  {
	  	this.distributorManager.searchDistributorsByCriteria(searchBaseWrapper);
	  }
	  catch (Exception ex)
	  {
		throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_ACTION);
	  }
	  return searchBaseWrapper;
  }

  public List<DistributorModel> searchDistributorsByExample(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
  {
    List<DistributorModel> distributorModelList=new ArrayList<>();
    try
    {
       distributorModelList=this.distributorManager.searchDistributorsByExample(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return distributorModelList;
  }

  @Override
  public List<DistributorModel> findAllDistributor() {
    return null;
  }

  public SearchBaseWrapper loadDistributor(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorManager.loadDistributor(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return searchBaseWrapper;
  }

  public BaseWrapper loadDistributor(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorManager.loadDistributor(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }
    return baseWrapper;
  }

  public SearchBaseWrapper searchDistributor(SearchBaseWrapper
                                             searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorManager.searchDistributor(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;
  }

  public BaseWrapper createOrUpdateDistributor(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorManager.createOrUpdateDistributor(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;
  }

  public void setDistributorManager(DistributorManager distributorManager)
  {
    this.distributorManager = distributorManager;
  }

  public void setDistributorLevelManager(DistributorLevelManager
                                         distributorLevelManager)
  {
    this.distributorLevelManager = distributorLevelManager;
  }

  public void setDistributorContactManager(DistributorContactManager
                                           distributorContactManager)
  {
    this.distributorContactManager = distributorContactManager;
  }

  public boolean findDistributorLevelByDistributorId(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    boolean isFound;
    try
    {
      isFound = this.distributorManager.findDistributorLevelByDistributorId(
          baseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return isFound;
  }

  /*
   ========================
   *  Distributor Contact *
   ========================
   */

  public SearchBaseWrapper loadDistributorContact(SearchBaseWrapper
                                                  searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorContactManager.loadDistributorContact(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return searchBaseWrapper;

  }

  public BaseWrapper loadDistributorContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorContactManager.loadDistributorContact(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return baseWrapper;

  }

  public SearchBaseWrapper searchDistributorContact(SearchBaseWrapper
      searchBaseWrapper) throws FrameworkCheckedException
  {
    try
    {
      this.distributorContactManager.searchDistributorContact(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper createDistributorContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorContactManager.createDistributorContact(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper updateDistributorContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorContactManager.updateDistributorContact(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }
  
  public BaseWrapper updateDistributorContactModel(BaseWrapper baseWrapper) throws
  FrameworkCheckedException
{
try
{
  this.distributorContactManager.updateDistributorContactModel(baseWrapper);
}
catch (Exception ex)
{
  throw this.frameworkExceptionTranslator.translate(ex,
      FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
}
return baseWrapper;

}

  public BaseWrapper createAppUserForDistributorContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    {
      try
      {
        this.distributorContactManager.createAppUserForDistributorContact(
            baseWrapper);
      }
      catch (Exception ex)
      {
        throw this.frameworkExceptionTranslator.translate(ex,
            FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
      }
      return baseWrapper;

    }
  }

  public boolean findDistributorCreditByDistributorContactId(BaseWrapper
      baseWrapper) throws FrameworkCheckedException
  {
    boolean isFound;
    try
    {
      isFound = this.distributorContactManager.
          findDistributorCreditByDistributorContactId(baseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return isFound;
  }

  /*
    ======================
   *  Distributor Level *
    ======================
   */
  public SearchBaseWrapper loadDistributorLevel(SearchBaseWrapper
                                                searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorLevelManager.loadDistributorLevel(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return searchBaseWrapper;

  }

  public BaseWrapper loadDistributorLevel(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorLevelManager.loadDistributorLevel(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return baseWrapper;

  }

  public SearchBaseWrapper searchDistributorLevel(SearchBaseWrapper
                                                  searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorLevelManager.searchDistributorLevel(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper createOrUpdateDistributorLevel(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.distributorLevelManager.createOrUpdateDistributorLevel(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }
  
  public BaseWrapper updateDistributorLevel(BaseWrapper baseWrapper) throws
  FrameworkCheckedException
{
try
{
  this.distributorLevelManager.updateDistributorLevel(baseWrapper);
}
catch (Exception ex)
{
  throw this.frameworkExceptionTranslator.translate(ex,
      FrameworkExceptionTranslator.UPDATE_ACTION);
}
return baseWrapper;

}


  public boolean findDistributorContactByDistributorLevelId(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException
  {
    boolean isFound;
    try
    {
      isFound = this.distributorLevelManager.
          findDistributorContactByDistributorLevelId(baseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return isFound;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

public DistributorContactModel findDistributorNationalManagerContact(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	// TODO Auto-generated method stub
	DistributorContactModel nationalManager = null;
	try
    {
      nationalManager = this.distributorContactManager.findDistributorNationalManagerContact(searchBaseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return nationalManager;


}

  public DistributorContactModel findDistributorContactByMobileNumber(
      SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
  {

    DistributorContactModel distributorContactManager = null;
    try
    {
      distributorContactManager = this.distributorContactManager.
          findDistributorContactByMobileNumber(searchBaseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return distributorContactManager;

  }

  public BaseWrapper loadDistributorContactByAppUser(
      BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
    try
    {
      baseWrapper = this.distributorContactManager.
          loadDistributorContactByAppUser(baseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return baseWrapper;
  }

  public boolean isManagingContact( Long fromDistributor, Long toDistributor )throws
      FrameworkCheckedException
  {
    try
    {
      return this.distributorContactManager.isManagingContact( fromDistributor, toDistributor ) ;
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }

  }

  public boolean isDistributorContact(  SearchBaseWrapper searchBaseWrapper )throws
  FrameworkCheckedException
{
try
{
  return this.distributorContactManager.isDistributorContact( searchBaseWrapper) ;
}

catch (Exception ex)
{
  throw this.frameworkExceptionTranslator.translate(ex,
      this.frameworkExceptionTranslator.FIND_ACTION);
}

}

  
  public boolean isDistributorContactHead( SearchBaseWrapper searchBaseWrapper)throws
  FrameworkCheckedException
{
try
{
  return this.distributorContactManager.isDistributorContactHead( searchBaseWrapper ) ;
}

catch (Exception ex)
{
  throw this.frameworkExceptionTranslator.translate(ex,
      this.frameworkExceptionTranslator.FIND_ACTION);
}

}

  public boolean isDistributorContactActive( SearchBaseWrapper searchBaseWrapper)throws
  FrameworkCheckedException
{
try
{
  return this.distributorContactManager.isDistributorContactActive( searchBaseWrapper ) ;
}

catch (Exception ex)
{
  throw this.frameworkExceptionTranslator.translate(ex,
      this.frameworkExceptionTranslator.FIND_ACTION);
}

}

  public boolean isDistributorActive( SearchBaseWrapper searchBaseWrapper)throws
  FrameworkCheckedException
{
try
{
  return this.distributorContactManager.isDistributorActive( searchBaseWrapper ) ;
}

catch (Exception ex)
{
  throw this.frameworkExceptionTranslator.translate(ex,
      this.frameworkExceptionTranslator.FIND_ACTION);
}

}
  
  public List<DistributorLevelModel> searchDistributorLevelModels(Long distributorId,String distributorLevelHQL) throws FrameworkCheckedException {
		 List<DistributorLevelModel> distributorLevelModelList = null;
		try
	    {
			distributorLevelModelList  =this.distributorLevelManager.searchDistributorLevelModels(distributorId,distributorLevelHQL);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    
	    return distributorLevelModelList;
	}
  
  
  public Long getAppUserPartnerGroupId(Long appUserId) throws FrameworkCheckedException {
		 
		Long getAppUserPartnerGroupId;
		try
		    {
		       getAppUserPartnerGroupId = this.distributorContactManager.getAppUserPartnerGroupId(appUserId);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex,
		          FrameworkExceptionTranslator.FIND_ACTION);
		    }
		    return getAppUserPartnerGroupId;

	}

public BaseWrapper getNationalDistributor() throws FrameworkCheckedException
{
	BaseWrapper baseWrapper ;
	try
    {
		baseWrapper = this.distributorContactManager.getNationalDistributor();
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.FIND_ACTION);
    }
    return baseWrapper;
}

@Override
public BaseWrapper saveOrUpdateDistributor(BaseWrapper baseWrapper)
		throws FrameworkCheckedException {
	// TODO Auto-generated method stub
	 try
	    {
	      this.distributorManager.saveOrUpdateDistributor(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
	    }
	  return baseWrapper;
	}

	@Override
	public List<DistributorModel> findActiveDistributor() {
		
		return distributorManager.findActiveDistributor();
	}
	
	@Override
	public SearchBaseWrapper getParentAgentsBydistributorLevelId(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		try
	    {
	      this.distributorLevelManager.getParentAgentsBydistributorLevelId(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return searchBaseWrapper;
	}

  @Override
  public List<DistributorLvlRetContactViewModel> getParentAgentsByDistributorLevelId(Long retailerId, Long distributorLevelId, Long distributorId) throws FrameworkCheckedException {
    return distributorLevelManager.getParentAgentsByDistributorLevelId(retailerId,distributorLevelId,distributorId);
  }

}

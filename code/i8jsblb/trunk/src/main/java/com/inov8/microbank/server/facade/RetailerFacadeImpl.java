package com.inov8.microbank.server.facade;


import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactSearchViewModel;
import com.inov8.microbank.common.vo.RetailerContactDetailVO;
import com.inov8.microbank.server.service.retailermodule.RetailerCommissionManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.retailermodule.RetailerManager;
import com.inov8.microbank.server.service.retailermodule.RetailerTransactionManager;

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
 * @author Asad Hayat
 * @version 1.0
 *
 */

public class RetailerFacadeImpl
    implements RetailerFacade
{

  private RetailerTransactionManager retailerTransactionManager;
  private RetailerManager retailerManager;
  private RetailerCommissionManager retailerCommissionManager;
  private RetailerContactManager retailerContactManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;


  public SearchBaseWrapper searchRetailerTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
  {
    try
    {
      this.retailerTransactionManager.searchRetailerTransaction(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

    @Override
    public SearchBaseWrapper searchRetailerTransactionView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return retailerTransactionManager.searchRetailerTransactionView( searchBaseWrapper );
        }
        catch( Exception ex )
        {
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    @Override
    public SearchBaseWrapper fetchRegionalRetailActivitySummary( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return retailerTransactionManager.fetchRegionalRetailActivitySummary( searchBaseWrapper );
        }
        catch( Exception ex )
        {
            throw frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

  public SearchBaseWrapper searchRetailerCommission(SearchBaseWrapper
      searchBaseWrapper) throws FrameworkCheckedException
  {
    try
    {
      this.retailerCommissionManager.searchRetailerCommission(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public void setRetailerCommissionManager(RetailerCommissionManager
                                           retailerCommissionManager)
  {
    this.retailerCommissionManager = retailerCommissionManager;
  }

  public void setRetailerTransactionManager(RetailerTransactionManager
                                            retailerTransactionManager)
  {
    this.retailerTransactionManager = retailerTransactionManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  public void setRetailerManager(RetailerManager retailerManager)
  {
    this.retailerManager = retailerManager;
  }

  public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
	  this.retailerContactManager = retailerContactManager;
  }

  //======================================================================
  // Methods for RetailerManager
  //======================================================================


  public SearchBaseWrapper loadRetailer(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retailerManager.loadRetailer(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper loadRetailer(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retailerManager.loadRetailer(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;

  }

  public SearchBaseWrapper searchRetailer(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retailerManager.searchRetailer(searchBaseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper createOrUpdateRetailer(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retailerManager.createOrUpdateRetailer(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;

  }

  //======================================================================
  // Methods for RetailerContactManager
  //======================================================================


  public SearchBaseWrapper loadRetailerContact(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retailerContactManager.loadRetailerContact(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;

  }

  public RetailerContactModel findRetailerContactByMobileNumber(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
  {
    try
    {
      return this.retailerContactManager.findRetailerContactByMobileNumber(searchBaseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
    }
}



  public BaseWrapper createRetailerContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.retailerContactManager.createRetailerContact(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;

  }


public BaseWrapper loadRetailerContact(BaseWrapper baseWrapper) throws FrameworkCheckedException {

	try
	{
		baseWrapper = this.retailerContactManager.loadRetailerContact(baseWrapper);
	}
	catch (Exception ex)
	{
	  throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
	}
	return baseWrapper;

}



public SearchBaseWrapper searchRetailerContact(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	// TODO Auto-generated method stub
	try
	{
	  this.retailerContactManager.searchRetailerContact(searchBaseWrapper);
	}
	catch (Exception ex)
	{
	  throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
	}
	return searchBaseWrapper;

}

      public BaseWrapper loadRetailerContactByAppUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
      {
        try
        {
          baseWrapper = this.retailerContactManager.loadRetailerContactByAppUser(baseWrapper);
        }
        catch (Exception ex)
        {
          throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
        }
        return baseWrapper;
      }


public SearchBaseWrapper searchRetailerContactByMobileNo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	// TODO Auto-generated method stub
	try
	{
	  searchBaseWrapper.setBasePersistableModel(this.retailerContactManager.findRetailerContactByMobileNumber(searchBaseWrapper)) ;
	}
	catch (Exception ex)
	{
	  throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
	}
	return searchBaseWrapper;
}

      public RetailerContactModel findRetailerHeadContact(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {

        try
        {
          return this.retailerContactManager.findRetailerHeadContact(searchBaseWrapper);
        }
        catch (Exception ex)
        {
          throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
        }

      }

	public BaseWrapper createAppUserForRetailerContact(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		 try
	      {
	        this.retailerContactManager.createAppUserForRetailerContact(
	            baseWrapper);
	      }
	      catch (Exception ex)
	      {
	        throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
	      }
	      return baseWrapper;

	}

	public BaseWrapper updateRetailerContact(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      this.retailerContactManager.updateRetailerContact(
	          baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
	    }
	    return baseWrapper;
	}

	public SearchBaseWrapper loadRetailerContactListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		 try
		    {
		      this.retailerContactManager.loadRetailerContactListView(searchBaseWrapper);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		    }
		    return searchBaseWrapper;
	}

	@Override
	public SearchBaseWrapper findExactRetailerContactListViewModel(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.retailerContactManager.findExactRetailerContactListViewModel(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper createOrUpdateRetailerContact(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      this.retailerContactManager.createOrUpdateRetailerContact(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
	    }
	    return baseWrapper;
	}

	public Long getAppUserPartnerGroupId(Long appUserId) throws FrameworkCheckedException {
		
		Long getAppUserPartnerGroupId;
		try
		    {
		       getAppUserPartnerGroupId = this.retailerContactManager.getAppUserPartnerGroupId(appUserId);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		    }
		    return getAppUserPartnerGroupId;

	}
      
	public  AppUserPartnerGroupModel getAppUserPartnetGroupByAppUserId(Long appUserId)throws FrameworkCheckedException{
		 AppUserPartnerGroupModel appUserPartnerGroup;
			try
			    {
				appUserPartnerGroup = this.retailerContactManager.getAppUserPartnetGroupByAppUserId(appUserId);
			    }
			    catch (Exception ex)
			    {
			      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
			    }
			    return appUserPartnerGroup;
	 } 
	
	public BaseWrapper loadHeadRetailerContactAppUser( Long retailerId ) throws FrameworkCheckedException
	{
		BaseWrapper baseWrapper;
		try
		{
			baseWrapper = retailerContactManager.loadHeadRetailerContactAppUser( retailerId );
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
		return baseWrapper;

	}
	
	public RetailerContactModel findHeadRetailerContactModelByRetailerId( Long retailerId ) throws FrameworkCheckedException
	{
		RetailerContactModel model;
		try
		{
			model = retailerContactManager.findHeadRetailerContactModelByRetailerId( retailerId );
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
		return model;

	}
///////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int countByExample(RetailerContactModel agentModel,
			ExampleConfigHolderModel exampleConfigHolder) {
			return this.retailerContactManager.countByExample(agentModel, exampleConfigHolder);
	}
    
	@Override
	public SearchBaseWrapper loadRetailerByInitialAppFormNo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return retailerContactManager.loadRetailerByInitialAppFormNo( searchBaseWrapper );
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	@Override
	public SearchBaseWrapper loadRetailerContactAddresses(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return retailerContactManager.loadRetailerContactAddresses( searchBaseWrapper );
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public BaseWrapper loadRetailerContactByIAF(String initialAppFormNo)
			throws FrameworkCheckedException {
		return retailerContactManager.loadRetailerContactByIAF(initialAppFormNo);
	}

	@Override
	public List<RetailerContactDetailVO> findRetailerContactModelList()
			throws FrameworkCheckedException {
		try
		{
			return retailerContactManager.findRetailerContactModelList();
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public RetailerContactModel loadRetailerContactByRetailerContactId(
			Long retailerContactId) throws FrameworkCheckedException {
		return retailerContactManager.loadRetailerContactByRetailerContactId(retailerContactId);
	}

	@Override
	public RetailerContactModel saveOrUpdateRetailerContactModel(
			RetailerContactModel mmodel) throws FrameworkCheckedException {
		return retailerContactManager.saveOrUpdateRetailerContactModel(mmodel);
	}
}

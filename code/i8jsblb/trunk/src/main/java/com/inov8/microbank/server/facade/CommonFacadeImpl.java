package com.inov8.microbank.server.facade;

import java.io.Serializable;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.PopupWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.service.common.PopupManager;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.server.service.common.AreaManager;

/**
 * <p>Title: Microbank</p>

 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */
public class CommonFacadeImpl
    implements CommonFacade
{
  private ReferenceDataManager referenceDataManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;
  private PopupManager popupManager;
  private AreaManager areaManager;

  public CommonFacadeImpl()
  {
  }

  /**======================================================================
   Methods for AreaManager
  ======================================================================
  */
  public SearchBaseWrapper loadArea(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.areaManager.loadArea(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;
  }


  public BaseWrapper loadArea(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.areaManager.loadArea(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;
  }

  public SearchBaseWrapper searchArea(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.areaManager.searchArea(searchBaseWrapper);
    }
    catch (Exception ex)
        {
        throw this.frameworkExceptionTranslator.translate(ex,
            this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;
  }

  public BaseWrapper createOrUpdateArea(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.areaManager.createOrUpdateArea(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
  }

  public boolean findDistributorContactByAreaId(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
     boolean isFound;
    try
    {
      isFound = this.areaManager.findDistributorContactByAreaId(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
    }
    return isFound;
  }





  /**======================================================================
    // End Methods for AreaManager
  ======================================================================
*/

  public ReferenceDataWrapper getReferenceData(ReferenceDataWrapper
                                               referenceDataWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.FIND_ACTION);
    }
    return referenceDataWrapper;
  }

  	@Override
	public ReferenceDataWrapper getReferenceData(ReferenceDataWrapper referenceDataWrapper, Long... idsToFilter) throws FrameworkCheckedException
	{
  		try
  	    {
  	      this.referenceDataManager.getReferenceData(referenceDataWrapper, idsToFilter);
  	    }
  	    catch (Exception ex)
  	    {
  	      throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
  	    }
  	    return referenceDataWrapper;
	}
  	
  	@Override
	public ReferenceDataWrapper getReferenceDataByExcluding(ReferenceDataWrapper referenceDataWrapper, Long... idsToExclude) throws FrameworkCheckedException
	{
  		try
  	    {
  	      this.referenceDataManager.getReferenceDataByExcluding(referenceDataWrapper, idsToExclude);
  	    }
  	    catch (Exception ex)
  	    {
  	      throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
  	    }
  	    return referenceDataWrapper;
	}
  //======================================================================
  // Other methods
  //======================================================================


  public void setReferenceDataManager(ReferenceDataManager
                                      referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  public void setPopupManager(PopupManager popupManager)
  {
    this.popupManager = popupManager;
  }

  public void setAreaManager(AreaManager areaManager)
  {
    this.areaManager = areaManager;
  }

  public PopupWrapper getPopupData(PopupWrapper popupWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.popupManager.getPopupData(popupWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.FIND_ACTION);
    }
    return popupWrapper;

  }

  public BasePersistableModel getBasePersistableModelByPrimaryKey(Class
      basePersistableModelClass,
      Serializable primaryKey) throws FrameworkCheckedException
  {
    BasePersistableModel basePersistableModel = null;
    try
    {
      basePersistableModel = this.popupManager.
          getBasePersistableModelByPrimaryKey(
              basePersistableModelClass, primaryKey);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.FIND_ACTION);
    }
    return basePersistableModel;

  }

}

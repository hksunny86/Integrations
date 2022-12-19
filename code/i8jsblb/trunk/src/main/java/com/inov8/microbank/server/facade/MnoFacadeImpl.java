package com.inov8.microbank.server.facade;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.SupplierModel;
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
 *
 * @version 1.0
 */
public class MnoFacadeImpl implements MnoFacade

{
  private MnoUserManager mnoUserManager;
   private FrameworkExceptionTranslator frameworkExceptionTranslator;
   private MnoManager mnoManager;
   private MnoDialingCodeManager mnoDialingCodeManager;





   public void setMnoDialingCodeManager(MnoDialingCodeManager mnoDialingCodeManager) {
	this.mnoDialingCodeManager = mnoDialingCodeManager;
}

public BaseWrapper activateDeactivateMnoUser(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	try
    {
      baseWrapper = this.mnoUserManager.activateDeactivateMnoUser(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
	return baseWrapper;
}

/**======================================================================
       Methods for MnoManager
      ====================================================================== */
   
   public SearchBaseWrapper loadMno(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.mnoManager.loadMno(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper loadMno(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.mnoManager.loadMno(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;

  }

  public SearchBaseWrapper searchMno(SearchBaseWrapper
                                          searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.mnoManager.searchMno(searchBaseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }
  
  public SearchBaseWrapper findMnoDialingCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
  {
	  try
	  {	
		  this.mnoDialingCodeManager.findMnoDialingCode(searchBaseWrapper);
	  }
	  catch (Exception ex)
	  {
		  throw this.frameworkExceptionTranslator.translate(ex,
				  this.frameworkExceptionTranslator.FIND_ACTION);
	  }
	  return searchBaseWrapper;
  }
    
  
  public BaseWrapper createOrUpdateMno(BaseWrapper baseWrapper) throws
            FrameworkCheckedException
    {
        try
        {
            this.mnoManager.createOrUpdateMno(baseWrapper);
        }
        catch (Exception ex)
        {
            throw this.frameworkExceptionTranslator.translate(ex,
                    this.frameworkExceptionTranslator.INSERT_ACTION);
        }
        return baseWrapper;

    }










  /**======================================================================
    Methods for MnoUserManager
   ====================================================================== */


   public SearchBaseWrapper loadMnoUser(SearchBaseWrapper
                                                  searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.mnoUserManager.loadMnoUser(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return searchBaseWrapper;

  }

  public BaseWrapper loadMnoUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.mnoUserManager.loadMnoUser(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return baseWrapper;

  }

  public SearchBaseWrapper searchMnoUser(SearchBaseWrapper
      searchBaseWrapper) throws FrameworkCheckedException
  {
    try
    {
      this.mnoUserManager.searchMnoUser(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper createMnoUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.mnoUserManager.createMnoUser(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper updateMnoUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.mnoUserManager.updateMnoUser(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper createAppUserForMno(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    {
      try
      {
        this.mnoUserManager.createAppUserForMno(
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

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  public void setMnoUserManager(MnoUserManager mnoUserManager)
  {
    this.mnoUserManager = mnoUserManager;
  }

  public void setMnoManager(MnoManager mnoManager)
  {
    this.mnoManager = mnoManager;
  }

public BaseWrapper createOrUpdateMnoDialingCode(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	 try
	    {
	      this.mnoDialingCodeManager.createOrUpdateMnoDialingCode(
	          baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.INSERT_ACTION);
	    }
	    return baseWrapper;
}

public SearchBaseWrapper loadMnoDialingCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	try
    {
      return this.mnoDialingCodeManager.loadMnoDialingCode(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }
}

public SearchBaseWrapper searchMnoDialingCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	try
	{
	  this.mnoDialingCodeManager.searchMnoDialingCode(searchBaseWrapper);
	}
	catch (Exception ex)
	{
	  throw this.frameworkExceptionTranslator.translate(ex,
	      this.frameworkExceptionTranslator.FIND_ACTION);
	}
	return searchBaseWrapper;
}



public List<SupplierModel> searchSupplierModels() throws FrameworkCheckedException {
	 List<SupplierModel> supplierModelList = null;
	try
   {
		supplierModelList  =this.mnoManager.searchSupplierModels();
   }
   catch (Exception ex)
   {
     throw this.frameworkExceptionTranslator.translate(ex,
         this.frameworkExceptionTranslator.FIND_ACTION);
   }
   
   return supplierModelList;
}

  public SearchBaseWrapper searchMnoByMnoUser(SearchBaseWrapper
                                             searchBaseWrapper) throws
          FrameworkCheckedException
  {
    try
    {
      this.mnoManager.searchMnoByMnoUser(searchBaseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
              this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

public Long getAppUserPartnerGroupId(Long appUserId) throws FrameworkCheckedException {
	
	Long getAppUserPartnerGroupId;
	try
	    {
	       getAppUserPartnerGroupId = this.mnoUserManager.getAppUserPartnerGroupId(appUserId);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.FIND_ACTION);
	    }
	    return getAppUserPartnerGroupId;

}


  /**=======================================================================
  // Other Methods
  ======================================================================= */






}

package com.inov8.microbank.server.facade;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.wrapper.suppliermodule.SupplierWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierUserManager;

/**
 * @author Jawwad Farooq
 * @version 1.0
 */

public class SupplierFacadeImpl
    implements SupplierFacade
{

  private SupplierManager supplierManager;
  private SupplierUserManager supplierUserManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;
  private SupplierBankInfoManager supplierBankInfoManager;

  public SupplierFacadeImpl()
  {
  }

  //======================================================================
  // Methods for SupplierManager
  //======================================================================

  public SearchBaseWrapper loadSupplier(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierManager.loadSupplier(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper loadSupplier(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierManager.loadSupplier(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;

  }

  public SearchBaseWrapper searchSupplier(SearchBaseWrapper
                                          searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierManager.searchSupplier(searchBaseWrapper);
    }

    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper createOrUpdateSupplier(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierManager.createOrUpdateSupplier(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;

  }

  //======================================================================
  //  SupplierManager Methods End here
  //======================================================================

  //======================================================================
  // Methods for SupplierUserManager
  //======================================================================

  public SearchBaseWrapper loadSupplierUser(SearchBaseWrapper
                                            searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierUserManager.loadSupplierUser(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return searchBaseWrapper;

  }

  public BaseWrapper loadSupplierUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierUserManager.loadSupplierUser(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return baseWrapper;

  }

  public SearchBaseWrapper searchSupplierUser(SearchBaseWrapper
                                              searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierUserManager.searchSupplierUser(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper createSupplierUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierUserManager.createSupplierUser(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper updateSupplierUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierUserManager.updateSupplierUser(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper loadSupplierBankInfo(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.supplierBankInfoManager.loadSupplierBankInfo(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }
  }

  public SearchBaseWrapper loadSupplierBankInfo(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException
{
try
{
  return this.supplierBankInfoManager.loadSupplierBankInfo(searchBaseWrapper);
}
catch (Exception ex)
{
  throw this.frameworkExceptionTranslator.translate(ex,
      this.frameworkExceptionTranslator.
      FIND_BY_PRIMARY_KEY_ACTION);

}
}

  public BaseWrapper createAppUserForSupplier(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    {
      try
      {
        this.supplierUserManager.createAppUserForSupplier(
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

  //======================================================================
  // Methods for SupplierUserManager Ends here
  //======================================================================



  public SupplierWrapper getSupplierClassPath(SupplierWrapper supplierInfo) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierManager.getSupplierClassPath(supplierInfo);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return supplierInfo;
  }

  public SupplierWrapper verify(SupplierWrapper supplierInfo,
                                WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierManager.verify(supplierInfo, workFlowWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return supplierInfo;
  }

  public SupplierWrapper updateSupplier(SupplierWrapper supplierInfo) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierManager.updateSupplier(supplierInfo);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return supplierInfo;
  }

  public SupplierWrapper rollback(SupplierWrapper supplierInfo) throws
      FrameworkCheckedException
  {
    try
    {
      this.supplierManager.rollback(supplierInfo);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }
    return supplierInfo;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  public void setSupplierManager(SupplierManager supplierModuleManager)
  {
    this.supplierManager = supplierModuleManager;
  }

  public void setSupplierUserManager(SupplierUserManager supplierUserManager)
  {
    this.supplierUserManager = supplierUserManager;
  }

  public void setSupplierBankInfoManager(SupplierBankInfoManager
                                         supplierBankInfoManager)
  {
    this.supplierBankInfoManager = supplierBankInfoManager;
  }

public List getServicesAgainstSupplier(Long supplierId) throws FrameworkCheckedException {
 
	try
    {
     return  this.supplierManager.getServicesAgainstSupplier(supplierId);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    
	
}

public BaseWrapper updateSupplierBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	 try
	    {
	      this.supplierBankInfoManager.updateSupplierBankInfo(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.UPDATE_ACTION);
	    }
	    return baseWrapper;
}

public BaseWrapper createSupplierBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	 try
	    {
	      this.supplierBankInfoManager.createSupplierBankInfo(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
	    }
	    return baseWrapper;
}
public SearchBaseWrapper searchSupplierBankInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	 try
	    {
	      this.supplierBankInfoManager.searchSupplierBankInfo(searchBaseWrapper);
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
	       getAppUserPartnerGroupId = this.supplierUserManager.getAppUserPartnerGroupId(appUserId);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.FIND_ACTION);
	    }
	    return getAppUserPartnerGroupId;


	
	}

	@Override
	public SupplierBankInfoModel getSupplierBankInfoModel(SupplierBankInfoModel example) throws FrameworkCheckedException {
		
		return supplierBankInfoManager.getSupplierBankInfoModel(example);
	}

}

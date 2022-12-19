package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.bankmodule.BankManager;
import com.inov8.microbank.server.service.bankmodule.BankUserManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 *
 * @version 1.0
 */



public class BankFacadeImpl
    implements BankFacade
{
  private BankManager bankManager;
   private BankUserManager bankUserManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;

  public BankFacadeImpl()
  {
  }

  //======================================================================
  // Methods for BankManager
  //======================================================================

  public SearchBaseWrapper loadBank(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.bankManager.loadBank(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;
  }

  public BaseWrapper loadBank(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.bankManager.loadBank(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;
  }

  public SearchBaseWrapper searchBank(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.bankManager.searchBank(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;
  }

  public SearchBaseWrapper searchBankByExample(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.bankManager.searchBankByExample(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

  
  public BaseWrapper updateBank(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.bankManager.updateBank(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
  }

  public BaseWrapper createBank(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.bankManager.createBank(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
  }

  //======================================================================
   // Methods for BankUserManager
   //======================================================================
   public SearchBaseWrapper loadBankUser(SearchBaseWrapper
                                                  searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.bankUserManager.loadBankUser(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return searchBaseWrapper;

  }

  public BaseWrapper loadBankUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.bankUserManager.loadBankUser(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return baseWrapper;

  }

  public SearchBaseWrapper searchBankUser(SearchBaseWrapper
      searchBaseWrapper) throws FrameworkCheckedException
  {
    try
    {
      this.bankUserManager.searchBankUser(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper createBankUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.bankUserManager.createBankUser(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper updateBankUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.bankUserManager.updateBankUser(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public BaseWrapper createAppUserForBank(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    {
      try
      {
        this.bankUserManager.createAppUserForBank(
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


  //=======================================================================
  // Other Methods
  //=======================================================================

  public void setBankManager(BankManager bankManager)
  {
    this.bankManager = bankManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  public void setBankUserManager(BankUserManager bankUserManager)
  {
    this.bankUserManager = bankUserManager;
  }

public Long getAppUserPartnerGroupId(Long appUserId) throws FrameworkCheckedException {
	 
	Long getAppUserPartnerGroupId;
	try
	    {
	       getAppUserPartnerGroupId = this.bankUserManager.getAppUserPartnerGroupId(appUserId);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.FIND_ACTION);
	    }
	    return getAppUserPartnerGroupId;

}
}

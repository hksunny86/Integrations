package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;

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

public class SmartMoneyFacadeImpl
    implements SmartMoneyFacade
{
	
  private SmartMoneyAccountManager smartMoneyAccountManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;

  public SmartMoneyFacadeImpl()
  {
  }

  //======================================================================
  // Methods for SmartMoneyAccountManager
  //======================================================================

  
  
  public SearchBaseWrapper loadSmartMoneyAccount(SearchBaseWrapper
                                                 searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.smartMoneyAccountManager.loadSmartMoneyAccount(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;
  }

  	@Override
	public SmartMoneyAccountModel getSMALinkedWithCoreAccount(Long retailerContactId) throws FrameworkCheckedException {
  		try
  	    {
  	      return this.smartMoneyAccountManager.getSMALinkedWithCoreAccount(retailerContactId);
  	    }
  	    catch (Exception ex)
  	    {
  	      throw this.frameworkExceptionTranslator.translate(ex,this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
  	    }
	}

public BaseWrapper loadSmartMoneyAccount(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return baseWrapper;
  }
  
  public BaseWrapper loadOLASmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
	  try
	    {
	      this.smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return baseWrapper;	  
  }




  public SearchBaseWrapper searchSmartMoneyAccount(SearchBaseWrapper
      searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.smartMoneyAccountManager.searchSmartMoneyAccount(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;
  }

  public BaseWrapper updateSmartMoneyAccount(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.smartMoneyAccountManager.updateSmartMoneyAccount(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
  }

  public BaseWrapper createSmartMoneyAccount(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.smartMoneyAccountManager.createSmartMoneyAccount(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
  }
  
  public BaseWrapper searchSmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
  	// TODO Auto-generated method stub
	  try
	    {
	      this.smartMoneyAccountManager.searchSmartMoneyAccount(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	    return baseWrapper;
  }
  

  //=======================================================================
  // Other Methods
  //=======================================================================

  public void setSmartMoneyAccountManager(SmartMoneyAccountManager
                                          smartMoneyAccountManager)
  {
    this.smartMoneyAccountManager = smartMoneyAccountManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

public BaseWrapper loadOLASMAForRetOrDistHead(BaseWrapper baseWrapper) throws FrameworkCheckedException
{
	try
    {
      this.smartMoneyAccountManager.loadOLASMAForRetOrDistHead(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;
}

public SmartMoneyAccountModel getSMAccountByRetailerContactId(Long retailerContactId) throws FrameworkCheckedException{
	try{
      return this.smartMoneyAccountManager.getSMAccountByRetailerContactId(retailerContactId);
    }catch (Exception ex){
      throw this.frameworkExceptionTranslator.translate(ex,this.frameworkExceptionTranslator.FIND_ACTION);
    }
}

public AppUserModel getAppUserModel(AppUserModel appUserModel) {
	
	return smartMoneyAccountManager.getAppUserModel(appUserModel);
}

/* (non-Javadoc)
 * @see com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager#getSMAccountByRetailer(com.inov8.microbank.common.model.RetailerContactModel)
 */
public SmartMoneyAccountModel getSMAccountByRetailer(RetailerContactModel retailerContactModel)	throws FrameworkCheckedException {
	
	return smartMoneyAccountManager.getSMAccountByRetailer(retailerContactModel);
}

public int countSmartMoneyAccountModel(BaseWrapper baseWrapper)
		throws FrameworkCheckedException {
	int count = 0;
	 try
	    {
	      count = this.smartMoneyAccountManager.countSmartMoneyAccountModel(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	return count;
}

	public CustomList<SmartMoneyAccountModel> loadCustomerSmartMoneyAccountByHQL( SmartMoneyAccountModel smartMoneyAccountModel ) throws FrameworkCheckedException{
		try{
			return this.smartMoneyAccountManager.loadCustomerSmartMoneyAccountByHQL(smartMoneyAccountModel);
		}catch (Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex,this.frameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public SmartMoneyAccountModel getSMAccountByHandlerId(Long handlerId)throws FrameworkCheckedException {
		try{
			return this.smartMoneyAccountManager.getSMAccountByHandlerId(handlerId);
		}catch (Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex,this.frameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void blockSmartMoneyAccount(AppUserModel appUserModel)
			throws Exception {
		// TODO Auto-generated method stub
		smartMoneyAccountManager.blockSmartMoneyAccount(appUserModel);
	}

	@Override
	public SmartMoneyAccountModel loadSmartMoneyAccountModel(
			AppUserModel appUserModel) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel);
	}

	@Override
	public SmartMoneyAccountModel loadSmartMoneyAccountModel(
			AppUserModel appUserModel, Long paymentModeId)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel, paymentModeId);
	}

  @Override
  public BaseWrapper updateSmartMoneyAccountDormancyWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException {
    return smartMoneyAccountManager.updateSmartMoneyAccountDormancyWithAuthorization(baseWrapper);
  }

  @Override
  public SmartMoneyAccountModel getSmartMoneyAccountByCustomerIdAndPaymentModeId(SmartMoneyAccountModel smartMoneyAccountModel) throws FrameworkCheckedException {
    smartMoneyAccountModel = smartMoneyAccountManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(smartMoneyAccountModel);
    return smartMoneyAccountModel;
  }

  @Override

  public SmartMoneyAccountModel getInActiveSMA(AppUserModel appUserModel, Long paymentModeId,Long statusId) {
    return smartMoneyAccountManager.getInActiveSMA(appUserModel,paymentModeId,statusId);
  }

    @Override
    public BaseWrapper updateDebitBlockWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try
        {
            return smartMoneyAccountManager.updateDebitBlockWithAuthorization(baseWrapper);
        } catch (FrameworkCheckedException e) {
            throw this.frameworkExceptionTranslator.translate(e,FrameworkExceptionTranslator.UPDATE_ACTION);
        }
    }

    @Override
    public BaseWrapper updateDebitBlock(Long appUserId, Double amount, Boolean isDebitBlocked) throws FrameworkCheckedException {
        try
        {
            return smartMoneyAccountManager.updateDebitBlock(appUserId,amount,isDebitBlocked);
        } catch (FrameworkCheckedException e) {
            throw this.frameworkExceptionTranslator.translate(e,FrameworkExceptionTranslator.UPDATE_ACTION);
        }
    }

    @Override
    public void validateDebitBlock(SmartMoneyAccountModel smaModel, Double txAmount, Double balance) throws FrameworkCheckedException {
        try{
            smartMoneyAccountManager.validateDebitBlock(smaModel, txAmount, balance);
        }

        catch (FrameworkCheckedException e) {
            frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

}

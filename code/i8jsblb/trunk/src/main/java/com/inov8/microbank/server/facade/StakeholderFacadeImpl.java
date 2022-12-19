package com.inov8.microbank.server.facade;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;

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
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class StakeholderFacadeImpl
    implements StakeholderFacade
{
  private StakeholderBankInfoManager stakeholderBankInfoManager;
  
  private FrameworkExceptionTranslator frameworkExceptionTranslator;

  public StakeholderFacadeImpl()
  {
  }

  //======================================================================
  // Methods for StakeholderBankInfoManager
  //======================================================================


  public BaseWrapper createOrUpdateStakeholderBankInfo(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.stakeholderBankInfoManager.createOrUpdateStakeholderBankInfo(
          baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
    return baseWrapper;

  }

  public SearchBaseWrapper searchStakeHolderBankInfo(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException
{
try
{
  this.stakeholderBankInfoManager.searchStakeHolderBankInfo(searchBaseWrapper);
}
catch (Exception ex)
{
  throw this.frameworkExceptionTranslator.translate(ex,
      this.frameworkExceptionTranslator.FIND_ACTION);
}
return searchBaseWrapper;
}
  //=======================================================================
  // Other Methods
  //=======================================================================

  public void setStakeholderBankInfoManager(StakeholderBankInfoManager
                                            stakeholderBankInfoManager)
  {
    this.stakeholderBankInfoManager = stakeholderBankInfoManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

public SearchBaseWrapper loadStakeHolderBankInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	 try
	    {
	      return this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          FIND_BY_PRIMARY_KEY_ACTION);

	    }
}
		@Override
		public List<StakeholderBankInfoModel> getStakeholderBankInfoForProduct(
				Long productId) throws FrameworkCheckedException 
				{
			try
		    {
		      return this.stakeholderBankInfoManager.getStakeholderBankInfoForProduct(productId);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex,
		          this.frameworkExceptionTranslator.FIND_ACTION);
		    }
		}

	@Override
	public List<StakeholderBankInfoModel> getStakeholderBankInfoForProductandAccountType(Long productId, String accountType) throws FrameworkCheckedException {

			try
			{
				return this.stakeholderBankInfoManager.getStakeholderBankInfoForProductandAccountType(productId,accountType);
			}
			catch (Exception ex)
			{
				throw this.frameworkExceptionTranslator.translate(ex,
						this.frameworkExceptionTranslator.FIND_ACTION);
			}
	}


	@Override
		public StakeholderBankInfoModel loadBLBStakeholderBankInfoModel(String accountNo) throws FrameworkCheckedException {
			
			try
		    {
				return this.stakeholderBankInfoManager.loadBLBStakeholderBankInfoModel(accountNo);
		    }
			catch (Exception ex)
			{
			      throw this.frameworkExceptionTranslator.translate(ex,
		          this.frameworkExceptionTranslator.FIND_ACTION);
		    }
		}

	@Override
	public StakeholderBankInfoModel loadStakeholderBankInfoModel(StakeholderBankInfoModel example) throws FrameworkCheckedException {
	
		return stakeholderBankInfoManager.loadStakeholderBankInfoModel(example);
	}

	@Override
	public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(AppUserModel appUserModel) throws FrameworkCheckedException {
		
		return stakeholderBankInfoManager.loadDistributorStakeholderBankInfoModel(appUserModel);
	}
	
	public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(DistributorModel distributorModel) throws FrameworkCheckedException {
		
		return stakeholderBankInfoManager.loadDistributorStakeholderBankInfoModel(distributorModel);
	}

	@Override
	public List<StakeholderBankInfoModel> getStakeholderBankInfoModelList() throws FrameworkCheckedException {

		return stakeholderBankInfoManager.getStakeholderBankInfoModelList();
	}

	@Override
	public boolean isAccountTypeUnique(String accountNumber,
			Long stakeHolderBankInfoId) {
		return stakeholderBankInfoManager.isAccountTypeUnique(accountNumber, stakeHolderBankInfoId);
	}
	
	@Override
	public List<StakeholderBankInfoModel> getStakeholderBankInfoByCommShId(Long commissionStakeholderId) throws FrameworkCheckedException {
	
		return stakeholderBankInfoManager.getStakeholderBankInfoByCommShId(commissionStakeholderId);
	}

	@Override
	public List<StakeholderBankInfoModel> getStakeholderBankInfoForInclCharges(Long productId) throws FrameworkCheckedException {
	
		return stakeholderBankInfoManager.getStakeholderBankInfoForInclCharges(productId);
	}

	@Override
	public StakeholderBankInfoModel getStakeholderAccountBankInfoModel(Long accTypeId) throws FrameworkCheckedException {
		return stakeholderBankInfoManager.getStakeholderAccountBankInfoModel(accTypeId);
	}
	@Override
	public BaseWrapper createStakeHolderAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		return stakeholderBankInfoManager.createStakeHolderAccount(baseWrapper);
	}

	@Override
	public StakeholderBankInfoModel loadStakeholderBankInfoModel(Long primaryKey) throws FrameworkCheckedException {
		return stakeholderBankInfoManager.loadStakeholderBankInfoModel(primaryKey);
	}
	
	@Override
	public List<Object[]> loadOfSettlementAccounts(Long accountType) throws FrameworkCheckedException {
		return stakeholderBankInfoManager.loadOfSettlementAccounts(accountType);
	}

	@Override
	public SearchBaseWrapper getStakehldAcctMappingList(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return stakeholderBankInfoManager.getStakehldAcctMappingList(searchBaseWrapper);
	}

	
}

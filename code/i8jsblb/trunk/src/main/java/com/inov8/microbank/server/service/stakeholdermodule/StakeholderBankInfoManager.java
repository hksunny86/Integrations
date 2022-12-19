package com.inov8.microbank.server.service.stakeholdermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;

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

public interface StakeholderBankInfoManager
{
  public BaseWrapper createOrUpdateStakeholderBankInfo(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchStakeHolderBankInfo(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException;
  public SearchBaseWrapper loadStakeHolderBankInfo(SearchBaseWrapper searchBaseWrapper) throws 
  FrameworkCheckedException; 
  public StakeholderBankInfoModel loadStakeholderBankInfoModel(StakeholderBankInfoModel example) throws FrameworkCheckedException;
  public StakeholderBankInfoModel loadStakeholderBankInfoModel(Long primaryKey) throws FrameworkCheckedException;
  public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(AppUserModel appUserModel) throws FrameworkCheckedException;
  public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(DistributorModel distributorModel) throws FrameworkCheckedException;
  public List<StakeholderBankInfoModel> getStakeholderBankInfoModelList() throws FrameworkCheckedException;
  public StakeholderBankInfoModel getStakeholderAccountBankInfoModel(Long accTypeId) throws FrameworkCheckedException;
  public boolean isAccountTypeUnique(String accountNumber, Long accountTypeId);
  public BaseWrapper createStakeHolderAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;
  public List<StakeholderBankInfoModel> getStakeholderBankInfoByCommShId(Long commissionStakeholderId) throws FrameworkCheckedException;
  public List<StakeholderBankInfoModel> getStakeholderBankInfoForInclCharges(Long productId) throws FrameworkCheckedException;
  List<StakeholderBankInfoModel> getStakeholderBankInfoForProduct(Long productId) throws FrameworkCheckedException;;
  List<StakeholderBankInfoModel> getStakeholderBankInfoForProductandAccountType(Long productId, String accountType) throws FrameworkCheckedException;
  /**
   * @author AtifHu
   * @return
   * @throws FrameworkCheckedException
   */
  List<Object[]> loadOfSettlementAccounts(Long accountType) throws FrameworkCheckedException;
  public SearchBaseWrapper getStakehldAcctMappingList(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
  
  StakeholderBankInfoModel loadBLBStakeholderBankInfoModel(String accountNo) throws FrameworkCheckedException;
  
}
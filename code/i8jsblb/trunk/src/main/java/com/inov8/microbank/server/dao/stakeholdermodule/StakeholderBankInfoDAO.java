package com.inov8.microbank.server.dao.stakeholdermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
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

public interface StakeholderBankInfoDAO extends BaseDAO<StakeholderBankInfoModel, Long> {

	  public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(AppUserModel appUserModel) throws FrameworkCheckedException;
	  public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(DistributorModel distributorModel) throws FrameworkCheckedException;
	  public List<StakeholderBankInfoModel> getStakeholderBankInfoModelList() throws FrameworkCheckedException;	
	  public boolean isAccountTypeUnique(String accountNumber, Long accountTypeId);
	  public StakeholderBankInfoModel getStakeholderAccountBankInfoModel(Long accTypeId) throws FrameworkCheckedException;
	  /**
	   * @author AtifHu
	   * @return
	   * @throws FrameworkCheckedException
	   */
	  List<Object[]> loadOfSettlementAccounts(Long accountType) throws FrameworkCheckedException;
}

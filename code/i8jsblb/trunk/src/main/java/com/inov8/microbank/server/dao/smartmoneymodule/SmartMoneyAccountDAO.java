package com.inov8.microbank.server.dao.smartmoneymodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;

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
 * @version 1.0
 */



public interface SmartMoneyAccountDAO
    extends BaseDAO<SmartMoneyAccountModel, Long>
{
	public List<SmartMoneyAccountModel> loadOLASmartMoneyAccount( Long customerId );
	public List<SmartMoneyAccountModel> loadOLASmartMoneyAccount( Long retailerContactId, Long distributorContactId );
	public List<SmartMoneyAccountModel> loadOLASmartMoneyAccount( Long retailerContactId, Long distributorContactId , Long handlerId);
	public List<SmartMoneyAccountModel> loadOLASMAForRetOrDistHead( Long retailerId, Long distributorId );
	public SmartMoneyAccountModel getSmartMoneyAccountByWalkinCustomerId(SmartMoneyAccountModel smartMoneyAccountModel);
	public CustomList<SmartMoneyAccountModel> loadCustomerSmartMoneyAccountByHQL( SmartMoneyAccountModel smartMoneyAccountModel );

	int updateSmartMoneyAccountModelToCloseAccount(SmartMoneyAccountModel smartMoneyAccountModel,Boolean isClosedSetteled);
	int updateSmartMoneyAccountModelToCloseAgentAccount(SmartMoneyAccountModel smartMoneyAccountModel,Boolean isClosedSetteled);

	int updateSmartMoneyAccountModelToLockUnlockAccount(SmartMoneyAccountModel smartMoneyAccountModel);
	void updateSmartMoneyAccountCardTypeId(SmartMoneyAccountModel model) throws FrameworkCheckedException;

	List<SmartMoneyAccountModel> getLastClosedSMAAccount(SmartMoneyAccountModel sma);
}

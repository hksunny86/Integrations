package com.inov8.microbank.server.dao.transactionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.MiniTransactionModel;

import java.util.List;

/**
 * 
 * @author Jawwad Farooq
 * July 19, 2007
 * 
 */


public interface MiniTransactionDAO extends BaseDAO<MiniTransactionModel, Long>
{
	public MiniTransactionModel LoadMiniTransactionModel(String transactionCode) throws FrameworkCheckedException;

	public MiniTransactionModel loadAccountAndLock(MiniTransactionModel miniTransactionModel);

	BaseWrapper loadAndLockMinitransaction(BaseWrapper baseWrapper);

	public List<MiniTransactionModel> LoadMiniTransactionModelByPK(Long miniTransactionId)throws FrameworkCheckedException;
	
	public List<MiniTransactionModel> searchMiniTransactionModel(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException;

	public abstract boolean updateMiniTransactionStatus(Long transactionCodeId, Long stateId);
	List<MiniTransactionModel> findOrphanTransactions(Long status, Long... commandId);

	boolean updateMiniTransactionModels(MiniTransactionModel miniTransactionModel, Long stateId);
	boolean updateMiniTransactionModelsFonepay(MiniTransactionModel miniTransactionModel, Long stateId);

	public void updateMiniTransactionModel(MiniTransactionModel miniTransactionModel);

	MiniTransactionModel loadMiniTransactionModel(MiniTransactionModel miniTransactionModel);
	MiniTransactionModel loadMiniTransactionByTransactionCode(String transactionCode);

}

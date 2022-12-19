package com.inov8.microbank.server.service.commissionstakeholdermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CommissionShSharesDefaultModel;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionShSharesViewModel;
import com.inov8.microbank.common.vo.product.CommissionShSharesDefaultVO;
import com.inov8.microbank.common.vo.product.CommissionShSharesVO;
import com.inov8.microbank.common.vo.product.CommissionStakeholderVO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */


public interface CommissionStakeholderManager
{
	SearchBaseWrapper loadCommissionStakeholder(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;

	SearchBaseWrapper searchCommissionStakeholder(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;

	BaseWrapper updateCommissionStakeholder(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

	BaseWrapper createCommissionStakeholder(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;
	
	BaseWrapper createCommissionStakeholderAccounts(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;
	
	public SearchBaseWrapper searchCommissionStakeholderAccounts(SearchBaseWrapper searchBaseWrapper)throws
    FrameworkCheckedException;

	List<CommissionShSharesViewModel> loadCommissionStakeholderSharesViewList(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;

	boolean saveCommissionShShares(List<CommissionShSharesModel> commissionShSharesList) throws FrameworkCheckedException;
	List<CommissionShSharesModel> loadCommissionShSharesList(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	boolean removeCommissionShSharesByStakeholderIds(List<Long> removeSharesList) throws FrameworkCheckedException;
	boolean removeCommissionShSharesByShShareIds(List<Long> removeSharesList) throws FrameworkCheckedException;
	List<CommissionStakeholderModel> loadCommissionStakeholdersList(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	List<CommissionShSharesModel>  loadCommissionShSharesList(CommissionShSharesModel vo) throws FrameworkCheckedException;
	public List<CommissionShSharesDefaultModel> loadDefaultCommissionShSharesList(Long productId) throws FrameworkCheckedException;
	public BaseWrapper saveCommissionStakeholderAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchCommissionStakeholderAccountsByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}

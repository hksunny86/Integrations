package com.inov8.microbank.server.dao.commissionstakeholdermodule.hibernate;

import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.commissionmodule.CommShAcctsListViewModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.commissionstakeholdermodule.CommissionStakeholderAccountsListViewDAO;

public class CommissionStakeholderAccountsListViewHibernateDAO 
	extends BaseHibernateDAO<CommShAcctsListViewModel, Long,CommissionStakeholderAccountsListViewDAO>
	implements CommissionStakeholderAccountsListViewDAO
{

	@Override
	public CustomList<CommShAcctsListViewModel> searchCommissionStakeholderAccountsByCriteria(
			CommShAcctsListViewModel commShAcctsListViewModel,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws FrameworkCheckedException {
		
		Disjunction disjunction = Restrictions.disjunction();
		if((null!=commShAcctsListViewModel.getAccountNo()) && (null==commShAcctsListViewModel.getAccountTitle())){
			
			Criterion criteria = null;			
			criteria = Restrictions.eq("accountNo", commShAcctsListViewModel.getAccountNo());
			disjunction.add(criteria);
			criteria = Restrictions.eq("relationT24StakeholderBankInfoModel.accountNo", commShAcctsListViewModel.getAccountNo());
			disjunction.add(criteria);
			criteria = Restrictions.eq("relationOfSettlementStakeholderBankInfoModel.accountNo", commShAcctsListViewModel.getAccountNo());
			disjunction.add(criteria);
		}
		if((null==commShAcctsListViewModel.getAccountNo()) && (null!=commShAcctsListViewModel.getAccountTitle())){
			
			Criterion criteria = null;			
			criteria = Restrictions.eq("accountTitle", commShAcctsListViewModel.getAccountTitle());
			disjunction.add(criteria);
			criteria = Restrictions.eq("relationT24StakeholderBankInfoModel.name", commShAcctsListViewModel.getAccountTitle());
			disjunction.add(criteria);
			criteria = Restrictions.eq("relationOfSettlementStakeholderBankInfoModel.name", commShAcctsListViewModel.getAccountTitle());
			disjunction.add(criteria);
		}
		return findByCriteria(disjunction,commShAcctsListViewModel, pagingHelperModel, sortingOrderMap);
	}
}

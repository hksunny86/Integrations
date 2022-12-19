package com.inov8.microbank.server.dao.portal.transactiondetail.hibernate;

import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;
import com.inov8.microbank.server.dao.portal.transactiondetail.TransactionAllViewDao;

/**
 * @author Naseer
 */
public class TransactionAllViewDaoImpl extends BaseHibernateDAO<TransactionAllViewModel, Long, TransactionAllViewDao> implements TransactionAllViewDao
{

    public List<?> findWalkInCustomerTransactions( String cnic,TransactionAllViewModel transactionAllViewModel, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap,DateRangeHolderModel dateRangeHolderModel )
    {
        List<?> results = null;

        Criterion criterion = Restrictions.or( Restrictions.eq( "senderCnic", cnic ), Restrictions.eq( "recipientCnic", cnic ) );
        CustomList<TransactionAllViewModel> customList = super.findByCriteria( criterion,transactionAllViewModel, pagingHelperModel, sortingOrderMap,dateRangeHolderModel );
        results = customList.getResultsetList();

        return results;
    }

}

package com.inov8.microbank.debitcard.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.debitcard.dao.DebitCardRequestsViewModelDAO;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class DebitCardRequestsViewModelHibernateDAO extends BaseHibernateDAO<DebitCardRequestsViewModel,Long, DebitCardRequestsViewModelDAO>
        implements DebitCardRequestsViewModelDAO{
    @Override
    public DebitCardRequestsViewModel loadDebitCardRequestsByAppUserId(Long appUserId, String mobileNo) {
        DebitCardRequestsViewModel debitCardRequestsViewModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM DebitCardRequestsViewModel user ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" user.appUserId = :appUserId ");
        queryBuilder.append(" AND ");
        queryBuilder.append(" user.mobileNo = :mobileNo ");
        String[] paramNames = { "appUserId", "mobileNo"};
        Object[] values = { appUserId, mobileNo};
        try {
            List<DebitCardRequestsViewModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if(CollectionUtils.isNotEmpty(userList)){
                debitCardRequestsViewModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return debitCardRequestsViewModel;
    }

    @Override
    public DebitCardRequestsViewModel loadDebitCardRequestsByDebitCardId(Long debitCardId) {
        DebitCardRequestsViewModel debitCardRequestsViewModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM DebitCardRequestsViewModel user ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" user.debitCardId = :debitCardId ");
        String[] paramNames = { "debitCardId"};
        Object[] values = { debitCardId};
        try {
            List<DebitCardRequestsViewModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if(CollectionUtils.isNotEmpty(userList)){
                debitCardRequestsViewModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return debitCardRequestsViewModel;
    }
}

package com.inov8.microbank.server.dao.safrepo.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SafRepoCashOutModel;
import com.inov8.microbank.common.model.SafRepoCoreModel;
import com.inov8.microbank.server.dao.safrepo.SafRepoCashOutDao;

/**
 * Created by Attique on 9/6/2018.
 */
public class SafRepoCashOutHibernateDao extends BaseHibernateDAO<SafRepoCashOutModel,Long,SafRepoCashOutDao> implements SafRepoCashOutDao {
    @Override
    public void updateStatus(SafRepoCashOutModel safRepoCoreModel) {
        String updateHql ="update SafRepoCashOutModel set updatedOn=?, status=? where transactionCode=?";
        Object[] values = {safRepoCoreModel.getUpdatedOn(), safRepoCoreModel.getStatus(), safRepoCoreModel.getTransactionCode()};
        getHibernateTemplate().bulkUpdate(updateHql, values);
    }
}

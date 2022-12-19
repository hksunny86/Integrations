package com.inov8.microbank.server.dao.schedulemodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.schedulemodule.ScheduleFundsTransferModel;
import com.inov8.microbank.server.dao.schedulemodule.ScheduleFundsTransferDao;

import java.util.List;

/**
 * Created by Muhammad Sajid on 3/16/2017.
 */
public class ScheduleFundsTransferHibernateDao extends BaseHibernateDAO<ScheduleFundsTransferModel, Long, ScheduleFundsTransferDao>
        implements ScheduleFundsTransferDao  {

    @Override
    public void updateScheduleFundsTransferList(List<ScheduleFundsTransferModel> sftList) {
        if(logger.isDebugEnabled())
        {
            logger.debug("Start of ScheduleFundsTransferHibernateDao.updateScheduleFundsTransferList()");
        }

        this.saveOrUpdateCollection(sftList);

        if(logger.isDebugEnabled())
        {
            logger.debug("Start of ScheduleFundsTransferHibernateDao.updateScheduleFundsTransferList()");
        }
    }

    @Override
    public List<ScheduleFundsTransferModel> getScheduleFundsTransferByCustomerId(long customerId) {

        String hql = "from ScheduleFundsTransferModel sch where sch.relationCustomerIdCustomerModel.customerId=:customerId and sch.isDeleted=:deleted";

        String[] paramNames = { "customerId","deleted"};
        Object[] values = { customerId,Boolean.FALSE};
        List<ScheduleFundsTransferModel> scheduleFundsTransferModels = this.getHibernateTemplate().findByNamedParam(hql,paramNames,values);
        return scheduleFundsTransferModels;

    }
}

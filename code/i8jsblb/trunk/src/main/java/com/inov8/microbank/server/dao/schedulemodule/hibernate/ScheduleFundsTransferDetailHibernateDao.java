package com.inov8.microbank.server.dao.schedulemodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.schedulemodule.ScheduleFundsTransferDetailModel;
import com.inov8.microbank.server.dao.schedulemodule.ScheduleFundsTransferDetailDao;
import org.apache.commons.collections.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Muhammad Sajid on 3/16/2017.
 */
public class ScheduleFundsTransferDetailHibernateDao extends BaseHibernateDAO<ScheduleFundsTransferDetailModel, Long, ScheduleFundsTransferDetailDao>
        implements ScheduleFundsTransferDetailDao {


    @Override
    public void updateScheduleFundsTransferList(List<ScheduleFundsTransferDetailModel> sftList) {
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
    public List<ScheduleFundsTransferDetailModel> getActiveFundTransferSchedule(Date date) {


        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH,-3);

        String hql = "from ScheduleFundsTransferDetailModel sch where sch.isExpired=0 and sch.status in (:status) and sch.transactionDate between :start and :end ";

        String[] paramNames = { "end","start","status"};
        Object[] values = { date , cal.getTime(),new String[]{"retry","new"}};

        List<ScheduleFundsTransferDetailModel> ScheduleFundsTransferDetailModels = this.getHibernateTemplate().findByNamedParam(hql,paramNames,values);


        if(CollectionUtils.isNotEmpty(ScheduleFundsTransferDetailModels)){
            return ScheduleFundsTransferDetailModels;
        }
        return null;
      /*  StringBuilder queryString = new StringBuilder();
        queryString.append(" SELECT scheduleFundsTransferDetailModel ");
        queryString.append(" FROM ScheduleFundsTransferDetailModel model ");
        queryString.append(" WHERE ");
        queryString.append(" model.isExpired=0 ");
        queryString.append(" AND model.transactionDate < :currentDate");

        String[] paramNames = { "currentDate"};
        Object[] values = { date };

        return getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
*/
    }
}

package com.inov8.microbank.server.dao.schedulemodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.schedulemodule.ScheduleLoanPaymentDetailModel;
import com.inov8.microbank.server.dao.schedulemodule.ScheduleBillPaymentDetailDao;
import org.apache.commons.collections.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Muhammad Sajid on 3/20/2017.
 */
public class ScheduleBillPaymentDetailHibernateDao extends BaseHibernateDAO<ScheduleLoanPaymentDetailModel, Long, ScheduleBillPaymentDetailDao>
        implements ScheduleBillPaymentDetailDao {
    @Override
    public void updateScheduleBillPaymentDetailList(List<ScheduleLoanPaymentDetailModel> sbpList) {
        if(logger.isDebugEnabled())
        {
            logger.debug("Start of ScheduleFundsTransferHibernateDao.updateScheduleBillPaymentList()");
        }

        this.saveOrUpdateCollection(sbpList);

        if(logger.isDebugEnabled())
        {
            logger.debug("Start of ScheduleFundsTransferHibernateDao.updateScheduleBillPaymentList()");
        }
    }

    @Override
    public List<ScheduleLoanPaymentDetailModel> getActiveBillPaymentSchedule(Date date) {

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH,-3);

        String hql = "from ScheduleBillPaymentDetailModel sch where sch.isExpired=0 and sch.status in (:status) and sch.transactionDate between :start and :end ";

        String[] paramNames = { "end","start","status"};
        Object[] values = { date , cal.getTime(),new String[]{"retry","new"}};
        List<ScheduleLoanPaymentDetailModel> scheduleLoanPaymentDetailModels = this.getHibernateTemplate().findByNamedParam(hql,paramNames,values);


        if(CollectionUtils.isNotEmpty(scheduleLoanPaymentDetailModels)){
            return scheduleLoanPaymentDetailModels;
        }
        return null;
    }
}

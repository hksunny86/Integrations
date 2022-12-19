package com.inov8.microbank.server.dao.schedulemodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ThirdPartyAccountOpeningModel;
import com.inov8.microbank.common.model.schedulemodule.ScheduleLoanPaymentModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.schedulemodule.ScheduleBillPaymentDao;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Muhammad Sajid on 3/20/2017.
 */
public class ScheduleBillPaymentHibernateDao extends BaseHibernateDAO<ScheduleLoanPaymentModel, Long, ScheduleBillPaymentDao>
        implements ScheduleBillPaymentDao {
    private DataSource dataSource;
    @Override
    public void updateScheduleBillPaymentList(List<ScheduleLoanPaymentModel> sbpList) {
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


    public List<ScheduleLoanPaymentModel> getSchedulePaymentsByCustomerId(long customerId)

    {

        String hql = "from ScheduleBillPaymentModel sch where sch.relationCustomerIdCustomerModel.customerId=:customerId and sch.isDeleted=:deleted";

        String[] paramNames = { "customerId","deleted"};
        Object[] values = { customerId,Boolean.FALSE};
        List<ScheduleLoanPaymentModel> scheduleLoanPaymentModels = this.getHibernateTemplate().findByNamedParam(hql,paramNames,values);
        return scheduleLoanPaymentModels;

       /* if(CollectionUtils.isNotEmpty(scheduleLoanPaymentModels)){
            return scheduleLoanPaymentModels;
        }*/

    }

    @Override
    public ScheduleLoanPaymentModel saveOrUpdateScheduleLoanRequest(WorkFlowWrapper wrapper) throws FrameworkCheckedException {
        ScheduleLoanPaymentModel model = wrapper.getScheduleLoanPaymentModel();
        if(model == null)
        {
            model = new ScheduleLoanPaymentModel();
            model.setCustomerId(wrapper.getAppUserModel().getCustomerId());
            model.setNoOfOccurence(MessageUtil.getMessage("NOOFINSTALLMENTS"));
            model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            if(wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_RRN) != null)
                model.setThirdPartyVerificationRRN(String.valueOf(wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_RRN)));
        }
        getHibernateTemplate().saveOrUpdate(model);
        wrapper.setScheduleLoanPaymentModel(model);
        return model;
    }

}

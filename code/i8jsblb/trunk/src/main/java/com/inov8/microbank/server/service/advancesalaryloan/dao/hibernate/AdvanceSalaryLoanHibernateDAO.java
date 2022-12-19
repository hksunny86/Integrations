package com.inov8.microbank.server.service.advancesalaryloan.dao.hibernate;

import com.ctc.wstx.util.DataUtil;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class AdvanceSalaryLoanHibernateDAO extends BaseHibernateDAO<AdvanceSalaryLoanModel, Long, AdvanceSalaryLoanDAO> implements AdvanceSalaryLoanDAO {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<AdvanceSalaryLoanModel> loadAllAdvanceSalaryLoan() throws FrameworkCheckedException {

        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        sb.append("SELECT * FROM LOAN ");
        sb.append("WHERE  extract(Month from LAST_PAYMENT_DATE) !="+ (date.getMonth()+1));
        sb.append(" AND IS_COMPLETED =0 ");
        sb.append("AND DEBIT_BLOCK=0");
        List<AdvanceSalaryLoanModel> list = (List<AdvanceSalaryLoanModel>) jdbcTemplate.query(sb.toString(),new AdvanceSalaryLoanModel());
        return list;
    }

    @Override
    public AdvanceSalaryLoanModel loadAdvanceSalaryLoanByMobileNumber(String mobileNo) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(AdvanceSalaryLoanModel.class);
        criteria.add( Restrictions.eq("mobileNo", mobileNo));
        criteria.add( Restrictions.eq("isCompleted", Boolean.FALSE));
        List<AdvanceSalaryLoanModel> list = getHibernateTemplate().findByCriteria(criteria);

        AdvanceSalaryLoanModel appUserModel = null;

        if(list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }

        return appUserModel;
    }

    @Override
    public void update(Long noOfInstallmentPaid, Boolean isComplete, Long advanceSalaryLoanId) {
        StringBuilder str = new StringBuilder();
        str.append("update AdvanceSalaryLoanModel model set model.noOfInstallmentPaid =:noOfInstallmentPaid, isCompleted =:isCompleted, lastPaymentDate =:lastPaymentDate, " +
                "updatedOn =:updatedOn ");
        str.append("where advaceSalaryLoanId =:advaceSalaryLoanId");

        Query query = getSession().createQuery(str.toString());
        query.setParameter("noOfInstallmentPaid", noOfInstallmentPaid);
        query.setParameter("isCompleted", isComplete);
        query.setParameter("lastPaymentDate", new Date(System.currentTimeMillis()));
        query.setParameter("updatedOn", new Date(System.currentTimeMillis()));
        query.setParameter("advaceSalaryLoanId", advanceSalaryLoanId);

        query.executeUpdate();
    }

    @Override
    public List<AdvanceSalaryLoanModel> loadAllAdvanceSalaryLoanByIsCompleted() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        sb.append("SELECT * FROM LOAN ");
        sb.append("WHERE IS_COMPLETED =1");
        sb.append(" AND IS_INTIMATED =0 ");
        List<AdvanceSalaryLoanModel> list = (List<AdvanceSalaryLoanModel>) jdbcTemplate.query(sb.toString(),new AdvanceSalaryLoanModel());
        return list;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}

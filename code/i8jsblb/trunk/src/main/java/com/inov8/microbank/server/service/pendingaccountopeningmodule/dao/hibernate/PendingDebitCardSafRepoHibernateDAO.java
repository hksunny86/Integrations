package com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.DebitCardPendingSafRepo;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.PendingDebitCardSafRepoDAO;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class PendingDebitCardSafRepoHibernateDAO extends BaseHibernateDAO<DebitCardPendingSafRepo, Long, PendingDebitCardSafRepoDAO> implements PendingDebitCardSafRepoDAO {
    private JdbcTemplate jdbcTemplate;

    @Override
    public DebitCardPendingSafRepo loadDebitCardSafRepoByMobileNoAndCnic(DebitCardPendingSafRepo debitCardPendingSafRepo) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(DebitCardPendingSafRepo.class);
        Criterion criterionOne = Restrictions.eq("mobileNo", debitCardPendingSafRepo.getMobileNo());
        Criterion criterionTwo = Restrictions.eq("isCompleted", debitCardPendingSafRepo.getIsCompleted());
        Criterion criterionThree = Restrictions.eq("productId", debitCardPendingSafRepo.getProductId());
        Criterion criterionFour = Restrictions.eq("cnic", debitCardPendingSafRepo.getCnic());
        criteria.add(criterionOne);
        criteria.add(criterionTwo);
        criteria.add(criterionThree);
        criteria.add(criterionFour);
        List<DebitCardPendingSafRepo> list = getHibernateTemplate().findByCriteria(criteria);
        DebitCardPendingSafRepo debitCardPendingSafRepo1 = null;
        if (list != null && !list.isEmpty()) {
            debitCardPendingSafRepo1 = list.get(0);
        }

        return debitCardPendingSafRepo1;
    }

    @Override
    public DebitCardPendingSafRepo loadDebitCardSafRepo(DebitCardPendingSafRepo debitCardPendingSafRepo) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(DebitCardPendingSafRepo.class);
        Criterion criterionOne = Restrictions.eq("mobileNo", debitCardPendingSafRepo.getMobileNo());
        Criterion criterionTwo = Restrictions.eq("isCompleted", "1");
        Criterion criterionThree = Restrictions.eq("productId", debitCardPendingSafRepo.getProductId());
        Criterion criterionFour = Restrictions.eq("cnic", debitCardPendingSafRepo.getCnic());
        criteria.add(criterionOne);
        criteria.add(criterionTwo);
        criteria.add(criterionThree);
        criteria.add(criterionFour);
        List<DebitCardPendingSafRepo> list = getHibernateTemplate().findByCriteria(criteria);
        DebitCardPendingSafRepo debitCardPendingSafRepo1 = null;
        if (list != null && !list.isEmpty()) {
            debitCardPendingSafRepo1 = list.get(0);
        }

        return debitCardPendingSafRepo1;
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}

package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.vo.product.CommissionStakeholderVO;
import com.inov8.microbank.common.vo.rowmappers.CommissionStakeholderRowMapper;
import com.inov8.microbank.server.dao.commissionmodule.CommissionStakeholderDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class CommissionStakeholderHibernateDAO
    extends BaseHibernateDAO<CommissionStakeholderModel, Long, CommissionStakeholderDAO>
    implements CommissionStakeholderDAO
{

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CommissionStakeholderModel> findAllStakeHolders() throws FrameworkCheckedException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM COMMISSION_STAKEHOLDER");
        return (List<CommissionStakeholderModel>) jdbcTemplate.query(stringBuilder.toString(), new CommissionStakeholderModel());
    }

    @Override
    public CommissionStakeholderModel findStakeHolderById(Long stakeHolderId) throws FrameworkCheckedException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM COMMISSION_STAKEHOLDER WHERE COMMISSION_STAKEHOLDER_ID = " + stakeHolderId);
        List<CommissionStakeholderModel> list = (List<CommissionStakeholderModel>) jdbcTemplate.query(stringBuilder.toString(), new CommissionStakeholderModel());
        if(!list.isEmpty())
            return list.get(0);
        return null;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}

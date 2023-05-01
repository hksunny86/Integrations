package com.inov8.microbank.server.service.advancesalaryloan.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.ZongBalanceUpdateModel;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.advancesalaryloan.dao.ZongBalanceUpdateDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class ZongBalanceUpdateHibernateDAO extends BaseHibernateDAO<ZongBalanceUpdateModel, Long, ZongBalanceUpdateDAO>  implements ZongBalanceUpdateDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ZongBalanceUpdateModel> loadAllZongUpdateBalance() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        sb.append("SELECT * FROM ZONG_BALANCE ");
        List<ZongBalanceUpdateModel> list = (List<ZongBalanceUpdateModel>) jdbcTemplate.query(sb.toString(),new ZongBalanceUpdateModel());
        return list;
    }
}

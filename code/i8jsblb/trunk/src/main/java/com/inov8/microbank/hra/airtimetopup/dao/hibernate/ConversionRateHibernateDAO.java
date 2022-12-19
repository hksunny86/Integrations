package com.inov8.microbank.hra.airtimetopup.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.hra.airtimetopup.dao.ConversionRateDAO;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;
import com.inov8.microbank.hra.airtimetopup.vo.AirTimeTopUpVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.orm.hibernate3.SessionFactoryUtils;


import javax.sql.DataSource;
import java.util.List;

public class ConversionRateHibernateDAO extends BaseHibernateDAO<ConversionRateModel, Long, ConversionRateDAO> implements ConversionRateDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ConversionRateModel> getAll() {
        return null;
    }

    @Override
    public List<ConversionRateModel> findAllRates(ConversionRateModel conversionRateModel) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM CONVERSION_RATES WHERE END_DATE IS NULL");
        List<ConversionRateModel> list = (List<ConversionRateModel>) jdbcTemplate.query(sb.toString(), new ConversionRateModel());
        logger.info(list.size());
        return list;
    }

    @Override
    public List<ConversionRateModel> findAllConversionRates(ConversionRateModel conversionRateModel) throws FrameworkCheckedException {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ConversionRateModel.class);
        detachedCriteria.add(Restrictions.in("relationRateTypeIdRateTypeModel.rateTypeId",new Long[]{1L,2L}));
        detachedCriteria.addOrder(Order.desc("endDate"));
        getHibernateTemplate().getSessionFactory().openSession();
        List<ConversionRateModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        return list;
    }

    @Override
    public void save(ConversionRateModel conversionRateModel) {

    }

    @Override
    public void update(ConversionRateModel conversionRateModel, String[] params) {

    }

    @Override
    public AirTimeTopUpVO getRateForSpecificRemitance(Long remitanceInfoId) throws FrameworkCheckedException {
        AirTimeTopUpVO airTimeTopUpVO = new AirTimeTopUpVO();
        StringBuilder query = new StringBuilder();
        query.append("SELECT B.RATE DOLLAR_CONVERSION, C.RATE TOPUP_RATE ");
        query.append("FROM HRA_REMITANCE_INFO A,");
        query.append("(SELECT * ");
        query.append("FROM CONVERSION_RATES ");
        query.append("WHERE RATE_TYPE_ID = 1) B,");
        query.append("(SELECT * ");
        query.append("FROM CONVERSION_RATES ");
        query.append("WHERE RATE_TYPE_ID = 2) C ");
        query.append("WHERE A.REQ_DATE_TIME BETWEEN B.START_DATE AND NVL (B.END_DATE, SYSDATE) ");
        query.append("AND A.REQ_DATE_TIME BETWEEN C.START_DATE AND NVL (C.END_DATE, SYSDATE)");
        query.append(" AND A.HRA_REMITANCE_INFO_ID = ");
        query.append(remitanceInfoId);
        logger.info("Query to Fetch Dollar/Top-Up Rate :: " + query.toString());
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query.toString());
        while (sqlRowSet.next()) {
            Double dollarRate = sqlRowSet.getDouble("DOLLAR_CONVERSION");
            Double topUpRate = sqlRowSet.getDouble("TOPUP_RATE");

            airTimeTopUpVO.setDollarRate(dollarRate);
            airTimeTopUpVO.setTopUpRate(topUpRate);
        }
        return airTimeTopUpVO;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}

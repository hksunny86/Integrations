package com.inov8.microbank.server.dao.blbreports;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.DateUtils;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.blbreports.BdeKpiReportViewModel;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class BdeKpiReportHibernateDAO extends BaseHibernateDAO<BdeKpiReportViewModel, Long, BdeKpiReportDAO> implements BdeKpiReportDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<BdeKpiReportViewModel> loadBdeKpiReportViewModel(BdeKpiReportViewModel bdeKpiReportViewModel) throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        String dateStr = "";
        String endStr = "";
        DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
        if(bdeKpiReportViewModel.getStartDate() != null) {
            dateStr = format.format(bdeKpiReportViewModel.getStartDate());
        }
        if(bdeKpiReportViewModel.getEndDate() != null) {
            endStr = format.format(bdeKpiReportViewModel.getEndDate());
        }
        sb.append("SELECT * FROM BDE_KPI_REPORT_VIEW WHERE (AGENT_ID= NVL(").append(bdeKpiReportViewModel.getAgentId()).append(",AGENT_ID))");
        if(bdeKpiReportViewModel.getMobileNo() != null) {
            sb.append(" AND (MOBILE_NO = NVL('" + bdeKpiReportViewModel.getMobileNo() + "'").append(",MOBILE_NO))");
        }
        else{
            sb.append(" AND (MOBILE_NO = NVL(").append(bdeKpiReportViewModel.getMobileNo()).append(",MOBILE_NO))");
        }
        if(bdeKpiReportViewModel.getCnic() != null) {
            sb.append(" AND (CNIC = NVL('" + bdeKpiReportViewModel.getCnic() + "'").append(",CNIC))");
        }
        else{
            sb.append(" AND (CNIC = NVL(").append(bdeKpiReportViewModel.getCnic()).append(",CNIC))");
        }
        if(bdeKpiReportViewModel.getJsCashAccountNo() != null) {
            sb.append(" AND (JSCASH_ACCOUNT_NO = NVL('" + bdeKpiReportViewModel.getJsCashAccountNo() + "'").append(",JSCASH_ACCOUNT_NO))");
        }
        else{
            sb.append(" AND (JSCASH_ACCOUNT_NO = NVL(").append(bdeKpiReportViewModel.getJsCashAccountNo()).append(",JSCASH_ACCOUNT_NO))");
        }
//        if(bdeKpiReportViewModel.getCoreAccountStatus() != null) {
//            sb.append(" AND (CORE_ACCOUNT_NO = NVL('" + bdeKpiReportViewModel.getCoreAccountNo() + "'").append(",CORE_ACCOUNT_NO))");
//        }
//        else{
//            sb.append(" AND (CORE_ACCOUNT_NO = NVL(").append(bdeKpiReportViewModel.getCoreAccountNo()).append(",CORE_ACCOUNT_NO))");
//        }
        if(bdeKpiReportViewModel.getSalesUser() != null) {
            sb.append(" AND (SALE_USER = NVL('" + bdeKpiReportViewModel.getSalesUser() + "'").append(",SALE_USER))");
        }
        else{
            sb.append(" AND (SALE_USER = NVL(").append(bdeKpiReportViewModel.getSalesUser()).append(",SALE_USER))");
        }
        sb.append(" AND (ACCOUNT_TYPE = NVL(" + bdeKpiReportViewModel.getAccountType()).append(",ACCOUNT_TYPE))");
        if(bdeKpiReportViewModel.getJcashAccountStatus() != null) {
            sb.append(" AND (JCASH_ACCOUNT_STATUS = NVL('" + bdeKpiReportViewModel.getJcashAccountStatus() + "'").append(",JCASH_ACCOUNT_STATUS))");
        }
        else{
            sb.append(" AND (JCASH_ACCOUNT_STATUS = NVL(" + bdeKpiReportViewModel.getJcashAccountStatus()).append(",JCASH_ACCOUNT_STATUS))");
        }
        sb.append(" AND trunc(TRANSACTION_SUMMARY_DATE) BETWEEN NVL('" + dateStr + "'").append(",TRANSACTION_SUMMARY_DATE)");
        sb.append(" AND NVL('" + endStr + "'").append(",TRANSACTION_SUMMARY_DATE)");

//        bdeKpiReportViewModel.setStartDate(DateUtils.getDayStartDate(bdeKpiReportViewModel.getStartDate()));
        logger.info("Loading BDE KPI Report with Criteria: " + sb.toString());
        List<BdeKpiReportViewModel> list = (List<BdeKpiReportViewModel>) jdbcTemplate.query(sb.toString(),new BdeKpiReportViewModel());

        return list;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}

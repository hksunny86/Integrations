package com.inov8.microbank.server.dao.portal.reports.finance.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportSummaryModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.portal.reports.finance.CustomerBalanceSummaryReportDao;

public class CustomerBalanceReportSummaryHibernateDao extends BaseHibernateDAO<CustomerBalanceReportSummaryModel, Long, CustomerBalanceSummaryReportDao>
implements CustomerBalanceSummaryReportDao {
	private static final Logger LOGGER = Logger.getLogger( CustomerBalanceReportSummaryHibernateDao.class );
	@Override
	public void updateDailyCustomerBalanceSummary(List<CustomerBalanceReportSummaryModel> customerBalanceReportSummaryModelList, Long actionId)throws FrameworkCheckedException {
		
		if(actionId==PortalConstants.ACTION_CREATE)
		{
			LOGGER.info("Inserting records into CUSTOMER_BALANCE_REPORT.");
			for( CustomerBalanceReportSummaryModel customerBalanceReportSummaryModel : customerBalanceReportSummaryModelList )
	        {
				try
				{
					getHibernateTemplate().save(customerBalanceReportSummaryModel);
				}
				catch (DataAccessException e)
				{						
					 LOGGER.error("Failed to insert record into CUSTOMER_BALANCE_REPORT with DAILY_ACCOUNT_STATS_ID: " + customerBalanceReportSummaryModel.getDailyAccountStatsId());
					 throw e;
				} 
	        }
			LOGGER.info(customerBalanceReportSummaryModelList.size() + " records inserted into CUSTOMER_BALANCE_REPORT.");
		}
		else
		{
			LOGGER.info("Updating records in CUSTOMER_BALANCE_REPORT.");
			for( CustomerBalanceReportSummaryModel customerBalanceReportSummaryModel : customerBalanceReportSummaryModelList )
	        {	           
				try
				{
					super.saveOrUpdate(customerBalanceReportSummaryModel);
				} 
				catch (DataAccessException e)
				{						
					LOGGER.error("Failed to update record in CUSTOMER_BALANCE_REPORT with DAILY_ACCOUNT_STATS_ID: " + customerBalanceReportSummaryModel.getDailyAccountStatsId());
                    throw e;
				}				
	        }
			LOGGER.info(customerBalanceReportSummaryModelList.size() + " records updated in CUSTOMER_BALANCE_REPORT.");
		}
		
	}

}

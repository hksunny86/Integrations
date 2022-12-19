package com.inov8.microbank.server.dao.portal.reports.finance.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportSummaryModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportViewModel;
import com.inov8.microbank.server.dao.portal.reports.finance.CustomerBalanceReportViewDao;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jan 11, 2013 4:23:40 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CustomerBalanceReportViewHibernateDao extends BaseHibernateDAO<CustomerBalanceReportViewModel, Long, CustomerBalanceReportViewDao>
                                                         implements CustomerBalanceReportViewDao
{

	
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerBalanceReportViewModel> customSearchCustomerBalanceReportView(String decryptionSchedulerStatus, Boolean isEndDayBalanceNull, Integer chunkSize) throws FrameworkCheckedException {
		List<CustomerBalanceReportViewModel> list = null;

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustomerBalanceReportViewModel.class);
		detachedCriteria.add(Restrictions.eq("decryptionSchedulerStatus", decryptionSchedulerStatus));

		if( isEndDayBalanceNull != null && isEndDayBalanceNull )
		{
			detachedCriteria.add( Restrictions.isNull("endDayBalance") );
		}
		else
		{
			detachedCriteria.add( Restrictions.isNotNull("endDayBalance") );
		}
		detachedCriteria.addOrder(Order.desc("dailyAccountStatsId"));
		list = getHibernateTemplate().findByCriteria(detachedCriteria, 0, chunkSize);

		return list;
	}

	@Override
	public CustomList<CustomerBalanceReportViewModel> searchDailyCustomerBalanceReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		
		Calendar currentDate = GregorianCalendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND,0);
		currentDate.set(Calendar.MILLISECOND,0);
		
		if(currentDate.getTime().equals((Date)wrapper.getDateRangeHolderModel().getToDate()))
		{
			CustomerBalanceReportSummaryModel customerBalanceReportSummaryModel = new CustomerBalanceReportSummaryModel();
			
			List list = this.getHibernateTemplate().find("from CustomerBalanceReportSummaryModel where statsDate = (select max(statsDate) from CustomerBalanceReportSummaryModel)");
			if(!list.isEmpty()){
				customerBalanceReportSummaryModel = (CustomerBalanceReportSummaryModel) list.get(0);
			}
					
			Calendar yesterday = GregorianCalendar.getInstance();
			yesterday.setTime(currentDate.getTime());
			yesterday.add(Calendar.DATE,-1);
			
			Calendar maxDate = GregorianCalendar.getInstance();
			maxDate.setTime(customerBalanceReportSummaryModel.getStatsDate());
			
			if(maxDate.before(yesterday)){
				
				maxDate.add(Calendar.DATE,1);
				wrapper.getDateRangeHolderModel().setFromDate(maxDate.getTime());
			}			
		}
		
		
		
		CustomList<CustomerBalanceReportViewModel> customList = super.findByExample((CustomerBalanceReportViewModel)wrapper.getBasePersistableModel() , wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap(),wrapper.getDateRangeHolderModel());	
		
		return customList;
	}
}

package com.inov8.microbank.server.dao.portal.reports.tax.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.reportmodule.WHTStakeholderViewModel;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.server.dao.portal.reports.tax.WHTStakeholderViewDao;

/**
 * @author Kashif
 */
public class WHTStakeholderViewDaoImpl extends BaseHibernateDAO<WHTStakeholderViewModel, Long, WHTStakeholderViewDao> implements WHTStakeholderViewDao {


	public CustomList<WHTStakeholderViewModel> searchWHTStakeholderView(WHTStakeholderViewModel viewModel,	PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel, String... excludeProperty) throws DataAccessException, SQLException {
        
        List<WHTStakeholderViewModel> resultsetList = null;
		
		Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

        java.sql.CallableStatement callableStatement = null;

        try {
        	
			callableStatement = connection.prepareCall("{call withholding_tax_report_PKG.SET_VALUES(?,?)}");
			
			if(dateRangeHolderModel.getFromDate()!=null) {
				callableStatement.setTimestamp(1,new java.sql.Timestamp(dateRangeHolderModel.getFromDate().getTime()));
			} else {
				callableStatement.setTimestamp(1,new java.sql.Timestamp(new Date().getTime()));
			}
			
			if(dateRangeHolderModel.getToDate()!=null) {
				callableStatement.setTimestamp(2,new java.sql.Timestamp(dateRangeHolderModel.getToDate().getTime()));
			} else {
				callableStatement.setTimestamp(2,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
			}
			
			callableStatement.execute();
	        
	    	DetachedCriteria criteria = DetachedCriteria.forClass(viewModel.getClass());
	    	
	        criteria.add( Example.create(viewModel));
	    	
	    	ProjectionList projectionList = Projections.projectionList();

	        projectionList.add( Projections.rowCount() );
	        criteria.setProjection( projectionList ); 
	        
	        List<Integer> listSize = this.getHibernateTemplate().findByCriteria(criteria);
	        pagingHelperModel.setTotalRecordsCount(listSize.get(0));
	    	
	        criteria = DetachedCriteria.forClass(WHTStakeholderViewModel.class);
	        criteria.add(Example.create(viewModel));

	        DaoUtils.addSortingToCriteria( criteria, sortingOrderMap, viewModel.getPrimaryKeyFieldName() );
	        
	        if(pagingHelperModel.getPageNo()!=null) {
	        	
	        	resultsetList = getHibernateTemplate().findByCriteria(criteria, (pagingHelperModel.getPageNo() - 1) * pagingHelperModel.getPageSize(), pagingHelperModel.getPageSize());        	
	        }else {
	        	
	        	resultsetList = getHibernateTemplate().findByCriteria(criteria);
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
        
        CustomList<WHTStakeholderViewModel> customList = new CustomList<WHTStakeholderViewModel>();
        customList.setResultsetList(resultsetList);
        
        return customList;
    }

}
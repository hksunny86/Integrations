package com.inov8.microbank.server.dao.portal.reports.tax.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.tax.WHTSummaryViewModel;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.server.dao.portal.reports.tax.AgentCustWHTSummaryDAO;

public class AgentCustWHTSummaryHibernateDAO extends BaseHibernateDAO<WHTSummaryViewModel, Long, AgentCustWHTSummaryDAO> implements AgentCustWHTSummaryDAO
{
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WHTSummaryViewModel> loadAgentCustWHTReport(WHTSummaryViewModel model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap,
			DateRangeHolderModel dateRangeHolderModel) throws DataAccessException {
		
		Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

        java.sql.CallableStatement cstmt = null;

        try {
        	
			cstmt = connection.prepareCall("{ call agent_tx_view_PKG.SET_VALUES(?,?)}");
			
			if(dateRangeHolderModel.getFromDate()!=null){
				cstmt.setTimestamp(1,new java.sql.Timestamp(dateRangeHolderModel.getFromDate().getTime()));
			}else{
				cstmt.setTimestamp(1,new java.sql.Timestamp(new Date().getTime()));
			}
			
			if(dateRangeHolderModel.getToDate()!=null){
				cstmt.setTimestamp(2,new java.sql.Timestamp(dateRangeHolderModel.getToDate().getTime()));
			}else{
				cstmt.setTimestamp(2,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
			}
			
			cstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

        List<WHTSummaryViewModel> list = null;
        
        //Doing this as getAgentId is Primary key findByExample can't help
		if(null!=model.getAgentId()){
			Criterion criteria = Restrictions.eq("agentId", model.getAgentId());
			CustomList<WHTSummaryViewModel> customList = findByCriteria(criteria, model, null, null);
			if(null != customList && customList.getResultsetList() != null && customList.getResultsetList().size() > 0){
				list = customList.getResultsetList();
				pagingHelperModel.setTotalRecordsCount(list.size());
			}else{
				pagingHelperModel.setTotalRecordsCount(0);
			}
		}else{	
        
	    	DetachedCriteria criteria = DetachedCriteria.forClass(WHTSummaryViewModel.class);
	    	
	        criteria.add( Example.create( model ) );
	    	
	    	ProjectionList projectionList = Projections.projectionList();
	    	
	        projectionList.add( Projections.rowCount() );
	        criteria.setProjection( projectionList ); 
	        
	        List<Integer> listSize = this.getHibernateTemplate().findByCriteria(criteria);
	        pagingHelperModel.setTotalRecordsCount(listSize.get(0));
	    	
	        criteria = DetachedCriteria.forClass(WHTSummaryViewModel.class);
	        criteria.add( Example.create( model ) );
	
	        DaoUtils.addSortingToCriteria( criteria, sortingOrderMap, model.getPrimaryKeyFieldName() );
	        
	        List<WHTSummaryViewModel> results;
	        
	        if(pagingHelperModel.getPageNo()!=null){
	        	results = getHibernateTemplate().findByCriteria(criteria, (pagingHelperModel.getPageNo() - 1) * pagingHelperModel.getPageSize(), pagingHelperModel.getPageSize());
	        }else {
	        	results = getHibernateTemplate().findByCriteria(criteria);
	        }
	        list =  results;
		}
		
        return list;
    }
	
	

}

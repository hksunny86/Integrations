package com.inov8.microbank.server.dao.portal.reports.agent.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
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
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.agentreportsmodule.AgentTransactionDetailViewModel;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.server.dao.portal.reports.agent.AgentTransactionDetailViewDao;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Mar 29, 2013 3:57:31 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class AgentTransactionDetailViewHibernateDao extends BaseHibernateDAO<AgentTransactionDetailViewModel, Long, AgentTransactionDetailViewDao> implements AgentTransactionDetailViewDao
{

	@SuppressWarnings("unchecked")
	@Override
	public List<AgentTransactionDetailViewModel> loadAgentTransactionSummary(AgentTransactionDetailViewModel model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel,
			String... excludeProperty) throws DataAccessException {
		
		Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

        java.sql.CallableStatement cstmt = null;

        try {
			cstmt = connection.prepareCall("{ call agent_tx_view_PKG.SET_VALUES(?,?)}");
			cstmt.setDate(1,new java.sql.Date(dateRangeHolderModel.getFromDate().getTime()));
			cstmt.setDate(2,new java.sql.Date(dateRangeHolderModel.getToDate().getTime()));
			cstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	DetachedCriteria criteria = DetachedCriteria.forClass(AgentTransactionDetailViewModel.class);
    	
        criteria.add( Example.create( model ) );
    	
    	ProjectionList projectionList = Projections.projectionList();
    	
        projectionList.add( Projections.rowCount() );
        criteria.setProjection( projectionList ); 
        
        List<Integer> listSize = this.getHibernateTemplate().findByCriteria(criteria);
        pagingHelperModel.setTotalRecordsCount(listSize.get(0));
    	
        criteria = DetachedCriteria.forClass(AgentTransactionDetailViewModel.class);
        criteria.add( Example.create( model ) );

        DaoUtils.addDateRangeToCriteria( dateRangeHolderModel, criteria );
        DaoUtils.addSortingToCriteria( criteria, sortingOrderMap, model.getPrimaryKeyFieldName() );
        
        List<AgentTransactionDetailViewModel> results = getHibernateTemplate().findByCriteria(criteria,
                (pagingHelperModel.getPageNo() - 1) * pagingHelperModel.getPageSize(), pagingHelperModel.getPageSize());
        List<AgentTransactionDetailViewModel> list =  results;

        return list;
    }
	//************************************************************************************************************************************************
	@Override
	public List<AgentTransactionDetailViewModel> loadAgentTransactionDetail( AgentTransactionDetailViewModel model, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel,
			String... excludeProperty) throws DataAccessException {
		Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

        java.sql.CallableStatement cstmt = null;

        try {
			cstmt = connection.prepareCall("{ call agent_tx_view_PKG.SET_VALUES(?,?)}");
			cstmt.setDate(1,new java.sql.Date(dateRangeHolderModel.getFromDate().getTime()));
			cstmt.setDate(2,new java.sql.Date(dateRangeHolderModel.getToDate().getTime()));
			cstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	DetachedCriteria criteria = DetachedCriteria.forClass(AgentTransactionDetailViewModel.class);
    	
        criteria.add( Example.create( model ) );
    	
    	ProjectionList projectionList = Projections.projectionList();
    	
        projectionList.add( Projections.rowCount() );
        criteria.setProjection( projectionList ); 
        
        List<Integer> listSize = this.getHibernateTemplate().findByCriteria(criteria);
        pagingHelperModel.setTotalRecordsCount(listSize.get(0));
    	
        criteria = DetachedCriteria.forClass(AgentTransactionDetailViewModel.class);
        criteria.add( Example.create( model ) );

        DaoUtils.addDateRangeToCriteria( dateRangeHolderModel, criteria );
        DaoUtils.addSortingToCriteria( criteria, sortingOrderMap, model.getPrimaryKeyFieldName() );
        
        
        List<AgentTransactionDetailViewModel> results;
        
        if(pagingHelperModel.getPageNo()!=null){
        	results = getHibernateTemplate().findByCriteria(criteria, (pagingHelperModel.getPageNo() - 1) * pagingHelperModel.getPageSize(), pagingHelperModel.getPageSize());
        }else {
        	results = getHibernateTemplate().findByCriteria(criteria);
        }
        List<AgentTransactionDetailViewModel> list =  results;

        return list;
	}
	
	
	
	

}

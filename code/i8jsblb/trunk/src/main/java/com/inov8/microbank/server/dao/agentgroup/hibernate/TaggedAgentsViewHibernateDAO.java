package com.inov8.microbank.server.dao.agentgroup.hibernate;

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
import com.inov8.microbank.common.model.portal.reportmodule.WHTAgentReportViewModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentsListViewModel;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.server.dao.agentgroup.TaggedAgentsViewDAO;

public class TaggedAgentsViewHibernateDAO extends BaseHibernateDAO<TaggedAgentsListViewModel, Long, TaggedAgentsViewDAO> implements TaggedAgentsViewDAO{
	
	
	
	public List<TaggedAgentsListViewModel> findAppUsersByType(Long appUserType)
	{
		List<TaggedAgentsListViewModel> appUserList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( TaggedAgentsListViewModel.class );
	    detachedCriteria.add( Restrictions.eq( "TaggedAgentsListViewModel.pk", appUserType) );
	    appUserList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return appUserList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaggedAgentsListViewModel> loadTaggedagentReport(
			TaggedAgentsListViewModel exampleInstance,
			PagingHelperModel pagingHelperModel,
			DateRangeHolderModel dateRangeHolderModel)
			throws DataAccessException {
		
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

	        List<TaggedAgentsListViewModel> list = null;
			if(null!=exampleInstance.getParentId()){
				Criterion criteria = Restrictions.eq("parentId", exampleInstance.getParentId());
				CustomList<TaggedAgentsListViewModel> customList = findByCriteria(criteria, exampleInstance, null, null);
				if(null != customList && customList.getResultsetList() != null && customList.getResultsetList().size() > 0){
					list = customList.getResultsetList();
					pagingHelperModel.setTotalRecordsCount(list.size());
				}else{
					pagingHelperModel.setTotalRecordsCount(0);
				}			
	        return list;
			}
			 return list;
	}

	

}

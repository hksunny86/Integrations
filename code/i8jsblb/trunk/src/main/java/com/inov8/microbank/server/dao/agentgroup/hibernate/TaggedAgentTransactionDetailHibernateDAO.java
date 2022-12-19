package com.inov8.microbank.server.dao.agentgroup.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.portal.reportmodule.WHTAgentReportViewModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentTransactionModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentsListViewModel;
import com.inov8.microbank.server.dao.agentgroup.TaggedAgentTransactionDetailDAO;
import com.inov8.microbank.server.dao.agentgroup.TaggedAgentsViewDAO;

public class TaggedAgentTransactionDetailHibernateDAO extends BaseHibernateDAO<TaggedAgentTransactionModel, Long, TaggedAgentTransactionDetailDAO> implements TaggedAgentTransactionDetailDAO{


	
	@Override
	public CustomList<TaggedAgentTransactionModel> getTaggedAgentTransactions(
			TaggedAgentTransactionModel model,
			PagingHelperModel pagingHelperModel,
			DateRangeHolderModel dateRangeHolderModel) {
		
		Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();
		CustomList<TaggedAgentTransactionModel> list = null;

        java.sql.CallableStatement cstmt = null;

        try {
        	
			cstmt = connection.prepareCall("{ call PROD_WISE_TX_VIEW_PKG.SET_VALUES(?,?,?,?)}");
			
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
			if(model.getAgentId()!=null)
			{
			cstmt.setString(3, model.getAgentId());
			cstmt.setString(4, null);
			}
			if(model.getHandlerId()!=null)
			{
				cstmt.setString(3, null);
			cstmt.setLong(4, model.getHandlerId());
			}
			
			
			 cstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

       
        
//        //Doing this as getAgentId is Primary key findByExample can't help
        
		if(null!=model.getAgentId()){
			//Criterion criteria = Restrictions.eq("agentId", model.getAgentId());
			CustomList<TaggedAgentTransactionModel> customList = findAll();
			if(null != customList && customList.getResultsetList() != null && customList.getResultsetList().size() > 0){
				list = customList;
				pagingHelperModel.setTotalRecordsCount(customList.getResultsetList().size());
			}else{
				pagingHelperModel.setTotalRecordsCount(0);
			}
		}else if(null!=model.getHandlerId()){
			CustomList<TaggedAgentTransactionModel> customList = findAll();
			if(null != customList && customList.getResultsetList() != null && customList.getResultsetList().size() > 0){
				list = customList;
				pagingHelperModel.setTotalRecordsCount(customList.getResultsetList().size());
			}else{
				pagingHelperModel.setTotalRecordsCount(0);
			}
		}
		return list;
		
	}

}

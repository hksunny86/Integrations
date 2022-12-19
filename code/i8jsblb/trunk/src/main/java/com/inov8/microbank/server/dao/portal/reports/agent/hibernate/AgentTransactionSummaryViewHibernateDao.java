package com.inov8.microbank.server.dao.portal.reports.agent.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
import com.inov8.framework.common.util.ThreadLocalExportInfoModel;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.agentreportsmodule.AgentTransactionSummaryViewModel;
import com.inov8.microbank.common.model.transactiondetailinfomodule.AllpayTransInfoListViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.server.dao.portal.reports.agent.AgentTransactionSummaryViewDao;

public class AgentTransactionSummaryViewHibernateDao extends BaseHibernateDAO<AgentTransactionSummaryViewModel, Long, AgentTransactionSummaryViewDao> implements AgentTransactionSummaryViewDao
{

	@SuppressWarnings("unchecked")
	@Override
	public List<AgentTransactionSummaryViewModel> loadAgentTransactionSummary(AgentTransactionSummaryViewModel model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel,
			String... excludeProperty) throws DataAccessException {
		
		Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

        java.sql.CallableStatement cstmt = null;

        try {
			cstmt = connection.prepareCall("{ call agent_tx_view_PKG.SET_VALUES(?,?)}");
			
			if(dateRangeHolderModel.getFromDate()!=null)
			{
				cstmt.setDate(1,new java.sql.Date(dateRangeHolderModel.getFromDate().getTime()));
			}
			else
			{
				cstmt.setDate(1,new java.sql.Date(new Date().getTime()));
			}
			if(dateRangeHolderModel.getToDate()!=null)
			{
				cstmt.setDate(2,new java.sql.Date(dateRangeHolderModel.getToDate().getTime()));
			}
			else
			{
				cstmt.setDate(2,new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			}
			
			cstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
//		StringBuilder hql = new StringBuilder();
//		hql.append("{?= call agent_tx_view_PKG.SET_VALUES("+new java.sql.Date(dateRangeHolderModel.getFromDate().getTime())+","+new java.sql.Date(dateRangeHolderModel.getToDate().getTime())+")}");
//				
//		this.getHibernateTemplate().find(hql.toString());

    	DetachedCriteria criteria = DetachedCriteria.forClass(AgentTransactionSummaryViewModel.class);
    	
        criteria.add( Example.create( model ) );
//    	DaoUtils.addDateRangeToCriteria(dateRangeHolderModel, criteria);
    	
    	ProjectionList projectionList = Projections.projectionList();
    	
        /*if( sortingOrderMap != null && sortingOrderMap.size() > 0 )
        {
            for( String key : sortingOrderMap.keySet() )
            {
                projectionList.add( Projections.groupProperty( key ) );
            }
        }*/
        
        /*projectionList.add( Projections.groupProperty( "productName" ) );
        projectionList.add( Projections.count( "productId" ) ); 
        projectionList.add( Projections.groupProperty( "agentId" ) ); 
        projectionList.add( Projections.groupProperty( "retailerId" ) );
        projectionList.add( Projections.groupProperty( "retailerName" ) );
        projectionList.add( Projections.groupProperty( "regionId" ) );
        projectionList.add( Projections.groupProperty( "regionName" ) );
        projectionList.add( Projections.groupProperty( "distributorId" ) );
        projectionList.add( Projections.groupProperty( "distributorName" ) );
        projectionList.add( Projections.sum( "bankCreditAmount" ) );
        projectionList.add( Projections.sum( "bankDebitAmount" ) );
        projectionList.add( Projections.sum( "agentCommission" ) ); 
        projectionList.add( Projections.sum( "franchiseCommission" ) );*/
        projectionList.add( Projections.rowCount() );
        criteria.setProjection( projectionList ); 
        
        List<Integer> listSize = this.getHibernateTemplate().findByCriteria(criteria);
        pagingHelperModel.setTotalRecordsCount(listSize.get(0));
    	
        criteria = DetachedCriteria.forClass(AgentTransactionSummaryViewModel.class);
        criteria.add( Example.create( model ) );
//        criteria.setProjection( projectionList );

//        DaoUtils.addDateRangeToCriteria( dateRangeHolderModel, criteria );
        DaoUtils.addSortingToCriteria( criteria, sortingOrderMap, model.getPrimaryKeyFieldName() );
        if(ThreadLocalExportInfoModel.getExportInfo()!=null)
        {
            ThreadLocalExportInfoModel.getExportInfo().setDetachedCriteria(criteria);
            ThreadLocalExportInfoModel.getExportInfo().setPackageCall("{ call agent_tx_view_PKG.SET_VALUES(?,?)}");
            ThreadLocalExportInfoModel.getExportInfo().setFromDate(new java.sql.Date(dateRangeHolderModel.getFromDate().getTime()));
            ThreadLocalExportInfoModel.getExportInfo().setToDate(new java.sql.Date(dateRangeHolderModel.getToDate().getTime()));
        }
        List<AgentTransactionSummaryViewModel> results;
        
        if(pagingHelperModel.getPageNo()!=null){
        	results = getHibernateTemplate().findByCriteria(criteria,
        			(pagingHelperModel.getPageNo() - 1) * pagingHelperModel.getPageSize(), pagingHelperModel.getPageSize());        	
        }else {
        	results = getHibernateTemplate().findByCriteria(criteria);
        }
        List<AgentTransactionSummaryViewModel> list =  results;

        return list;
    }

    private List<AgentTransactionSummaryViewModel> extractList( List<Object[]> results )
    {
        List<AgentTransactionSummaryViewModel> list = null;
        if( results != null && !results.isEmpty() )
        {
            list = new ArrayList<AgentTransactionSummaryViewModel>();
            int colIdx;
            Iterator<Object[]> resultIterator = results.iterator();

            while( resultIterator.hasNext() )
            {
                Object[] obj = resultIterator.next();
                colIdx = 0;
                
                AgentTransactionSummaryViewModel model = new AgentTransactionSummaryViewModel();
                model.setProductName( null == obj[++colIdx]? null : String.valueOf( obj[colIdx] ) );
                model.setProductId(  null == obj[++colIdx]? 0 : Long.valueOf( obj[colIdx].toString()) );//Total number of transactions
                model.setAgentId( null == obj[++colIdx]? null : obj[colIdx].toString() );
                model.setRetailerId( null == obj[++colIdx]? null : Long.valueOf( obj[colIdx].toString()) );
                model.setRetailerName( null == obj[++colIdx]? null : obj[colIdx].toString() );
                model.setRegionId( null == obj[++colIdx]? null : Long.valueOf( obj[colIdx].toString()) );
                model.setRegionName( null == obj[++colIdx]? null : obj[colIdx].toString() );
                model.setDistributorId( null == obj[++colIdx]? null : Long.valueOf( obj[colIdx].toString()) );
                model.setDistributorName( null == obj[++colIdx]? null : obj[colIdx].toString() );
                model.setBankCreditAmountSum( null == obj[++colIdx]? 0 : CommonUtils.getDoubleOrDefaultValue(obj[colIdx]) );
                model.setBankDebitAmountSum( null == obj[++colIdx]? 0 : CommonUtils.getDoubleOrDefaultValue(obj[colIdx]) );
                model.setAgentCommissionSum( null == obj[++colIdx]? 0 :  CommonUtils.getDoubleOrDefaultValue( obj[colIdx] ) );
                model.setFranchiseCommissionSum( null == obj[++colIdx]? 0 : CommonUtils.getDoubleOrDefaultValue( obj[colIdx] ) );

                list.add( model );
            }
        }
        return list;
    }

}

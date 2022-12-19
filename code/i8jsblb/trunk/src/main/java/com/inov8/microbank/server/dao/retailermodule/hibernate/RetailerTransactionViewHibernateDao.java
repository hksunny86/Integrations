package com.inov8.microbank.server.dao.retailermodule.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.retailertransactionmodule.RetailerTransactionViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.server.dao.retailermodule.RetailerTransactionViewDao;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jan 30, 2013 4:14:05 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class RetailerTransactionViewHibernateDao extends BaseHibernateDAO<RetailerTransactionViewModel, Long, RetailerTransactionViewDao> implements RetailerTransactionViewDao
{

    @SuppressWarnings( "unchecked" )
    @Override
    public SearchBaseWrapper fetchRegionalRetailActivitySummary( SearchBaseWrapper wrapper ) throws DataAccessException
    {
        RetailerTransactionViewModel retailerTransactionViewModel = (RetailerTransactionViewModel) wrapper.getBasePersistableModel();
        LinkedHashMap<String,SortingOrder> sortingOrderMap = wrapper.getSortingOrderMap();
        DateRangeHolderModel dateRangeHolderModel = wrapper.getDateRangeHolderModel();

        DetachedCriteria detachedCriteria = createDetachedCriteria( retailerTransactionViewModel );
        ProjectionList projectionList = createProjectionList( sortingOrderMap );
        detachedCriteria.setProjection( projectionList );
        DaoUtils.addDateRangeToCriteria( dateRangeHolderModel, detachedCriteria );
        DaoUtils.addSortingToCriteria( detachedCriteria, wrapper.getSortingOrderMap(), retailerTransactionViewModel.getPrimaryKeyFieldName() );

        List<Object[]> results = getHibernateTemplate().findByCriteria(detachedCriteria);

        List<RetailerTransactionViewModel> list = extractList( results );
        CustomList<RetailerTransactionViewModel> customList = new CustomList<RetailerTransactionViewModel>( list );
        wrapper.setCustomList( customList );
        return wrapper;
    }

    private DetachedCriteria createDetachedCriteria(RetailerTransactionViewModel retailerTransactionViewModel )
    {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RetailerTransactionViewModel.class );
        detachedCriteria.add( Example.create( retailerTransactionViewModel ) );
        detachedCriteria.add( Restrictions.isNotNull( "supplierId" ) );
        detachedCriteria.add( Restrictions.isNotNull( "productId" ) );
        detachedCriteria.add( Restrictions.ne( "supProcessingStatusId", SupplierProcessingStatusConstants.FAILED ) );

        return detachedCriteria;
    }

    private ProjectionList createProjectionList( LinkedHashMap<String,SortingOrder> sortingOrderMap )
    {
        ProjectionList projectionList = Projections.projectionList();

        if( sortingOrderMap != null && sortingOrderMap.size() > 0 )
        {
            for( String key : sortingOrderMap.keySet() )
            {
                projectionList.add( Projections.groupProperty( key ) );
            }
        }
        projectionList.add( Projections.countDistinct( "senderAgentId" ) ); //# of Sender Agents
        projectionList.add( Projections.countDistinct( "receiverAgentId" ) ); //# of Receiver Agents
        projectionList.add( Projections.count( "transactionCode" ) ); //# of Transactions
        projectionList.add( Projections.sum( "transactionAmount" ) ); //Transaction Amount
        projectionList.add( Projections.sum( "serviceChargesExclusive" ) ); //Exclusive Charges
        projectionList.add( Projections.sum( "taxDeducted" ) ); //Tax Deducted
        projectionList.add( Projections.sum( "toAskari" ) ); //Bank Share
        projectionList.add( Projections.sum( "toFranchise1" ) ); //Sender Franchise Share
        projectionList.add( Projections.sum( "toAgent1" ) ); //Sender Agent Share
        //added by atif hussain
        projectionList.add( Projections.sum( "salesTeamCommission" ) ); //Sender Agent Share
        projectionList.add( Projections.sum( "othersCommission" ) ); //Sender Agent Share
        
        return projectionList;
    }

    private List<RetailerTransactionViewModel> extractList( List<Object[]> results )
    {
        List<RetailerTransactionViewModel> list = null;
        if( results != null && !results.isEmpty() )
        {
            list = new ArrayList<RetailerTransactionViewModel>();
            int colIdx;
            Iterator<Object[]> resultIterator = results.iterator();

            while( resultIterator.hasNext() )
            {
                Object[] obj = resultIterator.next();
                colIdx = 0;

                RetailerTransactionViewModel retailerTransactionViewModel = new RetailerTransactionViewModel();
                retailerTransactionViewModel.setSenderDistributorName( ObjectUtils.toString( obj[colIdx]) );
                retailerTransactionViewModel.setSenderAgentRegionName( ObjectUtils.toString( obj[++colIdx] ) );
                retailerTransactionViewModel.setSenderDistLevelName( ObjectUtils.toString( obj[++colIdx] ) );
                retailerTransactionViewModel.setReceiverDistributorName( ObjectUtils.toString( obj[++colIdx] ) );
                retailerTransactionViewModel.setReceiverAgentRegionName( ObjectUtils.toString( obj[++colIdx] ) );
                retailerTransactionViewModel.setReceiverDistLevelName( ObjectUtils.toString( obj[++colIdx] ) );
                colIdx++;//Ignore Supplier ID column value
                retailerTransactionViewModel.setProductName( ObjectUtils.toString( obj[++colIdx] ) );
                colIdx++;
                retailerTransactionViewModel.setNumOfSenderAgents( null == obj[colIdx]? null : Long.valueOf( obj[colIdx].toString() ) );
                colIdx++;
                retailerTransactionViewModel.setNumOfReceiverAgents( null == obj[colIdx]? null : Long.valueOf( obj[colIdx].toString() ) );
                colIdx++;
                retailerTransactionViewModel.setNumOfTransaction( null == obj[colIdx]? null : Long.valueOf( obj[colIdx].toString() ) );//Total number of transactions
                retailerTransactionViewModel.setTransactionAmount( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                retailerTransactionViewModel.setServiceChargesExclusive( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                retailerTransactionViewModel.setTaxDeducted( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                retailerTransactionViewModel.setToAskari( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                retailerTransactionViewModel.setToFranchise1( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                retailerTransactionViewModel.setToAgent1( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );

                //added by atif hussain
                retailerTransactionViewModel.setSalesTeamCommission(CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                retailerTransactionViewModel.setOthersCommission(CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                
                list.add( retailerTransactionViewModel );
            }
        }
        return list;
    }

}

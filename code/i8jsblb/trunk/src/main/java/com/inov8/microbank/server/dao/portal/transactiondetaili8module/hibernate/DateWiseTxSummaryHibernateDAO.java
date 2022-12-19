package com.inov8.microbank.server.dao.portal.transactiondetaili8module.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.DateWiseTxSummaryModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.server.dao.portal.transactiondetaili8module.DateWiseTxSummaryDAO;

public class DateWiseTxSummaryHibernateDAO
extends BaseHibernateDAO<DateWiseTxSummaryModel, Long,DateWiseTxSummaryDAO>
implements DateWiseTxSummaryDAO
{

    @SuppressWarnings("unchecked")
	@Override
    public List<DateWiseTxSummaryModel> loadDateWiseSummary( SearchBaseWrapper searchBaseWrapper ) throws DataAccessException
    {
    	DateWiseTxSummaryModel dateWiseTxSummaryModel = (DateWiseTxSummaryModel) searchBaseWrapper.getBasePersistableModel();
        DetachedCriteria criteria = DetachedCriteria.forClass( DateWiseTxSummaryModel.class );
        criteria.add( Example.create( dateWiseTxSummaryModel ) );
        /*criteria.add( Restrictions.isNotNull( "supplierId" ) );
        criteria.add( Restrictions.isNotNull( "productId" ) );*/
        if(dateWiseTxSummaryModel.getProductId()!=null)
        {
        	criteria.add(Restrictions.eq("productId", dateWiseTxSummaryModel.getProductId()));
        }

        ProjectionList projectionList = Projections.projectionList();

        LinkedHashMap<String, SortingOrder> sortingOrderMap = searchBaseWrapper.getSortingOrderMap();
        if( sortingOrderMap != null && sortingOrderMap.size() > 0 )
        {
            for( String key : sortingOrderMap.keySet() )
            {
                projectionList.add( Projections.groupProperty( key ) );
            }
        }
        projectionList.add( Projections.groupProperty( "productName" ) );
        projectionList.add( Projections.count( "transactionCode" ) ); //# of Transactions
        projectionList.add( Projections.sum( "transactionAmount" ) ); //Transaction Amount
        projectionList.add( Projections.sum( "inclusiveCharges" ) ); //Inclusive Charges
        projectionList.add( Projections.sum( "exclusiveCharges" ) ); //Exclusive Charges
        projectionList.add( Projections.sum( "taxDeducted" ) ); //Tax Deducted
        projectionList.add( Projections.sum( "akblCommission" ) ); //Bank Share
        projectionList.add( Projections.sum( "franchise1Commission" ) ); //Sender Franchise Share
        projectionList.add( Projections.sum( "franchise2Commission" ) ); //Receiver Franchise Share
        projectionList.add( Projections.sum( "agentCommission" ) ); //Sender Agent Share
        projectionList.add( Projections.sum( "agent2Commission" ) ); //Receiver Agent Share
        //added by atif hussain
        projectionList.add( Projections.sum( "salesTeamCommission" ) ); //Sender Agent Share
        projectionList.add( Projections.sum( "otherCommission" ) ); //Sender Agent Share

        criteria.setProjection( projectionList );

        DaoUtils.addDateRangeToCriteria( searchBaseWrapper.getDateRangeHolderModel(), criteria );
        DaoUtils.addSortingToCriteria( criteria, searchBaseWrapper.getSortingOrderMap(), dateWiseTxSummaryModel.getPrimaryKeyFieldName() );

        List<Object[]> results = getHibernateTemplate().findByCriteria( criteria );
        List<DateWiseTxSummaryModel> list = extractList( results );

        return list;
    }

    private List<DateWiseTxSummaryModel> extractList( List<Object[]> results )
    {
        List<DateWiseTxSummaryModel> list = null;
        if( results != null && !results.isEmpty() )
        {
            list = new ArrayList<DateWiseTxSummaryModel>();
            int colIdx;
            Iterator<Object[]> resultIterator = results.iterator();

            while( resultIterator.hasNext() )
            {
                Object[] obj = resultIterator.next();
                colIdx = 2;

                DateWiseTxSummaryModel dateWiseTxSummaryModel = new DateWiseTxSummaryModel();
                dateWiseTxSummaryModel.setProductName( String.valueOf( obj[colIdx] ) );
                ++colIdx;
                dateWiseTxSummaryModel.setProductId( null == obj[colIdx]? null : Long.valueOf( obj[colIdx].toString() ) );//Total number of transactions
                dateWiseTxSummaryModel.setTransactionAmount( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                dateWiseTxSummaryModel.setInclusiveCharges( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                dateWiseTxSummaryModel.setExclusiveCharges( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                dateWiseTxSummaryModel.setTaxDeducted( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                dateWiseTxSummaryModel.setAkblCommission( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                dateWiseTxSummaryModel.setFranchise1Commission( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                dateWiseTxSummaryModel.setFranchise2Commission( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                dateWiseTxSummaryModel.setAgentCommission( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                dateWiseTxSummaryModel.setAgent2Commission( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                //added by atif hussain
                dateWiseTxSummaryModel.setSalesTeamCommission(CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                dateWiseTxSummaryModel.setOtherCommission(CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );

                list.add( dateWiseTxSummaryModel );
            }
        }
        return list;
    }

}
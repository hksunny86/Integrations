package com.inov8.microbank.server.dao.portal.transactiondetaili8module.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.server.dao.portal.transactiondetaili8module.TransactionDetailPortalListViewDAO;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class TransactionDetailPortalListViewHibernateDAO
        extends BaseHibernateDAO<TransactionDetailPortalListModel, Long,TransactionDetailPortalListViewDAO>
        implements TransactionDetailPortalListViewDAO
{

    @Override
    public List<TransactionDetailPortalListModel> fetchDateWiseSummary( SearchBaseWrapper searchBaseWrapper ) throws DataAccessException
    {
        TransactionDetailPortalListModel transactionDetailPortalListModel = (TransactionDetailPortalListModel) searchBaseWrapper.getBasePersistableModel();
        DetachedCriteria criteria = DetachedCriteria.forClass( TransactionDetailPortalListModel.class );
        criteria.add( Example.create( transactionDetailPortalListModel ) );
        criteria.add( Restrictions.isNotNull( "supplierId" ) );
        criteria.add( Restrictions.isNotNull( "productId" ) );

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
        projectionList.add( Projections.sum( "agentCommission" ) ); //Sender Agent Share


        criteria.setProjection( projectionList );

        DaoUtils.addDateRangeToCriteria( searchBaseWrapper.getDateRangeHolderModel(), criteria );
        DaoUtils.addSortingToCriteria( criteria, searchBaseWrapper.getSortingOrderMap(), transactionDetailPortalListModel.getPrimaryKeyFieldName() );

        List<Object[]> results = getHibernateTemplate().findByCriteria( criteria );
        List<TransactionDetailPortalListModel> list = extractList( results );

        return list;
    }

    @Override
    public CustomList<TransactionDetailPortalListModel> findByExampleP2PCashTransfer(TransactionDetailPortalListModel model,
                                                                                     PagingHelperModel pagingHelperModel,
                                                                                     LinkedHashMap<String, SortingOrder> sortingOrderMap,
                                                                                     DateRangeHolderModel dateRangeHolderModel,
                                                                                     String... arg4)
            throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(TransactionDetailPortalListModel.class);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("transactionCode"), "transactionCode");
        projectionList.add(Projections.property("senderCnic"), "senderCnic");
        projectionList.add(Projections.property("saleMobileNo"), "saleMobileNo");
        projectionList.add(Projections.property("agent1Id"), "agent1Id");
        projectionList.add(Projections.property("sendingRegion"), "sendingRegion");
        projectionList.add(Projections.property("deviceType"), "deviceType");
        projectionList.add(Projections.property("isManualOTPin"), "isManualOTPin");
        projectionList.add(Projections.property("createdOn"), "createdOn");
        projectionList.add(Projections.property("updatedOn"), "updatedOn");
        projectionList.add(Projections.property("transactionAmount"), "transactionAmount");
        projectionList.add(Projections.property("exclusiveCharges"), "exclusiveCharges");
        projectionList.add(Projections.property("totalAmount"), "totalAmount");
        projectionList.add(Projections.property("recipientMobileNo"), "recipientMobileNo");
        projectionList.add(Projections.property("recipientCnic"), "recipientCnic");
        projectionList.add(Projections.property("agent2Id"), "agent2Id");
        projectionList.add(Projections.property("receivingRegion"), "receivingRegion");
        projectionList.add(Projections.property("recipientDeviceType"), "recipientDeviceType");
        projectionList.add(Projections.property("processingStatusName"), "processingStatusName");

        criteria.setProjection( projectionList );

        criteria.setResultTransformer(Transformers.aliasToBean(TransactionDetailPortalListModel.class));

        List<TransactionDetailPortalListModel> results = getHibernateTemplate().findByCriteria( criteria );

        CustomList<TransactionDetailPortalListModel> customList = new CustomList<TransactionDetailPortalListModel>();
        customList.setResultsetList(results);
        return customList;
    }

    private List<TransactionDetailPortalListModel> extractList( List<Object[]> results )
    {
        List<TransactionDetailPortalListModel> list = null;
        if( results != null && !results.isEmpty() )
        {
            list = new ArrayList<TransactionDetailPortalListModel>();
            int colIdx;
            Iterator<Object[]> resultIterator = results.iterator();

            while( resultIterator.hasNext() )
            {
                Object[] obj = resultIterator.next();
                colIdx = 2;

                TransactionDetailPortalListModel transactionDetailPortalListModel = new TransactionDetailPortalListModel();
                transactionDetailPortalListModel.setProductName( String.valueOf( obj[colIdx] ) );
                ++colIdx;
                transactionDetailPortalListModel.setProductId( null == obj[colIdx]? null : Long.valueOf( obj[colIdx].toString() ) );//Total number of transactions
                transactionDetailPortalListModel.setTransactionAmount( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                transactionDetailPortalListModel.setInclusiveCharges( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                transactionDetailPortalListModel.setExclusiveCharges( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                transactionDetailPortalListModel.setTaxDeducted( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                transactionDetailPortalListModel.setAkblCommission( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                transactionDetailPortalListModel.setFranchise1Commission( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );
                transactionDetailPortalListModel.setAgentCommission( CommonUtils.getDoubleOrDefaultValue( obj[++colIdx] ) );

                list.add( transactionDetailPortalListModel );
            }
        }
        return list;
    }

    @Override
    public SearchBaseWrapper findSenderRedeemTransactionDetail(SearchBaseWrapper searchBaseWrapper) {
        TransactionDetailPortalListModel transactionDetailPortalListModel = (TransactionDetailPortalListModel) searchBaseWrapper.getBasePersistableModel();
        DetachedCriteria criteria = DetachedCriteria.forClass(TransactionDetailPortalListModel.class);
        //criteria.add( Restrictions.eq("mobileNo", mobileNo) );
        Criterion criterionOne = Restrictions.in("processingStatusId",
                new String[]{ SupplierProcessingStatusConstants.REVERSED.toString(), SupplierProcessingStatusConstants.REVERSE_COMPLETED.toString()});

        Criterion criterionTwo = Restrictions.in("productId", new Long[]{ProductConstantsInterface.CASH_TRANSFER,ProductConstantsInterface.ACCOUNT_TO_CASH});
        LogicalExpression expressionCriterion = Restrictions.and(criterionOne, criterionTwo);

        CustomList<TransactionDetailPortalListModel> list = findByCriteria(expressionCriterion,transactionDetailPortalListModel,searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
        //	CustomList<ExtendedTransactionDetailPortalListModel> list = (CustomList<ExtendedTransactionDetailPortalListModel>)findByCriteria()
        if(list!=null)
        {
            searchBaseWrapper.setCustomList(list);
        }
        return searchBaseWrapper;
    }

    @Override
    public CustomList<TransactionDetailPortalListModel> searchTransactionDetailForI8(SearchBaseWrapper searchBaseWrapper, MnoModel mnoModel) throws FrameworkCheckedException
    {
        CustomList<TransactionDetailPortalListModel> customList;
        TransactionDetailPortalListModel model=(TransactionDetailPortalListModel) searchBaseWrapper.getBasePersistableModel();

        Criterion criterion = Restrictions.or( Restrictions.eq( "senderServiceOPId", mnoModel.getMnoId() ), Restrictions.eq( "receiverServiceOPId", mnoModel.getMnoId() ) );
        // model.setMno1Id(null);
        customList = super.findByCriteria( criterion,model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModelList());

        return customList;
    }
}
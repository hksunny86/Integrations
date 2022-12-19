package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.tax.WhtDetailedReportModel;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.tax.dao.WhtDetailedReportDAO;
import org.hibernate.criterion.*;
import org.springframework.dao.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Attique on 8/22/2017.
 */
public class WhtDetailedReportHibernateDAO extends
        BaseHibernateDAO<WhtDetailedReportModel, Long, WhtDetailedReportDAO>
        implements WhtDetailedReportDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<WhtDetailedReportModel> getFilteredWhtData(SearchBaseWrapper baseWrapper) throws DataAccessException {
        Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

        java.sql.CallableStatement cstmt = null;

        try {

            cstmt = connection.prepareCall("{ call agent_tx_view_PKG.SET_VALUES(?,?)}");

            if(baseWrapper.getDateRangeHolderModel().getFromDate()!=null){
                cstmt.setTimestamp(1,new java.sql.Timestamp(baseWrapper.getDateRangeHolderModel().getFromDate().getTime()));
            }else{
                cstmt.setTimestamp(1,new java.sql.Timestamp(new Date().getTime()));
            }

            if(baseWrapper.getDateRangeHolderModel().getToDate()!=null){
                cstmt.setTimestamp(2,new java.sql.Timestamp(baseWrapper.getDateRangeHolderModel().getToDate().getTime()));
            }else{
                cstmt.setTimestamp(2,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
            }

            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<WhtDetailedReportModel> list = null;
        WhtDetailedReportModel model=(WhtDetailedReportModel)baseWrapper.getBasePersistableModel();
        //Doing this as getAgentId is Primary key findByExample can't help


/*        DetachedCriteria criteria = DetachedCriteria.forClass(WhtDetailedReportModel.class);

        criteria.add( Example.create( model ) );

        ProjectionList projectionList = Projections.projectionList();

        projectionList.add( Projections.rowCount() );
        criteria.setProjection( projectionList );

        List<Integer> listSize = this.getHibernateTemplate().findByCriteria(criteria);

        baseWrapper.getPagingHelperModel().setTotalRecordsCount(listSize.get(0));

        criteria = DetachedCriteria.forClass(WhtDetailedReportModel.class);
        criteria.add( Example.create( model ) );
        logger.info(criteria);
        DaoUtils.addSortingToCriteria( criteria, baseWrapper.getSortingOrderMap(), model.getPrimaryKeyFieldName() );

        List<WhtDetailedReportModel> results = null;

        if(baseWrapper.getPagingHelperModel().getPageNo()!=null){
            results = getHibernateTemplate().findByCriteria(criteria, (baseWrapper.getPagingHelperModel().getPageNo() - 1) * baseWrapper.getPagingHelperModel().getPageSize(), baseWrapper.getPagingHelperModel().getPageSize());
        }else {
            results = getHibernateTemplate().findByCriteria(criteria);
        }
        list =  results;*/
        if(null!=model.getTaxPayerId()){
            Criterion criteria = Restrictions.eq("taxPayerId", model.getTaxPayerId());
            CustomList<WhtDetailedReportModel> customList = findByCriteria(criteria, model, null, null);
            if(null != customList && customList.getResultsetList() != null && customList.getResultsetList().size() > 0){
                list = customList.getResultsetList();
                baseWrapper.getPagingHelperModel().setTotalRecordsCount(list.size());
            }else{
                baseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
            }
        }else{

            DetachedCriteria criteria = DetachedCriteria.forClass(WhtDetailedReportModel.class);

            criteria.add( Example.create( model ) );

            ProjectionList projectionList = Projections.projectionList();

            projectionList.add( Projections.rowCount() );
            criteria.setProjection( projectionList );

            List<Integer> listSize = this.getHibernateTemplate().findByCriteria(criteria);
            baseWrapper.getPagingHelperModel().setTotalRecordsCount(listSize.get(0));

            criteria = DetachedCriteria.forClass(WhtDetailedReportModel.class);
            criteria.add( Example.create( model ) );

            DaoUtils.addSortingToCriteria( criteria, baseWrapper.getSortingOrderMap(), model.getPrimaryKeyFieldName() );

            List<WhtDetailedReportModel> results;

            if(baseWrapper.getPagingHelperModel().getPageNo()!=null){
                results = getHibernateTemplate().findByCriteria(criteria, (baseWrapper.getPagingHelperModel().getPageNo() - 1) * baseWrapper.getPagingHelperModel().getPageSize(), baseWrapper.getPagingHelperModel().getPageSize());
            }else {
                results = getHibernateTemplate().findByCriteria(criteria);
            }
            list =  results;
        }

        return list;
    }
}

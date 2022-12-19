package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.tax.FedDetailedReportModel;
import com.inov8.microbank.common.util.DaoUtils;
import com.inov8.microbank.tax.dao.FedDetailedReportDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Attique on 8/18/2017.
 */
public class FedDetailedReportHibernateDAO extends
        BaseHibernateDAO<FedDetailedReportModel, Long, FedDetailedReportDAO>
        implements FedDetailedReportDAO {
    @SuppressWarnings("unchecked")
    @Override
    public List<FedDetailedReportModel> getFilteredData(SearchBaseWrapper baseWrapper) throws DataAccessException {
        Connection connection = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

        java.sql.CallableStatement cstmt = null;

        try {

            cstmt = connection.prepareCall("{ call agent_tx_view_PKG.SET_VALUES(?,?)}");

            if(baseWrapper.getDateRangeHolderModelList().get(0).getFromDate()!=null){
                cstmt.setTimestamp(1,new java.sql.Timestamp(baseWrapper.getDateRangeHolderModelList().get(0).getFromDate().getTime()));
            }else{
                cstmt.setTimestamp(1,new java.sql.Timestamp(new Date().getTime()));
            }

            if(baseWrapper.getDateRangeHolderModelList().get(0).getToDate()!=null){
                cstmt.setTimestamp(2,new java.sql.Timestamp(baseWrapper.getDateRangeHolderModelList().get(0).getToDate().getTime()));
            }else{
                cstmt.setTimestamp(2,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
            }

            cstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<FedDetailedReportModel> list = null;
        FedDetailedReportModel model=(FedDetailedReportModel)baseWrapper.getBasePersistableModel();
        //Doing this as getAgentId is Primary key findByExample can't help


            DetachedCriteria criteria = DetachedCriteria.forClass(FedDetailedReportModel.class);

            criteria.add( Example.create( model ) );

            ProjectionList projectionList = Projections.projectionList();

            projectionList.add( Projections.rowCount() );
            criteria.setProjection( projectionList );

            List<Integer> listSize = this.getHibernateTemplate().findByCriteria(criteria);
            baseWrapper.getPagingHelperModel().setTotalRecordsCount(listSize.get(0));

            criteria = DetachedCriteria.forClass(FedDetailedReportModel.class);
            criteria.add( Example.create( model ) );
        logger.info(criteria);
            DaoUtils.addSortingToCriteria( criteria, baseWrapper.getSortingOrderMap(), model.getPrimaryKeyFieldName() );

            List<FedDetailedReportModel> results;

            if(baseWrapper.getPagingHelperModel().getPageNo()!=null){
                results = getHibernateTemplate().findByCriteria(criteria, (baseWrapper.getPagingHelperModel().getPageNo() - 1) * baseWrapper.getPagingHelperModel().getPageSize(), baseWrapper.getPagingHelperModel().getPageSize());
            }else {
                results = getHibernateTemplate().findByCriteria(criteria);
            }
            list =  results;

        return list;
    }



}

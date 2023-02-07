package com.inov8.microbank.disbursement.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.disbursement.dao.BulkDisbursementsFileInfoDAO;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import java.util.List;

public class BulkDisbursementsFileInfoHibernateDAO extends BaseHibernateDAO<BulkDisbursementsFileInfoModel, Long, BulkDisbursementsFileInfoDAO> implements BulkDisbursementsFileInfoDAO {

    @Override
    public int updateDisbursementFileStatus(Long fileInfoId, Integer status) {
        Query query = this.getSession().createQuery("update BulkDisbursementsFileInfoModel au set au.status=:status where au.fileInfoId =:fileInfoId");
        query.setParameter("status", status);
        query.setParameter("fileInfoId", fileInfoId);

        return  query.executeUpdate();
    }

    @Override
    public Object getDisbursementFileSettlementStatus(String batchNumber) {

        StringBuilder str = new StringBuilder();

        str.append("select First_Settled_On, Last_Settled_On, Total_Settled, Total_Created, valid_records from (");
        str.append("select min(settled_on) as First_Settled_On, max(settled_on) as Last_Settled_On, count(settled_on) Total_Settled ");
        str.append("from BULK_DISBURSEMENTS where batch_number=:batchNumber and TRANSACTION_CODE is not null) settled, ");
        str.append("(select count(ACCOUNT_CREATION_STATUS) Total_Created from BULK_DISBURSEMENTS where batch_number=:batchNumber and ACCOUNT_CREATION_STATUS is not null) created,");
        str.append("(select valid_records from bulk_disbursements_file_info where batch_number =:batchNumber)");

        Query q = getSession().createSQLQuery(str.toString());
        q.setParameter("batchNumber", batchNumber);

        return q.uniqueResult();
    }

    @Override
    public int cancelBatch(Long disbursementFileInfoId) {

        Session session = getSession();
        Query query = session.createQuery("UPDATE BulkDisbursementsModel au set au.deleted =:deleted " +
                "where au.relationFileInfoIdBulkDisbursementsFileInfoModel.fileInfoId =:fileInfoId");

        query.setParameter("deleted", true);
        query.setParameter("fileInfoId", disbursementFileInfoId);
        int results = query.executeUpdate();

        if(results > 0) {
            query = session.createQuery("UPDATE BulkDisbursementsFileInfoModel au set au.status =:status where  au.fileInfoId =:fileInfoId");
            query.setParameter("status", DisbursementStatusConstants.STATUS_CANCELED);
            query.setParameter("fileInfoId", disbursementFileInfoId);

            results = query.executeUpdate();
        }

        return results;
    }

    @Override
    public int updateDisbursementFileStatusAndApprove(String batchNumber, Integer status, String isApproved) throws FrameworkCheckedException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE BULK_DISBURSEMENTS_FILE_INFO SET STATUS = " + status + " , ISAPPROVED = " + isApproved);
        stringBuilder.append(" WHERE BATCH_NUMBER = " + batchNumber);
        int updatedRows =this.getSession().createSQLQuery(String.valueOf(stringBuilder)).executeUpdate();
        return updatedRows;
    }

    @Override
    public BulkDisbursementsFileInfoModel getBulkDisbursementsDataByBatchNumber(String batchNumber) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(BulkDisbursementsFileInfoModel.class);
        criteria.add( Restrictions.eq("batchNumber", batchNumber));

        List<BulkDisbursementsFileInfoModel> list = getHibernateTemplate().findByCriteria(criteria);

        BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = null;

        if(list != null && !list.isEmpty()) {
            bulkDisbursementsFileInfoModel = list.get(0);
        }

        return bulkDisbursementsFileInfoModel;
    }

    @Override
    public BulkDisbursementsFileInfoModel getBulkDisbursementsDataByStatus(String processingStatus) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(BulkDisbursementsFileInfoModel.class);
        criteria.add( Restrictions.eq("processingStatus", processingStatus));

        List<BulkDisbursementsFileInfoModel> list = getHibernateTemplate().findByCriteria(criteria);

        BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel = null;

        if(list != null && !list.isEmpty()) {
            bulkDisbursementsFileInfoModel = list.get(0);
        }

        return bulkDisbursementsFileInfoModel;
    }

    @Override
    public int updateDisbursementFileProcessingStatus(Long fileInfoId, String processingStatus) {
        Query query = this.getSession().createQuery("update BulkDisbursementsFileInfoModel au set au.processingStatus=:processingStatus where au.fileInfoId =:fileInfoId");
        query.setParameter("processingStatus", processingStatus);
        query.setParameter("fileInfoId", fileInfoId);

        return  query.executeUpdate();
    }
}
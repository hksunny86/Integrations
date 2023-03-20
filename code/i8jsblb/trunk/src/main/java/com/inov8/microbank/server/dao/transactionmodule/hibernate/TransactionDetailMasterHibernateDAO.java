package com.inov8.microbank.server.dao.transactionmodule.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.WalletSafRepoModel;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailMasterDAO;

import javax.sql.DataSource;

/**
 * <p>Title: </p>
 * <p>
 * <p>Description: </p>
 * <p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TransactionDetailMasterHibernateDAO
        extends BaseHibernateDAO<TransactionDetailMasterModel, Long, TransactionDetailMasterDAO>
        implements TransactionDetailMasterDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public TransactionDetailMasterHibernateDAO() {
    }

    /**
     * @param transactionProcessingStatus
     * @param transactionId
     * @return
     */


    @SuppressWarnings("unchecked")
    public List<TransactionDetailMasterModel> getTaggedAgentTransactionDetailList(SearchBaseWrapper sBaseWrapper) {
        TransactionDetailMasterModel tDetailMasterModel = new TransactionDetailMasterModel();
        tDetailMasterModel = (TransactionDetailMasterModel) sBaseWrapper.getBasePersistableModel();
        String handlerMfsId = tDetailMasterModel.getHandlerMfsId();
        String agentId = tDetailMasterModel.getAgent1Id();
        Long productId = tDetailMasterModel.getProductId();


        List<TransactionDetailMasterModel> transactionDetailMasterList = new ArrayList<TransactionDetailMasterModel>();

        LogicalExpression logicalExperession = null;

        Criterion crProduct = Restrictions.eq("productId", productId);
        Criterion crStatus = Restrictions.in("supProcessingStatusId", new Long[]{1L, 4L, 5L, 6L, 12L});
        logicalExperession = Restrictions.and(crProduct, crStatus);


        if (!StringUtil.isNullOrEmpty(handlerMfsId)) {
            Criterion crHandler = Restrictions.eq("handlerMfsId", handlerMfsId);
            logicalExperession = Restrictions.and(logicalExperession, crHandler);
        }
        if (!StringUtil.isNullOrEmpty(agentId)) {
            Criterion criteriaOne = Restrictions.eq("agent1Id", agentId);
            Criterion criteriaTwo = Restrictions.eq("agent2Id", agentId);
            LogicalExpression agentIdExpression = Restrictions.or(criteriaOne, criteriaTwo);
            logicalExperession = Restrictions.and(logicalExperession, agentIdExpression);
        }

        CustomList<TransactionDetailMasterModel> customList = findByCriteria(logicalExperession, new TransactionDetailMasterModel(), null, sBaseWrapper.getSortingOrderMap(), sBaseWrapper.getDateRangeHolderModel());
        if (null != customList && customList.getResultsetList() != null && customList.getResultsetList().size() > 0) {
            transactionDetailMasterList = customList.getResultsetList();
            sBaseWrapper.getPagingHelperModel().setTotalRecordsCount(transactionDetailMasterList.size());
        } else {
            sBaseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
        }


        return transactionDetailMasterList;
    }


    public Integer updateTransactionProcessingStatus(List<Long> transactionId, Long transactionProcessingStatus, String processingStatusName) {

        StringBuilder query = new StringBuilder();
        query.append("update TransactionDetailMasterModel set supProcessingStatusId = :transactionProcessingStatus, processingStatusName = :processingStatusName ");
        query.append("where transactionId in (:transactionId) ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query sessionQuery = session.createQuery(query.toString());
        sessionQuery.setParameter("transactionProcessingStatus", transactionProcessingStatus);
        sessionQuery.setParameter("processingStatusName", processingStatusName);
        sessionQuery.setParameterList("transactionId", transactionId);

        int updateCount = sessionQuery.executeUpdate();
        SessionFactoryUtils.releaseSession(session, getSessionFactory());

        return updateCount;
    }

    @Override
    public Integer updateTransactionDetailMasterForLeg2(TransactionDetailMasterModel model) {

        StringBuilder query = new StringBuilder();
        query.append("update TransactionDetailMasterModel set supProcessingStatusId = :transactionProcessingStatus ");
        query.append(", processingStatusName = :processingStatusName ");
        query.append(", agent2Id = :agent2Id ");
        query.append(", recipientAccountNo = :recipientAccountNo ");
        query.append(", updatedOn = :updatedOn ");
        query.append(", recipientMfsId = :recipientMfsId ");
        query.append(", recipientAccountNick = :recipientAccountNick ");
        query.append("where transactionId = :transactionId ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query sessionQuery = session.createQuery(query.toString());
        sessionQuery.setParameter("transactionProcessingStatus", SupplierProcessingStatusConstants.COMPLETED);
        sessionQuery.setParameter("processingStatusName", SupplierProcessingStatusConstants.COMPLETE_NAME);
        sessionQuery.setParameter("agent2Id", model.getAgent2Id());
        sessionQuery.setParameter("recipientAccountNo", model.getRecipientAccountNo());
        sessionQuery.setParameter("updatedOn", new Date());
        sessionQuery.setParameter("transactionId", model.getTransactionId());
        sessionQuery.setParameter("recipientMfsId", model.getRecipientMfsId());
        sessionQuery.setParameter("recipientAccountNick", model.getRecipientAccountNick());

        int updateCount = sessionQuery.executeUpdate();
        SessionFactoryUtils.releaseSession(session, getSessionFactory());

        return updateCount;
    }

    @Override
    public List<TransactionDetailMasterModel> findCustomerPendingTrxByMobile(String recipientMobileNo) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransactionDetailMasterModel.class);
        detachedCriteria.add(Restrictions.eq("recipientMobileNo", recipientMobileNo));
        detachedCriteria.add(Restrictions.eq("supProcessingStatusId", SupplierProcessingStatusConstants.IN_PROGRESS));

        Criterion c1 = Restrictions.in("productId", new Long[]{50010L, 50011L});
        Criterion c2 = Restrictions.eq("serviceId", ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER);
        detachedCriteria.add(Restrictions.or(c1, c2));

        List<TransactionDetailMasterModel> list = this.getHibernateTemplate().findByCriteria(detachedCriteria);
        return list;
    }

    public List<TransactionDetailMasterModel> findCustomerPendingTrxByCNIC(String customerCNIC) {

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransactionDetailMasterModel.class);

        detachedCriteria.add(Restrictions.eq("recipientCnic", customerCNIC));
        detachedCriteria.add(Restrictions.eq("supProcessingStatusId", SupplierProcessingStatusConstants.IN_PROGRESS));

        Criterion c1 = Restrictions.in("productId", new Long[]{50010L, 50011L, 50052L});
        Criterion c2 = Restrictions.eq("serviceId", ServiceConstantsInterface.PAYMENT_SERVICE);
        detachedCriteria.add(Restrictions.or(c1, c2));

        List<TransactionDetailMasterModel> list = this.getHibernateTemplate().findByCriteria(detachedCriteria);

        return list;

    }

    @Override
    public int findAgentPendingTrxByCNIC(String agentCNIC) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransactionDetailMasterModel.class);

        detachedCriteria.add(Restrictions.eq("senderCnic", agentCNIC));
        detachedCriteria.add(Restrictions.in("supProcessingStatusId", new Long[]{SupplierProcessingStatusConstants.IN_PROGRESS,
                SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING}));

        //Criterion c1 = Restrictions.in("productId", new Long[]{50010L,50011L});
        //Criterion c2 = Restrictions.eq("serviceId", ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER);
        //detachedCriteria.add(Restrictions.or(c1, c2));

        List<TransactionDetailMasterModel> list = this.getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list != null)
            return list.size();

        return 0;
    }

    @Override
    public TransactionDetailMasterModel findCustomerTrxByTransactionCode(String transactionCode) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransactionDetailMasterModel.class);

        detachedCriteria.add(Restrictions.eq("transactionCode", transactionCode));
        detachedCriteria.add(Restrictions.eq("supProcessingStatusId", SupplierProcessingStatusConstants.IN_PROGRESS));

        Criterion c1 = Restrictions.in("productId", new Long[]{50010L, 50011L});
        Criterion c2 = Restrictions.eq("serviceId", ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER);
        detachedCriteria.add(Restrictions.or(c1, c2));

        List<TransactionDetailMasterModel> list = this.getHibernateTemplate().findByCriteria(detachedCriteria);

        if (list == null || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public TransactionDetailMasterModel findTrxByTransactionCodeAndStatus(String transactionCode) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransactionDetailMasterModel.class);

        detachedCriteria.add(Restrictions.eq("transactionCode", transactionCode));

        List<TransactionDetailMasterModel> list = this.getHibernateTemplate().findByCriteria(detachedCriteria);

        if (list == null || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }


    @Override
    public List<TransactionDetailMasterModel> findWalInUserPendingTransactions(String mobileNo, String cnic) {

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransactionDetailMasterModel.class);

        if ((!StringUtil.isNullOrEmpty(cnic)) && (!StringUtil.isNullOrEmpty(mobileNo))) {
            detachedCriteria.add(Restrictions.or(Restrictions.eq("recipientCnic", cnic), Restrictions.eq("recipientMobileNo", mobileNo)));
        } else if ((!StringUtil.isNullOrEmpty(cnic)) && StringUtil.isNullOrEmpty(mobileNo)) {
            detachedCriteria.add(Restrictions.eq("recipientCnic", cnic));
        } else if ((!StringUtil.isNullOrEmpty(mobileNo)) && StringUtil.isNullOrEmpty(cnic)) {
            detachedCriteria.add(Restrictions.eq("recipientMobileNo", mobileNo));
        }

        detachedCriteria.add(Restrictions.eq("supProcessingStatusId", SupplierProcessingStatusConstants.IN_PROGRESS));

        return this.getHibernateTemplate().findByCriteria(detachedCriteria);
    }

    @Override
    public int getCustomerChallanCount(String mobileNo) {
        int result = 0;
        if (mobileNo != null && mobileNo != "") {
            String query = "select SUP_PROCESSING_STATUS_ID from TRANSACTION_DETAIL_MASTER where SALE_MOBILE_NO='" + mobileNo
                    + "' and product_id=50055 and TRUNC(CREATED_ON) =TRUNC(SYSDATE) and SUP_PROCESSING_STATUS_ID IN (1,13)";
            try {
                result = jdbcTemplate.queryForInt(query);
            } catch (Exception ex) {
                result = 0;
            }
        }
        return result;
    }

    @Override
    public long getPaidChallan(String consumerNo, String productCode) {
        Long result = 0L;
        if (consumerNo != null && consumerNo != "") {
            String query = "select SUP_PROCESSING_STATUS_ID from TRANSACTION_DETAIL_MASTER where CONSUMER_NO='" + consumerNo + "' and SUP_PROCESSING_STATUS_ID IN(1,13,4)and product_Code='" + productCode + "'";
            try {
                logger.info("Query to validate Segment in TransactionDetailMasterHibernateDAO.getPaidChallan() :: " + query.toString());
                result = jdbcTemplate.queryForLong(query);
                logger.info("Query Result :: " + result);
            } catch (Exception ex) {
                result = 0L;
            }
        }
        return result;
    }

    @Override
    public TransactionDetailMasterModel loadTDMbyRRN(String rrn) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransactionDetailMasterModel.class);

        detachedCriteria.add(Restrictions.eq("fonepayTransactionCode", rrn));

        List<TransactionDetailMasterModel> list = this.getHibernateTemplate().findByCriteria(detachedCriteria);

        if (list == null || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public TransactionDetailMasterModel loadTDMbyReserved2(String reserved2) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransactionDetailMasterModel.class);

        detachedCriteria.add(Restrictions.eq("reserved2", reserved2));

        List<TransactionDetailMasterModel> list = this.getHibernateTemplate().findByCriteria(detachedCriteria);

        if (list == null || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public TransactionDetailMasterModel loadTDMbyMobileNumber(String mobileNo, String productId) {
        //query to be added
        StringBuilder sb = new StringBuilder();

        String query = "select * from TRANSACTION_DETAIL_MASTER where SALE_MOBILE_NO='" + mobileNo + "' " +
                "and PRODUCT_ID='" + productId + "'" + "and trunc(CREATED_ON) between trunc(sysdate)-180 and trunc(sysdate)";

        List<TransactionDetailMasterModel> result = jdbcTemplate.query
                (query, new BeanPropertyRowMapper<TransactionDetailMasterModel>(TransactionDetailMasterModel.class));
        if(!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public TransactionDetailMasterModel loadTDMbyProductId(String mobileNo, String productId) {
        StringBuilder sb = new StringBuilder();

        String query = "select * from TRANSACTION_DETAIL_MASTER where SALE_MOBILE_NO='" + mobileNo + "' " +
                "and PRODUCT_ID='" + productId + "'";

        List<TransactionDetailMasterModel> result = jdbcTemplate.query
                (query, new BeanPropertyRowMapper<TransactionDetailMasterModel>(TransactionDetailMasterModel.class));
        if(!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<TransactionDetailMasterModel> loadTDMbyMobileandDateRange(String mobileNo, Date startDate, Date endDate, String productId) {
        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT * FROM WALLET_SAF_REPO");
//        sb.append(" where IS_COMPLETE=0");
        DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
        String dateStr = format.format(startDate) ;
        String endStr = format.format(endDate);

        String query = "select * from TRANSACTION_DETAIL_MASTER where (SALE_MOBILE_NO || RECIPIENT_MOBILE_NO)='" + mobileNo + "' " +
                "and PRODUCT_ID='" + productId + "'" + "and trunc(CREATED_ON) between '" + dateStr + "'and'" + endStr + "'";

        List<TransactionDetailMasterModel> result = jdbcTemplate.query
                (query, new BeanPropertyRowMapper<TransactionDetailMasterModel>(TransactionDetailMasterModel.class));
        if(!result.isEmpty()) {
            return result;
        }
        return null;
    }

    @Override
    public BaseWrapper loadAndLockTransactionDetailMasterModel(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        TransactionDetailMasterModel transactionDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
        Session session = null;
        session = getSessionFactory().getCurrentSession();
//		and productId=:productId
//		.setLong("productId", transactionDetailMasterModel.getProductId())
        session = getSessionFactory().getCurrentSession();
        String queryString = "from TransactionDetailMasterModel tdm where fonepayTransactionCode = :fonepayTransactionCode";
        List<TransactionDetailMasterModel> tdm = session.createQuery(queryString).setString("fonepayTransactionCode", transactionDetailMasterModel.getTransactionCode()).setLockMode("tdm", LockMode.UPGRADE).list();
        if (tdm.size() > 0 && tdm != null) {
            baseWrapper.setBasePersistableModel(tdm.get(0));
        }

        return baseWrapper;

    }

    @Override
    public TransactionDetailMasterModel saveOrUpdate(TransactionDetailMasterModel transactionDetailMasterModel) {

        this.getHibernateTemplate().saveOrUpdate(transactionDetailMasterModel);

        this.getHibernateTemplate().flush();

        this.getHibernateTemplate().evict(transactionDetailMasterModel);

        return transactionDetailMasterModel;

    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }


}

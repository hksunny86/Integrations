package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.tax.common.TaxConstantsInterface;
import com.inov8.microbank.tax.dao.DateWiseUserWHTAmountViewDAO;
import com.inov8.microbank.tax.model.DateWiseUserWHTAmountViewModel;
import com.inov8.microbank.tax.model.WHTConfigModel;
import com.inov8.microbank.tax.service.TaxManager;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DateWiseUserWHTAmountViewHibernateDAO
        extends BaseHibernateDAO<DateWiseUserWHTAmountViewModel, Long, DateWiseUserWHTAmountViewDAO> implements DateWiseUserWHTAmountViewDAO {


    public List<DateWiseUserWHTAmountViewModel> loadWithholdingUsersList(WHTConfigModel cashWithdrawalWHTConfigModel ,WHTConfigModel transferWHTConfigModel) throws Exception{
       
    	logger.info("Start of DateWiseUserWHTAmountViewHibernateDAO.loadWithholdingUsersList()");
    	
    	List<DateWiseUserWHTAmountViewModel> resultList = null;
        
        
        Double withdrawalThresholdLimit = cashWithdrawalWHTConfigModel.getThresholdLimit();
        Double transferThresholdLimit = transferWHTConfigModel.getThresholdLimit();
        
        Criteria criteria = getSession().createCriteria(DateWiseUserWHTAmountViewModel.class, "model");
        
        Criterion withdrawalCriterionOne = Restrictions.eq("relationWhtConfigIdWhtConfigModel.whtConfigId", TaxConstantsInterface.WHT_CONFIG_WITHDRAWAL_ID.longValue());	
		Criterion withdrawalCriterionTwo = Restrictions.gt("transactionAmount", withdrawalThresholdLimit);
		LogicalExpression expressionCriterion = Restrictions.and(withdrawalCriterionOne, withdrawalCriterionTwo);
		
		Criterion transferCriterionOne = Restrictions.eq("relationWhtConfigIdWhtConfigModel.whtConfigId", TaxConstantsInterface.WHT_CONFIG_TRANSFER_ID.longValue());	
		Criterion transferCriterionTwo = Restrictions.gt("transactionAmount", transferThresholdLimit);
		LogicalExpression expressionCriterionTwo = Restrictions.and(transferCriterionOne, transferCriterionTwo);
        
        LogicalExpression finalExpression = Restrictions.or(expressionCriterion, expressionCriterionTwo);
        
        criteria.addOrder(Order.asc("transactionDate"));
        criteria.add(finalExpression);
        
        resultList = criteria.list();
        
        logger.info("End of DateWiseUserWHTAmountViewHibernateDAO.loadWithholdingUsersList()");
        
        return resultList;
    }

    public Map<String, Double> loadSumTransactionsByDate(Date fromDate, Date toDate, Long[] productIds){
        Map<String, Double> customersMap = new HashMap<String, Double>();
        String[] processingStatuses = {SupplierProcessingStatusConstants.COMPLETED + ""};
//        DetachedCriteria criteria = DetachedCriteria.forClass(TransactionDetailPortalListModel.class);
        Criteria criteria = getSession().createCriteria(TransactionDetailPortalListModel.class);
        criteria.add(Restrictions.in("productId", productIds));
        criteria.add(Restrictions.ge("createdOn", fromDate)).add(Restrictions.lt("createdOn", toDate));
//        criteria.add(Restrictions.in("processingStatusId", processingStatuses));

        criteria.setProjection(Projections.projectionList()
                .add(Projections.groupProperty("saleMobileNo"))
                .add(Projections.sum("transactionAmount")));

        List<Object> objectList = (List<Object>) criteria.list();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(objectList)){
            System.out.println(objectList.size());
            for (Object obj : objectList) {
                Object[] objArray = (Object[]) obj;
                String mobileNo = (String)objArray[0];
                Double sumAmount = (Double)objArray[1];
                if (mobileNo != null && sumAmount != null){
                    customersMap.put(mobileNo, sumAmount);
                }
            }

        }

        return  customersMap;
    }



}

package com.inov8.microbank.account.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.account.dao.BlacklistedCnicsDAO;
import com.inov8.microbank.account.model.BlacklistedCnicsModel;

/**
 * Created by Malik on 10/9/2016.
 */
public class BlacklistedCnicsHibernateDAO extends BaseHibernateDAO<BlacklistedCnicsModel,Long,BlacklistedCnicsDAO> implements BlacklistedCnicsDAO
{
    private static final String BLACKLISTED_CNICS = "SELECT CNIC_NO FROM BLACKLISTED_CNICS WHERE IS_BLACKLISTED = 1";

    private static final int PARAMETER_LIMIT = 800;

    
    @Override
    public List<String> loadBlacklistedCNICList() {

        Query query = getSession().createSQLQuery(BLACKLISTED_CNICS);
        return query.list();
    }

    @Override
    public BlacklistedCnicsModel findBlacklistedCnicsModelByCnicNo(String cnicNo){
        List<BlacklistedCnicsModel> blacklistedCnicsModelList=null;
        Session session = this.getSession();
        Criteria criteria = session.createCriteria( BlacklistedCnicsModel.class ) ;
        Criterion cnicCriteria = Restrictions.eq("cnicNo", cnicNo) ;
        criteria.add(cnicCriteria);
        blacklistedCnicsModelList=(List<BlacklistedCnicsModel>) criteria.list();
        if(blacklistedCnicsModelList.size()==0){
            return null;
        }
        else {
            return blacklistedCnicsModelList.get(0);
        }
    }


    @Override
    public List<BlacklistedCnicsModel> loadBlacklistedCnicsModelByCnicNos(Collection<String> cnicNos){
        DetachedCriteria criteria = DetachedCriteria.forClass(BlacklistedCnicsModel.class);
        //criteria.add(Restrictions.in("cnicNo", cnicNos));
        List cniclist = new ArrayList(cnicNos);
        criteria.add(BlacklistedCnicsHibernateDAO.buildInCriterion("cnicNo", cniclist));
        
        List<BlacklistedCnicsModel> list = this.getHibernateTemplate().findByCriteria(criteria);
        return list;
    }

    @Override
    public Boolean isCnicBlacklisted(String cnicNo)
    {
        DetachedCriteria criteria = DetachedCriteria.forClass(BlacklistedCnicsModel.class);
        criteria.add(Restrictions.eq("cnicNo", cnicNo));
        List<BlacklistedCnicsModel> list = this.getHibernateTemplate().findByCriteria(criteria);
        if(list != null && list.size()!=0) {
            return list.get(0).getBlacklisted();
        }
        else
        {
            return Boolean.FALSE;
        }
    }
    
    //**********************************************************************************
    //					Method for saving more than 1000 records in IN-Clause
    //**********************************************************************************
    public static Criterion buildInCriterion(String propertyName, List<?> values) {    
    	Criterion criterion = null;
    	int listSize = values.size();
    	for (int i = 0; i < listSize; i += PARAMETER_LIMIT) {    	 
	    	List<?> subList;	    	 
	    	if (listSize > i + PARAMETER_LIMIT) {	    	
	    		subList = values.subList(i, (i + PARAMETER_LIMIT));
	    	}else{
	    		subList = values.subList(i, listSize);
	    	}
	    	if (criterion != null) {
	    		criterion = Restrictions.or(criterion, Restrictions.in(propertyName, subList));	    		
	    	}else{
	    		criterion = Restrictions.in(propertyName, subList);  
	    	}
    	}
    	 return criterion;
    }    	
	//*********************************************************************************
}

package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.commissionmodule.CommissionShSharesViewModel;
import com.inov8.microbank.server.dao.commissionmodule.CommissionShSharesViewDAO;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : May 28, 2013 12:47:34 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CommissionShSharesViewHibernateDAO extends BaseHibernateDAO<CommissionShSharesViewModel, Long, CommissionShSharesViewDAO> implements CommissionShSharesViewDAO
{
    /**
     * @author Naseer Ullah
     * <p>Unlike super implementation, this method does not throw exception if no matching record is found </p>
     * @param primaryKey : The primaryKey column value of record to be fetched
     * @return {@link CommissionShSharesViewModel} or null if there is no record in DB e.g. in case of Agent Transfer
     */
    @Override
    public CommissionShSharesViewModel findByPrimaryKey( Long primaryKey ) throws DataAccessException
    {
        return (CommissionShSharesViewModel)getHibernateTemplate().get( getPersistentClass(), primaryKey );
    }

}

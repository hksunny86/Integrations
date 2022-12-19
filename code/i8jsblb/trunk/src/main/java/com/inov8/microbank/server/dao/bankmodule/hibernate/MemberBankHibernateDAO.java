package com.inov8.microbank.server.dao.bankmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.bankmodule.MemberBankModel;
import com.inov8.microbank.server.dao.bankmodule.MemberBankDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import java.util.List;

public class MemberBankHibernateDAO extends BaseHibernateDAO<MemberBankModel,Long,MemberBankDAO>
    implements MemberBankDAO {
    @Override
    public List<MemberBankModel> findAllBanks() {
        List<MemberBankModel> memberBankModels = null;
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( MemberBankModel.class )
                .addOrder(Order.asc("bankName"));

        memberBankModels = getHibernateTemplate().findByCriteria( detachedCriteria );

        return memberBankModels;
    }
}

package com.inov8.microbank.ivr.dao;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.IVRMpinVerificationViewModel;

// @Created On 1/27/2023 : Friday
// @Created By muhammad.aqeel
public class IVRMpinVerificationHibernateDAO extends BaseHibernateDAO<IVRMpinVerificationViewModel, Long, IVRMpinVerificationDAO> implements IVRMpinVerificationDAO {
    public IVRMpinVerificationHibernateDAO() {
    }
}

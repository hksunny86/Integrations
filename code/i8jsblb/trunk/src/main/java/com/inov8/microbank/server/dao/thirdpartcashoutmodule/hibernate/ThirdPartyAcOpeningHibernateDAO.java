package com.inov8.microbank.server.dao.thirdpartcashoutmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ThirdPartyAccountOpeningModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.ThirdPartyAcOpeningDAO;
import oracle.sql.DATE;

import java.sql.Timestamp;
import java.util.Date;

public class ThirdPartyAcOpeningHibernateDAO extends BaseHibernateDAO<ThirdPartyAccountOpeningModel,Long,ThirdPartyAcOpeningDAO>
    implements ThirdPartyAcOpeningDAO {
    @Override
    public ThirdPartyAccountOpeningModel saveOrUpdateThirdPartyAcOpeningRequest(WorkFlowWrapper wrapper) throws FrameworkCheckedException {
        ThirdPartyAccountOpeningModel model = wrapper.getThirdPartyAccountOpeningModel();
        if(model == null)
        {
            model = new ThirdPartyAccountOpeningModel();
            model.setAgentId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
            if(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                model.setCustomerMobileNumber((String)wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE));
            if(wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                model.setCustomerNic((String) wrapper.getObject(CommandFieldConstants.KEY_CNIC));
            model.setCreatedOn(new Date());
            model.setProductId(wrapper.getProductModel().getProductId());
            model.setStatus(Boolean.FALSE);
            if(wrapper.getObject(CommandFieldConstants.KEY_DEBIT_CARD_NO) != null)
            {
                model.setDebitCardNumber((String)wrapper.getObject(CommandFieldConstants.KEY_DEBIT_CARD_NO));
                wrapper.putObject(CommandFieldConstants.KEY_DEBIT_CARD_NO, EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0",model.getDebitCardNumber()));
            }
        }
        else{
            model.setUpdatedOn(new Timestamp(new Date().getTime()));
        }
        getHibernateTemplate().saveOrUpdate(model);
        wrapper.setThirdPartyAccountOpeningModel(model);
        return model;
    }
}

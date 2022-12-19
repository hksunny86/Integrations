package com.inov8.microbank.server.dao.thirdpartcashoutmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BOPCardIssuanceModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.BOPCardIssuanceDAO;

import java.util.Date;

public class BOPCardIssuanceHibernateDAO extends BaseHibernateDAO<BOPCardIssuanceModel,Long,BOPCardIssuanceDAO>
        implements BOPCardIssuanceDAO {
    @Override
    public BOPCardIssuanceModel saveOrUpdateBOPCardIssuanceRequest(WorkFlowWrapper wrapper) throws FrameworkCheckedException {
        BOPCardIssuanceModel model = wrapper.getBOPCardIssuanceModel();
        if(model == null)
        {
            model = new BOPCardIssuanceModel();
            model.setAgentId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
            if(wrapper.getObject(CommandFieldConstants.KEY_LATITUDE) != null){
                model.setLatitude((String) wrapper.getObject(CommandFieldConstants.KEY_LATITUDE));
            }
            if(wrapper.getObject(CommandFieldConstants.KEY_LONGITUDE) != null){
                model.setLongitude((String) wrapper.getObject(CommandFieldConstants.KEY_LONGITUDE));
            }
            if(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                model.setCustomerMobileNumber((String)wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE));
            if(wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                model.setCustomerNic((String) wrapper.getObject(CommandFieldConstants.KEY_CNIC));
            model.setCreatedOn(new Date());
            model.setUpdatedOn(new Date());
            model.setIssuanceDate(new Date());
            if(wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE) != null){
                model.setSegmentId(String.valueOf(wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE)));
            }
            if(wrapper.getObject(CommandFieldConstants.KEY_DEBIT_CARD_NO) != null)
            {
                model.setDebitCardNumber((String)wrapper.getObject(CommandFieldConstants.KEY_DEBIT_CARD_NO));
            }
        }
        else{
            model.setUpdatedOn(new Date());
        }
        getHibernateTemplate().saveOrUpdate(model);
        wrapper.setBOPCardIssuanceModel(model);
        return model;
    }
}

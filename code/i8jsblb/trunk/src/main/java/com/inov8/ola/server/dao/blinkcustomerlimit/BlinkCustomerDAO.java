package com.inov8.ola.server.dao.blinkcustomerlimit;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.BlinkCustomerLimitModel;

import javax.faces.view.facelets.FaceletException;
import java.util.List;

public interface BlinkCustomerDAO extends BaseDAO<BlinkCustomerLimitModel, Long> {

    public BlinkCustomerLimitModel getLimitsByCustomerAccountType(Long customerAccountTypeId,Long accountId,Long transactionType,Long limitType)throws FrameworkCheckedException;
    public void insertData(BlinkCustomerLimitModel blinkCustomerLimitModel) throws FrameworkCheckedException;
}

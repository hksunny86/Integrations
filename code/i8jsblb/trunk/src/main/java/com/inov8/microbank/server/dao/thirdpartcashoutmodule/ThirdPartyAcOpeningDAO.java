package com.inov8.microbank.server.dao.thirdpartcashoutmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ThirdPartyAccountOpeningModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

public interface ThirdPartyAcOpeningDAO extends BaseDAO<ThirdPartyAccountOpeningModel,Long> {

    ThirdPartyAccountOpeningModel saveOrUpdateThirdPartyAcOpeningRequest(WorkFlowWrapper wrapper) throws FrameworkCheckedException;
}

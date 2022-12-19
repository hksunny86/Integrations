package com.inov8.microbank.server.dao.thirdpartcashoutmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BOPCardIssuanceModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

public interface BOPCardIssuanceDAO extends BaseDAO<BOPCardIssuanceModel,Long> {
    BOPCardIssuanceModel saveOrUpdateBOPCardIssuanceRequest(WorkFlowWrapper wrapper) throws FrameworkCheckedException;

}

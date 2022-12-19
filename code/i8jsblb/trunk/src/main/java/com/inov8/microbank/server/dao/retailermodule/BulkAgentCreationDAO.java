package com.inov8.microbank.server.dao.retailermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BulkAgentDataHolderModel;

import java.util.List;

public interface BulkAgentCreationDAO extends BaseDAO<BulkAgentDataHolderModel,Long> {

    List<BulkAgentDataHolderModel> getDataForAgentCreation(Long isProcessByScheduler) throws FrameworkCheckedException;
}

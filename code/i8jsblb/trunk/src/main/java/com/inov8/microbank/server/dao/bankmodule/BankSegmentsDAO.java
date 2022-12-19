package com.inov8.microbank.server.dao.bankmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BankSegmentsModel;

import java.util.List;
import java.util.Map;

public interface BankSegmentsDAO extends BaseDAO<BankSegmentsModel,Long> {
    List<Map<String, Object>> notAllowedSegments(String imd) throws FrameworkCheckedException;

}

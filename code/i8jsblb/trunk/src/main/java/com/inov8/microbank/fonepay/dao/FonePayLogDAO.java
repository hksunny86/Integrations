package com.inov8.microbank.fonepay.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.fonepay.common.FonePayLogModel;

public interface FonePayLogDAO extends BaseDAO<FonePayLogModel, Long> {

    Boolean validateApiGeeRRN(WebServiceVO webServiceVO) throws FrameworkCheckedException;
}

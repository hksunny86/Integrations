package com.inov8.microbank.server.dao.securitymodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.fonepay.model.EcofinSubAgentModel;

import java.util.List;


public interface EcofinSubAgentDAO extends BaseDAO<EcofinSubAgentModel, Long> {

    public EcofinSubAgentModel loadEcofinSubAgentbyId(Long ecofinSubAgentId);

}

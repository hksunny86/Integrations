package com.inov8.microbank.server.dao.portal.partnergroupmodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.PartnerGroupModel;


public interface PartnerGroupDAO 

extends BaseDAO<PartnerGroupModel, Long> {
	List getPartnerGroups( Long partnerId,Boolean admin) ;
	List<PartnerGroupModel> findPartnerGroupsByPartnerId(Long partnerId);
	boolean isNameUnique(PartnerGroupModel partnerGroupModel);
}

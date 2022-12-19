package com.inov8.microbank.server.dao.portal.concernmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ConcernPartnerModel;

public interface ConcernPartnerDAO extends BaseDAO<ConcernPartnerModel, Long>{
	List findAssociatedPartnerPartners(Long partnerId);
//	List findAssociatedOPPartners(Long partnerId,String concernCode);
	public List findAllOtherPartners(Long partnerId);
	public List findAllPartnersIncludingMyself();
	public List<ConcernPartnerModel> findConcernPartnerByRetailerId(long retailerId) throws FrameworkCheckedException;
}

package com.inov8.microbank.server.dao.portal.kycmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ACOwnershipDetailModel;

/**
 * @author Abu Turab
 *
 */
public interface ACOwnerShipDAO  extends BaseDAO<ACOwnershipDetailModel, Long>{

	public List<ACOwnershipDetailModel> loadAccountOwnerShipsByCustomerId(ACOwnershipDetailModel ownerShipDetailModel) throws FrameworkCheckedException;

	List<ACOwnershipDetailModel> loadAccountOwnerShipsByRetailerContactId(ACOwnershipDetailModel ownerShipDetailModel)
			throws FrameworkCheckedException;
}

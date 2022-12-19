package com.inov8.microbank.server.dao.retailermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public interface RetailerContactDAO
    extends BaseDAO<RetailerContactModel, Long>
{
	public List<RetailerContactModel> findRetailerContactByDistributorLevelId(long distributorLevelId, long retailerId) throws FrameworkCheckedException;
	public List<RetailerContactModel> findHeadAgents(long distributorLevelId, long retailerId) throws FrameworkCheckedException;
	public List<RetailerContactModel> findChildRetailerContactsById(long retailerContactId) throws FrameworkCheckedException;
	public boolean isHeadAgent(String mobileNo);
	public boolean checkAgentExistsForDistributor(Long distributorId) throws FrameworkCheckedException;
	boolean isKinIdDocumentNumberAlreadyExist(String initialAppFormNumber, Long idDocumentType, String idDocumentNumber)
			throws FrameworkCheckedException;

	RetailerModel getRetailerModelByRetailerId(Long retailerId) throws FrameworkCheckedException;
	DistributorModel findDistributorModelById(Long distributorId) throws FrameworkCheckedException;
}

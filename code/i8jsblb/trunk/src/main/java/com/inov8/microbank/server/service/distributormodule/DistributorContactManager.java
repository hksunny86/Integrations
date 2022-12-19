package com.inov8.microbank.server.service.distributormodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DistributorContactModel;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public interface DistributorContactManager
{
	public SearchBaseWrapper loadDistributorContact(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	public BaseWrapper loadDistributorContact(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public SearchBaseWrapper searchDistributorContact(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	public BaseWrapper createDistributorContact(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public BaseWrapper updateDistributorContact(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	//  public BaseWrapper createDistributorCreditSummary(BaseWrapper baseWrapper)
	//      throws FrameworkCheckedException;

	public boolean findDistributorCreditByDistributorContactId(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public BaseWrapper createAppUserForDistributorContact(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public DistributorContactModel findDistributorNationalManagerContact(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	public DistributorContactModel findDistributorContactByMobileNumber(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	public BaseWrapper loadDistributorContactByAppUser(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public boolean isManagingContact(Long fromDistributor, Long toDistributor) throws FrameworkCheckedException;

	public BaseWrapper updateDistributorContactModel(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public boolean isDistributorContactHead(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	public boolean isDistributorContactActive(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	public boolean isDistributorContact(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	public boolean isDistributorActive(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	public Long getAppUserPartnerGroupId(Long appUserId) throws FrameworkCheckedException;

	public BaseWrapper getNationalDistributor() throws FrameworkCheckedException;

}

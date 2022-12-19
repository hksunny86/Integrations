package com.inov8.microbank.server.dao.portal.allpaymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AllpayDeReLinkListViewModel;

public interface AllpayDelinkRelinkPaymentModeViewDAO extends BaseDAO<AllpayDeReLinkListViewModel, Long>{

	boolean isCoreAccountLinkedToOtherAgent(String accountNumber, Long retailerContact) throws FrameworkCheckedException;

}

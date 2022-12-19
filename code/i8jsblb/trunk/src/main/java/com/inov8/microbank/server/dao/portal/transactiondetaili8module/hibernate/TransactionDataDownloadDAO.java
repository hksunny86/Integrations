package com.inov8.microbank.server.dao.portal.transactiondetaili8module.hibernate;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface TransactionDataDownloadDAO {

	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.portal.transactiondetaili8module.TransactionDetailPortalListViewDAO#createZipFile(com.inov8.framework.common.wrapper.SearchBaseWrapper)
	 */
	public abstract String createZipFile(SearchBaseWrapper searchBaseWrapper)
			throws Exception;

}
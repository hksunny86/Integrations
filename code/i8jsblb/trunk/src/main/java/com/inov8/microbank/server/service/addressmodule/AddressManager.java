package com.inov8.microbank.server.service.addressmodule;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;


public interface AddressManager {
	/**
	 * @author AtifHu
	 */
	public SearchBaseWrapper getPostalOfficesByCity(SearchBaseWrapper searchBaseWrapper);
}

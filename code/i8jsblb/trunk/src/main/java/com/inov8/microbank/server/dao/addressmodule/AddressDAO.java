package com.inov8.microbank.server.dao.addressmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AddressModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: </p>
 *
 * @author Usman Ashraf
 * @version 1.0
 */



public interface AddressDAO
    extends BaseDAO<AddressModel, Long>
{
	public AddressModel getAddress(Long addressId);
	public AddressModel findAddressById(long addressId) throws FrameworkCheckedException;
}

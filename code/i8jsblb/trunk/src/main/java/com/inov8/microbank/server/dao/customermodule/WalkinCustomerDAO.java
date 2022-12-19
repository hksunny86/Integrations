package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.WalkinCustomerModel;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Kashif Bashir
 * @version 1.0
 *
 */

public interface WalkinCustomerDAO extends BaseDAO<WalkinCustomerModel, Long> {
	
	WalkinCustomerModel getWalkinCustomerByCNIC(String cnic);
	WalkinCustomerModel getWalkinCustomerByMobile(String mobileNumber);
	WalkinCustomerModel getWalkinCustomerByPair(String mobile, String cnic);
}

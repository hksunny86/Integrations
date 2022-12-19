package com.inov8.microbank.server.service.accounttypemodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.PaymentModeModel;

public interface AccountTypeManager {
	
	SearchBaseWrapper loadAccountType(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
	
	BaseWrapper loadAccountType(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	
	public List<PaymentModeModel> searchPaymentModeModel(Long accountTypeId)throws
	  FrameworkCheckedException;

}

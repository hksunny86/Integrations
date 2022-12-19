package com.inov8.microbank.server.service.AllpayModule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface AllpayCommissionRatesManager
{

	SearchBaseWrapper loadCommissionRates(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	BaseWrapper loadCommissionRates(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	SearchBaseWrapper searchCommissionRates(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	BaseWrapper updateCommissionRates(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	BaseWrapper createCommissionRates(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	SearchBaseWrapper searchAllPayCommissionRate(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}

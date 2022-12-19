package com.inov8.microbank.server.service.financialintegrationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface FinancialIntegrationManager
{
	public AbstractFinancialInstitution loadFinancialInstitution(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public AbstractFinancialInstitution loadFinancialInstitutionByClassName(String className)throws FrameworkCheckedException;
}

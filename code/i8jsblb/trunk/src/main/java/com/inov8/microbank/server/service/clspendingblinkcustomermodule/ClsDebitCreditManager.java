package com.inov8.microbank.server.service.clspendingblinkcustomermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.ClsDebitCreditBlockModel;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;

import java.util.List;

public interface ClsDebitCreditManager {
    public List<ClsDebitCreditBlockModel> loadClsDebitCreditBlockModel (ClsDebitCreditBlockModel clsDebitCreditBlockModel) throws FrameworkCheckedException;

}

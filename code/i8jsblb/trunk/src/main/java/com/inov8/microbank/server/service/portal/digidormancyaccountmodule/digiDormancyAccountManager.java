package com.inov8.microbank.server.service.portal.digidormancyaccountmodule;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface digiDormancyAccountManager {
    SearchBaseWrapper searchDigiDormancyAccountsView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}

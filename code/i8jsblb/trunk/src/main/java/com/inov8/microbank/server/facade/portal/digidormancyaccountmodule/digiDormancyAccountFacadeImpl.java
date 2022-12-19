package com.inov8.microbank.server.facade.portal.digidormancyaccountmodule;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.digidormancyaccountmodule.digiDormancyAccountManager;

public class digiDormancyAccountFacadeImpl implements digiDormancyAccountFacade{

    private FrameworkExceptionTranslator frameworkExceptionTranslator;
    private digiDormancyAccountManager digiDormancyAccountManager;

    public SearchBaseWrapper searchDigiDormancyAccountsView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        try
        {
            return digiDormancyAccountManager.searchDigiDormancyAccountsView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator) {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

    public void setDigiDormancyAccountManager(com.inov8.microbank.server.service.portal.digidormancyaccountmodule.digiDormancyAccountManager digiDormancyAccountManager) {
        this.digiDormancyAccountManager = digiDormancyAccountManager;
    }
}

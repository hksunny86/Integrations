package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.esctoinov8.EscToInov8Manager;

public class EscToInov8FacadeImpl implements EscToInov8Facade {

    private EscToInov8Manager escToInov8Manager;
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    public SearchBaseWrapper searchEscToInov8(SearchBaseWrapper
                                              searchBaseWrapper) throws
            FrameworkCheckedException {

        try {
            this.escToInov8Manager.searchEscToInov8(searchBaseWrapper);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex,
                    this.frameworkExceptionTranslator.
                    FIND_BY_PRIMARY_KEY_ACTION);
        }
        return searchBaseWrapper;

    }

    public void setEscToInov8Manager(EscToInov8Manager
                                          escToInov8Manager) {
        this.escToInov8Manager = escToInov8Manager;
    }

    public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                                frameworkExceptionTranslator) {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }


}
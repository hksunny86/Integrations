package com.inov8.microbank.server.service.portal.esctoinov8;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface EscToInov8Manager {
    public SearchBaseWrapper searchEscToInov8(SearchBaseWrapper
            searchBaseWrapper) throws FrameworkCheckedException;
}

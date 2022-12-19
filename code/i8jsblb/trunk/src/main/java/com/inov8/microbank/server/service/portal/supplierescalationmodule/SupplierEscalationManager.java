package com.inov8.microbank.server.service.portal.supplierescalationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface SupplierEscalationManager {
    SearchBaseWrapper searchEscalateToInov8Product(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    SearchBaseWrapper searchEscalateToInov8Service(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}

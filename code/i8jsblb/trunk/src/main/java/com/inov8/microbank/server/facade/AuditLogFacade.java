package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureReasonManager;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface AuditLogFacade
    extends FailureLogManager, FailureReasonManager
{

}

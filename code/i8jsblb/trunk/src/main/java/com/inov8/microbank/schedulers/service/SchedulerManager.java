package com.inov8.microbank.schedulers.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface SchedulerManager {

    BaseWrapper createAgentAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;
}

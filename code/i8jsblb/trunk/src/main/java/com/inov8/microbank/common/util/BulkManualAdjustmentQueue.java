package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;

public interface BulkManualAdjustmentQueue {

	void push(IvrRequestDTO dto) throws FrameworkCheckedException;
}
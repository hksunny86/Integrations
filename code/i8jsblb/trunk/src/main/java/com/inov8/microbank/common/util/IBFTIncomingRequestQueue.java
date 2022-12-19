package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;

public interface IBFTIncomingRequestQueue {
	
	void sentIBFTRequest(MiddlewareAdviceVO messageVO) throws FrameworkCheckedException;
	
}

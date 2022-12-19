package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;

public interface CoreAdviceSender {

	void send(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException;
	void sendForAccOpening(MiddlewareAdviceVO middlewareAdviceVO, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;
}
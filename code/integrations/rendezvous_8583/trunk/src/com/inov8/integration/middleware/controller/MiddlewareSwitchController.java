package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.MiddlewareMessageVO;

public interface MiddlewareSwitchController {
	
	MiddlewareMessageVO billInquiry(MiddlewareMessageVO requestVO)throws RuntimeException;
	
	MiddlewareMessageVO billPayment(MiddlewareMessageVO requestVO)throws RuntimeException;
	
	MiddlewareMessageVO titleFetch(MiddlewareMessageVO requestVO)throws RuntimeException;
	
	MiddlewareMessageVO fundTransfer(MiddlewareMessageVO requestVO)throws RuntimeException;

	MiddlewareMessageVO fundTransferAdvice(MiddlewareMessageVO requestVO)throws RuntimeException;

	MiddlewareMessageVO acquirerReversalAdvice(MiddlewareMessageVO requestVO) throws RuntimeException;

	MiddlewareMessageVO accountBalanceInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

	MiddlewareMessageVO ibftTitleFetch(MiddlewareMessageVO requestVO) throws RuntimeException;

	MiddlewareMessageVO ibftAdvice(MiddlewareMessageVO requestVO) throws RuntimeException;


}

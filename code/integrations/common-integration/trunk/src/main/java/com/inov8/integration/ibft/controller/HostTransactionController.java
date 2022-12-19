package com.inov8.integration.ibft.controller;

import com.inov8.integration.vo.MiddlewareMessageVO;


public interface HostTransactionController {

    public MiddlewareMessageVO titleFetch(MiddlewareMessageVO middlewareMessageVO);

    public MiddlewareMessageVO creditAdvice(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    public MiddlewareMessageVO cashWithDrawal(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    public MiddlewareMessageVO cashWithDrawalReversal(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    public MiddlewareMessageVO posTransaction(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    public MiddlewareMessageVO posRefundTransaction(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    public MiddlewareMessageVO coreToWalletAdvice(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;
}
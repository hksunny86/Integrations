package com.inov8.microbank.server.service.jms;

import java.io.Serializable;

import com.inov8.framework.common.exception.FrameworkCheckedException;

public interface JmsProducer
{

	public abstract void produce(final Serializable obj, final String destinationName) throws
    FrameworkCheckedException;

}